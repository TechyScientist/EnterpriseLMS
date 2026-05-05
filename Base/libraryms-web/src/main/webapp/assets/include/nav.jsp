<% String projectRoot = "/libraryms"; %>
<nav>
    <a href="<%= projectRoot %>" <% if(pageName.equals("home")) { %> id="current" <% } %>>Home</a>
    <% if(session.getAttribute("user") == null) { %>
        <a href="<%= projectRoot %>/signin.jsp" <% if(pageName.equals("signin")) { %> id="current" <% } %>>Sign In</a>
    <% } else { %>
        <a href="<%= projectRoot %>/SignOutServlet">Sign Out</a>
    <% } %>
</nav>