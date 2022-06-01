<%@ include file="/include/taglib.jspf"%>

<table id="selectAudienceTable" class="table table-striped">
    <thead>
        <tr>
            <th><cms:contentText key="SELECT" code="diyCommunications.common.labels" /></th>
            <th><cms:contentText key="GROUP" code="diyCommunications.common.labels" /></th>
            <th></th>
            <th></th>
        </tr>
    </thead>

    {{#if audienceTable.results}}
        <tbody>
            {{#audienceTable.results}}
                <tr class="{{#if selected}}selected{{/if}}" data-group-id="{{id}}">
                    <td class="selectedColumn">
                        <a href="#" class="addToAudience"><cms:contentText key="ADD" code="diyCommunications.common.labels" /></a>
                        <i class="icon-check selected-txt"></i>
                        <input name="audienceSelGroup[{{id}}].id" value="{{id}}" type="checkbox" data-group-id="{{id}}" {{#if selected}} checked="checked"{{/if}} class="audienceSelectCheckbox" />
                        <input name="audienceSelGroup[{{id}}].name" value="{{group}}" type="checkbox" {{#if selected}} checked="checked"{{/if}} class="audienceSelectCheckbox" />
                    </td>
                    <td class="groupColumn">
                        {{group}}
                    </td>
                    <td class="participantColumn">
                        {{participantAmount}} <cms:contentText key="PARTICIPANTS" code="diyCommunications.common.labels" />
                    </td>
                    <td class="viewColumn">
                        <a {{#if participantExportLink}} href="{{participantExportLink}}" data-export="true" {{else}} href="#" {{/if}} class="viewAudienceList fileLink">
                            <cms:contentText key="VIEW_LIST" code="diyCommunications.common.labels" /> <i class="icon-file-xls btn btn-mini btn-icon btn-link btn-export-xls"></i>
                        </a>

                        <div class="audienceParticipants" style="display: none">
                            <ul>
                                {{#each participantList}}
                                <li>{{name}}</li>
                                {{/each}}
                            </ul>
                        </div>
                    </td>
                </tr>
            {{/audienceTable.results}}
        </tbody>
    {{/if}}
</table>

<div class="selectedAudienceTotal">
    <span><cms:contentText key="TOTAL_SELECTED" code="diyCommunications.common.labels" /> <span class="totalParticipants">0</span> <cms:contentText key="PARTICIPANTS" code="diyCommunications.common.labels" /></span>
</div>
