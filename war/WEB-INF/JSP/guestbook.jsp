<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page isELIgnored="false" %>
<html>
  <head>
    <link type="text/css" rel="stylesheet" href="/stylesheets/main.css" />
  </head>
  <body>

	<c:if test="${empty requestScope.user}">
		<p>Hello! <a href="${login}">Sign in</a> to include your name with greetings you post.</p>
	</c:if>
	<c:if test="${not empty requestScope.user}">
		<p>Hello, ${fn:escapeXml(requestScope.user.nickname)}! (You can <a href="${logout}"> sign out</a>.)</p>
	</c:if>
	
 	<c:if test="${fn:length(requestScope.greetings) > 0}">    
	    <p>Messages in Guestbook '${fn:escapeXml(guestbookName)}'.</p>	
	    <table>
	    <c:forEach items="${requestScope.greetings}" var="greeting">
	    	<tr>
	    		<td><span class="user">${fn:escapeXml(greeting.properties.user)}</span> on ${fn:escapeXml(greeting.properties.date)} wrote:</td>
	    	</tr>
	    	<tr>
	    		<td>${fn:escapeXml(greeting.properties.content)}</td>
	    	</tr>
	    </c:forEach>
	    </table>
    </c:if>
    <c:if test="${fn:length(requestScope.greetings) eq 0}">
        <p>Guestbook '${fn:escapeXml(guestbookName)}' has no messages.</p>
    </c:if>
    
    <form action="/" method="post">
      <div><textarea name="content" rows="3" cols="60"></textarea></div>
      <div><input type="submit" value="Post Greeting" /></div>
      <input type="hidden" name="guestbookName" value="${fn:escapeXml(guestbookName)}"/>
    </form>      
  </body>
</html>