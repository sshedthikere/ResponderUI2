package com.project.resp.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;

import spark.Request;

import com.project.resp.vo.ResponderVO;

public class ResponderUtil {
	
	public static final String SOAP = "SOAP";
	
	public static final String REST = "REST";	
	
	public static final String ENDPOINT_URI = "/soap-responder?ws=";
	
	public static final String RESPONSES_CONFIG = "/config";
	
	private static final String RESPONDER_CONFIG_FILE_NAME = "responderconfig.json";
	
	private static final String RESPONDER_DATA_DIR = "/wsdata";
	
	private final static Logger LOGGER = Logger.getLogger(ResponderUtil.class);
	
	private final static String ENDPOINT_URL = "http://%1$s/service-responder?ws=%2$s";
	
	
	public static String getEndpointURL(Request request, String responseName) {
		String formattedURL = String.format(ENDPOINT_URL, request.host(),responseName);
		LOGGER.info("formattedURL="+formattedURL);
		return formattedURL;
	}
	
	public static void objectToJson(Object obj, String basedir) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			basedir = basedir + RESPONSES_CONFIG;			
			createDirectory(basedir);
			File responseFile = new File (getFilePath(basedir,RESPONDER_CONFIG_FILE_NAME,null));
			PrintWriter writer = new PrintWriter(responseFile);
			mapper.configure(Feature.INDENT_OUTPUT,true);
			writer.write(mapper.writeValueAsString(obj));
			writer.write("\n");
			writer.flush();
			writer.close();
			
		} catch (IOException e) {			
			LOGGER.error(e);
		}
	}
	
	public static String getFormattedJson(String jsonInput) throws Exception{
		
		ObjectMapper mapper = new ObjectMapper();
		String indented = null;
		try {
			Object json = mapper.readValue(jsonInput, Object.class);
			indented = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
		} catch (IOException e) {
			LOGGER.error("Error while formatting json",e);
			throw new Exception("Error in formatting json:",e);
		}
		return indented;
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
			String filePath = basedir + RESPONSES_CONFIG;
			File configFile = new File(filePath + "/"+ RESPONDER_CONFIG_FILE_NAME);
			LOGGER.info("ConfigFile="+configFile);
			
			if (configFile.exists()) {
				returnObj =  mapper.readValue(configFile, mapper.getTypeFactory().constructCollectionType(List.class, ResponderVO.class));
			}
		} catch (IOException e) {
			LOGGER.error(e);
		}
		
		return returnObj;
	}
	
	
	public static void saveResponse(String responseStr, String basedir, String fileName) {
		try {
			String filePath = basedir + RESPONDER_DATA_DIR+"/"+fileName;
			FileUtils.writeStringToFile(new File(filePath), responseStr);
		} catch (IOException e) {
			LOGGER.error(e);
		}
	}
	
	public static String getResponseData(String basedir, String fileName) {
		
		String filePath = basedir + RESPONDER_DATA_DIR+"/"+fileName;
		String responseStr = null;
		try {
			responseStr = FileUtils.readFileToString(new File(filePath));
		} catch (IOException e) {			
			LOGGER.error(e);
		}
		return responseStr;
	}
	
	public static List<String> getFileNamesList(String basedir) {
		
		String[] extensions = {"xml", "json"};
		LOGGER.info("basedir="+basedir);
		@SuppressWarnings("unchecked")
		List<File> files = (List<File>) FileUtils.listFiles(new File(basedir+RESPONDER_DATA_DIR), extensions, false);
		List<String> filenames = filesToFilenames(files);		
		return filenames;
	}
	
	@SuppressWarnings("rawtypes")
	private static List<String> filesToFilenames(List<File> files) {
        List<String> filenames = new ArrayList<String>(files.size());
        Iterator i = files.iterator();
        while (i.hasNext()) {
            filenames.add(((File)i.next()).getName());
        }
        return filenames;
    }
	
	private static void createDirectory(String dirpath) {
		File file = new File(dirpath);		
		if (!file.exists()) {
			file.mkdir(); 
		}		
	}
	
	public static String getResponseName(String responseFileName, String contentType) {
		String responseName = "";
		if ("text/xml".equalsIgnoreCase(contentType)) {
			responseName = responseFileName.substring(0,responseFileName.lastIndexOf(".xml"));
		} else if ("application/json".equalsIgnoreCase(contentType)) {
			responseName = responseFileName.substring(0,responseFileName.lastIndexOf(".json"));
		}
		return responseName;
	}
}
