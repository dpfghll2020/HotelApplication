package pages;

import org.openqa.selenium.*;
import src.excelExportAndFileIO.ReadExcelFile;
import src.excelExportAndFileIO.WriteExcelFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertEquals;
import static pages.ConfirmationPage.*;
import static src.excelExportAndFileIO.WriteExcelFile.*;
import static src.timeFunctions.TimeFormattedFunctions.*;

public class FormPage {

    public static void searchHotelPage(WebDriver driver, ReadExcelFile objExcelFile, int rowNum, String appDataSheetName) throws
            InterruptedException, IOException, ParseException {
        // Get the data from the worksheet
        HashMap<String, String> hotelValues = objExcelFile.readHotelData(rowNum, appDataSheetName);
        HashMap<String, String> hotelDateValues = objExcelFile.readHotelDateData(rowNum, appDataSheetName);

        //split out the elements
        String location = hotelValues.get("Location");
        String hotelName = hotelValues.get("Hotels");
        String roomType = hotelValues.get("RoomType");
        String numRooms = hotelValues.get("NumOfRooms");
        String checkInDate = String.valueOf(hotelDateValues.get("CheckInDate"));
        String checkOutDate = String.valueOf(hotelDateValues.get("CheckOutDate"));
        String adultsNum = hotelValues.get("AdultsPerRoom");
        String childNum = hotelValues.get("ChildrenPerRoom");

        //wait for the page to be fully displayed
        waitForAlertBanner(driver, "right_adv");

        //Location
        driver.findElement(By.cssSelector("option[value='" + location + "']")).click();
        Thread.sleep(1000);

        //Hotels
        driver.findElement(By.cssSelector("option[value='" + hotelName + "']")).click();
        Thread.sleep(1000);

        //Room Type
        driver.findElement(By.cssSelector("option[value='" + roomType + "']")).click();
        Thread.sleep(1000);

        //Number of Rooms
        char numOfRooms = numRooms.charAt(0);
        driver.findElement(By.id("room_nos")).findElement(By.cssSelector("option[value='" + numOfRooms + "']")).click();
        driver.findElement(By.id("room_nos")).sendKeys(Keys.TAB);
        Thread.sleep(1000);

        //Check In Date
        driver.findElement(By.id("datepick_in")).sendKeys(checkInDate);
        Thread.sleep(1000);
        driver.findElement(By.id("datepick_in")).sendKeys(Keys.TAB);
        Thread.sleep(1000);

        //Check Out Date
        driver.findElement(By.id("datepick_out")).sendKeys(checkOutDate);
        Thread.sleep(1000);

        //Adults per Room
        char numAdults = adultsNum.charAt(0);
        driver.findElement(By.id("adult_room")).findElement(By.cssSelector("option[value='" + numAdults + "']")).click();
        Thread.sleep(2000);

        //Children per Room
        char numChildren = childNum.charAt(0);
        driver.findElement(By.id("child_room")).findElement(By.cssSelector("option[value='" + numChildren + "']")).click();
        Thread.sleep(2000);

        //Click either Search or Reset button
        driver.findElement(By.id("Submit")).click();
        Thread.sleep(2000);

        // There may be errors which require fixing the data and write back to
        boolean dateChanged = false;

        if (!driver.findElements(By.id("checkin_span")).isEmpty()) {

            if (driver.findElement(By.id("checkin_span")).isDisplayed()) {
                String error_msg = driver.findElement(By.id("checkin_span")).getText();
                switch (error_msg) {
                    case "Check-In Date should be either Today or Later Date":
                        System.out.println("Check-In Date issue: " + error_msg);
                        // change the checkin date to today
                        // check timezone calcs
                        String timeZoneString = getCorrectTimeZone(location);
                        String firstDate = getCurrentDateByTimeZone(timeZoneString);

                        // save the new date to the HashMap
                        //date format has to be American style, and be saved as a Date
                        String firstAmericanDate = changeTimeFormatToAmerican(firstDate);
                        hotelDateValues.put("CheckInDate", firstAmericanDate);

                        // save the updated CheckOutDate back to the HashMap, updated to be numDays from first Date
                        int numDays = getDateDifference(checkInDate, checkOutDate);
                        String secondDate = addDaysToCurrentDateByTimeZone(timeZoneString, numDays);
                        String secondAmericanDate = changeTimeFormatToAmerican(secondDate);
                        hotelDateValues.put("CheckOutDate", secondAmericanDate);

                        // signal that dates have changed and need to be updated in excel spreadsheet
                        dateChanged = true;

                        // have to re-click into the field in order to change the value
                        driver.findElement(By.id("datepick_in")).click();
                        //Thread.sleep(1000);
                        driver.findElement(By.id("datepick_in")).clear();
                        //Thread.sleep(1000);
                        driver.findElement(By.id("datepick_in")).sendKeys(firstDate);
                        //Thread.sleep(2000);
                        driver.findElement(By.id("datepick_in")).sendKeys(Keys.TAB);
                        //Thread.sleep(1000);

                        // Now update the check out date as well
                        driver.findElement(By.id("datepick_out")).click();
                        //Thread.sleep(1000);
                        driver.findElement(By.id("datepick_out")).clear();
                        //Thread.sleep(1000);
                        driver.findElement(By.id("datepick_out")).sendKeys(secondDate);
                        driver.findElement(By.id("datepick_out")).sendKeys(Keys.TAB);
                        Thread.sleep(1000);
                        break;
                    case "Check-in and Check-out date cannot be same":
                        System.out.println("Last name issue: " + error_msg);
                        break;
                    default:
                        System.out.println("Other error message issue: " + error_msg);
                }
                // Try clicking the Search button again
                driver.findElement(By.id("Submit")).click();
                Thread.sleep(2000);
            }
            if (dateChanged)
                WriteExcelFile.writeHotelDateData(hotelDateValues, rowNum, appDataSheetName);
        }
    }

