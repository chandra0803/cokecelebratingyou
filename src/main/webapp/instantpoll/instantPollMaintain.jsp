<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.value.instantpoll.InstantPollAudienceFormBean"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-en.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-setup.js"></script>

<script type="text/javascript">
  function setSpecifyAudienceVisibility()
  {
	  if ( $("input:radio[name='audienceType']:checked").val()=="specifyaudience" )	
	  {
		document.getElementById('specifyAudience').style.display = "table-row";
		document.getElementById('specifyAudienceRemove').style.display = "table-row";
		$("#specifyAudience").show();
		$("#specifyAudienceRemove").show();
	  }
	  else
	  {
		$("#specifyAudience").hide();
		$("#specifyAudienceRemove").hide();
	  }
  }

  $(document).ready(function(){
	$("#messageDiv").hide();
  });
</script>

<% 
	String actionNoValidation = "instantPollAudience.do";     // default action without validation
	String actionDispatch;
	String displayFlag = "false";
%>
<c:if test="${isDisabledFlag == 'true' }">
 <% displayFlag = "true"; %>
</c:if>
<html:form styleId="contentForm" action="instantPollMaintain">
  <html:hidden property="id" name="instantPollForm" styleId="id" />
  <html:hidden property="method"/>
  <html:hidden property="primaryAudienceListCount" name="instantPollForm"/>
  <c:if test="${isDisabledFlag == 'true' }">
  	<html:hidden property="startDate"/>
  	<html:hidden property="question"/>
  	<html:hidden property="answer1"/>
  	<html:hidden property="answer2"/>
  	<html:hidden property="answer3"/>
  	<html:hidden property="answer4"/>
  	<html:hidden property="answer5"/>
  	<html:hidden property="notifyParticipant"/>
    <html:hidden property="audienceType"/>
  </c:if>
  
  <table>
    <tr>
      <td>
        <span class="headline"><cms:contentText key="ADD_TITLE" code="instantpoll.library" /></span>      
        <br/>
        <span class="content-instruction">
          <cms:contentText key="INSTRUCTION" code="instantpoll.library"/>
        </span>
        <br/>
        <div id="messageDiv" class="error">
        </div>
        <cms:errors/>  
        
        <table>
          <tr id="instantPollStartDate" class="form-row-spacer">
            <cms:contentText key="SUBMISSION_DATES" code="instantpoll.library"/>
            <beacon:label property="startDate" required="true">
           		<cms:contentText key="START_DATE" code="instantpoll.library"/>
           	</beacon:label>
            <c:choose>
            	<c:when test="${isDisabledFlag == 'true' }">
	            	<td class="content-field">
		            	<html:text property="startDate" maxlength="10" size="10" styleClass="content-field" styleId="startDate"  readonly="true" onfocus="clearDateMask(this);" disabled="<%=displayFlag%>"/>
	           		</td>
				</c:when>
            	<c:otherwise> 
	            	<td class="content-field">          	     
	             	  <html:text property="startDate" maxlength="10" size="10" styleClass="content-field" styleId="startDate"  readonly="true" onfocus="clearDateMask(this);"/>
		           	  <img alt="start date" id="startDateTrigger" src="<%= RequestUtils.getBaseURI(request) %>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon"/>
		           	 </td>
	           	</c:otherwise>
            </c:choose>     	
          </tr> 
                 
          <tr id="instantPollEndDate" class="form-row-spacer">
            <beacon:label property="endDate" required="true">
              <cms:contentText key="END_DATE" code="instantpoll.library"/>
            </beacon:label>           
            <td class="content-field">
              <html:text property="endDate" maxlength="10" size="10" styleClass="content-field" readonly="true" styleId="endDate" onfocus="clearDateMask(this);"/>
              <img alt="end date" id="endDateTrigger" src="<%= RequestUtils.getBaseURI(request) %>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" />
            </td>
          </tr>
          
          <tr class="form-blank-row"><td colspan="3"></td></tr>
          
          <tr class="form-row-spacer">
            <beacon:label property="notifyParticipant">
              <cms:contentText key="NOTIFY_PARTICIPANT" code="instantpoll.library"/>
            </beacon:label>
            <td class="content-field">
	            <html:checkbox property="notifyParticipant" styleClass="content-field" disabled="<%=displayFlag%>"/> 
            </td>
          </tr>
          
          <tr class="form-blank-row"><td colspan="3"></td></tr>
          
          <tr class="form-row-spacer">
            <beacon:label property="question" required="true">
              <cms:contentText key="QUESTION" code="instantpoll.library"/>
            </beacon:label>
            <td class="content-field">
	            <html:text property="question" styleClass="content-field" size="85" maxlength="500" disabled="<%=displayFlag%>"/> 
            </td>
          </tr>
          
          <tr class="form-blank-row"><td colspan="3"></td></tr>
          
          <tr class="form-row-spacer" id="pollAnswers">
            <beacon:label property="answer1" styleClass="content-field-label-top" required="true">
               <cms:contentText key="POLL_ANSWERS" code="instantpoll.library"/><br/>
              <font color="#808080"><cms:contentText key="MINIMUM_REQUIRED" code="instantpoll.library"/></font><br>
            </beacon:label>
            <td class="content-field"></td>
          </tr>
          
          <tr class="form-blank-row"><td colspan="3"></td></tr>
          
          <tr class="form-row-spacer" id="pollAnswers">
            <beacon:label property="answer1" styleClass="content-field-label-top">
              &nbsp;&nbsp;&nbsp;&nbsp;<cms:contentText key="ANSWER1" code="instantpoll.library"/>
            </beacon:label>
            <td class="content-field">
	         <html:text property="answer1" styleClass="content-field" size="85" maxlength="350" disabled="<%=displayFlag%>"/> 
            </td>
          </tr>
          
          <tr class="form-blank-row"><td colspan="3"></td></tr>
          
          <tr class="form-row-spacer" id="pollAnswers">
            <beacon:label property="answer2" styleClass="content-field-label-top">
              &nbsp;&nbsp;&nbsp;&nbsp;<cms:contentText key="ANSWER2" code="instantpoll.library"/>
            </beacon:label>
            <td class="content-field">
	         <html:text property="answer2" styleClass="content-field" size="85" maxlength="350" disabled="<%=displayFlag%>"/> 
            </td>
          </tr>
          
          <tr class="form-blank-row"><td colspan="3"></td></tr>
          
          <tr class="form-row-spacer" id="pollAnswers">
            <beacon:label property="answer3" styleClass="content-field-label-top">
              &nbsp;&nbsp;&nbsp;&nbsp;<cms:contentText key="ANSWER3" code="instantpoll.library"/>
            </beacon:label>
            <td class="content-field">
	          <html:text property="answer3" styleClass="content-field" size="85" maxlength="350" disabled="<%=displayFlag%>"/> 
            </td>
          </tr>
          
          <tr class="form-blank-row"><td colspan="3"></td></tr>
          
          <tr class="form-row-spacer" id="pollAnswers">
            <beacon:label property="answer4" styleClass="content-field-label-top">
              &nbsp;&nbsp;&nbsp;&nbsp;<cms:contentText key="ANSWER4" code="instantpoll.library"/>
            </beacon:label>
            <td class="content-field">
	          <html:text property="answer4" styleClass="content-field" size="85" maxlength="350" disabled="<%=displayFlag%>"/> 
            </td>
          </tr>
          
          <tr class="form-blank-row"><td colspan="3"></td></tr>
          
          <tr class="form-row-spacer" id="pollAnswers">
            <beacon:label property="answer5" styleClass="content-field-label-top">
              &nbsp;&nbsp;&nbsp;&nbsp;<cms:contentText key="ANSWER5" code="instantpoll.library"/>
            </beacon:label>
            <td class="content-field">
	          <html:text property="answer5" styleClass="content-field" size="85" maxlength="350" disabled="<%=displayFlag%>"/> 
            </td>
          </tr>

          <tr class="form-blank-row"><td colspan="3"></td></tr>

          <tr class="form-row-spacer" id="audienceType">
            <beacon:label property="audienceType" required="true" styleClass="content-field-label-top">
              <cms:contentText key="AUDIENCE_TYPE" code="instantpoll.library"/>
            </beacon:label>
            <td colspan=2 class="content-field content-field-label-top">  	
              <table>
              	<tr>
              	  <td class="content-field">
              		<html:radio  property="audienceType" value="allactivepaxaudience" onclick="javascript: setSpecifyAudienceVisibility();"/>&nbsp;&nbsp;
              		<cms:contentText key="ACTIVE_PAX" code="instantpoll.library"/>
              		<br>
              		<html:radio property="audienceType" value="specifyaudience" onclick="javascript: setSpecifyAudienceVisibility();"/>&nbsp;&nbsp;
              		<cms:contentText key="SPEC_AUDIENCE" code="instantpoll.library"/>
              	  </td> 
              	</tr>
              
              	<tr id="specifyAudience">
              	  <td>
                	<table class="crud-table" width="100%">
                      <tr>
                    	<th colspan="3" class="crud-table-header-row">
                      	  <cms:contentText key="AUDIENCE_LIST" code="instantpoll.library"/>
                      	  &nbsp;&nbsp;&nbsp;&nbsp;
                      	  <html:select property="primaryAudienceId" styleClass="content-field">
                        	<html:options collection="availablePrimaryAudiences" property="id" labelProperty="name"  />
                      	  </html:select>
                      	  <%
                        	actionDispatch = "setActionAndDispatch('" + actionNoValidation +"', 'addSubmitterAudience')";
                      	  %>
                      	  <html:submit styleClass="content-buttonstyle" onclick="<%= actionDispatch%>">
                        	<cms:contentText code="system.button" key="ADD" />
                      	  </html:submit>
                      	  <br>
                      	  <cms:contentText key="CREATE_AUDIENCE_USING_TAG_NAME" code="instantpoll.library"/>
                      	  <a href="javascript:setActionDispatchAndSubmit('<%=actionNoValidation%>', 'prepareSubmitterAudienceLookup');" class="crud-content-link">
                      		<cms:contentText key="LIST_BUILDER" code="instantpoll.library"/>
                      	  </a>
                    	</th>
                    	<th valign="top" class="crud-table-header-row"><cms:contentText code="system.general" key="CRUD_REMOVE_LABEL"/></th>
                  	  </tr>
                  	  
                  	  <c:set var="switchColor" value="false"/>
                  	  <nested:iterate id="instantPollSubmitterAudience" name="instantPollForm" property="primaryAudienceListAsList" >
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
                  			<c:out value="${instantPollSubmitterAudience.name}"/>
                		</td>
                		<td class="content-field">                  
                    	  <&nbsp;
                    		<c:out value="${instantPollSubmitterAudience.size}"/>
                    	  &nbsp;>                  
                		</td>
                		<td class="content-field">
							<%	Map parameterMap = new HashMap();
								InstantPollAudienceFormBean temp = (InstantPollAudienceFormBean)pageContext.getAttribute( "instantPollSubmitterAudience" );
								parameterMap.put( "audienceId", temp.getAudienceId() );
								pageContext.setAttribute("linkUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), "/instantPollAudience.do?method=displayPaxListPopup", parameterMap, true ) );
							%>
                  			<a href="javascript:popUpWin('<c:out value="${linkUrl}"/>', 'console', 750, 500, false, true);" class="crud-content-link"><cms:contentText key="VIEW_LIST" code="forum.topic.details"/></a>
                		</td>
                  		<td align="center" class="content-field">
                    	  <nested:checkbox property="removed"  />
                  		</td>
              		  </nested:iterate>
                	</table>
              	  </td>
            	</tr>
            	
            	<tr id="specifyAudienceRemove" class="form-row-spacer">
              	  <td align="right">
                  	<% actionDispatch = "setActionAndDispatch('" + actionNoValidation +"', 'removeSubmitterAudience')"; %>
                  	<html:submit styleClass="content-buttonstyle" onclick="<%= actionDispatch%>">
                    	<cms:contentText key="REMOVE" code="system.button"/>
                  	</html:submit>
                  </td>
              	</tr> 
              </table>
            </td>
          </tr>
 
          <%--BUTTON ROWS ... only display these two rows if there are buttons to be displayed.--%>
          <tr class="form-buttonrow">
            <td></td>
            <td></td>
            <td align="right">
              <div align="center">
              	<c:choose>
                  <c:when test='${instantPollForm.id == null or instantPollForm.id == 0}'>
                  	<beacon:authorize ifNotGranted="LOGIN_AS">
                      <html:submit styleClass="content-buttonstyle" onclick="setDispatch('create')">
                      	<cms:contentText code="system.button" key="SAVE" />
                      </html:submit>
                  	</beacon:authorize>
                  </c:when>
                  <c:otherwise>
                  	<beacon:authorize ifNotGranted="LOGIN_AS">
                      <html:submit styleClass="content-buttonstyle" onclick="setDispatch('update')">
                      	<cms:contentText code="system.button" key="SAVE" />
                      </html:submit>
                  	</beacon:authorize>
                  </c:otherwise>
              	</c:choose>
              	<html:button property="cancel" styleClass="content-buttonstyle" onclick="callUrl('instantPollsList.do')">
                	<cms:contentText code="system.button" key="CANCEL" />
              	</html:button>
              </div>
            </td>
          </tr>
          <%--END BUTTON ROW--%>
        </table>
      </td>
    </tr>
  </table>
</html:form>

<SCRIPT TYPE="text/javascript">
  $(document).ready(function() {
	  // Handler for .ready() called.
	  setSpecifyAudienceVisibility();
  });
  
  Calendar.setup(
	      {
	        inputField  : "startDate",         // ID of the input field
	        ifFormat    : "${TinyMceDatePattern}",          // the date format
	        button      : "startDateTrigger"   // ID of the button
	      }
	    );
	    Calendar.setup(
	      {
	        inputField  : "endDate",          // ID of the input field
	        ifFormat    : "${TinyMceDatePattern}",         // the date format
	        button      : "endDateTrigger"    // ID of the button
	      }
	    );
</script>