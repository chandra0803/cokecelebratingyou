<%--UI REFACTORED--%>
<%--
UI REFACTORING Note: Header and footer pages needs refactoring if necessary.
As this doen't comes under any of our standard layouts and is specific to this page, changed the content wherever necessary as per refactoring requirements.
--%>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.domain.enums.BudgetFinalPayoutRule" %>

<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-en.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-setup.js"></script>

<c:set var="displayFlag" value="${promotionStatus == 'expired' }" />

<script type="text/javascript">
<!--
function callUrl( urlToCall )
{
  window.location=urlToCall;
}

function enableManagerOverride()
{
  document.promotionPayoutForm.managerOverridePercent.disabled = false;
}

function disableManagerOverride()
{
  document.promotionPayoutForm.managerOverridePercent.value = '';
  document.promotionPayoutForm.managerOverridePercent.disabled = true;
}

function selectOverrideRadio()
{
  for (i = 0; i < document.promotionPayoutForm.length; i++)
  {
    if (document.promotionPayoutForm.elements[i].name == 'managerOverride' &&
        document.promotionPayoutForm.elements[i].value == 'true' )
    {
      document.promotionPayoutForm.elements[i].checked = true;
    }
  }
}

function syncFields( field, fieldType, alreadyUpdated )
{
  if ( fieldType == 'start' )
  {
    subStringIndex = (field.name.length - 26);
  }
  else if ( fieldType == 'end' )
  {
    subStringIndex = (field.name.length - 24);
  }

  if ( fieldType == 'start' || fieldType == 'end' )
  {
    j=0;
    <c:forEach items="${promotionPayoutForm.promoPayoutGroupValueList}" var="group">
      h=0;
      <c:forEach items="${group.promoPayoutValueList}" var="payout">
        if ( findElement(getContentForm(), 'promoPayoutGroupValueList['+j+'].promoPayoutValueList['+h+'].categoryId').value ==
             findElement(getContentForm(), field.name.substring(0, subStringIndex)+'categoryId').value &&
             findElement(getContentForm(), 'promoPayoutGroupValueList['+j+'].promoPayoutValueList['+h+'].subcategoryId').value ==
             findElement(getContentForm(), field.name.substring(0, subStringIndex)+'subcategoryId').value &&
             findElement(getContentForm(), 'promoPayoutGroupValueList['+j+'].promoPayoutValueList['+h+'].productId').value ==
             findElement(getContentForm(), field.name.substring(0, subStringIndex)+'productId').value  )
        {
          if ( fieldType == 'start' )
          {
            findElement(getContentForm(), 'promoPayoutGroupValueList['+j+'].promoPayoutValueList['+h+'].productOrCategoryStartDate').value = field.value;
          }
          else
          {
            findElement(getContentForm(), 'promoPayoutGroupValueList['+j+'].promoPayoutValueList['+h+'].productOrCategoryEndDate').value = field.value;
          }
        }
        h++;
      </c:forEach>
      j++;
    </c:forEach>
  }
  else
  {
    var groupIndex = field.name.substring( (field.name.indexOf("[") + 1), field.name.indexOf("]") );
    var dependentArray = new Array();
    <c:forEach items="${promotionPayoutForm.groupDependentMap}" var="mapItem">
      var dependentGroup = new Array()
      <c:forEach items="${mapItem.value}" var="indexValue" varStatus="innerStatus">
        dependentGroup[<c:out value="${innerStatus.index}"/>] = <c:out value="${indexValue}"/>;
      </c:forEach>
      dependentArray[<c:out value="${mapItem.key}"/>] = dependentGroup;
    </c:forEach>

    var dependentGroups = dependentArray[groupIndex];
    var callUpdate = true;

    for (x=0; x<dependentGroups.length; x++)
    {
      for( i=0; i<alreadyUpdated.length; i++ )
      {
        if ( alreadyUpdated[i] == groupIndex )
        {
          callUpdate = false;
        }
      }
      if ( alreadyUpdated != null )
      {
        alreadyUpdated[alreadyUpdated.length] = groupIndex;
      }
      else
      {
        alreadyUpdated[0] = groupIndex;
      }

      if ( fieldType == 'minQualifier' )
      {
        findElement(getContentForm(), 'promoPayoutGroupValueList['+dependentGroups[x]+'].minimumQualifier').value = field.value;

        if ( callUpdate )
        {
          syncFields( findElement(getContentForm(), 'promoPayoutGroupValueList['+dependentGroups[x]+'].minimumQualifier'), 'minQualifier', alreadyUpdated );
        }
      }
      else
      {
        findElement(getContentForm(), 'promoPayoutGroupValueList['+dependentGroups[x]+'].retroPayout').value = field.value;
        if ( callUpdate )
        {
          syncFields( findElement(getContentForm(), 'promoPayoutGroupValueList['+dependentGroups[x]+'].retroPayout'), 'retro', alreadyUpdated );
        }
      }
    }
  }
  return false;
}

