package TestRunner;

import java.io.File;
import org.apache.logging.log4j.ThreadContext;
import org.testng.annotations.BeforeSuite;

import CommonLayer.Log;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
    features = "FeatureFiles",
    glue = { "StepDefinition", "HooksAPI" },
    tags = "@Company",
    plugin = {
        "pretty",
        "rerun:target/rerun-api.txt",
        "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
    }
)
public class APIRunner extends AbstractTestNGCucumberTests {

    @BeforeSuite(alwaysRun = true)
    public void setupAPI() {

        // 1️⃣ Create Logs folder if it doesn't exist
        File logDir = new File("Logs");
        if (!logDir.exists()) logDir.mkdirs();

        // 2️⃣ Delete all existing log files to start fresh
        File[] files = logDir.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isFile() && f.getName().endsWith(".log")) {
                    f.delete();
                }
            }
        }

        // 3️⃣ Set ThreadContext so Log4j routing appender picks REST
        ThreadContext.put("browser", "REST");

        // 4️⃣ Log the start of API suite
        Log.info("===== API Test Suite Started =====");

        // 5️⃣ Set ExtentReports path
        System.setProperty(
            "basefolder.name",
            System.getProperty("user.dir") + "/Reports/API"
        );
    }
}
