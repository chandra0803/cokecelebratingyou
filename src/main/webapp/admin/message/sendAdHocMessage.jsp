<%--UI REFACTORED--%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.ui.promotion.PromotionAudienceFormBean" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ include file="/include/taglib.jspf"%>

<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-en.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-setup.js"></script>
<%
	StringBuffer htmlEditorIds = new StringBuffer();
	StringBuffer stringEditorIds = new StringBuffer();
%>
<script type="text/javascript">
	function setMessageSubAudience()
	{
		getContentForm().messageSubAudience[0].checked = true;
	}

	function showOrHideDates()
	{	
		if(document.getElementById('programActivity') != null)
		{
			if (document.getElementById('programActivity').value == 'earnedPoints' )
			{
				showLayer('pointsDateRange');
			}
			else
			{
				hideLayer('pointsDateRange');
			}
		}
	}

	  function showLayer(whichLayer)
      {
        if (document.getElementById)
        {
          if(document.getElementById(whichLayer) != null){
			// this is the way the standards work
	        var style2 = document.getElementById(whichLayer).style;
			style2.display = "inline";
		  }
        }
        else if (document.all)
        {
          if(document.getElementById(whichLayer) != null){
			// this is the way old msie versions work
            var style2 = document.all[whichLayer].style;
            style2.display = "inline";
		  }
        }
        else if (document.layers)
        {
          if(document.getElementById(whichLayer) != null){
			// this is the way nn4 works
            var style2 = document.layers[whichLayer].style;
            style2.display = "inline";
		  }
        }
      }
      function hideLayer(whichLayer)
      {
        if (document.getElementById)
        {
          if(document.getElementById(whichLayer) != null){
			// this is the way the standards work
            var style2 = document.getElementById(whichLayer).style;
            style2.display = "none";
		  }
        }
        else if (document.all)
        {
          if(document.getElementById(whichLayer) != null){
			// this is the way old msie versions work
            var style2 = document.all[whichLayer].style;
            style2.display = "none";
		  }
        }
        else if (document.layers)
        {
          if(document.getElementById(whichLayer) != null){
			// this is the way nn4 works
            var style2 = document.layers[whichLayer].style;
            style2.display = "none";
		  }
        }
      }   
      
	function hideShowPromotionDates()
	{
		var haveOption = document.sendMessageForm.promotionHaveOrHaveNot.value;
		var activity = document.sendMessageForm.promotionActivity.value;

		if((haveOption == 'false') && (activity == 'usedAllBudget'))
		{
			document.getElementById("promotionDates").style.visibility = 'hidden';
		}
		else
		{
			document.getElementById("promotionDates").style.visibility = 'visible';
		}
	}
	function setDispatchToHome()
	{
	 document.forms[0].action="<%=RequestUtils.getBaseURI(request)%>/homePage.do";
	 document.forms[0].submit();
	}      
