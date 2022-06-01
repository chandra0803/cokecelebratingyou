<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%--UI REFACTORED--%>
  <script type="text/javascript">

  </script>
  
  <% String disableRequired = "false"; %>
  <c:if test="${ quizQuestionForm.quizType == 'fixed' }">
    <% disableRequired = "true"; %>
  </c:if>

<html:form styleId="contentForm" action="quizQuestionSave">
  <html:hidden property="method" />  
	<html:hidden property="quizName" />
  <html:hidden property="quizType" />
  <html:hidden property="newAnswerSequenceNum" />
  <html:hidden property="quizQuestionCmAssetName" />
	<beacon:client-state>
		<beacon:client-state-entry name="numberOfQuestionsAsked" value="${quizQuestionForm.numberOfQuestionsAsked}"/>
		<beacon:client-state-entry name="questionCount" value="${quizQuestionForm.questionCount}"/>
		<beacon:client-state-entry name="activeQuestionCount" value="${quizQuestionForm.activeQuestionCount}"/>
		<beacon:client-state-entry name="quizQuestionAnswerId" value="${quizQuestionForm.quizQuestionAnswerId}"/>
		<beacon:client-state-entry name="quizFormId" value="${quizQuestionForm.quizFormId}"/>
		<beacon:client-state-entry name="quizQuestionId" value="${quizQuestionForm.quizQuestionId}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
	  	<td>
        <span class="headline"><cms:contentText key="TITLE" code="quiz.question"/></span>
        <span id="quicklink-add"></span><script type="text/javascript">quicklink_display_add('<cms:contentText key="TITLE" code="quiz.question"/>','<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>'); </script>	  
        
        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="INSTRUCTIONS" code="quiz.question"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
     	
        <cms:errors/>
        
        <table>
		  <tr class="form-row-spacer">				  
            <beacon:label property="quizQuestionText" required="true" styleClass="content-field-label-top">
              <cms:contentText key="QUESTION" code="quiz.question"/>
            </beacon:label>	
            <td class="content-field-review">
              <%-- TEXTAREA needs to be on one line otherwise when displayed, there will be spaces --%>
              <TEXTAREA style="WIDTH: 100%" id="quizQuestionText" name="quizQuestionText" rows=6><c:if test="${quizQuestionForm.quizQuestionText != null}"><c:out escapeXml='false' value="${quizQuestionForm.quizQuestionText}" /></c:if></TEXTAREA>	
            </td>					
		  </tr>

          <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>	        
          
		  <tr class="form-row-spacer">			
            <beacon:label property="quizQuestionRequired" required="true" styleClass="content-field-label-top">
              <cms:contentText key="QUESTION_REQUIRED" code="quiz.question"/>
            </beacon:label>	        
            <td class="content-field-review">
              <html:radio styleId="required[1]" property="quizQuestionRequired" value="true"/>&nbsp;<cms:contentText code="system.common.labels" key="YES"/>
              <br>
              <html:radio styleId="required[0]" property="quizQuestionRequired" value="false" disabled="<%=disableRequired%>" />&nbsp;<cms:contentText code="system.common.labels" key="NO"/>
            </td>
          </tr>

          <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>	        
          
		  <tr class="form-row-spacer">				  
            <beacon:label property="quizQuestionStatus" required="true">
              <cms:contentText key="QUESTION_STATUS" code="quiz.question"/>
            </beacon:label>	        
            <td class="content-field-review">
              <html:select property="quizQuestionStatus" size="1" styleClass="content-field" >
<%--            <html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>  --%>
  				<html:options collection="quizQuestionStatusList" property="code" labelProperty="name"/>
              </html:select>	
            </td>
		  </tr>
					
          <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
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

<c:if test="${ quizQuestionForm.quizType == 'fixed' }">
   document.getElementById('required[1]').checked = true;
</c:if>
</script> 