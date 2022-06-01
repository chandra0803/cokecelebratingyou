<%--UI REFACTORED--%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.domain.product.ProductCategory"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ include file="/include/taglib.jspf"%>

<script type="text/javascript">
<!--
function callUrl( urlToCall )
{
	window.location=urlToCall;
}

//-->
</script>

<html:form styleId="contentForm" action="productCategoryListMaintain">
<html:hidden property="method" value=""/>


  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="TITLE" code="product.productcategory.list"/></span>
        <span id="quicklink-add"></span><script type="text/javascript">quicklink_display_add('<cms:contentText key="TITLE" code="product.productcategory.list"/>','<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>'); </script>        

        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="INSTRUCTION" code="product.productcategory.list"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
     	
        <cms:errors/>
        
	    <table width="40%">
          <tr>
            <td align="right">
			<%  Map parameterMap = new HashMap();
					ProductCategory temp;
			%>
			<display:table defaultsort="1" defaultorder="ascending" name="productCategoryList" id="productCategory" pagesize="20" sort="list" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
			<display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
			</display:setProperty>
			<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
				<display:column titleKey="product.productcategory.list.CATEGORY" headerClass="crud-table-header-row" class="crud-content left-align nowrap" sortable="true" sortProperty="name">

							<%  temp = (ProductCategory)pageContext.getAttribute("productCategory");
									parameterMap.put( "productCategoryId", temp.getId() );
									pageContext.setAttribute("viewUrl", ClientStateUtils.generateEncodedLink( "", "productCategoryDisplay.do", parameterMap ) );
							%>
			    		<a href="<c:out value="${viewUrl}"/>" class="crud-content-link">
			    		  <c:out value="${productCategory.name}"/>
			    		</a>		
				</display:column>
				<display:column headerClass="crud-table-header-row" class="crud-content left-align nowrap" sortable="false">
					<beacon:authorize ifAnyGranted="PROJ_MGR,BI_ADMIN">
					
							<%	    temp = (ProductCategory)pageContext.getAttribute("productCategory");
									parameterMap.remove( "productCategoryId" );
									parameterMap.put( "id", temp.getId() );
									pageContext.setAttribute("editUrl", ClientStateUtils.generateEncodedLink( "", "productCategoryMaintainDisplay.do?method=prepareUpdate", parameterMap ) );
							%>
			    		<a href="<c:out value="${editUrl}"/>" class="content-link">
			    					<cms:contentText code="system.link" key="EDIT"/>
			    		</a>
					</beacon:authorize>	     
		        </display:column>
				<beacon:authorize ifAnyGranted="PROJ_MGR,BI_ADMIN">
					<display:column titleKey="product.productcategory.list.REMOVE" headerClass="crud-table-header-row" class="crud-content center-align">
		            	<html:checkbox property="deleteProductCategoryIds" value="${productCategory.id}" />
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
									<html:button property="add" styleClass="content-buttonstyle" onclick="callUrl('productCategoryMaintainDisplay.do?method=prepareCreate')">
										<cms:contentText code="product.productcategory.list" key="ADD" />
									</html:button>				
								</td>
								<td align="right">
									<html:submit styleClass="content-buttonstyle" onclick="setDispatch('delete')">
										<cms:contentText code="system.button" key="REMOVE_SELECTED" />
									</html:submit>						
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</beacon:authorize>
	</table>		    
		
	  </td>
	</tr>
</table>
</html:form>
		