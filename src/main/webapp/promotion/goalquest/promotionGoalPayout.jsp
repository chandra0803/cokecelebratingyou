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

<c:if test="${promotionType == 'challengepoint'}">
	<c:set var="payoutForm" value="${challengePointPayoutForm}"/>
</c:if>
<c:if test="${promotionType == 'goalquest'}">
	<c:set var="payoutForm" value="${promotionGoalPayoutForm}"/>
</c:if>

<c:set var="displayFlag" scope="request" value="${payoutForm.promotionStatus == 'expired' || (payoutForm.promotionStatus == 'live' && payoutForm.beforeGoalSelection == 'false')}" />
<c:set var="displayFlag2" scope="request" value="${payoutForm.promotionStatus == 'expired' || (payoutForm.promotionStatus == 'live' && payoutForm.afterGoalSelection == 'true')}" />	


<script type="text/javascript">
<!--
function callUrl( urlToCall )
{
  window.location=urlToCall;
}


function payoutStructureChange()
{
	var contentForm = document.getElementById("contentForm");
  	var url = 'promotionPayout.do?payoutStructure=' + contentForm.payoutStructure.value;
  	url = url + '&oldPayoutStructure=' + contentForm.currentPayoutStructure.value;
  	url = url + '&id=' + contentForm.promotionId.value;
  	setActionDispatchAndSubmit(url,'payoutStructureChange');
}

function planningWorksheetChange()
{
	var contentForm = document.getElementById("contentForm");
  	var url = contentForm.goalPlanningWorksheet.value;
  	if (url == "") {
    	document.getElementById("goalWorksheetLink").style.display='none';
  	} else
  	{
   	 	document.getElementById("goalWorksheetLink").href = contentForm.goalPlanningWorksheet.value;
    	document.getElementById("goalWorksheetLink").style.display='block';
  	} 
}

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

//-->
</script>

