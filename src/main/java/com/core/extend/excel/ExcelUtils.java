package com.core.extend.excel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

public class ExcelUtils {
	

	private static final String EXCEL_XLS = "xls";
	private static final String EXCEL_XLSX = "xlsx";
	
	/**
	 * 根据excel的版本，获取相应的Workbook
	 *
	 * @param file
	 * @return
	 */
	public static Workbook getWorkbook(MultipartFile file) throws IOException {
		Workbook wb = null;
		InputStream fis = file.getInputStream();
		if (file.getOriginalFilename().endsWith(EXCEL_XLS)) //2003
		{
			wb = new HSSFWorkbook(fis);
		} else if (file.getOriginalFilename().endsWith(EXCEL_XLSX)) {
			wb = new XSSFWorkbook(fis);//2007 2010
		}
		if (fis != null) {
			fis.close();
		}
		return wb;
	}

	/**
	 * 读取给定sheet的内容
	 * 描述：
	 * 读取excel文件中的指定名称的表格 用于没有单元格合并的表格，且 (titleOfRow,titleOfColumn)位置为读取内容的起始位置的情况
	 * 每一行构成一个map(key值是列标题，value是列值)。没有值的单元格其value值为null。
	 * 返回结果最外层的list对应一个sheet页，第二层的map对应sheet页中的一行。
	 *
	 * @param sheet
	 * @return
	 */
	private static List<Map<Integer, Object>> readSheet(Sheet sheet, int titleInRow, int titleInColumn, Workbook book) {
		List<Map<Integer, Object>> sheetList = new ArrayList<Map<Integer, Object>>();
		List<String> titles = new ArrayList<>();
		int rowSize = sheet.getLastRowNum() + 1;
		for (int i = titleInRow; i < rowSize; i++) {
			Row row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			int cellSize = row.getLastCellNum();
			if (i == titleInRow) //标题行
			{
				for (int j = titleInColumn; j < cellSize; j++) {
					Cell cell = row.getCell(j);
					if (cell != null) {
						titles.add(cell.toString());
					}
				}
			} else { //对应每一行的数据
				Map<Integer, Object> rowDataMap = new LinkedHashMap<Integer, Object>();
				for (int j = titleInColumn; j < titleInColumn + titles.size(); j++) {
					Cell cell = row.getCell(j);
					String value = null;
					CellType cellType = null;
					if (cell == null) {
						continue;
					}
					cellType = cell.getCellTypeEnum();
					switch (cellType) {
					case STRING:
						value = superOrSubScript2007(cell,book);
						break;
					case NUMERIC: //包含日期和普通数字
					if (DateUtil.isCellDateFormatted(cell)) {
						Date date = cell.getDateCellValue();
						DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
						value = df.format(date);
					} else {
						double cellValue = cell.getNumericCellValue();
						value = String.valueOf(cellValue);
					}
					break;
					case FORMULA:
						cell.setCellType(CellType.STRING);
						value = cell.getStringCellValue();
						break;
					case BOOLEAN:
						value = String.valueOf(cell.getBooleanCellValue());
						break;
					default:
						if (cell != null) {
							value = cell.toString();
						}
					}
//					String key = titles.get(j - titleInColumn);
					rowDataMap.put(j, value);
					rowDataMap.put(999, sheet.getSheetName());
				}
				sheetList.add(rowDataMap);
			}
		}
		return sheetList;
	}


	public static String superOrSubScript2007(Cell cell, Workbook wb) {
		RichTextString richtextstring = cell.getRichStringCellValue();
		String textstring = richtextstring.getString();
		StringBuffer htmlstring = new StringBuffer();
		if (richtextstring.numFormattingRuns() > 0) { // we have formatted runs
			if (richtextstring instanceof HSSFRichTextString) { // HSSF does not count first run as formatted run when it does not have formatting
				if (richtextstring.getIndexOfFormattingRun(0) != 0) { // index of first formatted run is not at start of text
					String textpart = textstring.substring(0, richtextstring.getIndexOfFormattingRun(0)); // get first run from index 0 to index of first formatted run
					Font font = wb.getFontAt(cell.getCellStyle().getFontIndex()); // use cell font
					htmlstring.append(getHTMLFormatted(textpart, font));
				}
			}
			for (int i = 0; i < richtextstring.numFormattingRuns(); i++) { // loop through all formatted runs
				// get index of frormatting run and index of next frormatting run
				int indexofformattingrun = richtextstring.getIndexOfFormattingRun(i);
				int indexofnextformattingrun = textstring.length();
				if ((i+1) < richtextstring.numFormattingRuns()) indexofnextformattingrun = richtextstring.getIndexOfFormattingRun(i+1);
				// formatted text part is the sub string from index of frormatting run to index of next frormatting run
				String textpart = textstring.substring(indexofformattingrun, indexofnextformattingrun);
				// determine used font
				Font font = null;
				if (richtextstring instanceof XSSFRichTextString) {
					font = ((XSSFRichTextString)richtextstring).getFontOfFormattingRun(i);
					// font might be null if no formatting is applied to the specified text run
					// then font of the cell should be used.
					if (font == null) font = wb.getFontAt(cell.getCellStyle().getFontIndex());
				} else if (richtextstring instanceof HSSFRichTextString) {
					short fontIndex = ((HSSFRichTextString)richtextstring).getFontOfFormattingRun(i);
					// font index might be HSSFRichTextString.NO_FONT if no formatting is applied to the specified text run
					// then font of the cell should be used.
					if (fontIndex == HSSFRichTextString.NO_FONT) {
						font = wb.getFontAt(cell.getCellStyle().getFontIndex());
					} else {
						font = wb.getFontAt(fontIndex);
					}
				}
				htmlstring.append(getHTMLFormatted(textpart, font));
			}
		} else {
			Font font = wb.getFontAt(cell.getCellStyle().getFontIndex());
			htmlstring.append(getHTMLFormatted(textstring, font));
		}
		return htmlstring.toString();
	}

