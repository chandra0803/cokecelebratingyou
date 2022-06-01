<%@ include file="/include/taglib.jspf"%>
"parameters": [
<c:forEach items="${searchCriteria}" var="paramItem" varStatus="counter">
	<c:if test="${counter.index != 0}">,</c:if>
	{
	"name" : "${paramItem.label}",
	"value" : "${paramItem.value}"
	}
</c:forEach>
],