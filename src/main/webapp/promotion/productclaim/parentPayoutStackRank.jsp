<%--UI REFACTORED--%>
<%--
UI REFACTORING Note: Header and footer pages needs refactoring if necessary. 
As this doen't comes under any of our standard layouts and is specific to this page, changed the content wherever necessary as per refactoring requirements.
--%>

<%@ include file="/include/taglib.jspf"%>
 <%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

<%    
  int calendarCounter = 0;
%>

<tr class="form-row-spacer">
 	<beacon:label property="payoutType" required="true" styleClass="content-field-label-top">
		<cms:contentText key="ELIGIBLE_PRODUCTS" code="promotion.payout"/>
	</beacon:label>
  <td colspan="6">
    <table class="crud-table" width="100%">
      <tr>
        <th valign="top" class="crud-table-header-row"><cms:contentText code="promotion.payout" key="CATEGORY"/></th>
		<th valign="top" class="crud-table-header-row"><cms:contentText code="promotion.payout" key="SUBCATEGORY"/></th>
		<th valign="top" class="crud-table-header-row"><cms:contentText code="promotion.payout" key="PRODUCT"/></th>
		<th valign="top" class="crud-table-header-row"><cms:contentText code="promotion.payout" key="DATES_ELIGIBLE"/></th>		
      </tr>
      
          <c:set var="switchColor" value="false"/>
		    <nested:iterate id="promoPayoutGroup" name="promotion" property="promotionPayoutGroups">
		      <c:choose>
		        <c:when test="${switchColor == 'false'}">
			      <tr class="crud-table-row1">
			      <c:set var="switchColor" scope="page" value="true"/>
			    </c:when>
			    <c:otherwise>
			      <tr class="crud-table-row2">
			      <c:set var="switchColor" scope="page" value="false"/>
			    </c:otherwise>
		      </c:choose>
		      <nested:iterate id="promoPayoutValue" property="promotionPayouts">
   		    
	   		    <c:choose>
			      <c:when test="${promoPayoutValue.productCategory != null &&
			                      promoPayoutValue.productCategory.parentProductCategory != null}">
			        <td class="content-field left-align"><c:out value="${promoPayoutValue.productCategory.parentProductCategory.name}"/></td>
			        <td class="content-field left-align"><c:out value="${promoPayoutValue.productCategory.name}"/></td>
			        <td class="content-field left-align"><cms:contentText code="promotion.payout" key="ALL"/></td>
			      </c:when>
			      <c:when test="${promoPayoutValue.productCategory != null &&
			                      promoPayoutValue.productCategory.parentProductCategory == null}">
			        <td class="content-field left-align"><c:out value="${promoPayoutValue.productCategory.name}"/></td>
			        <td class="content-field left-align"><cms:contentText code="promotion.payout" key="ALL"/></td>
			        <td class="content-field left-align"><cms:contentText code="promotion.payout" key="ALL"/></td>
	              </c:when>
	              <c:otherwise>
	                <c:choose>
	                  <c:when test="${promoPayoutValue.product.productCategory.parentProductCategory != null}">
	                    <td class="content-field left-align"><c:out value="${promoPayoutValue.product.productCategory.parentProductCategory.name}"/></td>
			            <td class="content-field left-align"><c:out value="${promoPayoutValue.product.productCategory.name}"/></td>
			            <td class="content-field left-align"><c:out value="${promoPayoutValue.product.name}"/></td>
			          </c:when>
			          <c:otherwise>
			            <td class="content-field left-align"><c:out value="${promoPayoutValue.product.productCategory.name}"/></td>
			            <td class="content-field left-align">&nbsp;</td>
			            <td class="content-field left-align"><c:out value="${promoPayoutValue.product.name}"/></td>
			          </c:otherwise>
			        </c:choose>
			      </c:otherwise>
			    </c:choose>
			    
   		        <td align="left" nowrap>
			      <nested:write property="displayStartDate"/>
			      <c:if test="${promoPayoutValue.displayEndDate != ''}">
			        &nbsp;-&nbsp;
			        <nested:write property="displayEndDate"/>
	     	      </c:if>
			    </td>  		    		 
		   	  </nested:iterate>
		    </tr>
		  </nested:iterate>
	</table>
  </td>
</tr>


	<tr class="form-row-spacer">				  
          <beacon:label property="stackRankFactorType" required="true">
            <cms:contentText key="RANK_BASED_ON" code="promotion.payout"/>
          </beacon:label>	
          <td class="content-field">
            <c:out value="${promotion.stackRankFactorType.name}"/>
          </td>
     </tr>

 <tr class="form-row-spacer">
  	<beacon:label property="displayStackRankFactor" required="true" styleClass="content-field-label-top">
          <cms:contentText key="DISPLAY_RANK_FACTOR" code="promotion.payout"/>
        </beacon:label>	
        <td class="content-field">		        
	      <c:choose>
	        <c:when test="${promotion.displayStackRankFactor == 'true'}">
		      <cms:contentText key="YES" code="system.common.labels"/>
		    </c:when>
		    <c:otherwise>
		      <cms:contentText key="NO" code="system.common.labels"/>
		    </c:otherwise>
		  </c:choose>
	    </td>
 </tr>
 
  <tr class="form-row-spacer">
  		<beacon:label property="stackRankApprovalType" required="true" styleClass="content-field-label-top">
          <cms:contentText key="MANUALLY_APPROVE_RANKINGS" code="promotion.payout"/>
        </beacon:label>
        <td class="content-field">		        
	      <c:choose>
	        <c:when test="${promotion.stackRankApprovalType == 'true'}">
		      <cms:contentText key="YES" code="system.common.labels"/>
		    </c:when>
		    <c:otherwise>
		      <cms:contentText key="NO" code="system.common.labels"/>
		    </c:otherwise>
		  </c:choose>
	    </td>	
 </tr>      
 
  <tr class="form-row-spacer">
  	<beacon:label property="displayFullListLinkToParticipants" required="true" styleClass="content-field-label-top">
          <cms:contentText key="FULL_LIST_AVAILABLE" code="promotion.payout"/>
        </beacon:label>	
              <td class="content-field">		        
	      <c:choose>
	        <c:when test="${promotion.stackRankApprovalType == 'true'}">
		      <cms:contentText key="ALL_PARTICIPANTS" code="promotion.payout"/>
		    </c:when>
		    <c:otherwise>
		      <cms:contentText key="MANAGERS_ONLY" code="promotion.payout"/>
		    </c:otherwise>
		  </c:choose>
	    </td>	

 </tr>
            	    