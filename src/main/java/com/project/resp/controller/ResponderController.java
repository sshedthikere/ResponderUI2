package com.project.resp.controller;


import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.setPort;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.jetty.http.HttpStatus;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import com.project.resp.constants.WebConstants;
import com.project.resp.util.ResponderUtil;
import com.project.resp.vo.ResponderVO;

import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * This class encapsulates the controllers for the spark responder web application. 
 * 
 */
public class ResponderController {
	
    private final Configuration cfg;
    
    private final String BASE_DIR;
    
    private List<ResponderVO> responderVOs;
    
    
    
    private final static Logger LOGGER = Logger.getLogger(ResponderController.class); 
    
    private List<ResponderVO> responderSettingVOList;

    /**
     * 
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {          	
    	new ResponderController();        
    }
    
    /**
     * 
     * @throws IOException
     */
    public ResponderController() throws IOException {        
    	
    	String basedir = System.getProperty("build.dir");
    	BASE_DIR = basedir.replaceFirst("file:/", "");
    	cfg = createFreemarkerConfiguration();
        setPort(8082);
        responderVOs = new ArrayList<ResponderVO>();
        loadResponderVOList();
        Spark.staticFileLocation("/public");
        initializeRoutes();
       
    }

    abstract class FreemarkerBasedRoute extends Route {
        final Template template;

        /**
         * Constructor
         *
         * @param path The route path which is used for matching.
         */
        protected FreemarkerBasedRoute(final String path, final String templateName) throws IOException {
            super(path);
            template = cfg.getTemplate(templateName);
        }

        @Override
        public Object handle(Request request, Response response) {
            StringWriter writer = new StringWriter();
            try {
            	
            	doHandle(request, response, writer);
            } catch (Exception e) {
                e.printStackTrace();
                response.redirect("/internal_error");
            }
            return writer;
        }

        protected abstract void doHandle(final Request request, final Response response, final Writer writer)
                throws IOException, TemplateException;

    }
    
    /**
     * 
     * @throws IOException
     */
    private void initializeRoutes() throws IOException {        
       
        get(new FreemarkerBasedRoute("/dashboard", "responder_dashboard.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                    SimpleHash root = new SimpleHash();                    
                    updateDashboardInfo(root);
                    template.process(root, writer);
            }			
        });
        
