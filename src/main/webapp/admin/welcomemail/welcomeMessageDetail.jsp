<%--UI REFACTORED--%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>
<%
StringBuffer htmlEditorIds = new StringBuffer();
StringBuffer stringEditorIds = new StringBuffer();
%>
<html:form styleId="contentForm" action="newWelcomeMessageDisplay">
  <beacon:client-state>
    <beacon:client-state-entry name="messageId" value="${messageForm.messageId}" />
    <beacon:client-state-entry name="dateLastSent" value="${messageForm.dateLastSent}" />
    <beacon:client-state-entry name="cmAssetCode" value="${messageForm.cmAssetCode}" />
    <beacon:client-state-entry name="version" value="${messageForm.version}" />
    <beacon:client-state-entry name="dateCreated" value="${messageForm.dateCreated}" />
    <beacon:client-state-entry name="createdBy" value="${messageForm.createdBy}" />
  </beacon:client-state>
  <html:hidden property="method"/>
  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="MSG_DETAILS" code="admin.message.details"/></span>

        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="INSTRUCTIONAL_COPY" code="admin.message.details"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>

        <cms:errors/>

        <table>
	        <tr>
            <beacon:label property="name" required="true">
              <cms:contentText key="MSG_NAME" code="admin.message.details"/>
            </beacon:label>
            <td class="content-field">
			        <html:text property="name" size="30" maxlength="60" styleClass="content-field"/>
    	      </td>
          </tr>
          <tr class="form-blank-row"><td></td></tr>
	        <tr>
            <beacon:label property="moduleCode" required="true">
              <cms:contentText key="MODULE" code="admin.message.details"/>
            </beacon:label>
		        <td class="content-field">
	    	      <html:select property="moduleCode" styleClass="content-field" >
			          <html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>
			          <html:options collection="moduleList" property="code" labelProperty="name"  />
			        </html:select>
    	      </td>
          </tr>
          <tr class="form-blank-row"><td></td></tr>
	        <tr>
            <beacon:label property="messageTypeCode" required="true">
              <cms:contentText key="MESSAGE_TYPE" code="admin.message.details"/>
            </beacon:label>
		        <td class="content-field">
	    	      <html:select property="messageTypeCode" styleClass="content-field" >
			          <html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>
			          <html:options collection="messageTypeList" property="code" labelProperty="name"  />
			        </html:select>
    	      </td>
          </tr>
          <tr class="form-blank-row"><td></td></tr>
	        <tr>
            <beacon:label property="statusCode" required="true">
              <cms:contentText key="STATUS" code="admin.message.details"/>
            </beacon:label>
		        <td class="content-field">
	    	      <html:select property="statusCode" styleClass="content-field" >
			          <html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>
			          <html:options collection="messageStatusList" property="code" labelProperty="name"  />
			        </html:select>
    	      </td>
          </tr>
          <tr class="form-blank-row"><td></td></tr>
	        <tr>
            <beacon:label property="subject" required="false" styleClass="content-field-label-top">
              <cms:contentText key="SUBJECT" code="admin.message.details"/>
            </beacon:label>
		        <td class="content-field">
	            <textarea style="WIDTH: 100%" id="subject" name="subject" rows="2"><c:out value="${messageForm.subject}"/></textarea>
	            <% 
	            if (stringEditorIds.length() != 0) stringEditorIds.append(',');
                                    stringEditorIds.append("subject");
				%>	            
    	      </td>
          </tr>
          <tr class="form-blank-row"><td></td></tr>
	        <tr>
            <beacon:label property="htmlMsg" required="false" styleClass="content-field-label-top">
              <cms:contentText key="HTML_MSG" code="admin.message.details"/>
            </beacon:label>
		        <td class="content-field">
 	            <textarea style="WIDTH: 100%" id="htmlMsg" name="htmlMsg" rows="10" ><c:out value="${messageForm.htmlMsg}"/></textarea>
  	           <% 
           		if (htmlEditorIds.length() != 0) htmlEditorIds.append(',');
          			htmlEditorIds.append("htmlMsg");
				%> 	            
    	      </td>
          </tr>
          <tr class="form-blank-row"><td></td></tr>
	        <tr>
            <beacon:label property="plainTextMsg" required="false" styleClass="content-field-label-top">
              <cms:contentText key="PLAIN_TEXT_MSG" code="admin.message.details"/>
            </beacon:label>
		        <td class="content-field">
	            <textarea style="WIDTH: 100%" id="plainTextMsg" name="plainTextMsg" rows="10" ><c:out value="${messageForm.plainTextMsg}"/></textarea>
	 	           <% 
	           		if (stringEditorIds.length() != 0) stringEditorIds.append(',');
	          			stringEditorIds.append("plainTextMsg");
				 %>		            
    	      </td>
          </tr>
          <tr class="form-blank-row"><td></td></tr>
	        <tr>
            <beacon:label property="textMsg" required="false" styleClass="content-field-label-top">
              <cms:contentText key="TEXT_MSG" code="admin.message.details"/>
            </beacon:label>
		        <td class="content-field">
    	        <textarea style="WIDTH: 100%" id="textMsg" name="textMsg" rows="10" ><c:out value="${messageForm.textMsg}"/></textarea>
  	           <% 
           		if (stringEditorIds.length() != 0) stringEditorIds.append(',');
          			stringEditorIds.append("textMsg");
				%>     	        
    	      </td>
          </tr>
          <tr class="form-blank-row"><td></td></tr>

          <%--BUTTON ROWS ... For Input--%>
          <tr class="form-buttonrow">
            <td></td>
            <td></td>
            <td align="left">
              <beacon:authorize ifNotGranted="LOGIN_AS">
			        <html:submit styleClass="content-buttonstyle">
			          <cms:contentText code="system.button" key="SAVE" />
			        </html:submit>
              </beacon:authorize>
			        <html:cancel styleClass="content-buttonstyle">
				        <cms:contentText code="system.button" key="CANCEL" />
			        </html:cancel>
            </td>
          </tr>
          <%--END BUTTON ROW--%>

        </table>
        <%-- End Input --%>

      </td>
     </tr>
   </table>

  <script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/tinymce/jscripts/tiny_mce/tiny_mce.js"></script>
	<script type="text/javascript">
    // Creates a new plugin class and a custom listbox 
    tinymce.create('tinymce.plugins.ExamplePlugin', { 
        createControl: function(n, cm) { 
            switch (n) { 
                case 'mylistbox': 
                    var mlb = cm.createListBox('mylistbox', { 
                         title : 'input field', 
                         onselect : function(v) { 
                             tinyMCE.execCommand('mceInsertContent',false,v);	
                         } 
                    }); 
                    <c:forEach items="${insertFieldList}" var="item" varStatus="status" >
                    	mlb.add('<c:out value="${item.name}"/>', '<c:out value="${item.code}" />'); 
                    </c:forEach>
     
                    // Return the new listbox instance 
                    return mlb; 
            } 
     
            return null; 
        } 
    }); 
     
    // Register plugin with a short name 
    tinymce.PluginManager.add('mylistbox', tinymce.plugins.ExamplePlugin);     
    </script>
    
    <script type="text/javascript">
