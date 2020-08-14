<%@ page import="am.basic.springTest.model.User" %><%--
  Created by IntelliJ IDEA.
  User: ruben.manukyan
  Date: 5/23/2020
  Time: 12:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

<% if (request.getAttribute("message") != null) {  %>
<%=request.getAttribute("message")%>
<% } %>
<br><br>

<form method="post" action="<%=request.getServletContext().getContextPath()%>/register">
    name : <input type="text" name="name"><br/><small></small>
    surname : <input type="text" name="surname"><br>
    username : <input type="text" name="username"><br/>
    password : <input type="text" name="password"><br>
    g_mail: <input type="text" name="g_mail"><br>
    mm/dd/yyyy:<input type="text" name="dataOfBirth"><br>
    Gender:
    <select name="userGender">
        <option><%=User.Gender.MALE%></option>
        <option><%=User.Gender.FEMALE%></option>
        <option><%=User.Gender.OTHER%></option>
    </select>
    Country:
    <select name="userCountry">
        <option>India</option>
        <option>Pakistan</option>
        <option>Armenia</option>
        <option>America</option>
    </select>
    <input type="submit" name="submit"><br>
</form>




</body>
</html>
