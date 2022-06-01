<%-- UI REFACTORED --%>
<%@ include file="/include/taglib.jspf"%>

<html:form styleId="contentForm" action="/hierarchyMaintainCreate" >
  <html:hidden property="method" />
  <html:hidden property="cmAssetCode" />
  <html:hidden property="nameCmKey" />
  <html:hidden property="version" />
  <html:hidden property="createdBy" />
  <html:hidden property="dateCreated" />
  <html:hidden property="rosterHierarchyId" />  
	<beacon:client-state>
		<beacon:client-state-entry name="id" value="${hierarchyForm.id}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="TITLE_ADD" code="hierarchy.hierarchy"/></span>
	<%-- Commenting out to fix in a later release
        &nbsp;&nbsp;&nbsp;&nbsp;<input type="button" class="webhelp-button" onclick="javascript:FMCOpenHelp( 'G3H14', 'HTML Help Window', null, null );" target="_webhelp" value="<cms:contentText code="system.webhelp" key="WEB_HELP"/>">      
	--%>				
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="ADD_INSTRUCTIONAL_COPY" code="hierarchy.hierarchy"/>
        </span>
        <br/><br/>

        <cms:errors/>

        <%-- Add Hierarchy form --%>
        <table>
          <tr class="form-row-spacer">
            <beacon:label property="name" required="true">
              <cms:contentText key="LABEL_HIERARCHY_NAME" code="hierarchy.hierarchy"/>
            </beacon:label>
            <td class="content-field">
              <html:text property="name" styleClass="content-field killme" maxlength="255"/>
            </td>
          </tr>

          <tr class="form-blank-row"><td colspan="3"></td></tr>

          <tr class="form-row-spacer">
            <beacon:label property="description" required="false" styleClass="content-field-label-top">
              <cms:contentText key="LABEL_HIERARCHY_DESC" code="hierarchy.hierarchy"/>
            </beacon:label>
            <td class="content-field">
              <html:textarea property="description" styleClass="content-field" rows="5" cols="50"/>
            </td>
          </tr>

          <tr class="form-blank-row"><td colspan="3"></td></tr>

          <tr class="form-row-spacer">
            <beacon:label property="active" required="true">
              <cms:contentText key="LABEL_HIERARCHY_STATUS" code="hierarchy.hierarchy"/>
            </beacon:label>
            <td class="content-field">
              <html:select property="active">
                <html:option value="true"><cms:contentText key="TRUE" code="hierarchy.status"/></html:option>
                <html:option value="false"><cms:contentText key="FALSE" code="hierarchy.status"/></html:option>
              </html:select>
            </td>
          </tr>
          
          <tr class="form-blank-row"><td colspan="3"></td></tr>

          <tr class="form-row-spacer">
            <beacon:label property="active" required="true">
              <cms:contentText key="NODE_TYPE_REQ" code="hierarchy.hierarchy"/>
            </beacon:label>
            <td class="content-field">
              <table>
			    <tr>
			      <td class="content-field"><html:radio property="nodeTypeRequired" value="true"/></td>
			      <td class="content-field"><cms:contentText code="system.common.labels" key="YES"/></td>      
			    </tr>
			    <tr>
			      <td class="content-field"><html:radio property="nodeTypeRequired" value="false"/></td>
			      <td class="content-field"><cms:contentText code="system.common.labels" key="NO"/></td>       
			    </tr>
			  </table>
            </td>
          </tr>

          <tr class="form-blank-row"><td colspan="3"></td></tr>

          <tr class="form-row-spacer">
            <beacon:label property="addedNodeTypes" required="true" styleClass="content-field-label-top">
              <cms:contentText key="LABEL_NODE_TYPES" code="hierarchy.hierarchy"/>
            </beacon:label>
            <td>
              <table>
                <tr>
                  <td class="content-field"><cms:contentText key="LABEL_AVAILABLE" code="hierarchy.hierarchy"/></td>
                  <td></td>
                  <td class="content-field"><cms:contentText key="LABEL_ASSIGNED" code="hierarchy.hierarchy"/></td>
                </tr>
                <tr>
                  <td valign="top">
                    <html:select property="availableNodeTypes" multiple="true" size="10" style="width: 300px" styleClass="killme">
                      <html:options collection="allNodeTypes" property="id" labelProperty="i18nName" />
                    </html:select>
                  </td>

                  <td valign="middle">
                    <html:button property="right" styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('hierarchyAddNodeType.do', 'addNodeType')">
                      <cms:contentText key="BUTTON_RIGHT" code="hierarchy.hierarchy"/>
                    </html:button>
                    <br/>
                    <html:button property="left" styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('hierarchyRemoveNodeType.do', 'removeNodeType')">
                      <cms:contentText key="BUTTON_LEFT" code="hierarchy.hierarchy"/>
                    </html:button>
                  </td>

                  <td valign="top">
                    <c:choose>
                      <c:when test="${nodeTypes != null}">
                        <html:select property="addedNodeTypes" multiple="true" size="10" style="width: 300px" styleClass="killme">
                          <html:options collection="nodeTypes" property="id" labelProperty="i18nName" />
                        </html:select>
                      </c:when>
                      <c:otherwise>
                        <html:select property="addedNodeTypes" multiple="true" size="10" style="width:300px" styleClass="killme">
                          <html:option value="-1">&nbsp;</html:option>
                        </html:select>
                      </c:otherwise>
                    </c:choose>
                  </td>
                </tr>
              </table>
            </td>
          </tr>

          <tr class="form-buttonrow">
            <td></td>
            <td></td>
            <td align="left">
              <beacon:authorize ifNotGranted="LOGIN_AS">
              <html:submit styleClass="content-buttonstyle" onclick="setDispatch('create')">
                <cms:contentText code="system.button" key="SAVE" />
              </html:submit>
              </beacon:authorize>
              &nbsp;
              <html:cancel styleClass="content-buttonstyle">
                <cms:contentText code="system.button" key="CANCEL" />
              </html:cancel>
            </td>
          </tr>
        </table>

      </td>
    </tr>
  </table>
</html:form>
