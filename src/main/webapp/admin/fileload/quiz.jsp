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
  <display:column property="actionType" titleKey="admin.fileload.quiz.ACTION_TYPE" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false" sortName="actionType"/>
  <display:column titleKey="admin.fileload.quiz.ERROR" headerClass="crud-table-header-row"  class="crud-content nowrap top-align">
    <c:forEach items="${importRecord.importRecordErrors}" var="importRecordError">
      <c:out value="${importRecordError.errorMessage}" escapeXml="false"/><br/>
    </c:forEach>
  </display:column>
  <display:column property="recordType" titleKey="admin.fileload.quiz.RECORD_TYPE" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="quizName" titleKey="quiz.form.NAME" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false" sortName="quizName"/>
  <display:column property="quizDescription" titleKey="quiz.form.DESCRIPTION" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="questionRequiredDisplay" titleKey="quiz.question.QUESTION_REQUIRED" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="questionStatusTypeDisplay" titleKey="quiz.question.QUESTION_STATUS" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="quizType" titleKey="quiz.form.DISPLAY_METHOD" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="quizPassingScore" titleKey="quiz.form.PASSING_SCORE" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="quizNumberOfQuestionsAsked" titleKey="quiz.form.NUMBER_TO_ASK" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="question" titleKey="quiz.question.QUESTION" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="answerChoice" titleKey="quiz.question_answer.ANSWER" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="answerCorrectDisplay" titleKey="quiz.question_answer.CORRECT" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
  <display:column property="answerChoiceExplanation" titleKey="quiz.question_answer.EXPLANATION" headerClass="crud-table-header-row" class="crud-content nowrap top-align" sortable="false"/>
</display:table>