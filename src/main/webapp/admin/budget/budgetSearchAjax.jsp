<%@ include file="/include/taglib.jspf" %>
<%@page contentType="text/xml"%>
<results>
  <c:forEach items="${budgets}" var="budget">
      <result>
        <key><c:out value="${budget.id}"/></key>
        <originalvalue><c:out value="${budget.originalValueDisplay}"/></originalvalue>
        <currentvalue><c:out value="${budget.currentValueDisplay}"/></currentvalue>
      </result>
  </c:forEach>
</results>
