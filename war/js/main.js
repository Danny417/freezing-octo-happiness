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
};
		