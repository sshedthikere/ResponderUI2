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
      <a class="navbar-brand" href="dashboard">Spark Responder</a>
   </div>
   <div>
      <ul class="nav navbar-nav">
         <li id="homeNav"><a href="dashboard">HOME</a></li>        
         <li id="utilsNav"><a class="dropdown-toggle" data-toggle="dropdown" href="#">Utils<span class="caret"></span></a>
          <ul class="dropdown-menu">
            <li><a href="json-formatter-form">Format JSON</a></li>
            <li><a href="responder-config-settings">Sync Responder Config</a></li>            
          </ul>
        </li>
        <li id="helpNav"><a href="responder-help">Help</a></li>
      </ul>      
   </div>
</nav>	
