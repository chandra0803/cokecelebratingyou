<%-- UI REFACTORED --%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.hierarchy.Hierarchy"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ include file="/include/taglib.jspf" %>

<script type="text/javascript">
<!--
	function maintainHierarchy( method )
	{
	  document.hierarchyListForm.method.value=method;
	  document.hierarchyListForm.action = "hierarchyDisplay.do";
	  document.hierarchyListForm.submit();
	}
//-->
</script>

<html:form styleId="contentForm" action="hierarchyListMaintain">
  <html:hidden property="method" value=""/>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="TITLE_LIST" code="hierarchy.hierarchy"/></span>
        <span id="quicklink-add"></span><script type="text/javascript">quicklink_display_add('<cms:contentText key="TITLE_LIST" code="hierarchy.hierarchy"/>','<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>'); </script>
	<%-- Commenting out to fix in a later release
		<input type="button" class="webhelp-button" onclick="javascript:FMCOpenHelp( 'G3H13', 'HTML Help Window', null, null );" target="_webhelp" value="<cms:contentText code="system.webhelp" key="WEB_HELP"/>">						
		<input type="button" class="webhelp-button" onclick="javascript:FMCOpenHelp( 'G3H19', 'HTML Help Window', null, null );" target="_webhelp" value="<cms:contentText code="system.webhelp" key="HIERARCHY_SAMPLE"/>">				
	--%>				
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="LIST_INSTRUCTIONAL_COPY" code="hierarchy.hierarchy"/>
        </span>
        <br/><br/>

        <cms:errors/>

        <table width="80%">
          <tr>
            <td align="right">
              <c:set var="rowCount" value="1"/>
							<%  Map paramMap = new HashMap();
									Hierarchy temp;
							%>
              <display:table defaultsort="1" defaultorder="ascending" name="hierarchyList" id="hierarchy" sort="list" requestURI="hierarchyListDisplay.do">
              <display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
				</display:setProperty>
				<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
                <display:column titleKey="hierarchy.hierarchy.COL_NAME" headerClass="crud-table-header-row" class="crud-content nowrap top-align left-align" sortable="true" sortProperty="i18nName">
									<%  temp = (Hierarchy)pageContext.getAttribute("hierarchy");
											paramMap.put( "id", temp.getId() );
											pageContext.setAttribute("viewUrl", ClientStateUtils.generateEncodedLink( "", "hierarchyDisplay.do?method=display", paramMap ) );
									%>
									<a href="<c:out value="${viewUrl}"/>" class="crud-content-link">
										<c:out value="${hierarchy.i18nName}"/>
									</a>
                  <c:if test="${hierarchy.primary==true}">
                    <br/>* <cms:contentText key="LABEL_PRIMARY" code="hierarchy.hierarchy"/>
                  </c:if>
                </display:column>
                <display:column property="description" titleKey="hierarchy.hierarchy.COL_DESC" headerClass="crud-table-header-row" class="crud-content top-align left-align"/>
                <display:column titleKey="hierarchy.hierarchy.COL_STATUS" headerClass="crud-table-header-row" class="crud-content nowrap top-align left-align" sortable="true" sortProperty="active">
                  <cms:contentText key="${hierarchy.activeCMKey}" code="hierarchy.status" />
                </display:column>
                <display:column titleKey="hierarchy.hierarchy.COL_NODE_TYPES" headerClass="crud-table-header-row" class="crud-content nowrap left-align">
                  <table>
                    <c:forEach items="${hierarchy.hierarchyNodeTypes}" var="hierarchyNodeType" varStatus="status">
                      <tr>
                        <td class="crud-content nowrap left-align" width="150">
                          <cms:contentText code="${hierarchyNodeType.nodeType.cmAssetCode}" key="${hierarchyNodeType.nodeType.nameCmKey}"/>
                        </td>
                        <c:choose>
                          <c:when test="${status.first}">
                            <td class="crud-content nowrap">
                              <beacon:authorize ifAnyGranted="PROJ_MGR,BI_ADMIN">
																<% pageContext.setAttribute("viewUrl", ClientStateUtils.generateEncodedLink( "", "hierarchyDisplay.do?method=prepareUpdate", paramMap ) ); %>
																<a href="<c:out value="${viewUrl}"/>" class="crud-content-link">
                                	<cms:contentText key="LINK_ASSIGN_NODE_TYPES" code="hierarchy.hierarchy"/>
                              	</a>
															</beacon:authorize>
                            </td>
                          </c:when>
                          <c:when test="${!status.first}">
                            <td></td>
                          </c:when>
                        </c:choose>
                      </tr>
                    </c:forEach>
                  </table>
                </display:column>
                <beacon:authorize ifAnyGranted="PROJ_MGR,BI_ADMIN">
									<display:column titleKey="hierarchy.hierarchy.COL_REMOVE" headerClass="crud-table-header-row" class="crud-content top-align center-align">
                  	<c:if test="${hierarchy.primary==false}">
                    	<html:checkbox property="delete" value="${hierarchy.id}" />
                  	</c:if>
                	</display:column>
								</beacon:authorize>
                <c:set var="rowCount" value="${rowCount + 1}"/>
              </display:table>
            </td>
          </tr>

          <%--BUTTON ROWS --%>
          <beacon:authorize ifAnyGranted="PROJ_MGR,BI_ADMIN">
						<tr class="form-buttonrow">
            	<td>
              	<table width="100%">
                	<tr>
                  	<td align="left">
                    	<html:button property="Add" styleClass="content-buttonstyle" onclick="maintainHierarchy('prepareCreate');">
                      	<cms:contentText key="BUTTON_ADD_HIERARCHY" code="hierarchy.hierarchy"/>
                    	</html:button>
                  	</td>
                  	<td align="right">
                    	<html:button property="Remove" styleClass="content-buttonstyle" onclick="setDispatchAndSubmit('remove')">
                      	<cms:contentText key="REMOVE_SELECTED" code="system.button"/>
                    	</html:button>
                  	</td>
                	</tr>
              	</table>
            	</td>
          	</tr>
					</beacon:authorize>
        </table>
      </td>
    </tr>
  </table>
</html:form>