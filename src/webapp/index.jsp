<%--
	What is the key difference between using a <jsp:forward> and 
	HttpServletResponse.sendRedirect()?

	When you forward a request, the forwarding is done within the server, and 
	limited in scope to where you can forward. Redirections are done on the client 
	and thus don't have these limitations.

	With the <jsp:forward> tag, you can redirect the request to any JSP, 
	servlet, or static HTML page within the same context as the invoking page. 
	This effectively halts processing of the current page at the point where 
	the redirection occurs, although all processing up to that point still 
	takes place.

	301 redirect is the most efficient and Search Engine Friendly method for 
	webpage redirection. It should preserve your search engine rankings 
	for that particular page. If you have to change file names or move pages 
	around, it's the safest option. The code "301" is interpreted as 
	"moved permanently". 
	
	Forward request:
		<jsp:forward page="somePage.jsp"/>
	or:
	<%
		request.getRequestDispatcher("somePage.jsp").forward(request, response);
	%>

	Redirect request:
	<%
		response.sendRedirect("http://site.com/some/url");
	%>

	301 redirect:
	<%
		response.setStatus(301);
		response.setHeader("Location", "http://site.com/some/url");
		response.setHeader("Connection", "close");
	%>
--%>

<%-- TODO: Fix or not (n, if hard to fix. not big deal) --%>
<%-- Does not forward after upgrade from Stuts [2.1.6] to [2.1.8] --%>
<%--<jsp:forward page="/home"/>--%>

<%
	response.setStatus(301);
	response.setHeader("Location", request.getContextPath() + "/home");
	response.setHeader("Connection", "close");
%>
