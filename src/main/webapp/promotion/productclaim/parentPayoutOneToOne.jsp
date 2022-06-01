<%--UI REFACTORED--%>
<%--
UI REFACTORING Note: Header and footer pages needs refactoring if necessary. 
As this doen't comes under any of our standard layouts and is specific to this page, changed the content wherever necessary as per refactoring requirements.
--%>

<%@ include file="/include/taglib.jspf"%>

<tr class="form-row-spacer">
  <td></td>
  <td class="content-field-label" valign="top">
    <cms:contentText key="PRODUCTS" code="promotion.payout"/>
  </td>
  <td colspan="6">
    <table class="crud-table" width="100%">
      <tr>
        <th valign="top" class="crud-table-header-row"><cms:contentText code="promotion.payout" key="CATEGORY"/></th>
		<th valign="top" class="crud-table-header-row"><cms:contentText code="promotion.payout" key="SUBCATEGORY"/></th>
		<th valign="top" class="crud-table-header-row"><cms:contentText code="promotion.payout" key="PRODUCT"/></th>
	    <th valign="top" class="crud-table-header-row"><cms:contentText code="promotion.payout" key="DATES_ELIGIBLE"/></th>		
        <th valign="top" class="crud-table-header-row"><cms:contentText code="promotion.payout" key="MINIMUM"/><br>
    												   <cms:contentText code="promotion.payout" key="QUALIFIER"/></th>
	    <th valign="top" class="crud-table-header-row"><cms:contentText code="promotion.payout" key="RETRO"/><br>
	                                                   <cms:contentText code="promotion.payout" key="PAYOUT"/>?</th>
		<th valign="top" class="crud-table-header-row"><cms:contentText code="promotion.payout" key="SUBMITTER"/><br>
	                                                   <cms:contentText code="promotion.payout" key="PAYOUT"/></th>
        <c:if test="${promotion.teamUsed == 'true' }" >
		  <th valign="top" class="crud-table-header-row"><cms:contentText code="promotion.payout" key="TEAM_MEMBER"/><br>
	                                                     <cms:contentText code="promotion.payout" key="PAYOUT"/></th>
	    </c:if>
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
		    <td class="content-field center-align">		        
			  <nested:write property="../minimumQualifier"/>
		    </td>
		    <td class="content-field center-align">		        
		      <c:choose>
		        <c:when test="${promoPayoutGroup.retroPayout == 'true'}">
			      <cms:contentText key="YES" code="system.common.labels"/>
			    </c:when>
			    <c:otherwise>
			      <cms:contentText key="NO" code="system.common.labels"/>
			    </c:otherwise>
			  </c:choose>
		    </td>
		    <td class="content-field center-align">
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
			<c:if test="${promotion.teamUsed == 'true' }" >
			  <td class="content-field center-align">
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
		  </nested:iterate>
		</tr>
	  </nested:iterate>
	</table>
  </td>
</tr>