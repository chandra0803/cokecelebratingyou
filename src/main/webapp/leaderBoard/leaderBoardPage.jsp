<%@ include file="/include/taglib.jspf"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>
<%@ page import="com.biperf.core.service.system.SystemVariableService" %>
<%@ page import="com.biperf.core.utils.*" %>

		<%
		  Map createLBNew = new HashMap();
		    String siteUrl = ( (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME ) ).getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
		    createLBNew.put( "source", "detail" );
		    createLBNew.put( "type", "create" );
	        pageContext.setAttribute( "viewUrl", ClientStateUtils.generateEncodedLink( siteUrl, "/leaderBoardAction.do?method=prepareCreate", createLBNew ) );
%>

<div id="leaderboardPageView" class="page-content">

    <div class="page-topper">
        <div class="row-fluid">
            <div class="span12 leaderboard-topper-liner">

                <form class="form-inline form-labels-inline">

                    <fieldset>

                        <div class="control-group" id="controlLeaderboardSetSelect">

                            <label class="control-label" for="leaderboardSetSelect"><cms:contentText
								key="LEADER_BOARD_SETS" code="leaderboard.label" /></label>

                            <div class="controls">
                                <select id="leaderboardSetSelect">
                                    <!-- dynamic -->
                                </select>
                            </div>

                        </div><!-- /.control-group -->


                        <div class="control-group" id="controlLeaderboardSelect">

                            <label class="control-label" for="leaderboardSelect"><cms:contentText
								key="LEADERBOARDS" code="leaderboard.label" /></label>

                            <div class="controls">
                                <select id="leaderboardSelect" data-no-leaderboards="<cms:contentText key="NO_LEADERBOARD"
																						code="leaderboard.label" />">
                                    <!-- dynamic -->
                                </select>
                            </div>

                        </div>
							<beacon:authorize ifNotGranted="LOGIN_AS">
                        		<div class="control-group control-group-createnew">
									<beacon:authorize ifAnyGranted="MANAGER,BI_ADMIN" >
										<a href=${viewUrl} class="createnew btn btn-primary"> <cms:contentText key="CREATE_NEW" code="leaderboard.label" /> <i class="icon-plus-circle"></i></a>
									</beacon:authorize>
                        		</div>
							</beacon:authorize>
                    </fieldset>

                </form>

            </div>
        </div>
    </div><!-- /.page-topper -->

    <!-- Page Body -->
    <div class="row-fluid">
        <div class="span12">
            <div class="leaderboardModel">
                <!-- NOTE: dynamically loaded using template leaderboardSet.html -->
            </div>
        </div>
    </div>

</div><!-- /#leaderboardPageView -->

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
	$(document).ready(function() {

		//attach the view to an existing DOM element
		G5.props.URL_JSON_LEADERBOARD_SETS = 'leaderBoardDetail.do?method=fetchLeaderBoardsForDetailPage';

		//specific leaderboard json data
		G5.props.URL_JSON_LEADERBOARD_MODEL = G5.props.URL_ROOT+'leaderBoardDetail.do?method=fetchLeaderBoardByIdForDetail';

		//Mini Profile PopUp JSON
		G5.props.URL_JSON_PARTICIPANT_INFO = 'participantPublicProfile.do?method=populatePax';

		//Mini Profile PopUp Follow Unfollow Pax JSON
        G5.props.URL_JSON_PARTICIPANT_FOLLOW = G5.props.URL_ROOT+'publicRecognitionFollowUnFollowPax.do?method=followUnFollowPax';

		G5.views.leaderboardPageView = new LeaderboardPageView({
			el : $('#leaderboardPageView'),
      userRole : '${role}', // 'manager' or 'participant'
      //showLeaderboard : 1234,
      pageTitle : '<cms:contentText key="LEADER_BOARD_HEADER" code="leaderboard.label" />'
		});
	});
</script>

<script type="text/template" id="leaderboardModelTpl">
    <%@include file="/leaderBoard/leaderBoardModel.jsp" %>
</script>

<%@include file="/submitrecognition/easy/flipSide.jsp" %>

<c:if test="${not empty leaderBoard}">
	<tiles:insert definition="leaderBoard.create.success.confirmation.modal" flush="true" ignore="true" />
</c:if>
