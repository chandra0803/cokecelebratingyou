<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.ui.login.ForgotPasswordForm"%>
<%@ page import="com.biperf.core.ui.profile.ChangePasswordForm" %>
<html:form styleId="loginPageFormChangePass" action="getChangePassword.do?method=savePassword" method="POST" styleClass="form-horizontal">
 <div class="modal-body">
    <input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="2cc2a145d27ff674e68987c115ff6d0a">
    <input type="hidden" name="method" value="savePassword">

    <fieldset id="securityQuestionFieldset">
         <div class="control-group validateme" data-validate-flags="nonempty" data-validate-fail-msgs='{"nonempty" : "<cms:contentText  key="PWD_REQ" code="login.forgotpwd" />"}'>
            <label class="control-label"><cms:contentText
					key="PASSWORD_LABEL" code="login.forgotpwd" /></label>
            <div class="controls input-append input-append-inside">
                <input type="password" name="newPassword" id="newPassword" value="" autocomplete="<c:choose><c:when test="${allowPasswordFieldAutoComplete}">on</c:when><c:otherwise>off</c:otherwise></c:choose>">
                <a class="add-on btn btn-link icon-eye show-pw"></a>
            </div>
        </div>

        <div class="control-group validateme" data-validate-flags="nonempty,match" data-validate-match="newPassword" data-validate-fail-msgs='{"nonempty" : "<cms:contentText  key="CONF_PWD_REQ" code="login.forgotpwd" />","match" : "<cms:contentText  key="DOES_NOT_MATCH" code="login.forgotpwd" />"}'>
            <label class="control-label"><cms:contentText
					key="CONFIRM_PASSWORD_LABEL" code="login.forgotpwd" /></label>
            <div class="controls input-append input-append-inside">
                <input type="password" name="confirmNewPassword" id="confirmNewPassword" value="" autocomplete="<c:choose><c:when test="${allowPasswordFieldAutoComplete}">on</c:when><c:otherwise>off</c:otherwise></c:choose>">
                <a class="add-on btn btn-link icon-eye show-pw"></a>
            </div>
        </div>
    </fieldset>
</div>
<div class="modal-footer">
        <button type="submit" id="securityQuestionFormSubmit" name="button" value="securityQuestionFormSubmit" formaction="<%=RequestUtils.getBaseURI( request )%>/getChangePassword.do?method=savePassword" class="btn btn-primary form-action-btn"><cms:contentText code="system.button" key="SUBMIT" /></button>
        <a href="<%=RequestUtils.getBaseURI( request )%>/login.do" id="securityQuestionFormCancel" class="btn"><cms:contentText
				key="CANCEL" code="system.button" /></a>
</div>

</html:form>