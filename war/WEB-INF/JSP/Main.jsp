<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page isELIgnored="false" %>
<!DOCTYPE html>
<html>
  <head>
    <link type="text/css" rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.0/css/bootstrap.min.css" >
    <link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
	<link rel="stylesheet" type="text/css" href="/stylesheets/idangerous.swiper.css">
	<link rel="stylesheet" type="text/css" href="/stylesheets/idangerous.swiper.3dflow.css">
    <meta name="viewport" content="width=device-width, initial-scale=1">  
  </head>
  <body>

  	<h1 style="margin:40px; font-family:Harabara;"><font color="black" size="10">Find a place to park. </font></h1>
  	<hr style="margin-left:40px; margin-right:40px; border-color:white;">
  	<div class="swipeouttercontainner">
	<div class="swiper-container">
		<div class="swiper-wrapper">
			<c:if test="${empty requestScope.user}">
				<div class="swiper-slide" style="background-image:url(/img/sign_in.png)"><a href="${login}"></a></div>
			</c:if>
			<div class="swiper-slide" style="background-image:url(/img/quick_search.png)"><a id="quickSearch" href="/SearchController"></a></div>
			<div class="swiper-slide" style="background-image:url(/img/advanced_search.png)"><a href="/SearchController"></a></div>
			<div class="swiper-slide" style="background-image:url(/img/register.png)"><a href="/RegisterSpot"></a></div>			
			<c:if test="${not empty requestScope.user}">
				<div class="swiper-slide" style="background-image:url(/img/sign_out.png)"><a href="${logout}"></a></div>
			</c:if>
		</div>
	</div>
	</div>
	<hr style="margin-left:40px; margin-right:40px; border-color:white;">
	<p style="color:white; text-align:right; margin-right:40px">&#169; OurParkingSpot.com</p>	
		
	<script src="//ajax.googleapis.com/ajax/libs/jquery/2.1.0/jquery.min.js"></script>
    <script src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js" ></script>
	<script src="/js/idangerous.swiper-2.0.min.js"></script>
	<script src="/js/idangerous.swiper.3dflow-2.0.js"></script>
    <script src="/js/main.js"></script>	   	
  </body>
</html>