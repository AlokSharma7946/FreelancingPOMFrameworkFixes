package UtilitiesFactory;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import java.io.IOException;
import java.lang.reflect.Method;

import static UtilitiesFactory.BrowserFactory.getDriver;
import static UtilitiesFactory.EmailReportFactory.failed;
import static UtilitiesFactory.EmailReportFactory.passed;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.ITestResult;

public class ExtentReportFactory extends UtilFactory {
    public ExtentReports extent; // Already defined
    public static ThreadLocal<ExtentTest> testThreadLocal = new ThreadLocal<>(); // ThreadLocal to handle test-specific objects
    String fileName = reportLocation + "extentreport.html";
    public static int passed = 0;
    public static int failed = 0;

    public ExtentReportFactory() throws Exception {
    }

    public ExtentTest classLevelTest;

    @BeforeClass
    public void ExtentReport() {
        // Initialize ExtentReports
        extent = new ExtentReports();

        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(fileName);
        htmlReporter.config().setTheme(Theme.DARK);
        htmlReporter.config().setDocumentTitle("Google Automation Test Report");
        htmlReporter.config().setEncoding("utf-8");
        htmlReporter.config().setReportName("Google Automation Execution Report");
        htmlReporter.config().setTimeStampFormat("MMM dd, yyyy HH:mm:ss");

        extent.attachReporter(htmlReporter);
    }

    // Modify the method to pass a name for the screenshot
    public void ExtentFailStep(ITestResult iTestResult) throws IOException {
        failed++; // Increment failed counter
        String failureMessage = (iTestResult.getThrowable() != null) ? iTestResult.getThrowable().getMessage() : "No exception message available";

        if (testThreadLocal.get() != null) {
            // Capture the screenshot in base64
            String base64Screenshot = UtilFactory.getBase64Screenshot(BrowserFactory.getDriver(), "Failure Screenshot");

            String browserName = "Unknown";  // Default if we can't determine the browser

            WebDriver driverInstance = getDriver();  // Get the WebDriver instance

            if (driverInstance instanceof RemoteWebDriver) {
                // If the driver is a RemoteWebDriver, we can access capabilities
                Capabilities capabilities = ((RemoteWebDriver) driverInstance).getCapabilities();
                browserName = capabilities.getBrowserName();
            }

            // Get the test method name
            String methodName = iTestResult.getMethod().getMethodName(); // Using ITestResult to get method name

            // Create a more descriptive screenshot name with browser name and method name
            String screenshotName = browserName + "_" + methodName + "_Failure_" + System.currentTimeMillis() + ".png";

            // Include the screenshot name in the report
            testThreadLocal.get().fail("Test Failed: " + failureMessage + "\nScreenshot: " + screenshotName,
                    MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
        } else {
            System.out.println("Test node is not initialized for logging failure.");
        }
    }

    public void ExtentPassStep(ITestResult iTestResult) throws IOException {
        passed++; // Increment passed counter

        if (testThreadLocal.get() != null) {
            // Capture the screenshot in base64
            String base64Screenshot = UtilFactory.getBase64Screenshot(BrowserFactory.getDriver(), "Pass Screenshot");

            String browserName = "Unknown";  // Default if we can't determine the browser

            WebDriver driverInstance = getDriver();  // Get the WebDriver instance

            if (driverInstance instanceof RemoteWebDriver) {
                // If the driver is a RemoteWebDriver, we can access capabilities
                Capabilities capabilities = ((RemoteWebDriver) driverInstance).getCapabilities();
                browserName = capabilities.getBrowserName();
            }

            // Get the test method name
            String methodName = iTestResult.getMethod().getMethodName(); // Using ITestResult to get method name

            // Create a more descriptive screenshot name with browser name and method name
            String screenshotName = browserName + "_" + methodName + "_Pass_" + System.currentTimeMillis() + ".png";

            // Include the screenshot name in the report
            testThreadLocal.get().pass("Test Passed\nScreenshot: " + screenshotName,
                    MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
        } else {
            System.out.println("Test node is not initialized for logging pass.");
        }
    }


    @AfterMethod
    public void FlushReport() {
        extent.flush();
    }

    @AfterClass
    public void afterClass() {
        // Optionally, flush after all tests in the class are done
        extent.flush();
        // Log final passed and failed count if needed
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);
    }
}
