package UtilitiesFactory;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;
import org.testng.ITestContext;

import java.lang.reflect.Method;

public class Hooks {

    private static ThreadLocal<ExtentReports> extentReports = new ThreadLocal<>();
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private final BrowserFactory browserFactoryInstance = BrowserFactory.getInstance();

    @BeforeTest
    @Parameters({"browser", "headless"})
    public void beforeTest(@Optional("chrome") String browser, @Optional("false") String headless, ITestContext context) {
        // Convert headless parameter to boolean
        boolean isHeadless = Boolean.parseBoolean(headless);

        // Log browser and headless mode
        System.out.println("Using browser: " + browser + " | Headless: " + isHeadless);

        // Initialize WebDriver for the specific browser with headless mode
        browserFactoryInstance.setDriver(browser, isHeadless);
        driver.set(BrowserFactory.getDriver());  // Set WebDriver in ThreadLocal

        if (driver.get() == null) {
            System.out.println("Driver is still null after initialization!");
        }

        // Initialize ExtentReports for parallel execution
        if (extentReports.get() == null) {
            extentReports.set(new ExtentReports());
        }
        // Create a test-specific report
        test.set(extentReports.get().createTest(context.getName()));  // Set test-specific ExtentTest in ThreadLocal

        // Initialize scenarioDef in UtilFactory for logging
        UtilFactory.setScenarioDef(test.get());  // Initialize scenarioDef here
    }

    @BeforeMethod
    public void beforeMethod(Method method) throws Exception {
        // This ensures that the scenarioDef is initialized properly before each test method
        test.set(extentReports.get().createTest(method.getName()));
        UtilFactory.setScenarioDef(test.get()); // Ensure scenarioDef is reset for each method
    }

    @AfterTest
    public void afterTest() {
        // Clean up WebDriver for the specific thread
        if (driver.get() != null) {
            browserFactoryInstance.cleanUp();  // Clean up the WebDriver after the test is done
            driver.remove(); // Ensure thread-local WebDriver is cleaned up
        }
    }

    @AfterSuite
    public void afterSuite() {
        // Optionally, flush the ExtentReports after all tests have completed
        if (extentReports.get() != null) {
            extentReports.get().flush();
        }

        // Close the driver and clean up resources at the end of the suite
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove(); // Ensure thread-local WebDriver is cleaned up
        }
    }
}
