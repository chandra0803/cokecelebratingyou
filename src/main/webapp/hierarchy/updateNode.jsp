<%-- UI REFACTORED --%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.ui.node.NodeForm"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf" %>

<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-en.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-setup.js"></script>

<table border="0" cellpadding="10" cellspacing="0" width="100%">
  <tr>
    <td>
      <span class="headline"><cms:contentText key="TITLE" code="node.createnode"/></span>
	<%-- Commenting out to fix in a later release
	  &nbsp;&nbsp;&nbsp;&nbsp;<input type="button" class="webhelp-button" onclick="javascript:FMCOpenHelp( 'G3H16', 'HTML Help Window', null, null );" target="_webhelp" value="<cms:contentText code="system.webhelp" key="WEB_HELP"/>">      
	--%>				
      <br/><br/>
      <span class="content-instruction">
        <cms:contentText key="INSTRUCTIONAL_COPY" code="node.createnode"/>
      </span>
      <br/><br/>

      <cms:errors/>

      <%-- Edit Node Form --%>
      <html:form action="/nodeMaintainUpdate">
        <html:hidden property="method"/>
        <html:hidden property="createdBy"/>
        <html:hidden property="dateCreated"/>
        <html:hidden property="version"/>
				<beacon:client-state>
	 				<beacon:client-state-entry name="id" value="${nodeForm.id}"/>
	 				<beacon:client-state-entry name="parentNodeId" value="${nodeForm.parentNodeId}"/>
	 				<beacon:client-state-entry name="hierarchyId" value="${nodeForm.hierarchyId}"/>
	 				<beacon:client-state-entry name="path" value="${nodeForm.path}"/>
	 				<%--<beacon:client-state-entry name="nodeTypeId" value="${nodeForm.nodeTypeId}"/>--%>
				</beacon:client-state>

        <table>
          <tr class="form-row-spacer">
            <td></td>
            <td class="content-field-label top-align">
              <cms:contentText key="HIERARCHY_NAME" code="node.list"/>
            </td>
            <td class="content-field-review">
              <cms:contentText key="${hierarchy.nameCmKey}" code="${hierarchy.cmAssetCode}"/>
              <c:if test="${hierarchy.primary == true}">
                <span class="content-field"><br/>* <cms:contentText key="LABEL_PRIMARY" code="node.view"/></span>
              </c:if>
            </td>
          </tr>

          <tr class="form-blank-row"><td></td></tr>

          <c:if test="${parentNode != null}">
            <tr class="form-row-spacer">
              <td></td>
              <td class="content-field-label">
                <cms:contentText key="PARENT" code="node.list"/>
              </td>
              <td class="content-field-review">
                <c:out value="${parentNode.name}"/>
                <c:set var="parentNodeType" value="${parentNode.nodeType}"/>
                &nbsp;(<cms:contentText key="${parentNodeType.nameCmKey}" code="${parentNodeType.cmAssetCode}"/>)
              </td>
            </tr>

            <tr class="form-blank-row"><td></td></tr>
          </c:if>

          <tr class="form-row-spacer">
            <td></td>
            <td class="content-field-label">
              <cms:contentText key="NODE_TYPE" code="node.list"/>
            </td>
            <td class="content-field">
              <%--<cms:contentText key="${nodeType.nameCmKey}" code="${nodeType.cmAssetCode}"/> --%>
              <%-- %><html:select property="availableNodeTypes" size="10" styleClass="killme">
                      <html:options collection="allNodeTypes" property="id" labelProperty="i18nName" />
                    </html:select>--%>
              <html:select property="nodeTypeId" size="1" onchange="">
                          <c:forEach items="${hierarchy.hierarchyNodeTypes}" var="hierarchyNodeType">                          	
                            <html:option value="${hierarchyNodeType.nodeType.id}">                            	
                              <cms:contentText key="${hierarchyNodeType.nodeType.nameCmKey}" code="${hierarchyNodeType.nodeType.cmAssetCode}"/>
                            </html:option>
                          </c:forEach>
                        </html:select>
            </td>
          </tr>

          <tr class="form-blank-row"><td></td></tr>

          <tr class="form-row-spacer">
            <beacon:label property="name" required="true">
              <cms:contentText key="NAME" code="node.list"/>
            </beacon:label>
            <td class="content-field">
              <html:text property="name" size="30" maxlength="255" styleClass="content-field"/>
            </td>
          </tr>

          <tr class="form-blank-row"><td></td></tr>

          <tr class="form-row-spacer">
            <beacon:label property="description" styleClass="top-align">
              <cms:contentText key="DESCRIPTION" code="node.list"/>
            </beacon:label>
            <td class="content-field">
              <html:textarea property="description" rows="5" cols="30" styleClass="content-field"/>
            </td>
          </tr>

          <tr class="form-blank-row"><td></td></tr>

          <html:hidden property="nodeTypeCharacteristicValueListCount"/>
          <c:set var="characteristicType" scope="page" value="nodeType" />
          <c:forEach items="${nodeForm.nodeTypeCharacteristicValueList}" var="valueInfo" varStatus="status">
            <%--
            In order for the same page to use characteristicEntry.jspf for two different characteristic
            types, we need the iterated value to be named both ValueInfo and userCharacteristicValueInfo, so set
            the nodeTypeCharacteristicValueInfo bean to the valueInfo bean. Is there a tag way to do this rather than
            using a scriptlet?
            --%>
            <% pageContext.setAttribute("nodeTypeCharacteristicValueInfo", pageContext.getAttribute("valueInfo")); %>
            <%@ include file="/characteristic/characteristicEntry.jspf"%>

            <c:if test="${!status.last}">
              <tr class="form-blank-row"><td></td></tr>
            </c:if>
          </c:forEach>

            <%-- Buttons --%>
          <tr class="form-buttonrow">
            <td></td>
            <td></td>
            <td align="left">
              <beacon:authorize ifNotGranted="LOGIN_AS">
              <html:submit styleClass="content-buttonstyle">
                <cms:contentText key="SAVE" code="system.button"/>
              </html:submit>
              </beacon:authorize>

              <c:choose>
                <c:when test="${(nodeForm.parentNodeId != null) && (nodeForm.parentNodeId != '') && (nodeForm.parentNodeId != 0)}">
                  <html:cancel styleClass="content-buttonstyle">
                    <cms:contentText key="CANCEL" code="system.button"/>
                  </html:cancel>
                </c:when>
                <c:otherwise>
									<%  Map paramMap = new HashMap();
											NodeForm temp = (NodeForm)request.getAttribute("nodeForm");
											paramMap.put( "id", temp.getHierarchyId() );
											pageContext.setAttribute("cancelUrl", ClientStateUtils.generateEncodedLink( "", "hierarchyDisplay.do?method=display", paramMap ) );
									%>
                  <html:button property="cancelBtn" onclick="callUrl('${cancelUrl}')" styleClass="content-buttonstyle">
                    <cms:contentText key="CANCEL" code="system.button"/>
                  </html:button>
                </c:otherwise>
              </c:choose>
            </td>
          </tr>
        </table>
      </html:form>
    </td>
  </tr>
</table>