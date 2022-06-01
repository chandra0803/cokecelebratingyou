<%@ include file="/include/taglib.jspf"%>
  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="DETAIL_HEADER" code="claims.product.approval.details"/></span>
        <%-- Subheadline --%>
        <br/>       
        <span class="subheadline">
        	<c:out value="${claimDetails.promotion.name}"/>
        </span>
        <br/>
                
   		<%-- End Subheadline --%>
        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
<%--            <cms:contentText key="DETAILS_INFO" code="claims.details"/>  --%>
        </span>
        
        <%--END INSTRUCTIONS--%>
        	<cms:errors/>
			<table>
				<tr class="form-row-spacer">
					<td class="subheadline" colspan="2">
						<cms:contentText code="claims.product.approval.details" key="CLAIM_INFORMATION"/>
					</td>
				</tr>

			    <tr class="form-row-spacer">    	
			    	<td class="content-field-label">
			            <cms:contentText code="claims.product.approval.details" key="SUBMITTER_NAME"/>
			        </td>
			        <td class="content-field-review">
				  		<c:out value="${claimDetails.submitter.firstName}" />
						<c:out value="${claimDetails.submitter.middleName}" />
						<c:out value="${claimDetails.submitter.lastName}" />			        	
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
			        			<c:if test="${mostRecentClaimProduct.currentApproverUser != null && mostRecentClaimProduct.currentApproverUser.lastName != null}">
			        				<c:out value="${mostRecentClaimProduct.currentApproverUser.firstName}"/>&nbsp;
			        				<c:out value="${mostRecentClaimProduct.currentApproverUser.lastName}"/>
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
			          	     	
			    </c:if>			
			</table>
			
			<%@ include file="/claim/claimDetailsApproverComments.jspf"%>
      </td>
    </tr>
    <tr>
    <td align="center">
			<table>
			  <tr>
			    <td align="center" colspan="4">
					<html:button property="closeBtn" styleClass="content-buttonstyle" onclick="javascript:window.close()">
					  <cms:contentText code="system.button" key="CLOSE_WINDOW" />
					</html:button>
			    </td>
			  </tr>
			</table>
      </td>
    </tr>        
  </table>