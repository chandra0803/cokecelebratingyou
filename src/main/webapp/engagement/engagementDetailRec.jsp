<%@ include file="/include/taglib.jspf"%>

<div class="row-fluid">
    {{#if byPromo}}
    <div class="{{#if byBehavior}}span6{{else}}span12{{/if}} byPromo">
        <h4 class="subhead withHelp"><span><cms:contentText key="BY_PROMOTION" code="engagement.participant"/></span> <a class="byPromoHelp"><cms:contentText key="VIEW_DASHBOARD_PROMOTIONS" code="engagement.participant"/></a></h4>

        <div class="chartContainer" id="chartContainer_{{type}}"></div>
    </div><!-- /.byPromo -->
    {{/if}}

    {{#if byBehavior}}
    <div class="{{#if byPromo}}span6{{else}}span12{{/if}} byBehavior">
        <h4 class="subhead{{#unless byPromo}} withHelp{{/unless}}">{{#unless byPromo}}<span>{{/unless}}<cms:contentText key="BY_BEHAVIOR" code="engagement.participant"/>{{#unless byPromo}}</span> <a class="byPromoHelp"><cms:contentText key="VIEW_DASHBOARD_PROMOTIONS" code="engagement.participant"/></a>{{/unless}}</h4>

        <div class="wrapper">
            <ul class="behaviors unstyled">
        {{#each byBehavior}}
                <li class="behavior">
                    {{#if iconUrl}}<img src="{{iconUrl}}" alt="{{name}}" class="icon">{{/if}}
                    <h5 class="name">{{name}}</h5>
                    <div class="progress">
                        <div class="bar" data-percent="{{percent}}">
                            <span class="count">{{count}}</span>
                        </div>
                    </div>
                </li>
        {{/each}}
            </ul>
        </div>
    </div><!-- /.byBehavior -->
    {{/if}}

    <div class="byPromoHelpTip hide">
        <strong><cms:contentText key="YOUR_DASHBOARD" code="engagement.participant"/></strong>

        <div class="tipContent">
            <ul>
                <c:forEach var="promotion" items="${eligiblePromotionList}">
                <li><c:out value="${promotion}"/></li>
                </c:forEach>
            </ul>
        </div>
    </div>
</div><!-- /.row-fluid -->

{{#eq mode "user"}}
    <div class="engagementRecognitionsView pubRecTabs publicRecognition page"><!-- .publicRecognition and .page were borrowed to apply the pubRec styles -->
        <h4 class="subhead"><cms:contentText key="RECENT_RECOGNITIONS" code="engagement.participant"/></h4>

        <!-- Recognitions -->
        <div class="pubRecItemsCont" data-msg-empty="<cms:contentText key="NO_RECOGNITIONS_FOUND" code="engagement.participant"/>">
            <div class="publicRecognitionItems">
                <!-- dynamic - pubRecItems -->
            </div>

            <!-- shown when there are more recognitions -->
            <div class="app-row">
                <p>
                    <a href="publicRecognitionResult.do?method=fetchRecognitionsForEngagementDashboard" class="viewAllRecognitions"  style="display: none">
                        <cms:contentText key="VIEW_MORE" code="engagement.participant"/>
                    </a>
                </p>
            </div>
        </div>
    </div><!-- /.pubRecTabs.publicRecognition.page -->
{{/eq}}