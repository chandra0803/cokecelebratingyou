<%-- UI REFACTORED --%>
<%@ include file="/include/taglib.jspf" %>

<script type="text/javascript">
<!--
function maintainUserNode( method )
{
  document.userNodeListForm.method.value = method;
  document.userNodeListForm.action = "maintainUserNodes.do";
  document.userNodeListForm.submit();
}
//-->
</script>

<html:form styleId="contentForm" action="updateUserNodes" >
  <html:hidden property="method"/>
  <html:hidden property="userName"/>
  <html:hidden property="nameOfNode"/>
	<beacon:client-state>
		<beacon:client-state-entry name="userId" value="${userNodeForm.userId}"/>
		<beacon:client-state-entry name="nodeId" value="${userNodeForm.nodeId}"/>
		<beacon:client-state-entry name="returnActionMapping" value="${userNodeForm.returnActionMapping}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="TITLE" code="user.node.update"/></span>
        <br/>
        <span class="subheadline"><c:out value="${userNode.node.name}"/></span>
         <br/>
         <beacon:username userId="${displayNameUserId}"/>

        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="INSTRUCTIONAL_COPY" code="user.node.update"/>
        </span>
        <br/><br/>

        <cms:errors/>

        <table>
          <tr class="form-row-spacer">
            <td></td>
            <td class="content-field-label"><cms:contentText key="PARTICIPANT_NAME" code="user.node.update"/></td>
            <td class="content-field-review"><c:out value="${user.lastName}"/>,&nbsp;<c:out value="${user.firstName}"/></td>
          </tr>

          <tr class="form-blank-row"><td colspan="3"></td></tr>

          <tr class="form-row-spacer">
            <beacon:label property="role" required="true">
              <cms:contentText key="NODE_ROLE" code="user.node.update"/>
            </beacon:label>
            <td class="content-field">
              <html:select property="role">
                <html:options collection="hierarchyTypeValues" property="code" labelProperty="name"  />
              </html:select>
            </td>
          </tr>

          <tr class="form-buttonrow">
            <td></td>
            <td></td>
            <td align="left">
              <beacon:authorize ifNotGranted="LOGIN_AS">
              <html:submit styleClass="content-buttonstyle" onclick="setDispatch('${userNodeForm.method}')">
                <cms:contentText key="SAVE" code="system.button"/>
              </html:submit>
              </beacon:authorize>
              <html:cancel styleClass="content-buttonstyle">
                <cms:contentText key="CANCEL" code="system.button"/>
              </html:cancel>
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
</html:form>