package TestLayer;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.github.tomakehurst.wiremock.WireMockServer;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class RestWireMockServerPractise {

	WireMockServer server;
	String payload;

	@BeforeMethod
	public void setup() {

		server = new WireMockServer(3000);
		server.start();

		payload = "{\n" + "  \"id\": 101,\n" + "  \"firstName\": \"Sudip\",\n" + "  \"lastName\": \"Chothe\",\n"
				+ "  \"email\": \"sudip.chothe@gmail.com\",\n" + "  \"mobile\": \"98765843210\",\n"
				+ "  \"department\": \"QA Automation\",\n" + "  \"salary\": 85000,\n" + "  \"city\": \"Mumbai\",\n"
				+ "  \"status\": \"Employee Created Successfully\",\n" + "  \"createdAt\": \"2026-05-19T10:45:30\"\n"
				+ "}";

		server.stubFor(post(urlEqualTo("/employees")).willReturn(
				aResponse().withStatus(201).withHeader("Content-Type", "application/json").withBody(payload)));

		// GET
		server.stubFor(get(urlEqualTo("/employees/3030")).willReturn(
				aResponse().withStatus(200).withHeader("Content-Type", "application/json").withBody(payload)));
	}

	@AfterMethod
	public void tearDown() {
		server.stop();
	}

	@Test(groups = "Regression")
	public void postCallUsingWireMockServer() {

		RestAssured.baseURI = "http://localhost:3000";
		RequestSpecification httpRequest = RestAssured.given();
		httpRequest.header("Content-Type", "application/json");

		// ----payload passed via WireMockServer-------
		httpRequest.body(payload);

		Response response = httpRequest.post("/employees");

		response.prettyPrint();

		Assert.assertEquals(response.getStatusCode(), 201);
		Assert.assertTrue(response.getStatusLine().contains("201"));
		Assert.assertTrue(response.getTime() < 3000);
		Assert.assertEquals(response.getContentType(), "application/json");

		Assert.assertEquals(response.jsonPath().getInt("id"), 101);
		Assert.assertEquals(response.jsonPath().getString("firstName"), "Sudip");
		Assert.assertEquals(response.jsonPath().getString("lastName"), "Chothe");
		Assert.assertEquals(response.jsonPath().getString("email"), "sudip.chothe@gmail.com");
		Assert.assertEquals(response.jsonPath().getString("mobile"), "98765843210");
		Assert.assertEquals(response.jsonPath().getString("department"), "QA Automation");
		Assert.assertEquals(response.jsonPath().getInt("salary"), 85000);
		Assert.assertEquals(response.jsonPath().getString("city"), "Mumbai");
		Assert.assertEquals(response.jsonPath().getString("status"), "Employee Created Successfully");
	}

	@Test(groups = "Regression")
	public void getCallUsingWireMockServer() {

		RestAssured.baseURI = "http://localhost:3000";

		RequestSpecification httpRequest = RestAssured.given();

		httpRequest.header("Content-Type", "application/json");

		Response response = httpRequest.get("/employees/3030");

		response.prettyPrint();

		Assert.assertEquals(response.getStatusCode(), 200);
		Assert.assertTrue(response.getStatusLine().contains("200"));
		Assert.assertTrue(response.getTime() < 3000);
		Assert.assertEquals(response.getContentType(), "application/json");

		Assert.assertEquals(response.jsonPath().getInt("id"), 101);
		Assert.assertEquals(response.jsonPath().getString("firstName"), "Sudip");
		Assert.assertEquals(response.jsonPath().getString("lastName"), "Chothe");
		Assert.assertEquals(response.jsonPath().getString("email"), "sudip.chothe@gmail.com");
		Assert.assertEquals(response.jsonPath().getString("mobile"), "9876543210");
		Assert.assertEquals(response.jsonPath().getString("department"), "QA Automation");
		Assert.assertEquals(response.jsonPath().getInt("salary"), 85000);
		Assert.assertEquals(response.jsonPath().getString("city"), "Mumbai");
		Assert.assertEquals(response.jsonPath().getString("status"), "Employee Created Successfully");
	}
}