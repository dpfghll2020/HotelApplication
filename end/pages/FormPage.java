import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

public class FormPage {

    public static void submitForm(WebDriver driver) throws InterruptedException
    {
        driver.findElement(By.id("first-name")).sendKeys("John");
        Thread.sleep(2000);

        driver.findElement(By.id("last-name")).sendKeys("Doe");
        Thread.sleep(2000);

        driver.findElement(By.id("job-title")).sendKeys("QA Engineer");
        Thread.sleep(2000);

        driver.findElement(By.id("radio-button-3")).click();
        Thread.sleep(2000);

        driver.findElement(By.id("checkbox-1")).click();
        Thread.sleep(2000);

        driver.findElement(By.cssSelector("option[value='4']")).click();
        Thread.sleep(2000);

        driver.findElement(By.id("datepicker")).sendKeys("06/28/2020");
        driver.findElement(By.id("datepicker")).sendKeys(Keys.RETURN);
        Thread.sleep(2000);

        driver.findElement(By.cssSelector(".btn.btn-lg.btn-primary")).click();
    }
}
