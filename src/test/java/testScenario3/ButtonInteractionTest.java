package testScenario3;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;

import java.time.Duration;
import java.util.Objects;
import java.util.function.Function;

public class ButtonInteractionTest {
    WebDriver driver;
    LoginPage loginPage;
    HomePage homePage;

    private final int WAIT_TIMEOUT_SECONDS = 15;
    private final int SHORT_WAIT_SECONDS = 5; // Shorter wait for immediate changes like URL (used with FluentWait timeout)
    private final int POLLING_INTERVAL_MS = 500; // Polling interval for FluentWait

    // XPath for the profile image on the home page (used to verify successful login in setup)
    private final By PROFILE_IMAGE_ON_HOMEPAGE = By.xpath("//*[@id=\"root\"]/div/div/div[2]/div[1]/div/div/a[1]/div/img");


    // NEW XPaths for Add Place and Tag Friends buttons
    private final By ADD_PLACE_BUTTON_XPATH = By.xpath("//*[@id=\"root\"]/div/div/div[2]/div[2]/div/div[2]/div/div[2]/div[1]/div[1]");
    private final By TAG_FRIENDS_BUTTON_XPATH = By.xpath("//*[@id=\"root\"]/div/div/div[2]/div[2]/div/div[2]/div/div[2]/div[1]/div[2]");

    @BeforeMethod
    public void setup() {
        System.out.println("Setting up WebDriver and performing login for StatusUpdateValidationTest...");
        // --- IMPORTANT: Set the path to your ChromeDriver executable ---
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        String loginUrl = "http://localhost:3000/login"; // Your application's login URL
        driver.get(loginUrl);

        // Initialize page objects
        loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);

        // Perform successful login to reach the homepage
        System.out.println("Performing login to reach homepage...");
        loginPage.login("aditya10", "Aditya@7157"); // IMPORTANT: Use actual valid credentials

