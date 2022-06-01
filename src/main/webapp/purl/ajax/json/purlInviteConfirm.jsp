{
<%@ include file="/include/taglib.jspf" %>

<logic:iterate name="inviteList" id="invite" indexId="count">
	"${invite.id}" : "${invite.status}"<c:if test="${count!=(inviteListSize-1)}">,</c:if>
</logic:iterate>
}