<html:form styleId="contentForm" action="promotionPayoutSave" >
  <html:hidden property="method"/>
  <html:hidden property="promotionId"/>
  <html:hidden property="promotionName"/>
  <html:hidden property="promotionTypeName"/>
  <html:hidden property="promotionTypeCode"/>
  <html:hidden property="promotionStatus"/>
  <html:hidden property="alternateReturnUrl"/>
  <html:hidden property="version"/>
  <html:hidden property="awardType" styleId="awardType"/>
  <html:hidden property="awardTypeName"/>
  <html:hidden property="goalLevelValueListSize"/>
  <html:hidden property="beforeGoalSelection"/>  
  <html:hidden property="inGoalSelection"/>  
  <html:hidden property="afterGoalSelection"/>  

  <input type="hidden" name="currentPayoutStructure"
                     value="<c:out value="${payoutForm.payoutStructure}"/>"/>

  <c:if test="${displayFlag}">
    <c:if test="${promotionType == 'challengepoint'}">
      <html:hidden property="awardThreshold"/>
      <html:hidden property="awardIncrement"/>
      <html:hidden property="primaryAwardPerIncrement"/>
      <html:hidden property="awardThresholdFixedAmount"/>
	  <html:hidden property="awardIncrementFixedAmount"/>
	  <html:hidden property="awardIncrementPctBaseAmount"/>
	</c:if>
    <html:hidden property="achievementRuleTypeCode"/>
    <html:hidden property="payoutStructure"/>
    <html:hidden property="achievementPrecision"/>
    <html:hidden property="roundingMethod"/>
  </c:if>
  <table border="0" cellpadding="0" cellspacing="0" width="100%">
    <tr>
      <td>
        <c:set var="promoTypeName" scope="request" value="${payoutForm.promotionTypeName}" />
        <c:set var="promoTypeCode" scope="request" value="${payoutForm.promotionTypeCode}" />
        <c:set var="promoName" scope="request" value="${payoutForm.promotionName}" />

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
            <td class="content-field-review"><c:out value="${payoutForm.awardTypeName}" /></td>
          </tr>
                    
          <tiles:insert attribute="challengePointFields" ignore="true"/>
          


          <tr class="form-row-spacer">
            <beacon:label property="achievementRuleTypeCode" required="true" styleClass="content-field-label-top">
              <cms:contentText key="ACHIEVEMENT_RULE" code="promotion.payout.goalquest"/>
            </beacon:label>
            <td class="content-field">
              <html:select property="achievementRuleTypeCode" styleClass="content-field" disabled="${displayFlag}">
                <html:option value=''><cms:contentText key="SELECT_ONE" code="system.general"/></html:option>
                <html:options collection="achievementRuleList" property="code" labelProperty="name" />
              </html:select>
            </td>
          </tr>
          <c:if test="${ promotionType == 'goalquest' && payoutForm.awardType == 'points'}">
            <tr class="form-row-spacer">
              <beacon:label property="payoutStructure" required="true" styleClass="content-field-label-top">
                <cms:contentText key="PAYOUT_STRUCTURE" code="promotion.payout.goalquest"/>
              </beacon:label>
              <td class="content-field">
                <html:select property="payoutStructure" styleClass="content-field" onchange="payoutStructureChange()" disabled="${displayFlag}">
                  <html:options collection="payoutStructureList" property="code" labelProperty="name" />
                </html:select>
              </td>
            </tr>
          </c:if>

          <tr class="form-row-spacer">
            <beacon:label property="achievementPrecision" required="true" styleClass="content-field-label-top">
              <cms:contentText key="ACHIEVEMENT_PRECISION" code="promotion.payout.goalquest"/>
            </beacon:label>
            <td class="content-field">
              <html:select property="achievementPrecision" styleClass="content-field" onchange="achievementPrecisionChange()" disabled="${displayFlag}">
                <html:options collection="achievementPrecisionList" property="code" labelProperty="name" />
              </html:select>
            </td>
          </tr>

          <tr class="form-row-spacer">
            <beacon:label property="roundingMethod" required="true" styleClass="content-field-label-top">
              <cms:contentText key="ROUNDING_METHOD" code="promotion.payout.goalquest"/>
            </beacon:label>
            <td class="content-field">
              <html:select property="roundingMethod" styleClass="content-field" disabled="${displayFlag}">
                <html:option value=''><cms:contentText key="SELECT_ONE" code="system.general"/></html:option>
                <html:options collection="roundingMethodList" property="code" labelProperty="name" />
              </html:select>
            </td>
          </tr>
           <tr class="form-row-spacer">
            <beacon:label property="baseUnit"  styleClass="content-field-label-top">
              <cms:contentText key="BASE_UNIT" code="promotion.payout.goalquest"/>
            </beacon:label>
            <td class="content-field">
              <html:text property="baseUnit" size="15" maxlength="15" styleClass="content-field"/>
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
        </table>
      </td>
    </tr>
    <c:if test="${payoutForm.awardType != 'points' || payoutForm.payoutStructure != ''}">
      <tr>
        <td colspan="2" align="center">
	      <tiles:insert attribute="payoutTable" />
        </td>
      </tr>
    </c:if>
	<c:if test="${not empty planningWorksheetMap}">
      <tr class="form-row-spacer">
        <td width="50%" valign="top">
          <table>
            <tr class="form-row-spacer">
              <td>&nbsp;</td>    
            <tr>
            <tr class="form-row-spacer">
              <td>
              <beacon:label required="true" styleClass="content-field-label-top">
                <cms:contentText key="GOAL_PLANNING_WORKSHEET" code="promotion.payout.goalquest"/>
              </beacon:label>
              </td>
              <td class="content-field">
                <html:select property="goalPlanningWorksheet" styleClass="content-field" onchange="planningWorksheetChange()" >
                  <html:option value=""><cms:contentText key="NONE" code="promotion.payout.goalquest"/></html:option>
                  <html:options collection="planningWorksheetMap" property="value" labelProperty="key"/>
                </html:select>
              </td>
              <td>
                <a target="_blank" id="goalWorksheetLink" href=""><cms:contentText key="PREVIEW" code="promotion.payout.goalquest"/></a> 
              </td>
            </tr>
          </table>
        </td>
      </tr> 
	</c:if>
	<%-- To Fix the Bug 20305 Page fault error in Goals & Payout page --%>
	<c:if test="${empty planningWorksheetMap}">
			<html:hidden property="goalPlanningWorksheet" value=""/>
			 <a target="_blank" id="goalWorksheetLink" href=""></a>
	</c:if>
	
    <tr>
      <td colspan="2" align="center"><tiles:insert attribute="promotion.footer" /></td>
    </tr>
  </table>
</html:form>
<script type="text/javascript">
  	<c:if test="${promotionType == 'challengepoint'}">
  		preDisableElements();
  		handleAwardIncrement();
  	</c:if>
  	planningWorksheetChange();
  	achievementPrecisionChange();
</script>
