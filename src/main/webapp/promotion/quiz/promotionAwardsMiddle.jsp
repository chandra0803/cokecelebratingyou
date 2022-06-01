<%--UI REFACTORED--%>
<%--
UI REFACTORING Note: Header and footer pages needs refactoring if necessary. 
--%>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.ui.promotion.PromotionWizardManager"%>

<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-en.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-setup.js"></script>

<html:hidden property="budgetSegmentVBListSize"/>

<%-- Code Fix for bug 18215 --%>
<beacon:client-state>  	 
  	<beacon:client-state-entry name="hiddenEditbdgtMstrId" value="${promotionAwardsForm.budgetMasterId}"/>
</beacon:client-state>
<%-- Award Amount --%>
<tr class="form-row-spacer" >
  <beacon:label property="awardAmountTypeFixed" required="true" styleClass="content-field-label-top">
    <cms:contentText key="AMOUNT" code="promotion.awards" />
  </beacon:label>
  <td class="content-field" valign="top">
    <html:text styleId="fixedAmount" property="fixedAmount" size="5"
        styleClass="content-field" disabled="${displayFlag}" />
  </td>
</tr>
<tr class="form-blank-row">
<td colspan="3"> </td>
</tr>
<tr class="form-row-spacer" id="budgetInfo" >
  <beacon:label property="budgetOption" required="true" styleClass="content-field-label-top">
    <cms:contentText key="BREAK_BANK_BUDGET" code="promotion.awards" />
  </beacon:label>
  <td>
    <table> <%--  radio buttons table --%>
      <tr>
        <td class="content-field" nowrap valign="top">
          <html:radio styleId="budgetOptionNone" property="budgetOption" value="none" disabled="${displayFlag}" onclick="hideLayer('newBudget');hideLayer('finalPayout');" />
          <cms:contentText code="promotion.awards" key="BUDGET_NO" />
        </td>
      </tr>
	   
      <tr>
        <td colspan="2" class="content-field" nowrap valign="top">
			<c:if test="${ promoStatus == true &&( promotionAwardsForm.budgetOption == 'existing' || promotionAwardsForm.budgetOption == 'none') }">
			<html:hidden property="budgetOption" value="${promotionAwardsForm.budgetOption}"/>
			<html:hidden property="budgetMasterId" value="${promotionAwardsForm.budgetMasterId}"/>
			</c:if>	        	
          <html:radio styleId="budgetOptionExists" property="budgetOption" value="existing" disabled="${displayFlag}" onclick="hideLayer('newBudget');hideLayer('finalPayout');" />
          <cms:contentText code="promotion.awards" key="BUDGET_EXISTING_CENTRAL" /> &nbsp;
          <html:select styleId="budgetMasterId" property="budgetMasterId" styleClass="content-field" disabled="${displayFlag}">
            <html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general" /></html:option>
            <html:options collection="budgetMasterList" property="budgetMasterId" labelProperty="budgetMasterName" />
          </html:select>
        </td>
      </tr>

      <tr>
        <td class="content-field" nowrap valign="top">
          <html:radio styleId="budgetOptionNew" property="budgetOption" value="new" disabled="${displayFlag}" onclick="showLayer('newBudget');showLayer('finalPayout');updateLayerRemoveBudgetSegment();"/>
          <cms:contentText code="promotion.awards" key="BUDGET_CREATE_CENTRAL" />
        </td>
      </tr>
      <%--  new budget row  --%>
      <tr>
        <td>
          <table id="newBudget"><%--  new budget fields --%>
            <tr>
              <td></td>
              <td>
                <table>
                  <tr class="form-row-spacer" style="float:left;">
                    <beacon:label property="budgetMasterName" required="true">
                      <cms:contentText key="BUDGET_MASTER_NAME" code="promotion.awards" />
                    </beacon:label>
                    <td  class="content-field-label">
                      <table>
                        <tr>
                          <td class="content-field" valign="top">
                            <html:text styleId="budgetMasterName" property="budgetMasterName" size="20" maxlength="30" styleClass="content-field" disabled="${displayFlag}" />
                          </td>
                        </tr>
                      </table>
                    </td>
                  </tr>
					<tr class="form-blank-row">
					<td colspan="3"> </td>
					</tr>
                  <%-- Budget Type --%>
                  <tr class="form-row-spacer" style="float:left;">
                    <beacon:label property="budgetType" required="true" styleClass="content-field-label-top">
                      <cms:contentText key="BUDGET_TYPE" code="promotion.awards" />
                    </beacon:label>
                    <td  class="content-field-label">
                      <table>
                        <tr>
                          <td class="content-field" valign="top">
                            <html:hidden property="budgetType" value="central" disabled="${displayFlag}" />
                            <cms:contentText code="promotion.awards" key="BUDGET_TYPE_CENTRAL" />
                          </td>
                        </tr>
                      </table>
                    </td>
                  </tr>
				<tr class="form-blank-row">
					<td colspan="3"> </td>
					</tr>
                  <%-- Budget Cap Type --%>
                  <tr class="form-row-spacer" style="float:left;">
                    <beacon:label property="budgetCapType" required="true" styleClass="content-field-label-top">
                      <cms:contentText key="CAP_TYPE" code="promotion.awards" />
                    </beacon:label>

                    <td class="content-field-label">
                      <html:hidden property="budgetCapType" value="hard"/>
                      <c:out value="${hardCapBudgetType.name}"/>
                    </td>
                  </tr>
                  <tr class="form-blank-row">
					<td colspan="3"> </td>
					</tr>
				  
				  <tr class="form-row-spacer" style="float:left;">
					<beacon:label property="budgetMasterStartDate" required="true" styleClass="content-field-label-top">
		              <cms:contentText key="BUDGET_MASTER_DATES" code="promotion.awards" />
		            </beacon:label> 
					<td class="content-field-label">
		              <table>	
		                 <tr>
		                  <beacon:label property="budgetMasterStartDate" required="true">
				  		    <cms:contentText key="START_DATE" code="admin.budgetmaster.details"/>
				  		  </beacon:label>
				  		  <td class="content-field"> 
				  		  <label for="budgetMasterStartDate" class="date">	
			  		      	<html:text property="budgetMasterStartDate" styleId="budgetMasterStartDate" size="10" maxlength="10" styleClass="text usedatepicker"/>
			  		       	<img src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" alt="<cms:contentText key='START' code='promotion.basics'/>"/>
			  		      </label>
		                  </td>
		                </tr>
		                <tr>
		                  <beacon:label property="budgetMasterEndDate">
		              		<cms:contentText key="END_DATE" code="admin.budgetmaster.details"/>
		            	  </beacon:label>	
				          <td class="content-field">
				          <label for="budgetMasterEndDate" class="date">		  		  	
				            <html:text property="budgetMasterEndDate" styleId="budgetMasterEndDate" size="10" maxlength="10" styleClass="text usedatepicker"/>
				            <img src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" alt="<cms:contentText key='END' code='promotion.basics'/>"/>
				          </label>
				          </td> 
		            	</tr>    
		              </table>
		            </td>
		          </tr>
		          
					<%-- *******Budget Segment start******* --%>
		          <tr class="form-row-spacer" style="float:left;">
		          <beacon:label property="segmentName" required="true" styleClass="content-field-label-top">
		              <cms:contentText key="BUDGET_SEGMENT" code="admin.budgetmaster.details" />
                    </beacon:label>
		          <td>
			          <table class="table table-striped table-bordered" width="120%">
					    <tr class="form-row-spacer">
					 		<th class="crud-table-header-row">
					 			<cms:contentText key="BUDGET_SEGMENT_NAME" code="admin.budgetmaster.details"/>
					 		</th> 
					 		<th class="crud-table-header-row">
					 			<cms:contentText key="START_DATE" code="admin.budgetmaster.details"/>
					 		</th>      			
					 		<th class="crud-table-header-row">
					 			<cms:contentText key="END_DATE" code="admin.budgetmaster.details"/>
					 		</th>  
					 		<th class="crud-table-header-row"  id="newBudgetCentralTitle">
					 			<cms:contentText key="BUDGET_AMOUNT" code="admin.budgetmaster.details"/>
					 		</th>
					 		<c:if test="${ promotionAwardsForm.budgetSegmentVBListSize ne '1'}">
					 		<th class="crud-table-header-row" id="removeBudgetSegment"><cms:contentText code="system.general" key="CRUD_REMOVE_LABEL"/></th>
					 		</c:if>  			 		
					    </tr>
						<c:set var="switchColor" value="false"/>
						<%		  	 	int sIndex = 0; %>
					  	<c:forEach var="budgetSegmentVBList" items="${promotionAwardsForm.budgetSegmentVBList}" varStatus="status" >
					  	<tr>
					  	<html:hidden property="id" name="budgetSegmentVBList" indexed="true"/> 
					  	
					  	<%
							String startDateCalendarCounter = "charStartDate" + sIndex;
							String endDateCalendarCounter = "charEndDate" + sIndex;
							String removeBudgetSegmentCounter = "removeBudgetSegment" + sIndex;
						%>	
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
						    <td class="crud-content">
						    	<html:text property="segmentName" size="50" maxlength="50" indexed="true" styleId="segmentName" name="budgetSegmentVBList" styleClass="content-field" />
					      	</td> 
					    	<td class="crud-content">
					    	<label for="<%=startDateCalendarCounter%>" class="date">
							    <html:text property="startDateStr" maxlength="10" size="10" indexed="true" name="budgetSegmentVBList" styleClass="text usedatepicker" styleId="<%=startDateCalendarCounter%>"/>
			               		<img alt="start date" src="<%= RequestUtils.getBaseURI(request) %>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon"/>
			                </label>
						    </td> 			      	
						    <td class="crud-content">
						    <label for="<%=endDateCalendarCounter%>" class="date">
						       	<html:text property="endDateStr" maxlength="10" size="10" indexed="true" name="budgetSegmentVBList" styleClass="text usedatepicker" styleId="<%=endDateCalendarCounter%>" />
			               		<img alt="end date" src="<%= RequestUtils.getBaseURI(request) %>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon"/>
			               	</label>
							</td>  
							<td class="crud-content">
						    	<html:text property="originalValue" size="10" maxlength="10" indexed="true" styleId="originalValue" name="budgetSegmentVBList" styleClass="content-field" />
							</td>
							<td class="crud-content">
				            	<table id="<%=removeBudgetSegmentCounter%>">
				            	<tr>
				            	<td align="right">
						        <html:button property="remove" styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('promotionAwards.do', 'removeBudgetSegment')" >
						          <cms:contentText code="system.button" key="REMOVE" />
						        </html:button>
						        </td>
						        </tr>
						        </table>
						    </td>							
		            		<% sIndex = sIndex + 1; %>
						    </tr>
						  </c:forEach>

						  <tr class="form-blank-row"><td></td></tr>
						    <tr class="form-row-spacer">
						        <td align="left" colspan="2">
					     	  		<a id="addAnotherhref"  href="#" onclick="addAnotherSegment('addAnotherSegment')" >
					       			  <cms:contentText code="admin.budgetmaster.details" key="ADD_ANOTHER" />
					     	  		</a> 
					        	</td>
						    </tr>	  
					</table>
                    </td>
                  </tr>
				        <%-- ********Budget Segment end****** --%>			          
				          
                </table> <%--  sub new budget table --%>
              </td>
            </tr>
          </table>  <%-- End newBudget --%>
        </td>
      </tr> <%--  end budget row --%>
    </table> <%--  radio buttons for budget --%>
  </td>
