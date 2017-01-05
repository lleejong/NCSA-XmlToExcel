package core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import model.DataModel;

public class XMLConverter {

	public static final String WORKDIR = "C:\\Users\\lleej\\Desktop\\sample\\2010\\";
	public static final String XML_FOLDER = "xml\\";
	public static final String IMG_FOLDER = "jpg\\";
	public static final String CODE_IMG_FOLDER = "./code/";

	private ArrayList<String> xmlFileList;
	private ArrayList<String> imgFileList;
	private ArrayList<DataModel> dataModels;

	public XMLConverter() {
		xmlFileList = new ArrayList<String>();
		imgFileList = new ArrayList<String>();
		dataModels = new ArrayList<DataModel>();

		File xmlWorkDir = new File(WORKDIR + XML_FOLDER);
		File imgWorkDir = new File(WORKDIR + IMG_FOLDER);
		String imgRelativePath = "./jpg/";
		readFileList(xmlWorkDir, imgWorkDir, imgRelativePath);
		System.out.println("Total : " + xmlFileList.size() + " xml files.");
		if (xmlFileList.size() <= 0) {
			System.err.println("XML 파일이 해당 경로에 없습니다.");
			System.exit(1);
		}

		parseXMLfiles();
		ExcelFileCreator.createExcelFile(dataModels, WORKDIR + "result.xlsx");
	}

	private void readFileList(File xmlWorkDir, File imgWorkDir, String imgRelativePath) {
		String[] filelist = xmlWorkDir.list();
		for (String filename : filelist) {
			File xmlFile = new File(xmlWorkDir.getAbsolutePath() + "\\" + filename);
			if (xmlFile.isDirectory()) {
				File imgFile = new File(imgWorkDir.getAbsolutePath() + "\\" + filename);
				imgRelativePath += "/" + filename;
				readFileList(xmlFile, imgFile, imgRelativePath);
			} else {
				if (filename.endsWith(".xml")) {

					File imgFile = new File(imgWorkDir.getAbsolutePath() + "\\" + filename.split("\\.")[0] + ".jpg");
					if (!imgFile.exists()) {
						System.out.println(imgFile.toString() + " is not existed.");
					} else {
						xmlFileList.add(xmlWorkDir.getAbsolutePath() + "\\" + filename);
						imgFileList.add(imgRelativePath + "/" + filename.split("\\.")[0] + ".jpg");
						// imgFileList.add(imgWorkDir.getAbsolutePath() + "\\" +
						// filename.split("\\.")[0] + ".jpg");
					}
				}
			}
		}
	}

	private void parseXMLfiles() {

		for (int i = 0; i < xmlFileList.size(); i++) {
			DataModel newModel = extractDataModel(xmlFileList.get(i));
			newModel.imgPath = imgFileList.get(i);
			dataModels.add(newModel);
		}
	}

