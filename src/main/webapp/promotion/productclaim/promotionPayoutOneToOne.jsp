<%--UI REFACTORED--%>
<%--
UI REFACTORING Note: Header and footer pages needs refactoring if necessary. 
As this doen't comes under any of our standard layouts and is specific to this page, changed the content wherever necessary as per refactoring requirements.
--%>

<%@ include file="/include/taglib.jspf"%>
 <%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

<%    
  int calendarCounter = 0;
  int divCount = 0;
%>

<tr class="form-row-spacer">
 	<beacon:label property="payoutType" required="true" styleClass="content-field-label-top">
		<cms:contentText key="PRODUCTS" code="promotion.payout"/>
	</beacon:label>
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
        <c:if test="${promotionPayoutForm.teamUsed == 'true' }" >
		  <th valign="top" class="crud-table-header-row"><cms:contentText code="promotion.payout" key="TEAM_MEMBER"/><br>
	                                                     <cms:contentText code="promotion.payout" key="PAYOUT"/></th>
	    </c:if>
		  <th valign="top" class="crud-table-header-row"><cms:contentText code="system.general" key="CRUD_REMOVE_LABEL"/></th>
      </tr>
      <c:choose>
	    <c:when test="${promotionPayoutForm.promoPayoutValueListCount == '0'}">
	      <tr>
            <td colspan="6" class="content-field left-align"><cms:contentText code="promotion.payout" key="NOTHING_FOUND"/></td>
	    </c:when>

	    <c:otherwise>
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
   		      <%
				  String startDateCalendarCounter="productOrCategoryStartDate" +calendarCounter;
				  String endDateCalendarCounter="productOrCategoryEndDate" +calendarCounter;
	    	  %>
		        <nested:hidden property="../promoPayoutGroupId"/>
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
		        <td class="content-field left-align"><c:out value="${promoPayoutValue.categoryName}"/></td>
		        <td class="content-field left-align">
		   		<c:choose>
			        <c:when test="${empty promoPayoutValue.subcategoryName}">
		                   <cms:contentText key="ALL" code="promotion.payout"/>
		            </c:when>
		           	<c:otherwise>
		                   <c:out value="${promoPayoutValue.subcategoryName}"/>
		             </c:otherwise>
                </c:choose>
		        </td> 
		        <td class="content-field left-align">
				<c:choose>
			        <c:when test="${empty promoPayoutValue.productName}">
		                   <cms:contentText key="ALL" code="promotion.payout"/>
		            </c:when>
		           	<c:otherwise>
		                   <c:out value="${promoPayoutValue.productName}"/>
		             </c:otherwise>
                </c:choose>
		        </td>
            <td align="right" nowrap>
              <table>
                <tr>
                  <td class="content-field left-align">
                    <cms:contentText key="FROM" code="system.general"/>
                  </td>
                  <td class="content-field right-align">
                    <nested:text property="productOrCategoryStartDate" styleId="<%=startDateCalendarCounter%>" size="10" maxlength="10" readonly="true" styleClass="content-field" onfocus="clearDateMask(this);" onchange="syncFields( this, 'start' );"/>
                    <img alt="" id="productOrCategoryStartDateTrigger<%=calendarCounter%>" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" />
                  </td>
                </tr>
                <tr>
                  <td class="content-field left-align">
                    <cms:contentText key="TO" code="system.general"/>
                  </td>
                  <td class="content-field right-align">                    
                    <nested:text property="productOrCategoryEndDate" styleId="<%=endDateCalendarCounter%>" size="10" maxlength="10" readonly="true" styleClass="content-field" onfocus="clearDateMask(this);" onchange="syncFields( this, 'end' );"/>
                    <img alt="" id="productOrCategoryEndDateTrigger<%=calendarCounter%>" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" />
                  </td>
                </tr>
              </table>                
			      </td>


					<script type="text/javascript">
			    		Calendar.setup({
					        inputField      :    "productOrCategoryStartDate<%=calendarCounter%>",     // id of the input field
					        ifFormat        :    "${TinyMceDatePattern}",      // format of the input field
					        button          :    "productOrCategoryStartDateTrigger<%=calendarCounter%>"  // trigger for the calendar (button ID)
					    });
					    Calendar.setup({
					        inputField      :    "productOrCategoryEndDate<%=calendarCounter%>",     // id of the input field
					        ifFormat        :    "${TinyMceDatePattern}",      // format of the input field
					        button          :    "productOrCategoryEndDateTrigger<%=calendarCounter%>"  // trigger for the calendar (button ID)
					    });
					</script>
				
				<c:choose>
              <c:when
                test="${(promoPayoutValue.promoPayoutId != null) &&
				               	  (promoPayoutValue.promoPayoutId != '0') &&
				              	  (promotionPayoutForm.promotionStatus == 'live')}">
                <td class="content-field center-align"><nested:hidden property="../minimumQualifier" /> <nested:write
                  property="../minimumQualifier" /></td>
                <td class="content-field center-align"><nested:hidden property="../retroPayout" /> <c:choose>
                  <c:when test="${promoPayoutGroup.retroPayout == 'true'}">
                    <cms:contentText key="YES" code="system.common.labels" />
                  </c:when>
                  <c:otherwise>
                    <cms:contentText key="NO" code="system.common.labels" />
                  </c:otherwise>
                </c:choose></td>
                
                <td class="content-field center-align">
                  <table> 
                    <tr>
                      <td class="content-field left-align">
                        <c:out value="${promotionPayoutForm.awardType}"/>
                      </td>
                      <td class="content-field right-align">
                        <nested:hidden property="../submitterParentPayout"/>
                        <nested:write property="../submitterParentPayout"/>
                      </td>
                    </tr>
                  </table>
                </td>
                <c:if test="${promotionPayoutForm.teamUsed == 'true' }" >  
                  <td class="content-field center-align">
                    <table> 
                      <tr>
                        <td class="content-field left-align">
                          <c:out value="${promotionPayoutForm.awardType}"/>
                        </td>
                        <td class="content-field right-align">
                          <nested:hidden property="../teamMemberParentPayout"/>
                          <nested:write property="../teamMemberParentPayout"/>
                        </td>
                      </tr>
                    </table>
                  </td>
                </c:if>
              </c:when>
              <c:otherwise>
			        <td class="content-field center-align">
				      <nested:text property="../minimumQualifier" size="5" maxlength="10" styleClass="content-field" />
				    </td>
				    <td class="content-field center-align">
				      <nested:select property="../retroPayout" styleClass="content-field">
				        <html:option value='true'><cms:contentText key="YES" code="system.common.labels"/></html:option>
  				        <html:option value='false'><cms:contentText key="NO" code="system.common.labels"/></html:option>				        
				      </nested:select>
				    </td>
				    <td class="content-field center-align">
            
                <table>
                  <tr>
                    <td class="content-field left-align">
                      <c:out value="${promotionPayoutForm.awardType}"/>
                    </td>
                    <td class="content-field right-align">
                      <nested:text property="../submitterParentPayout" size="5" maxlength="10" styleClass="content-field"/>
                    </td>
                  </tr>
                </table>
				    </td>
				    <c:if test="${promotionPayoutForm.teamUsed == 'true' }" >
				      <td class="content-field center-align">
                  <table>
                    <tr>
                      <td class="content-field left-align">
                        <c:out value="${promotionPayoutForm.awardType}"/>
                      </td>
                      <td class="content-field right-align">
                        <nested:text property="../teamMemberParentPayout" size="5" maxlength="10" styleClass="content-field"/>
                      </td>
                    </tr>
                  </table>
				      </td>
				    </c:if>
			      </c:otherwise>
			    </c:choose>
				<td class="content-field center-align">
			   	  <c:if test="${(promoPayoutValue.promoPayoutId == null) ||
			                    (promoPayoutValue.promoPayoutId == '0') ||
			                    (promotionPayoutForm.promotionStatus != 'live')}">
			        <nested:checkbox property="removePayout" value="Y"/>
               <c:set var="hasRemoveablePayout" value="true"/>
			      </c:if>
		        </td>
		        <%
				  calendarCounter++;
		   		divCount++;
				%>
		   	  </nested:iterate>
		    </tr>
		  </nested:iterate>
		</c:otherwise>
      </c:choose>
	</table>
  </td>
</tr>

<%-- We are not using form-buttonrow class here as we dont need much space --%>
<tr class="form-row-spacer">
  <td colspan="2">&nbsp;</td>
  <td colspan="2">
    <html:button property="add_category" styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('promotionPayoutLookup.do?returnUrl==/promotionProductClaim/promotionPayout.do?method=returnCategoryLookup&payoutType=one_to_one','prepareCategoryLookup')" >
      <cms:contentText code="promotion.payout" key="ADD_CATEGORY" />
    </html:button>
    &nbsp;&nbsp;
    <html:button property="add_product" styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('promotionPayoutLookup.do?returnUrl==/promotionProductClaim/promotionPayout.do?method=returnProductLookup&payoutType=one_to_one','prepareProductLookup')" >
      <cms:contentText code="promotion.payout" key="ADD_PRODUCT" />
    </html:button>
  </td>
    <td align="right">
      <c:if test="${hasRemoveablePayout}">
        <html:button property="remove" styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('promotionPayout.do', 'removePayouts')" >
          <cms:contentText code="system.button" key="REMOVE_SELECTED" />
        </html:button>
      </c:if>
    </td>
</tr>
