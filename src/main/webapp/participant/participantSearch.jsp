<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.user.User"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.participant.ParticipantSearchForm" %>
<%@ include file="/include/taglib.jspf"%>

<html:form styleId="contentForm" action="participantSearch">
	<html:hidden property="method" value=""/>
	<beacon:client-state>
		<beacon:client-state-entry name="nodeId" value="${participantSearchForm.nodeId}"/>
		<beacon:client-state-entry name="returnActionUrl" value="${participantSearchForm.returnActionUrl}"/>
		<beacon:client-state-entry name="returnActionMapping" value="${participantSearchForm.returnActionMapping}"/>
		<beacon:client-state-entry name="forwardActionUrl" value="${participantSearchForm.forwardActionUrl}"/>
	</beacon:client-state>

	<c:forEach var='item' items='${participantSearchForm.paramsMap}'>
		<html:hidden property="params(${item.key})"/>
	</c:forEach>

<table border="0" cellpadding="3" cellspacing="1" class="crud-table">
	<tr>
		<td class="headline" width="15%" valign="top">
			<cms:contentText code="participant.search" key="TITLE" />
			<span id="quicklink-add"></span><script>quicklink_display_add('<cms:contentText key="TITLE" code="participant.search"/>','<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>');</script>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<table width="100%" cellpadding="3" cellspacing="1" >
				<tr>
				<td class="content-field-label"><cms:contentText code="participant.search" key="FIRSTNAME" /></td>
				<td><html:text property="firstName" styleClass="content-field" size="50" maxlength="50"/>
				</td>

				<td class="content-field-label"><cms:contentText code="participant.search" key="LASTNAME" /></td>
				<td><html:text property="lastName" styleClass="content-field" size="50" maxlength="50"/>
				</td>
				</tr>

				<tr>
				<td class="content-field-label"><cms:contentText code="participant.search" key="USERNAME" /></td>
				<td><html:text property="userName" styleClass="content-field" size="50" maxlength="50"/>
				</td>

				<td class="content-field-label"><cms:contentText code="participant.search" key="EMAILADDR" /></td>
				<td><html:text property="emailAddr" styleClass="content-field" size="50" maxlength="50"/>
				</td>
				</tr>

				<tr>
				<td class="content-field-label"><cms:contentText code="participant.search" key="COUNTRY" /></td>
				<td><html:text property="country" styleClass="content-field" size="50" maxlength="50"/>
				</td>

				<td class="content-field-label"><cms:contentText code="participant.search" key="ZIPCODE" /></td>
				<td><html:text property="postalCode" styleClass="content-field" size="50" maxlength="50"/>
				</td>
				</tr>

				<tr>
					<td>
						&nbsp;
					</td>
				</tr>

				<tr>
				<td>&nbsp;</td>
				<td>
					<html:submit styleClass="login-buttonstyle" onclick="setDispatch('search')">
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
							User temp;
					%>
					<display:table defaultsort="1" defaultorder="ascending" name="participantList" id="participant">
						<display:setProperty name="basic.msg.empty_list">
              <tr class="crud-content" align="left"><td colspan="{0}">
              <%-- TODO: must come from Content Manager --%>
              No Search Results
              </td></tr>
            </display:setProperty>
            <display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
						<display:column title='<cms:contentText code="participant.search" key="USER_NAME" />' headerClass="crud-table-header-row">
							<c:choose>
								<c:when test="${!empty participantSearchForm.forwardActionUrl}" >
									<%  temp = (User)pageContext.getAttribute("participant");
											ParticipantSearchForm tempForm = (ParticipantSearchForm)request.getAttribute("participantSearchForm");
											parameterMap.put( "userId", temp.getId() );
											parameterMap.put( "userName", temp.getUserName() );
											parameterMap.put( "firstName", temp.getFirstName() );
											parameterMap.put( "lastName", temp.getLastName() );
											parameterMap.put( "nodeId", tempForm.getNodeId() );
											parameterMap.put( "returnActionMapping", tempForm.getReturnActionMapping() );
											for(Iterator iter = tempForm.getParamsMap().keySet().iterator(); iter.hasNext();)
											{
												Object key = iter.next();
												parameterMap.put( key, tempForm.getParamsMap().get(key) );
											}
											pageContext.setAttribute("url", ClientStateUtils.generateEncodedLink( "", tempForm.getForwardActionUrl() + "?method=displayCreateUserNodeRole", parameterMap ) );
									%>
								</c:when>
								<c:otherwise>
									<c:if test="${!empty participantSearchForm.returnActionUrl}" >
										<%  Map parameterMap1 = new HashMap();
												User temp1 = (User)pageContext.getAttribute("participant");
												ParticipantSearchForm tempForm1 = (ParticipantSearchForm)request.getAttribute("participantSearchForm");
												parameterMap1.put( "userId", temp1.getId() );
												parameterMap1.put( "userName", temp1.getUserName() );
												parameterMap1.put( "firstName", temp1.getFirstName() );
												parameterMap1.put( "lastName", temp1.getLastName() );
												for(Iterator iter = tempForm1.getParamsMap().keySet().iterator(); iter.hasNext();)
												{
													Object key = iter.next();
													parameterMap1.put( key, tempForm1.getParamsMap().get(key) );
												}
												pageContext.setAttribute("url", ClientStateUtils.generateEncodedLink( "", tempForm1.getReturnActionUrl(), parameterMap1 ) );
										%>
									</c:if>
									<c:if test="${empty participantSearchForm.returnActionUrl}" >
										<%  Map parameterMap2 = new HashMap();
												User temp2 = (User)pageContext.getAttribute("participant");
												ParticipantSearchForm tempForm2 = (ParticipantSearchForm)request.getAttribute("participantSearchForm");
												parameterMap2.put( "userId", temp2.getId() );
												parameterMap2.put( "firstName", temp2.getFirstName() );
												parameterMap2.put( "lastName", temp2.getLastName() );
												for(Iterator iter = tempForm2.getParamsMap().keySet().iterator(); iter.hasNext();)
												{
													Object key = iter.next();
													parameterMap2.put( key, tempForm2.getParamsMap().get(key) );
												}
												pageContext.setAttribute("url", ClientStateUtils.generateEncodedLink( "", "/participant/participantDisplay.do?method=display", parameterMap2 ) );
										%>
									</c:if>
								</c:otherwise>
							</c:choose>
							<a href="<c:out value="${url}"/>"><c:out value="${participant.userName}"/></a>
						</display:column>
						<display:column titleKey="recognition.select.recipients.SEARCHBY_LAST_NAME" headerClass="crud-table-header-row">
							<c:out value="${participant.lastName}"/>
						</display:column>
						<display:column title="recognition.select.recipients.SEARCHBY_FIRST_NAME" headerClass="crud-table-header-row">
							<c:out value="${participant.firstName}"/>
						</display:column>
						<display:column title="user.search.MIDDLENAME" headerClass="crud-table-header-row">
							<c:out value="${participant.middleName}"/>
						</display:column>
						<display:column title="report.dashboard.POSITION" headerClass="crud-table-header-row">
							<c:out value="${participant.positionType}"/>
						</display:column>
						<display:column title="recognition.select.recipients.SEARCHBY_DEPARTMENT" headerClass="crud-table-header-row">
							<c:out value="${participant.departmentType}"/>
						</display:column>
				    </display:table>
				</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</html:form>