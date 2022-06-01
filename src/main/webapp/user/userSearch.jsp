<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.user.User"%>
<%@ page import="com.biperf.core.ui.user.UserSearchForm"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ include file="/include/taglib.jspf"%>

<html:form styleId="contentForm" action="userSearch">
	<html:hidden property="method" value="search"/>
	<beacon:client-state>
		<beacon:client-state-entry name="userId" value="${userSearchForm.userId}"/>
		<beacon:client-state-entry name="returnActionUrl" value="${userSearchForm.returnActionUrl}"/>
	</beacon:client-state>

<table border="0" cellpadding="3" cellspacing="1" class="crud-table">
	<tr>
		<td class="headline" width="15%" valign="top">
			<cms:contentText code="user.search" key="TITLE" />
			<span id="quicklink-add"></span><script>quicklink_display_add('<cms:contentText key="TITLE" code="user.search"/>','<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>');</script>
		</td>
	</tr>	
	<tr>
		<td colspan="2">
			<table width="100%" cellpadding="3" cellspacing="1" >
				<tr>
				  <td class="content-field-label"><cms:contentText code="user.search" key="FIRSTNAME" /></td>
				  <td><html:text property="firstName" styleClass="content-field" size="50" maxlength="50"/></td>

				  <td class="content-field-label"><cms:contentText code="user.search" key="LASTNAME" /></td>
				  <td><html:text property="lastName" styleClass="content-field" size="50" maxlength="50"/></td>												
				</tr>
				
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>					

				<tr>
				  <td colspan="4" align="center">
					  <html:submit styleClass="login-buttonstyle">
						  <cms:contentText code="system.button" key="SEARCH" />
					  </html:submit>
				  </td>					
				</tr>		
			</table>
			<table>			
				<tr>
					<td>
						&nbsp;
					</td>
				</tr>
																					
				<tr>
				<td>
					<%	Map parameterMap = new HashMap();
							UserSearchForm tempForm = (UserSearchForm)request.getAttribute( "userSearchForm" );
							User temp;
					%>
					<display:table defaultsort="1" defaultorder="ascending" name="userList" id="user">
					
						<display:setProperty name="basic.msg.empty_list">
							<tr class="crud-content" align="left"><td colspan="{0}">
                <cms:contentText key="NO_SEARCH_RESULTS" code="user.search"/>
              </td></tr>
						</display:setProperty>
						<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
						
						<display:column titleKey="user.search.USERNAME" headerClass="crud-table-header-row">
							<%	temp = (User)pageContext.getAttribute("user");
									parameterMap.put( "userId", String.valueOf(tempForm.getUserId()) );
									parameterMap.put( "assignedToUserId", String.valueOf(temp.getId()) );
									String returnUrl = request.getContextPath() + tempForm.getReturnActionUrl();
									pageContext.setAttribute("url", ClientStateUtils.generateEncodedLink( "", returnUrl, parameterMap ) );
							%>
							<a href="<c:out value="${url}"/>"><c:out value="${user.userName}"/></a>
						</display:column>
						<display:column titleKey="user.search.LASTNAME" headerClass="crud-table-header-row">
							<c:out value="${user.lastName}"/>
						</display:column>
						<display:column titleKey="user.search.FIRSTNAME" headerClass="crud-table-header-row">
							<c:out value="${user.firstName}"/>
						</display:column>
						<display:column titleKey="user.search.MIDDLENAME"  headerClass="crud-table-header-row">
							<c:out value="${user.middleName}"/>
						</display:column>
					</display:table>				
				</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</html:form>