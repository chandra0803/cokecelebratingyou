<%--UI REFACTORED--%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.user.UserEmailAddress" %>
<%@ page import="com.biperf.core.ui.user.UserEmailAddressForm" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<script type="text/javascript">
<!--
	function maintainUserEmailAddress( method, phoneType )
	{
		document.userEmailAddressForm.method.value=method;	
		document.userEmailAddressForm.action = "userEmailAddressDisplay.do";
		document.userEmailAddressForm.submit();
	}
//-->
</script>

<html:form styleId="contentForm" action="userEmailAddressListMaintain">
	<html:hidden property="method" value="displayList"/>
	<html:hidden property="fromPaxScreen"/>
	<beacon:client-state>
		<beacon:client-state-entry name="userId" value="${userEmailAddressForm.userId}"/>
	</beacon:client-state>

 <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="TITLE" code="participant.emailaddr.list"/></span>
       <%-- Subheadline --%>
        <br/>
   		<beacon:username userId="${displayNameUserId}"/>
        <%-- End Subheadline --%>
        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="ADD_INFO" code="participant.emailaddr.list"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
     	
        <cms:errors/>
        
        <table width="80%">
          <tr>
            <td align="right">
        			<% 	Map parameterMap = new HashMap();
									UserEmailAddress temp;
									UserEmailAddressForm tempForm = (UserEmailAddressForm)request.getAttribute( "userEmailAddressForm" );
									String size=(String) request.getAttribute( "size" );								
							%>
            	<display:table defaultsort="2" defaultorder="ascending" name="userEmailAddressList" id="userEmailAddress" sort="list" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
            	<display:setProperty name="basic.msg.empty_list_row">
					       <tr class="crud-content" align="left"><td colspan="{0}">
                          <cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                        </td></tr>
				   </display:setProperty>
				   <display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
            		<display:column titleKey="participant.emailaddr.list.PRIMARY" headerClass="crud-table-header-row" class="crud-content center-align">
         					<html:radio property="primary" value="${userEmailAddress.emailType.code}" />
		        		</display:column>
		        		<display:column property="emailType.name" titleKey="participant.emailaddr.list.TYPE" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true"/>
		        		<display:column titleKey="participant.emailaddr.list.EMAILADDR" headerClass="crud-table-header-row" class="crud-content left-align nowrap" sortable="true" sortProperty="emailAddr">
									<%	temp = (UserEmailAddress)pageContext.getAttribute( "userEmailAddress" );
											parameterMap.put( "emailAddrType", temp.getEmailType().getCode() );
											parameterMap.put( "fromPaxScreen", String.valueOf( tempForm.isFromPaxScreen() ) );
											parameterMap.put( "userId", tempForm.getUserId() );
											pageContext.setAttribute("updateUrl", ClientStateUtils.generateEncodedLink( "", "userEmailAddressDisplay.do?method=prepareUpdate", parameterMap ) );
									%>
							<beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
							<c:if test="${currentUserMatchesView or userEmailAddress.emailType.code ne 'rec'}">
									<a href="<c:out value='${updateUrl}'/>" align="right" class="crud-content-link">
							</c:if>
							</beacon:authorize>
							 <c:if test="${userEmailAddress.emailType.code eq 'rec'}">
		        		    <beacon:authorize ifAnyGranted="MODIFY_RECOVERY_CONTACTS">
		        		    	<a href="<c:out value='${updateUrl}'/>" align="right" class="crud-content-link">
		        		    </beacon:authorize>
		        		    </c:if>
		        		    <c:out value="${userEmailAddress.emailAddr}"/>
		        		    
		        		      <beacon:authorize ifAnyGranted="MODIFY_RECOVERY_CONTACTS">
		        		      <c:if test="${userEmailAddress.emailType.code eq 'rec'}">
		        		    	</a>
		        		    	</c:if>
		        		    </beacon:authorize>
		        		    
		        		    <beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
		        		    <c:if test="${currentUserMatchesView or userEmailAddress.emailType.code ne 'rec'}">
		        		            </a>   	
		        		    </c:if>	
		        		    </beacon:authorize>
		        	</display:column>
		            
		            <display:column property="verificationStatus.name" titleKey="participant.emailaddr.list.VERIFICATION" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true"/>
		            
		         	<display:column titleKey="participant.emailaddr.list.REMOVE" headerClass="crud-table-header-row" class="crud-content center-align">


									<c:if test="${userEmailAddress.emailType.code ne 'rec'}">
									    <beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
										    <html:checkbox property="delete" value="${userEmailAddress.emailType.code}" />
										</beacon:authorize>
									</c:if>
									<c:if test="${userEmailAddress.emailType.code eq 'rec'}">
										<beacon:authorize ifAnyGranted="MODIFY_RECOVERY_CONTACTS">
											<html:checkbox property="delete" value="${userEmailAddress.emailType.code}" />
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
                  	<html:button property="Add" styleClass="content-buttonstyle" disabled="true" onclick="maintainUserEmailAddress('prepareCreate','');">
											<cms:contentText key="ADD" code="participant.emailaddr.list"/>
										</html:button>
										 </c:when> 
										 <c:otherwise>
										 		<html:button property="Add" styleClass="content-buttonstyle" onclick="maintainUserEmailAddress('prepareCreate','');">
													<cms:contentText key="ADD" code="participant.emailaddr.list"/>
												</html:button>
										 </c:otherwise>
										 </c:choose>
                  </td>
                  <td align="right">
                   	<html:submit styleClass="content-buttonstyle" onclick="setDispatch('remove')">
											<cms:contentText key="REMOVE_SELECTED" code="system.button"/>
										</html:submit>   
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
