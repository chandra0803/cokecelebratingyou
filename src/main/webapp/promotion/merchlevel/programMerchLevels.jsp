<%@ include file="/include/taglib.jspf"%>
<%@ include file="/include/yui-imports.jspf"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.Locale"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>
<%@ page import="com.biperf.awardslinqDataRetriever.client.MerchLevelProduct"%>
<%@ page import="com.biperf.core.utils.UserManager"%>
<%@ page import="com.biperf.awardslinqDataRetriever.client.ProductGroupDescription"%>
<script src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/cards/cards.js" type="text/javascript"></script>
<script src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/tooltip.js" type="text/javascript"></script>
<script src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/modal/popaction.js" type="text/javascript"></script>
<script src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/modal/common.js" type="text/javascript"></script>
<script type="text/javascript">
<!--
function returnSelectionResults()
{
   <c:choose>
     <c:when test='${param.optionRoot == "1"}'>
       var modifier = 0;
     </c:when>
     <c:otherwise>
       var modifier = 1;
     </c:otherwise>
   </c:choose>
  <c:choose>
    <c:when test="${param.enableItemSelection }">
      returnSelectionItemResults();
    </c:when>
    <c:otherwise>
      var levelRadios = document.getElementsByName("levelRadio");
      for ( var i=0; i<levelRadios.length; i++ ) {
       var currentRadio = levelRadios[i];
       if (currentRadio.checked)
       {
         window.parent.dialogArguments.options[currentRadio.value-modifier].selected=true;
         if (window.parent.refreshLevelData) {
           window.parent.refreshLevelData();
         }
       }
      }
    </c:otherwise>
  </c:choose>
  window.parent.closeModalPopup();
  // window.close();

}

