<%-- UI REFACTORED --%>
<%@ include file="/include/taglib.jspf"%>

<table>
  <tr class="form-row-spacer">
    <beacon:label property="audienceName" required="true">
      <cms:contentText key="AUDIENCE_NAME_LABEL" code="participant.list.builder.details"/>
    </beacon:label>
    <td class="content-field">
      <html:hidden property="saveAudienceDefinition" value="true"/>
      <html:text property="audienceName" styleClass="content-field"/>
    </td>
  </tr>
</table>

<table width="100%">
  <tr class="form-buttonrow">
    <td align="center">
      <beacon:authorize ifNotGranted="LOGIN_AS">
      <html:submit styleClass="content-buttonstyle" onclick="preSubmit();setActionAndDispatch('audienceSave.do','save')">
        <cms:contentText key="SAVE" code="system.button"/>
      </html:submit>
      </beacon:authorize>
      &nbsp;&nbsp;&nbsp;
      <c:url var="cancelUrl" value="${listBuilderForm.saveAudienceReturnUrl}"/>
      <html:submit styleClass="content-buttonstyle" onclick="getContentForm().action='${cancelUrl}'">
        <cms:contentText key="CANCEL" code="system.button"/>
      </html:submit>
    </td>
  </tr>
</table>