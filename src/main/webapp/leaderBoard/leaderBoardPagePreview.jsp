<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.ui.leaderBoard.LeaderBoardForm"%>
<%@ page import="com.biperf.core.ui.servlet.*"%>
<%@ page import="com.biperf.core.domain.leaderboard.LeaderboardParticipantView"%>
<%@ include file="/include/taglib.jspf"%>
<!-- ======== LEADERBOARD PAGE PREVIEW ======== -->

<div class="page-content leaderBoardPreviewPage" id="leaderboardPageView">
<!-- Page Body -->

<div class="row-fluid" >
	<div class="span12">
	<%
	    LeaderBoardForm tempForm = (LeaderBoardForm) request.getAttribute("leaderBoardForm");
		String tempLeaderBoardName = StripXSSUtil.stripXSS(tempForm.getLeaderBoardName());
	%>
	
        <h2><c:out value="<%=tempLeaderBoardName%>" /></h2>
    </div>
	<div class="span12">
		<html:form styleId="contentForm" method="post" action="leaderBoardSaveAction" styleClass="form-horizontal">
			<html:hidden property="leaderBoardId" />
			<html:hidden property="status" />
			<html:hidden property="source" />
			<html:hidden property="clientState" />
			<html:hidden property="cryptoPassword" />
			<html:hidden property="type" />
			<html:hidden property="method" value ="prepareEdit"/>
			
			<dl class="dl-horizontal leaderboard-details">
				<dt>
					<cms:contentText key="PROMOTION_NAME" code="leaderboard.label" />
				</dt>
				<dd>
					<c:out value="<%=tempLeaderBoardName%>" />
					<html:hidden property="leaderBoardName" />&nbsp;
				</dd>
				<dt>
					<cms:contentText key="PROMOTION_START_DATE"
						code="leaderboard.label" />
				</dt>
				<dd>
				<%					   
					String tempStartDate = StripXSSUtil.stripXSS(tempForm.getStartDate());	
				%>
				
					<c:out value="<%=tempStartDate%>" />
					<input type="hidden" name="startDate" value="<c:out value='${leaderBoardForm.startDate}'  escapeXml='true'/>"/>&nbsp;
				</dd>
				<dt>
					<cms:contentText key="PROMOTION_END_DATE" code="leaderboard.label" />
				</dt>
				<dd>
				
				<%					   
					String tempEndDate = StripXSSUtil.stripXSS(tempForm.getEndDate());	
				%>
					<c:out value="<%=tempEndDate%>" />
					<input type="hidden" name="endDate" value="<c:out value='${leaderBoardForm.endDate}' escapeXml='true'/>"/>&nbsp;
				</dd>
				<dt>
					<cms:contentText key="DISPLAY_END_DATE" code="leaderboard.label" />
				</dt>
				<dd>
				
				<%					   
					String tempLeaderBoardDisplayEndDate = StripXSSUtil.stripXSS(tempForm.getLeaderBoardDisplayEndDate());	
				%>
					<c:out value="<%=tempLeaderBoardDisplayEndDate%>" />
					<input type="hidden" name="leaderBoardDisplayEndDate" value="<c:out value='${leaderBoardForm.leaderBoardDisplayEndDate}'  escapeXml='true'/>"/>
					
					&nbsp;
				</dd>
				<dt>
					<cms:contentText key="ACTIVITY_TITLE" code="leaderboard.label" />
				</dt>
				<dd>
				<%					   
					String tempActivityTitle = StripXSSUtil.stripXSS(tempForm.getActivityTitle());	
				%>
					<c:out value="<%=tempActivityTitle%>" />
					<html:hidden property="activityTitle" />&nbsp;
				</dd>
				<dt>
					<cms:contentText key="SORT_ORDER" code="leaderboard.label" />
				</dt>
				<dd>
					<c:choose>
						<c:when test="${leaderBoardForm.sortOrder == 'desc'}">
							<cms:contentText key="HIGH_LOW" code="leaderboard.label" />
						</c:when>
						<c:otherwise>
							<cms:contentText key="LOW_HIGH" code="leaderboard.label" />
						</c:otherwise>
					</c:choose>
					<html:hidden property="sortOrder" />&nbsp;
				</dd>
				<dt>
					<cms:contentText key="ACTIVITY_DATE" code="leaderboard.label" />
				</dt>
				<dd>
				    <%					   
					   String tempActivityDate = StripXSSUtil.stripXSS(tempForm.getActivityDate());	
					%>
					<c:out value="<%=tempActivityDate%>" />
						<input type="hidden" name="activityDate" value="<c:out value='${leaderBoardForm.activityDate}'  escapeXml='true'/>"/>
					&nbsp;
				</dd>

			</dl>
			<!-- /.dl-horizontal -->


			<div class="leaderboard-rules">
				<h3>
					<cms:contentText key="RULES" code="leaderboard.label" />
				</h3>
				<p>
					<%					   
					   String tempLeaderBoardRulesText = StripXSSUtil.stripXSS(tempForm.getLeaderBoardRulesText());	
					%>
					<c:out escapeXml="false" value="<%=tempLeaderBoardRulesText%>" />					
					<input type="hidden" name="leaderBoardRulesText" value="<c:out value='${leaderBoardForm.leaderBoardRulesText}' escapeXml='true'/>"/>
				</p>
			</div>

			<h3>
					<cms:contentText key="PARTICIPANTS" code="leaderboard.label" />
			</h3>

			<table class="table table-striped">

				<thead>
					<tr>
						<th class="participant"><cms:contentText key="PARTICIPANTS"
								code="leaderboard.label" />
						</th>
						<th class="newScore number"><cms:contentText
								key="CURR_ACTIVITY" code="leaderboard.label" />
						</th>
					</tr>
				</thead>

				<tbody>
					<c:forEach var="pax" items="${leaderBoardForm.refPaxs}"
						varStatus="status">
						<html:hidden property="participants[${status.index}].id"
							value="${pax.id}" />
						<input type="hidden" name="participants[${status.index}].firstName"
							value="<c:out value='${pax.firstName}'  escapeXml='true'/>" />
						<input type="hidden" name="participants[${status.index}].lastName"
							value="<c:out value='${pax.lastName}'  escapeXml='true'/>" />
						<html:hidden property="participants[${status.index}].score"
							value="${pax.score}" />
						<html:hidden property="participants[${status.index}].newScore"
							value="${pax.newScore}" />
						<tr>
							<td class="participant"><a class="participant-popover" href="#" data-participant-ids="[<c:out value="${pax.id}" />]">
							 <%
							 	LeaderboardParticipantView tempLDPax = (LeaderboardParticipantView) pageContext.getAttribute( "pax" );
							 	String lastName = StripXSSUtil.stripXSS(tempLDPax.getLastName());
							 	String firstName = StripXSSUtil.stripXSS(tempLDPax.getFirstName());
							 	String newScore = StripXSSUtil.stripXSS(tempLDPax.getNewScore().toString());
							 %>
                                <c:out value="<%=lastName%>" escapeXml="false"/>, <c:out value="<%=firstName%>" escapeXml="false"/>
                            </a></td>
							<%--<td><c:out value="${pax.orgName}" />
							</td>
							<td><c:out value="${pax.departmentName}" />
							</td>
							<td><c:out value="${pax.jobName}" />
							</td>
							<td>
								<img src="<%=RequestUtils.getBaseURI( request )%>/assets/img/flags/<c:out value='${pax.countryCode}'/>.png" />
							</td>--%>
							<td class="newScore number"><c:out value="<%=newScore%>" />
							</td>
						</tr>

					</c:forEach>
				</tbody>

			</table>

			<html:hidden styleId="notifyPaxChecked" property="notifyPaxChecked" value="${leaderBoardForm.notifyPaxChecked}"/>
			<div class="leaderboard-notify">
				<h3>
					<cms:contentText key="MESG_TO_PAX" code="leaderboard.label" />
				</h3>
				<p>
					<%					    
					   String tempNotifyMessage = StripXSSUtil.stripXSS(tempForm.getNotifyMessage(  ));	
					%>
					<c:out escapeXml="false" value="<%=tempNotifyMessage%>" />
					<input type="hidden" name="notifyMessage" value="<c:out value='${leaderBoardForm.notifyMessage}' escapeXml='true'/>"/>&nbsp;
					
				</p>
			</div>

			<!--     </div>

