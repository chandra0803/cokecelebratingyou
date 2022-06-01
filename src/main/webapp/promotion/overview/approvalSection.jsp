<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.ui.promotion.PromotionOverviewForm"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>
	<%	Map paramMap = new HashMap();
			PromotionOverviewForm temp = (PromotionOverviewForm)request.getSession().getAttribute( "promotionOverviewForm" );
			paramMap.put( "promotionId", temp.getPromotionId() );
			paramMap.put( "promotionTypeCode", temp.getPromotionTypeCode() );
	    String url = "/" + (String)request.getAttribute( "promotionStrutsModulePath" ) + "/promotionApproval.do?method=display";
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
      <c:when test="${ promotionOverviewForm.hasParent == 'true' }">
				<a class="content-link" href="<c:out value="${viewUrl}"/>">
          <cms:contentText code="system.link" key="VIEW" />
				</a>
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

<c:if test="${promotionOverviewForm.promotionTypeCode != 'self_serv_incentives'}">
<tr>
  <td>&nbsp;</td>
  <td class="content-field-label"><cms:contentText code="promotion.approvals" key="APPROVAL_TYPE"/></td>
  <td class="content-field-review">
    <c:choose>
      <c:when test="${promotionOverviewForm.approvalTypeCode == 'auto_delayed' }">
        <c:out value="${promotionOverviewForm.approvalType}" />&nbsp;
        <c:out value="${promotionOverviewForm.approvalAutoDelayDays}" />&nbsp;
        <cms:contentText code="promotion.overview" key="DAYS"/>
      </c:when>
      <c:when test="${promotionOverviewForm.approvalTypeCode == 'cond_nth' }">
        <cms:contentText code="promotion.overview" key="CONDITIONAL"/>&nbsp;
        <c:out value="${promotionOverviewForm.approvalConditionalClaimCount}" />&nbsp;
        <cms:contentText code="promotion.overview" key="CLAIM"/>
      </c:when>
      <c:when test="${promotionOverviewForm.approvalTypeCode == 'cond_amt' }">
        <cms:contentText code="promotion.overview" key="CONDITIONAL"/>&nbsp;
        <cms:contentText code="${promotionOverviewForm.claimFormAsset}" key="${promotionOverviewForm.approvalConditionalAmountField}" />&nbsp;
        <cms:contentText code="promotion.overview" key="MUST_BE"/>&nbsp;
        <c:out value="${promotionOverviewForm.approvalConditionalAmountOperator}"/>&nbsp;
        <c:out value="${promotionOverviewForm.approvalConditionalAmount}"/>                    
      </c:when>                               
      <c:when test="${promotionOverviewForm.approvalTypeCode == 'cond_pax' }">
        <cms:contentText code="promotion.overview" key="CONDITIONAL"/>&nbsp;
        <c:out value="${promotionOverviewForm.approvalSubmitterSize}"/>&nbsp;
        <cms:contentText code="promotion.overview" key="PARTICIPANTS"/>&nbsp;
      </c:when>
      <c:when test="${promotionOverviewForm.approvalTypeCode == 'auto_approve' }">
        <cms:contentText code="promotion.overview" key="APPROVAL_TYPE_SYSTEM"/>&nbsp;                    
      </c:when>
      <c:otherwise>
        <c:out value="${promotionOverviewForm.approvalType}" />
      </c:otherwise>
    </c:choose>
  </td>
</tr>
</c:if>

<c:if test="${ promotionOverviewForm.approvalStartDate != null }" >
  <tr>
    <td>&nbsp;</td>
    <td class="content-field-label"><cms:contentText code="promotion.approvals" key="APPROVAL_DATE"/></td>
    <td class="content-field-review" nowrap><c:out value="${promotionOverviewForm.approvalStartDate}" /> - 
      <c:choose>
        <c:when test="${promotionOverviewForm.approvalEndDate != ''}">
          <c:out value="${promotionOverviewForm.approvalEndDate}"/>
        </c:when>
        <c:otherwise>
          <cms:contentText code="promotion.overview" key="NOT_DEFINED"/>
        </c:otherwise>
      </c:choose>
    </td>
  </tr>
</c:if>

