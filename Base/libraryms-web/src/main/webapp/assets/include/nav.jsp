<%@ page import="com.johnnyconsole.libraryms.persistence.User" %>
<% String projectRoot = "/libraryms"; %>
<nav>
    <a href="<%= projectRoot %>" <% if(pageName.equals("home")) { %> id="current" <% } %>>Home</a>
    <% if(session.getAttribute("user") == null) { %>
        <a href="<%= projectRoot %>/signin.jsp" <% if(pageName.equals("signin")) { %> id="current" <% } %>>Sign In</a>
    <% } else { %>
        <a href="<%= projectRoot %>/catalog-search.jsp"<% if(pageName.equals("catalog-search")) { %> id="current" <% } %>>Catalog Search</a>
        <a href="<%= projectRoot %>/self-checkout.jsp"<% if(pageName.equals("self-checkout")) { %> id="current" <% } %>>Self-Check Out</a>
        <a href="<%= projectRoot %>/self-checkin.jsp"<% if(pageName.equals("self-checkin")) { %> id="current" <% } %>>Self-Check In</a>
        <% if(((User)session.getAttribute("user")).libraryStaff) { %>

        <% } if(((User)session.getAttribute("user")).libraryAdmin) { %>

        <% } %>
        <a href="<%= projectRoot %>/SignOutServlet">Sign Out</a>
    <% } %>
</nav>