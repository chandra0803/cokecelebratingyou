<%@ page import="java.util.*"%>
<%@ page import="com.biperf.core.ui.welcomemail.WelcomeMessageForm"%>
<%@ page import="com.biperf.core.domain.welcomemail.WelcomeMessageBean"%>

<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>
<%@ include file="/include/taglib.jspf"%>

<html:form styleId="contentForm" action="welcomeMessageDisplay">

	<html:hidden property="method" />
	<beacon:client-state>
		<beacon:client-state-entry name="welcomeMessageId"
			value="${welcomeMessageForm.welcomeMessageId}" />
	</beacon:client-state>
	<table border="0" cellpadding="10" cellspacing="0" width="100% valign="top">
		<tr>
			<td><span class="headline"> <cms:contentText
				key="WELCOME_MESSAGE" code="admin.welcomemessage" /> </span> <span
				id="quicklink-add"></span><script type="text/javascript">quicklink_display_add('<cms:contentText key="WELCOME_MESSAGE" code="admin.welcomemessage"/>','<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>'); </script>


			<br />
			<br />
			<span class="content-instruction"> <cms:contentText
				key="LIST_INSTRUCTION" code="admin.welcomemessage" /> </span> <%--END INSTRUCTIONS--%>
		<tr>
			<td><cms:errors /></td>
		</tr>

		<tr class="form-buttonrow">
			<td>
			<table width="100%">
				<tr>
					<td align="left"><html:button property="Add"
						styleClass="content-buttonstyle"
						onclick="setActionDispatchAndSubmit('welcomeMessageDisplay.do','prepareCreate')">
						<cms:contentText key="ADD_WELCOME_MESSAGE"
							code="admin.welcomemessage" />
					</html:button></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<display:table defaultsort="1" defaultorder="descending"
				name="welcomeMessageList" id="welcomeMessageFormConst" pagesize="10"
				sort="list"
				requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
		</tr>
		<display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
		</display:setProperty>
		<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
		<display:column titleKey="admin.welcomemessage.NOTIFICATION_DATE"
			headerClass="crud-table-header-row"
			class="crud-content  left-align nowrap"
			sortProperty="notificationDate" sortable="true">
			<c:out value="${welcomeMessageFormConst.notificationDate}" />
		</display:column>
		<%
					WelcomeMessageBean temp = (WelcomeMessageBean) pageContext
					.getAttribute("welcomeMessageFormConst");
		%>
		<display:column titleKey="admin.welcomemessage.AUDIENCE_SELECTED"
			headerClass="crud-table-header-row"
			class="crud-content left-align nowrap">
			<%
				String audienceList = temp.getAudienceList();

				if (audienceList.indexOf("$#@$") != -1) {
					StringTokenizer tokens = new StringTokenizer(audienceList,
					"$#@$");

					while (tokens.hasMoreTokens()) {
			%>
			<table width="100%">
				<tr>
					<%=tokens.nextToken()%>
				</tr>
			</table>
			<%
				}
				} else {
			%>
			<c:out value="${welcomeMessageFormConst.audienceList}" />
			<%
			}
			%>
		</display:column>
		<display:column titleKey="admin.welcomemessage.MESSAGE_SELECTED"
			headerClass="crud-table-header-row"
			class="crud-content left-align nowrap" sortProperty="message"
			sortable="true">
			<%
			    Map parameterMap = new HashMap();
			    if (temp != null) {
					parameterMap.put("messageId", new Long(temp.getMessageId()));
				}
				pageContext.setAttribute("detailsUrl", ClientStateUtils
						.generateEncodedLink("",
						"newWelcomeMessageDisplay.do?method=prepareUpdate",
						parameterMap));

			%>
            <div>
			  <a href="<c:out value="${detailsUrl}"/>" class="crud-content-link">
			    <c:out value="${welcomeMessageFormConst.message}"/>
              </a>
            </div>
		</display:column>
		<display:column titleKey="claims.form.list.REMOVE"
			headerClass="crud-table-header-row"
			class="crud-content left-align nowrap">
			<html:checkbox property="delete"
				value="${welcomeMessageFormConst.welcomeMessageId}" />
		</display:column>
		</display:table>
		</tr>
		<tr class="form-buttonrow">
			<td>
			<table width="100%">
				<tr>
					<td align="right"><html:button property="Remove"
						styleClass="content-buttonstyle"
						onclick="setDispatchAndSubmit('remove')">
						<cms:contentText key="REMOVE_SELECTED" code="system.button" />
					</html:button></td>
				</tr>

				<tr>
					<td align="center"><c:url var="homePageUrl"
						value="/homePage.do" /> <html:button property="homePageButton"
						styleClass="content-buttonstyle"
						onclick="callUrl('${homePageUrl}')">
						<cms:contentText key="BACK_TO_HOME" code="system.button" />
					</html:button></td>
				</tr>
			</table>
			</td>
		</tr>
	</table>


</html:form>
