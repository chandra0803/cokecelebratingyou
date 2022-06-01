<%@ include file="/include/taglib.jspf" %>
<%@page contentType="text/xml"%>
<results>
  <c:forEach items="${paxes}" var="pax">
      <result>
        <key><c:out value="${pax.id}"/></key>
        <value><c:out value="${pax.lastName}"/>, <c:out value="${pax.firstName}"/></value>
      </result>
  </c:forEach>
</results>
