<%@ include file="/include/taglib.jspf" %>

<html:form styleId="contentForm" action="characteristicDisplayNodeType">
  <html:hidden property="method" value=""/>
  <html:hidden property="version"/>
	<beacon:client-state>
		<beacon:client-state-entry name="domainId" value="${characteristicForm.domainId}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline">
          <cms:contentText key="VIEW_NODE_TYPE" code="node_type.labels"/>
        </span>

        <br/>
        <span class="subheadline"></span>

        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
			  <cms:contentText key="NODE_TYPE_VIEW_INSTRUCT" code="node_type.labels"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
        <cms:errors/>


        <%--  START HTML needed with display table --%>

        <table>
          <tr>
            <td class="content-field-label" nowrap>
              <cms:contentText key="NODE_TYPE_NAME" code="node_type.labels"/>
            </td>
            <td class="content-field-review left-align">
              <c:out value="${nodeType.nodeTypeName}"/>
            </td>
          </tr>
          <tr class="form-blank-row">
            <td></td>
          </tr>
        </table>

        <%-- Table --%>
        <table width="50%">


          <tr>
            <td align="right" colspan='2'>

              <%@ include file="/characteristic/characteristicList.jspf" %>

            </td>
          </tr>
          <tr>
            <td aligh="left" colspan='2'><BR/><BR/>
              <html:button property="backToList" styleClass="content-buttonstyle"
                           onclick="callUrl('../hierarchy/nodeTypeListDisplay.do')">
                <cms:contentText code="node_type.labels" key="BACK_TO_NODE_TYPE_LIST"/>
              </html:button>
            </td>
          </tr>
        </table>

      </td></tr></table>
</html:form>


