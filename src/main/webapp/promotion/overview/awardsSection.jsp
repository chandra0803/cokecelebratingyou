<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.ui.promotion.PromotionOverviewForm"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>
	<%	Map paramMap = new HashMap();
			PromotionOverviewForm temp = (PromotionOverviewForm)request.getSession().getAttribute( "promotionOverviewForm" );
			paramMap.put( "promotionId", temp.getPromotionId() );
			paramMap.put( "promotionTypeCode", temp.getPromotionTypeCode() );
	    String url = "/" + (String)request.getAttribute( "promotionStrutsModulePath" ) + "/promotionAwards.do?method=display";
			pageContext.setAttribute("viewUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), url, paramMap ) );
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

<%-- awards active --%>
<tr>
  <td>&nbsp;</td>
  <td class="content-field-label"><cms:contentText code="promotion.awards" key="ACTIVE"/></td>
  <td class="content-field-review">
  	<c:choose>  	
  		<c:when test="${promotionOverviewForm.promotionTypeCode == 'self_serv_incentives'}">
  			<c:choose>
  				<c:when test="${promotionOverviewForm.pointsAvailable or promotionOverviewForm.merchandiseAvailable or promotionOverviewForm.otherAvailable}">
  					<c:if test="${promotionOverviewForm.pointsAvailable}"><cms:contentText code="promotion.ssi.awards" key="AWARD_POINTS" /><br/></c:if>
<%--   					<c:if test="${promotionOverviewForm.merchandiseAvailable}"><cms:contentText code="promotion.ssi.awards" key="AWARD_MERCH" /><br/></c:if> --%> <%-- merchendise move to SSI_Phase_2 --%>
  					<c:if test="${promotionOverviewForm.otherAvailable}"><cms:contentText code="promotion.ssi.awards" key="AWARD_OTHER" /></c:if>
  				</c:when>
  				<c:otherwise><cms:contentText code="promotion.overview" key="NOT_DEFINED"/></c:otherwise>
  			</c:choose>
  		</c:when>
  		<c:otherwise><c:out value="${promotionOverviewForm.awardsActive}" /></c:otherwise>
  	</c:choose>
  </td>
</tr>

<c:if test="${promotionOverviewForm.promotionTypeCode == 'self_serv_incentives'}">
	<c:if test="${promotionOverviewForm.pointsAvailable or promotionOverviewForm.merchandiseAvailable or promotionOverviewForm.otherAvailable}">
		<tr><td>&nbsp;</td>
  			<td class="content-field-label"><cms:contentText key="BADGES" code="promotion.ssi.awards" /></td>
  			<td class="content-field-review">
  				<c:choose>
  					<c:when test="${empty promotionOverviewForm.badgeCount}"><cms:contentText key="NO_BADGE_SELECTED" code="promotion.ssi.awards" /></c:when>
  					<c:otherwise><&nbsp;<c:out value="${promotionOverviewForm.badgeCount}"/>&nbsp;></c:otherwise>
  				</c:choose>
  			</td>
</tr>
	</c:if>
	<tr>
	    <td>&nbsp;</td>
		<td class="content-field-label"><cms:contentText key="TAXABLE" code="promotion.basics" /></td>
		<td class="content-field-review">
		  <c:choose>
		    <c:when test="${promotionOverviewForm.taxable}">
		      <cms:contentText code="system.common.labels" key="YES" />
		    </c:when>
		    <c:otherwise>
		      <cms:contentText code="system.common.labels" key="NO" />
		    </c:otherwise>
		  </c:choose>
		</td>
   </tr>
</c:if>

<c:if test="${promotionOverviewForm.promotionTypeCode == 'nomination'}">
	<tr>
	    <td>&nbsp;</td>
		<td class="content-field-label"><cms:contentText key="TAXABLE" code="promotion.basics" /></td>
		<td class="content-field-review">
		  <c:choose>
		    <c:when test="${promotionOverviewForm.taxable}">
		      <cms:contentText code="system.common.labels" key="YES" />
		    </c:when>
		    <c:otherwise>
		      <cms:contentText code="system.common.labels" key="NO" />
		    </c:otherwise>
		  </c:choose>
		</td>
   </tr>
</c:if>

<c:if test="${promotionOverviewForm.awardsActive == 'no' }">
	<c:if test="${promotionOverviewForm.promotionTypeCode == 'recognition' && 
			promotionOverviewForm.awardType == 'merchandise'}">
      <tr>
        <td>&nbsp;</td>
        <td class="content-field-label"><cms:contentText code="promotion.overview" key="FEATURED_AWARDS"/></td>
        <td class="content-field-review">
          <c:out value="${promotionOverviewForm.featuredAwardsEnabled}"/>
        </td>
      </tr>
      <tr>
        <td>&nbsp;</td>
        <td class="content-field-label"><cms:contentText code="promotion.overview" key="WHATS_NEW"/></td>
        <td class="content-field-review">
          <c:choose>
	          <c:when test="${promotionOverviewForm.whatsNewDescription!=null }">
		        <c:out value="${promotionOverviewForm.whatsNewDescription}"/>
		      </c:when>
		      <c:otherwise>
		      	<cms:contentText key="NONE" code="promotion.awards" />
		      </c:otherwise>
	      </c:choose>
        </td>
      </tr>
    </c:if>
