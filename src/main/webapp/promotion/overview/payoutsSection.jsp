<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.ui.promotion.PromotionOverviewForm"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>
	<%	Map paramMap = new HashMap();
			PromotionOverviewForm temp = (PromotionOverviewForm)request.getSession().getAttribute( "promotionOverviewForm" );
			paramMap.put( "id", temp.getPromotionId() );
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
  <td class="content-field-label"><cms:contentText code="promotion.payout" key="PAYOUT_TYPE"/></td>
  <td class="content-field-review"><c:out value="${promotionOverviewForm.payoutType}" /></td>
</tr>

<%-- number of products --%>
<tr>
  <td>&nbsp;</td>
  <td class="content-field-label"><cms:contentText code="promotion.overview" key="NUMBER_OF_PRODUCTS"/></td>
  <td class="content-field-review"><c:out value="${promotionOverviewForm.numberOfProducts}" /></td>
</tr>

<%-- manager override --%>
<tr>
  <td>&nbsp;</td>
  <td class="content-field-label"><cms:contentText code="promotion.payout" key="MANAGER_OVERRIDE"/></td>
  <c:choose>
    <c:when test="${ promotionOverviewForm.payoutManager == true }">
      <td class="content-field-review"><c:out value="${promotionOverviewForm.payoutManagerPercent}" />&nbsp;<cms:contentText code="promotion.overview" key="PERCENT_PAID"/>&nbsp;<c:out value="${promotionOverviewForm.payoutManagerPeriod}"/></td>
    </c:when>
    <c:otherwise>
      <td class="content-field-review"><cms:contentText key="NONE" code="promotion.overview"/></td>
    </c:otherwise>
  </c:choose>
</tr>

<%-- break the bank budget --%>
<c:if test="${promotionOverviewForm.awardType == 'points'}">
  <c:if test="${(promotionOverviewForm.promotionTypeCode == 'product_claim') || (promotionOverviewForm.promotionTypeCode == 'quiz')}">
    <tr>
      <td>&nbsp;</td>
      <td class="content-field-label"><cms:contentText code="promotion.overview" key="BREAK_THE_BANK_BUDGET"/></td>
      <td class="content-field-review">
        <c:choose>
          <c:when test="${not empty promotionOverviewForm.budgetName}">
            <c:out value="${promotionOverviewForm.budgetName}"/>
          </c:when>
          <c:otherwise>
            <cms:contentText key="NONE" code="promotion.overview"/>
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
  </c:if>
</c:if>

<tr class="form-blank-row"><td></td></tr>