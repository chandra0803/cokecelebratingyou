<%@ include file="/include/taglib.jspf"%>

<!--
    each rate string has special CM keys containing the full translated string for the "X of Y team members have achieved the target" text
    the key output will have a {0} and {1} placeholders where the numbers of people are inserted
    this allows the translations to have plain text and the numbers in any order
    we embed this CM output as a tplVariables in our engagementSummaryModel Handlebars template
    we also have subTpls embedded in our engagementSummaryModel Handlebars template
    we pass the various values from the JSON to the subTpls, then replace the {0} and {1} with the rendered output
    the final string is assigned to rateFormatted in the JSON to be passed to the main template
-->

<!--tplVariable.rate= "<cms:contentText key="MEMBERS_ACHIEVED_TARGET" code="engagement.participant"/>" tplVariable-->
<!--tplVariable.rateScore= "<cms:contentText key="MEMBERS_ACHIEVED_GOAL" code="engagement.participant"/>" tplVariable-->
<!--subTpl.teamMemMetTarget= <span class="teamMemMetTarget">{{teamMemMetTarget}}</span> subTpl-->
<!--subTpl.teamMemCount= <span class="teamMemCount">{{teamMemCount}}</span> subTpl-->


<h4 class="title">
    <span>
        <a class="titleLink" href="{{#eq mode "team"}}${pageContext.request.contextPath}/engagement/engagementDisplay.do?method=displayDashboardPage\#{{type}} {{else}}${pageContext.request.contextPath}/participantProfilePage.do#profile/Dashboard/{{type}} {{/eq}}">
            {{title}}
        </a>
        <a href="#" class="showDescription" data-toggle="tooltip" title="{{description}}"><i class="icon-info"></i></a>
    </span>
</h4>

<p class="meta"><span class="timeframe"><cms:contentText key="FOR_THIS_MONTH" code="engagement.participant"/></span> <span class="sep">&#8226;</span> <span class="asof"><cms:contentText key="AS_OF" code="engagement.participant"/> {{asof}}</span></p>

<div class="summary">
    <div>
        {{#and isScoreActive areTargetsShown}}
        <span class="target">
            <i class="icon-g5-engagement"></i>
            <strong>{{target}}{{#eq type "score"}}<span class="pct">%</span>{{/eq}}</strong>
            {{#ueq type "score"}}<span>{{targetLabel}}</span>{{/ueq}}
        </span>
        {{else}}
            {{#unless isScoreActive}}
            <i class="icon-g5-engagement-{{type}} decorator"></i>
            {{/unless}}
        {{/and}}
        <span class="actual">
            <a href="{{#eq mode "team"}}${pageContext.request.contextPath}/engagement/engagementDisplay.do?method=displayDashboardPage\#{{type}} {{else}}${pageContext.request.contextPath}/participantProfilePage.do#profile/Dashboard/{{type}} {{/eq}}">
                <strong><b class="number">{{actual}}</b>{{#eq type "score"}}<sup>%</sup>{{/eq}}</strong>
                {{#ueq type "score"}}<span>{{actualLabel}}</span>{{/ueq}}
            </a>
            {{#if isScoreActive}}<span class="meter meterAch"></span><span class="meter"></span>{{/if}}
        </span>
    </div>
</div><!-- /.summary -->

<div class="extended">
    {{#ueq type "score"}}
    <div class="avg avgTeamMem">
        <h5><cms:contentText key="TEAM_MEMBER_AVERAGE" code="engagement.participant"/>:</h5>
        <span class="box" data-actual="{{avgTeamMem}}" data-target="{{target}}">
            <strong><b class="number">{{avgTeamMem}}</b></strong>
            {{#if isScoreActive}}<span class="meter meterAch"></span><span class="meter"></span>{{/if}}
        </span>
    </div>
    {{/ueq}}

    <div class="avg avgCompany">
        <h5><cms:contentText key="COMPANY_AVERAGE" code="engagement.participant"/>:</h5>
        <span class="box" data-actual="{{avgCompany}}" data-target="{{target}}">
            <strong><b class="number">{{avgCompany}}</b>{{#eq type "score"}}<sup>%</sup>{{/eq}}</strong>
            {{#if isScoreActive}}<span class="meter meterAch"></span><span class="meter"></span>{{/if}}
        </span>
    </div>

    {{#eq mode "team"}}
    {{#if isScoreActive}}
    <!--<p class="rate">{{{rateFormatted}}}</p>-->
    <div class="rate">
        <h5><cms:contentText key="TEAM_MEMBERS_ACHIEVED" code="engagement.participant"/>:</h5>
        <p class="frac">
            <strong class="met">{{teamMemMetTarget}}</strong><strong class="divider">/</strong><strong class="tot">{{teamMemCount}}</strong>
        </p>
    </div>
    {{/if}}

    {{#if reportUrl}}<p class="report"><a href="{{reportUrl}}" {{#if isLargeAudienceEnabled}}class="largeAudience"{{/if}}><i class="icon-bar-chart"></i> {{reportTitle}}</a></p>{{/if}}
    {{/eq}}
</div><!-- /.extended -->
