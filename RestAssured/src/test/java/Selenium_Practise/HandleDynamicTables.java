package Selenium_Practise;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class HandleDynamicTables {
	private static WebDriver driver;

	@Test
	public static void launchApp() throws InterruptedException {

		driver = new ChromeDriver();

		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));

		driver.get("https://www.nseindia.com/get-quote/optionchain/NIFTY/NIFTY-50");

		// Print Header
		List<WebElement> headers = driver.findElements(
		        By.xpath("//*[@id='quote-tabs-tabpane-optionchain']/div/div[3]/div/div/table/thead/tr[2]/th"));

		System.out.println("=========== HEADERS ===========");

		for (WebElement header : headers) {
		    System.out.print(header.getText() + " | ");
		}

		System.out.println();

		// Print Values (Example: Row 64)
		List<WebElement> values = driver.findElements(
		        By.xpath("//*[@id='quote-tabs-tabpane-optionchain']/div/div[3]/div/div/table/tbody/tr[64]/td"));

		System.out.println("=========== VALUES ===========");

		for (WebElement value : values) {
		    System.out.print(value.getText() + " | ");
		}
	}

	@AfterMethod
	public void tearDown() throws InterruptedException {
		Thread.sleep(3000);
		driver.quit();
	}
}
