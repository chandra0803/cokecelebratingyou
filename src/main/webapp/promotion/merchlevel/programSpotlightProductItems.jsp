<%@ page import="com.biperf.awardslinqDataRetriever.client.SpotlightProduct"%>
<%@ page import="com.biperf.awardslinqDataRetriever.client.OMProduct"%>
<%@ include file="/include/taglib.jspf"%>
<%@ include file="/include/yui-imports.jspf"%>

<script src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/cards/cards.js" type="text/javascript"></script>
<script src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/tooltip.js" type="text/javascript"></script>
<script src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/modal/popaction.js" type="text/javascript"></script>
<script src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/modal/common.js" type="text/javascript"></script>
<script type="text/javascript">
<!--
function returnSelectionResults()
{ 
  var selectedProductId = document.getElementsByName("selectedProductId");
  var selectedProductDescs = document.getElementsByName("selectedProductDesc");
  var selectedProductThumbNailImgs = document.getElementsByName("selectedProductThumbNailImg");
  var selectedProductCopys = document.getElementsByName("selectedProductCopy");
  var selectedProductDetailImgs = document.getElementsByName("selectedProductDetailImg");
  var selectedProductPrices = document.getElementsByName("selectedProductPrice");
  var selectedGiftables = document.getElementsByName("selectedGiftable");
  var selectedWrapTypes = document.getElementsByName("selectedWrapType");
  var selectedCardables = document.getElementsByName("selectedCardable");
  var selectedFastShippable = document.getElementsByName("selectedFastShippable");
   
  if (selectedProductId != '')
  {
    window.parent.itemIds.value = selectedProductId[0].value;
    window.parent.itemDescs.value = selectedProductDescs[0].value;
    window.parent.itemThumbImgURLs.value = selectedProductThumbNailImgs[0].value;
    window.parent.itemCopys.value = selectedProductCopys[0].value;
    window.parent.itemDetailImgURLs.value = selectedProductDetailImgs[0].value;
    window.parent.itemPrices.value = selectedProductPrices[0].value;
    window.parent.itemGiftables.value = selectedGiftables[0].value;
    window.parent.itemWrapTypes.value = selectedWrapTypes[0].value;
    window.parent.itemCardables.value = selectedCardables[0].value;
    window.parent.itemFastShippable.value = selectedFastShippable[0].value;
    window.parent.refreshData();
   }
  
  window.parent.closeModalPopup();
  //window.close();
}

function setFormValue(elementName, value)
{
    var element = document.getElementsByName(elementName);
    if(element != null)
    {
        element[0].value = value;
    }
}
//-->
</script>
<style>
 
