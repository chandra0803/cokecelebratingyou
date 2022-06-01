<%@ include file="/include/taglib.jspf" %>

{
	"messages" : [
			{
			<c:choose>
				<c:when test="${status == 'success'}">
					"isSuccess": true,
					"imageServiceUrlSrc":"<c:out value="${imageServiceUrlSrc}"/>",
					"imageServiceUrlThumb":"<c:out value="${imageServiceUrlThumb}"/>",
					"text":""
				</c:when>
				<c:otherwise>
					"isSuccess": false,
					<c:choose>
						<c:when test="${errorText != null}">
							"text":"<c:out value="${errorText}"/>"
						</c:when>
						<c:otherwise>
							"text":"<cms:contentText code="purl.ajax" key="COMMENT_POST_FAILED"/>"
						</c:otherwise>
					</c:choose>
				</c:otherwise>
			</c:choose>						
			}
		]
}