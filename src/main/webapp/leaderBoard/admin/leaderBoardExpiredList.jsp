<%--UI REFACTORED --%>
<%@page import="com.biperf.core.utils.BeanLocator"%>
<%@page import="com.biperf.core.service.system.SystemVariableService"%>
<%@page import="com.biperf.core.domain.leaderboard.LeaderBoard"%>
<%@ page import="java.util.*"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<script type="text/javascript">
<!--
	function callUrl(urlToCall) {
		window.location = urlToCall;
	}

	function filterPromotionList() {
		var url = 'leaderBoardList.do';
		var urlToCall = url;

		window.location = urlToCall;
	}
//-->
</script>
<html>
<body>
	<table border="0" cellpadding="10" cellspacing="0" width="100%">
		<tr>
			<td><span class="headline"><cms:contentText
						key="LB_LIST_HEADER" code="leaderboard.admin.labels" />
			</span>
			</td>
		</tr>
		<br>
		<br>
		<br>
		<%
		  Map expiredLBParamMap = new HashMap();
		    LeaderBoard temp1;
		    String siteUrl = ( (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME ) ).getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
		%>
		
		<!-- Under Construction Leader Boards -->
		<tr class="form-row-spacer">
			<td colspan="2" class="subheadline" align="left"><cms:contentText
					key="EXPIRED_LB_HEADER" code="leaderboard.admin.labels" /></td>
		</tr>
		<tr class="form-row-spacer">
			<td colspan="2"><display:table name="expiredList"
					id="expiredList" sort="list"
					requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
					<display:setProperty name="basic.msg.empty_list_row">
						<tr class="crud-content" align="left">
							<td colspan="{0}"><cms:contentText key="NOTHING_FOUND"
									code="system.errors" />
							</td>
						</tr>
					</display:setProperty>
					<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>

					<display:column titleKey="leaderboard.admin.labels.NAME"
						headerClass="crud-table-header-row"
						class="crud-content left-align top-align nowrap" sortable="true"
						sortProperty="name">
						<%
						  temp1 = (LeaderBoard)pageContext.getAttribute( "expiredList" );
								expiredLBParamMap.put( "boardId", temp1.getId() );
								expiredLBParamMap.put( "source", "admin" );
						        pageContext.setAttribute( "viewUrl", ClientStateUtils.generateEncodedLink( siteUrl, "/leaderBoardEditAction.do?method=prepareEdit", expiredLBParamMap ) );
						%>
						<c:url var="url" value="${viewUrl}" />
						<a href="<c:out value="${url}" />" class="crud-content-link">
							<c:out value="${expiredList.name}" />
						</a>
					</display:column>

					<display:column titleKey="leaderboard.admin.labels.FIRST_NAME"
						headerClass="crud-table-header-row"
						class="crud-content left-align top-align nowrap" sortable="true"
						sortProperty="name">
						<c:out value="${expiredList.user.firstName}" escapeXml="false"/>
					</display:column>

					<display:column titleKey="leaderboard.admin.labels.LAST_NAME"
						headerClass="crud-table-header-row"
						class="crud-content left-align top-align nowrap" sortable="true"
						sortProperty="name">
						<c:out value="${expiredList.user.lastName}" escapeXml="false"/>
					</display:column>

					<display:column titleKey="leaderboard.admin.labels.START_DATE"
						headerClass="crud-table-header-row"
						class="crud-content left-align top-align nowrap" sortable="true"
						sortProperty="name">
						<c:out value="${expiredList.displayUIStartDate}" />
					</display:column>

					<display:column titleKey="leaderboard.admin.labels.END_DATE"
						headerClass="crud-table-header-row"
						class="crud-content left-align top-align nowrap" sortable="true"
						sortProperty="name">
						<c:out value="${expiredList.displayUIEndDate}" />
					</display:column>

					<display:column titleKey="leaderboard.admin.labels.LAST_UPDATED"
						headerClass="crud-table-header-row"
						class="crud-content left-align top-align nowrap" sortable="true">
						<c:out value="${expiredList.displayLastUpdatedDate}" />
					</display:column>
				</display:table>
			</td>
		</tr>
		<tr class="form-buttonrow">
			<td colspan="2" align="center"><html:button property="back"
					styleClass="content-buttonstyle"
					onclick="callUrl('leaderBoardsList.do')">
					<cms:contentText key="BACK_TO_LEADERBOARD"
						code="leaderboard.admin.labels" />
				</html:button>
			</td>
		</tr>
	</table>
</body>
</html>