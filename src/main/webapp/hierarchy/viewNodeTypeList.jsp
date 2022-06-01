<%--UI REFACTORED--%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.hierarchy.NodeType"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ include file="/include/taglib.jspf" %>

<html:form styleId="contentForm" action="nodeTypeListMaintain" method="GET">
  <html:hidden property="method" value="remove"/>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="NODE_TYPE_LIST_HEADER"
                                                code="node_type.labels"/></span>
        <span id="quicklink-add"></span>
        <script>quicklink_display_add('<cms:contentText key="NODE_TYPE_LIST_HEADER" code="node_type.labels"/>','<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>');</script>
	<%-- Commenting out to fix in a later release
	    <input type="button" class="webhelp-button" onclick="javascript:FMCOpenHelp( 'G3H9', 'HTML Help Window', null, null );" target="_webhelp" value="<cms:contentText code="system.webhelp" key="WEB_HELP"/>">								
	--%>				
        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
			<cms:contentText key="NODE_TYPE_LIST_INSTRUCT" code="node_type.labels"/>
    </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
          <cms:errors/>


        <%--  START HTML needed with display table --%>
        <%-- Table --%>
      <table width="50%">
        <tr>
          <td align="right">
						<%  Map paramMap = new HashMap();
								NodeType temp;
						%>
            <display:table name="nodeTypeList" id="nodeType" sort="list" pagesize="20"
                           requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
			<display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
			</display:setProperty>
			<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>                           
              <display:column titleKey="node_type.labels.NAME" headerClass="crud-table-header-row"
                              class="crud-content left-align content-field-label-top" sortable="true"
                              sortProperty="nodeTypeName">
					<%  temp = (NodeType)pageContext.getAttribute("nodeType");
							paramMap.put( "domainId", temp.getId() );
							paramMap.put( "nodeTypeName", temp.getName() );
							pageContext.setAttribute("linkUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), "/hierarchy/characteristicListDisplayNodeType.do", paramMap ) );
					%>
                      <a href="<c:out value="${linkUrl}"/>" class="crud-content-link-bold">
                        <cms:contentText key="${nodeType.nameCmKey}" code="${nodeType.cmAssetCode}"/>
                      </a>
              </display:column>
              
              <display:column headerClass="crud-table-header-row"
                              class="crud-content left-align content-field-label-top" sortable="false">
					<%  Map parameterMap = new HashMap();
							temp = (NodeType)pageContext.getAttribute("nodeType");
							parameterMap.put( "id", temp.getId() );
							pageContext.setAttribute("editUrl", ClientStateUtils.generateEncodedLink( "", "nodeTypeDisplay.do?method=prepareUpdate", parameterMap ) );
					%>
                      <a href="<c:out value="${editUrl}"/>" class="crud-content-link">
                        <cms:contentText key="EDIT_LINK" code="node_type.labels"/>
                      </a>
              </display:column>              

              <display:column titleKey="node_type.labels.CHARACTERISTICS"
                              headerClass="crud-table-header-row" class="crud-content left-align nowrap"
                              sortable="false">
                <% Long ndId = (Long) request.getAttribute( "nodeId" );
                  request.setAttribute( "nodeIdStr", ndId );
                %>
                <c:if test="${empty characteristicMap[nodeType.id]}">
                  <cms:contentText key="NONE_DEFINED" code="node_type.labels" />
                </c:if>
                <c:forEach items="${characteristicMap[nodeType.id]}" var="characteristicName">
                	<c:if test="${characteristicName.active}">
                      <cms:contentText key="${characteristicName.nameCmKey}"
                                       code="${characteristicName.cmAssetCode}"/><br/>
                  	</c:if>
                </c:forEach>
              </display:column>

              <display:column titleKey="node_type.labels.REMOVE" headerClass="crud-table-header-row"
                              class="crud-content center-align" sortable="false">
                <html:checkbox property="delete" value="${nodeType.id}"/>
              </display:column>

            </display:table>
          </td></tr>

        <%--BUTTON ROWS --%>
        <tr class="form-buttonrow">
          <td>
            <table width="100%">
              <tr>
                <td align="left">
                  <html:button property="add" styleClass="content-buttonstyle"
                               onclick="callUrl('nodeTypeDisplay.do?method=prepareCreate')">
                    <cms:contentText key="ADD_NODE_TYPE" code="node_type.labels"/>
                  </html:button>
                </td>
                <td align="right">
                  <html:submit styleClass="content-buttonstyle" onclick="setDispatch('remove')">
                    <cms:contentText code="system.button" key="REMOVE_SELECTED"/>
                  </html:submit>
                </td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
  </table>
</html:form>