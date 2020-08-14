<%@ page import="static am.basic.springTest.util.constants.ParameterKeys.MESSAGE_ATTRIBUTE_KEY" %><%--
  Created by IntelliJ IDEA.
  User: Grigor
  Date: 13-Jul-20
  Time: 12:56 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<a href="<%=request.getServletContext().getContextPath()%>/logout" style="float: right">Logout</a>
<br>
<a href="<%=request.getServletContext().getContextPath()%>/secure/home">Home</a>
<br>
<br><br>
<% if (request.getAttribute(MESSAGE_ATTRIBUTE_KEY) != null) { %>
<%=request.getAttribute(MESSAGE_ATTRIBUTE_KEY)%>
<% } %>
<br><br>

<form method="post" action="<%=request.getServletContext().getContextPath()%>/secure/card-sign-in/sign-in">
    your card number : <input type="text" name="card_number">
    <input type="submit" value="card sign in">
</form>
</body>
</html>
