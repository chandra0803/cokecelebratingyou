<%@ include file="/include/taglib.jspf"%>
<div class="">

    <div id="activeActivityMask" class="mask" style="display:none"></div>

    <div class="activities container-splitter with-splitter-styles">

        <div class="lockUI" style="display:none"></div>

        <div class="activityItem noActivities text-error" style="display:none">
            <i class="icon-warning-circle"></i>
            <cms:contentText key="NO_ACTIVITIES_ADDED" code="ssi_contest.payout_dtgt" />
        </div>

        <div class="activityItem headerDescriptions" style="display: none">
            <div class="activityHeader activityDesc">
                <span><cms:contentText key="ACTIVITY_DESCRIPTION" code="ssi_contest.payout_dtgt" /></span>
            </div>
            <div class="activityHeader activityForEvery">
                <span><cms:contentText key="FOR_EVERY" code="ssi_contest.payout_dtgt" /></span>
            </div>
            <div class="activityHeader activityEarn">
                <span><cms:contentText key="EARN" code="ssi_contest.payout_dtgt" /></span>
            </div>
            <div class="activityHeader activityMinQual">
                <span><cms:contentText key="MINIMUM_QUALIFIER" code="ssi_contest.payout_dtgt" /></span>
            </div>
            <div class="activityHeader activityIndPayCap">
                <span><cms:contentText key="INDIVIDUAL_PAYOUT_CAP" code="ssi_contest.payout_dtgt" /></span>
            </div>
            <div class="activityHeader activityMaxActPay">
                <span><cms:contentText key="MAXIMUM_ACTIVITY_PAYOUT" code="ssi_contest.payout_dtgt" /></span>
            </div>
            <div class="activityHeader activityActGoal">
                <span><cms:contentText key="ACTIVITY_GOAL" code="ssi_contest.payout_dtgt" /></span>
            </div>
            <div class="activityHeader activityMaxActPot">
                <span><cms:contentText key="MAXIMUM_ACTIVITY_POTENTIAL" code="ssi_contest.payout_dtgt" /></span>
            </div>
            <div class="activityHeader activityEdit">
                <span><cms:contentText key="EDIT" code="ssi_contest.payout_dtgt" /></span>
            </div>
            <div class="activityHeader activityRemove">
                <span><cms:contentText key="REMOVE" code="ssi_contest.payout_dtgt" /></span>
            </div>
        </div><!-- /.activityItem.headerDescriptions -->

        <div class="activityList">
            <!-- activities rendered here -->
        </div><!-- /.activityList -->

        <div class="newActivity">
            <!-- single new activity rendered here -->
        </div><!-- /.newActivity -->


        <div class="activityItem footer" style="display: none">
            <div class="activityDesc">
                <span><cms:contentText key="TOTAL" code="ssi_contest.payout_dtgt" /></span>
            </div>
            <div class="activityForEvery">&nbsp;</div>
            <div class="activityEarn">&nbsp;</div>
            <div class="activityMinQual">&nbsp;</div>
            <div class="activityIndPayCap">&nbsp;</div>
            <div class="activityMaxActPay">
                <span class="currSymb pay">
                </span><span class="totMaxActPay"></span>
                <!--span class="currDisp pay"></span-->
            </div>
            <div class="activityActGoal">&nbsp;</div>
            <div class="activityMaxActPot">&nbsp;</div>
            <div class="activityEdit">&nbsp;</div>
            <div class="activityRemove">&nbsp;</div>
        </div><!-- /.activityItem.footer -->


        <div class="bottomControls">
            <button class="btn btn-primary addActivityBtn">
                 <cms:contentText key="BTN_ADD_ANOTHER_ACTIVITY" code="ssi_contest.payout_dtgt" /> <i class="icon-plus-circle"></i>
            </button>
        </div><!-- /.bottomControls -->


        <div class="activityRemoveDialog" style="display:none">
            <p>
                <b><cms:contentText key="REMOVE_ACTIVITY" code="ssi_contest.payout_dtgt" /></b>
            </p>
            <p>
                <cms:contentText key="ALL_DATA_LOST" code="ssi_contest.payout_dtgt" />
            </p>
            <p class="tc">
                <a class="btn btn-primary btn-small confirmBtn closeTip"><cms:contentText key="YES" code="ssi_contest.payout_dtgt" /></a>
                <a class="btn btn-small cancelBtn closeTip"><cms:contentText key="NO" code="ssi_contest.payout_dtgt" /></a>
            </p>
        </div><!-- /.activityRemoveDialog -->


    </div><!-- /.activities -->



</div>
