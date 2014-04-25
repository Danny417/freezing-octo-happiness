var xmlHttpReq = null;
var selectedMarkerID;
var markers;
var searchMarkers = [];
var parkingMarkers = [];
var searchStr = '/SearchController';
var map;

function getURLParam(variable) {
	var query = window.location.search.substring(1);
	var vars = query.split("&");
	for (var i=0;i<vars.length;i++) {
		var pair = vars[i].split("=");
		if (pair[0] == variable) {
			return pair[1];
		}
	} 
	return null;
}
	
function search() {
	var url=searchStr+'?num='+searchMarkers.length;
	for(var i = 0; i < searchMarkers.length; i++) {
		url +='&lat'+i+'='+searchMarkers[i].position.lat()+'&lng'+i+'='+searchMarkers[i].position.lng()+'&acc'+i+'=100';
	}
	window.location = url;
	
};
window.onload = function() {
	var lat = parseFloat(getURLParam('lat0')) || 49.232241;
	var lng = parseFloat(getURLParam('lng0')) || -123.12641;
	var mapOptions = {
	  		center: new google.maps.LatLng(lat,lng),//new google.maps.LatLng(37.34, -122.03),
	  		zoom: 13,
	  		mapTypeId: google.maps.MapTypeId.HYBRID 
		};
	map = new google.maps.Map(document.getElementById("map-canvas"), mapOptions);
	google.maps.Map.prototype.clearOverlays = function() {
	  for (var i = 0; i < searchMarkers.length; i++ ) {
		  searchMarkers[i].setMap(null);
	  }
	  for(var i = 0; i < parkingMarkers.length; i++) {
		  parkingMarkers[i].setMap(null);
	  }
	  searchMarkers.length = 0;
	  parkingMarkers.length = 0;
	  markers.length = 0;
	}
	if(getURLParam('num') !=null && getURLParam('num') != '0') {
		for(var i = 0; i < parseInt(getURLParam('num')); i++) {
			placeMarker(new google.maps.LatLng(getURLParam('lat'+i), getURLParam('lng'+i)));
		}
	}
	showMarkers();
	google.maps.event.addListener(map, 'click', function(event) {
	   placeMarker(event.latLng);
	});

	
};

function placeMarker(location) {
    var marker = new google.maps.Marker({
        position: location, 
        map: map,
		title : "Search Location"
    });
    searchMarkers.push(marker);
    return marker;
};

