<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="java.util.*"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>

<!-- ======== NOMINATIONS MORE INFO PAGE ======== -->
<div id="nominationsMoreInfoPageView" class="page-content nominationsMoreInfoPageView">
    <div class="row-fluid">
		<div class="span12">
			<div class="dataWrap"></div>
	<script id="nominationsMoreInfoDocUploadTpl" type="text/x-handlebars-template">
                <span class="nominationDisplayLink">
                    <div data-validate-fail-msgs='{"nonempty":"You must enter minimum of {{minDocsAllowed}} attachment or URL."}' data-validate-flags='nonempty' class="{{#lt updatedDocCount minDocsAllowed}}validateme{{else}}{{/lt}}">
                        <input type="text" id="nominationLink" name="nominationLink" data-model-key="nominationLink" value="{{#lt updatedDocCount minDocsAllowed}}{{else}}{{updatedDocCount}}{{/lt}}" />
                    </div>                    
                        {{#each nominationLinks}}
							<input type="hidden" id="nominationDocs" name="nominationLinks[{{@index}}].nominationUrl" value="{{#if this.nominationLink}}{{this.nominationLink}}{{else}}{{this.nominationUrl}}{{/if}}">  
                        	<input type="hidden" id="nominationLinkId" name="nominationLinks[{{@index}}].linkid" value="{{this.linkid}}">  
                        	<input type="hidden" id="nominationFileName" name="nominationLinks[{{@index}}].fileName" value="{{#if this.nominationLink}}{{this.fileName}}{{else}}{{/if}}">   
                            <p>
                            {{#if this.nominationLink}}
                                {{this.fileName}}
                            {{else}}
                                {{#if this.nominationUrl}}
                                    {{this.nominationUrl}}
                                {{/if}}
                            {{/if}}                      
                            <button data-linkid="{{this.linkid}}" class="removeLink btn btn-mini btn-icon btn-danger {{#if this.nominationUrl}} {{/if}} {{#if this.nominationLink}}{{/if}}"><i class="icon-trash"></i></button>
                            </p>                            
                        {{/each}}                    
                    </span>
                    {{#eq updatedDocCount maxDocsAllowed}}<p>You Reached Limit</p>{{/eq}}            
            </script>	
			<script id="nominationsMoreInfoTpl" type="text/x-handlebars-template">
				<div class="row-fluid">
					<div class="span12">
						<form id="nominationsMoreInfoForm" class="form-horizontal nominationsMoreInfoForm" method="POST" action="${submitNominationUrl}">
                            <input type="hidden" name="claimId" value="{{claimId}}" />
                            <input type="hidden" name="promoId" value="{{promotionId}}" />
                            <div class="nomination-name" >{{nominationName}}</div>

							<div class="control-group">
								<label class="control-label" for="approverMessage"><cms:contentText key="APPROVERS_MESSAGE_LABEL" code="promotion.nomination.more.info" /> </label>
								<div class="controls">
									<p class="approversMessage">{{approversMessage}}</p>
								</div>
							</div>

							<div class="control-group validateme"
								data-validate-fail-msgs='{"nonempty":"<cms:contentText key="ENTER_MORE_INFO" code="promotion.nomination.more.info" />"}'
								data-validate-flags='nonempty'>

								<label class="control-label" for="moreInformation"><cms:contentText key="MORE_INFO_LABEL" code="promotion.nomination.more.info" /> </label>
								<div class="controls">
									<textarea class="moreInformation" name="moreInformation" rows="6" maxlength="1000"  placeholder="add more information"></textarea>
								</div>
							</div>

							<div class="row-fluid">
								<div class="span10 offset2">
									<div class="control-group">
										<label class="checkbox" for="addAttachment">
											<input type="checkbox" name="addAttachment" id="addAttachment" />
											<cms:contentText key="ADD_ATTACHMENT_LABEL" code="promotion.nomination.more.info" />
										</label>
									</div>

									<div class="attachmentDrawer" style="display: none">
										<div class="control-group nominationAddLink nominationDisplayAttached"
										data-validate-fail-msgs='{"nonempty":"<cms:contentText key="ENTER_ATTACHMENT" code="promotion.nomination.more.info" />"}'
										data-validate-flags='nonempty'>
											<p class="attachmentHelpText">
												<cms:contentTemplateText code="promotion.nomination.more.info" key="ATTACHMENT_HELP_TEXT" args="{{minDocsAllowed}},{{maxDocsAllowed}}" delimiter=","/>
											</p>

											<div>
												{{#or attachmentName nominationLink}}
													<a href="#" class="addDoc" style="display:none;"><i class="icon-file"></i><cms:contentText key="ATTACH_DOCUMENT" code="promotion.nomination.more.info" /></a>
													<a href="#" class="addDocNew"><i class="icon-file"></i><cms:contentText key="CHANGE_DOCUMENT" code="promotion.nomination.more.info" /></a>
													<a href="#" class="addUrl" style="display:none;"><i class="icon-link"></i><cms:contentText key="ATTACH_URL" code="promotion.nomination.more.info" /></a>
													<a href="#" class="addUrlNew"><i class="icon-link"></i><cms:contentText key="CHANGE_URL" code="promotion.nomination.more.info" /></a>
												{{else}}
													<a href="#" class="addDoc"><i class="icon-file"></i><cms:contentText key="ATTACH_DOCUMENT" code="promotion.nomination.more.info" /></a>
													<a href="#" class="addDocNew" style="display:none;"><i class="icon-file"></i><cms:contentText key="CHANGE_DOCUMENT" code="promotion.nomination.more.info" /></a>
													<a href="#" class="addUrl"><i class="icon-link"></i><cms:contentText key="ATTACH_URL" code="promotion.nomination.more.info" /></a>
													<a href="#" class="addUrlNew" style="display:none;"><i class="icon-link"></i><cms:contentText key="CHANGE_URL" code="promotion.nomination.more.info" /></a>
												{{/or}}
											</div>
			                                      <div class="attachmentSection"></div>
	
                                             <div class="removeAttachmentPopover" style="display:none">
                                                <p><b><cms:contentText key="REMOVAL_ALERT" code="promotion.nomination.more.info" /></b></p>
                                                <p><cms:contentText key="REMOVAL_ALERT_INFO" code="promotion.nomination.more.info" /></p>
                                                <p class="tc">
                                                    <button id="removeAttachmentConfirmBtn" class="btn btn-primary"><cms:contentText key="ALERT_YES" code="promotion.nomination.more.info" /></button>
                                                    <button id="removeAttachmentCancelBtn" class="btn"><cms:contentText key="ALERT_NO" code="promotion.nomination.more.info" /></button>
                                                </p>
                                            </div><!-- /.removeAttachmentPopover -->

										</div>
									</div><!--/.attachmentDrawer-->

									<div class="addUrlPopover" style="display: none">
										<div class="control-group">
											<label class="control-label" for="nominationUrl"><cms:contentText key="ATTACH_URL_HELP_TEXT" code="promotion.nomination.more.info" /></label>
											<div class="controls">
												<input type="text" class="nominationUrlAdd" name="nominationUrl" id="nominationUrl" />
												<button type="button" class="attachUrl btn btn-primary"><cms:contentText key="ATTACH_BUTTON_LABEL" code="promotion.nomination.more.info" /></button>
												<p class="muted"><cms:contentText key="ATTACH_URL_EXAMPLE" code="promotion.nomination.more.info" /></p>
											</div>
										</div>
									</div><!--/.addUrlPopover-->
								</div>
							</div><!--/.row-fluid-->

							<div class="row-fluid">
								<div class="span10 offset2">
									<div class="formSection">
										<beacon:authorize ifNotGranted="LOGIN_AS">
											<button class="nominationsMoreInfoSubmit btn btn-primary" type="submit">
												<cms:contentText key="SUBMIT" code="system.button" />
											</button>
											<!-- JAVA NOTE: Set the data url to the page the user will return to-->
											<button href="#" class="btn cancelBtn" data-url="${pageContext.request.contextPath}/homePage.do"> <cms:contentText key="CANCEL" code="system.button" /></button>
										</beacon:authorize>
									</div>
									<div class="cancelNominationPopover" style="display:none">
										<p>
											<b><cms:contentText key="CANCEL_SUBMIT_POPUP_LABEL" code="promotion.nomination.more.info" /></b>
										</p>
										<p>
											<cms:contentText key="CANCEL_SUBMIT_CONFIRMATION_MESSAGE" code="promotion.nomination.more.info" />
										</p>
										<p class="tc">
											<button id="nominationCancelConfirmBtn" class="btn btn-primary"><cms:contentText key="YES" code="system.button" /></button>
											<button id="nominationDoNotCancelBtn" class="btn"><cms:contentText key="NO" code="system.button" /></button>
										</p>
									</div><!-- /.cancelNominationPopover -->
								</div>
							</div><!--/.row-fluid-->
						</form>
					</div>
				</div><!--/.row-fluid-->

				<div class="row-fluid">
					<div class="span12">
							<p class="submissionTitle"><cms:contentText key="ORIGINAL_SUBMISSION" code="promotion.nomination.more.info" /></p>
					</div>
				</div><!--/.row-fluid-->

			   {{#nominationsMoreInfo}}
				<div class="row-fluid">
					<div class="span6">
						<div class="row-fluid">
							{{#nominatorInfo}}
                            {{#if firstName}}

							<div class="span4 detailsData">
								<p><strong><cms:contentText key="NOMINEES_LABEL" code="promotion.nomination.more.info" /></strong></p>
							</div>

							<div class="span8">
                                <div class="nominatorInfoWrap">
                                    <div class="nominatorInfoWrap participant-item">
                                        <p class="">
                                            <a class="participant-popover" href="#" data-participant-ids="[{{id}}]">
                                                {{name}}

                                                {{#if countryCode}}<img src="${pageContext.request.contextPath}/assets/img/flags/{{countryCode}}.png" alt="{{countryCode}}" class="countryFlag" title="{{countryName}}" />{{/if}}
                                            </a>
                                            <span class="orgAndDept" style="display: block;">{{orgName}} {{#if departmentName}}{{#if orgName}}&bull;{{/if}} {{departmentName}}{{/if}} {{#if jobName}}{{#if departmentName}}&bull;{{else}}{{#if orgName}}&bull;{{/if}}{{/if}} {{jobName}}{{/if}}</span>
                                        </p>
                                    </div>
 								</div><!--/.nominatorInfoWrap-->
							</div>
                            {{else}}
							<div class="span4 detailsData">
								<p><strong><cms:contentText key="NOMINEES" code="promotion.audience" /></strong></p>
							</div>

							<div class="span8">
                            <span class="teamNomineeName">{{nominationName}}</span>
							<a class="teamMemberTrigger">{{teamMemberCount}} Members<!--TODO: cms key--></a>
								<div class="teamMembersTooltip" style="display: none">
									<ul>
										{{#teamMembers}}

										<li>{{firstName}} {{lastName}}</li>

										{{/teamMembers}}
									</ul>
								</div>
							</div>
                            {{/if}}
							{{/nominatorInfo}}
						</div><!--/.row-fluid-->
						{{#originalSubmission}}
						<div class="row-fluid">
							<div class="span4 detailsData">
								<p><strong><cms:contentText key="DATE_SUBMITTED_LABEL" code="promotion.nomination.more.info" /></strong></p>
							</div>

							<div class="span8">
								<p class="dateSubmitted">{{awardedDate}}</p>
							</div>
						</div><!--/.row-fluid-->

                        {{#if badges}}
						<div class="row-fluid">
							<div class="span4 detailsData">
								<p><strong><cms:contentText key="BEHAVIOR_LABEL" code="promotion.nomination.more.info" /></strong></p>
							</div>

							<div class="span8">
                                {{#badges}}
                                <span class="badges" data-toggle="tooltip"  data-original-title="{{behavior}}" data-placement="top" >
                                    {{#if badgeUrl}}
                                        <img class="behaviorBadge" src="{{badgeUrl}}" alt="{{behavior}}"/>
                                    {{else}}
                                        <span class="badgeName noImg">{{behavior}}</span>
                                    {{/if}}
                                </span>
                                {{/badges}}
                            </div>
						</div><!--/.row-fluid-->
                        {{/if}}

						<div class="row-fluid">
                            {{#each customFields}}
                            <div class="span4 detailsData">
                                <p><strong>{{label}}</strong></p>
                            </div>
                            <div class="span8">
                                <p class="recipientWrap translate customField" data-type="{{type}}" data-fieldId="{{fieldId}}">{{{value}}}</p>
                            </div>
                            {{/each}}
                        </div><!--/.row-fluid-->

						<div class="row-fluid">
							<div class="span4 detailsData">
								<p><strong><cms:contentText key="REASON_LABEL" code="promotion.nomination.more.info" /></strong></p>
							</div>

							<div class="span8">
								<p class="reason-data" data-reasonId="1234">{{{reason}}}</p>

                                {{#if allowTranslate}}
                                    <p class="translateTextLink"><a href="#" ><cms:contentText key="TRANSLATE_LABEL" code="promotion.nomination.more.info" /></a></p>
                                {{/if}}
							</div>
						</div><!--/.row-fluid-->

                        {{#if certificates}}
						<div class="row-fluid">
							<div class="span8 offset4">
								<p><strong><a href={{certificates.0.imgLg}}><cms:contentText key="VIEW_CERTIFICATE_LABEL" code="promotion.nomination.more.info" /></a></strong></p>
							</div>
						</div><!--/.row-fluid-->
                        {{/if}}
					</div><!--/.span6-->

					<div class="span6">
						{{#if ecardName}}
							<p class="mediaContainer">
								<img src="{{ecardUrl}}" title="{{ecardName}}"/>
							</p>
						{{/if}}
						{{#if videoContent}}
							<div class="mediaContainer">
								<a href="{{videoWebLink}}" target="_blank"><img src="{{ posterImg}}"/></a>
							</div>
						{{/if}}
					</div><!--/.span6-->
				</div><!--/.row-fluid-->
				{{/originalSubmission}}
				{{/nominationsMoreInfo}}

				<!--- Custom Added for Tooltip Doc -->
				<!-- Max Upload Popover -->
			    <div class="maxUploadReached" style="display: none">    
			    <div class="control-group" id="attachDocSection">
			        <p><strong>You Reached Maximum Limit of Uploads</strong></p>  
			        <p class="tc">            
			            <button class="btn btn-mini cancelTip">OK</button>
			        </p>      
			    </div>
			</div>

			<div class="attachingNewDoc" style="display:none">					
				<p>
				Attaching a New Document will Replace the Existing Document
				</p>
				<p class="tc">
					<button id="nominationNewDocConfirmBtn" class="btn btn-primary">Attach</button>
					<button id="nominationNewDocDoNotCancelBtn" class="btn">Cancel</button>
				</p>
			</div>
			<!--- //Custom Added for Tooltip Doc -->

			<!--- Custom Added for Tooltip for addUrlNew -->
				<div class="attachingNewUrl" style="display:none">
					<p>
					<cms:contentText key="ATTACHING_NEW_URL" code="promotion.nomination.submit" />
					</p>
					<p class="tc">
						<button id="nominationNewUrlConfirmBtn" class="btn btn-primary"><cms:contentText key="ATTACH_BUTTON_LABEL" code="promotion.nomination.more.info" /></button>
						<button id="nominationNewUrlDoNotCancelBtn" class="btn"><cms:contentText key="CANCEL" code="system.button" /></button>
					</p>
				</div>
			<!--- //Custom Added for Tooltip for addUrlNew -->

				<!-- Doc Popover -->
				<div class="addDocPopover" style="display: none">
					<div class="control-group">
						<label class="control-label"><cms:contentText key="UPLOAD_LABEL" code="promotion.nomination.more.info" /></label>
						<div class="controls">
							<form id="nominationDocUpload" enctype="multipart/form-data">
									<input type="file" name="nominationDoc" id="nominationDoc" />
							</form>
						</div>
					</div>
				</div>

			</script>
		</div>
    </div>
</div><!--/.nominationsMoreInfoPageView-->

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
$(document).ready(function() {
	 G5.props.URL_JSON_NOMINATIONS_MORE_INFO_TRANSLATE_COMMENT = '${translateCommentUrl}';
	 G5.props.URL_JSON_NOMINATIONS_MORE_INFO = '${moreInfoUrl}';
	 G5.props.URL_JSON_NOMINATIONS_MORE_INFO_UPLOAD = '${uploadDocUrl}';
	 G5.props.URL_JSON_NOMINATIONS_MORE_INFO_SUBMIT = '${submitNominationUrl}';
	 G5.props.URL_JSON_NOMINATIONS_UPLOAD_DOC_REMOVE = G5.props.URL_ROOT+'nomination/submitNomination.do?method=removeWhyAttachment';
	 G5.props.URL_JSON_EZ_RECOGNITION_MEMBER_INFO = "${pageContext.request.contextPath}/recognitionWizard/memberInfo.do";
	 G5.props.URL_JSON_EZ_RECOGNITION_SEND_EZRECOGNITION = "${pageContext.request.contextPath}/recognitionWizard/submitEasyRecognition.do";
	 G5.props.URL_JSON_PARTICIPANT_FOLLOW = G5.props.URL_ROOT+'publicRecognitionFollowUnFollowPax.do?method=followUnFollowPax';

    //attach the view to an existing DOM element
    var nlpv = new NominationsMoreInfoPageView({
        el:$('#nominationsMoreInfoPageView'),
        pageNav : {
            back : {
                text : '<cms:contentText key="BACK" code="system.button" />',
                url  : 'javascript:history.go(-1);'
            },
            home : {
                text : '<cms:contentText key="HOME" code="system.general" />',
                url  : '${pageContext.request.contextPath}/homePage.do'
            }
        },
        pageTitle : 'Returned Nomination'
    });
});
</script>
<%@include file="/submitrecognition/easy/flipSide.jsp" %>
