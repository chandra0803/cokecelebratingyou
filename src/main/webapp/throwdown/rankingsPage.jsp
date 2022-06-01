<%@ include file="/include/taglib.jspf"%>

<!-- ======== RANKINGS PAGE ======== -->
<div id="rankingsPageView" class="page-content throwdownRankings">
    <div class="page-topper">
        <div class="row-fluid">
            <div class="span6 rankings-topper-liner">
                <form id="rankingForm" class="form-inline form-labels-inline" >
                <input type="hidden" name="method" value="detail"/> <!-- Java side: Add type and action here, this form will submit when the user changes the promotion -->
                        <div class="control-group" id="controlRankingsSelect">
                            <div class="controls"> <!--Java side: build this select list dynamicly, the default selected drop down must have a selected tag-->
                                <select id="promotionSelect" name="promotionId" data-no-rankings="There are no promotions for this Match Filter.">
                                   <c:forEach items="${eligibleThrowdownPromotions}" var="promo">
										<option value="${promo.promotion.id}">${promo.promotion.name}</option>
									</c:forEach>
                                </select>
                            </div>
                        </div> <!-- /#controlRankingsSelect -->
                </form>
            </div><!-- /.rankings-topper-liner -->

			<!--UET Added markup below here for rules button, please update cms keys -->
			<div class="span6">
                <a href="${rulesUrl}" class="btn btn-primary rulesBtn"><cms:contentText key="RULES" code="leaderboard.label"/></a>
            </div>
        </div>
    </div><!-- /.page-topper -->

    <!-- Page Body -->
    <div class="row-fluid">
        <div class="span12">
            <form class="form-inline formRankingsSetSelect">
            <input type="hidden" name="method" value="detail"/>

                <div class="control-group" id="controlRankingsOrgSelect">
                    <div class="controls">
                    	<cms:contentText key="NODE_LABEL" code="promotion.stackrank.history"/><br/>
                        <select id="rankingsOrgSelect" name="nodeTypeId">
                            <c:forEach items="${nodeTypes}" var="nodeType">
        		                    <option value="${nodeType.id}">${nodeType.nodeTypeName}</option>
        		            </c:forEach>
                        </select>
                    </div>
                </div><!-- /.control-group -->

                <div class="control-group" id="controlRankingsSetSelect">
                    <div class="controls">
                        <select id="rankingsSetSelect" name="nodeId">
                            <!-- dynamic -->
                        </select>
                    </div>
                </div><!-- /.control-group -->

            </form>

            <div class="selectedRankingsView">
                <!-- NOTE: dynamically loaded using template rankingsSet.html -->
            </div>
        </div>
    </div><!-- /.row-fluid -->
</div><!-- /#rankingsPageView -->
<script>
$(document).ready(function(){


	//specific rankings json data
	G5.throwdown.promoId = "${promotionId}";

	document.getElementById("promotionSelect").value = "${promotionId}";
	/* var selectedPromotion = document.getElementById("selectedPromo");
	selectedPromotion.innerHTML = "${promotionName}"; */

	var nodeTypeId = "${nodeTypeId}";
	document.getElementById("rankingsOrgSelect").value = "${nodeTypeId}";
	
	var nodeId = "${nodeId}";	
	document.getElementById("rankingsSetSelect").value = "${nodeId}";	
	
	G5.props.URL_JSON_THROWDOWN_LEADERBOARD_MODEL = 'rankingsDetail.do?method=detailPage&nodeTypeId='+ nodeTypeId +'&nodeId='+ nodeId +'&promotionId=';
	
	G5.props.URL_JSON_PARTICIPANT_INFO = G5.props.URL_ROOT+'participantPublicProfile.do?method=populatePax';	
	
	G5.props.URL_JSON_PARTICIPANT_FOLLOW = G5.props.URL_ROOT+'publicRecognitionFollowUnFollowPax.do?method=followUnFollowPax';
	
    //attach the view to an existing DOM element
    throwdownRankingsPageView = new ThrowdownRankingsPageView({
        el : $('#rankingsPageView'),
        userRole : 'manager', // 'manager' or 'participant'
        //showRankings : 1234,
        pageTitle : '<cms:contentText key="THROWDOWN_RANKINGS_PAGE" code="home.rail"/>'
    });

});

function submitRankingForm()
{
	document.getElementById("rankingForm").submit();
}

</script>

<script type="text/template" id="throwdownRankingsModelTpl">
  <%@include file="/throwdown/rankingsModel.jsp" %>
</script>
<%@include file="/submitrecognition/easy/flipSide.jsp" %>