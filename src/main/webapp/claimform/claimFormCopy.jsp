<%--UI REFACTORED--%>
<%@ include file="/include/taglib.jspf"%>

<html:form styleId="contentForm" action="claimFormMaintainCopy">
  <html:hidden property="method" value="copy"/>
  <html:hidden property="formName"/>
	<beacon:client-state>
	 	<beacon:client-state-entry name="claimFormId" value="${claimFormForm.claimFormId}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="TITLE" code="claims.form.copy"/></span>

        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="INSTRUCTIONAL_COPY" code="claims.form.copy"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>

        <cms:errors/>

        <table>
	        <tr>
            <beacon:label property="formName" required="false">
              <cms:contentText key="FORM_NAME" code="claims.form.copy"/>
            </beacon:label>
            <td class="content-field">
			        <c:out value="${claimFormForm.formName}"/>
    	      </td>
          </tr>
          <tr class="form-blank-row"><td></td></tr>
	        <tr>
            <beacon:label property="newFormName" required="true">
              <cms:contentText key="NEW_FORM_NAME" code="claims.form.copy"/>
            </beacon:label>
            <td class="content-field">
			        <html:text property="newFormName" size="30" styleClass="content-field"/>
    	      </td>
          </tr>
          <tr class="form-blank-row"><td></td></tr>

          <%--BUTTON ROWS ... For Input--%>
          <tr class="form-buttonrow">
            <td></td>
            <td></td>
            <td align="left">
              <beacon:authorize ifNotGranted="LOGIN_AS">
			        <html:submit styleClass="content-buttonstyle" onclick="setDispatch('copy')">
				        <cms:contentText code="system.button" key="SAVE" />
			        </html:submit>
              </beacon:authorize>
			        <html:cancel styleClass="content-buttonstyle">
				        <cms:contentText code="system.button" key="CANCEL" />
			        </html:cancel>
            </td>
          </tr>
          <%--END BUTTON ROW--%>

        </table>
      </td>
    </tr>
  </table>
</html:form>
		