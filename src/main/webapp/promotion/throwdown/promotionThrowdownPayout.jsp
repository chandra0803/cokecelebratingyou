<%--UI REFACTORED--%>
<%--
UI REFACTORING Note: Header and footer pages needs refactoring if necessary.
As this doen't comes under any of our standard layouts and is specific to this page, changed the content wherever necessary as per refactoring requirements.
--%>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-en.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-setup.js"></script>

<c:set var="displayFlag" value="${promotionStatus == 'expired'}" />
<c:set var="displayFlagForRound" value="${promotionStatus == 'expired' || promotionStatus == 'live' || promotionStatus == 'complete'}" />
<c:set var="displayFlag2" value="${promotionStatus == 'expired' || promotionStatus == 'live'}" />

<html:form styleId="contentForm" action="promotionPayoutSave" >
  <html:hidden property="method"/>
  <html:hidden property="promotionId"/>
  <html:hidden property="promotionName"/>
  <html:hidden property="promotionTypeName"/>
  <html:hidden property="promotionTypeCode"/>
  <html:hidden property="promotionStatus"/>
  <html:hidden property="promotionStartDate"/>
  <html:hidden property="promotionEndDate"/>
  <html:hidden property="daysPriorToGenerateMatches"/>   
  <html:hidden property="version"/>
  <html:hidden property="awardType" styleId="awardType"/>
  <html:hidden property="awardTypeName"/>
  
  <c:if test="${displayFlag2}">
    <html:hidden property="achievementPrecision"/>
    <html:hidden property="roundingMethod"/>
  </c:if>
  
  <table border="0" cellpadding="0" cellspacing="0" width="100%">
    <tr>
      <td>
        <c:set var="promoTypeName" scope="request" value="${promotionThrowdownPayoutForm.promotionTypeName}" />
        <c:set var="promoTypeCode" scope="request" value="${promotionThrowdownPayoutForm.promotionTypeCode}" />
        <c:set var="promoName" scope="request" value="${promotionThrowdownPayoutForm.promotionName}" />
        
        <tiles:insert attribute="promotion.header" />
      </td>
    </tr>
    <tr>
      <td>
        <cms:errors/>
      </td>
    </tr>          
    <tr>
      <td width="50%" valign="top">
        <table>
        
          <tr>
            <td>&nbsp;</td>
            <td class="content-field-label"><cms:contentText code="promotion.payout" key="TYPE"/></td>
            <td class="content-field-review"><c:out value="${promotionThrowdownPayoutForm.awardTypeName}" /></td>
          </tr>
           <tr class="form-row-spacer">
            <beacon:label property="baseUnit"  styleClass="content-field-label-top">
              <cms:contentText key="BASE_UNIT" code="promotion.payout.goalquest"/>
            </beacon:label>
            <td class="content-field">
              <html:text property="baseUnit" size="20" maxlength="20" styleClass="content-field"/>
            </td>
          </tr>
          <tr class="form-row-spacer">
            <beacon:label property="baseUnitPosition"  styleClass="content-field-label-top">
              <cms:contentText key="BASE_UNIT_POSITION" code="promotion.payout.goalquest"/>
            </beacon:label>
            <td class="content-field">
              <html:select property="baseUnitPosition" styleClass="content-field" >
                <html:options collection="baseunitPositionList" property="code" labelProperty="name" />
              </html:select>
            </td>
            </tr> 
          <!--  start precision/rounding stuff -->
          <tr class="form-row-spacer">
            <beacon:label property="achievementPrecision" required="true" styleClass="content-field-label-top">
              <cms:contentText key="ACHIEVEMENT_PRECISION" code="promotion.payout.throwdown"/>
            </beacon:label>
            <td class="content-field">
              <html:select property="achievementPrecision" styleClass="content-field" onchange="achievementPrecisionChange()" disabled="${displayFlag2}">
                <html:options collection="achievementPrecisionList" property="code" labelProperty="name" />
              </html:select>
            </td>
          </tr>

          <tr class="form-row-spacer">
            <beacon:label property="roundingMethod" required="true" styleClass="content-field-label-top">
              <cms:contentText key="ROUNDING_METHOD" code="promotion.payout.throwdown"/>
            </beacon:label>
            <td class="content-field">
              <html:select property="roundingMethod" styleClass="content-field" disabled="${displayFlag2}">
                <html:option value=''><cms:contentText key="SELECT_ONE" code="system.general"/></html:option>
                <html:options collection="roundingMethodList" property="code" labelProperty="name" />
              </html:select>
            </td>
          </tr>
          
          <!-- end precision/rounding stuff  -->
		    <tr class="form-row-spacer">
		        <beacon:label property="billCodesActive" required="true" >
		            <cms:contentText code="promotion.bill.code" key="BILL_CODES_ACTIVE" />
		        </beacon:label>
		        <td class="content-field" valign="top" colspan="2">
		       		<html:radio styleId="billCodesActiveFalse" property="billCodesActive" value="false"
		           	disabled="${displayFlag}" onclick="enableFields();" />&nbsp;<cms:contentText code="promotion.bill.code" key="NO" />
		        </td>
		    </tr>
		    <tr class="form-row-spacer">
		        <td colspan="2">&nbsp;</td>
		        <td class="content-field" valign="top" colspan="2">
		        	<html:radio styleId="billCodesActiveTrue" property="billCodesActive" value="true"
		            disabled="${displayFlag}" onclick="enableFields();"/>&nbsp;<cms:contentText code="promotion.bill.code" key="YES" />
		       	</td>
		    </tr>    
		    <%@include file="/throwdown/throwdownBillCodes.jsp" %>
		    <tr>
            <td>&nbsp;</td>
            <td class="content-field-label"><cms:contentText code="leaderboard.label" key="PROMOTION_START_DATE"/></td>
            <td class="content-field-review"><c:out value="${promotionThrowdownPayoutForm.promotionStartDate}" /></td>
            </tr>
            <tr>
            <td>&nbsp;</td>
            <td class="content-field-label"><cms:contentText code="leaderboard.label" key="PROMOTION_END_DATE"/></td>
            <td class="content-field-review"><c:out value="${promotionThrowdownPayoutForm.promotionEndDate}" /></td>
            </tr>
			<tr class="form-row-spacer" id="numberOfRoundsRow">				  
		        <beacon:label property="numberOfRounds" required="true">
		          	<cms:contentText code="promotion.payout.throwdown" key="NUMBER_OF_ROUNDS" />
		        </beacon:label>	
		        <td class="content-field">
		        <c:choose>
		        <c:when test="${displayFlagForRound}">
		        	<html:hidden property="numberOfRounds"/>
		          	<html:text property="numberOfRounds" styleId="numberOfRounds" maxlength="2" size="10" styleClass="content-field" disabled="true" onkeyup="scheduleRounds()"/>
		        </c:when>
		        <c:otherwise>
		        <html:text property="numberOfRounds" styleId="numberOfRounds" maxlength="2" size="10" styleClass="content-field" onkeyup="scheduleRounds()"/>
		        </c:otherwise>
		        </c:choose>
		        </td>
		    </tr> 
		    
			<tr class="form-row-spacer" id="numberOfDayPerRoundRow">				  
		        <beacon:label property="numberOfDayPerRound" required="true">
		          	<cms:contentText code="promotion.payout.throwdown" key="NUMBER_OF_DAYS_PER_ROUND" />
		        </beacon:label>	
		        <td class="content-field">
		        <c:choose>
		        <c:when test="${displayFlagForRound}">
		        	<html:hidden property="numberOfDayPerRound"/>
		          	<html:text property="numberOfDayPerRound" styleId="numberOfDayPerRound" maxlength="3" size="10" styleClass="content-field" disabled="true" onkeyup="scheduleRounds()"/>
		        </c:when>
		         <c:otherwise>
		         <html:text property="numberOfDayPerRound" styleId="numberOfDayPerRound" maxlength="3" size="10" styleClass="content-field" onkeyup="scheduleRounds()"/>
		         </c:otherwise>
		        </c:choose>
		        </td>
		    </tr> 		     		               
              
			<tr class="form-row-spacer" id="startDateForFirst_RoundRow">				  
		        <beacon:label property="startDateForFirstRound" required="true">
		          	<cms:contentText code="promotion.payout.throwdown" key="START_DATE_FOR_FIRST_ROUND" />
		        </beacon:label>	
		        <td class="content-field">
			  		<c:choose>
			  		   <c:when test="${displayFlagForRound == 'false'}">
	  		       		<html:text property="startDateForFirstRound" styleId="startDateForFirstRound" size="10" maxlength="10" styleClass="content-field" readonly="true" disabled="false" onfocus="clearDateMask(this);" />
	  		       		<img id="startDateForFirstRoundTrigger" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" alt="<cms:contentText key='START_DATE_FOR_FIRST_ROUND' code='promotion.payout.throwdown'/>"/>							    
			  		   </c:when>	        
					   <c:otherwise>
					  	<html:hidden property="startDateForFirstRound"/>
					  	<html:text property="startDateForFirstRound" styleId="startDateForFirstRound" size="10" maxlength="10" styleClass="content-field" disabled="true"/>
					  </c:otherwise>
					</c:choose>			        
		        </td>
		    </tr>  
	<%-- Schedule Of Rounds - Start --%>
	<html:hidden property="roundListCount"/>
			<tr class="form-row-spacer" id="scheduleOfRounds">				  
				<beacon:label property="scheduleOfRounds" styleClass="content-field-label-top">
					<cms:contentText code="promotion.payout.throwdown" key="SCHEDULE_OF_ROUNDS" />
				</beacon:label>
		        <td class="content-field">
			  		<table class="crud-table" width="100%">
			  		<tr>
			  		<th class="crud-table-header-row"><cms:contentText code="promotion.payout.throwdown" key="ROUND_NUMBER" /></th>
			  		<th class="crud-table-header-row"><cms:contentText code="promotion.basics" key="START_DATE" /></th>
			  		<th class="crud-table-header-row"><cms:contentText code="promotion.basics" key="END_DATE" /></th>
			  		<th class="crud-table-header-row"><cms:contentText code="promotion.payout.throwdown" key="SCHEDULE_STATUS" /></th>
			  		<th class="crud-table-header-row"><cms:contentText code="promotion.payout.throwdown" key="PAYOUT_STATUS" /></th>
			  		</tr>
					<c:if test="${empty promotionThrowdownPayoutForm.roundsList}">
					  <tr><td colspan="3"><cms:contentText code="promotion.payout" key="NOTHING_FOUND"/></td></tr>
					</c:if>			  				        
					<c:if test="${not empty promotionThrowdownPayoutForm.roundsList}">
					<c:set var="switchColor" value="false"/>
					<nested:iterate id="round" name="promotionThrowdownPayoutForm" property="roundsList" indexId="idx">
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
			            <td class="content-field center-align">${round.roundNumber}<nested:hidden property="roundNumber"/></td>
			            <td class="content-field left-align">${round.startDate}<nested:hidden property="startDate"/></td>
			            <td class="content-field left-align">${round.endDate}<nested:hidden property="endDate"/></td>
			            <td class="content-field center-align"><c:if test="${round.scheduled}"><cms:contentText code="promotion.payout.throwdown" key="ROUND_SCHEDULED" /></c:if></td>
			            <td class="content-field center-align"><c:if test="${round.payoutComplete}"><cms:contentText code="promotion.payout.throwdown" key="PAYOUTS_ISSUED" /></c:if></td>
					</nested:iterate>
					</c:if>			 
			  		</table>					 				        					
		        </td>
		    </tr> 
		    
  <%-- Schedule Of Rounds - End --%>		                

  <%-- Division Payouts - Start --%>

	<html:hidden property="divisionEditId" value=""/>
	<html:hidden property="divisionValueListCount"/>
	<html:hidden property="divisionPayoutValueListCount"/>
				    
	<c:set var="groupCounter" value="${0}"/>		    
 	<nested:iterate id="promoPayoutGroup" name="promotionThrowdownPayoutForm" property="divisionValueList" indexId="idx">
     <tr class="form-row-spacer">
    
     <c:choose>
         <c:when test="${groupCounter == '0'}">
           	<beacon:label property="payoutType" required="true" styleClass="content-field-label-top">
        		<cms:contentText code="promotion.payout.throwdown" key="DIVISION_PAYOUTS"/>
   			</beacon:label>
         </c:when>
         <c:otherwise>
         	<td colspan="2">&nbsp;</td>
         </c:otherwise>
	</c:choose>
		

      <td colspan="5">
        <table class="crud-table" width="100%">
          <tr>
            <th class="crud-table-header-row"><cms:contentText code="promotion.payout.throwdown" key="DIVISION_NAME"/></th>
            <th class="crud-table-header-row"><cms:contentText code="promotion.payout.throwdown" key="MINIMUM_QUALIFIER"/></th>
            <th class="crud-table-header-row"><cms:contentText code="promotion.payout.throwdown" key="MATCH_OUTCOME"/></th>
            <th class="crud-table-header-row"><cms:contentText code="promotion.overview" key="AWARD"/></th>
            <th class="crud-table-header-row"><cms:contentText code="system.general" key="CRUD_REMOVE_LABEL"/></th>
          </tr>
                   
		 <c:choose>
		  <c:when test="${empty divisionList}">
              <tr>
                <td colspan="5" class="content-field left-align"><cms:contentText code="promotion.payout.errors" key="NO_DIVISION_EXISTS_ERROR" /></td>
              </tr>
          </c:when>
          <c:otherwise>
		          <input type="hidden" name="<c:out value="divisionPayoutSize${promoPayoutGroup.guid}"/>" value="<c:out value="${promoPayoutGroup.divisionPayoutValueListCount}"/>"/>
		          <nested:hidden property="guid"/>
		          <nested:hidden property="version"/>
		          <nested:hidden property="createdBy"/>
		          <nested:hidden property="dateCreated"/>
		          <c:choose>
		            <c:when test="${promoPayoutGroup.divisionPayoutValueListCount == '0'}">
		              <tr>
		                <td colspan="5" class="content-field left-align"><cms:contentText code="promotion.payout" key="NOTHING_FOUND"/></td>
		              </tr>
		            </c:when>
		            <c:otherwise>
		              <c:set var="promoPayoutCounter" value="${0}"/>
		              <c:set var="switchColor" value="false"/>
		              <nested:iterate id="promoPayoutValue" property="divisionPayoutValueList">
		         
		              <nested:hidden property="divisionPayoutId"/>
		              <nested:hidden property="version"/>
		              <nested:hidden property="createdBy"/>
		              <nested:hidden property="dateCreated"/>
		              <nested:hidden property="divisionId"/>
		
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
			              <c:when test="${promoPayoutCounter == '0'}">
			               <td class="content-field left-align">
			               <nested:hidden property="../divisionName" styleId='divSelectName${idx}'/>
			               <nested:select property="../divisionId" styleId='divSelectId${idx}' styleClass="content-field" onchange="javascript:saveForm(true),setDivisionName(${idx},this)">
			                <c:forEach items="${divisionList}" var="division">
			               		<html:option value="${division.id}">${division.divisionNameFromCM}</html:option>
			             	</c:forEach>
			              </nested:select>
						   </td>
				          <td class="content-field center-align">
				            <nested:text property="../minimumQualifier" size="5" styleClass="content-field" onchange="javascript:saveForm(true)"/>		              
				          </td>				   
						 </c:when>                  
			              <c:otherwise>	             
			               <td class="content-field left-align">
			                   &nbsp;
						   </td>
			               <td class="content-field left-align">
			                   &nbsp;
						   </td>				   
						 </c:otherwise>
					  </c:choose>   
		
		                <c:choose>
			              <c:when test="${promotionPayoutForm.promotionStatus == 'live'}">
				          <td class="content-field center-align">
				          <nested:select property="outcomeCode" styleClass="content-field" onchange="javascript:saveForm(true)">
			                <c:forEach items="${outcomeTypeList}" var="outcomeType">
			               		<html:option value="${outcomeType.code}">${outcomeType.name}</html:option>
			             	</c:forEach>		              
			              </nested:select>
				          </td>
				           <td class="content-field left-align">
				            <nested:text property="payoutAmount" size="5" styleClass="content-field" onchange="javascript:saveForm(true)"/>		              
				          </td>
				            </c:when>                  
			              <c:otherwise>	
				          <td class="content-field center-align">
				          <nested:select property="outcomeCode" styleClass="content-field" >
			                <c:forEach items="${outcomeTypeList}" var="outcomeType">
			               		<html:option value="${outcomeType.code}">${outcomeType.name}</html:option>
			             	</c:forEach>		              
			              </nested:select>
				          </td>
				           <td class="content-field left-align">
				            <nested:text property="payoutAmount" size="5" styleClass="content-field" />		              
				          </td>
			              </c:otherwise>
			              </c:choose> 
		                 
		              <td class="content-field center-align">
		                <nested:checkbox property="removePayout" value="Y"/>
		              </td>
		             
		              <c:set var="promoPayoutCounter" value="${promoPayoutCounter + 1}"/>
		            </tr>
		          </nested:iterate>
		        </c:otherwise>
		      </c:choose>          
          </c:otherwise>
          </c:choose>		  
    </table>
  </td>
