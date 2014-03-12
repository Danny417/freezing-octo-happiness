function loadMapScript() { //load google map javascript after the page is fully loaded
	var script = document.createElement('script');
	script.type = 'text/javascript';
	script.src = 'https://maps.googleapis.com/maps/api/js?key=AIzaSyBwIx4LV0tdO3OMeFZBvTroBgEBkFcbDTM&sensor=true';
	document.body.appendChild(script);
};
		
function setPosition(position) {
	var mapLatLng = position || {latitude : 0, longitude : 0, accuracy : 'UNKNOWN'};	
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
		
function showMarkers(map) {
	if(markers) {
		for(i in markers) {
			markers[i].setMap(map);
		}
	}
}
function getAjaxRequest() {
	//alert("getAjaxRequest");
	try {
		xmlHttpReq = new XMLHttpRequest();
		xmlHttpReq.onreadystatechange = httpCallBackFunction_getAjaxRequest;
		var url = "/ajax/?markerID=1&guestbookName=test";
		
		xmlHttpReq.open('GET', url, true);
    	xmlHttpReq.send(null);
    	
    	//alert();
    	
	} catch (e) {
    	alert("Error: " + e);
	}	
}

function httpCallBackFunction_getAjaxRequest() {
	//alert("httpCallBackFunction_getAjaxRequest");
	
	if (xmlHttpReq.readyState == 1){
		//updateStatusMessage("<blink>Opening HTTP...</blink>");
	}else if (xmlHttpReq.readyState == 2){
		//updateStatusMessage("<blink>Sending query...</blink>");
	}else if (xmlHttpReq.readyState == 3){ 
		//updateStatusMessage("<blink>Receiving...</blink>");
	}else if (xmlHttpReq.readyState == 4){
		var xmlDoc = null;

		if(xmlHttpReq.responseXML){
			xmlDoc = xmlHttpReq.responseXML;
		}else if(xmlHttpReq.responseText){
			var parser = new DOMParser();
		 	xmlDoc = parser.parseFromString(xmlHttpReq.responseText,"text/xml");			 	
		}

		if(xmlDoc){				
			console.log(xmlHttpReq.responseText);			
			//document.getElementById("msglist_"+selectedMarkerID).innerHTML=xmlHttpReq.responseText;					
		}else{
			alert("No data.");
		}	
	}		
}
function postAjaxRequest(postMsg, markerID, guestbookName, rspMsgList) {
	//alert("postAjaxRequest");
	try {
		xmlHttpReq = new XMLHttpRequest();
		xmlHttpReq.onreadystatechange = httpCallBackFunction_postAjaxRequest;
		var url = "/ajax";
	
		xmlHttpReq.open("POST", url, true);
		xmlHttpReq.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');		
		
		//var postMsgValue = document.getElementById(postMsg).value;
		//var markerIDValue = markerID; 
		//var guestbookNameValue = guestbookName; 
    	
		xmlHttpReq.send("content=testPost&markerID=1");
    	
    	//alert();
    	
	} catch (e) {
    	alert("Error: " + e);
	}	
}

function httpCallBackFunction_postAjaxRequest() {
	//alert("httpCallBackFunction_postAjaxRequest");
	
	if (xmlHttpReq.readyState == 1){
		//updateStatusMessage("<blink>Opening HTTP...</blink>");
	}else if (xmlHttpReq.readyState == 2){
		//updateStatusMessage("<blink>Sending query...</blink>");
	}else if (xmlHttpReq.readyState == 3){ 
		//updateStatusMessage("<blink>Receiving...</blink>");
	}else if (xmlHttpReq.readyState == 4){
		var xmlDoc = null;

		if(xmlHttpReq.responseXML){
			xmlDoc = xmlHttpReq.responseXML;			
		}else if(xmlHttpReq.responseText){
			var parser = new DOMParser();
		 	xmlDoc = parser.parseFromString(xmlHttpReq.responseText,"text/xml");		 		
		}
		
		if(xmlDoc){				
			console.log(xmlHttpReq.responseText);			
			//document.getElementById("msglist_"+selectedMarkerID).innerHTML=xmlHttpReq.responseText;
			//document.getElementById("msgbox_"+selectedMarkerID).value = "";
		}else{
			alert("No data.");
		}	
	}		
}