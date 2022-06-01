<%@ include file="/include/taglib.jspf"%>
<%@ page import="java.util.*"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.value.ParticipantRosterSearchValueBean"%>
<%@ page import="com.biperf.core.ui.user.UserForm"%>

	<%
		ParticipantRosterSearchValueBean bean1;
	%>

<c:set var="count" value='<%=request.getAttribute("totalRecords")%>' />
<display:table defaultsort="1" defaultorder="ascending" sort="page" class="table table-striped crud-table" partialList="true"
	name="results" id="item" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>" pagesize="50" size="${count}">
	<display:setProperty name="basic.msg.empty_list_row">
		<tr class="crud-content" align="left">
			<td><cms:contentText key="NOTHING_FOUND" code="system.errors" />
			</td>
		</tr>
	</display:setProperty>
	<display:setProperty name="basic.empty.showtable" value="true" />

	<display:column titleKey="participant.roster.management.list.EDIT" class="editColumn" >
		<%
			bean1 = (ParticipantRosterSearchValueBean)pageContext.getAttribute( "item" );
			UserForm userForm = (UserForm)request.getAttribute("userForm");
            Map parameterMap = new HashMap();
        	parameterMap.put( "rosterManagerId", userForm.getRosterManagerId() );
        	parameterMap.put( "nodeId", userForm.getNodeId() );
        	parameterMap.put( "userId", bean1.getId() );
        	pageContext.setAttribute("rosterSearchDetailUrl1", ClientStateUtils.generateEncodedLink( "", "rosterMgmtEditInit.do?method=prepareRosterEdit", parameterMap ) );
        	request.setAttribute("addUrl", ClientStateUtils.generateEncodedLink( "", "rosterMgmtAddInit.do", parameterMap ));
		%>
		<c:url var="url" value="${rosterSearchDetailUrl1}" />
		<a href="<c:out value="${url}" />"><i class="icon-pencil2"></i></a>
	</display:column>

	<display:column
		titleKey="participant.roster.management.list.TH_PAX_NAME"
		sortable="true" sortProperty="nameLFM" class="sortable nameColumn unsorted" headerClass="row-sortable row-sorted row-sorted-asc nameColumn">
		<a href="#" class="paxPopover" data-participant-ids="[${item.id}]">
            <c:out value="${item.nameLFM}" />
		</a>
	</display:column>

	<display:column
		titleKey="participant.roster.management.list.TH_ROLE_NAME" class="sortable roleColumn unsorted"
		sortable="true" sortProperty="role"
		headerClass="row-sortable roleColumn">
		<c:out value="${item.role}" />
	</display:column>

	<display:column
		titleKey="participant.roster.management.list.TH_LAST_LOGIN" class="sortable lastLoginColumn unsorted"
		sortable="true" sortProperty="displayLastLogin"
		headerClass="row-sortable lastLoginColumn">
		<c:out value="${item.displayLastLogin}" />
	</display:column>

	<display:column
		titleKey="participant.roster.management.modify.JOB_POSITION" class="sortable jobColumn unsorted"
		sortable="true" sortProperty="jobPosition" headerClass="row-sortable jobColumn">
		<c:out value="${item.jobPosition}" />
	</display:column>

	<display:column
		titleKey="participant.roster.management.list.TH_EMAIL_ADDR" class="sortable emailColumn unsorted"
		sortable="true" sortProperty="emailAddress" headerClass="row-sortable emailColumn">
		<c:out value="${item.emailAddress}" />
	</display:column>

	<display:column
		titleKey="participant.roster.management.list.TH_LOGIN_ID" class="sortable loginIdColumn unsorted"
		sortable="true" sortProperty="userName" headerClass="row-sortable loginIdColumn">
		<c:out value="${item.userName}" />
	</display:column>
</display:table>
