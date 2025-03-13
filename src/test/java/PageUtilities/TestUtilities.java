package PageUtilities;

import java.io.File;
import java.time.Duration;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestUtilities{

	public static String generateRandomString(int length) {
		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			sb.append(chars.charAt(random.nextInt(chars.length())));
		}
		return sb.toString();
	}

	public static String getRandonNumber() {
		Random random = new Random();
		int randomNumber = random.nextInt(9) + 1;
		return String.valueOf(randomNumber);
	}

	public void createTweets(WebDriver driver) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(40));

			// Open Twitter login page
			driver.get("https://twitter.com/login");
			driver.manage().window().maximize();

			// Wait for username field and enter data
			WebElement username = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("text")));
			username.sendKeys("OpenAk_2O");

			// Click Next button
			WebElement nextButton = wait
					.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Next']")));
			nextButton.click();

			// Wait for password field and enter data
			WebElement password = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("password")));
			password.sendKeys("HarshaTeja@123");

			// Click Login button
			WebElement loginButton = wait
					.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Log in']")));
			loginButton.click();

			// Wait for tweet box and enter text
			WebElement tweetBox = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(.//div[@data-offset-key and @data-editor])[1]")));
			tweetBox.sendKeys(" #ChildAbuserYSRCP " + TestUtilities.generateRandomString(4));

			// Click Tweet button
			WebElement tweetButton = wait
					.until(ExpectedConditions.elementToBeClickable(By.xpath("(.//span[@class and text()='Post'])[2]")));
			tweetButton.click();

			for (int i = 0; i <5; i++) {
				// Wait for tweet box and enter text
				Thread.sleep(3000);
				WebElement tweetBox2 = wait.until(ExpectedConditions
						.elementToBeClickable(By.xpath("(.//div[@data-offset-key and @data-editor])[1]")));
				new Actions(driver).click(tweetBox2)
						.sendKeys("#PichiReddyJagan  " + TestUtilities.generateRandomString(2) + "-->" + i).build()
						.perform();
				Thread.sleep(1000);
				// Locate the file input element
				WebElement uploadElement = driver.findElement(By.xpath("//input[@type='file']"));
				// Provide the file path (Use absolute path for Mac)
				String filePath = new File("src/test/resources/Files/P?.jpeg").getAbsolutePath();// Change this path
				uploadElement.sendKeys(filePath.replace("?", TestUtilities.getRandonNumber()));
				// Click Tweet button
				Thread.sleep(3000);
				WebElement tweetButton3 = wait.until(
						ExpectedConditions.elementToBeClickable(By.xpath("(.//span[@class and text()='Post'])[2]")));

				tweetButton3.click();
			}

			System.out.println("Tweet posted successfully!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
