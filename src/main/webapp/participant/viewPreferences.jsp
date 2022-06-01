<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf" %>

<html:form styleId="contentForm" action="saveParticipantMaintainUpdatePreferences">
  <html:hidden property="method"/>
  <html:hidden property="version"/>
  <html:hidden property="viewCurrentUser"/>
	<beacon:client-state>
		<beacon:client-state-entry name="id" value="${userForm.id}"/>
		<beacon:client-state-entry name="userId" value="${userForm.userId}"/>
	</beacon:client-state>

	<div id="main" class="content">
		<cms:errors/>
  		<div class="column1">
			<div class="module modcolor1">
				<div class="guts">
					<div class="topper">
						<h2><cms:contentText key="HEADER" code="participant.preference.edit"/></h2>
					</div>

					<c:if test="${not empty userForm.lastName}">
	          			<%-- Subheadline --%>          
						<h5>
						  <c:out value="${userForm.titleDesc}" />
						  <c:out value="${userForm.firstName}" />
						  <c:out value="${userForm.middleName}" />
						  <c:out value="${userForm.lastName}" />
						  <c:out value="${userForm.suffixDesc}" />
						</h5>          
	       			</c:if>
	
					<p><cms:contentText key="VIEW_INFO" code="participant.preference.edit"/></p>
				</div> <%-- end guts --%>
			</div> <%-- end module --%>
		</div>  <%-- end column1 --%>

  		<div class="column1">
			<div class="module modcolor1">
				<div class="guts"> 
					<fieldset>
  						<p class="readonly">
							<span class="label"><cms:contentText key="LANGUAGE" code="participant.preference.edit"/>:</span>
							&nbsp;&nbsp;
              				<c:set var="noLanguageSelected" value="true" scope="page" />
              				<c:forEach items="${languageList}" var="lang">
								<c:if test="${ lang.code == userForm.language }">
									<span class="input"><c:out value="${lang.name}"/></span>
									<c:set var="noLanguageSelected" value="false" scope="page" />
								</c:if>
              				</c:forEach>
							<c:if test="${noLanguageSelected}">
								<span class="input"><cms:contentText key="NONE_SELECTED" code="participant.preference.edit"/></span>
							</c:if>
						</p>    					
					</fieldset>
					            
					<c:if test="${(not empty contactMethodsTypeList) and (allowEstatements or allowTextMessages) }">
						<fieldset>            
							<h5><cms:contentText key="CONTACT_TYPES" code="participant.preference.edit"/></h5>            
						

							<c:set var="noCMSelected" value="true" scope="page" />
							<c:forEach items="${contactMethodsTypeList}" var="contactMethodType">
								<c:choose>
									<c:when test="${ (contactMethodType.code == 'estatements') and allowEstatements }">
										<c:forEach items="${userForm.contactMethodTypes}" var="cmt">
											<c:if test="${cmt == contactMethodType.code}">
												<c:set var="noCMSelected" value="false" scope="page" />
	                      		
	                            		<p class="readonly">
		                              		<span class="input" nowrap>
		                                		<c:out value="${contactMethodType.name}"/>
		                              			&nbsp;&nbsp;	                              		
		                              			<c:out value="${userForm.emailAddress}"/>
		                              		</span>
	                            		</p>
	
											</c:if>
										</c:forEach>
									</c:when>
									<c:otherwise>
										<c:if test="${ (contactMethodType.code == 'textmessages') and allowTextMessages }">
	                        				<h6><c:out value="${contactMethodType.name}"/></h6>
											<c:forEach items="${activeTextMessagesList}" var="message">
												<c:forEach items="${userForm.activeTextMessages}" var="m">
													<c:if test="${m == message.id}">
														<c:set var="noCMSelected" value="false" scope="page" />
														<p class="readonly">
															<span class="input" nowrap><c:out value="${message.name}"/></span>
														</p>
													</c:if>
												</c:forEach>		
											</c:forEach>
	                              			<p class="readonly">
												<span class="label" nowrap><cms:contentText key="TEXT_MESSAGE_ADDRESS" code="participant.preference.edit"/>:</span>
												<span class="input" nowrap><c:out value="textMessageAddress"/></span>
											</p>
										</c:if>
									</c:otherwise>
								</c:choose>
							</c:forEach>
							<c:if test="${noCMSelected}">
								<p class="readonly">
									<span class="input" nowrap>
										<cms:contentText key="NONE_SELECTED" code="participant.preference.edit"/>
									</span>
								</p>
							</c:if>
             		 </fieldset>
				  </c:if>
				  <c:if test="${allowContacts}">
					<fieldset>
            			<h5><cms:contentText key="CONTACT_METHOD" code="participant.preference.edit"/></h5>
            
						<c:set var="noMethodSelected" value="true" scope="page" />
							<c:forEach items="${contactMethodsList}" var="contactMethod">
								<c:forEach items="${userForm.contactMethods}" var="cm">
									<c:if test="${cm == contactMethod.code}">
										<c:set var="noMethodSelected" value="false" scope="page" />
										<p class="readonly">
											<span class="input" nowrap><c:out value="${contactMethod.name}"/></span>
                        				</p>                        
                      				</c:if>
								</c:forEach>
							</c:forEach>
							<c:if test="${noMethodSelected}">
								<p class="readonly">
									<span class="input" nowrap><cms:contentText key="NONE_SELECTED" code="participant.preference.edit"/></span>
								</p>
							</c:if>
						</fieldset>
          			</c:if> <%-- end allowContacts --%>
       
				</div> <%-- end guts --%>
			</div> <%-- end module --%>
		</div> <%-- end column1 --%>  

		<div class="column1">
			<div class="module modcolor1">
				<div class="guts"> 
					<div class="buttons">

						<button class="fancy" onclick="setDispatchAndSubmit('cancelled')">
							<span><span>
							  <c:choose>
								<c:when test="${userForm.viewCurrentUser}">
									<cms:contentText code="system.button" key="CANCEL" />
								</c:when>
								<c:otherwise>
									<cms:contentText code="system.button" key="BACK_TO_OVERVIEW" />
								</c:otherwise>
							  </c:choose>
							</span></span>
						</button> 				
					</div> <%-- end buttons --%>        

				</div> <%-- end guts --%>
			</div> <%-- end module --%>
		</div> <%-- end column1 --%>   
 
	</div> <%-- end main --%>
</html:form>