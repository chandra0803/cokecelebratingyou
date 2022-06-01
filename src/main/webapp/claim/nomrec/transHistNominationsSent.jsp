<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.ui.claim.RecognitionHistoryValueObject"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ include file="/include/taglib.jspf"%>

<%  Map paramMap = new HashMap();
		RecognitionHistoryValueObject temp;
%>
<display:table defaultsort="1" defaultorder="ascending" name="valueObjects" id="valueObject" pagesize="20" sort="list" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>" export="true">
<display:setProperty name="basic.msg.empty_list_row">
	<tr class="crud-content" align="left"><td colspan="{0}">
     <cms:contentText key="NOTHING_FOUND" code="system.errors"/>
       </td></tr>
	</display:setProperty>
	<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
  <%-- the name of the promotion --%>
  <display:column titleKey="nomination.history.PROMOTION" property="promotion.name" headerClass="crud-table-header-row" class="crud-content top-align left-align nowrap" sortable="true"/>

  <%-- the date the claim was submitted --%>
  <display:column titleKey="nomination.history.DATE_SUBMITTED" headerClass="crud-table-header-row" class="crud-content-link top-align left-align" sortable="true" sortProperty="submissionDate" media="html">
    <c:if test="${valueObject.claim != null }">
			<%  temp = (RecognitionHistoryValueObject)pageContext.getAttribute("valueObject");
					paramMap.put( "promotionTypeCode", request.getAttribute("promotionTypeCode") );
					paramMap.put( "mode", request.getAttribute("mode") );
					paramMap.put( "claimId", temp.getClaim().getId() );
					paramMap.put( "claimRecipientId", temp.getClaimRecipient().getId() );
					paramMap.put( "userId", request.getAttribute("participantId"));
					pageContext.setAttribute("detailUrl", ClientStateUtils.generateEncodedLink( "", "recognitionDetail.do", paramMap, true ) );
			%>
      <a href="javascript:setActionAndSubmit('<c:out value='${detailUrl}'/>');" class="content-link">
    </c:if>
    <fmt:formatDate value="${valueObject.submissionDate}" pattern="${JstlDatePattern}"/>
    <c:if test="${valueObject.claim != null }">
      </a>
    </c:if>
    <c:if test="${valueObject.proxyUser != null}">
      <br>
      <cms:contentText key="BY" code="nomination.history"/>&nbsp;<c:out value="${valueObject.proxyUser.nameLFMWithComma}"/>
    </c:if>
  </display:column>

  <display:column titleKey="nomination.history.DATE_SUBMITTED" headerClass="crud-table-header-row" class="crud-content-link top-align left-align" sortable="true" sortProperty="submissionDate" media="csv excel pdf">
    <fmt:formatDate value="${valueObject.submissionDate}" pattern="${JstlDatePattern}"/>
    <c:if test="${valueObject.submitter.id != participantId}">
      <cms:contentText key="BY" code="nomination.history"/>
      <c:choose>
        <c:when test="${recognitionHistoryForm.proxy}">
          <c:out value="${valueObject.proxyUser.nameLFMWithComma}"/>
        </c:when>
        <c:otherwise>
          <c:out value="${valueObject.submitter.nameLFMWithComma}"/>
        </c:otherwise>
      </c:choose>
    </c:if>
  </display:column>

  <%-- the name of the nominated participant --%>
  <display:column titleKey="nomination.history.RECEIVER" headerClass="crud-table-header-row" class="crud-content top-align left-align nowrap" sortable="true">
    <c:if test="${valueObject.awardGroupType.code == 'individual'}">
      <c:out value="${valueObject.claimRecipient.recipient.nameLFMWithComma}"/>&nbsp;(<c:out value="${valueObject.claimRecipient.node.name}"/>)
    </c:if>
    <c:if test="${(valueObject.awardGroupType.code == 'team') || (valueObject.awardGroupType.code == 'both')}">
      <c:forEach items="${valueObject.teamMembers}" var="teamMember" varStatus="status">
        <c:out value="${teamMember.recipient.nameLFMWithComma}"/>&nbsp;(<c:out value="${teamMember.claim.node.name}"/>)
        <c:if test="${!status.last}"><br/></c:if>
      </c:forEach>
    </c:if>
  </display:column>

</display:table>
