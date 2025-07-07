package pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

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
	
	private WebDriverWait wait;
	public static final By LOGIN_FORM_LOCATOR = By.xpath("//*[@id='root']/div/div[@class='login']");

	public LoginPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
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
	
	public boolean isLoginFormDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(LOGIN_FORM_LOCATOR)).isDisplayed();
        } catch (org.openqa.selenium.TimeoutException e) {
            return false;
        }
    }

}
