<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.ui.help.ContactUsForm"%>
<%@ page import="com.objectpartners.cms.util.CmsResourceBundle" %>

<html:form styleId="loginPageFormContactUs" action="contactUsSave" method="POST" styleClass="form-horizontal">
<div class="modal-body">
	<html:hidden property="method" value="" />
	<fieldset id="contactUsFieldset">
		<div class="control-group">
			<label class="control-label" for="firstName"><cms:contentText code="help.contact.us" key="FIRSTNAME" /> </label>
			<div class="controls">
				<html:text name="contactUsForm" property="firstName" styleId="firstName" />
			</div>
		</div>

		<div class="control-group">
			<label class="control-label" for="lastName"><cms:contentText code="help.contact.us" key="LASTNAME" /> </label>
			<div class="controls">
				<html:text name="contactUsForm" property="lastName" styleId="lastName" />
			</div>
		</div>

		<div class="control-group validateme" data-validate-flags="nonempty,email" data-validate-fail-msgs='{&quot;nonempty&quot; : &quot;<cms:contentText code="help.contact.us" key="EMAIL_REQ" escapeHtml="true" />&quot;, &quot;email&quot; : &quot;<cms:contentText code="help.contact.us" key="EMAIL_INVALID" escapeHtml="true" />&quot;}'>
			<label class="control-label" for="emailAddress"><cms:contentText code="help.contact.us" key="EMAIL" /></label>
			<div class="controls">
				<html:text name="contactUsForm" styleId="emailAddress" property="emailAddress" />
			</div>
		</div>
 {{#ueq this "loginPageFormContact"}}
		<div class="control-group validateme" data-validate-flags="nonempty,maxlength" data-validate-max-length="100"
			data-validate-fail-msgs='{&quot;nonempty&quot; : &quot;<cms:contentText code="help.contact.us" key="SUBJECT_REQ" escapeHtml="true" />&quot;, &quot;maxlength&quot; : &quot;<cms:contentText code="help.contact.us" key="SUBJECT_MAX_LENGTH" escapeHtml="true" />&quot;}'
			data-inputvalue-forgotid="<cms:contentText code="login.help.pages" key="FORGOT_LOGIN_ID" />">
			<label class="control-label" for="subject"><cms:contentText code="help.contact.us" key="SUBJECT" /></label>
			<div class="controls">
				<input type="text" maxlength="100" data-max-chars="100" id="subject" name="subject">
			</div>
		</div>
		<div class="control-group validateme" data-validate-flags="nonempty" data-validate-fail-msgs='{&quot;nonempty&quot; : &quot;<cms:contentText code="help.contact.us" key="COUNTRY_REQ" />&quot;}'>
			<label class="control-label" for="countryId"><cms:contentText code="help.contact.us" key="COUNTRY" /></label>
			<div class="controls">
				<html:select property="countryId" styleClass="content-field" styleId="countryId">
					<html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general" /></html:option>
					<html:options collection="countryList" property="id" labelProperty="i18nCountryName" />
				</html:select>
			</div>
		</div>

		<div class="control-group validateme"  data-validate-flags="nonempty,maxlength" data-validate-fail-msgs='{&quot;nonempty&quot; : &quot;<cms:contentText code="help.contact.us" key="COMMENT_REQ" />&quot;, &quot;maxlength&quot; : &quot;<cms:contentText code="help.contact.us" key="COMMENT_MAX_LENGTH"/>&quot;}' data-validate-max-length="2000">
			<label class="control-label" for="firstName"><cms:contentText code="help.contact.us" key="COMMENT" /></label>
			<div class="controls">
				<textarea name="comments" data-max-chars="2000" cols="35" rows="6"></textarea>
			</div>
		</div>
	</fieldset>
 {{/ueq}}
</div>
<div class="modal-footer">
		<button type="submit" id="contactUsFormSubmit" name="button"
			value="contactUsFormSubmit"
			formaction="<%=RequestUtils.getBaseURI( request )%>/contactUsSave.do?method=saveContactUs"
			class="btn btn-primary form-action-btn">
			<cms:contentText code="system.button" key="SUBMIT" />
		</button>
		<a href="<%=RequestUtils.getBaseURI( request )%>/login.do"
			id="contactUsFormCancel" class="btn cancel-btn"><cms:contentText
				key="CANCEL" code="system.button" /> </a>
</div>
</html:form>