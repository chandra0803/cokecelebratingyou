<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.value.forum.ForumAudienceFormBean"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<% 
	String actionNoValidation = "forumAudience.do";     // default action without validation
	String actionDispatch;
	String displayFlag = "false";
%>

<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-en.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-setup.js"></script>
<script type="text/javascript">

function setSpecifyOrderVisibility()
{
	  if ( $("input:radio[name='specifyOrder']:checked").val()=="true" )	
	  {
		document.getElementById('sortOrder').style.display = "table-row";
		$("#sortOrder").show();
	  }
	  else
	  {
		$("#sortOrder").hide();
	  }
}

function setStickyDateVisibility()
{
	  if ( $("input:radio[name='sticky']:checked").val()=="true" )	
	  {
		document.getElementById('stickyStartDate').style.display = "table-row";
		document.getElementById('stickyEndDate').style.display = "table-row";
		$("#stickyStartDate").show();
		$("#stickyEndDate").show();
	  }
	  else
	  {
		$("#stickyStartDate").hide();
		$("#stickyEndDate").hide();
	  }
}

function setSpecifyAudienceVisibility()
{
	  if ( $("input:radio[name='audienceType']:checked").val()=="specify audience" )	
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

</script>

<script type="text/javascript">

$(document).ready(function(){
	$("#messageDiv").hide();
});

</script>

<html:form styleId="contentForm" action="forumTopicMaintain">
<html:hidden property="id" name="forumTopicForm" styleId="id" />
<html:hidden property="method"/>
<html:hidden property="primaryAudienceListCount" name="forumTopicForm"/>

  <table>
    <tr>
      <td>
          <span class="headline"><cms:contentText key="ADD_TITLE" code="forum.topic.details" /></span>
          <span id="quicklink-add"></span><script type="text/javascript">quicklink_display_add('<cms:contentText key="ADD_TITLE" code="forum.topic.details"/>','<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>');</script>       
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="INSTRUCTION" code="forum.topic.details"/>
        </span>
        <br/><br/>
        <div id="messageDiv" class="error">
        </div>
         <cms:errors/>  
        <table>
          <tr class="form-row-spacer">
            <beacon:label property="topicCmAssetCode" required="true">
              <cms:contentText key="TOPIC_NAME" code="forum.topic.details"/>
            </beacon:label>
            <td class="content-field">
	               <html:text property="topicCmAssetCode" styleClass="content-field" size="50" maxlength="150" disabled="false"/> 
            </td>
          </tr>

          <tr class="form-blank-row"><td colspan="3"></td></tr>

          <tr class="form-row-spacer" id="sticky">
            <beacon:label property="sticky" required="true" styleClass="content-field-label-top">
              <cms:contentText key="STICKY" code="forum.topic.details"/>
            </beacon:label>
            <td class="content-field content-field-label-top">
              <html:radio property="sticky" value="false" onclick="javascript: setStickyDateVisibility();"/>&nbsp;&nbsp;<cms:contentText code="system.common.labels" key="NO" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
              <cms:contentText key="STICKY_TOPIC_DEF" code="forum.topic.details"/>
              <br>
              <html:radio property="sticky" value="true" onclick="javascript: setStickyDateVisibility();" />&nbsp;&nbsp;<cms:contentText code="system.common.labels" key="YES" />
            </td>
          </tr>
          
          <tr class="form-blank-row"><td colspan="3"></td></tr>

          <tr id="stickyStartDate" class="form-row-spacer">
            <beacon:label property="stickyStartDate" required="true">
              <cms:contentText key="START_DATE" code="forum.topic.details"/>
            </beacon:label>
            <td class="content-field">
	               <html:text property="stickyStartDate" maxlength="10" size="10" styleClass="content-field" styleId="startDate"  readonly="true" disabled="false" onfocus="clearDateMask(this);"/>
	               <img alt="start date" id="startDateTrigger" src="<%= RequestUtils.getBaseURI(request) %>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon"/>
           </td>
         </tr>        
          
          <tr class="form-blank-row"><td colspan="3"></td></tr>

          <tr id="stickyEndDate" class="form-row-spacer">
            <beacon:label property="stickyEndDate" required="true">
              <cms:contentText key="END_DATE" code="forum.topic.details"/>
            </beacon:label>
            <td class="content-field">
              <html:text property="stickyEndDate" maxlength="10" size="10" styleClass="content-field" readonly="true" styleId="endDate" onfocus="clearDateMask(this);"/>
              <img alt="end date" id="endDateTrigger" src="<%= RequestUtils.getBaseURI(request) %>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon"/>
            </td>
          </tr>

          <tr class="form-blank-row"><td colspan="3"></td></tr>
          
          <tr class="form-row-spacer" id="specifyOrder">
           <beacon:label property="specifyOrder" required="true" styleClass="content-field-label-top">
              <cms:contentText key="SORT_ORDER" code="forum.topic.details"/>
           </beacon:label>
          <td colspan=2 class="content-field content-field-label-top">  	
          <table>
          <tr>
          <td class="content-field">
          <html:radio property="specifyOrder" value="false" onclick="javascript: setSpecifyOrderVisibility();"/>&nbsp;&nbsp;<cms:contentText code="system.common.labels" key="NO"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          <cms:contentText key="SORT_ORDER_TEXTBOX_DESC" code="forum.topic.details"/>
          <br>      
          <html:radio property="specifyOrder" value="true"  onclick="javascript: setSpecifyOrderVisibility();"/>&nbsp;&nbsp;<cms:contentText code="system.common.labels" key="YES"/>
          </td> 
          </tr>
          <tr class="form-row-spacer" id="sortOrder">
          <td class="content-field">
          <html:text property="sortOrder" styleClass="content-field" size="2" maxlength="2" disabled="false" />&nbsp;&nbsp;&nbsp;
          </td>
          </tr>
         </table>
         </td>
         </tr>

          <tr class="form-blank-row"><td colspan="3"></td></tr>

              <tr class="form-row-spacer" id="audienceType">
              <beacon:label property="audienceType" required="true" styleClass="content-field-label-top">
              <cms:contentText key="AUDIENCE_TYPE" code="forum.topic.details"/>
              </beacon:label>
              <td colspan=2 class="content-field content-field-label-top">  	
              <table>
              <tr>
              <td class="content-field">
              <html:radio  property="audienceType" value="all active participants" onclick="javascript: setSpecifyAudienceVisibility();"/>&nbsp;&nbsp;
              <cms:contentText key="ACTIVE_PAX" code="forum.topic.details"/>
              <br>
              <html:radio property="audienceType" value="specify audience" onclick="javascript: setSpecifyAudienceVisibility();"/>&nbsp;&nbsp;
              <cms:contentText key="SPEC_AUDIENCE" code="forum.topic.details"/>
              </td> 
              </tr>
              
              <tr id="specifyAudience">
              <td>
                <table class="crud-table" width="100%">
                  <tr>
                    <th colspan="3" class="crud-table-header-row">
                      <cms:contentText key="AUDIENCE_LIST" code="forum.topic.details"/>
                      &nbsp;&nbsp;&nbsp;&nbsp;
                      <html:select property="primaryAudienceId" styleClass="content-field"  disabled="<%=displayFlag%>">
                        <html:options collection="availablePrimaryAudiences" property="id" labelProperty="name"  />
                      </html:select>
                      <%
                        actionDispatch = "setActionAndDispatch('" + actionNoValidation +"', 'addSubmitterAudience')";
                      %>
                      <html:submit styleClass="content-buttonstyle" onclick="<%= actionDispatch%>"  disabled="<%=displayFlag%>">
                        <cms:contentText code="system.button" key="ADD" />
                      </html:submit>
                      <br>
                      <cms:contentText key="CREATE_AUDIENCE_USING_TAG_NAME" code="forum.topic.details"/>
                      <a href="javascript:setActionDispatchAndSubmit('<%=actionNoValidation%>', 'prepareSubmitterAudienceLookup');" class="crud-content-link">
                      <cms:contentText key="LIST_BUILDER" code="forum.topic.details"/>
                      </a>
                    </th>
                    <th valign="top" class="crud-table-header-row"><cms:contentText code="system.general" key="CRUD_REMOVE_LABEL"/></th>
                  </tr>
                  <c:set var="switchColor" value="false"/>
                  <nested:iterate id="forumSubmitterAudience" name="forumTopicForm" property="primaryAudienceListAsList" >
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
                  <c:out value="${forumSubmitterAudience.name}"/>
                </td>
                <td class="content-field">                  
                    <&nbsp;
                    <c:out value="${forumSubmitterAudience.size}"/>
                    &nbsp;>                  
                </td>
                <td class="content-field">
									<%	Map parameterMap = new HashMap();
											ForumAudienceFormBean temp = (ForumAudienceFormBean)pageContext.getAttribute( "forumSubmitterAudience" );
											parameterMap.put( "audienceId", temp.getAudienceId() );
											pageContext.setAttribute("linkUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), "/forum/forumAudience.do?method=displayPaxListPopup", parameterMap, true ) );
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
                <c:when test='${forumTopicForm.id == null or forumTopicForm.id == 0}'>
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
                <html:button property="cancel" styleClass="content-buttonstyle" onclick="callUrl('forumTopicList.do')">
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
  

  
  <SCRIPT TYPE="text/javascript">
$(document).ready(function() {
	  // Handler for .ready() called.	
		setSpecifyOrderVisibility();
		setStickyDateVisibility();
		setSpecifyAudienceVisibility();
});
</SCRIPT>
  
  <script type="text/javascript">  
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

<script type="text/javascript">
 updateLayersShown();
 </script>

</html:form>

