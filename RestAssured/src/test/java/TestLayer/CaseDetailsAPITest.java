package TestLayer;

import org.testng.Assert;
import org.testng.annotations.Test;

import BaseLayer.BaseClass;
import io.restassured.response.Response;

public class CaseDetailsAPITest {

	@Test
	public void getCaseDetails() {
		BaseClass.initRestAssured();
		BaseClass.addPathParams("case_details/search");
		BaseClass.addQueryParams("partyIdentifier", "MYHBMB2024018");
		BaseClass.sendRequest("GET");

		Response response = BaseClass.getResponse();

		// Assertions
		Assert.assertEquals(response.getStatusCode(), 200, "Status code is not 200");
		Assert.assertTrue(response.getTime() < 500, "Response took too long"); // response time < 5s

	}

}
