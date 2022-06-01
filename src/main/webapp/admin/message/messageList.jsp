<%--UI REFACTORED--%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.message.Message"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.service.goalquest.GoalQuestSurveyUtils"%>
<%@ include file="/include/taglib.jspf"%>

<html:form styleId="contentForm" action="messageList">
<script type="text/javascript">
<!--
   function messageDetails( method)
  {
	  document.messageListForm.method.value = method;	  
	  document.messageListForm.action = "messageDisplay.do";
	  document.messageListForm.submit();
    return false;
  }
  function maintainMsgList( statusCode )
  {
	  document.messageListForm.method.value = "display";
	  document.messageListForm.statusCode.value = statusCode;
	  document.messageListForm.submit();
    return false;
  }
//-->
</script>
	<html:hidden property="statusCode"/>
	<html:hidden property="method"/>
	<beacon:client-state>
		<beacon:client-state-entry name="messageId" value="${messageListForm.messageId}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>

  
        <c:if test="${messageListForm.statusCode == 'act'}">
          <span class="headline"><cms:contentText key="MESSAGE_LIBRARY_TITLE" code="admin.message.library"/></span>
          <span id="quicklink-add"></span><script type="text/javascript">quicklink_display_add('<cms:contentText key="MESSAGE_LIBRARY_TITLE" code="admin.message.library"/>','<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>'); </script>
		<%-- Commenting out to fix in a later release
		  <input type="button" class="webhelp-button" onclick="javascript:FMCOpenHelp( 'G3H3', 'HTML Help Window', null, null );" target="_webhelp" value="<cms:contentText code="system.webhelp" key="WEB_HELP"/>">
		--%>				

          <%--INSTRUCTIONS--%>
          <br/><br/>
          <span class="content-instruction">
            <cms:contentText key="INSTRUCTIONAL_COPY" code="admin.message.library"/>
          </span>
          <br/><br/>
          <%--END INSTRUCTIONS--%>
        </c:if>
        <c:if test="${messageListForm.statusCode == 'ina'}">
          <span class="headline"><cms:contentText key="INACTIVE_TITLE" code="admin.message.library"/></span>
          <span id="quicklink-add"></span><script type="text/javascript">quicklink_display_add('<cms:contentText key="MESSAGE_LIBRARY_TITLE" code="admin.message.library"/>','<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>'); </script>
          <%--INSTRUCTIONS--%>
          <br/><br/>
          <span class="content-instruction">
            <cms:contentText key="INACTIVE_INSTRUCTIONAL_COPY" code="admin.message.library"/>
          </span>
          <br/><br/>
          <%--END INSTRUCTIONS--%>
        </c:if>

        <cms:errors/>

        <table width="100%">
        <c:if test="${messageListForm.statusCode == 'act'}">
        <tr>
          <td class="content-field">
            <span class="content-bold error"><cms:contentText key="MODULE" code="admin.message.details"/></span>
       	      <html:select property="moduleCode" styleClass="content-field" onchange="javascript:maintainMsgList('act')">
		          <html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>
		          <html:options collection="moduleList" property="code" labelProperty="name"  />
		          <html:option value='all'><cms:contentText key="ALL" code="system.general"/></html:option>
	          </html:select>
	          <cms:contentText key="SELECT_MODULE_INSTR" code="admin.message.library"/>
    	    </td>
          </tr>
          </c:if>
          <tr>
            <td align="right">
		        <%-- Added this seperate Goal Quest Survey messages.  GQ message starts with GQ_ vs other messages cm.asset code which starts with message_data.xxxxxx --%>
		 		<%-- Added new logic to add message_data. to all GQ survey message types by identifying the prefix "GQ_" --%>
		  		<c:set var="assetCodePrefix" value="<%= GoalQuestSurveyUtils.PREVIEW_MESSAGE_GQ_PREFIX  %>"/>
	  		
  		            
							<% 	Map parameterMap = new HashMap();
									Message temp;
							%>
              <display:table defaultsort="1" defaultorder="ascending" name="messageList" id="message" pagesize="${pageSize}" sort="list" requestURI="messageList.do">
              <display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
                <display:column property="name" titleKey="admin.message.library.MESSAGE_NAME" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true" sortProperty="name" />
                <display:column property="moduleCode.name" titleKey="admin.message.library.MODULE" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true" sortProperty="moduleCode.name"/>
                <display:column titleKey="admin.message.library.SUBJECT" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true" sortProperty="i18nSubject">
									<%	temp = (Message)pageContext.getAttribute( "message" );
										if(temp != null)
										{
											parameterMap.put( "messageId", temp.getId() );
										}
											pageContext.setAttribute("detailsUrl", ClientStateUtils.generateEncodedLink( "", "messageDisplay.do?method=prepareUpdate", parameterMap ) );
										
										
									%>
   				        <a href="<c:out value="${detailsUrl}"/>" class="crud-content-link">
						  <c:if test="${fn:startsWith(message.cmAssetCode,assetCodePrefix)}">
								<cms:contentText code="message_data.${message.cmAssetCode}" key="<%=Message.CM_KEY_SUBJECT%>"/>  
						  </c:if>
						  
						  <c:if test="${!fn:startsWith(message.cmAssetCode,assetCodePrefix)}">
						  		 <cms:contentText code="${message.cmAssetCode}" key="<%=Message.CM_KEY_SUBJECT%>"/>
						  </c:if>
		              </a>
                </display:column>
                <display:column property="translated" titleKey="admin.message.library.TRANSLATED" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true" sortProperty="translated"/>
                <display:column titleKey="admin.message.library.LAST_SENT" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true" sortProperty="dateLastSent">
		        	    <fmt:formatDate value="${message.dateLastSent}" pattern="${JstlDatePattern}" />
                </display:column>
								<c:if test="${messageListForm.statusCode == 'act'}">
                	<display:column titleKey="admin.message.library.ACTIONS" headerClass="crud-table-header-row" class="crud-content center-align" sortable="false">
		        	    	<c:if test="${message.wizardSendable}">
											<% pageContext.setAttribute("sendUrl", ClientStateUtils.generateEncodedLink( "", "sendMessageDisplay.do?method=prepareSend", parameterMap ) ); %>
											<input type="button" name="sendBtn" class="content-buttonstyle" onclick=location.href="<c:out value="${sendUrl}"/>" value="<cms:contentText code="admin.message.library" key="SEND_BUTTON" />" />
										</c:if>
                	</display:column>
								</c:if>
              </display:table>
            </td>
          </tr>
          <%--BUTTON ROWS --%>
          <tr class="form-buttonrow">
            <td>
              <table width="100%">
                <tr>
                  <td align="left">
                    <c:if test="${messageListForm.statusCode == 'act'}">
                    <beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
                		<html:submit styleClass="content-buttonstyle"  onclick="messageDetails('prepareCreate')">
			                <cms:contentText code="admin.message.library" key="ADD_NEW_BUTTON"/>
			            </html:submit>
	                </beacon:authorize>
			                <html:submit styleClass="content-buttonstyle"  onclick="maintainMsgList('ina')">
				                <cms:contentText code="admin.message.library" key="VIEW_INACTIVE_BUTTON"/>
			                </html:submit>
                    </c:if>
                    <c:if test="${messageListForm.statusCode == 'ina'}">
			                <html:submit styleClass="content-buttonstyle"  onclick="maintainMsgList('act')">
                    	  <cms:contentText code="admin.message.library" key="BACK_TO_LIB_BUTTON"/>
                    	</html:submit>
                    </c:if>
                  </td>
                </tr>
              </table>
            </td>
          </tr>
        </table>
        <%-- Table --%>

       </td>
     </tr>
  </table>
</html:form>

