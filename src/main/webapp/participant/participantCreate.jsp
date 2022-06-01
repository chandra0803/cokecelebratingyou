<%--UI REFACTORED--%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-en.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-setup.js"></script>
<script type="text/javascript">
<!--
function clearDatesAndSetDispatch( dispatch )
{
	clearDateMask(getContentForm().dateOfBirth);
	clearDateMask(getContentForm().hireDate);
	clearDateMask(getContentForm().terminationDate);
	setDispatch(dispatch);
}

function clearDatesAndSetActionDispatchAndSubmit(action, dispatch)
{
	clearDateMask(getContentForm().dateOfBirth);
	clearDateMask(getContentForm().hireDate);
	clearDateMask(getContentForm().terminationDate);
	setActionDispatchAndSubmit(action, dispatch);
}
function setDispatchToHome()
{
 document.forms[0].action="<%=RequestUtils.getBaseURI(request)%>/homePage.do";
 document.forms[0].submit();
}  
//-->
</script>
<html:form styleId="contentForm" action="participantMaintainCreate">
	<html:hidden property="method"/>
	<html:hidden property="userCharacteristicValueListCount"/>
	<html:hidden property="originalPaxTermsAcceptFromDB"/>
	<html:hidden property="originalPaxStatusFromDB"/>
	<html:hidden property="paxStatus"/>
	<html:hidden property="paxTermsAccept"/>
	<%--
	<beacon:client-state>
		<beacon:client-state-entry name="password2" value="${userForm.password2}"/>
		<beacon:client-state-entry name="confirmPassword2" value="${userForm.confirmPassword2}"/>
		<beacon:client-state-entry name="passwordSystemGenerated" value="${userForm.passwordSystemGenerated}"/>
	</beacon:client-state>
 	--%>
  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="TITLE_CREATE" code="participant.participant"/></span>
        <span id="quicklink-add"></span><script type="text/javascript">quicklink_display_add('<cms:contentText key="TITLE_CREATE" code="participant.participant"/>','<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>'); </script>    
	<%-- Commenting out to fix in a later release
        <input type="button" class="webhelp-button" onclick="javascript:FMCOpenHelp( 'G3H22', 'HTML Help Window', null, null );" target="_webhelp" value="<cms:contentText code="system.webhelp" key="WEB_HELP"/>">
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
				<table cellpadding="3" cellspacing="1">							
					<%-- START Personal Information --%>
					<tr class="form-row-spacer">
						<td class="subheadline" colspan="3">
							<cms:contentText key="PERSONAL_HEADER" code="participant.participant"/>
						</td>
					</tr>
					
					<tr class="form-row-spacer">				  
		              <beacon:label property="paxStatus" required="true">
		                <cms:contentText key="PARTICIPANT_STATUS" code="participant.participant"/>
		              </beacon:label>
		              <td class="content-field"><c:out value="${userForm.paxStatus}" /></td>
					</tr>

					<c:choose>
					  <c:when test="${userForm.termsConditionsRequired}">
					  	 <beacon:label property="paxTermsAccept" required="false">
					       <cms:contentText key="PARTICIPANT_TERMS_ACCEPT" code="participant.participant"/>
					     </beacon:label>
					     <td class="content-field">  
					       	<c:out value="${userForm.paxTermsAccept}" />
					     </td>
			       	  </c:when>						
					  <c:otherwise>
					    <tr class="form-row-spacer">			  
			              <beacon:label property="paxTermsAccept" required="false">
			                <cms:contentText key="PARTICIPANT_TERMS_ACCEPT" code="participant.participant"/>
			              </beacon:label>
			              <td class="content-field">  
					        <cms:contentText key="NOT_APPLICABLE" code="participant.participant"/> 
					        ( <cms:contentText key="DEFAULT" code="participant.participant"/>: <c:out value="${userForm.paxTermsAccept}" /> )
					      </td>
					     </tr>
					  </c:otherwise>
					</c:choose>
					   	      				  
					<tr class="form-row-spacer">				  
		              <beacon:label property="title">
		                <cms:contentText key="TITLE" code="participant.participant"/>
		              </beacon:label>	
		              <td class="content-field">
		                <html:select property="title" styleClass="content-field">
							<html:option value=''><cms:contentText key="SELECT_ONE" code="system.general"/></html:option>	
							<html:options collection="titleList" property="code" labelProperty="name"  />
						</html:select>
		              </td>
			        </tr>
												

					<tr class="form-row-spacer">				  
		              <beacon:label property="firstName" required="true">
		                <cms:contentText key="FIRST_NAME" code="participant.participant"/>
		              </beacon:label>	
		              <td class="content-field">
		                <html:text property="firstName" size="30" maxlength="40" styleClass="content-field"/>
		              </td>
			        </tr>
													

					<tr class="form-row-spacer">				  
		              <beacon:label property="middleName">
		                <cms:contentText key="MIDDLE_NAME" code="participant.participant"/>
		              </beacon:label>	
		              <td class="content-field">
		                <html:text property="middleName" size="30" maxlength="40" styleClass="content-field"/>
		              </td>
			        </tr>
						        
			        							

					<tr class="form-row-spacer">				  
		              <beacon:label property="lastName" required="true">
		                <cms:contentText key="LAST_NAME" code="participant.participant"/>
		              </beacon:label>	
		              <td class="content-field">
		                <html:text property="lastName" size="30" maxlength="40" styleClass="content-field"/>
		              </td>
			        </tr>
													

					<tr class="form-row-spacer">				  
		              <beacon:label property="suffix">
		                <cms:contentText key="SUFFIX" code="participant.participant"/>
		              </beacon:label>	
		              <td class="content-field">		                
						<html:select property="suffix" styleClass="content-field">
							<html:option value=''><cms:contentText key="SELECT_ONE" code="system.general"/></html:option>
							<html:options collection="suffixList" property="code" labelProperty="name"  />
						</html:select>
		              </td>
			        </tr>
													
														

					<tr class="form-row-spacer">				  
		              <beacon:label property="ssn">
		                <cms:contentText key="SSN" code="participant.participant"/>
		              </beacon:label>	
		              <td class="content-field">		                
						 <html:text property="ssn" size="30" maxlength="40" styleClass="content-field"/>
		              </td>
			        </tr>
						
														
					<tr class="form-row-spacer">				  
			            <beacon:label property="dateOfBirth">
			              <cms:contentText key="DATE_OF_BIRTH" code="participant.participant"/>
			            </beacon:label>	
			            <td class="content-field">		  		  	
			              <html:text property="dateOfBirth" readonly="true" styleId="dateOfBirth" size="10" maxlength="10" styleClass="content-field" onfocus="clearDateMask(this);"/>
			                <img id="dateOfBirthTrigger" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" alt="<cms:contentText key="DATE_OF_BIRTH" code="participant.participant"/>"/>
			            </td>              
          			</tr>		
          			
														
					<tr class="form-row-spacer">				  
			            <beacon:label property="gender">
			              <cms:contentText key="GENDER" code="participant.participant"/>
			            </beacon:label>	
			            <td class="content-field">		  		  	
							<html:select property="gender" styleClass="content-field">
								<html:option value=''><cms:contentText key="SELECT_ONE" code="system.general"/></html:option>
								<html:options collection="genderList" property="code" labelProperty="name"  />
							</html:select>
				        </td>              
          			</tr>								
						
				  <c:if test="${userForm.showSSOId}">
		  			<tr class="form-row-spacer">				  
            			<beacon:label property="ssoId" required="true">
              				<cms:contentText key="SSO_ID" code="participant.participant"/>  
            			</beacon:label>	
            			<td class="content-field"> 
							<html:text property="ssoId" size="30" maxlength="40" styleClass="content-field"/>
            			</td>
          			</tr>
          		  </c:if>
			        								
					<tr class="form-row-spacer">				  
			            <beacon:label property="addressType" required="true">
			              <cms:contentText key="PRIMARY_ADDRESS_TYPE" code="participant.participant"/>
			            </beacon:label>	
			            <td class="content-field">		  		  	
							<html:select property="addressType" styleClass="content-field">
								<html:options collection="addressTypeList" property="code" labelProperty="name"  />
							</html:select>
				        </td>              
          			</tr>								
						
								
					<tr class="form-row-spacer">				  
			            <beacon:label property="countryCode" required="true">
			              <cms:contentText key="COUNTRY" code="participant.participant"/>
			            </beacon:label>	
			            <td class="content-field">		  		  	
							<html:select property="addressFormBean.countryCode" styleClass="content-field" onchange="setActionDispatchAndSubmit('participantDisplay.do','changeCountry');">
								<%-- Bug Fix 23605 start --%>
								<c:if test="${multiple}">
									<html:option value=''><cms:contentText key="SELECT_ONE" code="system.general"/></html:option>
								</c:if>
								<%-- Bug Fix 23605 end --%>
								<html:options collection="countryListAll" property="countryCode" labelProperty="i18nCountryName"  />
							</html:select>
				        </td>              
          			</tr>								
							        							
		
					<tr class="form-row-spacer">				  
			            <beacon:label property="addr1" required="true">
			             	<cms:contentText key="ADDR1" code="participant.participant"/>
			            </beacon:label>	
			            <td class="content-field">		  		  	
							<html:text property="addressFormBean.addr1" size="40" maxlength="100" styleClass="content-field"/>
				        </td>              
          			</tr>								
															

					<tr class="form-row-spacer">				  
			            <beacon:label property="addr2">
			             	<cms:contentText key="ADDR2" code="participant.participant"/>
			            </beacon:label>	
			            <td class="content-field">		  		  	
							<html:text property="addressFormBean.addr2" size="40" maxlength="100" styleClass="content-field"/>
				        </td>              
          			</tr>								
														
					
					<tr class="form-row-spacer">				  
			            <beacon:label property="addr3">
			             	<cms:contentText key="ADDR3" code="participant.participant"/>
			            </beacon:label>	
			            <td class="content-field">		  		  	
							<html:text property="addressFormBean.addr3" size="40" maxlength="100" styleClass="content-field"/>
				        </td>              
          			</tr>								
										
												
				  			   
					<tr class="form-row-spacer">				  
			            <beacon:label property="city" required="true">
			             	<cms:contentText key="CITY" code="participant.participant"/>
			            </beacon:label>	
			            <td class="content-field">		  		  	
							<html:text property="addressFormBean.city" size="40" maxlength="30" styleClass="content-field"/>
				        </td>              
          			</tr>								
											
				<c:if test="${not empty stateList}">	
					<tr class="form-row-spacer">				  
			            <beacon:label property="stateTypeCode" required="true">
			             	<cms:contentText key="STATE" code="participant.participant"/>
			            </beacon:label>	
			            <td class="content-field">		  		  								
							<html:select property="addressFormBean.stateTypeCode" styleClass="content-field">
								<html:option value=''><cms:contentText key="SELECT_ONE" code="system.general"/></html:option>
								<html:options collection="stateList" property="code" labelProperty="name"  />
							</html:select>
				        </td>              
          			</tr>								
				</c:if>
				
				<c:if test="${requirePostalCode=='true'}">	
					<tr class="form-row-spacer">				  
			            <beacon:label property="postalCode" required="true">
			             	<cms:contentText key="POSTAL_CODE" code="participant.participant"/>
			            </beacon:label>	
			            <td class="content-field">		  		  								
							<html:text property="addressFormBean.postalCode" size="11" maxlength="11" styleClass="content-field"/>
				        </td>              
          			</tr>								
			   </c:if>			  

		
					<tr class="form-row-spacer">				  
			            <beacon:label property="emailType">
			             	<cms:contentText key="PRIMARY_EMAIL_TYPE" code="participant.participant"/>
			            </beacon:label>	
			            <td class="content-field">		  		  								
							<html:select property="emailType" styleClass="content-field">
								<html:option value=''><cms:contentText key="SELECT_ONE" code="system.general"/></html:option>
								<html:options collection="emailTypeList" property="code" labelProperty="name"  />
							</html:select>
				        </td>              
          			</tr>								
							
			        			        
					<tr class="form-row-spacer">				  
			            <beacon:label property="emailAddress">
			             	<cms:contentText key="EMAIL_ADDRESS" code="participant.participant"/>
			            </beacon:label>	
			            <td class="content-field">		  		  								
							<html:text property="emailAddress" size="30" maxlength="75" styleClass="content-field"/>
				        </td>              
          			</tr>								
																
			        
					<tr class="form-row-spacer">				  
			            <beacon:label property="phoneType">
			             	<cms:contentText key="PRIMARY_PHONE_TYPE" code="participant.participant"/>
			            </beacon:label>	
			            <td class="content-field">		  		  															
							<html:select property="phoneType" styleClass="content-field">
								<html:option value=''><cms:contentText key="SELECT_ONE" code="system.general"/></html:option>
								<html:options collection="phoneTypeList" property="code" labelProperty="name"  />
							</html:select>
				        </td>              
          			</tr>								
					

			        
					<tr class="form-row-spacer">				  
			           <beacon:label property="countryPhoneCode" >
			             	<cms:contentText key="TELEPHONE_COUNTRY_AND_COUNTRY_CODE" code="participant.participant"/>
			            </beacon:label>
			            <td class="content-field">		  		  															
							
							<html:select property="countryPhoneCode" styleClass="content-field">
								<%-- Bug Fix 23605 start --%>
								<c:if test="${multiple}">
								<html:option value=''><cms:contentText key="SELECT_ONE" code="system.general"/></html:option>
								</c:if>
								<c:forEach items="${countryListAll}" var="country">
									<option value="${country.countryCode}" 
									<c:if test="${country.countryCode == userForm.addressFormBean.countryCode}">
										selected 
									</c:if>									
										>${country.countryNameandPhoneCodeDisplay}</option>
								</c:forEach>
							</html:select>							
				        </td>              
          			</tr>								
					<tr class="form-row-spacer">			
						<beacon:label property="phoneNumber" >
			             	<cms:contentText key="PHONE_NUMBER" code="participant.roster.management.modify"/>
			            </beacon:label>
			            <td class="content-field">
			            	<html:text property="phoneNumber" size="12" maxlength="20" styleClass="content-field"/>						
				        </td>              
          			</tr>								
							
							<tr class="form-row-spacer">				  
			           <beacon:label property="phoneExtension" >
			             	<cms:contentText key="EXTENSION" code="participant.participant"/>
			            </beacon:label>	
			            <td class="content-field">						
							<html:text property="phoneExtension" size="6" maxlength="10" styleClass="content-field" />							
				        </td>              
          			</tr>								
									        								
																																
					<%-- END Personal Information --%>
				</table> <%-- End First Column Table --%>
			</td>
			<td width="2%" valign="top">&nbsp;</td>
			<td width="49%" valign="top">
				<table cellpadding="3" cellspacing="1">	
					<%-- START Web Site Information --%>
					<tr class="form-row-spacer">
						<td class="subheadline" colspan="3">
							<cms:contentText key="WEB_SITE_HEADER" code="participant.participant"/>
						</td>
					</tr>
					
					<tr class="form-row-spacer">				  
			            <beacon:label property="userName" required="true">
			             	<cms:contentText key="LOGIN_ID" code="participant.participant"/>
			            </beacon:label>	
			            <td class="content-field">		  		  								
							<html:text property="userName" size="30" maxlength="40" styleClass="content-field"/>
				        </td>              
          			</tr>								
						
