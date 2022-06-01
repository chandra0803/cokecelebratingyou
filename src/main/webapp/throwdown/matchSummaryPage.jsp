<!-- ======== MATCH PAGE ======== -->

<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@include file="/include/taglib.jspf"%>

<div id="matchPageView" class="page-content throwdownMatch" data-smack-talk-id="${matchBean.matchId}">

     <div class="page-topper">
        <div class="row-fluid">
            <div class="span6 match-page-topper-liner">

                <form action="<%=request.getContextPath()%>/throwdown/matchDetail.do" class="form-inline form-labels-inline" >
		    		<input type="hidden" name="method" value="detail"/>
                        <div class="control-group" id="controlMatchSelect">

                            <div class="controls">
                                <select id="promoSelect" name="promotionId" data-no-rankings="There are no promotions for this Match Filter.">
                                    <c:forEach items="${eligibleThrowdownPromotions}" var="promo">
        		                    <option value="${promo.promotion.id}">${promo.promotion.name}</option>
        		                    </c:forEach>
                                </select>

                            </div>

                        </div><!-- /.control-group -->
                </form>

            </div>
			<!--UET Added markup below here for rules button, please update cms keys -->
			<div class="span6">
                <a href="${matchBean.rulesUrl}" class="btn btn-primary rulesBtn"><cms:contentText key="RULES" code="leaderboard.label"/></a>
            </div>
        </div>
    </div><!-- /.page-topper -->

  <!-- Page Body -->
    <div class="row-fluid">
        <div class="span12">
            <div class="promotionInfo">
                <h4>${matchBean.promotion.name}</h4>
                <span><strong><cms:contentText key="START_DATE" code="participant.promotions"/></strong> <fmt:formatDate value="${matchBean.promotion.submissionStartDate }" pattern="${JstlDatePattern}" /></span>
                <span><strong><cms:contentText key="END_DATE" code="participant.promotions"/></strong> <fmt:formatDate value="${matchBean.promotion.submissionEndDate }" pattern="${JstlDatePattern}" /></span>
                <br/>
				<!--UET removed rules link -->
            </div>
            <c:if test="${matchBean.roundYetToStart == false && matchBean.roundCompleted == false}">
			   <c:set var="timeRemainingParts" value="${fn:split(matchBean.timeRemaining, ':')}" />
            <div class="td-promo-countdown" >
            <h4><cms:contentText key="TIME_REMAINING" code="participant.throwdownstats"/></h4>
                <ul>
					<li class="d"><span class="cd-digit">${timeRemainingParts[0]} </span><hr/><hr/><span class="cd-label"><cms:contentText key="DAYS" code="participant.throwdownstats"/></span></li>
					<li class="h"><span class="cd-digit"> ${timeRemainingParts[1]}</span><hr/><hr/><span class="cd-label"><cms:contentText key="HOURS" code="participant.throwdownstats"/></span></li>
					<li class="m"><span class="cd-digit">${timeRemainingParts[2]}</span><hr/><hr/><span class="cd-label"><cms:contentText key="MINUTES" code="participant.throwdownstats"/></span></li>
					<li class="s"><span class="cd-digit">${timeRemainingParts[3]}</span><hr/><hr/><span class="cd-label"><cms:contentText key="SECONDS" code="participant.throwdownstats"/></span></li>
				</ul>
            </div>
            </c:if>

			<c:if test="${matchBean.roundYetToStart == true}">
				<div class="round-start-end-message">
					<p><cms:contentText key="ROUND_STARTING_ON" code="participant.throwdownstats"/> <fmt:formatDate value="${matchBean.round.startDate}" pattern="${JstlDatePattern}" /></p>
				</div>
			</c:if>
			<c:if test="${matchBean.roundCompleted == true}">
				<div class="round-start-end-message">
					<p><cms:contentText key="ROUND_ENDED_ON" code="participant.throwdownstats"/> <fmt:formatDate value="${matchBean.round.endDate}" pattern="${JstlDatePattern}" /></p>
				</div>
			</c:if>

        </div>
	</div><!-- /.row-fluid -->

   	<div class="td-match-detail-container">
		<div class="row-fluid">
			<div class="span12">
				<div class="matchModel">

				</div>
			</div>
		</div>
	</div><!-- /.td-match-detail-container -->
	<c:if test="${matchBean.promotion.smackTalkAvailable == true}">
	<div class="td-match-smack-talk  smackTalk">
		<div class="row-fluid">
			<div class="span12">
				<div class="app-row" >
					<h4><cms:contentText key="SMACKTALK_THIS_MATCH" code="smacktalk.details"/></h4>

					<form class="form-inline smackTalkCommentForm" action="smackTalkComment.do?method=postComment">
                        <!-- data-smack-talk-id will be passed to JSON url along with any other data-* attrs -->
                        <textarea name="comment" class="comment-input commentInputTxt" placeholder="<cms:contentText key="ADD_SMACK_TALK" code="smacktalk.details" />" maxlength="500"></textarea>
                    </form>

					<div class="page-liner smackTalkItemsCont" >

						<div class="smackTalkItems">

						</div><!-- smackTalkItems -->

					<!-- shown when there are more smack talk itesm -->
                        <div class="app-row">
                            <p>
                                <a href="#" class="viewAllSmackTalks"  style="display: none">
                                    <cms:contentText key="VIEW_MORE" code="smacktalk.details" />
                                </a>
                            </p>
                        </div>

                    </div><!-- smackTalkItemsCont -->

                </div>
            </div>
        </div><!-- /.row-fluid -->
    </div> <!-- /.td-match-smack-talk -->
    </c:if>

</div><!-- /#matchPageView -->
<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>

    $(document).ready(function() {
	//G5.throwdown.promoId = "${matchBean.promotionId}";

	document.getElementById("promoSelect").value = "${matchBean.promotionId}";
	//var selectedPromotion = document.getElementById("selectedPromo");
	//selectedPromotion.innerHTML = "${matchBean.promotionName}";

	var matchId = "${matchBean.matchId}";

	//G5.props.URL_JSON_THROWDOWN_MATCHES = 'matchDetail.do?method=detailAjax&matchId='+ matchId +'&promotionId=';
	//Mini Profile PopUp  JSON
	G5.props.URL_JSON_PARTICIPANT_INFO = G5.props.URL_ROOT+'participantPublicProfile.do?method=populatePax';

    //G5.props.URL_JSON_SMACK_TALK_DETAIL='smackTalkMatchDetail.do?method=smackTalkDetail';

	var matchDetailsJson = ${matchDetailsJson};
    var smackTalkJson = ${smackTalkJson};
    //alert(smackTalkJson);
	//attach the view to an existing DOM element
	G5.views.throwdownMatchPageView = new ThrowdownMatchPageView({
	el: $('#matchPageView'),
	pageTitle: '<cms:contentText key="MATCH_DETAIL" code="participant.throwdown.promo.detail"/>',
	userRole : 'manager',
	matchDetailsJson: matchDetailsJson,
	smackTalkJson: smackTalkJson,
	matchId: matchId
	});

    });
</script>

<script type="text/template" id="throwdownMatchDetailsTpl">
     <%@ include file="/throwdown/matchDetailsInclude.jsp" %>
</script>
<script type="text/template" id="smackTalkItemTpl">
	<%@include file="/throwdown/smackTalkItem.jsp" %>
</script>
<script type="text/template" id="smackTalkCommentTpl">
	<%@include file="/throwdown/smackTalkComment.jsp" %>
</script>
