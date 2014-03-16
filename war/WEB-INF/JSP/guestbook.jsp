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
    <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBwIx4LV0tdO3OMeFZBvTroBgEBkFcbDTM&sensor=true"></script>
    <script src="/js/infoBox.js" type="text/javascript"></script>
    <script>
    	var markers = [];
    </script>
  </head>
  <body>
  	<div class="panel panel-default" style="margin:40px">
  		<div class="panel-body">
		<c:if test="${empty requestScope.user}">
			Hello! <a class="alert-link" href="${login}">Sign in</a> to include your name with greetings you post.
		</c:if>
		<c:if test="${not empty requestScope.user}">
			Hello, ${fn:escapeXml(requestScope.user.nickname)}! (You can <a class="alert-link" href="${logout}"> sign out</a>.)
		</c:if>
		</div>
	</div>
	
	<div id="map" class="block">
		<div id="map-canvas" class="box-shadow-right-bottom" >
		</div>
	</div>

    <script src="/js/main.js"></script>	   	
  </body>
</html>