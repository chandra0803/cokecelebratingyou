<%@page import="com.biperf.core.utils.BeanLocator"%>
<%@page import="com.biperf.core.service.system.SystemVariableService"%>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.ui.login.LoginPageFirstTimeForm"%>
<%@ page import="com.biperf.core.value.AboutMeValueBean"%>

<%

int imageSizeLimit = ( (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME ) ).getPropertyByName( SystemVariableService.SYSTEM_IMG_SIZE_LIMIT ).getIntVal();

%>
<div id="firstTimeLoginPage" class="firstTimeLoginPage page-content">

    <div class="row-fluid">
        <div class="span12">

            <h2><cms:contentText key="FIRST_TIME_VISIT" code="profile.security.tab" /></h2>
            <p class="lead"><cms:contentText key="FEW_THINGS" code="profile.security.tab" /></p>

			<div id="firstTimeLoginErrorBlock"
				class="alert alert-block alert-error" style="display: none;">
				<c:if test="${not empty serviceValidationErrors}">
					<div class="error">
						<h4>
							<cms:contentText key="FOLLOWING_ERRORS"
								code="system.generalerror" />
						</h4>
						<ul>
							<c:forEach var="serviceError" items="${serviceValidationErrors}">
								<li>${serviceError.arg4}</li>
							</c:forEach>
						</ul>
					</div>
				</c:if>
				<cms:errors />
			</div>

			<html:form styleId="firstTimeLoginPageForm" action="loginPageFirstTimeSaveInfo.do?method=saveUserInfo" method="POST" enctype="multipart/form-data">
				<html:hidden property="forceChangePassword" name="loginPageFirstTimeForm" value="${loginPageFirstTimeForm.forceChangePassword}" />
				<html:hidden property="showTermsAndConditions" name="loginPageFirstTimeForm" value="${loginPageFirstTimeForm.showTermsAndConditions}" />
				<html:hidden property="showUpdateEmail" name="loginPageFirstTimeForm" value="${loginPageFirstTimeForm.showUpdateEmail}" />
				<input type="hidden" id="serverReturnedErrored" name="serverReturnedErrored" value="${serverReturnedErrored}" />
				<input type="hidden" id="previousPhotoUrl" name="previousPhotoUrl" value="${previousPhotoUrl}">

				<c:if test="${loginPageFirstTimeForm.showTermsAndConditions}">
                <fieldset id="termsAndConditions" class="firstTimeLoginFieldSet">
                    <legend><cms:contentText key="TERMS_AND_CONDITION" code="profile.preference.tab" /></legend>
                    <cms:contentText key="TERMS_AND_COND_TEXT" code="profile.terms.text" />
                    <div class="control-group validateme" data-validate-flags="match" data-validate-match="TermsAndConditionsRadio1" data-validate-fail-msgs="{&quot;nonempty&quot; : &quot;<cms:contentText key="TERMS_COND_TO_CONT" code="profile.personal.info" />&quot;, &quot;match&quot; : &quot;<cms:contentText key="TERMS_COND_TO_CONT" code="profile.personal.info" />&quot;}">
                        <label class="control-label radio bold">
                            <input type="radio" name="TermsAndConditionsRadios" class="TermsAndConditionsRadios" id="TermsAndConditionsRadio1" value="accept"> <cms:contentText code="profile.personal.info" key="ACCEPT_TERMS_COND" />
                        </label>
                    </div>
                    <label class="control-label radio">
                        <cms:contentText code="profile.personal.info" key="DECLINE_TERMS" /> <input type="radio" name="TermsAndConditionsRadios" class="TermsAndConditionsRadios" id="TermsAndConditionsRadio2" value="decline">
                    </label>
                    <button class="btn btn-primary nextandClearSectionBtn" disabled>
                      <cms:contentText code="system.button" key="CONTINUE" />
                    </button>
                    <a href="<%=RequestUtils.getBaseURI(request)%>/login.do" class='btn button'><cms:contentText key="CANCEL" code="system.button"/></a>
                </fieldset>
				</c:if>

                <c:if test="${!loginPageFirstTimeForm.fromSSO && loginPageFirstTimeForm.forceChangePassword}">				
	                      <fieldset id="securitySettings" class="firstTimeLoginFieldSet form-horizontal" style="display:none">
	                          <legend><cms:contentText key="CHANGE_SETTINGS" code="profile.security.tab" /></legend>
	                                              
	                            <p><c:out value="${passwordRequirements}"/></p>
	                          	<div class="control-group validateme" data-validate-flags="nonempty" data-validate-fail-msgs="{&quot;nonempty&quot; : &quot;<cms:contentText  key="PWD_REQ" code="login.forgotpwd" />&quot;}">
	                              	<label class="control-label first-pass" for="inputPassword" > <cms:contentText key="PASSWORD_LABEL" code="login.forgotpwd" /></label>
                              		<span class="controls input-append input-append-inside passShow first-pass-input">
										<html:password styleId="inputPassword" property="newPassword" name="loginPageFirstTimeForm" styleClass="span4" />
										<a class="add-on btn btn-link show-pw icon-eye-off"></a>
									</span>
	                          	</div>
	                          	                                       
	                          <button class="btn btn-primary nextSectionBtn"><cms:contentText code="system.button" key="CONTINUE" /></button>
	                      </fieldset>
                </c:if>              
                
				<c:if test="${loginPageFirstTimeForm.showUpdateEmail}">
                 <fieldset id="emailAddress" class="firstTimeLoginFieldSet form-horizontal" style="display:none">
                    <legend><cms:contentText code="participant.emailaddr.list" key="EMAILADDR" /></legend>
                        <div class="control-group validateme" data-validate-flags="nonempty,email" data-validate-fail-msgs="{&quot;nonempty&quot; : &quot;<cms:contentText code="participant.emailaddr" key="EMAIL_REQUIRED" />&quot;, &quot;email&quot; : &quot;<cms:contentText code="participant.emailaddr" key="VALID_EMAIL" />&quot;}">
                        <label class="control-label" for="emailAddress" > <cms:contentText code="participant.emailaddr.list" key="EMAILADDR" /></label>
                        <div class="controls">
                            <html:text styleId="emailAddress" property="email" name="loginPageFirstTimeForm" />
                        </div>
                    </div>
                    <button class="btn btn-primary nextSectionBtn"><cms:contentText code="system.button" key="CONTINUE" /></button>
                </fieldset>
                </c:if>

                <fieldset id="personalInfo" class="firstTimeLoginFieldSet form-horizontal" style="display:none">
                    <legend><cms:contentText key="ABOUT_ME" code="profile.personal.info" /></legend>
                    <div class="control-group">
                        <label class="control-label"><cms:contentText key="PICTURE_ALT" code="profile.personal.info" />
                            <span class="optional"><cms:contentText key="OPTIONAL" code="system.common.labels" /></span>
                        </label>
                        <div class="controls">
                             <img src="${loginPageFirstTimeForm.avatarUrl}" width="60px" height="60px" class="avatar" />
                                <input type="file" name="profileImage" id="profilePicUpload" />
                                <p class="muted"><cms:contentTemplateText key="UPLOAD_INSTRUCTIONS" code="profile.personal.info" args="${beacon:systemVarInteger('system.image.upload.size.limit')}" /></p>
                            <p class="alert alert-info hide no-fileinput"><cms:contentText key="DEVICE_NOT_SUPPORTED" code="profile.personal.info" /></p>
                        </div>
                    </div>

                    <div id="aboutMeSection">
                        <nested:iterate id="aboutMeItem" name="loginPageFirstTimeForm" property="aboutMeQuestions">
						<nested:hidden property="aboutmeQuestion" />
						<nested:hidden property="aboutmeQuestioncode" />
                        <div class="control-group">
                            <label class="control-label" for="aboutmeAnswer">${aboutMeItem.aboutmeQuestion}</label>
                            <div class="controls">
                                <nested:text property="aboutmeAnswer" />
                                <span class="optional"><cms:contentText key="OPTIONAL" code="system.common.labels" /></span>
                            </div>
                        </div>
                        </nested:iterate>
                    </div>
                    <button class="btn btn-primary nextSectionBtn"><cms:contentText code="system.button" key="CONTINUE" /></button>
                </fieldset>

                <c:if test="${allowSmsForUserCountry}">
                <fieldset id="myPreferences" class="firstTimeLoginFieldSet form-horizontal" style="display:none">
                    <legend><cms:contentText key="MY_PREFERENCES" code="profile.preference.tab" /></legend>
                    <div class="control-group">
                        <div class="control-label"><cms:contentText code="profile.personal.info" key="TXT_MESGS_SELECT" />
                            <span class="optional"><cms:contentText key="OPTIONAL" code="system.common.labels" /></span></div>
                        <div class="controls">
                            <label class="checkbox selectToggle">
                                <a href="#" class="selectTextAlerts selectAll"><cms:contentText key="SELECT_ALL" code="profile.preference.tab"/></a> | <a href="#" class="selectTextAlerts deselectAll"><cms:contentText key="UNCHECK_ALL" code="profile.preference.tab"/></a>
                            </label>

                            <div id="allTextAlertsWrapper" data-validate-flags="nonempty" data-validate-fail-msgs='{&quot;nonempty&quot; : &quot;<cms:contentText code="profile.personal.info" key="NO_ALERTS_PICKED" />&quot;}'>
                                <c:forEach items="${messageSMSGroupTypeList}" var="SMSGroupType">
                                <label class="checkbox">
                                    <html:multibox property="activeSMSGroupTypes" value="${SMSGroupType.code}" />
                                    <c:out value="${SMSGroupType.name}" />
                                </label>
                                </c:forEach>
                            </div>
                        </div>
                    </div>

                    <div class="control-group">
                        <div class="controls"><strong><cms:contentText key="MESSAGE_TO" code="profile.preference.tab" /></strong></div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="telephoneCountryCode"><cms:contentText key="TELEPHONE_COUNTRY" code="profile.preference.tab" /></label>
                        <div class="controls">
                            <html:select styleId="telephoneCountryCode" property="countryPhoneCode"
									name="loginPageFirstTimeForm">
								<html:options collection="countryList" property="countryCode"
									labelProperty="countryNameandPhoneCodeDisplay" />
							</html:select>
                        </div>
                    </div>
                    <div class="control-group" data-validate-flags="nonempty" data-validate-fail-msgs="{&quot;nonempty&quot; : &quot;<cms:contentText key="PHN_REQ" code="profile.preference.tab" />&quot;}">
                        <label class="control-label" for="phoneNumber"><cms:contentText key="PHONE_NUMBER" code="profile.preference.tab" /></label>
                        <div class="controls">
                            <html:text styleId="phoneNumber" property="textPhoneNbr" maxlength="12" name="loginPageFirstTimeForm" />
                        </div>
                    </div>

                    <div class="control-group" data-validate-flags="nonempty" data-validate-fail-msgs="{&quot;nonempty&quot; : &quot;<cms:contentText code="profile.personal.info" key="ACCEPT_TERMS_TXT_MSG" />&quot;}">
                        <div class="controls">
                            <label class="checkbox">
                                <html:checkbox styleId="txtAlertTerms" property="termsAndConditionsForTxtMsgs" name="loginPageFirstTimeForm" />
                                <cms:contentText key="ACCEPT_TERMS_TXT_MSG" code="profile.preference.tab" />
                            </label>

                            <div class="txtTerms">
                                <label><cms:contentText key="TERMS_AND_CONDITION" code="profile.preference.tab" /></label>
                                <p><c:out value="${termsAndCondition}" /></p>
                            </div>
                        </div>
                    </div>
                     <!--Client customization starts for WIP #23784-->
				<a href="#termsAndConditionsModal" class="termsAndConditionsLanguages" data-toggle="modal"><cms:contentText key="TERMS_CONDITIONS_MULTIPLE_LANGUAGES" code="participant.termsAndConditions" /></a>
				<div class="modal hide fade" id="termsAndConditionsModal" >
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"><i class="icon-close"></i></button>
						<h3><cms:contentText key="TERMS_CONDITIONS_MULTIPLE_LANGUAGES" code="participant.termsAndConditions" /></h3>
					</div>
					<div class="modal-body">
								<ul style="list-style: none;">
									<c:forEach var="resource" items="${termsAndCondInMulLanguages}"
										varStatus="status">
										<c:if test="${null != resource.contentDataMap['TEXT']}">
											<li><a
												href="<c:out escapeXml="false" value="${resource.contentDataMap['URL']}"/>"
												target="_blank"> <c:out escapeXml="false"
														value="${resource.contentDataMap['TEXT']}" /> 
											</a></li>
										</c:if>
									</c:forEach>
									
							</div>
				</div><!--Client customization ends for WIP #23784-->
                </fieldset>
				</c:if>

                <div class="form-actions pullBottomUp" style="display:none">
                    <button class="btn btn-primary" type="submit" id="submitBtn"><cms:contentText code="system.button" key="SUBMIT" /></button>
                </div>
            </html:form>
            <!-- prefix urls for uploaded images. stager for thumbnail, final for full image. -->
            <input type="hidden" id="finalPrefixURL" value="">
            <input type="hidden" id="stagerPrefixURL" value="">

        </div>
    </div>
    <div id="declineModal" class="modal hide fade" role="dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><i class="icon-close"></i></button>
                <h3 class="modal-title"><cms:contentText key="DECLINE" code="system.general" /></h3>
            </div>
            <div class="modal-body">
                <cms:contentText key="DECLINE_INST_TEXT" code="system.general" />
            </div>
            <div class="form-actions " >
                <button class="btn btn-primary" id="confirmDeclination"><cms:contentText key="CONFIRM" code="system.general" /></button>
                <button type="button" class="btn" data-dismiss="modal"><cms:contentText key="CANCEL" code="ssi_contest.pax.manager" /></button>
            </div>

        </div>
    </div><!-- /#loginPageDeclineModal -->
</div>

<c:if test="${contactUsEmailConfirmation == true}">
	<tiles:insert definition="emailSent.success.confirmation.modal" flush="true" ignore="true" />
</c:if>


<script>
$(document).ready(function() {

	//Profile Pic Upload JSON
	G5.props.URL_JSON_FIRST_TIME_LOGIN_PHOTO_UPLOAD = G5.props.URL_ROOT+'loginPageFirstTimeUploadAvatar.do?method=uploadAvatar';
    G5.props.URL_DECLINE_URL = G5.props.URL_ROOT +'loginPageFirstTimeSaveInfo.do?method=declineTNC';
    window.loginPageFirstTimeView = new LoginPageFirstTimeView({
        el:$('#firstTimeLoginPage'),
        pageNav : {},
        pageTitle : '<cms:contentText code="profile.personal.info" key="FIRST_TIME_LOGIN_TITLE" />',
		loggedIn : false
    });
});
</script>