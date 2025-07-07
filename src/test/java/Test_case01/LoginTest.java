package Test_case01;

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

public class LoginTest {

    WebDriver driver;
    LoginPage loginPage;
    HomePage homePage; // Instantiated to represent the state after login
    private final By PROFILEIMAGE = By.xpath("//*[@id=\"root\"]/div/div/div[2]/div[1]/div/div/a[1]/div/img");

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

    @Test(description = "Verify successful user login with valid credentials")
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
