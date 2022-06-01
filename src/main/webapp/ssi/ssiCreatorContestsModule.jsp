<!-- ======== SSI MODULE ======== -->
<%@ include file="/include/taglib.jspf"%>
<script type="text/template" id="SSICreatorContestsModuleTpl">
<div class="module-liner">

    <div class="module-content">
        <div class="createContestSlide">
           <form class="contestCreate" action="ssi/createGeneralInfo.do?method=prepareCreate" method="post" accept-charset="utf-8" novalidate>
                <input type="hidden" name="locale" value="en_US">

                <fieldset>
                    <div class="module-flip-title">
                        <h3 class="module-title"><cms:contentText key="CREATE_A_CONTEST" code="ssi_contest.creator" /></h3>
                    </div>

                    <div class="module-actions">
                        <a href="ssi/creatorContestList.do?method=display" class="myContestsLink"><cms:contentText key="My_CONTESTS" code="ssi_contest.creator" /></a>
                    </div>

                    <div class="contestNameWrap">
                        <label for="contestName" class="contestNameLabel"><cms:contentText key="NAME_CONTEST" code="ssi_contest.creator" /></label>

                        <div class="contestStatus contestNameStatus">
                            <input type="text" class="contestName" name="contestName" value="" placeholder="<cms:contentText key="NAME_CONTEST" code="ssi_contest.creator" />" maxlength="50">

                            <span class="pending"></span>

                            <div class="iconWrap">
                                <i class="valid icon-check-circle"></i>
                                <i class="invalid icon-cancel-circle"></i>
                            </div>

                            <p class="msg"></p>
                        </div>
                    </div>

                    <div class="selectSubmitWrap">
                        <div class="selectWrap">
                            <input type="hidden" class="contestType" name="contestType"  />

                            <span class="selectText"><cms:contentText key="SELECT_TYPE" code="ssi_contest.creator" /></span>
                            <a href="#" class="showContestQtip"><cms:contentText key="SHOW_DETAILS" code="ssi_contest.creator" /></a>
                            <a href="#" class="hideContestQtip"><cms:contentText key="HIDE_DETAILS" code="ssi_contest.creator" /></a>

                            {{#each contestTypes}}
                            <div class="card">
                                <div class="card-front">
                                    <div class="selectedOverlay">
                                        <button type="submit" class="btn btn-primary invert-btn submitForm" disabled="disabled"><cms:contentText key="NEXT" code="system.button" /></button>
                                    </div>

                                    <i class="icon-check-circle card-select-icon" data-value="{{contestType}}"></i>

                                    <div class="card-top">
                                        {{#eq contestType "objectives"}}
                                            <span class="avatar">
                                                <span class="avatarContainer">
                                                    <i class="icon-g5-ssi-objectives card-icon"></i>
                                                </span>
                                            </span>
                                        {{/eq}}
                                        {{#eq contestType "stepItUp"}}
                                            <span class="avatar">
                                                <span class="avatarContainer">
                                                    <i class="icon-g5-ssi-stepItUp card-icon"></i>
                                                </span>
                                            </span>
                                        {{/eq}}
                                        {{#eq contestType "doThisGetThat"}}
                                            <span class="avatar">
                                                <span class="avatarContainer">
                                                    <i class="icon-g5-ssi-doThisGetThat card-icon"></i>
                                                </span>
                                            </span>
                                        {{/eq}}
                                        {{#eq contestType "stackRank"}}
                                            <span class="avatar">
                                                <span class="avatarContainer">
                                                    <i class="icon-g5-ssi-stackRank card-icon"></i>
                                                </span>
                                            </span>
                                        {{/eq}}
                                        {{#eq contestType "awardThemNow"}}
                                            <span class="avatar">
                                                <span class="avatarContainer">
                                                    <i class="icon-g5-ssi-awardThemNow card-icon"></i>
                                                </span>
                                            </span>
                                        {{/eq}}
                                    </div> <!-- /.card-top -->

                                    <div class="card-details">
                                        <h5 class="contestTypeName headline_5">{{contestTypeName}} <cms:contentText key="CONTEST" code="ssi_contest.creator" /></h5>
                                        <p class="contestDescriptionText">{{description}}</p>
                                    </div>
                                </div>
                            </div>
                            {{/each}}
                        </div>

                        <%-- <div class="submitWrap">
                            <button type="submit" class="btn btn-primary submitForm" disabled="disabled"><cms:contentText key="NEXT" code="system.button" /></button>
                        </div> --%>
                    </div>
                </fieldset>
            </form>
        </div>
    </div> <!-- ./module-content -->
</div>
</script>
<script>
	$(document).ready(function() {
		G5.props.URL_JSON_SSI_AVAILABLE_CONTEST_TYPES = G5.props.URL_ROOT+'ssi/createContest.do?method=fetchAvailableContestTypes';
		G5.props.URL_JSON_CONTEST_CHECK_NAME = G5.props.URL_ROOT+'ssi/createContest.do?method=validateContestName';

	});
</script>