<c:if test="${promotionOverviewForm.promotionTypeCode != 'self_serv_incentives'}">
<tr>
  <td>&nbsp;</td>
  <td class="content-field-label"><cms:contentText code="promotion.overview" key="APPROVERS_ARE"/></td>
  <td class="content-field-review"><c:out value="${promotionOverviewForm.approverType}" />&nbsp;
    <c:if test="${promotionOverviewForm.approverTypeCode == 'specific_approv'}">
      < <c:out value="${promotionOverviewForm.approvalApproversSize}"/> >
    </c:if>
  </td>
</tr>
</c:if>

<c:if test="${ promotionOverviewForm.approvalLevel != null }" >
  <tr>
    <td>&nbsp;</td>
    <td class="content-field-label"><cms:contentText code="promotion.approvals" key="APPROVAL_NODE_LEVELS"/></td>
    <td class="content-field-review"><c:out value="${promotionOverviewForm.approvalLevel}" />&nbsp;
    </td>
  </tr>
</c:if>

<c:if test="${promotionOverviewForm.promotionTypeCode != 'nomination' and promotionOverviewForm.promotionTypeCode != 'self_serv_incentives'}">
  <tr>
    <td>&nbsp;</td>
    <td class="content-field-label" nowrap><cms:contentText code="promotion.approvals" key="APPROVAL_OPTIONS"/></td>
    <td class="content-field-review"><c:out value="${promotionOverviewForm.approvalOptions}"/></td>
  </tr>
</c:if>
            
<c:if test="${promotionOverviewForm.promotionTypeCode == 'self_serv_incentives'}">
  <tr>
    <td>&nbsp;</td>
    <td class="content-field-label" nowrap><cms:contentText key="APPROVAL_OPTIONS" code="promotion.ssi.approvals" /></td>
    <td class="content-field-review">
    	<c:choose>
        <c:when test="${empty promotionOverviewForm.reqireApproval}">
        	<cms:contentText code="promotion.overview" key="NOT_DEFINED"/>
        </c:when>
        <c:otherwise>
          	<c:choose>
          		<c:when test="${promotionOverviewForm.reqireApproval}"><cms:contentText code="system.common.labels" key="YES" /></c:when>
          		<c:otherwise><cms:contentText code="system.common.labels" key="NO" /></c:otherwise>
          	</c:choose>
        </c:otherwise>
      </c:choose>
    </td>
  </tr>
</c:if>

<c:if test="${promotionOverviewForm.promotionTypeCode == 'self_serv_incentives' and promotionOverviewForm.reqireApproval}">
  <tr>
    <td>&nbsp;</td>
    <td class="content-field-label" nowrap><cms:contentText key="LEVEL_OPTIONS" code="promotion.ssi.approvals" /></td>
    <td class="content-field-review">
    	<c:out value="${promotionOverviewForm.numberOfApproverLevels}"/>
    </td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td class="content-field-label" nowrap><cms:contentText key="LEVEL1_APPROVER_DISPLAY" code="promotion.ssi.approvals" /></td>
    <td class="content-field-review">
    	<table>
            <c:forEach items="${promotionOverviewForm.level1ApproversList}" var="level1Audience" varStatus="status">
              <tr>
                <td class="content-field-review">
                  <c:out value="${level1Audience.audience.name}"/>
                </td>
                <td class="content-field-review">                  
                    &lt;&nbsp;
                    <c:out value="${level1Audience.audience.size}"/>
                    &nbsp;&gt;                                       
                </td>
              </tr>
            </c:forEach>
          </table>
    </td>
  </tr>
  <c:if test="${promotionOverviewForm.numberOfApproverLevels == 2 and not empty promotionOverviewForm.level2ApproversList}">
  	<tr>
    	<td>&nbsp;</td>
    	<td class="content-field-label" nowrap><cms:contentText key="LEVEL2_APPROVER_DISPLAY" code="promotion.ssi.approvals" /></td>
    	<td class="content-field-review">
    		<table>
            <c:forEach items="${promotionOverviewForm.level2ApproversList}" var="level2Audience" varStatus="status">
              <tr>
                <td class="content-field-review">
                  <c:out value="${level2Audience.audience.name}"/>
                </td>
                <td class="content-field-review">                  
                    &lt;&nbsp;
                    <c:out value="${level2Audience.audience.size}"/>
                    &nbsp;&gt;                                       
                </td>
              </tr>
            </c:forEach>
          </table>
    	</td>
  	</tr>
  </c:if>
</c:if>
            
<tr class="form-blank-row"><td></td></tr>