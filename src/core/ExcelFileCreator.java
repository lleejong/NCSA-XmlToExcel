package core;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import model.DataModel;
import ui.Main;

public class ExcelFileCreator {

	private static int rowCounter = 1;
	private static XSSFWorkbook workbook;
	private static XSSFSheet sheet;

	private static void createHeader() {
		XSSFRow head = sheet.createRow(0);
		head.createCell(0).setCellValue(new XSSFRichTextString("Case ID"));
		head.createCell(1).setCellValue(new XSSFRichTextString("Crash Date"));
		head.createCell(2).setCellValue(new XSSFRichTextString("Crash Time"));
		head.createCell(3).setCellValue(new XSSFRichTextString("Crash Type"));
		head.createCell(4).setCellValue(new XSSFRichTextString("Configuration"));
		head.createCell(5).setCellValue(new XSSFRichTextString("Vehicle NO."));
		head.createCell(6).setCellValue(new XSSFRichTextString("Posted Speed Limit"));
		head.createCell(7).setCellValue(new XSSFRichTextString("Crash Type Code"));
		head.createCell(8).setCellValue(new XSSFRichTextString("Crash Type"));
		head.createCell(9).setCellValue(new XSSFRichTextString("Total"));
		head.createCell(10).setCellValue(new XSSFRichTextString("LongItudlnal"));
		head.createCell(11).setCellValue(new XSSFRichTextString("Lateral"));
		head.createCell(12).setCellValue(new XSSFRichTextString("Barrier Equivalent Speed"));
		head.createCell(13).setCellValue(new XSSFRichTextString("Travel Lanes"));
		head.createCell(14).setCellValue(new XSSFRichTextString("Alignment"));
		head.createCell(15).setCellValue(new XSSFRichTextString("Profile"));
		head.createCell(16).setCellValue(new XSSFRichTextString("Surface Type"));
		head.createCell(17).setCellValue(new XSSFRichTextString("Surface Condition"));
		head.createCell(18).setCellValue(new XSSFRichTextString("Light"));
		head.createCell(19).setCellValue(new XSSFRichTextString("Weather"));
		head.createCell(20).setCellValue(new XSSFRichTextString("Scene Diagram"));
	}

	private static void processRow(DataModel model) {
		int numVehicles = model.numVehicles;

		int startRow = rowCounter;

		for (int i = 0; i < numVehicles; i++)
			sheet.createRow(rowCounter++);

		int endRow = rowCounter - 1;
		if (numVehicles > 1) {
			sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 0, 0));
			sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 1, 1));
			sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 2, 2));
			sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 3, 3));
			sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 4, 4));
			sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, 20, 20));
		}

		sheet.getRow(startRow).createCell(0).setCellValue(model.caseID);
		sheet.getRow(startRow).createCell(1).setCellValue(model.crashDate);
		sheet.getRow(startRow).createCell(2).setCellValue(model.crashTime);
		sheet.getRow(startRow).createCell(3).setCellValue(model.crashType);
		sheet.getRow(startRow).createCell(4).setCellValue(model.configuration);
		XSSFCell imgCell = sheet.getRow(startRow).createCell(20);
		imgCell.setCellValue("[Show Image]");

		CreationHelper createHelper = workbook.getCreationHelper();
		XSSFHyperlink link = (XSSFHyperlink) createHelper.createHyperlink(HyperlinkType.FILE);
		model.imgPath = model.imgPath.replace("\\", "/");
		link.setAddress(model.imgPath);

		imgCell.setHyperlink(link);

		for (int i = 0; i < numVehicles; i++) {
		
			sheet.getRow(startRow + i).createCell(5).setCellValue(model.vehicleNo[i]);
			sheet.getRow(startRow + i).createCell(6).setCellValue(model.postedSpeedLimit[i]);

			if (!model.crashTypeCode[i].equals(" ")) {
				XSSFCell codeImgCell = sheet.getRow(startRow + i).createCell(7);
				codeImgCell.setCellValue("[" + model.crashTypeCode[i] + "]");

				XSSFHyperlink codeLink = (XSSFHyperlink) createHelper.createHyperlink(HyperlinkType.FILE);
				model.crashTypeCodeImgPath[i] = model.crashTypeCodeImgPath[i].replace("\\", "/");
				codeLink.setAddress(model.crashTypeCodeImgPath[i]);
				codeImgCell.setHyperlink(codeLink);
			}
			else{
				sheet.getRow(startRow + i).createCell(7).setCellValue(model.crashTypeCode[i]);	
			}

			
			sheet.getRow(startRow + i).createCell(8).setCellValue(model.vehicleCrashType[i]);
			sheet.getRow(startRow + i).createCell(9).setCellValue(model.total[i]);
			sheet.getRow(startRow + i).createCell(10).setCellValue(model.longItudlnal[i]);
			sheet.getRow(startRow + i).createCell(11).setCellValue(model.lateral[i]);
			sheet.getRow(startRow + i).createCell(12).setCellValue(model.barrierEquivalent[i]);
			sheet.getRow(startRow + i).createCell(13).setCellValue(model.travelLanes[i]);
			sheet.getRow(startRow + i).createCell(14).setCellValue(model.alignment[i]);
			sheet.getRow(startRow + i).createCell(15).setCellValue(model.profile[i]);
			sheet.getRow(startRow + i).createCell(16).setCellValue(model.surfaceType[i]);
			sheet.getRow(startRow + i).createCell(17).setCellValue(model.surfaceCondition[i]);
			sheet.getRow(startRow + i).createCell(18).setCellValue(model.light[i]);
			sheet.getRow(startRow + i).createCell(19).setCellValue(model.weather[i]);
		}

	}

	public static void createExcelFile(ArrayList<DataModel> models, String path) {
		//System.out.println("Start To create Excel File");
		workbook = new XSSFWorkbook();
		sheet = workbook.createSheet();

		createHeader();

		int cnt = 0;
		int percentCount = 1;
		int percent = models.size()/10;
		Main.log("0%..");
		for (DataModel model : models) {
			if(cnt > percentCount * percent){
				Main.log(percentCount * 10+"%..");
				percentCount++;
			}
			processRow(model);
			cnt++;
		}
		Main.logln("");

//		System.out.println("Start to auto Size..");
//		XSSFRow head = sheet.getRow(0);
//
//		for (int i = 0; i < head.getLastCellNum(); i++) {
//			long startTime = System.currentTimeMillis();
//			System.out.println("--" + i);
//			sheet.autoSizeColumn(i);
//			long endTime = System.currentTimeMillis();
//			System.out.println((endTime - startTime) / 1000.0 + " sec.");
//		}
//
		try {
			FileOutputStream outfile = new FileOutputStream(path);
			workbook.write(outfile);
			outfile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
}
