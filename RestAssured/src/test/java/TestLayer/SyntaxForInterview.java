package TestLayer;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import BaseLayer.BaseClass;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class SyntaxForInterview {

	@Test
	public void postCall() throws JSONException {
		// ----------Given-------------
		RestAssured.baseURI = "http://localhost:9090";
		RequestSpecification httpRequest = RestAssured.given();
		httpRequest.header("Content-Type", "application/json");

		// ----------When--------------

		List<String> subjectsList = new LinkedList<>();
		subjectsList.add("Maths");
		subjectsList.add("Science");
		subjectsList.add("English");

		Map<String, String> address = new HashMap<>();
		address.put("city", "Pune");
		address.put("state", "Maharashtra");
		address.put("country", "India");

		Map<String, Object> json = new HashMap<>();

		json.put("firstName", "Rohan");
		json.put("lastName", "Kambuj");
		json.put("subjects", subjectsList);
		json.put("address", address);

		httpRequest.body(json);
		Response response = httpRequest.post("/student");
		response.prettyPrint();

		// ----------Then--------------
		Assert.assertEquals(response.statusCode(), 201);
		Assert.assertTrue(response.statusLine().contains("201"));
		Assert.assertTrue(response.getTime() < 3000);

		Assert.assertEquals(response.jsonPath().getString("firstName"), "Rohan");
		Assert.assertEquals(response.jsonPath().getString("lastName"), "Kambuj");

		Assert.assertEquals(response.jsonPath().getString("address.city"), "Pune");
		Assert.assertEquals(response.jsonPath().getString("subjects[0]"), "Maths");

	}

	@Test
	public void getCall() {
		// ----------Given-------------
		RestAssured.baseURI = "http://localhost:9090/";
		RequestSpecification httpRequest = RestAssured.given();
		httpRequest.header("Content-Type", "application/json");
//		httpRequest.pathParam("requestType", "/student");
//		httpRequest.queryParam("fromDate", "05-05-2026");
//		httpRequest.queryParam("toDate", "30-05-2026");

		// ----------When--------------
		Response response = httpRequest.get("/student");
		response.prettyPrint();

		// ----------Then--------------
		Assert.assertEquals(response.statusCode(), 200);
		System.out.println(response.statusLine());
		Assert.assertTrue(response.statusLine().contains("200"));
		Assert.assertTrue(response.getTime() < 3000);

		Assert.assertEquals(response.jsonPath().getString("firstName"), "Rohan");
		Assert.assertEquals(response.jsonPath().getString("lastName"), "Kambuj");
		Assert.assertEquals(response.jsonPath().getString("address.city"), "Pune");
		Assert.assertEquals(response.jsonPath().getString("subjects[0]"), "Maths");
	}

	@Test
	public void readExternalJson_PostCall() throws IOException, JSONException {
		// ----------Given-------------
		RestAssured.baseURI = "http://localhost:9090/";
		RequestSpecification httpRequest = RestAssured.given();
		httpRequest.header("Content-Type", "application/json");
		
		String filePath = System.getProperty("user.dir") + "/src/main/java/TestDataLayer/ExternalJsonPayload.json";
		JsonNode root = new ObjectMapper().readTree(new File(filePath));
		String requestBody = root.get("updateCompany").toString();

		httpRequest.body(requestBody);
		Response response = httpRequest.post("/student");
		response.prettyPrint();

		JSONAssert.assertEquals(requestBody, response.asString(), false);
	}
	
	public void wireMockServer() {
		
	}
}
