<%--UI REFACTORED--%>
<%@ include file="/include/taglib.jspf"%>

<html:form styleId="contentForm" action="userPhoneMaintainUpdate">
	<html:hidden property="method" />
	<html:hidden property="phoneType"/>
	<html:hidden property="phoneTypeDesc"/>
	<html:hidden property="version"/>
	<html:hidden property="dateCreated"/>
	<html:hidden property="createdBy"/>
	<html:hidden property="primary"/>
	<html:hidden property="fromPaxScreen"/>
	<html:hidden property="rosterPhoneId"/>	
	<html:hidden property="currentPhoneNbr" value="${userPhoneForm.phoneNbr}"/>
	<beacon:client-state>
		<beacon:client-state-entry name="id" value="${userPhoneForm.id}"/>
		<beacon:client-state-entry name="userId" value="${userPhoneForm.userId}"/>
	</beacon:client-state>

<table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="TITLE_UPDATE" code="participant.phone"/></span>
        
        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="ADD_INFO" code="participant.phone"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
     	
        <cms:errors/>
        
        <table>
        
          <tr class="form-row-spacer">
            <html:hidden property="verificationStatus"/>
            <beacon:label property="phoneType" required="false">
              <cms:contentText key="VERIFICATION" code="participant.phone.list"/>
            </beacon:label>	
            <td class="content-field">
            	<c:out value="${userPhoneForm.verificationStatusDesc}"/>
            </td>
          </tr>
          
          <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>	
          
          <tr class="form-row-spacer">				  
            <beacon:label property="verificationStatus" required="false">
              <cms:contentText key="PHONE_TYPE" code="participant.phone"/>
            </beacon:label>	
            <td class="content-field">
            	<c:out value="${userPhoneForm.phoneTypeDesc}"/>
            </td>
          </tr>
        
          <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>	
          
          <tr class="form-row-spacer">				  
             <beacon:label property="countryPhoneCode" >
			             	<cms:contentText key="TELEPHONE_COUNTRY_AND_COUNTRY_CODE" code="participant.participant"/>
			            </beacon:label>	
            <td class="content-field" nowrap>
            <html:select property="countryPhoneCode" styleClass="content-field">
															<%-- Bug Fix 23605 start --%>
															<c:if test="${multiple}">
																<html:option value=''><cms:contentText key="SELECT_ONE" code="system.general"/></html:option>
															</c:if>
															<%-- Bug Fix 23605 end --%>
															<html:options collection="countryList" property="countryCode" labelProperty="countryNameandPhoneCodeDisplay"  />
							</html:select>          
            </td>
          </tr>
          
            <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>	
          
          <tr class="form-row-spacer">				  
            <beacon:label property="phoneNbr" >
			             	<cms:contentText key="PHONE_NUMBER" code="participant.participant"/>
			            </beacon:label>
            <td class="content-field" nowrap>							
	            <html:text property="phoneNbr" size="8" styleClass="content-field"/>	            
            </td>
          </tr>
          
            <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>	
            <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>	
          
          <tr class="form-row-spacer">				  
            <beacon:label property="phoneExt" >
			             	<cms:contentText key="EXTENSION" code="participant.participant"/>
			            </beacon:label>	
            <td class="content-field" nowrap>     
	            <html:text property="phoneExt" size="5" styleClass="content-field"/>		        
            </td>
          </tr>
          
         
          
            <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>	
          
          
        <tr class="form-buttonrow">
	    	<td></td>
	        <td></td>
	        <td align="left">   
            <beacon:authorize ifNotGranted="LOGIN_AS">    		
				<html:submit styleClass="content-buttonstyle" onclick="setDispatch('update')">
					<cms:contentText code="system.button" key="SAVE" />
				</html:submit>				
				    </beacon:authorize>
				<html:cancel styleClass="content-buttonstyle">
					<cms:contentText code="system.button" key="CANCEL" />
				</html:cancel>
	        </td>
          </tr>   
        </table>
		</td>
	</tr>
</table>
</html:form>


		