package com.example.poi;

import java.util.ArrayList;
import java.util.List;
public class TableInfo {
    List<RowInfo> rows = new ArrayList<>();
    String engTbNm;
    String krnTbNm;

    public TableInfo () {}

    public TableInfo(String tableName, List<RowInfo> rows){
        this.engTbNm = tableName;
        this.rows.addAll(rows);
        this.krnTbNm = this.rows.get(0).getKrnTbNm();
    }
    public void addRow (RowInfo row){
        this.rows.add(row);
    }
    public String printCreateQuery(){
        List<RowInfo> rows = this.rows;
        String engTbNm = this.engTbNm;
        String krnTbNm = this.krnTbNm;

        StringBuilder output = new StringBuilder("CREATE TABLE LMS." + engTbNm + " (" + "\n");
        int rowLength = rows.size();
        for (int i = 0 ; i < rowLength ; i++){
            if (i != rowLength - 1)
                output.append("\t").append(rows.get(i).printRow()).append("\n");
            else
                output.append("\t").append(rows.get(i).printLastRow()).append("\n");
        }

        output.append(") ").append(krnTbNm).append("\n");
        return output.toString();
    }
}