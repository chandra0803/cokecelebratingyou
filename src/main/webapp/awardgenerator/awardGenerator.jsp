<%@ include file="/include/taglib.jspf"%>

<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.promotion.PromoMerchCountry" %>
<%@ page import="com.biperf.core.domain.promotion.PromoMerchProgramLevel" %>
<%@ page import="com.biperf.core.value.AwardGenPlateauValueBean" %>
<%@ page import="com.biperf.core.value.AwardGenPlateauFormBean" %>

<%@ page import="com.biperf.core.domain.country.Country" %>

<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI( request )%>/assets/g4skin/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI( request )%>/assets/g4skin/scripts/calendar/calendar-en.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI( request )%>/assets/g4skin/scripts/calendar/calendar-setup.js"></script>

<script type="text/javascript">

  function setNumberOfDaysAlertField( checkboxValue )
  {
    if ( checkboxValue.checked == true )
    { 
      $('#numberOfDaysForAlert').show();
      document.getElementById('numberOfDaysForAlert').disabled = false; 
      getContentForm().numberOfDaysForAlert.focus();
    }
    else
    {
      getContentForm().numberOfDaysForAlert.value = '0';   
      $('#numberOfDaysForAlert').hide();
      document.getElementById('numberOfDaysForAlert').disabled = true;
    }  
  }
  
  function reviewRemovedPlateaus( formName, methodName )
  {
	document.forms[formName].method.value=methodName
	var count = $( "input[name*='plateauValueFormBeansCount']" ).val();
	for( i = 0; i < count; i++ ){
	 name = 'plateauValueFormBeans['+i+'].deleted';
	
	 if( $( "input[name*='"+name+"']" ).attr('checked') )
	  document.forms[formName].submit();
	}
	return false;
  }
</script>

<%
  String successMessage = "";
  if ( request.getAttribute( "successMessage" ) != null )
  {
    successMessage = request.getAttribute( "successMessage" ).toString();
  }

  String successLaunchMessage = "";
  if ( request.getAttribute( "successLaunchMessage" ) != null )
  {
    successLaunchMessage = request.getAttribute( "successLaunchMessage" ).toString();
  }
  
  String successUpdateMessage = "";
  if ( request.getAttribute( "successUpdateMessage" ) != null )
  {
    successUpdateMessage = request.getAttribute( "successUpdateMessage" ).toString();
  }

  String successExtractMessage = "";
  if ( request.getAttribute( "successExtractMessage" ) != null )
  {
    successExtractMessage = request.getAttribute( "successExtractMessage" ).toString();
  }
%>

