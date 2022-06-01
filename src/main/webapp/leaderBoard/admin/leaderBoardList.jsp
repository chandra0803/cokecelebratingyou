<%--UI REFACTORED --%>
<%@ page import="java.util.*"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.domain.leaderboard.LeaderBoard"%>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.service.system.SystemVariableService" %>
<%@ page import="com.biperf.core.utils.*" %>

<script type="text/javascript">
<!--
	function callUrl(urlToCall) {
		window.location = urlToCall;
	}
	
function validateAndSubmit(leaderType)
{
	var checked=false;
	if(leaderType=='progress')
		checked = $("input[name=deleteLeaderBoardsProgress]:checked").length > 0;  
	else if(leaderType=='live')
		checked = $("input[name=deleteLeaderBoardsLive]:checked").length > 0;  
	else if(leaderType=='active')
		checked = $("input[name=deleteLeaderBoardsActive]:checked").length > 0;
		
	$("#messageDiv").hide();	
	var errorMessage='';
	var confirmationMessage='<cms:contentText key="CONFIRMATION_MESSAGE" code="leaderboard.errors" />';
		
	if(checked)
	{
		if(confirm(confirmationMessage))
		{
			setActionDispatchAndSubmit('leaderBoardMaintain.do?method=prepareRemove&leaderType='+leaderType, 'prepareRemove');
			return true;
		}
		else
		{
			return false;
		}
	}
	else
	{
		//errorMessage='<cms:contentText key="SELECT_ATLEAST_ONE_BADGE" code="gamification.validation.messages" />';
		if(leaderType=='progress')
			errorMessage='<cms:contentText key="ATLEAST_ONE_UNDER_CONSTRUCTION" code="leaderboard.errors" />';
			//errorMessage='Please select atleast 1 under construction leader board to remove';  
		else if(leaderType=='live')
			errorMessage='<cms:contentText key="ATLEAST_ONE_LIVE" code="leaderboard.errors" />';
			//errorMessage='Please select atleast 1 live leader board to remove';  
		else if(leaderType=='active')
			errorMessage='<cms:contentText key="ATLEAST_ONE_ACTIVE" code="leaderboard.errors" />';
			//errorMessage='Please select atleast 1 active leader board to remove';
		
		$("#messageDiv").html(errorMessage);
    	$("#messageDiv").show();
		return false;
	}
	
}

	function filterPromotionList() {
		var url = 'leaderBoardList.do';
		var urlToCall = url;

		window.location = urlToCall;
	}
	
	$(document).ready(function(){
	    	 $("#messageDiv").hide();
	});
//-->
</script>
		<%
		  Map createNewLB = new HashMap();
		    String createUrl = ( (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME ) ).getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
		    createNewLB.put( "source", "admin" );
		    createNewLB.put( "type", "create" );
	        pageContext.setAttribute( "createUrl", ClientStateUtils.generateEncodedLink( createUrl, "/leaderBoardAction.do?method=prepareCreate", createNewLB ) );
		%>
