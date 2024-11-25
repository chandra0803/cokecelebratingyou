<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf" %>
<table class="table table-striped">
    {{#if tableColumns}}
    <thead>
        <tr>
            {{#each tableColumns}}
            <th class="{{#if sortable}} sortable{{/if}}{{#if sortedOn}} sorted{{else}} unsorted{{/if}}{{#if sortedBy}} {{sortedBy}}{{/if}}" data-sort-by-id="{{id}}" data-sorted-on="{{sortedOn}}" data-sorted-by="{{sortedBy}}" {{#if colSpan}} colspan="{{colSpan}}" {{/if}}>
                {{#if sortable}}
                    <a href="{{sortUrl}}">{{displayName}} {{#unless sortedByDesc}}<i class="icon-arrow-1-up"></i>{{else}}<i class="icon-arrow-1-down"></i>{{/unless}}</a>
                {{else}}
                    {{displayName}}
                {{/if}}
            </th>
            {{/each}}
        </tr>
    </thead>
    {{/if}}

    <tbody>
        {{#each celebrations}}
        <tr>
            <td>
                {{#if avatarUrl}}
                    <img src="{{#timeStamp avatarUrl}}{{/timeStamp}}" alt="" class="avatar" />
                {{else}}
                     <span class="avatar">{{trimString firstName 0 1}}{{trimString lastName 0 1}}</span>
                {{/if}}
                <a href="#" class="profile-popover" data-participant-ids="[{{id}}]">{{firstName}} {{lastName}}</a>
            </td>
            <td>{{anniversary}}</td>
            <td>{{promotion}}</td>
            <td>{{expirationDate}}</td>
            <td>
                {{#if contributeUrl}}
                    <a href="{{contributeUrl}}" class="btn btn-primary"> <cms:contentText key="CONTRIBUTE" code="purl.celebration.module"/></a>
                {{else}}
                    <a href="{{viewUrl}}" class="btn btn-primary"><cms:contentText key="VIEW_PURL" code="purl.contribution.list"/></a>
                {{/if}}
            </td>
        </tr>
        {{/each}}
    </tbody>
</table>
