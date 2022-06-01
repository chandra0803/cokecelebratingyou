<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.ui.promotion.PromotionOverviewForm"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>
	<%	Map paramMap = new HashMap();
			PromotionOverviewForm temp = (PromotionOverviewForm)request.getSession().getAttribute( "promotionOverviewForm" );
			paramMap.put( "promotionId", temp.getPromotionId() );
			paramMap.put( "promotionTypeCode", temp.getPromotionTypeCode() );
	    String url = "/" + (String)request.getAttribute( "promotionStrutsModulePath" ) + "/promotionSweepstakes.do?method=display";
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
  <td class="content-field-label"><cms:contentText code="promotion.overview" key="SWEEPSTAKES_ACTIVE"/></td>
  <td class="content-field-review"><c:out value="${promotionOverviewForm.sweepstakesActive}" /></td>
</tr>
<c:if test="${ promotionOverviewForm.sweepstakesActive == 'yes'}">
  <c:choose>     
    <c:when test="${promotionOverviewForm.sweepsWinnerEligibilityCode == 'combineddraw'}">
      <tr>
        <td>&nbsp;</td>
        <td class="content-field-label">
          <cms:contentText code="promotion.sweepstakes" key="SUBMITTER_AND_TEAM"/>
        </td>
        <td class="content-field-review"><c:out value="${promotionOverviewForm.numberOrPercentOfGivers}"/>&nbsp;<cms:contentText key="WINNERS" code="promotion.sweepstakes"/>&nbsp;&nbsp;&nbsp;
          <c:out value="${promotionOverviewForm.amountOfGiversAwards}"/>&nbsp;<c:out value="${promotionOverviewForm.awardTypeText}"/>
        </td>
      </tr>                  
    </c:when>
    <c:otherwise>
      <%--To fix 18208 display givers and receivers combined --%>
      <c:if test="${not empty promotionOverviewForm.numberOrPercentOfGivers}">
        <tr>
          <td>&nbsp;</td>
          <td class="content-field-label">
            <c:if test="${promotionOverviewForm.promotionTypeCode == 'recognition' && promotionOverviewForm.sweepsWinnerEligibilityCode == 'givers'}">
              <cms:contentText code="promotion.audience" key="GIVERS"/>
            </c:if>
            <c:if test="${promotionOverviewForm.promotionTypeCode == 'recognition' && promotionOverviewForm.sweepsWinnerEligibilityCode == 'combined'}">
         
          <cms:contentText code="promotion.sweepstakes" key="GIVERS_AND_RECEIVERS"/>
            </c:if>
            <c:if test="${promotionOverviewForm.promotionTypeCode == 'recognition' && promotionOverviewForm.sweepsWinnerEligibilityCode == 'separate'}">
              <cms:contentText code="promotion.audience" key="GIVERS"/>
            </c:if>
            
            <c:if test="${promotionOverviewForm.promotionTypeCode == 'product_claim' || promotionOverviewForm.promotionTypeCode == 'survey' }">
              <cms:contentText code="promotion.sweepstakes" key="SUBMITTERS"/>
            </c:if>
            <c:if test="${promotionOverviewForm.promotionTypeCode == 'quiz' }">
              <cms:contentText code="promotion.audience" key="PARTICIPANTS"/>
            </c:if>
            <c:if test="${promotionOverviewForm.promotionTypeCode == 'nomination' }">
              <cms:contentText code="promotion.audience" key="NOMINATORS"/>
            </c:if>
          </td>
          <td class="content-field-review"><c:out value="${promotionOverviewForm.numberOrPercentOfGivers}"/>&nbsp;<cms:contentText key="WINNERS" code="promotion.sweepstakes"/>&nbsp;&nbsp;&nbsp;
            <c:choose>
            	<c:when test="${promotionOverviewForm.awardType == 'merchandise'}">
            		<cms:contentText code="recognition.merchandise" key="LEVEL_NAME"/> <c:out value="${promotionOverviewForm.sweepstakesPrimaryAwardLevel}"/>
            	</c:when>
            	<c:otherwise>
		            <c:out value="${promotionOverviewForm.amountOfGiversAwards}"/>
				</c:otherwise>
            </c:choose>
            <c:out value="${promotionOverviewForm.awardTypeText}"/>
          </td>
        </tr>
      </c:if>
	  <c:if test="${not empty promotionOverviewForm.numberOrPercentOfReceivers}">
        <tr>
	      <td>&nbsp;</td>
	      <td class="content-field-label">
	        <c:if test="${promotionOverviewForm.promotionTypeCode == 'recognition' ||
	                      promotoinOverviewForm.promotionTypeCode == 'quiz'}">
	          <cms:contentText code="promotion.audience" key="RECEIVERS"/>
	        </c:if>
	        <c:if test="${promotionOverviewForm.promotionTypeCode == 'product_claim'}">
	          <cms:contentText code="promotion.audience" key="TEAM_MEMBERS"/>
	        </c:if>
	        <c:if test="${promotionOverviewForm.promotionTypeCode == 'nomination'}">
	          <cms:contentText code="promotion.audience" key="NOMINEES"/>
	        </c:if>
	      </td> 
	      <td class="content-field-review"><c:out value="${promotionOverviewForm.numberOrPercentOfReceivers}"/>&nbsp;<cms:contentText key="WINNERS" code="promotion.sweepstakes"/>&nbsp;&nbsp;&nbsp;
	        <c:choose>
            	<c:when test="${promotionOverviewForm.awardType == 'merchandise'}">
            		<cms:contentText code="recognition.merchandise" key="LEVEL_NAME"/> <c:out value="${promotionOverviewForm.sweepstakesSecondaryAwardLevel}"/>
            	</c:when>
            	<c:otherwise>
		            <c:out value="${promotionOverviewForm.amountOfReceiversAwards}"/>
				</c:otherwise>
            </c:choose>
	        <c:out value="${promotionOverviewForm.awardTypeText}"/>
	      </td>
	    </tr>
      </c:if>
      
      
  	</c:otherwise>
  </c:choose>
</c:if>

<tr class="form-blank-row"><td></td></tr>