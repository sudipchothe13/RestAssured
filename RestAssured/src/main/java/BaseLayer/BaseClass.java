package BaseLayer;

import java.util.Map;
import java.util.Properties;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.PrintStream;

import org.apache.logging.log4j.ThreadContext;
import org.openqa.selenium.WebDriver;

import CommonLayer.ConfigReader;
import CommonLayer.Log;
import io.restassured.RestAssured;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class BaseClass {
	protected Properties prop;
	public  BaseClass ()  {
		prop = new Properties();
		try {
			FileInputStream fis = new FileInputStream(
			 System.getProperty("user.dir") + "/src/main/java/ConfigLayer/Config.properties");
			prop.load(fis);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    // ================= DRIVER =================
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    public static void setDriver(WebDriver drv) { driver.set(drv); }
    public static WebDriver getDriver() { return driver.get(); }
    public static void unloadDriver() { driver.remove(); }

    // ================= BROWSER =================
    private static ThreadLocal<String> browser = new ThreadLocal<>();
    public static void setBrowser(String browserName) { browser.set(browserName); }
    public static String getBrowser() { return browser.get(); }
    public static void unloadBrowser() { browser.remove(); }

    // ================= UNLOAD ALL =================
    public static void unloadAll() {
        unloadDriver();
        unloadBrowser();
    }

    // ================= THREADCONTEXT / LOGGING =================
    public static void setBrowserContext(String browserName) {
        if (browserName == null || browserName.isEmpty()) {
            ThreadContext.put("browser", "REST");
        } else {
            ThreadContext.put("browser",
                    browserName.substring(0, 1).toUpperCase() + browserName.substring(1).toLowerCase());
        }
    }

    public static void initRestLogging() {
        ThreadContext.put("browser", "REST"); // REST API logs
    }

    public static void clearRestLogging() {
        ThreadContext.remove("browser");
    }

    // ====================== REST ASSURED ======================
    private static ThreadLocal<RequestSpecification> httpRequest = new ThreadLocal<>();
    private static ThreadLocal<Response> response = new ThreadLocal<>();
    private static ThreadLocal<ByteArrayOutputStream> requestOutputStream = new ThreadLocal<>();
    private static ThreadLocal<ByteArrayOutputStream> responseOutputStream = new ThreadLocal<>();
    private static ThreadLocal<String> pathParamEndpoint = new ThreadLocal<>();

    public static void initRestAssured() {
        ByteArrayOutputStream reqStream = new ByteArrayOutputStream();
        ByteArrayOutputStream resStream = new ByteArrayOutputStream();
        requestOutputStream.set(reqStream);
        responseOutputStream.set(resStream);

        PrintStream reqPrintStream = new PrintStream(reqStream);
        PrintStream resPrintStream = new PrintStream(resStream);

        RestAssured.filters(
                new RequestLoggingFilter(LogDetail.ALL, reqPrintStream),
                new ResponseLoggingFilter(LogDetail.ALL, resPrintStream)
        );

        RestAssured.baseURI = ConfigReader.get("baseURI");
        httpRequest.set(RestAssured.given().contentType(ContentType.JSON));
        
    }
    
    public static void addHeaders(Map<String, String> headers) {
        if (headers != null && !headers.isEmpty()) httpRequest.get().headers(headers);
    }

    public static void addRequestBody(Object body) {
        if (body != null) httpRequest.get().body(body);
    }

    public static void addQueryParams(String key, String value) {
        if (key != null && value != null) httpRequest.get().queryParam(key, value);
    }

    public static void addPathParams(String endpoint) {
        if (endpoint != null && !endpoint.isEmpty()) pathParamEndpoint.set(endpoint);
    }

    public static Response sendRequest(String type) {
        String endpoint = pathParamEndpoint.get();
        if (endpoint == null || endpoint.isEmpty()) throw new IllegalArgumentException("Endpoint not set");

        switch (type.toUpperCase()) {
            case "GET": response.set(httpRequest.get().get(endpoint)); break;
            case "POST": response.set(httpRequest.get().post(endpoint)); break;
            case "PUT": response.set(httpRequest.get().put(endpoint)); break;
            case "PATCH": response.set(httpRequest.get().patch(endpoint)); break;
            case "DELETE": response.set(httpRequest.get().delete(endpoint)); break;
            default: throw new IllegalArgumentException("Unsupported request type: " + type);
        }

        logRequestResponse();
        return response.get();
    }

    private static void logRequestResponse() {
        try { requestOutputStream.get().flush(); responseOutputStream.get().flush(); } catch (Exception ignored) {}

        String requestLog = requestOutputStream.get().toString()
                .replaceAll("(Authorization: Bearer )(.*)", "$1********");
        String responseLog = responseOutputStream.get().toString();

        Log.info("========== API SCENARIO START ==========");
        Log.info("REQUEST:\n" + requestLog);
        Log.info("RESPONSE:\n" + responseLog);
        Log.info("========== API SCENARIO END ==========\n");
    }

    // ====================== GETTERS FOR REPORTS ======================
    public static String getRequestLog() {
        if (requestOutputStream.get() == null) return "";
        try { requestOutputStream.get().flush(); } catch (Exception ignored) {}
        return requestOutputStream.get().toString().replaceAll("(Authorization: Bearer )(.*)", "$1********");
    }

    public static String getResponseLog() {
        if (responseOutputStream.get() == null) return "";
        try { responseOutputStream.get().flush(); } catch (Exception ignored) {}
        return responseOutputStream.get().toString();
    }

    public static int getStatusCode() { return response.get().getStatusCode(); }
    public static String getResponseBody() { return response.get().getBody().asString(); }
    public static Response getResponse() { return response.get(); }

    public static void clear() {
        httpRequest.remove();
        response.remove();
        requestOutputStream.remove();
        responseOutputStream.remove();
        pathParamEndpoint.remove();
    }
}
