package HooksAPI;

import org.apache.logging.log4j.ThreadContext;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;

import BaseLayer.BaseClass;
import CommonLayer.Log;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class HooksAPI extends BaseClass {

//    @Before
//    public void beforeScenario(Scenario scenario) {
//        BaseClass.initRestLogging(); // Set logging to REST
//        Log.info("==================== API Scenario START: " + scenario.getName() + " ====================");
//    }
    
    @Before
    public void beforeScenario(Scenario scenario) {

        // 🔥 API runner decides this
        ThreadContext.put("browser", "RestAssuredLogs");

        Log.info("========== API Scenario START: " + scenario.getName() + " ==========");
    }


    @After
    public void afterScenario(Scenario scenario) {

        // ✅ Attach API logs to Extent
        ExtentCucumberAdapter.getCurrentStep()
                .log(Status.INFO, "<pre>" + BaseClass.getRequestLog() + "</pre>");
        ExtentCucumberAdapter.getCurrentStep()
                .log(Status.INFO, "<pre>" + BaseClass.getResponseLog() + "</pre>");

        Log.info("==================== API Scenario END : " 
                 + scenario.getName() + " ====================");

        // ✅ Clear ThreadContext ONLY ONCE
        BaseClass.clearRestLogging();
    }

}
