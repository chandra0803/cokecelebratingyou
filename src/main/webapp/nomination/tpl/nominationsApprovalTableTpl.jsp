<input type="hidden" class="startDateFilter" name="dateStart" />
<input type="hidden" class="endDateFilter" name="dateEnd" />
<input type="hidden" class="promotionIdInp" name="promotionId" />
<input type="hidden" class="levelIdFilter" name="levelId" />

<ul class="export-tools approvalsExportIconsWrapper pushDown">
    {{#unless tabularData.isCumulativeNomination}}
    <li class="export pdf">
        <a href="#">
            <span class="btn btn-inverse btn-compact btn-export-pdf pdfExportIcon">
                <cms:contentText key="PDF" code="system.general" /> <i class="icon-download-2"></i>
            </span>
        </a>
    </li>
    {{/unless}}
    <li class="export csv">
        <a href="{{tabularData.excelUrl}}">
            <span class="btn btn-inverse btn-compact btn-export-csv excelExportIcon">
                <cms:contentText key="CSV" code="system.general" /> <i class="icon-download-2"></i>
            </span>
        </a>
    </li>
</ul>
<div class="table-scroll-wrap">
    <%-- <div class="btn makeAllWinners">Make All Winners</div> --%>
    <table id="nominationApprovalTable" class="table table-striped">

        {{#if tabularData.meta.columns}}
        <thead>
            <tr>
                {{#each tabularData.meta.columns}}
                <th class="{{name}} {{#if sortable}}sortable{{/if}} {{#eq ../tabularData.sortedOn name}}sorted {{../tabularData.sortedBy}}{{else}}asc{{/eq}}" data-sort-on="{{name}}" data-sort-by="{{#eq ../tabularData.sortedOn name}}{{#eq ../tabularData.sortedBy "asc"}}desc{{else}}asc{{/eq}}{{else}}asc{{/eq}}" data-msg-points="<cms:contentText key="POINTS" code="nomination.approvals.module"/>">

                    {{#if sortable}}

                    <a href="#">
                        {{displayName}}

                         <i class="icon-arrow-1-up"></i><i class="icon-arrow-1-down"></i>
                    </a>

                    {{else}}

                    {{displayName}}

                    {{/if}}
                </th>
                {{/each}}
            </tr>
        </thead>
        {{/if}}

        {{#if tabularData.results}}
        <tbody>


        </tbody>
        {{else}}
        <tbody>
        <td colspan="9"><cms:contentText key="NO_PENDING_NOMINATIONS" code="nomination.approvals.module"/></td>
        </tbody>
        {{/if}}
    </table>
</div>
{{#if tabularData.results}}
<div class="viewMoreContainer tc" style="display: none">
    <p>
        <a href="#" class="viewMoreRows">
            <cms:contentText key="VIEW_MORE" code="nomination.approvals.module"/>
        </a>
    </p>
</div>
{{/if}}
