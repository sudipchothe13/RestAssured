package UtilsLayer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import org.testng.Assert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import BaseLayer.BaseClass;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class UtilsApi extends BaseClass {
	protected static RequestSpecification httpRequest;
	protected static RequestSpecification req;
	protected static String requestLog;
	protected static String responseLog;
	protected static Response response;

	public static String serialization(Object obj) {
		ObjectMapper mapper = new ObjectMapper();
		String json = null;
		try {
			json = mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {

			e.printStackTrace();
		}
		return json;
	}

	public static String captureValueFromRespBody(Response response, String key) {

		return response.getBody().jsonPath().getString(key);
	}

	// Method to check if the status code is as expected
	public static void assertStatusCode(Response response, int expectedStatusCode) {
		Assert.assertEquals(response.getStatusCode(), expectedStatusCode, "Status code validation failed");
	}

	// Method to check if the status line contains a specific string
	public static void assertStatusLineContains(Response response, String expectedString) {
		Assert.assertTrue(response.getStatusLine().contains(expectedString),
				"Status line validation failed. Expected: '" + expectedString + "', Actual: '"
						+ response.getStatusLine() + "'");
	}

	// Method to check if the response time is within a specified limit
	public static void assertRespTimeIsLess(Response response, long maxResponseTime) {
		Assert.assertTrue(response.getTime() < maxResponseTime,
				"Response time validation failed. Expected response time less than " + maxResponseTime + " ms, Actual: "
						+ response.getTime() + " ms");
	}

	// Method to check if the response body contains the specified string
	public static void assertResponseBody(Response response, String actualValue, String expectedValue) {

		Assert.assertEquals(response.jsonPath().getString(actualValue), expectedValue);

//		        Assert.assertTrue(response.getBody().asString().contains(expectedString),
//		                "Response body validation failed. Expected to contain: '" + expectedString + "', Actual: " + response.getBody().asString());
	}

	// Method to check if the 'Content-Type' header is as expected
	public static void assertContentTypeHeader(Response response, String expectedContentType) {
		Assert.assertEquals(response.header("Content-Type"), expectedContentType,
				"Content-Type header validation failed. Expected: '" + expectedContentType + "', Actual: '"
						+ response.header("Content-Type") + "'");
	}

	public static void assertHeader(Response response, String headerName, String expectedHeaderValue) {

		String actualHeaderValue = response.getHeader(headerName);
		Assert.assertEquals(actualHeaderValue, expectedHeaderValue,
				"Header '" + headerName + "' does not match the expected value.");
	}

	public void assertCookie(Response response, String expectedKey, String expectedValue) {

		Map<String, String> cookies = response.getCookies();

		// Check if the expected key exists in the cookies map
		if (cookies.containsKey(expectedKey)) {
			// Get the actual value of the cookie
			String actualValue = cookies.get(expectedKey);

			// Assert that the actual value matches the expected value
			Assert.assertEquals(actualValue, expectedValue);
		} else {

			Assert.fail("Cookie with key '" + expectedKey + "' not found in the response.");
		}
	}

	public void assertImage(String imagePath) throws IOException {

		File originalImageFile = new File(imagePath);

		byte[] responseImageBytes = response.asByteArray();
		byte[] originalImageBytes = Files.readAllBytes(Paths.get(originalImageFile.toURI()));

		Assert.assertEquals(responseImageBytes, originalImageBytes,
				"The original image and the response image are not the same.");
	}

	/*
	 * public static String getPayload(String filePath, String payloadName) {
	 * 
	 * try {
	 * 
	 * String json = Files.readString(Paths.get(filePath));
	 * 
	 * ObjectMapper mapper = new ObjectMapper();
	 * 
	 * JsonNode root = mapper.readTree(json);
	 * 
	 * JsonNode payload = root.get(payloadName);
	 * 
	 * if (payload == null) {
	 * 
	 * throw new RuntimeException( "Payload not found : " + payloadName); }
	 * 
	 * return mapper.writerWithDefaultPrettyPrinter() .writeValueAsString(payload);
	 * 
	 * } catch (Exception e) {
	 * 
	 * throw new RuntimeException( "Unable to fetch payload : " + payloadName, e); }
	 * }
	 * 
	 */

	public static String getPayload(String filePath, String payloadName) throws IOException {

		JsonNode root = new ObjectMapper().readTree(new File(filePath));

		return root.get(payloadName).toString();
	}
}
