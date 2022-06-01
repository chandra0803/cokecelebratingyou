<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

<script>
  $(document).ready(function(){
    G5.props.URL_JSON_EZ_RECOGNITION_MEMBER_INFO = "${pageContext.request.contextPath}/recognitionWizard/memberInfo.do";
    G5.props.URL_JSON_EZ_RECOGNITION_SEND_EZRECOGNITION = "${pageContext.request.contextPath}/recognitionWizard/submitEasyRecognition.do";
  });
</script>

<script type="text/template" id="recognitionModuleFlipSideTpl">

    <div class="ezRecLiner">

    <div class="modal-body">
        <button type="button" id="ezRecModalCloseBtn"  class="close"  aria-hidden="true"><i class="icon-close"></i></button>
        <div class="cardContainer">

        <!--  -->
            <div class="card card-large card-profile">
                <div class="card-front">
                    <div class="card-top">
                        <span class="avatar avatarwrap">
                        {{#if profilePicURL}}
                                <img src="{{profilePicURL}}"  />
                            {{else}}
                                <div class="avatar-initials">{{trimString firstName 0 1}}{{trimString lastName 0 1}}</div>
                            {{/if}}
                            {{#if follow}}
                                <span class="follow icon-user-add icon-user-check" data-follow="{{follow}}" data-id="{{id}}">
                                </span>
                            {{else}}
                                <span class="follow icon-user-add" data-follow="{{follow}}" data-id="{{id}}">
                                </span>
                            {{/if}}
                            </span>
                    </div>

                    <div class="card-details" recipientID="{{recipientId}}">
                        <div class='card-details-inner-wrap'>
                            <div class="participant-name"><a href='{{participantUrl}}'>{{firstName}} {{lastName}}</a></div>
                            <div class="participant-info">{{#if title}}{{title}} <br/> {{/if}}
                                {{#if orgName}}{{orgName}}{{/if}}
                                {{#if department}}{{#if orgName}} | {{/if}} {{department}}{{/if}}
                            </div>
                        </div>
                    </div>
                </div>
            </div>

        </div><!-- ./cardContainer -->

        <div class="formContainer">



                <form action="${pageContext.request.contextPath}/recognitionWizard/sendRecognitionDisplay.do" method="post" name="ezRecogntionForm" id="ezRecogntionForm" class="form-horizontal">
				<input type="hidden" name="org.apache.struts.action.TOKEN" value="<%=session.getAttribute("org.apache.struts.action.TOKEN") %>"/>
                {{#if hasPromotions}}
                    <div class="control-group validateme" data-validate-flags="nonempty" data-validate-fail-msgs='{"nonempty" : "<cms:contentText key="SELECT_PROMO" code="recognitionSubmit.errors"/>"}'>
                        <label class="control-label " for="ezRecognitionPromotionSelect"><cms:contentText key="SELECT_PROMO" code="recognition.submit"/>:</label>
                        <div class="controls">

                        {{#if hasOnePromotion}}
                            <span class="naked-input ">{{promotions.0.name}}</span>
                        {{/if}}
                            <select id='ezRecognitionPromotionSelect' name='promotionId' class="" {{#if hasOnePromotion}}style="display:none"{{/if}}>
                                <option value=""><cms:contentText key="SELECT_PROMO" code="recognition.submit"/></option>
                                {{#promotions}}
                                    <option value="{{id}}"
                                        data-isEasy="{{#if attributes.isEasy}}true{{else}}false{{/if}}"
                                        data-hasECard="{{#if attributes.ecardsActive}}true{{else}}false{{/if}}"
                                        data-hasComments="{{#if attributes.commentsActive}}true{{else}}false{{/if}}">
                                        {{name}}
                                    </option>
                                {{/promotions}}
                            </select>

                        </div>
                    </div>

                    {{#if hasOrgUnits}}
                    <div class="control-group validateme" data-validate-flags="nonempty" data-validate-fail-msgs='{"nonempty" : "<cms:contentText key="SELECT_NODE" code="recognitionSubmit.errors"/>"}'>
                        <label  class="control-label " for="ezRecognitionOrgUnitSelect"><cms:contentText key="SELECT_ORG_UNIT" code="recognition.submit"/>:</label>
                        <div class="controls">

                            <select id='ezRecognitionOrgUnitSelect' class="" name='nodeId'>
                                <option value=""><cms:contentText key="SELECT_ORG_UNIT" code="recognition.submit"/></option>
                                {{#orgUnits}}
                                    <option value="{{id}}">{{name}}</option>
                                {{/orgUnits}}
                            </select>

                        </div>
                    </div>
                    {{/if}}

                    <div id="ezRecognitionCommentBox">
                        <label class="comment-label"><cms:contentText key="COMMENTS" code="recognition.submit"/></label>

                        <div class="commentTools">
                            <span class="remCharsLabel"><cms:contentText key="CHAR_LIMIT" code="recognition.submit"/></span> <span class="remChars" id="ezRecharCount">&nbsp;</span>
                            <span class="spellchecker dropdown">
                                <button class="checkSpellingDdBtn btn btn-mini btn-icon btn-primary btn-inverse dropdown-toggle"
                                        title="<cms:contentText key="SPELL_CHECK" code="recognition.submit"/>"
                                        data-toggle="dropdown">
                                        <i class="icon-check"></i>
                                </button>
                                <ul class="dropdown-menu langs">
                                    <li><a class="check"><b><cms:contentText key="SPELL_CHECK" code="recognition.submit"/></b></a></li>
                                {{#spellChecker}}
                                <li class="lang {{lang}}" data-lang="{{lang}}"><a>{{langTitle}}</a></li>
                                {{/spellChecker}}
                                </ul>
                            </span>
                        </div>

                        <div class="control-group validateme"
                            data-validate-fail-msgs='{"nonempty":"<cms:contentText key="COMMENTS_REQUIRED" code="recognitionSubmit.errors"/>","maxlength":"<cms:contentText key="COMMENT_LIMIT" code="recognitionSubmit.errors"/>"}'
                            data-validate-flags='nonempty,maxlength'
                            data-validate-max-length="1000" >
                            <textarea name="comments" id="comments"
                                class="recognition-comment "
                                placeholder="<cms:contentText key='COMMENTS' code='recognition.submit'/>"
                            {{#if hasOrgUnits}}
                                rows="3"
                            {{else}}
                                rows="6"
                            {{/if}}
                                maxlength="1000"
                                data-max-chars="1000"></textarea>
                        </div>

                        <!-- <textarea rows="2" name="comments" class="recognition-comment" data-max-chars="1000"></textarea> -->
                    </div>

                    <div class="ezRecFormBtnsWrapper">

			<beacon:authorize ifNotGranted="LOGIN_AS">
                        <button type="button" id="ezRecognitionSendBtn" class="btn btn-primary ezRecButton btn-fullmobile"><cms:contentText key="SEND" code="system.button"/></button>
			</beacon:authorize>

                        <button type="button" id="ezRecognitionCardBtn" class="btn btn-primary btn-inverse ezRecButton btn-fullmobile"><cms:contentText key="MORE_OPTIONS" code="recognition.submit"/></button>

                        <button type="button" id="ezRecognitionContinueBtn" class="btn btn-primary btn-inverse ezRecButton btn-fullmobile"><cms:contentText key="CONTINUE" code="system.button"/></button>

                        <button type="button" id="ezRecognitionCancelBtn" class="btn ezRecButton btn-fullmobile"><cms:contentText key="CANCEL" code="system.button"/></button>

                    </div>

                {{else}}

                    <div class="EZRecNoPromotionsWrapper">
                        <p><cms:contentText key="NO_PROMOTIONS_COMMON" code="recognition.submit"/></p>
                    </div>
                    <div class="ezRecFormBtnsWrapper empty">
                        <button type="button" id="ezRecognitionCancelBtn" class="btn ezRecButton"><cms:contentText key="CANCEL" code="system.button"/></button>
                    </div>

                {{/if}}

                    {{#unless hasOrgunits}}
                    <input type="hidden" name="nodeId" value="{{orgUnits.0.id}}">
                    {{/unless}}
                    <input type="hidden" name="claimRecipientFormBeans[0].nodeId" value="{{recipientNodeId}}" />
                    <input type="hidden" name="claimRecipientFormBeans[0].userId" value="{{recipientId}}" />
                    <input type='hidden' name='claimRecipientFormBeans[0].nodes' value='{{recipNodesObj}}' />

                </form>

                <form action="${pageContext.request.contextPath}/recognitionWizard/sendRecognitionDisplay.do" method="post" id="complexRecognition" style="display:none">
				<input type="hidden" name="org.apache.struts.action.TOKEN" value="<%=session.getAttribute("org.apache.struts.action.TOKEN") %>"/>
                    <fieldset>
                        {{#unless hasOrgunits}}
                        <input type="hidden" name="nodeId" value="{{orgUnits.0.id}}">
                        {{/unless}}
                        <input type="hidden" name="claimRecipientFormBeans[0].nodeId" id="ezRecognitionRecipientID" value="{{recipientNodeId}}" />
                        <input type="hidden" name="claimRecipientFormBeans[0].userId" id="recipientNodeId" value="{{recipientId}}" />
                        <input type='hidden' name='claimRecipientFormBeans[0].nodes' value='{{recipNodesObj}}' />
                        <input type="hidden" name="hasECard" id="ezRecognitionHasEcard" />
                    </fieldset>
                </form>

            </div>
        </div>

    </div> <!-- ./module-liner -->
</script>

<div id="ezRecSendSuccessModal" class="modal hide fade ezRecSuccessModal">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="icon-close"></i></button>
    <p><cms:contentText key="RECOG_SENT" code="recognition.submit"/></p>
</div>

<script id="ezRecNoOrgErrorTemplate" type="text/x-handlebars-template">
    <span><cms:contentText key="ORG_AND_PROMO" code="recognition.submit"/></span>
</script>

<script id="ezRecNoPromoErrorTemplate" type="text/x-handlebars-template">
    <span><cms:contentText key="SELECT_PROMO" code="recognition.submit"/></span>
</script>
