<%@page import="com.biperf.core.utils.UserManager"%>
<%@page import="java.util.Locale"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>
<!-- ======== NOMINATION WINNERS DETAIL PAGE ======== -->
<%
	// &#12289; represents , in japanese
	String commaSeperator = Locale.JAPAN.equals(UserManager.getLocale()) ?  "&#12289;" : "," ;
	request.setAttribute("commaSepeartor", commaSeperator);
%>
<div id="nominationsWinnersDetailPageView"
	class="page-content nominationWinnersDetailPage">
	<div class="row-fluid">
		<div class="span12 nominationsTableWrap"></div>

		<script id="nominationWinnersDetailTpl" type="text/x-handlebars-template">
			{{#nominationWinnersDetail}}
				<div class="awardContent">
                    <ul class="export-tools approvalsExportIconsWrapper pushDown">
                        <li class="export pdf">
                            <a href="#">
                                <span class="pdfExportIcon">
                                    <span class="btn btn-inverse btn-compact btn-export-pdf">
                                        <cms:contentText key="PDF" code="system.general" /> <i class="icon-download-2"></i>
                                    </span>
                                </span>
                            </a>
                        </li>
                    </ul>

                    {{#if isWinner}}
    					{{#if isMine}}
    					<div class="awardHeader">
    						<h2 class="title"><cms:contentText key="YOU_WIN" code="nomination.past.winners"/></h2>

    					{{else}}
                        <div class="awardHeader">
    						<h2 class="title"><cms:contentText key="CONGRATULATIONS" code="nomination.past.winners"/>${commaSepeartor} {{winnerName}} !</h2>
    					{{/if}}

    					<h4 class="awardWrap">The <span class="awardName">{{awardName}}</span> <cms:contentText key="AWARD" code="nomination.past.winners"/></h4>

    					<p class="awardedDate"><cms:contentText key="AWARDED_ON" code="nomination.past.winners"/> {{awardedDate}}</p>
    					</div><!--/.awardHeader-->
                    {{/if}}

					<div class="awardMain container-splitter">
						<div class="carousel awardContainer">
                            {{#if isWinner}}
							{{#if award}}
                            {{#if isMine}}
                            <div class="slide item">
    							<div class="awardCircle">
    								<div class="pointCircle">
    									<p class="awardTitle"><cms:contentText key="YOUR_AWARD" code="nomination.past.winners"/></p>
    									{{#if currencyLabel}}
    									<p class="awardPoints">
    										{{award}}
											{{currencyLabel}}
    									</p>
    									{{else}}
											{{#if other}}
	                                            <p class="awardPoints">{{award}}</p>
	                                        {{else}}
										        <p class="awardPoints">{{award}}</p>
										        <p class="points"><cms:contentText key="POINTS" code="nomination.past.winners"/></p>
	                                        {{/if}}
										{{/if}}
    								</div>
                                </div>
							</div>
                            {{/if}}
							{{/if}}
                            {{/if}}

							{{#nominatorInfo}}
								{{#if ecardImg}}
                                <div class="slide item">
    								<div class="ecard">
    										<img src="{{ecardImg}}" title='<cms:contentText key="E_CARD" code="nomination.past.winners"/>' />
    								</div>
                                </div>
								{{else}}
	                                {{#if videoUrl}}
                                        <div class="slide item">
    	    								<div class="mediaContainer">
    	    									<a href="{{videoUrl}}" target="_blank"><img src="{{videoImg}}"/></a>
                                            </div>
	    								</div>
			                         {{/if}}
	                             {{/if}}
                             {{/nominatorInfo}}
						</div>
					</div><!--/.awardMain-->
				</div><!--/.awardContent-->


                <div class="row-fluid">
                    <div class="span6">
                        <p class="subTitle"><strong><cms:contentText key="YOUR_NOMINATION" code="nomination.past.winners"/></strong></p>
                    </div>
                    <div class="span6">
                        <p class="subTitle"><strong><cms:contentText key="THE_DETAILS" code="nomination.past.winners"/></strong></p>
                    </div>
                </div>

                {{#each nominatorInfo}}
                <hr>

                <div class="row-fluid">
    				<div class="nominatorInfoC span6">

    						<div class="comment-text translate" data-claimId="{{claimId}}" data-type="textarea">{{{commentText}}}</div>

                            {{#if ../allowTranslate}}
                                <p class="translateTextLink"><a href="#" ><cms:contentText key="TRANSLATE" code="nomination.past.winners"/></a></p>
                            {{/if}}

    						<div class="nominatorInfoWrap participant-item">
    							<p class=" participant">
                                    <a class="participant-popover" href="#" data-participant-ids="[{{id}}]">
                                    <span class="avatarwrap">
                                        {{#if avatarUrl}}
                                            <img alt="{{firstName}} {{lastName}}" class="avatar" src="{{#timeStamp avatarUrl}}{{/timeStamp}}">
                                        {{else}}
                                            <span class="avatar-initials">{{trimString firstName 0 1}}{{trimString lastName 0 1}}</span>
                                        {{/if}}
                                    </span>
                                        {{firstName}}
                                        {{lastName}}
                                    </a>

                                    <span class="org">{{orgName}} {{#if departmentName}}{{#if orgName}} | {{/if}} {{departmentName}}{{/if}} {{#if title}}{{#if departmentName}} | {{else}}{{#if orgName}} | {{/if}}{{/if}} {{title}}{{/if}}</span>
                                </p>
    						</div>
    				    </div><!--/.nominatorInfo-->

				    <div class="nominationDetailsC span6">

					<div class="row-fluid">
						<div class="span4 detailsData">
							<p><strong><cms:contentText key="DATE_SUBMITTED" code="nomination.past.winners"/></strong></p>
						</div>

						<div class="span8">
							<p class="dateSubmitted">{{dateSubmitted}}</p>
						</div>
					</div>

                    {{#if badges}}
					<div class="row-fluid">
						<div class="span4 detailsData">
							<p><strong><cms:contentText key="BEHAVIORS" code="nomination.past.winners"/></strong></p>
						</div>

						<div class="span8">
                        {{#badges}}
                          {{#if badgeUrl}}
    				        <span data-toggle="tooltip"  data-original-title="{{behavior}}" data-placement="top" >

    							    <img class="behaviorBadge" src="{{badgeUrl}}" alt="{{behavior}}"/>
    			                </span>
                            {{else}}
                                <span class="behaviorName">{{behavior}}</span>
                            {{/if}}
							{{/badges}}
						</div>
					</div>
                    {{/if}}

                    {{#if fields}}
                    {{#each fields}}
					<div class="row-fluid">
						<div class="span4 detailsData">
							<p><strong>{{label}}</strong></p>
						</div>

						<div class="span8">
							<p class="recipientWrap translate" data-type="{{type}}" data-fieldId="{{fieldId}}">{{{value}}}</p>
						</div>
					</div>
                    {{/each}}
                    {{/if}}

                    {{#if eCertUrl}}
                    <div class="row-fluid">
                        <div class="span8 offset4">
                            <p class="viewCertificate">
                                <strong><a href="#" class="generateCertPdf" data-claimId="{{claimId}}" target="_blank"><cms:contentText key="VIEW_CERTIFICATE" code="nomination.past.winners"/></a></strong>
                            </p>
                        </div>
                    </div>
                    {{/if}}

				</div><!--nominationDetails-->
                </div>
                {{/each}}


		    {{/nominationWinnersDetail}}
		</script>
	</div>
	<div class="modal hide fade certificateModal" id="certificateModal">
		<div class="modal-body loading">
			<div class="progress-indicator">
				<span class="spin"> </span>
				<p><cms:contentText key="GENERATING_PDF" code="nomination.approvals.module"/></p>
			</div>
			<div class="pdf-wrapper" style="height: 500px;"></div>
		</div>
		<div class="modal-footer">
			<a href="#" class="btn btn-primary" data-dismiss="modal" aria-hidden="true"><cms:contentText key="CLOSE" code="nomination.past.winners"/></a>
		</div>
	</div>
</div>

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
$(document).ready(function() {
	G5.props.URL_JSON_NOMINATIONS_WINNERS_DETAIL = '${pastWinnersPageDataUrl}';
	G5.props.URL_JSON_NOMINATIONS_WINNERS_DETAIL_TRANSLATE_COMMENT = G5.props.URL_ROOT+'nomination/translate.do?method=translate';
	G5.props.URL_JSON_NOMINATIONS_CERTIFICATE_DATA = G5.props.URL_ROOT+'nomination/viewCertificate.do?method=getCertificate';
    G5.props.URL_CERT_TPL_ROOT = G5.props.URL_ROOT + 'assets/tpl/';
    G5.props.URL_JSON_EZ_RECOGNITION_MEMBER_INFO = "${pageContext.request.contextPath}/recognitionWizard/memberInfo.do";
    G5.props.URL_JSON_EZ_RECOGNITION_SEND_EZRECOGNITION = "${pageContext.request.contextPath}/recognitionWizard/submitEasyRecognition.do";
    G5.props.URL_JSON_PARTICIPANT_FOLLOW = G5.props.URL_ROOT+'publicRecognitionFollowUnFollowPax.do?method=followUnFollowPax';
    G5.props.URL_PDF_SERVICE = '${pdfServiceUrl}';

    //attach the view to an existing DOM element
    var nlpv = new NominationsWinnersDetailPageView({
        el:$('#nominationsWinnersDetailPageView'),
        pageNav : {
            back : {
            	 text : '<cms:contentText key="BACK" code="system.button" />',
	              url : 'javascript:history.go(-1);'
            },
            home : {
            	    text : '<cms:contentText key="HOME" code="system.general" />',
	                url : '${pageContext.request.contextPath}/homePage.do'
            }
        },
        pageTitle : 'Nomination Winners Detail'
    });
});
</script>
<%@include file="/submitrecognition/easy/flipSide.jsp" %>
