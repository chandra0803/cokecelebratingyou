<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>
<script type="text/template" id="throwdownRankingsModuleTpl">
<!-- ======== RANKINGS MODULE ======== -->

<div class="module-liner">

	<beacon:authorize ifNotGranted="LOGIN_AS">
    <div class="rankingsModuleTop">
        <h2 class="rankingModuleTitle"><cms:contentText key="THROWDOWN_RANKINGS" code="home.rail"/></h2>
	 	<a href="{{ rankingsUrl }}" class="visitAppBtn">
        		<i class="icon-arrow-2-circle-right"></i>
    		</a>
    </div>
	</beacon:authorize>

    <div class="wide-view">

        <!-- Page Header  -->

        <!-- Carousel items -->
        <div class="rankingsSlides" data-cycle-legend-style="dots">
            <!-- NOTE: dynamically loaded using template throwdownRankingsModel.html -->
        </div>

    </div>

    <div class="title-icon-view">
        <h3><cms:contentText key="THROWDOWN_RANKINGS" code="home.rail"/></h3>
    </div>

</div>
</script>

<script>
	$(document).ready(function() {

		//specific rankings json data
		//G5.props.URL_JSON_THROWDOWN_LEADERBOARD_SETS  = G5.props.URL_ROOT+'rankingsSummary.do?method=fetchRankings&promotionId=';
		G5.props.URL_JSON_THROWDOWN_LEADERBOARD_MODEL = G5.props.URL_ROOT+'rankingsSummary.do?method=fetchRankings&promotionId=';

		G5.props.URL_JSON_PARTICIPANT_FOLLOW = G5.props.URL_ROOT+'publicRecognitionFollowUnFollowPax.do?method=followUnFollowPax';

	});
</script>

<script type="text/template" id="throwdownRankingsModelTpl">
  <%@include file="/throwdown/rankingsModel.jsp" %>
</script>
<%@include file="/submitrecognition/easy/flipSide.jsp" %>
