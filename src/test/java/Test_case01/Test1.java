package Test_case01;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import pages.LoginPage;

public class Test1 {
    public static void main(String[] args) {
        WebDriver driver = new ChromeDriver();
        String url = "http://localhost:3000/login";
        driver.get(url);
        LoginPage login = new LoginPage(driver);
        login.login("sample", "Mypass@123");
    }
}
