<%--UI REFACTORED--%>
<%--
UI REFACTORING Note: Header and footer pages needs refactoring if necessary. 
As this doen't comes under any of our standard layouts and is specific to this page, changed the content wherever necessary as per refactoring requirements.
--%>

<%@ include file="/include/taglib.jspf"%>


<html:hidden property="hasParent"/>
<tr class="form-row-spacer">
 	<beacon:label property="payoutType" required="true" styleClass="content-field-label-top">
		<cms:contentText key="PRODUCTS" code="promotion.payout"/>
	</beacon:label>
  <td colspan="6">
    <table class="crud-table" width="100%">
      <tr>
        <th valign="top" class="crud-table-header-row"><cms:contentText code="promotion.payout" key="INCLUDE"/></th>
        <th valign="top" class="crud-table-header-row"><cms:contentText code="promotion.payout" key="CATEGORY"/></th>
		<th valign="top" class="crud-table-header-row"><cms:contentText code="promotion.payout" key="SUBCATEGORY"/></th>
		<th valign="top" class="crud-table-header-row"><cms:contentText code="promotion.payout" key="PRODUCT"/></th>
		<th valign="top" class="crud-table-header-row"><cms:contentText code="promotion.payout" key="DATES_ELIGIBLE"/></th>		
      </tr>
    
          <c:set var="switchColor" value="false"/>
		    <nested:iterate id="promoPayoutGroup" name="promotionPayoutForm" property="promoPayoutGroupValueList">
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
		      <nested:iterate id="promoPayoutValue" property="promoPayoutValueList">
   		
		        <nested:hidden property="../promoPayoutGroupId"/>
		        <nested:hidden property="../parentPromoPayoutGroupId"/>
		        <nested:hidden property="../guid"/>
		        <nested:hidden property="promoPayoutId"/>
		        <nested:hidden property="version"/>
		        <nested:hidden property="createdBy"/>
		        <nested:hidden property="dateCreated"/>
		   	    <nested:hidden property="categoryId"/>
		        <nested:hidden property="categoryName"/>
		        <nested:hidden property="subcategoryId"/>
		        <nested:hidden property="subcategoryName"/>
		        <nested:hidden property="productId"/>
		        <nested:hidden property="productName"/>
		        <nested:hidden property="productOrCategoryStartDate"/>
      	  		<nested:hidden property="productOrCategoryEndDate"/>
		           <td class="content-field center-align">
        	<c:choose>
        	  <c:when test="${(promoPayoutValue.promoPayoutId != null) &&
	                     	  (promoPayoutValue.promoPayoutId != '0') &&
	                 	      (promotionPayoutForm.promotionStatus == 'live')}">
	      	    <nested:checkbox property="includePayout" disabled="true"/>
	      		<nested:hidden property="includePayout"/>
	    	  </c:when>
	    	  <c:otherwise>	
          	    <nested:checkbox property="includePayout"/>
	    	  </c:otherwise>
	    	</c:choose>
	      </td>
		        <td class="content-field left-align"><c:out value="${promoPayoutValue.categoryName}"/></td>
		        <td class="content-field left-align"><c:out value="${promoPayoutValue.subcategoryName}"/></td>
		        <td class="content-field left-align"><c:out value="${promoPayoutValue.productName}"/></td>
		         <td align="left" nowrap>
				    <nested:write property="productOrCategoryStartDate"/>
				    <c:if test="${promoPayoutValue.productOrCategoryEndDate != ''}">
				      &nbsp;-&nbsp;
				      <nested:write property="productOrCategoryEndDate"/>
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
            <c:if test="${not empty rankBasedList}">
             <html:select property="stackRankFactorType" styleClass="content-field" >
           		<html:option value="quantitySold"><cms:contentText key="QUANTITY_SOLD" code="promotion.payout"/></html:option>	
            	<html:options collection="rankBasedList" property="id" labelProperty="displayLabel" />
             </html:select>
            </c:if>
             <c:if test="${empty rankBasedList}">
            	 <nested:hidden property="stackRankFactorType" value="quantitySold"/>
             	 <cms:contentText key="QUANTITY_SOLD" code="promotion.payout"/>     
            </c:if>      
          </td>
     </tr>

 <tr class="form-row-spacer">
  	<beacon:label property="displayStackRankFactor" required="true" styleClass="content-field-label-top">
          <cms:contentText key="DISPLAY_RANK_FACTOR" code="promotion.payout"/>
        </beacon:label>	
	<td class="content-field-label">
 		<html:radio property="displayStackRankFactor" value="false"/>&nbsp;<cms:contentText key="NO" code="system.common.labels"/>
	</td>
 </tr>
 <tr class="form-row-spacer">
		<td colspan="2">&nbsp;</td>
		<td class="content-field-label">
  		<html:radio property="displayStackRankFactor" value="true"/>&nbsp;<cms:contentText key="YES" code="system.common.labels"/>
		</td>
 </tr>  
 
  <tr class="form-row-spacer">
  	<beacon:label property="stackRankApprovalType" required="true" styleClass="content-field-label-top">
          <cms:contentText key="MANUALLY_APPROVE_RANKINGS" code="promotion.payout"/>
        </beacon:label>	
	<td class="content-field-label">
 		<html:radio property="stackRankApprovalType" value="false"/>&nbsp;<cms:contentText key="NO" code="system.common.labels"/>
	</td>
 </tr>
 <tr class="form-row-spacer">
		<td colspan="2">&nbsp;</td>
		<td class="content-field-label">
  		<html:radio property="stackRankApprovalType" value="true"/>&nbsp;<cms:contentText key="YES" code="system.common.labels"/>
		</td>
 </tr>        
 
  <tr class="form-row-spacer">
  	<beacon:label property="displayFullListLinkToParticipants" required="true" styleClass="content-field-label-top">
          <cms:contentText key="FULL_LIST_AVAILABLE" code="promotion.payout"/>
        </beacon:label>	
	<td class="content-field-label">
 		<html:radio property="displayFullListLinkToParticipants" value="true"/>&nbsp;<cms:contentText key="ALL_PARTICIPANTS" code="promotion.payout"/>
	</td>
 </tr>
 <tr class="form-row-spacer">
		<td colspan="2">&nbsp;</td>
		<td class="content-field-label">
  		<html:radio property="displayFullListLinkToParticipants" value="false"/>&nbsp;<cms:contentText key="MANAGERS_ONLY" code="promotion.payout"/>
		</td>
 </tr>              	    