<div id="main"> 
<html:form styleId="contentForm" action="awardGeneratorMaintain">
    <html:hidden property="method" />
    <html:hidden property="awardGeneratorId"/>
    <html:hidden property="awardGenAwardListSize"/>
    <html:hidden property="plateauValueFormBeansCount"/>
    <html:hidden property="awardListCount"/>
    <html:hidden property="awardType"/>
    <html:hidden property="awardActive"/>   
    <html:hidden property="awardAmountTypeFixed"/>
    <html:hidden property="awardAmountFixed"/>
    <html:hidden property="awardAmountMin"/>
    <html:hidden property="awardAmountMax"/>  
    <html:hidden property="newAwardGenerator"/>   
    <c:set var="disabled" value="false"/>  
    <c:if test="${awardGeneratorForm.newAwardGenerator == false}">
    <html:hidden property="setupName"/>        
    <html:hidden property="promotionId"/>             
    <html:hidden property="examineField"/>     
    <c:set var="disabled" value="true"/>
    </c:if>

    
   	<div id="messageDiv" class="error">
	</div>
	<div id="messageDiv1" class="error">
	</div>
	<div id="messageDiv2" class="error">
	</div>
	<div id="messageDiv3" class="error">
	</div>
    
  	<table border="0" cellpadding="10" cellspacing="0" width="100%">
		<tr>
			<td>
			<c:choose>
			<c:when test="${awardGeneratorForm.newAwardGenerator == true}">
	        <span class="headline"><cms:contentText key="ADD_TITLE" code="awardgenerator.maintain"/></span>

	        <%--INSTRUCTIONS--%>
	        <span class="content-instruction">
	          <cms:contentText key="ADD_INSTRUCTION" code="awardgenerator.maintain"/>
	        </span>
	        <%--END INSTRUCTIONS--%>
     		</c:when>
			<c:otherwise>
				<span class="headline"><cms:contentText key="EDIT_TITLE" code="awardgenerator.maintain"/></span>
        		
		        <%--INSTRUCTIONS--%>
		        <span class="content-instruction">
		          <cms:contentText key="EDIT_INSTRUCTION" code="awardgenerator.maintain"/>
		        </span>
		        <%--END INSTRUCTIONS--%>
			</c:otherwise>
			</c:choose>
        	<cms:errors/>
           </td>
      </tr>       
   </table>
   <table border="0" cellpadding="10" cellspacing="0" width="100%">
     <tr class="form-row-spacer">
		<beacon:label property="promotionId" required="true">
			<cms:contentText key="PROMOTION" code="awardgenerator.maintain" />
		</beacon:label> 
		<td class="content-field">
			<html:select property="promotionId" styleClass="content-field killme" onchange="setActionDispatchAndSubmit('awardGeneratorMaintain.do','resetFormDisplay')" disabled="${disabled}">
			<html:option value=""><cms:contentText key="SELECT_ONE" code="system.general"/></html:option>
				<c:if test="${fn:length(promotionList)!=0}">
					<html:options collection="promotionList" property="id" labelProperty="value" />
				</c:if>
			</html:select>
		</td>
	 </tr>

	 <tr class="form-row-spacer">
		<beacon:label property="setupName" required="true">
			<cms:contentText key="SETUP_NAME" code="awardgenerator.maintain" />
		</beacon:label> 
		<td class="content-field">
		    <html:text property="setupName" styleClass="content-field" size="50" maxlength="50" disabled="${disabled}"/>
		</td>	
	</tr> 

	 <tr class="form-row-spacer"><td></td></tr>
	</table>

	<!-- points -->
	<!-- Award Active -->
	<c:if test="${awardGeneratorForm.awardType == 'points'}" > 
	<table border="0" cellpadding="10" cellspacing="0" width="80%">		
	    <tr>
	       <td>
	        <c:if test="${awardGeneratorForm.awardActive == true}"> 
	       		 <tiles:insert attribute="awardGenPointAwardActive" />
	        </c:if>
			<!-- Award Inactive  -->
			<c:if test="${awardGeneratorForm.awardActive == false}"> 
				<tiles:insert attribute="awardGenPointAwardInactive" />
			</c:if>
				  
			<tr class="form-blank-row"><td></td></tr>
		    <tr class="form-row-spacer">
		        <td align="left" colspan="2">
	     	  		<a id="addAnotherhref"  href="#" onclick="setDispatchAndSubmit( 'addAnotherPointAward')"   >
	       			  <cms:contentText code="awardgenerator.maintain" key="ADD_ANOTHER" />
	     	  		</a>
	        	</td>
		    </tr>
		  </td>
		</tr>   
	</table>
	</c:if>
	
	<!-- plateau awards-->
	<c:if test="${awardGeneratorForm.awardType == 'merchandise'}" >
    <table border="0" cellpadding="10" cellspacing="0" width="100%">
	<tr>
		<td>
		<!-- Award Active -->
		<c:if test="${awardGeneratorForm.awardActive == true}">
			<tiles:insert attribute="awardGenPlateauAwardActive" />
		</c:if>
		<!-- Award Inactive -->
		<c:if test="${awardGeneratorForm.awardActive == false}"> 
			<tiles:insert attribute="awardGenPlateauAwardInactive" />
		</c:if>
		</td>
	</tr>
		
	<tr class="form-blank-row">
		<td colspan="3">&nbsp;</td>
	</tr>
	<tr class="form-row-spacer">
        <td align="left" colspan="3">
   	  		<a id="addAnotherhref"  href="#" onclick="setDispatchAndSubmit('addAnotherPlateauAward')"   >
     			   <cms:contentText code="awardgenerator.maintain" key="ADD_ANOTHER" />
     	  	</a>
        </td>
	 </tr>
	</table>
	</c:if>
	
	<table border="0" cellpadding="10" cellspacing="0" width="36.5%" >
	  <tr class="form-row-spacer">
		<beacon:label property="examineField" required="true">
			<cms:contentText key="EXAMINE_FIELD" code="awardgenerator.maintain" />
		</beacon:label> 
	 	<td class="content-field" valign="top" align="left">
			<html:select property="examineField" styleClass="content-field killme" disabled="${disabled}">
			<html:option value="">
			<cms:contentText key="SELECT_ONE" code="system.general"/></html:option>
				<c:if test="${fn:length(examineList)!=0}">
					<html:options collection="examineList" property="value" labelProperty="value" />
				</c:if>
			</html:select>
		</td>		
	  </tr>
      <tr class="form-row-spacer">
		<beacon:label property="notifyManager" required="false" >
			<cms:contentText key="NOTIFY_MANAGER" code="awardgenerator.maintain" />
		</beacon:label> 
		 <td class="content-field" valign="top">
			<html:checkbox property="notifyManager" value="true" onclick="setNumberOfDaysAlertField(this)" />
		 </td>
	  </tr>
	  
	   <tr class="form-row-spacer" id="numberOfDaysForAlert">
			<beacon:label property="numberOfDaysForAlert" required="false">
				<cms:contentText key="NUMBER_OF_DAYS_FOR_ALERT" code="awardgenerator.maintain" />
			</beacon:label> 
		
			<td class="content-field" valign="top">
			    <html:text property="numberOfDaysForAlert" styleId="numberOfDaysForAlert" styleClass="content-field" size="10" maxlength="10"/>
			</td>	
	  </tr> 
	 </table>
	 
	<table border="0" cellpadding="10" cellspacing="0" width="100%">
	  <tr class="form-buttonrow">
           <td></td>
           <td></td>
           <td align="center">
            <div align="center">
             <html:submit styleClass="content-buttonstyle" onclick="setDispatch('save')">
               <cms:contentText code="system.button" key="SAVE" />
             </html:submit>
             <c:choose>
             <c:when test="${awardGeneratorForm.awardType == 'points'}" > 
             <html:submit styleClass="content-buttonstyle" onclick="setDispatch('removeSelectedPoints')">
				<cms:contentText code="system.button" key="REMOVE_SELECTED" />
			</html:submit>		
			</c:when>
			<c:otherwise>
			    <html:button property="removeBtn" styleClass="content-buttonstyle" onclick="reviewRemovedPlateaus('contentForm','removeSelectedPlateau')">
					<cms:contentText code="system.button" key="REMOVE_SELECTED" />
				</html:button>	
			</c:otherwise>
			</c:choose>
             <c:if test="${ awardGeneratorForm.awardGeneratorId == 0 || awardGeneratorForm.awardListCount == 0 }">
             	<html:button property="cancelBtn" styleClass="content-buttonstyle" onclick="setDispatchAndSubmit('cancel')">
              		 <cms:contentText code="system.button" key="CANCEL" />
             	</html:button>
             </c:if>
             </div>
           </td>
       </tr>		
   </table>
