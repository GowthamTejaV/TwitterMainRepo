package Tests;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import PageUtilities.TestUtilities;

public class TwitterTest {
	private WebDriver driver = null;
	public static ExtentReports extent;
	public static ExtentTest test;

	@BeforeSuite
	public void setupReport() {

		ExtentSparkReporter spark = new ExtentSparkReporter(
				System.getProperty("user.dir") + "/test-output/ExtentReport.html");
		extent = new ExtentReports();
		extent.attachReporter(spark);
	}

	@BeforeMethod
	public void initialiseDriver() {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless=new");
		options.addArguments(
				"user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");

		// Prevent detection
		Map<String, Object> prefs = new HashMap<>();
		prefs.put("credentials_enable_service", false);
		prefs.put("profile.password_manager_enabled", false);
		options.setExperimentalOption("prefs", prefs);
		options.setExperimentalOption("excludeSwitches", Arrays.asList("enable-automation"));
		options.setExperimentalOption("useAutomationExtension", false);

		driver = new ChromeDriver(options);
	}

	@Test
	public void verifyTwitterTest() {
		test = extent.createTest("Valid Login Test");
		TestUtilities testUtil = new TestUtilities();
		testUtil.createTweets(driver);
	}

	@AfterMethod
	public void tearDown(ITestResult result) {
		if (result.getStatus() == ITestResult.FAILURE) {
			String screenshotPath = captureScreenshot(result.getName());
			test.fail("Test Failed: " + result.getThrowable(),
					MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
		} else if (result.getStatus() == ITestResult.SUCCESS) {
			test.pass("Twitter Test Passed");
			String screenshotPath = captureScreenshot(result.getName());
			MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build();
			test.pass("Test Pass: ", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
		}

		driver.quit();
	}

	@AfterSuite
	public void tearDownReport() {
		extent.flush();
	}

	public String captureScreenshot(String testName) {
		File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

		// Ensure the folder exists
		String folderPath = System.getProperty("user.dir") + "/screenshots/";
		File folder = new File(folderPath);
		if (!folder.exists())
			folder.mkdirs(); // Create folder if not exists

		String destination = folderPath + testName + "_" + timestamp + ".png";

		try {
			FileUtils.copyFile(srcFile, new File(destination));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return destination;
	}

}
