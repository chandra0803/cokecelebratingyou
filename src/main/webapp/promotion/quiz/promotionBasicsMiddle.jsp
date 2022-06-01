<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

<SCRIPT TYPE="text/javascript">
//on page load checks if the allowUnlimitedAttempts is set to true and disables the maximumAttempts field 
$(document).ready(function() {
	
		  if($("input:radio[name='allowUnlimitedAttempts']:checked").val()== "true")
  		   {
			  $("input[name='maximumAttempts']").attr('disabled', 'disabled');
			  $("input[name='maximumAttempts']").val("");
		  }else{
			  $("input[name='maximumAttempts']").attr('disabled', '');
		  }
});

// onclick of the radio allowUnlimitedAttempts button, if the selection is true, the maximumAttempts field is cleared and disabled.
function disableMaximunAttempts(disabled)
{
	if(disabled == 'true')
	{
		$("input[name='maximumAttempts']").attr('disabled', 'disabled');
		$("input[name='maximumAttempts']").val("");
	}else
	{
	  $("input[name='maximumAttempts']").attr('disabled', ''); 
	}
}

</SCRIPT>
<c:if test="displayFlag">
  <html:hidden property="allowUnlimitedAttempts" />  
  <html:hidden property="maximumAttempts" />
</c:if>

<tr class="form-row-spacer">
	<beacon:label required="true" styleClass="content-field-label-top">
		<cms:contentText key="ALLOW_UNLIMITED_ATTEMPTS"
			code="promotion.basics" />
	</beacon:label>
	<td colspan=2 class="content-field">
		<table>
			<tr>
				<td class="content-field"><html:radio
						property="allowUnlimitedAttempts" value="true"
						disabled="${displayFlag}"
						onclick="disableMaximunAttempts('true');" />
				</td>
				<td class="content-field"><cms:contentText
						code="system.common.labels" key="YES" />
				</td>
			</tr>
			<tr>
				<td class="content-field"><html:radio
						property="allowUnlimitedAttempts" value="false"
						disabled="${displayFlag}"
						onclick="disableMaximunAttempts('false');" />
				</td>
				<td class="content-field">
				  <table>
					<tr>
					  <td>
					  	<cms:contentText code="system.common.labels" key="NO" />,&nbsp;
					  </td>
					  <beacon:label property="maximumAttempts" styleClass="content-field">
						<cms:contentText key="NO_OF_ATTEMPTS" code="promotion.basics" />
					  </beacon:label>
					  <td>
					  	&nbsp;<html:text property="maximumAttempts" size="4" maxlength="4" disabled="${displayFlag}" />
					  </td>
					</tr>
				  </table>
				</td>
			</tr>
		</table></td>
</tr>
<tr class="form-row-spacer">
	<beacon:label property="overviewDetailsText" required="true" styleClass="content-field-label-top">
		<cms:contentText key="QUIZ_DETAILS" code="quiz.form" />
	</beacon:label>
	<td class="content-field" colspan="2">
		<div id="webRulesTranslationTextLayer">
			<html:textarea style="WIDTH: 60%" styleId="overview"
				property="overviewDetailsText" rows="10" />
		</div></td>
</tr>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/tinymce/jscripts/tiny_mce/tiny_mce.js"></script>
<script type="text/javascript">
  tinyMCE.init(
  {
		mode : "exact",
		elements : "overview",
		theme : "advanced",
		remove_script_host : false,
		gecko_spellcheck : true ,
		plugins : "table,advhr,advimage,advlink,spellchecker,insertdatetime,preview,searchreplace,print,contextmenu",
		entity_encoding : "raw",
		force_p_newlines : true,
		forced_root_block : false,
		remove_linebreaks : true,
		convert_newlines_to_brs : false,
		preformatted : false,
		convert_urls : false,
		theme_advanced_buttons1_add : "fontselect,fontsizeselect",
		theme_advanced_buttons2_add : "separator,insertdate,inserttime,preview,separator,forecolor,backcolor",
		theme_advanced_buttons2_add_before: "cut,copy,paste,separator,search,replace,separator",
		theme_advanced_buttons3_add_before : "tablecontrols,separator",
		theme_advanced_buttons3_add : "spellchecker,advhr,separator,print",
		theme_advanced_toolbar_location : "top",
		theme_advanced_toolbar_align : "left",
		plugin_insertdate_dateFormat : "%Y-%m-%d",
		plugin_insertdate_timeFormat : "%H:%M:%S",
	    spellchecker_languages : "+${textEditorDictionaries}",
	    spellchecker_rpc_url    : "<%=RequestUtils.getBaseURI(request)%>/spellchecker/jazzySpellCheck.do",
		extended_valid_elements : "a[name|href|target|title|onclick],img[class|src|border=0|alt|title|hspace|vspace|width|height|align|onmouseover|onmouseout|name],hr[class|width|size|noshade],font[face|size|color|style],span[class|align|style]"

  });
 
</script>