</html:form>
</div>
<c:if test="${awardGeneratorForm.awardGeneratorId != null && awardGeneratorForm.awardGeneratorId != 0 && awardGeneratorForm.awardListCount > 0}">
<table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <tiles:insert attribute="awardGenBatchGenerate" />
        <br/>
      </td>
    </tr>
</table>   
	<table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <tiles:insert attribute="awardGenBatchUpdate" />
        <br/>
      </td>
    </tr>
</table>  
<table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <tiles:insert attribute="awardGenBatchExtract" />
        <br/>
      </td>
    </tr>
</table> 
</c:if>


<SCRIPT language="JavaScript" type="text/javascript">
  $(document).ready(function(){
	    $("#messageDiv").hide();
	    var successMessage='';
	    successMessage='<%=successMessage%>';
	    if(successMessage!='')
	    {
	    	$("#messageDiv").html(successMessage);
	    	$("#messageDiv").show();
	    }
	    else
	    {
	    	$("#messageDiv").hide();	
	    }
	    });
  
  $(document).ready(function(){
	    $("#messageDiv1").hide();
	    var successLaunchMessage='';
	    successLaunchMessage='<%=successLaunchMessage%>';
	    if(successLaunchMessage!='')
	    {
	    	$("#messageDiv1").html(successLaunchMessage);
	    	$("#messageDiv1").show();
	    }
	    else
	    {
	    	$("#messageDiv1").hide();	
	    }
	    });

  $(document).ready(function(){
	    $("#messageDiv2").hide();
	    var successUpdateMessage='';
	    successUpdateMessage='<%=successUpdateMessage%>';
	    if(successUpdateMessage!='')
	    {
	    	$("#messageDiv2").html(successUpdateMessage);
	    	$("#messageDiv2").show();
	    }
	    else
	    {
	    	$("#messageDiv2").hide();	
	    }
	    });

  $(document).ready(function(){
	    $("#messageDiv3").hide();
	    $('#numberOfDaysForAlert').hide();
	    var successExtractMessage='';
	    successExtractMessage='<%=successExtractMessage%>';
	    if(successExtractMessage!='')
	    {
	    	$("#messageDiv3").html(successExtractMessage);
	    	$("#messageDiv3").show();
	    }
	    else
	    {
	    	$("#messageDiv3").hide();	
	    }
	    });
</SCRIPT>
