<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="java.util.*" %>

<script type="text/javascript">
  function enableEvaluation()
  {
    var selectedAwardGroupMethod = "";
    for (counter = 0; counter < getContentForm().awardGroupMethod.length; counter++)
    {
      if (getContentForm().awardGroupMethod[counter].checked)
      {
        selectedAwardGroupMethod = getContentForm().awardGroupMethod[counter].value
      }    
    }
    
    
    if ( selectedAwardGroupMethod != 'individual' )
    {
      document.getElementById("evaluationTypeChoiceIndependently").checked = true;
      document.getElementById("evaluationTypeCumulative").disabled=true;
      <%-- Client customizations for WIP #59418 --%>
      displayTeamCmAssetText();
    }
    else
    {
      <c:if test = "${promotionStatus !='expired' and promotionStatus !='live'}">
        document.getElementById("evaluationTypeCumulative").disabled=false;
      </c:if>
    }
    
    <%--   award group validation starts  --%>
    if($("input:radio[name='teamAwardGroupSizeType']:checked").val()!= "limited" )
    	{
	    	$("input[name='teamMaxGroupMembers']").val("");
			$("input[name='teamMaxGroupMembers']").attr('disabled','disabled');
    	}else{
    		$("input[name='teamMaxGroupMembers']").attr('disabled','');
    	}
    
    
    if($("input:radio[name='bothAwardGroupSizeType']:checked").val()!= "limited" )
    	{
	    	$("input[name='bothMaxGroupMembers']").val("");
			$("input[name='bothMaxGroupMembers']").attr('disabled','disabled');
    	}else{
    		$("input[name='bothMaxGroupMembers']").attr('disabled','');
    	}
    
    
	if($("input:radio[name='awardGroupMethod']:checked").val()== "team" )
	    {
	   	$("input[name='bothMaxGroupMembers']").val("");
	   	
	   	$("input:radio[name='bothAwardGroupSizeType']").each(function (i) {        
	        this.checked = false;
	    });
	   	
		 showLayer("team"); 
		 hideLayer("both"); 
	    }
	else if($("input:radio[name='awardGroupMethod']:checked").val()== "both" ){
	   	$("input[name='teamMaxGroupMembers']").val("");
	   	
		$("input:radio[name='teamAwardGroupSizeType']").each(function (i) {        
	        this.checked = false;
	    });
	    	
		showLayer("both");  
	   	hideLayer("team");
	    }	
	else if($("input:radio[name='awardGroupMethod']:checked").val() != "team" && $("input:radio[name='awardGroupMethod']:checked").val()!= "both"){
		    $("input[name='bothMaxGroupMembers']").val("");
	   	    $("input[name='teamMaxGroupMembers']").val("");
	   	    
	   	 	$("input:radio[name='bothAwardGroupSizeType']").each(function (i) {        
		        this.checked = false;
		    });
	   	 	
	   	   $("input:radio[name='teamAwardGroupSizeType']").each(function (i) {        
		        this.checked = false;
		    });
		    		   	 	
	    	hideLayer("both"); 
	    	hideLayer("team"); 
	    }
	    
	<%--   award group validation ends  --%>    
    
    <%-- datepicker is removed and the publicationDate field is cleared when the publicationDateActive is false"--%>
    if($("input:radio[name='publicationDateActive']:checked").val()!= "true" )
    	{
    		$("input[name='publicationDate']").val("");
     		$('#publicationDateTrigger').fadeOut();
    	}else{
    	 	$('#publicationDateTrigger').fadeIn();
    	}
    
  }
	
</script>

<script type="text/javascript">
$(document).ready(function() {
	displayRecognitionPrivate();
	<%-- Client customizations for WIP #59418 --%>
	displayTeamCmAssetText();
	displayCapPerPax();
});

function displayRecognitionPrivate() {
	  if ($("input:radio[name='allowPublicRecognition']:checked").val() == "true") {
			$("#allowPromotionPrivate").show();
		}
		if ($("input:radio[name='allowPublicRecognition']:checked").val() == "false") {
			$("#allowPromotionPrivate").hide();
		}
	}
	<%-- Client customizations for WIP #59418 --%>
