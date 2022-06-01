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
  <display:column titleKey="admin.fileload.budgetdistribution.ERRORS" headerClass="crud-table-header-row" class="crud-content nowrap top-align">
    <c:forEach items="${importRecord.importRecordErrors}" var="importRecordError">
      <c:out value="${importRecordError.errorMessage}" escapeXml="false"/><br/>
    </c:forEach>
  </display:column>
  <display:column property="srcBudgetMasterName" titleKey="admin.fileload.budgetdistribution.BUDGET_MASTER_ID" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false" sortName="srcBudgetMasterName"/>
  <display:column property="srcBudgetTimePeriodName" titleKey="admin.fileload.budgetdistribution.BUDGET_TIME_PERIOD" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false" sortName="srcBudgetTimePeriodName"/>
  <display:column property="orgUnitId" titleKey="admin.fileload.budgetdistribution.ORG_UNIT_ID" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false" sortName="orgUnitId"/>
  <display:column property="srcBudgetOwnerName" titleKey="admin.fileload.budgetdistribution.BUDGET_OWNER_NAME" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false" sortName="srcBudgetOwnerName"/>
  <display:column property="srcBudgetOwnerLoginId" titleKey="admin.fileload.budgetdistribution.BUDGET_OWNER_LOGIN_ID" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false" sortName="srcBudgetOwnerLoginId"/>  
  <display:column property="srcBudgetAmount" titleKey="admin.fileload.budgetdistribution.BUDGET_AMOUNT" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false" sortName="srcBudgetAmount"/>
  <display:column property="transferToOwner1" titleKey="admin.fileload.budgetdistribution.TRANSFER_OWNER1" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false" sortName="transferToOwner1"/>
  <display:column property="amountOwner1" titleKey="admin.fileload.budgetdistribution.AMOUNT_OWNER1" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false" sortName="amountOwner1"/>
  <display:column property="transferToOwner2" titleKey="admin.fileload.budgetdistribution.TRANSFER_OWNER2" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false" sortName="transferToOwner2"/>
  <display:column property="amountOwner2" titleKey="admin.fileload.budgetdistribution.AMOUNT_OWNER2" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false" sortName="amountOwner2"/>
  <display:column property="transferToOwner3" titleKey="admin.fileload.budgetdistribution.TRANSFER_OWNER3" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false" sortName="transferToOwner3"/>
  <display:column property="amountOwner3" titleKey="admin.fileload.budgetdistribution.AMOUNT_OWNER3" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false" sortName="amountOwner3"/>
</display:table>

