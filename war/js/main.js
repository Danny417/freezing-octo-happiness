var xmlHttpReq = null;
var selectedMarkerID;
var markers;

function loadMarkers(){
	try {
		xmlHttpReq = new XMLHttpRequest();
		xmlHttpReq.onreadystatechange = httpCallBackFunction_loadMarkers;
		var url = "/resources/markers.xml";
	
		xmlHttpReq.open('GET', url, true);
		//alert(url);
    	xmlHttpReq.send(null);
    	
    	//alert("loadMarkers");
    	
	} catch (e) {
    	alert("Error: " + e);
	}	
}
function httpCallBackFunction_loadMarkers() {
	//alert("httpCallBackFunction_loadMarkers");
	
	if (xmlHttpReq.readyState == 1){
		//alert("readyState is 1");
		//updateStatusMessage("<blink>Opening HTTP...</blink>");
	}else if (xmlHttpReq.readyState == 2){
		//alert("readtState is 2, sending query");
		//updateStatusMessage("<blink>Sending query...</blink>");
	}else if (xmlHttpReq.readyState == 3){ 
		//alert("readyState is 3, receiving");
		//updateStatusMessage("<blink>Receiving...</blink>");
	}else if (xmlHttpReq.readyState == 4){
		//alert ("readyState is 4");
		var xmlDoc = null;

		if(xmlHttpReq.responseXML){
			xmlDoc = xmlHttpReq.responseXML;
			alert ("responsexml");
		}else if(xmlHttpReq.responseText){
			alert ("resposnseText")
			var parser = new DOMParser();
		 	xmlDoc = parser.parseFromString(xmlHttpReq.responseText,"text/xml");			 	
		 	
		}

		if(xmlDoc){				
			alert(xmlHttpReq.responseText);	
						
			var markerElements = xmlDoc.getElementsByTagName('marker');
			//alert(markerElements[0].getAttribute("srl"));	
			alert(markerElements.length);
			
			for(mE = 0; mE < markerElements.length; mE++) {
				var markerElement = markerElements[mE];
				
				//alert(markerElement.getAttribute("srl"));
				
				var lat = parseFloat(markerElement.getAttribute("lat"));
				var lng = parseFloat(markerElement.getAttribute("lng"));
				var srl = markerElement.getAttribute("srl");
							
				var myLatlng = new google.maps.LatLng(lat, lng);
								
				//var mrkID = ""+srl;
				//var msgbox = "msgbox_"+mrkID;				
				//var msglist = "msglist_"+mrkID; 
				//var gstBkNm = guestbookNameString; // "default"; 
													
				var marker = new google.maps.Marker({       
					position: myLatlng,
					map: map
					//title: ''+mrkID
				});
				alert("pushing marker");
				// marker.setMap(map);
				markers.push(marker);				
			}			
		}else{
			alert("No data.");
		}	
	}		
}
		
function setPosition(position) {
	var mapLatLng = position || {latitude : 0, longitude : 0, accuracy : 'UNKNOWN'};	
	var mapOptions = {
  		center: new google.maps.LatLng(mapLatLng.latitude || 49.28, mapLatLng.longitude || -123.12),
  		zoom: 12
	};
	var map = new google.maps.Map(document.getElementById("map-canvas"), mapOptions);
	loadMarkers();
	//showMarkers(map);
};
window.onload = function() {
	setPosition();
};
		
function showMarkers(map) {
	if(markers) {
		for(i in markers) {
			markers[i].setMap(map);
		}
	}
}
		