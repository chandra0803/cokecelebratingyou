{{#if invalid}}
    <!-- nothing -->
{{else}}
<tr class="participant-item{{#if invitationSentDate}} alreadyInvited{{/if}}"
        data-participant-cid="{{cid}}"
        data-participant-id="{{id}}">

    <td class="participant">

        <input type="hidden" name="{{_paxName}}[{{autoIndex}}].id" value="{{id}}" />
        <input type="hidden" name="{{_paxName}}[{{autoIndex}}].contribType" value="{{contribType}}" />
        <input type="hidden" name="{{_paxName}}[{{autoIndex}}].firstName" value="{{firstName}}" />
        <input type="hidden" name="{{_paxName}}[{{autoIndex}}].lastName" value="{{lastName}}" />
        <input type="hidden" name="{{_paxName}}[{{autoIndex}}].email" value="{{email}}" />
        <input type="hidden" name="{{_paxName}}[{{autoIndex}}].countryCode" value="{{countryCode}}" />
        <input type="hidden" name="{{_paxName}}[{{autoIndex}}].countryName" value="{{countryName}}" />
        <input type="hidden" name="{{_paxName}}[{{autoIndex}}].jobName" value="{{jobName}}" />
        <input type="hidden" name="{{_paxName}}[{{autoIndex}}].departmentName" value="{{departmentName}}" />
        <input type="hidden" name="{{_paxName}}[{{autoIndex}}].orgName" value="{{orgName}}" />
        <input type="hidden" name="{{_paxName}}[{{autoIndex}}].sourceType" value="{{sourceType}}" />

        {{#ueq contribType "other"}}
        <a class="participant-popover" href="#" data-participant-ids="[{{id}}]">
        {{/ueq}}
            {{#if lastName}}<span class="name">{{else}}{{#if firstName}}<span class="name">{{/if}}{{/if}}
            {{lastName}}{{#if lastName}}{{#if firstName}}, {{/if}}{{/if}}
            {{firstName}}
            {{#if lastName}}</span>{{else}}{{#if firstName}}</span>{{/if}}{{/if}}

            {{#if countryCode}}
            <img src="${pageContext.request.contextPath}/assets/img/flags/{{countryCode}}.png" alt="{{countryCode}}" class="countryFlag" />
            {{/if}}
        {{#ueq contribType "other"}}
        </a>

        <span class="org">{{orgName}} {{#if departmentName}}{{#if orgName}}&bull;{{/if}} {{departmentName}}{{/if}} {{#if jobName}}{{#if departmentName}}&bull;{{else}}{{#if orgName}}&bull;{{/if}}{{/if}} {{jobName}}{{/if}}</span>
        {{/ueq}}

        {{#eq contribType "other"}}<span class="email">{{email}} </span>{{/eq}}

    </td>
    <td class="type">
        <span class="sourceType">
            {{#if sourceType}}
                {{sourceType}}
            {{else}}
                Other
            {{/if}}
        </span>
    </td>
    {{#if _showInvitedColumn}}
    <td class="invitationSent">
        {{#if invitationSentDate}}
            {{invitationSentDate}}
            <span class="sortOnMe" style="display:none">{{invitationSentDateSort}}</span>
        {{else}}
            <!-- no invitation sent -->
        {{/if}}
    </td>
    {{/if}}

    <td class="remove">
        {{#if _noRemove}}
        <i class="icon-lock"></i>
        {{else}}
        {{#unless invitationSentDate}}
        	<a class="remParticipantControl" title='<cms:contentText key="REMOVE_THIS_PARTICIPANT" code="claims.submission" />'>
            <i class="icon-trash"></i>
        </a>
        {{else}}

        <i class="icon-lock"></i>
        {{/unless}}
        {{/if}}
    </td>


</tr><!-- /.participant-item -->{{/if}}
