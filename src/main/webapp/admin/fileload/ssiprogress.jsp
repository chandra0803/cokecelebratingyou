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
  <display:column titleKey="admin.fileload.ssiprogress.ERRORS" headerClass="crud-table-header-row" class="crud-content nowrap top-align">
    <c:forEach items="${importRecord.importRecordErrors}" var="importRecordError">
      <c:out value="${importRecordError.errorMessage}" escapeXml="false"/><br/>
    </c:forEach>
  </display:column>
  <display:column property="ssiContestId" titleKey="admin.fileload.ssiprogress.SSI_CONTEST_ID" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false" sortName="ssiContestId"/>
  <display:column property="userName" titleKey="admin.fileload.ssiprogress.LOGIN_ID" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false" sortName="userName"/>
  <display:column property="firstName" titleKey="admin.fileload.ssiprogress.FIRST_NAME" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false" sortName="firstName"/>
  <display:column property="lastName" titleKey="admin.fileload.ssiprogress.LAST_NAME" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false" sortName="lastName"/>
  <display:column property="nodeName" titleKey="admin.fileload.ssiprogress.ORG_UNIT_NAME" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false" sortName="nodeName"/>
  <display:column property="emailAddress" titleKey="admin.fileload.ssiprogress.EMAIL_ADDRESS" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false" sortName="emailAddress"/>
  <display:column property="activityDescription" titleKey="admin.fileload.ssiprogress.ACTIVITY_DESCRIPTION" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false" sortName="activityDescription"/>
  <display:column property="progress" titleKey="admin.fileload.ssiprogress.TOTAL_PROGRESS" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false" sortName="progress"/>
  <display:column property="activityDate" titleKey="admin.fileload.ssiprogress.ACTIVITY_DATE" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false" sortName="activityDate"/>
</display:table>

