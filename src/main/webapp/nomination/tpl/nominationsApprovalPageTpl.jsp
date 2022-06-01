<%@ include file="/include/taglib.jspf"%>
<%@page import="com.biperf.core.utils.UserManager"%>
<%@page import="com.biperf.core.ui.approvals.NominationsApprovalPageForm"%>

{{#promotion}}
<div class="row-fluid">
    <div class="span6 approvalFiltersWrap">
        <html:form styleId="approvalsSearchForm" styleClass="form" method="post" action="nominationsApprovalPage.do?method=fetchNominationsApprovalPageTableData">
           <!-- Custom for coco cola wip#58122 starts -->
                <c:set var="charIndex" value="${-1}" />
														<c:if test="${levelPayouts != null }">
														<logic:iterate name="levelPayouts" id="levelCopy">
														<c:set var="charIndex" value="${charIndex+1}" />
																<html:hidden property="levelCopy[${charIndex}].totalPoints"	styleClass="levelInputs"/>
							
														</logic:iterate>
														</c:if>
                <html:hidden property="capPerPax" 	styleClass="capPerPax"/>
                               
            <!-- Custom for coco cola wip#58122 ends -->
           
            <div class="control-group inline">
                <label class="control-label">
                    <cms:contentText key="DATE_SUBMITTED_RANGE" code="nomination.approvals.module"/>
                </label>

                <div class="controls">
                    <div class="input-append input-append-inside datepickerTrigger showTodayBtn"
                        data-date-format="<%=UserManager.getUserDatePattern().toLowerCase()%>"
                        data-date-language="<%=UserManager.getUserLocale()%>"
                        data-date-startdate=""
                        data-date-todaydate=""
                        data-date-autoclose="true">

                        <input type="text" id="dateStart" name="dateStart" value="${nominationsApprovalPageForm.dateStart}" readonly="readonly" class="date datepickerInp input-medium">

                        <button class="btn awardDateIcon">
                            <i class="icon-calendar"></i>
                        </button>
                    </div>
                </div>
            </div>

            <div class="control-group inline">
                <label class="control-label inline">
                    <cms:contentText key="TO" code="nomination.approvals.module"/>:
                </label>

                <div class="controls inline">
                    <div class="input-append input-append-inside datepickerTrigger showTodayBtn"
                        data-date-format="<%=UserManager.getUserDatePattern().toLowerCase()%>"
                        data-date-language="<%=UserManager.getUserLocale()%>"
                        data-date-startdate=""
                        data-date-todaydate=""
                        data-date-autoclose="true">

                        <input type="text" id="dateEnd" name="dateEnd" value="${nominationsApprovalPageForm.dateEnd}" readonly="readonly" class="date datepickerInp input-medium">

                        <button class="btn awardDateIcon">
                            <i class="icon-calendar"></i>
                        </button>
                    </div>
                </div>
            </div>

            {{#if timePeriodEnabled}}
                <div class="control-group">
                    <label class="control-label">
                        <cms:contentText key="TIME_PERIOD" code="nomination.approvals.module"/>
                    </label>

                    <div class="controls">
                        <select name="timePeriodFilter" id="timePeriodFilter">
                            <option value="" class="defaultOption"><cms:contentText key="SELECT_TIME_PERIOD" code="nomination.approvals.module"/></option>
                            {{#each timePeriods}}
                            <option value="{{id}}">{{name}}</option>
                            {{/each}}
                        </select>
                    </div>
                </div>
            {{/if}}

            <div class="control-group">
                <label class="control-label">
                    <cms:contentText key="APPROVAL_LEVELS" code="nomination.approvals.module"/>
                </label>

                <div class="controls">
                    <select name="levelsFilter" id="levelsFilter">
                        {{#each approvalLevels}}
                        <option value="{{id}}" {{#if selected}} selected {{/if}}>
                        {{#if displayName}}
                            {{displayName}}
                        {{else}}
                            Level {{id}}
                        {{/if}}
                        </option>
                        {{/each}}
                    </select>
                </div>
            </div>

            <div class="control-group statusGroup">
                <label class="control-label">
                    <cms:contentText key="STATUS" code="nomination.approvals.module"/>
                </label>

                <div class="controls">
                    <select name="statusFilter" id="statusFilter" multiple="true">
                        <option value="pend"><cms:contentText key="PENDING" code="nomination.approvals.module"/></option>
                        {{#or finalLevelApprover payoutAtEachLevel}}
                        <option value="winner"><cms:contentText key="WINNER" code="nomination.approvals.module"/></option>
                        {{else}}
                        <option value="approv"><cms:contentText key="APPROVE" code="nomination.approvals.module"/></option>
                        {{/or}}
                        <option value="non_winner"><cms:contentText key="NON_WINNER" code="nomination.approvals.module"/></option>
                        <option value="more_info"><cms:contentText key="MORE_INFO" code="nomination.approvals.module"/></option>
                        {{#if isAdmin}}
                        <option value="expired"><cms:contentText key="EXPIRED" code="nomination.approvals.module"/></option>
                        {{/if}}
                    </select>
                </div>
            </div>

            <div class="control-group">
                <div class="controls">
                    <input type="submit" name="showNominations" value='<cms:contentText key="SHOW_NOMINEES" code="nomination.approvals.module"/>' class="btn btn-primary showActivityBtn"/>
                </div>
            </div>
        </html:form>
      </div><!-- /.approvalFiltersWrap -->

    <div class="approvalTopRightCont span5 offset1">

    </div><!-- /.approvalBudgetWrap -->
</div><!--/.row-fluid-->

<div class="row-fluid">
    <div class="span12 compareSection min-height-zero">
         <!--dynamic-->
    </div>
 </div>

<div class="row-fluid">
    <div class="span12 min-height-zero">
        <div class="nominationTableControls fl">
            <a class="expandCollapseAll collapsed"><cms:contentText key="EXPAND_OR_COLLAPSE" code="nomination.approvals.module"/></a>
        </div>
	</div>
</div>

<div class="row-fluid">
    <div class="span12">
        <form class="approvalTableWrap span12" action="submitNominationsApproval.do?method=saveApprovals" method="post"><!--  dynamic from nominationApprovalTableTpl.html --></form>

        <div class="button-nominationTable" style = "display:none">
	        <beacon:authorize ifNotGranted="LOGIN_AS">
	        <button type="submit" class="btn btn-primary submitApproval btn-fullmobile"><cms:contentText key="DONE" code="system.button" /></button>
	        <button class="btn popoverTrigger btn-fullmobile" data-popover-content="cancelNomination"><cms:contentText key="CANCEL" code="system.button" /></button>
	        </beacon:authorize>
		</div>

        <div class="mobileDetectTooltip" style="display:none">
            <p><cms:contentText key="APPROVALS_DEVICE_TIP" code="nomination.approvals.module"/></p>
        </div>
    </div><!-- /.span12 -->
</div><!-- /.row-fluid -->

<!-- POPOVERS -->
<div class="conversionInfoPopover" style="display: none">
    <p><cms:contentText key="CONVERSION_INFO" code="nomination.approvals.module" /></p>
</div>

<div class="budgetIncreasePopover" style="display:none">
    <div class="control-group validateme"
        data-validate-fail-msgs='{"nonempty":"<cms:contentText key="ENTER_BUDGET_AMOUNT" code="nomination.approvals.module"/>"}'
        data-validate-flags='nonempty'>

        <label class="control-label" for="budgetIncrease"><cms:contentText key="REQUESTED_INCREASE" code="nomination.approvals.module"/></label>
        <div class="controls">
            <input type="text" name="budgetIncrease" class="budgetIncrease" id="budgetIncrease" />
        </div>
    </div>

    <button type="submit" class="btn btn-primary sendBudgetRequest"><cms:contentText key="SEND_REQUEST" code="nomination.approvals.module"/></button>
</div>

<div id="sameForAllTipTpl" class="sameForAllTip" style="display:none">
    <span class="singleClaim"><a href="#"><cms:contentText key="SAME_FOR_ALL" code="nomination.approvals.module"/></a></span>
</div><!-- /#sameForAllTipTpl -->

<div class="pastWinnerPopover" style="display: none">
    <p><cms:contentText key="PARTICIPANT_PAST_WINNER" code="nomination.approvals.module"/></p>
</div>

<div class="maxLimitPopover" style="display: none">
    <p><cms:contentText key="PARTICIPANT_WINNER_LIMIT" code="nomination.approvals.module"/></p>
</div>

<div class="cancelNominationPopover" style="display:none">
    <p>
        <cms:contentText key="ALL_CHANGES_LOST" code="nomination.approvals.module"/>
    </p>

    <p class="tc">
        <button href="${nominationApprovalsRedirectUrl}" id="nominationCancelConfirmBtn" class="btn btn-primary btn-fullmobile"><cms:contentText key="YES" code="system.button" /></button>
        <button id="nominationDoNotCancelBtn" class="btn btn-fullmobile"><cms:contentText key="NO" code="system.button" /></button>
    </p>
</div>

<div id="addAwardModal" class="modal hide fade">
    <div class="modal-header">
        <button data-dismiss="modal" class="close" type="button">
            <i class="icon-close"></i>
        </button>

        <h3><cms:contentText key="ADD_AWARD" code="nomination.approvals.module"/></h3>
        <h4 class="budgetContent"><cms:contentText key="REMAINING_BUDGET" code="nomination.approvals.module"/></h4>

        <span class="currentBalance budgetContent">

            <span class="balance"></span>

            {{#if currencyLabel}}
            {{currencyLabel}}
            {{/if}}
            {{#eq payoutType "points"}}
                <cms:contentText key="POINTS" code="nomination.approvals.module"/>
            {{/eq}}
        </span>
        <span class="budgetIncreaseText budgetContent"><cms:contentText key="RETURN_TO_REQUEST_INCREASE" code="nomination.approvals.module"/></span>
    </div>

    <div class="modal-body">
        <div class="teamHeader">
            <span><cms:contentText key="RECIPIENT" code="nomination.approvals.module"/></span>
            <span class="award" data-msg-points="<cms:contentText key="POINTS" code="nomination.approvals.module"/>"><cms:contentText key="AWARD" code="nomination.approvals.module"/></span>
        </div>

        <div class="teamRecipientList">
            <!-- dynamic from addAwardTpl -->
        </div>

    </div>

    <div class="modal-footer">
        <button type="submit" class="btn btn-primary addAwardBtn"><cms:contentText key="ADD" code="system.button" /> <cms:contentText key="AND" code="system.button" /> <cms:contentText key="CLOSE" code="system.button" /></button>
    </div>
</div><!-- /#addAwardModal -->

<!-- VALIDATION MSGS - informational tooltip for validation -->
<div class="errorTipWrapper" style="display:none">
    <div class="errorTip">
        <!-- display for validateme generic errors -->
        <div class="errorMsg msgGenericError">
            <i class="icon-exclamation-sign"></i>
            <cms:contentText key="PLEASE_CORRECT_ERROR" code="nomination.approvals.module"/>
        </div>
        <div class="errorMsg overBudget">
            <i class="icon-exclamation-sign"></i>
            <span id="errorText"><cms:contentText key="BUDGET_EXCEEDED" code="nomination.approvals.module"/></span>
        </div>
         <div class="errorMsg awardOutOfRange">
            <i class="icon-exclamation-sign"></i>
            <cms:contentText key="AWARD_VALUE_OUT_RANGE" code="nomination.approvals.module"/>
        </div>
        <div class="errorMsg fillAward">
			<i class="icon-warning-circle"></i>
			<cms:contentText key="FILL_AWARD_AMOUNT" code="nomination.approvals.module"/>
		</div>

    </div><!-- /.errorTip -->
</div><!-- /.errorTipWrapper -->

<!--subTpl.addAwardTpl=

    <ul data-team-index={{index}}>
    {{#each teamMembers}}
        <li>
            <div class="paxInfo">
                <a class="profile-popover" data-participant-ids="[{{paxId}}]">{{name}}</a>
                <img src="${pageContext.request.contextPath}/assets/img/flags/{{countryCode}}.png" />

                <span>{{orgName}}</span>
                {{#if departmentName}}<span>- {{departmentName}}</span>{{/if}}
                <span>- {{jobName}}</span>
            </div>

            <div class="awardInfo">
                <input type="number" class="input-medium awardPointsInp{{#if optOutAwards}} optOut{{/if}}" {{#if optOutAwards}} disabled data-msg="<cms:contentText key="USER_OPTED_OUT_OF_AWARDS" code="recognition.merchandise"/>"{{/if}} value="{{#if optOutAwards}}0{{else}}{{award}}{{/if}}" />
            </div>
        </li>
    {{/each}}
    </ul>
subTpl-->

<!--subTpl.comparePaxTpl=
    <div class="compareNomineeBlock" data-index="{{index}}">
        <input type="checkbox" class="close-compare fr" checked />
        <span class="avatarwrap">
            <span class="avatarContainer">
                {{#if isTeam}}
                    <span class="avatar-initials">
                        <i class="icon-team-1"></i>
                    </span>
                {{else if avatarUrl}}
                    <img src="{{avatarUrl}}" alt="{{trimString nominee 0 1}}" />
                {{else}}
                    <span class="avatar-initials">{{trimString nominee 0 1}}</span>
                {{/if}}
            </span>
        </span>

        <div class="compareDetails">
            <span class="nomineeName">{{nominee}}</span>

            {{#unless isTeam}}
                <img src="${pageContext.request.contextPath}/assets/img/flags/{{countryCode}}.png" alt="" />

                <span class="nomineeMeta">{{orgName}}</span>
                {{#if departmentName}}<span class="nomineeMeta">- {{departmentName}}</span>{{/if}}
                <span class="nomineeMeta">- {{jobName}}</span>
            {{/unless}}

            {{#if reason}}
                <strong><cms:contentText key="REASON" code="nomination.approvals.module"/></strong>
                <span>{{{reason}}}</span>
            {{/if}}

            {{#if behaviors}}
                <strong><cms:contentText key="BEHAVIORS" code="nomination.approvals.module"/></strong>

                {{#each behaviors}}

                <span>{{name}}</span>

                {{/each}}
            {{/if}}

            {{#if customFields}}
                {{#each customFields}}
                    <strong>{{name}}</strong>
                    <span>{{description}}</span>
                {{/each}}
            {{/if}}
        </div>

        <a class="viewTableRow"><cms:contentText key="VIEW_IN_TABLE" code="nomination.approvals.module"/> <i class="icon-arrow-2-square-down"></i></a>

    </div>

subTpl-->
{{/promotion}}

<!--subTpl.cumulativeCarouselDetails=
 {{#each this}}
    <dl class="item dl-horizontal">
        <dt><cms:contentText key="NOMINATOR" code="nomination.approvals.module"/></dt>
        <dd>{{nominator}}</dd>

        <dt><cms:contentText key="DATE_SUBMITTED" code="nomination.approvals.module"/></dt>
        <dd>{{dateSubmitted}}</dd>

        {{#if reason}}
            <dt><cms:contentText key="REASON" code="nomination.approvals.module"/></dt>
            <dd>{{{reason}}}</dd>
            <br/>
        {{/if}}

        {{#if moreInfo}}
            <dt><cms:contentText key="MORE_INFO_VALUE" code="nomination.approvals.module"/></dt>
            <dd>{{{moreInfo}}}</dd>
            <br/>
        {{/if}}

        {{#if customFields}}
            {{#each customFields}}
            <dt>{{name}}</dt>
            <dd>{{{description}}}</dd>
            <br/>
            {{/each}}
        {{/if}}

        {{#if attachmentUrl}}
            <dt><cms:contentText key="ATTACHMENT" code="nomination.approvals.module"/></dt>
            <dd><a href="{{attachmentUrl}}">{{#if attachmentName}}{{attachmentName}}{{else}}{{attachmentUrl}}{{/if}}</a></dd>
            <br/>
            {{/if}}

        {{#if eCertUrl}}
        <dt><cms:contentText key="ECERT" code="nomination.approvals.module"/></dt>
        <dd><a href="#" class="generateCertPdf" data-claimId="{{claimId}}" target="_blank">View eCertificate</a></dd>
        {{/if}}
    </dl>
{{/each}}
subTpl-->
