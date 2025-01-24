package Runner;

import UtilitiesFactory.BrowserFactory;
import UtilitiesFactory.EmailReportFactory;
import UtilitiesFactory.ExtentReportFactory;
import org.testng.*;
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

    @Override
    public void onTestStart(ITestResult iTestResult) {
        browserFactoryInstance.setBrowser(getParameterValue("browser"));
        emailReporting = getParameterValue("emailReport");
  //      emailRecipients = getParameterValue("emailRecipients");
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        try{
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
            extentReport.ExtentFailStep();
            getDriver().close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onStart(ITestContext iTestContext) {
     //   scenarioDef = extentReport.extent.createTest(iTestContext.getName());
        features = extentReport.extent.createTest("Google Search");
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