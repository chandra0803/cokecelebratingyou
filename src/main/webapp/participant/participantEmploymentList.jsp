<%--UI REFACTORED--%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<script type="text/javascript">
<!--
function maintainPaxEmployment( method, action )
{
	document.participantEmploymentListForm.method.value=method;
	document.participantEmploymentListForm.action = action;
	document.participantEmploymentListForm.submit();
}
//-->
</script>

<html:form styleId="contentForm" action="participantEmploymentListDisplay">
	<html:hidden property="method" value=""/>
	<beacon:client-state>
		<beacon:client-state-entry name="userId" value="${participantEmploymentListForm.userId}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="LIST_HEADER" code="participant.employment.history"/></span>
        <%-- Subheadline --%>
        <br/>
     	  <beacon:username userId="${displayNameUserId}"/>
        <%-- End Subheadline --%>

        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText code="participant.employment.history" key="LIST_INFO" />
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
     	
        <cms:errors/>
          
        <table width="80%">
          <tr>
            <td align="right">
              <display:table defaultsort="1" defaultorder="ascending" name="paxEmploymentList" id="employment" pagesize="20" sort="list" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
              <display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
			</display:setProperty>
			<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
            		<display:column titleKey="participant.participant.EMPLOYER" property="employer.name" headerClass="crud-table-header-row" class="crud-content left-align nowrap"  sortable="true"/>
            		<!-- picklist are changed to normal strings, remove .name from property value  -->	
								<display:column titleKey="participant.participant.JOB_POSITION"  property="positionType" headerClass="crud-table-header-row"    class="crud-content left-align"  sortable="true"/>				
								<display:column titleKey="participant.participant.DEPARTMENT" property="departmentType" headerClass="crud-table-header-row" class="crud-content left-align"  sortable="true"/>
								<display:column titleKey="participant.participant.HIRE_DATE" sortProperty="hireDate"  headerClass="crud-table-header-row"  class="crud-content right-align nowrap"  sortable="true">
 				   				<fmt:formatDate value="${employment.hireDate}" pattern="${JstlDatePattern}" />
 								</display:column>
								<display:column titleKey="participant.participant.TERMINATION_DATE" sortProperty="terminationDate"  headerClass="crud-table-header-row" class="crud-content right-align nowrap"  sortable="true">
				  				<fmt:formatDate value="${employment.terminationDate}" pattern="${JstlDatePattern}" />
								</display:column>
		    			</display:table>
						</td>
          </tr>
			
          <%--BUTTON ROWS --%>
          <beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
          <tr class="form-buttonrow">
            <td>
              <table width="100%">
                <tr>
                  <td align="left">
		             		<c:if test="${!emptyPaxEmploymentList}"> 
		              		<html:button titleKey="participant.employment.history.UPDATE_CURR_BUTTON" property="edit" styleClass="content-buttonstyle"
		      							onclick="maintainPaxEmployment('prepareUpdate','participantEmploymentUpdateDisplay.do');">
		   			  					<cms:contentText key="UPDATE_CURR_BUTTON" code="participant.employment.history"/>
		   			 					</html:button>   			    
		            		</c:if>
             
		            		<html:button titleKey="participant.employment.history.CREATE_NEW_BUTTON" property="new" styleClass="content-buttonstyle"
		              		onclick="maintainPaxEmployment('prepareCreate','participantEmploymentCreateDisplay.do');">
		            			<cms:contentText key="CREATE_NEW_BUTTON" code="participant.employment.history"/>
		           			</html:button>					                                  
           		 		</td>
                </tr>
              </table>
            </td>
          </tr>
          </beacon:authorize>
          <%--  second row of buttons  --%>
          <tr class="form-buttonrow">
            <td>
              <table width="100%">
                <tr>
                  <td align="center">
                    <html:button titleKey="participant.employment.history.BACK_BUTTON" property="edit" styleClass="content-buttonstyle"
		               onclick="maintainPaxEmployment('display','participantDisplay.do');">
        			     <cms:contentText key="BACK_BUTTON" code="participant.employment.history"/>
            		</html:button>
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