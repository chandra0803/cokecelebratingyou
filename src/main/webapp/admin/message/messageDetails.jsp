<%--UI REFACTORED--%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.ui.message.MessageForm"%>
<%@ include file="/include/taglib.jspf"%>
<script type="text/javascript">
	
  var jsTranslatedSubjects = new Array(); 
  <c:forEach var="translatedSubjects" items='${messageForm.translatedSubjects}'>
  	<c:set var="contentVal" value='${translatedSubjects.value}'/>
  	jsTranslatedSubjects["<c:out value='${translatedSubjects.key}'/>"] = "<%=MessageForm.getJsString( (String)pageContext.getAttribute("contentVal" ) )%>";
  </c:forEach>

  var jsTranslatedHtmlMsgs = new Array(); 
  <c:forEach var="translatedHtmlMsgs" items='${messageForm.translatedHtmlMsgs}'>
  	<c:set var="contentVal" value='${translatedHtmlMsgs.value}'/>
  	jsTranslatedHtmlMsgs["<c:out value='${translatedHtmlMsgs.key}'/>"] = "<%=MessageForm.getJsString( (String)pageContext.getAttribute("contentVal" ) )%>";
  </c:forEach>

  var jsTranslatedPlainTextMsgs = new Array(); 
  <c:forEach var="translatedPlainTextMsgs" items='${messageForm.translatedPlainTextMsgs}'>
    <c:set var="contentVal" value='${translatedPlainTextMsgs.value}'/>
  	jsTranslatedPlainTextMsgs["<c:out value='${translatedPlainTextMsgs.key}'/>"] = "<%=MessageForm.getJsString( (String)pageContext.getAttribute("contentVal" ) )%>";
  </c:forEach>

  var jsTranslatedTextMsgs = new Array(); 
  <c:forEach var="translatedTextMsgs" items='${messageForm.translatedTextMsgs}'>
  	<c:set var="contentVal" value='${translatedTextMsgs.value}'/>
  	jsTranslatedTextMsgs["<c:out value='${translatedTextMsgs.key}'/>"] = "<%=MessageForm.getJsString( (String)pageContext.getAttribute("contentVal" ) )%>";
  </c:forEach>    

  <c:if test="${DISPLAY_STRONGMAIL_CONTENT}">
	  var jsTranslatedStrongMailSubjects = new Array(); 
	  <c:forEach var="translatedStrongMailSubjects" items='${messageForm.translatedStrongMailSubjects}'>
	  	<c:set var="contentVal" value='${translatedStrongMailSubjects.value}'/>
	  	jsTranslatedStrongMailSubjects["<c:out value='${translatedStrongMailSubjects.key}'/>"] = "<%=MessageForm.getJsString( (String)pageContext.getAttribute("contentVal" ) )%>";
	  </c:forEach>
		
	  var jsTranslatedStrongMailMsgs = new Array(); 
	  <c:forEach var="translatedStrongMailMsgs" items='${messageForm.translatedStrongMailMsgs}'>
	    <c:set var="contentVal" value='${translatedStrongMailMsgs.value}'/>
	    jsTranslatedStrongMailMsgs["<c:out value='${translatedStrongMailMsgs.key}'/>"] = "<%=MessageForm.getJsString( (String)pageContext.getAttribute("contentVal" ) )%>";
	  </c:forEach>
  </c:if>
  
  function chooseTranslate()
  {
  	var obj = document.getElementById('translatedLocale');
  	if(obj.value == '<cms:contentText key="SELECT_ONE" code="system.general" />')
  	{
          hideLayer("subjectElementVal");  	
          hideLayer("htmlMsgElementVal");  	
          hideLayer("plainTextMsgElementVal"); 
          hideLayer("textMsgElementVal");
          <c:if test="${DISPLAY_STRONGMAIL_CONTENT}">
	          hideLayer("strongMailSubjectElementVal");
	          hideLayer("strongMailMsgElementVal"); 
          </c:if>
  	}
  	else
  	{
	  showLayer("subjectElementVal");
	  showLayer("htmlMsgElementVal");  	
	  showLayer("plainTextMsgElementVal");
	  showLayer("textMsgElementVal");  	
	  <c:if test="${DISPLAY_STRONGMAIL_CONTENT}">
		  showLayer("strongMailSubjectElementVal");
		  showLayer("strongMailMsgElementVal"); 
	  </c:if>
	  tinyMCE.get('translatedSubject').setContent(eval('jsTranslatedSubjects.' + obj.value ));
	  tinyMCE.get('translatedHtmlMsg').setContent(eval('jsTranslatedHtmlMsgs.' + obj.value ));
	  tinyMCE.get('translatedPlainTextMsg').setContent(eval('jsTranslatedPlainTextMsgs.' + obj.value ));
	  tinyMCE.get('translatedTextMsg').setContent(eval('jsTranslatedTextMsgs.' + obj.value ));
	  <c:if test="${DISPLAY_STRONGMAIL_CONTENT}">	  
		  tinyMCE.get('translatedStrongMailSubject').setContent(eval('jsTranslatedStrongMailSubjects.' + obj.value ));
		  tinyMCE.get('translatedStrongMailMsg').setContent(eval('jsTranslatedStrongMailMsgs.' + obj.value ));
	  </c:if>
  	}
  }   

  function showLayer(whichLayer)
  {
	if (document.getElementById)
	{	
	  // this is the way the standards work
	  var style2 = document.getElementById(whichLayer).style;
	  style2.display = "block";
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

  function hideLayer(whichLayer)
  {
	if (document.getElementById)
	{
	  // this is the way the standards work
	  var style2 = document.getElementById(whichLayer).style;
	  style2.display = "none";
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
  
</script>  

<%
	StringBuffer htmlEditorIds = new StringBuffer();
	StringBuffer stringEditorIds = new StringBuffer();
%>
<html:form styleId="contentForm" action="maintainMessage">
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

        <font color="red">
        <c:out value="${editErrorMessageDisplay}" />
        </font><br><br>
        
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
            <beacon:label property="statusCode">
              <cms:contentText key="SMSGROUP" code="admin.message.details"/>
            </beacon:label>
		        <td class="content-field">
	    	      <html:select property="messageSMSGroup" styleClass="content-field" >
			          <html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>
			          <html:options collection="messageSMSGroupTypeList" property="code" labelProperty="name"  />
			        </html:select>
    	      </td>
          </tr>           
          <tr class="form-blank-row"><td></td></tr>
	        <tr>
            <beacon:label property="subject" required="false" styleClass="content-field-label-top">
              <cms:contentText key="SUBJECT" code="admin.message.details"/>
            </beacon:label>
		        <td class="content-field">
	            <textarea style="WIDTH: 100%" id="subject" name="subject" rows="2" ><c:out value="${messageForm.subject}"/></textarea>
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
          <c:if test="${DISPLAY_STRONGMAIL_CONTENT}">
          <tr class="form-blank-row"><td></td></tr>
	      <tr>
            <beacon:label property="strongMailSubject" required="false" styleClass="content-field-label-top">
              <cms:contentText key="STRONGMAIL_SUBJECT" code="admin.message.details"/><br/>
              <cms:contentText key="STRONGMAIL_FORMATTING" code="admin.message.details"/>
            </beacon:label>
		        <td class="content-field">
	            <textarea style="WIDTH: 100%" id="strongMailSubject" name="strongMailSubject" rows="2" ><c:out value="${messageForm.strongMailSubject}"/></textarea>
	            <% 
		            if (stringEditorIds.length() != 0) stringEditorIds.append(',');
	                                    stringEditorIds.append("strongMailSubject");
				%>
    	      </td>
          </tr>
          <tr class="form-blank-row"><td></td></tr>
	      <tr>
            <beacon:label property="strongMailMsg" required="false" styleClass="content-field-label-top">
              <cms:contentText key="STRONGMAIL_MSG" code="admin.message.details"/><br/>
              <cms:contentText key="STRONGMAIL_FORMATTING" code="admin.message.details"/>
            </beacon:label>
		        <td class="content-field">
    	        <textarea style="WIDTH: 100%" id="strongMailMsg" name="strongMailMsg" rows="10" ><c:out value="${messageForm.strongMailMsg}"/></textarea>
 	           <% 
 	           		if (htmlEditorIds.length() != 0) htmlEditorIds.append(',');
 	          			htmlEditorIds.append("strongMailMsg");
				%>    	        
    	     </td>
        </tr>
        </c:if>
          
        <tr class="form-blank-row">
          <td></td>
        </tr>      
        <tr class="form-row-spacer">
          <beacon:label property="translatedWebRulesText" required="false" styleClass="content-field-label-top">
            <cms:contentText key="CONTENT_TRANSLATION" code="promotion.webrules" />
          </beacon:label>          
        	<td class="content-field">
        	    <div id="webRulesTranslationLocaleLayer">
        	      <select name="translatedLocale" id="translatedLocale" onchange="chooseTranslate();">
        	       <option value='<cms:contentText key="SELECT_ONE" code="system.general" />'><cms:contentText key="SELECT_ONE" code="system.general" /></option>
    		       <c:forEach var="localeItem" items="${localeItems}">
    		            <c:set var="code" value='${localeItem.contentDataMap["CODE"]}'/>
    		            <c:set var="desc" value='${localeItem.contentDataMap["DESC"]}'/>
    		            <option value="<c:out value='${code}'/>"
    		            	<c:if test="${messageForm.translatedLocale == code}">
                                selected
                            </c:if>
				><c:out value='${desc}'/></option>
    		       </c:forEach>  
    		      </select>
    		    </div>     
        	</td>
        </tr>          
          
          <tr class="form-blank-row"><td></td></tr>
	        <tr>
            <beacon:label property="translatedSubject" required="false" styleClass="content-field-label-top">
              <cms:contentText key="SUBJECT" code="admin.message.details"/>
            </beacon:label>
		        <td class="content-field">
		        <div id="subjectElementVal">
	            <textarea style="WIDTH: 100%" id="translatedSubject" name="translatedSubject" rows="2" ><c:out value="${messageForm.translatedSubject}"/></textarea>
	            <% 
		            if (stringEditorIds.length() != 0) stringEditorIds.append(',');
	                                    stringEditorIds.append("translatedSubject");
				%>
				</div>
    	      </td>
          </tr>
          <tr class="form-blank-row"><td></td></tr>
	        <tr>
            <beacon:label property="translatedHtmlMsg" required="false" styleClass="content-field-label-top">
              <cms:contentText key="HTML_MSG" code="admin.message.details"/>
            </beacon:label>
		        <td class="content-field">
		        <div id="htmlMsgElementVal">
 	            <textarea style="WIDTH: 100%" id="translatedHtmlMsg" name="translatedHtmlMsg" rows="10" ><c:out value="${messageForm.translatedHtmlMsg}"/></textarea>
 	           <% 
 	           		if (htmlEditorIds.length() != 0) htmlEditorIds.append(',');
 	          			htmlEditorIds.append("translatedHtmlMsg");
				%>
				</div>
    	      </td>
          </tr>
          <tr class="form-blank-row"><td></td></tr>
	        <tr>
            <beacon:label property="translatedPlainTextMsg" required="false" styleClass="content-field-label-top">
              <cms:contentText key="PLAIN_TEXT_MSG" code="admin.message.details"/>
            </beacon:label>
		        <td class="content-field">
		        <div id="plainTextMsgElementVal">
	            <textarea style="WIDTH: 100%" id="translatedPlainTextMsg" name="translatedPlainTextMsg" rows="10" ><c:out value="${messageForm.translatedPlainTextMsg}"/></textarea>
 	           <% 
 	           		if (stringEditorIds.length() != 0) stringEditorIds.append(',');
 	          			stringEditorIds.append("translatedPlainTextMsg");
				%>	            
				</div>
    	      </td>
          </tr>
          <tr class="form-blank-row"><td></td></tr>
	        <tr>
            <beacon:label property="translatedTextMsg" required="false" styleClass="content-field-label-top">
              <cms:contentText key="TEXT_MSG" code="admin.message.details"/>
            </beacon:label>
		        <td class="content-field">
		        <div id="textMsgElementVal">
    	        <textarea style="WIDTH: 100%" id="translatedTextMsg" name="translatedTextMsg" rows="10" ><c:out value="${messageForm.translatedTextMsg}"/></textarea>
 	           <% 
 	           		if (stringEditorIds.length() != 0) stringEditorIds.append(',');
 	          			stringEditorIds.append("translatedTextMsg");
				%>    	        
				</div>
    	      </td>
          </tr>
          <c:if test="${DISPLAY_STRONGMAIL_CONTENT}">        
		  <tr class="form-blank-row"><td></td></tr>
	        <tr>
            <beacon:label property="translatedStrongMailSubject" required="false" styleClass="content-field-label-top">
              <cms:contentText key="STRONGMAIL_SUBJECT" code="admin.message.details"/><br/>
              <cms:contentText key="STRONGMAIL_FORMATTING" code="admin.message.details"/>
            </beacon:label>
		        <td class="content-field">
		        <div id="strongMailSubjectElementVal">
	            <textarea style="WIDTH: 100%" id="translatedStrongMailSubject" name="translatedStrongMailSubject" rows="2" ><c:out value="${messageForm.translatedStrongMailSubject}"/></textarea>
	            <% 
		            if (stringEditorIds.length() != 0) stringEditorIds.append(',');
	                                    stringEditorIds.append("translatedStrongMailSubject");
				%>
				</div>
    	      </td>
          </tr>          
          <tr class="form-blank-row"><td></td></tr>
	      <tr>
            <beacon:label property="translatedStrongMailMsg" required="false" styleClass="content-field-label-top">
              <cms:contentText key="STRONGMAIL_MSG" code="admin.message.details"/><br/>
              <cms:contentText key="STRONGMAIL_FORMATTING" code="admin.message.details"/>
            </beacon:label>
		        <td class="content-field">
		        <div id="strongMailMsgElementVal">
    	        <textarea style="WIDTH: 100%" id="translatedStrongMailMsg" name="translatedStrongMailMsg" rows="10" ><c:out value="${messageForm.translatedStrongMailMsg}"/></textarea>
 	           <% 
 	           		if (htmlEditorIds.length() != 0) htmlEditorIds.append(',');
 	          			htmlEditorIds.append("translatedStrongMailMsg");
				%>    	        
				</div>
    	      </td>
          </tr>  
          </c:if>
          <tr class="form-blank-row"><td></td></tr>

          <%--BUTTON ROWS ... For Input--%>
          <tr class="form-buttonrow">
            <td></td>
            <td></td>
            <td align="left">
              <c:choose>
              <c:when test="${disable eq 'true'}">
			        <html:submit styleClass="content-buttonstyle" style="display:none">
			          <cms:contentText code="system.button" key="SAVE" />
			        </html:submit>
			  </c:when>
			  <c:otherwise>
			  <html:submit styleClass="content-buttonstyle" disabled="false">
			          <cms:contentText code="system.button" key="SAVE" />
			  </html:submit>
			  </c:otherwise>      
			  </c:choose>
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
		remove_script_host : false,
		gecko_spellcheck : true ,
		plugins : "table,advhr,advimage,paste,advlink,spellchecker,insertdatetime,preview,searchreplace,print,contextmenu,-mylistbox",
		force_p_newlines : false,
        forced_root_block : false,
        force_br_newlines : true,
		convert_newlines_to_brs : false,
		preformatted : false,
		convert_urls : false,
		paste_auto_cleanup_on_paste : true,
		theme_advanced_buttons1_add : "fontselect,fontsizeselect",
		theme_advanced_buttons2_add : "separator,insertdate,inserttime,preview,separator,forecolor,backcolor",
		theme_advanced_buttons2_add_before: "cut,copy,paste,separator,search,replace,separator",
		theme_advanced_buttons3_add_before : "tablecontrols,separator",
		theme_advanced_buttons3_add : "spellchecker,advhr,separator,print,mylistbox",
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
		plugins : "spellchecker,paste,insertdatetime,searchreplace,-mylistbox",
		entity_encoding : "named",
        force_p_newlines : false,
        forced_root_block : false,
        remove_linebreaks : false,
        gecko_spellcheck : true ,
        convert_newlines_to_brs : true,
		convert_urls : false,
	    paste_auto_cleanup_on_paste : true,
	    paste_text_sticky : true,
	    paste_text_sticky_default :true,
        preformatted : true,
        invalid_elements : "nbsp,p,pre",
        theme_advanced_buttons1 : "spellchecker,separator,insertdate,inserttime,separator,copy,cut,paste,undo,redo,separator,search,replace,mylistbox",
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
    html = html.replace(/%7B/g,'{');
    html = html.replace(/%7D/g,'}');
    //trim the string
    html = html.replace(/^\s+|\s+$/, '');
    return html;
  }
		<% } %>
		
	chooseTranslate();		
		
	</script>

</html:form>
