<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%--UI REFACTORED--%>
  <% String disableAnswerIncorrect = "false"; %>
  <c:if test="${ quizQuestionAnswerForm.quizLive && quizQuestionAnswerForm.quizQuestionAnswerCorrect }">
    <% disableAnswerIncorrect = "true"; %>
  </c:if>

<html:form styleId="contentForm" action="quizQuestionAnswerSave">
  <html:hidden property="method" />
  <html:hidden property="quizQuestionAnswerCmAssetCode" />
  <html:hidden property="quizQuestionAnswerCmKey" />
  <html:hidden property="quizQuestionAnswerExplanationCmKey" />
  <html:hidden property="quizQuestionText" />
  <html:hidden property="quizQuestionStatus" />
  <html:hidden property="quizQuestionStatusText" />
	<beacon:client-state>
		<beacon:client-state-entry name="quizQuestionId" value="${quizQuestionAnswerForm.quizQuestionId}"/>
		<beacon:client-state-entry name="quizQuestionAnswerId" value="${quizQuestionAnswerForm.quizQuestionAnswerId}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="TITLE" code="quiz.question_answer"/></span>
        <span id="quicklink-add"></span><script type="text/javascript">quicklink_display_add('<cms:contentText key="TITLE" code="quiz.question_answer"/>','<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>'); </script>

        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="INSTRUCTIONS" code="quiz.question_answer"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>

        <cms:errors/>
      </td>
    </tr>
    <tr>
      <td>
        <table>
          <tr class="form-row-spacer">
            <beacon:label property="quizQuestionText" required="false">
              <cms:contentText key="QUESTION" code="quiz.question"/>
            </beacon:label>
            <td class="content-field-review">
              <c:out escapeXml='false' value="${quizQuestionAnswerForm.quizQuestionText}" />
            </td>
          </tr>

          <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>

          <tr class="form-row-spacer">
            <beacon:label property="quizQuestionRequired" required="false">
              <cms:contentText key="QUESTION_REQUIRED" code="quiz.question"/>
            </beacon:label>
            <td class="content-field-review">
              <c:choose>
                <c:when test="${quizQuestionAnswerForm.quizQuestionRequired == 'true'}">
                  <cms:contentText code="system.common.labels" key="YES"/>
                </c:when>
                <c:otherwise>
                  <cms:contentText code="system.common.labels" key="NO"/>
                </c:otherwise>
              </c:choose>
            </td>
          </tr>

          <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>

          <tr class="form-row-spacer">
            <beacon:label property="quizQuestionStatus" required="false">
              <cms:contentText key="QUESTION_STATUS" code="quiz.question"/>
            </beacon:label>
            <td class="content-field-review">
              <c:out  escapeXml='false' value="${quizQuestionAnswerForm.quizQuestionStatusText}" />
            </td>
          </tr>

          <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>

          <tr class="form-row-spacer">
            <beacon:label property="quizQuestionAnswerText" required="true">
              <cms:contentText code="quiz.question_answer" key="ANSWER" />
            </beacon:label>
            <td class="content-field-review">
              <%-- TEXTAREA needs to be on one line otherwise when displayed, there will be spaces --%>
              <TEXTAREA style="WIDTH: 100%" id="quizQuestionAnswerText" name="quizQuestionAnswerText" rows=6><c:if test="${quizQuestionAnswerForm.quizQuestionAnswerText != null}"><c:out escapeXml='false' value="${quizQuestionAnswerForm.quizQuestionAnswerText}" /></c:if></TEXTAREA>
            </td>
          </tr>

          <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>

          <tr class="form-row-spacer">
            <beacon:label property="quizQuestionAnswerCorrect" required="true">
              <cms:contentText key="CORRECT" code="quiz.question_answer" />
            </beacon:label>
            <td class="content-field-review">
              <html:radio property="quizQuestionAnswerCorrect" value="false" disabled="<%=disableAnswerIncorrect%>" />&nbsp;<cms:contentText code="system.common.labels" key="NO"/>
              <br>
              <html:radio property="quizQuestionAnswerCorrect" value="true"/>&nbsp;<cms:contentText code="system.common.labels" key="YES"/>
            </td>
          </tr>

          <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>

          <tr class="form-row-spacer">
            <beacon:label property="quizQuestionAnswerExplanation" required="false">
              <cms:contentText code="quiz.question_answer" key="EXPLANATION"/>
            </beacon:label>
            <td class="content-field-review">
              <%-- TEXTAREA needs to be on one line otherwise when displayed, there will be spaces --%>
              <TEXTAREA style="WIDTH: 100%" id="quizQuestionAnswerExplanation" name="quizQuestionAnswerExplanation" rows=6><c:if test="${quizQuestionAnswerForm.quizQuestionAnswerExplanation != null}"><c:out escapeXml='false' value="${quizQuestionAnswerForm.quizQuestionAnswerExplanation}" /></c:if></TEXTAREA>
            </td>
          </tr class="form-row-spacer">

          <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
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
                <cms:contentText code="quiz.question" key="SAVE_ADD_BUTTON" />
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
