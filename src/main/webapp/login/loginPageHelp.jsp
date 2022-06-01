<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.ui.help.ContactUsForm"%>
<!-- ======== LOGIN PAGE HELP ======== -->

<script type="text/javascript">
    $('body').addClass('loginHelpPage');
</script>

<!-- Page Body -->

<div class="page-content" id="loginHelpPage">

    <div class="page-topper">
        <div class="row-fluid">
            <div class="span12">
                <form class="form-inline form-labels-inline">

                    <div class="control-group">
                        <label class="control-label" for="whichHelp"><cms:contentText code="login.help.pages" key="HELP_WITH" /></label>

                        <div class="controls">
                            <select name="whichHelp" id="whichHelp">
                                <option value=""><cms:contentText key="SELECT_ONE" code="system.general"/></option>
                                <option value="forgotid"><cms:contentText code="login.help.pages" key="FORGOT_LOGIN_ID" /></option>
                                <option value="forgotpass"><cms:contentText code="login.help.pages" key="FORGOT_PWD" /></option>
                                <%--<option value="registration"><cms:contentText code="login.registration" key="SELF_REGI" /></option>--%>
                                <option value="contactus"><cms:contentText code="login.help.pages" key="CONTACT_US" /></option>
                            </select>
                        </div>
                    </div>

                </form>
            </div>
        </div>
    </div>

    <div class="row-fluid">
        <div class="span12 tab-content" id="loginPageHelp">
            <div class="whichhelp hide helpwith_login" data-template="loginPageHelpLogin">
                <cms:contentText code="login.help.pages" key="LOGIN_HELP_LBL" />
            </div>

            <div class="whichhelp hide helpwith_forgotid helpwith_contactus" data-template="loginPageFormContact">
            </div>

            <div class="whichhelp hide helpwith_forgotpass" data-template="loginPageFormForgotPass">
            </div>

            <div class="whichhelp hide helpwith_registration">
                <cms:contentText code="login.registration" key="REGI_HELP" />
            </div>
        </div>
    </div>

</div>

<script>
    $(document).ready(function() {

       lpv = new LoginPageView({
            el : $('#loginHelpPage'),
            mode : 'help',
            pageNav : {
                back : {
                    text : '<cms:contentText key="BACK" code="system.button" />',
                    url : '<%= RequestUtils.getBaseURI(request)%>/homePage.do'
                }
            },
            pageTitle : '<cms:contentText code="login.help.pages" key="LOGIN_HELP_LBL" />',
            loggedIn : false // to prevent the profile view from executing
        });
    });
</script>

<script type="text/template" id="loginPageFormContactTpl">
	   <%@include file="/login/loginPageFormContact.jsp"%>
</script>

<script type="text/template" id="loginPageFormForgotPassTpl">
		<%@include file="/login/loginPageFormForgotPass.jsp"%>
</script>

<script type="text/template" id="loginPageHelpLoginTpl">
<p>
	<cms:contentText code="login.help.pages" key="YOUR_LOGIN_INFO" />
</p>
<p>
	<cms:contentText code="login.help.pages" key="YOUR_LOGIN_INFO_TW" />
</p>
<p>
	<button class="btn gethelp" data-whichhelp="forgotid">
		<cms:contentText code="login.help.pages" key="FORGOT_LOGIN_ID" />
	</button>
	<button class="btn gethelp" data-whichhelp="forgotpass">
		<cms:contentText code="login.help.pages" key="FORGOT_PWD" />
	</button>
	<button class="btn gethelp" data-whichhelp="contactus">
		<cms:contentText code="login.help.pages" key="CONTACT_US" />
	</button>
</p>
</script>

<script type="text/template" id="loginPageFormSecurityQuestionTpl">
		<%@include file="/login/loginPageFormSecurityQuestion.jsp"%>
</script>


<script type="text/template" id="loginPageFormChangePassTpl">
	   <%@include file="/login/loginPageFormChangePass.jsp"%>
</script>
