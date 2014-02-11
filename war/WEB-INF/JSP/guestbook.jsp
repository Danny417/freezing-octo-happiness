<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page isELIgnored="false" %>
<html>
  <head>
    <link type="text/css" rel="stylesheet" href="/stylesheets/bootstrap.min.css" >
    <link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
    <meta name="viewport" content="width=device-width, initial-scale=1">
  </head>
  <body>
	<table style="margin-left:10px">
		<tr>
			<td colspan="2">
				<c:if test="${empty requestScope.user}">
					<h4>Hello! <small><a href="${login}">Sign in</a> to include your name with greetings you post.</small></h4>
				</c:if>
				<c:if test="${not empty requestScope.user}">
					<h4>Hello, ${fn:escapeXml(requestScope.user.nickname)}! <small>(You can <a href="${logout}"> sign out</a>.)</small></h4>
				</c:if>
			</td>
		</tr>	
		<tr>
			<td>
				<span class="label label-info"><c:out value="${guestbookMsg}" /></span>
			    <table class="table-hover">
			    <c:forEach items="${requestScope.greetings}" var="greeting">
			    	<tr><td>
			    	<blockquote>
					  <p style="max-width:500px">${fn:escapeXml(greeting.properties.content)}</p>
					  <footer>written by <span class="label label-default">${fn:escapeXml(greeting.properties.user)}</span> on ${fn:escapeXml(greeting.properties.date)}</footer>
					</blockquote>
					</td>
			    	</tr>
			    </c:forEach>
			    </table>
			    
			    <form action="/" method="post">
			      <div><textarea class="form-control" name="content" rows="3" cols="60" placeholder="Enter message"></textarea></div>
			      <div><input type="submit" class="btn btn-default" value="Post Greeting" /></div>
			      <input type="hidden" name="guestbookName" value="${fn:escapeXml(guestbookName)}"/>
			    </form> 
		    </td>
		    <td> 
		    </td>
	    </tr>  
    </table>  
    <script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
    <script src="//netdna.bootstrapcdn.com/bootstrap/3.1.0/js/bootstrap.min.js"></script>
  </body>
</html>