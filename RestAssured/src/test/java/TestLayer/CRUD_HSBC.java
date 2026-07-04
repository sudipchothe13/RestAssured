package TestLayer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import BaseLayer.BaseClass;
import PojoLayer.Companies;
import PojoLayer.CompanyDetails;
import PojoLayer.RootResponse;
import io.restassured.response.Response;

public class CRUD_HSBC {
	
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
	
	  @Test
	    public void getCallHSBCFramework() {

	        // Initialize Rest Assured
	        BaseClass.initRestAssured();

	        // Headers
	        Map<String, String> headers = new HashMap<>();
	        headers.put("Content-Type", "application/json");
	        BaseClass.addHeaders(headers);

	        // Endpoint
	        BaseClass.addPathParams("/companies");

	        // Send Request
	        Response response = BaseClass.sendRequest("GET");

	        response.prettyPrint();

	        // Deserialize
	        RootResponse rootResponse = response.as(RootResponse.class);

	        // Assertions
	        Assert.assertEquals(response.getStatusCode(), 200);
	        Assert.assertTrue(response.getStatusLine().contains("200"));
	        Assert.assertTrue(response.getTime() < 3000);
	        Assert.assertEquals(response.getContentType(), "application/json");

	        Assert.assertEquals(rootResponse.getCompanies().get(0).getName(), "Apple");
	        Assert.assertEquals(rootResponse.getCompanies().get(1).getName(), "Samsung");
	        Assert.assertEquals(rootResponse.getCompanies().get(0).getProducts().get(0), "iPhone");
	        Assert.assertEquals(rootResponse.getCompanies().get(1).getHeadquarters(), "Seoul, South Korea");

	        BaseClass.clear();
	    }

}
