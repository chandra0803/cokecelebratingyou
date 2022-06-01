<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf" %>

<html:form styleId="contentForm" action="profilePasswordChange" >
	<html:hidden property="method" value=""/>
	<beacon:client-state>
		<beacon:client-state-entry name="userId" value="${profileForm.userId}"/>
	</beacon:client-state>

	<div id="main" class="content">
		<cms:errors/>
  		<div class="column1">
			<div class="module modcolor1">
				<div class="guts">
					<div class="topper">
						<h2><cms:contentText key="TITLE" code="user.profile"/></h2>
					</div>

					<p><cms:contentText key="INSTRUCTIONS" code="user.profile"/></p>
				</div> <%-- end guts --%>
			</div> <%-- end module --%>
		</div> <%-- end column1 --%>

		<div class="column2a">
			<div class="module modcolor1">
				<div class="guts">
					<fieldset>
              			<legend><cms:contentText key="PERSONAL_INFORMATION" code="user.profile"/></legend>
                  
						<%-- user name --%>
 						<p class="readonly">
							<span class="label"><cms:contentText key="NAME_LABEL" code="user.profile"/></span>
							<span class="input"><c:out value="${user.firstName}"/>&nbsp;<c:out value="${user.middleName}"/>&nbsp;<c:out value="${user.lastName}"/></span>
                        </p>
                
						<%-- street addresses --%>
						<c:forEach items="${user.userAddresses}" var="address">
							<p class="readonly">
								<span class="label"><c:out value="${address.addressType.name}"/></span>
								<span class="input">
									<c:out value="${address.address.addr1}"/>
									<c:if test='${address.address.addr2 != null}'>
										<br/>
										<c:out value="${address.address.addr2}"/>
									</c:if>
									<c:if test='${address.address.addr3 != null}'>
										<br/>
										<c:out value="${address.address.addr3}"/>
									</c:if>

									<c:if test='${address.address.city != null}'>
										<br/>
										<c:out value="${address.address.city}"/>,&nbsp;
										<c:out value="${address.address.stateType.name}"/>&nbsp;
										<c:out value="${address.address.postalCode}"/>
									</c:if>

									<c:if test='${address.address.country.countryName != null}'>
										<br/>
										<c:out value="${address.address.country.countryName}"/>
									</c:if>
								</span>
							</p>
						</c:forEach>

						<%-- email addresses --%>
						<c:forEach items="${user.userEmailAddresses}" var="emailAddress">
							<p class="readonly">
								<span class="label">                  
                      				<c:out value="${emailAddress.emailType.name}"/>&nbsp;
                      				<cms:contentText key="EMAIL_ADDRESS_LABEL" code="user.profile"/>
								</span>
								<span class="input">
									<c:out value="${emailAddress.emailAddr}"/>
								</span>
							</p>
						</c:forEach>


						<%-- telephone numbers --%>
						<c:forEach items="${user.userPhones}" var="phone">
								<p class="readonly" >
								<span class="label">
								<cms:contentText
										key="TELEPHONE_COUNTRY_AND_COUNTRY_CODE"
										code="participant.participant" /></span>
								<span class="input"><c:out value="${phone.displayCountryPhoneCode}"/>									
							</span>
								</p>
							
								<p class="readonly" >
								<span class="label">
								<cms:contentText key="PHONE_NUMBER" code="participant.participant" />
								</span>
								<span class="input"><c:out value="${phone.phoneNbr}"/>									
							</span>

								<p class="readonly" >
								<span class="label">
								<cms:contentText key="EXTENSION" code="participant.participant" />
								</span>
							    <span class="input"><c:out value="${phone.phoneExt}"/>									
								</span>
												
						</c:forEach>

					</fieldset>
				</div>
				<%-- end guts --%>
			</div>
			<%-- end module --%>
		</div>
		<%-- end column 2a --%>

		<div class="column2b">
			<div class="module modcolor1">
				<div class="guts">
					<%-- login information --%>
					<fieldset>
              			<legend><cms:contentText key="WEB_SITE_LOGIN_INFO" code="user.profile"/></legend>

                		<p class="readonly">
							<span class="label"><cms:contentText key="USER_NAME_LABEL" code="user.profile"/></span>
							<span class="input"><c:out value="${user.userName}"/></span>
						</p>

						<label for="newPassword" class="password req">
							<span class="label"><em class="req">*</em><cms:contentText key="PASSWORD_LABEL" code="user.profile"/></span>
							<html:password property="newPassword" size="30" maxlength="40" styleClass="text"/>
						</label>   

						<label for="confirmNewPassword" class="password req">
							<span class="label"><em class="req">*</em><cms:contentText key="CONFIRM_PASSWORD_LABEL" code="user.profile"/></span>
							<html:password property="confirmNewPassword" size="30" maxlength="40" styleClass="text"/>
						</label>
            
						<label for="secretQuestion" class="select req">
							<span class="label"><em class="req">*</em><cms:contentText key="SECRET_QUESTION" code="user.user"/></span>
							<span class="select">			  	
								<html:select styleId="secretQuestion" property="secretQuestion">
									<html:options collection="secretQuestionList" property="code" labelProperty="name"/>
					       		</html:select>
							</span>
						</label>


						<label for="secretAnswer" class="text req">
							<span class="label"><em class="req">*</em><cms:contentText key="SECRET_ANSWER" code="user.user"/></span>
							<html:text property="secretAnswer" size="30" styleClass="text"/>
						</label>
					</fieldset>

					<c:if test='${hasNodes == "true"}'>
						<fieldset>
              				<legend><cms:contentText key="NODE_ASSIGNMENTS" code="user.profile"/></legend>
							<c:forEach items="${user.userNodes}" var="userNode">
								<p class="readonly">
									<span class="label"><cms:contentText key="NODE_NAME_LABEL" code="user.profile"/></span>
									<span class="input">
										<c:out value="${userNode.node.name}"/>&nbsp;
                        				<c:if test="${userNode.node.hierarchy.primary}">
											<cms:contentText key="PRIMARY_INDICATOR" code="user.profile"/>
										</c:if>
									</span>
								</p>

								<p class="readonly">
									<span class="label"><cms:contentText key="NODE_ROLE_LABEL" code="user.profile"/></span>
									<span class="input"><c:out value="${userNode.hierarchyRoleType.name}"/></span>
								</p>

								<p class="readonly">
									<span class="label"><cms:contentText key="NODE_OWNER_LABEL" code="user.profile"/></span>
									<span class="input"><c:out value="${nodeOwners[userNode.node.name]}"/></span>
								</p>

								<c:forEach items="${nodeManagers[userNode.node.name]}" var="manager">
									<p class="readonly">
										<span class="label"><cms:contentText key="NODE_MANAGER_LABEL" code="user.profile"/></span>
										<span class="input"><c:out value="${manager}"/></span>
									</p>                    
								</c:forEach>
                  			</c:forEach>
						</fieldset>
					</c:if>

				</div> <%-- end guts --%>
			</div> <%-- end module --%>
		</div> <%-- end column 2b --%>

		<c:if test='${passwordChanged == "true"}'>
			<div class="column1">
				<div class="module error">
					<div class="guts">
						<cms:contentText key="PASSWORD_CHANGED" code="system.general"/>
					</div><%-- end guts --%>
				</div><%-- end module --%>
			</div><%-- end column1 --%>
		</c:if>

		<div class="column1">
			<div class="module modcolor1">
				<div class="guts">					
					<div class="buttons">
						<beacon:authorize ifNotGranted="LOGIN_AS">
							<button type="submit" class="fancy" onclick="setDispatch('changePassword')">
								<span><span><cms:contentText key="SUBMIT" code="system.button"/></span></span>
							</button>
						</beacon:authorize>
						<button type="submit" class="fancy" onclick="setActionAndSubmit('homePage.do')">
          					<span><span><cms:contentText key="CANCEL" code="system.button"/></span></span>
						</button>
					</div> <%-- end buttons --%>					
				</div><%-- end guts --%>
			</div><%-- end module --%>
		</div><%-- end column1 --%>


	</div> <%-- end main --%>
                
</html:form>