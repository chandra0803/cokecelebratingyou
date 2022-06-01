<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><c:set var="counter" value="0"/>
{
    query:'<c:out value="${query}"/>',
    suggestions:[<c:forEach items="${nodes}" var="node"><c:set var="counter" value="${counter+1}" /><c:if test="${counter > 1}">,</c:if>'<c:out value="${node.name}"/>'</c:forEach>],
    data:[<c:set var="counter" value="0"/><c:forEach items="${nodes}" var="node"><c:set var="counter" value="${counter+1}" /><c:if test="${counter > 1}">,</c:if>
        {
             secondLevel: 'true',
             isJQuery: 'true',
             emailRequired: 'true',
             criteria: '<c:out value="${criteria}"/>',
             query: '<c:out value="${node.id}"/>',
             queryName: '<c:out value="${node.name}"/>'
        }</c:forEach>
    ]
}