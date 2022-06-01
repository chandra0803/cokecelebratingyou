<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ include file="/include/taglib.jspf"%>


<div id="programSelectPageView" class="page-content">
    <div id="programSelectWrapper">
    <h2><cms:contentText key="RECOGNIZE_SOMEONE" code="recognitionSubmit.programSelector"/></h2>
    <h3 class="findRecipientTitle" data-purl-title=<cms:contentText key="FIND_RECIPIENTS" code="recognitionSubmit.programSelector"/> ><cms:contentText key="FIND_RECIPIENTS" code="recognitionSubmit.programSelector"/> </h3>
        <div id="paxSearchStartView"  data-search-url="${pageContext.request.contextPath}/search/paxHeroSearch.action" ></div><!-- /.paxSearchStartView -->
        <div id="purlTooManyRecips" style="display:none" class="alert-danger">
            <p>
                <b><cms:contentText key="PURL_SELECT_ONE_RECIPIENT" code="recognitionSubmit.programSelector"/> </b>
            </p>
        </div>
        <div id="invalidRecips" style="display:none" class="alert-danger">
            <p>
                <b><cms:contentText key="PAX_INELIGIBLE" code="recognitionSubmit.programSelector"/></b>
            </p>
        </div>
        <div id="PaxSelectedPaxView2"  data-save-group='true' data-search-url="${pageContext.request.contextPath}/purlSearch/purlContributors.action" class="full-width-neg-margin"></div>
        <div class="promotionWrapper">
            <h3><cms:contentText key="SELECTED_PROMOTION" code="recognitionSubmit.programSelector"/></h3>
            <div class="promotionList control-group">
                <!-- Generated content from promotionsListTpl -->
            </div>
        </div><!-- /.approvalPromotionWrapper.row-->



<form id="dataForm" class="recognitionDataForm" action="validate.do" method="post">

  <%@include file="dataForm.jsp" %>

  <c:if test="${not empty submitRecognitionValidationErrors}">
    <div class="error">
      <h4><cms:contentText key="FOLLOWING_ERRORS" code="system.generalerror" /></h4>
      <ul>
        <c:forEach var="serviceError" items="${submitRecognitionValidationErrors}">
          <li>${serviceError.arg1}</li>
        </c:forEach>
      </ul>
    </div>
  </c:if>

