package TestRunner;

import org.testng.annotations.BeforeSuite;

public class BaseRunner {

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        // overridden by child runners
    }
}
