<%@ include file="/include/taglib.jspf" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

<div id="firstTimeLoginPage" class="firstTimeLoginPage page-content">

	<div class="row-fluid">
		<div class="span12">

            <h2><cms:contentText key="FEW_PREFERENCES" code="profile.personal.info" /></h2>
            <p class="lead"><cms:contentText key="FUN_STARTS" code="profile.personal.info" /></p>

			<c:if test="${actionMessages != null && fn:length(actionMessages) gt 0}">
				<div id="serverReturnedErrored" class="hide  errors span12">
					<c:forEach var="field" items="${actionMessages}">
						<div data-error-field="${field.name}" class="alert alert-block alert-error"><c:out value="${field.text}"/></div>
					</c:forEach>
				</div>
			</c:if>

			<html:form styleId="firstTimeLoginPageForm" action="selfEnrollmentPaxPreferencesSave.do?method=savePreferences" method="POST" styleClass="form-horizontal" enctype="multipart/form-data">
                <html:hidden property="countryAllowSms"/>
                <fieldset id="personalInfo" class="firstTimeLoginFieldSet form-horizontal" style="display:none">
                    <legend><cms:contentText key="ABOUT_ME" code="profile.personal.info" /></legend>
                    <div class="control-group">
                        <label class="control-label"><cms:contentText key="PICTURE_ALT" code="profile.personal.info" />
                            <span class="optional"><cms:contentText key="OPTIONAL" code="system.common.labels" /></span>
                        </label>
                        <div class="controls">
                            <img src="${selfEnrollmentPaxRegistrationForm.avatarUrl}" width="60px" height="60px" class="avatar" />
                                <input type="file" name="profileImage" id="profilePicUpload" />
                                <p class="muted"><cms:contentTemplateText key="UPLOAD_INSTRUCTIONS" code="profile.personal.info" args="${beacon:systemVarInteger('system.image.upload.size.limit')}" /></p>
                            <p class="alert alert-info hide no-fileinput"><cms:contentText key="DEVICE_NOT_SUPPORTED" code="profile.personal.info" /></p>
                        </div>
                    </div>

                    <div id="aboutMeSection">
                        <nested:iterate id="aboutMeItem" name="selfEnrollmentPaxRegistrationForm" property="aboutMeQuestions">
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
                
                <!-- bug fix 56289 in bugzilla -->
                <c:if test="${selfEnrollmentPaxRegistrationForm.countryAllowSms}">
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
                        <label class="control-label" for="telephoneCountryCode"><cms:contentText key="COUNTRY_AND_CODE" code="self.enrollment.pax.registration"/></label>
                        <div class="controls">
                            <html:select styleId="telephoneCountryCode" property="telephoneCountryCode" name="selfEnrollmentPaxRegistrationForm">
								<html:option value=""><cms:contentText key="SELECT_ONE" code="system.general"/></html:option>
								<html:options collection="countryList" property="countryCode" labelProperty="countryNameandPhoneCodeDisplay" />
							</html:select>
                        </div>
                    </div>
                    <div class="control-group" data-validate-flags="nonempty" data-validate-fail-msgs="{&quot;nonempty&quot; : &quot;<cms:contentText key="PHONE_REQUIRED" code="self.enrollment.pax.registration"/>&quot;}">
                        <label class="control-label" for="phoneNumber"><cms:contentText key="TELEPHONE_NUMBER" code="self.enrollment.pax.registration"/></label>
                        <div class="controls">
                            <html:text property="smsPhoneNumber" name="selfEnrollmentPaxRegistrationForm" styleId="phoneNumber" />
                        </div>
                    </div>

                    <div class="control-group" data-validate-flags="nonempty" data-validate-fail-msgs="{&quot;nonempty&quot; : &quot;<cms:contentText code="profile.personal.info" key="ACCEPT_TERMS_TXT_MSG" />&quot;}">
                        <div class="controls">
                            <label class="checkbox">
                                <html:checkbox styleId="txtAlertTerms" property="txtAlertTerms" name="selfEnrollmentPaxRegistrationForm" value="accept"/>
                                <cms:contentText key="ACCEPT_TERMS_TXT_MSG" code="profile.preference.tab" />
                            </label>

                            <div class="txtTerms">
                                <label><cms:contentText key="TERMS_AND_CONDITION" code="profile.preference.tab" /></label>
                                <p><c:out value="${termsAndCondition}" /></p>
                            </div>
                        </div>
                    </div>
                </fieldset>
				</c:if>

                <div class="form-actions pullBottomUp" style="display:none">
                    <button class="btn btn-primary" type="submit" id="submitBtn"><cms:contentText code="system.button" key="SUBMIT" /></button>
                </div>

			</html:form>
			<!-- prefix urls for uploaded images. stager for thumbnail, final for full image. -->
			<input type="hidden" id="finalPrefixURL" value="http://cocchiar-dv.biperf.com/core/apps/purlContribute/img/">
			<input type="hidden" id="stagerPrefixURL" value="http://cocchiar-dv.biperf.com/core/apps/purlContribute/img/">
			<input type="hidden" id="serverReturnedErrored" value="false">
			<input type="hidden" id="previousPhotoUrl" value="http://metaversemodsquad.files.wordpress.com/2011/06/hello-kitty-cat-clothing-011.jpg">

		</div><!-- /.span12 -->
	</div><!-- /.row-fluid -->

</div><!-- /#firstTimeLoginPage -->

<script>
	$(document).ready(function(){
		//attach the view to an existing DOM element
			G5.props.URL_JSON_FIRST_TIME_LOGIN_PHOTO_UPLOAD = G5.props.URL_ROOT+'selfEnrollmentPaxPreferencesSave.do?method=uploadAvatar';
			G5.props.URL_JSON_SELFENROLLMENT_GENERATE_USERID = G5.props.URL_ROOT+'enrollmentOpenJsonAction.do?method=generateUserName';
			G5.props.URL_JSON_SELFENROLLMENT_LOCATIONS = G5.props.URL_ROOT+'enrollmentOpenJsonAction.do?method=fetchStatesByCountry';
		    G5.props.URL_JSON_SELFENROLLMENT_CHECK_USERID = G5.props.URL_ROOT + "enrollmentOpenJsonAction.do?method=validateUserName";
		    
        G5.props.globalHeader = { 
                clientLogo : '',
                programLogo : ''
        }
		window.lpftv = new LoginPageFirstTimeView({
			el:$('#firstTimeLoginPage'),
		    pageNav : {},
			pageTitle : '',
			loggedIn : false
		});
	});
</script>
