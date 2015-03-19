<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title></title>
    <!-- Le styles -->
    <link href="css/bootstrap.min.css" rel="stylesheet">
   	<script src="js/jquery-1.11.2.min.js"></script>
  	<script src="js/bootstrap.min.js"></script>
  	<link href="css/bootstrap-responsive.min.css" rel="stylesheet">  	
    <style type="text/css">
        
	textarea {
    width: 100%;
    height: 100%;
	}
    </style>
   
  </head>
  <body>
    <div class="container">
      <div class="masthead">
        <h3 class="muted">SOAP/Rest Responder</h3>
        
        <div class="navbar">
          <div class="navbar-inner">
            <div class="container">
              <ul class="nav">               
				<li class="active"><a href="dashboard">Home</a></li>
                <li></li>
                <li></li>
                <li></li>
                <li></li>
                <li></li>                
              </ul>
            </div>
          </div>
        </div><!-- /.navbar -->
		
		<ul class="nav nav-tabs" id="wsTabs">
			<li class="${soaptabactive!''}"><a href="dashboard">SOAP</a></li>
			<li class="${resttabactive!''}"><a href="restdashboard">REST</a></li>		
		</ul>		
      </div>
    <br/>
    <#if message??>
	<div class="alert alert-success" role="alert">
        <strong>${message!''}</strong>
    </div>
    </#if>
     <br/> 
	<form class="form-horizontal" role="form" action="submitResponse" method="post">
		  <div class="form-group">
		    <label class="col-sm-2 control-label" for="focusedInput1">EndPoint:</label>
		    <div class="col-sm-10">
		      <input class="form-control" id="focusedInput1" type="text" value="${responderVO['endpoint']!''}" readonly="true">
		    </div>		    
		  </div>
		  
		  <div class="form-group">
		    <label class="col-sm-2 control-label" for="focusedInput2">Response Name:</label>
		    <div class="col-sm-10">
		      <input class="form-control" id="focusedInput2" type="text" value="${responderVO['responseName']!''}" name="responseName">
		    </div>		    
		  </div>
		  <div class="form-group">
		    <label class="col-sm-2 control-label" for="focusedInput3">Response Status:</label>
		    <div class="col-sm-10">
		      <input class="form-control" id="focusedInput3" type="text" value="<#if responderVO['status']??>200<#else>${responderVO['status']}</#if>" name="responseStatus">
		    </div>		    
		  </div>
		  
		  <div class="form-group">
		    <label class="col-sm-2 control-label" >Content-Type:</label>
		    <div class="col-sm-10">
		      <input class="form-control" id="focusedInput4" type="text" value="${responderVO['contentType']!''}" readonly="true" name="contentType">		      
		    </div>		    
		  </div>
		  
		  <div class="form-group">
 			 <label for="textareaID">Response Body:</label>
 		 	<textarea class="form-control" rows="10" id="textareaID" name="responseBody">${responseBody!''}</textarea> 		  
		</div>
		<br/>
		<button type="submit" class="btn btn-info">Submit</button>
		</form>			
      <hr>

      <div class="footer">
        <p></p>
      </div>

    </div> <!-- /container -->
	
  </body>
</html>