</tr>
<tr>
<td colspan="3">
<table id="finalPayout">
<tr class="form-row-spacer" id="finalPayoutRule_radio">
  <beacon:label property="finalPayoutRule" required="true" styleClass="content-field-label-top">
    <cms:contentText key="FINAL_PAYOUT_RULE" code="admin.budgetmaster.details"/>
  </beacon:label>
   <td nowrap class="content-field content-field-label-top">
     <c:forEach items="${budgetFinalPayoutRuleList}" var="budgetFinalPayoutRule">
       <html:radio property="finalPayoutRule" value="${budgetFinalPayoutRule.value}" onclick=""/>&nbsp;<c:out value="${budgetFinalPayoutRule.label}"/><br>
     </c:forEach>
   </td>
</tr> <%--  budgetInfo --%>
</table>
</td>
</tr>

<SCRIPT type="text/javascript">

function addAnotherSegment(method)
{
  document.promotionAwardsForm.method.value=method;
  document.promotionAwardsForm.action = "promotionAwards.do";
  document.promotionAwardsForm.submit();
  return false;
}
</script>

<script>
$( document ).ready(function() {
    $( "#budgetMasterStartDate" ).change(function() {
        var curDate = $(this).val();           
		$("#charStartDate0").val(curDate);  
});
    $( "#budgetMasterEndDate" ).change(function() {
        var curDate = $(this).val();           
		$("#charEndDate0").val(curDate);  
});
});
</script>