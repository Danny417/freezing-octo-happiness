<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page isELIgnored="false" %>
<!DOCTYPE html>
<html>
  <head>
    <link type="text/css" rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.0/css/bootstrap.min.css" >
    <link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
    <meta name="viewport" content="width=device-width, initial-scale=1">  
  </head>
  <body>
  	<div class="panel panel-default" style="margin:40px">
  		<div class="panel-body">
  			<div class="btn-group">
			  <button type="button" class="btn btn-default">Home</button>
			  <button type="button" class="btn btn-default">Tutorial</button>
			  <div class="btn-group">
			    <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
			      Search
			      <span class="caret"></span>
			    </button>
			    <ul class="dropdown-menu">
			      <li><a href="/SearchController">By Map</a></li>
			      <li><a href="/SearchController">By Text</a></li>
			    </ul>
			  </div>
			</div>
  			<div style="float:right">
			<c:if test="${empty requestScope.user}">
				<a class="btn btn-primary" href="${login}">Sign in</a>
				<a class="btn btn-success" href="${login}">Register</a>
			</c:if>
			<c:if test="${not empty requestScope.user}">
				Hello, ${fn:escapeXml(requestScope.user.nickname)}! <a class="btn btn-warning" href="${logout}"> sign out</a>
			</c:if>
			</div>
		</div>
	</div>
  	Main Page : I am out for dinner. 
	<script src="//ajax.googleapis.com/ajax/libs/jquery/2.1.0/jquery.min.js"></script>
    <script src="/js/main.js"></script>	   	
    <script src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js" ></script>
  </body>
</html>