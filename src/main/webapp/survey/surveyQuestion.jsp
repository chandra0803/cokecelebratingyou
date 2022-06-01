<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%--UI REFACTORED--%>


<html:form styleId="contentForm" action="surveyQuestionSave">
  <html:hidden property="method" />  
	<html:hidden property="surveyName" />
  <html:hidden property="newResponseSequenceNum" />
  <html:hidden property="surveyQuestionCmAssetName" />
	<beacon:client-state>
		<beacon:client-state-entry name="surveyQuestionResponseId" value="${surveyQuestionForm.surveyQuestionResponseId}"/>
		<beacon:client-state-entry name="surveyFormId" value="${surveyQuestionForm.surveyFormId}"/>
		<beacon:client-state-entry name="surveyQuestionId" value="${surveyQuestionForm.surveyQuestionId}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
	  	<td>
        <span class="headline"><cms:contentText key="TITLE" code="survey.question"/></span>
        <span id="quicklink-add"></span><script type="text/javascript">quicklink_display_add('<cms:contentText key="TITLE" code="survey.question"/>','<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>'); </script>	  
        
        <%--INSTRUCTIONS--%>
        <br/>
        <span class="content-instruction">
          <cms:contentText key="INSTRUCTIONS" code="survey.question"/>
        </span>
        <br/>
        <%--END INSTRUCTIONS--%>
     	
        <cms:errors/>
        
        <table>
		  <tr class="form-row-spacer">				  
            <beacon:label property="surveyQuestionText" required="true" styleClass="content-field-label-top">
              <cms:contentText key="QUESTION" code="survey.question"/>
            </beacon:label>	
            <td class="content-field-review">
              <%-- TEXTAREA needs to be on one line otherwise when displayed, there will be spaces --%>
              <TEXTAREA style="WIDTH: 100%" id="surveyQuestionText" name="surveyQuestionText" rows=6><c:if test="${surveyQuestionForm.surveyQuestionText != null}"><c:out escapeXml='false' value="${surveyQuestionForm.surveyQuestionText}" /></c:if></TEXTAREA>	
            </td>					
		  </tr>

          <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>	        
          <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>	        
          
		  <tr class="form-row-spacer">				  
            <beacon:label property="surveyQuestionStatus" required="true">
              <cms:contentText key="QUESTION_STATUS" code="survey.question"/>
            </beacon:label>	        
            <td class="content-field-review">
              <html:select property="surveyQuestionStatus" size="1" styleClass="content-field" >
<%--            <html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>  --%>
  				<html:options collection="surveyQuestionStatusList" property="code" labelProperty="name"/>
              </html:select>	
            </td>
		  </tr>
					
          <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>
          
           <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td colspan="4"></td>
          </tr>
          
          <tr class="form-row-spacer" id="surveyOpenEnded" valign="top">
            <beacon:label property="surveyOpenEnded" required="false">
              <cms:contentText key="RESPONSE_TPYE" code="survey.question"/>
            </beacon:label>
            <td class="content-field" align="left">
		        <table>
		          <tr>
		            <td class="content-field" valign="top">
					  <html:radio styleId="standardResponse" property="responseType" value="standardResponse" onclick="updateLayersShown();" />
		                <cms:contentText key="STANDARD_RESPONSE" code="survey.question"/>
		            </td>
		          </tr>
		          <tr>
		            <td class="content-field" valign="top">
		              <html:radio styleId="openEnded" property="responseType" value="openEnded" onclick="updateLayersShown();" />
		                <cms:contentText key="OPEN_QUESTION" code="survey.question"/>
		            </td>
		          </tr>
		          <tr>
		            <td class="content-field" valign="top">
		              <html:radio styleId="slider" property="responseType" value="sliderSelection" onclick="updateLayersShown();" />
		                <cms:contentText key="SLIDER_SELECTION" code="survey.question"/>
		            </td>
		          </tr>
		        </table>
		    </td>
          </tr> 
          
           <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td colspan="4"></td>
          </tr>
          
          <tr class="form-row-spacer" id="surveyOpenEndedRequired" valign="top">
            <beacon:label property="surveyOpenEndedRequired" required="false">
              <cms:contentText key="OPEN_QUESTION_REQUIRED" code="survey.question"/>?
            </beacon:label>
            <td class="content-field" align="left">
		        <table>
		          <tr>
		            <td class="content-field" valign="top">
					  <html:radio styleId="openEndedRequiredFalse" property="openEndedRequired" value="false" onclick="updateLayersShown();" />
		              <cms:contentText  code="system.common.labels" key="NO" />
		            </td>
		          </tr>
		          <tr>
		            <td class="content-field" valign="top">
		              <html:radio styleId="openEndedRequiredTrue" property="openEndedRequired" value="true" onclick="updateLayersShown();" />
		                <cms:contentText  code="system.common.labels" key="YES" />
		            </td>
		          </tr>
		        </table>
		    </td>
          </tr>
          
          <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>	        
          
		  <tr class="form-row-spacer" id="startSelectionLabel">		  
            <beacon:label property="startSelectionLabel" required="true">
              <cms:contentText key="START_LABEL" code="survey.question"/>
            </beacon:label>	        
            <td class="content-field-review">
              <html:text property="startSelectionLabel" size="15" maxlength="30" styleClass="content-field" />
            </td>
		  </tr>
					
          <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>	        
          
		  <tr class="form-row-spacer" id="endSelectionLabel">		  
            <beacon:label property="endSelectionLabel" required="true">
              <cms:contentText key="END_LABEL" code="survey.question"/>
            </beacon:label>	        
            <td class="content-field-review">
              <html:text property="endSelectionLabel" size="15" maxlength="30" styleClass="content-field" />
            </td>
		  </tr>
          
          <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>	        
          
		  <tr class="form-row-spacer" id="startSelectionValue">		  
            <beacon:label property="startSelectionValue" required="true">
              <cms:contentText key="START_VALUE" code="survey.question"/>
            </beacon:label>	        
            <td class="content-field-review">
              <html:text property="startSelectionValue" size="15" maxlength="30" styleClass="content-field" />
            </td>
		  </tr>
					
          <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>	        
          
		  <tr class="form-row-spacer" id="endSelectionValue">		  
            <beacon:label property="endSelectionValue" required="true">
              <cms:contentText key="END_VALUE" code="survey.question"/>
            </beacon:label>	        
            <td class="content-field-review">
              <html:text property="endSelectionValue" size="15" maxlength="30" styleClass="content-field" />
            </td>
		  </tr>
					
           <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>	        
          
		  <tr class="form-row-spacer" id="precision">		  
            <beacon:label property="precision" required="true">
              <cms:contentText key="PRECISION" code="survey.question"/>
            </beacon:label>	        
            <td class="content-field-review">
              <html:text property="precision" size="10" maxlength="10" styleClass="content-field" />
            </td>
		  </tr>
					
	      <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td colspan="4"></td>
          </tr>

		  <tr class="form-buttonrow">
			<td></td>
           	<td></td>
           	<td align="left">
              <beacon:authorize ifNotGranted="LOGIN_AS">
              <html:submit styleClass="content-buttonstyle" onclick="setDispatch('save')">
                <cms:contentText code="system.button" key="SAVE" />
              </html:submit>
              </beacon:authorize>
              <html:cancel styleClass="content-buttonstyle" onclick="setDispatch('save')">
                <cms:contentText code="system.button" key="CANCEL" />
              </html:cancel>
							
	       	</td>
          </tr>
		</table>
      </td>
	</tr>        
  </table>
