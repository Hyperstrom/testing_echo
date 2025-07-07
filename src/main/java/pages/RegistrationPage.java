package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class RegistrationPage {
	WebDriver driver;

	@FindBy(name = "username")
	private WebElement usernameElement;

	@FindBy(name = "password")
	private WebElement passwordElement;

	@FindBy(name = "email")
	private WebElement emailElement;

	@FindBy(name = "name")
	private WebElement nameElement;

	@FindBy(xpath = "//*[@id=\"root\"]/div/div/div/div[2]/form/button")
	private WebElement registerElement;

	@FindBy(xpath = "//*[@id=\"root\"]/div/div/div/div[1]/a/button")
	private WebElement loginElement;

	public RegistrationPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public void setUserName(String userName) {
		usernameElement.sendKeys(userName);
	}

	public void setName(String name) {
		nameElement.sendKeys(name);
	}

	public void setPassword(String password) {
		passwordElement.sendKeys(password);
	}

	public void setEmail(String email) {
		emailElement.sendKeys(email);
	}

	public void register(String userName, String email, String password, String name) {
		usernameElement.sendKeys(userName);
		emailElement.sendKeys(email);
		passwordElement.sendKeys(password);
		nameElement.sendKeys(name);
		registerElement.click();
	}

	public void goToLoginPage() {
		loginElement.click();
	}
}
