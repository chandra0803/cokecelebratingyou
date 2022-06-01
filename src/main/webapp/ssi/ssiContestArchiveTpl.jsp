<%@ include file="/include/taglib.jspf"%>
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

                    {{#if isAdmin}}
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

                    {{#if isCreator}}
                    <th>
                        <cms:contentText key="COPY" code="ssi_contest.participant" />
                    </th>
                    {{/if}}
                </tr>
            </thead>

            <tbody>
                {{#each contests}}
                <tr>
                    <td>
                        {{#eq contestType "awardThemNow"}}
                        <a href="displayContestSummaryAwardThemNow.do?method=load&contestId={{id}}" title="">{{name}}</a>
                        {{else}}
                        <a href="{{detailPageUrl}}" title="">{{name}}</a>
                        {{/eq}}
                    </td>
                    <td>
                        {{startDate}}
                    </td>
                    <td>
                        {{endDate}}
                    </td>

                    {{#if ../isAdmin}}
                     <td>
                        {{updatedBy}}
                    </td>

                    <td>
                        {{updatedOn}}
                    </td>
                    {{/if}}
 					                 
                    <td class="iconCol">
                    {{#if canShowActionLinks}}    
                        {{#ueq contestType "awardThemNow"}}
                        <button type="button" class="btn btn-link btn-small promptAction"><i class="icon-file-copy"></i></button>
                        <div class="prompt promptCopy" data-prompt="copy">
                            <button type="button" class="promptClose close">
                                <i class="icon-close"></i>
                            </button>
                            <form class="copyContest" action="#URL_TO_CREATE_CONTEST" method="post">
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
                                        <button type="submit" class="submitCopy"><cms:contentText key="NEXT" code="system.button" /></button>
                                        <div class="contestStatus contestCopyStatus">
                                            <span class="pending"></span>
                                        </div>
                                    </div>
                                </fieldset>
                            </form>
                        </div>
                        {{/ueq}}
                         {{/if}}
                    </td>                    
                </tr>
                {{/each}}
            </tbody>
        </table>
    </div>
</div>
{{else}}
<div class="row-fluid ssiEmptyTableMsg">
    <div class="span12">
        <h3><cms:contentText key="NOT_ARCHIVED_CONTEST" code="ssi_contest.participant" /></h3>
    </div>
</div>
{{/if}}
