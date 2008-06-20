    <div id="sidebar">
<%
//============================================================================
// Menu Box
//============================================================================
%>
      <fieldset id="pages">
        <legend>Menu</legend>
        <ul>
          <li><a href="/" title="Alitheia overview">Home</a></li>
          <li><a href="/projects.jsp" title="Project details">Projects</a></li>
          <li><a href="/metrics.jsp" title="Metric details">Metrics</a></li>
          <li><a href="/files.jsp" title="Project Files">Files</a></li>
<%
if (user.getLoggedIn()) {
//        <li><a href="/logout.jsp" title="Click here to log out">Logout</a></li>
} else {
//        <li><a href="/login.jsp" title="Click here to log in or register">Login</a></li>
}
%>
        </ul>
      </fieldset>

<%
//============================================================================
// Message Box
//============================================================================
if ( (msg!=null) && msg.length() > 0 ) {
%>
      <fieldset id="messages">
        <legend>Messages</legend>
<%= msg %>
      </fieldset>
<%
}
//============================================================================
// Status Box
//============================================================================
%>
      <fieldset id="status">
        <legend>Status</legend>
<%
cruncher.hit();
out.println(cruncher.getStatus());
%>
      </fieldset>
<%
//============================================================================
// Error Box
//============================================================================
if (terrier.getError().length() > 0) {
%>
      <fieldset id="error">
        <legend>Errors</legend>
<%
    out.println(error(terrier.getError()));
    terrier.flushError();
%>
      </fieldset>
<%
}
%>

    </div>
