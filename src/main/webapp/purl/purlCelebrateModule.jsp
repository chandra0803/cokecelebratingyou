<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<!-- ======== PURL CELEBRATE MODULE ======== -->
<script type="text/template" id="purlCelebrateModuleTpl">
<div class="module-liner">
    <div class="purlCelebrateList">
    </div>
    <!--subTpl.purlCelebrateListTpl=
        <div class="purlCelebrateModuleTop">
            {{#if celebrations}}
            <h2 class="purlCelebrateModuleTitle multipleCelebrations">
                <cms:contentText key="UPCOMING_CELEBRATIONS" code="purl.celebration.module"/>
            </h2>
            <h2 class="purlCelebrateModuleTitle oneCelebration" style="display:none">
                <cms:contentText key="UPCOMING_CELEBRATIONS" code="purl.celebration.module"/>
            </h2>
            <a href="<%=RequestUtils.getBaseURI(request)%>/purl/purlCelebratePage.do" class="viewMore"><cms:contentText key="VIEW_MORE" code="purl.celebration.module"/></a>
            {{else}}
            {{/if}}

        </div>
        <div class="purlCelebrateModuleContent">
            {{#if celebrations}}

            <div id="purlCarousel" class="carousel">

                    {{#each celebrations}}
                    <div class="item">
                        <div class='item-inner-wrapper'>
                            {{#if avatarUrl}}
                            <img src="{{#timeStamp avatarUrl}}{{/timeStamp}}" alt="" class="avatar" />
                            {{else}}
                            <span class="avatar">{{trimString firstName 0 1}}{{trimString lastName 0 1}}</span>
                            {{/if}}
                            {{#if anniversaryInt}}
                            <span class="celebrationInfo">
                                <span class="promotion">{{anniversaryInt}}</span>
                                <span class="balloon"></span>
                                <span class='celebrationCircleMask'></span>
                            {{else}}
                            <span class="celebrationInfo noAnniversary">
                            {{/if}}
                            </span>
                            <span class="personalInfo">
                                <a href="#" class="profile-popover" data-participant-ids="[{{id}}]">{{firstName}}<br />{{lastName}}</a>
                                {{#if isToday}}
                                <span class="celebrationTimeLeft isToday">
                                {{else}}
                                <span class="celebrationTimeLeft">
                                {{/if}}
                                    <i class="icon-clock"></i>{{timeLeft}}

                                </span>

                                <a href="{{contributeUrl}}" class="shareLink"><cms:contentText key="SHARE_A_MEMORY" code="purl.celebration.module"/></a>
                            </span>
                        </div>
                    </div>
                    {{/each}}

            </div>

            {{else}}
                <div class="purlCelebrateEmpty">
                    <p><cms:contentText key="NO_UPCOMING_PURLS" code="purl.celebration.module"/></p>
                    <a href="<%=RequestUtils.getBaseURI(request)%>/purl/purlCelebratePage.do?purlPastPresentSelect=past" class="btn btn-primary"><cms:contentText key="VIEW_PAST_CELEBRATIONS" code="purl.celebration.module"/></a>
                </div>
            {{/if}}

        </div>
    subTpl-->
</div>
</script>