</tr>		    
	
<c:if test="${not empty divisionList}">		    
  <tr class="form-row-spacer">
    <td colspan="2">&nbsp;</td>
    <td colspan="2">
    <input type="button" onclick="setDivisionId('<c:out value="${promoPayoutGroup.guid}"/>');setActionDispatchAndSubmit('promotionPayout.do','addPayoutForThisDivision');" name="add_payout_this_division" class="content-buttonstyle" value="<cms:contentText code="promotion.payout.throwdown" key="ADD_PAYOUT_LEVEL_FOR_THIS_DIVISION"/>" />
    </td>
    <td align="right">
        <html:button property="remove" styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('promotionPayout.do', 'removeDivisionPayouts')" >
          <cms:contentText code="system.button" key="REMOVE_SELECTED" />
        </html:button>
    </td>
  </tr>
    
   <tr class="form-blank-row">
      <td></td>
    </tr>
  <c:set var="groupCounter" value="${groupCounter + 1}"/>
</c:if>  
  </nested:iterate>		    
		 
<c:if test="${not empty divisionList}">		    
<tr class="form-row-spacer">
  <td colspan="2">&nbsp;</td>
  <td colspan="2">
    <html:button property="add_division" styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('promotionPayout.do','addPayoutForDivision')" >
      <cms:contentText code="promotion.payout.throwdown" key="ADD_PAYOUT_LEVEL_FOR_ANOTHER_DIVISION" />
    </html:button>
  </td>
