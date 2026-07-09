package api.utils;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataReaderCSV {

    public static List<HashMap<String, String>> data(String filepath) throws IOException, CsvValidationException {
        List<HashMap<String, String>> myData = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(filepath))) {
            String[] headers = reader.readNext();
            if (headers == null) return myData;

            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                HashMap<String, String> currentHash = new HashMap<>();
                for (int i = 0; i < headers.length; i++) {
                    String value = (i < nextLine.length) ? nextLine[i] : "";
                    currentHash.put(headers[i], value);
                }
                myData.add(currentHash);
            }
        }
        return myData;
    }
}