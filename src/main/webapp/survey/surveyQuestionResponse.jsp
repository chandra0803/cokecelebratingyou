<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%--UI REFACTORED--%>
 

<html:form styleId="contentForm" action="surveyQuestionResponseSave">
  <html:hidden property="method" />
  <html:hidden property="surveyQuestionResponseCmAssetCode" />
  <html:hidden property="surveyQuestionText" />
  <html:hidden property="surveyQuestionStatus" />
  <html:hidden property="surveyQuestionStatusText"/>
  <html:hidden property="surveyPromotion" />
	<beacon:client-state>
		<beacon:client-state-entry name="surveyQuestionId" value="${surveyQuestionResponseForm.surveyQuestionId}"/>
		<beacon:client-state-entry name="surveyQuestionResponseId" value="${surveyQuestionResponseForm.surveyQuestionResponseId}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="TITLE" code="survey.question_response"/></span>
        <span id="quicklink-add"></span><script type="text/javascript">quicklink_display_add('<cms:contentText key="TITLE" code="survey.question_response"/>','<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>'); </script>

        <%--INSTRUCTIONS--%>
        <br/>
        <span class="content-instruction">
          <cms:contentText key="INSTRUCTIONS" code="survey.question_response"/>
        </span>
        <br/>
        <%--END INSTRUCTIONS--%>

        <cms:errors/>
      </td>
    </tr>
    <tr>
      <td>
        <table>
          <tr class="form-row-spacer">
            <beacon:label property="surveyQuestionText" required="false">
              <cms:contentText key="QUESTION" code="survey.question"/>
            </beacon:label>
            <td class="content-field-review">
              <c:out escapeXml='false' value="${surveyQuestionResponseForm.surveyQuestionText}" />
            </td>
          </tr>

          <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td colspan="4"></td>
          </tr>

          <tr class="form-row-spacer">
            <beacon:label property="surveyQuestionStatus" required="false">
              <cms:contentText key="QUESTION_STATUS" code="survey.question"/>
            </beacon:label>
            <td class="content-field-review">
              <c:out  escapeXml='false' value="${surveyQuestionResponseForm.surveyQuestionStatusText}" />
            </td>
          </tr>
          
          <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td colspan="4"></td>
          </tr>

          <tr class="form-row-spacer" id="surveyQuestionResponseText">
            <beacon:label property="surveyQuestionResponseText" required="true">
              <cms:contentText code="survey.question_response" key="RESPONSE" />
            </beacon:label>

            <td class="content-field">
              <%-- TEXTAREA needs to be on one line otherwise when displayed, there will be spaces --%>
              <TEXTAREA name="surveyQuestionResponseText" rows=6><c:out escapeXml='false' value="${surveyQuestionResponseForm.surveyQuestionResponseText}" /></TEXTAREA>
            </td>
          </tr>

          <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td colspan="4"></td>
          </tr>
          
          <tr class="form-row-spacer">				  
            <beacon:label property="surveyQuestionResponseStatus" required="true">
              <cms:contentText key="RESPONSE_STATUS" code="survey.question_response"/>
            </beacon:label>	        
            <td class="content-field-review">
              <html:select property="surveyQuestionResponseStatus" styleClass="content-field" >
  				<html:options collection="surveyQuestionStatusList" property="code" labelProperty="name"/>
              </html:select>	
            </td>
		  </tr>
          
          
          <tr class="form-buttonrow">
           	<td></td>
           	<td align="left" colspan="2">
              <beacon:authorize ifNotGranted="LOGIN_AS">
              <html:submit styleClass="content-buttonstyle" onclick="setDispatch('save')">
                <cms:contentText code="system.button" key="SAVE" />
              </html:submit>
              </beacon:authorize>

              <beacon:authorize ifNotGranted="LOGIN_AS">
              <html:submit styleClass="content-buttonstyle" onclick="setDispatch('saveAndAddAnother')">
                <cms:contentText code="survey.question" key="SAVE_ADD_BUTTON" />
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
    remove_linebreaks : false,    
    convert_newlines_to_brs : true,
    preformatted : true,
	convert_urls : false,
    paste_auto_cleanup_on_paste : true,
    paste_text_sticky : true,
    paste_text_sticky_default :true, 	
    invalid_elements : "nbsp,p,pre",
    theme_advanced_buttons1 : "spellchecker,separator,insertdate,inserttime,separator,copy,cut,paste,undo,redo,separator,search,replace,separator,insertfield",
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
	html = html.replace(/<br \/>/g,' ');
  //trim the string
  html = html.replace(/^\s+|\s+$/, '');
	return html;
}
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
  
</script>