</html:form>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/tinymce/jscripts/tiny_mce/tiny_mce.js"></script>
<script type="text/javascript">
  tinyMCE.init(
  {
    mode : "textareas",
    theme : "advanced",
    plugins : "spellchecker,paste,insertdatetime,searchreplace,insertfield",
    forced_root_block : false,
    gecko_spellcheck : true ,
	force_br_newlines : true,          
    force_p_newlines : false,
    force_br_newlines : true,
    remove_linebreaks : false,
    convert_newlines_to_brs : true,
	convert_urls : false,
    paste_auto_cleanup_on_paste : true,
    paste_text_sticky : true,
    paste_text_sticky_default :true,	
    preformatted : true,
    invalid_elements : "nbsp,p,pre",
	theme_advanced_buttons1 : "spellchecker,separator,insertdate,inserttime,separator,copy,cut,paste,undo,redo,separator,search,replace",
    theme_advanced_buttons2 : "",
    theme_advanced_buttons3 : "",
    theme_advanced_toolbar_location : "top",
    theme_advanced_toolbar_align : "left",
    plugin_insertdate_dateFormat : "%Y-%m-%d",
    plugin_insertdate_timeFormat : "%H:%M:%S",
    spellchecker_languages :"+${textEditorDictionaries}",
    spellchecker_rpc_url    : "<%=RequestUtils.getBaseURI(request)%>/spellchecker/jazzySpellCheck.do",
    save_callback : "myCustomSaveContent"
  });
  
  function myCustomSaveContent(element_id, html, body) {
	// Do some custom HTML cleanup
	html = html.replace(/&nbsp;/g,' ');
	html = html.replace(/&quot;/g,'\"');
	html = html.replace(/&amp;/g,'&');
  //trim the string
  html = html.replace(/^\s+|\s+$/, '');
	return html;
}

/* <c:if test="${ surveyQuestionForm.surveyType == 'fixed' }">
   document.getElementById('required[1]').checked = true;
</c:if> */
</script> 

<script type="text/javascript">
<!--
function hideLayer(whichLayer)
{
  if (document.getElementById)
  {
    // this is the way the standards work
    if (document.getElementById(whichLayer))
    {
    var style2 = document.getElementById(whichLayer).style;
    style2.display = "none";
    }
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


function showLayer(whichLayer)
{
  if (document.getElementById)
  {
    // this is the way the standards work
    if (document.getElementById(whichLayer))
    {
        var style2 = document.getElementById(whichLayer).style;
        style2.display = "";
    }
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
  
  
  function updateLayersShown()
 {
 	var standardResponseObj = document.getElementById("standardResponse");
 	var openEndedObj = document.getElementById("openEnded");
 	var sliderObj = document.getElementById("slider");
 	
 	if (openEndedObj != null && openEndedObj.checked == true )
 	{
 		showLayer("surveyOpenEndedRequired");
 	}
 	else
 	{
 		hideLayer("surveyOpenEndedRequired");
 	}
 	
 	if( sliderObj != null && sliderObj.checked == true )
	{
 		showLayer("startSelectionLabel");
 		showLayer("endSelectionLabel");
 		showLayer("startSelectionValue");
 		showLayer("endSelectionValue");
 		showLayer("precision");
	}
 	else
	{
 		hideLayer("startSelectionLabel");
 		hideLayer("endSelectionLabel");
 		hideLayer("startSelectionValue");
 		hideLayer("endSelectionValue");
 		hideLayer("precision");
	}
 } 

</script>

 <script type="text/javascript">
 updateLayersShown();
 </script> 