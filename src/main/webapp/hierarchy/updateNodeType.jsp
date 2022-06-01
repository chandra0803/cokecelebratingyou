<%--UI REFACTORED--%>
<%@ include file="/include/taglib.jspf" %>

<html:form styleId="contentForm" action="nodeTypeMaintainUpdate" method="POST">
  <html:hidden property="method" value="update"/>
	<beacon:client-state>
		<beacon:client-state-entry name="id" value="${nodeTypeForm.id}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="EDIT_NODE_TYPE"
                                                code="node_type.labels"/></span>
        <br/>
        <span class="subheadline"></span>
        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="EDIT_INSTRUCT" code="node_type.labels"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>

        <cms:errors/>

        <table>
          <tr class="form-row-spacer">
            <beacon:label property="name" required="true">
              <cms:contentText key="NODE_TYPE_NAME" code="node_type.labels"/>
            </beacon:label>
            <td class="content-field-review">
              <html:text property="name" size="30" maxlength="40" styleClass="content-field"/>
            </td>
          </tr>
          <tr class="form-buttonrow">
            <td></td>
            <td></td>
            <td align="left">
              <beacon:authorize ifNotGranted="LOGIN_AS">
              <html:submit styleClass="content-buttonstyle">
                <cms:contentText code="system.button" key="SAVE"/>
              </html:submit>
              </beacon:authorize>
              <html:cancel styleClass="content-buttonstyle">
                <cms:contentText code="system.button" key="CANCEL"/>
              </html:cancel>
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>

</html:form>



