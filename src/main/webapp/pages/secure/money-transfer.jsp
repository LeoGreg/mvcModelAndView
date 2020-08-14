<%@ page import="static am.basic.springTest.util.constants.ParameterKeys.MESSAGE_ATTRIBUTE_KEY" %>
<%@ page import="static am.basic.springTest.util.constants.ParameterKeys.NUMBER_REQUEST_KEY" %><%--
  Created by IntelliJ IDEA.
  User: Grigor
  Date: 11-Jul-20
  Time: 9:22 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body><a href="<%=request.getServletContext().getContextPath()%>/logout" style="float: right">Logout</a>
<br>
<a href="<%=request.getServletContext().getContextPath()%>/secure/home">Home</a>

<a href="<%=request.getServletContext().getContextPath()%>/secure/money">money transfer</a>
<a href="<%=request.getServletContext().getContextPath()%>/secure/cards">cards</a>


<% if (request.getAttribute(MESSAGE_ATTRIBUTE_KEY) != null) { %>
<%=request.getAttribute(MESSAGE_ATTRIBUTE_KEY)%>
<% } %>
<br><br>

<form method="post" action="<%=request.getServletContext().getContextPath()%>/secure/money-transfer/transfer">
    <input type="hidden" name="card_numberFrom" value="<%=request.getAttribute(NUMBER_REQUEST_KEY)%>">
    input money  :<input type="text" name="cash">
    getter's card_number : <input type="text" name="card_numberTo">
    <input type="submit" value="transfer">
</form>
</body>
</html>
