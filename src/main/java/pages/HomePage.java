package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {

	WebDriver driver;

	@FindBy(xpath = "//*[@id=\"root\"]/div/div/div[1]/div[1]/a/svg")
	private WebElement homeElement;

	@FindBy(xpath = "//*[@id=\"root\"]/div/div/div[1]/div[1]/svg")
	private WebElement toggleElement;

	@FindBy(xpath = "//*[@id=\"root\"]/div/div/div[1]/div[2]/div/input")
	private WebElement searchElement;
	
	@FindBy(xpath="//*[@id=\"root\"]/div/div/div[2]/div[1]/div/div/a[1]/div/img")
	private WebElement profileElement;

	@FindBy(xpath = "//*[@id=\"root\"]/div/div/div[2]/div[1]/div/div/div[1]")
	private WebElement exploreElement;

	@FindBy(xpath = "//*[@id=\"root\"]/div/div/div[2]/div[1]/div/div/div[2]")
	private WebElement notificationsElement;

	@FindBy(xpath = "//*[@id=\"root\"]/div/div/div[2]/div[1]/div/div/div[3]")
	private WebElement messagesElement;

	@FindBy(xpath = "//*[@id=\"root\"]/div/div/div[2]/div[1]/div/div/div[4]")
	private WebElement friendsElement;

	@FindBy(xpath = "//*[@id=\root\"]/div/div/div[1]/div[3]/div[1]/svg")
	private WebElement navbarMessagesElement;

	@FindBy(xpath = "//*[@id=\"root\"]/div/div/div[1]/div[3]/div[3]")
	private WebElement logoutElement;

	public HomePage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
}
