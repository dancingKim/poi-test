package com.example.poi;

import org.apache.poi.ss.usermodel.Row;
public class RowInfo {
    String engTbNm = "";
    String krnTbNm = "";
    String colName = "";
    String dataType = "";
    String pk = "PRIMARY KEY";
    String setNull = "NOT NULL";
    String defaultValue = "DEAFULT 'N'";
    String comment = "";

    RowInfo() {
    }

    RowInfo(Row row){
        this.engTbNm = row.getCell(10).getStringCellValue();
        this.krnTbNm = "COMMENT '" + row.getCell(11).getStringCellValue() + "';";
        this.comment = "COMMENT '" + row.getCell(13).getStringCellValue() + "'";
        this.colName = row.getCell(14).getStringCellValue();
        this.dataType = row.getCell(15).getStringCellValue();
        if (!row.getCell(16).getBooleanCellValue()) this.pk = "";
        if (!row.getCell(17).getBooleanCellValue()) this.setNull = "NULL";
        if("".equals(row.getCell(19).getStringCellValue())) this.defaultValue = "";
    }

    public String printRow() {
        return colName + " " + dataType + " "
                + setNull + " " + pk + " " + defaultValue + " " + comment + ",";
    }

    public String printLastRow() {
        return colName + " " + dataType + " "
                + setNull + " " + pk + " " + defaultValue + " " + comment;
    }
    public String getEngTbNm() {
        return this.engTbNm;
    }

    public String getKrnTbNm(){
        return this.krnTbNm;
    }
}