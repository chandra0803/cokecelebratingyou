<%@page import="com.biperf.core.utils.UserManager"%>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.ui.help.ContactUsForm"%>
<div id="contactFormView" class="contactForm-liner page-content">
    <h2><cms:contentText code="login.help.pages" key="CONTACT_US" /></h2>
    <div class="row-fluid">
        <div class="span12">
        		<html:form styleId="loginPageFormContactUs" action="contactUsSave" method="POST">
        			<c:if test="${isFullPage}">
        				<html:hidden property="isFullPage" value="true"/>
        			</c:if>
			    	<fieldset id="contactUsFieldset">
			        	<div class="control-group validateme" data-validate-flags="nonempty" data-validate-fail-msgs='{"nonempty" : "<cms:contentText code="help.contact.us" key="FIRSTNAME_REQ" />"}'>
			            	<label class="control-label" for="firstName"><cms:contentText code="help.contact.us" key="FIRSTNAME" /></label>
			            		<div class="controls">
			                		<html:text name="contactUsForm" property="firstName" styleId="firstName" />
			            		</div>
			        	</div>
			        	<div class="control-group validateme" data-validate-flags="nonempty" data-validate-fail-msgs='{"nonempty" : "<cms:contentText code="help.contact.us" key="LASTNAME_REQ" />"}'>
			            	<label class="control-label" for="lastName"><cms:contentText code="help.contact.us" key="LASTNAME" /></label>
			            		<div class="controls">
			                		<html:text name="contactUsForm" property="lastName" styleId="lastName" />
			            		</div>
			        	</div>
			        <div class="control-group validateme" data-validate-flags="nonempty,email" data-validate-fail-msgs='{&quot;nonempty&quot; : &quot;<cms:contentText code="help.contact.us" key="EMAIL_REQ" />&quot;, &quot;email&quot; : &quot;<cms:contentText code="help.contact.us" key="EMAIL_INVALID" />&quot;}'>
			            <label class="control-label" for="emailAddress"><cms:contentText code="help.contact.us" key="EMAIL" /></label>
			            	<div class="controls">
			                	<html:text name="contactUsForm" styleId="emailAddress" property="emailAddress" />
			            	</div>
			        </div>

			        <div class="control-group validateme" data-validate-flags="nonempty,maxlength" data-validate-max-length="100" data-validate-fail-msgs='{&quot;nonempty&quot; : &quot;<cms:contentText code="help.contact.us" key="SUBJECT_REQ" />&quot;, &quot;maxlength&quot; : &quot;<cms:contentText code="help.contact.us" key="SUBJECT_MAX_LENGTH"/>&quot;}'>
			            	<label class="control-label" for="subject"><cms:contentText code="help.contact.us" key="SUBJECT" /></label>
			            		<div class="controls">
			                		<input type="text" maxlength="100" data-max-chars="100" id="subject" name="subject">
			            		</div>
			        </div>
			        <div class="control-group validateme" data-validate-flags="nonempty" data-validate-fail-msgs='{&quot;nonempty&quot; : &quot;<cms:contentText code="help.contact.us" key="COUNTRY_REQ" />&quot;}'>
			            <label class="control-label" for="countryId"><cms:contentText code="help.contact.us" key="COUNTRY" /></label>
			            <div class="controls">
			                <html:select property="countryId" styleId="countryId">
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
			    <fieldset class="form-actions">
			    <beacon:authorize ifNotGranted="LOGIN_AS">
			        <button type="submit" id="contactUsFormSubmit" name="button" value="contactUsFormSubmit" formaction="<%=RequestUtils.getBaseURI( request )%>/contactUsSave.do?method=saveContactUs" class="btn btn-primary form-action-btn"><cms:contentText code="system.button" key="SUBMIT" /></button>
				</beacon:authorize>				        
			    </fieldset>
			</html:form>
        </div>
    </div>
</div>

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->

<c:choose>
	<c:when test="${userLoggedIn}">
		<script>
			$(document).ready(function()
				{
					var cfv = new ContactFormView(
						{
							el : $('#contactFormView'),
				            noSidebar : ${isDelegate},
				            noGlobalNav : ${isDelegate},
							pageNav : {
							back : {
									text : '<cms:contentText key="BACK" code="system.button" />',
									url : '${pageContext.request.contextPath}/homePage.do'
									},
							home : {
									text : '<cms:contentText key="HOME" code="system.general" />',
									url : '${pageContext.request.contextPath}/homePage.do'
									}
									},
							pageTitle : '<cms:contentText key="CONTACT_FORM" code="help.contact.us" />',
							loggedIn : true
						});
				});
		</script>
	</c:when>
	<c:otherwise>
		<script>
			$(document).ready(function()
				{
					var cfv = new ContactFormView(
						{
							el : $('#contactFormView'),
							pageNav : {},
							pageTitle : '<cms:contentText key="CONTACT_FORM" code="help.contact.us" />',
							loggedIn : false
						});
				});
		</script>
	</c:otherwise>
</c:choose>
