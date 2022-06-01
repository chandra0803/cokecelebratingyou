<%-- UI REFACTORED --%>
<%@ include file="/include/taglib.jspf"%>

<html:hidden property="saveAudienceDefinition" value="false"/>

<table width="100%">
  <tr class="form-buttonrow">
    <td class="center-align">
      <html:submit styleClass="content-buttonstyle" onclick="selectPax();setActionAndDispatch('listBuilderSave.do', 'sendAudienceMembers')">
        <cms:contentText key="CONTINUE" code="system.button"/>
      </html:submit>
      &nbsp;&nbsp;&nbsp;
      <html:submit styleClass="content-buttonstyle" onclick="preSubmit();setActionAndDispatch('listBuilder.do?cancelAudienceMembersLookup=true','sendAudienceMembers')">
        <cms:contentText key="CANCEL" code="system.button"/>
      </html:submit>
    </td>
  </tr>
</table>
