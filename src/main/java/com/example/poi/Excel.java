package com.example.poi;


import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class Excel {


    @Builder
    @Getter
    static class File{
        List<Table> tables;

        int numberOfTables;

        public static Excel.File of (List<Excel.Table> tables){
            return File.builder()
                    .tables(tables)
                    .numberOfTables(tables.size())
                    .build();
        }
    }


    @Getter
    @Builder
    static class Row {
        List<Cell> cells;
        int rowIdx;
        public static Row of (List<Cell> cells, int rowIdx){
            return Row.builder()
                    .cells(cells)
                    .rowIdx(rowIdx)
                    .build();
        }
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    static class Cell {
        String value;
        int colIdx;
        String title;


        public static Cell of (int colIdx, String value, String title){
            return Cell.builder()
                    .colIdx(colIdx)
                    .value(value)
                    .title(title)
                    .build();
        }

        public String toString(){
            return "column : "+this.colIdx +" title : "+ this.title + " value : " + this.value;
        }

        @Override
        public boolean equals(Object obj){
            return ((Cell)obj).toString().equals(this.toString());
        }
    }

    @Getter
    @Builder
    static class Table {
        String engTbNm;
        String krnTbNm;
        List<Row> rows;

        public static Table of (List<Row> rows){
            return Table.builder()
                    .rows(rows.subList(1,rows.size() - 1))
                    .engTbNm(rows.get(0).getCells().get(0).value)
                    .krnTbNm(rows.get(rows.size() - 1).getCells().get(0).value)
                    .build();
        }
    }
}