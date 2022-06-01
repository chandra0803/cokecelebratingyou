<%--UI REFACTORED--%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.enums.BudgetOverrideableType" %>
<%@ page import="com.biperf.core.domain.enums.BudgetType" %>
<%@ page import="com.biperf.core.domain.product.ProductCategory"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<script type="text/javascript">
<!--
	function callUrl( urlToCall )
	{
		window.location=urlToCall;
	}
//-->
function deleteCategory(){
	        var chkbox = document.productCategoryForm.deleteProductCategoryIds;
            var checkFound = false;
            if(chkbox!=null && chkbox.length!=null){
                  for (var counter=0; counter < chkbox.length; counter++) {
                        if(chkbox[counter].type.toUpperCase() == "CHECKBOX" && chkbox[counter].checked == true){
                              checkFound = true;
                        }
                  }
            }else if(chkbox!=null){
               // single checkbox.. access the checked property directly....
                    if(chkbox.checked){
                        checkFound = true;
                    }
            }
             if (checkFound == true) {
                document.productCategoryForm.method.value='delete';
				document.productCategoryForm.action ='<%= RequestUtils.getBaseURI(request) %>/product/productSubCategoryListMaintain.do';
				document.productCategoryForm.submit();
            }           
	

    }
</script>
<html:form styleId="contentForm" action="productSubCategoryListMaintain">
	<html:hidden property="method" value=""/>
	<beacon:client-state>
		<beacon:client-state-entry name="id" value="${productCategoryForm.id}"/>
		<beacon:client-state-entry name="parentCategoryId" value="${productCategoryForm.parentCategoryId}"/>
		<beacon:client-state-entry name="productCategoryId" value="${productCategoryForm.productCategoryId}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="VIEW_TITLE" code="product.productcategory.details"/></span>
        
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
            <td class="content-field-label">
				<cms:contentText key="NAME" code="product.productcategory.details"/>
            </td>
            <td class="content-field-review">
							<%  Map parameterMap = new HashMap();
								ProductCategory temp = (ProductCategory)request.getAttribute("productCategory");
								parameterMap.put( "id", temp.getId() );
								pageContext.setAttribute("editUrl", ClientStateUtils.generateEncodedLink( "", "productCategoryMaintainDisplay.do?method=prepareUpdate", parameterMap ) );
								parameterMap.put( "parentCategoryId", temp.getId() );
								parameterMap.put( "parentCategoryName", temp.getName() );
							%>
			  <c:out value="${productCategory.name}"/>
			  &nbsp;&nbsp;
			  <beacon:authorize ifAnyGranted="PROJ_MGR,BI_ADMIN">
					<a href="<c:out value="${editUrl}"/>" class="content-link">
			    	<cms:contentText key="EDIT_LINK" code="product.productcategory.details"/>
			  	</a>
				</beacon:authorize>		
            </td>
          </tr>	
          
		  <tr class="form-row-spacer">
            <td class="content-field-label">
              <cms:contentText key="DESCRIPTION" code="product.productcategory.details"/>
            </td>
            <td class="content-field-review">
              <c:out value="${productCategory.description}"/>			
            </td>
          </tr>					
			
		  <table width="40%">
            <tr>
              <td align="right">						
				<display:table defaultsort="1" defaultorder="ascending" name="subCategoryList" id="productSubCategory" pagesize="20" sort="list" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
				<display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
				  </display:setProperty>
				  <display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
					<display:column titleKey="product.productcategory.details.SUBCAT_NAME" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true" sortProperty="name">
						<table width="250">
						  <tr>
							<td align="left" class="crud-content">
								<%  	ProductCategory sub = (ProductCategory)pageContext.getAttribute( "productSubCategory" );
										parameterMap.put( "id", sub.getId() );
										pageContext.setAttribute("viewUrl", ClientStateUtils.generateEncodedLink( "", "productSubCategoryDisplay.do", parameterMap ) );
								%>
					    	<a href="<c:out value="${viewUrl}"/>" class="crud-content-link">
					    	  <c:out value="${productSubCategory.name}"/>
					    	</a>												
							</td>
							<td class="crud-content right-align">
								<% pageContext.setAttribute("subUrl", ClientStateUtils.generateEncodedLink( "", "productSubCategoryMaintainDisplay.do?method=prepareUpdate", parameterMap ) ); %>
					    	<beacon:authorize ifAnyGranted="PROJ_MGR,BI_ADMIN">
									<a href="<c:out value="${subUrl}"/>" class="crud-content-link">
								
					    		<cms:contentText key="EDIT_LINK" code="product.productcategory.details"/>
									</a>
								</beacon:authorize>
							</td>
						  </tr>
						</table>		      
				    </display:column>
						<beacon:authorize ifAnyGranted="PROJ_MGR,BI_ADMIN">
							<display:column titleKey="product.productcategory.details.REMOVE" headerClass="crud-table-header-row" class="crud-content center-align">
				      	<html:checkbox property="deleteProductCategoryIds" value="${productSubCategory.id}" />
				    	</display:column>
						</beacon:authorize>
				</display:table>	
			</td>
          </tr>
          <beacon:authorize ifAnyGranted="PROJ_MGR,BI_ADMIN">
						<tr class="form-buttonrow">
            	<td>
              	<table width="100%">
	              	<tr>
	                	<td align="left">
											<%  parameterMap.remove( "id" );
												pageContext.setAttribute("addUrl", ClientStateUtils.generateEncodedLink( "", "productSubCategoryMaintainDisplay.do?method=prepareCreate", parameterMap ) ); 
											%>
	                  	<html:button property="add" styleClass="content-buttonstyle" onclick="callUrl('${addUrl}')">
												<cms:contentText key="ADD_SUBCAT" code="product.productcategory.details"/>
					  					</html:button>	  
	                	</td>
	                	<td align="right">
	                   	<html:button styleClass="content-buttonstyle"  property="" onclick="javascript:deleteCategory();">
												<cms:contentText code="system.button" key="REMOVE_SELECTED" />
					   	</html:button>		    
	                	</td>
	              	</tr>
              	</table>
           		</td>
         		</tr>
					</beacon:authorize>
         <tr class="form-buttonrow">
            <td>
              <table width="100%">
	              <tr>
	                <td align="center" nowrap>
										<%  Map paramMap = new HashMap();
												paramMap.put( "productCategoryId", temp.getId() );
												pageContext.setAttribute("productLibraryUrl", ClientStateUtils.generateEncodedLink( "", "productLibrary.do?method=search", paramMap ) );
										%>
										<html:button property="add" styleClass="content-buttonstyle" onclick="callUrl('${productLibraryUrl}')">
											<cms:contentText key="VIEW_PROD" code="product.productcategory.details"/>
										</html:button>
						
										<html:button property="add" styleClass="content-buttonstyle" onclick="callUrl('productCategoryListDisplay.do')">
						  				<cms:contentText key="BACK_CAT" code="product.productcategory.details"/>
										</html:button>	  
	                </td>
	              </tr>
              </table>
           </td>
         </tr>
        </table>
		</td>
  </tr>
</table>
</html:form>