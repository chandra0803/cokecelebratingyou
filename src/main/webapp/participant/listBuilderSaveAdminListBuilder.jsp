<%-- UI REFACTORED --%>
<%@ include file="/include/taglib.jspf"%>

<table width="100%">
  <tr class="form-row-spacer">
    <beacon:label property="saveAudienceDefinition">
      <cms:contentText key="SAVE_AUDIENCE_DEFINITION" code="participant.list.builder.details"/>
    </beacon:label>
    <td class="content-field">
      <html:radio property="saveAudienceDefinition" value="false"/>
      <cms:contentText key="NO" code="system.common.labels"/>
    </td>
    <td></td>
  </tr>

  <tr class="form-row-spacer">
    <td colspan="2"/>
    <td class="content-field">
      <html:radio property="saveAudienceDefinition" value="true"/>
      <cms:contentText key="YES" code="system.common.labels"/>
    </td>
    <td class="content-field">
      <cms:contentText key="AUDIENCE_NAME_LABEL" code="participant.list.builder.details"/>&nbsp;
      <html:text property="audienceName" styleClass="content-field"/>
    </td>
  </tr>

  <tr class="form-buttonrow">
    <td class="center-align" colspan="4">
      <beacon:authorize ifNotGranted="LOGIN_AS">
      <html:submit styleClass="content-buttonstyle" onclick="preSubmit();setActionAndDispatch('audienceSave.do','save')">
        <cms:contentText key="CONTINUE" code="system.button"/>
      </html:submit>
      </beacon:authorize>
      &nbsp;&nbsp;&nbsp;
      <c:url var="cancelUrl" value="${listBuilderForm.saveAudienceReturnUrl}"/>
      <html:submit styleClass="content-buttonstyle" onclick="getContentForm().action='${cancelUrl}'">
        <cms:contentText code="system.button" key="CANCEL"/>
      </html:submit>
    </td>
  </tr>
</table>