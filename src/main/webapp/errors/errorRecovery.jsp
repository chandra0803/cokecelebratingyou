<!doctype html public "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ include file="/include/taglib.jspf" %>
<%@ page import="java.io.PrintWriter,
                 java.io.StringWriter"%>

<html>
<head>
<title> System Error </title>
</head>

<body bgcolor="#ffffff">
  <%
    String stacktrace = "";
   	boolean isDevOrQA = false;
		//Grab the stacktrace out of the exception if we are in dev or qa.
		if ( System.getProperty( "environment.name" ).equals( "qa" )
        || System.getProperty( "environment.name" ).equals( "dev" ) )
    {
			isDevOrQA = true;
    	Throwable exception = ((Throwable)request.getAttribute("javax.servlet.error.exception"));
    	if (exception != null) {
        StringWriter msg = new StringWriter();
        PrintWriter str = new PrintWriter( msg );
        exception.printStackTrace(str);
        stacktrace = msg.toString();
    	} 
			else {
        stacktrace = "Stack Trace Not Available";
    	}
		}
		request.setAttribute( "isDevOrQA", java.lang.String.valueOf( isDevOrQA ) );
  %>
	<c:choose>
		<c:when test="${isDevOrQA == 'true'}">
			Error page through web.xml. Do a view source for more information. 

			<%--START ERROR CODE GENERATION<BR>
			***************************************************************************************<br><br>
			<b>Status Code:</b> <%= request.getAttribute("javax.servlet.error.status_code") %> <BR>
			<b>Request URL:</b> <%= request.getRequestURL().toString() %> <BR>
			<b>Servlet Name:</b> <%= request.getAttribute("javax.servlet.error.servlet_name") %> <BR>
			<b>Message:</b> <%= request.getAttribute("javax.servlet.error.message") %> <BR>
			<b>ServerEnv:</b> <%= System.getProperty("svr") %> <BR>
			<b>Server:</b> <%= request.getServerName() %> : <%= request.getServerPort() %> <BR>
			<b>Stack Trace:</b> <BR><pre>
			<%= stacktrace %>
			</pre>
			<br>
			***************************************************************************************<br><br>
			END ERROR CODE GENERATION--%>
		</c:when>
		<c:otherwise>
			<%-- div error & the script is added as a fix for bug 17556 --%>
			<div id="error"><cms:contentText code="system.errors" key="USER_FRIENDLY_SYSTEM_ERROR_MESSAGE" /></div>
		</c:otherwise>		
	</c:choose>
	<script>
			if(error.innerHTML == '???system.errors.USER_FRIENDLY_SYSTEM_ERROR_MESSAGE???')
			{
				error.innerHTML = 'Your request could not be processed due to an application error. Please contact Application Support.'
			}
		</script>
</body>
</html>