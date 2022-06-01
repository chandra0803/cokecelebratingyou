<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>
<%--UI REFACTORED--%>
<html:form styleId="contentForm" action="calculatorCriterionSave">
  <html:hidden property="method" />
  <html:hidden property="calculatorName" />
  <html:hidden property="calculatorStatus" />
  <html:hidden property="weightedScore" />
  <html:hidden property="cmAssetName" />
	<beacon:client-state>
	  <beacon:client-state-entry name="calculatorId" value="${calculatorCriterionForm.calculatorId}"/>
	  <beacon:client-state-entry name="calculatorCriterionId" value="${calculatorCriterionForm.calculatorCriterionId}"/>
	</beacon:client-state>
  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
	  <td>
        <span class="headline"><cms:contentText key="TITLE" code="calculator.criterion"/></span>
        <span id="quicklink-add"></span><script type="text/javascript">quicklink_display_add('<cms:contentText key="TITLE" code="calculator.criterion"/>','<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>'); </script>	  
        <br/>
        <span class="subheadline">
          <c:out value="${calculatorCriterionForm.calculatorName}"/>
        </span>
        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="INSTRUCTIONS" code="calculator.criterion"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
     	
        <cms:errors/>
        
        <table>
		  <tr class="form-row-spacer">				  
            <beacon:label property="criterionText" required="true" styleClass="content-field-label-top">
              <cms:contentText key="CRITERION" code="calculator.criterion"/>
            </beacon:label>	
            <td class="content-field-review">
              <%-- TEXTAREA needs to be on one line otherwise when displayed, there will be spaces --%>
              <TEXTAREA style="WIDTH: 100%" id="criterionText" name="criterionText" rows=6><c:if test="${calculatorCriterionForm.criterionText != null}"><c:out escapeXml='false' value="${calculatorCriterionForm.criterionText}" /></c:if></TEXTAREA>	
            </td>					
		  </tr>

          <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>	        
          
          <c:if test="${calculatorCriterionForm.weightedScore == 'true'}">
  		    <tr class="form-row-spacer">			
              <beacon:label property="weightValue" required="true" styleClass="content-field-label-top">
                <cms:contentText key="WEIGHT" code="calculator.criterion"/>
              </beacon:label>	        
              <td class="content-field-review">
                <html:text property="weightValue"/>
              </td>
            </tr>
          </c:if>
          
          <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>	        
          
		  <tr class="form-row-spacer">				  
            <beacon:label property="criterionStatus" required="true">
              <cms:contentText key="CRITERION_STATUS" code="calculator.criterion"/>
            </beacon:label>	        
            <td class="content-field-review">
              <html:select property="criterionStatus" size="1" styleClass="content-field" >
<%--            <html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>  --%>
  				<html:options collection="criterionStatusList" property="code" labelProperty="name"/>
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
           	  <c:if test="${calculatorCriterionForm.editable}">
                <beacon:authorize ifNotGranted="LOGIN_AS">
              	  <html:submit styleClass="content-buttonstyle" onclick="setDispatch('save')">
                	<cms:contentText code="system.button" key="SAVE" />
              	  </html:submit>
              	</beacon:authorize>
              </c:if>
              <html:cancel styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('calculatorView.do', 'view');">
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
    remove_linebreaks : true,
    convert_newlines_to_brs : false,
	convert_urls : false,
    preformatted : true,
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
	html = html.replace(/<br \/>/g,'\n');
  //trim the string
  html = html.replace(/^\s+|\s+$/, '');
	return html;
}
</script> 