function showMarkers() {	
	var iconBase = 'https://maps.google.com/mapfiles/kml/shapes/';
	var parkingIcon = iconBase + 'parking_lot_maps.png';	
	
	for(mE = 0; mE < markers.length; mE++) {
		var lat = parseFloat(markers[mE].lat);
		var lng = parseFloat(markers[mE].lng);
		var myLatlng = new google.maps.LatLng(lat, lng);
		markers[mE].parkingSpotID = markers[mE].parkingSpotID.replace(/&#034;/g, '');
		var marker = new google.maps.Marker({       
			position: myLatlng,
			map: map,
			title : markers[mE].parkingSpotID,
			icon : parkingIcon
		});			
		parkingMarkers.push(marker);
		addInfoBox(marker, markers[mE].parkingSpotID);
		
	}
}

function addInfoBox(marker, mrkID){
	// find the marker first
	for (i = 0; i < markers.length; i++){
		if (mrkID == (markers[i]).parkingSpotID.replace(/&#034;/g, '')){
			var theMarker = markers[i];
			break;
		}
	}
	var myOptions = {
		 content: '<div id="infoBox"><table><tr><td><div class="imgContainer"><div class="img img-thumbnail" id="img'+ mrkID +'" ></div></div>'
		 +'<div name="desc" style="word-wrap:break-word;width:220px">Description:'+theMarker.description +'<br/></div><div class="rateit bigstars" id="totalRank'+mrkID+'" data-rateit-starwidth="32" data-rateit-starheight="32"></div></td><td>'+
			'<div id="content"><div class="msglist" id="'+mrkID+'" ></div>' +
			  '<div style="padding:10px"><div class="rateit" id="rateit_'+mrkID+'"></div><textarea id="'+mrkID+'_post" rows="3" cols="10" class="form-control"></textarea><br/>' +			  
			  '<input type="button" value="Post" onclick="postAjaxRequest(\''+ mrkID +'\')"/><input type="button" value="Rent" onclick="requestRent(\''+ mrkID +'\')"/></div></div></td></tr></table></div>'
		,disableAutoPan: false
		,maxWidth: 0
		,pixelOffset: new google.maps.Size(-140, 0)
		,zIndex: null
		,closeBoxMargin: "12px 2px 0px 2px"
		,boxStyle: {
	        background: "url('http://google-maps-utility-library-v3.googlecode.com/svn/trunk/infobox/examples/tipbox.gif') no-repeat",
	        width: "600px",
	        height: "400px"
	    }
		,closeBoxURL: "http://www.google.com/intl/en_us/mapfiles/close.gif"
		,infoBoxClearance: new google.maps.Size(1, 1)
		,isHidden: false
		,pane: "floatPane"
		,enableEventPropagation: false
	};

	var ib = new InfoBox(myOptions);
	google.maps.event.addListener(marker, 'click', function() {		
		ib.open(marker.get('map'), marker);
		ib.setPosition(marker.getPosition());		
		getAjaxRequest(mrkID);
		
	});
};

function requestRent(mrkID) {
	var url = '/Rent?parkingID='+mrkID;
	window.location = url;
};

function getAjaxRequest(mrkID) {
	try {
		xmlHttpReq = new XMLHttpRequest();
		xmlHttpReq.onreadystatechange = function() {httpCallBackFunction(mrkID);}
		var url = "/reviewController?markerID="+mrkID;
		
		xmlHttpReq.open('GET', url, true);
    	xmlHttpReq.send(null);    	
	} catch (e) {
    	alert("Error: " + e);
	}	
};

function parseResponse(xmlDoc, xmlHttpReq, mrkID) {
	$('.rateit').rateit();
	$('.bigstars').rateit('readonly', true);
	$('.bigstars').rateit('value', 0);
	var jsonArray;
	if(xmlDoc) jsonArray = JSON.parse(xmlHttpReq.responseText);
	console.log(jsonArray);
	if(!(!jsonArray) && jsonArray.length > 0){			
		var htmlText = "";
		var totalRate = 0;
		for (var i = 0; i < jsonArray.length; i++){
			var username = (jsonArray[i]['username'] == undefined) ? 'nija user' : jsonArray[i]['username'];
			htmlText = htmlText + "<b>" + jsonArray[i]['date'] +" <br/>"
			+ username +"</b> rates: "
			+ jsonArray[i]['rating']+ " out of 5.<br/>" +
			jsonArray[i]['reviewMessage']+"<br/>";
			totalRate += parseInt(jsonArray[i]['rating']);
		}
		document.getElementById(mrkID).innerHTML= htmlText;
		$('.bigstars').rateit('value', totalRate/jsonArray.length);
	}
	
}

function postAjaxRequest(mrkID) {
	try {
		xmlHttpReq = new XMLHttpRequest();
		xmlHttpReq.onreadystatechange = function(){httpCallBackFunction(mrkID);}
		var url = "/reviewController";	
		xmlHttpReq.open("POST", url, true);
		xmlHttpReq.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');		
		
		var postMsgValue = document.getElementById(mrkID+'_post').value;    
		xmlHttpReq.send("content="+postMsgValue+"&markerID="+mrkID+"&rating="+$('[id="rateit_'+mrkID+'"]').rateit('value'));
		document.getElementById(mrkID+'_post').value = '';
		$('[id="rateit_'+mrkID+'"]').rateit('value', 0);
	} catch (e) {
		console.log(e);
	}	
}

function httpCallBackFunction(mrkID) {
	
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
		parseResponse(xmlDoc, xmlHttpReq, mrkID);
	}		
}