<!DOCTYPE html>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/include/taglib.jspf" %>
<tiles:importAttribute/>
<html lang="en">
<head>
	<tiles:insert attribute="init" />
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />	
	<title><c:out value='${webappTitle}'/></title>
	
	<%-- css --%>
	<link rel="stylesheet" href="<%= RequestUtils.getBaseURI(request) %>/assets/g4skin/css/skin/main.css" type="text/css" media="screen" title="" />
	<link rel="stylesheet" href="<%= RequestUtils.getBaseURI(request) %>/assets/g4skin/css/skin/popup.css" type="text/css">
	<link rel="stylesheet" href="<%= RequestUtils.getBaseURI(request) %>/assets/g4skin/css/skin/css-cp-promotion.css" type="text/css">
 	<link rel="stylesheet" href="<%= RequestUtils.getBaseURI(request) %>/assets/g4skin/css/skin/css-cp-print.css" media="print" type="text/css">
	<tiles:useAttribute name="challengepoint_select" id="challengepoint_select" classname="java.lang.String" ignore="true"/>
    <c:if test="${challengepoint_select ne null and challengepoint_select eq 'true'}">
	<link rel="stylesheet" href="<%= RequestUtils.getBaseURI(request) %>/assets/g4skin/css/skin/css-cp-select.css" type="text/css">
	<link rel="stylesheet" href="<%= RequestUtils.getBaseURI(request) %>/assets/g4skin/css/skin/css-cp.css" type="text/css">
    </c:if>
    <link rel="stylesheet" href="<%= RequestUtils.getBaseURI(request) %>/assets/g4skin/css/calendarSkins/aqua/theme.css" type="text/css">
    
  <tiles:useAttribute name="insertPurlStyle" id="insertPurlStyle" classname="java.lang.String"/>
  <c:if test="${insertPurlStyle == 'true'}">
  	<link rel="stylesheet" href="<%= RequestUtils.getBaseURI(request) %>/assets/g4skin/css/skin/purl_g4.css" type="text/css">
  </c:if>
	
	<%-- js --%>
    <c:if test="${challengepoint_select ne null and challengepoint_select eq 'true'}">
	   	<link rel="stylesheet" href="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/css/skin/css-cp-home.css" type="text/css">    
		<link rel="stylesheet" href="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/css/skin/css-tabs.css" type="text/css">
		<link rel="stylesheet" href="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/css/skin/css-prototip.css" type="text/css">
		<link rel="stylesheet" href="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/css/skin/default.css" type="text/css">
   	    <script src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/prototip.js" type="text/javascript"></script>
		<script src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/show-hide.js" type="text/javascript"></script>
    </c:if>
	<script type="text/javascript" src="<%= RequestUtils.getBaseURI(request) %>/assets/g4skin/scripts/jquery/jquery-1.4.4.min.js"></script>
	<script type="text/javascript" src="<%= RequestUtils.getBaseURI(request) %>/assets/g4skin/scripts/jquery/ui.core.js"></script>
	<script type="text/javascript" src="<%= RequestUtils.getBaseURI(request) %>/assets/g4skin/scripts/jquery/ui.datepicker.js"></script>
	<script type="text/javascript" src="<%= RequestUtils.getBaseURI(request) %>/assets/g4skin/scripts/main.js" charset="utf-8"></script>
	<script type="text/javascript" src="<%= RequestUtils.getBaseURI(request) %>/assets/g4skin/scripts/jquery.touchSwipe-1.2.4.js"></script>
	<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/commonFunctions.js" ></script>    
	<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/jquery/jquery.numberformatter-1.1.2.js" ></script>    

	<%-- TODO - remove these at some point --%>
	<script src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/modal/common.js" type="text/javascript"></script>
	<script src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/modal/popaction.js" type="text/javascript"></script>
	<%-- end - remove these at some point --%>
 
	<tiles:insert attribute='origstyles' ignore="true"/>
</head>

<fmt:setLocale value="${LOCALE_SET_IN_REQUEST}" scope="request"/>

<body class="<c:out value='${pagelayout}'/><c:out value=' '/><c:out value='${quicksearchbody}'/>">
<a name="banner_top"></a>
	<tiles:useAttribute name="yuimodalpopup" id="yuimodalpopup" classname="java.lang.String"/>
    	<c:if test="${yuimodalpopup eq 'true'}">
        	<%@ include file="/include/modalPopup.jspf"%>
    	</c:if>

	<!-- server-env : <%=System.getProperty("com.sun.aas.instanceName")+ "-" + System.getProperty( "environment.name")%> -->
	
	<tiles:insert attribute="loading" ignore="true"/>
	
	<tiles:insert attribute="topsearch"/>

	<div id="container" class="<c:out value="${containerclass}"/>">
	    
	    <tiles:useAttribute name="navselected" id="navselected" classname="java.lang.String"/>
		<tiles:insert attribute="header" ignore="true">
        <c:if test="${navselected ne null and fn:length(navselected) > 0}">
		   <tiles:put name="navselected"><%=navselected%></tiles:put>
        </c:if>
        </tiles:insert>
		
		<tiles:insert attribute="maintop"/>

		<tiles:insert attribute="content"/>

		<tiles:insert attribute="mainbottom"/>

		<tiles:insert attribute="footer" ignore="true"/>
		
	
	</div><%-- /#container --%>

    <tiles:useAttribute name="trackingTitle" id="trackingTitle" classname="java.lang.String"/>
	<tiles:insert attribute='webtracking'>       
	   <tiles:put name="trackingTitle"><%=trackingTitle%></tiles:put>
    </tiles:insert>
	
</body>

</html>
