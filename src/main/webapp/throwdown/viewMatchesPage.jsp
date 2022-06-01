<%@page import="com.biperf.core.utils.DateUtils"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<!-- ======== THROWDOWN STANDINGS PAGE ======== -->
<div id="throwdownStandingsPage" class="page-content">

    <div class="page-topper">
        <div class="row-fluid">
            <div class="span6 rankings-topper-liner">
                <form id="mathcesForm" class="form-inline form-labels-inline" onsubmit="setSelectedTabForPromoChange(); return true;">
                <input type="hidden" name="method" value="detail"/>
                <input type="hidden" id="promoselectedTab" name="currentSelectedTab" value=""/>

                        <div class="control-group" id="controlMatchSelect">
                            <div class="controls">
                                <select id="promotionSelect" name="promotionId" data-no-rankings="There are no promotions for this Match Filter.">
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
                <a href="${rulesUrl}" class="btn btn-primary rulesBtn"><cms:contentText key="RULES" code="leaderboard.label"/></a>
            </div>
        </div>
    </div><!-- /.page-topper -->

    <div class="standingsPromoInfo">
        <div class="row-fluid">
            <div class="span8">
                <h2><c:out	value="${promotionName}" /></h2>
                <p class="startdate">
                   <strong><cms:contentText key="START_DATE" code="participant.promotions" />:</strong>
                   <span><fmt:formatDate value="${promotionStartDate}" type="date" pattern="${JstlDatePattern}" /></span> 
                </p>
                <p class="endDate">
                	<strong><cms:contentText key="END_DATE"	code="participant.promotions" />:</strong> 
					<span><fmt:formatDate value="${promotionEndDate}" type="date" pattern="${JstlDatePattern}" /></span>
				</p>
            </div>
            
            <div class="span4">
                <p><cms:contentText key="DISPLAY_MATCHES" code="promotion.throwdown.viewMatches" /></p>
                <form class="form-inline form-labels-inline" onsubmit="setSelectedTabForTypeChange(); return true;">
                <input type="hidden" name="method" value="detail"/>
                <input type="hidden" id="typeselectedTab" name="currentSelectedTab" value=""/>
                    <fieldset>
		                <div class="controls">
		                    <select id="matchesSelect" name="matchFilterName" data-no-rankings="There are no promotions for this Match Filter.">
		                        <option value="all" selected="selected"><cms:contentText key="ALL" code="promotion.throwdown.viewMatches" /> </option>
								<option value="mine"><cms:contentText key="MINE" code="promotion.throwdown.viewMatches" /> </option>
		                    </select>
		                </div>
		            </fieldset>
		        </form>
            </div>
        </div><!-- /.row-fluid -->

        <div class="row-fluid">
            <div class="span12" id="standingsMatchesTabs">
                <ul class="nav nav-tabs">
                	<li class="tabMatches active"><a href="#Matches" id="anchorMatches" data-toggle="tab"><cms:contentText key="MATCHES" code="participant.throwdownstats" /></a></li>
	                <li class="tabStandings"><a href="#Standings" id="anchorStandings" data-toggle="tab"><cms:contentText key="STANDINGS" code="home.rail"/></a></li>
                </ul>
            </div><!-- /#standingsMatchesTabs -->
        </div><!-- /.row-fluid -->
    </div><!-- /.standingsPromoInfo -->

     <div class="tab-content">
        <div class="tab-pane active" id="Matches">
            <div class="row-fluid" id="matchesTab">
                <div class="span12">

                    <div class="roundPagination pagination pagination-center paginationControls first"></div>
                    <!--subTpl.roundPaginationTpl=
                            <ul>

                                <li class="prev {{#eq currentRound "1"}} disabled {{/eq}}">
                                    <a>&#171;</a>
                                </li>

                                <li>
                                    <p><cms:contentText key="ROUND" code="promotion.stackrank.history" /> <span class="your-round">{{currentRound}}</span> <cms:contentText key="OF" code="promotion.stackrank.history" /> <span class="round-total">{{totalRounds}}</span></p>

                                     <p class= "round-dates">
                                    {{#if roundStartDate}}
                                        {{roundStartDate}} - {{roundEndDate}}
                                    {{/if}}
                                    </p>
                                </li>

                                <li class="next {{#eq totalRounds currentRound}} disabled {{/eq}}">
                                    <a>&#187;</a>
                                </li>

                            </ul>

                        subTpl-->

                    <div class="allMatchesPagination pagination pagination-right paginationControls first"></div>

                    <div class="allMatchesTable"></div>

                    <!-- Developers Note: Please do not add any additional whitespace in the below table as
                    that will have an impact on the spacing  and its view on the page-->
                    <script id="throwdownAllMatchesTableTpl" type="text/x-handlebars-template">
					  {{#if roundScheduled}}
						 {{#if doIHaveMatch}}
                        <table id="allMatchesTable" class="table table-striped">

                        {{#if tabularData.meta.columns}}
                        <thead>
                        <tr>{{! NOTE: the Handlebars tags in this block are broken onto different lines in a strange way because of an IE whitespace bug}}
                        {{#each tabularData.meta.columns}}<th class="{{type}}" data-sort-by-id="{{id}}" {{#if colSpan}} colspan="{{colSpan}}" {{/if}}> {{name}} </th>{{/each}}
                        </tr>
                        </thead>
                        {{/if}}

                        {{#if teams}}
                        <tbody>
                        {{#each teams}}
                        <tr>{{! NOTE: the Handlebars tags in this block are broken onto different lines in a strange way because of an IE }}
                        <td colspan="2"><img src="{{primaryTeam.avatarUrlSmall}}" alt="{{primaryTeam.name}}" /><p><a {{#if primaryTeam.id}} class="profile-popover" href="" data-participant-ids="[{{primaryTeam.id}}]" {{/if}}>{{primaryTeam.name}}</a> </p> <br /> <img src="{{secondaryTeam.avatarUrlSmall}}" alt="{{secondaryTeam.name}}" /><p><a {{#if secondaryTeam.id}} class="profile-popover" href="" data-participant-ids="[{{secondaryTeam.id}}]" {{/if}}>{{secondaryTeam.name}}</a></p></td>
                        <td><p><cms:contentText key="WIN_SHORT_FORM" code="participant.throwdownstats" />:<span>{{primaryTeam.stats.wins}}</span> <cms:contentText key="LOSS_SHORT_FORM" code="participant.throwdownstats" />:<span>{{primaryTeam.stats.losses}}</span> <cms:contentText key="TIE_SHORT_FORM" code="participant.throwdownstats" />:<span>{{primaryTeam.stats.ties}}</span></p><br /> <p><cms:contentText key="WIN_SHORT_FORM" code="participant.throwdownstats" />:<span>{{secondaryTeam.stats.wins}}</span> <cms:contentText key="LOSS_SHORT_FORM" code="participant.throwdownstats" />:<span>{{secondaryTeam.stats.losses}}</span> <cms:contentText key="TIE_SHORT_FORM" code="participant.throwdownstats" />:<span>{{secondaryTeam.stats.ties}}</span></p></td>
                        <td class="td-current-progress"><p>{{#if displayProgress}} {{primaryTeam.currentProgressForDisplayWithIndicator}} <span>{{primaryTeam.outcomeForDisplayShortForm}}</span> {{else}} {{primaryTeam.outcomeForDisplayFullForm}} {{/if}}</p><br /> <p>{{#if displayProgress}} {{secondaryTeam.currentProgressForDisplayWithIndicator}} <span>{{secondaryTeam.outcomeForDisplayShortForm}}</span> {{else}} {{secondaryTeam.outcomeForDisplayFullForm}} {{/if}}</p></td>
						<beacon:authorize ifNotGranted="LOGIN_AS">
                        {{#if smackTalkAvailable}}
						<td><a href="{{matchUrl}}" class="btn throwdown-match-detail-btn"><cms:contentText code="participant.throwdownstats" key="SMACK_TALK" /></a> <div class="app-col chevron"><a href="{{matchUrl}}"><i class="icon-chevron-right"></i></a></div></td>
	                    {{else}}
						<td><a href="{{matchUrl}}" class="btn throwdown-match-detail-btn"><cms:contentText code="participant.throwdownstats" key="MATCH_DETAILS" /></a> <div class="app-col chevron"><a href="{{matchUrl}}"><i class="icon-chevron-right"></i></a></div></td>
                        {{/if}}
						</beacon:authorize>
                        </tr>
                        {{/each}}
                        </tbody>
                        {{/if}}
                        </table>
						{{else}}
                        <h4><cms:contentText code="participant.throwdownstats" key="MATCHES_NOT_SCHEDULED" /></h4>
						{{/if}}
                      	{{else}}
                        <h4><cms:contentText code="participant.throwdownstats" key="ROUND_NOT_SCHEDULED" /></h4>
                      {{/if}}
                    </script>

                    <div class="allMatchesPagination pagination pagination-right paginationControls first"></div>

                </div><!-- /.span12 -->
            </div><!-- /.row-fluid -->
        </div><!-- /#Matches.tab-pane -->

        <div class="tab-pane" id="Standings">
            <div class="row-fluid" id="standingsTab">
                <div class="span12 standingsContent">

                    <div class="standingsPagination pagination pagination-right paginationControls first"></div>


                    <div class="standingsTable"></div>

                    <script id="throwdownStandingsTableTpl" type="text/x-handlebars-template">
                        <table id="standingsTable" class="table table-striped">

						<div class="roundDates">
							{{#if isProgressLoaded}}
                            <span><cms:contentText code="participant.throwdownstats" key="FROM" /> {{promotionStartDate}} <cms:contentText code="participant.throwdownstats" key="TO" /> {{progressEndDate}}</span>
							{{else}}
							<cms:contentText code="participant.throwdownstats" key="NO_PROGRESS" />
							{{/if}}
                        </div>

                        {{#if tabularData.meta.columns}}
                        <thead>
                        <tr>{{! NOTE: the Handlebars tags in this block are broken onto different lines in a strange way because of an IE whitespace bug}}
                        {{#each tabularData.meta.columns}}<th class="{{type}}{{#if sortable}} sortable{{/if}}{{#if sortedOn}} sorted{{else}} unsorted{{/if}}{{#if sortedBy}} {{sortedBy}}{{/if}}" data-sort-by-id="{{id}}" data-sorted-on="{{sortedOn}}" data-sorted-by="{{sortedBy}}">{{#if
                        sortable}}<a href="{{sortUrl}}">{{name}} {{#unless sortedByDesc}}<i class="icon-arrow-1-up"></i>{{else}}<i class="icon-arrow-1-down"></i>{{/unless}}</a>{{
                        else}}{{name}}{{/if
                        }}</th>{{/each}}
                        </tr>
                        </thead>
                        {{/if}}

                        {{#if tabularData.results}}
                        <tbody>
                        {{#each tabularData.results}}
                        <tr>{{! NOTE: the Handlebars tags in this block are broken onto different lines in a strange way because of an IE }}
                        <td><img src="{{avatarUrl}}" alt="{{name}}" /><p><a class="profile-popover" href="" data-participant-ids="[{{id}}]"> {{name}}</a></p></td>
                        <td><p>{{win}}</p></td>
                        <td><p>{{loss}}</p></td>
                        <td><p>{{tie}}</p></td>
                        </tr>
                        {{/each}}
                        </tbody>
                        {{/if}}
                        </table>
                    </script>

                   <div class="standingsPagination pagination pagination-right paginationControls first"></div>
                   <!--subTpl.matchesStandingsPaginationTpl=
                            {{#if pagination}}
							{{#with pagination}}
							<ul>
							    <li class="first{{#if first.state}} {{first.state}}{{/if}}" data-page="{{first.page}}">
							        <a href="#"><i class="icon-double-angle-left"></i>&nbsp;</a>
							    </li>
							    <li class="prev{{#if prev.state}} {{prev.state}}{{/if}}" data-page="{{prev.page}}">
							        <a href="#"><i class="icon-angle-left"></i> Prev</a>
							    </li>
							    {{#each pages}}
							    <li {{#if state}}class="{{state}}"{{/if}} data-page="{{page}}">
							        <a href="#">{{#if isgap}}&#8230;{{else}}{{page}}{{/if}}</a>
							    </li>
							    {{/each}}
							    <li class="next{{#if next.state}} {{next.state}}{{/if}}" data-page="{{next.page}}">
							        <a href="#">Next <i class="icon-angle-right"></i></a>
							    </li>
							    <li class="last{{#if last.state}} {{last.state}}{{/if}}" data-page="{{last.page}}">
							        <a href="#">&nbsp;<i class="icon-double-angle-right"></i></a>
							    </li>
							</ul>
							{{/with}}
							{{/if}}
                        subTpl-->

                </div><!-- /.span12 -->
            </div><!-- /.row-fluid -->
        </div><!-- /#Standings.tab-pane -->

    </div><!-- /.tab-content -->


</div><!-- /#throwdownStandingsPage.page-content -->


<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
$(document).ready(function(){


	 G5.throwdown.promoId = document.getElementById("promotionSelect").value = "${promotionId}";
	 var matchFilter = document.getElementById("matchesSelect").value = "${matchFilterName}";
	 /* var selectedTab = "${selectedTab}";
	 alert( selectedTab );
	 $("#standingsMatchesTabs ul li"+"."+selectedTab).addClass('active'); */

	 G5.props.URL_THROWDOWN_STANDINGS = G5.props.URL_ROOT+'throwdown/standingsDetail.do?method=detailInfo&promotionId=' + G5.throwdown.promoId + '&matchFilterName='+ matchFilter;
	 G5.props.URL_THROWDOWN_ALL_MATCHES = G5.props.URL_ROOT+'throwdown/viewMatchesDetail.do?method=detailInfo&promotionId=' + G5.throwdown.promoId + '&matchFilterName='+ matchFilter;
	 G5.props.URL_JSON_PARTICIPANT_INFO = G5.props.URL_ROOT+'participantPublicProfile.do?method=populatePax';
	 G5.props.URL_JSON_PARTICIPANT_FOLLOW = G5.props.URL_ROOT+'publicRecognitionFollowUnFollowPax.do?method=followUnFollowPax';

	 var $throwdownStandingsPage = $('#throwdownStandingsPage');
     throwdownStandingsPageView = new ThrowdownStandingsPageView({
         el: $throwdownStandingsPage,
         pageNav : {
             back : {
            	 text : '<cms:contentText key="BACK" code="system.button" />',
                 url : '<%= RequestUtils.getBaseURI(request) %>/homePage.do#launch/throwdown'
             },
             home : {
            	 text : '<cms:contentText key="HOME" code="system.general" />',
                 url : '${pageContext.request.contextPath}/homePage.do'
             }
         },
        pageTitle :'<cms:contentText key="VIEW_MATCHES_STANDINGS" code="home.rail"/>',
        standingsJson : '' ,
        standingsJsonUrl: G5.props.URL_THROWDOWN_STANDINGS,
        allMatchesJson : '',
        allMatchesJsonUrl: G5.props.URL_THROWDOWN_ALL_MATCHES
    });

});
</script>

<script type="text/template" id="throwdownStandingsPageTpl">
     <%@ include file="/throwdown/commonThrowdownStandingsPage.jsp" %>
</script>

<script type="text/template" id="paginationViewTpl">
     <%@ include file="/include/paginationView.jsp" %>
</script>

<%@include file="/submitrecognition/easy/flipSide.jsp" %>

<script>

// fix for 52260
// window.location.hash is not retained in URL for default form submission (form without action attribute) - only in IE.
// So below code is fix to make it work in IE also.

function setSelectedTabForPromoChange() {
	if ( window.location.hash == '#Standings'  ) {
		document.getElementById('promoselectedTab').value = 'Standings';
	} else {
		document.getElementById('promoselectedTab').value = 'Matches';
	}
}

function setSelectedTabForTypeChange() {
	if ( window.location.hash == '#Standings'  ) {
		document.getElementById('typeselectedTab').value = 'Standings';
	} else {
		document.getElementById('typeselectedTab').value = 'Matches';
	}
}

var loc = location.href;
if ( loc.indexOf("currentSelectedTab=Standings") != -1  ) {
	document.getElementById('anchorStandings').click();
} else if ( loc.indexOf("currentSelectedTab=Matches") != -1 ) {
	document.getElementById('anchorMatches').click();
}

</script>
