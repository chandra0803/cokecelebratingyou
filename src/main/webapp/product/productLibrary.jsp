<%--UI REFACTORED--%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.product.Product"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<script type="text/javascript">
<!--
	function addProduct()
	{
		getContentForm().productCategoryId.value = getContentForm().categoryId.options[getContentForm().categoryId.selectedIndex].value;
		getContentForm().subProductCategoryId.value = getContentForm().subCategoryId.options[getContentForm().subCategoryId.selectedIndex].value;
		setActionDispatchAndSubmit('productAdd.do', 'displayCreate');
	}

    function maintainProductSubCategory( method, action )
    {
	document.productLibraryForm.method.value=method;	
	document.productLibraryForm.action = action;
	document.productLibraryForm.submit();
    }
//-->
</script>
<html:form styleId="contentForm" action="productLibrary">
<html:hidden property="method" value="unspecified"/>
<html:hidden property="productCategoryId" value=""/>
<html:hidden property="subProductCategoryId" value=""/>
  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="TITLE" code="product.library"/></span>
        <span id="quicklink-add"></span><script type="text/javascript">quicklink_display_add('<cms:contentText key="TITLE" code="product.library"/>','<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>'); </script>	  
        
        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="INSTRUCTION" code="product.library"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
     	
        <cms:errors/>       
        <table>
          <tr class="form-row-spacer">				  
            <beacon:label property="productName">
              <cms:contentText key="PRODUCT_NAME" code="product.library"/>
            </beacon:label>	
            <td class="content-field">
              <html:text property="productName" styleClass="content-field" size="50" maxlength="50"/>
            </td>
          </tr>
          
          <tr class="form-blank-row">
            <td></td>
          </tr>	        

		  <tr class="form-row-spacer">				  
            <beacon:label property="itemNumber">
              <cms:contentText key="ITEM_NUMBER" code="product.library"/>
            </beacon:label>	
            <td class="content-field">
              <html:text property="itemNumber" styleClass="content-field" size="50" maxlength="50"/>
            </td>
          </tr>
          <tr class="form-blank-row">
            <td></td>
          </tr>	           
		  <tr class="form-row-spacer">				  
            <beacon:label property="categoryId">
              <cms:contentText key="CATEGORY" code="product.library"/>
            </beacon:label>	
            <td class="content-field">
               <html:select property="categoryId" styleClass="content-field" onchange="maintainProductSubCategory('searchProductSubCategory','productSubCategoryList.do')">
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
            <beacon:label property="subCategoryId">
              <cms:contentText key="SUBCATEGORY" code="product.library"/>
            </beacon:label>	
            <td class="content-field">
              <html:select property="subCategoryId" styleClass="content-field">
			    <c:forEach items="${subCategoryList}" var="subCategory">
			      <html:option value="${subCategory.id}"><c:out value="${subCategory.name}"/></html:option>
			    </c:forEach>			  
			  </html:select>
			  &nbsp;&nbsp;
			  <html:submit styleClass="content-buttonstyle" onclick="setDispatch('search')">
				<cms:contentText key="SHOW_PRODUCTS" code="product.library"/>
			  </html:submit>	
       </td>
      </tr>                    
		  <tr class="form-blank-row">
            <td></td>
          </tr>	 
		       <table width="50%">
          		<tr>
            	  <td align="right">	
					<%  Map parameterMap = new HashMap();
							Product temp;
					%>
					<display:table defaultsort="1" defaultorder="ascending" name="productList" id="product" pagesize="20" sort="list" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>" >						
            <display:setProperty name="basic.msg.empty_list" >
              <tr class="crud-content" align="left"><td colspan="{0}">
                <cms:contentText key="NO_PRODUCT" code="product.library"/>
              </td></tr>
            </display:setProperty>
            <display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
						<display:column titleKey="product.library.PRODUCT_NAME" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true" sortProperty="name">
							<table width="200">
							<tr>
								<td align="left" class="crud-content">
									<%  temp = (Product)pageContext.getAttribute("product");
											parameterMap.put( "productId", temp.getId() );
											pageContext.setAttribute("viewUrl", ClientStateUtils.generateEncodedLink( "", "productViewDisplay.do?method=display", parameterMap ) );
									%>
							<beacon:authorize ifAnyGranted="PROJ_MGR,BI_ADMIN">									
						    	<a href="<c:out value="${viewUrl}"/>" class="crud-content-link">
						     </beacon:authorize>	
						    	  <c:out value="${product.name}"/>
						     <beacon:authorize ifAnyGranted="PROJ_MGR,BI_ADMIN">
						    	</a>	
						    </beacon:authorize>												
								</td>
								<td class="crud-content right-align">
									<beacon:authorize ifAnyGranted="PROJ_MGR,BI_ADMIN">
										<% pageContext.setAttribute("editUrl", ClientStateUtils.generateEncodedLink( "", "productEdit.do?method=displayUpdate", parameterMap ) ); %>
						    		<a href="<c:out value="${editUrl}"/>" class="crud-content-link">
						    		 	<cms:contentText code="system.link" key="EDIT"/>
						    		</a>
									</beacon:authorize>
								</td>
							</tr>
							</table>
				        </display:column>	 
				        <display:column property="code" titleKey="product.library.ITEM_NUMBER" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true"/>
				        <display:column titleKey="product.library.CATEGORY" headerClass="crud-table-header-row" class="crud-content left-align nowrap" sortable="true">
							<c:if test='${!empty product.productCategory.parentProductCategory}'>
								<c:out value="${product.productCategory.parentProductCategory.name}"/>
							</c:if>
							<c:if test='${empty product.productCategory.parentProductCategory}'>
								<c:out value="${product.productCategory.name}"/> 
							</c:if>					
				        </display:column>
				        <display:column titleKey="product.library.SUBCATEGORY" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true">
							<c:if test='${!empty product.productCategory.parentProductCategory}'>
								<c:out value="${product.productCategory.name}"/>
							</c:if>
							<c:if test='${empty product.productCategory.parentProductCategory}'>
								<c:out value=""/>
							</c:if>	
				        </display:column>		               
								<beacon:authorize ifAnyGranted="PROJ_MGR,BI_ADMIN">
					        <display:column titleKey="product.library.REMOVE" headerClass="crud-table-header-row" class="crud-content center-align">
				            <html:checkbox property="deleteProductIds" value="${product.id}" />
					        </display:column>	
								</beacon:authorize>
		    		</display:table>		
		    	  </td>
		       </tr>
				<beacon:authorize ifAnyGranted="PROJ_MGR,BI_ADMIN">
          <%--BUTTON ROWS --%>
          <tr class="form-buttonrow">
            <td>
              <table width="100%">
                <tr>
                  <td align="left">
                    <html:button property="add" styleClass="content-buttonstyle" onclick="addProduct()">
											<cms:contentText key="ADD" code="product.library"/>
										</html:button>	
                  </td>
                 	<c:if test="${!empty productList}" >
                  <td align="right">
                    <html:submit styleClass="content-buttonstyle" onclick="setDispatch('delete')">
					  					<cms:contentText code="system.button" key="REMOVE_SELECTED" />
										</html:submit>			
                  </td>
                	</c:if>
                </tr>
                <tr>
                <c:if test="${!empty productList && !empty fn:trim(productCategoryId)}" >
                  <td align="left">
                  <%  Long id = (Long)request.getAttribute( "productCategoryId" );
                  	  parameterMap.put( "productCategoryId", id );
					  pageContext.setAttribute("backUrl", ClientStateUtils.generateEncodedLink( "", "productCategoryDisplay.do", parameterMap ) );
				  %>
                 <html:button property="add" styleClass="content-buttonstyle" onclick="callUrl('${backUrl}')">
					<cms:contentText key="BACK_CAT" code="product.productsubcategory.details"/>
				 </html:button>             

				  </td>
				  </c:if>
                </tr>
              </table>
	 					</td>
     			</tr>
				</beacon:authorize>
  	</table>
</html:form>