<%@ page import ="com.biperf.core.ui.promotion.PromotionPublicRecAddOnForm" %>
<%@ page import="com.biperf.core.ui.promotion.PromotionOverviewForm"%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.promotion.PromotionPublicRecognitionAudience" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.ui.promotion.PromotionWizardManager"%>
<%@ page import="com.biperf.core.ui.promotion.PromotionAudienceFormBean"%>

<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-en.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-setup.js"></script>

<%
	String actionNoValidation = "promotionPublicRecAddOn.do";     // default action without validation
	String actionDispatch;
	String displayFlag = "false";
%>
<c:if test="${promotionStatus == 'expired' }">
 <% displayFlag = "true"; %>
</c:if>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td colspan="3">
        <c:set var="promoTypeCode" scope="request" value="${promotionPublicRecAddOnForm.promotionTypeCode}" />
        <c:set var="promoTypeName" scope="request" value="${promotionPublicRecAddOnForm.promotionTypeName}" />
        <c:set var="promoName" scope="request" value="${promotionPublicRecAddOnForm.promotionName}" />
        <tiles:insert attribute="promotion.header" />
      </td>
    </tr>
    <cms:errors />
  </table>

<html:form styleId="contentForm" action="promotionPublicRecAddOnSave">
<html:hidden property="awardsType" styleId="awardsType" />
<html:hidden property="promotionId" styleId="promotionId" />
<html:hidden property="promotionType" styleId="promotionType" />
<html:hidden property="promotionTypeCode" styleId="promotionTypeCode" />
<html:hidden property="method" styleId="method" />
<html:hidden property="canRemoveAudience" styleId="canRemoveAudience"/>
<html:hidden property="version" styleId="version" />
<html:hidden property="hasParent" styleId="hasParent" />
<html:hidden property="live"  styleId="live"/>
<html:hidden property="allowPublicRecognition" styleId="allowPublicRecognition" />
<html:hidden property="promotionTypeName" styleId="promotionTypeName" />
<html:hidden property="promotionName" styleId="promotionName" />

  <beacon:client-state>
		<beacon:client-state-entry name="promotionId" value="${promotionPublicRecAddOnForm.promotionId}"/>
  </beacon:client-state>
 
  <table>
  <tr class="form-row-spacer">
          <beacon:label property="active" required="true">
            <cms:contentText key="PUBLIC_RECOGNITION_STATUS" code="promotion.public.recognition" />
          </beacon:label>
          <td class="content-field" valign="top" colspan="2"><html:radio styleId="allowPublicRecognitionPointsFalse" property="allowPublicRecognitionPoints" value="false"
            onclick="enableFields();" disabled="${displayFlag}"/>&nbsp;<cms:contentText key="IN_ACTIVE"
            code="promotion.public.recognition" /></td>
        </tr>
        <tr class="form-row-spacer">
          <td colspan="2">&nbsp;</td>
          <td class="content-field" valign="top" colspan="2"><html:radio styleId="allowPublicRecognitionPointsTrue" property="allowPublicRecognitionPoints" value="true"
            onclick="enableFields();" disabled="${displayFlag or promoTypeCode=='nomination'}"/>&nbsp;<cms:contentText key="ACTIVE"
            code="promotion.public.recognition" /></td>
        </tr>
        
        
        <tr class="form-row-spacer">
          <beacon:label property="audience" required="true" styleClass="content-field-label-top">
            <cms:contentText key="AUDIENCE" code="promotion.public.recognition" />
          </beacon:label>
          <td >
            <table width="100%">
               <tr>
                 <td class="content-field">
                    <html:radio styleId="audienceAllActivePax" property="audience" value="allactivepaxaudience"  onclick="hideLayer('submittersaudience');" 
                    disabled="${displayFlag}" /> &nbsp;<cms:contentText key="ALL_PAX" code="promotion.public.recognition" />
                 </td>
               </tr>
               <tr>
                 <td class="content-field">
                    <html:radio styleId="audiencePromoPublicRecg" property="audience" value="promopublicrecgaudience" onclick="showLayer('submittersaudience');" 
                    disabled="${displayFlag}" /> &nbsp;<cms:contentText key="CREATE_AUDIENCE" code="promotion.public.recognition" />
                 </td>
               </tr>
            </table>
          </td>     
        </tr>    
      
    <tr class="form-row-spacer">
      <td colspan="2"></td>
      <td>
        <DIV id="submittersaudience">
          <table>
            <tr>
              <td>
                <table class="crud-table" width="100%">
                  <tr>
                    <th valign="top" colspan="3" class="crud-table-header-row"><cms:contentText
                        key="AUDIENCE_LIST_LABEL" code="promotion.public.recognition" /> &nbsp;&nbsp;&nbsp;&nbsp; <html:select
                        property="audienceId" styleClass="content-field" disabled="${displayFlag}">
                        <html:options collection="availableAudiences" property="id" labelProperty="name" />
                      </html:select> 
                      
                      <html:button property="add_audience" styleClass="content-buttonstyle"
                        onclick="setActionDispatchAndSubmit('promotionPublicRecAddOn.do','addAudience');" disabled="${promotionStatus =='expired'}">
                        <cms:contentText key="ADD" code="system.button" />
                      </html:button>
                      
                        <br>
                      <cms:contentText key="CREATE_AUDIENCE_LABEL" code="promotion.public.recognition"/>
                      <a href="javascript:setActionDispatchAndSubmit('<%=actionNoValidation%>', 'preparePublicRecogAudienceLookup');" class="crud-content-link">
                        <cms:contentText key="LIST_BUILDER_LABEL" code="promotion.public.recognition"/>
                      </a>
                    </th>
                    <c:if test="${promotionPublicRecAddOnForm.canRemoveAudience}">
                      <th valign="top" class="crud-table-header-row"><cms:contentText code="promotion.public.recognition" key="CRUD_REMOVE_LABEL"/></th>
                    </c:if>
                  </tr>
                  <c:set var="switchColor" value="false"/>
              
             	 <nested:iterate id="promoPublicRecAudience" name="sessionPubliRecogAudienceList">
                      <c:choose>
                        <c:when test="${switchColor == 'false'}">
                          <tr class="crud-table-row1">
                            <c:set var="switchColor" scope="page" value="true" />
                        </c:when>
                        <c:otherwise>
                          <tr class="crud-table-row2">
                            <c:set var="switchColor" scope="page" value="false" />
                        </c:otherwise>
                      </c:choose>
                      <td class="crud-content"><c:out value="${promoPublicRecAudience.audience.name}" /></td>
                      <td class="crud-content">                       
                          <&nbsp;
                          <c:out value="${promoPublicRecAudience.audience.size}"/>
                          &nbsp;>                                                             
                      </td>
                      <td class="content-field">
												<%	Map parameterMap = new HashMap();
												    PromotionPublicRecognitionAudience temp = (PromotionPublicRecognitionAudience)pageContext.getAttribute( "promoPublicRecAudience" );
														parameterMap.put( "audienceId", temp.getAudience().getId() );
														pageContext.setAttribute("popupUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), "/promotion/promotionAudience.do?method=displayPaxListPopup", parameterMap, true ) );
												%>
                        <a href="javascript:popUpWin('<c:out value="${popupUrl}"/>', 'console', 750, 500, true, true);" class="crud-content-link">
													<cms:contentText key="VIEW_LIST" code="promotion.public.recognition" />
												</a>
											</td>
                      <td align="center" class="crud-content"><input type="checkbox"
                        name="deletePublicRecognitionAudience"
                        value="<c:out value="${promoPublicRecAudience.audience.name}"/>"></td>
                      
                    </nested:iterate>
              
                </table>
              </td>
            </tr>
             <tr>
                <td align="right">
                  <% actionDispatch = "setActionAndDispatch('" + actionNoValidation +"', 'removePublicRecognitionAudience')"; %>
                  <html:submit styleClass="content-buttonstyle" onclick="<%= actionDispatch%>"  disabled="<%=displayFlag%>">
                    <cms:contentText key="REMOVE" code="system.button"/>
                  </html:submit>
                </td>
             </tr>         
          </table>
        </DIV> <%-- end of submittersaudience DIV --%>
      </td>
    </tr>
    
    <%-- Award Amount --%>
    
    <SCRIPT TYPE="text/javascript">
	function disableAwardAmountType(disable)
	{
		if(disable == 'true')
		{
			if( document.getElementById('rangeAmountMin') != null )
			{
			 document.getElementById('rangeAmountMin').value='';
			}
			if( document.getElementById('rangeAmountMax') != null )
			{
			 document.getElementById('rangeAmountMax').value='';
			}
			if($("input:radio[name='awardAmountTypeFixed']:checked").val()== "false" )
				{
					$("input[id='fixedAmount']").val("");
				}
		}
	}
	</script>
    
	<tr class="form-row-spacer">
		<beacon:label property="awardAmountTypeFixed" required="true"
			styleClass="content-field-label-top">
			<cms:contentText key="AMOUNT" code="promotion.public.recognition" />
		</beacon:label>
		<td>
		<table>
			<tr>
		        <td class="content-field" valign="top"><html:radio styleId="awardAmountTypeFixedTrue" property="awardAmountTypeFixed" 
		        value="true" disabled="${displayFlag}" onclick="disableAwardAmountType('true');"/>
		          <cms:contentText code="promotion.awards" key="AMOUNT_FIXED" /> &nbsp;
		          <html:text styleId="fixedAmount" property="fixedAmount" size="5" styleClass="content-field" disabled="${displayFlag}" />
		        </td>
	        </tr>
	        
			<tr>
				<td class="content-field" valign="top"><html:radio
					styleId="awardAmountTypeFixedFalse" property="awardAmountTypeFixed"
					value="false" disabled="${displayFlag}" onclick="disableAwardAmountType('true');"/> <cms:contentText
					code="promotion.public.recognition" key="AMOUNT_RANGE" /> <cms:contentText
					code="promotion.public.recognition" key="AMOUNT_BETWEEN" /> &nbsp; <html:text
					styleId="rangeAmountMin" property="rangeAmountMin" size="5"
					styleClass="content-field" disabled="${displayFlag}" /> <cms:contentText
					code="promotion.public.recognition" key="AMOUNT_AND" />&nbsp; <html:text
					styleId="rangeAmountMax" property="rangeAmountMax" size="5"
					styleClass="content-field" disabled="${displayFlag}" /></td>
			</tr>
		</table>
	    </td>
    </tr>
			
	<tr class="form-row-spacer" id="budgetInfo">
	<beacon:label property="budgetOption" required="true"
		styleClass="content-field-label content-field-label-top">
		<cms:contentText key="HAS_BUDGET" code="promotion.public.recognition" />
	</beacon:label>
	<td>
	<table>
		<%--  radio buttons table --%>
		<html:hidden property="hiddenBudgetMasterId" styleId="hiddenBudgetMasterId"/>
		<html:hidden property="budgetSegmentVBListSize"/>
		<tr>
			<td class="content-field" nowrap valign="top"><html:radio
				styleId="budgetOptionNone" property="budgetOption" value="none"
				disabled="${displayFlag}"
				onclick="updateLayersShown();" />
			<cms:contentText code="promotion.public.recognition" key="NO_BUDGET" /></td>
		</tr>

		<tr>
			<td class="content-field" nowrap valign="top"><html:radio
				styleId="budgetOptionExists" property="budgetOption"
				value="existing" disabled="${displayFlag}"
				onclick="updateLayersShown();" />
			<cms:contentText code="promotion.public.recognition" key="BUDGET_EXISTING" />
			&nbsp; <html:select styleId="budgetMasterId"
				property="budgetMasterId" styleClass="content-field"
				disabled="${displayFlag}" >
				<html:option value=''>
					<cms:contentText key="CHOOSE_ONE" code="system.general" />
				</html:option>
				<html:options collection="budgetMasterList"
					property="budgetMasterId" labelProperty="budgetMasterName" /> 
			</html:select></td>
		</tr>

		 <tr>
			<td class="content-field" nowrap valign="top">
					<html:radio styleId="budgetOptionNew" property="budgetOption"
						value="new" disabled="${displayFlag}"
						onclick="updateLayersShown();" />
			<cms:contentText code="promotion.public.recognition" key="CREATE_BUDGET" />
			</td>
		</tr>

		<tr>
	      <td>
			<table id="newBudget">
				<tr>
					<td>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
					<td>
					<table>
						<tr class="form-row-spacer">
							<beacon:label property="budgetMasterName" required="true">
								<cms:contentText key="BUDGET_MASTER_NAME"
									code="promotion.public.recognition" />
							</beacon:label>
							<td class="content-field-label">
							<table>
								<tr>
									<td class="content-field" valign="top"><html:text
										styleId="budgetMasterName" property="budgetMasterName" maxlength="30"
										size="20" styleClass="content-field" disabled="${displayFlag}" /></td>
								</tr>
							</table>
							</td>
						</tr>
						<tr class="form-row-spacer"  id="budgetType">
							<beacon:label property="budgetType" required="true"
								styleClass="content-field-label-top">
								<cms:contentText key="BUDGET_TYPE" code="promotion.public.recognition" />
							</beacon:label>
							<td class="content-field-label">
							<table>
								<tr>
									<td class="content-field" valign="top"><c:choose>
										<c:when
											test="${promotionPublicRecAddOnForm.awardsType == 'merchandise' }">
											<html:radio styleId="budgetTypePax" property="budgetType"
												value="pax" disabled="${displayFlag}"
												onclick="updateLayersShown();" />
										</c:when>
										<c:otherwise>
											<html:radio styleId="budgetTypePax" property="budgetType"
												value="pax" disabled="${displayFlag}"
												onclick="updateLayersShown();" />
										</c:otherwise>
									</c:choose> <cms:contentText code="promotion.public.recognition" key="BUDGET_TYPE_PAX" /></td>
								</tr>

								<tr>
									<td class="content-field" valign="top">
											<html:radio styleId="budgetTypeNode" property="budgetType"
												value="node" disabled="${displayFlag}"
												onclick="updateLayersShown();" />
										<cms:contentText code="promotion.public.recognition"
										key="BUDGET_TYPE_NODE" /></td>
								</tr>
								<tr>
									<td class="content-field" valign="top"><c:choose>
										<c:when
											test="${promotionPublicRecAddOnForm.awardsType == 'merchandise' }">
											<html:radio styleId="budgetTypeCentral" property="budgetType"
												value="central" disabled="${displayFlag}"
												onclick="updateLayersShown();" />
										</c:when>
										<c:otherwise>
											<html:radio styleId="budgetTypeCentral" property="budgetType"
												value="central" disabled="${displayFlag}"
												onclick="updateLayersShown();" />
										</c:otherwise>
									</c:choose> <cms:contentText code="promotion.public.recognition"
										key="BUDGET_TYPE_CENTRAL" /></td>
								</tr>

							</table>
							</td>
						</tr>
						
						
						
						
						
						<%-- Budget Cap Type --%>


						<tr class="form-row-spacer" id="budgetCapRadio">
							<beacon:label property="budgetCapType" required="true"
								styleClass="content-field-label-top">
								<cms:contentText key="CAP_TYPE" code="promotion.awards" />
							</beacon:label>

							<td class="content-field-label">
							<table>
								<tr>
									<td class="content-field" valign="top"><html:radio
										styleId="budgetCapTypeHard" property="budgetCapType"
										value="hard" disabled="${displayFlag}"
										onclick="updateLayersShown();" /> <cms:contentText
										code="promotion.awards" key="CAP_TYPE_HARD" /></td>
								</tr>

								<%-- <tr>
									<td class="content-field"><html:radio
										styleId="budgetCapTypeSoft" property="budgetCapType"
										value="soft" disabled="${displayFlag}"
										onclick="updateLayersShown();" /> <cms:contentText
										code="promotion.awards" key="CAP_TYPE_SOFT" /></td>
								</tr> --%>

							</table>
							</td>
						</tr>
						
						<%-- Budget Cap Type Text--%>
						<tr class="form-row-spacer" id="budgetCapText">
							<beacon:label property="budgetCapType" required="true"
								styleClass="content-field-label-top">
								<cms:contentText key="CAP_TYPE" code="promotion.public.recognition" />
							</beacon:label>

							<td class="content-field-label">
							<table>
								<tr>
									<td class="content-field" valign="top"><cms:contentText
										code="promotion.public.recognition" key="CAP_TYPE_HARD" /></td>
								</tr>
							</table>
							</td>
						</tr>
						
						<tr class="form-row-spacer">
							<beacon:label property="budgetMasterStartDate" required="true" styleClass="content-field-label-top">
				              <cms:contentText key="BUDGET_MASTER_DATES" code="promotion.awards" />
				            </beacon:label> 
							<td class="content-field">
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
				        <tr class="form-row-spacer" >
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
							 		<c:if test="${ promotionPublicRecAddOnForm.budgetSegmentVBListSize ne '1'}">
							 		<th class="crud-table-header-row"><cms:contentText code="system.general" key="CRUD_REMOVE_LABEL"/></th>
							 		</c:if>  			 		
							    </tr>
								<c:set var="switchColor" value="false"/>
								<%		  	 	int sIndex = 0; %>
							  	<c:forEach var="budgetSegmentVBList" items="${promotionPublicRecAddOnForm.budgetSegmentVBList}" varStatus="status" >
							  	<tr>
							  	<html:hidden property="id" name="budgetSegmentVBList" indexed="true"/> 
							  	
							  	<%
									String startDateCalendarCounter = "charStartDate" + sIndex;
									String endDateCalendarCounter = "charEndDate" + sIndex;
									String newBudgetCentralValueCounter = "newBudgetCentralValue" + sIndex;
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
								    	<html:text property="segmentName" size="10" maxlength="10" indexed="true" styleId="segmentName" name="budgetSegmentVBList" styleClass="content-field" />
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
									<td class="crud-content" id="<%=newBudgetCentralValueCounter%>">
								    	<html:text property="originalValue" size="10" maxlength="10" indexed="true" styleId="originalValue" name="budgetSegmentVBList" styleClass="content-field" />
									</td>
									<td class="crud-content">
						            	<table id="<%=removeBudgetSegmentCounter%>">
						            	<tr>
						            	<td align="right">
								        <html:button property="remove" styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('promotionPublicRecAddOn.do', 'removeBudgetSegment')" >
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
						
						<%-- <tr class="form-row-spacer" id="softCapApprover">

							<beacon:label property="budgetApproverId" required="true">
								<cms:contentText key="CAP_APPROVER" code="promotion.awards" />
							</beacon:label>

							<td class="content-field-label">
							<table>
								<tr>
									<td class="content-field-review" valign="top"><b> <c:out
										value="${promotionPublicRecAddOnForm.budgetApproverName}" /></b> &nbsp; <span
										style="display: none"> <html:hidden
										property="budgetApproverId" styleId="budgetApproverId" /> </span> &nbsp; <a
										href="javascript:setActionDispatchAndSubmit('promotionPublicRecAddOn.do?approverLookupReturnUrl=/<%=PromotionWizardManager.getStrutsModulePath(request)%>/promotionPublicRecAddOn.do?method=returnApproverLookup','prepareApproverLookup');"
										class="crud-content-link"> <cms:contentText
										code="promotion.awards" key="CAP_LOOKUP" /> </a></td> 
								</tr>
							</table>
							</td>
						</tr> --%>
						
						<%-- End softCapApprover --%>
						
					</table>
					 </td>
				</tr>
			</table>
			</td>
		</tr>
	</table>
	 </td>
</tr> 

<%--  Budget Final rule payout --%>
<tr class="form-row-spacer" id="finalPayoutRule_radio">
	<beacon:label property="finalPayoutRule" required="true"
		styleClass="content-field-label-top">
		<cms:contentText key="FINAL_PAYOUT_RULE"
			code="admin.budgetmaster.details" />
	</beacon:label>
	<td class="content-field-label" valign="top" align="left" nowrap>
	<c:forEach items="${publicRecognitionFinalPayoutRuleList}"
		var="budgetFinalPayoutRule">
		<html:radio property="finalPayoutRule"
			value="${budgetFinalPayoutRule.value}" onclick="" />&nbsp;<c:out
			value="${budgetFinalPayoutRule.label}" />
		<br>
	</c:forEach></td>
</tr>

<tr class="form-blank-row">
	<td></td>
</tr>	 
<tr>
   <td colspan="3" align="center">
     <tiles:insert attribute="promotion.footer" />
   </td>
</tr>  
			  
</table>
<%--  budgetInfo --%>
</html:form>
<%--  budgetInfo --%>

<tiles:insert attribute="promotionPublicRecognitionJS"/>

<script>
function addAnotherSegment(method)
{
  document.promotionPublicRecAddOnForm.method.value=method;
  document.promotionPublicRecAddOnForm.action = "promotionPublicRecAddOn.do";
  document.promotionPublicRecAddOnForm.submit();
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
