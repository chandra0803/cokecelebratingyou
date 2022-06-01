<%@ include file="/include/taglib.jspf"%>
<div class="">

    <div id="activeLevelMask" class="mask" style="display:none"></div>

    <div class="levels container-splitter with-splitter-styles">

        <div class="lockUI" style="display:none"></div>

        <div class="levelItem noLevels text-error" style="display:none">
            <i class="icon-warning-circle"></i>
            <cms:contentText key="NO_LEVEL_ADDED" code="ssi_contest.payout_stepitup" />
        </div>

        <div class="levelItem headerDescriptions" style="display: none">
            <div class="levelHeader levelSequenceNumber">
                <span><cms:contentText key="LEVEL" code="ssi_contest.participant" /></span>
            </div>
            <div class="levelHeader levelAmount">
                <span class="msg measureLabel amount hide"><cms:contentText key="AMOUNT" code="ssi_contest.participant" /></span>
                <span class="msg measureLabel currency hide"><cms:contentText key="CURRENCY" code="ssi_contest.payout_dtgt" /></span>
                <span class="msg measureLabel percentOverBaseline hide"><cms:contentText key="PERCENTAGE_OVER_BASELINE" code="ssi_contest.payout_stepitup" /></span>
                <span class="msg measureLabel amountOverBaseline hide"><cms:contentText key="AMOUNT_OVER_BASELINE" code="ssi_contest.payout_stepitup" /></span>
                <span class="msg measureLabel currencyOverBaseline hide"><cms:contentText key="CURRENCY_OVER_BASELINE" code="ssi_contest.payout_stepitup" /></span>
            </div>
            <div class="levelHeader levelPayout">
                <span><cms:contentText key="PAYOUT" code="ssi_contest.payout_objectives" /></span>
            </div>
            <div class="levelHeader levelPayoutDesc">
                <span>
                    <cms:contentText key="PAYOUT_DESCRIPTION" code="ssi_contest.payout_objectives" />
                    <i class="icon-info pageView_help"
                        data-help-content="<cms:contentText key="SIU_PAY_DESC_INFO" code="ssi_contest.payout_objectives" />"></i>
                </span>
            </div>
            <div class="levelHeader levelBadge">
                <span><cms:contentText key="BADGE" code="ssi_contest.payout_objectives" /></span>
            </div>
            <div class="levelHeader levelEdit">
                <span><cms:contentText key="EDIT" code="ssi_contest.participant" /></span>
            </div>
            <div class="levelHeader levelRemove">
                <span><cms:contentText key="REMOVE" code="ssi_contest.pax.manager" /></span>
            </div>
        </div><!-- /.levelItem.headerDescriptions -->

        <div class="levelList ui-sortable">
            <!-- levels rendered here -->
        </div><!-- /.levelList -->

        <div class="newLevel">
            <!-- single new level rendered here -->
        </div><!-- /.newLevel -->


        <div class="bottomControls">
            <button class="btn btn-primary addLevelBtn">
                 <cms:contentText key="ADD_LEVEL" code="ssi_contest.payout_stepitup" /> <i class="icon-plus-circle"></i>
            </button>
        </div><!-- /.bottomControls -->


        <div class="levelRemoveDialog" style="display:none">
            <p>
                <b><cms:contentText key="REMOVE_LEVEL" code="ssi_contest.payout_stepitup" /></b>
            </p>
            <p>
                <cms:contentText key="ALL_LEVEL_DATA_LOST" code="ssi_contest.payout_stepitup" />
            </p>
            <p class="tc">
                <a class="btn btn-small confirmBtn closeTip"><cms:contentText key="YES" code="ssi_contest.preview" /></a>
                <a class="btn btn-small cancelBtn closeTip"><cms:contentText key="NO" code="ssi_contest.preview" /></a>
            </p>
        </div><!-- /.levelRemoveDialog -->


    </div><!-- /.levels -->



</div>
