<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>
<%@page import="com.biperf.core.utils.UserManager"%>

<!-- ======== NOMINATION WINNERS LIST PAGE ======== -->
<div id="nominationWinnersListPageView" class="page-content nominationWinnersPage">
	<input type="hidden" name="promotionId " id="promotionId" value="2" />

	<form id="nominationWinnersForm" method="post" action="ajax/nominationsWinnersList.json">
		<div class="page-topper">
			<div class="row-fluid">
				<div class="span12">
					<div class="formSection nominationSection">
						<div class="validateme nominationPromo" data-validate-fail-msgs='{"nonempty":"<cms:contentText key="SELECT_PROMO" code="nomination.past.winners"/>"}' data-validate-flags='nonempty'>
							<label class="control-label" for="nominationId"></label>
							<div class="controls nominationWrapper">
								<select id="nominationId" name="nominationId">
									 <option class="defaultOption" selected="selected" value=""><cms:contentText key="CHOOSE_ONE" code="nomination.past.winners"/></option>
									 <!-- dynamic -  once on page load  -->
								</select>
							</div><!-- /.nominationWrapper -->
						</div>

						<div class="controls nominationChangeWrapper span12" style="display:none">
							<span class="nominationName"><!-- dynamic --></span>

							<button type="button" id="buttonChangeNomination" class="btn btn-primary"><cms:contentText key="CHANGE" code="nomination.past.winners"/></button>
						</div><!-- /.nominationChangeWrapper -->

						<div class="nominationChangeConfirmDialog" style="display:none">
							<p>
							   <b><cms:contentText key="CHANGE_NOMINATION" code="nomination.past.winners"/></b>
							</p>

							<p><cms:contentText key="CURRENT_SELECTIONS_LOST" code="nomination.past.winners"/></p>

							<p class="tc">
							   <button id="buttonChangeNominationConfirm" class="btn btn-primary"><cms:contentText key="YES" code="nomination.past.winners"/></button>
							   <button id="buttonChangeNominationCancel" class="btn"><cms:contentText key="CANCEL" code="nomination.past.winners"/></button>
							</p>
						</div><!-- /.nominationChangeConfirmDialog -->
					</div><!--/.nominationSection-->
				</div>
			</div><!--/.row-fluid-->
		</div><!--/.page-topper-->
        <div class="row-fluid">
            <div class="span12">
                <div class="nominationWinnersSearch">
                    <h4 class="archivedWinnersTitle"><cms:contentText key="ARCHIVED_WINNERS" code="nomination.past.winners"/></h4>

                    <label for="nominationWinnersSearchInput"><cms:contentText key="SEARCH" code="nomination.past.winners"/> <cms:contentText key="OPTIONAL_FIELDS" code="nomination.past.winners"/></label>

                    <div class="defaultFilterCreationWrapper">
                            <select name="winnerSearchSelect" class="winnerSearchSelect" id="winnerSearchSelect">
                                <option value="lastName"><cms:contentText key="LAST_NAME" code="nomination.past.winners"/></option>
                                <option value="country"><cms:contentText key="COUNTRY" code="nomination.past.winners"/></option>
                                <option value="department"><cms:contentText key="DEPARTMENT" code="nomination.past.winners"/></option>
                                <option value="teamName"><cms:contentText key="TEAM_NAME" code="nomination.past.winners"/></option>
                                <option value="firstName"><cms:contentText key="FIRST_NAME" code="nomination.past.winners"/></option>
                            </select>
                            <div class="input-append searchWrap dropdown" data-validate-fail-msgs='{"minlength":"<cms:contentText key="ENTER_ATLEAST_TWO" code="nomination.past.winners"/>"}' data-validate-flags='minlength' data-validate-min-length="2">
                            <input name="nominationWinnerSearch" id="nominationWinnersSearchInput" type="text" placeholder="Last Name"
                            data-autocomp-delay="500"
                            data-autocomp-min-chars="2"
                            data-autocomp-url="${pageContext.request.contextPath}/recognitionWizard/recipientSearch.do?method=doAutoComplete"
                            data-search-url="${pageContext.request.contextPath}/recognitionWizard/recipientSearch.do?method=generatePaxSearchViewForNomination"
                            data-select-mode="multiple">
                            <ul class="dropdown-menu winnersSearchDropdownMenu" role="menu" data-msg-instruction="Start typing please." data-msg-no-results="No results found, please refine text.">
                                <!-- dynamic -->
                            </ul>
                            <div class="spinnerWrap"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div><!--/.row-fluid-->

		<div class="row-fluid">
			<div class="dateRange span12">

			<div class="control-group input-append input-append-inside"
				data-validate-flags="nonempty"
				data-validate-fail-msgs='{"nonempty" : "<cms:contentText key="START_DATE_REQ" code="nomination.past.winners"/>"}'>

				<label class="control-label" for="nominationsDateStart"><cms:contentText key="ACTIVITY_FROM" code="nomination.past.winners"/></label>

				<div class="controls datepickerTrigger showTodayBtn"
						data-date-format="<%=UserManager.getUserDatePattern().toLowerCase()%>"
                        data-date-language="<%=UserManager.getUserLocale()%>"
                        data-date-autoclose="true">
					<input type="text"  class="date datepickerInp" id="nominationsDateStart" name="startDate" value="" readonly="readonly">
					<button class="btn datepickerBtn awardDateIcon"><i class="icon-calendar"></i></button>
				</div>
			</div>

			<div class="control-group input-append input-append-inside"
				data-validate-flags="nonempty"
				data-validate-fail-msgs='{"nonempty" : "<cms:contentText key="END_DATE_REQUIRED" code="nomination.past.winners"/>"}'>

				<label class="control-label" for="nominationsDateEnd"><cms:contentText key="ACTIVITY_TO" code="nomination.past.winners"/></label>

				<div class="controls datepickerTrigger showTodayBtn" data-date-format="<%=UserManager.getUserDatePattern().toLowerCase()%>"
						data-date-language="<%=UserManager.getUserLocale()%>"
						data-date-autoclose="true">
						<input type="text"  class="date datepickerInp" id="nominationsDateEnd" name="endDate"  value="" readonly="readonly">

						<button class="btn datepickerBtn awardDateIcon">
							<i class="icon-calendar"></i>
						</button>
				</div>
			</div>

			<button type="submit" class="btn btn-primary" value="nominationsWinnersSubmit" id="nominationsWinnersSubmit"><cms:contentText key="SUBMIT" code="nomination.past.winners"/></button>
			</div>
		</div><!--/.row-fluid-->
        <div class="row-fluid filterContainer" style="display: none;">
            <span class="filteredBy span"><cms:contentText key="FILTERED_BY" code="nomination.past.winners"/></span>
        </div>
	</form>

    <div class="row-fluid">
		<div class="span12">
		    <!-- NOMINATION -->
			<div class="row-fluid">
				<div class="commentsListWrapper span12"></div>
			</div>

			<script id="commentItemTpl" type="text/x-handlebars-template">
				{{#each nominationApprovals}}
                <div class="row-fluid">
					<div class="innerCommentWrapper nominations-winners-list span12">
						<div class="app-row detail-row">
							<div class="app-col winnersProfile span4">
								{{#winnersInfo}}

								<div class="winnersPeriod">{{detailName}}</div>


								<span class="avatarwrap">
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


								<span class="winnersName">
									{{#if teamNomination}}
										<a href="#" class="profile-popover" data-participant-ids="[{{#commaList ../teamList}}{{id}}{{/commaList}}]">{{teamNominationWinnersName}}</a>
                                </span>
									{{else}}
										<a class="profile-popover" href="#" data-participant-ids="[{{winnerId}}]">
											{{winnerName}}
										</a>

                                    </span>

                                    <span class="orgName">

                                        {{#if winnerOrgName}}
                                        {{winnerOrgName}}
                                        {{/if}}
                                        {{#if departmentName}}
                                        - {{this.departmentName}}
                                        {{/if}}
                                        {{#if winnerPosition}}
                                        - {{winnerPosition}}
                                        {{/if}}
                                    </span>
									{{/if}}

								{{/winnersInfo}}
							</div><!--/.winnersProfile-->

							{{#nominatorInfo}}
							<div class="app-col comment-block span7">
								<p class="commentTitle"><strong><cms:contentText key="BECAUSE_THEY" code="nomination.past.winners"/></strong></p>

								<p>{{{commentText}}}</p>

								<p class="nominatorInfo">
									<span class="nominatorName">
										<a class="profile-popover" href="#" data-participant-ids="[{{nominatorID}}]">{{nominatorName}} </a>
									</span>
                                    <span class="nominatorOrg">
                                        {{#if nominatorOrg}}
                                        {{nominatorOrg}}
                                        {{/if}}
                                        {{#if departmentName}}- {{this.departmentName}}{{/if}}
                                    </span>

									<span class="title">{{#if title}}- {{this.title}}{{/if}}</span>
								</p>
							</div>
							{{/nominatorInfo}}

							<div class="app-col chevron">
								<a href="{{detailUrl}}">
								   <i class="icon-arrow-1-right"></i>
								</a>
							</div>
						</div>
					</div><!-- /.innerCommentWrapper -->
                </div>
				{{/each}}
			</script>
		</div>
    </div><!--/.row-fluid-->
    <!-- modal to catch errors from server on no search results -->
    <script id="commentErrorTpl" type="text/x-handlebars-template">
        <div class="modal hide fade winListErrorsModal">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><i class="icon-close"></i></button>
                <h3>
                    <i class="icon-warning-triangle"></i>
                    Error(s) encountered<!--TODO: cms key-->
                </h3>
            </div>
            <div class="modal-body">
                <ul class="errorsList">
                    {{#each messages}}
                    <li>{{text}}</li>
                    {{/each}}

                </ul>
            </div>
            <div class="modal-footer">
                <a href="#" class="btn" data-dismiss="modal"><cms:contentText key="CLOSE" code="system.button" /></a>
            </div>
        </div><!-- /.winListErrorsModal -->
    </script>
</div>
<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
$(document).ready( function() {

	G5.props.URL_JSON_NOMINATIONS_LIST = G5.props.URL_ROOT+'nomination/viewNominationPastWinnersList.do?method=eligiblePastWinnersPromotions';
	G5.props.URL_JSON_NOMINATIONS_WINNERS_LIST = G5.props.URL_ROOT+'nomination/viewNominationPastWinnersList.do?method=nominationsWinnersList';
	G5.props.URL_JSON_EZ_RECOGNITION_MEMBER_INFO = "${pageContext.request.contextPath}/recognitionWizard/memberInfo.do";
    G5.props.URL_JSON_EZ_RECOGNITION_SEND_EZRECOGNITION = "${pageContext.request.contextPath}/recognitionWizard/submitEasyRecognition.do";
    G5.props.URL_JSON_PARTICIPANT_FOLLOW = G5.props.URL_ROOT+'publicRecognitionFollowUnFollowPax.do?method=followUnFollowPax';

    //attach the view to an existing DOM element
    var nlpv = new NominationWinnersListPageView({
        el:$('#nominationWinnersListPageView'),
        pageNav : {
            back : {
            	 text : '<cms:contentText key="BACK" code="system.button" />',
	              url : 'javascript:history.go(-1);'
            },
            home : {
            	    text : '<cms:contentText key="HOME" code="system.general" />',
	                url : '${pageContext.request.contextPath}/homePage.do'
            }
        },
        pageTitle : 'Nomination Winners'
    });
});
</script>
<%@include file="/submitrecognition/easy/flipSide.jsp" %>
