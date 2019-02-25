package com.yesmywine.goods.util;

import com.yesmywine.util.basic.ValueUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;


/**
 * Twitter : @taylorwang789
 * Creat time : Apr 20, 2016    10:28:53 PM
 */
public class MSExcel {


    /**
     * Twitter : @taylorwang789
     * Creat time : Apr 20, 2016    11:28:59 PM
     *
     * @param excelFilePath
     * @param keyName       id,name,age,amt,cnt,createdate
     * @param keyType       number,string,number,number,number,date
     * @param startRow      start on row 3
     * @param startCol      start on col 4
     * @return
     */
    public static List<Map<String, Object>> parseExcel(String excelFilePath, String keyName, String keyType, int startRow, int startCol) {

        String key[] = keyName.split(",");
        String type[] = keyType.toLowerCase().split(",");
        String typeOrg[] = keyType.split(",");

        FileInputStream inputStream;
        Workbook workbook = null;
        try {
            inputStream = new FileInputStream(new File(excelFilePath));
            if (excelFilePath.toLowerCase().endsWith("xlsx")) {
                workbook = new XSSFWorkbook(inputStream);
            } else if (excelFilePath.toLowerCase().endsWith("xls")) {
                workbook = new HSSFWorkbook(inputStream);
            } else {
                System.out.println("not excel file ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Map<String, Object>> workbook_lsit = new ArrayList<>();

        //  sheets
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            Sheet firstSheet = workbook.getSheetAt(i);
            Iterator<Row> rowIterator = firstSheet.iterator();
            for (int j = 1; j < startRow; j++) {
                rowIterator.next();
            }
            // loop  rows
            while (rowIterator.hasNext()) {
                Map<String, Object> rowMap = new HashMap<>();
                Row row = rowIterator.next();
                // cells
                for (int k = 0; k < type.length; k++) {

                    Cell cell = row.getCell(k + startCol, Row.CREATE_NULL_AS_BLANK);
                    String currentType = type[k];

                    switch (currentType) {
                        case "number":
                            if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                                rowMap.put(key[k], Integer.parseInt(cell.getStringCellValue()));
                            } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                rowMap.put(key[k], cell.getNumericCellValue());
                            } else {
                                rowMap.put(key[k], 0);
                            }
                            break;
                        case "string":
                            if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                                rowMap.put(key[k], cell.getStringCellValue());
                            } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                rowMap.put(key[k], cell.getNumericCellValue() + "");
                            } else {
                                rowMap.put(key[k], " ");
                            }
                            break;
                        case "boolean":
                            if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
                                rowMap.put(key[k], cell.getBooleanCellValue());
                            } else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                                rowMap.put(key[k], cell.getStringCellValue());
                            } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                double num = cell.getNumericCellValue();
                                if (num == 0) {
                                    rowMap.put(key[k], false);
                                } else {
                                    rowMap.put(key[k], true);
                                }
                            } else {
                                rowMap.put(key[k], false);
                            }
                            break;
                        case "skip":
                            break;
                        default:
                            rowMap.put(key[k], typeOrg[k]);
                            break;
                    }
//    	        	  }
                } // end cells
                System.out.println(ValueUtil.toJson(rowMap));
                workbook_lsit.add(rowMap);
            } // end rows
        } // end sheet
        return workbook_lsit;
    }


}
