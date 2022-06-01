<%@ page import="java.util.*"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>
<%@ page import="com.biperf.core.value.forum.ForumTopicValueBean"%>
<%@ page import="com.biperf.core.domain.forum.ForumTopic"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.utils.DateUtils"%>

<script type="text/javascript">

function validateAndSubmit( )
{
	var checked = $("input[name=deleteForumTopics]:checked").length > 0;   
	var errorMessage='';
	var confirmationMessage='<cms:contentText key="CONFIRMATION_MESSAGE" code="forum.library" />';

	if(checked)
	{
		if(confirm(confirmationMessage))
		{
			setDispatchAndSubmit('remove');
			return true;
		}
		else
		{
			return false;
		}
	}
	else
	{
		errorMessage='<cms:contentText key="SELECT_ATLEAST_ONE_FORUM_TOPIC" code="forum.library" />';
		$("#messageDiv").html(errorMessage);
    	$("#messageDiv").show();
		return false;
	}
}
</script>

<script type="text/javascript">

$(document).ready(function(){
	$("#messageDiv").hide();
});

</script>



<html:form styleId="contentForm" action="forumTopicListMaintain">
	<html:hidden property="method" />
	<table border="0" cellpadding="10" cellspacing="0" width="100%">
		<tr>
			<td>
			<span class="headline"><cms:contentText key="TITLE" code="forum.library" /></span> 
			<span id="quicklink-add"></span> 
			    <script type="text/javascript"> quicklink_display_add( '<cms:contentText key="TITLE" code="forum.library"/>', '<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>');</script> 
			<br/> 
			<br/> 
			<span class="content-instruction"> <cms:contentText key="INSTRUCTION" code="forum.library" />
			</span> 
			<br/> 
			<br/>
			<div id="messageDiv" class="error">
	        </div>
			<cms:errors />
				<table width="50%">
					<tr>
						<td align="right">
							<%
							  Map parameterMap = new HashMap();
							  ForumTopicValueBean temp;
							%> 
							<%-- FORUM TOPICS --%>
							<table>
								<beacon:authorize
									ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
									<tr class="form-row-spacer">
										<td align="left">
											<html:button property="createForumTopic" styleClass="content-buttonstyle" onclick="callUrl('forumTopicMaintainDisplay.do?method=prepareCreate')">
												<cms:contentText code="forum.library" key="CREATE_FORUM_TOPIC" />
											</html:button>
										</td>
									</tr>
								</beacon:authorize>
								<tr class="form-row-spacer">
									<td align="left"><br/> <span class="headline"><cms:contentText key="HEADER" code="forum.library" /></span></td>
								</tr>
								<tr class="form-row-spacer">
									<td align="right">
									<display:table defaultorder="ascending" name="forumTopicList" id="forumTopic" pagesize="10" sort="list" requestURI="<%=RequestUtils.getOriginalRequestURI( request )%>">
											<display:setProperty name="basic.msg.empty_list_row">
												<tr class="crud-content" align="left">
													<td colspan="{0}">
													<cms:contentText key="NOTHING_FOUND" code="system.errors" />
													</td>
												</tr>
											</display:setProperty>
											<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
											<display:column titleKey="forum.library.NAME" headerClass="crud-table-header-row" class="crud-content left-align nowrap" sortProperty="topicCmAssetCode" sortable="true">
											<%
												  temp = (ForumTopicValueBean)pageContext.getAttribute( "forumTopic" );
												        parameterMap.put( "id", temp.getId() );
												        parameterMap.put( "topicCmAssetCode", temp.getTopicCmAssetCode() );
												        parameterMap.put( "sortOrder", temp.getSortOrder() );
												        parameterMap.put( "stickyStartDate", temp.getStickyStartDate(  ) );
												        parameterMap.put( "stickyEndDate", temp.getStickyEndDate(  ) );
												        parameterMap.put( "audienceType", temp.getAudienceName() );
												%>
												<%
												  pageContext.setAttribute( "editUrl", ClientStateUtils.generateEncodedLink( "", "forumTopicMaintainDisplay.do?method=prepareUpdate", parameterMap ) );
												%>
												<a href="<c:out value="${editUrl}"/>" class="crud-content-link"> <c:out value="${forumTopic.topicNameFromCM}" /> </a>
											</display:column>
											<display:column titleKey="forum.library.DISCUSSIONS" headerClass="crud-table-header-row" class="crud-content left-align nowrap">
											<c:out value="${forumTopic.discussionCount}" /><br>
												<%
												  pageContext.setAttribute( "editUrl", ClientStateUtils.generateEncodedLink( "", "forumDiscussionListMaintainDisplay.do?method=prepareUpdate", parameterMap ) );
												%>
												<a href="<c:out value="${editUrl}"/>" class="crud-content-link"><cms:contentText key="EDIT" code="system.button" /></a>
											</display:column>
											<display:column titleKey="forum.library.AUDIENCE" headerClass="crud-table-header-row" class="crud-content left-align nowrap">
												<c:out value="${forumTopic.audienceName}" />
											</display:column>
											<display:column titleKey="forum.library.STICKY" headerClass="crud-table-header-row" class="crud-content left-align nowrap">
											<%
											temp = (ForumTopicValueBean)pageContext.getAttribute( "forumTopic" );
											if( temp.getStickyEndDate( ) != null )
											{
											if( temp.getStickyEndDate( ).compareTo( DateUtils.getCurrentDateTrimmed() ) >= 0 )
											{
											%>
											<center><cms:contentText key="YES" code="system.common.labels" /></center>
											<%
											}
											else
											{
											%>
											<center><cms:contentText key="NO" code="system.common.labels" /></center>
											<%
											}
											}
											else
											{
											%>
											<center><cms:contentText key="NO" code="system.common.labels" /></center>
											<%
											}
											%>
											</display:column>
											<beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
												<display:column class="center-align" titleKey="system.general.CRUD_REMOVE_LABEL" headerClass="crud-table-header-row">
													<input type="checkbox" name="deleteForumTopics" id="deleteForumTopics"  value="<c:out value="${forumTopic.id}"/>">
												</display:column>
											</beacon:authorize>
										</display:table>
										</td>
								</tr>
							<beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
									<tr class="form-row-spacer">
										<td align="right">
										<html:submit styleClass="content-buttonstyle" onclick="return validateAndSubmit()">
												<cms:contentText key="REMOVE_SELECTED" code="system.button" />
										</html:submit>
										</td>
								</tr>
							</beacon:authorize>
						</table>
					</td>
				</tr>
			</table>
			</td>
		</tr>
	</table>
</html:form>
