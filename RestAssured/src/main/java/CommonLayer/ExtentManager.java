package CommonLayer;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

/**
 * Unified Extent Reports manager
 * Handles ExtentReports creation, ExtentTest management (thread-safe), and flushing
 */
public class ExtentManager {

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    // ================= EXTENT REPORTS =================
    public static ExtentReports getExtentReports() {
        if (extent == null) {
            try {
                String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String reportPath = System.getProperty("user.dir") + "/Reports/ExtentReport_" + timestamp + ".html";

                ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
                spark.config().setReportName("UI Test Report");
                spark.config().setDocumentTitle("Automation Report");

                extent = new ExtentReports();
                extent.attachReporter(spark);
                extent.setSystemInfo("Tester", "Sudip Chothe");
                extent.setSystemInfo("OS", System.getProperty("os.name"));
                extent.setSystemInfo("Java Version", System.getProperty("java.version"));

            } catch (Exception e) {
                throw new RuntimeException("Failed to initialize ExtentReports", e);
            }
        }
        return extent;
    }

    // ================= CREATE / GET / REMOVE TEST =================
    public static ExtentTest createTest(String testName) {
        ExtentTest t = getExtentReports().createTest(testName);
        setTest(t);
        return t;
    }

    public static void setTest(ExtentTest t) {
        test.set(t);
    }

    public static ExtentTest getTest() {
        return test.get();
    }

    public static void removeTest() {
        test.remove();
    }

    // ================= FLUSH REPORT =================
    public static void flushReports() {
        if (extent != null) {
            extent.flush();
        }
    }
}
