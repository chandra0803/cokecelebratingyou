<%@ include file="/include/taglib.jspf"%>
<%@page import="com.biperf.core.utils.UserManager"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

<div id="ssiUpdateResultsPageView" class="page-content">
    <div class="row-fluid">
        <div class="span6">
            <h2>${name}</h2>
        </div>

        <!--JAVA NOTE: Display error text in the below list when page submit returns an error and set the hidden input value to true. -->
        <!--<div id="ssiUpdateResultsErrorBlock" class="alert alert-block alert-error">
            <h4>The following errors occurred</h4>
            <ul>
                <li>This is an example of a specific error message</li>
            </ul>
            <input type="hidden" id="serverReturnedErrored" value="false">
        </div>-->
    </div>

    <div class="row-fluid">
        <div class="span12 uploadInProgress text-error" style="display:none">
            <p><cms:contentText key="UPDATE_RESULTS_IN_PROGRESS" code="ssi_contest.creator" /></p>
        </div>
    </div>

<c:if test="${canShowOnlineForm eq true }">
    <div class="row-fluid">
        <div class="span12">
            <p><cms:contentText key="UPDATE_RESULTS_PROCESS" code="ssi_contest.creator" /></p>
        </div>
    </div>

    <div class="row-fluid ssiEnterActivityWrap">
        <div class="span12">
            <h3 class="ssiToggleView" data-toggle="ssiEnterActivityForm"><cms:contentText key="ENTER_ACTIVITY_ONFILE" code="ssi_contest.creator" /></h3>
        </div>

        <form id="ssiEnterActivityForm" class="ssiShowHideForm span12" method="post" action="contestResults.do?method=save&id=${id}#contest/${id}" style="display: none">

            <div class=" control-group validateme"
                data-validate-fail-msgs='{"nonempty":"<cms:contentText key="ACTIVITY_DATE_MUST" code="ssi_contest.creator" />"}'
                data-validate-flags='nonempty' >
                <label for="ssiEnterActivityDate" class="control-label"><cms:contentText key="ACTIVITY_DATE" code="ssi_contest.creator" /></label>
                <div class="controls">
                    <!--
                        NOTE: JSP set data-date-format AND data-date-startdate
                    -->
                    <span class="input-append datepickerTrigger"
                            data-date-format="<%=UserManager.getUserDatePattern().toLowerCase()%>"
                            data-date-language="<%=UserManager.getUserLocale()%>"
                            data-date-startdate="08/14/2014"
                            data-date-todaydate="08/14/2014"
                            data-date-autoclose="true">
                        <input class="date datepickerInp"
                                type="text"
                                id="ssiEnterActivityDate"
                                name="ssiEnterActivityDate"
                                placeholder=""
                                readonly="readonly"><button class="btn datepickerBtn" type="button"><i class="icon-calendar"></i></button>
                    </span>
                </div>

            </div>

            <div class="ssiEnterActivityTableWrap">
                <!-- dynamic from ssiUpdateResultsActivityTable.html-->
            </div>

            <div class="ssiUploadFormActions">
                <button class="btn btn-primary ssiUpdateNext"><cms:contentText key="NEXT" code="system.button" /></button>
                <button class="btn btn-primary ssiUpdateSave" style="display:none"><cms:contentText key="SAVE" code="system.button" /></button>
                <button class="btn ssiUpdateCancel"><cms:contentText key="CANCEL" code="system.button" /></button>
            </div>
        </form>
    </div>

    <div class="row-fluid">
        <div class="span12">
            <h4><cms:contentText key="OR" code="ssi_contest.creator" /></h4>
        </div>
    </div>
