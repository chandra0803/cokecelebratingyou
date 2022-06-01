<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.objectpartners.cms.util.CmsResourceBundle" %>

<form id="loginPageFormForgotPass" action="<%=RequestUtils.getBaseURI( request )%>/getSecurityQuestion.do?method=getSecurityQuestion" method="POST" class="form-horizontal">
<div class="modal-body">
    <input type="hidden" name="org.apache.struts.taglib.html.TOKEN" value="57316dd9f62b17a7281782c571bc2d45">
    <input type="hidden" name="method" value="forgotPwdNameValidate">

    <fieldset id="forgotPasswordFieldset">
    	<c:set var="loginIdMessage" value='<%=CmsResourceBundle.getCmsBundle().getString( "login.errors.LOGIN_ID_REQ" ) %>' />
        <div class="control-group validateme" data-validate-flags="nonempty"
					data-validate-fail-msgs='{"nonempty" : "<c:out value="${loginIdMessage}" escapeXml="true"/>"}'>
            <label class="control-label"><cms:contentText
					key="USERNAME_FLD" code="login.forgotpwd" /></label>
            <div class="controls">
                <input type="text" id="userName" name="userName">
            </div>
        </div>
    </fieldset>

</div>
<div class="modal-footer">
        <button type="submit" id="forgotPasswordFormSubmit" name="button" value="forgotPasswordFormSubmit" formaction="<%=RequestUtils.getBaseURI( request )%>/forgotPwdNameValidate.do?method=forgotPwdNameValidate" class="btn btn-primary  form-action-btn">
        <cms:contentText code="system.button" key="SUBMIT" /></button>
        <a href="<%=RequestUtils.getBaseURI( request )%>/login.do" id="forgotPasswordFormCancel" class="btn cancel-btn"><cms:contentText
				key="CANCEL" code="system.button" /></a>
</div>
</form>