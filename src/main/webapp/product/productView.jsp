<%--UI REFACTORED--%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.product.Product"%>
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
  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="HEADER" code="product.view"/></span>

        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="INSTRUCTION" code="product.view"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>

        <cms:errors/>

        <table>
          <tr class="form-row-spacer">
            <td class="content-field-label">
              <cms:contentText key="PRODUCT_NAME" code="product.view"/>
            </td>
            <td class="content-field-review">
							<%	Map parameterMap = new HashMap();
									Product temp = (Product)request.getAttribute("product");
									parameterMap.put( "productId", temp.getId() );
									pageContext.setAttribute("editUrl", ClientStateUtils.generateEncodedLink( "", "productEdit.do?method=displayUpdate", parameterMap ) );
							%>
				      <c:out value="${product.name}"/>&nbsp;&nbsp;
						  <a href="<c:out value="${editUrl}"/>" class="content-link">
			    			<cms:contentText key="EDIT_LINK" code="product.view"/>
			  			</a>
            </td>
          </tr>

          <tr class="form-row-spacer">
            <td class="content-field-label">
              <cms:contentText key="ITEM_NUMBER" code="product.view"/>
            </td>
            <td class="content-field-review">
              <c:out value="${product.code}"/>
            </td>
          </tr>
	        <tr class="form-row-spacer">
            <td class="content-field-label">
              <cms:contentText key="CATEGORY" code="product.view"/>
            </td>
            <td class="content-field-review">
              <c:choose>
    			<c:when test="${product.productCategory.parentProductCategory == null}">
	  				<c:out value="${product.productCategory.name}"/>
    			</c:when>
    			<c:otherwise>
	  				<c:out value="${product.productCategory.parentProductCategory.name}"/>
				</c:otherwise>
  			  </c:choose>
            </td>
          </tr>
          <tr class="form-row-spacer">
            <td class="content-field-label">
              <cms:contentText key="SUBCATEGORY" code="product.view"/>
            </td>
            <td class="content-field-review">
              <c:choose>
				<c:when test="${product.productCategory.parentProductCategory == null}">
				  &nbsp;
				</c:when>
			    <c:otherwise>
				  <c:out value="${product.productCategory.name}"/>
				</c:otherwise>
			  </c:choose>
            </td>
          </tr>

          <tr class="form-row-spacer">
            <td class="content-field-label">
              <cms:contentText key="DESCRIPTION" code="product.view"/>
            </td>
            <td class="content-field-review">
              <c:out value="${product.description}"/>
            </td>
          </tr>

          <tr class="form-row-spacer">
            <td class="content-field-label">&nbsp;</td>
          </tr>
         </table>

 		 <table width="50%">
          <tr>
            <td>
		      		<html:form styleId="contentForm" action="characteristicDisplayProduct">
		 	    		<html:hidden property="method" value=""/>
							<html:hidden property="version"/>
							<%-- Also include domainId as productId since on the common Characteristics pages, domainId is used, but when
									 going back to a product Library page, productId is used
							--%>
							<beacon:client-state>
								<beacon:client-state-entry name="domainId" value="${characteristicForm.domainId}"/>
								<beacon:client-state-entry name="productId" value="${characteristicForm.domainId}"/>
							</beacon:client-state>

				<%@ include file="/characteristic/characteristicList.jspf" %>

		      </html:form>

		    </td>
          </tr>
          
          <beacon:authorize ifNotGranted="LOGIN_AS">
          <tr class="form-row-spacer">
            <td class="content-field-label">&nbsp;</td>
          </tr>
          <tr>
		      <html:form styleId="contentForm3" action="productAddCharacteristicSave">
			 	    <html:hidden property="method" value="addExistingCharacteristic"/>
						<beacon:client-state>
							<beacon:client-state-entry name="id" value="${product.id}"/>
						</beacon:client-state>

				<td align="left" class="content-field-label">
					<cms:contentText key="ADD_EXISTING_PROD_CHAR" code="product.view"/>&nbsp;
			    	<html:select property="selectedCharacteristicId" styleClass="content-field">
			    	   <html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>
			           <html:options collection="availableProductCharaceristicTypes" property="id" labelProperty="characteristicName" />
			     	</html:select>&nbsp;
			        <html:submit styleClass="content-buttonstyle">
			          <cms:contentText key="GO" code="system.button"/>
			        </html:submit>
	            </td>
		      </html:form>
          </tr>
          </beacon:authorize>

        <html:form styleId="contentForm2" action="productLibrary">
  	  		<html:hidden property="method" value="delete"/>
					<beacon:client-state>
						<beacon:client-state-entry name="deleteProductIds" value="${product.id}"/>
					</beacon:client-state>

		 <tr class="form-buttonrow">
            <td>
              <table width="100%">
                <tr>
                  <td align="center" nowrap>
                    			<beacon:authorize ifNotGranted="LOGIN_AS">
                    			<html:submit styleClass="content-buttonstyle" onclick="setDispatchWithForm('delete', 'contentForm2')" >
    		      				  <cms:contentText key="DELETE_PRODUCT" code="product.view"/>
      							</html:submit>
      							</beacon:authorize>
										<%	parameterMap.remove( "id" );
												if( temp.getProductCategory().getParentProductCategory() == null )
												{
													parameterMap.put( "productCategoryId", temp.getProductCategory().getId() );
												}
												else
												{
													parameterMap.put( "parentCategoryId", temp.getProductCategory().getParentProductCategory().getId() );
													parameterMap.put( "productCategoryId", temp.getProductCategory().getId() );
												}
												pageContext.setAttribute("libraryUrl", ClientStateUtils.generateEncodedLink( "", "productLibrary.do?method=prepareDisplay", parameterMap ) );
										%>
					    			<html:button property="back" styleClass="content-buttonstyle" onclick="callUrl('${libraryUrl}')" >
				        			<cms:contentText key="BACK_TO_LIBRARY" code="product.view"/>
					    			</html:button>
                  </td>
                </tr>
              </table>
            </td>
          </tr>
  		</html:form>
  		</table>
  		</td>
     </tr>
  </table>
</table>