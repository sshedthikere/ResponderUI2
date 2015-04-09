<!DOCTYPE html>
<html lang="en">
  <head>
   <#include "header.ftl">
  </head>
  <body>
    <div class="container">
    <#include "navigation.ftl">         	
    <div class="row">
    <div class="col-md-10 col-md-offset-1">
	    <div class="panel panel-success">
	            <div class="panel-heading">
	              <h3 class="panel-title">Spark Responder API</h3>
	            </div>
	            <div class="panel-body">
	               Spark Responder API is a light weight configurable service to mock out SOAP and REST services. 
	               This responder provides ability to quickly define REST and SOAP endpoints and have them return XML or JSON responses. 
	               It is built on <a href="http://sparkjava.com/">Spark framework</a> (simple and lightweight Java web framework built for rapid development)
	               and Bootstrap framework for front end.
	            </div>
	      </div>
	   </div>
	</div>
	<div class="row">
	<div class="col-md-10 col-md-offset-1">
	    <div class="panel panel-success">
	            <div class="panel-heading">
	              <h3 class="panel-title">Prerequisites to run Spark Responder</h3>
	            </div>
	            <div class="panel-body">
	              <ul>
	              	<li>Maven 3.x which is configured to a repo so that it can download Spark framework jars and other dependency jars</li> 
	              	<li>Java 7</li>
	              </ul>	
	            </div>
	      </div>
	   </div>
	</div>     
	<div class="row">
    <div class="col-md-10 col-md-offset-1">
	    <div class="panel panel-success">
	            <div class="panel-heading">
	              <h3 class="panel-title">Spark responder dashboard</h3>
	            </div>
	            <div class="panel-body">
	               Spark Responder dashboard provides the snapshot of the configured SOAP and REST responders as shown below. Each responder has attributes like responder name, 
	               content type, endpoint url. SOAP and REST responder configurations are listed under respective tabs.
	               Spark responder dashbaord is accessible using the url http://localhost:8082/dashboard. Default port 8082 can be configured to any other port in case of any port conflict.<br/>	                  
	               <img src="graphic/sparkresponder_dashboard.jpg" class="img-rounded" alt="Spark Responder Dashboard" width="800" height="400">
	            </div>	            
	      </div>
	   </div>
	</div>	     
    </div> <!-- /container -->
<script>
$(document).ready(function() {
	$("#helpNav").addClass('active');	
});
</script>
</body>
</html>