    public static void selectHotelPage(WebDriver driver, ReadExcelFile objExcelFile, int rowNum, String appDataSheetName, String buttonName) throws
            InterruptedException, IOException, ParseException {

        List testResults = new ArrayList<String>();
        testResults.clear();

        if (buttonName.equals("continue")) {
            // Get the data from the worksheet
            HashMap<String, String> hotelValues = objExcelFile.readHotelData(rowNum, appDataSheetName);
            HashMap<String, String> hotelDateValues = objExcelFile.readHotelDateData(rowNum, appDataSheetName);

            //split out the elements
            String location = hotelValues.get("Location");
            String hotelName = hotelValues.get("Hotels");
            String roomType = hotelValues.get("RoomType");
            String numRooms = hotelValues.get("NumOfRooms");
            String checkInDate = String.valueOf(hotelDateValues.get("CheckInDate"));
            String checkOutDate = String.valueOf(hotelDateValues.get("CheckOutDate"));

            String selectHotelTable = "/html/body/table[2]/tbody/tr[2]/td/form/table/tbody/tr[2]/td/table/tbody/tr[2]";

            //wait for the page to be fully displayed
            waitForAlertBanner(driver, "reg_button");

            //validate that the displayed data matches what was entered on the search page
            // read webtable information
            System.out.println("--- SELECT HOTEL PAGE COMPARISONS ---");
            testResults.add("---HOTEL PAGE---");

            // Hotel Name
            String val = driver.findElement(By.xpath(selectHotelTable + "/td[2]/input")).getAttribute("value");
            compareValuesString("Hotel Name", hotelName, val);

            // Location
            val = driver.findElement(By.xpath(selectHotelTable + "/td[3]/input")).getAttribute("value");
            compareValuesString("Location", location, val);

            // Number of Rooms
            int numberOfRooms = Integer.parseInt(String.valueOf(numRooms.charAt(0)));
            String numRoom = driver.findElement(By.xpath(selectHotelTable + "/td[4]/input")).getAttribute("value");
            int numOfRoomsOnPage = Integer.parseInt(String.valueOf(numRoom.charAt(0)));
            compareValuesInteger("Number of Rooms", numberOfRooms, numOfRoomsOnPage);

            // Arrival Date
            val = driver.findElement(By.xpath(selectHotelTable + "/td[5]/input")).getAttribute("value");
            compareValuesString("Arrival Date", checkInDate, val);

            // Departure Date
            val = driver.findElement(By.xpath(selectHotelTable + "/td[6]/input")).getAttribute("value");
            compareValuesString("Departure Date", checkOutDate, val);

            // Number of Days
            val = driver.findElement(By.xpath(selectHotelTable + "/td[7]/input")).getAttribute("value");
            String[] totalDays = val.split("D");
            int numberOfDays = Integer.parseInt(totalDays[0].trim());

//            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//            Date firstDate = dateFormat.parse(checkInDate);
//            Date secondDate = dateFormat.parse(checkOutDate);
//            long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
//            int numDays = Math.toIntExact(TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS));

            int numDays = getDateDifference(checkInDate, checkOutDate);
            compareValuesInteger("Number of Days", numDays, numberOfDays);

            // Room Type
            val = driver.findElement(By.xpath(selectHotelTable + "/td[8]/input")).getAttribute("value");
            compareValuesString("Room Type", roomType, val);

            // Price per Night
            String pricePerNight = driver.findElement(By.xpath(selectHotelTable + "/td[9]/input")).getAttribute("value");
            System.out.println("Price Per Night: |" + pricePerNight + "| displayed");

            // Total Price
            String totalPrice = driver.findElement(By.xpath(selectHotelTable + "/td[10]/input")).getAttribute("value");
            System.out.println("Total Price: |" + totalPrice + "| displayed");

            System.out.println("Table Row Data:" + hotelName + "|" + location + "|" + numRoom + "|" + checkInDate + "|" +
                    checkOutDate + "|" + numDays + "|" + roomType + "|" + pricePerNight + "|" + totalPrice);
            System.out.println();

            // write out the test results
            writeTestResults(testResults, rowNum, appDataSheetName);
        }
        if (buttonName.equals("continue")) {
            //Click the Continue button, or the Cancel button
            driver.findElement(By.id(buttonName)).click();
            Thread.sleep(2000);

            //check to see if the "Please Select a Hotel" text appears, since no radio button is checked
            //String warningText = getAlertBannerText(driver,"reg_error");
            assertEquals("Please Select a Hotel",
                    getAlertBannerText(driver, "reg_error"));
            Thread.sleep(2000);

            //select the row by clicking the radio button
            driver.findElement(By.cssSelector("form[name='select_form']")).
                    findElement(By.cssSelector("input[id='radiobutton_0'")).click();
            Thread.sleep(2000);
        }
        //Click the Continue button, or the Cancel button
        driver.findElement(By.id(buttonName)).click();
        Thread.sleep(2000);
    }

