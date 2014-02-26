<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page isELIgnored="false" %>
<html>
  <head>
    <link type="text/css" rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.0/css/bootstrap.min.css" >
    <link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
    <meta name="viewport" content="width=device-width, initial-scale=1">  
    <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBwIx4LV0tdO3OMeFZBvTroBgEBkFcbDTM&sensor=true"></script> 
    <script>
    	var markers = [];
    </script>
  </head>
  <body>
  	<div class="alert alert-info" style="margin:40px 40px 0 40px">
		<c:if test="${empty requestScope.user}">
			Hello! <a class="alert-link" href="${login}">Sign in</a> to include your name with greetings you post.
		</c:if>
		<c:if test="${not empty requestScope.user}">
			Hello, ${fn:escapeXml(requestScope.user.nickname)}! (You can <a class="alert-link" href="${logout}"> sign out</a>.)
		</c:if>
	</div>
	
	<div id="content">
		<div id="guestbook" class="block">
			<div class="box-shadow-left-bottom panel panel-success">
				<div class="panel-heading"><c:out value="${guestbookMsg}" /> <span class="badge"><c:out value="${fn:length(requestScope.greetings)}" /></span></div>
				<div class="panel-body">
			    <table class="table-hover">
			    <c:forEach items="${requestScope.greetings}" var="greeting" end="9">
			    	<tr><td>
			    	<blockquote>
					  <p style="max-width:500px">${fn:escapeXml(greeting.properties.content)}</p>
					  <footer>
					  	written by <span class="label label-default">${fn:escapeXml(greeting.properties.user)}</span> on ${fn:escapeXml(greeting.properties.date)}<br />
					  	at location : <fmt:formatNumber type="number" maxFractionDigits="6" value="${greeting.properties.latitude}"/>, 
					  		<fmt:formatNumber type="number" maxFractionDigits="6" value="${greeting.properties.longitude}"/>
					  		in range of ${fn:escapeXml(greeting.properties.accuracy)} meters.
					  </footer>
					</blockquote>
					<script>
						var marker = new google.maps.Marker({
						    position: new google.maps.LatLng(${greeting.properties.latitude}, ${greeting.properties.longitude}),
						    title: "${fn:escapeXml(greeting.properties.user)}"
						});
						markers.push(marker);		
					</script>
					</td></tr>
			    </c:forEach>
			    </table>
			    </div>
		    </div>		    
		    <form action="/" method="post">
		      <div><textarea class="form-control" name="content" rows="3" cols="60" placeholder="Enter message"></textarea></div>
		      <div><input type="submit" class="btn btn-primary" value="Post Greeting" /></div>
		      <input type="hidden" name="guestbookName" value="${fn:escapeXml(guestbookName)}"/>
		      <input type="hidden" name="coordinate" />
		    </form> 
		</div>
		
		<div id="map" class="block">
			<div id="map-canvas" class="box-shadow-right-bottom" >
			</div>
		</div>
	</div>
    <script type="text/javascript">	     	
      	function loadMapScript() { //load google map javascript after the page is fully loaded
			var script = document.createElement('script');
			script.type = 'text/javascript';
			script.src = 'https://maps.googleapis.com/maps/api/js?key=AIzaSyBwIx4LV0tdO3OMeFZBvTroBgEBkFcbDTM&sensor=true';
			document.body.appendChild(script);
		};
		
		function setPosition(position) {
			var mapLatLng = position || {latitude : 0, longitude : 0, accuracy : 'UNKNOWN'};
			document.getElementsByName("coordinate")[0].setAttribute('value', 
				"latitude:"+mapLatLng.latitude+",longitude:"+mapLatLng.longitude+",accuracy:"+mapLatLng.accuracy);	
				
			var mapOptions = {
          		center: new google.maps.LatLng(mapLatLng.latitude || 49.28, mapLatLng.longitude || -123.12),
          		zoom: 12
        	};
        	var map = new google.maps.Map(document.getElementById("map-canvas"), mapOptions);
        	
        	if(position) {
	        	var marker = new google.maps.Marker({
				    position: mapOptions.center,
				    title: "Current Position",
				    icon: new google.maps.MarkerImage("http://chart.apis.google.com/chart?chst=d_map_xpin_icon&chld=pin_star|home|4488FF",
				    		new google.maps.Size(20, 40),
				    		new google.maps.Point(0,0)),
				    zIndex: 99999
				});
				markers.push(marker);
			}
			showMarkers(map);
		};
				
		function showMarkers(map) {
			if(markers) {
				for(i in markers) {
					markers[i].setMap(map);
				}
			}
		};
		
		window.onload = function() {
			if (navigator.geolocation) {
				navigator.geolocation.getCurrentPosition(
					function(position) {
						setPosition(position.coords);
					}, function() {
						setPosition();
					});
			} else {
				setPosition();
			}
		};
    </script> 
  </body>
</html>