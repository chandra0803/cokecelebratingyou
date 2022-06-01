<%@ include file="/include/taglib.jspf" %>

{
	"messages" : [
			{
			"type":"<c:out value="${status}"/>",
			"name":"<cms:contentText key="PHOTO_UPLOAD_SUCCESS" code="purl.ajax"/>",
			<c:choose>
				<c:when test="${status == 'success'}">
					"isSuccess": true,
					"text":"<cms:contentText key="PHOTO_UPLOAD_SUCCESS" code="purl.ajax"/>",
				</c:when>
				<c:when test="${status == 'invalidFileTypeOrSize'}">
				"isSuccess": false,
				"text":"<cms:contentText key='UPLOAD_FAILED' code='recognition.submit'/>\n<cms:contentTemplateText code='recognition.submit' key='VALID_EXTENSION_MESSAGE' args='${maxImageSize}, ${maxVideoSize}' delimiter=','/>",
				</c:when>
				<c:otherwise>
					"isSuccess": false,
					"text":"<cms:contentTemplateText code='purl.ajax' key='PHOTO_UPLOAD_FAILED' args='${maxImageSize}, ${maxVideoSize}' delimiter=','/>",
				</c:otherwise>
			</c:choose>
			<c:choose>
				<c:when test="${type == 'video'}">
				    "vidURL":"<c:out value="${stagerPrefixURL}"/><c:out value="${mediaFullUrl}"/>",				    
				</c:when>
				<c:otherwise>
					"picURL":"<c:out value="${stagerPrefixURL}"/><c:out value="${mediaFullUrl}"/>",
				</c:otherwise>
			</c:choose>
			"fileType":"<c:out value="${extension}"/>",
			"thumbNailURL":"<c:out value="${stagerPrefixURL}"/><c:out value="${mediaThumbUrl}"/>"
			}
		]
}