</c:if>

<c:if test="${promotionOverviewForm.awardsActive == 'yes' }">
  <%-- awards --%>
  <tr>
    <td>&nbsp;</td>
    <td class="content-field-label"><cms:contentText code="promotion.overview" key="AWARD"/></td>
    <c:choose>
    	<c:when test="${promotionOverviewForm.awardType == 'merchandise'}">
    		<c:if test="${promotionOverviewForm.awardStructure == 'level' }">
			    <td class="content-field-review"><cms:contentText code="promotion.awards" key="AWARD_STRUCTURE_LEVEL"/></td>
			</c:if>			
	    </c:when>
	    <c:otherwise>
		    <td class="content-field-review"><c:out value="${promotionOverviewForm.award}" /></td>
	    </c:otherwise>
	</c:choose>
  </tr>
  
  <c:if test="${(promotionOverviewForm.promotionTypeCode == 'recognition') && (promotionOverviewForm.awardType == 'points')}">
      <tr>
        <td>&nbsp;</td>
        <td class="content-field-label"><cms:contentText code="promotion.overview" key="BUDGET_SWEEP"/></td>
        <td class="content-field-review">
          <c:out value="${promotionOverviewForm.budgetSweepEnabled}" />
        </td>
      </tr>
  </c:if>

  <c:if test="${promotionOverviewForm.awardType == 'points' || promotionOverviewForm.awardType == 'merchandise'}">
    <c:if test="${(promotionOverviewForm.promotionTypeCode == 'recognition') || (promotionOverviewForm.promotionTypeCode == 'nomination')}">
      <tr>
        <td>&nbsp;</td>
        <td class="content-field-label"><cms:contentText code="promotion.overview" key="BUDGET"/></td>
        <td class="content-field-review">
          <c:choose>
            <c:when test="${empty promotionOverviewForm.budgetName || promotionOverviewForm.processingMode == 'file_load'}">
              <cms:contentText code="promotion.overview" key="NO_BUDGET"/>
            </c:when>
            <c:otherwise>
              <c:out value="${promotionOverviewForm.budgetName}" />
            </c:otherwise>
          </c:choose>
        </td>
      </tr>
    </c:if>
	<c:if test="${promotionOverviewForm.promotionTypeCode == 'recognition' && 
			(promotionOverviewForm.awardStructure=='level')}">
      <tr>
        <td>&nbsp;</td>
        <td class="content-field-label"><cms:contentText code="promotion.overview" key="FEATURED_AWARDS"/></td>
        <td class="content-field-review">
          <c:out value="${promotionOverviewForm.featuredAwardsEnabled}"/>
        </td>
      </tr>
      <tr>
        <td>&nbsp;</td>
        <td class="content-field-label"><cms:contentText code="promotion.overview" key="WHATS_NEW"/></td>
        <td class="content-field-review">
          <c:choose>
	          <c:when test="${promotionOverviewForm.whatsNewDescription!=null }">
		        <c:out value="${promotionOverviewForm.whatsNewDescription}"/>
		      </c:when>
		      <c:otherwise>
		      	<cms:contentText key="NONE" code="promotion.awards" />
		      </c:otherwise>
	      </c:choose>
        </td>
      </tr>
    </c:if>
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
</c:if>

<tr class="form-blank-row"><td></td></tr>