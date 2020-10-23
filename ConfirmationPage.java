package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ConfirmationPage {

    public static void waitForButton(WebDriver driver, String buttonId)
    {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until((ExpectedConditions.visibilityOfElementLocated(By.id(buttonId))));
    }

    public static void waitForAlertBanner(WebDriver driver, String className)
    {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until((ExpectedConditions.visibilityOfElementLocated(By.className(className))));
    }

    public static String getAlertBannerText(WebDriver driver, String className)
    {
        if (driver.findElement(By.className(className)).isDisplayed()) {
            return driver.findElement(By.className(className)).getText();
        }
        else return " ";
    }

    public static String getColumnTextFromRow(WebDriver driver, String colName)
    {
        if (driver.findElement(By.cssSelector("form[name=select_form]")).findElement(By.cssSelector("input[id="+colName+"]")).isDisplayed())
        {
            String val = driver.findElement(By.cssSelector("form[name=select_form]")).
                    findElement(By.cssSelector(new StringBuilder().append("input[id=").append(colName).append("]").toString())).
                    getAttribute("value");
            return val;
        }
        else return "";
    }

    public static String getColumnTableElement(WebDriver driver, String colName)
    {
        if (driver.findElement(By.xpath("//*[@id=\""+colName+"\"]")).isDisplayed())
        {
            WebElement baseTable = driver.findElement(By.tagName("table"));
            WebElement tableRow = baseTable.findElement(By.xpath("//*[@id=\"select_form\"]/table/tbody/tr[2]/td/table/tbody/tr[2]"));
            String rowText = tableRow.getAttribute("value");
            System.out.println("Table row text is = " + rowText);

            String val = driver.findElement(By.cssSelector("input[id="+colName+"]")).getAttribute("value");
            return val;
        }
        else return "";
    }
}
