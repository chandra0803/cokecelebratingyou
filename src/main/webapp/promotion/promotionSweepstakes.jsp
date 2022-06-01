
<%--UI REFACTORED--%>
<%@ include file="/include/taglib.jspf"%>

<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript">
  function showLayer(whichLayer)
  {
	if (document.getElementById)
    {
      // this is the way the standards work
      var style2 = document.getElementById(whichLayer).style;
      style2.display = "block";
    }
    else if (document.all)
    {
      // this is the way old msie versions work
      var style2 = document.all[whichLayer].style;
      style2.display = "block";
    }
    else if (document.layers)
    {
      // this is the way nn4 works
      var style2 = document.layers[whichLayer].style;
      style2.display = "block";
    }
  }
  function hideLayer(whichLayer)
  {
	if (document.getElementById)
    {
      // this is the way the standards work
      var style2 = document.getElementById(whichLayer).style;
      style2.display = "none";
    }
    else if (document.all)
    {
      // this is the way old msie versions work
      var style2 = document.all[whichLayer].style;
      style2.display = "none";
    }
    else if (document.layers)
    {
      // this is the way nn4 works
      var style2 = document.layers[whichLayer].style;
      style2.display = "none";
    }
  }
	
  function displayEnabledDivs()
  {
	var selectedOption = null;
	var selectBox = document.getElementById('eligibleWinners');
    for (var i = 0; i < selectBox.options.length; i++)
    {
	  	if(selectBox.options[i].selected)
	  	{
				selectedOption = selectBox.options[i].value
                break;
         }
    }
    <c:if test="${promotionSweepstakesForm.promotionTypeCode == 'survey'}">     
      hideLayer("surveyGivers");
	  
	  if( selectedOption == "paxSelectedSurvey" )
	  {
	    showLayer("surveyGivers");
	  }
	  else
      {
		  $("select[name='eligibleWinners'] option[value='']").attr('selected', 'selected');
	      $("select[name='eligibleWinners']").attr('disabled', 'disabled');
      }
    </c:if>
    
    <c:if test="${promotionSweepstakesForm.promotionTypeCode == 'badge'}">     
    hideLayer("badgeReceivers");
	  
	  if( selectedOption == "badgereceiver" )
	  {
	    showLayer("badgeReceivers");
	  }
	  else
      {
		$("select[name='eligibleWinners'] option[value='']").attr('selected', 'selected');
      }
  </c:if>
    
    hideLayer("givers");
    hideLayer("receivers");
    hideLayer("giversAndReceivers");
		hideLayer("fullAwards");
		hideLayer("trimmedAwards");
		hideLayer("trimmedAwards");
        
		if( selectedOption == "givers" )
		{
	  	showLayer("givers");
	  	showLayer("trimmedAwards");
		}
		if( selectedOption == "receivers" )
		{
	  	showLayer("receivers");
	  	showLayer("trimmedAwards");
		}
		if( selectedOption == "separate" )
		{
      showLayer("givers");
      showLayer("receivers");
	  	showLayer("fullAwards");
		}
		if( selectedOption == "combined" )
		{
      showLayer("giversAndReceivers");
      showLayer("trimmedAwards");
		}
	  
  <c:if test="${promotionSweepstakesForm.promotionTypeCode == 'product_claim'}">     
	  if( selectedOption == "submittersdraw" )
	  {
			showLayer("givers");
			showLayer("trimmedAwards");
	  }
	  if( selectedOption == "teammembersdraw" )
	  {
			showLayer("receivers");
			showLayer("fullAwards");
	  }
	  if( selectedOption == "separatedraw" )
	  {
	    showLayer("givers");
	    showLayer("receivers");
			showLayer("fullAwards");
	  }
	  if( selectedOption == "combineddraw" )
	  {
      showLayer("giversAndReceivers");
			showLayer("trimmedAwards");
      }
	</c:if>	  
	<c:if test="${promotionSweepstakesForm.promotionTypeCode == 'nomination'}">     
	  if( selectedOption == "nominators" )
	  {
		  showLayer("givers");
		  showLayer("trimmedAwards");
	  }
	  if( selectedOption == "nominees" )
	  {
			showLayer("receivers");
			showLayer("trimmedAwards");
	  }
	  if( selectedOption == "nomseparate" )
	  {
	    showLayer("givers");
	    showLayer("receivers");
			showLayer("fullAwards");
	  }
	  if( selectedOption == "nomcombined" )
	  {
      showLayer("giversAndReceivers");
			showLayer("trimmedAwards");
	  }
  	
	</c:if>	  
  }
  
  function noSweepStakes()
  {
    var eligibleWinnersObj = document.getElementById("eligibleWinners");
    if(eligibleWinnersObj != null)
    {
      eligibleWinnersObj.value = '';
      displayEnabledDivs();
    }
    enableFields();
  }
