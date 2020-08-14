<%@ page import="static am.basic.springTest.util.constants.ParameterKeys.MESSAGE_ATTRIBUTE_KEY" %><%--
  Created by IntelliJ IDEA.
  User: Grigor
  Date: 12-Jul-20
  Time: 2:39 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>title</title>
</head>
<body>
<a href="<%=request.getServletContext().getContextPath()%>/secure/home">Home</a>

<br><br>
<% if (request.getAttribute(MESSAGE_ATTRIBUTE_KEY) != null) { %>
<%=request.getAttribute(MESSAGE_ATTRIBUTE_KEY)%>
<% } %>
<br><br>
<form method="post" action="<%=request.getServletContext().getContextPath()%>/secure/change-password">
    old password : <input type="text" name="password"><br>
    new password : <input type="text" name="newPassword"><br>
    confirm password: <input type="text" name="confirmingPassword"><br>

    <input type="submit" name="submit"><br>
</form>
</body>
</html>
