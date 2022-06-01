<%@ include file="/include/taglib.jspf" %>
<%@page contentType="text/xml"%>
<results>
  <c:forEach items="${nodes}" var="node">
    <result>
      <key><c:out value="${node.id}"/></key>
      <value><c:out value="${node.name}"/></value>
    </result>
  </c:forEach>
</results>
