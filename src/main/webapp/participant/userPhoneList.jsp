<%--UI REFACTORED--%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<script type="text/javascript">
<!--
function maintainUserPhone( method, phoneType )
{
	document.userPhoneListForm.phoneType.value=phoneType;
	document.userPhoneListForm.method.value=method;	
	document.userPhoneListForm.action = "userPhoneDisplay.do";
	document.userPhoneListForm.submit();
}
//-->
</script>
<% String size=(String) request.getAttribute( "size" );	 %>
<html:form styleId="contentForm" action="userPhoneListMaintain">
	<html:hidden property="method" value="displayList"/>
	<html:hidden property="phoneType"/>
	<html:hidden property="fromPaxScreen"/>
	<beacon:client-state>
		<beacon:client-state-entry name="userId" value="${userPhoneListForm.userId}"/>
	</beacon:client-state>

	<table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="TITLE" code="participant.phone.list"/></span>
        <%-- Subheadline --%>
        <br/>
   		<beacon:username userId="${displayNameUserId}"/>
        <%-- End Subheadline --%>
        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="ADD_INFO" code="participant.phone.list"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
     	
        <cms:errors/>
        
         <table width="80%">
          <tr>
            <td align="right">
            
            	<display:table defaultsort="2" defaultorder="ascending" name="phoneList" id="userPhone" sort="list" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
            	<display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
				</display:setProperty>
				<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
            		<display:column titleKey="participant.phone.list.PRIMARY_HEADER" headerClass="crud-table-header-row" class="crud-content center-align">
         				<html:radio property="primary" value="${userPhone.phoneType.code}" />
		        	</display:column>
		        	
		        	
		        	<display:column property="phoneType.name" titleKey="participant.phone.list.PHONE_TYPE_HEADER" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true"/>
		        	
		        	<display:column titleKey="participant.participant.COUNTRY" property="countryPhoneCode" headerClass="crud-table-header-row" class="crud-content left-align"/>
		   
		        	<display:column titleKey="participant.phone.list.PHONE_NUMBER_HEADER" headerClass="crud-table-header-row" class="crud-content left-align nowrap" sortable="true" sortProperty="phoneNbr">
		        	  <beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
		        	  <c:if test="${currentUserMatchesView or userPhone.phoneType.code ne 'rec'}">
		        	    <a href="javascript:maintainUserPhone('prepareUpdate', '<c:out value="${userPhone.phoneType.code}"/>');" class="crud-content-link" align="right">
		        	  </c:if>
		        	  </beacon:authorize>
		        	  <c:if test="${currentUserMatchesView or userPhone.phoneType.code eq 'rec'}">
		        		    
		        		   <beacon:authorize ifAnyGranted="MODIFY_RECOVERY_CONTACTS">
		        		    	<a href="javascript:maintainUserPhone('prepareUpdate', '<c:out value="${userPhone.phoneType.code}"/>');" class="crud-content-link" align="right">
		        		    </beacon:authorize>
		        	  </c:if>
		        	    <c:out value="${userPhone.phoneNbr}"/>
		        	    <beacon:authorize ifAnyGranted="MODIFY_RECOVERY_CONTACTS">
		        	      <c:if test="${userPhone.phoneType.code eq 'rec'}">
		        		    	</a>
		        		  </c:if>
		        		</beacon:authorize>
		        		    
		        	  <beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
		        	  <c:if test="${currentUserMatchesView or userPhone.phoneType.code ne 'rec'}">
		        	    </a>
		        	  </c:if>
		        	  </beacon:authorize>
		        	</display:column>
		        
		        	<display:column titleKey="participant.phone.list.PHONE_EXT_HEADER" property="phoneExt" headerClass="crud-table-header-row" class="crud-content left-align"/>
		   
		            <display:column property="verificationStatus.name" titleKey="participant.phone.list.VERIFICATION" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true"/>
		   
		         	<display:column titleKey="participant.phone.list.REMOVE_HEADER" headerClass="crud-table-header-row" class="crud-content center-align">
		         	  <c:if test="${currentUserMatchesView or userPhone.phoneType.code ne 'rec'}">
		         	  <beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
		            	<html:checkbox property="delete" value="${userPhone.phoneType.code}" />
		              </beacon:authorize>
		              </c:if>
		               <c:if test="${currentUserMatchesView or userPhone.phoneType.code eq 'rec'}">
		        		     <beacon:authorize ifAnyGranted="MODIFY_RECOVERY_CONTACTS">
		        		    <html:checkbox property="delete" value="${userPhone.phoneType.code}" />
		        		    </beacon:authorize>
		        		    </c:if>
		        	</display:column>		        
		    	</display:table>
		   </td>
		 </tr> 
		 
		    <%--BUTTON ROWS --%>
		  <beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN,MODIFY_RECOVERY_CONTACTS">
          <tr class="form-buttonrow">
            <td>
              <table width="100%">
              	<tr>
              		<td align="left">
              		<beacon:authorize ifNotGranted="LOGIN_AS">
                    <beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
                  	<html:submit styleClass="content-buttonstyle" onclick="setDispatch('changePrimary')">
											<cms:contentText code="system.button" key="CHANGE_PRIMARY" />
										</html:submit>&nbsp;&nbsp;
					</beacon:authorize>
                    </beacon:authorize>
										<c:choose>
                   <c:when test="${size==5}">  
										<html:button property="Add" styleClass="content-buttonstyle" disabled="true" onclick="maintainUserPhone('prepareCreate','');">
											<cms:contentText key="ADD_PHONE_BUTTON" code="participant.phone.list"/>
				  					</html:button>
				  					</c:when>
				  					<c:otherwise>
				  					<html:button property="Add" styleClass="content-buttonstyle" onclick="maintainUserPhone('prepareCreate','');">
											<cms:contentText key="ADD_PHONE_BUTTON" code="participant.phone.list"/>
				  					</html:button>
				  					</c:otherwise>
				  					</c:choose>				  					
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
          
		  		<tr>
            <td>
              <table width="100%">
              	<tr>
              		<td align="center">
										<html:cancel styleClass="content-buttonstyle">
											<cms:contentText code="system.button" key="BACK_TO_OVERVIEW" />
										</html:cancel>
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