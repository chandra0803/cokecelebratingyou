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
    <td align="center" colspan="3">
      <beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
      <html:submit styleClass="content-buttonstyle" onclick="preSubmit();setActionAndDispatch('audienceSave.do','save')">
        <cms:contentText key="SAVE" code="system.button"/>
      </html:submit>
      </beacon:authorize>
      &nbsp;&nbsp;&nbsp;
      <c:url var="cancelUrl" value="${listBuilderForm.saveAudienceReturnUrl}"/>
      <html:submit styleClass="content-buttonstyle" onclick="getContentForm().action='${cancelUrl}'">
        <cms:contentText key="CANCEL" code="system.button"/>
      </html:submit>
      &nbsp;&nbsp;&nbsp;
      <beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
      <html:button property="deleteAudience" styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('deleteAudience.do','delete');" >
          <cms:contentText code="purl.ajax" key="DELETE" />
      </html:button>
      &nbsp;&nbsp;&nbsp;
      <html:button property="copyPromo" styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('copyAudience.do','display');" >
          <cms:contentText code="participant.list.builder.details" key="COPY_AUDIENCE" />
      </html:button>
      </beacon:authorize>
    </td>
  </tr>
</table>
