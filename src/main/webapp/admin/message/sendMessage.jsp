<%--UI REFACTORED--%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.ui.promotion.PromotionAudienceFormBean" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ include file="/include/taglib.jspf"%>

<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-en.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-setup.js"></script>

<script type="text/javascript">
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
      
</script>
<html:form styleId="contentForm" action="sendMessage">
  <html:hidden property="method"/>
  <html:hidden property="messageName"/>
  <html:hidden property="moduleCode"/>
  <html:hidden property="moduleName"/>
  <html:hidden property="subject"/>
  <html:hidden property="audienceListCount"/>
	<beacon:client-state>
	  <beacon:client-state-entry name="messageId" value="${sendMessageForm.messageId}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="TITLE_SEND" code="admin.send.message"/></span>

        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="SEND_INSTRUCTIONAL_COPY" code="admin.send.message"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>

        <cms:errors/>

        <table>
	    	<tr>
	    		<beacon:label property="messageName" required="false">
	              <cms:contentText key="MESSAGE_NAME" code="admin.send.message"/>
	            </beacon:label>	    		
            	<td class="content-field-review">
            		<c:out value="${sendMessageForm.messageName}"/>
    	      	</td>
          	</tr>
          	<tr class="form-blank-row"><td></td></tr>
          	<tr>
          		<beacon:label property="moduleName" required="false">
	              <cms:contentText key="MODULE_NAME" code="admin.send.message"/>
	            </beacon:label>	
            	<td class="content-field-review">
            		<c:out value="${sendMessageForm.moduleName}"/>
    	      	</td>
          	</tr>
          	<tr class="form-blank-row"><td></td></tr>
          	<tr>
          		<beacon:label property="subject" required="false">
	              <cms:contentText key="SUBJECT" code="admin.send.message"/>
	            </beacon:label>
            	<td class="content-field-review">
            		<c:out value="${sendMessageForm.subject}"/>
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
		        		<%-- Message To Program Participants --%>
		        		<tr>
		        			<td>		        				
		        				<table>
		        					<tr>
		        						<td class="content-field">
		        							<html:radio property="messageAudience" value="programParticipants" styleClass="content-field"/>
		        						</td>
		        						<td class="content-field" colspan="3">
		        							<cms:contentText key="MESSAGE_TO_PAX" code="admin.send.message"/>
		        						</td>
		        					</tr>
		        					<tr>
		        						<td></td>
		        						<td class="content-field" width="20"></td>
		        						<td class="content-field">
		        							<html:radio property="messageSubAudience" value="allPaxInProgram" styleClass="content-field"/>
		        						</td>
		        						<td class="content-field">
		        							<cms:contentText key="ALL_PAX_ACTIVE_IN_PROGRAM" code="admin.send.message"/>
		        						</td>
		        					</tr>
		        					<tr>
		        						<td></td>
		        						<td class="content-field" width="20"></td>
		        						<td class="content-field">
		        							<html:radio property="messageSubAudience" value="allPaxInProgramWho" styleClass="content-field"/>
		        						</td>
		        						<td class="content-field" nowrap width="100%">
		        							<cms:contentText key="ALL_PAX_WHO" code="admin.send.message"/>
		        							&nbsp;
		        							<html:select property="programHaveOrHaveNot" styleClass="content-field" >
			          							<html:option value='true'><cms:contentText key="HAVE" code="admin.send.message"/></html:option>
			          							<html:option value='false'><cms:contentText key="HAVE_NOT" code="admin.send.message"/></html:option>
			        						</html:select>
			        						&nbsp;
			        						<html:select styleId="programActivity" property="programActivity" styleClass="content-field" onchange="showOrHideDates(this);">
			          							<html:option value=''><cms:contentText key="SELECT_ONE" code="system.general"/></html:option>
			          							<html:option value='earnedPoints'><cms:contentText key="EARNED_POINTS" code="admin.send.message"/></html:option>
			          							<html:option value='signedUpText'><cms:contentText key="SIGNED_UP_TEXT" code="admin.send.message"/></html:option>
			          						</html:select>
			          						&nbsp;&nbsp;
			          						<DIV id="pointsDateRange">	          						
			          						<cms:contentText key="FROM" code="system.general"/>
			          						&nbsp;
			          						<html:text property="programDateRangeStart" styleId="programDateRangeStart" size="10" maxlength="10" styleClass="content-field" readonly="true" onfocus="clearDateMask(this);"/>
                            				<img id="programDateRangeStartTrigger" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" alt="<cms:contentText key='FROM' code='admin.send.message'/>"/>
			          						&nbsp;
			          						<cms:contentText key="TO" code="system.general"/>
			          						&nbsp;
			          						<html:text property="programDateRangeEnd" styleId="programDateRangeEnd" size="10" maxlength="10" styleClass="content-field" readonly="true" onfocus="clearDateMask(this);"/>
                            				<img id="programDateRangeEndTrigger" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" alt="<cms:contentText key='TO' code='admin.send.message'/>"/>
                            				</DIV>
		        						</td>
		        					</tr>
		        				</table>		        				
		        			</td>
		        		</tr>
		        		<%-- End Message To Program Participants --%>
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
		        							<html:select property="promotionHaveOrHaveNot" styleClass="content-field" >
			          						<html:option value='true'><cms:contentText key="HAVE" code="admin.send.message"/></html:option>
			          						<html:option value='false'><cms:contentText key="HAVE_NOT" code="admin.send.message"/></html:option>
			        						</html:select>
			        						&nbsp;
													<html:select property="promotionActivity" styleClass="content-field" >
			        							<html:option value=''><cms:contentText key="SELECT_ONE" code="system.general"/></html:option>
					          				<c:choose>
															<c:when test="${sendMessageForm.moduleCode == 'prd'}">			
					          						<html:option value='submittedClaim'><cms:contentText key="SUBMITTED_CLAIM" code="admin.send.message"/></html:option>
															</c:when>
															<c:when test="${sendMessageForm.moduleCode == 'quiz'}">			
					          						<html:option value='passedQuiz'><cms:contentText key="PASSED_QUIZ" code="admin.send.message"/></html:option>
					          						<html:option value='takenQuiz'><cms:contentText key="TAKEN_QUIZ" code="admin.send.message"/></html:option>
															</c:when>
															<c:when test="${sendMessageForm.moduleCode == 'rec'}">			
					          						<html:option value='givenRecognition'><cms:contentText key="GIVEN_RECOGNITION" code="admin.send.message"/></html:option>
					          						<html:option value='receivedRecognition'><cms:contentText key="RECEIVED_RECOGNITION" code="admin.send.message"/></html:option>
			  		        						<html:option value='usedAnyBudget'><cms:contentText key="USED_ANY_BUDGET" code="admin.send.message"/></html:option>
			      		    						<html:option value='usedAllBudget'><cms:contentText key="USED_ALL_BUDGET" code="admin.send.message"/></html:option>
															</c:when>
															<c:otherwise>
			  		        						<html:option value='givenRecognition'><cms:contentText key="GIVEN_RECOGNITION" code="admin.send.message"/></html:option>
			      		    						<html:option value='receivedRecognition'><cms:contentText key="RECEIVED_RECOGNITION" code="admin.send.message"/></html:option>
			          								<html:option value='passedQuiz'><cms:contentText key="PASSED_QUIZ" code="admin.send.message"/></html:option>
			          								<html:option value='takenQuiz'><cms:contentText key="TAKEN_QUIZ" code="admin.send.message"/></html:option>
			          								<html:option value='submittedClaim'><cms:contentText key="SUBMITTED_CLAIM" code="admin.send.message"/></html:option>
			          								<html:option value='usedAnyBudget'><cms:contentText key="USED_ANY_BUDGET" code="admin.send.message"/></html:option>
			          								<html:option value='usedAllBudget'><cms:contentText key="USED_ALL_BUDGET" code="admin.send.message"/></html:option>
															</c:otherwise>
														</c:choose>
			          					</html:select>
			          					&nbsp;&nbsp;
			          					<cms:contentText key="FROM" code="system.general"/>
			          					&nbsp;
			          					<html:text property="promotionDateRangeStart" styleId="promotionDateRangeStart" size="10" maxlength="10" styleClass="content-field" readonly="true" onfocus="clearDateMask(this);"/>
                          <img id="promotionDateRangeStartTrigger" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" alt="<cms:contentText key='FROM' code='admin.send.message'/>"/>
			          					&nbsp;
			          					<cms:contentText key="TO" code="system.general"/>
			          					&nbsp;
			          					<html:text property="promotionDateRangeEnd" styleId="promotionDateRangeEnd" size="10" maxlength="10" styleClass="content-field"  readonly="true" onfocus="clearDateMask(this);"/>
                          <img id="promotionDateRangeEndTrigger" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" alt="<cms:contentText key='TO' code='admin.send.message'/>"/>
		        						</td>
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
										                      <html:submit styleClass="content-buttonstyle" onclick="javascript:setActionAndDispatch('sendMessageDisplay.do', 'addAudience')" >
										                        <cms:contentText code="system.button" key="ADD" />
										                      </html:submit>
										                      <br>
										                      <cms:contentText key="CREATE_AUDIENCE_LABEL" code="promotion.audience"/>
										                      <a href="javascript:setActionDispatchAndSubmit('sendMessageDisplay.do', 'prepareAudienceLookup');" class="crud-content-link">
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
																					pageContext.setAttribute("previewUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), "/promotion/promotionAudience.do?method=displayPaxListPopup", parameterMap, true ) );
																			%>
												              <a href="javascript:popUpWin('<c:out value="${previewUrl}"/>', 'console', 750, 500, false, true);" class="crud-content-link"><cms:contentText key="VIEW_LIST" code="promotion.audience"/></a>
												            </td>
												            <td align="center" class="content-field">
												              <nested:checkbox property="removed"/>
												            </td>
												          </tr>
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
										    			<html:submit styleClass="content-buttonstyle" onclick="javascript:setActionAndDispatch('sendMessageDisplay.do','removeAudience');">
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
		        		<%-- Start Exclude Previous Recipients --%>
		        		<tr>
		        			<td>		        				
		        				<table>
		        					<tr>
		        						<td class="content-field">
		        							<html:checkbox property="excludePreviousRecipients" styleClass="content-field"/>
		        						</td>
		        						<td class="content-field">
		        							<cms:contentText key="EXCLUDE_PREVIOUS_RECIPIENTS" code="admin.send.message"/>
		        						</td>
		        					</tr>		        					
		        				</table>		        				
		        			</td>
		        		</tr>
		        		<%-- End Exclude Previous Recipients --%>
		        	</table>
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
   
<script type="text/javascript">
  showOrHideDates();

  Calendar.setup(
    {
      inputField  : "programDateRangeStart",         // ID of the input field
      ifFormat    : "${TinyMceDatePattern}",    // the date format
      button      : "programDateRangeStartTrigger"       // ID of the button
    }
  );
  Calendar.setup(
    {
      inputField  : "programDateRangeEnd",         // ID of the input field
      ifFormat    : "${TinyMceDatePattern}",    // the date format
      button      : "programDateRangeEndTrigger"       // ID of the button
    }
  );
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
   
</html:form>
