{
<%@ include file="/include/taglib.jspf" %>

<logic:iterate name="mediaList" id="media" indexId="count">
	"${media.id}" : "${media.status}"<c:if test="${count!=(mediaListSize-1)}">,</c:if>
</logic:iterate>
}
