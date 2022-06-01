<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.ui.promotion.PromotionOverviewForm"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>
	<%	Map paramMap = new HashMap();
			PromotionOverviewForm temp = (PromotionOverviewForm)request.getSession().getAttribute( "promotionOverviewForm" );
			paramMap.put( "promotionId", temp.getPromotionId() );
	    String url = "/" + (String)request.getAttribute( "promotionStrutsModulePath" ) + "/promotionPartnerPayout.do?method=display";
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

<%--Partner payout structure --%>
<tr>
  <td>&nbsp;</td>
  <td class="content-field-label"><cms:contentText code="promotion.payout.goalquest" key="PARTNER_PAYOUT_STRUCTURE"/></td>
  <td class="content-field-review">
    <c:choose>
      <c:when test="${ promotionOverviewForm.partnerPayoutStructure != null }">
        <c:out value="${promotionOverviewForm.partnerPayoutStructure}" />
      </c:when>
      <c:otherwise>
        <cms:contentText code="system.general" key="NOT_AVAILABLE"/>
      </c:otherwise>
    </c:choose>
  </td>
</tr>
<tr class="form-blank-row"><td></td></tr>