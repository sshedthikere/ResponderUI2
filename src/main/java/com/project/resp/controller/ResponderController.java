package com.project.resp.controller;


import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.setPort;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

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
    

    public static void main(String[] args) throws IOException {
          	
    	new ResponderController();
        
    }

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
                    root.put("addResponderURL", "/addResponderForm?type=soap");
                    root.put("soaptabactive", "active");
                    template.process(root, writer);
                
            }			
        });
        
        get(new FreemarkerBasedRoute("/restdashboard", "responder_dashboard.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {            
                                    
                    SimpleHash root = new SimpleHash();  
                    List<ResponderVO> restResponderVOs = filterResponders("application/json");
                    root.put("responderVOs", restResponderVOs);
                    root.put("addResponderURL", "/addResponderForm?type=rest");
                    root.put("resttabactive", "active");
                    template.process(root, writer);
                
            }
        });

       
        get(new FreemarkerBasedRoute("/addResponderForm", "addResponderForm.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
              
            	SimpleHash root = new SimpleHash();
                ResponderVO responderVO = new ResponderVO();
                String responseType = request.queryParams("type");
                if ("soap".equalsIgnoreCase(responseType)) {
                	responderVO.setContentType("application/xml");
                	root.put("soaptabactive", "active");
                } else {
                	responderVO.setContentType("application/json");
                	root.put("resttabactive", "active");
                }
                root.put("responderVO", responderVO); 
                template.process(root, writer);
              
            }
        });
        
        post(new FreemarkerBasedRoute("/submitResponse", "addResponderForm.ftl") {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                String responseName = request.queryParams("responseName");
                String responseBody = request.queryParams("responseBody"); 
                String contentType = request.queryParams("contentType");
                String status = request.queryParams("responseStatus");
                System.out.println("contentType="+contentType);
                
                ResponderVO responderVO = new ResponderVO();
                responderVO.setContentType(contentType);
                responderVO.setEndpoint("http://"+request.host()+"/service-responder?ws="+responseName);               
                responderVO.setStatus(Integer.parseInt(status));
                responderVO.setResponseName(responseName);
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
                
               System.out.println("request url="+request.url());
               System.out.println("responseName="+responseName);
               System.out.println("responseBody="+responseBody);
               SimpleHash root = new SimpleHash();                
               root.put("message", "Added response successfully");
               root.put("responderVO", responderVO);
               root.put("responseBody", responseBody);
               template.process(root, writer);
               
            }
        });
        
        get(new Route("/service-responder") {
            @Override
            public Object handle(Request request, Response response) {
            	
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
        });
    }    
   
    
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
    
    private List<ResponderVO> filterResponders(String contentType) {
		List<ResponderVO> filteredresponderVOs = new ArrayList<ResponderVO>();
		for(ResponderVO respndrVO : responderVOs) {
			
			if (contentType.equalsIgnoreCase(respndrVO.getContentType())) {
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
    
    private Configuration createFreemarkerConfiguration() {
        Configuration retVal = new Configuration();
        retVal.setClassForTemplateLoading(ResponderController.class, "/freemarker");
        return retVal;
    }
}
