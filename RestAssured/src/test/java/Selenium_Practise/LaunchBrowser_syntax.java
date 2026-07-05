package Selenium_Practise;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class LaunchBrowser_syntax {
	private static WebDriver driver;

	@Test
	public static void launchApp() {
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
		
		driver.manage().deleteAllCookies();
		driver.get("https://www.google.com");

		WebElement googleSearchBox = driver.findElement(By.xpath("//textarea[@id=\"APjFqb\"]"));
		googleSearchBox.sendKeys("Amazon India");
		
		List<WebElement> googleSuggestions = driver.findElements(By.xpath("//div[@class=\"erkvQe\"]//li"));
		System.out.println(googleSuggestions.size());
		for(WebElement suggestion : googleSuggestions) {
			String s = suggestion.getText();
			System.out.println(s);
		}
	}

	@AfterMethod
	public void tearDown() throws InterruptedException {
		Thread.sleep(3000);
		driver.quit();
	}

}
