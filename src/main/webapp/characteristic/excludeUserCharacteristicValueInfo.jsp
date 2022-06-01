<%-- UI REFACTORED --%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>

<c:set var="characteristicType" value="${requestScope.characteristicType}" />
<c:set var="displayStyle" value="${requestScope.displayStyle}" />
<c:set var="displayAudienceExclusion" value="${requestScope.displayAudienceExclusion}" />

<c:forEach items="${listBuilderForm.excludeUserCharacteristicValueList}"
	var="valueInfo" varStatus="status">
	<%--
            In order for the same page to use characteristicEntry.jspf for two different characteristic
            types, we need the iterated value to be named both ValueInfo and excludeUserCharacteristicValueInfo, so set
            the excludeUserCharacteristicValueInfo bean to the valueInfo bean. Is there a tag way to do this rather than
            using a scriptlet?
            --%>
	<% pageContext.setAttribute( "excludeUserCharacteristicValueInfo", pageContext.getAttribute( "valueInfo" ) ); %>
	<%@ include file="/characteristic/characteristicEntry.jspf"%>
</c:forEach>
