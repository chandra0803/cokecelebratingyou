<%@ include file="/include/taglib.jspf"%>
<%@ page import="org.apache.struts.Globals"%>
<%@ page import="java.time.LocalDateTime"%>
<% pageContext.setAttribute("year",LocalDateTime.now().getYear()); %>
<!-- ======== LOGIN PAGE ======== -->

<!-- Page Body -->

<script>
    window.login = {
        content: [
            {
                key: "REGISTER",
                code: "login.loginpage",
                content: "<cms:contentText key="REGISTER" code="login.loginpage" escapeJavascript="true" />"
            },
            {
                key: "TITLE",
                code: "login.loginpage",
                content: "<cms:contentText key="TITLE" code="login.loginpage" escapeJavascript="true" />"
            },
            {
                key: "FORGOT_PASSWORD",
                code: "login.loginpage",
                content: "<cms:contentText key="FORGOT_PASSWORD" code="login.loginpage" escapeJavascript="true" />"
            },
            {
                key: "FORGOT_YOUR_PASSWORD",
                code: "login.loginpage",
                content: "<cms:contentText key="FORGOT_YOUR_PASSWORD" code="login.loginpage" escapeJavascript="true" />"
            },
            {
                key: "FORGOT_LOGIN_ID",
                code: "login.loginpage",
                content: "<cms:contentText key="FORGOT_LOGIN_ID" code="login.loginpage" escapeJavascript="true" />"
            },
            {
                key: "FORGOT_LOGIN_ID",
                code: "login.password.reset",
                content: "<cms:contentText key="FORGOT_LOGIN_ID" code="login.password.reset" escapeJavascript="true" />"
            },
            {
                key: "RESET_YOUR_PASSWORD",
                code: "login.password.reset",
                content: "<cms:contentText key="RESET_YOUR_PASSWORD" code="login.password.reset" escapeJavascript="true" />"
            },
            {
                key: "REGISTER_NEW_ACCOUNT",
                code: "login.loginpage",
                content: "<cms:contentText key="REGISTER_NEW_ACCOUNT" code="login.loginpage" escapeJavascript="true" />"
            },
            {
                key: "LOG_IN",
                code: "login.loginpage",
                content: "<cms:contentText key="LOG_IN" code="login.loginpage" escapeJavascript="true" />"
            },
            {
                key: "NO_LOGIN_ID_OR_PASSWORD",
                code: "login.loginpage",
                content: "<cms:contentText key="NO_LOGIN_ID_OR_PASSWORD" code="login.loginpage" escapeJavascript="true" />"
            },
            {
                key: "PASSWORD",
                code: "login.loginpage",
                content: "<cms:contentText key="PASSWORD" code="login.loginpage" escapeJavascript="true" />"
            },
            {
                key: "NEW_PASSWORD",
                code: "login.loginpage",
                content: "<cms:contentText key="PASSWORD_LABEL" code="login.forgotpwd" escapeJavascript="true" />"
            },
            {
                key: "I_FORGOT_PASSWORD",
                code: "login.loginpage",
                content: "<cms:contentText key="I_FORGOT_PASSWORD" code="login.loginpage" escapeJavascript="true" />"
            },
            {
                key: "I_FORGOT_LOGIN_ID",
                code: "login.loginpage",
                content: "<cms:contentText key="I_FORGOT_LOGIN_ID" code="login.loginpage" escapeJavascript="true" />"
            },
			{
                key: "INSTRUCTIONS",
                code: "login.loginpage",
                content: "<cms:contentText key="INSTRUCTIONS" code="login.loginpage" escapeJavascript="true" />"
            },
            {
                key: "USERNAME",
                code: "login.loginpage",
                content: "<cms:contentText key="USERNAME" code="login.loginpage" escapeJavascript="true" />"
            },
            {
                key: "INTRO_PARA_ONE",
                code: "login.loginpage",
                content: "<cms:contentText key="INTRO_PARA_ONE" code="login.loginpage" escapeJavascript="true" />"
            },
            {
                key: "INTRO_PARA_TWO",
                code: "login.loginpage",
                content: "<cms:contentText key="INTRO_PARA_TWO" code="login.loginpage" escapeJavascript="true" />"
            },
            {
                key: "LOGIN_ID_REQ",
                code: "login.errors",
                content: "<cms:contentText key="LOGIN_ID_REQ" code="login.errors" escapeJavascript="true" />"
            },
            {
                key: "PASSWORD_REQ",
                code: "login.errors",
                content: "<cms:contentText code="login.errors" key="PASSWORD_REQ" escapeJavascript="true" />"
            },
            {
                key: "COPYRIGHT_TEXT",
                code:"system.general",
                content:"<cms:contentTemplateText key="COPYRIGHT_TEXT" code="system.general" args="${year}" delimiter=","/>"
            },
            {
                key: "CONTACT_US",
                code:"system.general",
                content:"<cms:contentText key="CONTACT_US" code="system.general" escapeJavascript="true"/>"
            },
            {
                key: "T&C",
                code:"system.general",
                content:"<cms:contentText key="T&C" code="system.general" escapeJavascript="true"/>"
            },
            {
                key: "PRIVACY_POLICY",
                code:"system.general",
                content:"<cms:contentText key="PRIVACY_POLICY" code="system.general" escapeJavascript="true"/>"
            },
            {
                key: "HTML",
                code:"admin.privacy",
                content:"<cms:contentText key="HTML" code="admin.privacy"  escapeJavascript="true"/>"
            },
            {
                key: "SUBMIT",
                code:"system.button",
                content:"<cms:contentText key="SUBMIT" code="system.button" escapeJavascript="true"/>"
            },
            {
                key:"EMAIL_ADDRESS",
                code:"participant.participant",
                content:"<cms:contentText key="EMAIL_ADDRESS" code="participant.participant" escapeJavascript="true"/>"
            },
            {
                key:"HELP_TEXT",
                code:"system.general",
                content:"<cms:contentText key="HELP_TEXT" code="login.loginpage" escapeJavascript="true"/>"
            },
            {
                key:"HELP_TITLE",
                code:"system.general",
                content:"<cms:contentText key="HELP" code="login.loginpage" escapeJavascript="true"/>"
            },
            {
                key: "CLOSE",
                code:"system.button",
                content:"<cms:contentText key="CLOSE" code="system.button" escapeJavascript="true"/>"
            },
            {
                key: "BACK_TO_LOGIN",
                code: "login.loginpage",
                content: "<cms:contentText key="BACK_TO_LOGIN" code="login.loginpage" escapeJavascript="true"/>"
            },
            {
                key: "MESSAGE_SENT",
                code: "login.loginpage",
                content: "<cms:contentText key="MESSAGE_SENT" code="login.loginpage" escapeJavascript="true"/>"
            },
            {
                key: "CHECK_YOUR_EMAIL",
                code: "login.loginpage",
                content: "<cms:contentText key="CHECK_YOUR_EMAIL" code="login.loginpage" escapeJavascript="true"/>"
            },
            {
                key: "METHOD_OF_CONTACT",
                code: "login.loginpage",
                content: "<cms:contentText key="METHOD_OF_CONTACT" code="login.loginpage" escapeJavascript="true"/>"
            },
            {
                key: "CODE_VERIFICATION",
                code: "login.loginpage",
                content: "<cms:contentText key="CODE_VERIFICATION" code="login.loginpage" escapeJavascript="true"/>"
            },
            {
                key: "ONE_TIME_CODE",
                code: "login.loginpage",
                content: "<cms:contentText key="ONE_TIME_CODE" code="login.loginpage" escapeJavascript="true"/>"
            },
            {
                key: "HELP",
                code: "login.loginpage",
                content: "<cms:contentText key="HELP" code="login.loginpage" escapeJavascript="true"/>"
            },
            {
                key: "RESET_PASSWORD",
                code: "login.loginpage",
                content: "<cms:contentText key="RESET_PASSWORD" code="login.loginpage" escapeJavascript="true"/>"
            },
            {
                key: "CONFIRM_PASSWORD",
                code: "login.loginpage",
                content: "<cms:contentText key="CONFIRM_PASSWORD" code="login.loginpage" escapeJavascript="true"/>"
            },
            {
                key: "PHONE_NUMBER",
                code: "login.password.reset",
                content: "<cms:contentText key="PHONE_NUMBER" code="login.password.reset" escapeJavascript="true"/>"
            },
            {
                key: "NO_PASSWORD",
                code: "login.loginpage",
                content: "<cms:contentText key="NO_PASSWORD" code="login.loginpage" escapeJavascript="true" />"
            },
            {
                key: "ACTIVATION",
                code: "login.loginpage",
                content: "<cms:contentText key="ACTIVATION" code="login.loginpage" escapeJavascript="true" />"
            },
            {
                key: "ACTIVATION_ATTRIBUTES",
                code: "login.loginpage",
                content: "<cms:contentText key="ACTIVATION_ATTRIBUTES" code="login.loginpage" escapeJavascript="true" />"
            },
            {
                key: "CONTACT_INFORMATION",
                code: "login.loginpage",
                content: "<cms:contentText key="CONTACT_INFORMATION" code="login.loginpage" escapeJavascript="true" />"
            },
            {
                key: "ACTIVATE_ACCOUNT",
                code: "login.loginpage",
                content: "<cms:contentText key="ACTIVATE_ACCOUNT" code="login.loginpage" escapeJavascript="true" />"
            },
            {
                key: "COUNTRY_CODE",
                code: "login.loginpage",
                content: "<cms:contentText key="COUNTRY_CODE" code="login.loginpage" escapeJavascript="true" />"
            },
            {
                key: "RECOVERY_INFORMATION",
                code: "login.loginpage",
                content: "<cms:contentText key="RECOVERY_INFORMATION" code="login.loginpage" escapeJavascript="true" />"
            },
            {
                key: "AND_OR",
                code: "login.loginpage",
                content: "<cms:contentText key="AND_OR" code="login.loginpage" escapeJavascript="true" />"
            },
            {
                key: "CREATE_PASSWORD",
                code: "login.loginpage",
                content: "<cms:contentText key="CREATE_PASSWORD" code="login.loginpage" escapeJavascript="true" />"
            },
            {
                key: "EMAIL_OR_PHONE",
                code: "login.loginpage",
                content: "<cms:contentText key="EMAIL_OR_PHONE" code="login.loginpage" escapeJavascript="true" />"
            },
            {
                key: "FORGOT_ID_SEARCH",
                code: "login.loginpage",
                content: "<cms:contentText key="FORGOT_ID_SEARCH" code="login.loginpage" escapeJavascript="true" />"
            },
            {
                key: "WE_FOUND_YOU",
                code: "login.loginpage",
                content: "<cms:contentText key="WE_FOUND_YOU" code="login.loginpage" escapeJavascript="true" />"
            },
            {
                key: "GETTING_CLOSER",
                code: "login.loginpage",
                content: "<cms:contentText key="GETTING_CLOSER" code="login.loginpage" escapeJavascript="true" />"
            },
            {
                key: "NOTHING_FOUND",
                code: "login.password.reset.errors",
                content: "<cms:contentText key="NOTHING_FOUND" code="login.password.reset.errors" escapeJavascript="true" />"
            },
            {
                key: "LOCKED_DOWN",
                code: "login.loginpage",
                content: "<cms:contentText key="LOCKED_DOWN" code="login.loginpage" escapeJavascript="true" />"
            },
            {
                key: "ACCOUNT_LOCK_SCREEN_CONTENT",
                code: "login.loginpage",
                content: "<cms:contentText key="ACCOUNT_LOCK_SCREEN_CONTENT" code="login.loginpage" escapeJavascript="true"/>"
            },
            {
                key: "SHARED_CONTACT_WARNING",
                code: "login.loginpage",
                content: "<cms:contentText key="SHARED_CONTACT_WARNING" code="login.loginpage" escapeJavascript="true" />"
            },
            {
                key: "USER_ID_IS_YOUR_EMPLOYEE_NUMBER",
                code: "login.loginpage",
                content: "<cms:contentText key="USER_ID_IS_YOUR_EMPLOYEE_NUMBER" code="login.loginpage" escapeJavascript="true" />"
            },
            {
                key: "ACTIVATION_ATT_CONFIRM",
                code: "login.loginpage",
                content: "<cms:contentText key="ACTIVATION_ATT_CONFIRM" code="login.loginpage" escapeJavascript="true" />"
            },
            {
                key: "LOGIN_ID_CONFIRM",
                code: "login.loginpage",
                content: "<cms:contentText key="LOGIN_ID_CONFIRM" code="login.loginpage" escapeJavascript="true" />"
            },
            {
                key: "INFO_EMAIL_PHONE_DTLS",
                code: "login.forgotpwd",
                content: "<cms:contentText key="INFO_EMAIL_PHONE_DTLS" code="login.forgotpwd" escapeJavascript="true" />"
            },
            {
                key: "ACC_COMMUNICATION_ASSISTANCE",
                code: "login.forgotpwd",
                content: "<cms:contentText key="ACC_COMMUNICATION_ASSISTANCE" code="login.forgotpwd" escapeJavascript="true" />"
            },
            {
                key: "MBLOX_TNC",
                code: "login.forgotpwd",
                content: "<cms:contentText key="MBLOX_TNC" code="login.forgotpwd" escapeJavascript="true" />"
            },
            {
                key: "TERMS_AND_CONDITIONS_INSTRUCTIONS",
                code: "participant.preference.edit",
                content: "<cms:contentTemplateText key="TERMS_AND_CONDITIONS_INSTRUCTIONS" code="participant.preference.edit" args="${webappTitle}" escapeJavascript="true"/>"
            },
            {
                key: "PAGE_TITLE",
                code: "participant.termsAndConditions",
                content: "<cms:contentText key="PAGE_TITLE" code="participant.termsAndConditions" escapeJavascript="true" />"
            },
            {
                key: "REGI_CODE",
                code: "login.registration",
                content: "<cms:contentText key="REGI_CODE" code="login.registration" />"
            },
            {
                key: "REGISTER",
                code: "login.registration",
                content: "<cms:contentText key="REGISTER" code="login.registration" />"
            },
            {
                key: "NO_LOGIN_ID_OR_PASSWORD",
                code: "login.loginpage",
                content: "<cms:contentText key="NO_LOGIN_ID_OR_PASSWORD" code="login.loginpage" />"
            },
            {
                key: "REGISTER_NEW_ACCOUNT",
                code: "login.loginpage",
                content: "<cms:contentText key="REGISTER_NEW_ACCOUNT" code="login.loginpage" />"
            },
            {
                key: "CARRIER_SUPPORT",
                code: "login.forgotpwd",
                content: "<cms:contentText key="CARRIER_SUPPORT" code="login.forgotpwd" escapeJavascript="true" />"
            },
            {
                key: "CARRIER_SUPPORT_INFO",
                code: "login.forgotpwd",
                content: "<cms:contentTemplateText key="CARRIER_SUPPORT_INFO" code="login.forgotpwd" args="${webappTitle}" escapeJavascript="true" />"
            },
            {
                key: "RECOVERY_TNC",
                code: "login.forgotpwd",
                content: "<cms:contentTemplateText key="RECOVERY_TNC" code="login.forgotpwd" args="${webappTitle}" escapeJavascript="true" />"
            },
            {
                key: "DIDNT_GET_IT",
                code: "login.forgotpwd",
                content: "<cms:contentText key="DIDNT_GET_IT" code="login.forgotpwd" escapeJavascript="true" />"
            },
            {
            	key: "EMAIL_REQ",
                code: "login.forgotpwd",
                content: "<cms:contentText key="EMAIL_REQ" code="login.forgotpwd" escapeJavascript="true" />"            	
            },

            {
            	key: "TERMED_USER_DID_NOT_GET",
            	code: "login.account.activation.messages",
            	content: "<cms:contentText key="TERMED_USER_DID_NOT_GET" code="login.account.activation.messages" escapeJavascript="true" />"            	
            },
            {
            	key: "TERMED_USER_NONE_OF_THESE_WORK",
            	code: "login.account.activation.messages",
            	content: "<cms:contentText key="TERMED_USER_NONE_OF_THESE_WORK" code="login.account.activation.messages" escapeJavascript="true" />"            	
            },
            {
            	key: "TERMED_USER_EMAIL_TOOL_TIP",
            	code: "login.account.activation.messages",
            	content: "<cms:contentText key="TERMED_USER_EMAIL_TOOL_TIP" code="login.account.activation.messages" escapeJavascript="true" />"            	
            },            
            {
            	key: "SUSPICIOUS_ACTIVITY",
            	code: "login.loginpage",
            	content: "<cms:contentText key="SUSPICIOUS_ACTIVITY" code="login.loginpage" escapeJavascript="true" />"
            },
            {
            	key: "LOCK_SAFEGUARD",
            	code: "login.loginpage",
            	content: "<cms:contentText key="LOCK_SAFEGUARD" code="login.loginpage" escapeJavascript="true"/>"
            },
            {
            	key: "USER_ACCOUNT_LOCK_INFO",
            	code: "login.loginpage",
            	content: "<cms:contentText key="USER_ACCOUNT_LOCK_INFO" code="login.loginpage" escapeJavascript="true" />"
            },
            {
            	key: "LOCK_ACCOUNT",
            	code: "login.loginpage",
            	content: "<cms:contentText key="LOCK_ACCOUNT" code="login.loginpage" escapeJavascript="true"/>"
            },
            {
            	key: "LEAVE",
            	code: "login.loginpage",
            	content: "<cms:contentText key="LEAVE" code="login.loginpage" escapeJavascript="true"/>"
            },
            {
            	key: "ACCOUNT_LOCKED",
            	code: "login.loginpage",
            	content: "<cms:contentText key="ACCOUNT_LOCKED" code="login.loginpage" escapeJavascript="true"/>"
            },
            {
            	key: "ACCOUNT_LOCK_CHOSEN",
            	code: "login.loginpage",
            	content: "<cms:contentText key="ACCOUNT_LOCK_CHOSEN" code="login.loginpage" escapeJavascript="true" />"
            },
            {
            	key: "ACCOUNT_LOCKED_ALREADY",
            	code: "login.loginpage",
            	content: "<cms:contentText key="ACCOUNT_LOCKED_ALREADY" code="login.loginpage" escapeJavascript="true"/>"
            },
             {
            	key: "CONTACT_ADMINSTRATOR",
            	code: "login.loginpage",
            	content: "<cms:contentText key="CONTACT_ADMINSTRATOR" code="login.loginpage" escapeJavascript="true"/>"
            }, 
            {
            	key: "AFTER_ACCOUNT_UNLOCK",
            	code: "login.loginpage",
            	content: "<cms:contentText key="AFTER_ACCOUNT_UNLOCK" code="login.loginpage" escapeJavascript="true" />"
            },
            {
            	key: "SET_SECURE_PWD",
            	code: "login.loginpage",
            	content: "<cms:contentText key="SET_SECURE_PWD" code="login.loginpage" escapeJavascript="true" />"
            },
            {
            	key: "SECURITY_SETTINGS",
            	code: "login.loginpage",
            	content: "<cms:contentText key="SECURITY_SETTINGS" code="login.loginpage" escapeJavascript="true" />"
            },
            {
            	key: "SIGNS_SUSPICIOUS_ACTIVITY",
            	code: "login.loginpage",
            	content: "<cms:contentText key="SIGNS_SUSPICIOUS_ACTIVITY" code="login.loginpage" escapeJavascript="true" />"
            },
            {
            	key: "THANK_YOU_VIGILANT",
            	code: "login.loginpage",
            	content: "<cms:contentText key="THANK_YOU_VIGILANT" code="login.loginpage" escapeJavascript="true" />"
            },
            {
             key: "ACCOUNT_LOCK_PASSWORD",
             code: "login.loginpage",
             content: "<cms:contentText key="ACCOUNT_LOCK_PASSWORD" code="login.loginpage" escapeJavascript="true" />"
            }



        ],
        locales: [
            <c:forEach var="item" items="${languages}">
                {
                    code: "<c:out value="${item.code}" />",
                    label: "<c:out value="${item.name}" />"
                },
            </c:forEach>
        ],
        selfEnrollment: ${beacon:systemVarBoolean('self.enrollment.enabled')},
        contactUsEmailConfirmation: ${contactUsEmailConfirmation == true},
        strutsToken: "<%=session.getAttribute("org.apache.struts.action.TOKEN") %>",
        showTerms: ${displayTermsAndConditionsFooterLink == true},
        loginUrl: 'j_acegi_security_check.do',
        accountLockCheck: '/account/lockAccountConfirm.action',
        forgotIdUrl:'prelogin/userByContactInformation.action',
        methodIdUrl:'prelogin/sendForgotUserIdNotification.action',
        forgotPassUrl:'prelogin/resetPasswordByEmail.action',
        methodPassUrl: 'prelogin/userToken.action',
        activInitUrl: 'activate/v1/(userId)/activated.action',
        activAttrUrl: 'activate/v1/(userId)/validateActivation.action',
        activEmlPhoneUrl: 'activate/v1/(userId)/contactsActivation.action',
        activCodeVerifUrl: 'prelogin/resetPasswordByToken.action',
        resetPwUrl: 'prelogin/resetPassword.action',
        countryCodeUrl: 'activate/v1/countryPhones.action',
        activContMethUrl: 'activate/v1/activationLink.action',
        recoveryMethodUrl: 'activate/v1/registerRecoveryMethods.action',
        pwRulesUrl: 'prelogin/passwordValidationRules.action',
        autoCompleteUrl: 'prelogin/contactAutocomplete.action',
        selfEnrollUrl: 'selfenrollment/validateRegistrationCode.action',
        redirectLock: 'logout.do',
        skin: '${designTheme}',
        termedUserId: null,
        termedUserToolTip: false

    }
</script>
<%-- After errors/messages have been read, removed them from the session--%>
<% session.removeAttribute( Globals.ERROR_KEY ); %>
<% session.removeAttribute( Globals.MESSAGE_KEY ); %>