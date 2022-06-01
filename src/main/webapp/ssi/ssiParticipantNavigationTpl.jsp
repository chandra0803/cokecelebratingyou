<%@ include file="/include/taglib.jspf"%>
{{! NOTE: these variables are set via javascript: }}
{{!       isCreator, isManager, isSuperViewer }}

<div class="row-fluid">
	<div class="span6 contestSelectWrap">
		<select class="contestSelect dropdown-toggle">
			{{#if isCreator}}
			<option value="index"><cms:contentText key="ACTIVE_PENDING_INCOMPLETE" code="ssi_contest.participant" /></option>
			{{else}}
			<option value="index"><cms:contentText key="ALL_ACTIVE" code="ssi_contest.participant" /></option>
			{{/if}}
			<option value="archived"><cms:contentText key="ALL_ARCHIVED" code="ssi_contest.participant" /></option>
			{{#if isCreator}}
			<option value="denied"><cms:contentText key="ALL_DENIED_CONTESTS" code="ssi_contest.participant" /></option>
			{{/if}}
			<option disabled>&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;&#9472;</option>
			<optgroup label="<cms:contentText key="ACTIVE_PROMOTIONS" code="ssi_contest.participant" />">
				{{#each contests}}
					{{#eq contestType "awardThemNow"}}
					<option value="displayContestSummaryAwardThemNow.do?method=load&amp;contestId={{id}}" data-redirect="true">{{name}}</option>
					{{else}}
					<option data-contestid="{{contestId}}" value="contest/{{id}}">{{name}}</option>
					{{/eq}}
				{{/each}}
			</optgroup>
		</select>
		<span class="spinner"></span>
	</div>

<c:if test="${isSuperViewer ne true }">
	{{#if isCreator}}
		<div class="span6 text-right">
			<div class="formWrap">
				<span class="contestCreateSpinner"></span>
				<button class="createNewContest btn btn-primary"><cms:contentText key="CREATE_NEW_CONTEST" code="ssi_contest.participant" /> <i class="icon-plus-circle"></i></button>
				<form class="contestCreate" action="createGeneralInfo.do?method=prepareCreate" method="post" accept-charset="utf-8" novalidate>
						<input type="hidden" name="locale" value="en_US">
						<select class="contestType hiddenContestType" name="contestType" required>
							{{#each contestTypes}}
								<option value="{{contestType}}"></option>
							{{/each}}
						</select>
				</form>
			</div>
			<div class="editWrap">
				{{! Since this button is updated - not replaced - when changing contests, we use variable style text replace for the id }}
				<a data-href="createGeneralInfo.do?method=prepareEdit&currentStep=5&contestId={0}" class="editLink btn btn-primary"><cms:contentText key="EDIT_THIS_CONTEST" code="ssi_contest.participant" /> <i class="icon-pencil2"></i></a>
			</div>
		</div>
	{{/if}}
	</c:if>

</div>