</c:if>
    <div class="row-fluid ssiUploadSpreadsheet">
        <div class="span12">
            <h3 class="ssiToggleView" data-toggle="ssiUploadSSForm"><cms:contentText key="UPLOAD_SPREAD_SHEET" code="ssi_contest.creator" /></h3>
        </div>

        <form id="ssiUploadSSForm" class="ssiShowHideForm span12" method="post" action="uploadContestResults.do?method=uploadContestResults&id=${id}#contest/${id}" style="display: none" enctype="multipart/form-data">

            <div class="ssiUploadSSInstructions">
                <p><cms:contentText key="UPLOAD_STEPS_DETAILS" code="ssi_contest.creator" /></p>
                <div class="instructionStep">
                    <h4><cms:contentText key="UPLOAD_STEP_1" code="ssi_contest.creator" /></h4>
                    <p><cms:contentText key="UPLOAD_STEP_1_DTL" code="ssi_contest.creator" /></p>
                    <a href="contestDownloadAction.do?method=downloadContest&id=${id}" class="btn btn-primary exportCsvButton">  <cms:contentText key="DOWNLOAD_TEMPLATE" code="ssi_contest.creator" /> <i class="icon icon-download-1"></i></a>
                </div>

                <div class="instructionStep">
                    <h4><cms:contentText key="UPLOAD_STEP_2" code="ssi_contest.creator" /></h4>
                    <p><cms:contentText key="UPLOAD_STEP_2_DTL" code="ssi_contest.creator" /></p>
                </div>

                <div class="instructionStep">
                    <h4><cms:contentText key="UPLOAD_STEP_3" code="ssi_contest.creator" /></h4>
                    <p><cms:contentText key="UPLOAD_STEP_3_DTL" code="ssi_contest.creator" /></p>
                    <strong><cms:contentText key="UPLOAD_SPREADSHEET" code="ssi_contest.creator" /></strong>
                </div>
            </div>

            <div class="ssiUploadSSButton control-group validateme"
                 data-validate-fail-msgs='{"nonempty": "<cms:contentText key="SPREAD_SHEET_REQUIRED" code="ssi_contest.creator" />"}'
                 data-validate-flags='nonempty'
                 data-extra-validate='<cms:contentText key="UPLOAD_RESULTS_FILE_TYPE" code="ssi_contest.creator" />'>

                <div class="controls">
                    <input type="file" id="ssiHiddenUpload" name="ssiHiddenUpload" />
                </div>
                                
                <div class="controls">
                    <input type="hidden" id="saveAndSendProgressUpdate" name="saveAndSendProgressUpdate" />
                </div>                
            </div>

            <div class="ssiUploadFormActions">
                <button class="btn btn-primary ssiUpdateSave"><cms:contentText key="SAVE" code="system.button" /></button>
                <button class="btn ssiUpdateCancel"><cms:contentText key="CANCEL" code="system.button" /></button>
            </div>
        </form>
    </div>

    <!-- Send Progress Update tooltip: shown on Save action -->
    <div class="ssiSaveSendProgress" style="display:none">
        <p><cms:contentText key="UPDATE_NOTICE_ON_SAVE" code="ssi_contest.creator" /></p>

        <p class="tc">
            <button class="btn btn-primary sendProgressConfirmSubmit" name="saveAndSendProgressUpdate" value="yes"><cms:contentText key="YES" code="system.button" /></button>
            <button class="btn sendProgressConfirmSubmit" name="saveAndSendProgressUpdate" value="no" ><cms:contentText key="NO" code="system.button" /></button>
        </p>
    </div>

    <!-- Cancel update results tooltip: shown on Cancel action -->
    <div class="ssiUpdateCancelConfirm" style="display:none">
        <p><cms:contentText key="UPDATE_NOTICE_ON_CANCEL" code="ssi_contest.creator" /></p>
        <form id="ssiSendProgress" action="contestResults.do?method=cancelUpdate&id=${id}" method="post">
            <p class="tc">
                <button class="btn btn-primary sendProgressConfirmCancel" name="cancelAndSendProgress" value="yes"><cms:contentText key="YES" code="system.button" /></button>
                <button class="btn sendProgressConfirmCancel" name="cancelAndSendProgress" value="no"><cms:contentText key="NO" code="system.button" /></button>
            </p>
        </form>
    </div>

    <!-- modal to catch errors from server on submit OR save draft -->
    <div class="modal hide fade contestErrorsModal">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal"><i class="icon-close"></i></button>
            <h3>
                <i class="icon-warning-circle"></i>
                            <cms:contentText key="ERRORS_ENCOUNTERED" code="ssi_contest.creator" />
            </h3>
        </div>
        <div class="modal-body">
            <ul class="errorsList text-error">
                <html:messages id="error">
					<li><c:out value="${error}" /></li>
				</html:messages>
            </ul>
        </div>
        <div class="modal-footer">
            <a href="#" class="btn" data-dismiss="modal"><cms:contentText key="CLOSE" code="system.button" /></a>
        </div>
    </div><!-- /.contestErrorsModal -->


</div><!--/#ssiUpdateResultsPageView -->
<!-- TODO; improve; work around for multiline line js variables; -->
<input type="hidden" id="contestId" value="${id}"/>
<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>

    $(document).ready(function() {
    	//G5.props.URL_JSON_SSI_UPDATE_RESULTS_TABLE = G5.props.URL_ROOT+'assets/ajax/ssiUpdateResultsData.json';
    	<!-- TODO; improve; work around for multiline line js variables; -->
    	G5.props.URL_JSON_SSI_UPDATE_RESULTS_TABLE = G5.props.URL_ROOT+'ssi/contestResults.do?id='+document.getElementById("contestId").value;
    	G5.props.URL_JSON_SSI_UPDATE_RESULTS_UPLOAD = G5.props.URL_ROOT+'ssi/uploadContestResults.do?method=uploadContestResults&id='+document.getElementById("contestId").value;
    	//G5.props.URL_JSON_SSI_UPDATE_RESULTS_UPLOAD = G5.props.URL_ROOT+'assets/ajax/ssiUpdateResultsUploadSS.json';
    	//Bug# 61243, commenting out feature for large audience, moved to next phase
    	//G5.props.REPORTS_LARGE_AUDIENCE = ${largeAudience};
        var contestData = ${ssiContestUpdateResultsForm.initializationJson};

        //attach the view to an existing DOM element
        var ssiNav = new SSIUpdateResultsPageView({
            el: $('#ssiUpdateResultsPageView'),
            pageNav : {
                back : {
                    text : '<cms:contentText key="BACK" code="system.button" />',
                    //url : G5.props.URL_ROOT+'ssi/creatorContestList.do?method=display&id='+document.getElementById("contestId").value;
                    url : 'javascript:history.go(-1);'
                },
                home : {
                    text : '<cms:contentText key="HOME" code="system.general" />',
                    url : '<%=RequestUtils.getBaseURI(request)%>/homePage.do'
                }
            },
            pageTitle : "<cms:contentText key="UPDATE_CONTEST_RESULTS" code="ssi_contest.creator" />",
            data: contestData
        });
    });
</script>

<script type="text/template" id="ssiUpdateResultsActivityTableTpl">
  <%@include file="/ssi/ssiUpdateResultsActivityTable.jsp" %>
</script>

<script type="text/template" id="paginationViewTpl">
    <%@include file="/include/paginationView.jsp" %>
</script>
