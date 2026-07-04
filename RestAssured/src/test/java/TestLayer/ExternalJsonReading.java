package TestLayer;

import java.io.File;
import java.io.IOException;

import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import BaseLayer.BaseClass;
import io.restassured.response.Response;

public class ExternalJsonReading {
	@Test
	public void readExternalJson_PostCall() throws IOException, JSONException {

		BaseClass.initRestAssured();
		BaseClass.addPathParams("student");

		String filePath = System.getProperty("user.dir") + "/src/main/java/TestDataLayer/ExternalJsonPayload.json";
		JsonNode root = new ObjectMapper().readTree(new File(filePath));
		String requestBody = root.get("updateCompany").toString();

		BaseClass.addRequestBody(requestBody);
		Response response = BaseClass.sendRequest("POST");
		response.prettyPrint();

		JSONAssert.assertEquals(requestBody, response.asString(), false);

	}
}
