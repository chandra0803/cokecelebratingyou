<%--UI REFACTORED--%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>
<script type="text/javascript">
<!--
function editClaimFormStepElement( )
{
	document.claimFormStepViewForm.action = "claimFormStepElementDisplay.do";
	document.claimFormStepViewForm.method.value = "prepareUpdate";
	document.claimFormStepViewForm.submit();
}
//-->
</script>

<html:form styleId="contentForm" action="claimFormStepView">
  <html:hidden property="method"/>
	<beacon:client-state>
	 	<beacon:client-state-entry name="claimFormId" value="${claimFormStep.claimForm.id}"/>
	 	<beacon:client-state-entry name="claimFormStepId" value="${claimFormStepViewForm.claimFormStepId}"/>
	 	<beacon:client-state-entry name="claimFormStepElementId" value="${claimFormStepViewForm.claimFormStepElementId}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline">
          <cms:contentTemplateText key="VIEW_HEADER" code="claims.form.step.element" args="${claimFormStepElementForm.claimFormStepElementTypeDesc}" />
        </span>

        <br/><br/>
        <%--END INSTRUCTIONS--%>

        <cms:errors/>

        <table width="50%">
          <tr class="form-row-spacer">
		        <td ></td>
            <td class="content-field-label">
              <cms:contentText key="LABEL_FIELD" code="claims.form.step.element"/>
            </td>
            <td class="content-field-review" colspan="2">
			        <c:out value="${claimFormStepElementForm.cmData.elementLabel}"/>
			      <beacon:authorize ifNotGranted="LOGIN_AS">
			        <c:if test="${claimFormStep.claimForm.editable}">
  			        &nbsp;&nbsp;&nbsp;&nbsp;
  			        
      		      <a href="#" onclick="editClaimFormStepElement();" class="crud-content-link">
		              <cms:contentText key="EDIT" code="system.link"/>
		            </a>
		          </c:if>
		          </beacon:authorize>
            </td>
          </tr>
          <tr class="form-row-spacer">
		        <td ></td>
            <td class="content-field-label">
              <cms:contentText key="DESCRIPTION_FIELD" code="claims.form.step.element"/>
            </td>
            <td class="content-field-review" colspan="2">
			        <c:out value="${claimFormStepElementForm.description}"/>
            </td>
          </tr>

          <c:forEach items="${elementItems}" var="item" varStatus="status" >
            <!-- Only show the 'is why field' for nominations module -->
            <c:choose>
            <c:when test="${item.yesNoRadioField && item.formProperty == 'whyField' && formModuleType != 'nom'}">
            </c:when>
            <c:otherwise>
            <tr class="form-row-spacer">
		          <td ></td>
		          <td class="content-field-label" nowrap valign="top">
			          <cms:contentText key="${item.labelKey}" code="claims.form.step.element"/>
		          </td>
              <td class="content-field-review" colspan="2">
                <c:if test="${item.textField || item.numberField}"><c:out value="${elementValues[status.count-1]}"/></c:if>
                <c:if test="${item.textAreaField}"><c:out value="${elementValues[status.count-1]}" escapeXml="false"/></c:if>
                <c:if test="${item.selectField}"><c:out value="${elementValues[status.count-1].name}"/></c:if>
                <c:if test="${item.selectPickListField}"><c:out value="${elementValues[status.count-1]}"/></c:if>
                <c:if test="${item.yesNoRadioField}">
                  <c:if test="${!elementValues[status.count-1]}">
                    <cms:contentText key="NO" code="system.common.labels"/>
                  </c:if>
                  <c:if test="${elementValues[status.count-1]}">
                    <cms:contentText key="YES" code="system.common.labels"/>
                  </c:if>
                </c:if>
                <c:if test="${item.enumListRadioField}"><cms:contentText key="${elementValues[status.count-1]}" code="claims.form.step.element"/></c:if>
              </td>
            </tr>
            </c:otherwise>
            </c:choose>
          </c:forEach>

          <tr class="form-buttonrow">
            <td></td>
            <td></td>
            <td align="left" colspan="3" >
			        <html:button property="" styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('claimFormStepView.do', 'display');">
			          <cms:contentText code="system.button" key="BACK" />
			        </html:button>
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
</html:form>