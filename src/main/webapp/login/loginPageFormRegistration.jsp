<%@ include file="/include/taglib.jspf"%>
<%@ page import="org.apache.struts.Globals"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.objectpartners.cms.util.CmsResourceBundle" %>

 <form id="regForm" action="selfEnrollmentPaxRegistration.do"  method="POST" class="form-horizontal">
<div class="modal-body">
    <fieldset id="regFieldset">
        <c:set var="regiCodeMessage" value="<%=CmsResourceBundle.getCmsBundle().getString( \"login.errors.REGI_CODE_REQ\" ) %>" />
        <div class="control-group validateme" data-validate-flags="nonempty" data-validate-fail-msgs='{"nonempty" : "<c:out value="${regiCodeMessage}" escapeXml="true"/>"}'>
            <label class="control-label" for="regcode"><cms:contentText key="REGI_CODE" code="login.registration" /></label>
            <div class="controls">
                <input type="text" id="regcode" name="registrationCode">
                <%--<span class="help-inline"><a href="loginPageHelp.do?helpwith=registration" ><cms:contentText key="HELP" code="login.loginpage" /></a></span>--%>
            </div>
        </div>
    </fieldset>
</div>
<div class="modal-footer">
        <button type="submit" id="regFormSubmit" name="button" value="regFormSubmit" formaction="<%=RequestUtils.getBaseURI(request)%>/selfEnrollmentPaxRegistration.do?method=validateRegistrationCode" class="btn btn-primary form-action-btn"><cms:contentText key="REGISTER" code="login.registration" /></button>
        <a id="contactUsFormCancel" href="<%=RequestUtils.getBaseURI( request )%>/login.do" class="btn cancel-btn" data-dismiss="modal"><cms:contentText key="CANCEL" code="system.button" /></a>
</div>
</form>