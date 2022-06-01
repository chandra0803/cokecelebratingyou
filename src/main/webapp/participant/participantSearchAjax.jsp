<%@ include file="/include/taglib.jspf" %>
<%@page contentType="text/xml"%>
<results>
  <c:forEach items="${paxes}" var="pax">
    <c:forEach items="${pax.userNodes}" var="userNode">
      <result>
        <c:choose>
	        <c:when test="${countryRequired and not empty pax.primaryAddress}">
        		<key><c:out value="${pax.id}:${userNode.node.id}:${pax.primaryAddress.address.country.id}"/></key>
	        </c:when>
	        <c:otherwise>
        		<key><c:out value="${pax.id}:${userNode.node.id}"/></key>
	        </c:otherwise>
        </c:choose>
        <c:set var="paxFirstName" value="${fn:replace(pax.firstName, '-', ' ')}" />
        <c:set var="paxLastName" value="${fn:replace(pax.lastName, '-', ' ')}" />
        <c:set var="nodeName" value="${fn:replace(userNode.node.name, '-', ' ')}" />
        <c:set var="positionType" value="${fn:replace(pax.positionType, '-', ' ')}" />
        <c:set var="departmentType" value="${fn:replace(pax.departmentType, '-', ' ')}" />
		<value><c:out value="${paxLastName}"/>, <c:out value="${paxFirstName}"/> - <c:out value="${nodeName}"/> - <c:out value="${positionType}"/> - <c:out value="${departmentType}"/></value>
      </result>
    </c:forEach>
  </c:forEach>
</results>
