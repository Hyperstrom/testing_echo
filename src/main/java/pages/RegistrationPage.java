package pages;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

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
	private WebElement registerButton;

	@FindBy(xpath = "//*[@id=\"root\"]/div/div/div/div[1]/a/button")
	private WebElement loginButton;

	private WebDriverWait wait;

	public static final By USERNAME_ERROR_LOCATOR = By
			.xpath("//input[@name='username']/following-sibling::span[@class='error']");
	public static final By EMAIL_ERROR_LOCATOR = By
			.xpath("//input[@name='email']/following-sibling::span[@class='error']");
	public static final By PASSWORD_ERROR_LOCATOR = By
			.xpath("//input[@name='password']/following-sibling::span[@class='error']");
	public static final By NAME_ERROR_LOCATOR = By
			.xpath("//input[@name='name']/following-sibling::span[@class='error']");

	public static final By ALL_FORM_ERROR_MESSAGES_LOCATOR = By.xpath("//*[@id=\"root\"]/div/div/div/div[2]/form/span");

	// Locators for the UI elements in testUIForRegistrationPage()
	public static final By USERNAME_INPUT_LOCATOR = By.name("username");
	public static final By EMAIL_INPUT_LOCATOR = By.name("email");
	public static final By PASSWORD_INPUT_LOCATOR = By.name("password");
	public static final By NAME_INPUT_LOCATOR = By.name("name");
	public static final By REGISTER_BUTTON_LOCATOR = By.xpath("//button[text()='Register']");
	public static final By LOGIN_BUTTON_LOCATOR = By.xpath("//button[text()='Login']");

	// Locator to verify the registration page itself is displayed
	// Assuming a unique element/attribute for the registration form or page div
	public static final By REGISTRATION_PAGE_TITLE_LOCATOR = By.xpath("//h1[text()='Register']");
	public static final By REGISTRATION_FORM_LOCATOR = By.xpath("//*[@id='root']/div/div[@class='register']");

	public RegistrationPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
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
		registerButton.click();
	}

	public String getErrorMessageText(By errorLocator) {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(errorLocator)).getText();
	}

	public List<String> getAllGeneralErrorMessages() {
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(ALL_FORM_ERROR_MESSAGES_LOCATOR));
		List<WebElement> errorElements = driver.findElements(ALL_FORM_ERROR_MESSAGES_LOCATOR);
		List<String> errorTexts = new ArrayList<>();
		for (WebElement element : errorElements) {
			errorTexts.add(element.getText());
		}
		return errorTexts;
	}

	public boolean isElementDisplayed(By locator) {
		try {
			return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
		} catch (org.openqa.selenium.TimeoutException e) {
			return false;
		}
	}

	public boolean isRegistrationPageDisplayed() {
		try {
			return wait.until(ExpectedConditions.visibilityOfElementLocated(REGISTRATION_FORM_LOCATOR)).isDisplayed();
		} catch (org.openqa.selenium.TimeoutException e) {
			return false;
		}
	}
	
	public String getElementText(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getText();
    }

	public void goToLoginPage() {
		loginButton.click();
	}
}
