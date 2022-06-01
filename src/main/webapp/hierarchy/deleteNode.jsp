<%-- UI REFACTORED --%>
<%@ include file="/include/taglib.jspf" %>

<html:form styleId="contentForm" action="/maintainNode" >
  <html:hidden property="method"/>
	<beacon:client-state>
		<beacon:client-state-entry name="id" value="${nodeForm.id}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="TITLE" code="node.deletenode"/></span>
	<%-- Commenting out to fix in a later release
		&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" class="webhelp-button" onclick="javascript:FMCOpenHelp( 'G3H12', 'HTML Help Window', null, null );" target="_webhelp" value="<cms:contentText code="system.webhelp" key="WEB_HELP"/>">        
	--%>				
        <br/>
        <span class="subheadline"><c:out value="${nodeToDelete.name}"/></span>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="INSTRUCTIONAL_COPY" code="node.deletenode"/>
        </span>
        <br/><br/>

        <cms:errors/>

        <table>
          <tr class="form-row-spacer">
            <td></td>
            <td class="content-field-label top-align">
              <cms:contentText key="HIERARCHY_NAME" code="node.deletenode"/>
            </td>
            <td class="content-field-review">
              <cms:contentText key="${nodeToDelete.hierarchy.nameCmKey}" code="${nodeToDelete.hierarchy.cmAssetCode}"/>
              <c:if test="${nodeToDelete.hierarchy.primary == true}">
                <span class="content-field"><br/>* <cms:contentText key="PRIMARY" code="node.deletenode"/></span>
              </c:if>
            </td>
          </tr>

          <tr class="form-blank-row"><td colspan="3"></td></tr>

          <c:if test="${nodeToDelete.parentNode != null}">
            <tr class="form-row-spacer">
              <td></td>
              <td class="content-field-label">
                <cms:contentText key="NODE_PARENT" code="node.deletenode"/>
              </td>
              <td class="content-field-review">
                <c:out value="${nodeToDelete.parentNode.name}"/>
                <c:set var="parentNodeType" value="${nodeToDelete.parentNode.nodeType}"/>
                &nbsp;(<cms:contentText key="${parentNodeType.nameCmKey}" code="${parentNodeType.cmAssetCode}"/>)
              </td>
            </tr>

            <tr class="form-blank-row"><td colspan="3"></td></tr>
          </c:if>

          <tr class="form-row-spacer">
            <td></td>
            <td class="content-field-label">
              <cms:contentText key="NODE_DESCRIPTION" code="node.deletenode"/>
            </td>
            <td class="content-field-review">
              <c:out value="${nodeToDelete.description}" />
            </td>
          </tr>

          <tr class="form-blank-row"><td colspan="3"></td></tr>

          <c:forEach items="${nodeToDelete.nodeCharacteristics}" var="nodeCharacteristic">
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
            <beacon:label property="paxToNodeId" required="true" colspan="2">
              <cms:contentText key="PAX_REASSIGN" code="node.deletenode"/>
            </beacon:label>
          </tr>
          <tr>
            <td></td>
            <td class="content-field-label"></td>
            <td class="content-field">
              <html:select property="paxToNodeId" styleClass="content-field">
                <html:options collection="availableNodesForParticipants" property="id" labelProperty="name"  />
              </html:select>
            </td>
          </tr>

          <tr class="form-blank-row"><td colspan="3"></td></tr>

          <tr class="form-row-spacer">
            <beacon:label property="childrenToNodeId" required="true" colspan="2">
              <cms:contentText key="CHILD_NODE_REASSIGN" code="node.deletenode"/>
            </beacon:label>
          </tr>
          <tr>
            <td></td>
            <td class="content-field-label"></td>
            <td class="content-field">
              <html:select property="childrenToNodeId" styleClass="content-field">
                <html:options collection="availableNodesForChildren" property="id" labelProperty="name"/>
              </html:select>
            </td>
          </tr>

          <tr class="form-buttonrow">
            <td></td>
            <td></td>
            <td align="left">
              <beacon:authorize ifNotGranted="LOGIN_AS">
              <html:submit property="submit" styleClass="content-buttonstyle">
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
