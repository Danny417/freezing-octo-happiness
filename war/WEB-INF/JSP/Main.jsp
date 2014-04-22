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

  	<h1 style="margin:40px"><font face="Georgia" size="10" color="white"> Find a place to park. </font></h1>
  	<hr style="margin-left:40px; margin-right:40px; border-color:white;">
  	<div class="panel panel-default" style="margin:40px">
		<div class="swiper-container">
			<div class="swiper-wrapper">
				<div class="swiper-slide"><a href="#"><img src="/img/tutorial.png"></a></div>
				<div class="swiper-slide">
					<c:if test="${empty requestScope.user}">
						<a href="${login}"><img src="/img/sign_in.png"></a>
					</c:if>
					<c:if test="${not empty requestScope.user}">
						<a href="${logout}"><img src="/img/sign_in.png"></a>
					</c:if>
				</div>
				<div class="swiper-slide"><a href="/SearchController"><img src="/img/advanced_search.png"></a></div>
				<div class="swiper-slide"><a href="#"><img src="/img/quick_search.png"></a></div>
				<div class="swiper-slide"><a href="#"><img src="/img/register.png"></a></div>
				
			</div>
		</div>
	</div>
	</div>
	<hr style="margin-left:40px; margin-right:40px; border-color:white;">
	<p style="color:white; text-align:right; margin-right:40px">&#169; OurParkingSpot.com</p>
  	
  	<!--div class="panel panel-default" style="margin:40px">
    	<div class="panel-body">
    		<div class="swiper-container">
			<div class="swiper-wrapper">
				<div class="swiper-slide" style="background-color:white">
					<c:if test="${empty requestScope.user}">
						<a href="${login}"><img src="/img/signin.png"></a>
					</c:if>
					<c:if test="${not empty requestScope.user}">
						<a href="${logout}"><img src="/img/signin.png"></a>
					</c:if>
				</div>
				<div class="swiper-slide"><a href="/SearchController"><img src="/img/advanced_search.png"></a></div>
				<div class="swiper-slide"><a href="#"><img src="/img/quick_search.png"></a></div>
				<div class="swiper-slide"><a href="#"><img src="/img/register.png"></a></div>
				<div class="swiper-slide"><a href="#"><img src="/img/tutorial.png"></a></div>
			</div>
		</div>
    	</div>
	
	</div-->

  	<!--div class="panel panel-default" style="margin:40px">
  		<div class="panel-body">
  			<div class="btn-group">
			  <button type="button" class="btn btn-default">Home</button>
			  <button type="button" class="btn btn-default">Tutorial</button>
			  <div class="btn-group">
			    <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
			      Search
			      <span class="caret"></span>
			    </button>
			    <ul class="dropdown-menu">
			      <li><a href="/SearchController">By Map</a></li>
			      <li><a href="/SearchController">By Text</a></li>
			    </ul>
			  </div>
			</div>
  			<div style="float:right">
			<c:if test="${empty requestScope.user}">
				<a class="btn btn-primary" href="${login}">Sign in</a>
				<a class="btn btn-success" href="${login}">Register</a>
			</c:if>
			<c:if test="${not empty requestScope.user}">
				Hello, ${fn:escapeXml(requestScope.user.nickname)}! <a class="btn btn-warning" href="${logout}"> sign out</a>
			</c:if>
			</div>
		</div>
	</div-->
  	
		
	<script src="//ajax.googleapis.com/ajax/libs/jquery/2.1.0/jquery.min.js"></script>
	<script src="/js/idangerous.swiper-2.0.min.js"></script>
	<script src="/js/idangerous.swiper.3dflow-2.0.js"></script>
    <script src="/js/main.js"></script>	   	
    <script src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js" ></script>
  </body>
</html>