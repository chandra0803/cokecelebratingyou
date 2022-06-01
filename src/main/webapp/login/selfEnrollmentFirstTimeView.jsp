<%@ include file="/include/taglib.jspf" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@page import="botdetect.web.Captcha"%>
<%@taglib prefix="botDetect" uri="botDetect"%>

<style>
.LBD_CaptchaDiv a img {
	width:auto !important;
	height:auto !important;
}
</style>
<div id="firstTimeLoginPage" class="firstTimeLoginPage page-content">
	<logic:present name="org.apache.struts.action.ERROR">
	<div class="row-fluid">
		<div class="span12">
			<div style="" class="alert alert-block alert-error" id="serverErrorsContainer">
				<button data-dismiss="alert" class="close" type="button"><i class="icon-close"></i></button>
					<div class="error">
						<h4><cms:contentText key="FOLLOWING_ERRORS" code="system.generalerror" /></h4>
							<ul>
								<html:messages id="error" >
									<li><c:out value="${error}"/></li>
								</html:messages>
							</ul>
					</div>
			</div>
		</div>
	</div>
	</logic:present>
	<div class="row-fluid">
		<div class="span12">

            <h2><cms:contentText key="ENROLLED" code="profile.preference.tab" /></h2>
            <p class="lead"><cms:contentText key="FUN_START" code="profile.preference.tab" /></p>

			<html:form styleId="selfEnrollmentFirstTimeLoginPageForm" action="selfEnrollmentPaxRegistrationSave.do?method=save" method="POST">
				<html:hidden property="nodeName2"/>

				<html:hidden property="userCharacteristicValueListCount"/>
				<c:if test="${selfEnrollmentPaxRegistrationForm.showTermsAndConditions}">
				<fieldset id="termsAndConditions" class="firstTimeLoginFieldSet form">
                    <legend><cms:contentText key="TERMS_AND_CONDITION" code="profile.preference.tab" /></legend>
                    <cms:contentText key="TERMS_AND_COND_TEXT" code="profile.terms.text" />
					<div class="control-group validateme" data-validate-flags="match" data-validate-match="TermsAndConditionsRadio1" data-validate-fail-msgs="{&quot;nonempty&quot; : &quot;<cms:contentText key="TERMS_COND_TO_CONT" code="profile.personal.info" />&quot;, &quot;match&quot; : &quot;<cms:contentText key="TERMS_COND_TO_CONT" code="profile.personal.info" />&quot;}">
                      <label class="control-label radio bold">
                            <input type="radio" name="TermsAndConditionsRadios" id="TermsAndConditionsRadio1" value="accept"> <cms:contentText code="profile.personal.info" key="ACCEPT_TERMS_COND" />
                      </label>
                      <label class="control-label radio bold"><cms:contentText code="profile.personal.info" key="DECLINE_TERMS" /> <input type="radio" name="TermsAndConditionsRadios" id="TermsAndConditionsRadio2" value="decline"></label>
					  <div class= "controls"></div>
                    </div>
					<button class="btn btn-primary nextandClearSectionBtn"><cms:contentText code="system.button" key="CONTINUE" /></button>
                    <a href="<%=RequestUtils.getBaseURI(request)%>/login.do" class='btn button'><cms:contentText key="CANCEL" code="system.button"/></a>
				</fieldset>
				</c:if>

				<fieldset id="userInformation" class="firstTimeLoginFieldSet" style="display:none">

					<div class="control-group">
						<label class="control-label">
							<cms:contentText key="LOCATION_ORG_UNIT" code="self.enrollment.pax.registration" />
						</label>
						<div class="controls">
							<span class="naked-input"><c:out value="${selfEnrollmentPaxRegistrationForm.nodeName}" /></span>
						</div>
					</div>

					<div class="control-group validateme" data-validate-flags="nonempty" data-validate-fail-msgs="{&quot;nonempty&quot; : &quot;<cms:contentText key="FIRST_NAME_REQUIRED" code="system.form.errors"/>&quot;}">
						<label class="control-label" for="firstName"><cms:contentText key="FIRST_NAME" code="self.enrollment.pax.registration"/></label>
	                    <div class="controls">
							<html:text property="firstName" name="selfEnrollmentPaxRegistrationForm" styleId="firstName" size="30" maxlength="40"/>
						</div>
					</div>

					<div class="control-group">
						<label class="control-label"><cms:contentText key="MIDDLE_NAME" code="self.enrollment.pax.registration"/></label>
	                    <div class="controls">
							<html:text property="middleName" name="selfEnrollmentPaxRegistrationForm" styleId="middleName" size="30" maxlength="40"/>
                            <span class="optional"><cms:contentText key="OPTIONAL" code="system.common.labels" /></span>
						</div>
					</div>

					<div class="control-group validateme" data-validate-flags="nonempty" data-validate-fail-msgs="{&quot;nonempty&quot; : &quot;<cms:contentText key="LAST_NAME_REQUIRED" code="system.form.errors"/>&quot;}">
						<label class="control-label" for="lastName"><cms:contentText key="LAST_NAME" code="self.enrollment.pax.registration"/></label>
	                    <div class="controls">
							<html:text property="lastName" name="selfEnrollmentPaxRegistrationForm" styleId="lastName" size="30" maxlength="40"/>
						</div>
					</div>

					<div class="control-group">
						<label class="control-label" for="ssn"><cms:contentText key="SSN" code="self.enrollment.pax.registration"/></label>
	                    <div class="controls">
							<html:password property="ssn" name="selfEnrollmentPaxRegistrationForm" styleId="ssn" size="11" maxlength="11"/>
							<span class="optional"><cms:contentText key="OPTIONAL" code="system.common.labels" /></span>
							<span class="optional"><cms:contentText key="SSN_FORMAT" code="system.common.labels" /></span>
						</div>
					</div>

					<div class="capcha control-group validateme" data-validate-flags="nonempty" data-validate-fail-msgs="{&quot;nonempty&quot; : &quot;<cms:contentText key="CAPTCHA_VALIDATION" code="purl.terms.and.conditions"/>&quot;}">
                        <div class="controls">
                            <p><cms:contentText key="ENTER_CAPTCHA" code="self.enrollment.pax.registration"/></p>
                               <botDetect:captcha
							                id="loginFormCaptcha"
							                codeLength="4"
							                codeStyle="alpha"
							              />

                        </div>
		                <label class="control-label checkId"><cms:contentText key="CAPTCHA" code="self.enrollment.pax.registration"/></label>
		                <div class="controls">
							<input type="text" name="captchaResponse" id="captchaResponse" placeholder="">
						</div>
					</div>

					<div class="control-group validateme" data-validate-flags="nonempty" data-validate-fail-msgs="{&quot;nonempty&quot; : &quot;<cms:contentText key="LOGIN_ID_REQUIRED" code="system.form.errors"/>&quot;}">
						<div class="form-inline">
                            <div class="controls">
                                <p class="help-block"><cms:contentText key="CREATE_LOGIN" code="self.enrollment.pax.registration"/></p>
                            </div>
							<label class="control-label"><cms:contentText key="LOGIN_ID" code="self.enrollment.pax.registration"/></label>
							<div class="controls">
								<div class="input-append">
								    <html:text property="userName" name="selfEnrollmentPaxRegistrationForm" styleId="userName" size="20" maxlength="20"/>
								    <button class="generateId btn" type="button"><cms:contentText key="GENERATE_ID" code="self.enrollment.pax.registration"/></button>
								</div>
							</div>
						</div>
					</div>

					<legend><cms:contentText key="SET_SECURITY_HEADER" code="self.enrollment.pax.registration"/></legend>
					<div class="control-group validateme" data-validate-flags="nonempty" data-validate-fail-msgs="{&quot;nonempty&quot; : &quot;<cms:contentText key="PASSWORD_LABEL_REQUIRED" code="system.form.errors"/>&quot;}">
						<div class="controls">
                        	<p class="help-block"><c:out value="${passwordRequirements}"/></p>
                    	</div>
						<label class="control-label" for="inputPassword" ><cms:contentText key="PASSWORD_LABEL" code="login.forgotpwd" /></label>
						<div class="controls input-append input-append-inside passShow">
							<html:password property="inputPassword" name="selfEnrollmentPaxRegistrationForm" styleId="inputPassword"/>
							<a class="add-on btn btn-link icon-eye-off show-pw"></a>
						</div>
					</div>

					<div class="control-group">
						<label class="control-label"><cms:contentText key="JOB_TITLE" code="self.enrollment.pax.registration"/></label>
	                    <div class="controls">
							<html:select property="jobTitle" styleId="jobTitle" name="selfEnrollmentPaxRegistrationForm">
								<html:option value=''><cms:contentText key="SELECT_ONE" code="system.general" /></html:option>
								<html:options collection="jobTitleTypeList" property="code" labelProperty="name" />
							</html:select>
                            <span class="optional"><cms:contentText key="OPTIONAL" code="system.common.labels" /></span>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label"><cms:contentText key="DEPARTMENT" code="self.enrollment.pax.registration"/></label>
	                    <div class="controls">
							<html:select property="department" styleId="department" name="selfEnrollmentPaxRegistrationForm">
								<html:option value=''><cms:contentText key="SELECT_ONE" code="system.general" /></html:option>
								<html:options collection="departmentTypeList" property="code" labelProperty="name" />
							</html:select>
                            <span class="optional"><cms:contentText key="OPTIONAL" code="system.common.labels" /></span>
						</div>
					</div>
					<div class="control-group validateme" data-validate-flags="nonempty" data-validate-fail-msgs="{&quot;nonempty&quot; : &quot;<cms:contentText key="PRIMARY_ADDRESS_REQUIRED" code="system.form.errors"/>&quot;}">
						<label class="control-label"><cms:contentText key="PRIMARY_ADDRESS_TYPE" code="self.enrollment.pax.registration"/></label>
	                    <div class="controls">
							<html:select styleId="addressType" property="addressType" name="selfEnrollmentPaxRegistrationForm">
								<html:option value=''><cms:contentText key="SELECT_ONE" code="system.general"/></html:option>
								<html:options collection="addressTypeList" property="code" labelProperty="name"  />
							</html:select>
						</div>
					</div>

                    <div class="control-group validateme" data-validate-flags="nonempty" data-validate-fail-msgs="{&quot;nonempty&quot; : &quot;<cms:contentText key="COUNTRY_REQUIRED" code="system.form.errors"/>&quot;}">
                        <label for="country" class="control-label"><cms:contentText key="COUNTRY" code="self.enrollment.pax.registration"/></label>
                        <div class="controls">
                            <html:select styleId="selectCountry" property="addressFormBean.countryCode" name="selfEnrollmentPaxRegistrationForm">
                                <html:option value=''><cms:contentText key="SELECT_ONE" code="system.general"/></html:option>
								<html:options collection="countryList" property="countryCode" labelProperty="i18nCountryName"  />
                            </html:select>
                        </div>
                    </div>

					<div class="control-group validateme" data-validate-flags="nonempty" data-validate-fail-msgs="{&quot;nonempty&quot; : &quot;<cms:contentText key="ADDRESS_1_REQUIRED" code="system.form.errors"/>&quot;}">
						<label class="control-label"><cms:contentText key="ADDR1" code="self.enrollment.pax.registration"/></label>
	                    <div class="controls">
							<html:text property="addressFormBean.addr1" name="selfEnrollmentPaxRegistrationForm" size="40" maxlength="100"/>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label"><cms:contentText key="ADDR2" code="self.enrollment.pax.registration"/></label>
	                    <div class="controls">
							<html:text property="addressFormBean.addr2" name="selfEnrollmentPaxRegistrationForm" size="40" maxlength="100"/>
                            <span class="optional"><cms:contentText key="OPTIONAL" code="system.common.labels" /></span>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label"><cms:contentText key="ADDR3" code="self.enrollment.pax.registration"/></label>
	                    <div class="controls">
							<html:text property="addressFormBean.addr3" name="selfEnrollmentPaxRegistrationForm" size="40" maxlength="100"/>
                            <span class="optional"><cms:contentText key="OPTIONAL" code="system.common.labels" /></span>
						</div>
					</div>
					<div class="control-group validateme" data-validate-flags="nonempty" data-validate-fail-msgs="{&quot;nonempty&quot; : &quot;<cms:contentText key="CITY_REQUIRED" code="system.form.errors"/>&quot;}">
						<label class="control-label"><cms:contentText key="CITY" code="self.enrollment.pax.registration"/></label>
	                    <div class="controls">
							<html:text property="addressFormBean.city" name="selfEnrollmentPaxRegistrationForm" size="40" maxlength="30"/>
						</div>
					</div>

                    <div class="control-group validateme" data-validate-flags="nonempty" data-validate-fail-msgs="{&quot;nonempty&quot; : &quot;<cms:contentText key="STATE_REQUIRED" code="system.form.errors"/>&quot;}">
                        <label for="stateTypeCode" class="control-label"><cms:contentText key="STATE" code="self.enrollment.pax.registration"/></label>
                        <div class="controls">
							<html:select styleId="selectState" property="addressFormBean.stateTypeCode" name="selfEnrollmentPaxRegistrationForm">
                               	<html:option value=''><cms:contentText key="SELECT_ONE" code="system.general"/></html:option>
								<html:options collection="stateList" property="code" labelProperty="name"  />
                           	</html:select>
                        </div>
                    </div>

					<div class="control-group validateme" data-validate-flags="nonempty" data-validate-fail-msgs="{&quot;nonempty&quot; : &quot;<cms:contentText key="ZIP_CODE_REQUIRED" code="system.form.errors"/>&quot;}">
						<label class="control-label"><cms:contentText key="ZIP_CODE" code="self.enrollment.pax.registration"/></label>
	                    <div class="controls">
							<html:text property="addressFormBean.postalCode" name="selfEnrollmentPaxRegistrationForm"/>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label"><cms:contentText key="PRIMARY_EMAIL_ADDRESS_TYPE" code="self.enrollment.pax.registration"/></label>
	                    <div class="controls">
							<html:select styleId="emailType" property="emailType" name="selfEnrollmentPaxRegistrationForm">
                               	<html:option value=""><cms:contentText key="SELECT_ONE" code="system.general"/></html:option>
								<html:options collection="emailTypeList" property="code" labelProperty="name"  />
                           	</html:select>
                            <span class="optional"><cms:contentText key="OPTIONAL" code="system.common.labels" /></span>
						</div>
					</div>
					<c:set var="fieldName"><cms:contentText key="EMAIL_ADDRESS" code="self.enrollment.pax.registration"/></c:set>
					<div class="control-group validateme" data-validate-flags="nonempty,email" data-validate-fail-msgs="{&quot;nonempty&quot; : &quot;<cms:contentText key="EMAIL_ADDRESS_REQUIRED" code="system.form.errors"/>&quot;, &quot;email&quot; : &quot;<cms:contentText key="EMAIL_ADDRESS_INVALID" code="system.form.errors"/>&quot;}">
						<label class="control-label"><cms:contentText key="EMAIL_ADDRESS" code="self.enrollment.pax.registration"/></label>
	                    <div class="controls">
							<html:text styleId="emailAddress" property="emailAddress" name="selfEnrollmentPaxRegistrationForm" size="30" maxlength="75"/>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label"><cms:contentText key="PRIMARY_TELEPHONE_TYPE" code="self.enrollment.pax.registration"/></label>
	                    <div class="controls">
							<html:select styleId="phoneType" property="phoneType" name="selfEnrollmentPaxRegistrationForm">
								<html:option value=''><cms:contentText key="SELECT_ONE" code="system.general"/></html:option>
								<html:options collection="phoneTypeList" property="code" labelProperty="name"  />
							</html:select>
                            <span class="optional"><cms:contentText key="OPTIONAL" code="system.common.labels" /></span>
						</div>
					</div>
					<div class="control-group validateme" data-validate-flags="nonempty" data-validate-fail-msgs='{"nonempty" : &quot;<cms:contentText key="COUNTRY_AND_CODE_REQUIRED" code="system.form.errors"/>&quot;}'>
						<label class="control-label"><cms:contentText key="COUNTRY_AND_CODE" code="self.enrollment.pax.registration"/></label>
	                    <div class="controls">
							<html:select styleId="countryPhoneCode" property="countryPhoneCode" name="selfEnrollmentPaxRegistrationForm">
								<html:option value=""><cms:contentText key="SELECT_ONE" code="system.general"/></html:option>
								<html:options collection="countryList" property="countryCode" labelProperty="countryNameandPhoneCodeDisplay" />
							</html:select>
						</div>
					</div>
					<c:set var="fieldName"><cms:contentText key="TELEPHONE_NUMBER" code="self.enrollment.pax.registration"/></c:set>
					<div class="control-group validateme" data-validate-regex="(^\d{3}-?\d{3}-?\d{4}$|^\d{9}$)" data-validate-flags="nonempty,regex" data-validate-fail-msgs="{&quot;regex&quot;:&quot;<cms:contentText key="TELEPHONE_NUMBER_REQUIRED" code="system.form.errors"/>&quot;, &quot;regex&quot;:&quot;<cms:contentText key="TELEPHONE_NUMBER_INVALID" code="system.form.errors"/>&quot;}">
						<label class="control-label"><cms:contentText key="TELEPHONE_NUMBER" code="self.enrollment.pax.registration"/></label>
	                    <div class="controls">
							<html:text styleId="phoneNumber" property="phoneNumber" name="selfEnrollmentPaxRegistrationForm" size="12" maxlength="12"/>
						</div>
					</div>
					<c:set var="fieldName"><cms:contentText key="TELEPHONE_EXTENSION" code="self.enrollment.pax.registration"/></c:set>
					<div class="control-group validateme" data-validate-regex="(^\d{4}$|^$)" data-validate-flags="regex" data-validate-fail-msgs="{&quot;regex&quot;:&quot;<cms:contentText key="TELEPHONE_EXTENSION_INVALID" code="system.form.errors"/>&quot;}">
						<label class="control-label" for="extention"><cms:contentText key="TELEPHONE_EXTENSION" code="self.enrollment.pax.registration"/></label>
	                    <div class="controls">
							<html:text property="phoneExtension" name="selfEnrollmentPaxRegistrationForm" styleId="phoneExtension" /><span class="help-inline">(<cms:contentText key="EXTENSION_DESC" code="participant.participant"/>)</span>
                            <span class="optional"><cms:contentText key="OPTIONAL" code="system.common.labels" /></span>
						</div>
					</div>

					<c:if test="${fn:length(selfEnrollmentPaxRegistrationForm.userCharacteristicValueList) gt 0}">
							<h5><cms:contentText key="CHARACTERISTIC_HEADER" code="self.enrollment.pax.registration" /></h5>
							<c:set var="characteristicType" scope="page" value="user" />
							<c:forEach items="${selfEnrollmentPaxRegistrationForm.userCharacteristicValueList}" var="valueInfo" varStatus="status">
								<%--In order for the same page to use characteristicEntry.jspf for two different characteristic
					        	types, we need the iterated value to be named both ValueInfo and userCharacteristicValueInfo, so set
						        the userCharacteristicValueInfo bean to the valueInfo bean. Is there a tag way to do this rather than
						        using a scriptlet
						    	--%>
								<%
									Object obj = pageContext.getAttribute( "valueInfo" );
								  pageContext.setAttribute( "userCharacteristicValueInfo", pageContext.getAttribute( "valueInfo" ) );
								%>
								<%@ include file="/characteristic/characterisiticEntrySelfEnrollment.jspf"%>
							</c:forEach>
					</c:if>

					<button class="btn btn-primary" type="submit" id="submitBtn"><cms:contentText code="system.button" key="CONTINUE" /></button>
					<a href="<%=RequestUtils.getBaseURI(request)%>/login.do" class='btn button'><cms:contentText key="CANCEL" code="system.button"/></a>
				</fieldset>
			</html:form>
			<!-- prefix urls for uploaded images. stager for thumbnail, final for full image. -->
			<input type="hidden" id="finalPrefixURL" value="http://cocchiar-dv.biperf.com/core/apps/purlContribute/img/">
			<input type="hidden" id="stagerPrefixURL" value="http://cocchiar-dv.biperf.com/core/apps/purlContribute/img/">
			<input type="hidden" id="serverReturnedErrored" value="false">
			<input type="hidden" id="previousPhotoUrl" value="http://metaversemodsquad.files.wordpress.com/2011/06/hello-kitty-cat-clothing-011.jpg">

            <div id="myPreferences" class="hidden"></div>
		</div><!-- /.span12 -->
	</div><!-- /.row-fluid -->
	<div id="declineModal" class="modal hide fade" role="dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><i class="icon-close"></i></button>
                <h3 class="modal-title"><cms:contentText key="DECLINE_HEADER" code="self.enrollment.pax.registration"/></h3>
            </div>
            <div class="modal-body">
                <cms:contentText key="DECLINE_INSTRUCTIONAL_COPY" code="self.enrollment.pax.registration"/>
            </div>
            <div class="form-actions " >
                <a href="<%=RequestUtils.getBaseURI(request)%>/login.do" class="btn btn-primary"><cms:contentText key="DECLINE_CONFIRM" code="self.enrollment.pax.registration"/></a>
                <button type="button" class="btn" data-dismiss="modal"><cms:contentText key="CANCEL" code="ssi_contest.pax.manager" /></button>
            </div>

        </div>
    </div><!-- /#loginPageDeclineModal -->
</div><!-- /#firstTimeLoginPage -->

<script>
	$(document).ready(function(){
		//attach the view to an existing DOM element
			G5.props.URL_JSON_SELFENROLLMENT_GENERATE_USERID = G5.props.URL_ROOT+'enrollmentOpenJsonAction.do?method=generateUserName';
			G5.props.URL_JSON_SELFENROLLMENT_LOCATIONS = G5.props.URL_ROOT+'enrollmentOpenJsonAction.do?method=fetchStatesByCountry';
		    G5.props.URL_JSON_SELFENROLLMENT_CHECK_USERID = G5.props.URL_ROOT + "enrollmentOpenJsonAction.do?method=validateUserName";

        G5.props.globalHeader = {
                clientLogo : '',
                programLogo : ''
        }
		window.loginPageFirstTimeView = new LoginPageFirstTimeView({
			el:$('#firstTimeLoginPage'),
		    pageNav : {},
			pageTitle : '',
			loggedIn : false
		});
	});
</script>
