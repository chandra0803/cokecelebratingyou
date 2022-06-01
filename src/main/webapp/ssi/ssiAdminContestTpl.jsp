<%@ include file="/include/taglib.jspf"%>
{{! NOTE: these variables are set via javascript: }}
{{!       isCreator, contestHtml }}

<div class="row-fluid backToContestRow">
	<div class="span12 creatorDetailsBtns backToContestWrap" style="display: none;">
		<button type="button" class="btn btn-link btn-icon backToContest"><i class="icon-arrow-1-circle-left"></i> <cms:contentText key="BACK_TO_CONTEST" code="ssi_contest.participant" /></button>
	</div>
</div>

<div class="row-fluid">
	<div class="span12 contestInfo">
		{{#eq status "live"}}
		{{#ueq daysToEnd 0}}
			{{#eq daysToEnd 1}}
		<p class="daysToGo label label-warning"><i class="icon icon-clock"></i>
				<cms:contentText key="LAST_DAY" code="ssi_contest.participant" />
			{{else}}
		<p class="daysToGo label label-success"><i class="icon icon-clock"></i>
				{{daysToEnd}} <cms:contentText key="DAYS_TO_GO" code="ssi_contest.creator" />
			{{/eq}}
		</p>
		{{else}}
		<p class="daysToGo label label-important"><cms:contentText key="CONTEST_OVER" code="ssi_contest.creator" /></p>
		{{/ueq}}
		{{/eq}}

		{{#eq status "finalize_results"}}
		<p class="daysToGo label label-warning"><cms:contentText key="CONTEST_OVER" code="ssi_contest.creator" /></p>
		{{/eq}}

		{{#eq status "pending"}}
		<p class="daysToGo label label-inverse"><i class="icon icon-clock"></i>
			{{daysToStart}} {{#eq daysToStart 1}}<cms:contentText key="DAY_TO_START" code="ssi_contest.creator" />{{else}}<cms:contentText key="DAYS_TO_START" code="ssi_contest.creator" />{{/eq}} to start
		</p>
		{{/eq}}

		<p class="contestDates"><cms:contentText key="DATE_START" code="ssi_contest.creator" /> {{startDate}}<br><cms:contentText key="DATE_END" code="ssi_contest.creator" /> {{endDate}}</p>

		<h2 class="contestTitle">
			<i class="type-icon icon-g5-ssi-{{contestType}} {{contestType}}"></i>
			{{name}}
		</h2>

		{{#if attachmentUrl}}
		<div class="contestAttachment">
			<a href="{{attachmentUrl}}" title="" class="fileLink">
				{{attachmentTitle}}
				{{#eq attachmentType "pdf"}}
				<i class="icon-file-pdf btn btn-icon invert-btn btn-export-pdf"></i>
				{{else}}
					{{#eq attachmentType "word"}}
					<i class="icon-file-doc btn btn-icon invert-btn btn-export-doc"></i>
					{{/eq}}
				{{/eq}}
			</a>
		</div>
		{{/if}}

		{{#unless isCreator}}
			<span class="contestMeta"><cms:contentText key="FOR_QUESTION_CONTACT" code="ssi_contest.creator" /> {{creatorName}}</span>

			<br/>
		{{/unless}}

		{{#eq activityMeasuredIn "currency"}}
			<span class="contestMeta"><cms:contentText key="CURRENCY_USED_FOR_THIS_CONTEST" code="ssi_contest.participant" /> {{currencyAbbr}}</span>
		{{/eq}}

		<div class="partialDescription toggleDescriptionWrap">
			{{description}}
		</div>

		<div class="fullDescription toggleDescriptionWrap">
			{{description}}
		</div>

		<a href="#" class="activityDescriptionToggle hide readMore"><cms:contentText key="READ_MORE" code="ssi_contest.participant" /></a>
		<a href="#" class="activityDescriptionToggle hide readLess"><cms:contentText key="READ_LESS" code="ssi_contest.participant" /></a>
	</div>
</div>
<c:if test="${isSuperViewer ne true }">
{{#if isCreator}}
<div class="row-fluid">
	{{#if includeSubmitClaim}}
		{{#eq claimDaysToEnd 0}}
		{{#ueq status "finalize_results"}}
		{{#ueq status "closed"}}
		<div class="span12 ssiContestEndActions">
		<div class="well">
			<h3><cms:contentText code="ssi_contest.creator" key="CONTEST_ENDED" /></h3>

			<div class="row-fluid ssiContestEndSection">
				<div class="span4">
					<a href="approveContestClaims.do?method=display&clientState={{id}}" class="btn btn-block btn-primary"><cms:contentText key="APPROVE_CLAIMS" code="ssi_contest.creator" /></a>
				</div>
				<div class="span7">
					<p><cms:contentText key="UPDATE_MORE_CLAIMS" code="ssi_contest.creator" /></p>
				</div>
			</div>
			<div class="row-fluid ssiContestEndSection">
				<div class="span4">
				<a href="contestPayouts.do?method=displayContestPayouts&id={{id}}" class="btn btn-block btn-primary"><cms:contentText key="CLOSE_CONTEST_ISSUE_PAY" code="ssi_contest.creator" /></a>
				</div>
				<div class="span7">
					<p><cms:contentText key="END_CONTEST_CONFIRM" code="ssi_contest.creator" /></p>
				</div>
			</div>
		</div><!-- /.well -->
		</div><!-- /.span12.ssiContestEndActions -->
		{{/ueq}}
		{{/ueq}}
		{{/eq}}
	{{else}}
		{{#eq daysToEnd 0}}
		{{#ueq status "finalize_results"}}
		{{#ueq status "closed"}}
		{{#unless payoutIssued}}
			<div class="span12 ssiContestEndActions">
			<div class="well">
				<h3><cms:contentText code="ssi_contest.creator" key="CONTEST_ENDED" /></h3>

				<div class="row-fluid ssiContestEndSection">
					<div class="span4">
					    <a href="contestResults.do?method=populateContestResults&id={{id}}" class="btn btn-block btn-primary"><cms:contentText key="UPDATE_RESULTS" code="ssi_contest.creator" /></a>
					</div>
					<div class="span7">
						<p><cms:contentText key="UPDATE_MORE_ACTIVITY" code="ssi_contest.creator" /></p>
					</div>
				</div>
				<div class="row-fluid ssiContestEndSection">
					<div class="span4">
					    <a href="contestPayouts.do?method=displayContestPayouts&id={{id}}" class="btn btn-block btn-primary"><cms:contentText key="CLOSE_CONTEST_ISSUE_PAY" code="ssi_contest.creator" /></a>
					</div>
					<div class="span7">
						<p><cms:contentText key="END_CONTEST_CONFIRM" code="ssi_contest.creator" /></p>
					</div>
				</div>
			</div><!-- /.well -->
			</div><!-- /.span12.ssiContestEndActions -->
		{{/unless}}
		{{/ueq}}
		{{/ueq}}
		{{/eq}}
	{{/if}}
</div>
{{/if}}
</c:if>
<div class="row-fluid">
	<div class="{{#if isCreator}}span8{{else}}span12{{/if}}">
		<h3 class="sectionTitle">
			{{#ueq contestType "doThisGetThat"}}
				{{activityDescription}}
			{{else}}
			    <cms:contentText key="CONTEST_ACTIVITIES" code="ssi_contest.creator" />
			{{/ueq}}
		</h3>
		{{#ueq contestType "stackRank"}}
		    {{#ueq status "pending"}}
		        <p class="asOfDate"><cms:contentText key="AS_OF" code="ssi_contest.creator" /> {{updatedOnDate}}</p>
		    {{/ueq}}
		{{else}}
		    {{#if isProgressLoaded}}
		        <p class="asOfDate"><cms:contentText key="AS_OF" code="ssi_contest.creator" /> {{updatedOnDate}}</p>
		    {{/if}}
		    {{#if includeMinimumQualifier}}
		    	<p class="minimumQualifier"><cms:contentText key="MINIMUM_QUALIFIER" code="ssi_contest.payout_dtgt" /> <strong>{{minQualifier}}</strong></p>
	    	{{/if}}
		{{/ueq}}
	</div>

<c:if test="${isSuperViewer ne true }">
	{{#if isCreator}}
	<div class="span4 creatorDetailsBtns">
		<div class="pullRightBtns">
			{{#if includeSubmitClaim}}
				{{#eq status "live"}}
				{{#ueq claimDaysToEnd 0}}
				  <beacon:authorize ifNotGranted="LOGIN_AS">
					<a href="approveContestClaims.do?method=display&clientState={{id}}" class="btn btn-primary"><cms:contentText key="APPROVE_CLAIMS_BUTTON" code="ssi_contest.claims"/></a>
				  </beacon:authorize>
				{{/ueq}}
				{{/eq}}
			{{else}}
				{{#eq status "live"}}
				{{#ueq daysToEnd 0}}
					{{#unless payoutIssued}}
					    <a href="contestResults.do?method=populateContestResults&id={{id}}" class="btn btn-primary"><cms:contentText key="UPDATE_RESULTS" code="ssi_contest.creator" /></a>
					{{/unless}}
				{{/ueq}}
				{{/eq}}
			{{/if}}
		</div>
	</div>
	{{/if}}
	</c:if>
</div>

{{#eq daysToEnd 0}}
	<div class="statusBannerWrap">
		<div class="row-fluid">
			<div class="span12 statusBanner">
				{{#eq status "finalize_results"}}
					<span class="msg"><cms:contentText key="FINAL_RESULTS" code="ssi_contest.creator" /></span>
				{{else}}
					{{#eq status "closed"}}
						<span class="msg"><cms:contentText key="FINAL_RESULTS" code="ssi_contest.creator" /></span>
					{{else}}
						{{#if isCreator}}
						<span class="msg"><cms:contentText key="UPDATE_FINAL_RESULTS" code="ssi_contest.creator" /></span>
						{{else}}
						<span class="msg"><cms:contentText key="PENDING_RESULTS" code="ssi_contest.creator" /></span>
						{{/if}}
					{{/eq}}
				{{/eq}}
			</div>
		</div>
	</div>
{{/eq}}

<div class="contestUniqueContentWrapper">
	<div class="contestUniqueContent">
		<div class='contestWrapper {{contestType}} {{#eq status "finalize_results"}}complete{{/eq}} {{#eq status "closed"}}complete{{/eq}} {{#ueq contestType "stackRank"}}container-splitter with-splitter-styles{{/ueq}}'>
			{{! triple curly brackets prevents Handlebars from processing the data }}
			{{{contestHtml}}}
		</div>

<!-- 		{{#oror isCreator isManager isSuperViewer}}

			{{! stack rank is included in ssiCreatorStackRankTpl and ssiManagerStackRankTpl }}
			{{#ueq contestType "stackRank"}}
				{{#if isProgressLoaded}}
				<div class="row-fluid">
					<div class="span12 text-right exportBtns">
						<ul class="pullRightBtns export-tools">
							<li class="export csv">
								<a href="contestDetailsExportAction.do?method=downloadContest&role={{role}}&id={{id}}" class="exportCsvButton">
									<span class="btn btn-inverse btn-compact btn-export-csv">
										<cms:contentText key="CSV" code="system.general" /> <i class="icon-download-2"></i>
									</span>
								</a>
							</li>
						</ul>
					</div>
				</div>
				{{/if}}
			{{/ueq}}
		{{/oror}} -->

		{{#ueq contestType "stackRank"}}
			{{#oror isCreator isManager isSuperViewer}}
				{{#if isProgressLoaded}}
					<!-- <div class="text-right exportBtns"> -->
						<ul class="pullRightBtns export-tools">
							<li class="export csv">
								<a href="contestDetailsExportAction.do?method=downloadContest&role={{role}}&id={{id}}" class="exportCsvButton">
									<span class="btn btn-inverse btn-compact btn-export-csv">
										CSV <i class="icon-download-2"></i>
									</span>
								</a>
							</li>
						</ul>
					<!-- </div> -->
				{{/if}}
			{{/oror}}

			<div class="adminContestDetails">
				{{! contest details table}}
			</div>
		{{/ueq}}
	</div>

{{#if includeStackRanking}}
	<div class="contestFullStackRankWrap" style="display:none">
		{{! contest stack rank table}}

	<!-- Need splitter styles nested inside this wrapper -->
	<div class="container-splitter with-splitter-styles">

		{{#unless isCreator}}
		<div class="row-fluid">
			<div class="span12">
				<ul class="nav nav-tabs paginationTabs">
					<li class="active">
						<a href="#all" class="tabFilter" data-toggle="tab" data-filter="all">
							<cms:contentText key="ALL" code="ssi_contest.creator" />
						</a>
					</li>
					<li>
						<a href="#myTeam" class="tabFilter" data-toggle="tab" data-filter="team">
							<cms:contentText key="MY_TEAM" code="ssi_contest.creator" />
						</a>
					</li>
				</ul>
			</div>
		</div>
		{{/unless}}
		<div class="row-fluid">
			<div class="span12">
				<div class="stackRankBoard contestFullStackRank"
					data-stackrank-offset=""
					data-stackrank-limit="{{payoutCount}}"
					data-stackrank-rowcount="11"
					data-stackrank-tplname="leaderTpl">
					{{! subTpl.leaderTpl}}
				</div>
				<div class="pagination pagination-right paginationControls"></div>
			</div>
		</div><!-- .row-fluid -->
	</div><!-- /.container-splitter.with-splitter-styles -->

	</div>
{{/if}}
</div>

<div class="row-fluid">
	<div class="span12 creatorDetailsBtns backToContestWrap" style="display: none;">
		<button type="button" class="btn btn-link btn-icon backToContest"><i class="icon-arrow-1-circle-left"></i> <cms:contentText key="BACK_TO_CONTEST" code="ssi_contest.participant" /></button>
	</div>
</div>
