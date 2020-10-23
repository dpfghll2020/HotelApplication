package src.timeFunctions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimeFormattedFunctions {

    public static String getFormattedTime(String timeFormat) {
        LocalDateTime now = LocalDateTime.now();
        System.out.println("Before Formatting: " + now);
        DateTimeFormatter format = DateTimeFormatter.ofPattern(timeFormat);
        return now.format(format);
    }

    public static String changeTimeFormatToAmerican(String dateString) throws ParseException {
        //Date date1 = new SimpleDateFormat("MM/dd/yyyy").parse(dateString);

        String[] splitDateString = dateString.split("/");
        int dayOfMonth = Integer.parseInt(splitDateString[0]);
        int month = Integer.parseInt(splitDateString[1]);
        int year = Integer.parseInt(splitDateString[2]);
        LocalDate today = LocalDate.of(year, month, dayOfMonth);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        return today.format(formatter);
        //return date1;
    }

    public static String getCorrectTimeZone(String city) {
        String timeZoneString;

        switch (city) {
            case "Los Angeles":
              timeZoneString = "America/LosAngeles";
              break;
            case "New York":
                timeZoneString = "America/NewYork";
                break;
            case "London":
            case "Paris":
                timeZoneString = "Europe/" + city;
                break;
            case "Adelaide":
            case "Brisbane":
            case "Melbourne":
            case "Sydney":
                timeZoneString = "Australia/" + city;
                break;
            default:
                timeZoneString = "America/Chicago";
        }
        return timeZoneString;
    }

    public static String getCurrentDateByTimeZone(String timeZoneString) {
        ZoneId z = ZoneId.of(timeZoneString);
        LocalDate today = LocalDate.now(z);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return today.format(formatter);
    }

    public static String addDaysToCurrentDateByTimeZone(String timeZoneString, long days) {
        ZoneId z = ZoneId.of(timeZoneString);
        LocalDate today = LocalDate.now(z);
        LocalDate futureDate = today.plusDays(days);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return futureDate.format(formatter);
    }

    public static int getDateDifference(String beginDate, String endDate) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date firstDate = dateFormat.parse(beginDate);
        Date secondDate = dateFormat.parse(endDate);
        long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());

        return Math.toIntExact(TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS));
    }
}
