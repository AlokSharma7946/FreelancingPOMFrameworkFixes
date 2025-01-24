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
    public void beforeTest(@Optional("chrome") String browser) {
        // Ensure the browser is set before driver initialization
        browserFactoryInstance.setBrowser(browser);
        browserFactoryInstance.setDriver(browser); // Set driver after setting browser type
        WebDriver driver = BrowserFactory.getDriver();
        if (driver == null) {
            System.out.println("Driver is still null after initialization!");
        }
    }

    @BeforeSuite
    @Parameters("browser")
    public void setup(@Optional("chrome") String browser) {
        // Initialize ExtentReports
        extentReports = new ExtentReports();

        // Create a new test and assign it to scenarioDef
        test = extentReports.createTest("Test Name");
        UtilFactory.setScenarioDef(test);  // Initialize scenarioDef here
    }

}