function displayTeamCmAssetText() {
	  if ($("input:radio[name='awardGroupMethod']:checked").val() != 'individual') {
			$("#teamCmAssetText").show();
		}
		if ($("input:radio[name='awardGroupMethod']:checked").val() == 'individual') {
			$("#teamCmAssetText").hide();
		}
	}
	
function displayCapPerPax() {
	  if ($("input:radio[name='levelPayoutByApproverAvailable']:checked").val() == "true") {
			$("#capPerPax").show();
		}
		if ($("input:radio[name='levelPayoutByApproverAvailable']:checked").val() == "false") {
			$("input[name='capPerPax']").val("");
			$("#capPerPax").hide();
		}
	}
function displayFileUpload() {
	  if ($("input:radio[name='enableFileUploader']:checked").val() == "true") {
			$("#allowedFileTypes").show();
			$("#maxNumberOfFiles").show();
			$("#minNumberOfFiles").show();
		}
		if ($("input:radio[name='enableFileUploader']:checked").val() == "false") {
			$("#allowedFileTypes").hide();
			$("#maxNumberOfFiles").hide();
			$("#minNumberOfFiles").hide();
		}
	}
</script>

<tr class="form-blank-row"> 
  <td></td>
</tr>
<c:if test="${promotionBasicsForm.expired}">
  <html:hidden property="certificate" />
  <html:hidden property="selfNomination" />   
</c:if>
<c:if test="${ (promotionBasicsForm.expired == 'true') ||
                 (promotionBasicsForm.live == 'true') }">
  <%-- <html:hidden property="awardGroupMethod"/>
  <html:hidden property="teamAwardGroupSizeType"/>
  <html:hidden property="bothAwardGroupSizeType"/> --%>
  <html:hidden property="evaluationType"/>
</c:if> 

<%--allow public recognitions --%>
<tr class="form-row-spacer" id="allowPublicRecognition">
  <beacon:label property="allowPublicRecognition" required="true" styleClass="content-field-label-top">
   <cms:contentText key="ALLOW_PUBLIC_RECOGNITION" code="promotion.basics" />
  </beacon:label>
  <td colspan=2 class="content-field">  	
  <table>
    <tr>
      <td class="content-field"><html:radio property="allowPublicRecognition" value="true"  onclick="displayRecognitionPrivate()" /></td>
      <td class="content-field"><cms:contentText code="system.common.labels" key="YES"/></td>      
    </tr>
    <tr>
      <td class="content-field"><html:radio property="allowPublicRecognition" value="false"  onclick="displayRecognitionPrivate()" /></td>
      <td class="content-field"><cms:contentText code="system.common.labels" key="NO"/></td>       
    </tr>
  </table>
  </td>
</tr>
<%--allow public recognitions end --%>

<%-- recognitions isPrivate--%>
<tr class="form-row-spacer" id="allowPromotionPrivate">
  <beacon:label property="allowPromotionPrivate" required="true" styleClass="content-field-label-top">
   <cms:contentText key="IS_NOMINATION_PRIVATE" code="promotion.basics" />
  </beacon:label>
  <td colspan=2 class="content-field">  	
  <table>
    <tr>
      <td class="content-field"><html:radio property="allowPromotionPrivate" value="false" /></td>
      <td class="content-field"><cms:contentText code="system.common.labels" key="NO"/></td>       
    </tr>
    <tr>
      <td class="content-field"><html:radio property="allowPromotionPrivate" value="true" /></td>
      <td class="content-field"><cms:contentText code="system.common.labels" key="YES"/></td>      
    </tr>
  </table>
  </td>
</tr>
<%-- recognitions isPrivate--%>
<%-- Enable File Uploader --%>
<tr class="form-row-spacer" id="enableFileUploader">
  <beacon:label property="enableFileUploader" required="true" styleClass="content-field-label-top">
   <cms:contentText code="client.nominationSubmit" key="ENABLE_FILE_UPLOAD"/>
  </beacon:label>
  <td colspan=2 class="content-field">  	
  <table>
    <tr>
      <td class="content-field"><html:radio property="enableFileUploader" value="false" onclick="displayFileUpload()" /></td>
      <td class="content-field"><cms:contentText code="system.common.labels" key="NO"/></td>       
    </tr>
    <tr>
      <td class="content-field"><html:radio property="enableFileUploader" value="true" onclick="displayFileUpload()" /></td>
      <td class="content-field"><cms:contentText code="system.common.labels" key="YES"/></td>      
    </tr>
  </table>
  </td>
