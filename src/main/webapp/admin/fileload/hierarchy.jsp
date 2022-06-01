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
  <display:column property="actionType" titleKey="admin.fileload.hierarchy.ACTION_TYPE" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false" sortName="actionType"/>
  <display:column titleKey="admin.fileload.importRecordList.ERRORS" headerClass="crud-table-header-row" class="crud-content nowrap top-align">
    <c:forEach items="${importRecord.importRecordErrors}" var="importRecordError">
      <c:out value="${importRecordError.errorMessage}" escapeXml="false"/><br/>
    </c:forEach>
  </display:column>
  <display:column property="name" titleKey="admin.fileload.hierarchy.NAME" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false" sortName="name"/>
  <display:column property="oldNodeName" titleKey="admin.fileload.hierarchy.OLD_NODE_NAME" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="moveToNodeName" titleKey="admin.fileload.hierarchy.MOVE_TO_NODE_NAME" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="description" titleKey="admin.fileload.hierarchy.DESCRIPTION" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="nodeTypeName" titleKey="admin.fileload.hierarchy.NODE_TYPE_NAME" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="parentNodeName" titleKey="admin.fileload.hierarchy.PARENT_NODE_NAME" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="oldParentNodeName" titleKey="admin.fileload.hierarchy.OLD_PARENT_NODE_NAME" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicName1" titleKey="admin.fileload.hierarchy.CHARACTERISTIC_NAME_1" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicValue1" titleKey="admin.fileload.hierarchy.CHARACTERISTIC_VALUE_1" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicName2" titleKey="admin.fileload.hierarchy.CHARACTERISTIC_NAME_2" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicValue2" titleKey="admin.fileload.hierarchy.CHARACTERISTIC_VALUE_2" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicName3" titleKey="admin.fileload.hierarchy.CHARACTERISTIC_NAME_3" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicValue3" titleKey="admin.fileload.hierarchy.CHARACTERISTIC_VALUE_3" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicName4" titleKey="admin.fileload.hierarchy.CHARACTERISTIC_NAME_4" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicValue4" titleKey="admin.fileload.hierarchy.CHARACTERISTIC_VALUE_4" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicName5" titleKey="admin.fileload.hierarchy.CHARACTERISTIC_NAME_5" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicValue5" titleKey="admin.fileload.hierarchy.CHARACTERISTIC_VALUE_5" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicName6" titleKey="admin.fileload.hierarchy.CHARACTERISTIC_NAME_6" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicValue6" titleKey="admin.fileload.hierarchy.CHARACTERISTIC_VALUE_6" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicName7" titleKey="admin.fileload.hierarchy.CHARACTERISTIC_NAME_7" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicValue7" titleKey="admin.fileload.hierarchy.CHARACTERISTIC_VALUE_7" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicName8" titleKey="admin.fileload.hierarchy.CHARACTERISTIC_NAME_8" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicValue8" titleKey="admin.fileload.hierarchy.CHARACTERISTIC_VALUE_8" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicName9" titleKey="admin.fileload.hierarchy.CHARACTERISTIC_NAME_9" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicValue9" titleKey="admin.fileload.hierarchy.CHARACTERISTIC_VALUE_9" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicName10" titleKey="admin.fileload.hierarchy.CHARACTERISTIC_NAME_10" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="characteristicValue10" titleKey="admin.fileload.hierarchy.CHARACTERISTIC_VALUE_10" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
</display:table>