<!DOCTYPE html>
<html lang="en">
  <head>    
   <#include "header.ftl">  
  </head>  
  <body>
    <div class="container">
     <#include "navigation.ftl">
	<br/>
	<a href="${addResponderURL!''}" class="btn btn-info" role="button">Create</a>
	<br/>
	       
    <table class="table table-striped">
            <thead>
              <tr>
                <th>#</th>                
                <th>Responder Name</th>
                <th>ContentType</th>                
                <th>EndPointURL</th>
              </tr>
            </thead>
            <tbody>
            <#list responderVOs as responderVO>
            	<tr>
                <td>${responderVO_index+1}</td>
                <td>${responderVO['responseName']}</td>
                <td>${responderVO['contentType']}</td>
                <td>${responderVO['endpoint']}</td>
              </tr>
       		</#list>
            </tbody>
          </table>
          <br/>
          <a href="${addResponderURL!''}" class="btn btn-info" role="button">Create</a>        
      </div>     
    </div> <!-- /container -->   


  </body>
</html>