</tr>
<%-- Enable File Uploader --%>
<tr class="form-blank-row"><td></td></tr>


<%-- Allowed File Types --%>
<tr class="form-row-spacer" id="allowedFileTypes">
  <beacon:label property="allowedFileTypes" required="false" styleClass="content-field-label-top">
   <cms:contentText code="client.nominationSubmit" key="ALLOWED_FILE_TYPES"/>
  </beacon:label>
  <td colspan=2 class="content-field">  	
  <table>
    <tr><td class="content-field"><html:text property="allowedFileTypes"></html:text></td></tr>
  </table>
  </td>
</tr>
<%-- Allowed File Types --%>
<tr class="form-blank-row"><td></td></tr>

<%-- Max Number of Files --%>
<tr class="form-row-spacer" id="maxNumberOfFiles">
  <beacon:label property="maxNumberOfFiles" required="false" styleClass="content-field-label-top">
   <cms:contentText code="client.nominationSubmit" key="MAX_FILES"/>
  </beacon:label>
  <td colspan=2 class="content-field">  	
  <table>
    <tr>
      <td class="content-field"><html:text property="maxNumberOfFiles"></html:text></td>
    </tr>
  </table>
  </td>
</tr>
<%-- Max Number of Files --%>
<tr class="form-blank-row"><td></td></tr>

<%-- Min Number of Files --%>
<tr class="form-row-spacer" id="minNumberOfFiles">
  <beacon:label property="minNumberOfFiles" required="false" styleClass="content-field-label-top">
   <cms:contentText code="client.nominationSubmit" key="MIN_FILES"/>
  </beacon:label>
  <td colspan=2 class="content-field">  	
  <table>
    <tr>
      <td class="content-field"><html:text property="minNumberOfFiles"></html:text></td>
    </tr>
  </table>
  </td>
</tr>
<%-- Min Number of Files--%>
<tr class="form-blank-row"><td></td></tr>

<tr class="form-row-spacer">            
  <beacon:label required="true" styleClass="content-field-label-top">
    <cms:contentText key="SELF_NOMINATION" code="promotion.basics"/>
  </beacon:label> 
  <td colspan=2 class="content-field">
    <table>      
      <tr>
       <td class="content-field"><html:radio property="selfNomination" value="true" disabled="${promotionBasicsForm.expired}"/></td>
       <td class="content-field"><cms:contentText code="system.common.labels" key="YES"/></td>  
      </tr>
      <tr>
       <td class="content-field"><html:radio property="selfNomination" value="false" disabled="${promotionBasicsForm.expired}"/></td>
       <td class="content-field"><cms:contentText code="system.common.labels" key="NO"/></td>  
      </tr>      
    </table>
  </td>
</tr>

<%-- Additional Nomination Options --%>
<tr class="form-row-spacer">         
  <beacon:label property="awardGroupMethod" required="true" styleClass="content-field-label-top">
    <cms:contentText key="AWARD_GROUP_TYPE" code="promotion.basics"/>
  </beacon:label>         
  <td colspan=2 class="content-field">
    <table width="100%">
      <tr>
        <td class="content-field" style="float: left;"><html:radio onclick="enableEvaluation();" property="awardGroupMethod" value="individual" /></td>
        <td class="content-field" style="float: left;"><cms:contentText key="INDIVIDUAL" code="promotion.basics"/></td>
      </tr>
      <tr>
        <td class="content-field" style="float: left;"><html:radio onclick="enableEvaluation();" property="awardGroupMethod" value="team"/></td>
        <td class="content-field" style="float: left;"><cms:contentText key="TEAM" code="promotion.basics"/></td>
      </tr>
      <tr id="team">
     	<td colspan="2">
     		<div style="margin: auto;width: 85%;padding: 0px;">
     			<html:radio value="unlimited" property="teamAwardGroupSizeType" onclick="enableEvaluation();"/> <cms:contentText key="UNLIMITED_SIZE" code="promotion.basics"/>
     		</div>
     		<div style="margin: auto;width: 85%;padding: 0px;">
     			<html:radio value="limited" property="teamAwardGroupSizeType" onclick="enableEvaluation();"/>
   				<cms:contentText key="MAXIMUM_TEAM_MEMBERS" code="promotion.basics"/>&nbsp;	<html:text property="teamMaxGroupMembers" size="4" maxlength="4" />
     		</div>
    	</td>
      </tr>
      <tr>
        <td class="content-field" style="float: left;"><html:radio onclick="enableEvaluation();" property="awardGroupMethod" value="both"/></td>
        <td class="content-field" style="float: left;"><cms:contentText key="INDIVIDUAL_TEAM_SIZE" code="promotion.basics"/></td>
      </tr>
      <tr id="both">
      	<td colspan="2">
     		<div style="margin: auto;width: 85%;padding: 0px;">
     			<html:radio value="unlimited" property="bothAwardGroupSizeType" onclick="enableEvaluation();"/> <cms:contentText key="UNLIMITED_SIZE" code="promotion.basics"/>
     		</div>
     		<div style="margin: auto;width: 85%;padding: 0px;">
     			<html:radio value="limited" property="bothAwardGroupSizeType" onclick="enableEvaluation();"/>
     			<cms:contentText key="MAXIMUM_TEAM_MEMBERS" code="promotion.basics"/>&nbsp; <html:text property="bothMaxGroupMembers" size="4" maxlength="4"/>
     		</div>
    	</td>
      </tr>
    </table>
  </td>
