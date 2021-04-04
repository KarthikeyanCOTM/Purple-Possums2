<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
	<head>
		<title>CS320 TextBasedAdventureGame</title>
	</head>
	
	<body>
		<c:if test="${! empty errorMessage}">
			<div class="error">${errorMessage}</div>
		</c:if>
	
		<form action="${pageContext.servletContext.contextPath}/gameView" method="post">
			<table>
			    <tr>
					<td class="label">Message: </td>
					<td class="message">${message}</td>
				</tr>
				<tr>
       				<td class="label">Command: </td>
       				<td><input type="text" name="command" size="20" value="${command}" /></td>
			    </tr>
			</table>
			<input type="Submit" name="submitaction" value="Enter Command">
		</form>
	</body>
</html>