        // Verify successful login by waiting for a homepage element
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIMEOUT_SECONDS));
            wait.until(ExpectedConditions.visibilityOfElementLocated(PROFILE_IMAGE_ON_HOMEPAGE));
            System.out.println("Successfully logged in and reached homepage.");
        } catch (org.openqa.selenium.TimeoutException e) {
            Assert.fail("Setup failed: Could not log in or reach homepage within " + WAIT_TIMEOUT_SECONDS + " seconds. " + e.getMessage());
        }
    }

    @Test(description = " Test case ID TC_045 || Verify 'Add Place' button functionality (Expected to FAIL if no change/redirection occurs)")
    public void testAddPlaceButtonFunctionality() {
        System.out.println("Starting test: testAddPlaceButtonFunctionality (Expected to FAIL if no change/redirection occurs)");

        String initialUrl = driver.getCurrentUrl();
//        System.out.println("Initial URL: " + initialUrl);

        // 1. Click on the "Add Place" button
        System.out.println("Locating and clicking the 'Add Place' button...");
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIMEOUT_SECONDS));
            WebElement addPlaceButton = wait.until(ExpectedConditions.elementToBeClickable(ADD_PLACE_BUTTON_XPATH));
            addPlaceButton.click();
            System.out.println("'Add Place' button clicked.");
        } catch (org.openqa.selenium.TimeoutException e) {
            Assert.fail("Failed to find or click 'Add Place' button within " + WAIT_TIMEOUT_SECONDS + " seconds. " + e.getMessage());
        }

        // 2. Check if URL changed using FluentWait. If not, it means "nothing happened" (defect), so fail the test.
        System.out.println("Verifying if the URL changed after clicking 'Add Place' using FluentWait...");

        Wait<WebDriver> fluentWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(SHORT_WAIT_SECONDS)) // Total timeout for the wait
                .pollingEvery(Duration.ofMillis(POLLING_INTERVAL_MS)) // How often to check the condition
                .ignoring(NoSuchElementException.class); // Ignore this exception during polling

        try {
            // Use FluentWait to wait until the URL is NOT the initial URL.
            // If this condition becomes true, it means the URL changed (something happened).
            fluentWait.until(driver -> !Objects.equals(driver.getCurrentUrl(), initialUrl));

            // If we reach here, the URL changed, meaning something happened, so the test passes.
            System.out.println("Test Passed: URL changed from '" + initialUrl + "' to '" + driver.getCurrentUrl() + "' after clicking 'Add Place'.");

        } catch (org.openqa.selenium.TimeoutException e) {
            // If TimeoutException occurs, it means the URL remained the same within the FluentWait timeout.
            // This signifies "nothing happened" (a defect), so the test should FAIL.
            String finalUrl = driver.getCurrentUrl();
            Assert.assertEquals(finalUrl, initialUrl, "URL unexpectedly changed, but the test expected no change for failure.");
            Assert.fail("Test Failed: URL remained '" + initialUrl + "' after clicking 'Add Place'. This indicates 'nothing happened', which is a defect.");
        } catch (Exception e) {
            Assert.fail("An unexpected error occurred during 'Add Place' test: " + e.getMessage());
        }
    }

    @Test(description = " Test case ID TC_046 || Verify 'Tag Friends' button functionality (Expected to FAIL if no change/redirection occurs)")
    public void testTagFriendsButtonFunctionality() {
        System.out.println("Starting test: testTagFriendsButtonFunctionality (Expected to FAIL if no change/redirection occurs)");

        String initialUrl = driver.getCurrentUrl();
        System.out.println("Initial URL: " + initialUrl);

        // 1. Click on the "Tag Friends" button
        System.out.println("Locating and clicking the 'Tag Friends' button...");
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIMEOUT_SECONDS));
            WebElement tagFriendsButton = wait.until(ExpectedConditions.elementToBeClickable(TAG_FRIENDS_BUTTON_XPATH));
            tagFriendsButton.click();
            System.out.println("'Tag Friends' button clicked.");
        } catch (org.openqa.selenium.TimeoutException e) {
            Assert.fail("Failed to find or click 'Tag Friends' button within " + WAIT_TIMEOUT_SECONDS + " seconds. " + e.getMessage());
        }

        // 2. Check if URL changed using FluentWait. If not, it means "nothing happened" (defect), so fail the test.
        System.out.println("Verifying if the URL changed after clicking 'Tag Friends' using FluentWait...");

        Wait<WebDriver> fluentWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(SHORT_WAIT_SECONDS)) // Total timeout for the wait
                .pollingEvery(Duration.ofMillis(POLLING_INTERVAL_MS)) // How often to check the condition
                .ignoring(NoSuchElementException.class); // Ignore this exception during polling

        try {
            // Use FluentWait to wait until the URL is NOT the initial URL.
            // If this condition becomes true, it means the URL changed (something happened).
            fluentWait.until(new Function<WebDriver, Boolean>() {
                public Boolean apply(WebDriver driver) {
                    return !Objects.equals(driver.getCurrentUrl(), initialUrl);
                }
            });

            // If we reach here, the URL changed, meaning something happened, so the test passes.
            System.out.println("Test Passed: URL changed from '" + initialUrl + "' to '" + driver.getCurrentUrl() + "' after clicking 'Tag Friends'.");

        } catch (org.openqa.selenium.TimeoutException e) {
            // If TimeoutException occurs, it means the URL remained the same within the FluentWait timeout.
            // This signifies "nothing happened" (a defect), so the test should FAIL.
            String finalUrl = driver.getCurrentUrl();
            Assert.assertEquals(finalUrl, initialUrl, "URL unexpectedly changed, but the test expected no change for failure.");
            Assert.fail("Test Failed: URL remained '" + initialUrl + "' after clicking 'Tag Friends'. This indicates 'nothing happened', which is a defect.");
        } catch (Exception e) {
            Assert.fail("An unexpected error occurred during 'Tag Friends' test: " + e.getMessage());
        }
    }

    @AfterMethod
    public void tearDown() {
        System.out.println("Tearing down WebDriver for StatusUpdateValidationTest...");
        if (driver != null) {
            driver.quit(); // Close the browser and end the WebDriver session
        }
        System.out.println("WebDriver closed.");
    }
}

