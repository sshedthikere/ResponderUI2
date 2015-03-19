package com.project.resp.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;

import spark.Request;

import com.project.resp.vo.ResponderVO;

public class ResponderUtil {
	
	public static final String SOAP = "SOAP";
	
	public static final String REST = "REST";	
	
	public static final String ENDPOINT_URI = "/soap-responder?ws=";
	
	public static final String RESPONSES_CONFIG = "/config";
	
	private static final String FILE_EXTN_JSON = ".json";
	
	private static final String FILE_EXTN_XML = ".xml";
	
	private static final String RESPONDER_CONFIG_FILE_NAME = "responderconfig.json";
	
	private static final String RESPONDER_DATA_DIR = "/wsdata";
	
	public static String getDefaultContentType(String wsType) {
		
		if (SOAP.equalsIgnoreCase(wsType)) {			
			return "application/xml";
		} else {
			return "application/json";
		}
		
	}
	
	public static String getEndPointURL(String responderName, String host) {
		
		return "http://"+host+ENDPOINT_URI + responderName;
	}
	
	
	public static void objectToJson(Object obj, String basedir) {
				
		
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			basedir = basedir.replaceFirst("file:/", "") + RESPONSES_CONFIG;			
			createDirectory(basedir);
			File responseFile = new File (getFilePath(basedir,RESPONDER_CONFIG_FILE_NAME,null));
			PrintWriter writer = new PrintWriter(responseFile);
			mapper.configure(Feature.INDENT_OUTPUT,true);
			writer.write(mapper.writeValueAsString(obj));
			writer.write("\n");
			writer.flush();
			writer.close();
			
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
	
	public static String getFilePath(String basedir, String filename, String fileExtn) {
		
		StringBuffer buffer = new StringBuffer();
		buffer.append(basedir).append(File.separator).append(filename).append(null!=fileExtn?fileExtn:"");
		return buffer.toString();
		
	}
	
	public static Object jsonToObject(String basedir) {
		
		ObjectMapper mapper = new ObjectMapper();
		Object returnObj = null;
		try {
			String filePath = basedir.replaceFirst("file:/", "") + RESPONSES_CONFIG;
			File configFile = new File(filePath + "/"+ RESPONDER_CONFIG_FILE_NAME);
			System.out.println("ConfigFile="+configFile);
			
			if (configFile.exists()) {
				returnObj =  mapper.readValue(configFile, mapper.getTypeFactory().constructCollectionType(List.class, ResponderVO.class));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return returnObj;
	}
	
	
	public static void saveResponse(String responseStr, String basedir, String fileName) {
		
		try {
			
			String filePath = basedir.replaceFirst("file:/", "") + RESPONDER_DATA_DIR+"/"+fileName;
			FileUtils.writeStringToFile(new File(filePath), responseStr);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getResponseData(String basedir, String fileName) {
		
		String filePath = basedir.replaceFirst("file:/", "") + RESPONDER_DATA_DIR+"/"+fileName;
		String responseStr = null;
		try {
			responseStr = FileUtils.readFileToString(new File(filePath));
		} catch (IOException e) {			
			e.printStackTrace();
		}
		return responseStr;
	}
	
	private static void createDirectory(String dirpath) {		
		
		File file = new File(dirpath);
		
		if (!file.exists()) {
			file.mkdir(); 
		}		
	}
}
