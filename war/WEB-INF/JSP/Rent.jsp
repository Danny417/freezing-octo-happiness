<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page isELIgnored="false" %>
<!DOCTYPE html>
<html>
  <head>
    <link type="text/css" rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.0/css/bootstrap.min.css" >
    <link rel="stylesheet" href="//code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">
    <link type="text/css" rel="stylesheet" href="/stylesheets/rent.css" />
    <meta name="viewport" content="width=device-width, initial-scale=1">  

  </head>
  <body>

    <h1 style="margin:40px; font-family:Harabara;"><font color="black">When do you want to rent it?</font></h1>
    <hr style="margin-left:40px; margin-right:40px; border-color:white;">

    <div class="panel panel-success"  style="margin:40px;">
      <div class="panel-heading">
        <h3 class="panel-title">Parking spot detail </h3>
      </div>
      <div class="panel-body">
    
  <form class="form-horizontal" role="form">

    <!-- parking spot ID-->
     <div class="form-group">
        <label class="col-sm-2 control-label">Parking Spot ID</label>
        <div class="col-sm-10">
          <p class="form-control-static">${parkingSpot.parkingSpotID}</p>
        </div>
    </div>

    <!-- host name -->
  <div class="form-group">
        <label class="col-sm-2 control-label">Host Name</label>
        <div class="col-sm-10">
          <p class="form-control-static">${parkingSpot.host.name}</p>
        </div>
    </div>
  
  <!-- Price -->
  <div class="form-group">
        <label class="col-sm-2 control-label">Price per hour</label>
        <div class="col-sm-10">
          <p class="form-control-static">CAD $${parkingSpot.price}</p>
        </div>
    </div>

  <!-- Latitude -->
  <div class="form-group">
        <label class="col-sm-2 control-label">Latitude</label>
        <div class="col-sm-10">
          <p class="form-control-static">${parkingSpot.lat}</p>
        </div>
    </div>

  <!-- Longitude -->
  <div class="form-group">
        <label class="col-sm-2 control-label">Longitude</label>
        <div class="col-sm-10">
          <p class="form-control-static">${parkingSpot.lng}</p>
        </div>
    </div>
  
  <!-- Adress -->
  <div class="form-group">
        <label class="col-sm-2 control-label">Adress</label>
        <div class="col-sm-10">
          <p class="form-control-static">${parkingSpot.address}</p>
        </div>
    </div>

  <!-- Additional Description-->
  <div class="form-group">
        <label class="col-sm-2 control-label">Description</label>
        <div class="col-sm-10">
          	<p class="form-control-static">
          	<c:if test="${not empty requestScope.parkingSpot.description}">
			${fn:escapeXml(parkingSpot.description)}
			</c:if>	
          	</p>
        </div>
    </div>
    
</form>
</div></div>

   <div class="panel panel-success"  style="margin:40px;">
      <div class="panel-heading">
        <h3 class="panel-title">Rent Period detail </h3>
      </div>
      <div class="panel-body">
    
  <form class="form-horizontal" id="rentForm" role="form" method="post" action="Rent">
  <!-- Availability Date-->
  <div class="form-group" style="margin-right:40px">
    <label for="inputDate" class="col-sm-2 control-label">Available Date</label>
    <div class="col-sm-4">
      <input type="text" class="date form-control" id="date" placeholder="Availabile date" required>
    </div>
  </div>

  <!-- Availability Time Period-->
  <div class="form-group" style="margin-right:40px">
    <label for="inputStartTime" class="col-sm-2 control-label">From</label>
    <div class="col-sm-4">
      <input type="time" class="form-control" name="startTime" placeholder="Availability start time" required>
    </div>
  </div>
  <div class="form-group" style="margin-right:40px">
    <label for="inputEndTime" class="col-sm-2 control-label">To</label>
    <div class="col-sm-4">
      <input type="time" class="form-control" name="endTime" placeholder="Availability start time" required>
    </div>
  </div>

  <!-- Availability check -->
  <div class="form-group">
    <div class="col-sm-offset-2 col-sm-10">
      <button type="submit" class="btn btn-default">Check availability for chosen time slot</button>
    </div>
  </div>

  <!-- Submit and button -->
  <div class="form-group">
    <div class="col-sm-offset-2 col-sm-10">
      <button type="submit" class="btn btn-default">Rent this spot!</button>
      <a class="btn btn-default" href="/SearchController">Back to Search Page</a>
    </div>
  </div>
</form>
</div>
</div>

<hr style="margin-left:40px; margin-right:40px; border-color:white;">
<p style="color:white; text-align:right; margin-right:40px">&#169; OurParkingSpot.com</p>

	<script src="//ajax.googleapis.com/ajax/libs/jquery/2.1.0/jquery.min.js"></script> 	
  	<script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
	<script>
		$('.date').datepicker({dateFormat: 'MM d, yy', minDate:0});
	</script>
  </body>
</html>