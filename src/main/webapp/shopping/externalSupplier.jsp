<%@ include file="/include/taglib.jspf"%>

<script type="text/javascript">
<!--
window.onload=postExternalSupplier;

function postExternalSupplier(  )
{
	// url can be empty only if set up is wrong. handling error condition so that form is not submitted in infinte loop
	if ( document.externalSupplierForm.actionURL.value != '' )
	{
		document.externalSupplierForm.action = document.externalSupplierForm.actionURL.value;
		document.externalSupplierForm.submit();
	}
}

//-->
</script>
<html:form styleId="contentForm" action="externalSupplier">

<table border="0" cellpadding="3" cellspacing="1">
	<tr>
		<td>
		<cms:contentText key="LOADING" code="home.shopping.details"/>
		<c:choose>
		  <c:when test="${externalSupplierForm.allowPartnerSso}">
		     <input type="hidden" name="BankAccountNumber" value="<c:out value='${externalSupplierForm.bankAccountNumber}' />" />
		     <input type="hidden" name="campaignCode" value="<c:out value='${externalSupplierForm.campaignCode}' />" />
		  </c:when>
		  <c:otherwise>
		     <input type="hidden" name="CampaignID" value="<c:out value='${externalSupplierForm.campaignID}' />" />
		  </c:otherwise>
		</c:choose>
		<input type="hidden" name="BankAccountSystemNumber" value="<c:out value='${externalSupplierForm.bankAccountSystemNumber}' />" />		
		<input type="hidden" name="LanguageCode" value="<c:out value='${externalSupplierForm.languageCode}' />" />
		<input type="hidden" name="TargetPageId" value="<c:out value='${externalSupplierForm.targetPageId}' />" />
		<input type="hidden" name="CountryCode" value="<c:out value='${externalSupplierForm.countryCode}' />" />
		<input type="hidden" name="ErrorPage" value="<c:out value='${externalSupplierForm.errorPage}' />" />
		<input type="hidden" name="AccessDeniedPage" value="<c:out value='${externalSupplierForm.accessDeniedPage}' />" />
		<input type="hidden" name="Charset" value="<c:out value='${externalSupplierForm.charset}' />" />
		<input type="hidden" name="EncryptionType" value="<c:out value='${externalSupplierForm.encryptionType}' />" />
				
		<html:hidden property="actionURL"/>
		<html:hidden property="method"/>		
		</td>
	</tr>
</table>

</html:form>
		