<html:form styleId="contentForm" action="leaderBoardMaintain">
	<html:hidden property="method" value="displayList" />
	<div id="messageDiv" class="error">
	</div>
	
	<table border="0" cellpadding="10" cellspacing="0" width="100%">
		<tr>
			<td><span class="headline"><cms:contentText
						key="LB_LIST_HEADER" code="leaderboard.admin.labels" /> </span></td>
		</tr>
		<beacon:authorize
			ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
			<tr class="form-row-spacer">
				<td align="left"><html:button property="leaderBoardAdd"
						styleClass="content-buttonstyle"
						onclick="callUrl('${createUrl}')">
						<cms:contentText key="ADD_LEADERBOARD" code="leaderboard.admin.labels" />
					</html:button></td>
			</tr>
		</beacon:authorize>
		<br>
		<br>
		<!-- Under Construction Leader Boards -->
		<%
		  Map underConstLBParamMap = new HashMap();
		    LeaderBoard temp1;
		    String siteUrl = ( (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME ) ).getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
		%>
		<tr class="form-row-spacer">
			<td colspan="2" class="subheadline" align="left"><cms:contentText
					key="UNDER_CON_LB_HEADER" code="leaderboard.admin.labels" /></td>
		</tr>
		<tr class="form-row-spacer">
			<td colspan="2"><display:table
					name="underConstructionLeaderBoards"
					id="underConstructionLeaderBoards" sort="list"
					requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
					<display:setProperty name="basic.msg.empty_list_row">
						<tr class="crud-content" align="left">
							<td colspan="{0}"><cms:contentText key="NOTHING_FOUND"
									code="system.errors" /></td>
						</tr>
					</display:setProperty>
					<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>

					<display:column titleKey="leaderboard.admin.labels.NAME"
						headerClass="crud-table-header-row"
						class="crud-content left-align top-align nowrap" sortable="true"
						sortProperty="name">
						<%
						  temp1 = (LeaderBoard)pageContext.getAttribute( "underConstructionLeaderBoards" );
						        underConstLBParamMap.put( "boardId", temp1.getId() );
						        underConstLBParamMap.put( "source", "admin" );
						        pageContext.setAttribute( "viewUrl", ClientStateUtils.generateEncodedLink( siteUrl, "/leaderBoardEditAction.do?method=prepareEdit", underConstLBParamMap ) );
						%>
						<c:url var="url" value="${viewUrl}" />
						<a href="<c:out value="${url}" />" class="crud-content-link"><c:out
								value="${underConstructionLeaderBoards.name}" />
						</a>
					</display:column>

					<display:column titleKey="leaderboard.admin.labels.FIRST_NAME"
						headerClass="crud-table-header-row"
						class="crud-content left-align top-align nowrap" sortable="true"
						sortProperty="name">
						<c:out value="${underConstructionLeaderBoards.user.firstName}" escapeXml="false"/>
					</display:column>
					<display:column titleKey="leaderboard.admin.labels.LAST_NAME"
						headerClass="crud-table-header-row"
						class="crud-content left-align top-align nowrap" sortable="true"
						sortProperty="name">
						<c:out value="${underConstructionLeaderBoards.user.lastName}" escapeXml="false"/>
					</display:column>

					<display:column titleKey="leaderboard.admin.labels.START_DATE"
						headerClass="crud-table-header-row"
						class="crud-content left-align top-align nowrap" sortable="true"
						sortProperty="name">
						<c:out value="${underConstructionLeaderBoards.displayUIStartDate}" />
					</display:column>

					<display:column titleKey="leaderboard.admin.labels.END_DATE"
						headerClass="crud-table-header-row"
						class="crud-content left-align top-align nowrap" sortable="true"
						sortProperty="name">
						<c:out value="${underConstructionLeaderBoards.displayUIEndDate}" />
					</display:column>

					<display:column titleKey="leaderboard.admin.labels.LAST_UPDATED"
						headerClass="crud-table-header-row"
						class="crud-content left-align top-align nowrap" sortable="true">
						<c:out
							value="${underConstructionLeaderBoards.displayLastUpdatedDate}" />
					</display:column>

					<beacon:authorize
						ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
						<display:column class="crud-content left-align top-align nowrap"
							titleKey="system.general.CRUD_REMOVE_LABEL"
							headerClass="crud-table-header-row">
							<input type="checkbox" name="deleteLeaderBoardsProgress"
								value="<c:out value="${underConstructionLeaderBoards.id}"/>">
						</display:column>
					</beacon:authorize>

				</display:table></td>
		</tr>
		<tr>
			<td></td>
			<td><html:submit styleClass="content-buttonstyle"
					onclick="return validateAndSubmit('progress')">
					<cms:contentText key="REMOVE_SELECTED" code="system.button" />
				</html:submit></td>
		</tr>


		<!-- Active Leader Boards -->
		<%
		  Map activeLBParamMap = new HashMap();
		    LeaderBoard temp2;
		%>
		<tr class="form-row-spacer">
			<td colspan="2" class="subheadline" align="left"><cms:contentText
					key="ACTIVE_LB_HEADER" code="leaderboard.admin.labels" />
			</td>
		</tr>

		<tr class="form-row-spacer">
			<td colspan="2"><display:table name="activeLeaderBoards"
					id="activeLeaderBoards" sort="list"
					requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
					<display:setProperty name="basic.msg.empty_list_row">
						<tr class="crud-content" align="left">
							<td colspan="{0}"><cms:contentText key="NOTHING_FOUND"
									code="system.errors" /></td>
						</tr>
					</display:setProperty>
					<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>

					<display:column titleKey="leaderboard.admin.labels.NAME"
						headerClass="crud-table-header-row"
						class="crud-content left-align top-align nowrap" sortable="true"
						sortProperty="name">
						<%
						  temp2 = (LeaderBoard)pageContext.getAttribute( "activeLeaderBoards" );
						        activeLBParamMap.put( "boardId", temp2.getId() );
						        activeLBParamMap.put( "source", "admin" );
						        pageContext.setAttribute( "viewUrl", ClientStateUtils.generateEncodedLink( siteUrl, "/leaderBoardEditAction.do?method=prepareEdit", activeLBParamMap ) );
						%>
						<c:url var="url" value="${viewUrl}" />
						<a href="<c:out value="${url}" />" class="crud-content-link"><c:out
								value="${activeLeaderBoards.name}" /> </a>
					</display:column>

					<display:column titleKey="leaderboard.admin.labels.FIRST_NAME"
						headerClass="crud-table-header-row"
						class="crud-content left-align top-align nowrap" sortable="true"
						sortProperty="name">
						<c:out value="${activeLeaderBoards.user.firstName}" escapeXml="false"/>
					</display:column>
					<display:column titleKey="leaderboard.admin.labels.LAST_NAME"
						headerClass="crud-table-header-row"
						class="crud-content left-align top-align nowrap" sortable="true"
						sortProperty="name">
						<c:out value="${activeLeaderBoards.user.lastName}" escapeXml="false"/>
					</display:column>

					<display:column titleKey="leaderboard.admin.labels.START_DATE"
						headerClass="crud-table-header-row"
						class="crud-content left-align top-align nowrap" sortable="true"
						sortProperty="name">
						<c:out value="${activeLeaderBoards.displayUIStartDate}" />
					</display:column>

					<display:column titleKey="leaderboard.admin.labels.END_DATE"
						headerClass="crud-table-header-row"
						class="crud-content left-align top-align nowrap" sortable="true"
						sortProperty="name">
						<c:out value="${activeLeaderBoards.displayUIEndDate}" />
					</display:column>

					<display:column titleKey="leaderboard.admin.labels.LAST_UPDATED"
						headerClass="crud-table-header-row"
						class="crud-content left-align top-align nowrap" sortable="true">
						<c:out value="${activeLeaderBoards.displayLastUpdatedDate}" />
					</display:column>

					<beacon:authorize
						ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
						<display:column class="crud-content left-align top-align nowrap"
							titleKey="system.general.CRUD_REMOVE_LABEL"
							headerClass="crud-table-header-row">
							<input type="checkbox" name="deleteLeaderBoardsActive"
								value="<c:out value="${activeLeaderBoards.id}"/>">
						</display:column>
					</beacon:authorize>
				</display:table></td>
		</tr>

		<tr>
			<td></td>
			<td><html:submit styleClass="content-buttonstyle"
					onclick="return validateAndSubmit('active')">
					<cms:contentText key="REMOVE_SELECTED" code="system.button" />
				</html:submit></td>
		</tr>


		<!-- Live records -->
		<%
		  Map parameterMap = new HashMap();
		    LeaderBoard temp;
		%>
		<tr class="form-row-spacer">
			<td colspan="2" class="subheadline" align="left"><cms:contentText
					key="LIVE_LB_HEADER" code="leaderboard.admin.labels" />
			</td>
		</tr>
		<tr class="form-row-spacer">
			<td colspan="2"><display:table name="liveLeaderBoards"
					id="liveLeaderBoard" sort="list"
					requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
					<display:setProperty name="basic.msg.empty_list_row">
						<tr class="crud-content" align="left">
							<td colspan="{0}"><cms:contentText key="NOTHING_FOUND"
									code="system.errors" /></td>
						</tr>
					</display:setProperty>
					<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
					
					
					<display:column titleKey="leaderboard.admin.labels.NAME" headerClass="crud-table-header-row"
						class="crud-content left-align top-align nowrap" sortable="true"
						sortProperty="name">
						<%
						  temp = (LeaderBoard)pageContext.getAttribute( "liveLeaderBoard" );
						        parameterMap.put( "boardId", temp.getId() );
						        parameterMap.put( "source", "admin" );
						        pageContext.setAttribute( "viewUrl", ClientStateUtils.generateEncodedLink( siteUrl, "/leaderBoardEditAction.do?method=prepareEdit", parameterMap ) );
						%>
						<c:url var="url" value="${viewUrl}" />
						<a href="<c:out value="${url}"/>" class="crud-content-link"> <c:out
								value="${liveLeaderBoard.name}" /> </a>
					</display:column>
					
					<display:column titleKey="leaderboard.admin.labels.FIRST_NAME"
						headerClass="crud-table-header-row"
						class="crud-content left-align top-align nowrap" sortable="true"
						sortProperty="name">
						<c:out value="${liveLeaderBoard.user.firstName}" escapeXml="false"/>
					</display:column>

					<display:column titleKey="leaderboard.admin.labels.LAST_NAME"
						headerClass="crud-table-header-row"
						class="crud-content left-align top-align nowrap" sortable="true"
						sortProperty="name">
						<c:out value="${liveLeaderBoard.user.lastName}" escapeXml="false"/>
					</display:column>

					<display:column titleKey="leaderboard.admin.labels.START_DATE"
						headerClass="crud-table-header-row"
						class="crud-content left-align top-align nowrap" sortable="true"
						sortProperty="name">
						<c:out value="${liveLeaderBoard.displayUIStartDate}" />
					</display:column>

					<display:column titleKey="leaderboard.admin.labels.END_DATE"
						headerClass="crud-table-header-row"
						class="crud-content left-align top-align nowrap" sortable="true"
						sortProperty="name">
						<c:out value="${liveLeaderBoard.displayUIEndDate}" />
					</display:column>

					<display:column titleKey="leaderboard.admin.labels.LAST_UPDATED"
						headerClass="crud-table-header-row"
						class="crud-content left-align top-align nowrap" sortable="true">
						<c:out
							value="${liveLeaderBoard.displayLastUpdatedDate}" />
					</display:column>
					
					<beacon:authorize
						ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
						<display:column class="crud-content left-align top-align nowrap"
							titleKey="system.general.CRUD_REMOVE_LABEL"
							headerClass="crud-table-header-row">
							<input type="checkbox" name="deleteLeaderBoardsLive"
								value="<c:out value="${liveLeaderBoard.id}"/>">
						</display:column>
					</beacon:authorize>

				</display:table></td>
		</tr>

		<tr>
			<td></td>
			<td><html:submit styleClass="content-buttonstyle"
					onclick="return validateAndSubmit('live')">
					<cms:contentText key="REMOVE_SELECTED" code="system.button" />
				</html:submit></td>
		</tr>
		<%--BUTTON ROWS --%>
		<tr class="form-buttonrow">
			<td>
				<table width="100%">
					<tr>
						<td align="left"><html:button property="view_list"
								styleClass="content-buttonstyle"
								onclick="callUrl('leaderBoardExpiredDisplay.do')">
								<cms:contentText key="EXPIRED_LB_HEADER"
									code="leaderboard.admin.labels" />
							</html:button></td>
						<td><html:button property="cancelBtn"
								styleClass="content-buttonstyle"
								onclick="callUrl('./homePage.do')">
								<cms:contentText key="CANCEL" code="system.button" />
							</html:button></td>
					</tr>
				</table> 
</html:form>