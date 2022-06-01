<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<h2><cms:contentText key="CHANGE_SETTINGS" code="profile.security.tab" /></h2>

<html:form styleId="profilePageSecurityTabForm"
    action="saveProfilePageSecurity?method=changePasswordAndRecovery" styleClass="form-horizontal">
    <html:hidden property="method" value="changePasswordAndRecovery" />

    <fieldset>

		<div class="passwordReset wrapper">
			<h3><cms:contentText key="RESET_PASSWORD" code="login.loginpage" /></h3>
			<p><c:out value="${passwordRequirements}" /></p>
			<c:if test="${ changePasswordForm.displayOldPassword }">
			<div class="control-group" data-validate-flags="nonempty" data-validate-fail-msgs='{"nonempty" : "<cms:contentText key="CURRENT_PWD_REQ" code="login.forgotpwd" />"}'>
				<label class="control-label" for="profilePageSecurityTabCurrentPassword"><cms:contentText key="CURRENT_PASSWORD_LABEL" code="login.forgotpwd" /></label>
				<div class="controls input-append input-append-inside passShow">
					<html:password  styleId="profilePageSecurityTabCurrentPassword" property="oldPassword"
                    styleClass="span4" />
					<a class="add-on btn btn-link icon-eye-off show-pw"></a>
				</div>
			</div>
			</c:if>
			<div class="control-group" data-validate-flags="nonempty" data-validate-fail-msgs='{"nonempty" : "<cms:contentText key="NEW_PWD_REQ" code="login.forgotpwd" />"}'>
				<label class="control-label" for="profilePageSecurityTabConfirmNewPassword"><cms:contentText key="PASSWORD_LABEL" code="login.forgotpwd" /> </label>
				<span class="controls input-append input-append-inside passShow">
					<html:text  styleId="profilePageSecurityTabConfirmNewPassword" property="newPassword"
                    styleClass="span4" />
					<a class="add-on btn btn-link show-pw icon-eye"></a>
				</span>
			</div>
		</div>


		<div class="accountRecovery wrapper">
			<h3><cms:contentText key="ACCOUNT_RECOVERY" code="login.forgotpwd" /></h3>
			<p><cms:contentText key="INFO_EMAIL_PHONE_DTLS" code="login.forgotpwd" /></p>
			<p class="accountRecoveryInfo"><cms:contentText key="ACC_COMMUNICATION_ASSISTANCE" code="login.forgotpwd" /> <span class="terms-link"><cms:contentText key="MBLOX_TNC" code="login.forgotpwd" /></span></p>

			<div class="control-group validateme emailAddress" data-validate-flags="email" data-validate-fail-msgs='{"email" : "<cms:contentText key="EMAIL_REQ" code="login.forgotpwd" />"}'>
                <div class="row">
                    <label class="control-label" for="profilePageSecurityTabEmailAddress"><cms:contentText code="participant.emailaddr.list" key="EMAILADDR" /></label>
    				<div class="controls">
    					<c:choose>
                  <c:when test="${ maskSecurityQA }">
                    <c:out value="${ mask }"/>
                  </c:when>
                  <c:otherwise>
                    <html:text property="emailAddress" maxlength="100"
    					styleId="emailAddress"
                        styleClass="span4 saveField" readonly="true" />
                  </c:otherwise>
                </c:choose>
    				</div>
    				<div class="controls">
                        <p class="help-block"><cms:contentText key="EMAIL_PWD_REQ" code="login.forgotpwd" /></p>
                    </div>
                </div>
                <div class="row">
                    <div class="verify-wrapper">
                        <span class="emailVerified hide"><i class="icon-check-circle"/><cms:contentText key="VERIFIED" code="profile.preference.tab" /></span>
                        <span class="emailNotVerified hide"><i class="icon-cancel-circle"/><cms:contentText key="NOT_VERIFIED" code="profile.preference.tab" /></span>
                        <a class="btn btn-primary btn-small verifyEmail <beacon:authorize ifAnyGranted='LOGIN_AS'>hide</beacon:authorize>" data-type="email" data-verified="${ changePasswordForm.emailVerified }"><cms:contentText key="SEND_EMAIL" code="profile.preference.tab" /></a>
                    </div>
                </div>
			</div>
            <div class="andor"><span class="and-or-hr"><hr /></span><span class="and-or-text"><cms:contentText key="AND_OR" code="login.loginpage" /></span><span class="and-or-hr"><hr /></span></div>
            <div class="mobileNumber">
                <div class="row">
                    <div class="control-group countryPhoneCodeWrapper" data-validate-flags="nonempty"
                         data-validate-fail-msgs='{"nonempty" : "<cms:contentText key="COUNTRY_CODE_REQ" code="login.forgotpwd" />"}'>
                        <label class="control-label" for="countryPhoneCode"><cms:contentText key="TELEPHONE_COUNTRY" code="profile.preference.tab" /> + <br /><cms:contentText key="PHONE_NUMBER" code="login.password.reset" /></label>
                            <div class="controls">
                                <html:select styleId="countryPhoneCode" styleClass="saveField" property="countryPhoneCode"
                                        name="changePasswordForm">
                                    <html:option value=""><cms:contentText key="SELECT_ONE" code="system.general"/></html:option>
                                    <html:options collection="countryList" property="countryCode"
                                        labelProperty="countryNameandPhoneCodeDisplay" />
                                </html:select>
                            </div>
                    </div>
                    <div class="control-group phoneNumberWrapper" data-validate-flags="nonempty" data-validate-fail-msgs='{"nonempty" : "<cms:contentText key="PHNO_REQ" code="login.forgotpwd" />"}'>
                        <div class="controls phoneNumber">
                        <c:choose>
                            <c:when test="${ maskSecurityQA }">
                                <c:out value="${ mask }"/>
                            </c:when>
                            <c:otherwise>
                               <html:text property="phoneNumber" maxlength="100" styleId="phoneNumber"
                            styleClass="span4 saveField" readonly="true"/>
                            </c:otherwise>
                        </c:choose>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="verify-wrapper">
                        <span class="phoneVerified hide"><i class="icon-check-circle"/><cms:contentText key="VERIFIED" code="profile.preference.tab" /></span>
                        <span class="phoneNotVerified hide"><i class="icon-cancel-circle"/><cms:contentText key="NOT_VERIFIED" code="profile.preference.tab" /></span>
                        <a class="btn btn-primary btn-small verifyPhone <beacon:authorize ifAnyGranted='LOGIN_AS'>hide</beacon:authorize>" data-type="phone" data-verified="${ changePasswordForm.phoneVerified }"><cms:contentText key="SEND_TEXT_MESSAGE" code="profile.preference.tab" /></a>
                    </div>
                </div>
            </div>
            <h6><cms:contentText key="WHY_VERIFY" code="profile.preference.tab" /></h6>
            <p><cms:contentText key="VERIFY_EXPLANATION" code="profile.preference.tab" /></p>
			</div>
		</div>
        <fieldset id="profilePageSecurityTabFieldsetActions"
            class="form-actions">
          <beacon:authorize ifNotGranted="LOGIN_AS">
            <button id="profilePageSecurityTabButtonSave" name="triggeredBy" disabled value="profilePageSecurityTabButtonSave" class="btn btn-primary"
                type="submit"><cms:contentText key="SAVE_CHANGES" code="system.button" /></button>

            <button id="profilePageSecurityTabButtonCancel" name="triggeredBy"
                value="profilePageSecurityTabButtonCancel" class="btn"><cms:contentText key="CANCEL" code="system.button" /></button>
          </beacon:authorize>
        </fieldset>
    </fieldset>