</tr> 

<tr class="form-blank-row"><td></td></tr>   

<tr class="form-row-spacer"> 
  <beacon:label property="evalutionType" required="true" styleClass="content-field-label-top">
    <cms:contentText key="EVALUTION_TYPE" code="promotion.basics"/>
  </beacon:label>         
  <td colspan=2 class="content-field">
    <table>
      <tr>
        <td class="content-field"><html:radio styleId="evaluationTypeChoiceIndependently" property="evaluationType" value="independent" disabled="${promotionBasicsForm.expired or promotionBasicsForm.live}"/></td>
        <td class="content-field"><cms:contentText key="INDEPENDENTLY" code="promotion.basics"/></td>
      </tr>
      <tr>
        <td class="content-field"><html:radio styleId="evaluationTypeCumulative" property="evaluationType" value="cumulative" disabled="${promotionBasicsForm.expired or promotionBasicsForm.live}"/></td>
        <td class="content-field"><cms:contentText key="CUMULATIVE" code="promotion.basics"/></td>
      </tr>
    </table>
  </td>
</tr> 

<tr class="form-blank-row"><td></td></tr>   

<tr class="form-row-spacer">            
  <beacon:label required="true" styleClass="content-field-label-top">
    <cms:contentText key="SELF_NOMINATION" code="promotion.basics"/>
  </beacon:label> 
  <td colspan=2 class="content-field">
    <table>      
      <tr>
       <td class="content-field"><html:radio property="selfNomination" value="true" disabled="${promotionBasicsForm.expired}"/></td>
       <td class="content-field"><cms:contentText code="system.common.labels" key="YES"/></td>  
      </tr>
      <tr>
       <td class="content-field"><html:radio property="selfNomination" value="false" disabled="${promotionBasicsForm.expired}"/></td>
       <td class="content-field"><cms:contentText code="system.common.labels" key="NO"/></td>  
      </tr>      
    </table>
  </td>
</tr>

<%-- Client customization wip#56492 start--%>
<tr class="form-row-spacer">            
  <beacon:label required="true" styleClass="content-field-label-top">
    <cms:contentText key="LEVEL_SELECTION_BY_APPROVER" code="promotion.basics"/>
  </beacon:label> 
  <td colspan=2 class="content-field">
    <table>      
      <tr>
       <td class="content-field"><html:radio property="levelSelectionByApprover" value="true" /></td>
       <td class="content-field"><cms:contentText code="system.common.labels" key="YES"/></td>  
      </tr>
      <tr>
       <td class="content-field"><html:radio property="levelSelectionByApprover" value="false" /></td>
       <td class="content-field"><cms:contentText code="system.common.labels" key="NO"/></td>  
      </tr>      
    </table>
  </td>
</tr>
<%-- Client customization wip#56492 end--%>

<tr class="form-blank-row"><td></td></tr> 

<tr class="form-blank-row"><td></td></tr>   

