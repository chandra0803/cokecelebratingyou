<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.user.User"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ include file="/include/taglib.jspf"%>

<table border="0" cellpadding="3" cellspacing="1" class="crud-table"  width="550">
	<tr>
		<td colspan="2" class="headline"><cms:contentText key="PARTICIPANTS" code="system.general" /></td>
	</tr>
	<tr>
		<td width="100%">
			<%  Map parameterMap = new HashMap();
					User temp;
			%>
			<display:table defaultsort="1" defaultorder="ascending" name="paxList" id="participant">
			<display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
			</display:setProperty>
			<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
				<display:column title='<cms:contentText key="PARTICIPANT" code="system.general" />' class="center-align" headerClass="crud-table-header-row">
					<%  temp = (User)pageContext.getAttribute("participant");
							parameterMap.put( "userId", temp.getId() );
							pageContext.setAttribute("linkUrl", ClientStateUtils.generateEncodedLink( "", "participantDisplay.do?method=display", parameterMap ) );
					%>
					<a href="${linkUrl}">
						<c:out value="${participant.firstName}"/>&nbsp;
						<c:out value="${participant.lastName}"/>
					</a>
				</display:column>
			</display:table>
		</td>
	</tr>
</table>