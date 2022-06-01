<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/include/taglib.jspf" %>
<fmt:setLocale value="${LOCALE_SET_IN_REQUEST}" scope="request"/>

<html>
  <head>
    <tiles:insert attribute="init" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title><c:out value='${webappTitle}'/></title>
	<link rel="stylesheet" href="<%= RequestUtils.getBaseURI(request) %>/assets/g4skin/css/skin/main.css?t=@TIMESTAMP@" type="text/css" media="screen" title="" />
	<link rel="stylesheet" href="<%= RequestUtils.getBaseURI(request) %>/assets/g4skin/css/skin/default2.css?t=@TIMESTAMP@" type="text/css">
    <link rel="stylesheet" href="<%= RequestUtils.getBaseURI(request) %>/assets/g4skin/css/skin/navigation.css?t=@TIMESTAMP@" type="text/css">
	<link rel="stylesheet" href="<%= RequestUtils.getBaseURI(request) %>/assets/g4skin/css/skin/popup.css?t=@TIMESTAMP@" type="text/css">
	<link rel="stylesheet" href="<%= RequestUtils.getBaseURI(request) %>/assets/g4skin/css/skin/recognition.css?t=@TIMESTAMP@" type="text/css">
    <script src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/static.js?t=@TIMESTAMP@" type="text/javascript"></script>
    <script src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/commonFunctions.js?t=@TIMESTAMP@" type="text/javascript"></script>
  </head>
  <body leftmargin="0" topmargin="0" marginheight="0" marginwidth="0">
    <!-- server-env : <%=System.getProperty("com.sun.aas.instanceName")+ "-" + System.getProperty( "environment.name")%> -->
    <a name="banner_top"></a>
    <table border="0" cellpadding="0" cellspacing="0" width="100%">
      <tr valign='top'>
        <td width="100%">
          <table border="0" cellpadding="0" cellspacing="0" width="100%">
            <tr>
              <td><tiles:insert attribute='content'/></td>
            </tr>
          </table>
        </td>
      </tr>
    </table>

	<tiles:useAttribute name="trackingTitle" id="trackingTitle" classname="java.lang.String"/>
	<c:if test="${not empty trackingTitle}">
	<tiles:insert attribute='webtracking'>
	   <tiles:put name="trackingTitle"><%=trackingTitle%></tiles:put>
    </tiles:insert>
    </c:if>
  </body>
</html>
