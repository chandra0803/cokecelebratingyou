<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.ui.promotion.PromotionOverviewForm"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>
	<%	Map paramMap = new HashMap();
			PromotionOverviewForm temp = (PromotionOverviewForm)request.getSession().getAttribute( "promotionOverviewForm" );
			paramMap.put( "promotionId", temp.getPromotionId() );
			paramMap.put( "promotionTypeCode", temp.getPromotionTypeCode() );
	    String url = "/" + (String)request.getAttribute( "promotionStrutsModulePath" ) + "/promotionWebRules.do?method=display";
			pageContext.setAttribute("viewUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), url, paramMap ) );
	%>
   	<c:choose>
      <c:when test="${ promotionOverviewForm.promotionStatus == 'expired' && (!promotionOverviewForm.webRulesActive || promotionOverviewForm.webRulesEndDate != '') }">
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

<html:hidden property="partnerAvailable" styleId="partnerAvailable" />
<c:set var="partnerAvailable" scope="request" value="${promotionWebRulesForm.partnerAvailable}" />

<c:if test="${promotionOverviewForm.promotionTypeCode == 'recognition'}" >
  <c:set var="primaryAudienceKey" value="ALL_GIVER"/>
  <c:set var="secondaryAudienceKey" value="ALL_RECEIVER"/>
  <c:set var="primaryAndSecondaryAudienceKey" value="ALL_GIVER_AND_RECEIVER"/>
</c:if>
<c:if test="${promotionOverviewForm.promotionTypeCode == 'nomination'}" >
  <c:set var="primaryAudienceKey" value="ALL_NOMINATOR"/>
  <c:set var="secondaryAudienceKey" value="ALL_NOMINEE"/>
  <c:set var="primaryAndSecondaryAudienceKey" value="ALL_NOMINATOR_AND_NOMINEE"/>
</c:if>
<c:if test="${promotionOverviewForm.promotionTypeCode == 'quiz'}" >
  <c:set var="primaryAudienceKey" value="ALL_ELIGIBLE_PAX"/>
</c:if>
<c:if test="${promotionOverviewForm.promotionTypeCode == 'goalquest'}" >
  <c:set var="primaryAndSecondaryAudienceKey" value="ALL_ELIGIBLE_PAX"/>
</c:if>
<c:if test="${promotionOverviewForm.promotionTypeCode == 'challengepoint'}" >
  <c:set var="primaryAndSecondaryAudienceKey" value="ALL_ELIGIBLE_PAX"/>
</c:if>
<c:choose>			 
  <%-- If the webRules is active --%>
  <c:when test="${promotionOverviewForm.webRulesActive == 'true' }">
    <tr>
      <td>&nbsp;</td>
      <td class="content-field-label"><cms:contentText code="promotion.overview" key="RULES_TEXT_ACTIVE"/></td>
      <td class="content-field-review"><cms:contentText code="promotion.overview" key="TRUE"/></td>
    </tr>

    <tr>
      <td>&nbsp;</td>
      <td class="content-field-label" valign="top">
        <c:choose>
          <c:when test="${promotionOverviewForm.promotionTypeCode != 'goalquest' && promotionOverviewForm.promotionTypeCode != 'challengepoint'}" >
            <cms:contentText code="promotion.webrules" key="AUDIENCE"/>
          </c:when>
          <c:otherwise>
            <cms:contentText code="promotion.webrules" key="PARTICIPANT_AUDIENCE"/>
          </c:otherwise>
        </c:choose>
      </td>
      <c:choose>
        <%-- When webRules is same as submitters --%>
        <c:when test="${ promotionOverviewForm.webRulesSameAsPrimary == 'true' }">
          <td class="content-field-review" nowrap>
            <cms:contentText code="promotion.webrules" key="SAME_AS_SUBMITTER"/>
          </td>
        </c:when>

        <%--  When webRules is all Active Pax --%>
        <c:when test="${ promotionOverviewForm.webRulesAllPax == 'true' }">
          <td class="content-field-review" nowrap>
            <cms:contentText code="promotion.webrules" key="ALL_PAX"/>
          </td>
        </c:when>

        <%--  When webRules is All Eligible Primary --%>
        <c:when test="${ promotionOverviewForm.webRulesAllEligiblePrimary == 'true' }">
          <td class="content-field-review" nowrap>
            <cms:contentText code="promotion.webrules" key="${ primaryAudienceKey }"/>
          </td>
        </c:when>
          
        <%--  When webRules is All Eligible Primary and Secondary --%>
        <c:when test="${ promotionOverviewForm.webRulesAllEligiblePrimaryAndSecondary == 'true' }">
          <td class="content-field-review" nowrap>
            <cms:contentText code="promotion.webrules" key="${ primaryAndSecondaryAudienceKey }"/>
          </td>
        </c:when>
        
        <%--  When webRules is All Eligible Secondary --%>
        <c:when test="${ promotionOverviewForm.webRulesAllEligibleSecondary == 'true' }">
          <td class="content-field-review" nowrap>
            <cms:contentText code="promotion.webrules" key="${ secondaryAudienceKey }"/>
          </td>
        </c:when>
          
        <%-- When the webRules is Audience --%>
        <c:when test="${ promotionOverviewForm.webRulesSpecifyAudience == 'true' }">
          <td class="content-field-review">
            <table>
              <c:forEach items="${promotionOverviewForm.webRulesAudienceList}" var="promoWebRulesAudience" varStatus="status">
                <tr>
                  <td class="content-field-review">
                    <c:out value="${promoWebRulesAudience.audience.name}"/>
                  </td>
                  <td class="content-field-review">                    
                      &lt;&nbsp;
                      <c:out value="${promoWebRulesAudience.audience.size}"/>
                      &nbsp;&gt;                    
                  </td>
                </tr>
              </c:forEach>
            </table>
          </td>
        </c:when>
        <c:otherwise>
          <td class="content-field-review" nowrap>
            <c:out value="${promotionOverviewForm.webRulesAudienceTypeName}"/>
          </td>
        </c:otherwise>
      </c:choose>
	</tr>
	<c:if test="${promotionOverviewForm.promotionTypeCode != 'goalquest' && promotionOverviewForm.promotionTypeCode != 'challengepoint' && promotionOverviewForm.promotionTypeCode != 'throwdown'}" >
	<tr>
	  <td>&nbsp;</td>
	  <td class="content-field-label"><cms:contentText code="promotion.webrules" key="DISPLAY_DATES"/></td>
	  <td class="content-field-review" nowrap>
	    <c:out value="${promotionOverviewForm.webRulesStartDate}" /> - 
		<c:choose>
		  <c:when test="${promotionOverviewForm.webRulesEndDate != ''}">
		    <c:out value="${promotionOverviewForm.webRulesEndDate}"/>
		  </c:when>
		  <c:otherwise>
		    <cms:contentText code="promotion.overview" key="NOT_DEFINED"/>
		  </c:otherwise>
		</c:choose>
	  </td>
	</tr>
	</c:if>
	
	<c:if test="${promotionOverviewForm.promotionTypeCode == 'goalquest' || promotionOverviewForm.promotionTypeCode == 'challengepoint'}" >
	<tr>
	  <td>&nbsp;</td>
      <td class="content-field-label" valign="top"><cms:contentText code="promotion.webrules" key="MANAGER_AUDIENCE"/></td>
      	<c:choose>
	      <c:when test="${ promotionOverviewForm.managerWebRulesAllPax == 'true' }">
	        <td class="content-field-review" nowrap>
	          <cms:contentText code="promotion.webrules" key="ALL_PAX"/>
	        </td>
  </c:when>

	      <c:when test="${ promotionOverviewForm.managerWebRulesAllEligible == 'true' }">
	        <td class="content-field-review" nowrap>
	          <cms:contentText code="promotion.webrules" key="${ primaryAndSecondaryAudienceKey }"/>
	        </td>
	      </c:when>
	      
	      <c:when test="${ promotionOverviewForm.managerWebRulesSpecifyAudience == 'true' }">
          <td class="content-field-review">
            <table>
              <c:forEach items="${promotionOverviewForm.managerWebRulesAudienceList}" var="promoManagerWebRulesAudience" varStatus="status">
                <tr>
                  <td class="content-field-review">
                    <c:out value="${promoManagerWebRulesAudience.audience.name}"/>
                  </td>
                  <td class="content-field-review">                    
                      &lt;&nbsp;
                      <c:out value="${promoManagerWebRulesAudience.audience.size}"/>
                      &nbsp;&gt;                    
                  </td>
                </tr>
              </c:forEach>
            </table>
          </td>
        </c:when>
        <c:otherwise>
          <td class="content-field-review" nowrap>
            <c:out value="${promotionOverviewForm.managerWebRulesAudienceTypeName}"/>
          </td>
        </c:otherwise>
	      
	  </c:choose>
	</tr>
	<c:if test="${ promotionOverviewForm.partnerAudienceExists == 'true' }">
	<tr>
	  <td>&nbsp;</td>
      <td class="content-field-label" valign="top"><cms:contentText code="promotion.webrules" key="PARTNER_AUDIENCE"/></td>
      	<c:choose>
	      <c:when test="${ promotionOverviewForm.partnerWebRulesAllPax == 'true' }">
	        <td class="content-field-review" nowrap>
	          <cms:contentText code="promotion.webrules" key="ALL_PAX"/>
	        </td>
	      </c:when>
	      
	      <c:when test="${ promotionOverviewForm.partnerWebRulesAllEligible == 'true' }">
	        <td class="content-field-review" nowrap>
	          <cms:contentText code="promotion.webrules" key="${ primaryAndSecondaryAudienceKey }"/>
	        </td>
	      </c:when>
	      
	      <c:when test="${ promotionOverviewForm.partnerWebRulesSpecifyAudience == 'true' }">
          <td class="content-field-review">
            <table>
              <c:forEach items="${promotionOverviewForm.partnerWebRulesAudienceList}" var="promoPartnerWebRulesAudience" varStatus="status">
                <tr>
                  <td class="content-field-review">
                    <c:out value="${promoPartnerWebRulesAudience.audience.name}"/>
                  </td>
                  <td class="content-field-review">                    
                      &lt;&nbsp;
                      <c:out value="${promoPartnerWebRulesAudience.audience.size}"/>
                      &nbsp;&gt;                    
                  </td>
                </tr>
              </c:forEach>
            </table>
          </td>
        </c:when>
        <c:otherwise>
          <td class="content-field-review" nowrap>
            <c:out value="${promotionOverviewForm.partnerWebRulesAudienceTypeName}"/>
          </td>
        </c:otherwise>
	      
	  </c:choose>
	</tr>
	</c:if>
  </c:if>
  
  </c:when>
  <%-- The webRules isn't defined --%>
  <c:otherwise>
	<tr>
      <td>&nbsp;</td>
      <td class="content-field-label"><cms:contentText code="promotion.overview" key="RULES_TEXT_ACTIVE"/></td>
      <td class="content-field-review"><cms:contentText code="promotion.overview" key="FALSE"/></td>
    </tr>
  </c:otherwise>
</c:choose>

<tr class="form-blank-row"><td></td></tr>