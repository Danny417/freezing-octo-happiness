<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page isELIgnored="false" %>
<!DOCTYPE html>
<html>
  <head>
    <link type="text/css" rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.0/css/bootstrap.min.css" >
    <link type="text/css" rel="stylesheet" href="/stylesheets/Search.css" />
    <link type="text/css" rel="stylesheet" href="/stylesheets/rateit.css" />
    <meta name="viewport" content="width=device-width, initial-scale=1">  
    <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBwIx4LV0tdO3OMeFZBvTroBgEBkFcbDTM&sensor=true"></script>
    <script src="/js/infoBox.js" type="text/javascript"></script>
  </head>
  <body>
  	<div class="panel panel-default" style="margin:40px">
  		<div class="panel-body" id="menu">
  			<div class="btn-group">
			    <a class="btn btn-default" href="/">Home</a>			  
			    <a class="btn btn-default" href="/RegisterSpot">Register Spot</a>
				<div class="btn btn-primary" onclick="search()">Search</div>
				<div class="btn btn-warning" onclick="map.clearOverlays();">Clear</div>	
			</div>
  			<div style="float:right">
			<c:if test="${empty requestScope.user}">
				<a class="btn btn-primary" href="${login}">Sign in</a>
			</c:if>
			<c:if test="${not empty requestScope.user}">
				Welcome, ${fn:escapeXml(requestScope.user.nickname)}! <a class="btn btn-danger" href="${logout}"> sign out</a>
			</c:if>	
			</div>
		</div>
	</div>
	
	<div id="map" class="block">
		<div id="map-canvas" class="box-shadow-right-bottom" >
		</div>
	</div>
	
    <script>
    	var markers = [
    		<c:forEach items="${parkingSpots}" var="ps" varStatus="status">
    			{
    				parkingSpotID : '${fn:escapeXml(ps.parkingSpotID)}',
    				host : {
    					name : '${ps.host.name}'
    				},
    				lat : '${ps.lat}',
    				lng : '${ps.lng}',
    				address : '${ps.address}',
            description : '${ps.description}'
    			}
    			<c:if test="${!status.last}">,</c:if>
    		</c:forEach>
    	];
    </script>
	<script src="//ajax.googleapis.com/ajax/libs/jquery/2.1.0/jquery.min.js"></script>
    <script src="/js/Search.js"></script>	   	
    <script src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js" ></script>
    <script src="/js/jquery.rateit.min.js"></script>
  </body>
</html>