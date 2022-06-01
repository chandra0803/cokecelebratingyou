<%--UI REFACTORED--%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<html:form styleId="contentForm" action="claimFormMaintainUpdate">
  <html:hidden property="method"/>
  <html:hidden property="cmAssetCode"/>
  <html:hidden property="claimFormStatusType"/>
  <html:hidden property="claimFormStatusTypeDesc"/>
  <html:hidden property="version"/>
  <html:hidden property="dateCreated"/>
  <html:hidden property="createdBy"/>
	<beacon:client-state>
	 	<beacon:client-state-entry name="claimFormId" value="${claimFormForm.claimFormId}"/>
	</beacon:client-state>
  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="EDIT_HEADER" code="claims.form.details"/></span>

        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="EDIT_INFO" code="claims.form.details"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>

        <cms:errors/>

        <table>
	        <tr>
            <beacon:label property="name" required="true">
              <cms:contentText key="FORM_NAME" code="claims.form.details"/>
            </beacon:label>
            <td class="content-field">
			        <html:text property="formName" size="30" maxlength="60" styleClass="content-field"/>
    	      </td>
          </tr>
          <tr class="form-blank-row"><td></td></tr>
	        <tr>
            <beacon:label property="description" required="false" styleClass="content-field-label-top">
              <cms:contentText key="DESCRIPTION" code="claims.form.details"/>
            </beacon:label>
            <td class="content-field">
              <html:textarea name="claimFormForm"  property="description" styleClass="content-field" rows="6" cols="35"/>
    	      </td>
          </tr>
          <tr class="form-blank-row"><td></td></tr>
	        <tr>
            <beacon:label property="claimFormModuleType" required="true">
              <cms:contentText key="MODULE" code="claims.form.details"/>
            </beacon:label>
            <td class="content-field">
	    	      <html:select property="claimFormModuleType" styleClass="content-field" >
			          <html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>
			          <html:options collection="moduleList" property="code" labelProperty="name"  />
			        </html:select>
    	      </td>
          </tr>
          <tr class="form-blank-row"><td></td></tr>

          <%--BUTTON ROWS ... For Input--%>
          <tr class="form-buttonrow">
            <td></td>
            <td></td>
            <td align="left">
              <beacon:authorize ifNotGranted="LOGIN_AS">
			        <html:submit styleClass="content-buttonstyle" onclick="setDispatch('update')">
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