function syncAllFields( field, groupIndex )
{
  if ( field.checked )
  {
    alert('<cms:contentText code="promotion.payout" key="MIN_QUAL_RETRO_MESSAGE"/>');
    syncChildFields( findElement(getContentForm(), 'promoPayoutGroupValueList['+groupIndex+'].minimumQualifier'), 'minQualifier', new Array() );
    syncChildFields( findElement(getContentForm(), 'promoPayoutGroupValueList['+groupIndex+'].retroPayout'), 'retro', new Array() );
  }
  return false;
}

function syncChildFields( field, fieldType, alreadyUpdated )
{
  var groupCount = 0;
  for( i=0; i<getContentForm().elements.length; i++ )
  {
    if ( getContentForm().elements[i].name.substring( (getContentForm().elements[i].name.indexOf(".") + 1) ) == 'promoPayoutGroupId' )
    {
      groupCount++;
    }
  }

  var groupProdArray = new Array();
  for( x=0; x<groupCount; x++ )
  {
    var payoutArray = new Array();
    var payoutArrayIndex = 0;
    for( i=0; i<getContentForm().elements.length; i++ )
    {
      if ( getContentForm().elements[i].name.substring(
        (getContentForm().elements[i].name.indexOf("[") + 1),
         getContentForm().elements[i].name.indexOf("]") ) == x )
      {
        if ( getContentForm().elements[i].name.substring( (getContentForm().elements[i].name.lastIndexOf(".") + 1) ) == 'includePayout' )
        {
          if ( getContentForm().elements[i].checked )
          {
            var payoutIndex = getContentForm().elements[i].name.substring(
                           (getContentForm().elements[i].name.lastIndexOf("[") + 1),
                            getContentForm().elements[i].name.lastIndexOf("]") );
            var categoryId = findElement(getContentForm(), 'promoPayoutGroupValueList['+x+'].promoPayoutValueList['+payoutIndex+'].categoryId').value;
            var subcategoryId = findElement(getContentForm(), 'promoPayoutGroupValueList['+x+'].promoPayoutValueList['+payoutIndex+'].subcategoryId').value;
            var productId = findElement(getContentForm(), 'promoPayoutGroupValueList['+x+'].promoPayoutValueList['+payoutIndex+'].productId').value;

            var payoutString = categoryId+'|'+subcategoryId+'|'+productId;

            payoutArray[payoutArrayIndex] = payoutString;
            payoutArrayIndex++;
          }
        }
      }
    }
    groupProdArray[x] = payoutArray;
  }


  var dependentArray = new Array(groupProdArray.length);


  for ( x=0; x<dependentArray.length; x++ )
  {
    dependentArray[x] = new Array();
  }

  for ( x=0; x<groupProdArray.length; x++ )
  {
    var currentPayoutArray = groupProdArray[x];
    for ( y=0; y<currentPayoutArray.length; y++ )
    {
      var currentPayoutString = currentPayoutArray[y];
      for ( i=x+1; i<groupProdArray.length; i++ )
      {
        compareArray = groupProdArray[i];
        for ( z=0; z<compareArray.length; z++ )
        {
          comparePayoutString = compareArray[z];
          if ( currentPayoutString == comparePayoutString )
          {
            dependentArray[x][dependentArray[x].length] = i;
            dependentArray[i][dependentArray[i].length] = x;
            break;
          }
        }
      }
    }
  }

    var groupIndex = field.name.substring( (field.name.indexOf("[") + 1), field.name.indexOf("]") );
    var dependentGroups = dependentArray[groupIndex];
    var callUpdate = true;

    for (x=0; x<dependentGroups.length; x++)
    {
      for( i=0; i<alreadyUpdated.length; i++ )
      {
        if ( alreadyUpdated[i] == groupIndex )
        {
          callUpdate = false;
        }
      }
      if ( alreadyUpdated != null )
      {
        alreadyUpdated[alreadyUpdated.length] = groupIndex;
      }
      else
      {
        alreadyUpdated[0] = groupIndex;
      }

      if ( fieldType == 'minQualifier' )
      {
        findElement(getContentForm(), 'promoPayoutGroupValueList['+dependentGroups[x]+'].minimumQualifier').value = field.value;

        if ( callUpdate )
        {
          syncChildFields( findElement(getContentForm(), 'promoPayoutGroupValueList['+dependentGroups[x]+'].minimumQualifier'), 'minQualifier', alreadyUpdated );
        }
      }
      else
      {
        findElement(getContentForm(), 'promoPayoutGroupValueList['+dependentGroups[x]+'].retroPayout').value = field.value;
        if ( callUpdate )
        {
          syncChildFields( findElement(getContentForm(), 'promoPayoutGroupValueList['+dependentGroups[x]+'].retroPayout'), 'retro', alreadyUpdated );
        }
      }
    }
    //alert('dependentArray = '+dependentArray);
}

