package TestLayer;

import java.util.Arrays;
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

public class CRUD extends BaseClass{
	private Context context = new Context();
	
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
	    public void getCallUsingRestAssured() {

	        RestAssured.baseURI = prop.getProperty("baseURI");

	        RequestSpecification httpRequest = RestAssured.given();

	        httpRequest.header("Content-Type", "application/json");

	        Response response = httpRequest.get("/companies");

	        response.prettyPrint();

	        // Deserialize
	        RootResponse rootResponse = response.as(RootResponse.class);
	        context.setRootResponse(rootResponse);

	        // Status Assertions
	        Assert.assertEquals(response.getStatusCode(), 200);
	        Assert.assertTrue(response.getStatusLine().contains("200"));
	        Assert.assertTrue(response.getTime() < 3000);

	        // Content Type
	        Assert.assertEquals(response.getContentType(), "application/json");

	        // Company Assertions
	        Assert.assertEquals(context.getRootResponse().getCompanies().get(0).getName(), "Apple");
	        Assert.assertEquals(context.getRootResponse().getCompanies().get(1).getName(), "Samsung");
	        Assert.assertEquals(context.getRootResponse().getCompanies().get(0).getProducts().get(0), "iPhone");
	        Assert.assertEquals(context.getRootResponse().getCompanies().get(1).getHeadquarters(), "Seoul, South Korea");
	    }
}