function returnSelectionItemResults()
{
  var selectedProductId = document.getElementsByName("selectedProductId");
  var selectedProductDescs = document.getElementsByName("selectedProductDesc");
  var selectedProductThumbNailImgs = document.getElementsByName("selectedProductThumbNailImg");
  var selectedProductCopys = document.getElementsByName("selectedProductCopy");
  var selectedProductDetailImgs = document.getElementsByName("selectedProductDetailImg");
  var selectedLevelCounts = document.getElementsByName("selectedLevelCount");

  if (selectedProductId != '')
  {
    window.parent.itemIds.value = selectedProductId[0].value;
    window.parent.itemDescs.value = selectedProductDescs[0].value;
    window.parent.itemThumbImgURLs.value = selectedProductThumbNailImgs[0].value;
    window.parent.itemCopys.value = selectedProductCopys[0].value;
    window.parent.itemDetailImgURLs.value = selectedProductDetailImgs[0].value;
    var count = selectedLevelCounts[0].value;
    window.parent.levelCounts.options[count].selected = true;
    window.parent.refreshData();
   }
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

td, td.card { border: 1px solid #FFFFFF; font-family: arial; font-size: 11px; }
.levelHeader { background-color:#ddd; }
.levelHeaderOn { background-color:#0089C4; }
.levelHeader .subheadline a { color:#0089C4; }
.levelHeaderOn .subheadline { color:white; }

</style>
<c:set var="chars">& < > " '</c:set>
<c:set var="enableLevelSelection" value="${param.enableLevelSelection}"/>
<c:set var="enableItemSelection" value="${param.enableItemSelection}"/>
<form name="programMerchLevels">
<table border="0" cellpadding="3" cellspacing="1" width="100%" >
  <tr>
    <td width="100%" height="100%" valign="top">
    <table border="0" align="center" cellpadding="5" width="100%">
      <tr>
        <td>
          <span class="headline">
          <c:choose>
            <c:when test="${ enableLevelSelection}">
              <cms:contentText code="recognition.merchandise" key="VIEW_AWARD_LEVEL_TITLE" /></span>
            </c:when>
            <c:when test="${ enableItemSelection}">
              <cms:contentText code="recognition.merchandise" key="VIEW_AWARD_TITLE" /></span>
            </c:when>
            <c:otherwise>
              <cms:contentText code="recognition.merchandise" key="VIEW_MERCHANDISE_TITLE" /></span>
            </c:otherwise>
          </c:choose>
          <c:if test="${lastName != null }">
            <br/><c:out value="${lastName}"/>
            <c:if test="${firstName != null}">,&nbsp;<c:out value="${firstName}"/></c:if>
          </c:if>
          <c:if test="${country != null }">
            <br/><cms:contentText code="${country.cmAssetCode}" key="COUNTRY_NAME" />
          </c:if>
          <%-- define the number of columns --%>
          <c:set var="columns" value="5" />
          <c:if test="${ enableItemSelection}">
              <input type="hidden" name="selectedProductId">
            <input type="hidden" name="selectedProductDesc">
            <input type="hidden" name="selectedProductThumbNailImg">
            <input type="hidden" name="selectedProductCopy">
            <input type="hidden" name="selectedProductDetailImg">
            <input type="hidden" name="selectedLevelCount">
          </c:if>

          <c:if test="${ null!= merchLevelData }">

                        <c:set var="levelIndex" value="${param.level}" />
                        <c:if test="${empty param.level}">
                          <c:set var="levelIndex" value="1" />
                        </c:if>

            <table border="0" align="center" width="100%" cellpadding="5" cellspacing="0">

                <c:forEach items="${merchLevelData.levels}" var="level" varStatus="levelStatus">
                <% long counter = 0 ;%>
                <c:set var="itemsCounter" value="0"/>
                  <tr>
                    <td colspan="5" valign="bottom"
                          <c:choose>
                            <c:when test="${levelStatus.count == levelIndex}">
                              class="levelHeaderOn"
                            </c:when>
                            <c:otherwise>
                              class="levelHeader"
                            </c:otherwise>
                          </c:choose>
                      >
                      <div id="level<c:out value="${levelStatus.count}"/>"></div>
          <c:if test="${levelStatus.count != levelIndex}">
                      <div style="float:right;">
                      <c:if test="${ enableLevelSelection}">
                              <%
                                Map paramMap = new HashMap();
                                paramMap.put( "programId", request.getAttribute( "programId" ) );
                                paramMap.put( "promotionIdToDisplayLevelNames", request.getAttribute( "promotionIdToDisplayLevelNames" ) );
                                pageContext.setAttribute( "linkUrl", ClientStateUtils.generateEncodedLink( "",
                                    "/promotionRecognition/displayMerchLevelsDetail.do?enableLevelSelection=true"+"&enableItemSelection=false"+"&optionRoot=1", paramMap, true ) );
                              %>
                      </c:if>
                      <c:if test="${ enableItemSelection}">
                              <%
                                Map paramMap = new HashMap();
                                paramMap.put( "programId", request.getAttribute( "programId" ) );
                                paramMap.put( "promotionIdToDisplayLevelNames", request.getAttribute( "promotionIdToDisplayLevelNames" ) );
                                pageContext.setAttribute( "linkUrl", ClientStateUtils.generateEncodedLink( "",
                                    "/promotionRecognition/displayMerchLevelsDetail.do?enableItemSelection=true"+"&enableLevelSelection=false"+"&optionRoot=1", paramMap, true ) );
                              %>
                      </c:if>                      
                      <c:if test="${! enableLevelSelection and !enableItemSelection}">
                              <%
                                Map paramMap = new HashMap();
                                paramMap.put( "programId", request.getAttribute( "programId" ) );
                                paramMap.put( "promotionIdToDisplayLevelNames", request.getAttribute( "promotionIdToDisplayLevelNames" ) );
                                pageContext.setAttribute( "linkUrl", ClientStateUtils.generateEncodedLink( "",
                                    "/promotionRecognition/displayMerchLevelsDetail.do", paramMap, true ) );
                              %>
                      </c:if>                      

                              <a href="javascript:window.parent.closeModalPopup(); window.parent.openModalPopup('<c:out value="${linkUrl}"/>&amp;level=<c:out value="${levelStatus.count}"/>',POPUP_SIZE_LARGE,true)">
                        <img border="0" src="../assets/g4skin/images/plus2.gif"/>
                              </a>
                      </div>
          </c:if>
                      <span class="subheadline">
                      <c:choose>
                        <c:when test="${promoMerchCountryLevels != null}">
                          <c:forEach items="${promoMerchCountryLevels}" var="currentPromoMerchCountryLevel">
                            <c:if test="${level.name == currentPromoMerchCountryLevel.levelName}">
                              <c:out value="${currentPromoMerchCountryLevel.displayLevelName}"/>
                            </c:if>
                          </c:forEach>
                        </c:when>
                        <c:otherwise>
                          <c:choose>
                            <c:when test="${levelStatus.count == levelIndex}">
                              <c:out value="${level.name}" />
                            </c:when>
                            <c:otherwise>
			                      <c:if test="${ enableLevelSelection}">
			                              <%
			                                Map paramMap = new HashMap();
			                                paramMap.put( "programId", request.getAttribute( "programId" ) );
			                                paramMap.put( "promotionIdToDisplayLevelNames", request.getAttribute( "promotionIdToDisplayLevelNames" ) );
			                                pageContext.setAttribute( "linkUrl", ClientStateUtils.generateEncodedLink( "",
			                                    "/promotionRecognition/displayMerchLevelsDetail.do?enableLevelSelection=true"+"&enableItemSelection=false"+"&optionRoot=1", paramMap, true ) );
			                              %>
			                      </c:if>
			                      <c:if test="${ enableItemSelection}">
			                              <%
			                                Map paramMap = new HashMap();
			                                paramMap.put( "programId", request.getAttribute( "programId" ) );
			                                paramMap.put( "promotionIdToDisplayLevelNames", request.getAttribute( "promotionIdToDisplayLevelNames" ) );
			                                pageContext.setAttribute( "linkUrl", ClientStateUtils.generateEncodedLink( "",
			                                    "/promotionRecognition/displayMerchLevelsDetail.do?enableItemSelection=true"+"&enableLevelSelection=false"+"&optionRoot=1", paramMap, true ) );
			                              %>
			                      </c:if>			                      
			                      <c:if test="${!enableLevelSelection and !enableItemSelection}">
			                              <%
			                                Map paramMap = new HashMap();
			                                paramMap.put( "programId", request.getAttribute( "programId" ) );
			                                paramMap.put( "promotionIdToDisplayLevelNames", request.getAttribute( "promotionIdToDisplayLevelNames" ) );
			                                pageContext.setAttribute( "linkUrl", ClientStateUtils.generateEncodedLink( "",
			                                    "/promotionRecognition/displayMerchLevelsDetail.do", paramMap, true ) );
			                              %>
			                      </c:if> 
                   

                              <a href="javascript:window.parent.closeModalPopup(); window.parent.openModalPopup('<c:out value="${linkUrl}"/>&amp;level=<c:out value="${levelStatus.count}"/>',POPUP_SIZE_LARGE,true)">
                                <c:out value="${level.name}" />
                              </a>
                            </c:otherwise>
                          </c:choose>
                        </c:otherwise>
                      </c:choose>
                      <c:if test="${param.showValue == 'true'}">&nbsp;(<cms:contentText code="recognition.merchandise" key="VALUE" />&nbsp;<c:out value="${level.maxValue}" />)
                      </c:if>
                      </span>
                      <c:if test="${ enableLevelSelection}">
                        <br>
                        <input type="radio" name="levelRadio" value="<c:out value="${levelStatus.count}" />" onclick="javascript:returnSelectionResults();">
                        <span class="subheadline"><cms:contentText code="recognition.merchandise" key="SELECT_THIS_LEVEL" /></span>
                      </c:if>
                    </td>
                  </tr>
                  <tr>
                    <%-- TODO: re-implement with a tag library --%>
                    <%-- start iteration over the products in the level --%>

                    <c:choose>
                      <c:when test="${ levelStatus.count != levelIndex }">
                      </c:when>

                      <c:when test="${ level.products != null && !empty level.products }">


                    <c:forEach items="${ level.products }" var="product" varStatus="status">
                      <c:forEach items="${product.productIds}" var="productId" varStatus="idStatus">
                        <%-- in case of product groups (i.e. multiple product IDs, display first one only --%>
                        <%-- (image is the same for all product IDs within a product group) --%>
                        <% MerchLevelProduct merchProduct = (MerchLevelProduct)pageContext.getAttribute( "product");
                           ProductGroupDescription productDescription = merchProduct.getProductGroupDescriptions().get(UserManager.getLocale());
                           if(productDescription == null)
                           {
                        	   productDescription = merchProduct.getProductGroupDescriptions().get(Locale.US);
                           }
                           pageContext.setAttribute("productDescription", productDescription);
                           %>
                        <c:if test="${idStatus.first}">
                          <td id="product_<c:out value="${product.productSetId }" />" class="product-tooltip" align="center" valign="top" width="<c:out value="${ 100/columns }"/>%" height="175" style="border:1px solid #FFF">
                                                      <table width=100%" height="100%">
                                                        <tr valign="top">
                                                          <td>
                              <div id="desc_<c:out value="${product.productSetId }" />">
                                 <c:if test="${ enableItemSelection}">
                                 <a onclick="setFormValue('selectedProductId','<beacon:toJavascriptString><c:out value="${productId}"/></beacon:toJavascriptString>');setFormValue('selectedProductDesc','<beacon:toJavascriptString><c:out escapeXml="false" value="${productDescription.description}"/></beacon:toJavascriptString>');
                                            setFormValue('selectedProductThumbNailImg','<c:out value="${product.thumbnailImageURL}"/>');setFormValue('selectedProductCopy','<beacon:toJavascriptString><c:out escapeXml="false" value="${productDescription.copy}"/></beacon:toJavascriptString>');setFormValue('selectedProductDetailImg','<c:out value="${product.detailImageURL}"/>');
                                            setFormValue('selectedLevelCount','<c:out value="${levelStatus.count}" />');javascript:returnSelectionResults();">
                                  </c:if>
                              <img id="productimg_<c:out value="${product.productSetId }" />" border="1" width="100" height="100" src="<c:out value="${product.thumbnailImageURL}" />" />
                              <br>
                              <c:out escapeXml="false" value="${ productDescription.description }" />
                                 <c:if test="${ enableItemSelection}">
                                 </a>
                                 </c:if>
                              </div>
                              </td>
                            </tr>

                <script type="text/javascript">
                  createProductDetailPopup(
                    'product',
                    '<c:out value="${product.productSetId}"/>',
                    '<c:out escapeXml="false" value="${product.detailImageURL}"/>',
                    '<beacon:toJavascriptString><c:out escapeXml="false" value="${productDescription.description}"/></beacon:toJavascriptString>',
                    '<beacon:toJavascriptString><c:out escapeXml="false" value="${productDescription.copy}"/></beacon:toJavascriptString>',
                    null
                  );
                </script>

                      <br>
                      <c:if test="${ enableItemSelection}">
                        <tr valign="bottom">
                          <td valign="bottom">
                        <input type="radio" name="itemRadio" value="<c:out value='${productId}'/>" onclick="setFormValue('selectedProductId','<beacon:toJavascriptString><c:out value="${productId}"/></beacon:toJavascriptString>');setFormValue('selectedProductDesc','<beacon:toJavascriptString><c:out escapeXml="false" value="${productDescription.description}"/></beacon:toJavascriptString>');
                                            setFormValue('selectedProductThumbNailImg','<c:out value="${product.thumbnailImageURL}"/>');setFormValue('selectedProductCopy','<beacon:toJavascriptString><c:out escapeXml="false" value="${productDescription.copy}"/></beacon:toJavascriptString>');setFormValue('selectedProductDetailImg','<c:out value="${product.detailImageURL}"/>');
                                            setFormValue('selectedLevelCount','<c:out value="${levelStatus.count}" />');javascript:returnSelectionResults();"/>
                            <cms:contentText code="recognition.merchandise" key="SELECT_THIS_ITEM" />
                        </td>
                        </tr>
                      </c:if>
                      <% counter++;%>
                      <c:set var="itemsCounter" value="${itemsCounter + 1}"/>
                                  </table>
                    </td>
                    <c:choose>
                      <c:when test="${ (itemsCounter % columns ) == 0 }">
                      <% counter=0; %>
                        </tr>
                      </c:when>
                      <c:when test="${ (itemsCounter % columns ) != 0 && (status.last) }">
                        <% for( int i=0;i<5-counter;i++ ){%>
                          <td align="center" valign="top">
                            &nbsp;
                                  </td>
                                <% }
                                   counter = 0 ;
                                %>
                                </tr>
                      </c:when>
                    </c:choose>
                  </c:if>
                </c:forEach>
              </c:forEach>
            </c:when>
          <c:otherwise>
          <td colspan="5" align="center">
            <span class="subheadline"><cms:contentText code="recognition.merchandise" key="NO_PRODUCTS_AVAILABLE" /></span>
          </td>
          </c:otherwise>
        </c:choose>

        <tr class="form-blank-row">
            <td colspan="5"></td>
          </tr>
          <tr class="form-blank-row">
            <td colspan="5"></td>
          </tr>
      <%-- end iteration over the products in the level --%>
    </c:forEach>
            </table>
          </c:if>
        </td>
      </tr>
    </table>
  </td>
</tr>
</table>
</form>

<script type="text/javascript">
  initializeProductDetailPopups( 'product' );
  window.parent.showModalPopupLoadingIndicator( false );

  <c:if test="${levelIndex > 1}">

    scrollToLevel();

    function scrollToLevel() {
      var levelHeader = document.getElementById('level<c:out value="${levelIndex}"/>');
      if ( levelHeader.offsetTop < 0 ) {
        window.setTimeout( scrollToLevel, 10 ); // for IE
      } else {
        window.scrollTo( 0, levelHeader.parentNode.offsetTop );
      }
    }

  </c:if>
</script>
