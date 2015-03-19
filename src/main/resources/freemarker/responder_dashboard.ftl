<!DOCTYPE html>
<html lang="en">
  <head>    
    <title>SOAP/REST Responder</title>
    <!-- Le styles -->
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/bootstrap-responsive.min.css" rel="stylesheet">
    <script src="js/jquery-1.11.2.min.js"></script>
  	<script src="js/bootstrap.min.js"></script>    
  </head>
  
  <body>
    <div class="container">
      <div class="masthead">
        <h3 class="muted">SOAP/Rest Responder</h3>
      </div>
      
      
		
	<ul class="nav nav-tabs" id="wsTabs">
		<li class="${soaptabactive!''}"><a href="dashboard">SOAP</a></li>
		<li class="${resttabactive!''}"><a href="restdashboard">REST</a></li>		
	</ul>
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
          <a href="addSoapForm" class="btn btn-info" role="button">Create</a>        
      </div>

      <hr>

      <div class="footer">
        <p></p>
      </div>
    </div> <!-- /container -->   


  </body>
</html>
