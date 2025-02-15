package UtilitiesFactory;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ExcelFactory {

    private static final String FILE_PATH = "./src/test/resources/testdata.xlsx";
    private static final ThreadLocal<Map<String, List<Map<String, String>>>> excelCache = ThreadLocal.withInitial(ConcurrentHashMap::new);

    // Get all data for a sheet
    public static synchronized List<Map<String, String>> getData(String sheetName) {
        return excelCache.get().computeIfAbsent(sheetName, s -> readExcel(sheetName));
    }

    // Get value from the first row for a given column
    public static String getValue(String sheetName, String columnName) {
        return getValue(sheetName, 0, columnName);
    }

    // Get value from a specific row and column
    public static String getValue(String sheetName, int rowIndex, String columnName) {
        List<Map<String, String>> data = getData(sheetName);
        if (rowIndex < data.size()) {
            return data.get(rowIndex).getOrDefault(columnName, "");
        }
        throw new IllegalArgumentException("Invalid row index or column name: " + sheetName + ", " + columnName);
    }

    // Read data from the Excel file
    private static List<Map<String, String>> readExcel(String sheetName) {
        List<Map<String, String>> dataList = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(FILE_PATH);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) throw new IllegalArgumentException("Sheet not found: " + sheetName);

            // Print sheet name
            System.out.println("Reading data from sheet: " + sheetName);

            // Read header row
            Row headerRow = sheet.getRow(0);
            List<String> headers = new ArrayList<>();
            for (Cell cell : headerRow) {
                String header = cell.getStringCellValue();
                headers.add(header);
                System.out.println("Header found: " + header);
            }

            // Read data rows
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Map<String, String> rowData = new HashMap<>();
                for (int j = 0; j < headers.size(); j++) {
                    Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    String cellValue = getCellValue(cell);
                    System.out.println("Row " + i + ", Column '" + headers.get(j) + "' => " + cellValue);
                    rowData.put(headers.get(j), cellValue);
                }
                dataList.add(rowData);
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to read Excel file: " + FILE_PATH, e);
        }

        return dataList;
    }


    // Get cell value as String (Java 8 compatible, with POI version handling)
    private static String getCellValue(Cell cell) {
        switch (cell.getCellType()) { // Compatible with POI 3.x and Java 8
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }

    // Clear cache after execution
    public static void clearCache() {
        excelCache.remove();
    }
}
