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
							"<td>" + i + "</td>" +
							"<td>" + rentSpots[i].address + "</td>" +
							"<td>" + rentSpots[i].lat + "/" + rentSpots[i].lng + "</td>" +
							"<td>" + rentSpots[i].price + "</td>" +
							"<td>" + bookDate.toDateString() + "</td>";
			if (((new Date()).getTime()) < ((new Date(rentSpots[i].bookingDate)).getTime())){
				rentContent = rentContent + "<td><a class='btn btn-default' href='/UserProfile?entryId="+rentSpots[i].id+"'>Cancel</a></td></tr>";
			} else {
				rentContent = rentContent + "<td>Expired</td></tr>";
			}
		}
		rentContent = rentContent + "</tbody>";
	}
	
	
	// initialize the owned table
	var ownedContent = "<thead><tr><th>#</th>"+
					"<th>Location</th>"+
					"<th>Lat/Lon</th>" + 
					"<th>Price per hour</th>"+
					"</tr></thead>";
	if (ownedSpots.length > 0) {
		ownedContent = ownedContent + "<tbody>";
		
		for (var i=0; i < ownedSpots.length; i++){
			ownedContent = ownedContent + "<tr>" +
							"<td>" + i + "</td>" +
							"<td>" + ownedSpots[i].address + "</td>" +
							"<td>" + ownedSpots[i].lat + "/" + ownedSpots[i].lng + "</td>" +
							"<td>" + ownedSpots[i].price + "</td>" +
							"</tr>";
		}
		ownedContent = ownedContent + "</tbody>";
	}
	document.getElementById("rentSpotsTable").innerHTML=rentContent;
	document.getElementById("ownedSpotsTable").innerHTML=ownedContent;
};