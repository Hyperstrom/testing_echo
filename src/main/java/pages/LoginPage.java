package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {

	WebDriver driver;

	@FindBy(name = "username")
	private WebElement usernameElement;

	@FindBy(name = "password")
	private WebElement passwordElement;

	@FindBy(xpath = "//*[@id=\"root\"]/div/div/div/div[1]/a/button")
	private WebElement registerElement;

	@FindBy(xpath = "//*[@id=\"root\"]/div/div/div/div[2]/form/button")
	private WebElement loginElement;

	public LoginPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public void setUserName(String userName) {
		usernameElement.sendKeys(userName);
	}

	public void setPassword(String password) {
		passwordElement.sendKeys(password);
	}

	public void login(String username, String password) {
		usernameElement.sendKeys(username);
		passwordElement.sendKeys(password);
		loginElement.click();
	}

	public void goToRegistrationPage() {
		registerElement.click();

	}

}
