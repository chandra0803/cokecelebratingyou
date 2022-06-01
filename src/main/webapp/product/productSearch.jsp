<%-- UI REFACTORED --%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<script type="text/javascript">
<!--
  function onFormSubmit()
  {
    if ( document.productSearchForm.selectedBox )
    {
      selectAll("selectedBox");
    }
    return true;
  }

  function updateCounts()
  {
    document.getElementById("resultsCount").innerHTML = document.getElementById('resultsBox').length;
    document.getElementById("selectedCount").innerHTML = document.getElementById('selectedBox').length;
  }

  function continueSubmit(target)
  {
    if ( document.productSearchForm.selectedBox.options.length == 0 )
    {
  		alert("<cms:contentText key="PRODUCT_SELECTION_ALERT" code="product.search"/>");
      return false;
    }
    else if ( document.productSearchForm.selectedBox )
    {
      selectAll("selectedBox");
      setDispatchAndSubmit(target);
      return true;
    }
  }

	function categoryChange()
	{
		//clear the other fields since they are no longer valid
		getContentForm().subCategoryDD.options[0].selected = true;
		getContentForm().productName.value="";
		getContentForm().productId.value="";
		selectAll("selectedBox");
		searchPlease();
	}

	function subCategoryChange()
	{
		//clear the text fields since they are no longer valid
		getContentForm().productName.value="";
		getContentForm().productId.value="";
		selectAll("selectedBox");
		searchPlease();
	}

	function searchPlease(){
	    setDispatchAndSubmit('search');
	}

//-->
</script>

