<%@ include file="/include/taglib.jspf"%>

<div class="row-fluid participantSearchSelectAndInput">

    <div class="span12 filterCreationModeSelectWrapper" style="display:none">
        <ul class="nav nav-tabs">
            <li class="active presetFilterBtn">
                <a href="#">
                    <i class="icon-g5-team"></i>
                    <span class="presetFilters_name"></span>
                </a>
            </li>
            <li class="defaultFilterBtn">
                <a href="#">
                    <i class="icon-search"></i>
                    {{defaultFilterCreationBtnLabel}}
                </a>
            </li>
        </ul>
    </div><!-- /.filterCreationModeSelectWrapper -->

    <div class="span12 presetFilterCreationWrapper" style="display:none">
        <select class="presetFilterSelect">
            <!--subTpl.presetFilters=
                <option value="">{{instruction}}</option>
                {{#filters}}
                    <option value='{"type":"{{type}}","typeName":"{{typeName}}","id":"{{id}}","name":"{{name}}"}'>{{name}}</option>
                {{/filters}}
            subTpl-->
        </select>
    </div><!-- /.presetFilterCreationWrapper -->

    <div class="span12 defaultFilterCreationWrapper">
        <select class="participantSearchSelect">
            {{#searchTypes}}
                <option value="{{this.id}}">{{this.name}}</option>
            {{/searchTypes}}
        </select>
        <div class="dropdown participantSearchDropdownWrapper" >
            <input type="text" data-placeholder="<cms:contentText key="HERE" code="participant.search" />" placeholder="enter text here" class="participantSearchInput dropdown-toggle">
            <ul class="dropdown-menu participantSearchDropdownMenu" role="menu"
                data-msg-instruction="<cms:contentText key="START_TYPING" code="participant.search" />"
                data-msg-no-results="<cms:contentText key="NO_RESULTS" code="participant.search" />">
                <!-- dynamic -->
            </ul>
        </div>
    </div><!-- /.defaultFilterCreationWrapper -->

</div><!-- /.row-fluid.participantSearchSelectAndInput -->

<div class="row-fluid participantSearchCountAndFilters" style="display:none">

    <div class="span1 participantSearchResultCountWrap">
        <span class="participantSearchResultCount badge badge-inverse">0</span>
        <cms:contentText key="RESULTS" code="participant.search" />
    </div>

    <!--subTpl.participantSearchFilter=
        <span class="filter xlabel xlabel-info" data-ac-type="{{type}}">
            <a class="filterDelBtn btn btn-mini" href="#" data-ac-type="{{type}}"><i class="icon-close"></i></a>
            <span class="muted">{{typeName}}:</span> <strong>{{name}}</strong>
        </span>
    subTpl-->

    <div class="span11 participantSearchFilters">
        <span class="filteredBy"><cms:contentText key="FILTERED_BY" code="participant.search" /></span>
        <!-- dynamic -->
    </div>
</div><!-- /.row-fluid.participantSearchCountAndFilters -->

<div class="row-fluid participantSearchMessagesAndResults">

    <div class="span12 participantSearchMsg" style="display:none">
        <div class="alert">
            <span class="msg">
                Some message.
            </span>
        </div>
    </div>

    <div class="tableMsg alert alert-error" style="display:none">
        <a href="#" class="close">
            <i class="icon-close"></i>
        </a>
        <div class="msgContent">
            <!-- dynamic -->
        </div>
    </div>

    <div class="span12 participantSearchTableWrapper" style="display:none">
        <!-- NOTE: data-msg-no-results attr used for cases where no participants are
                    returned and no message is returned.
        -->
        <table class="table table-striped table-bordered table-condensed"
            data-msg-no-results="<cms:contentText key="NO_PARTICIPANTS" code="participant.search" />">
            <thead>
                <tr>
                    <th class="selectHeader">
                        <span class="msgSelect">
                            <cms:contentText key="SELECT" code="system.button" />
                        </span>
                        <button class="btn btn-mini selectAllBtn" style="display:none">
                            <cms:contentText key="SELECT_ALL" code="profile.personal.info" />
                        </button>
                    </th>
                    <th class="sortHeader nameHeader">
                        <cms:contentText key="NAME" code="profile.personal.info" />
                        <i data-sort="lastName" class="sortControl"></i>
                    </th>
                    <th class="sortHeader orgHeader">
                        <cms:contentText key="ORG_NAME" code="profile.personal.info" />
                        <i data-sort="orgName" class="sortControl"></i>
                    </th>
                    <th class="sortHeader countryHeader">
                        <cms:contentText key="COUNTRY" code="profile.personal.info" />
                        <i data-sort="countryName" class="sortControl"></i>
                    </th>
                    <th class="sortHeader departmentHeader">
                        <cms:contentText key="DEPARTMENT" code="profile.personal.info" />
                        <i data-sort="departmentName" class="sortControl"></i>
                    </th>
                    <th class="sortHeader jobHeader">
                        <cms:contentText key="JOB_TITLE" code="participant.search" />
                        <i data-sort="jobName" class="sortControl"></i>
                    </th>
                </tr>
            </thead>
            <tbody>
                <!-- dynamic -->
            </tbody>
        </table>
    </div><!-- /.participantSearchTableWrapper -->

</div><!-- /.row-fluid.participantSearchMessagesAndResults -->

<div class="visibilityControls participantSearchVisibilityControls" style="display:none">
    <div class="visibilityControlsLiner">
        <a class="showHideBtn btn btn-primary"
            data-msg-show-single="Set Participant"
            data-msg-show="<cms:contentText key="MORE_PAX" code="leaderboard.label" />"
            data-msg-hide="<cms:contentText key="DONE_ADDING" code="profile.proxies.tab"/>">
        </a>
    </div>
</div>
