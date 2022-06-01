<%-- UI REFACTORED --%>
<%@ include file="/include/taglib.jspf"%>

<script type="text/javascript">
<!--
function callUrl( urlToCall )
{
	window.location=urlToCall;
}

function continueSubmit(target)
{
  if(document.productCategorySearchForm.categoryId.selectedIndex == 0){
    alert("You have not selected any products.  Please select a product or click cancel to return without selecting a product.");
    return false;
  } else {
    setDispatchAndSubmit(target);
    return true;
  }
}
//-->
</script>

<html:form styleId="contentForm" action="productCategorySearch">
  <html:hidden property="method"/>
  <html:hidden property="returnActionUrl"/>
  
 <c:forEach var='item' items='${productCategorySearchForm.paramsMap}'>
  <html:hidden property="params(${item.key})"/>
 </c:forEach>
 
 
  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText code="product.category.search" key="TITLE" /></span>

        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText code="product.category.search" key="INSTRUCTION" />
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
     	
        <cms:errors/>
        
    	<table>
	      <tr class="form-row-spacer">				  
            <beacon:label property="categoryId" >
              <cms:contentText code="product.category.search" key="CATEGORY" />
            </beacon:label>	
            <td class="content-field">
              <html:select property="categoryId" styleClass="content-field" onchange="javascript:document.productCategorySearchForm.submit();">
			    <c:forEach items="${categoryList}" var="category">
			      <html:option value="${category.id}"><c:out value="${category.name}"/></html:option>
			    </c:forEach>			  
			  </html:select>
            </td>
          </tr>
          
          <tr class="form-blank-row">
            <td></td>
          </tr>
          
	      <tr class="form-row-spacer">				  
            <beacon:label property="subCategoryId" >
              <cms:contentText code="product.category.search" key="SUBCATEGORY" />
            </beacon:label>	
            <td class="content-field">
               <html:select property="subCategoryId" styleClass="content-field">
			    <c:forEach items="${subCategoryList}" var="subCategory">
			      <html:option value="${subCategory.id}"><c:out value="${subCategory.name}"/></html:option>
			    </c:forEach>			  
			  </html:select>
            </td>
          </tr>
    
	      <tr class="form-buttonrow">
           <td></td>
           <td></td>
           <td align="left">
             <html:button property="continue" styleClass="content-buttonstyle" onclick="continueSubmit('continueBack');">
			    <cms:contentText code="system.button" key="CONTINUE" />
			  </html:button>

              <c:url var="url" value="${productCategorySearchForm.returnActionUrl}" >
			   <c:forEach var='item' items='${productCategorySearchForm.paramsMap}'>
				<c:param name="${item.key}" value="${item.value}" />
				<c:param name="cancelled" value="true" />
			   </c:forEach>					
			  </c:url>
			  <html:cancel styleClass="content-buttonstyle" onclick="setActionAndDispatch('${url}','');">
				<cms:contentText code="system.button" key="CANCEL" />
			  </html:cancel>
           </td>
         </tr>
	
		</table>
		
	  </td>
	</tr>
  </table>
</html:form>
