<%--UI REFACTORED--%>
<%@ include file="/include/taglib.jspf"%>

<script type="text/javascript">
<!--
function callUrl( urlToCall )
{
	window.location=urlToCall;
}

//This method is only needed as long as Product Claim is the only available promotion type

//function enforceProductClaim( method ){
//	var isProductClaim = getContentForm().promotionType.value=="product_claim";
//	var isBlank = getContentForm().promotionType.value=="";
//
//	if(isProductClaim || isBlank){
//		//setActionAndDispatch( submitUrl, method);
//		setDispatch(method);
//		getContentForm().submit();	
//	}else{
//		alert("Product Claim is the only available selection at this time.");
//	}
//}

//-->
</script>

<html:form styleId="contentForm" action="submitPromotionWizardCreatePromotionType">
	<html:hidden property="method"/>
	
 <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="TITLE" code="promotion.promotiontype.list"/></span>
        <span id="quicklink-add"></span><script type="text/javascript">quicklink_display_add('<cms:contentText key="TITLE" code="promotion.promotiontype.list"/>','<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>'); </script>	  
        <br/><br/>

        <%--INSTRUCTIONS--%>
        
        <span class="content-instruction">
          <cms:contentText key="INSTRUCTIONS" code="promotion.promotiontype.list"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
     	
        <cms:errors/>
        
        <%-- Start Input Example  --%>
        <table>
          <tr class="form-row-spacer">					  
		  <beacon:label property="promotionType" required="true">
  			<cms:contentText key="PROMOTION_TYPE" code="promotion.promotiontype.list"/>
		  </beacon:label>	
		  <td class="content-field">
			<html:select property="promotionType" styleClass="content-field" >
			  <html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>	
			  <html:options collection="promotionTypeList" property="code" labelProperty="name"  />
			</html:select>
		  </td>
		</tr>
		<tr class="form-blank-row">
		  <td></td>
		</tr>		

	
		<%--BUTTON ROWS ... only display these two rows if there are buttons to be displayed.--%>
        <tr class="form-buttonrow">
	    	<td></td>
	        <td></td>
	        <td align="left">
            <beacon:authorize ifNotGranted="LOGIN_AS">
	          	<html:submit styleClass="content-buttonstyle" onclick="setDispatch('forward')">
				  <cms:contentText code="system.button" key="START" />
				</html:submit>

            </beacon:authorize>
		
	
				<html:cancel styleClass="content-buttonstyle" onclick="setDispatch('forward')">
					<cms:contentText code="system.button" key="CANCEL" />
				</html:cancel>
	        </td>
        </tr>
		<%--END BUTTON ROW--%>
	
   	   </table>
     </td>
   </tr>	
</table>
</html:form>
	
