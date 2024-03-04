package com.example.poi;

import java.io.IOException;

public class PoiApplication {
	public static void main(String[] args) {
		try {
			ExcelUtility.exportToTxt();
			ExcelUtility.exportToTxt("C:\\gitlab\\poiTest\\outTest1\\");
			ExcelUtility.exportToTxt("C:\\gitlab\\poiTest\\inTest1\\MLOP209TB.xlsx","C:\\gitlab\\poiTest\\outTest2\\");
			ExcelUtility.exportToTxt("C:\\gitlab\\poiTest\\inTest2\\MLOP209TB.xlsx","C:\\gitlab\\poiTest\\outTest2\\");
		} catch (IOException ignored) {
		}
	}
}