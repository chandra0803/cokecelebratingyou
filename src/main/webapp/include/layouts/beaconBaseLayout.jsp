<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/include/taglib.jspf" %>

<tiles:insert attribute="init" />
<tiles:useAttribute name="origstyles" id="origstyles" classname="java.lang.String"/>
<tiles:useAttribute name="pagelayout" id="pagelayout" classname="java.lang.String"/>
<tiles:useAttribute name="containerclass" id="containerclass" classname="java.lang.String"/>
<tiles:useAttribute name="header" id="header" classname="java.lang.String"/>
<tiles:useAttribute name="maintop" id="maintop" classname="java.lang.String"/>
<tiles:useAttribute name="content" id="content" classname="java.lang.String"/>
<tiles:useAttribute name="mainbottom" id="mainbottom" classname="java.lang.String"/>
<tiles:useAttribute name="footer" id="footer" classname="java.lang.String"/>
<tiles:useAttribute name="topsearch" id="topsearch" classname="java.lang.String"/>
<tiles:useAttribute name="trackingTitle" id="trackingTitle" classname="java.lang.String"/>
<tiles:useAttribute name="yuimodalpopup" id="yuimodalpopup" classname="java.lang.String"/>
<tiles:useAttribute name="insertPurlStyle" id="insertPurlStyle" classname="java.lang.String"/>
  
<tiles:insert definition="g3redux.default" flush="true" ignore="true">		
	<tiles:put name="pagelayout"><%=pagelayout%></tiles:put>
	<tiles:put name="containerclass"><%=containerclass%></tiles:put>
	<tiles:put name="maintop"><%=maintop%></tiles:put>	
	<tiles:put name="content"><%=content%></tiles:put>	
	<tiles:put name="mainbottom"><%=mainbottom%></tiles:put>		
	<tiles:put name="origstyles"><%=origstyles%></tiles:put>
	<tiles:put name="topsearch"><%=topsearch%></tiles:put>         
	<tiles:put name="trackingTitle"><%=trackingTitle%></tiles:put>
	<tiles:put name="yuimodalpopup"><%=yuimodalpopup%></tiles:put>
	<tiles:put name="insertPurlStyle"><%=insertPurlStyle%></tiles:put>
	<tiles:put name="navselected"></tiles:put>
</tiles:insert>