function payoutTypeChange()
{
var url = 'promotionPayout.do?payoutType=' + document.promotionPayoutForm.payoutType.value;
url = url + '&oldPayoutType=' + document.promotionPayoutForm.currentPayoutType.value;
url = url + '&id=' + document.promotionPayoutForm.promotionId.value;
url = url + '&method=payoutTypeChange';
setActionDispatchAndSubmit(url,'payoutTypeChange');
}

function showLayer(whichLayer)
{
  if (document.getElementById)
  {
    if(document.getElementById(whichLayer) != null)
    {
    // this is the way the standards work
    var style2 = document.getElementById(whichLayer).style;
    style2.display = "";

    if( whichLayer == "newBudget")
	  {
	  	//the if block will disabled the budgetMAsterId dropdown and default its value to choose one
	  	//when create budget is selected
  	  if ( $("input:radio[name='budgetOption']:checked").val()== "new")
 		   {
 		    $("select[name='budgetMasterId'] option[value='']").attr('selected', 'selected');
 			$("select[name='budgetMasterId']").attr('disabled', 'disabled');
 		   }else if($("input:radio[name='budgetOption']:checked").val()== "existing")
 		   {
 		   	 $("select[name='budgetMasterId']").attr('disabled', '');
    	   }
  		}
    }
  }
  else if (document.all)
  {
    if(document.getElementById(whichLayer) != null)
    {
    // this is the way old msie versions work
      var style2 = document.all[whichLayer].style;
      style2.display = "block";
  }
  }
  else if (document.layers)
  {
    if(document.getElementById(whichLayer) != null)
    {
    // this is the way nn4 works
      var style2 = document.layers[whichLayer].style;
      style2.display = "block";
    }
  }
}

function hideLayer(whichLayer)
{
  if (document.getElementById)
  {
    if(document.getElementById(whichLayer) != null)
    {
      // this is the way the standards work
      var style2 = document.getElementById(whichLayer).style;
      style2.display = "none";

      if( whichLayer == "newBudget")
	  {
	  	//the if block will disabled the budgetMAsterId dropdown and default its value to choose one when no budget is selected
    	  if ( $("input:radio[name='budgetOption']:checked").val()== "none")
   		   {
   		    $("select[name='budgetMasterId'] option[value='']").attr('selected', 'selected');
   			$("select[name='budgetMasterId']").attr('disabled', 'disabled');
   		   }else if($("input:radio[name='budgetOption']:checked").val()== "existing")
   		   {
   		   	 $("select[name='budgetMasterId']").attr('disabled', '');
       	   }
	  }
  }
  }
  else if (document.all)
  {
    if(document.getElementById(whichLayer) != null)
    {
    // this is the way old msie versions work
      var style2 = document.all[whichLayer].style;
      style2.display = "none";
  }
  }
  else if (document.layers)
  {
    if(document.getElementById(whichLayer) != null)
    {
    // this is the way nn4 works
      var style2 = document.layers[whichLayer].style;
      style2.display = "none";
  }
  }
}

//onclick of the radio allowDefaultQuantity button, if the selection is true, the defaultQuantity field is cleared and disabled.
function disableDefaultQuantity(disabled)
{
	if(disabled == 'true')
	{
		$("input[name='defaultQuantity']").attr('disabled', 'disabled');
		$("input[name='defaultQuantity']").val("");
	}
	else
	{
	  $("input[name='defaultQuantity']").attr('disabled', '');
	}
}

