<%@ include file="/include/taglib.jspf"%>
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
                    <th>
                        <cms:contentText key="DENIAL_REASON" code="ssi_contest.approvals.summary" />
                    </th>
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
                    <th>
                        <cms:contentText key="COPY" code="ssi_contest.participant" />
                    </th>
                    <th>
                        <cms:contentText key="DELETE" code="ssi_contest.participant" />
                    </th>
                </tr>
            </thead>

            <tbody>
                {{#each contests}}
                <tr>
                    <td>
                        <a href="#/contest/{{id}}" title="">{{name}}</a>
                    </td>
                    <td>
                        {{deniedReason}}
                    </td>
                    <td>
                        {{startDate}}
                    </td>
                    <td>
                        {{endDate}}
                    </td>
                    <td>
                        {{updatedBy}}
                    </td>

                    <td>
                        {{updatedOn}}
                    </td>
                    {{#if canShowActionLinks}}
                    <td>
                        <button type="button" class="btn btn-link btn-small promptAction"><i class="icon-file-copy"></i></button>
                        <div class="prompt promptCopy" data-prompt="copy">
                            <button type="button" class="promptClose close">
                                <i class="icon-close"></i>
                            </button>
                            <form class="copyContest" action="creatorContestList.do?method=copyContest" method="post">
                                <fieldset>
                                    <legend><cms:contentText key="COPY_CONTEST" code="ssi_contest.participant" /></legend>
                                    <label for="contestName"><cms:contentText key="NEW_CONTEST_NAME" code="ssi_contest.participant" /></label>
                                    <input type="text" class="contestName" name="contest_name" value="" placeholder="" data-validate-fail-msgs='{"nonempty":"You must enter a contest name."}' maxlength="50">
                                    <input type="hidden" class="contestId" name="contest_id" value="{{id}}">
                                    <div class="contestStatus contestNameStatus">
                                        <span class="pending"></span>
                                        <i class="valid icon-check-circle"></i>
                                        <i class="invalid icon-cancel-circle"></i>
                                        <p class="msg"></p>
                                    </div>
                                    <div class="submitWrap clearfix">
                                        <button type="submit" class="submitCopy"><cms:contentText key="NEXT" code="ssi_contest.participant" /></button>
                                        <div class="contestStatus contestCopyStatus">
                                            <span class="pending"></span>
                                        </div>
                                    </div>
                                </fieldset>
                            </form>
                        </div>
                    </td>
                    <td>
                        <button type="button" class="btn btn-link btn-small promptAction"><i class="icon-trash"></i></button>
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
                    </td>
                    {{/if}}
                </tr>
                {{/each}}
            </tbody>
        </table>
    </div>
</div>
{{else}}
<div class="row-fluid ssiEmptyTableMsg">
    <div class="span12">
        <h3><cms:contentText key="NOT_DENIED_CONTEST" code="ssi_contest.participant" /></h3>
    </div>
</div>
{{/if}}
