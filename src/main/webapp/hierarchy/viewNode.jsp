<%-- UI REFACTORED --%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.hierarchy.Node"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ include file="/include/taglib.jspf"%>

<table border="0" cellpadding="10" cellspacing="0" width="100%">
  <tr>
    <td>
      <span class="headline"><cms:contentText key="TITLE" code="node.view"/></span>
	<%-- Commenting out to fix in a later release
	  &nbsp;&nbsp;&nbsp;&nbsp;<input type="button" class="webhelp-button" onclick="javascript:FMCOpenHelp( 'G3H21', 'HTML Help Window', null, null );" target="_webhelp" value="<cms:contentText code="system.webhelp" key="WEB_HELP"/>">      
	--%>				
      <br/>
      <span class="subheadline"><c:out value="${node.name}"/></span>
      <br/><br/>
      <span class="content-instruction">
        <cms:contentText key="INSTRUCTIONAL_COPY" code="node.view"/>
      </span>
      <br/><br/>

      <cms:errors/>

      <%-- Node View --%>
      <table width="100%">
        <tr>
          <td valign="top" width="49%">
            <table>
              <tr class="form-row-spacer">
                <td class="content-field-label top-align">
                  <cms:contentText key="HIERARCHY_NAME" code="node.view"/>
                </td>
                <td class="content-field-review">
                  <cms:contentText key="${node.hierarchy.nameCmKey}" code="${node.hierarchy.cmAssetCode}"/>
                  <c:if test="${node.hierarchy.primary == true}">
                    <span class="content-field"><br/>* <cms:contentText key="LABEL_PRIMARY" code="node.view"/></span>
                  </c:if>
                </td>
              </tr>

              <c:if test="${node.parentNode != null}">
                <tr class="form-row-spacer">
                  <td class="content-field-label">
                    <cms:contentText key="PARENT" code="node.view"/>
                  </td>
                  <td class="content-field-review">
                    <c:out value="${node.parentNode.name}"/>
                    <c:set var="parentNodeType" value="${node.parentNode.nodeType}"/>
                    &nbsp;(<cms:contentText key="${parentNodeType.nameCmKey}" code="${parentNodeType.cmAssetCode}"/>)
                  </td>
                </tr>
              </c:if>

              <tr class="form-row-spacer">
                <td class="content-field-label">
                  <cms:contentText key="TYPE" code="node.view"/>
                </td>
                <td class="content-field-review">
                  <cms:contentText key="${node.nodeType.nameCmKey}" code="${node.nodeType.cmAssetCode}"/>
                </td>
              </tr>

              <tr class="form-row-spacer">
                <td class="content-field-label <c:if test="${childNodeCount > 0}">top-align</c:if >">
                  <cms:contentText key="CHILDREN" code="node.view"/>
                </td>
                <td class="content-field-review">
                  <table width="100%">
                    <c:forEach items="${childNodeList}" var="childNode">
                       <tr>
                         <td class="content-field">
                           <c:out value="${childNode.name}"/>&nbsp;(<cms:contentText key="${childNode.nodeType.nameCmKey}" code="${childNode.nodeType.cmAssetCode}"/>)
                         </td>
                       </tr>
                    </c:forEach>
                    <tr>
                      <td>
                        <html:form styleId="contentForm" action="/viewCreateNode"  style="display: inline;">
                          <html:hidden property="method" value=""/>
													<beacon:client-state>
	 													<beacon:client-state-entry name="parentNodeId" value="${node.id}"/>
	 													<beacon:client-state-entry name="hierarchyId" value="${node.hierarchy.id}"/>
													</beacon:client-state>
                          <beacon:authorize ifAnyGranted="PROJ_MGR,BI_ADMIN">
                            <span class="content-field"><cms:contentText key="ADD_CHILD_PREFIX" code="node.view"/></span>
                            <html:select property="nodeTypeId">
                              <c:forEach items="${nodeTypeList}" var="nodeType">
                                <html:option value="${nodeType.nodeType.id}">
                                  <cms:contentText key="${nodeType.nodeType.nameCmKey}" code="${nodeType.nodeType.cmAssetCode}"/>
                                </html:option>
                              </c:forEach>
                            </html:select>&nbsp;
                            <span class="content-field"><cms:contentText key="ADD_CHILD_SUFFIX" code="node.view"/>&nbsp;</span>
							<html:submit styleClass="content-buttonstyle" onclick="setDispatch('displayCreate')"><cms:contentText key="GO" code="system.button"/></html:submit>
						  </beacon:authorize>
                        </html:form>
                      </td>
                    </tr>
                  </table>
                </td>
              </tr>

              <tr class="form-row-spacer">
                <td class="content-field-label">
                  <cms:contentText key="PAX" code="node.view"/>
                </td>
                <td class="content-field-review">
                  <c:out value="${nodePaxCount}"/>&nbsp;&nbsp;&nbsp;&nbsp;
									<%  Map paramMap = new HashMap();
											Node temp = (Node)request.getAttribute("node");
											paramMap.put( "nodeId", temp.getId() );
											pageContext.setAttribute("viewPaxListUrl", ClientStateUtils.generateEncodedLink( "", "nodeParticipantListDisplay.do", paramMap ) );
									%>
                  <a href="<c:out value="${viewPaxListUrl}"/>" class="content-link"><cms:contentText key="VIEW_PAX_LIST" code="node.view"/></a>
                  <c:if test="${nodePaxCount != 0}">
                    &nbsp;&nbsp;&nbsp;&nbsp;
										<% pageContext.setAttribute("reassignPaxUrl", ClientStateUtils.generateEncodedLink( "", "nodeParticipantReassignDisplay.do?method=prepareUpdateReassignParticipants", paramMap ) ); %>
                    					<beacon:authorize ifAnyGranted="PROJ_MGR,BI_ADMIN">
											<a href="<c:out value="${reassignPaxUrl}"/>" class="content-link"><cms:contentText key="REASSIGN_PAX" code="node.view"/></a>
										</beacon:authorize>
                  </c:if>
                </td>
              </tr>
            </table>
          </td>

          <td width="2%"></td>

          <td valign="top" width="49%">
            <table>
              <tr class="form-row-spacer">
                <td class="content-field-label">
                  <cms:contentText key="DESC" code="node.view"/>
                </td>
                <td class="content-field-review">
                  <c:out value="${node.description}"/>
                </td>
              </tr>

              <c:forEach items="${node.nodeCharacteristics}" var="nodeCharacteristic">
	              <c:if test="${nodeCharacteristic.nodeTypeCharacteristicType.active == true}">
	                <tr class="form-row-spacer">
						<td class="content-field-label content-field-label-top">
	                  	 <c:out value="${nodeCharacteristic.nodeTypeCharacteristicType.characteristicName}"/>
	                 	 </td>
	                
						<td class="content-field-review content-field-label-top" colspan="2">
							<c:choose>
								<c:when test="${nodeCharacteristic.nodeTypeCharacteristicType.characteristicDataType.code == 'single_select' ||
								                nodeCharacteristic.nodeTypeCharacteristicType.characteristicDataType.code == 'multi_select'}">
									<c:forEach items="${nodeCharacteristic.characteristicDisplayValueList}" var="dynaPickListType">
							    	  <c:out value="${dynaPickListType.name}"/>
							    	  <br/>
								    </c:forEach>
								</c:when>
								<c:otherwise>
									<c:out value="${nodeCharacteristic.characteristicValue}"/>&nbsp;
								</c:otherwise>
							</c:choose>
		                </td>
					</tr>                
	               </c:if>
              </c:forEach>
            </table>
          </td>
        </tr>
       </table>

        <%-- Buttons --%>
      <table width="100%">
        <tr class="form-buttonrow">
          <td align="center">
            <beacon:authorize ifAnyGranted="PROJ_MGR,BI_ADMIN">
							<%  pageContext.setAttribute("deleteUrl", ClientStateUtils.generateEncodedLink( "", "maintainNode.do?method=displayDelete", paramMap ) );
									paramMap.put( "hierarchyId", temp.getHierarchy().getId() );
									paramMap.put( "nodeTypeId", temp.getNodeType().getId() );
									if( temp.getParentNode() != null && temp.getParentNode().getId() != null )
									{
										paramMap.put( "parentNodeId", temp.getParentNode().getId() );
									}
									pageContext.setAttribute("editUrl", ClientStateUtils.generateEncodedLink( "", "viewUpdateNode.do?method=displayUpdate", paramMap ) );
							%>
            	<button class="content-buttonstyle" onclick="callUrl('<c:out value="${editUrl}"/>')">
             	 	<cms:contentText key="EDIT_NODE_BUTTON" code="node.view"/>
            	</button>
            	&nbsp;
            	<button class="content-buttonstyle" onclick="callUrl('<c:out value="${deleteUrl}"/>')">
            	  <cms:contentText key="DELETE_NODE" code="node.view"/>
            	</button>
            	&nbsp;
						</beacon:authorize>
						<%  Map parameterMap = new HashMap();
								parameterMap.put( "id", temp.getHierarchy().getId() );
								pageContext.setAttribute("backUrl", ClientStateUtils.generateEncodedLink( "", "hierarchyDisplay.do?method=display", parameterMap ) );
						%>
            <button class="content-buttonstyle" onclick="callUrl('<c:out value="${backUrl}"/>')">
              <cms:contentText key="BACK_HIERARCHY" code="node.view"/>
            </button>
          
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>