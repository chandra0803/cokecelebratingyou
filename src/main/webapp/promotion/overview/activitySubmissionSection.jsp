<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="com.biperf.core.ui.promotion.PromotionOverviewForm"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<%
  Map paramMap = new HashMap();
  PromotionOverviewForm temp = (PromotionOverviewForm)request.getSession().getAttribute( "promotionOverviewForm" );
  paramMap.put( "promotionId", temp.getPromotionId() );
  paramMap.put( "promotionTypeCode", temp.getPromotionTypeCode() );
  String url = "/" + (String)request.getAttribute( "promotionStrutsModulePath" ) + "/promotionActivitySubmission.do?method=display";
  pageContext.setAttribute( "viewUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), url, paramMap ) );
%>

<c:choose>
      <c:when test="${ promotionOverviewForm.promotionStatus == 'expired' }">
        <c:if test="${!hideEditLinks}"> 
          <a class="content-link" href="<c:out value="${viewUrl}"/>">
            <cms:contentText code="system.link" key="VIEW" />
		  </a>
        </c:if>
	  </c:when>
	  <c:otherwise>
		<beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
		  <c:if test="${!hideEditLinks}"> 
		    <a class="content-link" href="<c:out value="${viewUrl}"/>">
              <cms:contentText code="system.link" key="EDIT" />
		    </a>	
		  </c:if>
	    </beacon:authorize>
	  </c:otherwise>
	</c:choose>

	<tr>
  		<td>&nbsp;</td>
  		<td class="content-field-label"><cms:contentText key="SPREADSHEET_UPLOAD" code="promotion.ssi.activitysubmission" /></td>
  		<td class="content-field-review">
  			<c:choose>
  				<c:when test="${promotionOverviewForm.allowSpreadSheet==null}"><cms:contentText code="promotion.overview" key="NOT_DEFINED"/></c:when>
  				<c:when test="${promotionOverviewForm.allowSpreadSheet}"><cms:contentText code="system.common.labels" key="YES" /></c:when>
  				<c:otherwise><cms:contentText code="system.common.labels" key="NO" /></c:otherwise>
  			</c:choose>
  		</td>
	</tr>
	<%-- Claim submission and approval moved to SSI_Phase_2 --%>

	<tr>
  		<td>&nbsp;</td>
  		<td class="content-field-label"><cms:contentText key="CLAIM_SUBMISSION" code="promotion.ssi.activitysubmission" /></td>
  		<td class="content-field-review">
  			<c:choose>
  				<c:when test="${promotionOverviewForm.allowClaimSubmission==null}"><cms:contentText code="promotion.overview" key="NOT_DEFINED"/></c:when>
  				<c:when test="${promotionOverviewForm.allowClaimSubmission}"><cms:contentText code="system.common.labels" key="YES" /></c:when>
  				<c:otherwise><cms:contentText code="system.common.labels" key="NO" /></c:otherwise>
  			</c:choose>
  		</td>
	</tr>
<!--	
	<c:if test="${promotionOverviewForm.allowClaimSubmission}">
		<tr>
  			<td>&nbsp;</td>
  			<td class="content-field-label"><cms:contentText key="REQUIRE_APPROVAL" code="promotion.ssi.activitysubmission" /></td>
  			<td class="content-field-review">
  				<c:choose>
	  				<c:when test="${promotionOverviewForm.requireClaimApproval==null}"><cms:contentText code="promotion.overview" key="NOT_DEFINED"/></c:when>
	  				<c:when test="${promotionOverviewForm.requireClaimApproval}"><cms:contentText code="system.common.labels" key="YES" /></c:when>
	  				<c:otherwise><cms:contentText code="system.common.labels" key="NO" /></c:otherwise>
  				</c:choose>
  			</td>
		</tr>
		<c:if test="${promotionOverviewForm.requireClaimApproval and not empty promotionOverviewForm.claimApprovalAudienceTypeName}">
			<tr>
  				<td>&nbsp;</td>
  				<td class="content-field-label"><cms:contentText code="promotion.ssi.activitysubmission" key="DAYS_TO_APPROVE" /></td>
  				<td class="content-field-review" colspan="2" nowrap="nowrap"><c:out value="${promotionOverviewForm.numberOfDaysToApproveClaim}"/></td>
			</tr>
			<tr>
  				<td>&nbsp;</td>
  				<td class="content-field-label"><cms:contentText key="CLAIM_APPROVAL_AUDIENCE" code="promotion.ssi.activitysubmission" /></td>
  				<c:choose>
  					<c:when test="${promotionOverviewForm.claimApprovalAudienceTypeName eq 'creatororgandaboveaudience'}">
  						<td class="content-field-review" colspan="2" nowrap="nowrap"><cms:contentText key="CREATOR_ORG_AND_ABOVE" code="promotion.ssi.audience"/></td>
  					</c:when>
  					<c:otherwise>
  						<td class="content-field-review" colspan="2" nowrap="nowrap">
  							<table>
            					<c:forEach items="${promotionOverviewForm.claimApprovalList}" var="claimApproverAudience" varStatus="status">
              						<tr>
              							<td class="content-field-review"><c:out value="${claimApproverAudience.audience.name}"/></td>
                						<td class="content-field-review">&lt;&nbsp;<c:out value="${claimApproverAudience.audience.size}"/>&nbsp;&gt;</td>
              						</tr>
            					</c:forEach>
          					</table>
  						</td>
  					</c:otherwise>
  				</c:choose>
			</tr>
		</c:if>
	</c:if> 
-->
<tr class="form-blank-row"><td></td></tr>