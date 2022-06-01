<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.ui.promotion.PromotionOverviewForm"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>
	<%	Map paramMap = new HashMap();
			PromotionOverviewForm temp = (PromotionOverviewForm)request.getSession().getAttribute( "promotionOverviewForm" );
			paramMap.put( "promotionId", temp.getPromotionId() );
			paramMap.put( "promotionTypeCode", temp.getPromotionTypeCode() );
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

<%-- payout type --%>
<tr>
  <td>&nbsp;</td>
  <td class="content-field-label"><cms:contentText code="promotion.payout" key="TYPE"/></td>
  <td class="content-field-review"><c:out value="${promotionOverviewForm.awardTypeText}" /></td>
</tr>

<%-- Base Unit --%>
<tr>
  <td>&nbsp;</td>
  <td class="content-field-label"><cms:contentText code="promotion.payout.goalquest" key="BASE_UNIT"/></td>
  <td class="content-field-review"><c:out value="${promotionOverviewForm.baseUnit}" /></td>
</tr>

<%-- Base Unit Position --%>
<tr>
  <td>&nbsp;</td>
  <td class="content-field-label"><cms:contentText code="promotion.payout.goalquest" key="BASE_UNIT_POSITION"/></td>
  <td class="content-field-review"><c:out value="${promotionOverviewForm.baseUnitPosition}" /></td>
</tr>

<%-- Achievement Precision --%>
<tr>
  <td>&nbsp;</td>
  <td class="content-field-label"><cms:contentText key="ACHIEVEMENT_PRECISION" code="promotion.payout.throwdown"/></td>
  <td class="content-field-review"><c:out value="${promotionOverviewForm.achievementPrecision}" /></td>
</tr>   
        
<%-- Rounding Method --%>
<tr>
  <td>&nbsp;</td>
  <td class="content-field-label"><cms:contentText key="ROUNDING_METHOD" code="promotion.payout.throwdown"/></td>
  <td class="content-field-review"><c:out value="${promotionOverviewForm.roundingMethod}" /></td>
</tr>

<tr>
  <td>&nbsp;</td>
  <td class="content-field-label"><cms:contentText code="promotion.bill.code" key="BILL_CODES_ACTIVE" /></td>
  <td class="content-field-review"><c:out value="${promotionOverviewForm.billCodesActive}" /></td>
</tr>

<%-- Number of Rounds--%>
<tr>
  <td>&nbsp;</td>
  <td class="content-field-label"><cms:contentText code="promotion.payout.throwdown" key="NUMBER_OF_ROUNDS"/></td>
  <td class="content-field-review"><c:out value="${promotionOverviewForm.numberOfRounds}" /></td>
</tr>

<%-- Number of Days per Round--%>
<tr>
  <td>&nbsp;</td>
  <td class="content-field-label"><cms:contentText code="promotion.payout.throwdown" key="NUMBER_OF_DAYS_PER_ROUND"/></td>
  <td class="content-field-review"><c:out value="${promotionOverviewForm.numberOfDayPerRound}"  /></td>
</tr>

<%-- Start Date--%>
<tr>
  <td>&nbsp;</td>
  <td class="content-field-label"><cms:contentText code="promotion.payout.throwdown" key="START_DATE_FOR_FIRST_ROUND"/></td>
  <td class="content-field-review"><c:out value="${promotionOverviewForm.startDateForFirstRound}"  /></td>
</tr>

<tr class="form-blank-row"><td></td></tr>