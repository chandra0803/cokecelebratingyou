<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><c:set var="counter" value="0"/><c:if test="${!secondLevel}">{
    query:'<c:out value="${query}"/>',
    suggestions:[<c:forEach items="${paxes}" var="pax"><c:forEach items="${pax.userNodes}" var="userNode"><c:set var="counter" value="${counter+1}" /><c:if test="${counter > 1}">,</c:if>'<c:out value="${pax.lastName}"/>, <c:out value="${pax.firstName}"/> - <c:out value="${userNode.node.name}"/>'</c:forEach></c:forEach>],
    data:</c:if>[<c:set var="counter" value="0"/><c:forEach items="${paxes}" var="pax"><c:forEach items="${pax.userNodes}" var="userNode"><c:set var="counter" value="${counter+1}" /><c:if test="${counter > 1}">,</c:if>
        {
            paxid: <c:out value="${pax.id}"/>,
            nodeid: <c:out value="${userNode.node.id}"/>,
            firstname: '<c:out value="${pax.firstName}"/>',
            lastname: '<c:out value="${pax.lastName}"/>',
            email: '<c:out value="${pax.primaryEmailAddress.emailAddr}"/>',
            title: '<c:out value="${pax.positionTypePickList.name}"/>',
            department: '<c:out value="${pax.departmentTypePickList.name}"/>',
            location: '<c:out value="${userNode.node.name}"/>',
            countrycode: '<c:out value="${pax.primaryCountryCode}"/>'
        }</c:forEach></c:forEach>
    ]<c:if test="${!secondLevel}">
}</c:if>