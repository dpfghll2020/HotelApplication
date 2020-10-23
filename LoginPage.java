package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static pages.ConfirmationPage.waitForAlertBanner;

public class LoginPage {

    public static void login(WebDriver driver,String username,String password) throws InterruptedException {
        //wait for the page to be fully displayed
        waitForAlertBanner(driver, "content_left");

        //fill in fields username and password
        driver.findElement(By.id("username")).sendKeys(username);
        Thread.sleep(2000);

        driver.findElement(By.id("password")).sendKeys(password);
        Thread.sleep(2000);

        // Login button
        driver.findElement(By.id("login")).click();
        Thread.sleep(2000);
    }

    public static boolean checkAuthorizationError(WebDriver driver) throws InterruptedException {
        // check to see if the classId exists on the page - use findElements method to avoid unhandled exception
        String classId = "auth_error";
        List<WebElement> elements = driver.findElements(By.className(classId));

        if (!elements.isEmpty()) {
            // check for authorization error message
            //ConfirmationPage confirmationPage = new ConfirmationPage();
            waitForAlertBanner(driver, classId);
            //Thread.sleep(5000);

            // Check for Invalid Login or Password text
            String alertText = ConfirmationPage.getAlertBannerText(driver, classId);

            if (alertText.equals("Invalid Login details or Your Password might have expired. Click here to reset your password")) {
                // go to the fix login issue page
                driver.get("http://adactin.com/HotelAppBuild2/ForgotPassword.php");
                Thread.sleep(2000);

                // check for the existence of "Email Password" and "Reset" buttons
                if (!driver.findElement(By.id("Submit")).isDisplayed()) System.out.println("Missing the Submit button");
                if (!driver.findElement(By.id("Reset")).isDisplayed()) System.out.println("Missing the Reset button");

                // back to the login page
                driver.get("http://adactinhotelapp.com/HotelAppBuild2/index.php");
                return true;
            }
            if (alertText.equals("Error: Pending Email Verification")) {
                // Normally, do nothing, but for now, click on "Forgot Password?" link
                driver.get("http://adactin.com/HotelAppBuild2/ForgotPassword.php");
                Thread.sleep(2000);

                // check for the existence of "Email Password" and "Reset" buttons
                if (!driver.findElement(By.id("Submit")).isDisplayed()) System.out.println("Missing the Submit button");
                if (!driver.findElement(By.id("Reset")).isDisplayed()) System.out.println("Missing the Reset button");

                // back to the login page
                driver.get("http://adactinhotelapp.com/HotelAppBuild2/index.php");
                return true;
            }
        }
        return false;
    }

    public static void newUserLogin(WebDriver driver) throws InterruptedException {
        //Enter new login information, but don't click the Register button, but click Reset
        driver.findElement(By.className("login_register")).click();
        //Thread.sleep(2000);

        // wait for page to be drawn...wait for "New User Registration Form" to be displayed
        //ConfirmationPage confirmationPage = new ConfirmationPage();
        waitForAlertBanner(driver, "login_title");
        Thread.sleep(2000);

        driver.findElement(By.id("username")).sendKeys("JohnDoe01");
        Thread.sleep(2000);

        driver.findElement(By.id("password")).sendKeys("jJuU01!");
        Thread.sleep(2000);

        driver.findElement(By.id("re_password")).sendKeys("jJuU01!");
        Thread.sleep(2000);

        driver.findElement(By.id("full_name")).sendKeys("John Doe");
        Thread.sleep(2000);

        driver.findElement(By.id("email_add")).sendKeys("test@test.com");
        Thread.sleep(2000);

        driver.findElement(By.id("captcha-form")).sendKeys("bugaloo");
        Thread.sleep(2000);

        // Terms and conditions checkbox
        driver.findElement(By.id("tnc_box")).click();
        Thread.sleep(2000);

        // Reset button
        driver.findElement(By.id("Reset")).click();
        Thread.sleep(2000);

        // back to the login page
        driver.get("http://adactinhotelapp.com/HotelAppBuild2/index.php");
        Thread.sleep(2000);
    }

    public static void logout(WebDriver driver) {
        //ConfirmationPage confirmationPage = new ConfirmationPage();

        // find out which page you are on
        if (driver.findElement(By.cssSelector("a[href='Logout.php']")).isDisplayed()) {
            driver.findElement(By.cssSelector("a[href='Logout.php']")).click();

            //make sure that the successful logout text is displayed
            assertEquals("You have successfully logged out. Click here to login again",
                    ConfirmationPage.getAlertBannerText(driver, "reg_success"));
        }
        else
            System.out.println("Unable to find logout link on the page!");
    }
}
