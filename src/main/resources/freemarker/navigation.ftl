<style>
.navbar-default {
  background-color: #3f2860;
  border-color: #3f2860;
}
.navbar-default .navbar-brand {
  color: #ffffff;
}
.navbar-default .navbar-brand:hover, .navbar-default .navbar-brand:focus {
  color: #ef6d3b;
}
.navbar-default .navbar-text {
  color: #ffffff;
}
.navbar-default .navbar-nav > li > a {
  color: #ffffff;
}
.navbar-default .navbar-nav > li > a:hover, .navbar-default .navbar-nav > li > a:focus {
  color: #ef6d3b;
}
.navbar-default .navbar-nav > .active > a, .navbar-default .navbar-nav > .active > a:hover, .navbar-default .navbar-nav > .active > a:focus {
  color: #ef6d3b;
  background-color: #3f2860;
}
.navbar-default .navbar-nav > .open > a, .navbar-default .navbar-nav > .open > a:hover, .navbar-default .navbar-nav > .open > a:focus {
  color: #ef6d3b;
  background-color: #3f2860;
}
</style>

<nav class="navbar navbar-default" role="navigation">
   <div class="navbar-header">
      <a class="navbar-brand" href="dashboard">SOAP/REST Responder</a>
   </div>
   <div>
      <ul class="nav navbar-nav">
         <li class="active"><a href="dashbaord">HOME</a></li>
         <li><a href="#">Utils</a></li>
      </ul>
   </div>
</nav>
		
<ul class="nav nav-tabs" id="wsTabs">
	<li class="${soaptabactive!''}"><a href="dashboard">SOAP</a></li>
	<li class="${resttabactive!''}"><a href="restdashboard">REST</a></li>		
</ul>