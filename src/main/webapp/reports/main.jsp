<%@ include file="/include/taglib.jspf" %>

<fmt:setLocale value="${LOCALE_FOR_REPORT}" scope="request"/>

{
	<%-- The below condition will check if there are any action messages then display those otherwise --%>
	"messages": [
			<logic:messagesPresent>
			<c:set var="count" value="0"/>
			  <html:messages id="actionMessage" >
			    <logic:present name="actionMessage">
			    <c:if test="${count!=0}">,</c:if>
			      {
						"type":"error",
						"code":"validationError",
						"name":"<cms:contentText key="MSG_NAME_MISSING_INFO" code="report.errors"/>",
						"text":"${actionMessage}"
				  }
				  <c:set var="count" value="1"/>
			    </logic:present>
			  </html:messages>
			</logic:messagesPresent>
			<%-- If the max rows in the result set is zero then display error message  --%>
			<logic:messagesNotPresent>
			<c:if test="${empty maxRows || maxRows==0}">
				{
					"type":"error",
					"name":"<cms:contentText key="MSG_NAME_NO_DATA_FOUND" code="report.errors"/>",
					"text":"<cms:contentText key="EMPTY_RESULT_SET" code="report.errors"/>"
				}
			</c:if>
			</logic:messagesNotPresent>
			],
 		"report": {
   		"id": "<c:out value="${report.id}"/>",
   		"reportDisplayName": "<cms:contentText key="${report.name}" code="${report.cmAssetCode}"/>",
        <%@include file="/reports/searchCriteria.jsp" %>
	 	<tiles:insert attribute="tabularData" /> 
        <%@include file="/reports/chartSet.jsp" %>
	}
}