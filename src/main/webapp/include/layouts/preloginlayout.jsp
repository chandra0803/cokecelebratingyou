<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/include/taglib.jspf" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<fmt:setLocale value="${LOCALE_SET_IN_REQUEST}" scope="request"/>

<tiles:importAttribute/>
<html>
<head>
  <tiles:insert attribute="init" />
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
  <title><c:out value='${webappTitle}'/></title>

<c:choose>
<c:when test="${ not empty isPurlUrl and isPurlUrl == 'true'}">
<c:set var="siteUrlPrefixPageScope" value="${siteUrlPrefix}" />
</c:when>
<c:otherwise>
<c:set var="siteUrlPrefixPageScope" value="<%= RequestUtils.getBaseURI(request) %>" />
</c:otherwise>
</c:choose>

  <link rel="stylesheet" href="${siteUrlPrefixPageScope}/assets/g4skin/css/skin//main.css?t=@TIMESTAMP@" type="text/css" media="screen" title="" />
  <link rel="stylesheet" href="${siteUrlPrefixPageScope}/assets/g4skin/css/skin//default2.css?t=@TIMESTAMP@" type="text/css">
  <script src="${siteUrlPrefixPageScope}/assets/g4skin/scripts/commonFunctions.js?t=@TIMESTAMP@" type="text/javascript"></script>
</head>
<body leftmargin="0" topmargin="0" marginheight="0" marginwidth="0" class="bodystyle contentold">
	<div align="center">
  <!-- server-env : <%=System.getProperty("com.sun.aas.instanceName")+ "-" + System.getProperty( "environment.name")%> -->
  <a name="banner_top"></a>
	<table border="0" cellpadding="0" cellspacing="0" width="900" class="main-container-table">
		<tr>
			<td>
				<table border="0" cellpadding="0" cellspacing="0" width="100%" class="login-logo-area">
					<tr>
						<td>
					  		<img alt="" border="0" src="${siteUrlPrefixPageScope}/assets/g4skin/images/skin/logo.gif"/>
						</td>
					</tr>
				</table>
				<table width="100%" class="navrowbottom">
					<tr>
						<td></td>
					</tr>
				</table>
				<br>
				<table border="0" cellpadding="0" cellspacing="0" width="100%" >
					<tr>
						<td valign="top">
							<%--  start main content --%>
    					    <tiles:insert attribute="content"/>
							<%--  end main content --%>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>

	<%--footer--%>
	<tiles:insert attribute='footer' ignore="true"/>
	<%--end footer--%>
	</div>

	<tiles:useAttribute name="trackingTitle" id="trackingTitle" classname="java.lang.String"/>
	<tiles:insert attribute='webtracking'>
	   <tiles:put name="trackingTitle"><%=trackingTitle%></tiles:put>
    </tiles:insert>
</body>
</html>