        get(new FreemarkerBasedRoute("/restdashboard", "responder_dashboard.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {            
                                    
                    SimpleHash root = new SimpleHash();  
                    List<ResponderVO> restResponderVOs = filterResponders("rest");
                    root.put("responderVOs", restResponderVOs);
                    root.put("addResponderURL", WebConstants.ADD_RESPONDER_FORM_URL+"?type=rest");
                    setActiveTab(root, "rest");
                    template.process(root, writer);
            }
        });

       
        get(new FreemarkerBasedRoute(WebConstants.ADD_RESPONDER_FORM_URL, "addResponderForm.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
              
            	SimpleHash root = new SimpleHash();
                ResponderVO responderVO = new ResponderVO();
                String responseType = request.queryParams("type");
                responderVO.setServiceType(responseType);
                setActiveTab(root,responseType);
                if ("soap".equalsIgnoreCase(responseType)) {
                	responderVO.setContentType(WebConstants.TEXT_XML);                	
                } else {
                	responderVO.setContentType(WebConstants.APPLICATION_JSON);                	
                }
                root.put("responderVO", responderVO); 
                template.process(root, writer);
              
            }
        });
        
        get(new FreemarkerBasedRoute("/edit-responder-form", "addResponderForm.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
              
            	String responseName = request.queryParams("responderkey");
            	if (null == responseName) {
            		response.redirect(WebConstants.ADD_RESPONDER_FORM_URL);
            	} else {
	            	int matchedIndex = getMatchedResponderIndex(responderVOs, responseName);
	            	SimpleHash root = new SimpleHash();
	                ResponderVO responderVO = responderVOs.get(matchedIndex);
	                String responseBody = ResponderUtil.getResponseData(BASE_DIR, responderVO.getResponseFileName());
	                root.put("responderVO", responderVO); 
	                root.put("responseBody", responseBody);
	                setActiveTab(root,responderVO.getServiceType());
	                template.process(root, writer);
            	}
            }
        });
        
        post(new FreemarkerBasedRoute("/submit-response", "addResponderForm.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                           	
            	String responseName = request.queryParams("responseName");
                String responseBody = request.queryParams("responseBody"); 
                String contentType = request.queryParams("contentType");
                String status = request.queryParams("responseStatus");
                String serviceType = request.queryParams("serviceType");
                LOGGER.info("contentType="+contentType);
                
                ResponderVO responderVO = new ResponderVO();
                responderVO.setContentType(contentType);
                responderVO.setEndpoint(ResponderUtil.getEndpointURL(request, responseName));               
                responderVO.setStatus(Integer.parseInt(status));
                responderVO.setResponseName(responseName);
                responderVO.setServiceType(serviceType);
                if (WebConstants.TEXT_XML.equalsIgnoreCase(contentType)) {
                	responderVO.setResponseFileName(responseName+ ".xml");
                } else {
                	responderVO.setResponseFileName(responseName+ ".json");
                }
                
                int matchedIndex = getMatchedResponderIndex(responderVOs, responseName);
                if (matchedIndex != -1){                	
                	responderVOs.set(matchedIndex, responderVO);
                	LOGGER.info("Matched existing....");
                } else {                              
                	responderVOs.add(responderVO);
                }
                
                ResponderUtil.objectToJson(responderVOs, BASE_DIR);
                loadResponderVOList();
                ResponderUtil.saveResponse(responseBody, BASE_DIR, responderVO.getResponseFileName());                
               
               SimpleHash root = new SimpleHash();                
               root.put("message", "Added response successfully");
               root.put("responderVO", responderVO);
               root.put("responseBody", responseBody);
               setActiveTab(root,serviceType);               
               template.process(root, writer);
            }
        });
        
        get(new FreemarkerBasedRoute("/responder-config-settings", "responder-config-settings.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
              
            	SimpleHash root = new SimpleHash();
            	responderSettingVOList = getResponderSettingVOList();                
                root.put("responderConfigVOs", responderSettingVOList);
                template.process(root, writer);              
            }
        });        
        
        
        post(new Route("/submit-responder-settings") {
            @Override
            public Object handle(Request request, Response response) {            	
            	
            	List<ResponderVO> outOfSyncResponderVOs = new ArrayList<ResponderVO>();
            	if (null != responderSettingVOList) {
	            	for (ResponderVO responderVO: responderSettingVOList) {
	            		if (null == responderVO.getEndpoint()) {
	            			String responseFileName = responderVO.getResponseFileName();
	            			String contentType = responderVO.getContentType();
	            			String responseName = ResponderUtil.getResponseName(responseFileName, contentType);
	            			responderVO.setEndpoint(ResponderUtil.getEndpointURL(request, responseName));
	            			responderVO.setResponseName(responseName);
	            			responderVO.setStatus(HttpStatus.OK_200);
	            			outOfSyncResponderVOs.add(responderVO);
	            		}
	            	}
	            	responderVOs.addAll(outOfSyncResponderVOs);
	            	ResponderUtil.objectToJson(responderVOs, BASE_DIR);
	                loadResponderVOList();
            	}
            	response.redirect("/responder-setting-form");
            	return null;
            } 
        });
        
        get(new Route("/service-responder") {
            @Override
            public Object handle(Request request, Response response) {
            	
            	return handleResponderRequest(request, response);
            } 
        });
        
        post(new Route("/service-responder") {
            @Override
            public Object handle(Request request, Response response) {
            	
            	return handleResponderRequest(request, response);
            } 
        });
        
        put(new Route("/service-responder") {
            @Override
            public Object handle(Request request, Response response) {
            	
            	return handleResponderRequest(request, response);
            } 
        });
        
        delete(new Route("/service-responder") {
            @Override
            public Object handle(Request request, Response response) {
            	
            	return handleResponderRequest(request, response);
            } 
        });
        
        get(new FreemarkerBasedRoute("/json-formatter-form", "json-formatter.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {            
                                    
                    SimpleHash root = new SimpleHash();
                    template.process(root, writer);
                
            }
        });
        
        post(new FreemarkerBasedRoute("/format-json", "json-formatter.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {            
                                    
            	 String jsonInput = request.queryParams("jsonResponseBody"); 
            	 String jsonOutput = "";
            	 boolean errorFlag = false;
            	 SimpleHash root = new SimpleHash();
				try {
					jsonOutput = ResponderUtil.getFormattedJson(jsonInput);
				} catch (Exception e) {
					errorFlag = true;
					LOGGER.info(e.getMessage());
				}
            	if (errorFlag) {
            		root.put("formatErrorMessage", "Unable to Format Json.Not a valid JSON");
            		root.put("jsonResponseBody", jsonInput);
            	} else {
            		root.put("jsonResponseBody", jsonOutput);
            	}
            	 
                 template.process(root, writer);                
            }
        });
        
        get(new FreemarkerBasedRoute("/delete-responder", "responder_dashboard.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {            
                                    
            	String responseName = request.queryParams("ws");
            	SimpleHash root = new SimpleHash();
            	LOGGER.info(responseName);
            	if (deleteResponder(responderVOs, responseName)) {
            		ResponderUtil.objectToJson(responderVOs, BASE_DIR);
                    loadResponderVOList();                	
                	root.put("message", "Deleted " +responseName+" response successfully. Only config file entry is deleted.");
            	} else {
            		root.put("errorMsg", "Unable to delete responder");
            	}
            	updateDashboardInfo(root);
                template.process(root, writer);
            }
        });
    }    
   
    
    @SuppressWarnings("unchecked")
	private List<ResponderVO> loadResponderVOList() {
    	
    	if (null == responderVOs || responderVOs.isEmpty()) {
	    	Object responderVOListObject = ResponderUtil.jsonToObject(BASE_DIR);
	         if (null != responderVOListObject) {
	         	responderVOs = (List<ResponderVO>)responderVOListObject;
	         	LOGGER.info("responderVOs.size()="+responderVOs.size());
	         }
    	}
        return responderVOs;
    	
    }
    
    private List<ResponderVO> filterResponders(String serviceType) {
		List<ResponderVO> filteredresponderVOs = new ArrayList<ResponderVO>();
		for(ResponderVO respndrVO : responderVOs) {
			
			if (serviceType.equalsIgnoreCase(respndrVO.getServiceType())) {
				filteredresponderVOs.add(respndrVO);
			}
		}
		return filteredresponderVOs;
	}
    
    private int getMatchedResponderIndex(List<ResponderVO> responderVOs, String responderName) {    	
    	int index = -1;    	
    	for (int i = 0;i <responderVOs.size();i++) {
    		if (responderVOs.get(i).getResponseName().equalsIgnoreCase(responderName)) {            			
    			index = i;
    			break;
    		}
    	}
    	return index;
    }
    
    private boolean deleteResponder(List<ResponderVO> responderVOs, String responderName) {
		int index = getMatchedResponderIndex(responderVOs, responderName);
		if (index != -1) {
			responderVOs.remove(index);
			return true;
		} else {
			return false;
		}
    }
    
    private void setActiveTab(SimpleHash simpleHash, String type) {
    	if ("soap".equalsIgnoreCase(type)) {
    		simpleHash.put("active_tab_soap", "active");
    	} else {
    		simpleHash.put("active_tab_rest", "active");
    	}
    }
    
    private Configuration createFreemarkerConfiguration() {
        Configuration retVal = new Configuration();
        retVal.setClassForTemplateLoading(ResponderController.class, "/freemarker");
        return retVal;
    }
	

	private String handleResponderRequest(Request request, Response response) {
		String responderName = request.queryParams("ws");
		ResponderVO selectedResponderVO = null;
		for (ResponderVO responderVO: responderVOs) {
			
			if (responderVO.getResponseName().equalsIgnoreCase(responderName)) {            			
				selectedResponderVO = responderVO;
				break;
			}
		}            	
		response.status(selectedResponderVO.getStatus());
		response.type(selectedResponderVO.getContentType());
		LOGGER.info("Retrieved "+selectedResponderVO.getEndpoint()+":"+selectedResponderVO.getStatus());
		return ResponderUtil.getResponseData(BASE_DIR, selectedResponderVO.getResponseFileName());
	}

	private void updateDashboardInfo(SimpleHash root) {
		List<ResponderVO> soapResponderVOs = filterResponders("soap");
		root.put("responderVOs", soapResponderVOs);
		root.put("addResponderURL", WebConstants.ADD_RESPONDER_FORM_URL+"?type=soap");
		setActiveTab(root, "soap");
	}

	private List<ResponderVO> getResponderSettingVOList() {
		Map<String, ResponderVO> responderVOMap = new HashMap<String,ResponderVO>();
		responderSettingVOList = new ArrayList<ResponderVO>();
		List<String> fileNamesList = ResponderUtil.getFileNamesList(BASE_DIR);
		
		for(ResponderVO responderVO: responderVOs){			
			responderVOMap.put(responderVO.getResponseFileName(), responderVO);
		}
		
		for(String filename: fileNamesList) {
			
			ResponderVO localResponderVO = responderVOMap.get(filename);
			if (null != localResponderVO) {
				responderSettingVOList.add(localResponderVO);
			} else {                		
				ResponderVO newResponderVO = new ResponderVO();
				newResponderVO.setResponseFileName(filename);				
				newResponderVO.setServiceType((filename.contains(".xml")?"soap":"rest"));
				if (filename.contains(".xml")) {
					newResponderVO.setContentType(WebConstants.TEXT_XML);                			
				} else if (filename.contains(".json")){
					newResponderVO.setContentType(WebConstants.APPLICATION_JSON);
				} else {
					newResponderVO.setContentType("UNKNOWN");
				}
				responderSettingVOList.add(newResponderVO);
			}
		}
		
		Collections.sort(responderSettingVOList, new Comparator<ResponderVO>() {
		    public int compare(ResponderVO r1,ResponderVO r2) {
		        if (r1 == r2) 
		        	return 0;                        
		        else if (r1.getEndpoint() == null) 
		        	return -1;   
		        else if (r2.getEndpoint() == null)
		        	return 1;
		        else {
		        	return r1.getEndpoint().compareTo(r2.getEndpoint());
		        }
		     }
		});
		
	return 	responderSettingVOList;
	}
	
	
}
