<!DOCTYPE html>
<html lang="en">
  <head>
   <#include "header.ftl">
  </head>
  <body>
    <div class="container">
      	<#include "navigation.ftl">
    <#if message??>
	<div class="alert alert-success" role="alert">
        <strong>${message!''}</strong>
    </div>
    </#if>
    <#if formatErrorMessage??>
	<div class="alert alert-danger" role="alert">
        <strong>${formatErrorMessage!''}</strong>
    </div>
    </#if>
     <br/> 
	<form class="form-horizontal" role="form" action="format-json" method="post">		  	  
		   <style type="text/css">        
			textarea {
		    width: 100%;
		    height: 100%;
			}
    		</style>
		  <div class="form-group"> 			
     		 <label for="jsonbodytextarea">Enter Json to format:</label>
     		 <textarea class="form-control" rows="20" id="jsonbodytextarea" name="jsonResponseBody">${jsonResponseBody!''}</textarea>
     		 <div  id="counter"></div>
     		 <button type="submit" class="btn btn-info" role="button" id="submitButton" name="jsonBody">Format</button>
		  </div>
	</form>
    </div> <!-- /container -->
    
 <script>
 $(document).ready(function()  { 	
 	$("#utilsNav").addClass('active');
    var characters = 800000;
    var totalLength = 0;
    
    $("#counter").append("You have <strong>"+  characters+"</strong> characters remaining");
    $("#jsonbodytextarea").keyup(function(){
        if($(this).val().length > characters){
           //$(this).val($(this).val().substr(0, characters));
            $("#submitButton").attr('disabled','disabled');
        } else {        	
        	$("#submitButton").removeAttr('disabled');
        }
    totalLength = $(this).val().length
    var remaining = characters -  totalLength;
    $("#counter").html("Total characters entered:<strong>"+totalLength+ "</strong>"+ " remaining characters:<strong>"+remaining+"</strong>");
    if(remaining <= 10)
    {
        $("#counter").css("color","red");
    }
    else
    {
        $("#counter").css("color","black");
    }
    });
});
</script>
</body>
</html>
