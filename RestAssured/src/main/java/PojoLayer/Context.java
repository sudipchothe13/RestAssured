package PojoLayer;

import org.openqa.selenium.WebDriver;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;

public class Context {

	// ---------------- API INFO ----------------
	private int port;
	private String endPoint;
	private Response response;
	private RequestSpecBuilder requestSpecBuilder;

	// ---------------- DESERIALIZED ROOT ----------------
	private RootResponse rootResponse;

	// ---------------- GETTERS & SETTERS ----------------

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

	public RequestSpecBuilder getRequestSpecBuilder() {
		return requestSpecBuilder;
	}

	public void setRequestSpecBuilder(RequestSpecBuilder requestSpecBuilder) {
		this.requestSpecBuilder = requestSpecBuilder;
	}

	public RootResponse getRootResponse() {
		return rootResponse;
	}

	public void setRootResponse(RootResponse rootResponse) {
		this.rootResponse = rootResponse;
	}
	
    private static ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();

    public static void setDriver(WebDriver driver) {
        driverThread.set(driver);
    }

    public static WebDriver getDriver() {
        return driverThread.get();
    }

    public static void removeDriver() {
        driverThread.remove();
    }
}
