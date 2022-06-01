<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

<!-- participantInfoPopover TPL -->
<div class="participantPopover{{#if _isLongList}} long-list{{/if}}" data-msg-loading="<cms:contentText key="LOADING" code="system.common.labels" />">
    <button type="button" class="close closePopoverBtn"><i class='icon-close'></i></button>

{{#isSingle participants}}

    <div class='row-fluid'>

<!--
        <div class='profile-popover-left'>
            {{#if single.isPublic}}<a href='{{single.profileUrl}}' class="publicProfileLink" data-title-page-view="<cms:contentText key="PUBLIC_PROFILE" code="participant.profile" />">{{/if}}<img alt='' class="avatar" src='{{single.avatarUrl}}' width="48" height="48">{{#if single.isPublic}}</a>{{/if}}
        </div>
-->
        <div class='profile-popover-right'>

            <div class="profile-popover-info">
                <p>
                    {{#if single.isPublic}}<a href='{{single.profileUrl}}' class="publicProfileLink" data-title-page-view="<cms:contentText key="PUBLIC_PROFILE" code="participant.profile" />">{{/if}}{{single.firstName}} {{single.lastName}}{{#if single.isPublic}}</a>{{/if}}
                    {{#if single.orgName}}<span>{{single.orgName}}</span>{{/if}}
                    {{#if single.departmentName}}<span>{{single.departmentName}}</span>{{/if}}
                    {{#if single.jobName}}<span>{{single.jobName}}</span>{{/if}}
                </p>
                <p>
                    <span class="country"><img src="${pageContext.request.contextPath}/assets/img/flags/{{single.countryCode}}.png" /></span>
                    {{single.countryName}}
                </p>
            </div>

            <div class="profile-popover-links">
    			<beacon:authorize ifNotGranted="LOGIN_AS">
    				<c:if test="${not beacon:systemVarBoolean('drive.enabled')}">
	    				<c:if test="${beacon:systemVarBoolean('install.recognition')}">
		                    {{#if ../_isShowRecognizeLink}}
		                    	{{#unless single.isSelf}}
		                    		 {{#if single.isDelegate}}
		                    		 	{{#if single.canRecognize}}
		                   					<span><a href='#' class="regonizeFromPopover" ><cms:contentText key="RECOGNIZE" code="participant.profile" /></a></span>
		                   				{{/if}}
		                   			 {{else}}
		                   			  	<span><a href='#' class="regonizeFromPopover" ><cms:contentText key="RECOGNIZE" code="participant.profile" /></a></span>
		                   			 {{/if}}
		
		                   		{{/unless}}
		                    {{/if}}
	                    </c:if>

                    {{#if ../_isShowFollowLink}}
                        {{#if single.isPublic}}
                            {{#unless single.isSelf}}
                            	{{#unless single.isDelegate}}
                            	<c:if test="${beacon:systemVarBoolean('profile.followlist.tab.show')}">
                                <span><a href='#' class="miniProfFollowLink unfollow"
                                        {{#unless single.isFollowed}}style="display:none"{{/unless}}><cms:contentText key="UNFOLLOW" code="participant.profile" /></a><a href='#' class="miniProfFollowLink follow"
                                        {{#if single.isFollowed}}style="display:none"{{/if}}><cms:contentText key="FOLLOW" code="participant.profile" /></a></span>
                                        </c:if>
                                {{/unless}}
                            {{/unless}}
                        {{/if}}
                    {{/if}}
                    </c:if>
                </beacon:authorize>

                {{#if single.isPublic}}
                    {{#if single.profileUrl}}
                        {{#unless single.isDelegate}}
                        <span><a href="{{single.profileUrl}}" class="publicProfileLink" {{#if single.isSelf}}data-isSelf="true"{{/if}} data-title-page-view="<cms:contentText key="PUBLIC_PROFILE" code="participant.profile" />"><cms:contentText key="VIEW_PROFILE" code="participant.profile" /></a></span>
                        {{/unless}}
                    {{/if}}
                {{/if}}

                {{#if single.isThrowdownEnabled}}
                    {{#if single.playerStatsUrl}}
                        <span><a href="{{single.playerStatsUrl}}" class="publicProfileLink" {{#if single.isSelf}}data-isSelf="true"{{/if}} data-title-page-view="<cms:contentText key="PUBLIC_PROFILE" code="participant.profile" />"><cms:contentText key="THROWDOWN_STATS" code="participant.participant" /></a></span>
                    {{/if}}
                {{/if}}
            </div><!-- /.profile-popover-links -->

        </div><!-- /.profile-popover-right -->
    </div><!-- /.row-fluid -->

    {{#if ../_isShowBadge}}
        {{#if single.isPublic}}
            {{#if single.badges}}
                <div class='row-fluid'>
                    <%-- <div class='profile-popover-left'>&nbsp;</div> --%>
                    <div class='profile-popover-right'>

                        {{#single.badges}}
                       <div class="popover-badge">
                            <img src="<%=RequestUtils.getBaseURI(request)%>/{{img}}" title="{{name}}">
                            <!--p>{{name}}</p-->
                        </div>
                        {{/single.badges}}

                    </div>
                </div>
            {{/if}}
        {{/if}}
    {{/if}}


{{else}}<!-- not single, produce list of participants -->

    <ul class="profileList unstyled">

    <!--subTpl.paxList=
    {{#participants}}
        <li class='multiProfile'>
            {{#if ../_isLongList}}
                {{#if this.avatarUrl}}
                    <img alt="{{this.firstName}} {{this.lastName}}" class="avatar" src="{{this.avatarUrl}}" />
                {{else}}
                    <span class="avatar avatar-initials">{{trimString this.firstName 0 1}}{{trimString this.lastName 0 1}}</span>
                {{/if}}

                {{#if this.isPublic}}
                    <a class="naked-name-link" href="{{this.profileUrl}}" data-title-page-view="<cms:contentText key="PUBLIC_PROFILE" code="participant.profile" />">
                {{/if}}

                {{this.firstName}} {{this.lastName}}

                {{#if this.isPublic}}
                    </a>
                {{/if}}

                {{#if this.isPublic}}<a class="naked-name-link" href="{{this.profileUrl}}">{{/if}}{{this.firstName}} {{this.lastName}}{{#if this.isPublic}}</a>{{/if}}
            {{else}}
                <div class='profile-popover-left'>
                    {{#if this.isPublic}}
                        <a href='{{this.profileUrl}}' class="publicProfileLink" data-title-page-view="<cms:contentText key="PUBLIC_PROFILE" code="participant.profile" />">
                    {{/if}}

                    {{#if this.avatarUrl}}
                        <img alt="{{this.firstName}} {{this.lastName}}" class="avatar" src="{{this.avatarUrl}}" />
                    {{else}}
                        <span class="avatar avatar-initials">{{trimString this.firstName 0 1}}{{trimString this.lastName 0 1}}</span>
                    {{/if}}

                    {{#if this.isPublic}}
                        </a>
                    {{/if}}
                </div>
                <div class='profile-popover-right'>
                    <p>
                        {{#if this.isPublic}}
                            <a href='{{this.profileUrl}}' class="publicProfileLink" data-title-page-view="<cms:contentText key="PUBLIC_PROFILE" code="participant.profile" />">
                        {{/if}}

                        {{this.firstName}} {{this.lastName}}

                        {{#if this.isPublic}}
                            </a>
                        {{/if}}<br />

                        {{this.orgName}}<br />

                        <img src="${pageContext.request.contextPath}/assets/img/flags/{{this.countryCode}}.png" />{{this.countryName}}
                    </p>
                </div>
            {{/if}}
        </li>
    {{/participants}}
    subTpl-->

    </ul>

    {{#if _isShowMoreBtn}}
        <button class="btn btn-mini moreParticipantsBtn">
            <cms:contentText key="MORE" code="participant.profile" />
        </button>
    {{/if}}


{{/isSingle}}

</div><!-- /.participantPopover -->
<!-- /participantInfoPopover TPL -->

<div class="modal hide fade recognition" id="ezRecognizeMiniProfileModal" data-backdrop="static" data-keyboard="false">
</div>
