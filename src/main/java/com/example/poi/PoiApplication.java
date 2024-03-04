package com.example.poi;

import java.io.IOException;

public class PoiApplication {
	public static void main(String[] args) {
		ExcelUtility.exportAllToEachTxt("C:\\gitlab\\poiTest\\inTest2\\TO-BE.xlsx","C:\\gitlab\\poiTest\\table-queries\\");
	}
}