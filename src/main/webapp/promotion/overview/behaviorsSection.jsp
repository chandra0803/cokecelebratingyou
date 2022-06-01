<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.ui.promotion.PromotionOverviewForm"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>
	<%	Map paramMap = new HashMap();
			PromotionOverviewForm temp = (PromotionOverviewForm)request.getSession().getAttribute( "promotionOverviewForm" );
			paramMap.put( "promotionId", temp.getPromotionId() );
			paramMap.put( "promotionTypeCode", temp.getPromotionTypeCode() );
	    String url = "/" + (String)request.getAttribute( "promotionStrutsModulePath" ) + "/promotionBehaviors.do?method=display";
			pageContext.setAttribute("viewUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), url, paramMap ) );
	%>
   	<c:choose>
      <c:when test="${ promotionOverviewForm.promotionStatus == 'expired' }">
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
<tr>
  <td>&nbsp;</td>
  <td class="content-field-label"><cms:contentText code="promotion.overview" key="BEHAVIORS_ACTIVE"/></td>
  <td class="content-field-review"><c:out value="${promotionOverviewForm.behaviorsActive}" /></td>
</tr><c:if test="${ promotionOverviewForm.promotionTypeCode == 'recognition' }" >
<tr>
  <td>&nbsp;</td>
  <td class="content-field-label"><cms:contentText key="BEHAVIORS_REQUIRED" code="promotion.behaviors"/></td>
  <td class="content-field-review"><c:out value="${promotionOverviewForm.behaviorRequired}" /></td>
</tr>
</c:if>
<c:if test="${promotionOverviewForm.behaviorsActive == 'Yes' }">
  <tr>
	<td>&nbsp;</td>
	<td class="content-field-label"><cms:contentText code="promotion.overview" key="NUMBER_OF_BEHAVIORS"/></td>
	<td class="content-field-review"><c:out value="${promotionOverviewForm.numberOfBehaviors}" /></td>
  </tr>
</c:if>
<tr class="form-blank-row"><td></td></tr>