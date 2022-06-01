<%--UI REFACTORED--%>
<%--
UI REFACTORING Note: Header and footer pages needs refactoring if necessary. 
If you add new types other than recognition and claim, you might want to refactor as per requirements.
As this doen't comes under any of our standard layouts, most of the layout is specific to this page (which ever fits) and changed the content wherever necessary as
per refactoring requirements.
--%>
<%@ include file="/include/taglib.jspf"%>

<script type="text/javascript">
  function showLayer(whichLayer)
  {
    if (document.getElementById)
    {
      if(document.getElementById(whichLayer) != null)
      {
		// this is the way the standards work
	    var style2 = document.getElementById(whichLayer).style;
		style2.display = "block";
		//this logic disables the teamhasmaxcount field when no maximum nuber is checked.
		if( whichLayer == "teamaudience")
		{
		   enableTeamHasMaxFields();
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

  function selectAllCheckBox( whichCheckboxes )
  {
    if ( whichCheckboxes == 'team' )
    {
      j=0;
      for ( i=0;i<getContentForm().length;i++ )
      {
        if(getContentForm().elements[i].name == 'secondaryAudienceList['+j+'].selected' )
        {
          j++;
          getContentForm().elements[i].checked = 'checked';
        }
      }
    }
    if ( whichCheckboxes == 'submitter' )
    {
      j=0;
      for ( i=0;i<getContentForm().length;i++ )
      {
   	    if(getContentForm().elements[i].name == 'primaryAudienceList['+j+'].selected' )
        {
          j++;
          getContentForm().elements[i].checked = 'checked';
        }
      }
    }
    if ( whichCheckboxes == 'position' )
    {
      j=0;
      for ( i=0;i<getContentForm().length;i++ )
      {
   		if(getContentForm().elements[i].name == 'promotionTeamPositionList['+j+'].selected' )
        {
          j++;
          getContentForm().elements[i].checked = 'true';
        }
      }
    }
  }
 
 //this function disables the teamhasmaxcount field when no maximum nuber is checked.
  function enableTeamHasMaxFields()
  {
	  if ( $("input:radio[name='teamHasMax']:checked").val()== "false")
	   {
		   $("input[name='teamMaxCount']").val("");
		   $("input[name='teamMaxCount']").attr('disabled', 'disabled');
	   }
	   else{
		   $("input[name='teamMaxCount']").attr('disabled', '');
	   }
  }  
</script>

<html:form styleId="contentForm" action="promotionAudienceSave">
  <html:hidden property="method" value="" styleId="method"/>
  <html:hidden property="version"/>
  <html:hidden property="promotionName" />
  <html:hidden property="promotionTypeCode"/>      
  <html:hidden property="promotionTypeName"/>
  <html:hidden property="promotionStatus"/>
  <html:hidden property="canRemoveAudience"/>
  <html:hidden property="hasParent"/>
  <html:hidden property="hasChildren"/>
  <html:hidden property="primaryAudienceListCount"/>
  
  <html:hidden property="divisionListCount"/>
  <nested:nest property="divisionList"/> 
  <nested:iterate id="divisionAudience" property="divisionList" >
	<nested:hidden property="divisionAudienceListCount"/>
  </nested:iterate>

  <html:hidden property="secondaryAudienceListCount"/>
  <html:hidden property="promotionTeamPositionListCount"/>
  <html:hidden property="fileLoadEntry"/>
  <html:hidden property="awardMerchandise"/>
  <html:hidden property="points"/>
  <html:hidden property="promotionId"/>
  <html:hidden property="partnerAudienceListCount"/>
  <html:hidden property="gqpartnersEnabled"/>
  <beacon:client-state>
		<beacon:client-state-entry name="promotionId" value="${promotionAudienceForm.promotionId}"/>		
  </beacon:client-state>  

  <table width="100%" cellpadding="3" cellspacing="1">
	<tr>
	  <td colspan="2">
	    <c:set var="promoTypeName" scope="request" value="${promotionAudienceForm.promotionTypeName}" />
	    <c:set var="promoTypeCode" scope="request" value="${promotionAudienceForm.promotionTypeCode}" />	    
  	    <c:set var="promoName" scope="request" value="${promotionAudienceForm.promotionName}" />
  	    <c:set var="fileLoadEntry" scope="request" value="${promotionAudienceForm.fileLoadEntry}" />
	    <tiles:insert attribute="promotion.header" />
	  </td>
	</tr>
	<tr><td>&nbsp;</td></tr>
    <tr>
      <td colspan="2"><cms:errors /></td>
    </tr>
    <tr>
      <td width="50%" valign="top">
      	<tiles:insert attribute="audiencePageTop"/>
      	<c:if test="${promotionAudienceForm.promotionTypeCode != 'quiz' && promotionAudienceForm.promotionTypeCode != 'wellness' && promotionAudienceForm.promotionTypeCode != 'goalquest' && promotionAudienceForm.promotionTypeCode != 'challengepoint'}">
	      <tiles:insert attribute="audiencePageBottom"/>
      	</c:if>
      	<c:if test="${promotionAudienceForm.gqpartnersEnabled == 'true'}">
	      <tiles:insert attribute="partneraudiencePage"/>
      	</c:if>
      	<c:if test="${promotionAudienceForm.promotionTypeCode == 'goalquest' || promotionAudienceForm.promotionTypeCode == 'challengepoint'}">
      	<c:if test="${promotionAudienceForm.gqpartnersEnabled == 'true'}">
	      	<table>
		      	<tr class="form-row-spacer" id="partnerCount">
				    <beacon:label property="partnerCount" required="true" styleClass="content-field-label-top">
				      <cms:contentText code="promotion.audience" key="MAX_PARTNER_COUNT" />
				    </beacon:label>
				    <td class="content-field" align="left" width="75%">
				    	 <html:text styleId="partnerCount" property="partnerCount" maxlength="4" size="8" styleClass="content-field" />
				    </td>
		        </tr>
	        </table>
	    </c:if>
        </c:if>
      	<c:if test="${promotionAudienceForm.promotionTypeCode == 'recognition'}">
      		<c:if test="${promotionAudienceForm.awardMerchandise}">
      		  <script type="text/javascript" src="<%=request.getContextPath()%>/assets/g4skin/scripts/taconite/taconite-client.js"></script>
			  <script type="text/javascript" src="<%=request.getContextPath()%>/assets/g4skin/scripts/taconite/taconite-parser.js"></script>
      		  	<div id="activeCountryTable">
		  			<tiles:insert attribute="audiencePageCountry"/>	  
		      	</div>
	      	</c:if>

<%-- Recognition Open Enrollment --%>
<%-- Commented out to enable this option in a later release
    <c:if test="${promotionAudienceForm.points}">
    <table border="0" cellpadding="0" cellspacing="0">
    <tr class="form-row-spacer">
      <beacon:label property="openEnrollmentEnabled" required="true" styleClass="content-field-label-top">
        <cms:contentText key="RECOGNITION_OPEN_ENROLLMENT" code="promotion.audience" />
      </beacon:label>
      <td class="content-field" align="left" width="75%">
        <table>
          <tr>
            <td class="content-field" valign="top">
              <html:radio styleId="openEnrollmentEnabledFalse" property="openEnrollmentEnabled" value="false" />
              <cms:contentText  code="system.common.labels" key="NO" />
            </td>
          </tr>
          <tr>
            <td class="content-field" valign="top">
		      <html:radio styleId="openEnrollmentEnabledTrue" property="openEnrollmentEnabled" value="true" />
              <cms:contentText  code="system.common.labels" key="YES" />
            </td>
          </tr>
        </table>
      </td>
    </tr>
    </table>
	</c:if>
--%>
	<%-- Recognition Open Enrollment : Temporary Backout changes - Remove after the above changes are enabled --%>
    <c:if test="${promotionAudienceForm.points}">
      <html:hidden property="openEnrollmentEnabled" value="false" />
	</c:if>
	      	
	<%-- Self Recognition --%>
    <table border="0" cellpadding="0" cellspacing="0">
    <tr class="form-row-spacer">
      <beacon:label property="selfRecognitionEnabled" required="true" styleClass="content-field-label-top">
        <cms:contentText key="SELF_RECOGNITION" code="promotion.audience" />
      </beacon:label>
      <td class="content-field" align="left" width="75%">
        <table>
          <tr>
            <td class="content-field" valign="top">
              <html:radio styleId="selfRecognitionEnabledFalse" property="selfRecognitionEnabled" value="false" />
              <cms:contentText  code="system.common.labels" key="NO" />
            </td>
          </tr>
          <tr>
            <td class="content-field" valign="top">
		      <html:radio styleId="selfRecognitionEnabledTrue" property="selfRecognitionEnabled" value="true" />
              <cms:contentText  code="system.common.labels" key="YES" />
            </td>
          </tr>
        </table>
      </td>
    </tr>
    </table>
	      	
      	</c:if>
      </td>
    </tr>
    <tr>
      <td colspan="2" align="center"><tiles:insert attribute="promotion.footer" /></td>
    </tr>    
  </table>
  
</html:form>

<SCRIPT language="JavaScript" type="text/javascript">

  //Begin inline javascript  (because window.onload conflicts with menu.js call to window.onload)
  hideLayer("teamaudiencelist");
  hideLayer("maxpeople");
  hideLayer("jobpositions");
  hideLayer("teamaudience");
  hideLayer("submittersaudience");
  hideLayer("partneraudience");
  hideLayer("preSelectedPartnerCharacteristic");
  
  if( getContentForm().teamUsed != null)
  {
	if( getContentForm().teamUsed[1].checked==true )
	{
      showLayer("teamaudience");
	}
  }

  if( getContentForm().teamCollectedAsGroup != null)
  {
	if( getContentForm().teamCollectedAsGroup[0].checked==true )
	{
      showLayer("maxpeople");
      hideLayer("jobpositions");
	}
    if( getContentForm().teamCollectedAsGroup[1].checked==true )
    {
      hideLayer("maxpeople");
      showLayer("jobpositions");
	}
  }

  <c:if test="${ (promotionTypeCode == 'recognition') || (promotionTypeCode == 'nomination') || (promotionTypeCode == 'self_serv_incentives' )}">
    var audienceType4 = document.getElementById('secondaryAudienceType[4]');
  </c:if>
  
  var audienceType2 = document.getElementById('secondaryAudienceType[2]');
  var primaryType2 = document.getElementById('primaryAudienceType[2]');
  var partnerType2 = document.getElementById('partnerAudienceType[2]');
  
  <c:if test="${ (promotionTypeCode == 'recognition') || (promotionTypeCode == 'nomination') || (promotionTypeCode == 'self_serv_incentives' )}">
    if( audienceType4.value != null && audienceType4.checked==true )
    {
	  showLayer("teamaudiencelist");
	}
  </c:if>
  
  if(primaryType2.checked==true) 
  {
	showLayer("submittersaudience");
  }
  
  <c:if test="${ (promotionTypeCode == 'goalquest') || (promotionTypeCode == 'challengepoint')}">
  	  var partnerAudienceType4 = document.getElementById('partnerAudienceType[4]');
	  if( partnerAudienceType4.value != null && partnerAudienceType4.checked==true )
	  {
	 	 showLayer("preSelectedPartnerCharacteristic");
	  }
</c:if>

  <c:if test="${ promotionTypeCode == 'product_claim' }">
  	if( audienceType2 != null && audienceType2.checked==true )
	{
	   showLayer("teamaudiencelist");
    }
  </c:if>
  
 <c:if test="${promotionAudienceForm.gqpartnersEnabled == 'true'}">
  if( partnerType2.checked==true) {
    showLayer("partneraudience");
  }
 </c:if>
 
 function enableLayers()
 {
	  var partnerType1 = document.getElementById('partnerAudienceType[1]');
	  var partnerType2 = document.getElementById('partnerAudienceType[2]');
	  var partnerType3 = document.getElementById('partnerAudienceType[3]');
	  var partnerType4 = document.getElementById('partnerAudienceType[4]');
	  
	  if( partnerType1 != null && partnerType1.value !=null && partnerType1.checked==true )
	  {
		  hideLayer("partnerCount");
	  }
	  if( ( partnerType2 !=null && partnerType2.value !=null && partnerType2.checked==true) 
			  || ( partnerType3 !=null && partnerType3.value !=null && partnerType3.checked==true) 
			  || ( partnerType4 !=null && partnerType4.value !=null && partnerType4.checked==true) )
	  {
		  showLayer("partnerCount");
	  }

 }
 
 enableLayers();

</SCRIPT>

  <script type="text/javascript">
  	function updateActiveCountry(method) {
  		document.getElementById("method").value = method;
  		var url = document.promotionAudienceForm.action;
  		var ajaxRequest = new AjaxRequest( url ); 		
 		//ajaxRequest.addFormElements("promotionAudienceForm");
		ajaxRequest.addNamedFormElements("primaryAudienceList","secondaryAudienceList","primaryAudienceType","secondaryAudienceType","promotionTypeCode","promotionId","reqActCntCount","nonreqActCntCount","awardMerchandise");
		ajaxRequest.addFormElementsById("method");
		ajaxRequest.setUsePOST();
  		document.getElementById("busyMessage").style.display = "";
  		ajaxRequest.sendRequest();
  	}    
  </script>