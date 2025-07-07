package testScenario3;

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

import pages.LoginPage;
import pages.HomePage;

import java.time.Duration;
import java.util.UUID;


public class StatusUpdateValidationTest {

    WebDriver driver;
    LoginPage loginPage;
    HomePage homePage;

    private final int WAIT_TIMEOUT_SECONDS = 15;
    // XPath for the profile image on the home page (used to verify successful login in setup)
    private final By PROFILE_IMAGE_ON_HOMEPAGE = By.xpath("//*[@id=\"root\"]/div/div/div[2]/div[1]/div/div/a[1]/div/img");
    // Assuming a textarea for the post input
    private final By STATUS_UPDATE_INPUT_XPATH = By.xpath("//input[contains(@placeholder, \"What's on your mind\")]");
    // Assuming a button with text 'Share' or 'Post'
    private final By SHARE_BUTTON_XPATH = By.xpath("(//button[normalize-space()='Share'])[1]");
    // Assuming a span or div with an 'error' class for validation messages within the post form
    private final By STATUS_VALIDATION_MESSAGE_XPATH = By.xpath("//form[contains(@class, 'post-form')]//span[@class='error'] | //form[contains(@class, 'post-form')]//div[@class='error-message'] | //*[contains(text(),'cannot be empty') or contains(text(),'required')]");
    //post time verification
    private final By POST_TIME_XPATH = By.xpath("//*[@id=\"root\"]/div/div/div[2]/div[2]/div/div[3]/div[1]/div/div[1]/div/div/span");

    // NEW XPaths for commenting functionality
    private final By COMMENT_INPUT_BOX_XPATH = By.xpath("/html/body/div/div/div/div[2]/div[2]/div/div[3]/div[1]/div/div[4]/div[1]/input");
    private final By COMMENT_SEND_BUTTON_XPATH = By.xpath("/html/body/div/div/div/div[2]/div[2]/div/div[3]/div[1]/div/div[4]/div[1]/button");
    private final By COMMENT_TOGGLE_BUTTON_XPATH = By.xpath("/html/body/div/div/div/div[2]/div[2]/div/div[3]/div[1]/div/div[3]/div[2]"); // New XPath for the comment toggle/reveal button


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