</tr>		  
</c:if>  

  <%-- Division Payouts - End --%>
		
  <%-- Stack Standing Payouts - Start --%>
	<html:hidden property="stackStandingGroupEditId" value=""/>
	<html:hidden property="promoStackStandingPayoutGroupValueListCount"/>
	<html:hidden property="promoStackStandingPayoutValueListCount"/>
				    
	<c:set var="groupCounter" value="${0}"/>		    
 	<nested:iterate id="promoPayoutGroup" name="promotionThrowdownPayoutForm" property="promoStackStandingPayoutGroupValueList">		    
     <tr class="form-row-spacer">
    
     <c:choose>
         <c:when test="${groupCounter == '0'}">
           	<beacon:label property="payoutType" styleClass="content-field-label-top">
        		<cms:contentText key="RANKING_PAYOUTS" code="promotion.payout.throwdown"/>
   			</beacon:label>
         </c:when>
         <c:otherwise>
         	<td colspan="2">&nbsp;</td>
         </c:otherwise>
	</c:choose>
		

      <td colspan="5">
        <table class="crud-table" width="100%">
          <tr>
            <th class="crud-table-header-row"><cms:contentText code="promotion.payout" key="NODE_TYPE"/></th>
            <th class="crud-table-header-row"><cms:contentText code="promotion.payout.throwdown" key="RANKINGS_AND_PAYOUT"/></th>
            <th class="crud-table-header-row"><cms:contentText code="promotion.payout.throwdown" key="STACK_RANK_FROM"/></th>
            <th class="crud-table-header-row"><cms:contentText code="promotion.payout.throwdown" key="STACK_RANK_TO"/></th>
            <th class="crud-table-header-row"><cms:contentText code="promotion.overview" key="AWARD"/></th>
            <th class="crud-table-header-row"><cms:contentText code="system.general" key="CRUD_REMOVE_LABEL"/></th>
          </tr>
                   
 
          <input type="hidden" name="<c:out value="groupStackStandingPayoutSize${promoPayoutGroup.guid}"/>" value="<c:out value="${promoPayoutGroup.promoStackStandingPayoutValueListCount}"/>"/>
          <nested:hidden property="guid"/>
          <nested:hidden property="promoPayoutGroupId"/>
          <nested:hidden property="version"/>
          <nested:hidden property="createdBy"/>
          <nested:hidden property="dateCreated"/>
          <c:choose>
            <c:when test="${promoPayoutGroup.promoStackStandingPayoutValueListCount == '0'}">
              <tr>
                <td colspan="5" class="content-field left-align"><cms:contentText code="promotion.payout" key="NOTHING_FOUND"/></td>
              </tr>
            </c:when>
            <c:otherwise>
              <c:set var="promoPayoutCounter" value="${0}"/>
              <c:set var="switchColor" value="false"/>
              <nested:iterate id="promoPayoutValue" property="promoStackStandingPayoutValueList">
         
              <nested:hidden property="promoPayoutId"/>
              <nested:hidden property="version"/>
              <nested:hidden property="createdBy"/>
              <nested:hidden property="dateCreated"/>
              <nested:hidden property="nodeTypeId"/>

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
	              <c:when test="${promoPayoutCounter == '0'}">
	               <td class="content-field left-align">
	                   <nested:select property="../nodeTypeId" styleClass="content-field" onchange="javascript:saveForm(true)">
	                    <html:option value=""><cms:contentText key="ALL" code="system.general"/></html:option>
	                    <c:forEach items="${nodeTypeList}" var="nodeType">
		                  <html:option value="${nodeType.id}"><cms:contentText key="${nodeType.nameCmKey}" code="${nodeType.cmAssetCode}"/></html:option>
		                </c:forEach>
	                  </nested:select>
				   </td>
		            <td class="content-field center-align">
				       <nested:select styleId="rankingsPayoutType" property="../rankingsPayoutType" styleClass="content-field" onchange="javascript:saveForm(true)">
				       	<html:options collection="rankingsPayoutTypeList" property="code" labelProperty="name"  />
				       </nested:select> 
				    </td>				   
				 </c:when>                  
	              <c:otherwise>	             
	               <td class="content-field left-align">
	                   &nbsp;
				   </td>
	               <td class="content-field left-align">
	                   &nbsp;
				   </td>				   
				 </c:otherwise>
			  </c:choose>   

                <c:choose>
	              <c:when test="${promotionPayoutForm.promotionStatus == 'live'}">
			            <td class="content-field center-align">
			              <nested:text property="fromStanding" size="5" styleClass="content-field" onchange="javascript:saveForm(true)"/>		              
			            </td>
			             <td class="content-field center-align">
			              <nested:text property="toStanding" size="5" styleClass="content-field" onchange="javascript:saveForm(true)"/>		              
			            </td>
			             <td class="content-field center-align">
			              <nested:text property="payoutAmount" size="5" styleClass="content-field" onchange="javascript:saveForm(true)"/>		              
			            </td>
		            </c:when>                  
	              <c:otherwise>	
	                  	<td class="content-field center-align">
			              <nested:text property="fromStanding" size="5" styleClass="content-field"/>		              
			            </td>
			             <td class="content-field center-align">
			              <nested:text property="toStanding" size="5" styleClass="content-field"/>		              
			            </td>
			             <td class="content-field center-align">
			              <nested:text property="payoutAmount" size="5" styleClass="content-field"/>		              
			            </td>
	              </c:otherwise>
	              </c:choose> 
                 
              <td class="content-field center-align">
                <nested:checkbox property="removePayout" value="Y"/>
              </td>
             
              <c:set var="promoPayoutCounter" value="${promoPayoutCounter + 1}"/>
            </tr>
          </nested:iterate>
        </c:otherwise>
      </c:choose>
    </table>
  </td>
