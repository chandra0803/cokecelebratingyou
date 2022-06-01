<%--UI REFACTORED--%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.user.User"%>
<%@ page import="com.biperf.core.domain.user.UserAcl"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf" %>

<html:form styleId="contentForm" action="displayUserSecurity" >
	<html:hidden property="method"/>
	<beacon:client-state>
		<beacon:client-state-entry name="userId" value="${userSecurityForm.userId}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText code="participant.security" key="HEADING"/></span>
        <%-- Subheadline --%>
        <br/>
   		<%@ include file="/user/userName.jspf" %>
        <%-- End Subheadline --%>

        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText code="participant.security" key="INSTRUCTION" />
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
     	
        <cms:errors/>
        <table>
        	<tr>
        		<td>
		        <table width="50%">
		          <tr>
		            <td align="right">
		              <display:table defaultsort="1" defaultorder="ascending" name="user.userRoles" id="userRole" pagesize="20" sort="list" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
		              	<display:setProperty name="basic.msg.empty_list_row">
					       <tr class="crud-content" align="left"><td colspan="{0}">
                          <cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                        </td></tr>
				        </display:setProperty>
				        <display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
						<display:column titleKey="admin.role.ROLE_NAME" headerClass="crud-table-header-row" class="crud-content left-align nowrap" sortable="true">
							<c:out value="${userRole.role.name}"/>
						</display:column>
						<display:column titleKey="admin.role.ROLE_CODE" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true">				
							<c:out value="${userRole.role.code}"/>
						</display:column>
					  </display:table>
					</td>
		          </tr>
		          <%--BUTTON ROWS --%>
		          <tr class="form-buttonrow">
		            <td>
		              <table width="100%">
		                <tr>
		                  <td align="left">
							<%	Map parameterMap = new HashMap();
								User temp = (User)request.getAttribute("user");
								parameterMap.put( "userId", temp.getId() );
								pageContext.setAttribute("assignRoleUrl", ClientStateUtils.generateEncodedLink( "", "../participant/displayCreateUserRole.do?method=displayCreateUserRole", parameterMap ) );
								pageContext.setAttribute("clearDashboardUrl", ClientStateUtils.generateEncodedLink( "", "../participant/clearDashboardAction.do?method=clearDashboard", parameterMap ) );
							%>
							<html:submit property="createUserRole" styleClass="content-buttonstyle" onclick="setActionAndSubmit('${assignRoleUrl}')">
								<cms:contentText key="ASSIGN_ROLE" code="participant.security"/>
							</html:submit>
		                  </td>
		                  <c:if test="${showClearDashboard}">
		                  <td align="right">
							<html:submit styleClass="content-buttonstyle" onclick="setActionAndSubmit('${clearDashboardUrl}')">
								<cms:contentText key="CLEAR_DASHBOARD" code="participant.security"/>
							</html:submit>
		                  </td>
		                  </c:if>
		                </tr>
		              </table>
		            </td>
		          </tr>
		        </table>
        	</td>
        </tr>	  
		<tr class="form-blank-row">
            <td></td>
        </tr>	  
		
		<tr>
          <td>
			<table width="50%">
	          <tr>
	            <td align="right">
								<%	UserAcl tempAcl; %>
	              <display:table defaultsort="1" defaultorder="ascending" name="user.userAcls" id="userAcl" pagesize="20" sort="list" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
	              <display:setProperty name="basic.msg.empty_list_row">
					       <tr class="crud-content" align="left"><td colspan="{0}">
                          <cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                        </td></tr>
				   </display:setProperty>
				   <display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
					<display:column titleKey="admin.acl.ACL_NAME" headerClass="crud-table-header-row" class="crud-content left-align nowrap" sortable="true">
						<%	tempAcl = (UserAcl)pageContext.getAttribute( "userAcl" );
								parameterMap.put( "guid", tempAcl.getGuid() );
								pageContext.setAttribute("aclUrl", ClientStateUtils.generateEncodedLink( "", "displayCreateUserAcl.do?method=displayUpdateUserAcl", parameterMap ) );
						%>
						<a class="crud-content-link" href="<c:out value="${aclUrl}"/>">
							<c:out value="${userAcl.acl.name}"/>
						</a>
					</display:column>
					<display:column titleKey="admin.acl.ACL_CODE" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true">
						<c:out value="${userAcl.acl.code}"/>
					</display:column>
					<display:column titleKey="admin.user.acl.ACL_TARGET" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true">
						<c:out value="${userAcl.target}"/>
					</display:column>
					<display:column titleKey="admin.user.acl.ACL_PERMISSION" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true">
						<c:out value="${userAcl.permission}"/>
					</display:column>
					<display:column titleKey="participant.security.REMOVE_ACL" headerClass="crud-table-header-row" class="crud-content center-align">
						<html:checkbox property="removeUserAcls" value="${userAcl.acl.id}||${userAcl.target}||${userAcl.permission}||${userAcl.guid}"/>
					</display:column>
				</display:table>
				</td>
	          </tr>
	          <%--BUTTON ROWS --%>
	          <tr class="form-buttonrow">
	            <td>
	              <table width="100%">
	                <tr>
	                  <td align="left">
											<%	parameterMap.remove( "guid" );
													pageContext.setAttribute("paxAclUrl", ClientStateUtils.generateEncodedLink( "", "../participant/displayCreateUserAcl.do?userTypeCode=pax", parameterMap ) );
											%>
											<html:submit property="createUserAcl" styleClass="content-buttonstyle" onclick="setActionAndDispatch('${paxAclUrl}','displayCreateUserAcl')">
												<cms:contentText code="participant.security" key="CREATE_USER_ACL"/>
											</html:submit>
	                  </td>
	                  <td align="right">
						<c:if test="${userSecurityForm.userAclListSize > 0}">
							<html:submit styleClass="content-buttonstyle">
								<cms:contentText code="participant.security" key="REMOVE_USER_ACL"/>
							</html:submit>
						</c:if>
	                  </td>
	                </tr>
	              </table>
	            </td>
	          </tr>
          </table>
          </td>
          </tr>
               
           <tr class="form-buttonrow">
            <td>
              <table width="100%">
                <tr>
                  <td align="center">
					<c:choose>
						<c:when test="${user.userType.code == 'pax'}">
							<html:submit styleClass="content-buttonstyle" onclick="setActionAndDispatch('participantDisplay.do','display')">
						 		<cms:contentText key="BACK_TO_OVERVIEW" code="participant.security"/>
						 	</html:submit>			
						</c:when>
						<c:otherwise>
						 	<html:submit styleClass="content-buttonstyle" onclick="setActionAndDispatch('../userDisplayInternal.do','display')">
						 		<cms:contentText key="BACK_TO_OVERVIEW" code="participant.security"/>
						 	</html:submit>					
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