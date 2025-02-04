package UtilitiesFactory;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;
import org.testng.ITestContext;

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
        extentReports.set(new ExtentReports());
        test.set(extentReports.get().createTest(context.getName()));  // Set test-specific ExtentTest in ThreadLocal
        UtilFactory.setScenarioDef(test.get());  // Initialize scenarioDef here
    }

    @AfterTest
    public void afterTest() {
        // Clean up WebDriver for the specific thread
        browserFactoryInstance.cleanUp();  // Clean up the WebDriver after the test is done
    }

    @AfterSuite
    public void afterSuite() {
        // Optionally, flush the ExtentReports after all tests have completed
        if (extentReports.get() != null) {
            extentReports.get().flush();
        }
        if (driver != null) {
            driver.get().quit();
        }
    }
}
