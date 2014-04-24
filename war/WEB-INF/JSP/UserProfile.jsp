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
    <link type="text/css" rel="stylesheet" href="/stylesheets/userProfile.css" />
    <meta name="viewport" content="width=device-width, initial-scale=1">  

  </head>
  <body>

  <div style="float:right; margin-right:40px;"><a class="btn btn-primary" href="/">Back to Home</a>
      </div>
  
  <h1 style="margin:40px; font-family:Harabara;"><font color="black">${user}</font></h1>
      
      
    <hr style="margin-left:40px; margin-right:40px; border-color:white;">

    <div class="panel panel-info"  style="margin:40px;">
      <div class="panel-heading">
        <h3 class="panel-title">Booking History</h3>
      </div>
    <div class="panel-body">
      <table class="table table-condensed" id="rentSpotsTable">
      </table>
    </div>
  </div>

   <div class="panel panel-info"  style="margin:40px;">
      <div class="panel-heading">
        <h3 class="panel-title">Own Parking Spots</h3>
      </div>
    <div class="panel-body">
      <table class="table table-condensed" id="ownedSpotsTable">
      </table>
    </div>
  </div>
  

  

<hr style="margin-left:40px; margin-right:40px; border-color:white;">
<p style="color:white; text-align:right; margin-right:40px">&#169; OurParkingSpot.com</p>

	<script src="//ajax.googleapis.com/ajax/libs/jquery/2.1.0/jquery.min.js"></script> 	
  	<script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>

    <script>
      var ownedSpots = [
        <c:forEach items="${ownedParkingSpot}" var="ownedParkingSpot" varStatus="status">
        {
          lat : '${ownedParkingSpot.lat}',
          lng : '${ownedParkingSpot.lng}',
          address : '${ownedParkingSpot.address}',
          price : '${ownedParkingSpot.price}'
        }
        <c:if test="${!status.last}">,</c:if>
        </c:forEach>
       ];
    </script>

    <script>
      var rentSpots = [
        <c:forEach items="${relatedRegisterEntries}" var="relatedRegisterEntries" varStatus="status">
        {
          id : '${relatedRegisterEntries.registerParkingEntryKey}',
          lat : '${relatedRegisterEntries.parkingSpot.lat}',
          lng : '${relatedRegisterEntries.parkingSpot.lng}',
          address : '${relatedRegisterEntries.parkingSpot.address}',
          price : '${relatedRegisterEntries.parkingSpot.price}',
          bookingDate : '${relatedRegisterEntries.bookingDate}'
        }
        <c:if test="${!status.last}">,</c:if>
        </c:forEach>
       ];
    </script>
    <script src="//ajax.googleapis.com/ajax/libs/jquery/2.1.0/jquery.min.js"></script>
    <script src="/js/UserProfile.js"></script> 
  </body>
</html>