<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf" %>
<%@ page import="java.io.PrintWriter,
                 java.io.StringWriter"%>

<div id="errorPageView" name ="main" class="page page-content">
<p id="pageHomeLink">
                <a href="<%=RequestUtils.getBaseURI(request)%>/homePage.do"><i class="icon-home"></i><span></span></a>
</p>
<font color=red><H2><B><cms:contentText code="system.errors" key="ERROR_OCCURED" /></B></H2></font>  
  <%
		String stacktrace = "";
    boolean isDevOrQA = false;
		//Grab the stacktrace out of the exception if we are in dev or qa.
		if ( System.getProperty( "environment.name" ).equals( "qa" )
        || System.getProperty( "environment.name" ).equals( "dev" ) )
    {
			isDevOrQA = true;
    
			//I'm sure someone will want to refactor this to use jstl.
    	org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog( "error.jsp" );

    	Throwable exception = ((Throwable)request.getAttribute("javax.servlet.error.exception"));
  
  		if ( exception == null )
    	{
      	exception = ((Throwable)request.getAttribute("org.apache.struts.action.EXCEPTION"));
    	}
    	if (exception != null) 
    	{
      	StringWriter msg = new StringWriter();
        PrintWriter str = new PrintWriter( msg );
        exception.printStackTrace(str);
        stacktrace = msg.toString();
        log.error(exception.getMessage(), exception);
    	} 
			else 
    	{
        stacktrace = "Stack Trace Not Available";
        log.error("Stack Trace Not Available in error.jsp");
    	}
		}
		request.setAttribute( "isDevOrQA", java.lang.String.valueOf( isDevOrQA ) );
  %>
   
	<c:choose>
		<c:when test="${isDevOrQA == 'true'}">
			<c:choose>
				<c:when test="${! empty requestScope['org.apache.struts.action.EXCEPTION']}">
					<c:set var="exceptionKey" value="org.apache.struts.action.EXCEPTION"/>
				</c:when>
				<c:otherwise>
					<c:set var="exceptionKey" value="javax.servlet.error.exception"/>
				</c:otherwise>
			</c:choose>

			<BR>(<cms:contentText code="system.errors" key="VIEW_SOURCE" />)<BR><BR>  

			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
    				<td><c:out value="${requestScope[exceptionKey]}" escapeXml="false"/></td>
  			</tr>
			</table>
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

</div>