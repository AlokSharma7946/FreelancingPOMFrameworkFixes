package Runner;

import UtilitiesFactory.BrowserFactory;
import UtilitiesFactory.EmailReportFactory;
import UtilitiesFactory.ExtentReportFactory;
import com.aventstack.extentreports.MediaEntityBuilder;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.*;

import java.io.File;
import java.io.IOException;
import static UtilitiesFactory.BrowserFactory.getDriver;
import static UtilitiesFactory.UtilFactory.features;

public class TestRunnerListener implements ITestListener, IExecutionListener {

    ExtentReportFactory extentReport = new ExtentReportFactory();
    EmailReportFactory emailReport = new EmailReportFactory();
    String emailReporting;
    String emailRecipients;

    private final BrowserFactory browserFactoryInstance = BrowserFactory.getInstance();

    public TestRunnerListener() throws Exception {
        extentReport.ExtentReport();
        if (this.emailReporting == null) {
            System.out.println("emailReporting is null, initializing it...");
            this.emailReporting = "default_value"; // Initialize it if needed
        }
        if (features == null) {
//            features = extentReport.extent.createTest("DefaultTestFeature");  // Default value if features is null
        }
    }

    @Override
    public void onTestStart(ITestResult iTestResult) {
        // Extract the simple class name
        String className = iTestResult.getTestClass().getRealClass().getSimpleName();

        // Check if the class node already exists or if it's for a new class
        if (features == null || !features.getModel().getName().equals(className)) {
            features = extentReport.extent.createTest(className); // Create class node
        }

        // Create a child node for the method
        String methodName = iTestResult.getMethod().getMethodName();
        extentReport.testThreadLocal.set(features.createNode(methodName));

        // Debug logs for verification
        System.out.println("Class node created: " + className);
        System.out.println("Method node created: " + methodName);
        emailRecipients = getParameterValue("emailRecipients");
    }

    private void createClassNode(ITestResult iTestResult) {
        if (features == null) {
            String className = iTestResult.getTestClass().getName();
            features = extentReport.extent.createTest(className);
        }
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        try {
            // Capture the screenshot and get the path
            String screenshotPath = captureScreenshot(iTestResult.getMethod().getMethodName());

            // Use absolute path for the screenshot
            String absolutePath = new File(screenshotPath).getAbsolutePath();

            // Extract the screenshot name
            String screenshotName = iTestResult.getMethod().getMethodName() + ".png";

            // Debugging: Log the absolute path
            System.out.println("Screenshot saved to: " + absolutePath);

            // Add the screenshot to the Extent report with the screenshot name
            extentReport.testThreadLocal.get().pass("Test Passed: " + screenshotName +
                    MediaEntityBuilder.createScreenCaptureFromPath(absolutePath).build());

            // Logging success step in the Extent report
            extentReport.ExtentPassStep(iTestResult);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        try {
            // Capture the screenshot and get the path
            String screenshotPath = captureScreenshot(iTestResult.getMethod().getMethodName());

            // Use absolute path for the screenshot
            String absolutePath = new File(screenshotPath).getAbsolutePath();

            // Extract the screenshot name
            String screenshotName = iTestResult.getMethod().getMethodName() + ".png";

            // Debugging: Log the absolute path
            System.out.println("Screenshot saved to: " + absolutePath);

            // Add the screenshot to the Extent report with the screenshot name
            extentReport.testThreadLocal.get().fail("Test Failed: " + iTestResult.getThrowable().getMessage() +
                            "\nScreenshot: " + screenshotName,
                    MediaEntityBuilder.createScreenCaptureFromPath(absolutePath).build());

            // Logging failure step in the Extent report
            extentReport.ExtentFailStep(iTestResult);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String captureScreenshot(String methodName) throws IOException {
        // Retrieve the browser name from WebDriver's capabilities
        String browserName = "Unknown";  // Default if we can't determine the browser

        WebDriver driverInstance = getDriver();  // Get the WebDriver instance

        if (driverInstance instanceof RemoteWebDriver) {
            // If the driver is a RemoteWebDriver, we can access capabilities
            Capabilities capabilities = ((RemoteWebDriver) driverInstance).getCapabilities();
            browserName = capabilities.getBrowserName();
        }

        // Take screenshot
        TakesScreenshot ts = (TakesScreenshot) getDriver();
        File source = ts.getScreenshotAs(OutputType.FILE);

        // Construct the destination file path with browser name
        String destination = System.getProperty("user.dir") + "/screenshots/" + methodName + "_" + browserName + ".png";

        // Copy the screenshot file to the specified destination
        FileUtils.copyFile(source, new File(destination));

        // Return the destination path
        return destination;
    }


//    @Override
//    public void onStart(ITestContext iTestContext) {
//        // Extract the simple class name from the context
//        String className = iTestContext.getAllTestMethods()[0].getRealClass().getSimpleName();
//
//        // Create a class node if not already created
//        if (features == null || !features.getModel().getName().equals(className)) {
//            features = extentReport.extent.createTest(className);
//        }
//
//        // Debug log
//        System.out.println("Class node initialized in onStart: " + className);
//    }

    @Override
    public void onFinish(ITestContext iTestContext) {
        extentReport.FlushReport();
        if (getDriver() != null) {
            getDriver().quit();
        }
    }

    @Override
    public void onExecutionFinish() {
        if (emailReporting != null && emailReporting.equalsIgnoreCase("on")) {
            emailReport.EmailReporter(emailRecipients);
        }
    }

    public String getParameterValue(String key) {
        String value = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter(key);
        return value != null ? value : "default";  // Return default if the parameter is not found
    }
}
