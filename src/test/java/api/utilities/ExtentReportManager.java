package api.utilities;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.ITestContext;
import org.testng.ITestListener;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentReportManager implements ITestListener {
	
	public ExtentReports extent;
	public ExtentTest test;
	public ExtentSparkReporter sparkReporter;
	
	String repName;
	
	public void onStart(ITestContext context) {
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		repName = "Test-Report-" + timeStamp + ".html";
		
		sparkReporter = new ExtentSparkReporter(System.getProperty("user.dir") + "/test-output/ExtentReport.html");
		sparkReporter.config().setDocumentTitle("RestAssured Automation Report");
		sparkReporter.config().setReportName("Pet Store API Test Report");
		sparkReporter.config().setTheme(Theme.DARK);
		
		extent = new ExtentReports();
		extent.attachReporter(sparkReporter);
		extent.setSystemInfo("Host Name", "LocalHost");
		extent.setSystemInfo("Environment", "QA");
		extent.setSystemInfo("User", "Your Name");
	}

	public void onFinish(ITestContext context) {
		extent.flush();
	}
	
	public void onTestStart(org.testng.ITestResult result) {
		test = extent.createTest(result.getName());
	}
	
	public void onTestSuccess(org.testng.ITestResult result) {
		test.pass("Test Passed");
	}
	
	public void onTestFailure(org.testng.ITestResult result) {
		test=extent.createTest(result.getName());
		test.createNode(result.getName());
		test.assignCategory(result.getMethod().getGroups());
		test.log(Status.FAIL, "Test Failed");
		test.log(Status.FAIL, result.getThrowable().getMessage());
	}
	
	public void onTestSkipped(org.testng.ITestResult result) {
		test.skip(result.getThrowable());
	}
}
