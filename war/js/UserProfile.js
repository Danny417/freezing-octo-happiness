var ownedSpots;

window.onload = function() {
	
	
	// initialize the owned table
	var ownedContent = "<thead><tr><th>Parking Spot ID</th>"+
					"<th>Location</th>"+
					"<th>Price</th>"+
					"<th>Status</th></tr></thead>";
	if (ownedSpots.length > 0) {
		ownedContent = ownedContent + "<tbody>";
		
		for (var i=0; i < ownedSpots.length; i++){
			ownedContent = ownedContent + "<tr>" +
							"<th>" + ownedSpots[i].parkingSpotID + "</th>" +
							"<th>" + ownedSpots[i].address + "</th>" + 
							"<th>" + ownedSpots[i].price + "</th>" +
							"<th></th></tr>";
		}
		ownedContent = ownedContent + "</tbody>";
	}
	document.getElementById("ownedSpotsTable").innerHTML=ownedContent;
};