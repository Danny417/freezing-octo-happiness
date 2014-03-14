var xmlHttpReq = null;
var selectedMarkerID;
var markers;
var map;

function loadMarkers(){
	try {
		xmlHttpReq = new XMLHttpRequest();
		xmlHttpReq.onreadystatechange = httpCallBackFunction_loadMarkers;
		var url = "/resources/markers.xml";
	
		xmlHttpReq.open('GET', url, true);
    	xmlHttpReq.send(null);
    	
	} catch (e) {
    	alert("Error: " + e);
	}	
}
function httpCallBackFunction_loadMarkers() {	
	if (xmlHttpReq.readyState == 1){
	}else if (xmlHttpReq.readyState == 2){
	}else if (xmlHttpReq.readyState == 3){ 
	}else if (xmlHttpReq.readyState == 4){
		var xmlDoc = null;

		if(xmlHttpReq.responseXML){
			xmlDoc = xmlHttpReq.responseXML;
		}else if(xmlHttpReq.responseText){
			var parser = new DOMParser();
		 	xmlDoc = parser.parseFromString(xmlHttpReq.responseText,"text/xml");	 
		}
		if(xmlDoc){				
			var tempArray = xmlDoc.getElementsByTagName('marker');
			console.log(tempArray);
			for (var i = 0; i < tempArray.length; i++){
				var temp = new Object();
				temp.marker = tempArray[i];
				temp.id = "";
				temp.greeting;
				markers.push(temp);
			}
			showMarkers();
		}else{
			alert("No data.");
		}	
	}		
}
		
window.onload = function() {
	var mapOptions = {
	  		center: new google.maps.LatLng(37.34, -122.03),
	  		zoom: 12
		};
	map = new google.maps.Map(document.getElementById("map-canvas"), mapOptions);
	loadMarkers();
};

function showMarkers() {	
	var iconBase = 'https://maps.google.com/mapfiles/kml/shapes/';
	var parkingIcon = iconBase + 'parking_lot_maps.png';		     
	for(mE = 0; mE < markers.length; mE++) {
		var markerElement = markers[mE]['marker'];
		
		var lat = parseFloat(markerElement.getAttribute("lat"));
		var lng = parseFloat(markerElement.getAttribute("lng"));
		var srl = markerElement.getAttribute("srl");
		var myLatlng = new google.maps.LatLng(lat, lng);		
		var mrkID = ""+srl;
		markers[mE]['id'] = mrkID;
		var marker = new google.maps.Marker({       
			position: myLatlng,
			map: map,
			title : mrkID,
			icon : (mE < parseInt(markers.length/2)) ? null : parkingIcon
		});	

		marker.setMap(map);		
		addInfowindow(marker, mrkID);		
	}
}

function addInfowindow(marker, mrkID) {
	var infowindow = new google.maps.InfoWindow({
			content: '<div id="content"><div class="msglist" id="'+mrkID+'"></div></div>' +
			  '<textarea id="'+mrkID+'_post" rows="2" cols="20"></textarea>' +			  
			  '<input type="button" value="Post" onclick="postAjaxRequest(\''+ mrkID +'\')"/>'
	});
	var index = mrkID - 1;
	google.maps.event.addListener(marker, 'click', function() {
		infowindow.setPosition(marker.getPosition());
		infowindow.open(marker.get('map'), marker);			
		if (!markers[index]['greeting']){
			getAjaxRequest(mrkID);
		} else {
			var htmlText = '<div id="content"><div class="msglist" id="'+mrkID+'"><div>';
			for (var i = 0; i < markers[index]["greeting"].length; i++){
				htmlText = htmlText +markers[index]['greeting'][i]['propertyMap']['user']+" "+
				markers[index]['greeting'][i]['propertyMap']['date']+" writes:<br>"+
				markers[index]['greeting'][i]['propertyMap']['content']+"<br>";
			}
			htmlText = htmlText+ '</div></div></div>' +
				'<textarea id="'+mrkID+'_post" rows="2" cols="20"></textarea>' +			  
				'<input type="button" value="Post" onclick="postAjaxRequest(\'' + mrkID + '\')"/>';
			infowindow.setContent(""+htmlText);
		}		
	});
}

function getAjaxRequest(mrkID) {
	try {
		xmlHttpReq = new XMLHttpRequest();
		xmlHttpReq.onreadystatechange = httpCallBackFunction;
		var url = "/ajax/?markerID="+mrkID;
		
		xmlHttpReq.open('GET', url, true);
    	xmlHttpReq.send(null);    	
	} catch (e) {
    	alert("Error: " + e);
	}	
}

function parseResponse(xmlDoc, xmlHttpReq) {
	var jsonArray;
	if(xmlDoc) jsonArray = JSON.parse(xmlHttpReq.responseText);
	if(!(!jsonArray) && jsonArray.length > 0){					
		var id = jsonArray[0].propertyMap.markerID -1;
		markers[id]["greeting"] = jsonArray;			
		
		var htmlText = "<div>";
		for (var i = 0; i < markers[id]["greeting"].length; i++){
			htmlText = htmlText +markers[id]['greeting'][i]['propertyMap']['user']+" "+
			markers[id]['greeting'][i]['propertyMap']['date']+" writes:<br>"+
			markers[id]['greeting'][i]['propertyMap']['content']+"<br>";
		}
		htmlText = htmlText+ "</div>";
		document.getElementById(id+1).innerHTML= htmlText;
	}
}

function postAjaxRequest(markerID) {
	try {
		xmlHttpReq = new XMLHttpRequest();
		xmlHttpReq.onreadystatechange = httpCallBackFunction;
		var url = "/ajax";	
		xmlHttpReq.open("POST", url, true);
		xmlHttpReq.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');		
		
		var postMsgValue = document.getElementById(markerID+'_post').value;    	
		xmlHttpReq.send("content="+postMsgValue+"&markerID="+markerID);
    	
	} catch (e) {
    	alert("Error: " + e);
	}	
}

function httpCallBackFunction() {
	
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
		parseResponse(xmlDoc, xmlHttpReq);
	}		
}