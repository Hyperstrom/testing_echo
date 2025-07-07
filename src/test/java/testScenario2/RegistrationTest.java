package testScenario2;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import pages.LoginPage;
import pages.RegistrationPage;

public class RegistrationTest {

	WebDriver driver;
	LoginPage loginPage;
	RegistrationPage registerPage;
	private final int WAIT_TIMEOUT_SECONDS = 15;

	private final By USERNAME_ERROR_LOCATOR = By
			.xpath("//input[@name='username']/following-sibling::span[@class='error']");
	private final By EMAIL_ERROR_LOCATOR = By.xpath("//input[@name='email']/following-sibling::span[@class='error']");
	private final By PASSWORD_ERROR_LOCATOR = By
			.xpath("//input[@name='password']/following-sibling::span[@class='error']");
	private final By NAME_ERROR_LOCATOR = By.xpath("//input[@name='name']/following-sibling::span[@class='error']");
	private final By loginForm = By.xpath("//*[@id='root']/div/div[@class='login']");
	
	@BeforeMethod
	public void setup() {
		driver = new ChromeDriver();
		driver.manage().window().maximize(); // Maximize the browser window
		String url = "http://localhost:3000/login";
		driver.get(url);
		loginPage = new LoginPage(driver);
		registerPage = new RegistrationPage(driver);
		loginPage.goToRegistrationPage();
	}

	@Test(description = "Verify successful registration possible using correct credentials")
	public void testSuccessfulRegistration() {
		registerPage.register("adi10", "adex@gmail.com", "Aditya@7157", "Aditya");
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIMEOUT_SECONDS));
			WebElement login = wait.until(ExpectedConditions.visibilityOfElementLocated(loginForm));
			assertTrue(login.isDisplayed(), "Registration Failed: Login Page is not displayed");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test(description = "Verify that error message is displayed when no credentials are passed")
	public void testEmptyCredentials() {
		registerPage.register("", "", "", "");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIMEOUT_SECONDS));
		List<WebElement> errorEles = driver.findElements(By.xpath("//*[@id=\"root\"]/div/div/div/div[2]/form/span"));
		wait.until(ExpectedConditions.visibilityOfAllElements(errorEles));

		List<String> errorText = new ArrayList<>();
		for (int i = 0; i < errorEles.size(); i++) {
			errorText.add(errorEles.get(i).getText());
		}

		List<String> expectedErrors = List.of("Username is required.", "Email is required.", "Password is required.",
				"Name is required.", "Please correct the highlighted errors.");
		assertEquals(errorText, expectedErrors, "The error messages are not as expected.");

	}

	@Test(description = "Validate error message when username field is empty.")
	public void testUserNameFieldIsEmpty() {
		registerPage.register("", "ad@gmail.com", "Aditya@7157", "Aditya");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIMEOUT_SECONDS));
		WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(USERNAME_ERROR_LOCATOR));
		assertEquals(error.getText(), "Username is required.", "Error message is not displayed when username is empty");

	}

	@Test(description = "Validate error message when name field is empty.")
	public void testNameFieldIsEmpty() {
		registerPage.register("adi", "ad@gmail.com", "Aditya@7157", "");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIMEOUT_SECONDS));
		WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(NAME_ERROR_LOCATOR));
		assertEquals(error.getText(), "Name is required.", "Error message is not displayed when name is empty");

	}

	@Test(description = "Validate error message when password field is empty")
	public void testPasswordFieldIsEmpty() {
		registerPage.register("adi", "ad@gmail.com", "", "Aditya");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIMEOUT_SECONDS));
		WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(PASSWORD_ERROR_LOCATOR));
		assertEquals(error.getText(), "Password is required.", "Error message is not displayed when password is empty");

	}

	@Test(description = "Validate error message when email field is empty")
	public void testEmailFieldIsEmpty() {
		registerPage.register("adi", "", "Aditya@7157", "Aditya");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIMEOUT_SECONDS));
		WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(EMAIL_ERROR_LOCATOR));
		assertEquals(error.getText(), "Email is required.", "Error message is not displayed when email is empty");

	}

	@Test(description = "Validate error message when email field is invalid")
	public void testValidEmailField() {
		registerPage.register("adi", "adi@.com", "Aditya@7157", "Aditya");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIMEOUT_SECONDS));
		By regError = By.xpath("//*[@id=\"root\"]/div/div/div/div[2]/form/span[1]");
		WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(regError));
		assertEquals(error.getText(), "Email address is invalid.",
				"Error message is not displayed when email is invalid");

	}

	@Test(description = "Validate error message when existing username.")
	public void testExistingUsername() {
		registerPage.register("aditya10", "ad@gmail.com", "Aditya@7157", "Aditya");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIMEOUT_SECONDS));
		By regError = By.xpath("//*[@id=\"root\"]/div/div/div/div[2]/form/span[1]");
		WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(regError));
		assertEquals(error.getText(), "This username already exists.",
				"Error message is not displayed when email is invalid");

	}

	@Test(description = "Validate error message when existing email.")
	public void testExistingEmail() {
		registerPage.register("adi10", "adi@gmail.com", "Aditya@7157", "Aditya");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIMEOUT_SECONDS));
		By regError = By.xpath("//*[@id=\"root\"]/div/div/div/div[2]/form/span[1]");
		WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(regError));
		assertEquals(error.getText(), "This email already exists.",
				"Error message is not displayed when email is invalid");

	}
	
	@Test(description = "Validate password is accepted if Password contains underscore \"_\"")
	public void testGivingUnderScoreAsPassword() {
		registerPage.register("adi10", "adex@gmail.com", "Abcd_123", "Aditya");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIMEOUT_SECONDS));
		WebElement login = wait.until(ExpectedConditions.visibilityOfElementLocated(loginForm));
		assertTrue(login.isDisplayed(), "Registration Failed: \"_\" is not taken as special character");
	}

	@AfterMethod
	public void tearDown() {
		System.out.println("Tearing down WebDriver...");
		if (driver != null) {
			driver.quit();
		}
		System.out.println("WebDriver closed.");
	}
}
