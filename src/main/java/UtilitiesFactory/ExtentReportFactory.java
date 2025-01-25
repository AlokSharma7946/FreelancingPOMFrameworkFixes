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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

public class ExtentReportFactory extends UtilFactory {
    public ExtentReports extent; // Already defined
    public ThreadLocal<ExtentTest> testThreadLocal = new ThreadLocal<>(); // ThreadLocal to handle test-specific objects
    String fileName = reportLocation + "extentreport.html";
    public static int passed = 0;
    public static int failed = 0;

    public ExtentReportFactory() throws Exception {
    }

    public ExtentTest classLevelTest;

    @BeforeMethod
    public void startClassReport(Method method) {
        // Set the test for the current thread
        testThreadLocal.set(extent.createTest(method.getDeclaringClass().getSimpleName() + " - " + method.getName()));
    }

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
    public void ExtentFailStep() throws IOException {
        failed++; // Increment failed counter
        String failureMessage = (failureException != null) ? failureException.replaceAll(",", "<br>") : "No exception message available";

        if (testThreadLocal.get() != null) {
            String base64Screenshot = UtilFactory.getBase64Screenshot(BrowserFactory.getDriver(), "Failure Screenshot");
            testThreadLocal.get().fail("Test Failed: " + failureMessage,
                    MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
        } else {
            System.out.println("Test node is not initialized for logging failure.");
        }
    }

    public void ExtentPassStep() throws IOException {
        passed++; // Increment passed counter
        if (testThreadLocal.get() != null) {
            String base64Screenshot = UtilFactory.getBase64Screenshot(BrowserFactory.getDriver(), "Pass Screenshot");
            testThreadLocal.get().pass("Test Passed",
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