</tr>		    
		    
  <tr class="form-row-spacer">
    <td colspan="2">&nbsp;</td>
    <td colspan="2">
    <input type="button" onclick="setStackStandingGroupId('<c:out value="${promoPayoutGroup.guid}"/>');setActionDispatchAndSubmit('promotionPayout.do','addPayoutForThisNode');" name="add_payout_this_node" class="content-buttonstyle" value="<cms:contentText code="promotion.payout" key="ADD_PAY_OUT_LEVEL"/>" />
    </td>
    <td align="right">
        <html:button property="remove" styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('promotionPayout.do', 'removeStackStandingPayouts')" >
          <cms:contentText code="system.button" key="REMOVE_SELECTED" />
        </html:button>
    </td>
  </tr>
    
   <tr class="form-blank-row">
      <td></td>
    </tr>
  <c:set var="groupCounter" value="${groupCounter + 1}"/>
  </nested:iterate>		    
		    
<tr class="form-row-spacer">
  <td colspan="2">&nbsp;</td>
  <td colspan="2">
	    <html:button property="add_group" styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('promotionPayout.do','addPayoutForNode')" >
	      <cms:contentText code="promotion.payout" key="ADD_PAYOUT_ANOTHER_NODE" />
	    </html:button>
  </td>
</tr>		    
		    
  <%-- Stack Standing Payouts - End --%>
              
        </table>
      </td>
    </tr>   
    
    <tr>
      <td colspan="2" align="center"><tiles:insert attribute="promotion.footer" /></td>
    </tr>
  </table>
