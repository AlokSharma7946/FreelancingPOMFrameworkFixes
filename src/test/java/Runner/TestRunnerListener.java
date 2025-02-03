package Runner;

import UtilitiesFactory.BrowserFactory;
import UtilitiesFactory.EmailReportFactory;
import UtilitiesFactory.ExtentReportFactory;
import com.aventstack.extentreports.ExtentTest;
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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.aventstack.extentreports.ExtentTest;

import static UtilitiesFactory.BrowserFactory.getDriver;
//import static UtilitiesFactory.UtilFactory.features;

public class TestRunnerListener implements ITestListener, IExecutionListener {

    ExtentReportFactory extentReport = new ExtentReportFactory();
    EmailReportFactory emailReport = new EmailReportFactory();
    String emailReporting;
    String emailRecipients;
//    private static ThreadLocal<com.aventstack.extentreports.ExtentTest> featuresThreadLocal = new ThreadLocal<>();


    private final BrowserFactory browserFactoryInstance = BrowserFactory.getInstance();

    public TestRunnerListener() throws Exception {
        extentReport.ExtentReport();
        if (this.emailReporting == null) {
            System.out.println("emailReporting is null, initializing it...");
            this.emailReporting = "default_value"; // Initialize it if needed
        }
        if (featuresThreadLocal == null) {
//            features = extentReport.extent.createTest("DefaultTestFeature");  // Default value if features is null
        }
    }



    private static ThreadLocal<Map<String, com.aventstack.extentreports.ExtentTest>> featuresThreadLocal = ThreadLocal.withInitial(ConcurrentHashMap::new);

    @Override
    public void onTestStart(ITestResult iTestResult) {
        String className = iTestResult.getTestClass().getRealClass().getSimpleName();
        String methodName = iTestResult.getMethod().getMethodName();
        String browserName = getBrowserName(); // Get the browser name

        // Unique key for each class-browser combination
        String classBrowserKey = className + " - " + browserName;

        // Get the thread-local map and ensure class node is created
        Map<String, ExtentTest> classNodeMap = featuresThreadLocal.get();

        if (!classNodeMap.containsKey(classBrowserKey)) {
            classNodeMap.put(classBrowserKey, extentReport.extent.createTest(classBrowserKey));
        }

        // Set the child node for the test method
        extentReport.testThreadLocal.set(classNodeMap.get(classBrowserKey).createNode(methodName));

        System.out.println("Class node created: " + classBrowserKey);
        System.out.println("Method node created: " + methodName);
    }



    private String getBrowserName() {
        String browserName = "Unknown"; // Default value
        WebDriver driverInstance = getDriver(); // Get the WebDriver instance

        if (driverInstance instanceof RemoteWebDriver) {
            Capabilities capabilities = ((RemoteWebDriver) driverInstance).getCapabilities();
            browserName = capabilities.getBrowserName();
        }

        return browserName;
    }



    private void createClassNode(ITestResult iTestResult) {
        if (featuresThreadLocal.get() == null) {
            String className = iTestResult.getTestClass().getRealClass().getSimpleName();
            featuresThreadLocal.set((Map<String, ExtentTest>) extentReport.extent.createTest(className));
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
            getDriver().quit();
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
