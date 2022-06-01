<%--UI REFACTORED--%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.employer.Employer" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.utils.UserManager"%>
<%@ include file="/include/taglib.jspf"%>

<script type="text/javascript">

function getContentFormName()
{
	return "contentForm";
}

function setActionAndDispatch(myaction, target) 
{
	setActionAndDispatchWithForm(myaction,target,getContentFormName());    
}

function setActionAndDispatchWithForm(myaction, target, formName) 
{
	document.forms[formName].method.value=target;
    document.forms[formName].action=myaction;    
    document.forms[formName].submit();
}

</script>

<html:form styleId="contentForm" action="employerListMaintain">
  <html:hidden property="method" value="displayList"/>
	<beacon:client-state>
		<beacon:client-state-entry name="employerId" value="${employerListForm.employerId}"/>
	</beacon:client-state>
  
   <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="LIST_HEADER" code="employer.list"/></span>
        <span id="quicklink-add"></span><script type="text/javascript">quicklink_display_add('<cms:contentText key="LIST_HEADER" code="employer.list"/>','<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>'); </script>    
        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="LIST_INFO" code="employer.list"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
       
        <cms:errors/>
        
        <table width="50%">
			<tr>
				<td align="right">
					<%-- Notes On display:table tag --%>
		      <%-- if pagination pagesize="20, --%>
		      <%-- If need sorting sort="list" and requestURI="name of .do" --%>
		      <%-- if exporting export="true" --%>
					<%	Map parameterMap = new HashMap();
							Employer temp;
					%>
		      <display:table defaultsort="1" defaultorder="ascending" name="employerList" id="employer" pagesize="20" sort="list" requestURI="employerListDisplay.do">
		      <display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
			 </display:setProperty>	
			 <display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>					
						<display:column titleKey="employer.list.NAME" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true" sortProperty="name" media="html">						
				    	<beacon:authorize ifAnyGranted="PROJ_MGR,BI_ADMIN">
								<%	temp = (Employer)pageContext.getAttribute( "employer" );
										parameterMap.put( "employerId", temp.getId() );
										pageContext.setAttribute("linkUrl", ClientStateUtils.generateEncodedLink( "", "employerDisplay.do?method=prepareUpdate", parameterMap ) );
								%>
								<a href="<c:out value="${linkUrl}"/>" class="crud-content-link">
							</beacon:authorize>
							<string:substring start="0" end="30"><c:out value="${(employer.name)}"/></string:substring>
							<beacon:authorize ifAnyGranted="PROJ_MGR,BI_ADMIN">
					  		</a>
							</beacon:authorize>
				    </display:column>
 				    <display:column titleKey="employer.list.NAME" headerClass="crud-table-header-row" class="crud-content" media="csv excel pdf">
					  	<string:substring start="0" end="30"><c:out value="${(employer.name)}"/></string:substring>
						</display:column>
				    <display:column titleKey="employer.list.STATUS" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true">
				    	<c:if test="${employer.active}">
				      	<cms:contentText key="TRUE" code="employer.list"/>
				      </c:if>
				      <c:if test="${!employer.active}">
				        <cms:contentText key="FALSE" code="employer.list"/>
				      </c:if>		        	
				    </display:column>
		        <display:column titleKey="employer.list.ACTIVE_PARTICIPANTS" class="crud-content right-align" headerClass="crud-table-header-row" sortable="true">
				      <fmt:formatNumber value="${employer.activeParticipantCount}"/>
		    		</display:column> 
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
                		<html:button property="AddBtn" styleClass="content-buttonstyle" onclick="setActionAndDispatch( 'employerDisplay.do', 'prepareCreate')">
											<cms:contentText key="ADD_EMP_BUTTON" code="employer.list"/>
										</html:button>	
              	  </td>
            		</tr>
          	  </table>
        		</td>
      	  </tr>
				</beacon:authorize>	
	</table>
     <%--  END HTML needed with display table --%>
    </td>
  </tr>
  </table>
</html:form>