</form><!-- /#dataForm -->
<!-- =============== END - DATA FORM =================== -->


        <!-- promotions name view template -->
        <script id="promotionNameTpl" type="text/x-handlebars-template">
            <div class="controls promoChangeWrapper">
                <span class="nominationPromotionName headline_5">
                    {{name}}
                </span>

                {{#gte totalPromotionCount 2}}
                <button type="button" id="nominationChangePromoBtn" class="btn btn-primary btn-inverse popoverTrigger" data-popover-content="promoChange"><cms:contentText key="CHANGE" code="recognitionSubmit.programSelector"/></button>
                {{/gte}}

                {{#if rulesText}}
                <a href="#" class="doViewRules">View Rules</a>
                {{/if}}
				<div class="raPromo fr" style="display:none"><span class="raBrandingSmallLogo">
				<svg id="Layer_1" data-name="Layer 1" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 191.97 189.22"><defs><clipPath id="clip-path" transform="translate(0 0)"><rect width="191.97" height="189.21" /></clipPath></defs><title>Vector Smart Object</title><g><path class="raBrandingOuter" d="M183.85,33a95.66,95.66,0,0,0-22-31.16,6.69,6.69,0,0,0-9.19,0A82.26,82.26,0,0,1,96,24.31,82.26,82.26,0,0,1,39.35,1.82a6.7,6.7,0,0,0-9.19,0,96,96,0,0,0,7.38,146h0l-8.9,41.4,54.95-22.35h0a96.83,96.83,0,0,0,12.41.8,95.91,95.91,0,0,0,96-96A95.26,95.26,0,0,0,183.85,33m-29.46,97.09A82,82,0,0,1,96,154.27a83.63,83.63,0,0,1-13.79-1.14L46.92,166.91,52.7,142a83.23,83.23,0,0,1-15.12-12A82.51,82.51,0,0,1,35.08,15.9,95.56,95.56,0,0,0,96,37.7a95.56,95.56,0,0,0,60.91-21.8,82.59,82.59,0,0,1-2.51,114.18" transform="translate(0 0)"/><path class="raBrandingInner"  d="M140.63,59.42a24.39,24.39,0,0,0-17,6.88,10.21,10.21,0,0,0-1,.83L96,92.2,69.39,67.13a10.1,10.1,0,0,0-1-.83,24.55,24.55,0,1,0,.32,35A24.74,24.74,0,0,0,72,97.22l17.07,16.09a10,10,0,0,0,13.78,0l17.07-16.09a24.74,24.74,0,0,0,3.32,4.11,24.55,24.55,0,1,0,17.36-41.92M51.34,95.14A11.16,11.16,0,1,1,62.5,84,11.16,11.16,0,0,1,51.34,95.14m89.29,0A11.16,11.16,0,1,1,151.79,84a11.16,11.16,0,0,1-11.16,11.16" transform="translate(0 0)" /></g>
				</svg>
				</span><a class="promoRa"><cms:contentText key="RA_PROGRAMS_DISPLAY_LONGFORM" code="recognition.content.model.info"/></a></div>
            </div><!-- /.promoChangeWrapper -->
        </script><!-- /#promotionsListTpl -->

        <!-- promotions list client side template -->
        <script id="promotionsListTpl" type="text/x-handlebars-template">
            <div class="controls promoWrapper">
                <select id="promotionId" name="promotionId" data-msg-instructions="<cms:contentText key="START_RECOGNITION_PROCESS" code="recognitionSubmit.programSelector"/>">
                    <option class="defaultOption" value=""><cms:contentText key="SELECT_PROGRAM" code="recognitionSubmit.programSelector"/></option>

                    {{#each this}}
                        <option value="{{promoId}}" data-type="{{type}}" data-purl="{{isPurl}}"{{#if selected}}selected="selected"{{/if}}>
                            {{name}}
                        </option>
                    {{/each}}
                </select>
				<div class="raPromo fr" style="display:none"><span class="raBrandingSmallLogo">
				<svg id="Layer_1" data-name="Layer 1" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 191.97 189.22"><defs><clipPath id="clip-path" transform="translate(0 0)"><rect width="191.97" height="189.21" /></clipPath></defs><title>Vector Smart Object</title><g><path class="raBrandingOuter" d="M183.85,33a95.66,95.66,0,0,0-22-31.16,6.69,6.69,0,0,0-9.19,0A82.26,82.26,0,0,1,96,24.31,82.26,82.26,0,0,1,39.35,1.82a6.7,6.7,0,0,0-9.19,0,96,96,0,0,0,7.38,146h0l-8.9,41.4,54.95-22.35h0a96.83,96.83,0,0,0,12.41.8,95.91,95.91,0,0,0,96-96A95.26,95.26,0,0,0,183.85,33m-29.46,97.09A82,82,0,0,1,96,154.27a83.63,83.63,0,0,1-13.79-1.14L46.92,166.91,52.7,142a83.23,83.23,0,0,1-15.12-12A82.51,82.51,0,0,1,35.08,15.9,95.56,95.56,0,0,0,96,37.7a95.56,95.56,0,0,0,60.91-21.8,82.59,82.59,0,0,1-2.51,114.18" transform="translate(0 0)"/><path class="raBrandingInner"  d="M140.63,59.42a24.39,24.39,0,0,0-17,6.88,10.21,10.21,0,0,0-1,.83L96,92.2,69.39,67.13a10.1,10.1,0,0,0-1-.83,24.55,24.55,0,1,0,.32,35A24.74,24.74,0,0,0,72,97.22l17.07,16.09a10,10,0,0,0,13.78,0l17.07-16.09a24.74,24.74,0,0,0,3.32,4.11,24.55,24.55,0,1,0,17.36-41.92M51.34,95.14A11.16,11.16,0,1,1,62.5,84,11.16,11.16,0,0,1,51.34,95.14m89.29,0A11.16,11.16,0,1,1,151.79,84a11.16,11.16,0,0,1-11.16,11.16" transform="translate(0 0)" /></g>
				</svg>
				</span><a class="promoRa"><cms:contentText key="RA_PROGRAMS_DISPLAY_LONGFORM" code="recognition.content.model.info"/></a></div>
            </div><!-- /.promoWrapper -->
        </script><!-- /#promotionsListTpl -->
        <div class="nominationOrgSection">
            <div class="orgUnitList">
                <!-- Generated content from orgUnitListTpl -->
            </div>
        <div class="approvalSearchWrapper"><!--nominationsApprovalPageTpl.html--></div>

    </div><!-- /#pendingNominationsWrapper-->
    

    <div class="raInsights insightSection" style="display:none"> 
		<div>
			<div class="raInsightsLogoPosition">
				<div class="raInsightsLogo">
				<svg id="Layer_1" data-name="Layer 1" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 191.97 189.22"><defs><clipPath id="clip-path" transform="translate(0 0)"><rect width="191.97" height="189.21" /></clipPath></defs><title>Vector Smart Object</title><g><path class="raBrandingOuter" d="M183.85,33a95.66,95.66,0,0,0-22-31.16,6.69,6.69,0,0,0-9.19,0A82.26,82.26,0,0,1,96,24.31,82.26,82.26,0,0,1,39.35,1.82a6.7,6.7,0,0,0-9.19,0,96,96,0,0,0,7.38,146h0l-8.9,41.4,54.95-22.35h0a96.83,96.83,0,0,0,12.41.8,95.91,95.91,0,0,0,96-96A95.26,95.26,0,0,0,183.85,33m-29.46,97.09A82,82,0,0,1,96,154.27a83.63,83.63,0,0,1-13.79-1.14L46.92,166.91,52.7,142a83.23,83.23,0,0,1-15.12-12A82.51,82.51,0,0,1,35.08,15.9,95.56,95.56,0,0,0,96,37.7a95.56,95.56,0,0,0,60.91-21.8,82.59,82.59,0,0,1-2.51,114.18" transform="translate(0 0)"/><path class="raBrandingInner"  d="M140.63,59.42a24.39,24.39,0,0,0-17,6.88,10.21,10.21,0,0,0-1,.83L96,92.2,69.39,67.13a10.1,10.1,0,0,0-1-.83,24.55,24.55,0,1,0,.32,35A24.74,24.74,0,0,0,72,97.22l17.07,16.09a10,10,0,0,0,13.78,0l17.07-16.09a24.74,24.74,0,0,0,3.32,4.11,24.55,24.55,0,1,0,17.36-41.92M51.34,95.14A11.16,11.16,0,1,1,62.5,84,11.16,11.16,0,0,1,51.34,95.14m89.29,0A11.16,11.16,0,1,1,151.79,84a11.16,11.16,0,0,1-11.16,11.16" transform="translate(0 0)" /></g>
				</svg>
				</div>
			</div>
			<div class="raInsightsBackgrondImg"></div>
			<div class="raInsightsGradgient"></div>
		</div>
		<div class="raInsightsBodyContent">
			<h4><cms:contentText key="RA_LONGFORM_HEADING" code="recognition.content.model.info"/></h4>
			<p><cms:contentText key="RA_LONGFORM_MESSAGE" code="recognition.content.model.info"/></p>
			<div class="clear"></div>
		</div>
	</div>
    

    <div id="errorAbove" style="display:none;" class="alert-danger">
        <p>
            <b><cms:contentText key="SEE_ABOVE" code="nomination.inprogress"/></b>
        </p>
    </div>
    <div id="rulesModal" class="modal hide fade">
        <div class="modal-header">
            <button data-dismiss="modal" class="close" type="button">
                <i class="icon-close"></i>
            </button>
            <h3></h3>
        </div>
        <div class="modal-body">
            <!-- dynamic -->
        </div>
    </div><!-- /#rulesModal -->

    <div id="statusModal" class="modal hide fade">
        <div class="modal-header">
            <button data-dismiss="modal" class="close" type="button">
                <i class="icon-close"></i>
            </button>
            <h3><cms:contentText key="STATUS" code="recognitionSubmit.programSelector"/> </h3>
        </div>
        <div class="modal-body">
            <cms:contentText key="SAVE_YOUR_CHANGES" code="recognitionSubmit.programSelector"/>
        </div>
    </div><!-- /#statusModal -->

    <div id="sortModal" class="modal hide fade">
        <div class="modal-header">
            <button data-dismiss="modal" class="close" type="button">
                <i class="icon-close"></i>
            </button>
            <h3><cms:contentText key="STATUS" code="recognitionSubmit.programSelector"/></h3>
        </div>
        <div class="modal-body">
            <cms:contentText key="BEFORE_SORT_MESSAGE" code="recognitionSubmit.programSelector"/>
        </div>
        <div class="modal-footer">
            <button class="btn btn-primary" data-dismiss="modal"><cms:contentText key="OK" code="recognitionSubmit.programSelector"/></button>
            <button class="btn btn-primary doTheSort" data-dismiss="modal"> <cms:contentText key="SORT_ANYWAY" code="recognitionSubmit.programSelector"/></button>
        </div>
    </div><!-- /#sortModal -->

    <div class="promoChangePopover" style="display:none">
        <p>
            <b><cms:contentText key="CHANGE_PROMOTION" code="recognitionSubmit.programSelector"/> </b>
        </p>
        <p>
            <cms:contentText key="CURRENT_SELECTION_WILL_BE_LOST_MSG" code="recognitionSubmit.programSelector"/>
        </p>
        <p class="tc">
            <button id="nominationChangePromoConfirmBtn" class="btn btn-primary"><cms:contentText key="YES" code="recognitionSubmit.programSelector"/></button>
            <button id="nominationChangePromoCancelBtn" class="btn"><cms:contentText key="CANCEL" code="recognitionSubmit.programSelector"/> </button>
        </p>
    </div><!-- /.promoChangeConfirmDialog -->

    <!-- Success Modal -->
     <div class="modal hide fade approvalSuccessModal" data-backdrop="static">
        <div class="modal-header">
            <h3> <cms:contentText key="STATUS_UPDATED" code="recognitionSubmit.programSelector"/> </h3>
        </div>
        <div class="modal-body">
            <!-- <p>Your statuses have been updated.</p> -->

            <button class="btn btn-primary updateBtn"><cms:contentText key="OK" code="recognitionSubmit.programSelector"/></button>
        </div>
    </div><!-- /.approvalSuccessModal -->
    
    <c:if test="${isRAPromoFlag}">
		<div class="byPromoHelpTip hide">
		    <strong><cms:contentText key="RA_PROGRAMS_DISPLAY_LONGFORM_HEADING" code="recognition.content.model.info"/></strong>
	        <div class="tipContent">
                <ul>
	                <c:forEach var="promotionMenuBean" items="${raEligiblePromotionList}">
				   	     <li><c:out value="${promotionMenuBean.promotion.promotionName}"/></li>
				    </c:forEach>
	            </ul>
            </div>
	    </div>
    </c:if>

</div><!-- /#nominationsApprovalPageView. -->
<div class="modal hide fade certificateModal" id="certificateModal">
    <div class="modal-body loading">
        <div class="progress-indicator">
            <span class="spin">
            </span>
            <p> <cms:contentText key="GENERATING_PDF" code="recognitionSubmit.programSelector"/> </p>
        </div>        <div class="pdf-wrapper" style="height: 500px;">
        </div>
    </div>
    <div class="modal-footer">
        <a href="#" class="btn btn-primary" data-dismiss="modal" aria-hidden="true"><cms:contentText key="CLOSE" code="recognitionSubmit.programSelector"/> </a>
    </div>
</div>
<div id="programContainer">
    <!--- dynamic recognition/nomination loaded in -->
</div>
<script type="text/template" id="recognitionPageSendTpl">
 <%@include file="main.jsp" %>
</script>
<script type="text/template" id="wizardTabTpl">
    <%@include file="/include/wizardTab.jsp" %>
</script>


 <script type="text/template" id="nominationsSubmitPageTpl">
<%@include file="/nomination/nominationsSubmitPage.jsp" %>
</script>

    <!-- promotions list client side template -->
    <script id="orgUnitListTpl" type="text/x-handlebars-template">
        {{#unless this.[1]}}
            <select id="nodeId" class="singleNodeId" name="nodeId" style="display:none;" data-msg-instructions="<cms:contentText key="NODE_SELECTION" code="recognitionSubmit.programSelector"/>">
                {{#each this}}
                    <option value="{{id}}" {{#if selected}}selected="selected"{{/if}}>
                        {{name}}
                    </option>
                {{/each}}
            </select>
        {{else}}
        <div class="control-group validateme"
            data-validate-flags="nonempty"
            data-validate-fail-msgs='{"nonempty" : "You must select an org unit for reporting"}'>
            <div class="controls orgUnitWrapper">
                <h3 for="nodeId"><cms:contentText key="SELECT_ORG_UNIT" code="promotion.nomination.submit" /></h3>
                <select id="nodeId" name="nodeId" data-msg-instructions="<cms:contentText key="NODE_SELECTION" code="recognitionSubmit.programSelector"/>">
                    <option class="defaultOption" value="" disabled="disabled" selected="selected"><cms:contentText key="CHOOSE_ONE" code="promotion.nomination.submit" /></option>
                    {{#each this}}
                        <option value="{{id}}" {{#if selected}}selected="selected"{{/if}}>
                            {{name}}
                        </option>
                    {{/each}}
                </select>
            </div><!-- /.orgUnitWrapper -->
        </div>
        {{/unless}}
    </script><!-- /#promotionsListTpl -->

</div>
<script  type="text/javascript" >

$(document).ready(function() {
    // Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to

    G5.props.SEARCH_URL_DEFAULT;
    G5.props.SEARCH_URL_PROMO = '${pageContext.request.contextPath}/participantSearch/participantSearch.action';
    var idJson = ${recognitionState.idJson};
    G5.props.VALID_USERS =   G5.props.URL_ROOT+'participantSearch/validatePaxForRecog.action';


    G5.props.URL_JSON_NOMINATIONS_SUBMIT_DATA = G5.props.URL_ROOT + '${nomsSubmitDataUrl}';
    G5.props.URL_JSON_NOMINATIONS_TABS_DATA = G5.props.URL_ROOT + '${tabMenu}';
    G5.props.URL_JSON_NOMINATIONS_EDIT_SAVE = G5.props.URL_ROOT + '${rootUrl}';
    G5.props.URL_JSON_NOMINATIONS_LIST = G5.props.URL_ROOT+'nomination/nominationTile.do?method=eligiblePromo';
    G5.props.URL_JSON_NOMINATIONS_PARTICIPANTS = G5.props.URL_ROOT + '${getParticipantData}';
    G5.props.URL_JSON_SAVED_PARTICIPANT_GROUPS = G5.props.URL_ROOT + 'participant/participantGroup.do?method=getGroupList';

    G5.props.URL_JSON_NOMINATIONS_CALCULATOR_DATA  =  "${pageContext.request.contextPath}/nomination/nominationPromotionNodeCheck.do?method=nominationPromo";
    G5.props.URL_JSON_NOMINATIONS_CALCULATOR_SEND_INFO = "${pageContext.request.contextPath}/nomination/sendNominationCalculatorInfo.do";

    G5.props.URL_JSON_DRAW_TOOL_IMAGE_UPLOAD = "${pageContext.request.contextPath}/recognitionWizard/ecardUpload.do";
    G5.props.URL_JSON_PARTICIPANT_INFO = "${pageContext.request.contextPath}/participantPublicProfile.do?method=populatePax";
    G5.props.URL_GET_CERT_IMAGES = G5.props.URL_ROOT+'nomination/submitNomination.do?method=getCertificateImages';

    //Mini Profile PopUp Follow Unfollow Pax JSON
    G5.props.URL_JSON_PARTICIPANT_FOLLOW = G5.props.URL_ROOT+'publicRecognitionFollowUnFollowPax.do?method=followUnFollowPax';

    G5.props.URL_JSON_CUSTOM_FORM_ELEMENTS = G5.props.URL_ROOT + 'promotion/dataService.do?method=getCustomElements';

    G5.props.URL_JSON_NOMINATIONS_UPLOAD_DOC = G5.props.URL_ROOT+'nomination/submitNomination.do?method=uploadWhyAttachment';

    G5.props.URL_JSON_NOMINATIONS_UPLOAD_DOC_REMOVE = G5.props.URL_ROOT+'nomination/submitNomination.do?method=removeWhyAttachment';
    G5.props.URL_JSON_EZ_RECOGNITION_MEMBER_INFO = "${pageContext.request.contextPath}/recognitionWizard/memberInfo.do";
    G5.props.URL_JSON_EZ_RECOGNITION_SEND_EZRECOGNITION = "${pageContext.request.contextPath}/recognitionWizard/submitEasyRecognition.do";
    G5.props.URL_JSON_PARTICIPANT_FOLLOW = G5.props.URL_ROOT+'publicRecognitionFollowUnFollowPax.do?method=followUnFollowPax';

    G5.props.URL_JSON_SEND_RECOGNITION_CHECK_PROMO_NODE = "${pageContext.request.contextPath}/recognitionWizard/promotionNodeCheck.do";
    G5.props.URL_JSON_SEND_RECOGNITION_PRESELECTED_CONTRIBUTORS = "${pageContext.request.contextPath}/recognitionWizard/preselectedContributors.do";
    G5.props.URL_JSON_DRAW_TOOL_IMAGE_UPLOAD = "${pageContext.request.contextPath}/recognitionWizard/ecardUpload.do";
    G5.props.URL_JSON_SEND_RECOGNITION_CALCULATOR_INFO = "${pageContext.request.contextPath}/recognitionWizard/sendRecognitionCalculatorInfo.do";
    G5.props.URL_JSON_PARTICIPANT_INFO = "${pageContext.request.contextPath}/participantPublicProfile.do?method=populatePax";

    //Mini Profile PopUp Follow Unfollow Pax JSON
    G5.props.URL_JSON_PARTICIPANT_FOLLOW = G5.props.URL_ROOT+'publicRecognitionFollowUnFollowPax.do?method=followUnFollowPax';

    G5.props.URL_JSON_PLATEAU_AWARDS = '${pageContext.request.contextPath}/plateauAwardsDetail.do?method=detailViewResult';
    // Client Customizations For WIP #39189 Starts
    G5.props.URL_JSON_NOMINATION_FILE_UPLOAD = G5.props.URL_ROOT+'recognitionWizard/startUploadFiles.do?method=uploadClaimFile';
    G5.props.URL_JSON_NOMINATION_FILE_REMOVE = G5.props.URL_ROOT+'recognitionWizard/startUploadFiles.do?method=deleteClaimFile';
    // Client Customizations For WIP #39189 Ends
    var formSetup, rpsv;

    // BOOTSTRAPPED JSON DATA - render this dynamically in the JSP
    // JSON object with valid nodes and promotion + form setup for each promo
    // JAVA/JSP: generate this array of 'form setups' for different promo/node combos
    formSetup = ${sendRecognitionForm.initializationJsonStr}
    //attach the view to an existing DOM element
    var napv = new ProgramSelectPageView({
        el:$('#programSelectPageView'),
        pageNav : {
            back : {
                text : 'Back',
                url : 'layout.html?tpl=approvalsPageIndex&tplPath=apps/approvals/tpl/'
            },
            home : {
                text : 'Home',
                url : 'layout.html?tplPath=base/tpl/&amp;tpl=layout.html'
            }
        },
        pageTitle : 'Nomination Approvals',
        idJson: idJson
    });

	//Mini Profile Popup JSON
    G5.props.URL_JSON_PARTICIPANT_INFO = G5.props.URL_ROOT + 'participantPublicProfile.do?method=populatePax';

    //Mini Profile PopUp Follow Unfollow Pax JSON
    G5.props.URL_JSON_PARTICIPANT_FOLLOW = G5.props.URL_ROOT+'publicRecognitionFollowUnFollowPax.do?method=followUnFollowPax';

    // attach the view to an existing DOM element







});

</script>




<script>
  function onCountryChange(prefix, stateList, index)
  {
//    console.log("prefix: " + prefix + "\nstateList: " + stateList + "\nindex: " + index);

    filtery( prefix, stateList );

    if ( prefix == 'us_' || prefix == 'ca_' || prefix == 'mx_' )
    {
      showLayer('northamerican' + index);
      hideLayer('international' + index);
    }
    else
    {
      showLayer('international' + index);
      hideLayer('northamerican' + index);
    }

    if ( stateList.length == 0 )
    {
      document.getElementsByName('claimElement['+index+'].mainAddressFormBean.stateTypeCode')[0].value = "";
      hideLayer('displayStateList' + index);
    }
    else
    {
      showLayer('displayStateList' + index);
    }

  }

  function showLayer(whichLayer)
  {
    if(document.getElementById(whichLayer) != null)
    {
      var style2 = document.getElementById(whichLayer).style;
//      console.log("showing: " + whichLayer);
      style2.display = "";
	}
  }

  function hideLayer(whichLayer)
  {
	if(document.getElementById(whichLayer) != null)
    {
      var style2 = document.getElementById(whichLayer).style;
//      console.log("hiding: " + whichLayer);
      style2.display = "none";
	}
  }

/*

  Based on code from:
  Author: Justin Whitford
  Source: www.evolt.org

  */

  /*
  filtery(pattern, list)
  pattern: a string of zero or more characters by which to filter the list
  list: reference to a form object of type, select
  */
  function filtery(pattern, list)
  {
	  if (!list.bak){
	    list.bak = new Array();
	    for (n=0;n<list.length;n++){
	      list.bak[list.bak.length] = new Array(list[n].value, list[n].text, list[n].selected);
	    }
	  }

	  match = new Array();
	  nomatch = new Array();
	  for (n=0;n<list.bak.length;n++){
	    if(list.bak[n][0].toLowerCase().indexOf(pattern.toLowerCase())!=-1){
	      match[match.length] = new Array(list.bak[n][0], list.bak[n][1], list.bak[n][2]);
	    }else{
	      nomatch[nomatch.length] = new Array(list.bak[n][0], list.bak[n][1], list.bak[n][2]);
	    }
	  }

	  list.options.length = match.length;
	  for (n=0;n<match.length;n++){
	    list[n].value = match[n][0];
	    list[n].text = match[n][1];
	    list[n].selected = match[n][2];
	  }

	  //list.selectedIndex=0;
  }

</script>










<script type="text/template" id="calculatorTemplateTpl">
  <%@include file="../tpl/calculatorTemplate.jsp" %>
</script>



<script type="text/template" id="participantRowAwardItemTpl">
  <%@include file="../tpl/participantRowAwardItem.jsp" %>
</script>

<script type="text/template" id="recognitionCalculatorPayoutGridTemplateTpl">
  <%@include file="../tpl/recognitionCalculatorPayoutGridTemplate.jsp" %>
</script>

<script type="text/template" id="recognitionCalculatorScoreWrapperTemplateTpl">
  <%@include file="../tpl/recognitionCalculatorScoreWrapperTemplate.jsp" %>
</script>

<script type="text/template" id="participantRowItemTpl">
  <%@include file="../../profileutil/participantRowItem.jsp" %>
</script>

<script type="text/template" id="drawToolTemplateTpl">
  <%@include file="../tpl/drawToolTemplate.jsp" %>
</script>

<script type="text/template" id="drawToolCardListTpl">
  <%@include file="../tpl/drawToolCardList.jsp" %>
</script>

<%@include file="/submitrecognition/easy/flipSide.jsp" %>

<script type="text/template" id="plateauAwardsItemTpl">
<%@include file="/activities/plateauAwardsItem.jsp" %>
</script>

<script type="text/template" id="plateauAwardsDrawerTpl">
<%@include file="/activities/plateauAwardsDrawer.jsp" %>
</script>

<%@include file="/search/paxSearchStart.jsp" %>

<script type="text/template" id="nominationsSubmitNomineeTplTpl">
  <%@include file="/nomination/tpl/nominationsSubmitNomineeTpl.jsp" %>
</script>
<script type="text/template" id="nominationsSubmitEcardTplTpl">
  <%@include file="/nomination/tpl/nominationsSubmitEcardTpl.jsp" %>
</script>
<script type="text/template" id="nominationsSubmitBehaviorsTplTpl">
  <%@include file="/nomination/tpl/nominationsSubmitBehaviorsTpl.jsp" %>
</script>
<script type="text/template" id="nominationsSubmitWhyTplTpl">
  <%@include file="/nomination/tpl/nominationsSubmitWhyTpl.jsp" %>
</script>
<script type="text/template" id="nominationsCalcTemplateTpl">
  <%@include file="/nomination/tpl/nominationsCalcTemplate.jsp" %>
</script>
<script type="text/template" id="nominationsCalcPayoutGridTplTpl">
  <%@include file="/nomination/tpl/nominationsCalcPayoutGrid.jsp" %>
</script>
<script type="text/template" id="nominationsCalcScoreWrapperTplTpl">
  <%@include file="/nomination/tpl/nominationsCalcScoreWrapper.jsp" %>
</script>

<script type="text/template" id="participantSearchTableRowTpl">
  <%@include file="../../profileutil/participantSearchTableRow.jsp" %>
</script>

<script type="text/template" id="nominationsWizardTabVerticalTpl">
  <%@include file="/nomination/tpl/nominationsWizardTabVertical.jsp" %>
</script>
<script id="badgeBtnTpl" type="text/x-handlebars-template">
    <span class="badgeBtnContent {{contClass}}">
        {{#if img}}<img src="{{img}}" alt="{{name}}">{{/if}}
        {{#if iconClass}}<i class="{{iconClass}}"></i>{{/if}}
        <span class="badgeName">{{name}}</span>
    </span>
    <i class="icon-arrow-1-down badgeBtnCaret"></i>
</script><!-- /#badgeBtnTpl -->

<script id="badgeItemTpl" type="text/x-handlebars-template">
    <li class="badgeItem">
        <a href="#" data-badge-id="{{id}}">
            {{#if img}}<img src="{{img}}" alt="{{name}}">{{/if}}
            {{#if iconClass}}<i class="{{iconClass}}"></i>{{/if}}
            {{name}}
        </a>
    </li>
</script><!-- /#badgeItemTpl -->




<script id="selectGroupTpl" type="text/x-handlebars-template">
    <div class="selectGroupSection">
        <div class="control-group">
            <select name="selectGroup" id="selectGroup" {{#unless this}}readonly="readonly" disabled{{/unless}}>
                <option value="" class="defaultOption"><cms:contentText key="SELECT_MY_GROUPS" code="promotion.nomination.submit" /></option>
            {{#each this}}
                <option value="{{id}}" data-pax-count="{{paxCount}}" {{#if selected}}selected="selected"{{/if}}>{{name}}</option>
            {{/each}}
            </select>

            {{#unless this}}
                <span><cms:contentText key="NO_GROUPS_SAVED" code="promotion.nomination.submit" /></span>
            {{/unless}}
        </div>
    </div>
</script>
<script id="data" type="application/json">
${sendRecognitionForm.initializationJsonStr}
</script>