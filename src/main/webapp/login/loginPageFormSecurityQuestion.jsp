<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.ui.login.ForgotPasswordForm"%>
<form id="loginPageFormSecurityQuestion" action="<%=RequestUtils.getBaseURI( request )%>/getChangePassword.do?method=getChangePassword" method="POST" class="form-horizontal">
<div class="modal-body">
    <input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="f56aa4802dd6e974e4e0af1b8760bda4">
    <input type="hidden" name="method" value="forgotPwdAnswerValidate">

    <fieldset id="securityQuestionFieldset">
        <div class="control-group">
            <label class="control-label"><cms:contentText
						key="USERNAME_FLD" code="login.forgotpwd" /></label>
            <div class="controls">
                <span class="input naked-input"><strong><c:out
								value="${sessionScope.s_forgot_pwd_user.userName}" /></strong></span>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label"><cms:contentText
						key="QUESTION_FLD" code="login.forgotpwd" /></label>
            <div class="controls">
                <span class="input naked-input input-auto-width"><strong><c:out
								value="${sessionScope.s_forgot_pwd_user.secretQuestionType.name}" /></strong></span>
            </div>
        </div>

        <div class="control-group validateme" data-validate-flags="nonempty" data-validate-fail-msgs='{"nonempty" : "<cms:contentText
				code="login.errors" key="SECRET_ANS_REQ" />"}'>
            <label class="control-label" for="secretAnswer"><cms:contentText
						key="ANSWER_FLD" code="login.forgotpwd" /></label>
            <div class="controls">
                <input type="text" name="secretAnswer" id="secretAnswer">
            </div>
        </div>
    </fieldset>

</div>
<div class="modal-footer">
        <button type="submit" id="securityQuestionFormSubmit" name="button" value="securityQuestionFormSubmit" formaction="<%=RequestUtils.getBaseURI( request )%>/forgotPwdAnswerValidate.do?method=forgotPwdAnswerValidate" class="btn btn-primary form-action-btn"><cms:contentText code="system.button" key="SUBMIT" /></button>
        <a href="<%=RequestUtils.getBaseURI( request )%>/login.do" id="securityQuestionFormCancel" class="btn"><cms:contentText
				key="CANCEL" code="system.button" /></a>
</div>

</form>