package UtilitiesFactory;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

public class Hooks {
    private ExtentReports extentReports;
    private ExtentTest test;
    private WebDriver driver;
    private final BrowserFactory browserFactoryInstance = BrowserFactory.getInstance();

    @BeforeSuite
    @Parameters("browser")
    public void beforeTest(String browser) {
        // Set browser type from the XML configuration
        System.out.println("Using browser: " + browser);
        browserFactoryInstance.setDriver(browser);  // Set the driver based on the browser parameter
        driver = BrowserFactory.getDriver();  // Get the initialized WebDriver
        if (driver == null) {
            System.out.println("Driver is still null after initialization!");
        }
    }

    @BeforeSuite
    @Parameters("browser")
    public void setup(String browser) {
        // Initialize ExtentReports
        extentReports = new ExtentReports();

        // Create a new test and assign it to scenarioDef
        test = extentReports.createTest("Test Name");
        UtilFactory.setScenarioDef(test);  // Initialize scenarioDef here
    }

    @AfterSuite
    public void cleanUp() {
        browserFactoryInstance.cleanUp(); // Cleanup the WebDriver after tests
    }
}