<tr class="form-row-spacer">            
  <beacon:label required="true" styleClass="content-field-label-top">
	<cms:contentText key="IS_WHY_DEFAULT_ACTIVE" code="promotion.basics"/>
  </beacon:label> 
  <td colspan=2 class="content-field">
    <table>      
      <tr>
       <td class="content-field"><html:radio property="whyNomination" value="true" disabled="${promotionBasicsForm.expired}"/></td>
       <td class="content-field"><cms:contentText code="system.common.labels" key="YES"/></td>  
      </tr>
      <tr>
       <td class="content-field"><html:radio property="whyNomination" value="false" disabled="${promotionBasicsForm.expired}"/></td>
       <td class="content-field"><cms:contentText code="system.common.labels" key="NO"/>&nbsp;&nbsp;&nbsp;&nbsp;<cms:contentText code="promotion.basics" key="IS_WHY_INSTRUCTION"/></td>  
      </tr>      
    </table>
  </td>
</tr>

<tr class="form-blank-row"><td></td></tr>  

<tr class="form-row-spacer">            
  <beacon:label required="true" styleClass="content-field-label-top">
    <cms:contentText key="VIEW_PAST_WINNERS" code="promotion.basics"/>
  </beacon:label> 
  <td colspan=2 class="content-field">
    <table>      
      <tr>
       <td class="content-field"><html:radio property="viewPastWinners" value="true" disabled="${promotionBasicsForm.expired}"/></td>
       <td class="content-field"><cms:contentText code="system.common.labels" key="YES"/></td>  
      </tr>
      <tr>
       <td class="content-field"><html:radio property="viewPastWinners" value="false" disabled="${promotionBasicsForm.expired}"/></td>
       <td class="content-field"><cms:contentText code="system.common.labels" key="NO"/></td>  
      </tr>      
    </table>
  </td>
</tr>

