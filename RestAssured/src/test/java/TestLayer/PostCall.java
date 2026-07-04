package TestLayer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import BaseLayer.BaseClass;
import PojoLayer.Companies;
import PojoLayer.CompanyDetails;
import PojoLayer.Context;
import PojoLayer.RootResponse;
import UtilsLayer.UtilsApi;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class PostCall extends BaseClass {

	private Context context = new Context();

	@Test
	public void postCallUsingMap() {
		RestAssured.baseURI = prop.getProperty("baseURI");
		RequestSpecification httpRequest = RestAssured.given();

		httpRequest.header("Content-Type", "application/json");

		// ----------- Request body using Map interface class -----------
		Map<String, String> map = new LinkedHashMap<>();
		map.put("firstName", "Sudip");
		map.put("company", "LTM");

		httpRequest.body(map);

		Response response = httpRequest.post("/student");
		response.prettyPrint();

		Assert.assertEquals(response.getStatusCode(), 201);
		Assert.assertTrue(response.getStatusLine().contains("201"));
		Assert.assertTrue(response.getTime() < 3000);

		Assert.assertEquals(response.jsonPath().getString("firstName"), "Sudip");
		Assert.assertEquals(response.jsonPath().getString("company"), "LTM");
	}

	@Test
	public void postCallUsingJSONObject() throws JSONException {

		RestAssured.baseURI = prop.getProperty("baseURI");
		RequestSpecification httpRequest = RestAssured.given();
		httpRequest.header("Content-Type", "application/json");

		JSONObject json = new JSONObject();
		json.put("firstName", "Sudip");
		json.put("company", "LTM");
		httpRequest.body(json.toString());

		Response response = httpRequest.post("/student");
		response.prettyPrint();

		// Status Assertions
		Assert.assertEquals(response.getStatusCode(), 201);
		Assert.assertTrue(response.getStatusLine().contains("201"));
		Assert.assertTrue(response.getTime() < 3000);

		// Content Type
		Assert.assertEquals(response.getContentType(), "application/json");

		// JSON Assertions
		Assert.assertEquals(response.jsonPath().getString("firstName"), "Sudip");
		Assert.assertEquals(response.jsonPath().getString("company"), "LTM");
	}

	@Test
	public void postCall() throws JSONException {
		RestAssured.baseURI = prop.getProperty("baseURI");
		RequestSpecification httpRequest = RestAssured.given();

		httpRequest.header("Content-Type", "application/json");
		// httpRequest.header("Trust-Token", "HSBC-Trust-tocken");

		// ----------- Request body using Map interface class -----------
		Map<String, String> map = new LinkedHashMap<>();
		map.put("firstName", "Sudip");
		map.put("company", "LTM");

		// ----------- Request body using JSONObject class -----------
		JSONObject json = new JSONObject();
		json.put("firstName", "Sudip");
		json.put("company", "LTM");

		// ----------- Request body using POJO class -----------
		CompanyDetails apple = new CompanyDetails();
		apple.setName("Apple");
		apple.setProducts(Arrays.asList("iPhone", "iPad", "MacBook"));
		apple.setHeadquarters("Cupertino, CA");
		apple.setCountry("USA");

		CompanyDetails samsung = new CompanyDetails();
		samsung.setName("Samsung");
		samsung.setProducts(Arrays.asList("Galaxy S23", "Galaxy Tab"));
		samsung.setHeadquarters("Seoul, South Korea");
		samsung.setCountry("Korea");

		Companies requestPayload = new Companies();
		requestPayload.setCompanies(Arrays.asList(apple, samsung));

		String requestBody = UtilsApi.serialization(requestPayload);
		httpRequest.body(requestBody);

		Response response = httpRequest.post("/companies");
		response.prettyPrint();

		// Deserialize
		RootResponse rootResponse = response.as(RootResponse.class);

		context.setRootResponse(rootResponse);

		// Status Code Assertion
		Assert.assertEquals(response.getStatusCode(), 201);

		// Company Assertions
		Assert.assertEquals(context.getRootResponse().getCompanies().get(0).getName(), "Apple");

		Assert.assertEquals(context.getRootResponse().getCompanies().get(1).getName(), "Samsung");

		Assert.assertEquals(context.getRootResponse().getCompanies().get(0).getProducts().get(0), "iPhone");

		Assert.assertEquals(context.getRootResponse().getCompanies().get(1).getHeadquarters(), "Seoul, South Korea");

	}
	
	@Test
	public void postCallHSBCFramework() {

		    // Initialize Rest Assured
		    BaseClass.initRestAssured();

		    // Headers
		    Map<String, String> headers = new HashMap<>();
		    headers.put("Content-Type", "application/json");
		    BaseClass.addHeaders(headers);

		    // Request Body
		    CompanyDetails apple = new CompanyDetails();
		    apple.setName("Apple");
		    apple.setProducts(Arrays.asList("iPhone", "iPad", "MacBook"));
		    apple.setHeadquarters("Cupertino, CA");
		    apple.setCountry("USA");

		    CompanyDetails samsung = new CompanyDetails();
		    samsung.setName("Samsung");
		    samsung.setProducts(Arrays.asList("Galaxy S23", "Galaxy Tab"));
		    samsung.setHeadquarters("Seoul, South Korea");
		    samsung.setCountry("Korea");

		    Companies request = new Companies();
		    request.setCompanies(Arrays.asList(apple, samsung));

		    BaseClass.addRequestBody(request);

		    // Endpoint
		    BaseClass.addPathParams("/companies");

		    // Send Request
		    Response response = BaseClass.sendRequest("POST");

		    // Deserialize
		    RootResponse rootResponse = response.as(RootResponse.class);

		    // Assertions
		    Assert.assertEquals(response.getStatusCode(), 201);
		    Assert.assertEquals(rootResponse.getCompanies().get(0).getName(), "Apple");
		    Assert.assertEquals(rootResponse.getCompanies().get(1).getName(), "Samsung");
		    Assert.assertEquals(rootResponse.getCompanies().get(0).getProducts().get(0), "iPhone");
		    Assert.assertEquals(rootResponse.getCompanies().get(1).getHeadquarters(), "Seoul, South Korea");

		    BaseClass.clear();
		}
}