</SCRIPT>


<html:form styleId="contentForm" action="promotionSweepstakesSave">
  <html:hidden property="method" value=""/>
  <html:hidden property="promotionName"/>
  <html:hidden property="promotionTypeName"/>
  <html:hidden property="promotionTypeCode"/>
  <html:hidden property="awardTypeCode" />
  <html:hidden property="version" />
  <html:hidden property="awardTypeText" />
	<beacon:client-state>
		<beacon:client-state-entry name="promotionId" value="${promotionSweepstakesForm.promotionId}"/>
	</beacon:client-state>

  <table class="crud-table" width="100%" cellpadding="3" cellspacing="1" >
	<tr>
	  <td colspan="2">
	    <c:set var="promoTypeName" scope="request" value="${promotionSweepstakesForm.promotionTypeName}" />
  	    <c:set var="promoTypeCode" scope="request" value="${promotionSweepstakesForm.promotionTypeCode}" />	    
  	    <c:set var="promoName" scope="request" value="${promotionSweepstakesForm.promotionName}" />
	    <tiles:insert attribute="promotion.header" />
	  </td>
	</tr>
    <tr>
      <td colspan="2">
      	<cms:errors />
      </td>
    </tr>
    <tr>
      <td width="15">&nbsp;</td>
	  <td valign="top">
		<table>
        <c:if test="${promotionSweepstakesForm.promotionTypeCode == 'product_claim' }">
          <%@include file="/promotion/productclaim/promotionSweepstakesChildTop.jsp" %>
        </c:if>
        
        <c:if test="${promotionSweepstakesForm.promotionTypeCode != 'badge' }">
          <tr class="form-row-spacer">
            <beacon:label property="active" required="true" styleClass="content-field-label-top" >
              <cms:contentText key="SWEEPSTAKES_ACTIVE" code="promotion.sweepstakes"/>
            </beacon:label>          
            <td class="content-field content-field-label-top">
              <html:radio styleId="activeFalse" property="active" value="false" onclick="noSweepStakes();" disabled="${promotionStatus=='expired' or isActiveNotEditable}"/>&nbsp;&nbsp;<cms:contentText key="NO" code="system.common.labels" />
               <br/>
              <html:radio styleId="activeTrue" property="active" value="true" onclick="enableFields();" disabled="${promotionStatus=='expired' or isActiveNotEditable or promotionSweepstakesForm.awardTypeText == 'Plateau'}"/>&nbsp;&nbsp;<cms:contentText code="system.common.labels" key="YES" />	              
            </td>
          </tr>
        </c:if>

        <c:if test="${promotionSweepstakesForm.promotionTypeCode == 'product_claim' }">   
          <%@include file="/promotion/productclaim/promotionSweepstakesEligibleClaims.jsp" %>
        </c:if>           

        <tiles:insert attribute="promotionSweepstakesMiddle" />
        
        <%@include file="/promotion/sweepstakes/promotionSwpBillCodes.jsp" %>

    	</table>
      </td>
    </tr>
    <tr>
      <td colspan="2" align="center"><tiles:insert attribute="promotion.footer" /></td>
    </tr>    
  </table>
</html:form>

<tiles:insert attribute="promotionSweepstakesJS" />