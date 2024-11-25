<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<div class="pagination pagination-right paginationControls"></div>

<div class="ssiApproveInviteesList">
    <ul>
        {{#each members}}
        <li>
            <a class="profile-popover" href="#" data-participant-ids="[{{id}}]">
                {{lastName}}, {{firstName}}
            </a>
        {{#if countryCode}}<img src="<%=RequestUtils.getBaseURI(request)%>/assets/img/flags/{{countryCode}}.png" alt="{{countryCode}}" class="countryFlag" title="{{countryName}}" />{{/if}}
        <span class="org">{{orgName}} {{#if departmentName}}{{#if orgName}}&bull;{{/if}} {{departmentName}}{{/if}} {{#if jobName}}{{#if departmentName}}&bull;{{else}}{{#if orgName}}&bull;{{/if}}{{/if}} {{jobName}}{{/if}}</span>
        </li>
        {{/each}}
    </ul>
</div>

<div class="pagination pagination-right paginationControls"></div>
<!--XXXsubTpl.paginationTpl=
    NOTE: you can safely take the JSP conversion of core/base/tpl/paginationView.html and include it here. Then, remove the "XXX" above and the script will use this child template instead of the remote one
subTpl-->