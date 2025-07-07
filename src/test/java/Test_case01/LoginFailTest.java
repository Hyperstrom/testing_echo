package Test_case01;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;

import java.time.Duration;

public class LoginFailTest {

    WebDriver driver;
    LoginPage loginPage;
    HomePage homePage; // Instantiated to represent the state after login

    // Define a timeout for explicit waits (e.g., 15 seconds)
    private final int WAIT_TIMEOUT_SECONDS = 15;

    // XPath for the error message element as provided by the user
    private final By ERROR_MESSAGE_XPATH = By.xpath("//*[@id=\"root\"]/div/div/div/div[2]/form/span");


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

    @Test(description = "Verify error message for invalid login credentials")
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

    @AfterMethod
    public void tearDown() {
        System.out.println("Tearing down WebDriver...");
        if (driver != null) {
            driver.quit(); // Close the browser and end the WebDriver session
        }
        System.out.println("WebDriver closed.");
    }

}
