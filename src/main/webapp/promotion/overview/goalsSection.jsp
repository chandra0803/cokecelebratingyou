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
  <td class="content-field-label"><cms:contentText code="promotion.payout.goalquest" key="ACHIEVEMENT_RULE"/></td>
  <td class="content-field-review"><c:out value="${promotionOverviewForm.achievementRule}" /></td>
</tr>

<%-- payout structure --%>
<tr>
  <td>&nbsp;</td>
  <td class="content-field-label"><cms:contentText code="promotion.payout.goalquest" key="PAYOUT_STRUCTURE"/></td>
  <td class="content-field-review">
    <c:choose>
      <c:when test="${ ! empty promotionOverviewForm.payoutStructure }">
        <c:out value="${promotionOverviewForm.payoutStructure}" />
      </c:when>
      <c:otherwise>
        <cms:contentText code="system.general" key="NOT_AVAILABLE"/>
      </c:otherwise>
    </c:choose>
  </td>
</tr>

<%-- achievement precision --%>
<tr>
  <td>&nbsp;</td>
  <td class="content-field-label"><cms:contentText code="promotion.payout.goalquest" key="ACHIEVEMENT_PRECISION"/></td>
  <td class="content-field-review"><c:out value="${promotionOverviewForm.achievementPrecision}" /></td>
</tr>

<%-- rounding method --%>
<tr>
  <td>&nbsp;</td>
  <td class="content-field-label"><cms:contentText code="promotion.payout.goalquest" key="ROUNDING_METHOD"/></td>
  <td class="content-field-review"><c:out value="${promotionOverviewForm.roundingMethod}" /></td>
</tr>

<%-- base Unit--%>
<tr>
  <td>&nbsp;</td>
  <td class="content-field-label"><cms:contentText key="BASE_UNIT" code="promotion.payout.goalquest"/></td>
  <td class="content-field-review"><c:out value="${promotionOverviewForm.baseUnit}" /></td>
</tr>

<%-- base unit position --%>
<tr>
  <td>&nbsp;</td>
  <td class="content-field-label"><cms:contentText key="BASE_UNIT_POSITION" code="promotion.payout.goalquest"/></td>
  <td class="content-field-review"><c:out value="${promotionOverviewForm.baseUnitPosition}" /></td>
</tr>

<tr class="form-blank-row"><td></td></tr>