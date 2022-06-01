<%--UI REFACTORED--%>
<%@ include file="/include/taglib.jspf" %>

<html:form styleId="contentForm" action="supplierCreate" focus="supplierName">
  <html:hidden property="method" />
  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="ADD_HEADER" code="admin.supplier.details"/></span>
        
        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="INSTRUCTION" code="admin.supplier.details"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
     	
        <cms:errors/>

	 	<table>
          <tr class="form-row-spacer">				  
            <beacon:label property="supplierName" required="true">
              <cms:contentText key="SUPPLIER_NAME" code="admin.supplier.details"/>
            </beacon:label>	
            <td class="content-field">
              <html:text property="supplierName" styleClass="content-field" size="35" maxlength="30"/>
            </td>
          </tr>

          <tr class="form-blank-row">
            <td></td>
          </tr>	        

          <tr class="form-row-spacer">				  
            <beacon:label property="description" styleClass="content-field-label-top">
              <cms:contentText key="DESCRIPTION" code="admin.supplier.details"/>
            </beacon:label>	
            <td class="content-field">
              <html:textarea property="description"  onkeyup="checkMaxlength(this)" styleClass="content-field" rows="5" cols="35" />
            </td>
          </tr>

          <tr class="form-blank-row">
            <td></td>
          </tr>	  
                                 
          <tr class="form-row-spacer">				  
            <beacon:label property="status" required="true">
              <cms:contentText key="STATUS" code="admin.supplier.details"/>
            </beacon:label>	
            <td class="content-field">
                <html:select property="status" styleClass="content-field">
		          <html:options collection="statusList" property="code" labelProperty="name"  />
        		</html:select>
            </td>
          </tr>

          <tr class="form-blank-row">
            <td></td>
          </tr>	

          <tr class="form-row-spacer">			
            <beacon:label property="supplierType" styleClass="content-field-label-top" required="true" >
              <cms:contentText key="SUPPLIER_TYPE" code="admin.supplier.details"/>
            </beacon:label>		
            <td class="content-field content-field-label-top">
              <html:radio property="supplierType" value="internal" onclick="allowPartnerSSO()" />&nbsp;&nbsp;<cms:contentText key="INTERNAL_TYPE" code="admin.supplier.details"/>
               <br/>
              <html:radio property="supplierType" value="external" onclick="allowPartnerSSO()"/>&nbsp;&nbsp;<cms:contentText key="EXTERNAL_TYPE" code="admin.supplier.details"/>
            </td>
          </tr> 
  
          <tr class="form-blank-row">
            <td></td>
          </tr>	
          
  		  <tr class="form-row-spacer">				  
            <beacon:label property="catalogUrl">
              <cms:contentText key="CATALOG_URL" code="admin.supplier.details"/>
            </beacon:label>	
            <td class="content-field">
				<html:text property="catalogUrl" styleClass="content-field" size="46" maxlength="250"/>
            </td>
          </tr>

          <tr class="form-blank-row">
            <td></td>
          </tr>	

          
  		  <tr class="form-row-spacer">				  
            <beacon:label property="catalogUrl">
				<cms:contentText key="CATALOG_TARGET_ID" code="admin.supplier.details"/>
            </beacon:label>	
            <td class="content-field">
				<html:text property="catalogTargetId" styleClass="content-field" size="35" maxlength="30"/>
            </td>
          </tr>

          <tr class="form-blank-row">
            <td></td>
          </tr>	
          
  		  <tr class="form-row-spacer">				  
            <beacon:label property="statementUrl">
				<cms:contentText key="STATEMENT_URL" code="admin.supplier.details"/>
            </beacon:label>	
            <td class="content-field">
				<html:text property="statementUrl" styleClass="content-field" size="46" maxlength="250"/>
            </td>
          </tr>

          <tr class="form-blank-row">
            <td></td>
          </tr>	

		  <tr class="form-row-spacer">				  
            <beacon:label property="statementTargetId">
				<cms:contentText key="STATEMENT_TARGET_ID" code="admin.supplier.details"/>
            </beacon:label>	
            <td class="content-field">
				<html:text property="statementTargetId" styleClass="content-field" size="35" maxlength="30"/>
            </td>
          </tr>
          
          <tr class="form-row-spacer">				  
            <beacon:label property="allowSso" required="false">
				<cms:contentText key="ALLOW_SSO" code="admin.country.details"/>
            </beacon:label>	
            <td class="content-field">
              <html:checkbox property="allowPartnerSso" value="TRUE" />
            </td>
          </tr>  

		  <tr class="form-buttonrow">
           <td></td>
           <td></td>
           <td align="left">
             <beacon:authorize ifNotGranted="LOGIN_AS">
           	<html:submit styleClass="content-buttonstyle" onclick="setDispatch('create')">
          		<cms:contentText key="SAVE" code="system.button"/>
		    </html:submit>
             </beacon:authorize>

            <html:cancel styleClass="content-buttonstyle">
          		<cms:contentText key="CANCEL" code="system.button"/>
        	</html:cancel>
           </td>
         </tr>
		</table>
		
   	  </td>
     </tr>        
   </table>
</html:form>

<script type="text/javascript">

function checkMaxlength(desc)
{
	if ( desc.value.length > 250 )
	{
      alert( "Maximum length of description is 250" );	  
	
	}
}

function allowPartnerSSO() 
{
	if ($("input:radio[name='supplierType']:checked").val() == "internal")
	{
		alert("HI");
		$("#allowSSo").html(' <input type="checkbox" name="allowPartnerSso" value="TRUE" class="rdochk" disabled="true">');
	}
	if ($("input:radio[name='supplierType']:checked").val() == "external")
	{
		$("#allowSSo").html(' <input type="checkbox" name="allowPartnerSso" value="TRUE" class="rdochk" >');
	}
}

</script>
