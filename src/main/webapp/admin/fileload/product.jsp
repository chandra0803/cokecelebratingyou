<%-- UI REFACTORED --%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<display:table defaultsort="1" defaultorder="ascending" name="importRecords" id="importRecord" sort="external" pagesize="${pageSize}" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>" export="true" partialList="true" size="${importRecordCount}">
<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
	<%-- Explicit displayTag.properties START --%>
	  <display:setProperty name="export.xml" value="false" />
	  <display:setProperty name="export.csv.label" value="CSV" />
	  <display:setProperty name="export.excel.label" value="XLS" />				  
	  <display:setProperty name="export.pdf.label" value="PDF" />
	  <display:setProperty name="export.pdf" value="true" />
	  <display:setProperty name="export.pdf.filename" value="export.pdf" />
	  <display:setProperty name="export.pdf.include_header" value="true" />
	  <display:setProperty name="export.pdf.class" value="com.biperf.core.ui.utils.CustomPdfView" />
	<%-- Explicit displayTag.properties END --%>  
  <display:column property="actionType" titleKey="admin.fileload.deposit.ACTION_TYPE" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false" sortName="actionType"/>
  <display:column titleKey="admin.fileload.deposit.ERRORS" headerClass="crud-table-header-row"  class="crud-content nowrap top-align">
    <c:forEach items="${importRecord.importRecordErrors}" var="importRecordError">
      <c:out value="${importRecordError.errorMessage}" escapeXml="false"/><br/>
    </c:forEach>
  </display:column>
  <display:column property="productName" titleKey="admin.fileload.product.PRODUCT_NAME" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false" sortName="productName"/>
  <display:column property="productDescription" titleKey="admin.fileload.product.PRODUCT_DESCRIPTION" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="skuCode" titleKey="admin.fileload.product.SKU_CODE" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="categoryName" titleKey="admin.fileload.product.CATEGORY_NAME" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="categoryDescription" titleKey="admin.fileload.product.CATEGORY_DESC" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="subCategoryName" titleKey="admin.fileload.product.SUB_CATEGORY_NAME" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="subCategoryDescription" titleKey="admin.fileload.product.SUB_CATEGORY_DESC" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
</display:table>