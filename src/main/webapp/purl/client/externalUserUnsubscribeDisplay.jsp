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

<html>
<body>
<html:form styleId="contentForm" action="externalUnsubscribe" >
<html:hidden property="method" value="unsubscribe"/>
<table border="0" cellpadding="10" cellspacing="0" width="100%">
<tr>
<td>
<cms:errors/>
<table>  
<tr class="form-row-spacer">				  
            <beacon:label property="timeZoneId" required="true">
              <p><cms:contentText key="UNSUBSCRIBE_CONTRIBUTOR" code="purl.contributor"/></p>
              <cms:contentText key="EMAIL_ADDRESS" code="self.enrollment.pax.registration"/> <br></br>
              <html:text styleId="emailAddress" property="emailAddress" name="purlContributionForm" size="30" maxlength="75"/><br></br>
            </beacon:label>
</tr>

<tr class="form-blank-row">
            <td></td>
</tr>

<tr class="form-row-spacer">				  
            <beacon:label property="timeZoneId" required="true">
              <div class="capcha control-group validateme" data-validate-flags="nonempty" data-validate-fail-msgs="{&quot;nonempty&quot; : &quot;<cms:contentText key="CAPTCHA_VALIDATION" code="purl.terms.and.conditions"/>&quot;}">
                        <div class="controls">
                            <p><cms:contentText key="ENTER_CAPTCHA" code="self.enrollment.pax.registration"/></p>
                               <botDetect:captcha
							                id="loginFormCaptcha"
							                codeLength="4"
							                codeStyle="alpha"
							                
							              /><br></br>
                            
                        </div>
		                <div class="controls">
							<input type="text" name="captchaResponse" id="captchaResponse" placeholder="">
						</div>
			</div>
            </beacon:label>
</tr>

<tr class="form-blank-row">
            <td></td>
</tr>	 

<tr class="form-buttonrow">
           <td></td>
           <td align="center">
             <html:submit styleClass="content-buttonstyle" onclick="setDispatch('unsubscribe')">
               <cms:contentText key="SAVE" code="system.button"/>
             </html:submit>
             <html:button property="cancelBtn" styleClass="content-buttonstyle" onclick="window.close()">
                <cms:contentText key="CANCEL" code="system.button"/>
             </html:button>
           </td>
</tr>
</table>
</td>
</tr>
</table>
</html:form>
</body>
</html>
  