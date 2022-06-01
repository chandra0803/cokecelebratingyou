<%@page import="com.biperf.core.utils.UserManager"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@page import="botdetect.web.Captcha"%>
<%@taglib prefix="botDetect" uri="botDetect"%>
<%@ page import="java.time.LocalDateTime"%>
<%@ page import="java.time.format.DateTimeFormatter"%>
<%@ page import="com.biperf.core.utils.DateFormatterUtil" %>
<% pageContext.setAttribute("year",LocalDateTime.now().getYear()); %>
<%
  DateTimeFormatter formatter = null;
  if ( request.getSession().getAttribute( "loggedInUserDate" ) != null )
  {
    formatter = DateTimeFormatter.ofPattern( request.getSession().getAttribute( "loggedInUserDate" ).toString() );
  }
  else
  {
    formatter = DateTimeFormatter.ofPattern( DateFormatterUtil.getDatePattern( UserManager.getLocale() ) );
  }
  pageContext.setAttribute( "date", LocalDateTime.now().toLocalDate().format( formatter ) );
%>

<style>
.LBD_CaptchaDiv a img {
	width:auto !important;
	height:auto !important;
}
</style>
<!-- ======== PURLCONTRIBUTE: PURL TERMS AND CONDITIONS ======== -->

<div id="purlTermsPageView" class="page-content purlTermsPageView">
    <%-- this entire page-topper could be set to show only when no language preference is known about the current user, or it could show all the time just because... --%>
    <c:if test="${!isUserLoggedIn}">
	    <div class="page-topper">
	        <div class="row-fluid">
	            <div class="span12">
	                <form class="changeLanguage form-inline form-labels-inline" method="POST" action="${pageContext.request.contextPath}/changeLanguage.do?method=updateLanguagePreference&amp;changeLanguageForwardUrl=${changeLanguageForwardUrl}">
	                    <div class="control-group">
	                        <label class="control-label"><cms:contentText key="CHOOSE_LANGUAGE" code="purl.terms.and.conditions"/></label>
	                        <div class="controls">
	                            <select id="changeLanguage" name="cmsLocaleCode" class="input-xlarge">
	                            <c:forEach items="${languageList}" var="lang">
	                                <c:if test="${cmsLocaleCode eq lang.code}">
	                                <option value="${lang.code}" selected>${lang.name}</option>
	                                </c:if>
	                                <c:if test="${cmsLocaleCode ne lang.code}">
	                                <option value="${lang.code}" >${lang.name}</option>
	                                </c:if>
	                            </c:forEach>
	                            </select>
	                        </div>
	                    </div>
	                </form><!-- /.changeLanguage.form-inline.form-labels-inline -->
	            </div><!-- /.span12 -->
	        </div><!-- /.row-fluid -->
	    </div><!-- /.page-topper -->
    </c:if>

    <div class="row-fluid">
        <div class="span12">

            <div id="purlTermsErrorBlock" class="alert alert-block alert-error" style="display:none;">
                <h4><cms:contentText key="FOLLOWING_ERRORS" code="system.generalerror" /></h4>
                <ul>
                <html:messages id="actionMessage" >
                    <c:set var="serverReturnedError" value="true"/>
                    <li>${actionMessage}</li>
                </html:messages>
                </ul>
            </div>

            <h2><cms:contentText key="TITLE" code="purl.terms.and.conditions"/></h2>

            <div class="well termsBlock">
                 <cms:contentTemplateText code="purl.terms.text" key="TERMS_AND_COND_TEXT" args="${clientName},${date},${year}" delimiter=","/>
            </div>

            <html:form action="purlTNC" method="submit">
                <html:hidden property="method" value="submit" />
                <html:hidden property="purlContributor.id" />
                <html:hidden property="userId" />
                <html:hidden property="purlRecipientId" />
                <fieldset class="purlContributeTermsRadios">
                    <div class="validateme" data-validate-flags="nonempty" data-validate-fail-msgs="{&quot;nonempty&quot; : &quot;<cms:contentText key="ACCEPT_TNC_VALIDATION" code="purl.terms.and.conditions"/>&quot;}">
                        <label class="radio bold">
                            <input type="radio" name="acceptTNC" id="TermsAndConditionsRadio1" value="true">
                            <cms:contentText key="ACCEPT" code="purl.terms.and.conditions"/>
                        </label>
                        <label class="radio">
                        <input type="radio" name="acceptTNC" id="TermsAndConditionsRadio2" value="false">
                            <cms:contentText key="DECLINE" code="purl.terms.and.conditions"/>
                        </label>
                    </div>


                </fieldset>

            <%-- Do not show CAPTCHA for users already logged into the system --%>
            <c:if test="${!isUserLoggedIn}">
                <p><cms:contentText key="ENTER_CAPTCHA" code="purl.terms.and.conditions"/></p>
                  <botDetect:captcha
							                id="loginFormCaptcha"
							                codeLength="4"
							                codeStyle="alpha"
							              />
                <div id="purlTermsCaptcha" class="purlTermsCaptcha">
                    <div class="control-group validateme" data-validate-flags="nonempty" data-validate-fail-msgs="{&quot;nonempty&quot; : &quot;<cms:contentText key="CAPTCHA_VALIDATION" code="purl.terms.and.conditions"/>&quot;}">
                        <div class="controls">
                            <input type="text" name="captchaResponse">
                        </div>
                    </div>
                </div>
            </c:if>
			<c:if test="<%= !UserManager.getUser().isLaunched() %>">
                <div class="form-actions">
                    <button type="submit" name="submitTerms" value="Continue" class="btn btn-primary"><cms:contentText key="SUBMIT" code="system.button"/></button>
                </div>
            </c:if>
            </html:form>

        </div><!-- /.span12 -->
    </div><!-- /.row-fluid -->

</div>

<input type="hidden" id="serverReturnedErrored" value="${serverReturnedError}">

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
    var pcptac;

    $(document).ready(function(){
        <c:if test="${!isUserLoggedIn}">
        G5.props.globalHeader = {
                clientLogo : '',
                programLogo : ''
        }
        </c:if>

        //attach the view to an existing DOM element
        pcptac = new PurlContributeTermsPageView({
        el:$('#purlTermsPageView'),
        pageNav : {
            <c:if test="${isUserLoggedIn}">
                back : {
                    text : '<cms:contentText key="BACK" code="system.button"/>',
                    url : 'javascript:history.go(-1);'
                },
                home : {
                    text : '<cms:contentText key="HOME" code="system.general" />',
                    url : '<%= RequestUtils.getBaseURI(request)%>/homePage.do'
                }
            </c:if>
        },
        pageTitle : '<cms:contentText key="RECOGNITION_PURL_TITLE" code="purl.common"/>',
        loggedIn : <%=UserManager.isUserLoggedIn()%>
    }); //using the default page view for the T&C
});
</script>
