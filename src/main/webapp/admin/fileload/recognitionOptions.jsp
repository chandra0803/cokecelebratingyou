<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

<link rel="stylesheet" href="<%= RequestUtils.getBaseURI(request) %>/styles/calendarSkins/aqua/theme.css" type="text/css">
  
<script src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/menu/menu.js" type="text/javascript"></script>
<script src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/commonFunctions.js" type="text/javascript"></script>
<script src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/selectbox.js" type="text/javascript"></script>
<script src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/quicklinks.js" type="text/javascript"></script>
<script src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/modal/common.js" type="text/javascript"></script>
<script src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/modal/popaction.js" type="text/javascript"></script>

<%@ include file="/include/taglib.jspf"%>
<%@ include file="/include/yui-imports.jspf"%>

<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/selectbox.js"></script>
<script src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/tinymce/jscripts/tiny_mce/tiny_mce.js" type="text/javascript"></script>
<script type="text/javascript">
	 tinyMCE.init({
          mode : "textareas",
          theme : "advanced",
          gecko_spellcheck : true ,
          forced_root_block : false,
		  force_br_newlines : true,
          force_p_newlines : false,
          remove_linebreaks : false,
          paste_auto_cleanup_on_paste : true,
		  paste_text_sticky : true,
		  paste_text_sticky_default :true,
          convert_urls : false,
          convert_newlines_to_brs : true,
          invalid_elements : "nbsp,p,pre,br,a",
          plugins : "spellchecker,paste",
          theme_advanced_buttons1 : "spellchecker",
          theme_advanced_buttons2 : "",
          theme_advanced_buttons3 : "",
          theme_advanced_toolbar_location : "top",
          theme_advanced_toolbar_align : "left",
          spellchecker_languages :"+${textEditorDictionaries}",
          spellchecker_rpc_url    : "<%=RequestUtils.getBaseURI(request)%>/spellchecker/jazzySpellCheck.do",
          save_callback : "myCustomSaveContent"
      }); 

  function myCustomSaveContent(element_id, html, body) 
  {
  	// Do some custom HTML cleanup
  	html = html.replace(/&nbsp;/g,' ');
  	html = html.replace(/&quot;/g,'\"');
	html = html.replace(/&amp;/g,'&');
	html = html.replace(/<br \/>/g,' ');
	//trim the string
	html = html.replace(/^\s+|\s+$/, '');
	return html;
  }
</script>
  
