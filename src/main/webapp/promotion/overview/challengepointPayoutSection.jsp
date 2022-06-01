<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.ui.promotion.PromotionOverviewForm"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>
	<%	Map paramMap = new HashMap();
			PromotionOverviewForm temp = (PromotionOverviewForm)request.getSession().getAttribute( "promotionOverviewForm" );
			paramMap.put( "promotionId", temp.getPromotionId() );
	    String url = "/" + (String)request.getAttribute( "promotionStrutsModulePath" ) + "/promotionPayout.do?method=display";
			pageContext.setAttribute("viewUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), url, paramMap ) );
	%>
    <c:choose>
      <c:when test="${promotionOverviewForm.promotionStatus == 'expired'}">
        <a class="content-link" href="<c:out value="${viewUrl}"/>">
          <cms:contentText code="system.link" key="VIEW" />
        </a>
      </c:when>
      <c:otherwise>
        <beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
          <a class="content-link" href="<c:out value="${viewUrl}"/>">
            <cms:contentText code="system.link" key="EDIT" />
          </a>
        </beacon:authorize>
      </c:otherwise>
    </c:choose>
  </td>
</tr>

<%-- goal structure --%>
<tr>
  <td>&nbsp;</td>
  <td class="content-field-label"><cms:contentText code="promotion.payout" key="PRIMARY_AWARD_TYPE"/></td>
  <td class="content-field-review"><c:out value="${promotionOverviewForm.awardTypeText}" /></td>
</tr>

<%-- payout structure --%>
<tr>
  <td>&nbsp;</td>
  <td class="content-field-label"><cms:contentText code="promotion.payout" key="PRIMARY_AWARD_THRESHOLD"/></td>
  <td class="content-field-review">

 <c:choose>
      <c:when test="${empty promotionOverviewForm.awardThresholdType}">
<cms:contentText code="promotion.payout.challengepoint" key="NOT_APPLICABLE"/>
 </c:when>
 <c:otherwise>
  <c:out value="${promotionOverviewForm.awardThresholdValue}" />
 &nbsp;
 <c:out value="${promotionOverviewForm.awardThresholdType}" />
 </c:otherwise>
 </c:choose>
 </td>
</tr>
<tr>
  <td>&nbsp;</td>
  <td class="content-field-label"><cms:contentText code="promotion.payout" key="PRIMARY_AWARD_INCREMENT"/></td>
  <td class="content-field-review">
  <c:out value="${promotionOverviewForm.awardIncrementValue}" />
  &nbsp;
 <c:out value="${promotionOverviewForm.awardIncrementType}" />
 </td>
</tr>
<%-- achievement precision --%>
<tr>
  <td>&nbsp;</td>
  <td class="content-field-label"><cms:contentText code="promotion.payout" key="PRIMARY_AWARD_PER_INCREMENT"/></td>
  <td class="content-field-review"><c:out value="${promotionOverviewForm.awardPerIncrement}" /></td>
</tr>


<tr>
  <td>&nbsp;</td>
  <td class="content-field-label"><cms:contentText code="promotion.payout" key="CHALLENGEPOINT_AWARD_TYPE"/></td>
  <td class="content-field-review">
  
  <c:out value="${promotionOverviewForm.challengePointAwardType}" /></td>
</tr>

<tr>
  <td>&nbsp;</td>
  <td class="content-field-label"><cms:contentText code="promotion.payout" key="CHALLENGEPOINT_ACHIEVEMENT_RULE"/></td>
  <td class="content-field-review"><c:out value="${promotionOverviewForm.achievementRule}" /></td>
</tr>
<%-- rounding method --%>
<tr>
  <td>&nbsp;</td>
  <td class="content-field-label"><cms:contentText code="promotion.payout" key="ACHIEVEMENT_PRECISION"/></td>
  <td class="content-field-review"><c:out value="${promotionOverviewForm.achievementPrecision}" /></td>
</tr>

<tr>
  <td>&nbsp;</td>
  <td class="content-field-label"><cms:contentText code="promotion.payout" key="ROUNDING_METHOD"/></td>
  <td class="content-field-review"><c:out value="${promotionOverviewForm.roundingMethod}" /></td>
</tr>
<%-- base Unit--%>
<tr>
  <td>&nbsp;</td>
  <td class="content-field-label"><cms:contentText key="UNIT_LABEL" code="promotion.payout"/></td>
  <td class="content-field-review"><c:out value="${promotionOverviewForm.baseUnit}" /></td>
</tr>

<%-- base unit position --%>
<tr>
  <td>&nbsp;</td>
  <td class="content-field-label"><cms:contentText key="UNIT_LABEL_POSITION" code="promotion.payout"/></td>
  <td class="content-field-review"><c:out value="${promotionOverviewForm.baseUnitPosition}" /></td>
</tr>

<tr class="form-blank-row"><td></td></tr>