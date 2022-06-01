<%@ include file="/include/taglib.jspf" %>
<div class="searchWrapper hide">
    <div class="PaxSelectedPaxView fixedPos" ></div>
    <div class="PaxSearchView"><!-- if act as delegate add class no-follow <div class="PaxSearchView no-follow">-->
        <div class="closeBtn"><i class="icon-close"></i></div>

        <div class="container" class="col-xs-4">
            <div class="searchBG hide"></div>
            <div class="only-eligible-prompt hide">
                <span class="bold"><cms:contentText key="CAN_NOT_FIND_SOME_ONE" code="participant.search.view" /></span><br/>
                <cms:contentText key="DIAPLYING_ONLY_ELIGIBLE_FOR_PROGRAM" code="participant.search.view" />
            </div>

            <div class="search-control">
                <div class="search-wrap">
                    <div class="check-all-wrap">
                        <div class="checkbox">
                            <label>
                                <input class='checkAll' type="checkbox" value="">
                                <span class="cr"><i class="cr-icon icon-check"></i></span>
                                <cms:contentText key="ALL" code="participant.search.view" />
                          </label>
                        </div>
                    </div>

                    <!-- filter search -->
                    <div class="pubRecFilterWrap hide">
                        <div class="dropdown">
                            <a class="dropdown-toggle btn btn-primary btn-inverse" data-toggle="dropdown" href="#"><span><cms:contentText key="SELECT_TEAM" code="participant.search.view" /></span> <b class="caret"></b></a>
                            <ul class="dropdown-menu pubRecTabs" role="menu" aria-labelledby="dLabel">
                            </ul>
                        </div>
                    </div>

                    <!-- wordsearch -->
                    <div class="search-input-wrap">
                        <div class="search-input hide-scrollbars hide">

                            <div class="selected-filters">
                                <input class="paxOverlaySearchInput" type="text" autocomplete="off" placeholder="<cms:contentText key="FIND_PEOPLE" code="participant.search.view" />" data-autocomp-delay="<c:out value="${beacon:autoCompleteDelay()}"/>"  data-autocomp-min-chars="2" />
                            </div>

                        </div>

                        <i class="icon-magnifier-1"></i>
                    </div>

                    <div class="totalRecordsFound"><span class="totalRecordsFoundText"></span> <cms:contentText key="PEOPLE_FOUND" code="participant.search.view" /></div>

                    <div class="hints-wrapper hide" >
                        <div class="search-hints ">
                            <div class="triangle"></div>
                            <ul class="tokens">

                            </ul>
                        </div>
                    </div>

                </div><!-- /.search-wrap -->
            </div><!-- /.search-control -->

            <div class="search-results">

                <div class="results-container">
                    <div class="card card-tall left-to-load">
                        <div class="left-to-load-container">
                            <div class='left-to-load-num'></div>
                            <div class="left-to-load-spinner hide"></div>
                        </div>
                    </div>
                </div>

                <div class="empty-search-message hide">
                    <div class="type-label"><cms:contentText key="TYPE_LABEL" code="participant.search.view" /></div>
                    <div class="type-hint"><cms:contentText key="TYPE_HINT" code="participant.search.view" /></div>
                </div>

                <div class="no-results hide">
                    <div class="image">
                        <div class="boo-image">
                            <i class="icon-ghost"></i>
                        </div>
                        <div class="caption">
                            <cms:contentText key="BOO" code="participant.search.view" />
                        </div>
                        <div class="no-data">
                            <cms:contentText key="NO_RESULT_FOUND" code="participant.search.view" />
                        </div>
                    </div>
                    <div class="suggestion">
                        <cms:contentText key="SUGGESTION_MSG" code="participant.search.view" />
                    </div>
                </div>
            </div><!-- /.search-results -->
        </div><!-- /.container -->

    </div><!-- /.PaxSearchView -->


    <div class="modal hide fade pax-modal-exit-confirm" >
        <div class="modal-header">
            <h3 class="modal-title"><cms:contentText key="DISCARD_SELECTED" code="participant.search.view" /></h3>
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="icon-close"></i></button>
        </div>
        <div class="modal-body">
            <cms:contentText key="DISCARD_PAXS" code="participant.search.view" />
        </div>
        <div class="modal-footer">
            <button class="btn btn-secondary btn-fullmobile" data-dismiss="modal"><cms:contentText key="KEEP_SEARCHING" code="participant.search.view" /></button>
            <button class="btn btn-danger exit-confirm btn-fullmobile"><cms:contentText key="LEAVE_SEARCH" code="participant.search.view" /></button>
        </div>
    </div>


    <div class="modal hide fade pax-modal-confirm" >
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="icon-close"></i></button>
        </div>
        <div class="modal-body">
            <cms:contentText key="ADD_ALERT_MSG" code="participant.search.view" />&nbsp; <span class='total-to-add'></span> <cms:contentText key="PEOPLE" code="participant.search.view" />?
        </div>
        <div class="modal-footer">
            <button class="btn btn-secondary btn-fullmobile" data-dismiss="modal"><cms:contentText key="CANCEL" code="participant.search.view" /></button>
            <button class="btn btn-primary add-all-btn btn-fullmobile"><cms:contentText key="ADD_ITEMS" code="participant.search.view" /></button>
        </div>
    </div>


    <div class="modal hide fade pax-modal-too-many" >
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="icon-close"></i></button>
        </div>
        <div class="modal-body">
            <cms:contentText key="PAX_MAX_LIMIT_REACHED1" code="participant.search.view" /> <span class='max-allowed'></span> <cms:contentText key="PAX_MAX_LIMIT_REACHED2" code="participant.search.view" />
        </div>
        <div class="modal-footer">
            <button class="btn btn-primary btn-fullmobile" data-dismiss="modal"><cms:contentText key="OK" code="participant.search.view" /></button>
        </div>
    </div>


    <div class="modal hide fade recognition ezRecognizeMiniProfileModal" data-backdrop="static" data-keyboard="false"></div>

