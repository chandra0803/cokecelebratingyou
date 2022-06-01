<%--UI REFACTORED--%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.ui.product.ProductForm"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ include file="/include/taglib.jspf"%>


<script type="text/javascript">
<!--
	function refreshPage()
	{
		document.productForm.action = "productEdit.do";
		document.productForm.method.value="displayUpdate";
		document.productForm.submit();
	}
//-->
</script>

<html:form styleId="contentForm" action="/productEditSave" method="GET">
  <html:hidden property="method" value="update"/>
  <html:hidden property="version" />
  <html:hidden property="createdBy" />
  <html:hidden property="dateCreated" />
	<beacon:client-state>
		<beacon:client-state-entry name="id" value="${productForm.id}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="TITLE_EDIT" code="product.maintainproducts"/></span>

        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="INSTRUCTION_EDIT" code="product.maintainproducts"/>
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
	          <html:select property="productCategoryId" styleClass="content-field" onchange="refreshPage();">
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
	          <html:select property="subProductCategoryId" styleClass="content-field">
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
			  <html:textarea property="description" styleClass="content-field" rows="5" cols="35"/>
			</td>
          </tr>

          <tr class="form-blank-row">
            <td></td>
          </tr>

         <tr class="form-buttonrow">
           <td></td>
           <td></td>
           <td align="left">
             <beacon:authorize ifNotGranted="LOGIN_AS">
	          <html:submit styleClass="content-buttonstyle" onclick="setDispatch('update')">
	            <cms:contentText code="system.button" key="SAVE" />
	          </html:submit>
            </beacon:authorize>
							<%	Map parameterMap = new HashMap();
									ProductForm temp = (ProductForm)request.getAttribute("productForm");
									if( temp.getSubProductCategoryId() == null )
									{
										parameterMap.put( "productCategoryId", temp.getProductCategoryId() );
									}
									else
									{
										parameterMap.put( "parentCategoryId", temp.getProductCategoryId() );
										parameterMap.put( "productCategoryId", temp.getSubProductCategoryId() );
									}
									pageContext.setAttribute("libraryUrl", ClientStateUtils.generateEncodedLink( "", "productLibrary.do?method=prepareDisplay", parameterMap ) );
							%>
						  <html:button property="back" styleClass="content-buttonstyle" onclick="callUrl('${libraryUrl}')" >
								<cms:contentText key="CANCEL" code="system.button"/>
			  			</html:button>
           </td>
         </tr>
        </table>
       </td>
     </tr>
   </table>
</html:form>
