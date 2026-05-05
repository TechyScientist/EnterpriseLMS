<% String projectRoot = "/libraryms"; %>
<nav>
    <a href="<%= projectRoot %>" <% if(pageName.equals("home")) { %> id="current" <% } %>>Home</a>
    <a href="<%= projectRoot %>/signin.jsp" <% if(pageName.equals("signin")) { %> id="current" <% } %>>Sign In</a>
</nav>