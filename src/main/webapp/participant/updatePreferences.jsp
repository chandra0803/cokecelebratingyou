<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf" %>
<script type="text/javascript" src="<%=request.getContextPath()%>/assets/g4skin/scripts/taconite/taconite-client.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/assets/g4skin/scripts/taconite/taconite-parser.js"></script>
<SCRIPT LANGUAGE="JavaScript">
	<!-- 	
	// by Nannette Thacker
	// http://www.shiningstar.net
	// This script checks and unchecks boxes on a form
	// Checks and unchecks unlimited number in the group...
	// Pass the Checkbox group name...
	// call buttons as so:
	// <input type=button name="CheckAll"   value="Check All"
		//onClick="checkAll(document.myform.list)">
	// <input type=button name="UnCheckAll" value="Uncheck All"
		//onClick="uncheckAll(document.myform.list)">
	// -->
	
	<!-- Begin
	function checkAll()
	{
		var field =document.forms["userForm"].activeSMSGroupTypes;

		for (i = 0; i < field.length; i++)
			field[i].checked = true ;
	}
	
	function uncheckAll()
	{
		var field =document.forms["userForm"].activeSMSGroupTypes;
		for (i = 0; i < field.length; i++)
			field[i].checked = false ;
	}
	//  End -->
</script>
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
	
					<p><cms:contentText key="ADD_INFO" code="participant.preference.edit"/></p>
				</div> <%-- end guts --%>
			</div> <%-- end module --%>
		</div>  <%-- end column1 --%>

  		<div class="column1">
			<div class="module modcolor1">
				<div class="guts"> 
					<fieldset>
  						<p>
						    <span class="label"><cms:contentText key="LANGUAGE" code="participant.preference.edit"/>:</span>
							&nbsp;&nbsp;
						    <span class="select">
								 <html:select property="language" name="userForm">
					                <html:options collection="languageList" property="code" labelProperty="name"/>
              					</html:select>						       
						    </span>
						</p>    					
					</fieldset>
					
		
		  <fieldset id="profilePagePreferencesTabFieldsetPublic">
             <h5><cms:contentText key="ALLOW_PUBLIC" code="profile.preference.tab"/></h5>
                <div class="control-group validateme" data-validate-flags="nonempty" data-validate-fail-msgs='{"nonempty" : "<cms:contentText  key="MAKE_A_CHOICE" code="profile.errors" />"}'> 
                    <label class="radio" for="allowInformationPublicYes">
                    	<html:radio property="allowPublicInformation" value="true" />
                        <cms:contentText code="profile.preference.tab" key="YES_MAKE_INFO_PUBLIC"/>
                    </label>
                    <label class="radio" for="allowInformationPublicNo">
                    	<html:radio property="allowPublicInformation" value="false" /></td>
                        <cms:contentText code="profile.preference.tab" key="DONT_MAKE_INFO_PUBLIC"/>
                    </label>
                </div>
            
                <div class="control-group validateme" data-validate-flags="nonempty" data-validate-fail-msgs='{"nonempty" : "<cms:contentText  key="MAKE_A_CHOICE" code="profile.errors" />"}'>
                    <label class="radio" for="allowActivitiesPublicYes">
                        <html:radio property="allowPublicRecognition" value="true" />
                        <cms:contentText code="profile.preference.tab" key="YES_MAKE_ACTIVITY_PUBLIC"/>
                    </label>
                    <label class="radio" for="allowActivitiesPublicNo">
                        <html:radio property="allowPublicRecognition" value="false" /></td>
                        <cms:contentText code="profile.preference.tab" key="DONT_MAKE_ACTIVITY_PUBLIC"/>
                    </label>
                </div>
            </fieldset>
		
		
					
					<%-- Twitter and Facebook options should be available if PURL gets the ability to post directly --%>
					<c:if test="${isTwitterEnabled && false}">
					<fieldset>
  				     <p>
						<legend>Twitter Integration</legend>
							&nbsp;&nbsp;
						  <div id="twitterAuthorization">
						     <c:choose>
							    <c:when test="${ empty userForm.userTwitter}">
							        <%@include file="twitter/startAuthorization.jsp" %>							 
						        </c:when>
						        <c:when test="${not empty userForm.userTwitter.accessToken}">
						            <%@include file="twitter/disableAuthorization.jsp" %>						       						    
						        </c:when>						    
						     </c:choose>
						 </div>							
					  </p>    					
					</fieldset>
					</c:if>
					
				   <c:if test="${isFaceBookEnabled && false}">
				   <fieldset>
				    <p>
				       <legend>Facebook Integration</legend>&nbsp;&nbsp;
				       	<div id="facebookSettings">
				         	<%-- first put the facebook api key in a hidden field so the javascript can pick it up --%>
				           	<input type="hidden" id="facebookAppId" name="facebookAppId" value="${facebookAppId}" />
				           	
				         	<%-- now include the necessary facebook javascript --%>
                         	<div id="fb-root"></div>
                         	<script type="text/javascript" src="http://connect.facebook.net/en_US/all.js"></script>
                         	<script type="text/javascript" src="<%=request.getContextPath()%>/assets/g4skin/scripts/facebook.js"></script>
                         	                         	
                         	<c:choose>
                				<c:when test="${ empty userForm.userFacebook }">
                  					<%@include file="facebookDisabled.jsp" %>
                				</c:when>
                				<c:otherwise>
                  					<%@include file="facebookEnabled.jsp" %>
                				</c:otherwise>
              				</c:choose>
				       	</div>				    
				    </p>				   
				   </fieldset>
				   </c:if>
				   					
				<%-- Bug # 36141   <c:if test="${isUserCountryAllowedForTextMessages }">  --%>	
					<c:if test="${(not empty contactMethodsTypeList) and (allowEstatements or allowTextMessages) }">
						<fieldset>
							<h5><cms:contentText key="CONTACT_TYPES" code="participant.preference.edit"/></h5>
										
                		  <c:forEach items="${contactMethodsTypeList}" var="contactMethodType">
                  			<c:choose>
                    			<c:when test="${ (contactMethodType.code == 'estatements') and allowEstatements }">									
										<p>
	 										<html:multibox property="contactMethodTypes" value="${contactMethodType.code}"/>
	                                    	    <span class="label"><c:out value="${contactMethodType.name}"/></span>                                    	
	                                	  	&nbsp;&nbsp;									  
		                              		<c:choose>
		                                		<c:when test="${empty userForm.emailAddress}">                                  
		                                    		<html:text property="emailAddress" styleClass="text"/>
		                                  
				                                    <html:select property="emailType" styleClass="select">
					                                    <html:options collection="emailTypeList" property="code" labelProperty="name"  />
				  	                                </html:select>                                  
		                                		</c:when>
			                                	<c:otherwise>                                  
			                                    	<html:hidden property="emailAddress"/>
			                                    	<span class="input"><c:out value="${userForm.emailAddress}"/></span>                                  
												</c:otherwise>
		                              		</c:choose>
										</p> 
                    			</c:when>
		                    	<c:otherwise>
		                      		<c:if test="${ (contactMethodType.code == 'textmessages') and allowTextMessages and isUserCountryAllowedForTextMessages }">
			                        		<h6><c:out value="${contactMethodType.name}"/></h6>
			                        		<a class="selectall" href="javascript:checkAll()"><cms:contentText code="system.general" key="SELECT_ALL"/></a>
			                        		|&nbsp;<a class="selectall" href="javascript:uncheckAll()"><cms:contentText code="system.general" key="DESELECT_ALL"/></a>
											<c:forEach items="${messageSMSGroupTypeList}" var="SMSGroupType">
												<p>
													<html:multibox property="activeSMSGroupTypes" value="${SMSGroupType.code}"/>
	                                    			<span class="label"><c:out value="${SMSGroupType.name}"/></span>                                    	
	                                			</p>  
											</c:forEach>						

										
										<p>
										<span class="label"><cms:contentText
										key="TELEPHONE_COUNTRY_AND_COUNTRY_CODE"
										code="participant.participant" /></span>
											<span class="input">									
											<html:select property="countryPhoneCode" styleClass="content-field">
													<html:options collection="countryList" property="countryCode" labelProperty="countryNameandPhoneCodeDisplay"  />
											</html:select>											
											</span>
											<p>
											<span class="label"><cms:contentText key="PHONE_NUMBER" code="participant.preference.edit"/></span>
											<span class="input">
											<html:text property="textPhoneNbr" size="12" maxlength="20" styleClass="content-field"/>											
											</span>
											</p>
											
							    			<p>
							   					<html:checkbox property="acceptTermsOnTextMessages" value="yes"></html:checkbox>
												<span class="label"><cms:contentText key="ACCEPT_TERMS_CONDITIONS" code="participant.preference.edit"/></span>
											</p>
							           
							    
							     			<h6><cms:contentText key="TERMS_AND_CONDITIONS" code="participant.preference.edit"/></h6>
							         		
											<p>
												<span class="text"><c:out value="${termsAndCondition}" /></span>
											</p>               
									</c:if>
								</c:otherwise>
							</c:choose>
						 </c:forEach>
					</fieldset>
				  </c:if>				
				  <c:if test="${allowContacts}">
   					<fieldset>
						<h5><cms:contentText key="CONTACT_METHOD" code="participant.preference.edit"/></h5>            
               
						<c:forEach items="${contactMethodsList}" var="contactMethod">
							<c:choose>
								<c:when test="${contactMethod.code == 'fax'}">
									<p>								  
									    <html:multibox property="contactMethods" value="${contactMethod.code}" disabled="${participantForm.hasFax}"/>
										<span class="label"><c:out value="${contactMethod.name}"/></span>									  
									</p>
								</c:when>
								<c:otherwise>
									<p>									  
									    <html:multibox property="contactMethods" value="${contactMethod.code}"/>
										<span class="label"><c:out value="${contactMethod.name}"/></span>									 
									</p>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</fieldset>     
				  </c:if>    

				</div> <%-- end guts --%>
			</div> <%-- end module --%>
		</div> <%-- end column1 --%>  
     	
		<div class="column1">
			<div class="module modcolor1">
				<div class="guts"> 
					<div class="buttons">
						<beacon:authorize ifNotGranted="LOGIN_AS">
							<button class="fancy" type="button" onclick="setDispatchAndSubmit('updatePreferences')">
								<span><span><cms:contentText code="system.button" key="SAVE" /></span></span>
							</button> 	
						</beacon:authorize>
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