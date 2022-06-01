<%@page import="com.biperf.core.utils.ImageUtils"%>
<%@ include file="/include/taglib.jspf"%>
<div class="pagination pagination-right paginationControls"></div>

<table class="table table-striped">
    <thead>
        <tr>
            <th class="member string sortable {{#eq meta.sortedOn "member"}}sorted {{meta.sortedBy}}{{else}}asc{{/eq}}" data-sort-on="member" data-sort-by="{{#eq meta.sortedOn "member"}}{{#eq meta.sortedBy "asc"}}desc{{else}}asc{{/eq}}{{else}}asc{{/eq}}"><a href="#">{{#eq membersType "user"}}<cms:contentText key="TEAM_MEMBERS" code="engagement.participant"/>{{else}}<cms:contentText key="TEAMS" code="engagement.participant"/>{{/eq}} <i class="icon-arrow-1-up"></i><i class="icon-arrow-1-down"></i></a></th>
            {{#if meta.areTargetsShown}}
            <th class="score     target number"><cms:contentText key="COMPANY_GOAL" code="engagement.participant"/></th>
            {{/if}}
            <th class="score     actual number sortable {{#eq meta.sortedOn "score_actual"    }}sorted {{meta.sortedBy}}{{else}}asc{{/eq}}" data-sort-on="score_actual"     data-sort-by="{{#eq meta.sortedOn "score_actual"    }}{{#eq meta.sortedBy "asc"}}desc{{else}}asc{{/eq}}{{else}}asc{{/eq}}"><a href="#"><cms:contentText key="ACTUAL" code="engagement.participant"/>     <i class="icon-arrow-1-up"></i><i class="icon-arrow-1-down"></i></a></th>
            <th class="recSent   actual number sortable {{#eq meta.sortedOn "recSent_actual"  }}sorted {{meta.sortedBy}}{{else}}asc{{/eq}}" data-sort-on="recSent_actual"   data-sort-by="{{#eq meta.sortedOn "recSent_actual"  }}{{#eq meta.sortedBy "asc"}}desc{{else}}asc{{/eq}}{{else}}asc{{/eq}}"><a href="#"><cms:contentText key="ACTUAL" code="engagement.participant"/>     <i class="icon-arrow-1-up"></i><i class="icon-arrow-1-down"></i></a></th>
            {{#if meta.areTargetsShown}}
            <th class="recSent   target number sortable {{#eq meta.sortedOn "recSent_target"  }}sorted {{meta.sortedBy}}{{else}}asc{{/eq}}" data-sort-on="recSent_target"   data-sort-by="{{#eq meta.sortedOn "recSent_target"  }}{{#eq meta.sortedBy "asc"}}desc{{else}}asc{{/eq}}{{else}}asc{{/eq}}"><a href="#"><cms:contentText key="TARGET" code="engagement.participant"/>     <i class="icon-arrow-1-up"></i><i class="icon-arrow-1-down"></i></a></th>
            <th class="recSent   diff   number sortable {{#eq meta.sortedOn "recSent_diff"    }}sorted {{meta.sortedBy}}{{else}}asc{{/eq}}" data-sort-on="recSent_diff"     data-sort-by="{{#eq meta.sortedOn "recSent_diff"    }}{{#eq meta.sortedBy "asc"}}desc{{else}}asc{{/eq}}{{else}}asc{{/eq}}"><a href="#"><cms:contentText key="DIFFERENCE" code="engagement.participant"/> <i class="icon-arrow-1-up"></i><i class="icon-arrow-1-down"></i></a></th>
            {{/if}}
            <th class="recRecv   actual number sortable {{#eq meta.sortedOn "recRecv_actual"  }}sorted {{meta.sortedBy}}{{else}}asc{{/eq}}" data-sort-on="recRecv_actual"   data-sort-by="{{#eq meta.sortedOn "recRecv_actual"  }}{{#eq meta.sortedBy "asc"}}desc{{else}}asc{{/eq}}{{else}}asc{{/eq}}"><a href="#"><cms:contentText key="ACTUAL" code="engagement.participant"/>     <i class="icon-arrow-1-up"></i><i class="icon-arrow-1-down"></i></a></th>
            {{#if meta.areTargetsShown}}
            <th class="recRecv   target number sortable {{#eq meta.sortedOn "recRecv_target"  }}sorted {{meta.sortedBy}}{{else}}asc{{/eq}}" data-sort-on="recRecv_target"   data-sort-by="{{#eq meta.sortedOn "recRecv_target"  }}{{#eq meta.sortedBy "asc"}}desc{{else}}asc{{/eq}}{{else}}asc{{/eq}}"><a href="#"><cms:contentText key="TARGET" code="engagement.participant"/>     <i class="icon-arrow-1-up"></i><i class="icon-arrow-1-down"></i></a></th>
            <th class="recRecv   diff   number sortable {{#eq meta.sortedOn "recRecv_diff"    }}sorted {{meta.sortedBy}}{{else}}asc{{/eq}}" data-sort-on="recRecv_diff"     data-sort-by="{{#eq meta.sortedOn "recRecv_diff"    }}{{#eq meta.sortedBy "asc"}}desc{{else}}asc{{/eq}}{{else}}asc{{/eq}}"><a href="#"><cms:contentText key="DIFFERENCE" code="engagement.participant"/> <i class="icon-arrow-1-up"></i><i class="icon-arrow-1-down"></i></a></th>
            {{/if}}
            <th class="paxRecTo  actual number sortable {{#eq meta.sortedOn "paxRecTo_actual" }}sorted {{meta.sortedBy}}{{else}}asc{{/eq}}" data-sort-on="paxRecTo_actual"  data-sort-by="{{#eq meta.sortedOn "paxRecTo_actual" }}{{#eq meta.sortedBy "asc"}}desc{{else}}asc{{/eq}}{{else}}asc{{/eq}}"><a href="#"><cms:contentText key="ACTUAL" code="engagement.participant"/>     <i class="icon-arrow-1-up"></i><i class="icon-arrow-1-down"></i></a></th>
            {{#if meta.areTargetsShown}}
            <th class="paxRecTo  target number sortable {{#eq meta.sortedOn "paxRecTo_target" }}sorted {{meta.sortedBy}}{{else}}asc{{/eq}}" data-sort-on="paxRecTo_target"  data-sort-by="{{#eq meta.sortedOn "paxRecTo_target" }}{{#eq meta.sortedBy "asc"}}desc{{else}}asc{{/eq}}{{else}}asc{{/eq}}"><a href="#"><cms:contentText key="TARGET" code="engagement.participant"/>     <i class="icon-arrow-1-up"></i><i class="icon-arrow-1-down"></i></a></th>
            <th class="paxRecTo  diff   number sortable {{#eq meta.sortedOn "paxRecTo_diff"   }}sorted {{meta.sortedBy}}{{else}}asc{{/eq}}" data-sort-on="paxRecTo_diff"    data-sort-by="{{#eq meta.sortedOn "paxRecTo_diff"   }}{{#eq meta.sortedBy "asc"}}desc{{else}}asc{{/eq}}{{else}}asc{{/eq}}"><a href="#"><cms:contentText key="DIFFERENCE" code="engagement.participant"/> <i class="icon-arrow-1-up"></i><i class="icon-arrow-1-down"></i></a></th>
            {{/if}}
            <th class="paxRecBy  actual number sortable {{#eq meta.sortedOn "paxRecBy_actual" }}sorted {{meta.sortedBy}}{{else}}asc{{/eq}}" data-sort-on="paxRecBy_actual"  data-sort-by="{{#eq meta.sortedOn "paxRecBy_actual" }}{{#eq meta.sortedBy "asc"}}desc{{else}}asc{{/eq}}{{else}}asc{{/eq}}"><a href="#"><cms:contentText key="ACTUAL" code="engagement.participant"/>     <i class="icon-arrow-1-up"></i><i class="icon-arrow-1-down"></i></a></th>
            {{#if meta.areTargetsShown}}
            <th class="paxRecBy  target number sortable {{#eq meta.sortedOn "paxRecBy_target" }}sorted {{meta.sortedBy}}{{else}}asc{{/eq}}" data-sort-on="paxRecBy_target"  data-sort-by="{{#eq meta.sortedOn "paxRecBy_target" }}{{#eq meta.sortedBy "asc"}}desc{{else}}asc{{/eq}}{{else}}asc{{/eq}}"><a href="#"><cms:contentText key="TARGET" code="engagement.participant"/>     <i class="icon-arrow-1-up"></i><i class="icon-arrow-1-down"></i></a></th>
            <th class="paxRecBy  diff   number sortable {{#eq meta.sortedOn "paxRecBy_diff"   }}sorted {{meta.sortedBy}}{{else}}asc{{/eq}}" data-sort-on="paxRecBy_diff"    data-sort-by="{{#eq meta.sortedOn "paxRecBy_diff"   }}{{#eq meta.sortedBy "asc"}}desc{{else}}asc{{/eq}}{{else}}asc{{/eq}}"><a href="#"><cms:contentText key="DIFFERENCE" code="engagement.participant"/> <i class="icon-arrow-1-up"></i><i class="icon-arrow-1-down"></i></a></th>
            {{/if}}
            <th class="visits    actual number sortable {{#eq meta.sortedOn "visits_actual"   }}sorted {{meta.sortedBy}}{{else}}asc{{/eq}}" data-sort-on="visits_actual"    data-sort-by="{{#eq meta.sortedOn "visits_actual"   }}{{#eq meta.sortedBy "asc"}}desc{{else}}asc{{/eq}}{{else}}asc{{/eq}}"><a href="#"><cms:contentText key="ACTUAL" code="engagement.participant"/>     <i class="icon-arrow-1-up"></i><i class="icon-arrow-1-down"></i></a></th>
            {{#if meta.areTargetsShown}}
            <th class="visits    target number sortable {{#eq meta.sortedOn "visits_target"   }}sorted {{meta.sortedBy}}{{else}}asc{{/eq}}" data-sort-on="visits_target"    data-sort-by="{{#eq meta.sortedOn "visits_target"   }}{{#eq meta.sortedBy "asc"}}desc{{else}}asc{{/eq}}{{else}}asc{{/eq}}"><a href="#"><cms:contentText key="TARGET" code="engagement.participant"/>     <i class="icon-arrow-1-up"></i><i class="icon-arrow-1-down"></i></a></th>
            <th class="visits    diff   number sortable {{#eq meta.sortedOn "visits_diff"     }}sorted {{meta.sortedBy}}{{else}}asc{{/eq}}" data-sort-on="visits_diff"      data-sort-by="{{#eq meta.sortedOn "visits_diff"     }}{{#eq meta.sortedBy "asc"}}desc{{else}}asc{{/eq}}{{else}}asc{{/eq}}"><a href="#"><cms:contentText key="DIFFERENCE" code="engagement.participant"/> <i class="icon-arrow-1-up"></i><i class="icon-arrow-1-down"></i></a></th>
            {{/if}}
        </tr>
    </thead>

    <tbody>
        {{#if model}}
        <tr class="team-model">
            <td class="member string">
                <span class="avatarwrap">
                    <div class="avatar-initials">{{trimString model.nodeName 0 1}}</div>
                </span>

                <span class="nodeName">{{model.nodeName}}</span>
            </td>
            {{#each model.data}}
                {{#eq type "score"}}
                {{#if ../meta.areTargetsShown}}
                <td class="{{type}} target number">{{target}}</td>
                {{/if}}
                <td class="{{type}} actual number {{ontrack}}">
                    {{#if ../meta.isScoreActive}}
                        <strong>{{actual}}</strong>
                        {{#ueq diff ""}}
                        <span class="meter meterAch"></span><span class="meter" data-target="{{target}}" data-actual="{{actual}}"></span>
                        {{/ueq}}
                    {{else}}
                        {{actual}}
                    {{/if}}
                </td>
                {{else}}
                <td class="{{type}} actual number {{ontrack}}">{{actual}}</td>
                {{#if ../meta.areTargetsShown}}
                <td class="{{type}} target number">{{target}}</td>
                <td class="{{type}} diff   number {{ontrack}}">{{#ueq diff 0}}{{diff}}{{/ueq}}</td>
                {{/if}}
                {{/eq}}
            {{/each}}
        </tr>
        {{/if}}

        {{#each members}}
        <tr {{#eq ../membersType "user"}}class="user-model"{{/eq}}>
            <td class="member string">
                {{#eq ../membersType "team"}}
                    <span class="avatarwrap">
                        <div class="avatar-initials">{{trimString nodeName 0 1}}</div>
                    </span>

                    <span class="nodeAndOwner">
                        <a class="drill nodeName" href="engagementDisplay.do?method=fetchDashboardData" data-node-id="{{nodeId}}" data-node-name="{{nodeName}}">{{nodeName}}</a>
                        <span class="owner">{{nodeOwnerName}}</span>
                    </span>
                {{/eq}}

                {{#eq ../membersType "user"}}
                    <i class="icon-arrow-1-right"></i>
                    <span class="avatarwrap">
                        {{#if avatarUrl}}
                            <img src="{{#timeStamp avatarUrl}}{{/timeStamp}}"  />
                        {{else}}
                            <div class="avatar-initials">{{trimString firstName 0 1}}{{trimString lastName 0 1}}</div>
                        {{/if}}
                    </span>
                    <a class="drill userName" href="engagementDisplay.do?method=fetchDashboardData" data-user-id="{{userId}}" data-node-id="{{nodeId}}" data-user-name="{{userName}}">{{userName}}</a>
                    {{#if unrecognized}}
                    <a class="unrecognized" href="#"><i class="icon-info"></i></a>
                    <span class="unrecognized-tip hide"><cms:contentText key="NO_REC_RCVD_30_DAYS" code="engagement.participant"/><a class="recognize" href="#" data-participant-id="{{userId}}" data-node-id="{{nodeId}}">Recognize now</a></span>
                    {{/if}}
                {{/eq}}
            </td>
            {{#each data}}
                {{#eq type "score"}}
                {{#if ../meta.areTargetsShown}}
                <td class="{{type}} target number">{{target}}</td>
                {{/if}}
                <td class="{{type}} actual number {{ontrack}}" data-actual="{{actual}}" data-target="{{target}}">
                    {{#if ../meta.isScoreActive}}
                        <strong>{{actual}}</strong>
                        {{#ueq diff ""}}
                        <span class="meter meterAch"></span><span class="meter"></span>
                        {{/ueq}}
                    {{else}}
                        {{actual}}
                    {{/if}}
                </td>
                {{else}}
                <td class="{{type}} actual number {{ontrack}}">{{actual}}</td>
                    {{#if ../meta.areTargetsShown}}
                        <td class="{{type}} target number">{{target}}</td>
                        <td class="{{type}} diff number {{ontrack}}">{{diff}}</td>
                    {{/if}}
                {{/eq}}
            {{/each}}
        </tr>
        {{/each}}
    </tbody>
</table>

<div class="pagination pagination-right paginationControls"></div>

{{#eq membersType "user"}}
<div class="modal hide fade module recognition" id="ezRecognizeMiniProfileModal" data-backdrop="static" data-keyboard="false">
</div>
{{/eq}}

{{#if meta.isScoreActive}}<div class="raphael" data-src="${siteUrlPrefix}/assets/libs/raphael-min.js"></div>{{/if}}

<!--subTpl.paginationTpl=
    <%@include file="/include/paginationView.jsp" %>
subTpl-->
