<%--UI REFACTORED--%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.claim.Claim"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ include file="/include/taglib.jspf"%>

<table border="0" cellpadding="10" cellspacing="0" width="500">
  <tr>
    <td>
      <span class="headline"><cms:contentText key="DETAIL_HEADER" code="claims.details"/></span>
      <%-- Subheadline --%>
      <br/>
      <span class="subheadline"><c:out value="${claimDetails.promotion.name}"/></span>
      <%-- End Subheadline --%>
			<br>
      <table>

  <c:if test="${userNodeSize > 1}">
    <tr class="form-row-spacer">
      <td class="content-field-label">
        <cms:contentText key="SELECTED_NODE" code="claims.submission"/>
      </td>
      <td class="content-field-review">
        <c:out value="${claimDetails.node.name}"/>
      </td>
    </tr>
    <tr class="form-blank-row"><td></td></tr>
  </c:if>

	<tr class="form-row-spacer">
		<td class="subheadline" colspan="2">
			<cms:contentText code="claims.product.approval.details" key="CLAIM_INFORMATION"/>
		</td>
	</tr>
  
    <tr class="form-row-spacer">    	
    	<td class="content-field-label">
            <cms:contentText code="claims.product.approval.details" key="CLAIM_NUMBER"/>
        </td>
        <td class="content-field-review">
        	<c:out value="${claimDetails.claimNumber}"/>
        </td>
    </tr>	
  
  	
    <tr class="form-row-spacer">    	       
    	<td class="content-field-label">
            <cms:contentText code="claims.product.approval.details" key="CLAIM_DATE"/>
        </td>
        <td class="content-field-review">
        	<fmt:formatDate value="${claimDetails.auditCreateInfo.dateCreated}" pattern="${JstlDatePattern}" />
        </td>
    </tr>	
    
    <tr class="form-row-spacer">    	       
    	<td class="content-field-label">
            <cms:contentText code="claims.product.approval.details" key="CLAIM_STATUS"/>
        </td>
        <td class="content-field-review">
        	<c:choose>
    			<c:when test="${claimDetails.open}">
    				<cms:contentText code="claims.product.approval.details" key="OPEN"/>
    			</c:when>
    			<c:otherwise>
    				<cms:contentText code="claims.product.approval.details" key="CLOSED"/>
    			</c:otherwise>
    		</c:choose>
        </td>
    </tr>	
    
    <c:if test="${!claimDetails.open}">
       
       <tr class="form-row-spacer">    	       
    		<td class="content-field-label">
            	<cms:contentText code="claims.product.approval.details" key="APPROVED_BY"/>
        	</td>
        	<c:if test="${!systemApproved}">
        		<td class="content-field-review">
              <c:if test="${mostRecentClaimProduct.currentClaimItemApprover != null}">
                <c:set var="approverUser" value="${mostRecentClaimProduct.currentClaimItemApprover.approverUser}"/>
                <c:if test="${approverUser.lastName != null}">
                  <c:out value="${approverUser.firstName}"/>&nbsp;
                  <c:out value="${approverUser.lastName}"/>
                </c:if>
              </c:if>
        		</td>
        	</c:if> 
        	<c:if test="${systemApproved}">
        		<td class="content-field-review">
        			<cms:contentText code="claims.product.approval.details" key="SYSTEM"/>
        		</td>
        	</c:if>  
    	</tr>	
    
    	<tr class="form-row-spacer">    	       
    		<td class="content-field-label">
            	<cms:contentText code="claims.product.approval.details" key="APPROVAL_DATE"/>
        	</td>
        	<td class="content-field-review">
        		<fmt:formatDate value="${mostRecentClaimProduct.dateApproved}" pattern="${JstlDatePattern}" />
        	</td>
    	</tr>
          	     	
      <c:if test="${claimDetails.approverComments != null && claimDetails.approverComments != ''}">
      	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<%  Map paramMap = new HashMap();
						Claim temp = (Claim)request.getAttribute("claimDetails");
						paramMap.put( "claimid", temp.getId() );
						pageContext.setAttribute("linkUrl", ClientStateUtils.generateEncodedLink( "", "claimDetailsApproverCommentsDisplay.do", paramMap ) );
				%>
        <a class="crud-content-link" href="${linkUrl}" target="_blank">
	      	<cms:contentText code="claims.product.approval.details" key="SEE_COMMENTS"/>
	      </a>
			</c:if>  	     	
      <tr>
				<td></td>
			</tr>    	     	
    </c:if>
    
  	<%@ include file="/claim/claimDetailsClaimElements.jspf"%>
    
    <c:if test="${claimDetails.promotion.promotionType.productClaimPromotion && claimDetails.promotion.teamUsed}">
    
    <%@ include file="/claim/claimDetailsTeamInfo.jspf"%>
    
    </c:if>  
    
</table>

<table width="500">
  
  <tr class="form-blank-row">
    <td></td>
  </tr>
    
<c:if test="${claimDetails.promotion.promotionType.productClaimPromotion}">
  <%-- Start PRODUCT INFORMATION --%>
  <tr class="form-row-spacer">
    <td class="subheadline">
      <cms:contentText code="claims.submission" key="PRODUCT_INFORMATION"/>
    </td>
  </tr>
  
