import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {

    public static void login(WebDriver driver) throws InterruptedException
    {
        driver.findElement(By.id("username")).sendKeys("bigtalltester");
        Thread.sleep(2000);

        driver.findElement(By.id("password")).sendKeys("testitRight01");
        Thread.sleep(2000);

        // Login button
        driver.findElement(By.id("login")).click();
        Thread.sleep(2000);
    }
}
