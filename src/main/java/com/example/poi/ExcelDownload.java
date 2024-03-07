package com.example.poi;

import java.io.IOException;

public class ExcelDownload {

    public static void main(String[] args) throws IOException {
        FormatConverter fc = new FormatConverter();

        fc.downloadExcelFile();

    }
}