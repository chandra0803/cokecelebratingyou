<%-- UI REFACTORED --%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<display:table name="importRecords" id="importRecord" sort="external" pagesize="${pageSize}" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>" export="true" partialList="true" size="${importRecordCount}">
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
  <display:column property="submitterUserName" titleKey="admin.fileload.productclaim.SUBMITTER_USER_NAME" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false" sortName="submitterUserName"/>
  <display:column property="submitterUserId" titleKey="admin.fileload.productclaim.SUBMITTER_USER_ID" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false" sortName="submitterUserId"/>
  <display:column property="trackToNodeId" titleKey="admin.fileload.productclaim.TRACK_TO_NODE_ID" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false" sortName="trackToNodeId"/>
  <display:column property="trackToNodeName" titleKey="admin.fileload.productclaim.TRACK_TO_NODE_NAME" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false" sortName="trackToNodeName"/>
  
  <c:set var="importRecordIndex" value="${importRecord_rowNum -1}" />

  <c:set var="productClaimImportFieldRecords" value="${importRecords[importRecordIndex].productClaimImportFieldRecords}" />
  <display:column titleKey="admin.fileload.productclaim.IMPORT_FIELD_RECORDS" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false">
    <%-- Bug # 34829  fix --%>
    <display:table name="${productClaimImportFieldRecords}" id="child${parent_rowNum}" class="simple sublist">
      <display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
      <display:column property="claimFormStepElementId" titleKey="admin.fileload.productclaim.CFSE_ID" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
      <display:column property="claimFormStepElementName" titleKey="admin.fileload.productclaim.CFSE_NAME" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
      <display:column property="claimFormStepElementValue" titleKey="admin.fileload.productclaim.CFSE_VALUE" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
    </display:table>
  </display:column>

  <c:set var="productClaimImportProductRecords" value="${importRecords[importRecordIndex].productClaimImportProductRecords}" />
  <display:column titleKey="admin.fileload.productclaim.IMPORT_PRODUCT_RECORDS" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false">
  	<%-- Bug # 34829   fix --%>
    <display:table name="${productClaimImportProductRecords}" id="child${parent_rowNum}" class="simple sublist">
    	<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
      <display:column property="productId" titleKey="admin.fileload.productclaim.PRODUCT_ID" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
      <display:column property="productName" titleKey="admin.fileload.productclaim.PRODUCT_NAME" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
      <display:column property="soldQuantity" titleKey="admin.fileload.productclaim.SOLD_QUANTITY" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
      <display:column property="productCharacteristicId1" titleKey="admin.fileload.productclaim.PRODUCT_CHAR_ID_1" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
      <display:column property="productCharacteristicName1" titleKey="admin.fileload.productclaim.PRODUCT_CHAR_NAME_1" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
      <display:column property="productCharacteristicValue1" titleKey="admin.fileload.productclaim.PRODUCT_CHAR_VALUE_1" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
      <display:column property="productCharacteristicId2" titleKey="admin.fileload.productclaim.PRODUCT_CHAR_ID_2" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
      <display:column property="productCharacteristicName2" titleKey="admin.fileload.productclaim.PRODUCT_CHAR_NAME_2" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
      <display:column property="productCharacteristicValue2" titleKey="admin.fileload.productclaim.PRODUCT_CHAR_VALUE_2" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
      <display:column property="productCharacteristicId3" titleKey="admin.fileload.productclaim.PRODUCT_CHAR_ID_3" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
      <display:column property="productCharacteristicName3" titleKey="admin.fileload.productclaim.PRODUCT_CHAR_NAME_3" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
      <display:column property="productCharacteristicValue3" titleKey="admin.fileload.productclaim.PRODUCT_CHAR_VALUE_3" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
      <display:column property="productCharacteristicId4" titleKey="admin.fileload.productclaim.PRODUCT_CHAR_ID_4" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
      <display:column property="productCharacteristicName4" titleKey="admin.fileload.productclaim.PRODUCT_CHAR_NAME_4" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
      <display:column property="productCharacteristicValue4" titleKey="admin.fileload.productclaim.PRODUCT_CHAR_VALUE_4" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
      <display:column property="productCharacteristicId5" titleKey="admin.fileload.productclaim.PRODUCT_CHAR_ID_5" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
      <display:column property="productCharacteristicName5" titleKey="admin.fileload.productclaim.PRODUCT_CHAR_NAME_5" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
      <display:column property="productCharacteristicValue5" titleKey="admin.fileload.productclaim.PRODUCT_CHAR_VALUE_5" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
    </display:table>
  </display:column>
  
</display:table>
