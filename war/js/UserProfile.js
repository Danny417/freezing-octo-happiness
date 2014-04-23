var ownedSpots;

window.onload = function() {
	
	// initialize the rent table
	var rentContent = "<thead><tr><th>#</th>"+
						"<th>Location</th>"+
						"<th>Lat/Lon</th>"+
						"<th>Price per hour</th>" + 
						"<th>Book Date</th>"+
						"<th>Status</th></tr></thead>";
	
	if (rentSpots.length > 0){
		rentContent = rentContent + "<tbody>";
		
		for (var i=0; i < rentSpots.length; i++){
			var bookDate = new Date(rentSpots[i].bookingDate);
			rentContent = rentContent + "<tr>" +
							"<th>" + i + "</th>" +
							"<th>" + rentSpots[i].address + "</th>" +
							"<th>" + rentSpots[i].lat + "/" + rentSpots[i].lng + "</th>" +
							"<th>" + rentSpots[i].price + "</th>" +
							"<th>" + bookDate.toDateString() + "</th>";
			if (((new Date()).getTime()) < ((new Date(rentSpots[i].bookingDate)).getTime())){
				rentContent = rentContent + "<th><a class='btn btn-default' href='#'>Cancel</a></th></tr>";
			} else {
				rentContent = rentContent + "<th>Expired</th></tr>";
			}
		}
		rentContent = rentContent + "</tbody>";
	}
	
	
	// initialize the owned table
	var ownedContent = "<thead><tr><th>#</th>"+
					"<th>Location</th>"+
					"<th>Lat/Lon</th>" + 
					"<th>Price per hour</th>"+
					"<th>Status</th></tr></thead>";
	if (ownedSpots.length > 0) {
		ownedContent = ownedContent + "<tbody>";
		
		for (var i=0; i < ownedSpots.length; i++){
			ownedContent = ownedContent + "<tr>" +
							"<th>" + i + "</th>" +
							"<th>" + ownedSpots[i].address + "</th>" +
							"<th>" + ownedSpots[i].lat + "/" + ownedSpots[i].lng + "</th>" +
							"<th>" + ownedSpots[i].price + "</th>" +
							"<th></th></tr>";
		}
		ownedContent = ownedContent + "</tbody>";
	}
	document.getElementById("rentSpotsTable").innerHTML=rentContent;
	document.getElementById("ownedSpotsTable").innerHTML=ownedContent;
};