<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
	<head>
		<title>CS320 TextBasedAdventureGame</title>
	</head>
	
	<body style="background-color: black; color: white;">
		<c:if test="${! empty errorMessage}">
			<div class="error">${errorMessage}</div>
		</c:if>
	
		<form action="${pageContext.servletContext.contextPath}/gameView" method="post">
			
					<div style="position: absolute; right: 200px; width: 240px; float: left; padding:10px;">
					<span class="label">Player HP: </span>
					<span class="health">${health}</span>
					</div>
				
			<table>
				<tr>
				<div style="width:350px; height:200px; overflow:auto; padding:5px;">
				<span class="previous">${previous}</span>
				</div>
				
				</tr>
			    <tr>
					<td class="label">Message: </td>
					<td class="message">${message}</td>
				</tr>
				<tr>
       				<td class="label">Command: </td>
       				<td><input type="text" name="command" size="20" value="${command}" /></td>
			    </tr>
			</table>
			<input type="Submit" name="submitaction" value="Enter Command" size="50">
		</form>
	</body>
</html>