<tr>
 <td>

 <table>
   <tr>
     <td>
        <display:table defaultsort="1" defaultorder="ascending" name="claimDetails.claimProducts" id="claimProduct" sort="list">
        <display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
			</display:setProperty>
			<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
          <display:column titleKey="claims.product.approval.details.CATEGORY" headerClass="crud-table-header-row" class="crud-content left-align content-field-label-top">
          	   <c:choose>
                    <c:when test="${claimProduct.product.productCategory.parentProductCategory == null}">
                      <c:out value="${claimProduct.product.productCategory.name }"/>
                    </c:when>
                    <c:otherwise >
                      <c:out value="${claimProduct.product.productCategory.parentProductCategory.name }"/>
                    </c:otherwise>
              </c:choose>
          </display:column>
          <display:column titleKey="claims.product.approval.details.SUBCATEGORY" headerClass="crud-table-header-row" class="crud-content left-align content-field-label-top">
		          <c:choose>
                    <c:when test="${claimProduct.product.productCategory.parentProductCategory == null}">
                      &nbsp;
                    </c:when>
                    <c:otherwise >
                      <c:out value="${claimProduct.product.productCategory.name }"/>
                    </c:otherwise>
                  </c:choose>
          </display:column>
          <display:column property="product.name" titleKey="claims.product.approval.details.PRODUCT" headerClass="crud-table-header-row" class="crud-content left-align content-field-label-top"/>
          <display:column titleKey="claims.product.approval.details.CHARACTERISTICS" headerClass="crud-table-header-row" class="crud-content left-align content-field-label-top">
          	<c:forEach items="${claimProduct.claimProductCharacteristics}" var="claimProductCharacteristic">
              <c:out value="${claimProductCharacteristic.productCharacteristicType.characteristicName}"/>&nbsp;=&nbsp;<c:out value="${claimProductCharacteristic.value}"/>
              <br>
            </c:forEach>
          </display:column>
          <display:column property="quantity" titleKey="claims.product.approval.details.QUANTITY" headerClass="crud-table-header-row" class="crud-content right-align content-field-label-top"/>
          <c:if test="${showDetail == 'true'}">
          	<display:column titleKey="claims.product.approval.details.STATUS" headerClass="crud-table-header-row" class="crud-content left-align content-field-label-top nowrap">
          		
          		<c:out value="${claimProduct.approvalStatusType.name}"/>
                <c:if test="${claimProduct.approvalStatusType.code == 'pend' || claimProduct.approvalStatusType.code == 'deny' || claimProduct.approvalStatusType.code == 'hold' && claimProduct.promotionApprovalOptionReasonType.code != null && claimProduct.promotionApprovalOptionReasonType.code != ''}">
                   &nbsp;-&nbsp;<c:out value="${ claimProduct.promotionApprovalOptionReasonType.name }"/>
                </c:if>
          	</display:column>          	
          </c:if>
        </display:table>

      </td>
    </tr>
  </table>

 </td>
</tr>
<%-- END PRODUCT INFORMATION --%>
</c:if>
  
  <c:if test="${!claimDetails.open}">
		<c:choose>
			<c:when test="${empty journals}">
				<tr class="form-blank-row">
	    		<td></td>
		  	</tr>
		  	
		    <tr class="form-row-spacer">
		    	<td class="subheadline">
		           <cms:contentText code="claims.product.approval.details" key="PAYOUT_INFO"/> - <c:out value="${claimDetails.promotion.name}"/>
		        </td>
		    </tr>	
            
		    <tr class="form-row-spacer">		    		    	
		        <td class="content-field-review" colspan="3">		        	
		        	<cms:contentText code="claims.product.approval.details" key="NO_PAYOUT_INFO"/>
		        </td>
		    </tr>	        		                      		
			</c:when>
      <c:otherwise>
				<c:forEach items="${journals}" var="journal">
       		<tr class="form-blank-row">
	    			<td></td>
		  		</tr>
		  	         
		    	<tr class="form-row-spacer">
		    		<td class="subheadline">
		            <cms:contentText code="claims.product.approval.details" key="PAYOUT_INFO"/> - <c:out value="${journal.promotion.name}"/>
		        </td>		        
		    	</tr>	
		    
		    	<tr class="form-row-spacer">
		        <td class="content-field-label">
		        	<fmt:formatDate value="${journal.transactionDate}" pattern="${JstlDatePattern}" />
							&nbsp;&nbsp;&nbsp;&nbsp;
							<cms:contentText code="claims.product.approval.details" key="AWARD"/> = <c:out value="${journal.transactionAmount}"/>
		        </td>
		    	</tr>	
                      			
					<c:if test="${journal.promotion.payoutCarryOver && journal.promotion.payoutType.code == 'tiered'}">
						<c:forEach items="${journal.activityJournals}" var="activityJournal">
							<tr class="form-row-spacer">
			        	<td class="content-field-label">
			        		&nbsp;&nbsp;&nbsp;
			        		<cms:contentText code="claims.product.approval.details" key="QTY"/> = <c:out value="${activityJournal.activity.quantity}"/>
                  &nbsp;&nbsp;&nbsp;&nbsp;
                  <cms:contentText code="claims.product.approval.details" key="CLAIM_NBR"/> = <c:out value="${activityJournal.activity.claim.claimNumber}"/>
			        	</td>
			      	</tr>                					
						</c:forEach>
					</c:if>
        </c:forEach>
      </c:otherwise>
    </c:choose>                  	    	
  </c:if>  
</table>

    </td>
  </tr>
</table>

<table width="100%">
  <tr class="form-buttonrow">
    <td align="center">
      <html:button property="closeBtn" styleClass="content-buttonstyle" onclick="javascript:window.print()">
        <cms:contentText code="system.button" key="PRINT" />
      </html:button>
      &nbsp;&nbsp;
      <html:button property="closeBtn" styleClass="content-buttonstyle" onclick="javascript:window.close()">
        <cms:contentText code="system.button" key="CLOSE_WINDOW" />
      </html:button>
    </td>
  </tr>
</table>