</div><!-- /.searchWrapper -->


<!--subTpl.paxCardTpl=
    {{#iter participants}}
    {{#if ../recognition}}
        <div class="card card-large card-profile card-tall card-flip {{#if ../addanimation}}animateUp{{/if}} {{#unless participantUrl}}follow-lock{{/unless}}" data-id="{{id}}">
    {{else}}
        <div class="card card-large card-short card-profile {{#if ../addanimation}}animateUp{{/if}} {{#unless participantUrl}}follow-lock{{/unless}}" data-id="{{id}}">
    {{/if}}
       <div class="card-front">
            <div class="selectedOverlay"></div>
            <i class="icon-check-circle card-select-icon"></i>

            {{#if purlData.length}}
                {{#if purlData.0.anniversaryInt}}
                    <span class="celebrationInfo" title='<cms:contentText key="SHARE_A_MEMORY" code="purl.celebration.module"/>'>
                        <span class="promotion">{{purlData.0.anniversaryInt}}</span>
                        <span class="balloon"></span>
                        <span class='celebrationCircleMask'></span>
                    </span>
                {{/if}}
            {{/if}}

            <div class="card-top">
                <span class="avatar">
                <span class="avatarContainer">
                    {{#if avatarUrl}}
                        <img src="{{#timeStamp avatarUrl}}{{/timeStamp}}" alt="{{trimString firstName 0 1}}{{trimString lastName 0 1}}" />
                    {{else}}
                        <span class="avatar-initials">{{trimString firstName 0 1}}{{trimString lastName 0 1}}</span>
                    {{/if}}
                </span>
                    {{#if ../canFollow}}
                        {{#if follow}}
                            <span class="follow icon-user-add icon-user-check following unfollowReady"
                                data-follow="{{follow}}"
                                data-id="{{id}}"
                                data-title-follow="<cms:contentText key='FOLLOW' code='participant.profile' />"
                                data-title-unfollow="<cms:contentText key='UNFOLLOW' code='participant.profile' />">
                            </span>
                        {{else}}
                            <span class="follow icon-user-add followReady"
                                data-follow="{{follow}}"
                                data-id="{{id}}"
                                data-title-follow="<cms:contentText key='FOLLOW' code='participant.profile' />"
                                data-title-unfollow="<cms:contentText key='UNFOLLOW' code='participant.profile' />">
                            </span>
                        {{/if}}
                        {{#unless participantUrl}}
                            <span class="follow icon-user-lock follow-lock"> </span>
                        {{/unless}}
                    {{/if}}
                </span>
            </div>

            <div class="card-details">
                <div class='card-details-inner-wrap'>
                    {{#if participantUrl}}
                        <div class="participant-name" ><a href='{{participantUrl}}' data-title-page-view="<cms:contentText key="PUBLIC_PROFILE" code="participant.profile" />" >{{firstName}} {{lastName}}</a></div>
                    {{else}}
                        <div class="participant-name" >{{firstName}} {{lastName}}</div>
                    {{/if}}
                    <div class="participant-info">
                        {{#if jobName}}<span class="pi-title">{{jobName}}</span>{{/if}}
                        {{#if organization}}<span class="pi-org">{{organization}}</span>{{/if}}
                        {{#if departmentName}}<span class="pi-dept" data-toggle="tooltip" title="{{departmentNameTooltip}}" data-departmentNameTooltip="{{departmentNameTooltip}}">{{#if organization}}<i class="sep">|</i>{{/if}} {{departmentName}}</span>{{/if}}
                    </div>
                </div>
            </div>

            {{#if ../recognition}}
            <div class="card-action">
                <button type="button" class="btnRecognize btn btn-primary btn-block flipButton"><cms:contentText key="RECOGNIZE" code="participant.search.view" /></button>
            </div>
            {{/if}}
        </div>

        <div class="card-back">
            {{#gte promotions.length 4}}
            <div class="card-back-content card-filled">
            {{else}}
            <div class="card-back-content">
            {{/gte}}
                <span class="back-arrow flipButton"><i class="icon-arrow-2-circle-left"></i></span>

                <div class="card-top">
                    <span class="avatar">
                    <span class="avatarContainer">
                    {{#if avatarUrl}}
                        <img src="{{#timeStamp avatarUrl}}{{/timeStamp}}" alt="{{trimString firstName 0 1}}{{trimString lastName 0 1}}" />
                    {{else}}
                        <span class="avatar-initials">{{trimString firstName 0 1}}{{trimString lastName 0 1}}</span>
                    {{/if}}
                    </span>
                    </span>
                </div>

                <div class="card-details">
                    {{#if participantUrl}}
                        <div class="participant-name" ><a href='{{participantUrl}}' data-title-page-view="Public Profile">{{firstName}} {{lastName}}</a></div>
                    {{else}}
                        <div class="participant-name" >{{firstName}} {{lastName}}</div>
                    {{/if}}
                </div>

                <div class="promotions-container">
                    <div class="promotion hide-scrollbars">
                    {{#iter promotions}}
                    	{{#if attributes.isCheers}}
                    		<button type="button" class="btn btn-block btn-primary btnCheers cheers-popover" data-iseasy="true"  data-cheers-promotion-id="{{id}}" data-participant-id="{{../id}}"><i class="icon-cheers"></i><cms:contentText key="CHEERS" code="client.cheers.recognition" /></button>
                        {{else}}
                        	<button type="button" class="btn btn-block btn-primary btn-inverse" data-participant-id="{{../id}}" data-promotion-id="{{id}}" data-promotion-type="{{type}}" data-iseasy="{{attributes.isEasy}}"  data-url="{{url}}">{{name}}</button>
                    	{{/if}}
                    {{/iter}}
                    </div>

                    <span class="up-arrow hide"><i class="icon-arrow-1-circle-up" alt="up" data-id="{{id}}"></i></span>
                    <span class="down-arrow hide"><i class="icon-arrow-1-circle-down" alt="down" data-id="{{id}}"></i></span>
                </div>
            </div>{{! /.card-back-content }}

            {{#if purlData.length}}
                {{#gte purlData.length 2}}
            <div class="card-back-content-alt card-filled">
                {{else}}
            <div class="card-back-content-alt">
                {{/gte}}
                <span class="back-arrow-alt"><i class="icon-arrow-2-circle-left"></i></span>

                <div class="card-top">
                    <span class="avatar">
                    <span class="avatarContainer">
                    {{#if avatarUrl}}
                        <img src="{{#timeStamp avatarUrl}}{{/timeStamp}}" alt="{{trimString firstName 0 1}}{{trimString lastName 0 1}}" />
                    {{else}}
                        <span class="avatar-initials">{{trimString firstName 0 1}}{{trimString lastName 0 1}}</span>
                    {{/if}}
                    </span>
                    </span>
                </div>

                <div class="card-details">
                    {{#if participantUrl}}
                        <div class="participant-name" ><a href='{{participantUrl}}' data-title-page-view="Public Profile">{{firstName}} {{lastName}}</a></div>
                    {{else}}
                        <div class="participant-name" >{{firstName}} {{lastName}}</div>
                    {{/if}}
                </div>

                <div class="purlrec-container">
                    <ol class="unstyled">
                    {{#purlData}}
                        <li class="purlItem">
                            {{#if anniversaryInt}}
                                <span class="celebrationInfo">
                                    <span class="promotion">{{anniversaryInt}}</span>
                                    <span class="balloon"></span>
                                    <span class='celebrationCircleMask'></span>
                                </span>
                            {{/if}}
                            <h4>{{promotion}}</h4>

                            {{#if isToday}}
                            <p class="celebrationTimeLeft isToday">
                            {{else}}
                            <p class="celebrationTimeLeft">
                            {{/if}}
                                <i class="icon-clock"></i>{{timeLeft}}
                            </p>

                            <a href="{{contributeUrl}}" class="btn btn-primary btn-inverse btn-block btn-small"><cms:contentText key="SHARE_A_MEMORY" code="purl.celebration.module"/></a>
                        </li>
                    {{/purlData}}
                    </ol>
                </div>
            </div>{{! /.card-back-content-alt }}
            {{/if}}
        </div>
    </div>
    {{/iter}}
subTpl-->


<!--subTpl.paxFilterTpl=
    {{#iter this}}
        {{#eq id "name"}}
            <li class='tokenselector name-token active' data-id="{{id}}">
                <span class='hint-header' data-id="{{id}}"><cms:contentText key="SEARCHING_BY_NAME" code="participant.search.view" /></span>
                <div class="enter-btn"><cms:contentText key="ENTER" code="participant.search.view" /><i class="icon-back-1"></i></div>
                <span class='filter-count'><span class='num-count'>0</span> <cms:contentText key="FOUND" code="participant.search.view" /></span>
            </li>
        {{else}}
            <li class='tokenselector disabled' data-id="{{id}}">
                <i class="icon icon-plus-circle" data-id="{{id}}"></i>
                <span class='filter-count'><span class='num-count'>0</span> <cms:contentText key="FOUND" code="participant.search.view" /></span>
                <span class='hint-header' data-id="{{id}}">{{name}} :</span>
                <span class='hint-value filter-bold'>{{value}}</span>
                <div class="hints-container"></div>
            </li>
        {{/eq}}
    {{/iter}}
subTpl-->


<!--subTpl.paxSelectedFilterTpl=
    <span class="selected-filter" data-id="{{code}}">{{name}}:<span class='filter-bold'> {{value}}</span><span data-id="{{code}}" class="remove"><i class="icon icon-cancel-circle" ></i></span></span>
subTpl-->


<!--subTpl.paxAutoCompleteSuggetionsTpl=
    <ul>
        {{#iter this}}
            <li data-field-id="{{id}}">{{name}}</li>
        {{/iter}}
    </ul>
subTpl-->
