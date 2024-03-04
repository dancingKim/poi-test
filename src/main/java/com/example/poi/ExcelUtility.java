package com.example.poi;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ExcelUtility {
    static Map<String, List<RowInfo>> allTables;
    static List<RowInfo> allRows = new ArrayList<>();
    static HashMap<String, TableInfo> tables = new HashMap<>();
    static StringBuilder outputs = new StringBuilder();
    static private final String DEF_INPUT_FILE_PATH = "C:\\gitlab\\poiTest\\MLOP209TB.xlsx";
    static private final String DEF_OUT_DIR_PATH = "C:\\gitlab\\poiTest\\out\\";
    static String importFilePath = DEF_INPUT_FILE_PATH;
    static String exportDirPath = DEF_OUT_DIR_PATH;
    static int isReaded = 0;
    ExcelUtility() {

    }

    public static void readExcel() {
        readExcel(importFilePath);
    }

    public static void readExcel(String filePath){
        importFilePath = filePath;
        try {
            FileInputStream file = new FileInputStream(importFilePath);
            IOUtils.setByteArrayMaxOverride(Integer.MAX_VALUE);
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(0);

            int rowLength = sheet.getPhysicalNumberOfRows(); // 행 개수

            for (int i = 2; i < rowLength; i++) {
                Row row = sheet.getRow(i);
                RowInfo rowInfo = new RowInfo(row);
                allRows.add(rowInfo);
            }

            allTables = allRows.stream()
                    .collect(Collectors.groupingBy(RowInfo::getEngTbNm));

            allTables.forEach((key, value) -> tables.computeIfAbsent(key, k -> new TableInfo(k, value)));
            isReaded = 1;
            file.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeAllCreateQuery(BufferedWriter writer) throws IOException {
        if (isReaded == 0) readExcel(importFilePath);
        tables.values().forEach(v -> outputs.append(v.printCreateQuery()));
        System.out.println(outputs.toString());
        writer.write(outputs.toString());
    }
    public static void printAllCreateQuery(){
        if (isReaded == 0) readExcel(importFilePath);
        tables.values().forEach(v -> outputs.append(v.printCreateQuery()));
        System.out.println(outputs);
    }

    public static void exportToTxt() throws IOException {
        exportToTxt(exportDirPath);
    }

    public static void exportToTxt(String outDirPath) throws IOException {
        exportToTxt(importFilePath, outDirPath);
    }
    public static void exportToTxt(String inputFilePath, String outDirPath) throws IOException {
        if (isReaded == 0) readExcel(inputFilePath);
        String fileName = UUID.randomUUID().toString();
        String fileExtension = ".txt";
        exportDirPath = "".equals(outDirPath) ? DEF_OUT_DIR_PATH : outDirPath ;
        String filePath = exportDirPath + fileName + fileExtension; // 출력 파일 경로
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            try {
                writeAllCreateQuery(writer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}