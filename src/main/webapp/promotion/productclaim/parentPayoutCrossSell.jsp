<%--UI REFACTORED--%>
<%--
UI REFACTORING Note: Header and footer pages needs refactoring if necessary. 
As this doen't comes under any of our standard layouts and is specific to this page, changed the content wherever necessary as per refactoring requirements.
--%>

<%@ include file="/include/taglib.jspf"%>
 
<c:set var="groupCounter" value="${0}"/>
	
<nested:iterate id="promoPayoutGroup" name="promotion" property="promotionPayoutGroups">   
  <tr class="form-row-spacer">
    <td></td>
    <td class="content-field-label" valign="top">
	  <cms:contentText key="GROUP" code="promotion.payout"/>
	  <c:out value="${groupCounter + 1}"/>
	</td>	
	<td colspan="6">
	  <table class="crud-table" width="100%">
		<tr>
      	  <th class="crud-table-header-row"><cms:contentText code="promotion.payout" key="CATEGORY"/></th>
		  <th class="crud-table-header-row"><cms:contentText code="promotion.payout" key="SUBCATEGORY"/></th>
		  <th class="crud-table-header-row"><cms:contentText code="promotion.payout" key="PRODUCT"/></th>
  		  <th class="crud-table-header-row"><cms:contentText code="promotion.payout" key="QUANTITY"/></th>		 
	      <th class="crud-table-header-row"><cms:contentText code="promotion.payout" key="DATES_ELIGIBLE"/></th>		    
          <th valign="top" class="crud-table-header-row"><cms:contentText code="promotion.payout" key="SUBMITTER"/><br>
	                                                     <cms:contentText code="promotion.payout" key="PAYOUT"/></th>
          <c:if test="${promotionPayoutForm.teamUsed == 'true' }" >
		    <th class="crud-table-header-row"><cms:contentText code="promotion.payout" key="TEAM_MEMBER"/><br>
		                                        <cms:contentText code="promotion.payout" key="PAYOUT"/></th>
		  </c:if>
		</tr>
	
		<c:set var="promoPayoutCounter" value="${0}"/>
		<c:set var="switchColor" value="false"/>
	    <nested:iterate id="promoPayoutValue" property="promotionPayouts">
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
		  <td class="content-field center-align">
		    <nested:write property="quantity"/>
		  </td>
		  <td align="left" nowrap>
		    <nested:write property="displayStartDate"/>
		    <c:if test="${promoPayoutValue.displayEndDate != ''}">
		        &nbsp;-&nbsp;
		        <nested:write property="displayEndDate"/>
     	    </c:if>
		  </td>
		  <c:if test="${promoPayoutCounter == '0'}">
			<td rowspan="<c:out value="${promoPayoutGroup.promotionPayoutsCount}"/>" valign="top" align="center">
        <table> 
          <tr>
            <td class="content-field left-align">
              <c:out value="${promotion.awardType.name}"/>
            </td>
            <td class="content-field right-align">
              <nested:write property="../submitterPayout"/>
            </td>
          </tr>
        </table>			  
			</td>
	        <c:if test="${promotionPayoutForm.teamUsed == 'true' }" >
	          <td rowspan="<c:out value="${promoPayoutGroup.promotionPayoutsCount}"/>" valign="top" align="center">
          <table> 
            <tr>
              <td class="content-field left-align">
                <c:out value="${promotion.awardType.name}"/>
              </td>
              <td class="content-field right-align">
                <nested:write property="../teamMemberPayout"/>
              </td>
            </tr>
          </table>        
			  </td>
		    </c:if>
		  </c:if>
		  
		  <c:set var="promoPayoutCounter" value="${promoPayoutCounter + 1}"/>
		</tr>
        </nested:iterate>
      </table>
    </td>
  </tr>
  <c:set var="groupCounter" value="${groupCounter + 1}"/>  
</nested:iterate>