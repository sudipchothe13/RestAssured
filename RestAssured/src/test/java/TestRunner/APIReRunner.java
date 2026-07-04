package TestRunner;

import org.testng.annotations.BeforeSuite;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "@target/rerun-api.txt",
        glue = { "stepDefinitions", "HooksAPI" },
        plugin = {
                "pretty",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
        }
)
public class APIReRunner extends AbstractTestNGCucumberTests {

    @BeforeSuite(alwaysRun = true)
    public void setAPIRerunReportPath() {
        System.setProperty("basefolder.name", "Reports/API/RerunSparkReport");
    }
}
