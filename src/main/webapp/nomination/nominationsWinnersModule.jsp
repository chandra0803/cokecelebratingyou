<!-- ======== NOMINATIONS WINNERS MODULE ======== -->

<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="java.util.*"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>

<script type="text/template" id="nominationsWinnersModuleTpl">
<div class="module-liner">
    <div class="module-content">

        <h3 class="module-title">
            <a href="nomination/viewNominationPastWinnersList.do?method=display">
                <cms:contentText key="PAST_WINNERS" code="nomination.winners"/>
            </a>
        </h3>
			<c:choose>
               <c:when test="${myWinners gt 0 }">
        			<div class="module-actions">
            	 		<a class="btn btn-primary" href="<%=RequestUtils.getBaseURI(request)%>/nomination/viewNominationPastWinnersList.do?method=getNominationMyWinnersListPage"><cms:contentText key="MY_WINNER_LIST" code="nomination.winners"/></a>
					</div>
               </c:when>
               <c:otherwise>
					<div class="module-actions" style="opacity: 0;">
				 		<a class="btn btn-primary btn-disabled"><cms:contentText key="MY_WINNER_LIST" code="nomination.winners"/></a>
					</div>
               </c:otherwise>
            </c:choose>

        <div id="winnersContainer">
        </div>

    </div> <!-- ./module-content -->

</div><!-- ./module-liner -->

<!--subTpl.nominationWinnersTpl=
    <div class="winnersContainerInner">
    <ul class="nomsList">
        {{#nominationApprovals}}
            {{#winnersInfo}}
            <li class="nom" id="{{winnerId}}">
            <a href="{{../detailUrl}}" class="detailUrl">
                <p class="title">
                    <span class="awardName">{{awardName}}</span>
                    <span class="detailName">{{detailName}}</span>
                </p>

                <div class="card">
                    <div class="card-front">
                        <div class="card-top">
                            <span class="avatar">
                            <span class="avatarContainer">
                                {{#if avatarUrl}}
                                    <img src="{{#timeStamp avatarUrl}}{{/timeStamp}}"  />
                                {{else}}
                                    {{#if teamNomination}}
                                        <div class="avatar-initials">{{trimString teamNominationWinnersName 0 1}}</div>
                                    {{else}}
                                        <div class="avatar-initials">{{trimString winnerName 0 1}}</div>
                                    {{/if}}
                                {{/if}}
                                </span>
                            </span>
                        </div>

                        <div class="card-details">
                            <div class="participant-name">
                                {{#if teamNomination}}
                                    <span class="profile-popover" data-participant-ids="[{{#commaList ../teamList}}{{id}}{{/commaList}}]">
                                        {{teamNominationWinnersName}}
                                    </span>
                                {{else}}
                                    <span class="profile-popover" data-participant-ids="[{{winnerId}}]">
                                        {{winnerName}}
                                    </span>
                                {{/if}}
                            </div>
                            <div class="participant-info">
                                {{#unless teamNomination}}
                                    {{#if winnerPosition}}<span class="pi-title">{{winnerPosition}}</span>{{/if}}
                                    {{#if winnerOrgName}}<span class="pi-org"><i class="sep">|</i> {{winnerOrgName}}</span>{{/if}}
                                    {{#if departmentName}}<span class="pi-dept"><i class="sep">|</i> {{departmentName}}</span>{{/if}}
                                {{/unless}}
                            </div>
                        </div>
                    </div>
                </div>
            </a>
            </li>
            {{/winnersInfo}}
        {{/nominationApprovals}}
    </ul>
    </div>
subTpl-->

</script>
