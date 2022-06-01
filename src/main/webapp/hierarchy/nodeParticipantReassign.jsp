<%-- UI REFACTORED --%>
<%@ include file="/include/taglib.jspf"%>

<html:form styleId="contentForm" action="nodeParticipantMaintain" >
  <html:hidden property="method" value="updateReassignParticipants"/>
	<beacon:client-state>
		<beacon:client-state-entry name="oldNodeId" value="${nodeParticipantReassignForm.oldNodeId}"/>
		<beacon:client-state-entry name="newNodeId" value="${nodeParticipantReassignForm.newNodeId}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="TITLE" code="node.reassign"/></span>
	<%-- Commenting out to fix in a later release
		&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" class="webhelp-button" onclick="javascript:FMCOpenHelp( 'G3H18', 'HTML Help Window', null, null );" target="_webhelp" value="<cms:contentText code="system.webhelp" key="WEB_HELP"/>">        
	--%>				
        <br/>
        <span class="subheadline"><c:out value="${node.name}"/></span>

        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="INSTRUCTIONAL_COPY" code="node.reassign"/>
        </span>
        <br/><br/>

        <cms:errors/>

        <table>
          <tr class="form-row-spacer">
            <td></td>
            <td class="content-field-label top-align">
              <cms:contentText key="HIERARCHY_NAME" code="node.reassign"/>
            </td>
            <td class="content-field-review">
              <cms:contentText key="${node.hierarchy.nameCmKey}" code="${node.hierarchy.cmAssetCode}"/>
              <c:if test="${node.hierarchy.primary == true}">
                <span class="content-field"><br/>* <cms:contentText key="PRIMARY" code="node.reassign"/></span>
              </c:if>
            </td>
          </tr>

          <tr class="form-blank-row"><td colspan="3"></td></tr>

          <c:if test="${node.parentNode != null}">
            <tr class="form-row-spacer">
              <td></td>
              <td class="content-field-label">
                <cms:contentText key="NODE_PARENT" code="node.reassign"/>
              </td>
              <td class="content-field-review">
                <c:out value="${node.parentNode.name}"/>
                <c:set var="parentNodeType" value="${node.parentNode.nodeType}"/>
                &nbsp;(<cms:contentText key="${parentNodeType.nameCmKey}" code="${parentNodeType.cmAssetCode}"/>)
              </td>
            </tr>

            <tr class="form-blank-row"><td colspan="3"></td></tr>
          </c:if>

          <tr class="form-row-spacer">
            <td></td>
            <td class="content-field-label">
              <cms:contentText key="NODE_DESCRIPTION" code="node.reassign"/>
            </td>
            <td class="content-field-review">
              <c:out value="${node.description}"/>
            </td>
          </tr>

          <tr class="form-blank-row"><td colspan="3"></td></tr>

          <c:forEach items="${nodeCharacteristicList}" var="nodeCharacteristic">
            <tr class="form-row-spacer">
              <td></td>
              <td class="content-field-label">
                <c:out value="${nodeCharacteristic.nodeTypeCharacteristicType.characteristicName}"/>
              </td>
              <td class="content-field-review">
                <c:out value="${nodeCharacteristic.characteristicValue}"/>
              </td>
            </tr>

            <tr class="form-blank-row"><td colspan="3"></td></tr>
          </c:forEach>

          <tr class="form-row-spacer">
            <beacon:label property="newNodeName" required="true">
              <cms:contentText key="NEW_NODE_NAME" code="node.reassign"/>
            </beacon:label>
            <td class="content-field">
              <html:text property="newNodeName" size="30" styleClass="content-field"/>&nbsp;&nbsp;
              <a href="javascript:setActionDispatchAndSubmit('nodeParticipantReassignDisplay.do','prepareNodeLookup');">
                <cms:contentText key="LOOKUP_NODE" code="node.reassign"/>
              </a>
            </td>
          </tr>

          <tr class="form-buttonrow">
            <td></td>
            <td></td>
            <td align="left">
              <beacon:authorize ifNotGranted="LOGIN_AS">
              <html:submit styleClass="content-buttonstyle">
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