td.card { border: 1px solid #FFFFFF }

</style>
<c:set var="enabled" value="${param.enabled}"/>
<form name="programSpotlightProductItems">
<table border="0" cellpadding="3" cellspacing="1" width="100%" >
  <input type="hidden" name="selectedProductId">
  <input type="hidden" name="selectedProductDesc">
  <input type="hidden" name="selectedProductThumbNailImg">
  <input type="hidden" name="selectedProductCopy">
  <input type="hidden" name="selectedProductDetailImg">
  <input type="hidden" name="selectedProductPrice">
  <input type="hidden" name="selectedGiftable">
  <input type="hidden" name="selectedWrapType">
  <input type="hidden" name="selectedCardable">
  <input type="hidden" name="selectedFastShippable">
  <tr>
    <td width="65%" height="100%" valign="top">
		<table border="0" align="center" cellpadding="5">
			<tr>
				<td>
					<span class="headline">
				    <cms:contentText code="recognition.merchandise" key="VIEW_AWARD_TITLE" /></span>

					<c:if test="${lastName != null }">
						<br/><c:out value="${lastName}"/>
						<c:if test="${firstName != null}">,&nbsp;<c:out value="${firstName}"/></c:if>
					</c:if>
					<c:if test="${country != null }">
						<br/><cms:contentText code="${country.cmAssetCode}" key="COUNTRY_NAME" />
					</c:if>
					<%-- define the number of columns --%>
					<c:set var="columns" value="4" />
					 
					<c:if test="${ spotlightProductData != null }">
						<%long counter=0; 
						  long itemsCounter=0;
						  long columns = 4; %>
						
						<table border="0" align="center" width="90%" cellpadding="5" cellspacing="0">	
							<tr>										
								<c:choose>
									<c:when test="${ spotlightProductData.spotlightProducts != null && !empty spotlightProductData.spotlightProducts }">										 
										<c:forEach items="${ spotlightProductData.spotlightProducts }" var="product" varStatus="status">	
											<%
											  boolean showDetail = false;
											  String productId = "";
											  String productPrice = "";
											  OMProduct omProduct = null;
											  SpotlightProduct product = (SpotlightProduct)pageContext.getAttribute("product");
											  java.util.Collection productIds = product.getOmProducts();
											  if(productIds != null && productIds.size()==1){
											    if(productIds.iterator().hasNext()){
											        omProduct = (OMProduct)productIds.iterator().next();
											        productId = omProduct.getProductId();
											        productPrice = " $"+omProduct.getPrice() / 100+" ";
											    }
											  	showDetail = true;
											  }
											  else{
											    showDetail = false;
											  }
											  pageContext.setAttribute("showDetail",new Boolean(showDetail));
											  pageContext.setAttribute("productId",productId);
											  pageContext.setAttribute("productPrice",productPrice);
											%>
 											<c:if test="${showDetail}">
  												<td id="product_<c:out value="${product.productSetId }" />" class="card" align="center" valign="top" width="<c:out value="${ 100/columns }"/>%"  height="195">
                                                   <table width=100%" height="100%">
                                                     <tr valign="top">
                                                       <td>		  												
														<div id="desc_<c:out value="${product.productSetId }" />">
														<c:if test="${ enabled}">
															<a onclick="setFormValue('selectedProductId','<beacon:toJavascriptString><c:out value="${productId}"/></beacon:toJavascriptString>');setFormValue('selectedProductDesc','<beacon:toJavascriptString><c:out escapeXml="false" value="${product.productDescription}"/></beacon:toJavascriptString>');
												  		setFormValue('selectedProductThumbNailImg','<c:out value="${product.thumbnailImageURL}"/>');setFormValue('selectedProductCopy','<beacon:toJavascriptString><c:out escapeXml="false" value="${product.productCopy}"/></beacon:toJavascriptString>');setFormValue('selectedProductDetailImg','<c:out value="${product.detailImageURL}"/>');
												  		setFormValue('selectedProductPrice','<%=omProduct.getPrice() %>');setFormValue('selectedGiftable','<%=omProduct.isGiftable() %>');setFormValue('selectedWrapType','<%=omProduct.getWrapType() %>');setFormValue('selectedCardable','<%=omProduct.isCardable() %>');setFormValue('selectedFastShippable','<%=omProduct.isFastshipable() %>');javascript:returnSelectionResults();">
											  			</c:if>
														<img id="productimg_<c:out value="${product.productSetId }" />" border="1" width="100" height="100" src="<c:out value="${product.thumbnailImageURL}" />" />							
														<br>	
														<span class="content-field"><c:out escapeXml="false" value="${ product.productDescription }" /></span>
														<c:if test="${ enabled}">
															</a>
											  			</c:if>
														</div>
														<br> 													 
													   </td>
													 </tr>
                <script type="text/javascript">
                    createProductDetailPopup(
                    'product',
                    '<c:out value="${product.productSetId}"/>',
                    '<c:out escapeXml="false" value="${product.detailImageURL}"/>',
                    '<beacon:toJavascriptString><c:out escapeXml="false" value="${product.productDescription}"/></beacon:toJavascriptString>',
                    '<beacon:toJavascriptString><c:out escapeXml="false" value="${product.productCopy}"/></beacon:toJavascriptString>',
                    null
                  );
                </script>
                							
											  <tr valign="bottom">
											    <td valign="bottom">												
												  	<c:if test="${enabled}">
												  	<input type="radio" name="itemRadio" value="<c:out value='${productId}'/>" onclick="setFormValue('selectedProductId','<beacon:toJavascriptString><c:out value="${productId}"/></beacon:toJavascriptString>');setFormValue('selectedProductDesc','<beacon:toJavascriptString><c:out escapeXml="false" value="${product.productDescription}"/></beacon:toJavascriptString>');
												  		setFormValue('selectedProductThumbNailImg','<c:out value="${product.thumbnailImageURL}"/>');setFormValue('selectedProductCopy','<beacon:toJavascriptString><c:out escapeXml="false" value="${product.productCopy}"/></beacon:toJavascriptString>');setFormValue('selectedProductDetailImg','<c:out value="${product.detailImageURL}"/>');
												  		setFormValue('selectedProductPrice','<%=omProduct.getPrice() %>');setFormValue('selectedGiftable','<%=omProduct.isGiftable() %>');setFormValue('selectedWrapType','<%=omProduct.getWrapType() %>');setFormValue('selectedCardable','<%=omProduct.isCardable() %>');setFormValue('selectedFastShippable','<%=omProduct.isFastshipable() %>');javascript:returnSelectionResults();"/>
												  	<span class= "content-field-label"><cms:contentText code="recognition.merchandise" key="SELECT_THIS_ITEM" /></span>
												  	</c:if>
													<% counter++; %>
													<% itemsCounter++; %>													
												</td>
											  </tr>
											</table>
									      </td>		
											<%													
												if((itemsCounter % columns) == 0){
												   counter = 0; %>
												</tr><tr>
											<%	   
													}
											%>			
											</c:if>
											</c:forEach>	
											
										</c:when>
								</c:choose>	
								<%													
								  for( int i=0;i<4-counter;i++ ){%>
									<td align="center" valign="top">
										&nbsp;
	                				</td>
	                			<% } %>
							       </tr>
	

						</table>
					</c:if>
				</td>
			</tr>
		</table>
	</td>
</tr>
</table>
</form>
<script>
	initializeProductDetailPopups( 'product' );
</script>