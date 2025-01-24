package Runner;

import UtilitiesFactory.BrowserFactory;
import UtilitiesFactory.EmailReportFactory;
import UtilitiesFactory.ExtentReportFactory;
import com.aventstack.extentreports.MediaEntityBuilder;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.*;

import java.io.File;
import java.io.IOException;
import static UtilitiesFactory.BrowserFactory.getDriver;
import static UtilitiesFactory.UtilFactory.features;



public class TestRunnerListener implements ITestListener,IExecutionListener {

    ExtentReportFactory extentReport = new ExtentReportFactory();
    EmailReportFactory emailReport = new EmailReportFactory();
    String emailReporting;
    String emailRecipients;

    private final BrowserFactory browserFactoryInstance = BrowserFactory.getInstance();

    public TestRunnerListener() throws Exception {
        extentReport.ExtentReport();
    }

//    @Override
//    public void onTestStart(ITestResult iTestResult) {
//        // Creating child nodes for individual test methods
//        String methodName = iTestResult.getMethod().getMethodName();
//        extentReport.test = features.createNode(methodName);
//        browserFactoryInstance.setBrowser(getParameterValue("browser"));
//        emailReporting = getParameterValue("emailReport");
//  //      emailRecipients = getParameterValue("emailRecipients");
//
//    }

    @Override
    public void onTestStart(ITestResult iTestResult) {
        // Get the class name of the test method
        String className = iTestResult.getTestClass().getName();

        // Check if the class node already exists, if not, create it
        if (features == null || !features.getModel().getName().equals(className)) {
            features = extentReport.extent.createTest(className);  // Create a new test for the class
        }

        // Create the method node under the class node
        String methodName = iTestResult.getMethod().getMethodName();
        extentReport.test = features.createNode(methodName);  // Method under the class node
    }

    private void createClassNode(ITestResult iTestResult) {
        if (features == null) {
            String className = iTestResult.getTestClass().getName();
            features = extentReport.extent.createTest(className);
        }
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        try{
            String screenshotPath = captureScreenshot(iTestResult.getMethod().getMethodName());
            extentReport.test.pass("Test Passed: " +
                    MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
            extentReport.ExtentPassStep();
//            getDriver().close();
//            getDriver().quit();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        try{
            String screenshotPath = captureScreenshot(iTestResult.getMethod().getMethodName());
            extentReport.test.fail("Test Failed: " + iTestResult.getThrowable().getMessage(),
                    MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
            getDriver().close();
            extentReport.ExtentFailStep();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private String captureScreenshot(String methodName) throws IOException {
        TakesScreenshot ts = (TakesScreenshot) getDriver();
        File source = ts.getScreenshotAs(OutputType.FILE);
        String destination = System.getProperty("user.dir") + "/screenshots/" + methodName + ".png";
        FileUtils.copyFile(source, new File(destination));
        return destination;
    }

    @Override
    public void onStart(ITestContext iTestContext) {
     //   scenarioDef = extentReport.extent.createTest(iTestContext.getName());
//        features = extentReport.extent.createTest("Google Search");
        // Creating a parent node in the Extent Report based on the test class name
        String className = iTestContext.getAllTestMethods()[0].getRealClass().getSimpleName();
        features = extentReport.extent.createTest(className);
    }

    @Override
    public void onFinish(ITestContext iTestContext) {
        extentReport.FlushReport();
        if(getDriver()!=null){
            getDriver().quit();
        }
    }

    @Override
    public void onExecutionFinish() {
        if (emailReporting.equalsIgnoreCase("on"))
        {
            emailReport.EmailReporter(emailRecipients);
        }
    }

    public String getParameterValue(String key){
        return Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter(key);
    }
}