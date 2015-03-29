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
    <form id="configSettingsForm" class="form-horizontal" role="form" action="submit-responder-settings" method="post">     
	<table id="responder-config-table" class="table table-striped">
            <thead>
              <tr>
                <th>#</th>                                
                <th>Responder FileName</th>
                <th>ContentType</th>                
                <th>EndPointURL</th>
                <th>Service Type</th>
                <th>Sync Status</th>
              </tr>
            </thead>
            <tbody>
            <#list responderConfigVOs as responderVO>
            	<tr>
                <td>${responderVO_index+1}</td>                
                <td class="rpname">${responderVO['responseFileName']}</td>
                <td>${responderVO['contentType']!''}</td>
                <td>${responderVO['endpoint']!''}</td>
                <td>${responderVO['serviceType']!''}</td>                
                <td>
                	<#if responderVO['endpoint']??>
                		<span class="label label-success">In-Sync</span>
                	<#else>
                		<span id="outOfSync" class="label label-danger">Out-of-Sync</span>
                	</#if>
                </td>
              </tr>
       		</#list>
            </tbody>
          </table>
   	 
    	<div class="form-group">
      		<button type="submit" class="btn btn-info" role="button" id="syncallButton">Sync All</button>      		
   		</div> 
	</form>
    </div> <!-- /container -->
<script>
$(document).ready(function() {
	$("#utilsNav").addClass('active');
	if ($("#outOfSync").length) {		
          $("#syncallButton").removeAttr('disabled');
        } else {        	
          $("#syncallButton").attr('disabled','disabled');
   }
});
</script>
</body>
</html>
