package config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class DriverSetup {
	
	private static WebDriver driver;
	private static final String baseUrl = "http://localhost:3000/login";
	
	public static WebDriver getDriver() {
		driver = new ChromeDriver();
		driver.manage().window().maximize();;
		driver.get(baseUrl);
		return driver;
	}

}
