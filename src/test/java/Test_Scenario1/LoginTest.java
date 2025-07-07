package Test_Scenario1;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
// import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import pages.HomePage;
import pages.LoginPage;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LoginTest {

    WebDriver driver;
    LoginPage loginPage;
    HomePage homePage; // Instantiated to represent the state after login
    private final int WAIT_TIMEOUT_SECONDS = 15;

    private final By PROFILEIMAGE = By.xpath("//*[@id=\"root\"]/div/div/div[2]/div[1]/div/div/a[1]/div/img");
    private final By ERROR_MESSAGE_XPATH = By.xpath("//*[@id=\"root\"]/div/div/div/div[2]/form/span[@class='error']");

    @BeforeMethod
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize(); // Maximize the browser window
        String url = "http://localhost:3000/login";
        driver.get(url);

        // Initialize page objects
        loginPage = new LoginPage(driver);
        homePage = new HomePage(driver); // Initialize HomePage as well, though its methods aren't used for actions here
    }
    @Test(description = "Verify multiple error messages for empty username || Test Case ID TC_003")
    public void testEmptyUsernameShowsMultipleErrorMessages() {
        System.out.println("Starting negative test: testEmptyUsernameShowsMultipleErrorMessages");

        // Step 1: Input empty username and a valid password
        // The login method in LoginPage handles clicking the button after setting credentials
        loginPage.login("", "abcd@1234"); // This will re-enter username/password and click

        System.out.println("Login attempted with empty username...");
        System.out.println("Waiting for error messages to appear...");

        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIMEOUT_SECONDS));

            // Wait for ALL error message elements to be visible
            List<WebElement> errorMessageElements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(ERROR_MESSAGE_XPATH));

            // Define the expected error messages based on the HTML structure and common validation
            List<String> expectedErrorMessages = Arrays.asList(
                    "Username is required.",
                    "Please fill in all required fields."
            );

            // Extract the actual text from the found elements
            List<String> actualErrorMessages = errorMessageElements.stream()
                    .map(WebElement::getText)
                    .toList();

            System.out.println("Found error messages: " + actualErrorMessages);

            // Assert that the correct number of error messages are displayed
            Assert.assertEquals(actualErrorMessages.size(), expectedErrorMessages.size(),
                    "Number of error messages mismatch.");

            // Assert that all expected messages are present in the actual messages
            // We'll sort both lists to ensure order doesn't matter for comparison
            List<String> sortedExpected = expectedErrorMessages.stream().sorted().collect(Collectors.toList());
            List<String> sortedActual = actualErrorMessages.stream().sorted().collect(Collectors.toList());

            Assert.assertEquals(sortedActual, sortedExpected,
                    "Error message text mismatch for empty username scenario.");

            System.out.println("Negative test passed: Correct error messages displayed for empty username.");

        } catch (org.openqa.selenium.TimeoutException e) {
            Assert.fail("Negative test failed: Error messages did not appear within " + WAIT_TIMEOUT_SECONDS + " seconds for empty username. " + e.getMessage());
        } catch (Exception e) {
            Assert.fail("An unexpected error occurred during empty username test: " + e.getMessage());
        }
    }

    @Test(description = "Verify error message for invalid login credentials || Test Case ID TC_009")
    public void testInvalidLoginShowsErrorMessage() {
        System.out.println("Starting negative test: testInvalidLoginShowsErrorMessage");

        // Step 1: Input username and wrong password using LoginPage methods
        System.out.println("Entering username and and wrong password...");
        loginPage.login("aditya10", "Abc@1234"); // This will re-enter username/password and click
        System.out.println("Waiting for error message to appear...");

        try{
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIMEOUT_SECONDS)); // Using the general timeout
            WebElement errorMessageElement = wait.until(ExpectedConditions.visibilityOfElementLocated(ERROR_MESSAGE_XPATH));

            String actualErrorMessage = errorMessageElement.getText();
            System.out.println("Found error message: " + actualErrorMessage);

            // Assert that the error message is displayed
            Assert.assertTrue(errorMessageElement.isDisplayed(), "Error message element is not displayed.");

            // Optional: Assert that the error message contains expected text
            // You'll need to know what the actual error message text is for invalid credentials
            // For example: "Invalid credentials", "Username or password incorrect", etc.
            String expectedPartialErrorMessage = "Login failed. Please check your credentials."; // REPLACE with the actual expected error message text
            Assert.assertTrue(actualErrorMessage.contains(expectedPartialErrorMessage),
                    "Error message text mismatch. Expected to contain '" + expectedPartialErrorMessage + "' but got '" + actualErrorMessage + "'");

            System.out.println("Negative test passed: Error message displayed correctly for invalid login.");


        }catch (org.openqa.selenium.TimeoutException e) {
            // This block will execute if the error message doesn't appear within the timeout
            Assert.fail("Negative test failed: Error message did not appear within " + WAIT_TIMEOUT_SECONDS + " seconds. " + e.getMessage());
        } catch (Exception e) {
            // Catch any other unexpected exceptions during the verification step
            Assert.fail("An unexpected error occurred during invalid login test: " + e.getMessage());
        }
    }

    @Test(description = "Verify multiple error messages for empty password || Test Case ID TC_005")
    public void testEmptyPasswordShowsMultipleErrorMessages() {
        System.out.println("Starting negative test: testEmptyPasswordShowsMultipleErrorMessages");

        // Step 1: Input username and empty password
        System.out.println("Entering username and and empty password...");
        // The login method in LoginPage handles clicking the button after setting credentials
        loginPage.login("aditya10", ""); // This will re-enter username/password and click

        System.out.println("Login attempted with empty password...");
        System.out.println("Waiting for error messages to appear...");

        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIMEOUT_SECONDS));

            // Wait for ALL error message elements to be visible
            List<WebElement> errorMessageElements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(ERROR_MESSAGE_XPATH));

            // Define the expected error messages based on the HTML snippet
            List<String> expectedErrorMessages = Arrays.asList(
                    "Password is required.",
                    "Please fill in all required fields."
            );

            // Extract the actual text from the found elements
            List<String> actualErrorMessages = errorMessageElements.stream()
                    .map(WebElement::getText)
                    .toList();

            System.out.println("Found error messages: " + actualErrorMessages);

            // Assert that the correct number of error messages are displayed
            Assert.assertEquals(actualErrorMessages.size(), expectedErrorMessages.size(),
                    "Number of error messages mismatch.");

            // Assert that all expected messages are present in the actual messages
            // We'll sort both lists to ensure order doesn't matter for comparison
            List<String> sortedExpected = expectedErrorMessages.stream().sorted().collect(Collectors.toList());
            List<String> sortedActual = actualErrorMessages.stream().sorted().collect(Collectors.toList());

            Assert.assertEquals(sortedActual, sortedExpected,
                    "Error message text mismatch for empty password scenario.");

            System.out.println("Negative test passed: Correct error messages displayed for empty password.");

        } catch (org.openqa.selenium.TimeoutException e) {
            Assert.fail("Negative test failed: Error messages did not appear within " + WAIT_TIMEOUT_SECONDS + " seconds for empty password. " + e.getMessage());
        } catch (Exception e) {
            Assert.fail("An unexpected error occurred during empty password test: " + e.getMessage());
        }
    }

    @Test(description = "Verify successful user login with valid credentials || Test Case ID TC_010")
    public void testSuccessfulLogin() {
        System.out.println("Starting test: testSuccessfulLogin");

        // Step 1: Input username and password using LoginPage methods
        System.out.println("Entering username and password...");
        loginPage.login("aditya10", "Aditya@7157"); // This will re-enter username/password and click
        System.out.println(" login to the system ...");
        // Make sure to use the correct credentials here.


        // Step 2: Verify successful login
        // After clicking login, the page should transition to the home page.
        // We'll wait for a specific element on the home page (e.g., profileElement) to become visible.
        // The XPath for profileElement is "//*[@id="root"]/div/div/div[2]/div[1]/div/div/a[1]/div/img"
        System.out.println("Waiting for home page elements to appear...");
        // Define a timeout for explicit waits (e.g., 15 seconds)
        int WAIT_TIMEOUT_SECONDS = 15;
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIMEOUT_SECONDS));
            // Wait for the profile element to be visible on the new page
            WebElement profileIcon = wait.until(ExpectedConditions.visibilityOfElementLocated(PROFILEIMAGE));

            // Assert that the profile icon is displayed, indicating successful login
            Assert.assertTrue(profileIcon.isDisplayed(), "Login failed: Profile icon not displayed on Home Page.");
            System.out.println("Login successful! Profile icon is displayed.");

            // Optional: You could also assert the URL change
            // Assert.assertTrue(driver.getCurrentUrl().contains("home"), "URL does not contain 'home' after successful login.");

        } catch (org.openqa.selenium.TimeoutException e) {
            // This block will execute if the profile element doesn't appear within the timeout
            Assert.fail("Login failed: Home page element (profile icon) did not become visible within " + WAIT_TIMEOUT_SECONDS + " seconds. " + e.getMessage());
        } catch (Exception e) {
            // Catch any other unexpected exceptions during the verification step
            Assert.fail("An unexpected error occurred during login verification: " + e.getMessage());
        }
    }





    @AfterMethod
    public void tearDown() {
        System.out.println("Tearing down WebDriver...");
        if (driver != null) {
            driver.quit(); // Close the browser and end the WebDriver session
        }
        System.out.println("WebDriver closed.");
    }
}