</script>
<html:form styleId="contentForm" action="sendAdHocMessage">
  <html:hidden property="method"/>
  <html:hidden property="audienceListCount"/>
  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="TITLE_SEND_ADHOC" code="admin.send.message"/></span>
	<%-- Commenting out to fix in a later release
	    &nbsp;&nbsp;&nbsp;&nbsp;<input type="button" class="webhelp-button" onclick="javascript:FMCOpenHelp( 'G3H2', 'HTML Help Window', null, null );" target="_webhelp" value="<cms:contentText code="system.webhelp" key="WEB_HELP"/>">		
	--%>				
        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="SEND_ADHOC_INSTRUCTIONAL_COPY" code="admin.send.message"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>

        <cms:errors/>

        <table>
	  
          	<tr>
          		<beacon:label property="subject" required="true" styleClass="content-field-label-top">
	              <cms:contentText key="SUBJECT" code="admin.send.message"/>
	            </beacon:label>
            	<td class="content-field">
            		<textarea style="WIDTH: 80%" id="subject" name="subject" rows="2"><c:out value="${sendMessageForm.subject}"/></textarea>
	            <% 
		            if (stringEditorIds.length() != 0) stringEditorIds.append(',');
	                                    stringEditorIds.append("subject");
				%>            		
    	      	</td>
          	</tr>
          	<tr class="form-blank-row"><td></td></tr>	
	        <tr>
	            <beacon:label property="sender" required="true">
	              <cms:contentText key="SENDER" code="admin.send.message"/>
	            </beacon:label>
		        <td class="content-field">
		        	<html:text property="sender" size="50" maxlength="75" styleClass="content-field"/>
	    	    </td>
          	</tr>
          	<tr class="form-blank-row"><td></td></tr>	
	        <tr>
	            <beacon:label property="messageAudience" required="true" styleClass="content-field-label-top">
	              <cms:contentText key="SEND" code="admin.send.message"/>
	            </beacon:label>
		        <td width="90%">
		        	<table>
		        		<%-- Preview To Row --%>
		        		<tr>
		        			<td>		        				
		        				<table>
		        					<tr>
		        						<td class="content-field">
		        							<html:radio property="messageAudience" value="preview" styleClass="content-field"/>
		        						</td>
		        						<td class="content-field">
		        							<cms:contentText key="PREVIEW_TO" code="admin.send.message"/>
		        						</td>
		        						<td class="content-field">
		        							<html:text property="previewEmailAddress1" size="50" maxlength="75" styleClass="content-field"/>
		        						</td>
		        					</tr>
		        					<tr>
		        						<td class="content-field" colspan="2"></td>
		        						<td class="content-field">
		        							<html:text property="previewEmailAddress2" size="50" maxlength="75" styleClass="content-field"/>
		        						</td>
		        					</tr>
		        					<tr>
		        						<td class="content-field" colspan="2"></td>
		        						<td class="content-field">
		        							<html:text property="previewEmailAddress3" size="50" maxlength="75" styleClass="content-field"/>
		        						</td>
		        					</tr>
		        				</table>		        				
		        			</td>
		        		</tr>
		        		<%-- End Preview To Row --%>
		        		
		        		<%-- Message To All Participants --%>
    						<div id="programPaxDiv">
    		        		<html:radio property="messageSubAudience" value="allPaxInProgram"/>
    						</div>	
		        		<tr>
		        		  <td>
		        		    <table>			
      								<tr>
             						<td class="content-field">
								      <html:radio property="messageAudience" value="programParticipants" styleClass="content-field" onclick="setMessageSubAudience();"/>
       							      <cms:contentText key="MESSAGE_TO_ALL_ACTIVE_PARTICIPANTS" code="admin.send.message"/>
             						</td>
             					</tr>
                    </table>
                  </td>
                </tr>
		        		
		        		<%-- End Message To All Participants --%>
		        		
		        		<%-- Message To Promotion Audience --%>
		        		<tr>
		        			<td>		        				
		        				<table>
		        					<tr>
		        						<td class="content-field">
		        							<html:radio property="messageAudience" value="promotionAudience" styleClass="content-field"/>
		        						</td>
		        						<td class="content-field" colspan="3">
		        							<cms:contentText key="MESSAGE_TO_PROMO_AUDIENCE" code="admin.send.message"/>
		        							&nbsp;
		        							<html:select property="selectedPromotionId" styleClass="content-field" >
			          							<html:option value=''><cms:contentText key="SELECT" code="system.general"/></html:option>
			          							<html:options collection="promotionList" property="id" labelProperty="name"  />
			          						</html:select>
		        						</td>
		        					</tr>
		        					<tr>
		        						<td></td>
		        						<td class="content-field" width="20"></td>
		        						<td class="content-field">
		        							<html:radio property="messageSubAudience" value="allPaxInPromotion" styleClass="content-field"/>
		        						</td>
		        						<td class="content-field">
		        							<cms:contentText key="ALL_PAX_ACTIVE_IN_PROMO" code="admin.send.message"/>		        							
		        						</td>
		        					</tr>
		        					<tr>
		        						<td></td>
		        						<td class="content-field" width="20"></td>
		        						<td class="content-field">
		        							<html:radio property="messageSubAudience" value="allPaxInPromotionWho" styleClass="content-field"/>
		        						</td>
		        						<td class="content-field" nowrap width="100%">
		        							<cms:contentText key="ALL_PAX_WHO" code="admin.send.message"/>
		        							&nbsp;
		        							<html:select property="promotionHaveOrHaveNot" styleClass="content-field" onchange="javascript:hideShowPromotionDates();" >
			          							<html:option value='true'><cms:contentText key="HAVE" code="admin.send.message"/></html:option>
			          							<html:option value='false'><cms:contentText key="HAVE_NOT" code="admin.send.message"/></html:option>
			        						</html:select>
			        						&nbsp;
			        						<html:select property="promotionActivity" styleClass="content-field" onchange="javascript:hideShowPromotionDates();" >
			          						<html:option value=''><cms:contentText key="SELECT_ONE" code="system.general"/></html:option>
			          						<html:option value='givenRecognition'><cms:contentText key="GIVEN_RECOGNITION" code="admin.send.message"/></html:option>
			          						<html:option value='receivedRecognition'><cms:contentText key="RECEIVED_RECOGNITION" code="admin.send.message"/></html:option>
			          						<html:option value='passedQuiz'><cms:contentText key="PASSED_QUIZ" code="admin.send.message"/></html:option>
			          						<html:option value='takenQuiz'><cms:contentText key="TAKEN_QUIZ" code="admin.send.message"/></html:option>
			          						<html:option value='submittedClaim'><cms:contentText key="SUBMITTED_CLAIM" code="admin.send.message"/></html:option>
			          						<html:option value='usedAnyBudget'><cms:contentText key="USED_ANY_BUDGET" code="admin.send.message"/></html:option>
			          						<html:option value='usedAllBudget'><cms:contentText key="USED_ALL_BUDGET" code="admin.send.message"/></html:option>
			          					</html:select>
			          					&nbsp;&nbsp;
			          					<span id="promotionDates"><cms:contentText key="FROM" code="system.general"/>
			          					&nbsp;
			          					<html:text property="promotionDateRangeStart" styleId="promotionDateRangeStart" size="10" maxlength="10" styleClass="content-field" readonly="true" onfocus="clearDateMask(this);"/>
                          <img id="promotionDateRangeStartTrigger" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" alt="<cms:contentText key='FROM' code='admin.send.message'/>"/>
			          					&nbsp;
			          					<cms:contentText key="TO" code="system.general"/>
			          					&nbsp;
			          					<html:text property="promotionDateRangeEnd" styleId="promotionDateRangeEnd" size="10" maxlength="10" styleClass="content-field" readonly="true" onfocus="clearDateMask(this);"/>
                          <img id="promotionDateRangeEndTrigger" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" alt="<cms:contentText key='TO' code='admin.send.message'/>"/>
			          					</span></td>
		        					</tr>
		        				</table>		        				
		        			</td>
		        		</tr>
		        		<%-- End Message To Promotion Audience --%>
		        		<%-- Start Message To Specific Audience --%>
		        		<tr>
		        			<td>		        				
		        				<table>
		        					<tr>
		        						<td class="content-field">
		        							<html:radio property="messageAudience" value="specificAudience" styleClass="content-field"/>
		        						</td>
		        						<td class="content-field" colspan="2">
		        							<cms:contentText key="MESSAGE_TO_SPECIFIC_AUDIENCE" code="admin.send.message"/>
		        						</td>
		        					</tr>	
		        					<tr>
		        						<td></td>
		        						<td class="content-field" width="20"></td>
		        						<td>
		        							<table>
		        								<tr>
		        									<td>
					        							<table class="crud-table" width="100%">
										                  <tr>
										      	            <th colspan="3" class="crud-table-header-row">
										      	              <cms:contentText key="AUDIENCE_LIST_LABEL" code="promotion.audience"/>
										      	              &nbsp;&nbsp;&nbsp;&nbsp;
										                      <html:select property="selectedAudienceId" styleClass="content-field">
										                      	<html:option value=''><cms:contentText key="SELECT_SAVED_AUDIENCE" code="admin.send.message"/></html:option>										                        
										                        <html:options collection="availableAudiences" property="id" labelProperty="name" />
										                      </html:select>
										                      <html:submit styleClass="content-buttonstyle" onclick="javascript:setActionAndDispatch('sendAdHocMessageDisplay.do', 'addAudience')" >
										                        <cms:contentText code="system.button" key="ADD" />
										                      </html:submit>
										                      <br>
										                      <cms:contentText key="CREATE_AUDIENCE_LABEL" code="promotion.audience"/>
										                      <a href="javascript:setActionDispatchAndSubmit('sendAdHocMessageDisplay.do', 'prepareAudienceLookup');" class="crud-content-link">
										                        <cms:contentText key="LIST_BUILDER_LABEL" code="promotion.audience"/>
										                      </a>
										                    </th>
										 		            <th valign="top" class="crud-table-header-row"><cms:contentText code="promotion.audience" key="CRUD_REMOVE_LABEL"/></th>
										 		          </tr>
										 		          <c:set var="rowCount" value="0"/>  
												 		  <c:set var="switchColor" value="false"/>  
												          <nested:iterate id="audience" name="sendMessageForm" property="audienceAsList">   
												            <nested:hidden property="id"/>
												            <nested:hidden property="audienceId"/>
												            <nested:hidden property="name"/>
												            <nested:hidden property="size"/>
												            <nested:hidden property="audienceType"/>
												            <c:choose>
													          <c:when test="${switchColor == 'false'}">
														        <tr class="crud-table-row1">
														  		<c:set var="switchColor" scope="page" value="true"/>
													     	  </c:when>
													     	  <c:otherwise>
														  		<tr class="crud-table-row2">
														  		<c:set var="switchColor" scope="page" value="false"/>
													     	  </c:otherwise>
													    	</c:choose>
													    	<%-- in general we use content-field-review for no editables, but for this screen class is content-field--%>
													    	<%-- To fix 19551 add doNotSaveToken parameter --%>
												            <td class="content-field">
												              <c:out value="${audience.name}"/>
												            </td>
												            <td class="content-field">
												              < <c:out value="${audience.size}"/> >
												            </td>
												            <td class="content-field">
																			<%  Map parameterMap = new HashMap();
																					PromotionAudienceFormBean temp = (PromotionAudienceFormBean)pageContext.getAttribute("audience");
																					parameterMap.put( "audienceId", temp.getAudienceId() );
																					pageContext.setAttribute("previewUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), "/promotion/promotionAudience.do?doNotSaveToken=true&method=displayPaxListPopup", parameterMap, true ) );
																			%>
												              <a href="javascript:popUpWin('<c:out value="${previewUrl}"/>', 'console', 750, 500, false, true);" class="crud-content-link"><cms:contentText key="VIEW_LIST" code="promotion.audience"/></a>
												            </td>
												            <td align="center" class="content-field">
												              <nested:checkbox property="removed"/>
												            </td>
												          <c:set var="rowCount" value="${rowCount+1}"/>  
												          </nested:iterate>
												          <c:if test="${rowCount==0}">
												          	<tr class="crud-table-row1">
												          		<td class="content-field">
												              		<cms:contentText key="NONE_DEFINED" code="system.general"/>
												            	</td>
												          	</tr>
												          </c:if>
										                </table>	
										        	</td>
										    	</tr>
										    	<c:if test="${rowCount>0}">
										    	<tr>
										    		<td align="right">
										    			<html:submit styleClass="content-buttonstyle" onclick="javascript:setActionAndDispatch('sendAdHocMessageDisplay.do','removeAudience');">
										                  <cms:contentText key="REMOVE" code="system.button"/>
										                </html:submit>
										            </td>
										    	</tr>
										    	</c:if>
											</table>						                
		        						</td>
		        					</tr>	        					
		        				</table>		        				
		        			</td>
		        		</tr>
		        		<%-- End Message To Specific Audience --%>
		        	</table>
	    	    </td>
          	</tr>
          	
          <tr class="form-blank-row"><td></td></tr>
	      <tr>
            <beacon:label property="htmlMsg" required="true" styleClass="content-field-label-top">
              <cms:contentText key="HTML_MESSAGE" code="admin.send.message"/>
            </beacon:label>
		        <td class="content-field">
 	            <textarea style="WIDTH: 84%" id="htmlMsg" name="htmlMsg" rows="10" convert_beacon="true"><c:out value="${sendMessageForm.htmlMsg}"/></textarea>
 	           <% 
 	           		if (htmlEditorIds.length() != 0) htmlEditorIds.append(',');
 	          			htmlEditorIds.append("htmlMsg");
				%> 	            
    	      </td>
          </tr>
          
          <tr class="form-blank-row"><td></td></tr>
	        <tr>
            <beacon:label property="plainTextMsg" required="true" styleClass="content-field-label-top">
              <cms:contentText key="PLAIN_TEXT_MESSAGE" code="admin.send.message"/>
            </beacon:label>
		        <td class="content-field">
	            <textarea style="WIDTH: 84%" id="plainTextMsg" name="plainTextMsg" rows="10" ><c:out value="${sendMessageForm.plainTextMsg}"/></textarea>
 	           <% 
 	           		if (stringEditorIds.length() != 0) stringEditorIds.append(',');
 	          				stringEditorIds.append("plainTextMsg");
				%>		            
    	      </td>
          </tr>
          <tr class="form-blank-row"><td></td></tr>
	        <tr>
            <beacon:label property="textMsg" required="false" styleClass="content-field-label-top">
              <cms:contentText key="TEXT_MESSAGE" code="admin.send.message"/>
            </beacon:label>
		        <td class="content-field">
    	        <textarea style="WIDTH: 84%" id="textMsg" name="textMsg" rows="10" ><c:out value="${sendMessageForm.textMsg}"/></textarea>
 	           <% 
 	           		if (stringEditorIds.length() != 0) stringEditorIds.append(',');
 	          				stringEditorIds.append("textMsg");
				%>       	        
    	      </td>
          </tr>
          
			<tr class="form-blank-row"><td></td></tr>	
	        <tr>
	            <beacon:label property="deliveryMethod" required="true" styleClass="content-field-label-top">
	              <cms:contentText key="DELIVERY" code="admin.send.message"/>
	            </beacon:label>
	            <td>
	            	<table>
	            		<tr>
	            			<td class="content-field">
		        				<html:radio property="deliveryMethod" value="immediate" styleClass="content-field"/>
		        			</td>
		        			<td class="content-field">
       							<cms:contentText key="IMMEDIATE" code="admin.send.message"/>
       						</td>
       					</tr>
       					<tr>
	            			<td class="content-field">
		        				<html:radio property="deliveryMethod" value="scheduled" styleClass="content-field"/>
		        			</td>
		        			<td class="content-field">
       							<cms:contentText key="SCHEDULED" code="admin.send.message"/>
       							&nbsp;
       							<html:text property="deliveryDate" styleId="deliveryDate" size="20" maxlength="19" styleClass="content-field" readonly="true" onfocus="clearDateMask(this);"/>
                            	<img id="deliveryDateTrigger" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" alt="<cms:contentText key='SCHEDULED' code='admin.send.message'/>"/>
       						</td>
       					</tr>
       				</table>
	    	    </td>
          	</tr>
          	<%--BUTTON ROWS ... For Input--%>
          	<tr class="form-buttonrow">
            	<td></td>
            	<td></td>
            	<td align="left">
                <beacon:authorize ifNotGranted="LOGIN_AS">
			        <html:submit styleClass="content-buttonstyle">
			          <cms:contentText code="admin.send.message" key="SEND_BUTTON" />
			        </html:submit>
                </beacon:authorize>
					<html:button  property=""  styleClass="content-buttonstyle" onclick="setDispatchToHome()">
						<cms:contentText code="system.button" key="CANCEL" />
					</html:button>  			        
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
  hideShowPromotionDates();
  showOrHideDates();
	hideLayer('programPaxDiv');

  Calendar.setup(
    {
      inputField  : "promotionDateRangeStart",         // ID of the input field
      ifFormat    : "${TinyMceDatePattern}",    // the date format
      button      : "promotionDateRangeStartTrigger"       // ID of the button
    }
  );
  Calendar.setup(
    {
      inputField  : "promotionDateRangeEnd",         // ID of the input field
      ifFormat    : "${TinyMceDatePattern}",    // the date format
      button      : "promotionDateRangeEndTrigger"       // ID of the button
    }
  );  
  Calendar.setup(
    {
      inputField  : "deliveryDate",         // ID of the input field
      ifFormat    : "${TinyMceDateTimePattern}",    // the date format
      showsTime   : "true",
      timeFormat  : "24",
      button      : "deliveryDateTrigger"       // ID of the button
    }
  );
</script>
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
		plugins : "table,advhr,advimage,advlink,spellchecker,paste,insertdatetime,preview,searchreplace,print,contextmenu,-mylistbox",
		force_p_newlines : true,
		paste_auto_cleanup_on_paste : true,
		remove_linebreaks : true,
		forced_root_block : false,
		convert_newlines_to_brs : false,
		convert_urls : false,
		preformatted : false,
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
		plugins : "spellchecker,paste,insertdatetime,searchreplace,autoresize,-mylistbox",
		entity_encoding : "named",
	    force_p_newlines : false,
	    forced_root_block : false,
	    paste_auto_cleanup_on_paste : true,
	    paste_text_sticky : true,
	    paste_text_sticky_default :true, 
	    gecko_spellcheck : true ,
	    remove_linebreaks : false,
	    convert_newlines_to_brs : true,
		convert_urls : false,
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
</script>

</html:form>