<%@ include file="/include/taglib.jspf"%>
<c:if test="${displayLeaderBoard}">
<!--  LEADERBOARD MODULE -->
<script type="text/template" id="leaderboardModuleTpl">

<!-- ======== LEADERBOARD MODULE ======== -->
<div class="module-liner">
    <div class="module-content">
        <!-- Carousel items -->
        <div class="leaderboardModel">
            <h3 class="module-title">
                <cms:contentText key="LEADER_BOARD_HEADER" code="leaderboard.label" />
            </h3>
        </div>

        <div class="module-actions">
            <a href="leaderBoardsDetailPage.do?method=getUserRole"><cms:contentText key="ALL_LEADER_BOARDS" code="leaderboard.label" /></a>
        </div>
    </div><!-- /.module-content -->
</div><!-- /.module-liner -->
</script>

<script>
	$(document).ready(function() {

		//specific leaderboard json data
		G5.props.URL_JSON_LEADERBOARD_MODEL = G5.props.URL_ROOT+'leaderBoardDetail.do?method=fetchLeaderBoardByIdForTile';

	});
</script>

<script type="text/template" id="leaderboardModelTpl">
  <%@include file="/leaderBoard/leaderBoardModel.jsp" %>
</script>
</c:if>
