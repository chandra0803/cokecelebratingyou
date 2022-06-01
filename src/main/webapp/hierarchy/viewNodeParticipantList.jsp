<%-- UI REFACTORED --%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.hierarchy.Node"%>
<%@ page import="com.biperf.core.domain.user.User"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<html:form styleId="contentForm" action="nodeParticipantListMaintain" >
  <html:hidden property="method" value="" />
	<beacon:client-state>
		<beacon:client-state-entry name="nodeId" value="${nodeParticipantListForm.nodeId}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="TITLE" code="hierarchy.node.participantlist"/></span>
	<%-- Commenting out to fix in a later release
 		&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" class="webhelp-button" onclick="javascript:FMCOpenHelp( 'G3H17', 'HTML Help Window', null, null );" target="_webhelp" value="<cms:contentText code="system.webhelp" key="WEB_HELP"/>">
	--%>				
        <br/>
        <span class="subheadline"><c:out value="${node.name}"/></span>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="INSTRUCTIONAL_COPY" code="hierarchy.node.participantlist"/>
        </span>
        <br/><br/>

        <cms:errors/>

        <%-- Node Participants Form --%>
        <table>
          <tr class="form-row-spacer">
            <td>
              <table>
                <tr>
                  <td class="content-field-label top-align">
                    <cms:contentText key="HIERARCHY_NAME_LABEL" code="hierarchy.node.participantlist"/>
                  </td>
                  <td class="content-field-review">
                    <cms:contentText code="${hierarchy.cmAssetCode}" key="${hierarchy.nameCmKey}"/>
                    <c:if test="${hierarchy.primary == true}">
                      <span class="content-field"><br/>* <cms:contentText key="PRIMARY_HIERARCHY_LABEL" code="hierarchy.node.participantlist"/></span>
                    </c:if>
                  </td>
                </tr>
              </table>
            </td>
          </tr>

          <tr class="form-blank-row"><td></td></tr>

			<beacon:authorize ifAnyGranted="PROJ_MGR,BI_ADMIN">
			 <tr>
            	<td class="content-field">
              		<cms:contentText key="NODE_SEARCH_LABEL" code="hierarchy.node.participantlist"/>&nbsp;
              		<html:text property="lastName" maxlength="25" size="20" styleClass="content-field"/>
              		<html:submit onclick="setDispatch('search')" styleClass="content-buttonstyle">
               			<cms:contentText key="SEARCH_FOR_PAX_BUTTON" code="hierarchy.node.participantlist"/>
              		</html:submit>
            	</td>
          	 </tr>
			</beacon:authorize>

          <tr class="form-blank-row"><td></td></tr>

          <tr>
            <td align="right" valign="top">
					<%  Map paramMap = new HashMap();
						User tempUser;
					%>
              <display:table defaultsort="1" defaultorder="ascending" name="nodeParticipantList" id="nodeParticipant" pagesize="20" sort="list" requestURI="<%= RequestUtils.getOriginalRequestURI(request) %>" export="true">
              <display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
				</display:setProperty>
				<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
                <display:column titleKey="hierarchy.node.participantlist.PARTICIPANT_NAME_LABEL" headerClass="crud-table-header-row" class="crud-content nowrap" sortable="true" sortProperty="nameLFMNoComma">
                  <c:out value="${nodeParticipant.lastName}"/>, <c:out value="${nodeParticipant.firstName}"/>
                </display:column>
                <display:column titleKey="hierarchy.node.participantlist.ROLE_NAME_LABEL" headerClass="crud-table-header-row" class="crud-content nowrap" sortable="true" media="html">
                  <table width="100%">
                    <tr>
                      <td class="content-field" style="width: 80px;">
                        <c:out value="${nodeParticpantRoles[nodeParticipant.id].name}"/>
                      </td>
                      <td>
						<%  tempUser = (User)pageContext.getAttribute("nodeParticipant");
							Node temp = (Node)request.getAttribute("node");
							paramMap.put( "nodeId", temp.getId() );
							paramMap.put( "userId", tempUser.getId() );
							paramMap.put( "userName", tempUser.getUserName() );
							pageContext.setAttribute("editUserRoleUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), "/participant/maintainUserNodes.do?method=displayUpdateUserNodeRole&returnActionMapping=view_paxnode_list", paramMap ) );
						%>
                        <beacon:authorize ifAnyGranted="PROJ_MGR,BI_ADMIN">
							<a href="<c:out value='${editUserRoleUrl}'/>" class="content-link"><cms:contentText key="EDIT" code="system.link"/></a>
						</beacon:authorize>
                      </td>
                    </tr>
                  </table>
                </display:column>
                <display:column titleKey="hierarchy.node.participantlist.ROLE_NAME_LABEL" headerClass="crud-table-header-row" class="crud-content nowrap" media="csv excel pdf">
                  <c:out value="${nodeParticpantRoles[nodeParticipant.id].name}"/>
                </display:column>
                <beacon:authorize ifAnyGranted="PROJ_MGR,BI_ADMIN">
					<display:column titleKey="hierarchy.node.participantlist.REMOVE_LABEL" headerClass="crud-table-header-row" class="crud-content center-align" media="html">
                  		<html:checkbox property="delete" value="${nodeParticipant.id}" />
                	</display:column>
				</beacon:authorize>
              </display:table>
            </td>
          </tr>

          <tr class="form-buttonrow">
            <td>
              <table width="100%">
                <tr>
                  <beacon:authorize ifAnyGranted="PROJ_MGR,BI_ADMIN">
					<td align="left">
						<%  Map parameterMap3 = new HashMap();
							Node tempr3 = (Node)request.getAttribute("node");
							parameterMap3.put("nodeId" , tempr3.getId());
							pageContext.setAttribute("returnUrl", ClientStateUtils.generateEncodedLink( "", "/participant/maintainUserNodes.do?method=displayCreateUserNodeRole&returnActionMapping=view_paxnode_list", parameterMap3 ) );
						%>
                    	<c:url var="url" value="/participant/listBuilderPaxDisplay.do">
                      		<c:param name="audienceMembersLookupReturnUrl" value="${returnUrl}" />
                    	</c:url>
                    	<html:button onclick="callUrl('${url}')" property="addAParticipant" styleClass="content-buttonstyle">
                      		<cms:contentText key="ADD_A_PAX_LABEL" code="hierarchy.node.participantlist"/>
                    	</html:button>
                  	</td>
				  </beacon:authorize>
                  <td align="center">
						<% Map parameterMap = new HashMap();
						   Node tempr = (Node)request.getAttribute("node");
						   parameterMap.put("nodeId" , tempr.getId());
						   pageContext.setAttribute("backToNodeViewUrl", ClientStateUtils.generateEncodedLink( "", "viewNode.do", parameterMap ) ); 
						%>
                    <html:button onclick="callUrl('${backToNodeViewUrl}')" property="backToNodeDetails" styleClass="content-buttonstyle">
                      <cms:contentText key="BACK_TO_NODE_LABEL" code="hierarchy.node.participantlist"/>
                    </html:button>
                  </td>
				  <beacon:authorize ifAnyGranted="PROJ_MGR,BI_ADMIN">
                  	<td align="right">
                    	<html:submit styleClass="content-buttonstyle" onclick="setDispatch('remove')">
                      	<cms:contentText key="REMOVE_PAX_LABEL" code="hierarchy.node.participantlist"/>
                    	</html:submit>
                  	</td>
				  </beacon:authorize>
                </tr>
              </table>
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
</html:form>

<script>
    /* Default focus on the Go button so pressing the Enter key works as clicking the Go button */
	setFocusOnInput(2);
</script>
