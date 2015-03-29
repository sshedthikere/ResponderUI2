<!DOCTYPE html>
<html lang="en">
  <head>    
   <#include "header.ftl">  
  </head>  
  <body>
    <div class="container">
     <#include "navigation.ftl">
     <#include "tabs.ftl">
	<br/>
	<#if message??>
	<div class="alert alert-success" role="alert">
        <strong>${message!''}</strong>
    </div>
    </#if>
    <#if errorMsg??>
	<div class="alert alert-danger" role="alert">
        <strong>${errorMsg!''}</strong>
    </div>
    </#if>
	<a href="${addResponderURL!''}" class="btn btn-info" role="button">Create</a>
	<br/>	       
    <table class="table table-striped">
            <thead>
              <tr>
                <th>#</th>                
                <th>Responder Name</th>
                <th>ContentType</th>                
                <th>EndPointURL</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
            <#list responderVOs as responderVO>
            	<tr>
                <td>${responderVO_index+1}</td>
                <td class="rpname">${responderVO['responseName']}</td>
                <td>${responderVO['contentType']}</td>
                <td>${responderVO['endpoint']}</td>
                <td><a href="edit-responder-form?responderkey=${responderVO['responseName']}" class="btn btn-info btn-sm" title="Edit responder"><strong>E</strong></a>
                <a id="deleteLink" data-toggle="modal" href="#deleteModal" class="btn btn-info btn-sm" title="Delete responder"><strong>D</strong></a>                
                </td>
              </tr>
       		</#list>
            </tbody>
          </table>          
          <a href="${addResponderURL!''}" class="btn btn-info" role="button">Create</a>        
      </div>     
    </div> <!-- /container -->
     
	<script>
	$(document).ready(function () {
	$("#homeNav").addClass('active');	        
	$(".btn-sm").click(function() {
   	 var $item = $(this).closest("tr").find(".rpname").text();   	 
     $("#deleteModalLink").attr("href", "delete-responder?ws="+$item);
     $("#deleteModalLabel").text("Delete Responder:"+$item);       
	});
});
	</script>

<div class="modal fade" id="deleteModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="deleteModalLabel">Delete Responder</h4>
      </div>
      <div class="modal-body">
        Are you sure you want to delete this responder?
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">No</button>
        <a id="deleteModalLink" href="#" class="btn btn-info" role="button">Yes</a>
      </div>
    </div>
  </div>
</div>

  </body>
</html>
