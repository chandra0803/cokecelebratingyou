<%--UI REFACTORED--%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.ui.user.UserForm"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<html:form styleId="contentForm" action="participantMaintainCreate">
	<html:hidden property="method" value=""/>
	<beacon:client-state>
		<beacon:client-state-entry name="userId" value="${userForm.userId}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="TITLE_OVERVIEW" code="participant.participant"/></span>
	<%-- Commenting out to fix in a later release
        &nbsp;&nbsp;&nbsp;&nbsp;<input type="button" class="webhelp-button" onclick="javascript:FMCOpenHelp( 'G3H23', 'HTML Help Window', null, null );" target="_webhelp" value="<cms:contentText code="system.webhelp" key="WEB_HELP"/>">
	--%>				
        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="NOTE" code="participant.participant"/>
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
							<cms:contentText key="PERSONAL_HEADER" code="participant.participant"/>
							&nbsp;&nbsp;
							<beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
								<a href="javascript: setActionDispatchAndSubmit('participantDisplay.do','prepareUpdatePersonalInfo');" class="content-link">
							  	<cms:contentText key="UPDATE" code="participant.participant"/>
								</a>
							</beacon:authorize>
							<beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
								<html:button  styleClass="content-buttonstyle" property="unlock_button" onclick="setActionDispatchAndSubmit('participantDisplay.do','index')">
									<cms:contentText code="participant.participant" key="INDEX" />
								</html:button>
							</beacon:authorize>
						</td>
					</tr>
					
					<tr>
						<td class="content-field-label"></td>
						<td class="content-field-label">
							<cms:contentText key="USER_ID" code="participant.participant"/>
						</td>
						<td class="content-field-review">
							<c:out value="${userForm.userId}" />
						</td>
					</tr>
										
					<tr>
						<td class="content-field-label"></td>
						<td class="content-field-label">
							<cms:contentText key="PARTICIPANT_STATUS" code="participant.participant"/>
						</td>
						<td class="content-field-review">
							<c:out value="${userForm.paxStatusDesc}" />
						</td>
					</tr>
					   
					<tr>
						<td class="content-field-label"></td>
						<td class="content-field-label">
							<cms:contentText key="ACTIVATION_COMPLETE" code="participant.participant"/>
						</td>
						<td class="content-field-review">						
							<c:out value="${userForm.activationCompleteDesc}" />				
						</td>
					</tr>
				
					
					<tr>
						<td class="content-field-label"></td>
						<td class="content-field-label">
							<cms:contentText key="SUSPENSION_STATUS" code="participant.participant"/>
						</td>
						<td class="content-field-review">
							<c:out value="${userForm.suspensionStatusDesc}" />
						</td>
					</tr>

				<c:if test="${allowOptOutAwards }">   
					<tr>
						<td class="content-field-label"></td>
						<td class="content-field-label">
							<cms:contentText key="OPT_OUT_AWARDS" code="participant.participant"/>
						</td>
						<td class="content-field-review">						
							<c:out value="${userForm.optOutAwardsStatusDesc}" />				
						</td>
					</tr>
				</c:if>

 				<c:if test="${allowOptOutOfProgram }">   
 					<tr>
 						<td class="content-field-label"></td>
 						<td class="content-field-label">
 							<cms:contentText key="OPT_OUT_PROGRAM" code="participant.participant"/>
 						</td>
 						<td class="content-field-review">						
 							<c:out value="${userForm.optOutOfProgramStatusDesc}" />				
 						</td>
 					</tr>
 				</c:if>	

					<%-- Terms & Conditions Acceptance --%>
					<tr>
						<td class="content-field-label"></td>
						<td class="content-field content-field-label-top">
							<cms:contentText key="PARTICIPANT_TERMS_ACCEPT" code="participant.participant"/>
						</td>
						<c:choose>
							<c:when test="${userForm.termsConditionsRequired}">		
								<td class="content-field-review">
									<c:out value="${userForm.paxTermsAcceptDesc}" />
									<c:if test="${userForm.paxTermsAccept != 'notaccepted' }">
										<cms:contentText key="BY" code="forum.library" />
										<c:out value="${userForm.termsAcceptedBy}" />
										<br>
										<c:out value="${userForm.termsAcceptedDate}" />
									</c:if>
								</td>
								<c:if test="${userForm.userAllowedToAcceptForPax && userForm.paxTermsAcceptDesc != 'accepted' }">
									<beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
										<td>
											<html:button styleClass="content-buttonstyle" property="accept_button" onclick="setActionDispatchAndSubmit('participantDisplay.do','acceptTermsAndConditions')">
												<cms:contentText code="participant.participant" key="ACCEPT_TERMS_CONDITIONS" />
											</html:button>
										</td>
									</beacon:authorize>
								</c:if>
							</c:when>
			  				<c:otherwise>
			  					<td class="content-field-review">
									 <cms:contentText key="NOT_APPLICABLE" code="participant.participant"/>
								</td>
			  				</c:otherwise>
			  			</c:choose>
					</tr>
					
					<tr>
						<td class="content-field-label"></td>
						<td class="content-field-label">
							<cms:contentText key="WELCOME_EMAIL_RECIEVED" code="participant.participant"/>
						</td>
						<td class="content-field-review">
						<c:choose>
						  <c:when test="${userForm.welcomeEmailSent == 'true'}" >
						      <cms:contentText code="system.common.labels" key="YES" />
						  </c:when>
						  <c:otherwise>
						     <cms:contentText code="system.common.labels" key="NO" />
						  </c:otherwise>							
						</c:choose>
						</td>
					 </tr>	  
			
					<tr>
						<td class="content-field-label"></td>
						<td class="content-field-label">
							<cms:contentText key="NAME" code="participant.participant"/>
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
							<cms:contentText key="SSN" code="participant.participant"/>
						</td>
						<td class="content-field-review">
							<c:out value="${userForm.maskedSsn}" />
						</td>
					</tr>		
											
					<tr>
						<td class="content-field-label"></td>
						<td class="content-field-label">
							<cms:contentText key="DATE_OF_BIRTH" code="participant.participant"/>
						</td>
						<td class="content-field-review">
							<c:out value="${userForm.dateOfBirthDisplayString}" />
						</td>
					</tr>				
											
					<tr>
						<td class="content-field-label"></td>
						<td class="content-field-label">
							<cms:contentText key="GENDER" code="participant.participant"/>
						</td>
						<td class="content-field-review">
							<c:out value="${userForm.genderDesc}" />
						</td>
					</tr>		
					
					<c:if test="${userForm.showSSOId}">
	          			<tr>
							<td class="content-field-label"></td>
							<td class="content-field-label">
				              <cms:contentText key="SSO_ID" code="participant.participant"/>  
				            </td>
				            <td class="content-field-review"> 
								<c:out value="${userForm.ssoId}" />
				            </td>
				          </tr>
			        </c:if>		
											
					<tr>
						<td class="content-field-label"></td>
						<td class="content-field-label">
							<cms:contentText key="ENROLLMENT_DATE" code="participant.participant"/>
						</td>
						<td class="content-field-review">
							<c:out value="${userForm.enrollmentDate}" />
						</td>
					</tr>				
					
					<tr>
						<td class="content-field-label"></td>
						<td class="content-field-label">
							<cms:contentText key="ENROLLMENT_SOURCE" code="participant.participant"/>
						</td>
						<td class="content-field-review">
							<c:out value="${userForm.enrollmentSourceDesc}" />
						</td>
					</tr>											
					<%-- End Personal Information --%>
					
					<tr class="form-blank-row">
            			<td></td>
          			</tr>
          			<tr class="form-blank-row">
            			<td></td>
          			</tr>
          				 		
					<%-- START Address Information --%>
					<tr>
						<td class="subheadline" colspan="3">
							<cms:contentText key="ADDRESS_HEADER" code="participant.participant"/>
							&nbsp;&nbsp;
							<beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN,VIEW_PARTICIPANTS">
								<a href="javascript: setActionDispatchAndSubmit('userAddressListDisplay.do','displayList');" class="content-link">
									<cms:contentText key="MORE_ADDRESSES" code="participant.participant"/>
								</a>
							</beacon:authorize>
						</td>
					</tr>
					
					<tr>
						<td class="content-field-label"></td>
						<td class="content-field-label">
							<cms:contentText key="PRIMARY_ADDRESS_TYPE" code="participant.participant"/>
						</td>
						<td class="content-field-review">
							<c:out value="${userForm.addressTypeDesc}" />
						</td>
					</tr>	

					
					<tr>
						<td class="content-field-label"></td>
						<td class="content-field-label">
							<cms:contentText key="ADDRESS_HEADER" code="participant.participant"/>
						</td>
						<td class="content-field-review">
							<c:out value="${userForm.addressFormBean.countryName}" />
						</td>
					</tr>	
					<tr>
						<td class="content-field-label"></td>
						<td class="content-field-label"></td>
						<td class="content-field-review">
							<c:out value="${userForm.addressFormBean.addr1}" />
						</td>
					</tr>										
							
					<c:if test="${userForm.addressFormBean.addr2 != null && userForm.addressFormBean.addr2 != '' }">
						<tr>
							<td class="content-field-label"></td>
							<td class="content-field-label"></td>
							<td class="content-field-review">
								<c:out value="${userForm.addressFormBean.addr2}" />
							</td>
						</tr>									
					</c:if>
					<c:if test="${userForm.addressFormBean.addr3 != null && userForm.addressFormBean.addr3 != '' }">
						<tr>
							<td class="content-field-label"></td>
							<td class="content-field-label"></td>
							<td class="content-field-review">
								<c:out value="${userForm.addressFormBean.addr3}" />
							</td>
						</tr>	
					</c:if>
							
						<tr>
							<td class="content-field-label"></td>
							<td class="content-field-label"></td>
							<td class="content-field-review">
								<c:out value="${userForm.addressFormBean.city}" />,&nbsp;
								<c:out value="${userForm.addressFormBean.stateTypeDesc}" />&nbsp;
								<c:out value="${userForm.addressFormBean.postalCode}" />
							</td>
						</tr>							
							

					<%-- End Address Information --%>
					
					<tr class="form-blank-row">
            			<td></td>
          			</tr>
          			<tr class="form-blank-row">
            			<td></td>
          			</tr>
          			
					<%-- Start Email Address Information --%>
					<tr>
						<td class="subheadline" colspan="3">
							<cms:contentText key="EMAIL_HEADER" code="participant.participant"/>
							&nbsp;&nbsp;
							<beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN,VIEW_PARTICIPANTS">
								<a href="javascript: setActionDispatchAndSubmit('participantDisplay.do','prepareEmailAddressesDisplay');" class="content-link">
									<cms:contentText key="MORE_EMAIL_ADDRESSES" code="participant.participant"/>
								</a>
							</beacon:authorize>	
						</td>
					</tr>
										
					<tr>
						<td class="content-field-label"></td>
						<td class="content-field-label">
							<cms:contentText key="PRIMARY_EMAIL_TYPE" code="participant.participant"/>
						</td>
						<td class="content-field-review">
							<c:out value="${userForm.emailTypeDesc}" />
						</td>
					</tr>	
					
					<tr>
						<td class="content-field-label"></td>
						<td class="content-field-label">
							<cms:contentText key="EMAIL_ADDRESS" code="participant.participant"/>
						</td>
						<td class="content-field-review">
							<c:out value="${userForm.emailAddress}" />
						</td>
					</tr>															
					<%-- End Email Address Information --%>
					
					<tr class="form-blank-row">
            			<td></td>
          			</tr>
          			<tr class="form-blank-row">
            			<td></td>
          			</tr>
					<%-- Start Phone Information --%>
					<tr>
						<td class="subheadline" colspan="3">
							<cms:contentText key="PHONE_HEADER" code="participant.participant"/>
							&nbsp;&nbsp;
							<beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN,VIEW_PARTICIPANTS">
								<a href="javascript: setActionDispatchAndSubmit('participantDisplay.do','preparePhoneListDisplay');" class="content-link">
							  	<cms:contentText key="MORE_PHONE" code="participant.participant"/>
								</a>
							</beacon:authorize>
						</td>
					</tr>
					
					<tr>
						<td class="content-field-label"></td>
						<td class="content-field-label">
							<cms:contentText key="PRIMARY_PHONE_TYPE" code="participant.participant"/>
						</td>
						<td class="content-field-review">
							<c:out value="${userForm.phoneTypeDesc}" />
						</td>
					</tr>
							
					<tr>
						<td class="content-field-label"></td>
						<td class="content-field-label">
							<cms:contentText key="PHONE_NUMBER" code="participant.participant"/>
						</td>
						<td class="content-field-review">
							<c:out value="${userForm.displayCountryPhoneCode}" />
							<c:out value="${userForm.phoneNumber}" />
							<c:out value="${userForm.phoneExtension}" />
						</td>
					</tr>			
					
					<%-- End Phone Information --%>
				</table> <%-- End table 1st column --%>
			</td>
			<td width="2%" valign="top">&nbsp;</td>
			<td width="49%" valign="top">
				<table cellpadding="3" cellspacing="1" >	
					<%-- START Web Site Information --%>
					<tr>
						<td class="subheadline" colspan="3">
							<cms:contentText key="WEB_SITE_HEADER" code="participant.participant"/>
							&nbsp;&nbsp;
							<beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
								<a href="javascript: setActionDispatchAndSubmit('<%=RequestUtils.getBaseURI(request)%>/userDisplay.do','prepareUpdateLoginInfo');" class="content-link"><cms:contentText key="UPDATE" code="participant.participant"/></a>
							</beacon:authorize>
						</td>
						<td>
              <beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN,LAUNCH_AS_PAX">
  							<html:button styleClass="content-buttonstyle" 
                property="launch_site"
                onclick="setActionDispatchAndSubmit('loginAs.do','loginAs')">
  								<cms:contentText code="participant.participant" key="LAUNCH_SITE" />
  							</html:button>
              </beacon:authorize> 
              <beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN,LAUNCH_AS_PAX">
  							<html:button styleClass="content-buttonstyle" 
                property="launch_site"
                onclick="setActionDispatchAndSubmit('loginAsCmsDebug.do','loginAs')">
  								<cms:contentText code="participant.participant" key="LAUNCH_SITE_CMS" />
  							</html:button>
              </beacon:authorize>
						</td>
					</tr>
					<tr>
						<td class="content-field-label"></td>
						<td class="content-field-label">
							<cms:contentText key="LOGIN_ID" code="participant.participant"/>
						</td>
						<td class="content-field-review">
							<c:out value="${userForm.userName}" />
						</td>
						<beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN,UNLOCK_LOGIN">
							<td>
								<html:button disabled="${isAccountLocked}" styleClass="content-buttonstyle" property="unlock_button" onclick="setActionDispatchAndSubmit('participantDisplay.do','unlockAccount')">
									<cms:contentText code="participant.participant" key="UNLOCK_ACCOUNT" />
								</html:button>
							</td>
						</beacon:authorize>
					</tr>
											
				  <c:if test="${accountJustUnlocked}">
					<tr>
						<td class="content-field-label" colspan="3"></td>
						<td class="content-field-label">
							<cms:contentText code="user.profile" key="ACCOUNT_UNLOCKED" />
						</td>
					</tr>
				  </c:if>
				  <c:if test="${not empty accountLockedInfo}">
	  			    <tr>
						<td class="content-field-label" colspan="3"></td>
						<td class="content-field-label content-error">
							<c:out value="${accountLockedMessage}" />
						</td>
					</tr>							
				  </c:if>
				 									
					<%-- END Web Site Information --%>
					<tr class="form-blank-row">
            		  <td></td>
          			</tr>
          			<tr class="form-blank-row">
            		  <td></td>
          			</tr>	 	        
							
					<%-- START Hierarchy Information --%>
					<tr>
						<td class="subheadline" colspan="4">
							<cms:contentText key="HIERARCHY_HEADER" code="participant.participant"/>
							&nbsp;&nbsp;
							<%  Map parameterMap = new HashMap();
									UserForm temp = (UserForm)request.getAttribute("userForm");
									parameterMap.put( "userId", temp.getUserId() );
									pageContext.setAttribute("linkUrl", ClientStateUtils.generateEncodedLink( "", "../participant/maintainUserNodes.do?method=displayList", parameterMap ) );
							%>
							<beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
								<a href="<c:out value='${linkUrl}'/>" class="content-link"><cms:contentText key="UPDATE" code="participant.participant"/></a>
								&nbsp;&nbsp;
							</beacon:authorize>
							<% pageContext.setAttribute("historyUrl", ClientStateUtils.generateEncodedLink( "", "../participant/userNodeHistoryDisplay.do", parameterMap) ); %>
							<a href="<c:out value='${historyUrl}'/>" class="content-link">
							  <cms:contentText key="HISTORY" code="participant.participant"/>
							</a>
						</td>
					</tr>
					
				  <c:forEach items="${userForm.userNodes}" var="userNode">					
					<tr>
						<td class="content-field-label"></td>
						<td class="content-field-review" colspan="3">
							<c:out value="${userNode.hierarchyRoleType.name}" />&nbsp;-&nbsp;<c:out value="${userNode.node.name}" />
							<c:if test="${userNode.isPrimary}">
							   &nbsp;-&nbsp;<cms:contentText key="PRIMARY" code="participant.participant"/>
							</c:if>
						</td>
					</tr>															
				  </c:forEach>
					<%-- END Hierarchy Information --%>
							
					<tr class="form-blank-row">
            		  <td></td>
          			</tr>
          			<tr class="form-blank-row">
            		  <td></td>
          			</tr>							
          			
					<%-- START Characteristic Information --%>
					<tr>
						<td class="subheadline" colspan="3">
							<cms:contentText key="CHARACTERISTIC_HEADER" code="participant.participant"/>
							&nbsp;&nbsp;
							<beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
								<% pageContext.setAttribute("characteristicsUrl", ClientStateUtils.generateEncodedLink( "", "../userCharacteristicEdit.do", parameterMap) ); %>
								<a href="<c:out value="${characteristicsUrl}"/>" class="content-link">
							  	<cms:contentText key="UPDATE" code="participant.participant"/>
								</a>
							</beacon:authorize>
						</td>
					</tr>
				<c:forEach items="${userForm.userCharacteristics}" var="userChar">
				
					<tr>
						<td class="content-field-label"></td>
						<td class="content-field-label content-field-label-top">
							<c:out value="${userChar.userCharacteristicType.characteristicName}"/>
						</td>
						<td class="content-field-review content-field-label-top" colspan="2">
							<c:choose>
								<c:when test="${userChar.userCharacteristicType.characteristicDataType.code == 'single_select' ||
								                userChar.userCharacteristicType.characteristicDataType.code == 'multi_select'}">
									<c:forEach items="${userChar.characteristicDisplayValueList}" var="dynaPickListType">
							    	  <c:out value="${dynaPickListType.name}"/>
							    	  <br/>
								    </c:forEach>
								</c:when>
								<c:otherwise>
									<c:out value="${userChar.characteristicValue}"/>&nbsp;
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
				</c:forEach>							
				<%-- END Characteristic Information --%>
					<tr class="form-blank-row">
       		  <td></td>
     			</tr>
     			<tr class="form-blank-row">
       		  <td></td>
     			</tr>							
					<%-- START Bank Account Information --%>
					<tr>
						<td class="subheadline" colspan="4">
							<cms:contentText key="BANK_ACCOUNT_HEADER" code="participant.participant"/>
							&nbsp;&nbsp;
							<% pageContext.setAttribute("balanceUrl", ClientStateUtils.generateEncodedLink( "", "../participant/participantBalances.do", parameterMap) ); %>
							<a href="<c:out value='${balanceUrl}'/>" class="content-link">
							  <cms:contentText key="BALANCES" code="participant.participant"/>
							</a>
						</td>
					</tr>
					<tr>
						<td class="content-field-label"></td>
						<td class="content-field-label">
							<cms:contentText key="AWARDBANQ_NUMBER" code="participant.participant"/>
						</td>
						<td class="content-field-review">
							<beacon:authorize ifAnyGranted="PROJ_MGR">
								<c:out value="${userForm.awardbanqNumber}" />
							</beacon:authorize>
							<beacon:authorize ifNotGranted="PROJ_MGR">
								<c:out value="${userForm.awardbanqNumberEncrypted}" />
							</beacon:authorize>							
						</td>
					</tr>
					<tr>
						<td class="content-field-label"></td>
						<td class="content-field-label">
							<cms:contentText key="CENTRAX_ID" code="participant.participant"/>
						</td>
						<td class="content-field-review">
							<beacon:authorize ifAnyGranted="PROJ_MGR">
								<c:out value="${userForm.centraxId}" />
							</beacon:authorize>
							<beacon:authorize ifNotGranted="PROJ_MGR">
								<c:out value="${userForm.centraxIdEncrypted}" />
							</beacon:authorize>							
						</td>
					</tr>
					<tr>
						<td class="content-field-label"></td>
						<td class="content-field-label">
							<cms:contentText key="HONEYCOMB_PARTICIPANT_ID" code="participant.participant" />
						</td>
						<td class="content-field-review">
							<c:out	value="${userForm.honeycombUserId}" />
						</td>
				</tr>
				<%-- END Bank Account Information --%>
					<tr class="form-blank-row">
       		  <td></td>
     			</tr>
     			<tr class="form-blank-row">
       		  <td></td>
     			</tr>	
					<%-- START Additional Information --%>
					<tr>					
						<td class="subheadline" colspan="4">
							<cms:contentText key="ADDL_INFO_HEADER" code="participant.participant"/>
						</td>
					</tr>
					<tr>
 					   <td class="content-field-label"></td>
						<td class="content-field-review" colspan="3">
							<% pageContext.setAttribute("personalInfoMaintainUrl", ClientStateUtils.generateEncodedLink( "", "personalInfoAdminMaintain.do?method=display", parameterMap) ); %>
							<a href="<c:out value='${personalInfoMaintainUrl}'/>" class="content-link">
							  <cms:contentText key="ABOUT_ME" code="participant.participant"/>
							</a>
						</td>
					</tr>
					<tr>
 						<td class="content-field-label"></td>
						<td class="content-field-review" colspan="3">
							<% pageContext.setAttribute("commLogUrl", ClientStateUtils.generateEncodedLink( "", "userCommLogListDisplay.do?method=displayList", parameterMap) ); %>
							<a href="<c:out value='${commLogUrl}'/>" class="content-link">
							  <cms:contentText key="COMMUNICATIONS_LOG" code="participant.participant"/>
							</a>
						</td>
					</tr>
					
					<c:if test="${allowMgrDiscAward}">
						<c:if test="${userForm.optOutAwards ne true }">
							<beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
								<tr>
		 							<td class="content-field-label"></td>
									<td class="content-field-review" colspan="3">
										<% pageContext.setAttribute("awardUrl", ClientStateUtils.generateEncodedLink( "", "../promotion/discretionaryAward.do?method=display", parameterMap) ); %>
										<a href="<c:out value='${awardUrl}'/>" class="content-link">
									  	<cms:contentText key="DISCRETIONARY_AWARD" code="participant.participant"/>
										</a>
									</td>
								</tr>
							</beacon:authorize>
						</c:if>
					</c:if>
					
					<tr>
 						<td class="content-field-label"></td>					
						<td class="content-field-review" colspan="3">
						    <a href="javascript: setActionDispatchAndSubmit('participantEmploymentListDisplay.do','displayList');" class="content-link">
				              <cms:contentText key="EMPLOYMENT_HEADER" code="participant.participant"/>
                			</a>
						</td>
					</tr>
					<tr>
 						<td class="content-field-label"></td>					
						<td class="content-field-review" colspan="3">
							<% pageContext.setAttribute("prefUrl", ClientStateUtils.generateEncodedLink( "", "../participant/participantMaintainUpdatePreferences.do?viewCurrentUser=false&method=prepareUpdatePreferences", parameterMap) ); %>
							<a href="<c:out value='${prefUrl}'/>" class="content-link">
								<cms:contentText key="PREFERENCES_HEADER" code="participant.participant"/>
							</a>
						</td>
					</tr>
					<tr>
 						<td class="content-field-label"></td>
						<td class="content-field-review" colspan="3">
							<% pageContext.setAttribute("promoUrl", ClientStateUtils.generateEncodedLink( "", "../participant/participantPromotionsDisplay.do?moduleType=all&method=firstRun", parameterMap) ); %>
							<a href="<c:out value='${promoUrl}'/>" class="content-link">
								<cms:contentText key="PROMOTIONS_HEADER" code="participant.participant"/>
							</a>
						</td>
					</tr>
					<beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN,MODIFY_PROXIES">
					<tr>
 						<td class="content-field-label"></td>
						<td class="content-field-review" colspan="3">
							<% pageContext.setAttribute("proxyListUrl", ClientStateUtils.generateEncodedLink( "", "../proxy/proxyListDisplay.do?includeCancel=true", parameterMap) ); %>
							<a href="<c:out value='${proxyListUrl}'/>" class="content-link">
								<cms:contentText key="PROXY_HEADER" code="participant.participant"/>
							</a>
						</td>
					</tr>
					</beacon:authorize>
					<beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
						<tr>
 							<td class="content-field-label"></td>					
							<td class="content-field-review" colspan="3">
								<% pageContext.setAttribute("securityUrl", ClientStateUtils.generateEncodedLink( "", "../participant/displayUserSecurity.do?method=display", parameterMap) ); %>
								<a href="<c:out value='${securityUrl}'/>" class="content-link">
							  	<cms:contentText key="SECURITY_DETAILS" code="participant.participant"/>
								</a>
							</td>
						</tr>
					</beacon:authorize>
					<tr>
 						<td class="content-field-label"></td>					
						<td class="content-field-review" colspan="3">
							<% pageContext.setAttribute("transUrl", ClientStateUtils.generateEncodedLink( "", "../claim/transactionHistory.do?method=display", parameterMap) ); %>
							<a href="<c:out value='${transUrl}'/>" class="content-link">
							  <cms:contentText key="TRANSACTION_HISTORY" code="participant.transactionhistory"/>
							</a>
						</td>			
					</tr>
					
					<%-- Display only if PAX is active --%>
					
					<beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
					<tr>
 						<td class="content-field-label"></td>					
						<td class="content-field-review" colspan="3">
							<% pageContext.setAttribute("resendWelcomeEmailUrl", ClientStateUtils.generateEncodedLink( "", "../participant/resendWelcomeEmail.do?method=resendWelcomeEmailView", parameterMap) ); %>
							<a href="<c:out value='${resendWelcomeEmailUrl}'/>" class="content-link">
							  <cms:contentText key="RESEND_WELCOME_EMAIL" code="participant.participant"/>
							</a>
						</td>			
					</tr>
					</beacon:authorize>
					
					<c:if test="${userForm.showThrowdownPlayerStats}">
					<tr>
 						<td class="content-field-label"></td>					
						<td class="content-field-review" colspan="3">
							<% pageContext.setAttribute("throwdownStatsUrl", ClientStateUtils.generateEncodedLink( "", "../participant/throwdownParticipantStatistic.do?method=display", parameterMap) ); %>
							<a href="<c:out value='${throwdownStatsUrl}'/>" class="content-link">
							  <cms:contentText key="THROWDOWN_STATS" code="participant.participant"/>
							</a>
						</td>			
					</tr>
					</c:if>
					
					<%-- END Additional Information --%>
				   </table>
				  </td>
				</tr>				
			</table>
		 </td>
     </tr>        
   </table>
</html:form>