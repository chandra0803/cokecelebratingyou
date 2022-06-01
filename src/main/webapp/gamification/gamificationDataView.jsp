<%@ include file="/include/taglib.jspf"%>

{{#each .}}
    <li class="badge-item {{this.type}}-type earned-{{#if earned}}true{{else}}false{{/if}}" id="badge{{id}}">
        <a href="${pageContext.request.contextPath}/participantProfilePage.do#profile/Badges/badge{{id}}">
            <img src="{{this.img}}" alt="{{this.name}}" class="badge-image badge-image-small" />
            <img src="{{this.imgLarge}}" alt="{{this.name}}" class="badge-image badge-image-large" />

            <div class="badge-meta">
                <h4 class="badge-name{{#if dateEarned}} hasDateEarned{{/if}}">
                    {{#if contestName}}
                        {{this.contestName}}
                    {{else}}
                        {{this.name}}
                    {{/if}}
                </h4>
                {{#if howToEarnText}}
                <p class="badge-how-to-earn"><strong>({{howToEarnText}})</strong></p>
                {{/if}}

                {{#if progress}}
                {{#unless earned}}
                <div class="progress">
                    <span>{{this.progressNumerator}}/{{this.progressDenominator}}</span>
                    <div class="bar" style="width:{{progress}}%"></div>
                </div>
                {{/unless}}
                {{/if}}

                {{#if earned}}
                <p class="badge-date-earned"><cms:contentText key="EARNED_DATE" code="gamification.admin.labels" /> {{this.dateEarned}}</p>
                {{/if}}
            </div><!-- /.div-meta -->
        </a>
    </li>

{{/each}}