    @Test(description = "Test Case ID TC_042 || Verify placeholder and input validation for empty status update (Expected to Fail)",priority = 4)
    public void testEmptyStatusUpdateValidation() {
        System.out.println("Starting test: testEmptyStatusUpdateValidation (Expected to Fail if no validation message appears)");

        // 1. Enter an empty string in post text input.
        System.out.println("Locating status update input field...");

        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIMEOUT_SECONDS));
            WebElement statusInput = wait.until(ExpectedConditions.elementToBeClickable(STATUS_UPDATE_INPUT_XPATH));
            statusInput.clear(); // Ensure the field is empty
            System.out.println("Status update input field located and cleared.");
        } catch (org.openqa.selenium.TimeoutException e) {
            Assert.fail("Failed to find status update input field within " + WAIT_TIMEOUT_SECONDS + " seconds. " + e.getMessage());
        }

        // 2. Click the share button
        System.out.println("Locating and clicking the 'Share' button...");

        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIMEOUT_SECONDS));
            WebElement shareButton = wait.until(ExpectedConditions.elementToBeClickable(SHARE_BUTTON_XPATH));
            shareButton.click();
            System.out.println("'Share' button clicked.");
        } catch (org.openqa.selenium.TimeoutException e) {
            Assert.fail("Failed to find or click 'Share' button within " + WAIT_TIMEOUT_SECONDS + " seconds. " + e.getMessage());
        }

        // 3. Verify validation message appears. If it does NOT appear, the test should FAIL.
        System.out.println("Verifying validation message for empty status update...");
        try {
            // Attempt to wait for the validation message to become visible.
            // If it becomes visible, the test proceeds to assert its content and passes.
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIMEOUT_SECONDS));
            WebElement validationMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(STATUS_VALIDATION_MESSAGE_XPATH));

            String actualValidationText = validationMessage.getText();
            System.out.println("Found validation message: " + actualValidationText);

            Assert.assertTrue(validationMessage.isDisplayed(), "Validation message element is not displayed.");

            String expectedPartialValidationMessage = "Post cannot be empty."; // IMPORTANT: Adjust this
            Assert.assertTrue(actualValidationText.contains(expectedPartialValidationMessage),
                    "Validation message text mismatch. Expected to contain '" + expectedPartialValidationMessage + "' but got '" + actualValidationText + "'");

            System.out.println("Test passed: Validation message displayed as expected (Input rejected).");

        } catch (org.openqa.selenium.TimeoutException e) {
            // This block is executed if the validation message does NOT appear within the timeout.
            // This signifies that "nothing happened" (no error message) and the post might have been uploaded.
            // As per your requirement, this scenario should cause the test to FAIL.
            Assert.fail("Test Failed: Validation message for empty status update did not appear within " + WAIT_TIMEOUT_SECONDS + " seconds. " +
                    "This indicates the post might have been uploaded without validation, which is a defect.");
        } catch (Exception e) {
            Assert.fail("An unexpected error occurred during empty status update test: " + e.getMessage());
        }
    }


    @Test(description = "Verify posting text and its visibility on the timeline",priority = 3)
    public void testPostTextAndVisibility() {
        System.out.println("Starting test: testPostTextAndVisibility");

        String postText = "This is first post from testing side";

        // 1. Locate the status update input field and enter text
        System.out.println("Locating status update input field and entering text: \"" + postText + "\"");
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIMEOUT_SECONDS));
            WebElement statusInput = wait.until(ExpectedConditions.elementToBeClickable(STATUS_UPDATE_INPUT_XPATH));
            statusInput.clear(); // Ensure the field is empty before typing
            statusInput.sendKeys(postText);
            System.out.println("Text entered into status update field.");
        } catch (org.openqa.selenium.TimeoutException e) {
            Assert.fail("Failed to find status update input field within " + WAIT_TIMEOUT_SECONDS + " seconds. " + e.getMessage());
        }

        // 2. Click the share button
        System.out.println("Locating and clicking the 'Share' button...");
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIMEOUT_SECONDS));
            WebElement shareButton = wait.until(ExpectedConditions.elementToBeClickable(SHARE_BUTTON_XPATH));
            shareButton.click();
            System.out.println("'Share' button clicked.");
        } catch (org.openqa.selenium.TimeoutException e) {
            Assert.fail("Failed to find or click 'Share' button within " + WAIT_TIMEOUT_SECONDS + " seconds. " + e.getMessage());
        }

        // 3. Verify the posted text is present in the specified div
        System.out.println("Verifying if the posted text is visible on the timeline...");
        try {
            // Dynamically construct the XPath using the postText variable
            By dynamicPostedTextXpath = By.xpath("//p[normalize-space()='" + postText + "'][1]");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIMEOUT_SECONDS));
            WebElement postedTextElement = wait.until(ExpectedConditions.visibilityOfElementLocated(dynamicPostedTextXpath));

            Assert.assertTrue(postedTextElement.isDisplayed(), "Posted text element is not displayed on the timeline.");

            // Optional: Also verify the text content matches exactly
            Assert.assertEquals(postedTextElement.getText().trim(), postText,
                    "Actual posted text does not match expected text.");

            System.out.println("Test Passed: The text \"" + postText + "\" was successfully posted and is visible on the timeline.");

        } catch (org.openqa.selenium.TimeoutException e) {
            Assert.fail("Test Failed: Posted text \"" + postText + "\" did not appear on the timeline within " + WAIT_TIMEOUT_SECONDS + " seconds. " + e.getMessage());
        } catch (Exception e) {
            Assert.fail("An unexpected error occurred during post text visibility test: " + e.getMessage());
        }
    }

    @Test(description = "Verify post time is displayed as 'a few seconds ago'",priority = 1)
    public void testPostTimeVisibilityAndContent() {
        System.out.println("Starting test: testPostTimeVisibilityAndContent");

        // Generate a unique post text to ensure we're looking at a fresh post
        String postText = "Test Post with Time Check - " + UUID.randomUUID().toString().substring(0, 8);

        // 1. Locate the status update input field and enter text
        System.out.println("Locating status update input field and entering text: \"" + postText + "\"");
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIMEOUT_SECONDS));
            WebElement statusInput = wait.until(ExpectedConditions.elementToBeClickable(STATUS_UPDATE_INPUT_XPATH));
            statusInput.clear();
            statusInput.sendKeys(postText);
            System.out.println("Text entered into status update field.");
        } catch (org.openqa.selenium.TimeoutException e) {
            Assert.fail("Failed to find status update input field within " + WAIT_TIMEOUT_SECONDS + " seconds. " + e.getMessage());
        }

        // 2. Click the share button
        System.out.println("Locating and clicking the 'Share' button...");
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIMEOUT_SECONDS));
            WebElement shareButton = wait.until(ExpectedConditions.elementToBeClickable(SHARE_BUTTON_XPATH));
            shareButton.click();
            System.out.println("'Share' button clicked.");
        } catch (org.openqa.selenium.TimeoutException e) {
            Assert.fail("Failed to find or click 'Share' button within " + WAIT_TIMEOUT_SECONDS + " seconds. " + e.getMessage());
        }

        // 3. Verify the posted text is present (optional, but good practice to ensure the post itself is there)
        System.out.println("Verifying if the posted text is visible on the timeline (pre-check for time)...");
        try {
            // Corrected XPath: Added single quotes around the dynamic text
            By dynamicPostedTextXpath = By.xpath("//p[normalize-space()='" + postText + "'][1]");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIMEOUT_SECONDS));
            wait.until(ExpectedConditions.visibilityOfElementLocated(dynamicPostedTextXpath));
            System.out.println("Posted text confirmed visible.");
        } catch (org.openqa.selenium.TimeoutException e) {
            Assert.fail("Pre-check failed: Posted text did not appear on timeline. Cannot verify time. " + e.getMessage());
        }

        // 4. Verify the time of the post
        System.out.println("Verifying the time of the post...");
        String expectedTimeText = "a few seconds ago";
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIMEOUT_SECONDS));
            WebElement postTimeElement = wait.until(ExpectedConditions.visibilityOfElementLocated(POST_TIME_XPATH));

            String actualTimeText = postTimeElement.getText().trim();
            System.out.println("Found post time: \"" + actualTimeText + "\"");

            // Assert that the actual time text matches the expected text
            // The test will FAIL if it's not "a few seconds ago"
            Assert.assertEquals(actualTimeText, expectedTimeText,
                    "Post time mismatch. Expected \"" + expectedTimeText + "\" but got \"" + actualTimeText + "\"");

            System.out.println("Test Passed: Post time is displayed as \"" + expectedTimeText + "\".");

        } catch (org.openqa.selenium.TimeoutException e) {
            Assert.fail("Test Failed: Post time element did not appear or was not visible within " + WAIT_TIMEOUT_SECONDS + " seconds. " + e.getMessage());
        } catch (Exception e) {
            Assert.fail("An unexpected error occurred during post time verification: " + e.getMessage());
        }
    }

    @Test(description = "Verify commenting on the first post and its visibility",priority = 2)
    public void testCommentingOnFirstPost() {
        System.out.println("Starting test: testCommentingOnFirstPost");

        // We assume there is at least one post already present on the timeline
        // from previous tests or application state.
        // If no post exists, the test will likely fail when trying to find the comment box.

        // Define the comment text
        String commentText = "My comment on first post - " + UUID.randomUUID().toString().substring(0, 6);
        System.out.println("Attempting to post comment: \"" + commentText + "\" on the first post.");

        // NEW STEP: Click the element to reveal/activate the comment input box
        System.out.println("Clicking the comment toggle/reveal button...");
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIMEOUT_SECONDS));
            WebElement commentToggleButton = wait.until(ExpectedConditions.elementToBeClickable(COMMENT_TOGGLE_BUTTON_XPATH));
            commentToggleButton.click();
            System.out.println("Comment toggle/reveal button clicked.");
        } catch (org.openqa.selenium.TimeoutException e) {
            Assert.fail("Failed to find or click the comment toggle/reveal button within " + WAIT_TIMEOUT_SECONDS + " seconds. " + e.getMessage());
        }

        // 1. Locate the comment input box for the FIRST post and enter the comment text
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIMEOUT_SECONDS));
            WebElement commentInputBox = wait.until(ExpectedConditions.elementToBeClickable(COMMENT_INPUT_BOX_XPATH));
            commentInputBox.clear(); // Ensure the field is empty
            commentInputBox.sendKeys(commentText);
            System.out.println("Comment text entered into input box for the first post.");
        } catch (org.openqa.selenium.TimeoutException e) {
            Assert.fail("Failed to find or interact with comment input box for the first post within " + WAIT_TIMEOUT_SECONDS + " seconds. " + e.getMessage());
        }

        // 2. Locate and click the send button for the comment on the FIRST post
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIMEOUT_SECONDS));
            WebElement commentSendButton = wait.until(ExpectedConditions.elementToBeClickable(COMMENT_SEND_BUTTON_XPATH));
            commentSendButton.click();
            System.out.println("Comment send button clicked for the first post.");
        } catch (org.openqa.selenium.TimeoutException e) {
            Assert.fail("Failed to find or click comment send button for the first post within " + WAIT_TIMEOUT_SECONDS + " seconds. " + e.getMessage());
        }

        // 3. Verify the posted comment text is present on the FIRST post
        System.out.println("Verifying if the posted comment is visible on the first post...");
        try {
            // Dynamically construct the XPath for the displayed comment text
            // This XPath assumes the comment text is directly within a <p> tag as per your provided XPath structure for comments
            By dynamicCommentTextDisplayedXpath = By.xpath("//p[normalize-space()='" + commentText + "']");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIMEOUT_SECONDS));
            WebElement postedCommentElement = wait.until(ExpectedConditions.visibilityOfElementLocated(dynamicCommentTextDisplayedXpath));

            Assert.assertTrue(postedCommentElement.isDisplayed(), "Posted comment element is not displayed on the first post's timeline.");

            // Optional: Also verify the text content matches exactly
            Assert.assertEquals(postedCommentElement.getText().trim(), commentText,
                    "Actual posted comment text does not match expected comment text on the first post.");

            System.out.println("Test Passed: The comment \"" + commentText + "\" was successfully posted and is visible on the first post.");

        } catch (org.openqa.selenium.TimeoutException e) {
            Assert.fail("Test Failed: Posted comment \"" + commentText + "\" did not appear on the first post's timeline within " + WAIT_TIMEOUT_SECONDS + " seconds. " + e.getMessage());
        } catch (Exception e) {
            Assert.fail("An unexpected error occurred during comment visibility test on the first post: " + e.getMessage());
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

