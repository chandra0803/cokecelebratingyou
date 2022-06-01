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
  <display:column property="userName" titleKey="admin.fileload.deposit.USER_NAME" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false" sortName="userName"/>
  <display:column property="awardAmount" titleKey="admin.fileload.deposit.AWARD_AMOUNT" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false" sortName="awardAmount"/>
  <display:column titleKey="admin.awardlevel.AWARD_DATE" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false">
  	<fmt:formatDate value="${importRecord.awardDate}" pattern="${JstlDatePattern}" />
  </display:column>
  <display:column property="comments" titleKey="admin.fileload.deposit.COMMENTS" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false" sortName="comments"/>
  <display:column property="formElement1" titleKey="admin.fileload.deposit.FORM_ELEMENT_1" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false" sortName="formElement1"/>
  <display:column property="formElement2" titleKey="admin.fileload.deposit.FORM_ELEMENT_2" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false" sortName="formElement2"/>
  <display:column property="formElement3" titleKey="admin.fileload.deposit.FORM_ELEMENT_3" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false" sortName="formElement3"/>
  <display:column property="anniversaryNumberOfDaysOrYears" titleKey="admin.fileload.deposit.ANNIVERSARY_NUM" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false" sortName="anniversaryNumberOfDaysOrYears"/>
</display:table>
 