	static StringBuffer getHTMLFormatted(String textpart, Font font) {
		StringBuffer htmlstring = new StringBuffer();
//		boolean wasbold = false;
//		boolean wasitalic = false;
//		boolean wasunderlined = false;
		boolean wassub = false;
		boolean wassup = false;
		if (font != null) {
//			if (font.getBold() ) {
//				htmlstring.append("<b>");
//				wasbold = true;
//			}
//			if (font.getItalic()) {
//				htmlstring.append("<i>");
//				wasitalic = true;
//			}
//			if (font.getUnderline() == Font.U_SINGLE) {
//				htmlstring.append("<u>");
//				wasunderlined = true;
//			}
			if (font.getTypeOffset() == Font.SS_SUB) {
				htmlstring.append("<sub>");
				wassub = true;
			}
			if (font.getTypeOffset() == Font.SS_SUPER) {
				htmlstring.append("<sup>");
				wassup = true;
			}
		}

		htmlstring.append(textpart);

		if (wassup) {
			htmlstring.append("</sup>");
		}
		if (wassub) {
			htmlstring.append("</sub>");
		}
//		if (wasunderlined) {
//			htmlstring.append("</u>");
//		}
//		if (wasitalic) {
//			htmlstring.append("</i>");
//		}
//		if (wasbold) {
//			htmlstring.append("</b>");
//		}
		return htmlstring;
	}


	/**
	 * 读取excel文件中的全部表格
	 * 描述：适用于没有单元格合并的excel，并且 (titleOfRow,titleOfColumn)位置为读取内容的起始位置的情况
	 * 每一行构成一个map，key值是列标题，value是列值。没有值的单元格其value值为null
	 * 返回结果最外层list对应excel文件，第二层Iterable对应sheet页，第三层map对应sheet页中一行
	 *
	 * @param filePath 文件路径
	 * @param sheetCnt 读取的文件中前sheetCnt个sheet数据。如果值为-1，则读取所有的sheet，否则，读取前sheetCnt个sheet的数据。
	 * @return
	 * @throws Exception
	 */
	public static List<Map<Integer, Object>> readExcelWithFixPos(MultipartFile file, int titleInRow, int titleInColumn, int sheetCnt) throws IOException {
		Workbook wb = null;
		try {
			List<Map<Integer, Object>> excelData = new ArrayList<>();
			if (file.getOriginalFilename().endsWith(EXCEL_XLS) || file.getOriginalFilename().endsWith(EXCEL_XLSX)) {
				wb = getWorkbook(file);
				int sheetSize = sheetCnt == -1 ? wb.getNumberOfSheets() : sheetCnt;
				//遍历sheet
				for (int i = 0; i < sheetSize; i++) {
					Sheet sheet = wb.getSheetAt(i);
					List<Map<Integer, Object>> theSheetData = readSheet(sheet, titleInRow, titleInColumn,wb);
					excelData.addAll(theSheetData);
				}
			}
			return excelData;
		} catch (FileNotFoundException e) {
			throw e;
		} finally {
			if (wb != null) {
				wb.close();
			}
		}
	}

	public static void main(String[] args) throws FileNotFoundException, IOException {
//		String path="D:\\work\\五院\\环境探测\\需求分析阶段\\20230826\\数据\\23.9.25 PM数据定稿(1).xlsx";
//		List<Map<Integer, Object>> l = readExcelWithFiexPos(path,0,0,-1);
//		for (int i = 0; i < l.size(); i++) { 
//		}
		
	}
}