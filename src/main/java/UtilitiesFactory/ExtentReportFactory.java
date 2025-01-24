package UtilitiesFactory;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import java.io.IOException;
import java.lang.reflect.Method;

import static UtilitiesFactory.EmailReportFactory.failed;
import static UtilitiesFactory.EmailReportFactory.passed;



import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;



public class ExtentReportFactory extends UtilFactory {
    public ExtentReports extent; // Already defined
    public ExtentTest scenarioDef; // Represents class-level nodes
    public ExtentTest test;
    String fileName = reportLocation + "extentreport.html";
    public ExtentReportFactory() throws Exception {
    }

    public ExtentTest classLevelTest;

    @BeforeMethod
    public void startClassReport(Method method) {
        classLevelTest = extent.createTest(method.getDeclaringClass().getSimpleName()); // This will create a node for the class
    }

    @BeforeMethod
    public void ExtentReport() {
        //First is to create Extent Reports
        extent = new ExtentReports();

        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(fileName);
        htmlReporter.config().setTheme(Theme.DARK);
        htmlReporter.config().setDocumentTitle("Google Automation Test Report");
        htmlReporter.config().setEncoding("uft-8");
        htmlReporter.config().setReportName("Google Automation Execution Report");
        htmlReporter.config().setTimeStampFormat("MMM dd, yyyy HH:mm:ss");

        extent.attachReporter(htmlReporter);

    }

//    public void ExtentFailStep() throws IOException {
//        failed++;
//        scenarioDef.log(Status.FAIL,
//                "<details>" + "<summary> <b> <font color=red> Cause of Failure: </b> "
//                        + "</font>" + "</summary>"
//                        + failureException.replaceAll(",", "<br>") + "</details>", MediaEntityBuilder.createScreenCaptureFromBase64String(
//                        UtilFactory.getBase64Screenshot(BrowserFactory.getDriver(), "Screenshot")).build());
//    }

//    public void ExtentFailStep() throws IOException {
//        failed++;
//        String failureMessage = (failureException != null) ? failureException.replaceAll(",", "<br>") : "No exception message available";
//
//        // Ensure scenarioDef is initialized before logging
//        if (scenarioDef != null) {
//            scenarioDef.log(Status.FAIL,
//                    "<details>" + "<summary> <b> <font color=red> Cause of Failure: </b> "
//                            + "</font>" + "</summary>"
//                            + failureMessage + "</details>", MediaEntityBuilder.createScreenCaptureFromBase64String(
//                            UtilFactory.getBase64Screenshot(BrowserFactory.getDriver(), "Screenshot")).build());
//        } else {
//            System.out.println("scenarioDef is not initialized");
//        }
//    }

    // Modify the method to pass a name for the screenshot
    public void ExtentFailStep() throws IOException {
        failed++;

        String failureMessage = (failureException != null) ? failureException.replaceAll(",", "<br>") : "No exception message available";

        // Ensure test node is initialized
        if (classLevelTest != null) {
            // Use Base64 encoded screenshot and provide a name for the screenshot
            String base64Screenshot = UtilFactory.getBase64Screenshot(BrowserFactory.getDriver(),"Failure Screenshot");
            classLevelTest.fail("Test Failed: " + failureMessage,
                    MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
        } else {
            System.out.println("Test node is not initialized for logging failure.");
        }
    }


//    public void ExtentPassStep() throws IOException {
//        passed++;
//        scenarioDef.log(Status.PASS,
//                "<summary> <b> <font color=green> Test Passed: </b> "
//                        + "</font>" + "</summary>"
//                , MediaEntityBuilder.createScreenCaptureFromBase64String(
//                        UtilFactory.getBase64Screenshot(BrowserFactory.getDriver(), "Screenshot")).build());
//    }

    public void ExtentPassStep() throws IOException {
        passed++;

        // Ensure the test node is initialized for logging
        if (classLevelTest != null) {
            // Get the Base64 screenshot string
            String base64Screenshot = UtilFactory.getBase64Screenshot(BrowserFactory.getDriver(), "Pass Screenshot");

            // Log the success with the screenshot attached
            classLevelTest.pass("Test Passed",
                    MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
        } else {
            System.out.println("Test node is not initialized for logging pass.");
        }
    }

    @AfterMethod
    public void FlushReport(){
        extent.flush();
    }
}
