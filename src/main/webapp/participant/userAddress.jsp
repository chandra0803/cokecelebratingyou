<%--UI REFACTORED--%>
<%@ include file="/include/taglib.jspf"%>

<html:form styleId="contentForm" action="userAddressMaintainUpdate">
	<html:hidden property="method" value="update"/>
	<html:hidden property="addressType"/>
	<html:hidden property="addressTypeDesc"/>
	<html:hidden property="addressFormBean.countryCode"/>
	<html:hidden property="addressFormBean.countryName"/>
	<html:hidden property="version"/>
	<html:hidden property="dateCreated"/>
	<html:hidden property="createdBy"/>
	<html:hidden property="primary"/>
	<html:hidden property="rosterAddressId"/>	
	<beacon:client-state>
		<beacon:client-state-entry name="id" value="${userAddressForm.id}"/>
		<beacon:client-state-entry name="userId" value="${userAddressForm.userId}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="UPDATE_ADDR_TITLE" code="participant.address"/></span>
                
		<br/>
        <beacon:username userId="${displayNameUserId}"/>
        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="EDIT_INFO" code="participant.address"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
     	
        <cms:errors/>
     
  		<table>
          <tr class="form-row-spacer">				  
            <beacon:label property="addressType">
              <cms:contentText key="ADDR_TYPE_LABEL" code="participant.address"/>
            </beacon:label>	
            <td class="content-field-review">              
				<c:out value="${userAddressForm.addressTypeDesc}"/>
            </td>
          </tr>
 		  <tr class="form-blank-row">
            <td></td>
          </tr>	 
         <tr class="form-row-spacer">				  
            <beacon:label property="countryCode">
              <cms:contentText key="COUNRTY_LABEL" code="participant.address"/>
            </beacon:label>	
            <td class="content-field-review">              
				<c:out value="${userAddressForm.addressFormBean.countryName}"/>
            </td>
          </tr>
 		  <tr class="form-blank-row">
            <td></td>
          </tr>
	
         <tr class="form-row-spacer">				  
            <beacon:label property="addr1" required="true">
              <cms:contentText key="ADDR1_LABEL" code="participant.address"/>
            </beacon:label>	
            <td class="content-field">              
				<html:text property="addressFormBean.addr1" size="40" maxlength="100" styleClass="content-field"/>
            </td>
          </tr>
 		  <tr class="form-blank-row">
            <td></td>
          </tr>
	
         <tr class="form-row-spacer">				  
            <beacon:label property="addr2">
              <cms:contentText key="ADDR2_LABEL" code="participant.address"/>
            </beacon:label>	
            <td class="content-field">              
				<html:text property="addressFormBean.addr2" size="40" maxlength="100" styleClass="content-field"/>
            </td>
          </tr>
 		  <tr class="form-blank-row">
            <td></td>
          </tr>
	
	
         <tr class="form-row-spacer">				  
            <beacon:label property="addr3">
              <cms:contentText key="ADDR3_LABEL" code="participant.address"/>
            </beacon:label>	
            <td class="content-field">              
				<html:text property="addressFormBean.addr3" size="40" maxlength="100" styleClass="content-field"/>
            </td>
          </tr>
 		  <tr class="form-blank-row">
            <td></td>
          </tr>
	
	
         <tr class="form-row-spacer">				  
            <beacon:label property="city" required="true">
              <cms:contentText key="CITY_LABEL" code="participant.address"/>
            </beacon:label>	
            <td class="content-field">              
				<html:text property="addressFormBean.city" size="40" maxlength="30" styleClass="content-field"/>
            </td>
          </tr>
 		  <tr class="form-blank-row">
            <td></td>
          </tr>
	
	<c:if test="${not empty stateTypeList}">	
         <tr class="form-row-spacer">				  
            <beacon:label property="stateTypeCode" required="true">
              <cms:contentText key="STATE_LABEL" code="participant.address"/>
            </beacon:label>
            <td class="content-field">	
              <html:select property="addressFormBean.stateTypeCode" styleClass="content-field">
				<html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>
				<html:options collection="stateTypeList" property="code" labelProperty="name"  />
			  </html:select>
			</td>
          </tr>
 		  <tr class="form-blank-row">
            <td></td>
          </tr>
	</c:if>
	<c:if test="${requirePostalCode=='true'}">	
         <tr class="form-row-spacer">				  
            <beacon:label property="postalCode" required="true">
              <cms:contentText key="POSTAL_CODE_LABEL" code="participant.address"/>
            </beacon:label>
            <td class="content-field">	
              <html:text property="addressFormBean.postalCode" size="11" maxlength="11" styleClass="content-field"/>
            </td>
          </tr>
 		  <tr class="form-blank-row">
            <td></td>
          </tr>
	</c:if>

	
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
       
	