<html:form styleId="contentForm" action="/productSearch"  onsubmit="onFormSubmit()">
  <html:hidden property="method" value="displaySearch"/>
  <html:hidden property="returnActionUrl"/>
  <c:forEach items='${productSearchForm.paramsMap}' var='item'>
   <html:hidden property="params(${item.key})"/>
  </c:forEach>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="TITLE" code="product.search"/></span>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="INSTRUCTION" code="product.search"/>
        </span>
        <br/><br/>

        <cms:errors/>

        <table>
          <tr>
            <td class="top-align">
              <table>

                <tr class="form-row-spacer">
                  <beacon:label property="categoryId">
                    <cms:contentText key="CATEGORY" code="product.search"/>
                  </beacon:label>
                  <td class="content-field">
                    <html:select property="categoryId" styleClass="content-field" onchange="categoryChange()">
                      <c:forEach items="${categoryList}" var="category">
                        <html:option value="${category.id}"><c:out value="${category.name}"/></html:option>
                      </c:forEach>
                    </html:select>
                  </td>
                </tr>

                <tr class="form-blank-row"><td colspan="3"/></tr>

                <tr class="form-row-spacer">
                  <beacon:label property="subCategoryId">
                    <cms:contentText key="SUBCATEGORY" code="product.search"/>
                  </beacon:label>
                  <td class="content-field">
                    <html:select styleId="subCategoryDD" property="subCategoryId" styleClass="content-field" onchange="subCategoryChange()" disabled="${disableSubCategoryList}">
                      <c:forEach items="${subCategoryList}" var="subCategory">
                        <html:option value="${subCategory.id}"><c:out value="${subCategory.name}"/></html:option>
                      </c:forEach>
                    </html:select>
									<c:if test="${disableSubCategoryList}">
										<beacon:client-state>
											<beacon:client-state-entry name="subCategoryId" value="-1"/>
										</beacon:client-state>
									</c:if>
                  </td>
                </tr>

                <tr class="form-blank-row"><td colspan="3"/></tr>

                <tr class="form-row-spacer">
                  <beacon:label property="productName">
                    <cms:contentText key="PROD_NAME" code="product.search"/>
                  </beacon:label>
                  <td class="content-field">
                    <html:text property="productName" styleId="productName" styleClass="content-field"/>
                  </td>
                </tr>

                <tr class="form-blank-row"><td colspan="3"/></tr>

                <tr class="form-row-spacer">
                  <beacon:label property="productId">
                    <cms:contentText key="PROD_CODE" code="product.search"/>
                  </beacon:label>
                  <td class="content-field">
                    <html:text property="productId" styleId="productId" styleClass="content-field"/>
                  </td>
                </tr>

                <tr class="form-buttonrow">
                  <td></td>
                  <td></td>
                  <td align="left">
                    <html:submit styleClass="content-buttonstyle" onclick="setDispatch('search')">
                      <cms:contentText code="system.button" key="SEARCH" />
                    </html:submit>
                  </td>
                </tr>
              </table>
            </td>

            <td class="lb-divider">
              <img alt="" src="<%= RequestUtils.getBaseURI(request) %>/assets/g4skin/images/spacer-solid.gif" border="0">
            </td>

            <td class="lb-right">
              <table width="100%">
                <tr class="form-row-spacer">
                  <td class="content-field">
                    <c:choose>
                      <c:when test="${empty productList}" >
                        <cms:contentText key="SEARCH_RESULTS" code="product.search"/>&nbsp;
                        (<span id="resultsCount">0</span>):
                        <br/>
                        <html:select property="resultsBox" multiple="multiple" ondblclick="moveSelectedOptions(this,document.getElementById('selectedBox'),false) || updateCounts()" styleId="resultsBox" size="5" style="width: 430px"/>
                      </c:when>
                      <c:otherwise>
                        <cms:contentText key="SEARCH_RESULTS" code="product.search"/>&nbsp;
                        (<SPAN ID="resultsCount">1</SPAN>):&nbsp;
                        <a href="javascript:selectAll('resultsBox')">
                          <cms:contentText key="SELECT_ALL" code="product.search"/>
                        </a>
                        <br/>
                        <html:select property="resultsBox" multiple="multiple" ondblclick="moveSelectedOptions(this,document.getElementById('selectedBox'),false) || updateCounts()" styleId="resultsBox" size="5" style="width: 430px">
                          <html:options collection="productList" property="formattedId" labelProperty="value" filter="false"/>
                        </html:select>
                      </c:otherwise>
                    </c:choose>
                  </td>
                </tr>

                <tr class="form-buttonrow">
                  <td align="center">
                    <html:button property="moveDown" styleClass="content-buttonstyle" onclick="moveSelectedOptions(document.getElementById('resultsBox'),document.getElementById('selectedBox'),false) || updateCounts()">
                      <cms:contentText key="ADD_TO_LIST" code="product.search"/>
                    </html:button>
                    &nbsp;
                    <html:button property="removeItem" styleClass="content-buttonstyle" onclick="removeSelectedOptions(document.getElementById('selectedBox'),false) || updateCounts()">
                      <cms:contentText key="REMOVE_FROM_LIST" code="product.search"/>
                    </html:button>
                  </td>
                </tr>

                <tr class="form-row-spacer">
                  <td class="content-field">
                    <c:choose>
                      <c:when test="${empty selectedResults}" >
                        <cms:contentText code="product.search" key="SELECTED"/>&nbsp;
                        (<span id="selectedCount">0</span>):&nbsp;
                        <a href="javascript:selectAll('selectedBox')">
                          <cms:contentText key="SELECT_ALL" code="product.search"/>
                        </a>
                        <br/>
                        <html:select property="selectedBox" multiple="multiple" ondblclick="removeSelectedOptions(this,false) || updateCounts()" styleId="selectedBox" size="5" style="width: 430px"/>
                      </c:when>
                      <c:otherwise>
                        <cms:contentText key="SELECTED" code="product.search"/>&nbsp;
                        (<SPAN ID="selectedCount">1</SPAN>):&nbsp;
                        <a href="javascript:selectAll('selectedBox')">
                          <cms:contentText code="product.search" key="SELECT_ALL" />
                        </a>
                        <br/>
                        <html:select property="selectedBox" multiple="multiple" ondblclick="removeSelectedOptions(this,false) || updateCounts()" styleId="selectedBox" size="5" style="width: 430px">
                          <html:options collection="selectedResults" property="formattedId" labelProperty="value" filter="false"/>
                        </html:select>
                      </c:otherwise>
                    </c:choose>
                  </td>
                </tr>

                <tr class="form-buttonrow">
                  <td align="center">
                    <html:button property="continue" styleClass="content-buttonstyle" onclick="continueSubmit('continueBack');">
                      <cms:contentText key="CONTINUE" code="system.button"/>
                    </html:button>
                    &nbsp;
                    <c:url var="url" value="${productSearchForm.returnActionUrl}" >
                      <c:forEach items='${productSearchForm.paramsMap}' var='item'>
                        <c:param name="${item.key}" value="${item.value}" />
                      </c:forEach>
                    </c:url>
                    <html:cancel styleClass="content-buttonstyle" onclick="setActionAndDispatch('${url}','');">
                      <cms:contentText key="CANCEL" code="system.button"/>
                    </html:cancel>
                  </td>
                </tr>
              </table>
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>

  <script type="text/javascript">
    updateCounts();
  </script>

</html:form>
