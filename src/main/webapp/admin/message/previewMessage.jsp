<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.service.goalquest.GoalQuestSurveyUtils"%>


<table width="100%">

  <%-- title and instructional copy --%>
  <tr>
    <td class="headline">
       <cms:contentText key="PREVIEW_MESSAGE" code="admin.message.details"/> <c:out value="${messageObj.name}" /> 
    </td>
  </tr>
</table>

<table class="crud-table" width="100%" >
  <thead>
    <tr>
      <th align="left" class="crud-table-header-row"><cms:contentText key="KEY" code="admin.message.details"/></th> 
      <th align="left" class="crud-table-header-row"><cms:contentText key="TEXT" code="admin.message.details"/></th>
    </tr>
  </thead>
<tbody>

 <%-- Added this seperate Goal Quest Survey messages.  GQ message starts with GQ_ vs other messages cm.asset code which starts with message_data.xxxxxx --%>
 <%-- Added new logic to add message_data. to all GQ survey message types by identifying the prefix "GQ_" --%>
  <c:set var="assetCodePrefix" value="<%= GoalQuestSurveyUtils.PREVIEW_MESSAGE_GQ_PREFIX  %>"/>
	
  <c:if test="${fn:startsWith(requestScope.assetCode,assetCodePrefix)}">
		<c:set var="assetCodeModified" value="message_data.${requestScope.assetCode}"/>
  </c:if>
  
  <c:if test="${!fn:startsWith(requestScope.assetCode,assetCodePrefix)}">
		<c:set var="assetCodeModified" value="${requestScope.assetCode}"/>
		<!-- <c:out value="${assetCodeModified}"></c:out> -->
  </c:if>
    
  <tr class="crud-table-row1">
    <td width="20%" align="left" class="crud-content"><strong><cms:contentText key="SUBJECT" code="admin.message.details"/></strong></td>
    <td align="left" class="crud-content"><cms:contentText code="${assetCodeModified}" key="SUBJECT"/></td>
  </tr>

  <tr class="crud-table-row2">
      <td align="left" class="crud-content"><strong><cms:contentText key="HTML_MSG" code="admin.message.details"/></strong></td>
      <td align="left" class="crud-content"><cms:contentText code="${assetCodeModified}" key="HTML_MSG"/></td>
  </tr>

  <tr class="crud-table-row1">
      <td align="left" class="crud-content"><strong><cms:contentText key="PLAIN_TEXT_MSG" code="admin.message.details"/></strong></td>
      <td align="left" class="crud-content"><cms:contentText code="${assetCodeModified}" key="PLAIN_TEXT_MSG"/></td>
  </tr>
  <tr class="crud-table-row2">
      <td align="left" class="crud-content"><strong><cms:contentText key="TEXT_MSG" code="admin.message.details"/></strong></td>
      <td align="left" class="crud-content"><cms:contentText code="${assetCodeModified}" key="TEXT_MSG"/></td>
  </tr>
</tbody>
</table>

<%-- buttons --%>
<table width="750">
  <tr align="center">
    <td align="center">
		<html:button property="closeBtn" styleClass="content-buttonstyle" onclick="javascript:window.close()">
		  <cms:contentText code="system.button" key="CLOSE_WINDOW" />
		</html:button>
    </td>
  </tr>
</table>
