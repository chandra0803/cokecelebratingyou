<%--UI REFACTORED--%>
<%@ include file="/include/taglib.jspf"%>

<script type="text/javascript">
<!--
function callUrl( urlToCall )
{
	window.location=urlToCall;
}

//-->
</script>

<html:form styleId="contentForm" action="productCategoryMaintain">
	<html:hidden property="method"/>
	<beacon:client-state>
		<beacon:client-state-entry name="id" value="${productCategoryForm.id}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline">
           	<c:if test='${productCategoryForm.id == null || v.id == 0}'>
				<cms:contentText key="ADD_TITLE" code="product.productcategory.details"/>
			</c:if>
			<c:if test='${productCategoryForm.id != null && productCategoryForm.id > 0}'>
				<cms:contentText key="EDIT_TITLE" code="product.productcategory.details"/>
			</c:if>	
        </span>
        
        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="INSTRUCTION" code="product.productcategory.details"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
     	
        <cms:errors/>

        <table>
          <tr class="form-row-spacer">				  
            <beacon:label property="name" required="true">
              <cms:contentText key="NAME" code="product.productcategory.details"/>
            </beacon:label>	
            <td class="content-field">
              <html:text property="name" styleClass="content-field" size="40" maxlength="50"/>                         
            </td>
          </tr>

          <tr class="form-blank-row">
            <td></td>
          </tr>	        
        
 		  <tr class="form-row-spacer">				  
            <beacon:label property="description" styleClass="content-field-label-top">
              <cms:contentText key="DESCRIPTION" code="product.productcategory.details"/>
            </beacon:label>	
            <td class="content-field">
              <html:textarea property="description"  styleClass="content-field"  rows="6" cols="40"/>
            </td>
          </tr>

         <tr class="form-buttonrow">
           <td></td>
           <td></td>
           <td align="left">
            <beacon:authorize ifNotGranted="LOGIN_AS">
            <c:choose>
              <c:when test='${productCategoryForm.id == null || productCategoryForm.id == 0}'>
		  		<html:submit styleClass="content-buttonstyle" onclick="setDispatch('create')">
	  				<cms:contentText code="system.button" key="SAVE" />
  				</html:submit>
			  </c:when>
              <c:otherwise>
              	<html:submit styleClass="content-buttonstyle" onclick="setDispatch('update')">
                	<cms:contentText code="system.button" key="SAVE" />
              	</html:submit>
              </c:otherwise>
            </c:choose>
            </beacon:authorize>
            
			<html:button property="cancel" styleClass="content-buttonstyle" onclick="callUrl('productCategoryListDisplay.do')">
			 	<cms:contentText code="system.button" key="CANCEL" />
		  	</html:button>
           </td>
         </tr>
	    </table>
        
      </td>
     </tr>        
   </table>
</html:form>

