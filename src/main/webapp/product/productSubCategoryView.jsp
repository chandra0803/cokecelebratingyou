<%--UI REFACTORED--%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.product.ProductCategory"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.domain.enums.BudgetOverrideableType" %>
<%@ page import="com.biperf.core.domain.enums.BudgetType" %>

<script type="text/javascript">
<!--
function callUrl( urlToCall )
{
	window.location=urlToCall;
}

//-->
</script>
<html:form styleId="contentForm" action="productSubCategoryMaintain">
	<html:hidden property="method" value=""/>
	<beacon:client-state>
		<beacon:client-state-entry name="parentCategoryId" value="${productCategoryForm.parentCategoryId}"/>
		<beacon:client-state-entry name="id" value="${productCategoryForm.id}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="VIEW_TITLE" code="product.productsubcategory.details"/></span>

        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
			<cms:contentText key="INSTRUCTION" code="product.productsubcategory.details"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
     	
        <cms:errors/>
        
		<table>	

          <tr class="form-row-spacer">
            <td class="content-field-label">
              <cms:contentText key="CAT_NAME" code="product.productsubcategory.details"/>
            </td>
            <td class="content-field-review">
              <c:out value="${productCategory.parentProductCategory.name}"/>		
            </td>
          </tr>	


          <tr class="form-row-spacer">
            <td class="content-field-label">
              <cms:contentText key="SUBCAT_NAME" code="product.productsubcategory.details"/>
            </td>
            <td class="content-field-review">
              <c:out value="${productCategory.name}"/>&nbsp;&nbsp; <%	Map parameterMap = new HashMap();
									ProductCategory temp = (ProductCategory)request.getAttribute("productCategory");
									parameterMap.put( "parentCategoryId", temp.getParentProductCategory().getId() );
									parameterMap.put( "parentCategoryName", temp.getParentProductCategory().getName() );
									parameterMap.put( "id", temp.getId() );
									pageContext.setAttribute("editUrl", ClientStateUtils.generateEncodedLink( "", "productSubCategoryMaintainDisplay.do?method=prepareUpdate", parameterMap ) );
							%> <beacon:authorize
								ifNotGranted="LOGIN_AS">
								<a href="<c:out value="${editUrl}"/>" class="content-link">
									<cms:contentText key="EDIT_LINK"
										code="product.productsubcategory.details" /> </a>
							</beacon:authorize>
				</td>
          </tr>	

           <tr class="form-row-spacer">
            <td class="content-field-label">
              <cms:contentText key="DESCRIPTION" code="product.productsubcategory.details"/>
            </td>
            <td class="content-field-review">
               <c:out value="${productCategory.description}"/>		
            </td>
          </tr>	

          <tr class="form-buttonrow">
            <td></td>
            <td align="left">
							<%	parameterMap.remove( "parentCategoryName" );
									parameterMap.remove( "id" );
									parameterMap.put( "parentCategoryId", temp.getParentProductCategory().getId() );
									parameterMap.put( "productCategoryId", temp.getId() );
									pageContext.setAttribute("productLibraryUrl", ClientStateUtils.generateEncodedLink( "", "productLibrary.do?method=search", parameterMap ) );
							%>
							<html:button property="add" styleClass="content-buttonstyle" onclick="callUrl('${productLibraryUrl}')">
								<cms:contentText key="VIEW_PROD" code="product.productsubcategory.details"/>
							</html:button>
							<%	Map paramMap = new HashMap();
									paramMap.put( "productCategoryId", temp.getParentProductCategory().getId() );
									pageContext.setAttribute("cancelUrl", ClientStateUtils.generateEncodedLink( "", "productCategoryDisplay.do", paramMap ) );
							%>
							<html:button property="add" styleClass="content-buttonstyle" onclick="callUrl('${cancelUrl}')">
								<cms:contentText key="BACK_CAT" code="product.productsubcategory.details"/>
							</html:button>									
            </td>
          </tr>
        </table>
      </td>
    </tr>        
  </table>
</html:form>