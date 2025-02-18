package Runner;

import UtilitiesFactory.BrowserFactory;
import UtilitiesFactory.EmailReportFactory;
import UtilitiesFactory.ExtentReportFactory;
import UtilitiesFactory.UtilFactory;
import com.aventstack.extentreports.ExtentTest;
import org.openqa.selenium.WebDriver;
import org.testng.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static UtilitiesFactory.BrowserFactory.getDriver;

public class TestRunnerListener implements ITestListener, IExecutionListener {

    ExtentReportFactory extentReport = new ExtentReportFactory();
    EmailReportFactory emailReport = new EmailReportFactory();
    String emailReporting;
    String emailRecipients;

    private static ThreadLocal<Map<String, ExtentTest>> featuresThreadLocal = ThreadLocal.withInitial(ConcurrentHashMap::new);

    public TestRunnerListener() throws Exception {
        extentReport.ExtentReport();
    }

    @Override
    public void onTestStart(ITestResult iTestResult) {
        String className = iTestResult.getTestClass().getRealClass().getSimpleName();
        String methodName = iTestResult.getMethod().getMethodName();
        String browserName = getBrowserName();

        String classBrowserKey = className + " - " + browserName;

        Map<String, ExtentTest> classNodeMap = featuresThreadLocal.get();
        if (!classNodeMap.containsKey(classBrowserKey)) {
            classNodeMap.put(classBrowserKey, extentReport.extent.createTest(classBrowserKey));
        }

        // Create a node for the method and set it in the thread-local for the current test
        ExtentTest methodTest = classNodeMap.get(classBrowserKey).createNode(methodName);
        extentReport.testThreadLocal.set(methodTest);  // Ensure that the thread-local test object is set
        UtilFactory.setScenarioDef(methodTest);  // Set the scenarioDef in UtilFactory so that logging works correctly
    }


    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        try {
            extentReport.ExtentPassStep(iTestResult);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        try {
            extentReport.ExtentFailStep(iTestResult);
            extentReport.FlushReport();  // Ensure that the report is flushed before quitting
            getDriver().quit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String getBrowserName() {
        String browserName = "Unknown";
        WebDriver driverInstance = getDriver();
        if (driverInstance instanceof org.openqa.selenium.remote.RemoteWebDriver) {
            browserName = ((org.openqa.selenium.remote.RemoteWebDriver) driverInstance).getCapabilities().getBrowserName();
        }
        return browserName;
    }

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
        return value != null ? value : "default";
    }
}
