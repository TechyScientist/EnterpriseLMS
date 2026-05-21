<% String projectRoot = "/library"; %>
<nav>
    <a href="<%= projectRoot %>" <% if(pageName.equals("home")) { %> id="current" <% } %>>Home</a>
    <% if(user == null) { %>
        <a href="<%= projectRoot %>/signin.jsp" <% if(pageName.equals("signin")) { %> id="current" <% } %>>Sign In</a>
    <% } else { %>
        <a href="<%= projectRoot %>/dashboard.jsp" <% if(pageName.equals("dashboard")) { %> id="current" <% } %>>Dashboard</a>
        <a href="<%= projectRoot %>/catalog-search.jsp" <% if(pageName.equals("catalog-search")) { %> id="current" <% } %>>Catalog Search</a>
        <a href="<%= projectRoot %>/self-service.jsp" <% if(pageName.equals("self-service")) { %> id="current" <% } %>>Self Service</a>
        <% if(user.libraryStaff || user.libraryAdmin) { %>
            <a href="<%= projectRoot %>/staff.jsp" <% if(pageName.equals("staff")) { %> id="current" <% } %>>Staff Area</a>
        <% } %>
        <a href="<%= projectRoot %>/profile.jsp" <% if(pageName.equals("profile")) { %> id="current" <% } %>>My Profile</a>
        <a href="<%= projectRoot %>/SignOutServlet">Sign Out</a>
    <% } %>
</nav>
