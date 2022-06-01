<%@ include file="/include/taglib.jspf" %>

{
	"messages":[
		{
			"firstName": "<c:out value="${firstName}"/>",
			"lastName": "<c:out value="${lastName}"/>",
			"emailAddress": "<c:out value="${emailAddress}"/>",
			"avatarUrl": "<c:out value="${avatarUrl}"/>",
			"contributorID": <c:out value="${contributorID}"/>
		}
	]
}