<%@ page import="static am.basic.springTest.util.constants.ParameterKeys.MESSAGE_ATTRIBUTE_KEY" %>
<%@ page import="am.basic.springTest.model.Card" %>
<%@ page import="java.util.List" %>
<%@ page import="static am.basic.springTest.util.constants.ParameterKeys.CARD_REQUEST_KEY" %>
<%@ page import="am.basic.springTest.model.User" %>
<%@ page import="static am.basic.springTest.util.constants.ParameterKeys.*" %>
<%@ page import="am.basic.springTest.service.CardService" %>
<%@ page import="am.basic.springTest.service.impl.CardServiceImpl" %><%--
  Created by IntelliJ IDEA.
  User: Grigor
  Date: 11-Jul-20
  Time: 6:05 PM
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
<a href="<%=request.getServletContext().getContextPath()%>/secure/money">money transfer</a>

<br><br>
<% if (request.getAttribute(MESSAGE_ATTRIBUTE_KEY) != null) { %>
<%=request.getAttribute(MESSAGE_ATTRIBUTE_KEY)%>
<% } %>
<br><br>

<form method="post" action="<%=request.getServletContext().getContextPath()%>/secure/cards/sign_up">
    bank_branding : <input type="text" name="bank_branding">
    card_number : <input type="text" name="card_number">
    expiration_date : <input type="date" name="expiration_date" min="2018-04-01" max="2025-04-30">
    payment_network_logo : <input type="text" name="payment_network_logo">
    bank_contact_information : <input type="text" name="bank_contact_information">
    balance : <input type="text" name="balance">
    <input type="submit" value="sign up for free">
</form>
<br>
<br>


<%
    List<Card> cards = (List<Card>) request.getAttribute(CARD_REQUEST_KEY);
%>

<table border="solid 1px">


    <%
        for (Card card : cards) {
    %>
    <tr>

        <td>
            <form method="post" action="<%=request.getServletContext().getContextPath()%>/secure/cards/edit">
                <input type="hidden" name="id" value="<%=card.getId()%>">
                bank_branding : <input type="text" name="bank_branding" value="<%=card.getBank_branding()%>">

                card_number : <input type="text" name="card_number" value="<%=card.getCard_number()%>">

                expiration_date : <input type="date" name="expiration_date" value="<%=card.getExpiration_date()%>">

                payment_network_logo : <input type="text" name="payment_network_logo"
                                              value="<%=card.getPayment_network_logo()%>">

                bank_contact_information : <input type="text" name="bank_contact_information"
                                                  value="<%=card.getBank_contact_information()%>">
                balance: <input type="text" name="balance" value="<%=card.getBalance()%>">
                <input type="submit" name="submit" value="DELETE">
                <input type="submit" name="submit" value="UPDATE">
            </form>
        </td>
            <%
        }
    %>





</table>

<br>
<br>
</body>
</html>
