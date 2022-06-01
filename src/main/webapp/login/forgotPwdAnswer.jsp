<%--UI REFACTORED--%>
<%@ include file="/include/taglib.jspf" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils" %>

<script language='javascript'>
  function enter()
  {
    if ( window.event && window.event.keyCode == 13 )
      setDispatch('forgotPwdAnswerValidate');
    else
      return true;
  }
  function toCancel()
  {
    getContentForm().method.value = "forgotPwdCancel";
    getContentForm().action = "<%=RequestUtils.getBaseURI(request)%>/forgotPwdCancel.do";
  }

</script>

<html:form styleId="contentForm" action="/forgotPwdAnswerValidate" method="post" focus="secretAnswer">
  <html:hidden property="method" value=""/>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td align="left">
        <span class="headline"><cms:contentText key="TITLE" code="login.forgotpwd"/></span>
        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
	      <cms:contentText key="ANSWER_INST" code="login.forgotpwd"/>
	  </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
        <cms:errors/>

        <table>
          <c:if test="${not empty sessionScope.s_forgot_pwd_user}">
            <tr class="form-row-spacer">
              <beacon:label property="na" required="false">
                <cms:contentText key="USERNAME_FLD" code="login.forgotpwd"/>
              </beacon:label>
              <td class="content-field-review">
                <c:out value="${sessionScope.s_forgot_pwd_user.userName}"/>
              </td>
            </tr>

            <tr class="form-blank-row">
              <td></td>
            </tr>

            <tr class="form-row-spacer">
              <beacon:label property="na" required="false">
                <cms:contentText key="QUESTION_FLD" code="login.forgotpwd"/>
              </beacon:label>
              <td class="content-field-review">
                <c:out value="${sessionScope.s_forgot_pwd_user.secretQuestionType.name}"/>
              </td>
            </tr>

            <tr class="form-blank-row">
              <td></td>
            </tr>
          </c:if>

          <tr class="form-row-spacer">
            <beacon:label property="secretAnswer" required="true">
              <cms:contentText key="ANSWER_FLD" code="login.forgotpwd"/>
            </beacon:label>
            <td class="content-field">
            	<%-- bug fix 16402 called onkeypress="return enter() --%>
              <html:text name="forgotPasswordForm" property="secretAnswer" size="10" onkeypress="return enter();"/>
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
                           onclick="setDispatch('forgotPwdAnswerValidate')">
                <cms:contentText key="CONTINUE" code="system.button"/>
              </html:submit>

              <html:submit property="cancelBtn" styleClass="login-buttonstyle" onclick="toCancel()">
                <cms:contentText key="CANCEL" code="system.button"/>
              </html:submit>
            </td>
          </tr>
          <%--END BUTTON ROW--%>
        </table>

      </td>
    </tr>
  </table>

</html:form>
