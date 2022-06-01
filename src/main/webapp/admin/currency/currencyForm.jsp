<%@ include file="/include/taglib.jspf" %>

<html:form styleId="contentForm" action="currencyMaintain.do?method=save">
  <html:hidden property="currencyId" />
   <html:hidden property="cmAssetName" />
  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline">
        	<c:choose>
        		<c:when test="${currencyForm.currencyId eq null or currencyForm.currencyId eq 0}"><cms:contentText key="CURRENCY_ADD" code="currency.label"/></c:when>
        		<c:otherwise><cms:contentText key="CURRENCY_EDIT" code="currency.label"/></c:otherwise>
        	</c:choose>
        </span>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="INFO" code="currency.label"/>
        </span>
        <br/><br/>        
        <cms:errors/>

		  <table>	    
			<tr class="form-row-spacer">
			  <td class="content-field-label"><cms:contentText key="CURRENCY_NAME" code="currency.label"/></td>
			  <td><html:text property="currencyName" maxlength="50"></html:text></td>
			</tr>
			<tr class="form-row-spacer">
			  <td class="content-field-label"><cms:contentText key="CURRENCY_CODE" code="currency.label"/></td>
		      <td>
		      	<c:choose>
        			<c:when test="${currencyForm.currencyId eq null or currencyForm.currencyId eq 0}"><html:text property="currencyCode" maxlength="3" /></c:when>
        			<c:otherwise><html:hidden property="currencyCode" /><c:out value="${currencyForm.currencyCode}"/></c:otherwise>
        		</c:choose>
		      </td>
		    </tr>  
		    <tr class="form-row-spacer">
			  <td class="content-field-label"><cms:contentText key="SYMBOL" code="currency.label"/></td>
		      <td><html:text property="currencySymbol" maxlength="5"></html:text></td>
		    </tr>
			<tr class="form-row-spacer">
			  <td class="content-field-label"><cms:contentText key="STATUS" code="currency.label"/></td>
			  <td class="content-field">
		        <html:select property="status">
			      <html:option value="active"><cms:contentText key="ACTIVE" code="currency.label"/></html:option>
			      <html:option value="inactive"><cms:contentText key="INACTIVE" code="currency.label"/></html:option>
			    </html:select>
			  </td>
			</tr>
			
		    <tr class="form-buttonrow">
		       <td></td>
		       <td align="left">
             		<beacon:authorize ifNotGranted="LOGIN_AS"><button class="content-buttonstyle"><cms:contentText key="SAVE" code="system.button"/></button></beacon:authorize>
					&nbsp;&nbsp;
					<html:cancel styleClass="content-buttonstyle"><cms:contentText key="CANCEL" code="system.button"/></html:cancel>
		       </td>
		     </tr>	    	    	    	    
          </table>
      </td>
     </tr>        
   </table>
</html:form>