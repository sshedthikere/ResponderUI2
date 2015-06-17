# Spark ResponderUI

Spark Responder is a light weight configurable service to mock out SOAP and REST services. 
This responder provides ability to quickly define REST and SOAP endpoints and have them return XML or JSON responses. 
It is built on <a href="http://sparkjava.com/">Spark framework</a> (simple and lightweight Java web framework built for rapid development)
and Bootstrap framework for front end.

<h3>Prerequisites to run Spark Responder</h3>
<li>Maven 3.x which is configured to a repo so that it can download Spark framework jars and other dependency jars</li> 
<li>Java 7</li>

<h3>Spark responder dashboard</h3>
Spark Responder dashboard provides the snapshot of the configured SOAP and REST responders as shown below. Each responder has attributes like responder name, 
content type, endpoint url. SOAP and REST responder configurations are listed under respective tabs.
Spark responder dashbaord is accessible using the url http://localhost:8082/dashboard. 
Default port 8082 can be configured to any other port in case of any port conflict.

<h3> Auto Discover services</h3>
We can use the sync feature under Utils menu to discover all the responses available in root directory (under wsdata folder).
Responses which are auto discovered is available for consuming either as SOAP or REST Services. Endpoint urls are generated using the name of the response file.

<h3> Steps to run</h3>
1. Unzip the zipped artifact(make sure there are no spaces in the folder name)
2. Locate the pom.xml. Run "mvn clean install" so that it downloads all the dependency jars to your local repo (required only for the first time)
3. Run the command given in run.txt (mvn exec command)
4. Launch the application using URL: http://localhost:8082/dashboard