<%-- Client customization wip#58122 start--%>
<tr class="form-row-spacer">            
  <beacon:label required="true" styleClass="content-field-label-top">
    <cms:contentText key="LEVEL_PAYOUT_BY_APPROVER" code="promotion.basics"/>
  </beacon:label> 
  <c:set var="promotionId" scope="request" value="${promotionBasicsForm.promotionId}" />
  <td colspan=2 class="content-field">
    <table>      
      <tr>
       <td class="content-field"><html:radio property="levelPayoutByApproverAvailable" value="true" onclick="displayCapPerPax()"/></td>
       <td class="content-field"><cms:contentText code="system.common.labels" key="YES"/></td> 
       <c:if test="${promotionBasicsForm.levelPayoutByApproverAvailable == 'true'}">
       <td class="content-field">
       
		       <%	Map parameterMap = new HashMap();
												    String promotionId = (String)request.getAttribute( "promotionId" );
														parameterMap.put( "promotionId", promotionId );
														pageContext.setAttribute("popupUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), "/admin/levelPayoutMaintain.do?method=display", parameterMap, true ) );
												%>
												
                                                <a href="javascript:popUpWin('<c:out value="${popupUrl}"/>', 'console', 750, 500, true, true);" class="crud-content-link">
													  Update Payout
                        
												</a>
		</td> 
		</c:if>   
      </tr>
      <tr>
       <td class="content-field"><html:radio property="levelPayoutByApproverAvailable" value="false" onclick="displayCapPerPax()"/></td>
       <td class="content-field"><cms:contentText code="system.common.labels" key="NO"/></td>  
      </tr>      
    </table>
  </td>
</tr>

<tr class="form-row-spacer" id="capPerPax">
  <beacon:label property="capPerPax" required="false" styleClass="content-field-label-top">
      <cms:contentText code="promotion.basics" key="CAP_PER_PAX"/>
  </beacon:label>
  <td colspan=2 class="content-field">  	
  <table>
    <tr>
      <td class="content-field"><html:text property="capPerPax"></html:text></td>
    </tr>
  </table>
  </td>
</tr>
<%-- Client customization wip#58122 end--%>

<tr class="form-blank-row"><td></td></tr> 

<%-- Client customizations for WIP #59418 --%>
<tr class="form-row-spacer" id="teamCmAssetText">
  <beacon:label property="teamCmAssetText" required="false" styleClass="content-field-label-top">
   <cms:contentText code="promotion.basics" key="TEAM_CM_ASSET_TEXT"/>
  </beacon:label>
  <td colspan=2 class="content-field">  	
  <table>
    <tr>
      <td class="content-field"><html:text property="teamCmAssetText"></html:text></td>
    </tr>
  </table>
  </td>
</tr>
<%-- Client customizations for WIP #59418 --%>
  

<script type="text/javascript">
  enableEvaluation();
 // enableGiverActivityDesc();
  //enableReceiverActivityDesc();
	Calendar.setup(
	{
	  inputField  : "publicationDate",         	// ID of the input field
	  ifFormat    : "${TinyMceDatePattern}",    		// the date format
	  button      : "publicationDateTrigger"    // ID of the button
	});

</script>        
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/tinymce/jscripts/tiny_mce/tiny_mce.js"></script>
<script type="text/javascript">
  tinyMCE.init(
  {
    mode : "textareas",
    theme : "advanced",
    forced_root_block : false,
	force_br_newlines : true,         
    force_p_newlines : false,
    remove_linebreaks : false,
    convert_urls : false,
    convert_newlines_to_brs : true,
    invalid_elements : "nbsp,p,pre,br,a",
    gecko_spellcheck : true ,
    plugins : "spellchecker",
    theme_advanced_buttons1 : "spellchecker",
    theme_advanced_buttons2 : "",
    theme_advanced_buttons3 : "",
    theme_advanced_toolbar_location : "top",
    theme_advanced_toolbar_align : "left",
    spellchecker_languages :"+${textEditorDictionaries}",
    spellchecker_rpc_url    : "<%=RequestUtils.getBaseURI(request)%>/spellchecker/jazzySpellCheck.do",
    
    setup : function(ed) {
		ed.onKeyUp.add(function(ed, e) {
			headHandler(ed);
		});
		ed.onKeyDown.add(function(ed, e) {
			headHandler(ed);
      	});
		ed.onLoadContent.add(function(ed, o) {
			headHandler(ed);
	    });
      },
      
    save_callback : "myCustomSaveContent"
  });
  
  function headHandler(e) {
		if(e.ctrlKey && e.keyCode == 86) // CTRL-V
		{
			return tinyMCE.cancelEvent(e);
		}
		else if(e.type == "drop")
		{
			return tinyMCE.cancelEvent(e); //Drop
		}
		    
		tinyMCE.selectedInstsance = tinyMCE.getInstanceById('purpose');

		var temp = tinyMCE.selectedInstance.getContent();
		var x = temp;
		//alert('current text length:'+x.length);
		if (x.length <= 2000) {
			textCounter(tinyMCE.selectedInstance.getContent(), document.promotionBasicsForm.characterCounter, 2000);
			return;
		}
		i=0;

		while (x.length > 2000) {
			x = temp.substring(0, 2000-i);
			tinyMCE.selectedInstance.setContent(x);
			x = tinyMCE.selectedInstance.getContent();
			i = i+4;
		}
	}

	function textCounter(textareafield, counterfield, maxlimit) {
		
		//textareafield=textareafield.replaceAll('&nbsp;',' ');
		textareafield=textareafield.replace(/\&nbsp;/g,' ');
		 //alert('textareafield:'+textareafield.length);
		// alert('counterfield:'+counterfield.value);
		// alert('maxlimit:'+maxlimit);
		 counterfield.value = maxlimit - textareafield.length;
	}
		 
  
  function myCustomSaveContent(element_id, html, body) {
	// Do some custom HTML cleanup
	html = html.replace(/&nbsp;/g,' ');
	html = html.replace(/&quot;/g,'\"');
	html = html.replace(/&amp;/g,'&');
	html = html.replace(/<br \/>/g,' ');
  	//trim the string
  	html = html.replace(/^\s+|\s+$/, '');
	return html;
  }
  
  setTimeout(function() {
	  checkTinyMceEditor();
	}, 750);

	<%--
	This checks to see if the tinyMCE editor is actually active.  It won't be active
	on iPads (for example) so just the plain textarea is shown.  So, make sure the
	charcters remaining counter still works.
	--%>
	function checkTinyMceEditor() {
	  var editor = tinyMCE.getInstanceById("purpose");
	  if(typeof editor == "undefined") {
	    var comments = $("textarea[name='purpose']");
	    comments.keyup(function(event) {
	      $("input[name='characterCounter']").val(2000 - comments.val().length);
	    });
	  }
	}
</script>
<%-- End If Nomination Promotion --%>