</html:form>    

<SCRIPT type="text/javascript">

	Calendar.setup(
		{
		  inputField  : "startDateForFirstRound",       	// ID of the input field
		  ifFormat    : "${TinyMceDatePattern}",    		// the date format
		  button      : "startDateForFirstRoundTrigger",  // ID of the button
		  onClose	  : onClose
		}); 
	 	function onClose(calendar, date) {
	 		scheduleRounds(); 			
		};	

	var billCodesActiveTrueObj = document.getElementById("billCodesActiveTrue");
	var billCodesActiveFalseObj = document.getElementById("billCodesActiveFalse"); 
	
	var billCode1Obj = document.getElementById("billCode1");
	var customValue1Obj = $("input[name='customValue1']").val();
	
	var billCode2Obj = document.getElementById("billCode2");
	var customValue2Obj = document.getElementById("customValue2");
	
	var billCode3Obj = document.getElementById("billCode3");
	var customValue3Obj = document.getElementById("customValue3");
	
	var billCode4Obj = document.getElementById("billCode4");
	var customValue4Obj = document.getElementById("customValue4");
	
	var billCode5Obj = document.getElementById("billCode5");
	var customValue5Obj = document.getElementById("customValue5");
	
	var billCode6Obj = document.getElementById("billCode6");
	var customValue6Obj = document.getElementById("customValue6");
	
	var billCode7Obj = document.getElementById("billCode7");
	var customValue7Obj = document.getElementById("customValue7");
	
	var billCode8Obj = document.getElementById("billCode8");
	var customValue8Obj = document.getElementById("customValue8");
	
	var billCode9Obj = document.getElementById("billCode9");
	var customValue9Obj = document.getElementById("customValue9");
	
	var billCode10Obj = document.getElementById("billCode10");
	var customValue10Obj = document.getElementById("customValue10");
	
 	function enableFields()
 	{
 		if( billCodesActiveFalseObj.checked == true)
      	{
	          $("select[name='billCode1'] option[value='']").attr('selected', 'selected');
	          $("select[name='billCode1']").attr('disabled', 'disabled');
	          $("select[name='billCode2'] option[value='']").attr('selected', 'selected');
	          $("select[name='billCode2']").attr('disabled', 'disabled');
	          $("select[name='billCode3'] option[value='']").attr('selected', 'selected');
	          $("select[name='billCode3']").attr('disabled', 'disabled');
	          $("select[name='billCode4'] option[value='']").attr('selected', 'selected');
	          $("select[name='billCode4']").attr('disabled', 'disabled');
	          $("select[name='billCode5'] option[value='']").attr('selected', 'selected');
	          $("select[name='billCode5']").attr('disabled', 'disabled');
	          $("select[name='billCode6'] option[value='']").attr('selected', 'selected');
	          $("select[name='billCode6']").attr('disabled', 'disabled');
	          $("select[name='billCode7'] option[value='']").attr('selected', 'selected');
	          $("select[name='billCode7']").attr('disabled', 'disabled');
	          $("select[name='billCode8'] option[value='']").attr('selected', 'selected');
	          $("select[name='billCode8']").attr('disabled', 'disabled');
	          $("select[name='billCode9'] option[value='']").attr('selected', 'selected');
	          $("select[name='billCode9']").attr('disabled', 'disabled');
	          $("select[name='billCode10'] option[value='']").attr('selected', 'selected');
	          $("select[name='billCode10']").attr('disabled', 'disabled');
	          
	          $("input[name='customValue1']").val("");
	          $("input[name='customValue2']").val("");
	          $("input[name='customValue3']").val("");
	          $("input[name='customValue4']").val("");
	          $("input[name='customValue5']").val("");
	          $("input[name='customValue6']").val("");
	          $("input[name='customValue7']").val("");
	          $("input[name='customValue8']").val("");
	          $("input[name='customValue9']").val("");
	          $("input[name='customValue10']").val("");
	          
	          $("input[name='customValue1']").hide();
			  $("#billCode1Custom").hide();
			  
			  $("input[name='customValue2']").hide();
			  $("#billCode2Custom").hide();
			  
			  $("input[name='customValue3']").hide();
			  $("#billCode3Custom").hide();
			  
			  $("input[name='customValue4']").hide();
			  $("#billCode4Custom").hide();
			  
			  $("input[name='customValue5']").hide();
			  $("#billCode5Custom").hide();
			  
			  $("input[name='customValue6']").hide();
			  $("#billCode6Custom").hide();
			  
			  $("input[name='customValue7']").hide();
			  $("#billCode7Custom").hide();
			  
			  $("input[name='customValue8']").hide();
			  $("#billCode8Custom").hide();
			  
			  $("input[name='customValue9']").hide();
			  $("#billCode9Custom").hide();
			  
			  $("input[name='customValue10']").hide();
			  $("#billCode10Custom").hide();
      	}
		if( billCodesActiveTrueObj.checked == true)
      	{
			$("select[name='billCode1']").attr('disabled', false);
			$("select[name='billCode2']").attr('disabled', false);
			$("select[name='billCode3']").attr('disabled', false);
			$("select[name='billCode4']").attr('disabled', false);
			$("select[name='billCode5']").attr('disabled', false);
			$("select[name='billCode6']").attr('disabled', false);
			$("select[name='billCode7']").attr('disabled', false);
			$("select[name='billCode8']").attr('disabled', false);
			$("select[name='billCode9']").attr('disabled', false);
			$("select[name='billCode10']").attr('disabled', false);
      	}
    } 
    enableFields();
    
    function enableBillCode1()
    {
      var billCode1Selected = document.getElementById("billCode1").value;
      var patt = new RegExp("custom");
      var res = patt.test(billCode1Selected);
      if(  res )
        {
            document.getElementById('customValue1').style.display = 'inline';
            document.getElementById('billCode1Custom').style.display = 'table-row';
            enableBillCode2();
        }
      else
      {
            document.getElementById('customValue1').style.display = 'none';
            document.getElementById('billCode1Custom').style.display = 'none';
            enableBillCode2();
        }
    }
     enableBillCode1();
    
    function enableBillCode2()
    {
       var billCode2Selected = document.getElementById("billCode2").value;
      var patt = new RegExp("custom");
      var res = patt.test(billCode2Selected);
      if(  res )
        {
            document.getElementById('customValue2').style.display = 'inline';
            document.getElementById('billCode2Custom').style.display = 'table-row';
            enableBillCode3();
        }
      else
      {
            document.getElementById('customValue2').style.display = 'none';
            document.getElementById('billCode2Custom').style.display = 'none';
            enableBillCode3();
        }
    }
    enableBillCode2();

    
    function enableBillCode3()
    {
       var billCode3Selected = document.getElementById("billCode3").value;
      var patt = new RegExp("custom");
      var res = patt.test(billCode3Selected);
      if(  res )
        {
            document.getElementById('customValue3').style.display = 'inline';
            document.getElementById('billCode3Custom').style.display = 'table-row';
            enableBillCode4();
        }
      else
      {
            document.getElementById('customValue3').style.display = 'none';
            document.getElementById('billCode3Custom').style.display = 'none';
            enableBillCode4();
        }
    }
    enableBillCode3();
    
    
    function enableBillCode4()
    {
       var billCode4Selected = document.getElementById("billCode4").value;
      var patt = new RegExp("custom");
      var res = patt.test(billCode4Selected);
      if(  res )
        {
            document.getElementById('customValue4').style.display = 'inline';
            document.getElementById('billCode4Custom').style.display = 'table-row';
            enableBillCode5();
        }
      else
      {
            document.getElementById('customValue4').style.display = 'none';
            document.getElementById('billCode4Custom').style.display = 'none';
            enableBillCode5();
        }
    }
    enableBillCode4();
    
    function enableBillCode5()
    {
       var billCode5Selected = document.getElementById("billCode5").value;
      var patt = new RegExp("custom");
      var res = patt.test(billCode5Selected);
      if(  res )
        {
            document.getElementById('customValue5').style.display = 'inline';
            document.getElementById('billCode5Custom').style.display = 'table-row';
            enableBillCode6();
        }
      else
      {
            document.getElementById('customValue5').style.display = 'none';
            document.getElementById('billCode5Custom').style.display = 'none';
            enableBillCode6();
        }
    }
    enableBillCode5();
    
    function enableBillCode6()
    {
       var billCode6Selected = document.getElementById("billCode6").value;
      var patt = new RegExp("custom");
      var res = patt.test(billCode6Selected);
      if(  res )
        {
            document.getElementById('customValue6').style.display = 'inline';
            document.getElementById('billCode6Custom').style.display = 'table-row';
            enableBillCode7();
        }
      else
      {
            document.getElementById('customValue6').style.display = 'none';
            document.getElementById('billCode6Custom').style.display = 'none';
            enableBillCode7();
        }
    }
    enableBillCode6();
    
    
    function enableBillCode7()
    {
       var billCode7Selected = document.getElementById("billCode7").value;
      var patt = new RegExp("custom");
      var res = patt.test(billCode7Selected);
      if(  res )
        {
            document.getElementById('customValue7').style.display = 'inline';
            document.getElementById('billCode7Custom').style.display = 'table-row';
            enableBillCode8();
        }
      else
      {
            document.getElementById('customValue7').style.display = 'none';
            document.getElementById('billCode7Custom').style.display = 'none';
            enableBillCode8();
        }
    }
    enableBillCode7();
    
    function enableBillCode8()
    {
       var billCode8Selected = document.getElementById("billCode8").value;
      var patt = new RegExp("custom");
      var res = patt.test(billCode8Selected);
      if(  res )
        {
            document.getElementById('customValue8').style.display = 'inline';
            document.getElementById('billCode8Custom').style.display = 'table-row';
            enableBillCode9();
        }
      else
      {
            document.getElementById('customValue8').style.display = 'none';
            document.getElementById('billCode8Custom').style.display = 'none';
            enableBillCode9();
        }
    }
    enableBillCode8();
    
    function enableBillCode9()
    {
       var billCode9Selected = document.getElementById("billCode9").value;
      var patt = new RegExp("custom");
      var res = patt.test(billCode9Selected);
      if(  res )
        {
            document.getElementById('customValue9').style.display = 'inline';
            document.getElementById('billCode9Custom').style.display = 'table-row';
            enableBillCode10();
        }
      else
      {
            document.getElementById('customValue9').style.display = 'none';
            document.getElementById('billCode9Custom').style.display = 'none';
            enableBillCode10();
        }
    }
    enableBillCode9();
    
    
    function enableBillCode10()
    {
       var billCode10Selected = document.getElementById("billCode10").value;
      var patt = new RegExp("custom");
      var res = patt.test(billCode10Selected);
      if(  res )
        {
            document.getElementById('customValue10').style.display = 'inline';
            document.getElementById('billCode10Custom').style.display = 'table-row';
        }
      else
      {
            document.getElementById('customValue10').style.display = 'none';
            document.getElementById('billCode10Custom').style.display = 'none';
        }
    }
    enableBillCode10();
    
    function setStackStandingGroupId( id )
    {
      document.promotionThrowdownPayoutForm.stackStandingGroupEditId.value = id;
    }      
    
    function setDivisionId( id )
    {
      document.promotionThrowdownPayoutForm.divisionEditId.value = id;
    }          

    function setDivisionName( index, id )
    {
      document.getElementById('divSelectName'+index).value = document.getElementById('divSelectId'+index).options[document.getElementById('divSelectId'+index).selectedIndex].text
    }          
    
    var needToConfirm = false;
    function saveForm(value)
    { 
      needToConfirm = value;
    }     
    
    function scheduleRounds()
    {
	  setActionDispatchAndSubmit('promotionPayout.do','recalculateRounds')
    }
    
    achievementPrecisionChange();
    
    function achievementPrecisionChange()
    {
    	var contentForm = document.getElementById("contentForm");
      	if ( contentForm.achievementPrecision.value == 'zero' )
      	{
        	contentForm.roundingMethod.value = 'standard';
        	contentForm.roundingMethod.disabled = true;
      	}
      	else
      	{
        	contentForm.roundingMethod.disabled = false;
     	}
    }
    
</SCRIPT> 
