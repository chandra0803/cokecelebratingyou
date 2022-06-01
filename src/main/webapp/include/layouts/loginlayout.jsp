<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/include/taglib.jspf" %>
<fmt:setLocale value="${LOCALE_SET_IN_REQUEST}" scope="request"/>

<tiles:importAttribute/>
<html>
<head>
  <tiles:insert attribute="init" />
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
  <title>${webappTitle}</title>
  <link rel="stylesheet" href="<%= RequestUtils.getBaseURI(request) %>/assets/g4skin/css/skin/main.css?t=@TIMESTAMP@" type="text/css" media="screen" title="" />
  <link rel="stylesheet" href="<%= RequestUtils.getBaseURI(request) %>/assets/g4skin/css/skin/default2.css?t=@TIMESTAMP@" type="text/css">
  <script src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/commonFunctions.js?t=@TIMESTAMP@" type="text/javascript"></script>
</head>
<body leftmargin="0" topmargin="0" marginheight="0" marginwidth="0" class="login-body" onLoad="setFocus()">
	<center>

  <input type="hidden" name="server-env" value='<%=System.getProperty( "com.sun.aas.instanceName") + "-" + System.getProperty( "environment.name")%>'/>

	<table border="0" cellpadding="0" cellspacing="0" width="900" class="login-container-table">
		<tr>
			<td>
				<table border="0" cellpadding="0" cellspacing="0" width="100%" class="login-logo-area">
					<tr>
					  <td align="left">
					  <img alt="" border="0" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/skin/logo.gif" />
					  </td>
					</tr>
				</table>
				<table border="0" cellpadding="0" cellspacing="0" width="100%" class="login-photo">
					<tr>
						<td valign="top" class="login-form-table">
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
	</center>

    <tiles:useAttribute name="trackingTitle" id="trackingTitle" classname="java.lang.String"/>
	<tiles:insert attribute='webtracking'>
	   <tiles:put name="trackingTitle"><%=trackingTitle%></tiles:put>
    </tiles:insert>
</body>
</html>
