package com.example.poi;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.file.Path;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class FormatConverter {
    private static Excel.File excelFile;
    static ArrayList<File> txtFiles = new ArrayList<>();
    static String exportDirPath = "C:\\gitlab\\poiTest\\make-excel-test\\";
    public void downloadExcelFile() throws IOException {

        readTxtFiles("C:\\gitlab\\poiTest\\table-queries\\");
        filesToClassData();
        createExcelFile();
    }
    private void readTxtFiles(String dirPath) throws IOException {
        txtFiles.addAll(
                Files.walk(Paths.get(dirPath), 1)
                        .filter(Files::isRegularFile)
                        .filter(path -> path.toString().endsWith(".txt"))
                        .map(Path::toFile)
                        .toList()
        );
    }

    private void filesToClassData() throws IOException {
        ArrayList<Excel.Table> tables = new ArrayList<>(txtFiles.size());
        for (File file : txtFiles) {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            ArrayList<Excel.Row> rows = new ArrayList<>();

            int tableRowIdx = 0;

            while ((line = br.readLine()) != null) {
                ArrayList<Excel.Cell> cells = new ArrayList<>(11);
                String[] values = line.split(" ");
                int valueLength = values.length;
                StringBuilder setColumn06 = new StringBuilder();
                StringBuilder setColumn07 = new StringBuilder();

                if ("MIDDLE".equals(checkLineLocation(values))){
                    cells.add(Excel.Cell.of(4, values[0].trim(), "컬럼영문명"));
                    cells.add(Excel.Cell.of(5, values[1], "데이터타입"));
                    cells.add(Excel.Cell.of(3,
                            values[valueLength -1].substring(1, values[valueLength -1].lastIndexOf("'")),
                            "컬럼한글명"));
                    for (int i = 2; i < valueLength - 1; i++) {
                        checkColumn(values[i], cells, setColumn06, setColumn07);
                    }
                } else if ("FIRST".equals(checkLineLocation(values))){
                    cells.add(Excel.Cell.of(0,values[2].substring(4), "테이블영문"));
                } else if("LAST".equals(checkLineLocation(values))){
                    cells.add(Excel.Cell.of(1,values[2].substring(1, values[2].length() - 2), "테이블한글"));
                }

                if ("PRIMARY".contentEquals(setColumn06))
                    cells.add(Excel.Cell.of(6, "TRUE", "PK 여부"));
                else if ("".contentEquals(setColumn06))
                    cells.add(Excel.Cell.of(6, "FALSE", "PK 여부"));

                cells.sort(Comparator.comparingInt(Excel.Cell::getColIdx));
                rows.add(Excel.Row.of(cells, tableRowIdx));
                tableRowIdx++;
            }
            rows.sort(Comparator.comparingInt(Excel.Row :: getRowIdx));
            tables.add(Excel.Table.of(rows));
        }

        excelFile = Excel.File.of(tables);
    }
    private String checkLineLocation(String[] values){
        if("CREATE".equals(values[0].trim()) && "TABLE".equals(values[1].trim())){
            return "FIRST";
        } else if (")".equals(values[0].trim())){
            return "LAST";
        }
        return "MIDDLE";
    }

    private void checkColumn(String value, List<Excel.Cell> cells, StringBuilder setColumn06, StringBuilder setColumn07) {

        if ("NOT".equals(value)) {
            setColumn07.append(value);
        } else if ("NULL".equals(value)){
            if ("NOT".contentEquals(setColumn07))
                cells.add(Excel.Cell.of(7,"TRUE", "NOT NULL"));
            else
                cells.add(Excel.Cell.of(7,"FALSE", "NOT NULL"));
        }else if ("PRIMARY".equals(value)){
            setColumn06.append(value);
        } else if ("DEFAULT".equals(value)){
            cells.add(Excel.Cell.of(9,"default : N", "사용자 정의"));
        }
    }
    private void createExcelFile() throws IOException {
        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("TO-BE TABLE");
        int excelRowNum = 0;

        excelRowNum = setHeader(sheet, excelRowNum);
        setBody(sheet, excelRowNum);

        String fileExtension = ".xlsx";
        String filename = UUID.randomUUID().toString();
        String exportFilePath = exportDirPath + filename + fileExtension;

        try (FileOutputStream outputStream = new FileOutputStream(exportFilePath)){
            workbook.write(outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        workbook.close();
    }

    private int setHeader(Sheet sheet, int excelRowNum){
        Row row0 = sheet.createRow(excelRowNum++);

        row0.createCell(0).setCellValue("TO-BE 테이블영문");
        row0.createCell(1).setCellValue("TO-BE 테이블한글");
        row0.createCell(2).setCellValue("컬럼");
        row0.createCell(3).setCellValue("컬럼한글명");
        row0.createCell(4).setCellValue("컬럼영문명");
        row0.createCell(5).setCellValue("데이터타입");
        row0.createCell(6).setCellValue("PK 여부");
        row0.createCell(7).setCellValue("NOT NULL");
        row0.createCell(8).setCellValue("COMMENT");
        row0.createCell(9).setCellValue("사용자저의(fixed value)");
        row0.createCell(10).setCellValue("비고");
        row0.createCell(11).setCellValue("컬럼 변경 Y/N");

        return excelRowNum;
    }
    private void setBody(Sheet sheet, int excelRowNum){
        for (Excel.Table table : excelFile.getTables()) {

            List<Excel.Row> rows = table.getRows();
            String engTbNm = table.getEngTbNm();
            String krnTbNm = table.getKrnTbNm();

            for (int i = 0; i < rows.size(); i++){

                Row row = sheet.createRow(excelRowNum++);
                Excel.Row line = rows.get(i);
                Cell cell0 = row.createCell(0);
                cell0.setCellValue(engTbNm);
                Cell cell1 = row.createCell(1);
                cell1.setCellValue(krnTbNm);

                line.getCells().forEach(
                        (col) -> {
                            Cell cell = row.createCell(col.getColIdx());
                            cell.setCellValue(col.getValue());
                        }
                );
            }
        }
    }
}