<%-- UI REFACTORED --%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.hierarchy.Node"%>
<%@ page import="com.biperf.core.ui.node.NodeSearchForm"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<%  NodeSearchForm temp = (NodeSearchForm)request.getAttribute("nodeSearchForm");
		Map parameterMap = new HashMap();
		parameterMap.put( "nodeId", temp.getNodeId() );
		parameterMap.put( "nodeName", temp.getNameOfNode() );
		parameterMap.put( "nodeSearchType", temp.getNodeSearchType() );
		for(Iterator iter = temp.getParamsMap().keySet().iterator(); iter.hasNext();)
		{
			String key = (String)iter.next();
			parameterMap.put( key, temp.getParamsMap().get( key ) );
		}
		pageContext.setAttribute("cancelUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), temp.getReturnActionUrl(), parameterMap ) );
%>
<script type="text/javascript">
	function doCancel()
	{
		var url = "<c:out value='${cancelUrl}'/>";
		url = url.replace(/&amp;/g, "&");
    window.location = url;
	}
</script>

<html:form styleId="contentForm" action="nodeLookup" >
  <html:hidden property="method"/>
  <html:hidden property="returnActionUrl"/>
  <html:hidden property="nodeSearchType"/>
  <html:hidden property="hierarchyListDisabled"/>

  <c:forEach items='${nodeSearchForm.paramsMap}' var='item'>
    <html:hidden property="params(${item.key})"/>
  </c:forEach>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="TITLE" code="hierarchy.node.lookup"/></span>
	<%-- Commenting out to fix in a later release
        &nbsp;&nbsp;&nbsp;&nbsp;<input type="button" class="webhelp-button" onclick="javascript:FMCOpenHelp( 'G3H15', 'HTML Help Window', null, null );" target="_webhelp" value="<cms:contentText code="system.webhelp" key="WEB_HELP"/>">              
	--%>				
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="INSTRUCTIONAL_COPY" code="hierarchy.node.lookup"/>
        </span>
        <br/><br/>

        <cms:errors/>

        <table width="50%">
          <tr class="form-row-spacer">
            <beacon:label property="hierarchyId">
              <cms:contentText key="HIERARCHY" code="hierarchy.node.lookup"/>
            </beacon:label>
            <td class="content-field">
              <c:choose>
                <c:when test="${nodeSearchForm.hierarchyListDisabled}">
									<beacon:client-state>
										<beacon:client-state-entry name="hierarchyId" value="${nodeSearchForm.hierarchyId}"/>
									</beacon:client-state>
                  <c:forEach items="${hierarchies}" var="hierarchy">
                    <c:if test="${nodeSearchForm.hierarchyId == hierarchy.id}">
                      <cms:contentText key="${hierarchy.nameCmKey}" code="${hierarchy.cmAssetCode}"/>
                    </c:if>
                  </c:forEach>
                </c:when>
                <c:otherwise>
                  <html:select property="hierarchyId" styleClass="content-field">
                    <c:forEach items="${hierarchies}" var="hierarchy">
                      <html:option value="${hierarchy.id}"><cms:contentText key="${hierarchy.nameCmKey}" code="${hierarchy.cmAssetCode}"/></html:option>
                    </c:forEach>
                  </html:select>
                </c:otherwise>
              </c:choose>
            </td>
          </tr>

          <tr class="form-blank-row"><td colspan="3"></td></tr>

          <tr class="form-row-spacer">
            <beacon:label property="nameOfNode">
              <cms:contentText key="NODENAME" code="hierarchy.node.lookup"/>
            </beacon:label>
            <td class="content-field">
              <html:text property="nameOfNode" styleClass="content-field" size="50" maxlength="50"/>
            </td>
          </tr>

          <tr class="form-blank-row"><td colspan="3"></td></tr>

          <tr class="form-row-spacer">
            <beacon:label property="nodeTypeId">
              <cms:contentText key="NODETYPE" code="hierarchy.node.lookup"/>
            </beacon:label>
            <td class="content-field">
              <html:select property="nodeTypeId" styleClass="content-field">
                <c:forEach items="${nodeTypes}" var="nodeType">
                  <html:option value="${nodeType.id}"><cms:contentText key="${nodeType.nameCmKey}" code="${nodeType.cmAssetCode}"/></html:option>
                </c:forEach>
              </html:select>
            </td>
          </tr>

          <tr class="form-buttonrow">
            <td></td>
            <td></td>
            <td align="left">
              <html:submit styleClass="content-buttonstyle" onclick="setDispatch('search')">
                <cms:contentText key="SEARCH" code="system.button"/>
              </html:submit>
              <html:button property="cancelButtonProperty" styleClass="content-buttonstyle" onclick="doCancel();">
                <cms:contentText key="CANCEL" code="system.button"/>
              </html:button>
            </td>
          </tr>

          <tr>
            <td align="right" colspan="3">
              <display:table defaultsort="1" defaultorder="ascending" name="nodeList" id="node" pagesize="100" sort="list" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
              <display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
				</display:setProperty>
				<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
                <display:column titleKey="hierarchy.node.lookup.NODENAME" headerClass="crud-table-header-row" class="crud-content" sortable="true" sortProperty="name">
									<%  Map paramMap = new HashMap();
											Node tempNode = (Node)pageContext.getAttribute("node");
											paramMap.put( "nodeId", tempNode.getId() );
											paramMap.put( "nodeName", tempNode.getName() );
											paramMap.put( "nodeSearchType", temp.getNodeSearchType() );
											for(Iterator iterator = temp.getParamsMap().keySet().iterator(); iterator.hasNext();)
											{
												String key = (String)iterator.next();
												paramMap.put( key, temp.getParamsMap().get( key ) );
											}
											pageContext.setAttribute("linkUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), temp.getReturnActionUrl(), paramMap ) );
									%>
                  <a href="<c:out value="${linkUrl}"/>"><c:out value="${node.name}"/></a>
                </display:column>
                <display:column property="description" titleKey="hierarchy.node.lookup.NODE_DESCRIPTION" headerClass="crud-table-header-row" class="crud-content" sortable="true"/>
              </display:table>
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
</html:form>