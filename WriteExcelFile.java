package src.excelExportAndFileIO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class WriteExcelFile {

    public void writeExcelRow(String filePath, String fileName, String sheetName, Boolean newSheet, String[] dataToWrite) throws IOException{

        //Create an object of File class to open xlsx or xls file
        File file = new File(filePath+"\\"+fileName);

        //Create an object of FileInputStream class to read excel file
        FileInputStream inputStream = new FileInputStream(file);

        HSSFWorkbook dataDrivenWorkbook = new HSSFWorkbook(inputStream);

        //Find the file extension by splitting  file name in substring and getting only extension name
//        String fileExtensionName = fileName.substring(fileName.indexOf("."));

        //Check condition if the file is xlsx file
//        if(fileExtensionName.equals(".xls")){
//            //If it is xlsx file then create object of XSSFWorkbook class
//            dataDrivenWorkbook = new HSSFWorkbook(inputStream);
//        }
//        //Check condition if the file is xls file
//        else if(fileExtensionName.equals(".xls")){
//            //If it is xls file then create object of XSSFWorkbook class
//            dataDrivenWorkbook = new HSSFWorkbook(inputStream);
//        }

        //Sheet newsheet = dataDrivenWorkbook.createSheet("NewSheet");

        //Read excel sheet by sheet name
        Sheet sheet = dataDrivenWorkbook.getSheet(sheetName);

        //Get the current count of rows in excel file
        //Have to check to see if adding to a new worksheet, last row num is 0
        int rowCount = 0;
        if (!newSheet) {
            rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
        }
        //Get the first row from the sheet
        Row row = sheet.getRow(0);

        //Create a new row (unless first row of new sheet) and append it at last of sheet
        Row newRow;
        if (rowCount == 0) {
            newRow = sheet.createRow(0);
        }
        else newRow = sheet.createRow(rowCount + 1);

        //Create a loop over the cell of newly created Row
        for(int j = 0; j < dataToWrite.length; j++){
        //for(int j = 0; j < row.getLastCellNum(); j++){

            //Fill data in row
            Cell cell = newRow.createCell(j);
            cell.setCellValue(dataToWrite[j]);
        }

        //Close input stream
        inputStream.close();

        //Create an object of FileOutputStream class to create write data in excel file
        FileOutputStream outputStream = new FileOutputStream(file);

        //write data in the excel file
        dataDrivenWorkbook.write(outputStream);

        //close output stream
        outputStream.close();

    }

    public static void writeTestResults(List testResults, int rowNum, String appDataSheetName) throws IOException {

        //create an object of the File class to open the config file
        File fileClassVar = new File(System.getProperty("user.dir")+"\\ExportExcel.xls");
        FileInputStream inputStream = new FileInputStream(fileClassVar);

        HSSFWorkbook dataDrivenWorkbook = new HSSFWorkbook(inputStream);

        // Get the sheet to write result data to
        Sheet sheet = dataDrivenWorkbook.getSheet(appDataSheetName);

        // Get the current row of the sheet to write result data to
        Row row = sheet.getRow(rowNum);

        // Now write the whole set of List results into the row
        int start = row.getLastCellNum() + 1;
        int end = start + testResults.size();
        for (int j = 0; j < testResults.size(); j++) {
            Cell cell = row.createCell(start + j);
            cell.setCellValue((String) testResults.get(j));
        }

        // write the composed row into the datasheet at the specified row
        // MUST CLOSE inputStream first; otherwise, will overwrite the entire sheet and lose everything!!
        inputStream.close();

        FileOutputStream outputStream = new FileOutputStream(fileClassVar);
        dataDrivenWorkbook.write(outputStream);

        // Now close the streams prior to exiting
        outputStream.close();
    }

    public void createNewDataSheet(String filePath, String fileName, String sheetName) throws IOException {

        //Create an object of File class to open xlsx or xls file
        File file = new File(filePath+"\\"+fileName);

        //Create an object of FileInputStream class to read excel file
        FileInputStream inputStream = new FileInputStream(file);

//        Workbook dataDrivenWorkbook = null;

        //Find the file extension by splitting  file name in substring and getting only extension name
//        String fileExtensionName = fileName.substring(fileName.indexOf("."));

//        //Check condition if the file is xlsx file
//        if(fileExtensionName.equals(".xls")){
//            //If it is xlsx file then create object of XSSFWorkbook class
//            dataDrivenWorkbook = new HSSFWorkbook(inputStream);
//        }
//        //Check condition if the file is xls file
//        else if(fileExtensionName.equals(".xls")){
//            //If it is xls file then create object of HSSFWorkbook class
//            dataDrivenWorkbook = new HSSFWorkbook(inputStream);
//        }
        HSSFWorkbook dataDrivenWorkbook = new HSSFWorkbook(inputStream);
        Sheet newsheet = dataDrivenWorkbook.createSheet(sheetName);

        //Close input stream
        inputStream.close();

        //Create an object of FileOutputStream class to create write data in excel file
        FileOutputStream outputStream = new FileOutputStream(file);

        //write data in the excel file
        dataDrivenWorkbook.write(outputStream);

        //close output stream
        outputStream.close();

    }

    public static void writeHotelDateData(HashMap<String, String> hotelDateValues, int rowNum, String appDataSheetName)
            throws IOException, ParseException {
        //create an object of the File class to open the config file
        File fileClassVar = new File(System.getProperty("user.dir")+"\\ExportExcel.xls");
        FileInputStream inputStream = new FileInputStream(fileClassVar);

        HSSFWorkbook dataDrivenWorkbook = new HSSFWorkbook(inputStream);

        // Get the sheet to write result data to
        Sheet sheet = dataDrivenWorkbook.getSheet(appDataSheetName);

        // Get the current row of the sheet to write result data to
        Row row = sheet.getRow(rowNum);

        // Now put the data from the HashMap table into the correct cells of the current row
        // check in date - change from String to Date value
        Date date1 = new SimpleDateFormat("MM/dd/yyyy").parse(hotelDateValues.get("CheckInDate"));
        Cell cell = row.getCell(4);
        cell.setCellValue(date1);

        // check out date
        Date date2 = new SimpleDateFormat("MM/dd/yyyy").parse(hotelDateValues.get("CheckOutDate"));
        cell = row.getCell(5);
        cell.setCellValue(date2);

        // write the composed row into the datasheet at the specified row
        // MUST CLOSE inputStream first; otherwise, will overwrite the entire sheet and lose everything!!
        inputStream.close();

        FileOutputStream outputStream = new FileOutputStream(fileClassVar);
        dataDrivenWorkbook.write(outputStream);

        // Now close the streams prior to exiting
        outputStream.close();
    }

/*
    public static void main(String...strings) throws IOException{

        //Create an array with the data in the same order in which you expect to be filled in excel file
        String[] valueToWrite = {"Mr. E","Noida"};

        //Create an object of current class
        WriteExcelFile objExcelFile = new WriteExcelFile();

        //Write the file using file name, sheet name and the data to be filled
        objExcelFile.writeExcelRow(System.getProperty("user.dir")+"\\src\\excelExportAndFileIO","ExportExcel.xlsx","ExcelGuru99Demo",valueToWrite);

    }
    */
}
