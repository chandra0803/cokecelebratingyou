<%@page import="com.biperf.core.utils.DateUtils"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>
<script type="text/template" id="throwdownAllMatchesModuleTpl">
<div class="module-liner">
	<a href="{{allMatchesUrl}}" class="visitAppBtn">
        <i class="icon-arrow-2-circle-right"></i>
    </a>

	<div class="app-row main-nav">

		<div class="app-col">
			<h3><cms:contentText code="participant.throwdownstats" key="MATCHES" /></h3>

			<ul class="nav nav-tabs allMatchesTabs">
				<li class="tabGlobal active"><a title="" href="#AllMatches" data-toggle="tab" data-original-title='<cms:contentText code="participant.throwdownstats" key="ALL" />' data-name-id="global"><i class="icon-internet"></i><span><cms:contentText code="participant.throwdownstats" key="GLOBAL" /></span></a></li>
                <li class=" tabTeam"><a title="" href="#TeamMatches" data-toggle="tab" data-original-title='<cms:contentText code="participant.throwdownstats" key="TEAM" />' data-name-id="team"><i class="icon-team-1"></i><span><cms:contentText code="participant.throwdownstats" key="TEAM" /></span></a></li>
           </ul>

		</div>

	</div>

    <div class="wide-view">
		<div class="tab-content">
			<div id="AllMatches" class="tab-pane active">
				<div id="allMatchesTab">

						<div class="roundPagination pagination pagination-center paginationControls first"></div>

						<div class="allMatchesTable"></div>

				</div>
			</div>

			<div id="TeamMatches" class="tab-pane">
				<div id="allMatchesTeamTab">
						<div class="roundPagination pagination pagination-center paginationControls first"></div>

						<div class="allMatchesTeamTable"></div>

				</div>
			</div>
		</div>
    </div><!--/.wide-view-->

	<div class="title-icon-view">

        <h3><cms:contentText code="participant.throwdownstats" key="ALL_MATCHES" /></h3>

    </div>

    <!--subTpl.roundPaginationTpl=
		<ul>
			<li class="prev {{#eq currentRound "1"}} disabled {{/eq}}">
				<a>&#171;</a>
            </li>

			<li>
				<p><cms:contentText key="ROUND" code="promotion.stackrank.history" /> <span class="your-round">{{currentRound}}</span> <cms:contentText key="OF" code="promotion.stackrank.history" /> <span class="round-total">{{totalRounds}}</span></p>

            </li>

            <li class="next {{#eq totalRounds currentRound}} disabled {{/eq}}">
				<a>&#187;</a>
            </li>

        </ul>

    subTpl-->

	<!--subTpl.throwdownAllMatchesTpl=

		<div class="clearBoth">
            <span class="td-fine-print">{{promotionOverview}}, {{#if isProgressLoaded}}<cms:contentText code="participant.throwdownstats" key="FROM" /> {{roundStartDate}} <cms:contentText code="participant.throwdownstats" key="TO" /> {{progressEndDate}}
{{else}} {{#if roundCompleted}}<cms:contentTemplateText code="participant.throwdownstats" key="AS_OF" args="{{roundEndDate}}"/>{{else}} <cms:contentText code="participant.throwdownstats" key="NO_PROGRESS" />
            {{/if}}
            {{/if}}</span>
        </div>
	{{#if roundScheduled}}
		<table id="allMatchesTable" class="table table-bordered table-striped">

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
						<td colspan="2"><img src="{{primaryTeam.avatarUrlSmall}}" alt="{{primaryTeam.name}}" /><p><a {{#if primaryTeam.id}}class="profile-popover" href="#" data-participant-ids="[{{primaryTeam.id}}]{{/if}}">{{primaryTeam.name}}</a> </p> <br /> <img src="{{secondaryTeam.avatarUrlSmall}}" alt="{{secondaryTeam.name}}" /><p><a {{#if secondaryTeam.id}}class="profile-popover" href="#" data-participant-ids="[{{secondaryTeam.id}}]{{/if}}">{{secondaryTeam.name}}</a></p></td>
						<td class="td-record"><p>W:<span>{{primaryTeam.stats.wins}}</span> L:<span>{{primaryTeam.stats.losses}}</span> T:<span>{{primaryTeam.stats.ties}}</span></p> <br/> <p>W:<span>{{secondaryTeam.stats.wins}}</span> L:<span>{{secondaryTeam.stats.losses}}</span> T:<span>{{secondaryTeam.stats.ties}}</span></p></td>
                        <td class="td-current-progress"><p>{{#if displayProgress}} {{primaryTeam.currentProgressForDisplayWithIndicator}} <span>{{primaryTeam.outcomeForDisplayShortForm}}</span> {{else}} {{primaryTeam.outcomeForDisplayFullForm}} {{/if}}</p><br /> <p>{{#if displayProgress}} {{secondaryTeam.currentProgressForDisplayWithIndicator}} <span>{{secondaryTeam.outcomeForDisplayShortForm}}</span> {{else}} {{secondaryTeam.outcomeForDisplayFullForm}} {{/if}}</p></td>
						<td><div class="app-col chevron"><a href="{{matchUrl}}"><i class="icon-chevron-right"></i></a></div></td>
					</tr>
					{{/each}}
					</tbody>
					{{/if}}
					</table>

					<a href="{{allMatchesUrl}}"><cms:contentText code="smacktalk.details" key="VIEW_MORE" /></a>
	{{else}}
    	<h4><cms:contentText code="participant.throwdownstats" key="ROUND_NOT_SCHEDULED" /></h4>
	{{/if}}
	subTpl-->

	<!--subTpl.throwdownAllMatchesTeamTpl=

		<div class="clearBoth">
            <span class="td-fine-print">{{promotionOverview}}, {{#if isProgressLoaded}}<cms:contentText code="participant.throwdownstats" key="FROM" /> {{roundStartDate}} <cms:contentText code="participant.throwdownstats" key="TO" /> {{progressEndDate}}
{{else}} {{#if roundCompleted}}<cms:contentTemplateText code="participant.throwdownstats" key="AS_OF" args="{{roundEndDate}}"/>{{else}} <cms:contentText code="participant.throwdownstats" key="NO_PROGRESS" />
            {{/if}}
            {{/if}}</span>
        </div>

		<table id="allMatchesTable" class="table table-bordered table-striped">

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
						<td colspan="2"><img src="{{primaryTeam.avatarUrlSmall}}" alt="{{primaryTeam.name}}" /><p><a {{#if primaryTeam.id}}class="profile-popover" href="#" data-participant-ids="[{{primaryTeam.id}}]{{/if}}">{{primaryTeam.name}}</a> </p> <br /> <img src="{{secondaryTeam.avatarUrlSmall}}" alt="{{secondaryTeam.name}}" /><p><a {{#if secondaryTeam.id}}class="profile-popover" href="#" data-participant-ids="[{{secondaryTeam.id}}]{{/if}}">{{secondaryTeam.name}}</a></p></td>
						<td><p>W: <span>{{primaryTeam.stats.wins}}</span> L: <span>{{primaryTeam.stats.losses}}</span> T: <span>{{primaryTeam.stats.ties}}</span></p> <br/> <p>W: <span>{{secondaryTeam.stats.wins}}</span> L: <span>{{secondaryTeam.stats.losses}}</span> T: <span>{{secondaryTeam.stats.ties}}</span></p></td>
						<td ><p>{{#if displayProgress}} {{primaryTeam.currentProgressForDisplayWithIndicator}} {{else}} {{primaryTeam.outcomeForDisplayFullForm}} {{/if}}</p><br/> <p>{{#if displayProgress}} {{secondaryTeam.currentProgressForDisplayWithIndicator}} {{else}} {{secondaryTeam.outcomeForDisplayFullForm}} {{/if}}</p></td>
						<td><div class="app-col chevron"><a href="{{matchUrl}}"><i class="icon-chevron-right"></i></a></div></td>
					</tr>
					{{/each}}
					</tbody>
					{{/if}}
					</table>

					<a href="{{allMatchesUrl}}"><cms:contentText code="smacktalk.details" key="VIEW_MORE" /></a>
	subTpl-->
</div>
</script>
