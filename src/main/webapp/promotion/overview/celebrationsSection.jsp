<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.ui.promotion.PromotionOverviewForm"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>
	<%	Map paramMap = new HashMap();
			PromotionOverviewForm temp = (PromotionOverviewForm)request.getSession().getAttribute( "promotionOverviewForm" );
			paramMap.put( "promotionId", temp.getPromotionId() );
			paramMap.put( "promotionTypeCode", temp.getPromotionTypeCode() );
	    String url = "/" + (String)request.getAttribute( "promotionStrutsModulePath" ) + "/promotionCelebrations.do?method=display";
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

<%-- <tr>
  <td>&nbsp;</td>
  <td class="content-field-label">Anniversary in Years or Days?</td>
  <td class="content-field-review"><c:out value="${promotionOverviewForm.anniversaryInYears}" /></td>
</tr> --%>

<%-- <tr>
  <td>&nbsp;</td>
  <td class="content-field-label">Celebration Display Period</td>
  <td class="content-field-review"><c:out value="${promotionOverviewForm.celebrationDisplayPeriod}" /></td>
</tr> --%>

<tr>
  <td>&nbsp;</td>
  <td class="content-field-label">
  <cms:contentText code="promotion.celebrations" key="SERVICE_ANNIVERSARY"/>?</td>
  <td class="content-field-review">
	  <c:choose>
		    <c:when test="${promotionOverviewForm.serviceAnniversary}">
		      <cms:contentText code="system.common.labels" key="YES" />
		    </c:when>
		    <c:otherwise>
		      <cms:contentText code="system.common.labels" key="NO" />
		    </c:otherwise>
	  </c:choose>
  </td>
</tr>

<tr>
  <td>&nbsp;</td>
  <td class="content-field-label"><cms:contentText code="promotion.celebrations" key="OWNER_MESSAGE_QUESTION_MARK" /></td>
  <td class="content-field-review">
	  <c:choose>
		    <c:when test="${promotionOverviewForm.allowOwnerMessage}">
		      <cms:contentText code="system.common.labels" key="YES" />
		    </c:when>
		    <c:otherwise>
		      <cms:contentText code="system.common.labels" key="NO" />
		    </c:otherwise>
	  </c:choose>
  </td>
</tr>

<tr>
  <td>&nbsp;</td>
  <td class="content-field-label"><cms:contentText code="promotion.celebrations" key="SHARE_TO_SOCIAL_QUESTION_MARK" /></td>
  <td class="content-field-review">
	  <c:choose>
		    <c:when test="${promotionOverviewForm.shareToMedia}">
		      <cms:contentText code="system.common.labels" key="YES" />
		    </c:when>
		    <c:otherwise>
		      <cms:contentText code="system.common.labels" key="NO" />
		    </c:otherwise>
	  </c:choose>
  </td>
</tr>

<tr class="form-blank-row"><td></td></tr>