<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript">

  function saveOptions(callToMethod)
  {
   document.getElementById('method') .value=callToMethod;
   setDispatch(callToMethod);
   //window.close();
  }

  function hideOrShowLayer( whichLayer, checkBox )
  {
    if ( checkBox.checked==true )
    {
      hideLayer( whichLayer );
    }
    else
    {
      showLayer( whichLayer );
    }
  }

  function showLayer(whichLayer)
  {
    if (document.getElementById)
    {
      if(document.getElementById(whichLayer) != null)
      {
		// this is the way the standards work
	    var style2 = document.getElementById(whichLayer).style;
		style2.display = "block";
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

  function toggleRadioButton( whichButton, checkbox )
  {
    if ( checkbox.checked == true )
    {
      document.getElementById(whichButton).checked = true;
    }
  }

  function unselectCheckboxes( whichCheckBoxes, radiobutton )
  {
    if ( radiobutton.checked == true )
    {
      for ( i=0;i<getContentForm().length;i++ )
      {
        if ( whichCheckBoxes == 'prodClaim' )
        {
          if( getContentForm().elements[i].name.substring(0,18)  == 'prodClaimProxyList' &&
              getContentForm().elements[i].name.substring(22) == 'selected' )
          {
		    getContentForm().elements[i].checked = false;
          }
        }
        if ( whichCheckBoxes == 'recognition' )
        {
          if( getContentForm().elements[i].name.substring(0,20)  == 'recognitionProxyList' &&
              getContentForm().elements[i].name.substring(24) == 'selected' )
          {
		    getContentForm().elements[i].checked = false;
          }
        }
        if ( whichCheckBoxes == 'nomination' )
        {
          if( getContentForm().elements[i].name.substring(0,19)  == 'nominationProxyList' &&
              getContentForm().elements[i].name.substring(23) == 'selected' )
          {
		    getContentForm().elements[i].checked = false;
          }
        }
      }
    }
  }

 function previewRecOptionsMessage(obj)
    {
      
	  var returnValue = false;

      var selectObj =document.getElementById(obj);
	  
      if (selectObj != null)
      {
  
       var tools="";
	       
       tools = "resizable,toolbar=no,location=no,scrollbars=no"+",width=750"+",height=500s"+",left=0,top=0";
	    newWindow = window.open('<%= request.getContextPath() %>/admin/displayPreviewMessage.do?doNotSaveToken=true&messageId=' + selectObj.value, 'newWin', tools);
	    newWindow.focus();
		
      }

      return returnValue;
    }

</script>

  <html:form styleId="contentForm" action="displayRecognitionOptions">
  <html:hidden property="method" value="save" styleId="method"/>
  <html:hidden property="submitterId" value="${recognitionOptionsForm.submitterId}" styleId="submitterId"/>
  <html:hidden property="submitterNodeId" value="${recognitionOptionsForm.submitterNodeId}" styleId="submitterNodeId"/>
  <html:hidden property="importFileId" value="${recognitionOptionsForm.importFileId}" />
  <html:hidden property="importFileType" value="${recognitionOptionsForm.importFileType}" />
  <html:hidden property="promotionId" value="${recognitionOptionsForm.promotionId}"/>
  <html:hidden property="messageId" value="${recognitionOptionsForm.messageId}"/>
  
  <table border="0" cellpadding="20" cellspacing="0" width="100%">
    <tr>
      <td>
      
      <table border="0" cellpadding="3" cellspacing="1" width="100%">
		<tr class="form-row-spacer">
		<td>
			<%-- title and instructional copy --%>
           	<span class="headline">
            	<cms:contentText key="RECOGNITION_OPTIONS_TITLE" code="admin.fileload.importFileDetails" />
           	</span>
           	<br/><br/><br/>
           
		   	<%--INSTRUCTIONS--%>
           	<span class="content-instruction">
            	<cms:contentText key="RECOGNITION_OPTIONS_INSTRUCTIONS" code="admin.fileload.importFileDetails"/>
           	</span>
		  	<%--END INSTRUCTIONS--%>
		  
     	  	<br/>
     	  
		  	<cms:errors />
		</td>
		</tr>
      </table>
      
      <%-- Message Details --%>
      <div class="recognition-area">
      <table border="0" cellpadding="3" cellspacing="1" width="100%">
	    <tr>
    	<td valign="top" align="left" colspan="3">
            <span class="subheadline"><cms:contentText key="RECEPIENT_NOTIFICATION_MESSAGE"
				code="admin.fileload.importFileDetails" /></span>
        </td>
	    </tr>
	  
		<%-- Needed between every regular row --%>
		<tr class="form-blank-row">
			<td colspan="3">&nbsp;</td>
		</tr>
		
		<tr>
		<beacon:label property="searchBy" required="false"
			requiredColumnWidth="10" labelColumnWidth="50">
			<cms:contentText key="MESSAGE_LABEL" code="admin.fileload.importFileDetails" />:
		</beacon:label>
		<td class="content-field-label" valign="top" align="left"><c:out value="${recognitionOptionsForm.message}"/> &nbsp;&nbsp; 
		    <a href="javascript:void(0)" onclick="previewRecOptionsMessage('messageId'); return false;"><cms:contentText
			key="PREVIEW" code="admin.fileload.importFileDetails" /></a></td>
		</tr>
	  
		<%-- Needed between every regular row --%>
		<tr class="form-blank-row">
			<td colspan="3">&nbsp;</td>
		</tr>
		
      </table>
      </div>
      
      <%-- Submitter Details --%>
      <div class="recognition-area">
      <table border="0" cellpadding="3" cellspacing="1" width="100%">
	    <tr>
    	<td valign="top" align="left" colspan="4">
            <span class="subheadline"><cms:contentText key="SELECT_THE_GIVER"
				code="admin.fileload.importFileDetails" /></span>
        </td>
	    </tr>
	    
		<%-- Needed between every regular row --%>
		<tr class="form-blank-row">
			<td colspan="4">&nbsp;</td>
		</tr>
		
		<tr>
   		  <beacon:label property="searchBy" required="false"
				requiredColumnWidth="10" labelColumnWidth="120">
				<cms:contentText key="SEARCH_BY" code="participant.search" />:
          </beacon:label>
			<td class="content-field" width="5"><select name="searchBy" id="searchBy"
				class="killme" onchange="searchByChanged();">
				<option value="lastName"><cms:contentText
					key="SEARCH_BY_LAST_NAME" code="participant.search" /></option>
				<option value="firstName"><cms:contentText
					key="SEARCH_BY_FIRST_NAME" code="participant.search" /></option>
				<option value="location"><cms:contentText
					key="SEARCH_BY_LOCATION" code="participant.search" /></option>
				<option value="jobTitle"><cms:contentText
					key="SEARCH_BY_JOB_TITLE" code="participant.search" /></option>
				<option value="department"><cms:contentText
					key="SEARCH_BY_DEPARTMENT" code="participant.search" /></option>
			</select></td>
			<td class="content-field"><div id="autocomplete" class="yui-ac"><input type="text"
				class="content-field" size="20" name="searchQuery" id="searchQuery"
				styleClass="killme"><div id="searchResults" style="z-index:1;width:500px;"></div></div></td>
		</tr>
		
		<tr id="secondaryResultsRow" style="display:none;">
			<td colspan="3"></td>
			<td class="content-field">
      			<table width="100%">
        		<tr>
          			<td>
            			<select id="secondaryResults" size="5" style="width: 350px;"  class="content-field" styleClass="killme"/>
          			</td>
          			<td width="100%" valign="top" id="secondaryResultsButtons"><input type="button" id="addButton" class="content-buttonstyle" onclick="paxSearch_addSelectedSecondaryResults('secondaryResults');" value="<cms:contentText key="SELECT" code="system.button"/>"></input></td>
				</tr>
				</table>
			</td>
		</tr>
		
		<%-- Needed between every regular row --%>
		<tr class="form-blank-row">
			<td colspan="4">&nbsp;</td>
		</tr>
		
		<tr>
			<td colspan="1">&nbsp;</td>
			<td class="content-field"><cms:contentText key="IS_GIVER"
				code="admin.fileload.importFileDetails" />:</td>
			<td class="content-field" nowrap colspan="2" id="selectedProxyText">
			<c:choose>
				<c:when test="${empty recognitionOptionsForm.submitterName}">
					<cms:contentText code="proxy.detail" key="NONE_DEFINED" />
				</c:when>
				<c:otherwise>
					<c:out value="${recognitionOptionsForm.submitterName}"/>
				</c:otherwise>
			</c:choose></td>
		</tr>
		
		<%-- Needed between every regular row --%>
		<tr class="form-blank-row">
			<td colspan="4">&nbsp;</td>
		</tr>
		
      </table>
      </div>
      
	  <c:if test="${isCardActive}">
	  <div class="recognition-area">
		<tiles:insert attribute="cardInsert" />
	  </div>
	  </c:if>
	  
	  <div class="recognition-area">	  
		<table border="0" cellpadding="3" cellspacing="1" width="100%">
			<c:if test="${!empty behaviorList}">
				<tr>
					<td valign="top" align="left" colspan="1">
						<span class="subheadline"><cms:contentText key="SELECT_BEHAVIOR"
							code="admin.fileload.importFileDetails" /></span>
					</td>
				</tr>
				<tr class="form-blank-row">
					<td colspan="1">&nbsp;</td>
				</tr>
			</c:if>	
		</table>
		<table border="0" cellpadding="3" cellspacing="1" width="100%">
		  <c:if test="${!empty behaviorList}">
		    <tr class="form-row-spacer">
		    
		      <beacon:label property="selectedBehavior" required="true" requiredColumnWidth="10" labelColumnWidth="120">
		        <cms:contentText key="BEHAVIOR" code="recognition.select.recipients"/>
		      </beacon:label>
		     
		      <td colspan="2">
		        <c:choose>
		          <c:when test="${empty behaviorList}">&nbsp;</c:when>
		          <c:otherwise>
		            <html:select property="selectedBehavior" styleClass="content-field">
		              <html:option value=''>
		                <cms:contentText key="CHOOSE_ONE" code="system.general" />
		              </html:option>
		              <html:options collection="behaviorList" property="code" labelProperty="name" />
		            </html:select>
		          </c:otherwise>
		        </c:choose>
		      </td>
		  	</tr>
		  </c:if>
		</table>
	</div>
	  
	 <c:if test="${showComments == 'false'}">	
      <%-- Submitter Details --%>
      <div class="recognition-area">
      <table border="0" cellpadding="3" cellspacing="1" width="100%">
	    <tr>
    	<td valign="top" align="left" colspan="1">
            <span class="subheadline"><cms:contentText key="ENTER_COMMENTS"
				code="admin.fileload.importFileDetails" /></span>
        </td>
	    </tr>
	    
		<%-- Needed between every regular row --%>
		<tr class="form-blank-row">
			<td colspan="1">&nbsp;</td>
		</tr>
		  
		  <tr class="form-row-spacer">
		    <td class="content-field" valign="top">
		    <table border="0" cellpadding="0" cellspacing="0">

		    <tr>
		      <beacon:label property="comments" required="false" styleClass="content-field-label-top" requiredColumnWidth="10" labelColumnWidth="120">
		        <cms:contentText key="COMMENTS" code="recognition.select.recipients"/>:
		      </beacon:label>
		      <td><html:textarea styleClass="content-field-label hideZIndex" property="comments" cols="75" rows="5" /></td>
		    </tr>
		    </table>
		    </td>
		  </tr>
	    
		<%-- Needed between every regular row --%>
		<tr class="form-blank-row">
			<td colspan="1">&nbsp;</td>
		</tr>
      </table>
      </div>
	</c:if>
	 <!-- Copy to Manager -->
	  <div class="recognition-area">	  
		 <table border="0" cellpadding="3" cellspacing="1" width="100%">
		  <tr>
			  <td valign="top" align="left" colspan="1">
			            <span class="subheadline"><cms:contentText key="MORE_INFORMATION"
							code="admin.fileload.importFileDetails" /></span>
			        </td>
		  </tr>
		        <tr class="form-blank-row">
					<td colspan="1">&nbsp;</td>
				</tr>
				 <tr class="form-row-spacer">
				     <td class="content-field" valign="top">
			        <table border="0" cellpadding="0" cellspacing="0">	    
								<tr class="form-row-spacer">
								  <beacon:label required="false" styleClass="content-field-label-top">
								    <cms:contentText key="COPY_RECIPIENT_MGR" code="admin.fileload.importFileDetails" />
								  </beacon:label>
								  <td colspan=2 class="content-field">
								  	<table>
											<tr>
												<td class="content-field"><html:radio property="copyManager" value="false"  /></td>
												<td class="content-field"><cms:contentText code="system.common.labels" key="NO"/></td>
											</tr>
											<tr>
												<td class="content-field"><html:radio property="copyManager" value="true"  /></td>
												<td class="content-field"><cms:contentText code="system.common.labels" key="YES"/></td>
											</tr>
								    	<tr>
								      	    				
								      </table>
									</td>
								 </tr>
				  </table>
			     </td>
				  </tr>
				<%-- Needed between every regular row --%>
				<tr class="form-blank-row">
					<td colspan="1">&nbsp;</td>
				</tr>
				 </table>
		 </DIV> 
	<%-- buttons --%>
	<table width="100%">
		<tr class="form-buttonrow">
			<td class="headline" align="center">
			<html:submit
				styleClass="content-buttonstyle"
				onclick="javascript:saveOptions('save');">
				<cms:contentText code="system.button" key="SAVE" />
			</html:submit> <html:cancel styleClass="content-buttonstyle"
				onclick="javascript:window.close()">
				<cms:contentText code="system.button" key="CANCEL" />
			</html:cancel></td>
		</tr>
	</table>
	
      </td>
    </tr>
  </table>
</html:form>

<%@ include file="/include/paxSearch.jspf"%>
<script type="text/javascript">
	var adminCopyManager = <c:out value="${adminCopyManager}"/>;
    var recCopyMgr = document.getElementsByName("copyManager");
	
	if( adminCopyManager == true)
	{
		recCopyMgr[0].checked=false;
		recCopyMgr[1].checked=true;
		recCopyMgr[0].disabled=true;
		recCopyMgr[1].disabled=true;
	}
  YAHOO.example.ACXml = paxSearch_instantiate(
      '<%=RequestUtils.getBaseURI(request)%>/proxy/participantSearch.do',
      '<c:out value="${proxyDetailForm.mainUserNode}"/>'
  );

  function paxSearch_selectPax( paxId, nodeId, paxDisplayName ) {
    
    //document.getElementById('proxyUserId').value = paxId;
   document.getElementById('selectedProxyText').innerHTML = paxDisplayName;
   document.getElementById('submitterId') .value=paxId;
   document.getElementById('submitterNodeId') .value=nodeId;
   
  }

  function searchByChanged() {
    document.getElementById('searchQuery').value = '';
    document.getElementById('secondaryResultsRow').style.display = 'none';
  }
  
YAHOO.util.Event.addListener(window, "load", function(){ YAHOO.util.Dom.addClass(document.body,"bodystyle bi-yui") });

</script>
