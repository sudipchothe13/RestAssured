package StepDefinition;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;

import BaseLayer.BaseClass;
import PojoLayer.Companies;
import PojoLayer.CompanyDetails;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

public class CompaniesStepDefinition extends BaseClass{

	private Response response;

	@Given("Company request payload is prepared")
	public void company_request_payload_is_prepared() {

		BaseClass.initRestAssured();

		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		BaseClass.addHeaders(headers);

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
	}

	@Given("Companies endpoint is available")
	public void companies_endpoint_is_available() {

		BaseClass.initRestAssured();

		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		BaseClass.addHeaders(headers);
	}

	@When("User sends {string} request to {string}")
	public void user_sends_request_to(String requestType, String endpoint) {

		BaseClass.addPathParams(endpoint);
		response = BaseClass.sendRequest(requestType);
	}

	@Then("Response status code should be {int}")
	public void response_status_code_should_be(Integer statusCode) {

		Assert.assertEquals(response.getStatusCode(), statusCode.intValue());

	}
}