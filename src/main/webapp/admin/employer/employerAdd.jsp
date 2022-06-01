<%--UI REFACTORED--%>
<%@ include file="/include/taglib.jspf"%>

<script type="text/javascript">
<!--
function maintainEmployer( method, action )
{
	document.employerForm.method.value=method;	
	document.employerForm.action = action;
	document.employerForm.submit();
}
//-->
</script>
<html:form styleId="contentForm" action="employerMaintainCreate">

<html:hidden property="method" value="create"/>

<table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="ADD_HEADER" code="employer.list"/></span>
        
        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="ADD_INFO" code="employer.list"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
     	
        <cms:errors/>
        
		<table>
			<tr class="form-row-spacer">				  
	            <beacon:label property="employerName" required="true">
	              <cms:contentText key="NAME" code="employer.list"/>
	            </beacon:label>	
	            <td class="content-field">
	              <html:text property="employerName" size="40" maxlength="255" styleClass="content-field"/>
	            </td>
	        </tr>
	        
	          <%-- Needed between every regular row --%>
	          <tr class="form-blank-row">
	            <td></td>
	          </tr>
	          
	    <tr class="form-row-spacer">				  
            <beacon:label property="federalTaxId" required="false">
              <cms:contentText key="FED_TAX_ID" code="employer.list"/>
            </beacon:label>	
            <td class="content-field">
              <html:text property="federalTaxId" size="9" maxlength="9" styleClass="content-field"/>
            </td>
        </tr>
        
          <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>	  
          
        <tr class="form-row-spacer">				  
            <beacon:label property="stateTaxId" required="false">
              <cms:contentText key="STATE_TAX_ID" code="employer.list"/>
            </beacon:label>	
            <td class="content-field">
              <html:text property="stateTaxId" size="20" maxlength="20" styleClass="content-field"/>
            </td>
        </tr>
        
          <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>	
          
         <tr class="form-row-spacer">				  
            <beacon:label property="active" required="true">
              <cms:contentText key="STATUS" code="employer.list"/>
            </beacon:label>	
            <td class="content-field">
              <html:select property="active" size="1" styleClass="content-field">
				<html:option value="true" ><cms:contentText key="TRUE" code="employer.list"/></html:option>
				<html:option value="false" ><cms:contentText key="FALSE" code="employer.list"/></html:option>
			  </html:select>
            </td>
        </tr>
        
          <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>	
          
        <tr class="form-row-spacer">				  
            <beacon:label property="statusReason" required="false">
              <cms:contentText key="STATUS_REASON" code="employer.list"/>
            </beacon:label>	
            <td class="content-field">
              <html:text property="statusReason" size="40" maxlength="4000" styleClass="content-field"/>
            </td>
        </tr>
        
          <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>	 	   

    	<tr class="form-row-spacer">				  
            <beacon:label property="addressFormBean.countryCode" required="false">
              <cms:contentText key="COUNRTY_LABEL" code="employer.address"/>
            </beacon:label>	
            <td class="content-field">
              <html:select property="addressFormBean.countryCode" styleClass="content-field" onchange="maintainEmployer('changeCountryCreate','employerDisplay.do')">
				<c:if test="${multiple}">
					<html:option value=''><cms:contentText key="SELECT_ONE" code="system.general"/></html:option>
				</c:if>
				<html:options collection="countryList" property="countryCode" labelProperty="i18nCountryName"  />
				</html:select>
            </td>
        </tr>
        
          <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>	 

	
		  <tr class="form-row-spacer">				  
	            <beacon:label property="addr1" required="true">
	              <cms:contentText key="ADDR1_LABEL" code="employer.address"/>
	            </beacon:label>	
	            <td class="content-field">
	              <html:text property="addressFormBean.addr1" size="40" maxlength="100" styleClass="content-field"/>
	            </td>
	      </tr>
	        
	          <%-- Needed between every regular row --%>
	          <tr class="form-blank-row">
	            <td></td>
	          </tr>
	          
	      <tr class="form-row-spacer">				  
	            <beacon:label property="addr2" required="false">
	              <cms:contentText key="ADDR2_LABEL" code="employer.address"/>
	            </beacon:label>	
	            <td class="content-field">
	              <html:text property="addressFormBean.addr2" size="40" maxlength="100" styleClass="content-field"/>
	            </td>
	      </tr>
	        
	          <%-- Needed between every regular row --%>
	          <tr class="form-blank-row">
	            <td></td>
	          </tr>	
	          
	       <tr class="form-row-spacer">				  
	            <beacon:label property="addr3" required="false">
	              <cms:contentText key="ADDR3_LABEL" code="employer.address"/>
	            </beacon:label>	
	            <td class="content-field">
	              <html:text property="addressFormBean.addr3" size="40" maxlength="100" styleClass="content-field"/>
	            </td>
	      </tr>
	        
	          <%-- Needed between every regular row --%>
	          <tr class="form-blank-row">
	            <td></td>
	          </tr>
	          
	      <tr class="form-row-spacer">				  
	            <beacon:label property="city" required="true">
	              <cms:contentText key="CITY_LABEL" code="employer.address"/>
	            </beacon:label>	
	            <td class="content-field">
	              <html:text property="addressFormBean.city" size="40" maxlength="100" styleClass="content-field"/>
	            </td>
	      </tr>
	        
	          <%-- Needed between every regular row --%>
	          <tr class="form-blank-row">
	            <td></td>
	          </tr>	 		 	
	       <c:if test="${not empty stateList}">	 
	        <tr class="form-row-spacer">				  
	            <beacon:label property="stateTypeCode" required="true">
	              <cms:contentText key="STATE_LABEL" code="employer.address"/>
	            </beacon:label>	
	            <td class="content-field">
	              <html:select property="addressFormBean.stateTypeCode" styleClass="content-field">
				    <html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>
					<html:options collection="stateList" property="code" labelProperty="name"  />
				</html:select>
	            </td>
	        </tr>
	        
	          <%-- Needed between every regular row --%>
	          <tr class="form-blank-row">
	            <td></td>
	          </tr>      
	        </c:if>  
	        
	        <c:if test="${requirePostalCode=='true'}">	  
	       <tr class="form-row-spacer">				  
	            <beacon:label property="postalCode" required="true">
	              <cms:contentText key="POSTAL_CODE_LABEL" code="employer.address"/>
	            </beacon:label>	
	            <td class="content-field">
	              <html:text property="addressFormBean.postalCode" size="11" maxlength="11" styleClass="content-field"/>
	            </td>
	      </tr>
	        
	          <%-- Needed between every regular row --%>
	          <tr class="form-blank-row">
	            <td></td>
	          </tr>	 		 	   	 	
		
		</c:if>
	  <tr class="form-row-spacer">				  
           <beacon:label property="countryPhoneCode" >
			             	<cms:contentText key="TELEPHONE_COUNTRY_AND_COUNTRY_CODE" code="participant.participant"/>
			            </beacon:label>
            <td class="content-field">
              <html:select
					property="countryPhoneCode" styleClass="content-field">
					<%-- Bug Fix 23605 start --%>
					<c:if test="${multiple}">
						<html:option value=''>
							<cms:contentText key="SELECT_ONE" code="system.general" />
						</html:option>
					</c:if>
					<%-- Bug Fix 23605 end --%>
					<c:forEach items="${countryList}" var="country">
									<option value="${country.countryCode}" 
									<c:if test="${country.countryCode == employerForm.addressFormBean.countryCode}">
										selected 
									</c:if>									
										>${country.countryNameandPhoneCodeDisplay}</option>
					</c:forEach>
				</html:select>           
            </td>
      </tr>
      
      <tr class="form-row-spacer">				  
            <beacon:label property="phoneNumber" >
			             	<cms:contentText key="PHONE_NUMBER" code="participant.participant"/>
			            </beacon:label>
            <td class="content-field">               
              <html:text property="phoneNumber" size="8" maxlength="40" styleClass="content-field"/>           
            </td>
      </tr>
      
      <tr class="form-row-spacer">				  
            <beacon:label property="phoneExtension" >
			             	<cms:contentText key="EXTENSION" code="participant.participant"/>
			            </beacon:label>
            <td class="content-field">       
              <html:text property="phoneExtension" size="3" maxlength="40" styleClass="content-field"/>           
            </td>
      </tr>
        
  
      <%--BUTTON ROWS ... For Input--%>
          <tr class="form-buttonrow">
	    	<td></td>
	        <td></td>
	        <td align="left">
            <beacon:authorize ifNotGranted="LOGIN_AS">
	          	<html:submit styleClass="content-buttonstyle" onclick="setDispatch('create')">
		  			<cms:contentText code="system.button" key="SAVE" />
				</html:submit>
            </beacon:authorize>
				<html:cancel styleClass="content-buttonstyle">
				  <cms:contentText code="system.button" key="CANCEL" />
				</html:cancel>
	        </td>
          </tr>
	      <%--END BUTTON ROW--%>
    
	   </table>
        <%-- End Input Example  --%>
        
      </td>
    </tr>        
</table>
</html:form>
		