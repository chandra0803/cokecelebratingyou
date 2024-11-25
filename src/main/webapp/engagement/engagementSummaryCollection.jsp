<!-- this template/view should always be inserted into '<div class="engagementSummaryCollectionView">' by the parent view initializing it -->

<div class="tabbable">
    <ul class="tab-tab">
        {{#each summaries}}
        <li class="tab {{type}}" data-toggle="tooltip" title="{{title}}" data-actual="{{actual}}" data-target="{{target}}">
            <a data-target="#engagement_{{mode}}_{{type}}_{{../unique}}" data-toggle="tab">
                <i class="icon-g5-engagement-{{type}}"></i>
                <span class="title">{{title}}</span>
            </a>

            {{#if ../isScoreActive}}
            <span class="box">
                <span class="meter meterAch"></span><span class="meter"></span>
            </span>
            {{/if}}
        </li>
        {{/each}}
    </ul>
    <div class="tab-content">
        {{#each summaries}}
        <div class="tab-pane {{type}}" id="engagement_{{mode}}_{{type}}_{{../unique}}"></div>
        {{/each}}
    </div>
</div>

{{#if isScoreActive}}<div class="raphael" data-src="${siteUrlPrefix}/assets/libs/raphael-min.js"></div>{{/if}}
