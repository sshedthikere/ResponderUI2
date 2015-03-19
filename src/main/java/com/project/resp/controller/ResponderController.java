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
import java.util.List;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import com.project.resp.util.ResponderUtil;
import com.project.resp.vo.ResponderVO;

import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * This class encapsulates the controllers for the blog web application.  It delegates all interaction with MongoDB
 * to three Data Access Objects (DAOs).
 * <p/>
 * It is also the entry point into the web application.
 */
public class ResponderController {
    private final Configuration cfg;
    
    private final String BASE_DIR;
    
    private List<ResponderVO> responderVOs;
    
    private static final String APPLICATION_XML = "application/xml";
    
    private static final String ADD_RESPONDER_FORM_URL = "/add-responder-form";
    

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
        
    	BASE_DIR = System.getProperty("build.dir");
    	cfg = createFreemarkerConfiguration();
        setPort(8082);
        responderVOs = new ArrayList<ResponderVO>();
        getResponderVOList();
        Spark.staticFileLocation("/public");
        initializeRoutes();
       
    }

    abstract class FreemarkerBasedRoute extends Route {
        final Template template;

        /**
         * Constructor
         *
         * @param path The route path which is used for matching. (e.g. /hello, users/:name)
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

    private void initializeRoutes() throws IOException {
        
       // used to display actual blog post detail page
        get(new FreemarkerBasedRoute("/dashboard", "responder_dashboard.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {            
                                    
                    SimpleHash root = new SimpleHash();                    
                    List<ResponderVO> soapResponderVOs = filterResponders(APPLICATION_XML);
                    root.put("responderVOs", soapResponderVOs);
                    root.put("addResponderURL", ADD_RESPONDER_FORM_URL+"?type=soap");
                    root.put("soaptabactive", "active");
                    template.process(root, writer);
                
            }			
        });
        
        get(new FreemarkerBasedRoute("/restdashboard", "responder_dashboard.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {            
                                    
                    SimpleHash root = new SimpleHash();  
                    List<ResponderVO> restResponderVOs = filterResponders("rest");
                    root.put("responderVOs", restResponderVOs);
                    root.put("addResponderURL", ADD_RESPONDER_FORM_URL+"?type=rest");
                    root.put("resttabactive", "active");
                    template.process(root, writer);
                
            }
        });

       
        get(new FreemarkerBasedRoute(ADD_RESPONDER_FORM_URL, "addResponderForm.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
              
            	SimpleHash root = new SimpleHash();
                ResponderVO responderVO = new ResponderVO();
                String responseType = request.queryParams("type");
                responderVO.setServiceType(responseType);
                setActiveTab(root,responseType);
                if ("soap".equalsIgnoreCase(responseType)) {
                	responderVO.setContentType("application/xml");                	
                } else {
                	responderVO.setContentType("application/json");                	
                }
                root.put("responderVO", responderVO); 
                template.process(root, writer);
              
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
                System.out.println("contentType="+contentType);
                
                ResponderVO responderVO = new ResponderVO();
                responderVO.setContentType(contentType);
                responderVO.setEndpoint(getEndpointURL(request, responseName));               
                responderVO.setStatus(Integer.parseInt(status));
                responderVO.setResponseName(responseName);
                responderVO.setServiceType(serviceType);
                if ("application/xml".equalsIgnoreCase(contentType)) {
                	responderVO.setResponseFileName(responseName+ ".xml");
                } else {
                	responderVO.setResponseFileName(responseName+ ".json");
                }
                
                int matchedIndex = getMatchedResponderIndex(responderVOs, responseName);
                if (matchedIndex != -1){                	
                	responderVOs.set(matchedIndex, responderVO);
                	System.out.println("Matched existing....");
                } else {                              
                	responderVOs.add(responderVO);
                }
                
                ResponderUtil.objectToJson(responderVOs, BASE_DIR);
                getResponderVOList();
                ResponderUtil.saveResponse(responseBody, BASE_DIR, responderVO.getResponseFileName());                
               
               SimpleHash root = new SimpleHash();                
               root.put("message", "Added response successfully");
               root.put("responderVO", responderVO);
               root.put("responseBody", responseBody);
               setActiveTab(root,serviceType);               
               template.process(root, writer);
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
    }    
   
    
    @SuppressWarnings("unchecked")
	private List<ResponderVO> getResponderVOList() {
    	
    	if (null == responderVOs || responderVOs.isEmpty()) {
	    	Object responderVOListObject = ResponderUtil.jsonToObject(BASE_DIR);
	         if (null != responderVOListObject) {
	         	responderVOs = (List<ResponderVO>)responderVOListObject;
	         	System.out.println("responderVOs.size()="+responderVOs.size());
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
    
    private void setActiveTab(SimpleHash simpleHash, String type) {
    	
    	if ("soap".equalsIgnoreCase(type)) {
    		simpleHash.put("soaptabactive", "active");
    	} else {
    		simpleHash.put("resttabactive", "active");
    	}
    }
    
    private Configuration createFreemarkerConfiguration() {
        Configuration retVal = new Configuration();
        retVal.setClassForTemplateLoading(ResponderController.class, "/freemarker");
        return retVal;
    }

	private String getEndpointURL(Request request, String responseName) {
		return "http://"+request.host()+"/service-responder?ws="+responseName;
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
		return ResponderUtil.getResponseData(BASE_DIR, selectedResponderVO.getResponseFileName());
	}
}
