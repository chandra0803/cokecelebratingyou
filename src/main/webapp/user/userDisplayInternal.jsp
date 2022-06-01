<%--UI REFACTORED--%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.ui.user.UserForm" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<script type="text/javascript">
<!--
function callUrl( urlToCall )
{
	window.location=urlToCall;
}

//-->
</script>

<html:form styleId="contentForm" action="userDisplayInternal">
	<html:hidden property="method"/>
	<beacon:client-state>
		<beacon:client-state-entry name="userId" value="${userForm.userId}"/>
	</beacon:client-state>

 <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="TITLE_USER_OVERVIEW" code="user.user"/></span>
        
        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="ADD_INFO" code="user.user"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
     	
        <cms:errors/>
        
        <table width="100%" cellpadding="3" cellspacing="1">
		  <tr>
			<td width="49%" valign="top">
				<table cellpadding="3" cellspacing="1" >							
					<%-- START Personal Information --%>
					<tr>
						<td class="subheadline" colspan="3">
							<cms:contentText key="PERSONAL_INFO_HEADER" code="user.user"/>
							&nbsp;&nbsp;
							<a href="javascript: setActionDispatchAndSubmit('userDisplayInternal.do','prepareUpdatePersonalInfo');" class="content-link">
								<cms:contentText key="UPDATE" code="user.user"/>
							</a>
						</td>
					</tr>
					
					<tr>
						<td class="content-field-label"></td>
						<td class="content-field-label">
							<cms:contentText key="USER_STATUS" code="user.user"/>
						</td>
						<td class="content-field-review">
							<c:out value="${userForm.activeDesc}" />
						</td>
					</tr>
					
					<tr>
						<td class="content-field-label"></td>
						<td class="content-field-label">
							<cms:contentText key="USER_TYPE" code="user.user"/>
						</td>
						<td class="content-field-review">
							<c:out value="${userForm.userTypeDesc}" />
						</td>
					</tr>
					
					<tr>
						<td class="content-field-label"></td>
						<td class="content-field-label">
							<cms:contentText key="NAME" code="user.user"/>
						</td>
						<td class="content-field-review">
							<c:if test="${userForm.titleDesc != null && userForm.titleDesc != '' }"><c:out value="${userForm.titleDesc}" /></c:if>
							<c:if test="${userForm.firstName != null && userForm.firstName != '' }"><c:out value="${userForm.firstName}" /></c:if>
							<c:if test="${userForm.middleName != null && userForm.middleName != '' }"><c:out value="${userForm.middleName}" /></c:if>
							<c:if test="${userForm.lastName != null && userForm.lastName != '' }"><c:out value="${userForm.lastName}" /></c:if>
							<c:if test="${userForm.suffixDesc != null && userForm.suffixDesc != '' }"><c:out value="${userForm.suffixDesc}" /></c:if>
						</td>
					</tr>
					
					<tr>
						<td class="content-field-label"></td>
						<td class="content-field-label">
							<cms:contentText key="ENROLLMENT_DATE" code="user.user"/>
						</td>
						<td class="content-field-review">
							<c:out value="${userForm.enrollmentDate}" />
						</td>
					</tr>
					
					<tr>
						<td class="content-field-label"></td>
						<td class="content-field-label">
							<cms:contentText key="ENROLLMENT_SOURCE" code="user.user"/>
						</td>
						<td class="content-field-review">
							<c:out value="${userForm.enrollmentSourceDesc}" />
						</td>
					</tr>
					<tr class="form-blank-row">
            			<td></td>
          			</tr>
          			
          			<tr>
						<td class="content-field-label"></td>
						<td class="content-field-label">
							<cms:contentText key="PRIMARY_EMAIL_TYPE" code="user.user"/>
						</td>
						<td class="content-field-review" nowrap>
							<c:out value="${userForm.emailTypeDesc}" />
							&nbsp;&nbsp;&nbsp;
							<a href="javascript: setActionDispatchAndSubmit('userEmailAddressListDisplay.do','displayList');" class="content-link"><cms:contentText key="MORE_EMAIL_ADDRESSES" code="user.user"/></a>

						</td>
					</tr>
					<tr>
						<td class="content-field-label"></td>
						<td class="content-field-label">
							<cms:contentText key="EMAIL_ADDRESS" code="user.user"/>
						</td>
						<td class="content-field-review">
							<c:out value="${userForm.emailAddress}" />
						</td>
					</tr>
					
					<tr class="form-blank-row">
            			<td></td>
          			</tr>
					
					<tr>
						<td class="content-field-label"></td>
						<td class="content-field-label">
							<cms:contentText key="PRIMARY_PHONE_TYPE" code="user.user"/>
						</td>
						<td class="content-field-review">
							<c:out value="${userForm.phoneTypeDesc}" />
								&nbsp;&nbsp;&nbsp;
							<a href="javascript: setActionDispatchAndSubmit('<%=RequestUtils.getBaseURI(request)%>/participant/userPhoneListDisplay.do','displayList');" class="content-link"><cms:contentText key="MORE_PHONE" code="user.user"/></a>
	
						</td>
					</tr>
					<tr>
						<td class="content-field-label"></td>
						<td class="content-field-label">
							<cms:contentText key="PHONE_NUMBER" code="user.user"/>
						</td>
						<td class="content-field-review">
						<c:out value="${userForm.countryPhoneCode}" />
							<c:out value="${userForm.phoneNumber}" />
							<c:out value="${userForm.phoneExtension}" />
						</td>
					</tr>
					
					<%-- END Personal Information --%>
				</table> <%-- End table 1st column --%>
			</td>
			<td width="2%" valign="top">&nbsp;</td>
			<td width="49%" valign="top">
				<table cellpadding="3" cellspacing="1" >	
					<%-- START Web Site Information --%>
					<tr>
						<td class="subheadline" colspan="3" nowrap>
							<cms:contentText key="WEB_SITE_INFO_HEADER" code="user.user"/>
							&nbsp;&nbsp;
							<a href="javascript: setActionDispatchAndSubmit('<%=RequestUtils.getBaseURI(request)%>/userDisplay.do','prepareUpdateLoginInfo');" class="content-link">
								<cms:contentText key="UPDATE" code="user.user"/>
							</a>						
						</td>
						<td>
							<html:button disabled="${isAccountLocked}" styleClass="content-buttonstyle" property="unlock_button" onclick="setActionDispatchAndSubmit('userDisplay.do','unlockAccount')">
								<cms:contentText code="user.user" key="UNLOCK_ACCOUNT" />
							</html:button>
						</td>
					</tr>
					
					<tr>
						<td class="content-field-label"></td>
						<td class="content-field-label">
							<cms:contentText key="LOGIN_ID" code="user.user"/>
						</td>
						<td class="content-field-review">
							<c:out value="${userForm.userName}" />
						</td>
			
					</tr>
					
				  <c:if test="${accountJustUnlocked}">
					<tr>
						<td class="content-field-label" colspan="3"></td>
						<td class="content-field-label">
							<cms:contentText code="user.profile" key="ACCOUNT_UNLOCKED" />
						</td>
					</tr>
				  </c:if>
				  <c:if test="${accountLocked}">
	  			    <tr>
						<td class="content-field-label" colspan="3"></td>
						<td class="content-field-label content-error">
							<cms:contentText code="user.profile" key="ACCOUNT_CURRENTLY_LOCKED" />
						</td>
					</tr>							
				  </c:if>
				  
				  <tr class="form-row-spacer" >
							<beacon:label property="welcomeEmail" required="false">
			              		<cms:contentText key="WELCOME_EMAIL" code="user.user"/>
			            	</beacon:label>	
						<td class="content-field" >
							<html:button property="SendWelcomeEmail" styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('userDisplayInternal.do','SendNewWelcomeEmail');">
						   		<cms:contentText code="system.button" key="SEND_WELCOME_EMAIL" />
						 	</html:button>
						</td>
					</tr>	
				   <%-- END Web Site Information --%>
					<tr class="form-blank-row">
            		  <td></td>
          			</tr>	
          			<tr>
						<td class="subheadline" colspan="3">
							<cms:contentText key="ROLE_HEADER" code="user.user"/>
							&nbsp;&nbsp;&nbsp;
							<%	Map parameterMap = new HashMap();
									UserForm temp = (UserForm)request.getAttribute( "userForm" );
									parameterMap.put( "userId", temp.getUserId() );
									pageContext.setAttribute("updateUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), "/participant/displayCreateUserRole.do", parameterMap ) );
							%>
							<a href="<c:out value="${updateUrl}"/>" class="crud-content-link">
								<cms:contentText key="UPDATE" code="user.user"/>
							</a>												
						</td>
					</tr>
					<tr>
						<td class="content-field-label"></td>						
						<td class="content-field-review" colspan="3">
							<c:forEach items="${userForm.userRoles}" var="userRole">
								<c:out value="${userRole.role.name}"/><br>
							</c:forEach>
						</td>
					</tr>
					<%-- END Role Information --%>
          		
				   </table>
				  </td>
				</tr>
				<tr class="form-buttonrow">
			        <td colspan="3" align="center">	 
						 <html:button property="backToPromo" styleClass="content-buttonstyle" onclick="callUrl('userList.do')" >
		            		<cms:contentText code="user.user" key="BACK_TO_LIST" />
		          		 </html:button>
			        </td>
		       </tr>   						
			</table>
		 </td>
     </tr>        
   </table>
</html:form>	