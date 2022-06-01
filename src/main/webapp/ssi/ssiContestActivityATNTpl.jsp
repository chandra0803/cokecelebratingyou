<div class="row-fluid">
    <div class="span12">
        <table id="" class="table table-striped atnTable">
            <thead>
                <tr>
                    <th class="sortHeader unsorted" data-sort="name">
                        <a href="#">
                            <cms:contentText key="CONTEST_NAME" code="ssi_contest.atn.summary"/>
                            <i class="icon-arrow-1-up"></i>
                            <i class="icon-arrow-1-down"></i>
                        </a>
                    </th>
                    <th class="sortHeader unsorted" data-sort="startDate">
                        <a href="#">
                            <cms:contentText key="START_DATE" code="ssi_contest.participant"/>
                            <i class="icon-arrow-1-up"></i>
                            <i class="icon-arrow-1-down"></i>
                        </a>
                    </th>
                    <th class="sortHeader unsorted" data-sort="endDate">
                        <a href="#">
                            <cms:contentText key="END_DATE" code="ssi_contest.participant"/>
                            <i class="icon-arrow-1-up"></i>
                            <i class="icon-arrow-1-down"></i>
                        </a>
                    </th>
                    <th>
                        <cms:contentText key="ISSUE_AWARDS" code="ssi_contest.generalInfo"/>
                    </th>
                </tr>
            </thead>

            <tbody>
                {{#each contests}}
                <tr>
                    <td>
                        <a href="displayContestSummaryAwardThemNow.do?method=load&contestId={{id}}" title="">{{name}}</a>
                    </td>
                    <td>
                        {{startDate}}
                    </td>
                    <td>
                        {{endDate}}
                    </td>
                    <td>
                      <a href="displayContestSummaryAwardThemNow.do?method=load&contestId={{id}}" class="btn btn-primary"><cms:contentText key="ISSUE_AWARDS" code="ssi_contest.generalInfo"/></a>
                    </td>
                </tr>
                {{/each}}
            </tbody>
        </table>
    </div>
</div>
