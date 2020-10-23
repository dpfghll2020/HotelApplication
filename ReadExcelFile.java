package src.excelExportAndFileIO;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class ReadExcelFile {

    public HSSFWorkbook openExcelWorkbook(String filePath, String fileName) throws IOException{
        //Create an object of File class to open xlsx or xls file
        File file = new File(filePath+"\\"+fileName);

        //Create an object of FileInputStream class to read excel file
        FileInputStream inputStream = new FileInputStream(file);

        HSSFWorkbook dataDrivenWorkbook = null;

        //Find the file extension by splitting file name in substring  and getting only extension name

        String fileExtensionName = fileName.substring(fileName.indexOf("."));

        //Check condition if the file is xlsx file

        if(fileExtensionName.equals(".xlsx")){

            //If it is xlsx file then create object of XSSFWorkbook class
            dataDrivenWorkbook = new HSSFWorkbook(inputStream);
        }
        //Check condition if the file is xls file
        else if(fileExtensionName.equals(".xls")){
            //If it is xls file then create object of HSSFWorkbook class
            dataDrivenWorkbook = new HSSFWorkbook(inputStream);
        }
        return dataDrivenWorkbook;
    }

//    public List<String> readConfigFile() throws IOException {
//        List<String> configValues = new ArrayList<>() ;
//
//        //create an object of the File class to open the config file
//        File fileClassVar = new File(System.getProperty("user.dir")+"\\ConfigFile.xls");
//        FileInputStream inputStream = new FileInputStream(fileClassVar);
//        //Workbook dataDrivenWorkbook = null;
//        HSSFWorkbook dataDrivenWorkbook = new HSSFWorkbook(inputStream);
//
//        //return the filename
//        configValues.add(readExcelCellItem (dataDrivenWorkbook, "InitConfig", 1, 0));
//
//        // return the sheet name
//        configValues.add(readExcelCellItem (dataDrivenWorkbook, "InitConfig", 1, 1));
//
//        // return the selenium web driver location
//        configValues.add(readExcelCellItem (dataDrivenWorkbook, "InitConfig", 1, 2));
//
//        // return application web address
//        configValues.add(readExcelCellItem (dataDrivenWorkbook, "InitConfig", 1, 3));
//
//        // return the application data sheet name
//        configValues.add(readExcelCellItem (dataDrivenWorkbook, "InitConfig", 1, 4));
//
//        // close the config file
//        dataDrivenWorkbook.close();
//        return configValues;
//    }

    public HashMap<String,String> readHashMapConfigFile() throws IOException {
        HashMap<String,String> configValues = new HashMap<>() ;
        String key;
        String value;
        String sheetName = "InitConfig";

        //create an object of the File class to open the config file
        File fileClassVar = new File(System.getProperty("user.dir")+"\\ConfigFile.xls");
        FileInputStream inputStream = new FileInputStream(fileClassVar);

        HSSFWorkbook dataDrivenWorkbook = new HSSFWorkbook(inputStream);

        //return the filename
        key = readExcelCellItem (dataDrivenWorkbook, sheetName, 0, 0);
        value = readExcelCellItem (dataDrivenWorkbook, sheetName, 1, 0);
        configValues.put(key, value);

        // return the sheet name
        key = readExcelCellItem (dataDrivenWorkbook, sheetName, 0, 1);
        value = readExcelCellItem (dataDrivenWorkbook, sheetName, 1, 1);
        configValues.put(key, value);

        // return the selenium web driver location
        key = readExcelCellItem (dataDrivenWorkbook, sheetName, 0, 2);
        value = readExcelCellItem (dataDrivenWorkbook, sheetName, 1, 2);
        configValues.put(key, value);

        // return application web address
        key = readExcelCellItem (dataDrivenWorkbook, sheetName, 0, 3);
        value = readExcelCellItem (dataDrivenWorkbook, sheetName, 1, 3);
        configValues.put(key, value);

        // return the application data sheet name
        key = readExcelCellItem (dataDrivenWorkbook, sheetName, 0, 4);
        value = readExcelCellItem (dataDrivenWorkbook, sheetName, 1, 4);
        configValues.put(key, value);

        // close the config file
        dataDrivenWorkbook.close();
        //System.out.println(configValues);
        return configValues;
    }

    public HashMap<String,String> readHotelData(int rowNum, String sheetName) throws IOException {
        //List<String> hotelValues = new ArrayList<>();
        HashMap<String,String> hotelValues = new HashMap<>();

        //create an object of the File class to open the config file
        File fileClassVar = new File(System.getProperty("user.dir")+"\\ExportExcel.xls");
        FileInputStream inputStream = new FileInputStream(fileClassVar);
        //Workbook dataDrivenWorkbook;
        HSSFWorkbook dataDrivenWorkbook = new HSSFWorkbook(inputStream);

        //return the location
        hotelValues.put(readExcelCellItem (dataDrivenWorkbook, sheetName, 0, 0),
                readExcelCellItem (dataDrivenWorkbook, sheetName, rowNum, 0));

        // return the hotel name
        hotelValues.put(readExcelCellItem (dataDrivenWorkbook, sheetName, 0, 1),
                readExcelCellItem (dataDrivenWorkbook, sheetName, rowNum, 1));

        // return the room type
        hotelValues.put(readExcelCellItem (dataDrivenWorkbook, sheetName, 0, 2),
                readExcelCellItem (dataDrivenWorkbook, sheetName, rowNum, 2));

        // return number of rooms
        hotelValues.put(readExcelCellItem (dataDrivenWorkbook, sheetName, 0, 3),
                readExcelCellItem (dataDrivenWorkbook, sheetName, rowNum, 3));

        // return the number of adults
        hotelValues.put(readExcelCellItem (dataDrivenWorkbook, sheetName, 0, 6),
                readExcelCellItem (dataDrivenWorkbook, sheetName, rowNum, 6));

        // return the number of children
        hotelValues.put(readExcelCellItem (dataDrivenWorkbook, sheetName, 0, 7),
                readExcelCellItem (dataDrivenWorkbook, sheetName, rowNum, 7));

        // return button name to use on the search page: Search or Reset
        hotelValues.put(readExcelCellItem (dataDrivenWorkbook, sheetName, 0, 8),
                readExcelCellItem (dataDrivenWorkbook, sheetName, rowNum, 8));

        // return the First Name
        hotelValues.put(readExcelCellItem (dataDrivenWorkbook, sheetName, 0, 9),
                readExcelCellItem (dataDrivenWorkbook, sheetName, rowNum, 9));

        // return the Last Name
        hotelValues.put(readExcelCellItem (dataDrivenWorkbook, sheetName, 0, 10),
                readExcelCellItem (dataDrivenWorkbook, sheetName, rowNum, 10));

        // return the Street Address
        hotelValues.put(readExcelCellItem (dataDrivenWorkbook, sheetName, 0, 11),
                readExcelCellItem (dataDrivenWorkbook, sheetName, rowNum, 11));

        // return the CityStateZip
        hotelValues.put(readExcelCellItem (dataDrivenWorkbook, sheetName, 0, 12),
                readExcelCellItem (dataDrivenWorkbook, sheetName, rowNum, 12));

        // return the Credit Card Number
        hotelValues.put(readExcelCellItem (dataDrivenWorkbook, sheetName, 0, 13),
                readExcelCellItem (dataDrivenWorkbook, sheetName, rowNum, 13));

        // return the Credit Card Type
        hotelValues.put(readExcelCellItem (dataDrivenWorkbook, sheetName, 0, 14),
                readExcelCellItem (dataDrivenWorkbook, sheetName, rowNum, 14));

        // return the Expiration Month
        hotelValues.put(readExcelCellItem (dataDrivenWorkbook, sheetName, 0, 15),
                readExcelCellItem (dataDrivenWorkbook, sheetName, rowNum, 15));

        // return the Expiration Year
        hotelValues.put(readExcelCellItem (dataDrivenWorkbook, sheetName, 0, 16),
                readExcelCellItem (dataDrivenWorkbook, sheetName, rowNum, 16));

        // return the CVV Number
        hotelValues.put(readExcelCellItem (dataDrivenWorkbook, sheetName, 0, 17),
                readExcelCellItem (dataDrivenWorkbook, sheetName, rowNum, 17));

        // return the Book Now or Cancel button name
        hotelValues.put(readExcelCellItem (dataDrivenWorkbook, sheetName, 0, 18),
                readExcelCellItem (dataDrivenWorkbook, sheetName, rowNum, 18));

        // return the Booking Confirmation Page button name
        hotelValues.put(readExcelCellItem (dataDrivenWorkbook, sheetName, 0, 19),
                readExcelCellItem (dataDrivenWorkbook, sheetName, rowNum, 19));

        // return the Booked Itinerary Page button name
        hotelValues.put(readExcelCellItem (dataDrivenWorkbook, sheetName, 0, 20),
                readExcelCellItem (dataDrivenWorkbook, sheetName, rowNum, 20));

        // close the Hotel Data workbook
        dataDrivenWorkbook.close();
        return hotelValues;
    }

    public HashMap<String,String> readHotelDateData(int rowNum, String sheetName) throws IOException {
        HashMap<String,String> hotelDateValues = new HashMap<>();

        //create an object of the File class to open the config file
        File fileClassVar = new File(System.getProperty("user.dir")+"\\ExportExcel.xls");
        FileInputStream inputStream = new FileInputStream(fileClassVar);
        //Workbook dataDrivenWorkbook;
        HSSFWorkbook dataDrivenWorkbook = new HSSFWorkbook(inputStream);

        // return the check in date
        hotelDateValues.put(readExcelCellItem(dataDrivenWorkbook, sheetName, 0, 4),
                readExcelCellDateItem(dataDrivenWorkbook, sheetName, rowNum, 4));

        // return the check out date
        hotelDateValues.put(readExcelCellItem (dataDrivenWorkbook, sheetName, 0, 5),
                readExcelCellDateItem (dataDrivenWorkbook, sheetName, rowNum, 5));

        // close the Hotel data workbook
        dataDrivenWorkbook.close();

        return hotelDateValues;
    }

    public int getNumOfWorksheetRows(HSSFWorkbook dataDrivenWorkbook, String sheetName) {
        //Read sheet inside the workbook by its name
        Sheet dataSheet = dataDrivenWorkbook.getSheet(sheetName);

        //Find number of rows in excel file
        return dataSheet.getLastRowNum() - dataSheet.getFirstRowNum();
    }

    public String readExcelCellItem (HSSFWorkbook dataDrivenWorkbook, String sheetName, int rowNum, int colNum) {
        Sheet dataSheet = dataDrivenWorkbook.getSheet(sheetName);

        if (rowNum < dataSheet.getFirstRowNum()) {
            System.out.println("rowNum = '" + rowNum + "' is less than first row number of '"+ dataSheet.getFirstRowNum());
            return "";
        }
        else if (rowNum > dataSheet.getLastRowNum()) {
            System.out.println("rowNum = '" + rowNum + "' is > than last row number of '"+ dataSheet.getLastRowNum());
            return "";
        }
        Row row = dataSheet.getRow(rowNum);

        // read the cell value based on the type of data stored in the cell
        try {
            switch (row.getCell(colNum).getCellType())
            {
                case STRING:
                    return row.getCell(colNum).getStringCellValue();
                case NUMERIC:
                    // have to return just decimal part of the number
                   return String.valueOf((int)row.getCell(colNum).getNumericCellValue());

                default:
                    return " ";
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception Generated in readExcelCellItem reading cell: " + colNum);
            return " ";
        }
    }

    public String readExcelCellDateItem (HSSFWorkbook dataDrivenWorkbook, String sheetName, int rowNum, int colNum) {
        Sheet dataSheet = dataDrivenWorkbook.getSheet(sheetName);

        if (rowNum < dataSheet.getFirstRowNum()) {
            System.out.println("rowNum = '" + rowNum + "' is less than first row number of '"+ dataSheet.getFirstRowNum());
            return "";
        }
        else if (rowNum > dataSheet.getLastRowNum()) {
            System.out.println("rowNum = '" + rowNum + "' is > than last row number of '"+ dataSheet.getLastRowNum());
            return "";
        }

        Row row = dataSheet.getRow(rowNum);

        try {
            // check to see if retrieving a Date from the cell...that is a special case
            // needs to be formatted as mm/DD/yyyy
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date dateVal = row.getCell(colNum).getDateCellValue();
            return dateFormat.format(dateVal);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception Generated in readExcelCellDateItem reading cell: " + colNum);
            return " ";
        }
    }
}

    /*
        public void readExcel(String filePath,String fileName,String sheetName) throws IOException{

            //Create an object of File class to open xlsx file
            File file = new File(filePath+"\\"+fileName);

            //Create an object of FileInputStream class to read excel file
            FileInputStream inputStream = new FileInputStream(file);

            Workbook dataDrivenWorkbook = null;

            //Find the file extension by splitting file name in substring  and getting only extension name

            String fileExtensionName = fileName.substring(fileName.indexOf("."));

            //Check condition if the file is xlsx file

            if(fileExtensionName.equals(".xlsx")){

                //If it is xlsx file then create object of XSSFWorkbook class
                dataDrivenWorkbook = new HSSFWorkbook(inputStream);
            }
            //Check condition if the file is xls file
            else if(fileExtensionName.equals(".xls")){
                //If it is xls file then create object of HSSFWorkbook class
                dataDrivenWorkbook = new HSSFWorkbook(inputStream);
            }

        HSSFWorkbook dataDrivenWorkbook = openExcelWorkbook(filePath, fileName, sheetName);

        //Read sheet inside the workbook by its name
        Sheet dataSheet = dataDrivenWorkbook.getSheet(sheetName);

        //Find number of rows in excel file
        int rowCount = dataSheet.getLastRowNum() - dataSheet.getFirstRowNum();

        //Create a loop over all the rows of excel file to read it

        for (int i = 0; i < rowCount+1; i++) {
            Row row = dataSheet.getRow(i);

            //Create a loop to print cell values in a row
            for (int j = 0; j < row.getLastCellNum(); j++) {

                //Print Excel data in console
                System.out.print(row.getCell(j).getStringCellValue()+"|| ");

            }
            // last line
            System.out.println();
        }
        // close the excel file
        dataDrivenWorkbook.close();
    }
*/