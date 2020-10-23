package src;

import org.openqa.selenium.By;
import src.excelExportAndFileIO.ReadExcelFile;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import pages.FormPage;
import pages.LoginPage;

import java.io.IOException;
import java.text.ParseException;

import java.util.HashMap;

import static pages.ConfirmationPage.waitForButton;

public class HotelApp {
    public static void main(String[] args) throws InterruptedException, IOException, ParseException {

        // Start by reading the initial configuration from the configuration file
        ReadExcelFile objExcelFile = new ReadExcelFile();

        HashMap<String,String> configHashValues = objExcelFile.readHashMapConfigFile();

        //split out the elements
        String fileName = configHashValues.get("Filename");
        String loginSheetName = configHashValues.get("LoginSheetName");
        String chromeDriverDirLocation = configHashValues.get("seleniumDriverDirLoc");
        String appDirLoc = configHashValues.get("ApplicationDirectoryLocation");
        String appDataSheetName = configHashValues.get("HotelAppDataSheetName");

        //Prepare the path of the configuration excel file
        String filePath = System.getProperty("user.dir") + "\\";

        // Open the application data-driven worksheet
        HSSFWorkbook dataDrivenWorkbook =
                objExcelFile.openExcelWorkbook(filePath, fileName);

        //Get number of rows of the worksheet for logins
        int rowsOfSheet = objExcelFile.getNumOfWorksheetRows(dataDrivenWorkbook,loginSheetName);

        //START SELENIUM SECTION ON HOTEL APP

        System.setProperty("webdriver.chrome.driver", chromeDriverDirLocation);

        WebDriver driver = new ChromeDriver();

        driver.get(appDirLoc);

        //test logins by looping through all rows of the login worksheet
        // Now loop through all of the rows of the worksheet
        for (int i = 1; i < rowsOfSheet + 1; i++) {
            // First item: login name
            String loginName = objExcelFile.readExcelCellItem(dataDrivenWorkbook, loginSheetName, i, 0);
            //System.out.print("||" + loginName + "||");

            // Second item: password
            String loginPassword = objExcelFile.readExcelCellItem(dataDrivenWorkbook, loginSheetName, i, 1);
            //System.out.println(loginPassword + "||");

            //check login page with the specified login and login password
            LoginPage.login(driver, loginName, loginPassword);

            // check to see if there is an invalid login text displayed after click the Submit button
            if (LoginPage.checkAuthorizationError(driver)) {
                // If there is an error, then go to the new user registration page
                LoginPage.newUserLogin(driver);
            }
            else {
                String buttonName = "continue";

                // get information on the application data worksheet
                int appDataRows = objExcelFile.getNumOfWorksheetRows(dataDrivenWorkbook,appDataSheetName);

                for (int rowNum = 1; rowNum < appDataRows + 1; rowNum++) {

                    // Short circuit the normal flow: click the Booked Itinerary link to go to the Booked Itinerary page
                    waitForButton(driver, "Reset");
                    Thread.sleep(2000);

                    // click the "Booked Itinerary" link
//                    driver.findElement(By.cssSelector("a[href='BookedItinerary.php']")).click();
//                    Thread.sleep(2000);
//
//                    String orderNum = "O2VL92534R";
//                    buttonName = FormPage.bookedItineraryPage(driver, objExcelFile, rowNum, appDataSheetName, orderNum);
//                    Thread.sleep(2000);
//
//                    FormPage.cancelBookedItinerary(driver, orderNum);

                    // Start a hotel reservation
                    FormPage.searchHotelPage(driver, objExcelFile, rowNum, appDataSheetName);

                    // if cancel button is clicked, then return to the selectHotelPage function to try over

                    // Select the Hotel page
                    FormPage.selectHotelPage(driver, objExcelFile, rowNum, appDataSheetName, buttonName);

                    // Pay for the Hotel reservation page
                    buttonName = FormPage.bookAHotelPage(driver, objExcelFile, rowNum, appDataSheetName);
                    if (buttonName.equals("cancel")) {
                        FormPage.selectHotelPage(driver, objExcelFile, rowNum, appDataSheetName, buttonName);
                        buttonName = "continue";
                    }
                    else {
                        // Check out the Booking Confirmation Page
                        String orderNumber = FormPage.bookingConfirmationPage(driver, objExcelFile, rowNum, appDataSheetName);

                        // Check to see if the just booked itinerary is listed on the Booked Itinerary page
                        buttonName = FormPage.bookedItineraryPage(driver, objExcelFile, rowNum, appDataSheetName, orderNumber);
                    }
                }

                // finally, logout when everything is completed
                LoginPage.logout(driver);
            }
        }
        // close the excel file
        dataDrivenWorkbook.close();


/*
        assertEquals("Invalid Login details or Your Password might have expired. Click here to reset your password",
                confirmationPage.getAlertBannerText(driver,"auth_error"));

        FormPage formPage = new FormPage();
        formPage.submitForm(driver);

        ConfirmationPage confirmationPage = new ConfirmationPage();
        confirmationPage.waitForAlertBanner(driver);

        assertEquals("The form was successfully submitted!", confirmationPage.getAlertBannerText(driver));
*/
        // exit the app
        Thread.sleep(2000);
        driver.quit();
        }
    }
