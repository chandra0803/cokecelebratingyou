<%@ include file="/include/taglib.jspf"%>
{{! NOTE: These variables are set via JavaScript: }}
{{!       showStatus, showAdminLinks, showDeleteLink, showCopyLink, showEditLink, showContestLink, isCreator }}


{{debug}}
{{#if contests}}
<div class="row-fluid">
    <div class="span12">
        <table id="" class="table table-striped">
            <thead>
                <tr>
                    <th class="sortHeader unsorted" data-sort="name">
                        <a href="#">
                            <cms:contentText key="CONTEST_NAME" code="ssi_contest.participant" />
                            <i class="icon-arrow-1-up"></i>
                            <i class="icon-arrow-1-down"></i>
                        </a>
                    </th>
                    <th class="sortHeader unsorted" data-sort="name">
                        <a href="#">
                            <cms:contentText key="CONTEST_TYPE" code="ssi_contest.creator" />
                            <i class="icon-arrow-1-up"></i>
                            <i class="icon-arrow-1-down"></i>
                        </a>
                    </th>                    
                    {{#if showRole}}
                    <th class="sortHeader unsorted" data-sort="role">
                        <a href="#">
                            <cms:contentText key="ROLE" code="ssi_contest.participant" />
                            <i class="icon-arrow-1-up"></i>
                            <i class="icon-arrow-1-down"></i>
                        </a>
                    </th>
                    {{/if}}
                    {{#if showStatus}}
                    <th class="sortHeader unsorted" data-sort="status">
                        <a href="#">
                            <cms:contentText key="STATUS" code="ssi_contest.participant" />
                            <i class="icon-arrow-1-up"></i>
                            <i class="icon-arrow-1-down"></i>
                        </a>
                    </th>
                    {{/if}}
                    <th class="sortHeader unsorted" data-sort="startDate">
                        <a href="#">
                            <cms:contentText key="START_DATE" code="ssi_contest.participant" />
                            <i class="icon-arrow-1-up"></i>
                            <i class="icon-arrow-1-down"></i>
                        </a>
                    </th>
                    <th class="sortHeader unsorted" data-sort="endDate">
                        <a href="#">
                            <cms:contentText key="END_DATE" code="ssi_contest.participant" />
                            <i class="icon-arrow-1-up"></i>
                            <i class="icon-arrow-1-down"></i>
                        </a>
                    </th>

                    {{#if showUpdatedBy}}
                    <th class="sortHeader unsorted" data-sort="updatedBy">
                        <cms:contentText key="UPDATED_BY" code="ssi_contest.participant" />
                        <i class="icon-arrow-1-up"></i>
                        <i class="icon-arrow-1-down"></i>
                    </th>

                    <th class="sortHeader unsorted" data-sort="updatedOn">
                        <cms:contentText key="LAST_UPDATE" code="ssi_contest.participant" />
                        <i class="icon-arrow-1-up"></i>
                        <i class="icon-arrow-1-down"></i>
                    </th>
                    {{/if}}

                    {{#if showAdminLinks}}
                    <beacon:authorize ifAnyGranted="LOGIN_AS">
                        <th class="iconCol">
                            <cms:contentText key="VIEW" code="ssi_contest.participant" />
                        </th>
                    </beacon:authorize>

                    <th class="iconCol">
                        <cms:contentText key="EDIT" code="ssi_contest.participant" />
                    </th>
                    <th class="iconCol">
                        <cms:contentText key="COPY" code="ssi_contest.participant" />
                    </th>
                    <th class="iconCol">
                        <cms:contentText key="DELETE" code="ssi_contest.participant" />
                    </th>
                    {{/if}}
                </tr>
            </thead>

            <tbody>
                {{#each contests}}
                <tr>
                    <!-- <td class="crud-content nowrap">
                    </td> -->
                    <td>
                        {{#if canShowActionLinks}}
	                        {{#if showContestLink}}
	                            {{#eq contestTypeName "Award Them Now"}}
			                        <a href="displayContestSummaryAwardThemNow.do?method=load&contestId={{id}}" title="">{{name}}</a>
			                        {{else}}
			                        <a href="{{detailPageUrl}}" title="">{{name}}</a>
			           			{{/eq}}
	                        {{else}}
	                            {{name}}
	                        {{/if}}
	                   {{else}}
	                       {{name}}
	                   {{/if}}
                    </td>
                    <td>
                        {{contestTypeName}}
                    </td>                    
                    {{#if ../showRole}}
                    <td>
                        {{roleLabel}}
                        {{#if delegate}}
                            <br>
                            <cms:contentText key="DELEGATE" code="ssi_contest.participant" />: {{delegate.firstName}} {{delegate.lastName}}
                        {{/if}}
                    </td>
                    {{/if}}
                    {{#if ../showStatus}}
                    <td>
                        {{statusLabel}}
                    </td>
                    {{/if}}
                    <td>
                        {{startDate}}
                    </td>
                    <td>
                        {{endDate}}
                    </td>

                    {{#if ../showUpdatedBy}}
                    <td>
                        {{updatedBy}}
                    </td>

                    <td>
                        {{updatedOn}}
                    </td>
                    {{/if}}

                    {{#if ../showAdminLinks}}
                    <beacon:authorize ifAnyGranted="LOGIN_AS">
                        <td class="iconCol">
                            <a href="{{readOnlyUrl}}" title="" class="btn btn-link btn-small btn-icon"><i class="icon-eye"></i></a>
                        </td>
                    </beacon:authorize>

                    <td class="iconCol">
                        {{#if showEditLink}}
                          {{#eq status "draft"}}
                              <a href="createGeneralInfo.do?method=prepareEdit&contestId={{id}}" title="" class="btn btn-link btn-small btn-icon"><i class="icon-pencil2"></i></a>
                          {{else}}
                              <a href="createGeneralInfo.do?method=prepareEdit&currentStep=5&contestId={{id}}" title="" class="btn btn-link btn-small btn-icon"><i class="icon-pencil2"></i></a>
                          {{/eq}}
                        {{/if}}
                        {{#if showViewLink}}
                        <a href="{{readOnlyUrl}}" title="" class="btn btn-link btn-small btn-icon"><i class="icon-eye"></i></a>
                        {{/if}}
                    </td>

                    <td class="iconCol">
                        {{#if showCopyLink}}
                        <!-- <a href="#/URL_TO_COPY_PAGE?CONTEST_ID_GET_PARAM={{id}}" title="" class="promptAction">copy</a> -->

                        <button type="button" class="btn btn-link btn-small btn-icon promptAction"><i class="icon-file-copy"></i></button>
                        <div class="prompt promptCopy" data-prompt="copy">
                            <button type="button" class="promptClose close">
                                <i class="icon-close"></i>
                            </button>
                            <form class="copyContest" action="creatorContestList.do?method=copyContest" method="post">
                                <fieldset>
                                    <legend><cms:contentText key="COPY_CONTEST" code="ssi_contest.participant" /></legend>
                                    <label for="contestName"><cms:contentText key="NEW_CONTEST_NAME" code="ssi_contest.participant" /></label>
                                    <input type="text" class="contestName" name="contest_name" value="" placeholder="" data-validate-fail-msgs='{"nonempty":"<cms:contentText key="CONTEST_NAME_REQ" code="ssi_contest.participant" />"}' maxlength="50">
                                    <input type="hidden" class="contestId" name="contest_id" value="{{id}}">
                                    <div class="contestStatus contestNameStatus">
                                        <span class="pending"></span>
                                        <i class="valid icon-check-circle"></i>
                                        <i class="invalid icon-cancel-circle"></i>
                                        <p class="msg"></p>
                                    </div>
                                    <div class="submitWrap clearfix">
                                        <button type="submit" class="submitCopy btn btn-primary"><cms:contentText key="NEXT" code="ssi_contest.participant" /></button>
                                        <div class="contestStatus contestCopyStatus">
                                            <span class="pending"></span>
                                        </div>
                                    </div>
                                </fieldset>
                            </form>
                        </div>
                        {{/if}}
                    </td>
                    <td class="iconCol">
                    <c:if test="${canShowDeleteLink eq true }">
                        {{#if showDeleteLink}}
                        <button type="button" class="btn btn-link btn-small btn-icon promptAction"><i class="icon-trash"></i></button>
                        <div class="prompt promptDelete" data-prompt="delete">
                            <p>
                                <strong><cms:contentText key="DELETE_MESG" code="ssi_contest.participant" /></strong>
                            </p>
                            <p class="tc">
                                <button type="button" class="btn promptClose"><cms:contentText key="CANCEL" code="system.button" /></button>
                                <a href="creatorContestList.do?method=deleteContest&id={{id}}" title="" class="btn btn-danger deleteContest" data-id="{{id}}"><cms:contentText key="DELETE_LINK" code="ssi_contest.participant" /></a>
                            </p>
                            <div class="contestStatus contestDeleteStatus">
                                <span class="pending"></span>
                            </div>
                        </div>
                        {{/if}}
                        </c:if>
                    </td>
                    {{/if}}
                </tr>
                {{/each}}
            </tbody>
        </table>
    </div>
</div>

<div id="activityModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
        <h3 id="myModalLabel"><cms:contentText key="ERROR" code="ssi_contest.participant" /></h3>
    </div>
    <div class="modal-body">
        <p>{{! dynamic error message }}</p>
    </div>
    <div class="modal-footer">
        <button class="btn" data-dismiss="modal"><cms:contentText key="CLOSE" code="ssi_contest.participant" /></button>
        <!-- <button class="btn btn-primary">Save changes</button> -->
    </div>
</div>
{{else}}
<div class="row-fluid ssiEmptyTableMsg">
    <div class="span12">
        <h3><cms:contentText key="NOT_ACTIVE_CONTEST" code="ssi_contest.participant" /></h3>
    </div>
</div>
{{/if}}
