package api.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import io.restassured.response.Response;

public class ExtentReportManager implements ITestListener {

    private static ExtentReports extent;
    private static ExtentSparkReporter sparkReporter;

    // Thread-safe for parallel execution
    private static final ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    public static ExtentTest getTest() {
        return test.get();
    }

    @Override
    public void onStart(ITestContext context) {

        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss")
                .format(new Date());

        String reportPath =
                PropertyReader.getProperty("report.path")
                + "/API_Report_" + timeStamp + ".html";

        sparkReporter = new ExtentSparkReporter(reportPath);

        sparkReporter.config().setDocumentTitle(
                PropertyReader.getProperty("report.title"));

        sparkReporter.config().setReportName(
                PropertyReader.getProperty("report.name"));

        String theme = PropertyReader.getProperty("report.theme");

        if ("DARK".equalsIgnoreCase(theme)) {
            sparkReporter.config().setTheme(Theme.DARK);
        } else {
            sparkReporter.config().setTheme(Theme.STANDARD);
        }

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);

        extent.setSystemInfo(
                "Application",
                PropertyReader.getProperty("application"));

        extent.setSystemInfo(
                "Environment",
                PropertyReader.getProperty("environment"));

        extent.setSystemInfo(
                "OS",
                System.getProperty("os.name"));

        extent.setSystemInfo(
                "Java Version",
                System.getProperty("java.version"));

        extent.setSystemInfo(
                "User",
                System.getProperty("user.name"));
    }

    @Override
    public void onTestStart(ITestResult result) {

        ExtentTest extentTest = extent.createTest(
                result.getMethod().getMethodName());

        extentTest.assignCategory(
                result.getTestClass().getRealClass().getSimpleName());

        test.set(extentTest);

        test.get().log(Status.INFO, "Execution Started");
    }

    @Override
    public void onTestSuccess(ITestResult result) {

        test.get().log(Status.PASS, "Test Passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {

        test.get().log(Status.FAIL, "Test Failed");

        if (result.getThrowable() != null) {
            test.get().fail(result.getThrowable());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {

        test.get().log(Status.SKIP, "Test Skipped");

        if (result.getThrowable() != null) {
            test.get().skip(result.getThrowable());
        }
    }

    @Override
    public void onFinish(ITestContext context) {

        if (extent != null) {
            extent.flush();
        }
    }

    public static void logApiDetails(String endpoint, String httpMethod, Object request, Response response) {

    ExtentTest extentTest = getTest();

     if (extentTest == null) {
        return;
    }

    extentTest.info("<b>Endpoint :</b> " + endpoint);
    extentTest.info("<b>HTTP Method :</b> " + httpMethod);

    if (request != null) {
        extentTest.info("<b>Request Body</b><br><pre>"
                + request.toString()
                + "</pre>");
    }

    extentTest.info("<b>Status Code :</b> "
            + response.getStatusCode());

    extentTest.info("<b>Response Time :</b> "
            + response.getTime() + " ms");

    extentTest.info("<b>Response Headers</b><br><pre>"
            + response.getHeaders()
            + "</pre>");

    extentTest.info("<b>Response Body</b><br><pre>"
            + response.asPrettyString()
            + "</pre>");
    }
}