    public static String bookAHotelPage(WebDriver driver, ReadExcelFile objExcelFile, int rowNum, String appDataSheetName) throws
            InterruptedException, IOException, ParseException {
        // Get the data from the worksheet
        HashMap<String, String> hotelValues = objExcelFile.readHotelData(rowNum, appDataSheetName);
        HashMap<String, String> hotelDateValues = objExcelFile.readHotelDateData(rowNum, appDataSheetName);

        //split out the elements
        String location = hotelValues.get("Location");
        String hotelName = hotelValues.get("Hotels");
        String roomType = hotelValues.get("RoomType");
        String numRooms = hotelValues.get("NumOfRooms");
        String checkInDate = String.valueOf(hotelDateValues.get("CheckInDate"));
        String checkOutDate = String.valueOf(hotelDateValues.get("CheckOutDate"));
        //String adultsNum = hotelValues.get(4);
        //String childNum = hotelValues.get(5);

        String firstName = hotelValues.get("FirstName");
        String lastName = hotelValues.get("LastName");
        String streetAddress = hotelValues.get("StreetAddress");
        String cityStateZip = hotelValues.get("CityStateZip");
        String billingAddress = streetAddress + "\n" + cityStateZip;
        String creditCardNumber = hotelValues.get("CreditCardNumber");
        String creditCardType = hotelValues.get("CreditCardType");
        String expiryMonth = hotelValues.get("ExpirationMonth");
        String expiryYear = hotelValues.get("ExpirationYear");
        String cvvNumber = hotelValues.get("CVVNumber");
        String bookOrCancelButton = hotelValues.get("BookOrCancelButton");

        //wait for the page to be fully displayed
        waitForAlertBanner(driver, "reg_button");
        Thread.sleep(2000);

        //validate that the fields displayed are same as fields that were entered on the Search Hotel page
        // HotelName
        String val = driver.findElement(By.xpath("//*[@id=\"hotel_name_dis\"]")).getAttribute("value");
        System.out.println("--- BOOK A HOTEL PAGE COMPARISONS ---");
        compareValuesString("Hotel Name", hotelName, val);

        // Location
        val = driver.findElement(By.xpath("//*[@id=\"location_dis\"]")).getAttribute("value");
        compareValuesString("location", location, val);

        // Room Type
        val = driver.findElement(By.xpath("//*[@id=\"room_type_dis\"]")).getAttribute("value");
        compareValuesString("Room Type", roomType, val);

        // Number of Rooms
        int numberOfRooms = Integer.parseInt(String.valueOf(numRooms.charAt(0)));
        val = driver.findElement(By.xpath("//*[@id=\"room_num_dis\"]")).getAttribute("value");
        int numOfRoomsOnPage = Integer.parseInt(String.valueOf(val.charAt(0)));
        compareValuesInteger("Number of Rooms", numberOfRooms, numOfRoomsOnPage);

        // Total Days
        val = driver.findElement(By.xpath("//*[@id=\"total_days_dis\"]")).getAttribute("value");
        String[] totalDays = val.split("D");
        int numberOfDays = Integer.parseInt(totalDays[0].trim());

//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//        Date firstDate = dateFormat.parse(checkInDate);
//        Date secondDate = dateFormat.parse(checkOutDate);
//        long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
//        int numDays = Math.toIntExact(TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS));

        int numDays = getDateDifference(checkInDate, checkOutDate);
        compareValuesInteger("Total Days", numDays, numberOfDays);

        // Price per Night
        val = driver.findElement(By.xpath("//*[@id=\"price_night_dis\"]")).getAttribute("value");
        String[] pricePerNightArray = val.split(" ");
        int pricePerNight = Integer.parseInt(pricePerNightArray[2].trim());

        // Total Price = Price per Night * Total Days * Number of Rooms
        val = driver.findElement(By.xpath("//*[@id=\"total_price_dis\"]")).getAttribute("value");
        String[] totalPriceArray = val.split(" ");
        int totalPrice = Integer.parseInt(totalPriceArray[2].trim());
        //calculated
        int totPriceCalculated = pricePerNight * numDays * numberOfRooms;
        compareValuesInteger("Total Price", totPriceCalculated, totalPrice);

        // GST = 0.10 * Total Price
        val = driver.findElement(By.xpath("//*[@id=\"gst_dis\"]")).getAttribute("value");
        String[] gstArray = val.split(" ");
        double gstValue = Double.parseDouble(gstArray[2].trim());
        //calculated
        double gstCalcValue = totPriceCalculated * 0.10;
        compareValuesDouble("GST", gstCalcValue, gstValue);

        // Final Billed Price = Total Price + GST
        val = driver.findElement(By.xpath("//*[@id=\"final_price_dis\"]")).getAttribute("value");
        String[] finalPriceArray = val.split(" ");
        double finalBilledPrice = Double.parseDouble(finalPriceArray[2].trim());
        //calculated
        double finalCalcBilledPrice = totPriceCalculated + gstCalcValue;
        compareValuesDouble("Final Billed Price", finalCalcBilledPrice, finalBilledPrice);

        System.out.println();

        // Fill in the fields to book the hotel room(s)
        // First Name
        driver.findElement(By.id("first_name")).sendKeys(firstName);
        Thread.sleep(2000);

        // Last Name
        driver.findElement(By.id("last_name")).sendKeys(lastName);
        Thread.sleep(2000);

        // Billing Address
        driver.findElement(By.id("address")).sendKeys(billingAddress);
        Thread.sleep(2000);

        // Credit Card Number
        driver.findElement(By.id("cc_num")).sendKeys(creditCardNumber);
        Thread.sleep(2000);

        // Credit Card Type
        driver.findElement(By.cssSelector("option[value='" + creditCardType + "']")).click();
        Thread.sleep(2000);

        // Expiry Date: Month
        driver.findElement(By.cssSelector("option[value='" + expiryMonth + "']")).click();
        Thread.sleep(2000);

        // Expiry Date: Year
        driver.findElement(By.cssSelector("option[value='" + expiryYear + "']")).click();
        Thread.sleep(2000);

        // CVV Number
        driver.findElement(By.id("cc_cvv")).sendKeys(cvvNumber);
        Thread.sleep(2000);

        // Book Now or Cancel button
        driver.findElement(By.id(bookOrCancelButton)).click();
        Thread.sleep(2000);

        // Cancel button returns to the Select A Hotel Page
        // Book Now button goes to

        // check for error messages
        try {
            if (bookOrCancelButton.equals("book_now")) {
                if (driver.findElement(By.id("cc_num_span")).isDisplayed()) {
                    String error_msg = driver.findElement(By.id("cc_num_span")).getText();
                    switch (error_msg) {
                        case "Please Enter your First Name":
                            System.out.println("First name issue: " + error_msg);
                            break;
                        case "Please Enter your Last Name":
                            System.out.println("Last name issue: " + error_msg);
                            break;
                        case "Please Enter your Address":
                            System.out.println("Address issue: " + error_msg);
                            break;
                        case "Please Select your Credit Card Type":
                            System.out.println("Credit Card Type issue: " + error_msg);
                            break;
                        case "Please Select your Credit Card Expiry Month":
                            System.out.println("Credit Card Type Expiry Month issue: " + error_msg);
                            break;
                        case "Please Select your Credit Card Expiry Year":
                            System.out.println("Credit Card Type Expiry Year issue: " + error_msg);
                            break;
                        case "Please Select your Credit Card CVV Number":
                            System.out.println("Credit Card Type CVV Number issue: " + error_msg);
                            break;
                        case "Please Enter your 16 Digit Credit Card Number":
                            System.out.println("Credit Card issue: " + error_msg);
                            break;
                        default:
                            Thread.sleep(2000);
                    }
                    bookOrCancelButton = "cancel";
                    driver.findElement(By.id(bookOrCancelButton)).click();
                    Thread.sleep(2000);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Fatal error occurs right after the Book Now button is clicked");
        }
        Thread.sleep(2000);
        return bookOrCancelButton;
    }

    public static String bookingConfirmationPage(WebDriver driver, ReadExcelFile objExcelFile, int rowNum, String appDataSheetName) throws
            InterruptedException, IOException, ParseException {
        // Get the data from the worksheet
        HashMap<String, String> hotelValues = objExcelFile.readHotelData(rowNum, appDataSheetName);
        HashMap<String, String> hotelDateValues = objExcelFile.readHotelDateData(rowNum, appDataSheetName);

        //split out the elements
        String location = hotelValues.get("Location");
        String hotelName = hotelValues.get("Hotels");
        String roomType = hotelValues.get("RoomType");
        String numRooms = hotelValues.get("NumOfRooms");
        String checkInDate = String.valueOf(hotelDateValues.get("CheckInDate"));
        String checkOutDate = String.valueOf(hotelDateValues.get("CheckOutDate"));
        String adultsNum = hotelValues.get("AdultsPerRoom");
        String childNum = hotelValues.get("ChildrenPerRoom");

        String firstName = hotelValues.get("FirstName");
        String lastName = hotelValues.get("LastName");
        String streetAddress = hotelValues.get("StreetAddress");
        String cityStateZip = hotelValues.get("CityStateZip");
        String billingAddress = streetAddress + "\n" + cityStateZip;
        String confirmPageButton = hotelValues.get("ConfirmPageButton");

        //wait for the page to be fully displayed
        waitForButton(driver, "my_itinerary");
        Thread.sleep(4000);

        // Get the values of all of the fields and compare to values in excel table
        System.out.println("--- BOOKING CONFIRMATION PAGE COMPARISONS ---");

        //validate that the fields displayed are same as fields that were entered on the Search Hotel page
        // HotelName
        String val = driver.findElement(By.id("hotel_name")).getAttribute("value");
        System.out.println("--- BOOK A HOTEL PAGE COMPARISONS ---");
        compareValuesString("Hotel Name", hotelName, val);

        // Location
        val = driver.findElement(By.id("location")).getAttribute("value");
        compareValuesString("Location", location, val);

        // Room Type
        val = driver.findElement(By.id("room_type")).getAttribute("value");
        compareValuesString("Room Type", roomType, val);

        // Arrival Date
        val = driver.findElement(By.id("arrival_date")).getAttribute("value");
        compareValuesString("Arrival Date", checkInDate, val);

        // Departure Date
        val = driver.findElement(By.id("departure_text")).getAttribute("value");
        compareValuesString("Departure Date", checkOutDate, val);

        // Total Rooms
        int numberOfRooms = Integer.parseInt(String.valueOf(numRooms.charAt(0)));
        val = driver.findElement(By.id("total_rooms")).getAttribute("value");
        int numOfRoomsOnPage = Integer.parseInt(String.valueOf(val.charAt(0)));
        compareValuesInteger("Total Rooms", numberOfRooms, numOfRoomsOnPage);

        // Adults per Room
        val = driver.findElement(By.id("adults_room")).getAttribute("value");
        compareValuesString("Adults Per Room", adultsNum, val);

        // Children per Room
        val = driver.findElement(By.id("children_room")).getAttribute("value");
        compareValuesString("Children Per Room", childNum, val);

        // Price per Night
        val = driver.findElement(By.id("price_night")).getAttribute("value");
        String[] pricePerNightArray = val.split(" ");
        int pricePerNight = Integer.parseInt(pricePerNightArray[2].trim());
        System.out.println("Price per Night: |" + val + "| displayed");

        // Total Price = Price per Night * Total Days * Number of Rooms
        val = driver.findElement(By.id("total_price")).getAttribute("value");
        String[] totalPriceArray = val.split(" ");
        int totalPrice = Integer.parseInt(totalPriceArray[2].trim());

        // Find the number of days
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date firstDate = dateFormat.parse(checkInDate);
        Date secondDate = dateFormat.parse(checkOutDate);
        long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
        int numDays = Math.toIntExact(TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS));

        //calculated
        int totPriceCalculated = pricePerNight * numDays * numberOfRooms;
        compareValuesInteger("Total Price", totPriceCalculated, totalPrice);

        // GST = 0.10 * Total Price
        val = driver.findElement(By.id("gst")).getAttribute("value");
        String[] gstArray = val.split(" ");
        double gstValue = Double.parseDouble(gstArray[2].trim());
        //calculated
        double gstCalcValue = totPriceCalculated * 0.10;
        compareValuesDouble("GST", gstCalcValue, gstValue);

        // Final Billed Price = Total Price + GST
        val = driver.findElement(By.id("final_price")).getAttribute("value");
        String[] finalPriceArray = val.split(" ");
        double finalBilledPrice = Double.parseDouble(finalPriceArray[2].trim());
        //calculated
        double finalCalcBilledPrice = totPriceCalculated + gstCalcValue;
        compareValuesDouble("Final Billed Price", finalCalcBilledPrice, finalBilledPrice);

        // First Name
        val = driver.findElement(By.id("first_name")).getAttribute("value");
        compareValuesString("First Name", firstName, val);

        // Last Name
        val = driver.findElement(By.id("last_name")).getAttribute("value");
        compareValuesString("Last Name", lastName, val);

        // Billing Address
        val = driver.findElement(By.id("address")).getAttribute("value");
        compareValuesString("Billing Address", billingAddress, val);

        // Get the Order Number
        String orderNumber = driver.findElement(By.id("order_no")).getAttribute("value");
        Thread.sleep(2000);

        System.out.println("Order Number is:" + orderNumber);
        System.out.println();

        // click the button to continue to the booked itinerary page
        driver.findElement(By.id(confirmPageButton)).click();
        Thread.sleep(2000);

        return orderNumber;
    }

    public static String bookedItineraryPage(WebDriver driver, ReadExcelFile objExcelFile, int rowNum, String appDataSheetName,
                                             String orderNumber) throws IOException, ParseException, InterruptedException {
        // Get the data from the worksheet
        HashMap<String, String> hotelValues = objExcelFile.readHotelData(rowNum, appDataSheetName);
        HashMap<String, String> hotelDateValues = objExcelFile.readHotelDateData(rowNum, appDataSheetName);

        //split out the elements
        String location = hotelValues.get("Location");
        String hotelName = hotelValues.get("Hotels");
        String roomType = hotelValues.get("RoomType");
        String numRooms = hotelValues.get("NumOfRooms");
        String checkInDate = String.valueOf(hotelDateValues.get("CheckInDate"));
        String checkOutDate = String.valueOf(hotelDateValues.get("CheckOutDate"));

        String firstName = hotelValues.get("FirstName");
        String lastName = hotelValues.get("LastName");
        String bookedItineraryPageButton = hotelValues.get("BookedItineraryPageButton");

        //wait for the page to be fully displayed
        waitForButton(driver, "logout");

        // set a WebElement pointer to the whole table, then determine the number of rows in the table
        WebElement bookedItineraryTable = driver.findElement(By.cssSelector("table[cellpadding='5']"));
        List<WebElement> rows = bookedItineraryTable.findElements(By.tagName("tr"));
        int numrows = rows.size();
        System.out.println("Number of rows is = " + numrows);
        //int k;
//        for (int k = 0; k < numrows; k++){
//            String val = rows.get(k).getText();
//            if (!val.isEmpty()) {
//                System.out.println(rows.get(k).getText());
//            }
//        }
//
        // The path to the Booked Itinerary Page web table, and get its number of rows
        // The newest row is always on the bottom (or should be)
        String htmlPath = "/html/body/table[2]/tbody/tr[2]/td/form/table/tbody/tr[2]/td/table/tbody/";
        String mStr = Integer.toString(numrows);

        System.out.println("--- BOOKED ITINERARY PAGE COMPARISONS ---");

        // retrieve values from the page -- last item in table is the last one added
        // Order Number
        String val = driver.findElement(By.xpath(htmlPath + "tr[" + mStr + "]/td[2]/input")).getAttribute("value");
        compareValuesString("Order Number", orderNumber, val);

        // Cancel button: extract the order number from it
        val = driver.findElement(By.xpath(htmlPath + "tr[" + mStr + "]/td[3]/input")).getAttribute("value");
        String[] cancelButtonArray = val.split(" ");
        String orderNum = cancelButtonArray[1].trim();
        compareValuesString("Cancel Order Number", orderNumber, orderNum);

        // Hotel Name
        val = driver.findElement(By.xpath(htmlPath + "tr[" + mStr + "]/td[4]/input")).getAttribute("value");
        compareValuesString("Hotel Name", hotelName, val);

        // Location
        val = driver.findElement(By.xpath(htmlPath + "tr[" + mStr + "]/td[5]/input")).getAttribute("value");
        compareValuesString("Location", location, val);

        // Number of Rooms
        int numberOfRooms = Integer.parseInt(String.valueOf(numRooms.charAt(0)));
        val = driver.findElement(By.xpath(htmlPath + "tr[" + mStr + "]/td[6]/input")).getAttribute("value");
        int numOfRoomsOnPage = Integer.parseInt(String.valueOf(val.charAt(0)));
        compareValuesInteger("Number of Rooms", numberOfRooms, numOfRoomsOnPage);

        // First Name
        val = driver.findElement(By.xpath(htmlPath + "tr[" + mStr + "]/td[7]/input")).getAttribute("value");
        compareValuesString("First Name", firstName, val);

        // Last Name
        val = driver.findElement(By.xpath(htmlPath + "tr[" + mStr + "]/td[8]/input")).getAttribute("value");
        compareValuesString("Last Name", lastName, val);

        // Arrival Date
        val = driver.findElement(By.xpath(htmlPath + "tr[" + mStr + "]/td[9]/input")).getAttribute("value");
        compareValuesString("Arrival Date", checkInDate, val);

        // Departure Date
        val = driver.findElement(By.xpath(htmlPath + "tr[" + mStr + "]/td[10]/input")).getAttribute("value");
        compareValuesString("Departure Date", checkOutDate, val);

        // Find the number of days
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//        Date firstDate = dateFormat.parse(checkInDate);
//        Date secondDate = dateFormat.parse(checkOutDate);
//        long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
//        int numDays = Math.toIntExact(TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS));

        int numDays = getDateDifference(checkInDate, checkOutDate);

        // No. of Days
        String dayVal = driver.findElement(By.xpath(htmlPath + "tr[" + mStr + "]/td[11]/input")).getAttribute("value");
        val = String.valueOf(dayVal.charAt(0));
        int displayDays = Integer.parseInt(val);
        compareValuesInteger("No. of Days", numDays, displayDays);

        // Rooms Type
        val = driver.findElement(By.xpath(htmlPath + "tr[" + mStr + "]/td[12]/input")).getAttribute("value");
        compareValuesString("Room Type", roomType, val);

        // Price per Night
        val = driver.findElement(By.xpath(htmlPath + "tr[" + mStr + "]/td[13]/input")).getAttribute("value");
        String[] pricePerNightArray = val.split(" ");
        int pricePerNight = Integer.parseInt(pricePerNightArray[2].trim());
        System.out.println("Price per Night: |" + val + "| displayed");

        // Total Price (incl. GST)
        val = driver.findElement(By.xpath(htmlPath + "tr[" + mStr + "]/td[14]/input")).getAttribute("value");
        String[] totalPriceArray = val.split(" ");
        int totalPrice = Integer.parseInt(totalPriceArray[2].trim());

        // calculated
        int totPriceCalculated = pricePerNight * numDays * numberOfRooms;
        double gstCalcValue = totPriceCalculated * 0.10;
        totPriceCalculated += gstCalcValue;
        compareValuesInteger("Total Price", totPriceCalculated, totalPrice);

        System.out.println();

        // Click the Search Hotel page button
        driver.findElement(By.id(bookedItineraryPageButton)).click();
        Thread.sleep(2000);

        return bookedItineraryPageButton;
    }

    public static void cancelBookedItinerary(WebDriver driver, String orderNumber) throws InterruptedException {

        //wait for the page to be fully displayed
        waitForButton(driver, "logout");

        // set a WebElement pointer to the whole table, then determine the number of rows in the table
        WebElement bookedItineraryTable = driver.findElement(By.cssSelector("table[cellpadding='5']"));
        List<WebElement> rows = bookedItineraryTable.findElements(By.tagName("tr"));
        int numrows = rows.size();
        String mStr = Integer.toString(numrows);
        System.out.println("Number of rows is = " + mStr);

        // Search using the order number
        // The path to the Booked Itinerary Page web table, and get its number of rows
        String htmlPath = "/html/body/table[2]/tbody/tr[2]/td/form/table/tbody/tr[2]/td/table/tbody/";

        for (int i = 2; i <= numrows; i++) {
            mStr = Integer.toString(i);
            // Order Number
            String val = driver.findElement(By.xpath(htmlPath + "tr[" + mStr + "]/td[2]/input")).getAttribute("value");
            if (orderNumber.equals(val)) {
                System.out.println("Order Number: |" + val + "| displayed = |" + orderNumber + "| passed in");
                Thread.sleep(2000);

                // now click the checkbox
                driver.findElement(By.xpath(htmlPath + "tr[" + mStr + "]/td[1]/input")).click();
                Thread.sleep(2000);

                //now click the "Cancel Selected" button
                //driver.findElement(By.name("cancelall")).click();
                //Thread.sleep(2000);

                // Now process the popup window, clicking the "OK" button
                driver.switchTo().alert().accept();
                Thread.sleep(2000);

                String cancelText = driver.findElement(By.id("search_result_error")).getAttribute("value");
                System.out.println("Cancel Text = " + cancelText);

                break;
            }
        }
        Thread.sleep(2000);

        // Click the Search Hotel page button
        //driver.findElement(By.id("search_hotel")).click();
        //Thread.sleep(2000);

    }

    private static void compareValuesString(String fieldName, String expectedValue, String actualValue) {
        if (expectedValue.equals(actualValue)) {
            System.out.println(fieldName + ": |" + actualValue + "| displayed = |" + expectedValue + "| from Excel spreadsheet");
        } else {
            System.out.println("ERROR::" + fieldName + ": |" + actualValue + "| displayed = |" + expectedValue + "| from Excel spreadsheet");
        }
        assertEquals(expectedValue, actualValue);
    }

    private static void compareValuesInteger(String fieldName, int expectedValue, int actualValue) {
        if (expectedValue == actualValue) {
            System.out.println(fieldName + ": |" + actualValue + "| displayed = |" + expectedValue + "| calculated");
        } else {
            System.out.println("ERROR::" + fieldName + ": |" + actualValue + "| displayed = |" + expectedValue + "| calculated");
        }
        assertEquals(expectedValue, actualValue);
    }

    private static void compareValuesDouble(String fieldName, double expectedValue, double actualValue) {
        if (expectedValue == actualValue) {
            System.out.println(fieldName + ": |" + actualValue + "| displayed = |" + expectedValue + "| calculated");
        } else {
            System.out.println("ERROR::" + fieldName + ": |" + actualValue + "| displayed = |" + expectedValue + "| calculated");
        }
        assertEquals(expectedValue, actualValue);
    }
}