function updateLayerRemoveBudgetSegment(){
	  var count = $("input[name='budgetSegmentVBListSize']").val();
	  for(i=0; i<= count; i++){
		  if( i != 0 ){
			  if( i+1 == count ){
					 showLayer('removeBudgetSegment'+i);
				  }else{
					  hideLayer('removeBudgetSegment'+i);
				  }
	  		}else{
	  			 hideLayer('removeBudgetSegment0');
	  		}
		  };
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
<html:hidden property="promoPayoutGroupValueListCount"/>
<html:hidden property="promoStackRankPayoutGroupValueListCount"/>
<html:hidden property="promoPayoutValueListCount"/>
<html:hidden property="promoStackRankPayoutValueListCount"/>
<html:hidden property="prevPayoutType"/>
<html:hidden property="hasParent"/>
<html:hidden property="teamUsed"/>
<html:hidden property="awardType" styleId="awardType"/>
<html:hidden property="awardTypeName"/>
<html:hidden property="promotionSubmitStartDate"/>
<html:hidden property="promotionSubmitEndDate"/>
<html:hidden property="budgetSegmentVBListSize"/>

<input type="hidden" name="currentPayoutType"
                     value="<c:out value="${promotionPayoutForm.payoutType}"/>"/>

   <table border="0" cellpadding="0" cellspacing="0" width="100%">
    <tr>
      <td>
      <c:set var="promoTypeName" scope="request" value="${promotionPayoutForm.promotionTypeName}" />
      <c:set var="promoTypeCode" scope="request" value="${promotionPayoutForm.promotionTypeCode}" />
        <c:set var="promoName" scope="request" value="${promotionPayoutForm.promotionName}" />

      <tiles:insert attribute="promotion.header" />
    </td>
  </tr>
    <tr>
    <td>
        <cms:errors/>
      </td>
  </tr>

  <c:if test="${promotionPayoutForm.hasParent}">
    <tr>
        <td colspan="3">
          <table>
            <tr>
              <td class="subheadline"><cms:contentText code="promotion.payout" key="PARENT_PROMO"/>
              <td>
                <DIV id="showSection">
                  <table>
                    <tr>
                      <td class="content-link"><a href="javascript:showLayer('parentPayoutSection');showLayer('hideSection');hideLayer('showSection');" class="content-link"><cms:contentText code="ssi_contest.creator" key="SHOW_DETAILS" /></a></td>
                    </tr>
                  </table>
                </DIV>
                <DIV id="hideSection">
                  <table>
                    <tr>
                      <td><a href="javascript:hideLayer('parentPayoutSection');hideLayer('hideSection');showLayer('showSection');" class="content-link"><cms:contentText code="ssi_contest.creator" key="HIDE_DETAILS" /></a></td>
                    </tr>
                  </table>
                </DIV>
              </td>
            </tr>
          </table>
        </td>
      </tr>
    <tr>
      <td width="50%" valign="top">
        <%-- Parent Section --%>
        <DIV id="parentPayoutSection">
          <table>
              <tr class="form-row-spacer">
                <td></td>
                 <td class="content-field-label">
                   <cms:contentText key="PAYOUT_TYPE" code="promotion.payout"/>
                 </td>
          <td class="content-field-review">
              <c:out value="${promotion.payoutType.name}"/>
            </td>
            </tr>
              <tr>
                <td></td>
                <td class="content-field-label"><cms:contentText code="promotion.payout" key="TYPE"/></td>
                <%-- <td class="content-field-review"><c:out value="${promotion.awardType.name}" /></td>--%>
                <td class="content-field-review"><c:out value="${promotionPayoutForm.awardTypeName}" /></td>
              </tr>

            <tiles:insert attribute="parentPayoutTable" />
              <c:if test="${promotion.payoutType.code == 'stack_rank'}">
              <tiles:insert attribute="parentPayoutStackRankBottomTable" />
         </c:if>

       <c:if test="${promotion.payoutType.code != 'stack_rank'}">
          <tr class="form-row-spacer">
            <td></td>
            <td class="content-field-label">
                <cms:contentText key="MANAGER_OVERRIDE" code="promotion.payout"/>
              </td>
              <td class="content-field-review">
                <c:choose>
                    <c:when test="${promotion.payoutManager}">
                <c:out value="${promotion.payoutManagerPercent}"/>&nbsp;
                <cms:contentText code="promotion.payout" key="PERCENT_PAID"/>&nbsp;
                <c:out value="${promotion.payoutManagerPeriod.name}"/>
              </c:when>
              <c:otherwise>
                <cms:contentText code="promotion.payout" key="NONE"/>
              </c:otherwise>
            </c:choose>
            </td>
          </tr>
          <tr class="form-row-space">
          	<td></td>
          	<td class="content-field-label">
    			<cms:contentText key="BREAK_BANK_BUDGET" code="promotion.payout"/>
  			</td>
  			<td class="content-field-review">
				<c:if test="${promotion.budgetMaster == null}">
					<cms:contentText code="promotion.payout" key="BUDGET_NO"/>
        </c:if>
				<c:if test="${promotion.budgetMaster != null}">
					<cms:contentText code="${promotion.budgetMaster.cmAssetCode}" key="${promotion.budgetMaster.nameCmKey}"/>
				</c:if>
  			</td>
          </tr>
        </c:if>

          <c:if test="${promotion.payoutType.code == 'tiered'}">
            <tr class="form-row-spacer">
              <td></td>
             <td class="content-field-label">
                  <cms:contentText key="CARRYOVER_ALLOWED" code="promotion.payout"/>
                </td>
            <td class="content-field-review">
              <c:choose>
                <c:when test="${promotion.payoutCarryOver}">
                <cms:contentText key="YES" code="system.common.labels"/>
              </c:when>
              <c:otherwise>
                <cms:contentText key="NO" code="system.common.labels"/>
              </c:otherwise>
            </c:choose>
            </td>
            </tr>
               </c:if>
        </table>
      </DIV>
      <%-- END Parent Section --%>
      </td>
    </tr>
  </c:if>


  <tr>
    <td width="50%" valign="top">
    <table>
        <c:if test="${promotionPayoutForm.hasParent}">
          <tr>
              <td colspan="2" class="subheadline"><cms:contentText code="promotion.payout" key="CHILD_PROMOTION"/></td>
            </tr>
          </c:if>
          <tr class="form-row-spacer">
               <beacon:label property="payoutType" required="true" styleClass="content-field-label-top">
                <cms:contentText key="PAYOUT_TYPE" code="promotion.payout"/>
              </beacon:label>

        <% String payoutTypeDisabled = "false"; %>

        <c:if test="${promotionPayoutForm.promotionStatus == 'live'}">
          <% payoutTypeDisabled = "true"; %>
          <html:hidden property="payoutType"/>
        </c:if>

            <td class="content-field">
            <html:select property="payoutType" styleClass="content-field" onchange="payoutTypeChange()" disabled="<%=payoutTypeDisabled%>">
                <html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>
                <html:options collection="payoutTypeList" property="code" labelProperty="name" />
            </html:select>
            </td>
        </tr>
          <tr>
            <td>&nbsp;</td>
            <td class="content-field-label"><cms:contentText code="promotion.payout" key="TYPE"/></td>
            <td class="content-field-review"><c:out value="${promotionPayoutForm.awardTypeName}" /></td>
          </tr>

    <c:choose>
      <c:when test="${promotionPayoutForm.payoutType != ''}">

        <tiles:insert attribute="payoutTable" />
        <c:if test="${promotionPayoutForm.payoutType == 'stack_rank'}">
          <tiles:insert attribute="payoutStackRankBottomTable" />
    </c:if>
      <tr class="form-blank-row">
            <td></td>
          </tr>

    <tr class="form-row-spacer">
	  <beacon:label required="true" styleClass="content-field-label-top">
		<cms:contentText key="ALLOW_DEFAULT_QUANTITY" code="promotion.payout" />
	  </beacon:label>
	  <td colspan=2 class="content-field">
		<table>
		  <tr>
			<td class="content-field">
				<html:radio property="allowDefaultQuantity" value="true" disabled="${displayFlag}" onclick="disableDefaultQuantity('false');" />
			</td>
			<td class="content-field">
			  <table>
				<tr>
				  <td>
					<cms:contentText code="system.common.labels" key="YES" />,&nbsp;
				  </td>
				  <beacon:label property="defaultQuantity" styleClass="content-field">
					<cms:contentText key="DEFAULT_QUANTITY" code="promotion.payout" />
				  </beacon:label>
				  <td>
					&nbsp;<html:text property="defaultQuantity" size="4" maxlength="4" disabled="${displayFlag}" />
				  </td>
				</tr>
			  </table>
			</td>
		  </tr>
		  <tr>
			<td class="content-field">
				<html:radio property="allowDefaultQuantity" value="false" disabled="${displayFlag}" onclick="disableDefaultQuantity('true');" />
			</td>
			<td class="content-field">
				<cms:contentText code="system.common.labels" key="NO" />
			</td>
		  </tr>
		</table>
	  </td>
	</tr>

      <c:if test="${promotionPayoutForm.payoutType != 'stack_rank'}">
      <tr class="form-row-spacer">
        <beacon:label property="managerOverridePercent" required="true" styleClass="content-field-label-top">
                <cms:contentText key="MANAGER_OVERRIDE" code="promotion.payout"/>
          </beacon:label>

        <td class="content-field-label">
        <html:radio property="managerOverride" value="false" onclick="disableManagerOverride();"/><cms:contentText code="promotion.payout" key="NONE"/>
          </td>
      </tr>

      <tr class="form-row-spacer">
        <td colspan="2">&nbsp;</td>
        <td class="content-field-label">
        <html:radio property="managerOverride" value="true" onclick="enableManagerOverride();"/>
        <html:text property="managerOverridePercent" styleClass="content-field" size="5" maxlength="10" onclick="selectOverrideRadio();"/>
        &nbsp;<cms:contentText code="promotion.payout" key="PERCENT_PAID"/>&nbsp;
        <html:select property="managerOverrideFrequency" styleClass="content-field">
          <html:options collection="managerPayoutFreqList" property="code" labelProperty="name" />
        </html:select>
        </td>
      </tr>

       <c:if test="${promotionPayoutForm.payoutType == 'tiered'}">
      <tr class="form-blank-row">
            <td></td>
          </tr>

      <tr class="form-row-spacer">
           <beacon:label property="carryoverAllowed" required="true" styleClass="content-field-label-top">
                <cms:contentText key="CARRYOVER_ALLOWED" code="promotion.payout"/>
              </beacon:label>
          <td class="content-field-label">
          <html:radio property="carryoverAllowed" value="false"/>&nbsp;<cms:contentText key="NO" code="system.common.labels"/>
          </td>
      </tr>
      <tr class="form-row-spacer">
        <td colspan="2">&nbsp;</td>
        <td class="content-field-label">
        <html:radio property="carryoverAllowed" value="true"/>&nbsp;<cms:contentText key="YES" code="system.common.labels"/>
        </td>
      </tr>
         </c:if>

       <%-- Break the Bank Budget --%>
       <tr class="form-row-spacer" id="budgetInfo">
	  	<beacon:label property="budgetOption" required="true" styleClass="content-field-label-top">
	    	<cms:contentText key="BREAK_BANK_BUDGET" code="promotion.payout" />
	  	</beacon:label>
	  <td>
	    <table> <%--  radio buttons table --%>
	      <tr>
	        <td class="content-field" nowrap valign="top">
	          <html:radio styleId="budgetOptionNone" property="budgetOption" value="none" disabled="${displayFlag}" onclick="hideLayer('newBudget');hideLayer('finalPayout');" />
	          <cms:contentText code="promotion.payout" key="BUDGET_NO" />
	        </td>
	      </tr>
		  <c:if test="${promotionPayoutForm.hasParent}">
		  	<tr>
		  		<td class="content-field" nowrap valign="top">
		  			<html:radio styleId="budgetOptionSameAsParent" property="budgetOption" value="sameAsParent" disabled="${displayFlag}" onclick=""/>
		  			<cms:contentText code="promotion.payout" key="SAME_AS_PARENT"/>
		  		</td>
		  	</tr>
        </c:if>
	      <tr>
	        <td colspan="2" class="content-field" nowrap valign="top">
	          <html:radio styleId="budgetOptionExists" property="budgetOption" value="existing" disabled="${displayFlag}" onclick="hideLayer('newBudget');hideLayer('finalPayout');" />
	          <cms:contentText code="promotion.payout" key="BUDGET_EXISTING_CENTRAL" /> &nbsp;
	          <html:select styleId="budgetMasterId" property="budgetMasterId" styleClass="content-field" disabled="${displayFlag}">
	            <html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general" /></html:option>
	            <html:options collection="budgetMasterList" property="budgetMasterId" labelProperty="budgetMasterName" />
	          </html:select>
	        </td>
	      </tr>

	      <tr>
	        <td class="content-field" nowrap valign="top">
	          <html:radio styleId="budgetOptionNew" property="budgetOption" value="new" disabled="${displayFlag}" onclick="showLayer('newBudget');showLayer('finalPayout');updateLayerRemoveBudgetSegment();"/>
	          <cms:contentText code="promotion.payout" key="BUDGET_CREATE_CENTRAL" />
	        </td>
	      </tr>

	      <%--  new budget row  --%>
	      <tr>
	        <td>
	          <table id="newBudget" ><%--  new budget fields --%>
	            <tr>
	              <td></td>
	              <td>
	                <table>
	                  <tr class="form-row-spacer">
	                    <beacon:label property="budgetMasterName" required="true">
	                      <cms:contentText key="BUDGET_MASTER_NAME" code="promotion.payout" />
	                    </beacon:label>

	                    <td  class="content-field-label">
	                      <table>
	                        <tr>
	                          <td class="content-field" valign="top" >
	                          	<html:text styleId="budgetMasterName" property="budgetMasterName" size="20" styleClass="content-field" disabled="${displayFlag}" />
	                          </td>
	                        </tr>
	                      </table>
	                    </td>
	                  </tr>

	                  <%-- Budget Type --%>
	                  <tr class="form-row-spacer">
	                    <beacon:label property="budgetType" required="true" styleClass="content-field-label-top">
	                      <cms:contentText key="BUDGET_TYPE" code="promotion.payout" />
	                    </beacon:label>

	                    <td  class="content-field-label">
	                      <table>
	                        <tr>
	                          <td class="content-field" valign="top">
	                            <html:hidden property="budgetType" value="central" disabled="${displayFlag}" />
	                            <cms:contentText code="promotion.payout" key="BUDGET_TYPE_CENTRAL" />
	                          </td>
	                        </tr>
	                      </table>
	                    </td>
	                  </tr>

	                  <%-- Budget Cap Type --%>
	                  <tr class="form-row-spacer">
	                    <beacon:label property="budgetCapType" required="true" styleClass="content-field-label-top">
	                      <cms:contentText key="CAP_TYPE" code="promotion.payout" />
	                    </beacon:label>

	                    <td  class="content-field-label">
	                     <%-- <c:choose> --%>
	                          <html:hidden property="budgetCapType" value="hard"/>
	                          <c:out value="${hardCapBudgetType.name}"/>
	                     <%-- </c:choose>--%>
	                    </td>
	                  </tr>

	                 <tr class="form-row-spacer">
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
		          <tr class="form-row-spacer">
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
						 		<c:if test="${ promotionPayoutForm.budgetSegmentVBListSize ne '1'}">
						 		<th class="crud-table-header-row" id="removeBudgetSegment"><cms:contentText code="system.general" key="CRUD_REMOVE_LABEL"/></th>
						 		</c:if>
						    </tr>
							<c:set var="switchColor" value="false"/>
							<%		  	 	int sIndex = 0; %>
						  	<c:forEach var="budgetSegmentVBList" items="${promotionPayoutForm.budgetSegmentVBList}" varStatus="status" >
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
								<td class="crud-content" id="newBudgetCentralValue">
							    	<html:text property="originalValue" size="10" maxlength="10" indexed="true" styleId="originalValue" name="budgetSegmentVBList" styleClass="content-field" />
								</td>
								<td class="crud-content">
					            	<table id="<%=removeBudgetSegmentCounter%>">
					            	<tr>
					            	<td align="right">
							        <html:button property="remove" styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('promotionPayout.do', 'removeBudgetSegment')" >
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
						     	  		<a id="addAnotherhref"  href="#" onclick="setActionDispatchAndSubmit('promotionPayout.do','addAnotherSegment');" >
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
	</tr> <%--  budgetInfo --%>
		<table id="finalPayout">
			<tr class="form-row-spacer" id="finalPayoutRule_radio">
	            <beacon:label property="finalPayoutRule" required="true" styleClass="content-field-label-top">
	              <cms:contentText key="FINAL_PAYOUT_RULE" code="admin.budgetmaster.details"/>
	            </beacon:label>
	            <td>
	              <table id="finalPayout">
				      <tr>
				        <td nowrap class="content-field content-field-label-top">
				          <c:forEach items="${budgetFinalPayoutRuleList}" var="budgetFinalPayoutRule">
				            <html:radio property="finalPayoutRule" value="${budgetFinalPayoutRule.value}" onclick=""/>&nbsp;<c:out value="${budgetFinalPayoutRule.label}"/><br>
				          </c:forEach>
				        </td>
				      </tr>
			      </table>
	            </td>
	        </tr>
		</table>
       <%-- END Break the Bank Budget --%>
    </c:if>
         </c:when>
        <c:otherwise>
        <tr class="form-row-spacer">
          <td>&nbsp;</td>
          <td class="content-field-label"><cms:contentText code="promotion.payout" key="PRODUCTS"/></td>
          <td class="content-field-review"><cms:contentText code="promotion.payout" key="NOTHING_FOUND"/></td>
        </tr>
        </c:otherwise>
      </c:choose>
      </table>
      </td>
    </tr>
  </table>
  <tiles:insert attribute="promotion.footer" />
</html:form>
<SCRIPT language="JavaScript" type="text/javascript">
  //Begin inline javascript  (because window.onload conflicts with menu.js call to window.onload)
  hideLayer("hideSection");
  hideLayer("parentPayoutSection");

  //**** Break the Bank Budget ****
  hideLayer("budgetInfo");
  hideLayer("newBudget");
  hideLayer("finalPayout");

  var budgetOptionNoneObj = document.getElementById("budgetOptionNone");
  var budgetOptionExistsObj = document.getElementById("budgetOptionExists");
  var budgetOptionSameAsParentObj = document.getElementById("budgetOptionSameAsParent");
  var budgetMasterIdObj = document.getElementById("budgetMasterId");
  var budgetMasterNameObj = document.getElementById("budgetMasterName");
  var awardsTypeObj = document.getElementById("awardType");
  destination = awardsTypeObj.value;
  if( destination == 'points' )
{
    showLayer("budgetInfo");
  }
  else
  {
    hideLayer("budgetInfo");
  }
  var budgetOptionNewObj = document.getElementById("budgetOptionNew");
  if( budgetOptionNewObj != null && budgetOptionNewObj.checked==true )
  {
    showLayer("newBudget");
    showLayer("finalPayout");
    updateLayerRemoveBudgetSegment();
  }
  else
  {
    hideLayer("newBudget");
    hideLayer("finalPayout");
  }
  var disableBudget = false;
  // if promotion is live then disable all of the budget fields
	<c:if test="${ (promotionStatus == 'live') || (promotionStatus=='expired') }">
  		disableBudget = true;
</c:if>
	if (budgetOptionNoneObj != null)
	{
   		budgetOptionNoneObj.disabled=disableBudget;
   	}
   	if (budgetOptionExistsObj != null)
   	{
   		budgetOptionExistsObj.disabled=disableBudget;
   	}
   	if (budgetOptionNewObj != null)
   	{
   		budgetOptionNewObj.disabled=disableBudget;
   	}
   	if (budgetOptionSameAsParentObj != null)
   	{
   		budgetOptionSameAsParentObj.disabled=disableBudget;
   	}
   	if (budgetMasterIdObj != null)
   	{
   		budgetMasterIdObj.disabled=disableBudget;
   	}
   	if (budgetMasterNameObj != null)
   	{
   		budgetMasterNameObj.disabled=disableBudget;
    }
  //**** END Break the Bank Budget ****

  var payoutCalculation = document.getElementById("payoutCalculation");

enableFields();

function enableFields()
{
	var disabled = false;
	var activeFalseObj = document.getElementById("activeFalse");

	if(activeFalseObj != null)
	{
		disabled = activeFalseObj.checked == true;
	}
	// if promotion is expired then disabled = true;
	<c:if test="${promotionStatus=='expired'}">
	  disabled = true;
	</c:if>

    payoutCalculation.disabled = disabled;
    if(disabled)
	{
	   	hideLayer("addAnotherLayer");
	   	hideLayer("generatePayoutLayer")
	}
	else
	{
		showLayer("addAnotherLayer");
		if( disableGeneratePayout != null && disableGeneratePayout.length != 0 )
		{
			showLayer("generatePayoutLayer");
		}
	}
    if( $("input:radio[name='managerOverride']:checked").val()== "true" )
	{
		enableManagerOverride();
	}else{
		disableManagerOverride();
	}
}
</SCRIPT>

<SCRIPT type="text/javascript">
function addAnotherSegment(method)
{
  document.promotionAwardsForm.method.value=method;
  document.promotionAwardsForm.action = "promotionPayout.do";
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
