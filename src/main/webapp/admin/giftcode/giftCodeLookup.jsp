<%--UI REFACTORED--%>
<%@ include file="/include/taglib.jspf" %>

<html:form styleId="contentForm" action="giftCodeLookup">
  <html:hidden property="method" />
  
  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="HEADING" code="giftcode.lookup"/></span>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="INFO" code="giftcode.lookup"/>
        </span>
        <br/><br/>

        <cms:errors/>  
  
		  <table>
		    <tr class="form-row-spacer">				  
		      <beacon:label property="entityName" required="true">
		        <cms:contentText key="GIFT_CODE" code="giftcode.lookup"/>
		      </beacon:label>	
		      <td class="content-field">
		        <html:text name="giftCodeLookupForm" property="giftCode" styleClass="content-field" size="25"/>
		        
		        <html:submit styleClass="content-buttonstyle" onclick="setDispatch('searchByGiftCode')">
				  <cms:contentText key="SEARCH" code="system.button"/>
				</html:submit>
				
		      </td>
		    </tr>
		    
		    <tr class="form-row-spacer">				  
		      <beacon:label property="key" required="true">
		        <cms:contentText key="REFERENCE_NUMBER" code="giftcode.lookup"/>
		      </beacon:label>	
		      <td class="content-field">
		        <html:text name="giftCodeLookupForm" property="referenceNumber" styleClass="content-field" size="25"/>
		        
		        <html:submit styleClass="content-buttonstyle" onclick="setDispatch('searchByReferenceNumber')">
				  <cms:contentText key="SEARCH" code="system.button"/>
				</html:submit>
		        
		      </td>
		    </tr>
		    
		<c:if test="${ giftCodeLookupForm.merchOrder!=null }">
		    <tr class="form-row-spacer">			
		      <beacon:label property="typeCode" required="true" styleClass="content-field-label-top">
		        <cms:contentText key="LEVEL" code="giftcode.lookup"/>
		      </beacon:label>	        
		      <td class="content-field">
		         <c:choose>
		          <c:when test="${ giftCodeLookupForm.merchOrder.claim != null or giftCodeLookupForm.merchOrder.promoMerchProgramLevel != null }">
		            <cms:contentText key="LEVEL_NAME" code="${ giftCodeLookupForm.levelCMKey }" />
		          </c:when>
		          <c:otherwise>
		            <c:out value="${giftCodeLookupForm.levelCMKey}"/>
		          </c:otherwise>
		        </c:choose>
		      </td>  	
		    </tr>
		       	 
		    <tr class="form-row-spacer">			
		      <beacon:label property="typeCode" required="true" styleClass="content-field-label-top">
		        <cms:contentText key="STATUS" code="giftcode.lookup"/>
		      </beacon:label>	        
		      <td class="content-field">
		        <c:choose>
		        	<c:when test="${ giftCodeLookupForm.redeemed }"><cms:contentText key="REDEEMED" code="giftcode.lookup"/></c:when>
		        	<c:otherwise><cms:contentText key="UN_REDEEMED" code="giftcode.lookup"/></c:otherwise>
		        </c:choose>
		      </td>  	
		    </tr>
		       
		    <tr class="form-row-spacer">				  
		      <beacon:label property="stringVal" required="true" styleClass="content-field-label-top">
		        <cms:contentText key="ISSUED_TO" code="giftcode.lookup"/>
		      </beacon:label>	
		      <td class="content-field">
			  	<c:out value="${ giftCodeLookupForm.recipientLastName  }"/>, <c:out value="${ giftCodeLookupForm.recipientFirstName  }"/>
		      </td>
		    </tr>
		    
		    <tr class="form-row-spacer">				  
		      <beacon:label property="stringVal" required="true" styleClass="content-field-label-top">
		        <cms:contentText key="ISSUED_DATE" code="giftcode.lookup"/>
		      </beacon:label>	
		      <td class="content-field">
			  	<fmt:formatDate value="${ giftCodeLookupForm.issuedDate }" pattern="${JstlDatePattern}" />
		      </td>
		    </tr>
		</c:if>
          </table>
      </td>
     </tr>        
   </table>
</html:form>