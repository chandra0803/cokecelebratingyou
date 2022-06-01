<%--UI REFACTORED--%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils" %>
<%@ include file="/include/taglib.jspf" %>
<script language='javascript'>

  function enter()
  {
  	if (window.event && window.event.keyCode == 13)
   	setDispatch('forgotPwdNameValidate');
  	else
    return true;
  }

  function toCancel()
  {
    getContentForm().method.value = "forgotPwdCancel";
    getContentForm().action = "<%=RequestUtils.getBaseURI(request)%>/forgotPwdCancel.do";
  }

</script>

<html:form styleId="contentForm" action="/forgotPwdNameValidate" method="post" focus="userName">
  <html:hidden property="method" value=""/>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td align="left">
        <span class="headline"><cms:contentText key="TITLE" code="login.forgotpwd"/></span>
        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
	      <cms:contentText key="NAME_INST" code="login.forgotpwd"/>
	  </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>

        <cms:errors/>

        <table>
          <tr class="form-row-spacer">
            <beacon:label property="userName" required="true">
              <cms:contentText key="USERNAME_FLD" code="login.forgotpwd"/>
            </beacon:label>
            <td class="content-field">
              <html:text name="forgotPasswordForm" property="userName" onkeypress="return enter();"/>
            </td></tr>

          <tr class="form-blank-row">
            <td></td>
          </tr>
          <%--BUTTON ROWS ... only display these two rows if there are buttons to be displayed.--%>
          <tr class="form-buttonrow">
            <td></td>
            <td></td>
            <td align="left">
              <html:submit styleClass="login-buttonstyle"
                           onclick="setDispatch('forgotPwdNameValidate')">
                <cms:contentText key="CONTINUE" code="system.button"/>
              </html:submit>

              <html:submit property="cancelBtn" styleClass="login-buttonstyle" onclick="toCancel()">
                <cms:contentText key="CANCEL" code="system.button"/>
              </html:submit>
            </td>
          </tr>
          <%--END BUTTON ROW--%>
        </table>

      </td></tr></table>

</html:form>
