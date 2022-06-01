<%--UI REFACTORED--%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<html:form styleId="contentForm" action="participantDisplay">
	<html:hidden property="method" value="display"/>
	<beacon:client-state>
		<beacon:client-state-entry name="userId" value="${requestScope.displayNameUserId}"/>
	</beacon:client-state>
	<table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="TITLE" code="node.assignment.history"/></span>
        <%-- Subheadline --%>
        <br/>
   		<beacon:username userId="${displayNameUserId}"/>
        <%-- End Subheadline --%>
        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="ADD_INFO" code="node.assignment.history"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
     	
        <cms:errors/>
        
         <table width="90%">
          <tr>
            <td align="right">
            
            	<display:table defaultsort="1" defaultorder="ascending" name="nodeList" id="userNode" pagesize="20" sort="list" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
            	
            	<display:setProperty name="basic.msg.empty_list_row">
					       <tr class="crud-content" align="left"><td colspan="{0}">
                          <cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                        </td></tr>
				   </display:setProperty>
				   <display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
		        	<display:column titleKey="node.assignment.history.NODE" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true">
		        	 	<c:out value="${userNode.node.name}"/>
	            	</display:column>
		        	
		        	<display:column titleKey="node.assignment.history.HIERARCHY" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true">
		        		<c:out value="${userNode.node.hierarchy.i18nName}"/>
	            	</display:column>
		        	
		        	<display:column titleKey="node.assignment.history.ROLE" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true">
		        		<c:out value="${userNode.hierarchyRoleType.name}"/>
	            	</display:column>
		        	
		        	<display:column titleKey="node.assignment.history.STATUS" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true">
		        		<c:choose>
		                  <c:when test="${userNode.active}">
		                    <cms:contentText key="ACTIVE" code="user.node.list"/>
		                  </c:when>
		                  <c:otherwise>
		                    <cms:contentText key="INACTIVE" code="user.node.list"/>
		                  </c:otherwise>
                		</c:choose>
	            	</display:column>
		        	
		        	<display:column titleKey="node.assignment.history.DATE_UPDATE" headerClass="crud-table-header-row" class="crud-content center-align" sortable="true">
		        		<fmt:formatDate value="${userNode.auditCreateInfo.dateCreated}" pattern="${JstlDatePattern}" />
	            	</display:column>
		       
		    	</display:table>
		   </td>
		 </tr> 
          <tr class="form-buttonrow">
            <td>
              <table width="100%">
                <tr>
                  <td align="center">
										<html:submit styleClass="content-buttonstyle" property="back_button">
											<cms:contentText key="BACK_BUTTON" code="node.assignment.history"/>
										</html:submit>
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