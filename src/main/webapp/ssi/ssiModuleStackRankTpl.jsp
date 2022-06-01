<%@ include file="/include/taglib.jspf"%>
{{! JAVA NOTE: isManagerOrCreatorOrSuperViewer is set in the JavaScript }}

<div class="row-fluid">
    <div class="span12">
        {{#if isManagerOrCreatorOrSuperViewer}}
            <span class="activityProgress"><cms:contentText key="ACTIVITY" code="ssi_contest.creator" /> - <strong>{{progress}}</strong></span>
        {{else}}
            <span class="activityProgress">{{activityDescription}} - <strong>{{progress}}</strong></span>
        {{/if}}
    </div>
</div>

{{! 2x2 module }}
<%--<div class="narrowModule">
    {{#if isProgressLoaded}}
    <div class="stackRankBoard jsStackRankBoard" data-dimension="2x2">
        <!-- sub template is rendered here -->
    </div>
    {{else}}
    <div class="row-fluid progressNotLoaded">
        <div class="span12">
            <p><cms:contentText key="WAITING_FOR_DATA" code="ssi_contest.participant" /></p>
        </div>
    </div>
    {{/if}}
</div>

{{! 4x2 module }}
<div class="shortAndWideModule">
    {{#if isProgressLoaded}}
    <div class="stackRankBoard jsStackRankBoard" data-dimension="4x2">
        <!-- sub template is rendered here -->
    </div>
    {{else}}
    <div class="row-fluid progressNotLoaded">
        <div class="span12">
            <p><cms:contentText key="WAITING_FOR_DATA" code="ssi_contest.participant" /></p>
        </div>
    </div>
    {{/if}}
</div>--%>

{{! 4x4 module }}
<div class="wideModule">
    {{#if isProgressLoaded}}
    <div class="stackRankBoard jsStackRankBoard" data-dimension="4x4">
        <!-- sub template is rendered here -->
    </div>
    {{else}}
    <div class="row-fluid progressNotLoaded">
        <div class="span12">
            <p class="alert alert-info"><i class="icon-timer-2"></i> <cms:contentText key="WAITING_FOR_DATA" code="ssi_contest.participant" /></p>
        </div>
    </div>
    {{/if}}
</div>

<%-- DOES NOT APPEAR TO GET USED
<!--subTpl.leaderTpl=
    {{#leaders}}
    <li value="{{rank}}" {{#if classes.length}}class="{{#classes}}{{this}} {{/classes}}"{{/if}}>
        <span>{{score}}</span>
        <b>{{rank}}.</b>
        {{#if avatarUrl}}<img alt="{{lastName}}, {{firstName}}" class="avatar" src="{{#timeStamp avatarUrl}}{{/timeStamp}}">{{/if}}
        <span class="leaderName">{{lastName}}, {{firstName}}</span>
    </li>
    {{/leaders}}
subTpl-->
--%>
