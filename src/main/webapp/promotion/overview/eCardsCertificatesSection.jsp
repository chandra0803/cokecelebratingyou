<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.ui.promotion.PromotionOverviewForm"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>
	<%	Map paramMap = new HashMap();
			PromotionOverviewForm temp = (PromotionOverviewForm)request.getSession().getAttribute( "promotionOverviewForm" );
			paramMap.put( "promotionId", temp.getPromotionId() );
			paramMap.put( "promotionTypeCode", temp.getPromotionTypeCode() );
	    String url = "/" + (String)request.getAttribute( "promotionStrutsModulePath" ) + "/promotionEcard.do?method=display";
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

<!-- Cards / certificates active flag -->
<c:choose>
	<c:when test="${promotionOverviewForm.promotionTypeCode == 'nomination' }">
		<tr>
		  <td>&nbsp;</td>
		  <td class="content-field-label"><cms:contentText code="promotion.overview" key="ECARDS_ACTIVE"/></td>
		  <td class="content-field-review"><c:out value="${promotionOverviewForm.ECardsActive}" /></td>
		</tr>
		<tr>
		  <td>&nbsp;</td>
		  <td class="content-field-label"><cms:contentText code="promotion.overview" key="CERTIFICATES_ACTIVE"/></td>
		  <td class="content-field-review"><c:out value="${promotionOverviewForm.certificatesActive}" /></td>
		</tr>
	</c:when>
	
	<c:otherwise>
		<tr>
		  <td>&nbsp;</td>
		  <td class="content-field-label"><cms:contentText code="promotion.overview" key="ECARDS_CERTIFICATES_ACTIVE"/></td>
		  <td class="content-field-review"><c:out value="${promotionOverviewForm.ECardsActive}" /></td>
		</tr>
	</c:otherwise>
</c:choose>

<tr>
   <td>&nbsp;</td>
     <td valign="top" class="content-field-label"><cms:contentText code="promotion.basics" key="ALLOW_YOUR_OWN_CARD"/></td>
	 <td class="content-field-review">
	    <c:choose>
	       <c:when test="${promotionOverviewForm.allowYourOwnCard == 'true'}">
	          <cms:contentText code="promotion.overview" key="TRUE"/>
	       </c:when>
	       <c:otherwise>
	         <cms:contentText code="promotion.overview" key="FALSE"/>
	       </c:otherwise>
	    </c:choose>
	  </td>
</tr>
<tr>
   <td>&nbsp;</td>
     <td valign="top" class="content-field-label"><cms:contentText key="DRAW_YOUR_OWN_CARD" code="promotion.basics" /></td>
	 <td class="content-field-review">
	    <c:choose>
	       <c:when test="${promotionOverviewForm.drawYourOwnCard == 'true'}">
	          <cms:contentText code="promotion.overview" key="TRUE"/>
	       </c:when>
	       <c:otherwise>
	         <cms:contentText code="promotion.overview" key="FALSE"/>
	       </c:otherwise>
	    </c:choose>
	  </td>
</tr>

<!-- Number of cards / certificates section -->
<c:choose>
	<c:when test="${promotionOverviewForm.promotionTypeCode == 'nomination' }">
		<c:if test="${promotionOverviewForm.ECardsActive == 'Yes' }">
			<tr>
				<td>&nbsp;</td>
				<td class="content-field-label"><cms:contentText code="promotion.overview" key="NUMBER_OF_ECARDS"/></td>
				<td class="content-field-review"><c:out value="${promotionOverviewForm.numberOfeCards}" /></td>
		  	</tr>
		</c:if>
		
		<c:if test="${promotionOverviewForm.certificatesActive == 'Yes' }">
			<tr>
				<td>&nbsp;</td>
				<td class="content-field-label"><cms:contentText code="promotion.overview" key="NUMBER_OF_CERTIFICATES"/></td>
				<td class="content-field-review"><c:out value="${promotionOverviewForm.numberOfCertificates}" /></td>
		  	</tr>
		</c:if>
	</c:when>
	
	<c:otherwise>
		<c:if test="${promotionOverviewForm.ECardsActive == 'Yes' }">
		  <tr>
			<td>&nbsp;</td>
			<td class="content-field-label"><cms:contentText code="promotion.overview" key="NUMBER_OF_ECARDS"/></td>
			<td class="content-field-review"><c:out value="${promotionOverviewForm.numberOfeCards}" /></td>
		  </tr>
		  <tr>
			<td>&nbsp;</td>
			<td class="content-field-label"><cms:contentText code="promotion.overview" key="NUMBER_OF_CERTIFICATES"/></td>
			<td class="content-field-review"><c:out value="${promotionOverviewForm.numberOfCertificates}" /></td>
		  </tr>  
		</c:if>
	</c:otherwise>
</c:choose>
	          		 
<tr class="form-blank-row"><td></td></tr>