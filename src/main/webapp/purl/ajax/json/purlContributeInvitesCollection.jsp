<%@ include file="/include/taglib.jspf" %>

{
	"messages": [],

	"invites": [
		<c:forEach var="inviteItem" items="${invites}" varStatus="inviteIndex">
			{
				"emailAddress": "<c:out value="${inviteItem.emailAddr}"/>",
				"firstName": "<c:out value="${inviteItem.firstName}"/>",
				"lastName": "<c:out value="${inviteItem.lastName}"/>",
				"status": "<c:out value="${inviteItem.status}"/>"
			}
			<c:if test="${inviteIndex.index < fn:length(invites) - 1}">,</c:if>
		</c:forEach>
	]
}