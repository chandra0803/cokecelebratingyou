<%--UI REFACTORED--%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<script>
<!--
function maintainClaimFormStepElement( method )
{
	document.claimFormStepElementForm.method.value= method;
}
//-->
</script>
<html:form styleId="contentForm" action="claimFormStepElementCreate">
  <html:hidden property="method"/>
  <html:hidden property="claimFormStepElementTypeCode"/>
	<beacon:client-state>
	 	<beacon:client-state-entry name="claimFormId" value="${claimFormStepElementForm.claimFormId}"/>
	 	<beacon:client-state-entry name="claimFormStepId" value="${claimFormStepElementForm.claimFormStepId}"/>
	 	<beacon:client-state-entry name="claimFormStepElementId" value="${claimFormStepElementForm.claimFormStepElementId}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentTemplateText key="ADD_HEADER" code="claims.form.step.element" args="${claimFormStepElementForm.claimFormStepElementTypeDesc}" /></span>

        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="ADD_INFO" code="claims.form.step.element"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>

        <cms:errors/>


        <table>
	        <tr>
            <beacon:label property="cmData.elementLabel" required="true">
              <cms:contentText key="LABEL_FIELD" code="claims.form.step.element"/>
            </beacon:label>
            <td class="content-field">
			        <html:text property="cmData.elementLabel" size="30" maxlength="60" styleClass="content-field"/>
    	      </td>
          </tr>
          <tr class="form-blank-row"><td></td></tr>
	        <tr>
            <beacon:label property="description" required="false" styleClass="content-field-label-top">
              <cms:contentText key="DESCRIPTION_FIELD" code="claims.form.step.element"/>
            </beacon:label>
            <td class="content-field">
			        <html:textarea property="description" cols="45" rows="5" styleClass="content-field"/>
    	      </td>
          </tr>
          <tr class="form-blank-row"><td></td></tr>
          <c:forEach items="${elementItems}" var="item">
            <!-- Only show the 'is why field' for nominations module -->
            <c:choose>
            <c:when test="${item.yesNoRadioField && item.formProperty == 'whyField' && formModuleType != 'nom'}">
            </c:when>
            <c:otherwise>
              <tr>
                <beacon:label property="${item.formProperty}" required="true" styleClass="content-field-label-top">
                  <cms:contentText key="${item.labelKey}" code="claims.form.step.element"/>
                </beacon:label>
                <td class="content-field">
                  <c:if test="${item.textField}"><html:text property="${item.formProperty}" /></c:if>
                  <c:if test="${item.numberField}"><html:text property="${item.formProperty}" maxlength="10" size="5" /></c:if>
                  <c:if test="${item.textAreaField}"><%--TEXTAREA currently means COPY BLOCK hence the call to getCopyBlock on the form--%>
                                      <textarea style="WIDTH: 100%" id='<c:out value="${item.formProperty}"/>' name='<c:out value="${item.formProperty}"/>' rows="5" convert_beacon="true"><c:out value="${claimFormStepElementForm.copyBlock}"/></textarea>
                                  </c:if>
                  <c:if test="${item.selectField}">
                    <c:set var="pickList" value="${item.pickList}" scope="page"  />
                    <html:select property="${item.formProperty}" styleClass="content-field" >
                             <html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>
                        <html:options collection="pickList" property="code" labelProperty="name"  />
                        </html:select>
                  </c:if>
                  <c:if test="${item.selectPickListField}">
                      <html:select property="${item.formProperty}" styleClass="content-field" >
                             <html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>
                        <html:options collection="pickListItemAssets" property="code" labelProperty="name"  />
                        </html:select>
                  </c:if>
                  <c:if test="${item.yesNoRadioField}">
                            <html:radio property="${item.formProperty}" value="false"/> &nbsp;&nbsp; <cms:contentText key="NO" code="system.common.labels"/><br>
                            <html:radio property="${item.formProperty}" value="true"/> &nbsp;&nbsp; <cms:contentText key="YES" code="system.common.labels"/>
                  </c:if>
                  <c:if test="${item.enumListRadioField}">
                    <c:forEach items="${item.enumList}" var="enumListItem">
                            <html:radio property="${item.formProperty}" value="${enumListItem.code}"/> &nbsp;&nbsp; <cms:contentText key="${enumListItem.code}" code="claims.form.step.element"/><br>
                    </c:forEach>
                  </c:if>
                </td>
              </tr>
              <tr class="form-blank-row"><td></td></tr>
            </c:otherwise>
            </c:choose>
          </c:forEach>

          <tr class="form-buttonrow">
            <td></td>
            <td></td>
            <td align="left">
              <beacon:authorize ifNotGranted="LOGIN_AS">
			        <html:submit styleClass="content-buttonstyle" onclick="maintainClaimFormStepElement('create')">
			          <cms:contentText code="system.button" key="SAVE" />
			        </html:submit>
              </beacon:authorize>
			        <html:cancel styleClass="content-buttonstyle" onclick="setActionAndDispatch('claimFormStepView.do', 'display');">
				        <cms:contentText code="system.button" key="CANCEL" />
			        </html:cancel>
            </td>
          </tr>

        </table>
      </td>
    </tr>
  </table>

</html:form>