</html:form>
<div class="modal hide fade autoModal recognitionResponseModal" id="saveSuccessModal">
    <div class="modal-header">
        <button class="close" data-dismiss="modal"><i class="icon-close"></i></button>
        <h1><cms:contentText key="SUCCESS" code="system.general" /></h1>
        <p class="messageText"></p>
    </div>
</div>
<div class="modal hide fade autoModal" id="verifyModal">
    <div class="modal-header">
        <button class="close" data-dismiss="modal"><i class="icon-close"></i></button>
        <h1 class="phoneHeader hide"><cms:contentText key="CODE_VERIFICATION" code="login.loginpage" /></h1>
        <h1 class="emailHeader hide"><cms:contentText key="CODE_VERIFICATION_EMAIL" code="login.loginpage" /></h1>

    </div>
    <div class="modal-body">
        <div class="alert alert-error hide"></div>
        <div class="alert alert-success hide"></div>
        <label class="code-label" for="code"><cms:contentText key="ONE_TIME_CODE" code="login.loginpage" /></label>
        <a class="no-get-wrapper"><span class="recovery-no-phone"><span class="icon-info"></span> <cms:contentText key="DIDNT_GET_IT" code="login.forgotpwd" /></span></a>
            <input type="text" name="code" id="code" />
    </div>
    <div class="modal-footer">
        <a class="btn btn-primary submitCode"><cms:contentText key="SUBMIT" code="system.button" /></a>
        <a class="btn" data-dismiss="modal"><cms:contentText key="CANCEL" code="system.button" /></a>
    </div>
</div>
<div class="modal hide fade autoModal" id="tncModal">
    <div class="modal-header">
        <button class="close" data-dismiss="modal"><i class="icon-close"></i></button>
        <h1><cms:contentText key="PAGE_TITLE" code="participant.termsAndConditions" escapeJavascript="true" /></h1>

    </div>
    <div class="modal-body">
        <p><cms:contentTemplateText key="RECOVERY_TNC" code="login.forgotpwd" args="${webappTitle}" escapeJavascript="true"/></p>
        <h3><cms:contentText key="CARRIER_SUPPORT" code="login.forgotpwd" escapeJavascript="true" /></h3>
        <p><cms:contentTemplateText key="CARRIER_SUPPORT_INFO" code="login.forgotpwd" args="${webappTitle}" escapeJavascript="true"/></p>
    </div>
</div>
<div class="modal hide fade autoModal" id="carrierModal">
    <div class="modal-header">
        <button class="close" data-dismiss="modal"><i class="icon-close"></i></button>
        <h1><cms:contentText key="CARRIER_SUPPORT" code="login.forgotpwd" escapeJavascript="true" /></h1>

    </div>
    <div class="modal-body">
        <p><cms:contentTemplateText key="CARRIER_SUPPORT_INFO" code="login.forgotpwd" args="${webappTitle}" escapeJavascript="true"/></p>
    </div>
</div>