</div>

<div class="row-fluid">

    <div class="span12"> -->


			<fieldset id="leaderboardFieldsetActions" class="form-actions">

				<button type="submit" id="leaderboardButtonSubmit"
					name="button" value="leaderboardButtonSubmit"
					formaction="<%=RequestUtils.getBaseURI( request )%>/leaderBoardSaveAction.do?method=createOrUpdate"
					class="btn btn-primary">
					<cms:contentText key="SUBMIT" code="leaderboard.label" />
				</button>
				<html:submit styleId="leaderboardButtonEdit" styleClass="btn" >
					<cms:contentText key="EDIT" code="leaderboard.label" />
				</html:submit>

			</fieldset>
			<!-- /#leaderboardFieldsetActions -->

			<!-- /#leaderboardFormPreview -->
		</html:form>
	</div>
	<!-- /.span12 -->

</div>
<!-- /.row-fluid -->

</div>
<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
	$(document).ready(function() {

		var lbp;
		//attach the view to an existing DOM element

		//Mini Profile PopUp JSON
        G5.props.URL_JSON_PARTICIPANT_INFO = 'participantPublicProfile.do?method=populatePax';

        //Mini Profile PopUp Follow Unfollow Pax JSON
        G5.props.URL_JSON_PARTICIPANT_FOLLOW = G5.props.URL_ROOT+'publicRecognitionFollowUnFollowPax.do?method=followUnFollowPax';

		var lbp = new LeaderboardPageCreateEditCopyView({
			el : $('#leaderboardPageView'),
			mode : 'preview',
            noSidebar : ${isDelegate},
            noGlobalNav : ${isDelegate},
			pageNav : {
	            back : {
	                text : '<cms:contentText key="BACK" code="system.button" />',
	                formId : 'contentForm',
                    formAction : '<%=RequestUtils.getBaseURI(request)%>/leaderBoardEditAction.do?method=prepareEdit'
	            },
	            home : {
	                text : '<cms:contentText key="HOME" code="system.general" />',
	                url : '${pageContext.request.contextPath}/homePage.do'
	            }
	        },
			pageTitle : '<cms:contentText key="CREATE_TITLE" code="leaderboard.label" />'
		});
	});
</script>

<%@include file="/submitrecognition/easy/flipSide.jsp"%>
