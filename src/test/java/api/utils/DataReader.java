package api.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class DataReader {

    public static List<HashMap<String, String>> data(String filepath, String sheetName) throws IOException {
    List<HashMap<String, String>> mydata = new ArrayList<>();

    try (FileInputStream file = new FileInputStream(filepath);
         Workbook workbook = WorkbookFactory.create(file)) {
        
        Sheet sheet = workbook.getSheet(sheetName);
        DataFormatter formatter = new DataFormatter();
        Row headerRow = sheet.getRow(0);
        int totalRows = sheet.getLastRowNum();
        int totalCols = headerRow.getLastCellNum();

        String[] headers = new String[totalCols];
        for (int j = 0; j < totalCols; j++) {
            headers[j] = formatter.formatCellValue(headerRow.getCell(j));
        }

        for (int i = 1; i <= totalRows; i++) {
            Row currentRow = sheet.getRow(i);
            if (currentRow == null) continue;

            HashMap<String, String> currentHash = new HashMap<>();
            for (int j = 0; j < totalCols; j++) {
                Cell currentCell = currentRow.getCell(j);
                currentHash.put(headers[j], formatter.formatCellValue(currentCell));
            }
            mydata.add(currentHash);
        }
    } 
    return mydata;
}
}