	private DataModel extractDataModel(String path) {
		DataModel newModel = null;

		try {
			DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
			DocumentBuilder parser = f.newDocumentBuilder();

			Document doc = parser.parse(path);

			newModel = new DataModel();
			Element root = doc.getDocumentElement();
			newModel.caseID = root.getAttribute("CaseStr");

			newModel.crashType = root.getElementsByTagName("CrashType").item(0).getTextContent();
			newModel.crashDate = root.getElementsByTagName("CrashDate").item(0).getTextContent();
			newModel.crashTime = root.getElementsByTagName("CrashTime").item(0).getTextContent();
			newModel.configuration = root.getElementsByTagName("Configuration").item(0).getTextContent();

			newModel.numVehicles = Integer.parseInt(root.getElementsByTagName("NumberVehicles").item(0).getAttributes().getNamedItem("value").getTextContent());
			newModel.vehicleNo = new String[newModel.numVehicles];
			newModel.postedSpeedLimit = new String[newModel.numVehicles];
			newModel.crashTypeCode = new String[newModel.numVehicles];
			newModel.crashTypeCodeImgPath = new String[newModel.numVehicles];
			newModel.vehicleCrashType = new String[newModel.numVehicles];
			newModel.total = new String[newModel.numVehicles]; 
			newModel.longItudlnal = new String[newModel.numVehicles];
			newModel.lateral = new String[newModel.numVehicles];
			newModel.barrierEquivalent = new String[newModel.numVehicles];
			newModel.travelLanes = new String[newModel.numVehicles];
			newModel.alignment = new String[newModel.numVehicles];
			newModel.profile = new String[newModel.numVehicles];
			newModel.surfaceType = new String[newModel.numVehicles];
			newModel.surfaceCondition = new String[newModel.numVehicles];
			newModel.light = new String[newModel.numVehicles];
			newModel.weather = new String[newModel.numVehicles];

			System.out.println(newModel.caseID);
			for (int i = 0; i < newModel.numVehicles; i++) {
				newModel.vehicleNo[i] = root.getElementsByTagName("GeneralVehicleForm").item(i).getAttributes().getNamedItem("VehicleNumber").getTextContent();

				newModel.postedSpeedLimit[i] = root.getElementsByTagName("PostedSpeedLimit").item(i).getTextContent();
				if (!newModel.postedSpeedLimit[i].equals("Unknown") && !newModel.postedSpeedLimit[i].equals(""))
					newModel.postedSpeedLimit[i] += " " + root.getElementsByTagName("PostedSpeedLimit").item(i).getAttributes().getNamedItem("UOM").getTextContent();

				newModel.vehicleCrashType[i] = root.getElementsByTagName("CrashType").item(i + 1).getAttributes().getNamedItem("ConfigCatStr").getTextContent();
				
				
				if(root.getElementsByTagName("resource").getLength() - 1 < i)
					newModel.crashTypeCode[i] = " ";
				else{
					newModel.crashTypeCode[i] = root.getElementsByTagName("resource").item(i).getTextContent();
					newModel.crashTypeCodeImgPath[i] = CODE_IMG_FOLDER + newModel.crashTypeCode[i] + ".bmp";
				}
				
				newModel.travelLanes[i] = root.getElementsByTagName("TravelLanes").item(i).getTextContent();
				newModel.alignment[i] = root.getElementsByTagName("Alignment").item(i).getTextContent();
				newModel.profile[i] = root.getElementsByTagName("Profile").item(i).getTextContent();
				newModel.surfaceType[i] = root.getElementsByTagName("SurfaceType").item(i).getTextContent();
				newModel.surfaceCondition[i] = root.getElementsByTagName("SurfaceCondition").item(i).getTextContent();
				newModel.light[i] = root.getElementsByTagName("Light").item(i).getTextContent();
				newModel.weather[i] = root.getElementsByTagName("Weather").item(i).getTextContent();
				
				newModel.total[i] = root.getElementsByTagName("Total").item(i).getTextContent();
				if (!newModel.total[i].equals("Unknown") && !newModel.total[i].equals(""))
					newModel.total[i] += " " + root.getElementsByTagName("Total").item(i).getAttributes().getNamedItem("UOM").getTextContent();
				newModel.longItudlnal[i] = root.getElementsByTagName("Longitudinal").item(i).getTextContent();
				if (!newModel.longItudlnal[i].equals("Unknown") && !newModel.longItudlnal[i].equals(""))
					newModel.longItudlnal[i] += " " + root.getElementsByTagName("Longitudinal").item(i).getAttributes().getNamedItem("UOM").getTextContent();

				newModel.lateral[i] = root.getElementsByTagName("Lateral").item(i).getTextContent();
				if (!newModel.lateral[i].equals("Unknown") && !newModel.lateral[i].equals(""))
					newModel.lateral[i] += " " + root.getElementsByTagName("Lateral").item(i).getAttributes().getNamedItem("UOM").getTextContent();

				newModel.barrierEquivalent[i] = root.getElementsByTagName("BarrierEquivalentSpeed").item(i).getTextContent();
				if (!newModel.barrierEquivalent[i].equals("Unknown") && !newModel.barrierEquivalent[i].equals(""))
					newModel.barrierEquivalent[i] += " " + root.getElementsByTagName("BarrierEquivalentSpeed").item(i).getAttributes().getNamedItem("UOM").getTextContent();
			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return newModel;
	}

	public static void main(String args[]) {
		new XMLConverter();
	}
}
