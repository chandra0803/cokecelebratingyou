<%@ include file="/include/taglib.jspf" %>
<%@page contentType="text/xml"%>
<results>
  <c:forEach items="${paxes}" var="pax">
    <c:forEach items="${pax.userNodes}" var="userNode">
      <result>
		<key><c:out value="${pax.userName}"/></key>
		<c:set var="paxUserName" value="${fn:replace(pax.userName, '-', ' ')}" />
        <c:set var="paxLastName" value="${fn:replace(pax.lastName, '-', ' ')}" />
        <c:set var="paxFirstName" value="${fn:replace(pax.firstName, '-', ' ')}" />
		<value><c:out value="${paxUserName}"/> - <c:out value="${paxLastName}"/>, <c:out value="${paxFirstName}"/></value>
      </result>
    </c:forEach>
  </c:forEach>
</results>
