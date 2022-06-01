<%@ include file="/include/taglib.jspf" %>

{
	"messages": [
		{
			"type":"<c:out value="${status}"/>",
			"name":"<cms:contentText key="AVATAR_UPLOAD_SUCCESS" code="purl.ajax"/>",
			<c:choose>
				<c:when test="${status == 'success'}">
					"isSuccess": true,
					"text":"<cms:contentText key="AVATAR_UPLOAD_SUCCESS" code="purl.ajax"/>",
				</c:when>
				<c:otherwise>
					"isSuccess": false,
					"text":"<cms:contentText key="AVATAR_UPLOAD_FAILED" code="purl.ajax"/>",
				</c:otherwise>
			</c:choose>	
			"picURL": "<c:out value="${avatarImgUrl}"/>"
		}
	]
}