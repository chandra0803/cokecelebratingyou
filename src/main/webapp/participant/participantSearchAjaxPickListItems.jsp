<%@ include file="/include/taglib.jspf" %>
<%@page contentType="text/xml"%>
<results>
  <c:forEach items="${pickListItems}" var="item">
    <result>
      <key><c:out value="${item.code}"/></key>
      <value><c:out value="${item.name}"/></value>
    </result>
  </c:forEach>
</results>