<% if (htmlEditorIds.length() != 0) { %>
      tinyMCE.init({
      		mode : "exact",
      		elements : "<%= htmlEditorIds.toString() %>",
            theme : "advanced",
            forced_root_block : false,
			force_br_newlines : true,
          	force_p_newlines : false,
          	remove_linebreaks : false,
			remove_script_host : false,
			gecko_spellcheck : true ,
			convert_urls : false,
		    paste_auto_cleanup_on_paste : true,
            plugins : "table,advhr,advimage,paste,advlink,insertdatetime,preview,searchreplace,print,contextmenu,spellchecker,-mylistbox",
            theme_advanced_buttons1_add : "fontselect,fontsizeselect",
            theme_advanced_buttons2_add : "separator,insertdate,inserttime,preview,separator,forecolor,backcolor",
            theme_advanced_buttons2_add_before: "cut,copy,paste,separator,search,replace,separator",
            theme_advanced_buttons3_add_before : "tablecontrols,separator",
            theme_advanced_buttons3_add : "spellchecker,advhr,separator,print,separator,mylistbox",
            theme_advanced_toolbar_location : "top",
            theme_advanced_toolbar_align : "left",
            plugin_insertdate_dateFormat : "%Y-%m-%d",
            plugin_insertdate_timeFormat : "%H:%M:%S",
            spellchecker_languages :"+${textEditorDictionaries}",
    	    spellchecker_rpc_url    : "<%=RequestUtils.getBaseURI(request)%>/spellchecker/jazzySpellCheck.do",
            extended_valid_elements : "a[name|href|target|title|onclick],img[class|src|border=0|alt|title|hspace|vspace|width|height|align|onmouseover|onmouseout|name],hr[class|width|size|noshade],font[face|size|color|style],span[class|align|style]"
      });
      <% } if (stringEditorIds.length() != 0) { %>
      tinyMCE.init({
      		mode : "exact",
      		elements : "<%= stringEditorIds.toString() %>",
      		theme : "advanced",
      		forced_root_block : false,
			force_br_newlines : true,
          	force_p_newlines : false,
          	remove_linebreaks : false,
          	gecko_spellcheck : true ,
      		plugins : "insertdatetime,paste,searchreplace,-mylistbox,spellchecker,-mylistbox",
      		force_p_newlines : false,
          	remove_linebreaks : false,
          	convert_newlines_to_brs : true,
    		convert_urls : false,
    	    paste_auto_cleanup_on_paste : true,
    	    paste_text_sticky : true,
    	    paste_text_sticky_default :true,
          	preformatted : true,
      		invalid_elements : "nbsp,p,pre",
          	theme_advanced_buttons1 : "spellchecker,separator,insertdate,inserttime,separator,copy,cut,paste,undo,redo,separator,search,replace,separator,mylistbox",
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
<% } %>
	</script>

</html:form>
