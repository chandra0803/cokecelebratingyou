<%@ page import="com.biperf.core.domain.enums.nomination.NominationsSubmitConstants"%>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="java.util.*"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>

<!-- ======== NOMINATIONS SUBMIT PAGE ======== -->
<div id="nominationsSubmitPageView" class="nominationsSubmitEdit">

    <div class="row-fluid">
        <div class="span12">

            <!-- JAVA NOTE: in the rare event that the server has an error (FE should catch all errors) -->
            <!--div class="alert alert-block alert-error">
                <button type="button" class="close" data-dismiss="alert">×</button>
                <div class="error">
                    <h4>The following errors occurred:</h4>
                    <ul>
                        <li>Owner must be selected</li>
                        <li>Date for recipient send is not a date. Date must follow the format MM/DD/YYYY.</li>
                    </ul>
                </div>
            </div-->

            <div class="wizardTabsVerticalContainer fl">

                <!-- **************************************************************
                    WIZARD TABS VERTICAL
                 ***************************************************************** -->
                <div class="wizardTabsVerticalView" data-content=".wizardTabsContent">
                    <!-- generated using json+tpl by WizardTabsVerticalView -->
                </div><!-- /.wizardTabsVerticalView -->

                <!-- **************************************************************
                    WIZARD TABS VERICAL CONTENT
                 ***************************************************************** -->
                 <!--After the template loads we move the stepContent from here and place it under the matching stepTab inside .wizardTabsVerticalView so the flow matches creative -->
                <div class="wizardTabsVertContent">

                    <!-- **************************************************************
                        NOMINEE
                    ************************************************************** -->
                    <div id="nominationsTabNomineeView" class="stepNomineeContent stepContent" style="display:none">
                    </div>

                    <!-- **************************************************************
                        BEHAVIORS
                    ************************************************************** -->
                    <div id="nominationsTabBehaviorView" class="stepBehaviorContent stepContent" style="display:none">


                    </div>

                    <!-- **************************************************************
                        ECARD
                    ************************************************************** -->
                    <div id="nominationsTabEcardView" class="stepEcardContent stepContent" style="display:none">


                    </div>

                    <!-- **************************************************************
                        WHY
                    ************************************************************** -->
                    <div id="nominationsTabWhyView" class="stepWhyContent stepContent" style="display:none">

                    </div><!-- /#nominationsTabWhyView-->
                </div><!-- /.wizardTabsVertContent-->

                <!-- VALIDATION MSGS - informational tooltip for validation -->
                <div class="errorTipWrapper" style="display:none">
                    <div class="errorTip">

                        <!-- display for validateme generic errors -->
                        <div class="errorMsg msgGenericError">
                            <cms:contentText key="CORRECT_THE_ERRORS" code="promotion.nomination.submit" />
                            <i class="icon-warning-circle"></i>
                        </div>

                        <!-- Nominee errors -->
                        <div class="errorMsg msgNoParticipants">
                            <cms:contentText key="ATLEAST_ONE_PARTICIPANT" code="promotion.nomination.submit" />
                            <i class="icon-warning-circle"></i>
                        </div>

                        <div class="errorMsg msgTeamName">
                            <cms:contentText key="ENTER_TEAM_NAME" code="promotion.nomination.submit" />
                            <i class="icon-warning-circle"></i>
                        </div>

                        <div class="errorMsg msgNoAward">
                            <cms:contentText key="AWARD_AMOUNT_EACH_PARTICIPANT" code="promotion.nomination.submit" />
                            <i class="icon-warning-circle"></i>
                        </div>

                        <div class="errorMsg msgIndividualPax">
                            <cms:contentText key="ONE_PAX_FOR_INDIVITUAL_NOMINATION" code="promotion.nomination.submit" />
                            <i class="icon-warning-circle"></i>
                        </div>

                        <div class="errorMsg maxParticipantsReached">
                            <cms:contentText key="MAX_PAX_REACHED" code="promotion.nomination.submit" />
                            <i class="icon-warning-circle"></i>
                        </div>

                        <div class="errorMsg maxParticipantsGroup">
                            <cms:contentText key="MAX_GROUP_PAX_REACHED" code="promotion.nomination.submit" />
                            <i class="icon-warning-circle"></i>
                        </div>

                         <div class="errorMsg awardOutOfRange">
                            <cms:contentText key="AWARD_VALUE_RANGE" code="promotion.nomination.submit" />
                            <i class="icon-warning-circle"></i>
                        </div>
                        
                        <div class="errorMsg fillAward">
                            <i class="icon-warning-circle"></i>
                            <cms:contentText key="FILL_AWARD_AMOUNT" code="promotion.nomination.submit" />
                        </div>

                        <div class="errorMsg msgGroupName">
                            <cms:contentText key="GROUP_NAME_UNIQUE" code="promotion.nomination.submit" />
                            <i class="icon-warning-circle"></i>
                        </div>

                        <!-- Behavior errors -->
                        <div class="errorMsg msgNoBehaviors">
                            <cms:contentText key="ATLEAST_ONE_BEHAVIOR" code="promotion.nomination.submit" />
                            <i class="icon-warning-circle"></i>
                        </div>
                        <!-- eCard Errors -->
                        <div class="errorMsg msgNoECard">
                            <cms:contentText key="YOU_MUST_SELECT" code="promotion.nomination.submit" />
                            <i class="icon-warning-circle"></i>
                        </div>
                    </div><!-- /.errorTip -->
                </div><!-- /.errorTipWrapper -->

                <!-- modal to catch errors from server on submit OR save draft -->
                <div class="modal hide fade nomsErrorsModal">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal"><i class="icon-close"></i></button>
                        <h3>
                            <i class="icon-warning-circle"></i>
                            <cms:contentText key="ERRORS_ENCOUNTERED" code="promotion.nomination.submit" />
                        </h3>
                    </div>
                    <div class="modal-body">
                        <ul class="errorsList">
                            <!-- dynamic -->
                        </ul>
                    </div>
                    <div class="modal-footer">
                        <a href="#" class="btn" data-dismiss="modal"><cms:contentText key="CLOSE" code="promotion.nomination.submit" /></a>
                    </div>
                </div><!-- /.nomsErrorsModal -->

                <div class="cancelNominationPopover" style="display:none">
                    <p>
                        <b><cms:contentText key="CANCEL_NOMINATION" code="promotion.nomination.submit" /></b>
                    </p>
                    <p>
                        <cms:contentText key="DELETE_NOMINATION" code="promotion.nomination.submit" />
                    </p>
                    <p class="tc">
                        <button href="${pageContext.request.contextPath}/homePage.do" id="nominationCancelConfirmBtn" class="btn btn-primary"><cms:contentText code="system.common.labels" key="YES"/></button>
                        <button id="nominationDoNotCancelBtn" class="btn"><cms:contentText code="system.common.labels" key="NO"/></button>
                    </p>
                </div><!-- /.cancelNominationPopover -->

                <!-- modal after successfully submitting a nomination -->
                <div class="modal hide fade nominationSubmittedModal tc" data-backdrop="static">
                    <div class="modal-header">
                        <h3>
                           <cms:contentText key="SUCCESS" code="promotion.nomination.submit" />
                        </h3>
                    </div>
                    <div class="modal-body">
                        <p><cms:contentText key="NOMINATION_SUBMITTED_FOR_REVIEW" code="promotion.nomination.submit" /></p>

                        <a href="${pageContext.request.contextPath}/homePage.do" class="btn btn-primary"><cms:contentText key="BACK" code="promotion.nomination.submit" /></a>
                    </div>
                </div><!-- /.nomsErrorsModal -->
            </div><!-- /.wizardTabsVerticalContainer-->
        </div>
    </div>

</div><!--/#nominationsSubmitPageView-->

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
<%pageContext.setAttribute("nomsSubmitDataUrl", NominationsSubmitConstants.GET_NOMINATIONS_SUBMIT_DATA_URL );%>
<%pageContext.setAttribute("tabMenu", NominationsSubmitConstants.NOMINATIONS_SUBMIT_TAB );%>
<%pageContext.setAttribute("rootUrl", NominationsSubmitConstants.NOMINATION_SUBMIT_ROOT );%>
<%pageContext.setAttribute("getParticipantData", NominationsSubmitConstants.GET_PAX_DATA_URL );%>
</script>
