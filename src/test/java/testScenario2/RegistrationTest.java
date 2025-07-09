package testScenario2;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.List;
import java.util.UUID;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import pages.LoginPage;
import pages.RegistrationPage;

public class RegistrationTest {

	WebDriver driver;
	LoginPage loginPage;
	RegistrationPage registerPage;

	@BeforeMethod
	public void setup() {
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		String url = "http://localhost:3000/login";
		driver.get(url);
		loginPage = new LoginPage(driver);
		registerPage = new RegistrationPage(driver);
		loginPage.goToRegistrationPage();
	}

	@Test(description = "Verify all the Elements are present", groups = { "ui", "sanity", "smoke" })
	public void testUIForRegistrationPage() {
		SoftAssert softAssert = new SoftAssert();
		softAssert.assertTrue(registerPage.isRegistrationPageDisplayed(), "Registration page itself is not displayed.");

		softAssert.assertTrue(registerPage.isElementDisplayed(RegistrationPage.USERNAME_INPUT_LOCATOR),
				"Username input field is not displayed");
		softAssert.assertTrue(registerPage.isElementDisplayed(RegistrationPage.EMAIL_INPUT_LOCATOR),
				"Email input field is not displayed");
		softAssert.assertTrue(registerPage.isElementDisplayed(RegistrationPage.PASSWORD_INPUT_LOCATOR),
				"Password input field is not displayed");
		softAssert.assertTrue(registerPage.isElementDisplayed(RegistrationPage.NAME_INPUT_LOCATOR),
				"Name input field is not displayed");

		softAssert.assertEquals(registerPage.getElementText(RegistrationPage.REGISTER_BUTTON_LOCATOR), "Register",
				"Register button text is incorrect or not displayed");
		softAssert.assertEquals(registerPage.getElementText(RegistrationPage.LOGIN_BUTTON_LOCATOR), "Login",
				"Login button text is incorrect or not displayed");
		softAssert.assertAll();
	}

	@Test(description = "Verify successful registration possible using correct credentials", groups = { "regression",
			"positive", "smoke" })
	public void testSuccessfulRegistration() {
		String uniqueUsername = "user_" + UUID.randomUUID().toString().substring(0, 8);
		String uniqueEmail = "email_" + UUID.randomUUID().toString().substring(0, 8) + "@example.com";

		registerPage.register(uniqueUsername, uniqueEmail, "Aditya@7157", "Aditya");

		assertTrue(loginPage.isLoginFormDisplayed(),
				"Registration Failed: Login Page is not displayed after successful registration.");
	}

	@Test(description = "Verify that error message is displayed when no credentials are passed", groups = {
			"regression", "negative", "smoke" })
	public void testEmptyCredentials() {
		registerPage.register("", "", "", "");

		List<String> actualErrorText = registerPage.getAllGeneralErrorMessages();

		List<String> expectedErrors = List.of("Username is required.", "Email is required.", "Password is required.",
				"Name is required.", "Please correct the highlighted errors.");
		assertEquals(actualErrorText, expectedErrors, "The error messages for empty credentials are not as expected.");
	}

	@Test(description = "Validate error message when username field is empty.", groups = { "regression", "negative" })
	public void testUserNameFieldIsEmpty() {
		registerPage.register("", "ad@gmail.com", "Aditya@7157", "Aditya");
		assertEquals(registerPage.getErrorMessageText(RegistrationPage.USERNAME_ERROR_LOCATOR), "Username is required.",
				"Error message for empty username is not displayed correctly.");
	}

	@Test(description = "Validate error message when name field is empty.", groups = { "regression", "negative" })
	public void testNameFieldIsEmpty() {
		registerPage.register("adi", "ad@gmail.com", "Aditya@7157", "");
		assertEquals(registerPage.getErrorMessageText(RegistrationPage.NAME_ERROR_LOCATOR), "Name is required.",
				"Error message for empty name is not displayed correctly.");
	}

	@Test(description = "Validate error message when password field is empty", groups = { "regression", "negative" })
	public void testPasswordFieldIsEmpty() {
		registerPage.register("adi", "ad@gmail.com", "", "Aditya");
		assertEquals(registerPage.getErrorMessageText(RegistrationPage.PASSWORD_ERROR_LOCATOR), "Password is required.",
				"Error message for empty password is not displayed correctly.");
	}

	@Test(description = "Validate error message when email field is empty")
	public void testEmailFieldIsEmpty() {
		registerPage.register("adi", "", "Aditya@7157", "Aditya");
		assertEquals(registerPage.getErrorMessageText(RegistrationPage.EMAIL_ERROR_LOCATOR), "Email is required.",
				"Error message for empty email is not displayed correctly.");
	}

	@Test(description = "Validate error message when email field is invalid", groups = { "regression", "negative" })
	public void testValidEmailField() {
		registerPage.register("adi", "adi@.com", "Aditya@7157", "Aditya");
		assertEquals(registerPage.getErrorMessageText(RegistrationPage.EMAIL_ERROR_LOCATOR),
				"Email address is invalid.", "Error message for invalid email format is not displayed correctly.");
	}

	@Test(description = "Validate error message when existing username.", groups = { "regression", "negative" })
	public void testExistingUsername() {
		registerPage.register("aditya10", "unique_" + UUID.randomUUID().toString().substring(0, 8) + "@example.com",
				"Aditya@7157", "Aditya");
		assertEquals(registerPage.getErrorMessageText(RegistrationPage.USERNAME_ERROR_LOCATOR),
				"This username already exists.", "Error message for existing username is not displayed correctly.");
	}

	@Test(description = "Validate error message when existing email.", groups = { "regression", "negative" })
	public void testExistingEmail() {
		registerPage.register("unique_" + UUID.randomUUID().toString().substring(0, 8), "adi@gmail.com", "Aditya@7157",
				"Aditya");
		assertEquals(registerPage.getErrorMessageText(RegistrationPage.EMAIL_ERROR_LOCATOR),
				"This email already exists.", "Error message for existing email is not displayed correctly.");
	}

	@Test(description = "Validate password is accepted if Password contains underscore \"_\"", groups = { "regression",
			"positive" })
	public void testGivingUnderScoreAsPassword() {
		String uniqueUsername = "user_" + UUID.randomUUID().toString().substring(0, 8);
		String uniqueEmail = "email_" + UUID.randomUUID().toString().substring(0, 8) + "@example.com";

		registerPage.register(uniqueUsername, uniqueEmail, "Abcd_123", "Aditya");
		assertTrue(loginPage.isLoginFormDisplayed(),
				"Registration Failed: Underscore ('_') should be accepted as a special character in password.");
	}

	@Test(description = "Validate error message when name field is numeric only", groups = { "regression", "negative" })
	public void testGivingNumericValueAsName() {
		registerPage.register("adit01", "adet@gmail.com", "Abcd@123", "12345678");
		assertEquals(registerPage.getErrorMessageText(RegistrationPage.NAME_ERROR_LOCATOR),
				"Name can not contain numeric value", "Error message for numeric name is not displayed correctly.");
	}
	
	@Test(description = "Validate user is able to register when username is longer than 16 characters.")
	public void testLengthOfUsername() {
		String uniqueUsername = "user_" + UUID.randomUUID().toString().substring(0, 20);
		String uniqueEmail = "email_" + UUID.randomUUID().toString().substring(0, 8) + "@example.com";
		registerPage.register(uniqueUsername, uniqueEmail, "Aditya@7157", "Aditya");
		assertTrue(loginPage.isLoginFormDisplayed(),
				"Registration Failed: Login Page is not displayed after successful registration.");
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