<%--
					<tr class="form-row-spacer">SIMI
						<c:choose>
			            	<c:when test="${userForm.passwordSystemGenerated}">			  
					            <beacon:label property="password" required="false">
					             	<cms:contentText key="PASSWORD" code="participant.participant"/>
					            </beacon:label>				            
					            <td class="content-field">  							
		                			<html:text property="password2" size="30" styleClass="content-field" disabled="true"/>
		                		</td>
							</c:when>
						  	<c:otherwise>
						  		<beacon:label property="password" required="true">
					             	<cms:contentText key="PASSWORD" code="participant.participant"/>
					            </beacon:label>				            
					            <td class="content-field"> 
									<html:password property="password" size="30" maxlength="40" styleClass="content-field"/>
								</td>
						  	</c:otherwise>
			 			 </c:choose>				                     
          			</tr>	
          						        
					<tr class="form-row-spacer">SIMI	
						<c:choose>
							<c:when test="${userForm.passwordSystemGenerated}">			  
								<beacon:label property="confirmPassword" required="false">
									<cms:contentText key="CONFIRM_PASSWORD" code="participant.participant"/>
								</beacon:label>	
								<td class="content-field">						 
									<html:text property="confirmPassword2" size="30" styleClass="content-field" disabled="true"/>
								</td>
							</c:when>
							<c:otherwise>
								<beacon:label property="confirmPassword" required="true">
						        	<cms:contentText key="CONFIRM_PASSWORD" code="participant.participant"/>
						    	</beacon:label>	
						    	<td class="content-field">	
									<html:password property="confirmPassword" size="30" maxlength="40" styleClass="content-field"/>
								</td>
							</c:otherwise>
			 			 </c:choose>    
          			</tr>	
          						        						
					<tr class="form-row-spacer">				  
			            <td></td>
			            <td></td>
			            <td>	SIMI	  		  								
							<html:button property="CreatePassword" styleClass="content-buttonstyle" onclick="clearDatesAndSetActionDispatchAndSubmit('participantDisplay.do','generatePasswordCreate');">
								<cms:contentText code="system.button" key="CREATE_PASSWORD" />
							</html:button>
				        </td>              
          			</tr>								
						 			
									
					<tr class="form-row-spacer">				  
			            <beacon:label property="secretQuestion">
			             	<cms:contentText key="SECRET_QUESTION" code="participant.participant"/>
			            </beacon:label>	
			            <td class="content-field">	SIMI	  		  								
							<html:select property="secretQuestion" styleClass="content-field">
							  <html:option value=''><cms:contentText key="SELECT_ONE" code="system.general"/></html:option>
							  <html:options collection="secretQuestionList" property="code" labelProperty="name"  />
							</html:select>
				        </td>              
          			</tr>								
							
							
					<tr class="form-row-spacer">				  
			            <beacon:label property="secretAnswer">SIMI
			             	<cms:contentText key="SECRET_ANSWER" code="participant.participant"/>
			            </beacon:label>	
			            <td class="content-field">		  		  								
							<html:text property="secretAnswer" size="30" maxlength="250" styleClass="content-field"/>
				        </td>              
          			</tr>								
						 --%>
					<%-- END Web Site Information --%>
							
					<tr class="form-blank-row">
			           <td></td>
			        </tr>	
							
					<%-- START Employment Information --%>
					<tr class="form-row-spacer">
						<td class="subheadline" colspan="3">
							<cms:contentText key="EMPLOYMENT_HEADER" code="participant.participant"/>
						</td>
					</tr>	
					
					<tr class="form-row-spacer">				  
			            <beacon:label property="employerId">
			             	<cms:contentText key="EMPLOYER" code="participant.participant"/>
			            </beacon:label>	
			            <td class="content-field">		  		  															
							<html:select property="employerId" styleClass="content-field">
								<html:option value=''><cms:contentText key="SELECT_ONE" code="system.general"/></html:option>
						        <html:options collection="employerList" property="id" labelProperty="name"  />
							</html:select>
				        </td>              
          			</tr>								
						
			        
			        <tr class="form-row-spacer">				  
			            <beacon:label property="position">
			             	<cms:contentText key="JOB_POSITION" code="participant.participant"/>
			            </beacon:label>	
			            <td class="content-field">		  		  																						
							<html:select property="position" styleClass="content-field">
								<html:option value=''><cms:contentText key="SELECT_ONE" code="system.general"/></html:option>
								<html:options collection="jobPositionList" property="code" labelProperty="name"  />
							</html:select>
				        </td>              
          			</tr>								
						
			        
					<tr class="form-row-spacer">				  
			            <beacon:label property="department">
			             	<cms:contentText key="DEPARTMENT" code="participant.participant"/>
			            </beacon:label>	
			            <td class="content-field">		  		  																						
							<html:select property="department" styleClass="content-field">
								<html:option value=''><cms:contentText key="SELECT_ONE" code="system.general"/></html:option>
								<html:options collection="departmentList" property="code" labelProperty="name"  />
							</html:select>
				        </td>              
          			</tr>								
										        	
							
									
					<tr class="form-row-spacer">				  
			            <beacon:label property="hireDate">
			              <cms:contentText key="HIRE_DATE" code="participant.participant"/>
			            </beacon:label>	
			            <td class="content-field">		  		  				             
			              <html:text property="hireDate" styleId="hireDate" size="10" maxlength="10" styleClass="content-field" onfocus="clearDateMask(this);" readonly="true"/>
			                <img id="hireDateTrigger" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" alt="<cms:contentText key="HIRE_DATE" code="participant.participant"/>"/>
			            </td>              
          			</tr>		
          			

				
					<tr class="form-row-spacer">				  
			            <beacon:label property="terminationDate">
			              <cms:contentText key="TERMINATION_DATE" code="participant.participant"/>
			            </beacon:label>	
			            <td class="content-field">		  		  				             
			              <html:text property="terminationDate" styleId="terminationDate" size="10" maxlength="10" styleClass="content-field" readonly="true" onfocus="clearDateMask(this);"/>
			                <img id="terminationDateTrigger" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" alt="<cms:contentText key="TERMINATION_DATE" code="participant.participant"/>"/>
			            </td>              
          			</tr>		
          			
							
					<%-- END Employment Information --%>

					<tr class="form-blank-row">
			           <td></td>
			        </tr>

					<%-- START Hierarchy Information --%>
					<tr class="form-row-spacer">
						<td class="subheadline" colspan="3">
							<cms:contentText key="HIERARCHY_HEADER" code="participant.participant"/>
						</td>
					</tr>

					<tr class="form-row-spacer">				  
			            <beacon:label property="nameOfNode" required="true" styleClass="nowrap">
			              <cms:contentText key="HIERARCHY_NODE" code="participant.participant"/>
			            </beacon:label>	
			            <td class="content-field" nowrap>
										<beacon:client-state>
											<beacon:client-state-entry name="nodeId" value="${userForm.nodeId}"/>
										</beacon:client-state>	
							<html:text property="nameOfNode" size="30" styleClass="content-field"/>							
			            </td>              
          			</tr>		
          			<tr class="form-row-spacer">				  
          				<td></td>
          				<td></td>
			            <td class="content-field">		  		  				             			             	
							<a href="javascript: setActionDispatchAndSubmit('participantDisplay.do','prepareNodeLookup');" class="crud-content-link">
							  <cms:contentText key="LOOKUP_NODE" code="participant.participant"/>
							</a>
			            </td>              
          			</tr>					
					
					
					<tr class="form-row-spacer">				  
			            <beacon:label property="nodeRelationship" required="true">
			              <cms:contentText key="NODE_RELATIONSHIP" code="participant.participant"/>
			            </beacon:label>	
			            <td class="content-field">		  		  				             			              
							<html:select property="nodeRelationship" styleClass="content-field">
								<html:options collection="nodeRelationshipList" property="code" labelProperty="name"  />
							</html:select>
			            </td>              
          			</tr>		
          			
					<%-- END Hierarchy Information --%>
					
					<tr class="form-blank-row">
			           <td></td>
			        </tr>	
							
					<%-- START Characteristic Information --%>
					<tr class="form-row-spacer">
						<td class="subheadline" colspan="3">
							<cms:contentText key="CHARACTERISTIC_HEADER" code="participant.participant"/>
						</td>
					</tr>
					
					<c:set var="characteristicType" scope="page" value="user" />
					<c:forEach items="${userForm.userCharacteristicValueList}" var="valueInfo" varStatus="status">
			          <%--	In order for the same page to use characteristicEntry.jspf for two different characteristic
				        	types, we need the iterated value to be named both ValueInfo and userCharacteristicValueInfo, so set
					        the userCharacteristicValueInfo bean to the valueInfo bean. Is there a tag way to do this rather than
					        using a scriptlet --%>
							<%pageContext.setAttribute("userCharacteristicValueInfo", pageContext.getAttribute("valueInfo")); %>
				            <%@ include file="/characteristic/characteristicEntry.jspf"%>
				            
			        </c:forEach>							
					<%-- END Characteristic Information --%>
				</table> <%-- end 2nd column table --%>
			</td>
		  </tr>
		</table>
		
		<table width="100%" cellpadding="3" cellspacing="1" >
			<tr class="form-buttonrow">            
                <td align="center">
                  <beacon:authorize ifNotGranted="LOGIN_AS">
                  	<html:submit styleClass="content-buttonstyle" onclick="clearDatesAndSetDispatch('create')">
						<cms:contentText code="system.button" key="SAVE" />
					</html:submit>
                  </beacon:authorize>
					<html:button  property=""  styleClass="content-buttonstyle" onclick="setDispatchToHome()">
						<cms:contentText code="system.button" key="CANCEL" />
					</html:button>  
                </td>	        
	    	</tr>
      	</table>
        
		</td>
     </tr>
  </table>
</html:form>       

<script type="text/javascript">
  Calendar.setup(
    {
      inputField  : "hireDate",         // ID of the input field
      ifFormat    : "${TinyMceDatePattern}",    // the date format
      button      : "hireDateTrigger"       // ID of the button
    }
  );
  Calendar.setup(
    {
      inputField  : "terminationDate",         // ID of the input field
      ifFormat    : "${TinyMceDatePattern}",    // the date format
      button      : "terminationDateTrigger"       // ID of the button
    }
  );
  Calendar.setup(
    {
      inputField  : "dateOfBirth",         // ID of the input field
      ifFormat    : "${TinyMceDatePattern}",    // the date format
      button      : "dateOfBirthTrigger"       // ID of the button
    }
  );
 
</script>
