package core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import model.DataModel;

public class XMLConverter {

	public static final String WORKDIR = "C:\\Users\\lleej\\Desktop\\sample";
	public static final String XML_FOLDER = "\\xml";
	public static final String IMG_FOLDER = "\\jpg";

	private ArrayList<String> xmlFileList;

	public XMLConverter() {
		readFileList();
		if (xmlFileList.size() <= 0) {
			System.err.println("XML 파일이 해당 경로에 없습니다.");
			System.exit(1);
		}
		parseXMLfiles();
	}

	private void readFileList() {
		xmlFileList = new ArrayList<String>();
		File workDir = new File(WORKDIR + XML_FOLDER);
		String[] filelist = workDir.list();
		for (String filename : filelist) {
			if (filename.endsWith(".xml") && !(new File(WORKDIR + XML_FOLDER + "\\" + filename).isDirectory()))
				xmlFileList.add(filename);
		}
	}

	private void parseXMLfiles() {
		for (String filename : xmlFileList) {
			String fullPath = WORKDIR + XML_FOLDER + "\\" + filename; 
			DataModel newModel = extractDataModel(fullPath);
		}
	}
	private DataModel extractDataModel(String path){
		DataModel newModel = null;
		
		
		try {
			DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
			DocumentBuilder parser = f.newDocumentBuilder();
			
			Document doc = parser.parse(path);
			
			//찾아야하는 정보
//			public String caseID;
//			public String crashType;
//			public String configuration;
//			public String[] vehicleNo;
//			public String[] postedSpeedLimit;
//			public String[] vehicleCrashType;
//			public String[] total;
//			public String[] longItudlnal;
//			public String[] lateral;
//			public String[] barrierEquivalent;
//			public String imgPath;
			
			//--Case ( attr : caseID = CaseStr)
			//----CaseForm
			//----EMSForm
			//----GeneralVehicleForms
			//------GeneralVehicleForm
			//--------
			
			newModel = new DataModel();
			Element root = doc.getDocumentElement();
			newModel.caseID = root.getAttribute("CaseStr");
			
			newModel.crashType = root.getElementsByTagName("CrashType").item(0).getTextContent();
			newModel.configuration = root.getElementsByTagName("Configuration").item(0).getTextContent();
						
			
			
			
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
