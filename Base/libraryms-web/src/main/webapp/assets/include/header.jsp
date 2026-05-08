<%@ page import="com.johnnyconsole.libraryms.persistence.User" %>
<html>
  <head>
    <title>Library Web<% if(pageTitle != null) { %>: <%= pageTitle %> <% } %> </title>
    <link rel="stylesheet" href="/library/assets/style/main.css"/>
    <link rel="icon" href="/library/assets/img/icon.jpg"/>
  </head>
  <body>
    <header>
      <h1>Library Web<% if(pageTitle != null) { %>: <%= pageTitle %> <% } %></h1>
    </header>
      <% User user = (User)session.getAttribute("user"); %>
    <%@ include file="nav.jsp" %>
  <main>