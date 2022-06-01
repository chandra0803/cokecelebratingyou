<%--UI REFACTORED--%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.user.UserNode"%>
<%@ page import="com.biperf.core.ui.user.UserNodeForm"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf" %>

<script type="text/javascript">
<!--
function maintainUserNode( method )
{
  document.userNodeListForm.method.value = method;
  document.userNodeListForm.action = "maintainUserNodes.do";
  document.userNodeListForm.submit();
}
//-->
</script>

<html:form styleId="contentForm" action="maintainUserNodes">
  <html:hidden property="method"/>
	<beacon:client-state>
		<beacon:client-state-entry name="userId" value="${userNodeForm.userId}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="LIST_TITLE" code="user.node.list"/></span>            
         <br/>
         <beacon:username userId="${displayNameUserId}"/>
         <br/><br/>
         <span class="content-instruction">
           <cms:contentText key="INFO" code="user.node.list"/>
         </span>
         <br/><br/>

        <cms:errors/>

        <table width="50%">
          <tr>
            <td align="right">
						<%	Map parameterMap = new HashMap();
								UserNode temp;
						%>
            <display:table defaultsort="1" defaultorder="ascending" name="userNodeList" id="userNode" pagesize="20" sort="list" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
            <display:setProperty name="basic.msg.empty_list_row">
					       <tr class="crud-content" align="left"><td colspan="{0}">
                          <cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                        </td></tr>
			 </display:setProperty>   
			 <display:setProperty name="basic.empty.showtable" value="true"></display:setProperty> 
			  <display:column titleKey="user.node.list.PRIMARY_HEADER" headerClass="crud-table-header-row" class="crud-content center-align content-field-label-top" >					
		         <html:radio property="nodeId" value="${userNode.node.id}" />
		      </display:column>        
              <display:column titleKey="user.node.list.NAME" headerClass="crud-table-header-row" class="crud-content left-align nowrap" sortable="true">
                <c:out value="${userNode.node.name}"/>
              </display:column>
              <display:column titleKey="user.node.list.HIERARCHY" headerClass="crud-table-header-row" class="crud-content left-align nowrap" sortable="true">
                <c:out value="${userNode.node.hierarchy.i18nName}"/>
              </display:column>
              <display:column titleKey="user.node.list.STATUS" headerClass="crud-table-header-row" class="crud-content left-align nowrap" sortable="false">
                <c:choose>
                  <c:when test="${userNode.active}">
                    <cms:contentText key="ACTIVE" code="user.node.list"/>
                  </c:when>
                  <c:otherwise>
                    <cms:contentText key="INACTIVE" code="user.node.list"/>
                  </c:otherwise>
                </c:choose>
              </display:column>
              <display:column titleKey="user.node.list.ROLE" headerClass="crud-table-header-row" class="crud-content left-align nowrap" sortable="true" sortProperty="hierarchyRoleType.code">
                <table width="300">
                  <tr>
                    <td align="left" class="crud-content">
                      <c:out value="${userNode.hierarchyRoleType.name}"/>
                    </td>
                    <td align="right" class="crud-content">
											<%	temp = (UserNode)pageContext.getAttribute("userNode");
													parameterMap.put( "userId", temp.getUser().getId() );
													parameterMap.put( "nodeId", temp.getNode().getId() );
													pageContext.setAttribute("maintainUrl", ClientStateUtils.generateEncodedLink( "", "maintainUserNodes.do?method=displayUpdateUserNodeRole", parameterMap ) );
											%>
                      <a class="crud-content-link" href="<c:out value='${maintainUrl}'/>"/>
                        <cms:contentText code="system.button" key="EDIT" />
                      </a>
                    </td>
                  </tr>
                </table>
              </display:column>
              <display:column titleKey="user.node.list.REMOVE" headerClass="crud-table-header-row" class="crud-content center-align">
                 <html:checkbox property="nodeIds" styleClass="content-field" value="${userNode.node.id}" />
              </display:column>
            </display:table>
            </td>
          </tr>

          <%--BUTTON ROWS --%>
          <c:if test="${!empty unassignedNodeList}">
          <tr class="form-buttonrow">
            <td>
              <table width="100%">
                <tr>
                 <td align="left">
                    <html:button property="ChangePrimaryBtn" styleClass="content-buttonstyle" onclick="setDispatchAndSubmit('changePrimary')">
					  <cms:contentText key="CHANGE_PRIMARY_BUTTON" code="user.node.list"/>
				    </html:button>                  
                    <html:button property="Add" styleClass="content-buttonstyle" onclick="setDispatchAndSubmit('displayListToAdd')">
                      <cms:contentText key="BUTTON_ADD_NODE" code="user.node.list"/>
                    </html:button>
                  </td>
                  <td align="right">
                    <html:button property="Remove" styleClass="content-buttonstyle" onclick="setDispatchAndSubmit('removeUserNodes')">
                      <cms:contentText key="REMOVE_SELECTED" code="user.node.list"/>
                    </html:button>
                  </td>
                </tr>
              </table>
            </td>
          </tr>
          </c:if>
          <tr class="form-buttonrow">
            <td>
              <table width="100%">
                <tr>
                  <td align="center">
                    <c:choose>
                      <c:when test="${user.userType.code == 'pax'}">
												<%	UserNodeForm tempForm = (UserNodeForm)request.getAttribute("userNodeForm");
														parameterMap.put( "userId", tempForm.getUserId() );
														parameterMap.remove( "nodeId" );
														pageContext.setAttribute("backUrl", ClientStateUtils.generateEncodedLink( "", "participantDisplay.do?method=display", parameterMap ) );
												%>
                        <html:button onclick="callUrl('${backUrl}')" property="backToOverview" styleClass="content-buttonstyle">
                          <cms:contentText code="participant.security" key="BACK_TO_OVERVIEW"/>
                        </html:button>
                      </c:when>
                      <c:otherwise>
												<%	UserNodeForm tempForm = (UserNodeForm)request.getAttribute("userNodeForm");
														parameterMap.put( "userId", tempForm.getUserId() );
														parameterMap.remove( "nodeId" );
														pageContext.setAttribute("backUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), "/userDisplayInternal.do?method=display", parameterMap ) );
												%>
                        <html:button onclick="callUrl('${url}')" property="backToOverview" styleClass="content-buttonstyle">
                          <cms:contentText code="participant.security" key="BACK_TO_USER_OVERVIEW"/>
                        </html:button>
                      </c:otherwise>
                    </c:choose>
                  </td>
                </tr>
              </table>
            </td>
          </tr>          
        </table>
       </td>
     </tr>
  </table>
</html:form>