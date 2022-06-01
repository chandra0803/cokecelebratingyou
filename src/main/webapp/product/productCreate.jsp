<%--UI REFACTORED--%>
<%@ include file="/include/taglib.jspf"%>
<script type="text/javascript">
<!--
function refreshPage()
{
	document.productForm.action = "productAdd.do";
	document.productForm.method.value="displayCreate";
	document.productForm.submit();
}

//-->
</script>

<html:form styleId="contentForm" action="/productAddSave" method="GET">
  <html:hidden property="method" value="create"/>

	<table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="TITLE_ADD" code="product.maintainproducts"/></span>

        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="INSTRUCTION_ADD" code="product.maintainproducts"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>

        <cms:errors/>

        <table>
          <tr class="form-row-spacer">
            <beacon:label property="name" required="true">
              <cms:contentText key="PRODUCT_NAME_LABEL" code="product.maintainproducts"/>
            </beacon:label>
            <td class="content-field">
              <html:text property="name" styleClass="content-field" size="46"/>
            </td>
          </tr>

          <tr class="form-blank-row">
            <td></td>
          </tr>

          <tr class="form-row-spacer">
            <beacon:label property="code" required="true">
              <cms:contentText key="ITEM_NUMBER_LABEL" code="product.maintainproducts"/>
            </beacon:label>
            <td class="content-field">
              <html:text property="code" styleClass="content-field" size="46"/>
            </td>
          </tr>

          <tr class="form-blank-row">
            <td></td>
          </tr>

          <tr class="form-row-spacer">
            <beacon:label property="productCategoryId" required="true">
              <cms:contentText key="CATEGORY_LABEL" code="product.maintainproducts"/>
            </beacon:label>
            <td class="content-field">
              <html:select property="productCategoryId"  styleClass="content-field" onchange="refreshPage();">
          		<html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>
    	    	<html:options collection="categoryList" property="id" labelProperty="name"  />
       	  	  </html:select>
            </td>
          </tr>

          <tr class="form-blank-row">
            <td></td>
          </tr>


          <tr class="form-row-spacer">
            <beacon:label property="subProductCategoryId">
              <cms:contentText key="SUBCATEGORY_LABEL" code="product.maintainproducts"/>
            </beacon:label>
            <td class="content-field">
          		<html:select property="subProductCategoryId"  styleClass="content-field" >
            		<html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>
    	    		<html:options collection="subCategoryList" property="id" labelProperty="name"  />
       	  		</html:select>
            </td>
          </tr>

          <tr class="form-blank-row">
            <td></td>
          </tr>

      	  <tr class="form-row-spacer">
            <beacon:label property="description" styleClass="content-field-label-top">
               <cms:contentText key="DESCRIPTION_LABEL" code="product.maintainproducts"/>
            </beacon:label>
            <td class="content-field">
              <html:textarea property="description"  styleClass="content-field"  rows="5" cols="35"/>
            </td>
          </tr>

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
       </table>

      </td>
     </tr>
   </table>
</html:form>
