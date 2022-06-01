<%@ include file="/include/taglib.jspf"%>

    <div class="pagination pagination-right paginationControls"></div>

    <table id="ssiUpdateResultsActivityTable" class="table table-striped">
        <thead>
            <tr>
                <th class="participant sortable string {{#eq sortedOn "lastName"}} sorted {{sortedBy}}{{else}}asc{{/eq}}" data-sort-on="lastName" data-sort-by="{{#eq sortedOn "lastName"}}{{#eq sortedBy "asc"}}desc{{else}}asc{{/eq}}{{else}}asc{{/eq}}" {{#eq contestType "doThisGetThat"}}rowspan="2"{{/eq}}>
                    <a href="#">
                        <cms:contentText key="PARTICIPANT" code="ssi_contest.generalInfo" />
                        <i class="icon-arrow-1-up"></i><i class="icon-arrow-1-down"></i>
                    </a>
                </th>
                {{#eq contestType "doThisGetThat"}}
                    {{#each activitiesTableData}}
                    <th class="activity number">
                        {{activityName}}
                    </th>
                    {{/each}}
                {{else}}
                <th class="activity sortable string {{#eq sortedOn "activity"}} sorted {{sortedBy}}{{else}}asc{{/eq}}" data-sort-on="activity" data-sort-by="{{#eq sortedOn "activity"}}{{#eq sortedBy "asc"}}desc{{else}}asc{{/eq}}{{else}}asc{{/eq}}">
                    <a href="#">
                        <cms:contentText key="ACTIVITY_DESCRIPTION" code="ssi_contest.details.export" />
                        <i class="icon-arrow-1-up"></i><i class="icon-arrow-1-down"></i>
                    </a>
                </th>

                <th class="totalActivity number">
                    <cms:contentText key="TOTAL_CUR_ACTIVITY" code="ssi_contest.creator" /> <i class="icon icon-info"></i>
                </th>
                {{/eq}}
            </tr>
            {{#eq contestType "doThisGetThat"}}
            <tr>
                {{#each activitiesTableData}}

                <th class="totalActivity number">
                    <cms:contentText key="TOTAL_CUR_ACTIVITY" code="ssi_contest.creator" /> <i class="icon icon-info"></i>
                </th>
                {{/each}}
            </tr>
            {{/eq}}
        </thead>

        <!-- <tfoot>
            <tr>
                {{#eq contestType "doThisGetThat"}}
                <td><cms:contentText key="TOTAL" code="ssi_contest.participant" /></td>

                {{#each activitiesTableData}}
                <td class="number">{{activityTotal}}</td>
                {{/each}}

                {{else}}

                <td colspan="2"><cms:contentText key="TOTAL" code="ssi_contest.participant" /></td>
                <td class="number">{{activityTotal}}</td>

                {{/eq}}
            </tr>
        </tfoot> -->

        {{#if results}}
        <tbody>
            {{#each results}}
            <tr>
                <td>
                    <a class="profile-popover" href="#" data-participant-ids="[{{id}}]">
                        {{participantName}}
                    </a>
                </td>

                {{#eq ../contestType "doThisGetThat"}}

                    {{#each activities}}

                    <td class="number">
                        <input type="hidden" name="participant[{{../index}}].activity[{{activityNumber}}].id" value="{{id}}" />
                        <input type="text" class="ssiTotalActivityText" name="participant[{{../index}}].activity[{{activityNumber}}].totalActivity"  value="{{activity}}" />
                    </td>
                    {{/each}}

                {{else}}
                <td>{{activityName}}</td>

                <td class="number">
                    <input type="text" class="ssiTotalActivityText" name="participant[{{index}}].totalActivity"  value="{{activity}}" />
                </td>
                {{/eq}}
                <input type="hidden" name="participant[{{index}}].id" value="{{id}}" />
            </tr>
            {{/each}}
        </tbody>
        {{/if}}
    </table>

    <div class="pagination pagination-right paginationControls"></div>


<div class="ssiSavePagePaginationPopover" style="display:none">
    <p>
        <b><cms:contentText key="CONFIRM_SAVE" code="ssi_contest.creator" /></b>
    </p>
    <p class="tc">
        <button type="button" class="btn btn-primary ssiSavePaginationProgress" data-url="creatorContestList.do?method=saveAndFetchContestResults&id=${id}#contest/${id}"><cms:contentText key="SAVE" code="system.button" /></button>
        <button type="button" class="btn ssiCancelProgress" data-url="creatorContestList.do?method=display&id=${id}#contest/${id}"><cms:contentText key="CANCEL" code="system.button" /></button>
    </p>
</div>

<div class="ssiSavePageSortPopover" style="display:none">
    <p>
        <b><cms:contentText key="CONFIRM_SAVE" code="ssi_contest.creator" /></b>
    </p>
    <p class="tc">
        <button type="button" class="btn btn-primary ssiSaveSortProgress" data-url="creatorContestList.do?method=saveAndFetchContestResults&id=${id}#contest/${id}"><cms:contentText key="SAVE" code="system.button" /></button>
        <button type="button" class="btn ssiCancelProgress" data-url="creatorContestList.do?method=display&id=${id}#contest/${id}"><cms:contentText key="CANCEL" code="system.button" /></button>
    </p>
</div>

<div class="ssiTotalActivityInfoPopover" style="display: none">
    <p><cms:contentText key="TOTAL_ACTIVITY_INFO" code="ssi_contest.creator" /></p>
</div>

