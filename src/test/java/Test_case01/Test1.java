package Test_case01;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import pages.LoginPage;
import pages.RegistrationPage;

public class Test1 {
    public static void main(String[] args) {
        WebDriver driver = new ChromeDriver();
        String url = "http://localhost:3000/login";
        driver.get(url);
//        RegistrationPage register = new RegistrationPage(driver);
//        register.register("sample","Mypass@123","sample@email.com","Name A B");
        LoginPage login = new LoginPage(driver);
        login.login("sample","Mypass@123");
    }
}
