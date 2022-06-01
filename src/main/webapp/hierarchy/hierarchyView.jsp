<%-- UI REFACTORED --%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.hierarchy.Hierarchy"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.jenkov.prizetags.tree.itf.ITree" %>
<%@ page import="com.jenkov.prizetags.tree.impl.Tree" %>
<%@ include file="/include/taglib.jspf" %>

<script type="text/javascript">
<!--
function hierarchyList()
{
	document.hierarchyForm.method.value='displayList';	
	document.hierarchyForm.action = "hierarchyListDisplay.do";
	document.hierarchyForm.submit();
}
//-->
</script>

<table border="0" cellpadding="10" cellspacing="0" width="100%">
  <tr>
    <td>
      <span class="headline"><cms:contentText key="TITLE_VIEW" code="hierarchy.hierarchy"/></span>
	<%-- Commenting out to fix in a later release
      &nbsp;&nbsp;&nbsp;&nbsp;<input type="button" class="webhelp-button" onclick="javascript:FMCOpenHelp( 'G3H20', 'HTML Help Window', null, null );" target="_webhelp" value="<cms:contentText code="system.webhelp" key="WEB_HELP"/>">      
	--%>				
      <br/><br/>
      <span class="content-instruction">
        <cms:contentText key="VIEW_INSTRUCTIONAL_COPY" code="hierarchy.hierarchy"/>
      </span>
      <br/><br/>

      <cms:errors/>

      <%-- Hierarchy View --%>
      <table>
        <tr class="form-row-spacer">
          <td class="content-field-label">
            <cms:contentText key="LABEL_HIERARCHY_NAME" code="hierarchy.hierarchy"/>
          </td>
          <td class="content-field-review">
            <cms:contentText code="${hierarchy.cmAssetCode}" key="${hierarchy.nameCmKey}"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <beacon:authorize ifAnyGranted="PROJ_MGR,BI_ADMIN">
							<%  Map paramMap = new HashMap();
									Hierarchy temp = (Hierarchy)request.getAttribute("hierarchy");
									paramMap.put( "id", temp.getId() );
									pageContext.setAttribute("updateUrl", ClientStateUtils.generateEncodedLink( "", "hierarchyDisplay.do?method=prepareUpdate", paramMap ) );
							%>
							<a href="<c:out value="${updateUrl}"/>" class="content-link"><cms:contentText code="system.link" key="EDIT"/></a>
						</beacon:authorize>
          </td>
        </tr>

        <tr class="form-row-spacer">
          <td class="content-field-label">
            <cms:contentText key="LABEL_HIERARCHY_DESC" code="hierarchy.hierarchy"/>
          </td>
          <td class="content-field-review">
            <c:out value="${hierarchy.description}"/>
          </td>
        </tr>

        <tr class="form-row-spacer">
          <td class="content-field-label">
            <cms:contentText key="LABEL_PRIMARY_HIERARCHY" code="hierarchy.hierarchy"/>
          </td>
          <td class="content-field-review">
            <cms:contentText key="${hierarchy.primaryCMKey}" code="hierarchy.primary" />
          </td>
        </tr>

        <tr class="form-row-spacer">
          <td class="content-field-label">
            <cms:contentText key="LABEL_HIERARCHY_STATUS" code="hierarchy.hierarchy"/>
          </td>
          <td class="content-field-review">
            <cms:contentText key="${hierarchy.activeCMKey}" code="hierarchy.status" />
          </td>
        </tr>

        <tr class="form-row-spacer">
          <td class="content-field-label <c:if test="${hierarchyNodeTypeCount > 1}">top-align</c:if >">
            <cms:contentText key="LABEL_NODE_TYPES" code="hierarchy.hierarchy"/>
          </td>
          <td class="content-field-review">
            <c:forEach items="${hierarchy.hierarchyNodeTypes}" var="hierarchyNodeType" varStatus="status">
              <cms:contentText code="${hierarchyNodeType.nodeType.cmAssetCode}" key="${hierarchyNodeType.nodeType.nameCmKey}"/>
              <c:if test="${!status.last}"><br/></c:if>
            </c:forEach>
          </td>
        </tr>

        <tr class="form-row-spacer">
          <c:choose>
            <c:when test="${empty hierarchy.nodesAsList}" >
              <td class="content-field-label middle-align">
                <cms:contentText key="LABEL_NODES" code="hierarchy.hierarchy"/>
              </td>
              <td class="content-field-review">
                <html:form styleId="contentForm" action="/maintainNode"  style="display: inline;">
                  <html:hidden property="method" value="displayCreate"/>
									<beacon:client-state>
										<beacon:client-state-entry name="hierarchyId" value="${hierarchy.id}"/>
									</beacon:client-state>
                  <table>
                    <tr>
                      <td class="content-field-label"><cms:contentText key="LABEL_ASSIGN_ROOT_NODE" code="hierarchy.hierarchy"/></td>
                      <td>
                        <html:select property="nodeTypeId" size="1">
                          <c:forEach items="${hierarchy.hierarchyNodeTypes}" var="hierarchyNodeType">                          	
                            <html:option value="${hierarchyNodeType.nodeType.id}">                            	
                              <cms:contentText key="${hierarchyNodeType.nodeType.nameCmKey}" code="${hierarchyNodeType.nodeType.cmAssetCode}"/>
                            </html:option>
                          </c:forEach>
                        </html:select>
                      </td>
                      <td>
                        <beacon:authorize ifAnyGranted="PROJ_MGR,BI_ADMIN">
													<html:submit styleClass="content-buttonstyle">
                          	<cms:contentText key="GO" code="system.button"/>
                        	</html:submit>
												</beacon:authorize>	
                      </td>
                    </tr>
                  </table>
                </html:form>
              </td>
            </c:when>

            <c:otherwise >
              <td class="content-field-label top-align">
                <cms:contentText key="LABEL_NODES" code="hierarchy.hierarchy"/>
              </td>
              <td class="content-field-review">
                <table cellspacing="0" cellpadding="0" border="0">
                <%  
                	Hierarchy tempr = (Hierarchy)request.getAttribute("hierarchy");
                	
                	Map parameterMap = new HashMap();
                	parameterMap.put("hierarchyId", tempr.getId());
                  	pageContext.setAttribute("viewNodeUrl", ClientStateUtils.generateEncodedLink("", "viewNode.do", parameterMap));
                	
                  	Map paramMapExpand = new HashMap();
                  	paramMapExpand.put("id", tempr.getId());
                  	pageContext.setAttribute("expandUrl", ClientStateUtils.generateEncodedLink("","hierarchyDisplay.do?method=display",paramMapExpand));
                  	
                  	Map paramMapNodeLookup = new HashMap();
                	paramMapNodeLookup.put("hierarchyId", tempr.getId());
                  	pageContext.setAttribute("nodeLookupUrl", ClientStateUtils.generateEncodedLink("","hierarchyDisplay.do?method=prepareNodeLookup",paramMapNodeLookup));
				%>
				<tr>
					<td class="content-field">
						<a href="<c:out value="${nodeLookupUrl}"/>" class="heirarchy-content-link">
							<cms:contentText key="LOOKUP_NODE" code="hierarchy.hierarchy"/>
						</a>
						<br />
					</td>
				</tr>
                  <tree:tree tree="tree.hierarchy" node="tree.node" includeRootNode="true">
                    <tr>
                      <td>
                        <table cellspacing="0" cellpadding="0" border="0">
                          <tr>
                            <td>
                              <tree:nodeIndent node="tree.node" indentationType="type"><tree:nodeIndentVerticalLine indentationType="type" ><img alt="" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/spacer.gif" border="0" width="20" height="9"></tree:nodeIndentVerticalLine><tree:nodeIndentBlankSpace indentationType="type" ><img alt="" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/spacer.gif" border="0" width="20" height="9"></tree:nodeIndentBlankSpace></tree:nodeIndent>
                            </td>
                            <td>
                              <tree:nodeMatch node="tree.node" hasChildren="true" expanded="false" isLastChild="false"><a href="<c:out value="${expandUrl}"/>&expand=<tree:nodeId node="tree.node"/>"><img alt="" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/plus.gif" border="0"></a></tree:nodeMatch>
                              <tree:nodeMatch node="tree.node" hasChildren="true" expanded="true" isLastChild="false"><a href="<c:out value="${expandUrl}"/>&collapse=<tree:nodeId node="tree.node"/>"><img alt="" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/minus.gif" border="0"></a></tree:nodeMatch>
                              <tree:nodeMatch node="tree.node" hasChildren="true" expanded="false" isLastChild="true"><a href="<c:out value="${expandUrl}"/>&expand=<tree:nodeId node="tree.node"/>"><img alt="" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/plus.gif" border="0"></a></tree:nodeMatch>
                              <tree:nodeMatch node="tree.node" hasChildren="true" expanded="true" isLastChild="true"><a href="<c:out value="${expandUrl}"/>&collapse=<tree:nodeId node="tree.node"/>"><img alt="" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/minus.gif" border="0"></a></tree:nodeMatch>
                              <tree:nodeMatch node="tree.node" hasChildren="false" isLastChild="false"><img alt="" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/nonode.gif" border="0"></tree:nodeMatch>
                              <tree:nodeMatch node="tree.node" hasChildren="false" isLastChild="true"><img alt="" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/nonode.gif" border="0"></tree:nodeMatch>
                            </td>
                            <td class="content-field">&nbsp;<a href="<c:out value="${viewNodeUrl}"/>&nodeId=<tree:nodeId node="tree.node"/>" class="heirarchy-content-link"><tree:nodeName node="tree.node"/></a> (<tree:nodeToolTip node="tree.node"/>)</td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                  </tree:tree>
                </table>
              </td>
            </c:otherwise>
          </c:choose>
        </tr>

        <tr class="form-buttonrow">
          <td></td>
          <td align="left">
            <html:form styleId="contentForm" action="/hierarchyListDisplay" >
              <html:hidden property="method" value="displayList"/>
              <html:submit property="back" styleClass="content-buttonstyle">
                <cms:contentText key="BUTTON_BACK_HIERARCHY" code="hierarchy.hierarchy"/>
              </html:submit>
            </html:form>
          </td>
        </tr>
      </table>

    </td>
  </tr>
</table>
