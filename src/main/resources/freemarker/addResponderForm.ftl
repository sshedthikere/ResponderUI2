<!DOCTYPE html>
<html lang="en">
  <head>
   <#include "header.ftl">
  </head>
  <body>
    <div class="container">
      	<#include "navigation.ftl">
    <br/>
    <#if message??>
	<div class="alert alert-success" role="alert">
        <strong>${message!''}</strong>
    </div>
    </#if>
     <br/> 
	<form data-toggle="validator" class="form-horizontal" role="form" action="submit-response" method="post">
		  <div class="form-group">
		    <label class="col-sm-2 control-label" for="focusedInput1">EndPoint:</label>
		    <div class="col-sm-10">
		      <input class="form-control" id="focusedInput1" type="text" value="${responderVO['endpoint']!''}" readonly="true">
		    </div>		    
		  </div>
		  
		  <div class="form-group">
		    <label class="col-sm-2 control-label" for="focusedInput2">Response Name:*</label>
		    <div class="col-sm-10">
		      <input class="form-control" id="focusedInput2" type="text" value="${responderVO['responseName']!''}" name="responseName" required>
		    </div>		   	    
		  </div>
		  <div class="form-group">
		    <label class="col-sm-2 control-label" for="focusedInput3">Response Status:*</label>
		    <div class="col-sm-10">
		      <input class="form-control" id="focusedInput3" type="text" value="<#if responderVO['status']??>200<#else>${responderVO['status']}</#if>" name="responseStatus" required>
		    </div>		    
		  </div>
		  
		  <div class="form-group">
		  	 <input type="hidden" value="${responderVO['serviceType']!''}" name="serviceType">
		  	 <label class="col-sm-2 control-label" >Content-Type:*</label>
		     <#if responderVO['serviceType']?? && responderVO['serviceType'] == 'rest'>
		     <div class="col-sm-10">
			      <select class="form-control" id="contentTypeSelect" name="contentType">
			        <option selected>application/json</option>
			        <option>application/xml</option>
			      </select>
		      </div>
		     <#else> 
			    <div class="col-sm-10">
			      <input class="form-control" id="focusedInput4" type="text" value="${responderVO['contentType']!''}" readonly="true" name="contentType" required>		      
			    </div>
			 </#if>   		    
		  </div>
		  
		   <style type="text/css">        
			textarea {
		    width: 100%;
		    height: 100%;
			}
    		</style>
		  <div class="form-group">
 			 <label class="col-sm-2 control-label" >Response Body:*</label>
 		 	<div class="col-sm-10">
 		 		<textarea class="form-control" rows="10" name="responseBody" required>${responseBody!''}</textarea>
 		 	 </div>		  
		  </div>
		  
		  <div class="form-group">
      		<div class="col-sm-offset-2 col-sm-10">
         		<button type="submit" class="btn btn-info" role="button">Submit</button>
      		</div>
   		</div>
	</form>
    </div> <!-- /container -->
    	
</body>
</html>
