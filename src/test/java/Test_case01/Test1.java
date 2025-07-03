package Test_case01;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class Test1 {
    public static void main(String[] args) {
        WebDriver driver = new ChromeDriver();
        String url = "http://localhost:3000/";
        driver.get(url);

        System.out.println(driver.getTitle());

    }
}
