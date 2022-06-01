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
  <display:column titleKey="admin.fileload.nomapprover.ERRORS" headerClass="crud-table-header-row" class="crud-content nowrap top-align">
    <c:forEach items="${importRecord.importRecordErrors}" var="importRecordError">
      <c:out value="${importRecordError.errorMessage}" escapeXml="false"/><br/>
    </c:forEach>
  </display:column>
  <display:column property="userName" titleKey="admin.fileload.nomapprover.USER_NAME" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false" sortName="userName"/>
  <display:column property="approvalType" titleKey="admin.fileload.nomapprover.APPROVAL_TYPE" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false" sortName="approvalType"/>
  <display:column property="minValue" titleKey="admin.fileload.nomapprover.MINIMUM_VALUE" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false" sortName="minValue"/>
  <display:column property="maxValue" titleKey="admin.fileload.nomapprover.MAXIMUM_VALUE" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false" sortName="maxValue"/>
  <display:column property="approvalRound" titleKey="admin.fileload.nomapprover.APPROVAL_ROUND" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false" sortName="approvalRound"/>  
  
  
</display:table>

