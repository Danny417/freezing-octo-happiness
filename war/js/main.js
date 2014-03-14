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
		}else if(xmlHttpReq.responseText){
			var parser = new DOMParser();
		 	xmlDoc = parser.parseFromString(xmlHttpReq.responseText,"text/xml");			 	
		 	
		}

		if(xmlDoc){				
			var tempArray = xmlDoc.getElementsByTagName('marker');
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
		console.log(markers[mE]);
		
		var lat = parseFloat(markerElement.getAttribute("lat"));
		var lng = parseFloat(markerElement.getAttribute("lng"));
		var srl = markerElement.getAttribute("srl");
		var myLatlng = new google.maps.LatLng(lat, lng);
		
		var mrkID = ""+srl;
		var msgbox = "msgbox_"+mrkID;				
		var msglist = "msglist_"+mrkID; 
		var gstBkNm = "default"; //guestbookNameString; // "default"; 
		
		
		
		if (mE < parseInt(markers.length/2)) {
			var marker = new google.maps.Marker({       
				position: myLatlng,
				map: map,
				title : mrkID
			});	
			marker.setMap(map);
			
			addInfowindow(marker, mrkID);
		} else {
			var parkingMarker = new google.maps.Marker({       
				  position: myLatlng,
				  map: map,
				  icon: parkingIcon,
				  title : mrkID
			}); 
			parkingMarker.setMap(map);
			addInfowindow(parkingMarker, mrkID);
		}
	}
}

function addInfowindow(marker, mrkID) {
	var content  = 
		'<div id="content"><div class="msglist" id="'+mrkID+'"></div></div>' +
	  '<textarea id="'+mrkID+'_post" rows="2" cols="20"></textarea>' +			  
	  '<input type="button" value="Post" onclick="postAjaxRequest('+ 
		"'', '" + mrkID + "', '', ''" +')"/>';
	var infowindow = new google.maps.InfoWindow({
			content: content
	});
	google.maps.event.addListener(marker, 'click', function() {
		
		
		if (!markers[mrkID]['greeting']){
			getAjaxRequest();
		} else {
			var htmlText = '<div id="content"><div class="msglist" id="'+mrkID+'"><div>';
			for (var i = 0; i < markers[mrkID]["greeting"].length; i++){
				htmlText = htmlText +markers[mrkID]['greeting'][i]['propertyMap']['user']+" "+
				markers[mrkID]['greeting'][i]['propertyMap']['date']+" writes:<br>"+
				markers[mrkID]['greeting'][i]['propertyMap']['content']+"<br>";
			}
			htmlText = htmlText+ '</div></div></div>' +
				'<textarea id="'+mrkID+'_post" rows="2" cols="20"></textarea>' +			  
				'<input type="button" value="Post" onclick="postAjaxRequest('+ 
				"'', '" + mrkID + "', '', ''" +')"/>';
			infowindow.setContent(""+htmlText);
		}
		infowindow.setPosition(marker.getPosition());
		infowindow.open(marker.get('map'), marker);	
		
	});
	
	
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

			var jsonArray = eval(xmlHttpReq.responseText);
			console.log(jsonArray[0].propertyMap.markerID);
			
			var id = jsonArray[0].propertyMap.markerID;
			markers[id]["greeting"] = jsonArray;
			
			
			var htmlText = "<div>";
			console.log(markers[id]["greeting"].length);
			for (var i = 0; i < markers[id]["greeting"].length; i++){
				htmlText = htmlText +markers[id]['greeting'][i]['propertyMap']['user']+" "+
				markers[id]['greeting'][i]['propertyMap']['date']+" writes:<br>"+
				markers[id]['greeting'][i]['propertyMap']['content']+"<br>";
			}
			htmlText = htmlText+ "</div>";
			console.log(htmlText);
			document.getElementById(id).innerHTML= htmlText;
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
		
		var postMsgValue = document.getElementById(markerID+'_post').value;
		console.log(postMsgValue);
		//var markerIDValue = markerID; 
		//var guestbookNameValue = guestbookName; 
    	
		xmlHttpReq.send("content="+postMsgValue+"&markerID="+markerID);
    	
    	//alert();
    	
	} catch (e) {
    	alert("Error: " + e);
	}	
}

function showGreetings(index, infowindow){
	
	if (!markers[index]['greeting']){
		console.log('ajax');
		getAjaxRequest();
	} else {
		var htmlText = "<div>";
		for (var i = 0; i < markers[index]["greeting"].length; i++){
			htmlText = htmlText +markers[index]['greeting'][i]['propertyMap']['user']+" "+
			markers[index]['greeting'][i]['propertyMap']['date']+" writes:<br>"+
			markers[index]['greeting'][i]['propertyMap']['content']+"<br>";
		}
		htmlText = htmlText+ "</div>";
		console.log('no ajax');
		console.log(document.getElementById(1));
		document.getElementById(1).innerHTML= htmlText;
		//console.log(document.getElementById(index).innerHTML);
	}

	infowindow.setContent(htmlText);
	infowindow.setPosition(marker.getPosition());
	infowindow.open(marker.get('map'), marker);	
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