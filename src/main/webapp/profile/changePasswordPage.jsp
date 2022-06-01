<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.ui.profile.ChangePasswordForm"%>

<div id="changePasswordPageView"
	class="changePasswordPageView page-content">

	<div class="row-fluid">
		<div class="span12">
			<html:form action="saveNewPassword.do?method=saveForceChangePassword"
				styleId="changePassForm">

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

				<fieldset id="securitySettings" class="firstTimeLoginFieldSet">
					<legend>
						<cms:contentText key="CHANGE_SETTINGS" code="profile.security.tab" />
					</legend>
					<p><c:out value="${passwordRequirements}"/></p>
					<div class="passwordReset wrapper">
						<div class="control-group validateme" data-validate-flags="nonempty" data-validate-fail-msgs='{"nonempty" : "<cms:contentText key="NEW_PWD_REQ" code="login.forgotpwd" />"}'>
							<label class="control-label" for="profilePageSecurityTabConfirmNewPassword"><cms:contentText key="PASSWORD_LABEL" code="login.forgotpwd" /> </label>
							<span class="controls input-append input-append-inside passShow">
								<html:password  styleId="profilePageSecurityTabConfirmNewPassword" property="newPassword"
								styleClass="span4" />
								<a class="add-on btn btn-link show-pw icon-eye-off"></a>
							</span>
						</div>
					</div>

					<button id="submitBtn" class="btn btn-primary nextSectionBtn">
						<cms:contentText code="system.button" key="SUBMIT" />
					</button>
				</fieldset>

				<input type="hidden" id="serverReturnedErrored"
					name="serverReturnedErrored" value="${serverReturnedErrored}" />
			</html:form>

		</div>
	</div>
</div>

<script>

	$(document).ready(function() {
		var cppv = new ChangePasswordPageView({
			el : $('#changePasswordPageView'),
			pageNav : {},
			pageTitle : '<cms:contentText code="login.forgotpwd" key="CHANGE_PWD_TITLE" />',
			loggedIn : false
		});
	});

</script>
