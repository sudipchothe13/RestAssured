package TestLayer;

import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import BaseLayer.BaseClass;
import PojoLayer.Context;
import PojoLayer.RootResponse;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class GetCall extends BaseClass {

    private Context context = new Context();

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