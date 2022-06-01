<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ include file="/include/taglib.jspf"%>

<c:choose>
  <c:when test="${ecardUploadStatus.success}">
    <c:set var="text"><cms:contentText key="IMAGE_UPLOAD_SUCCESS" code="recognition.submit"/></c:set>
  </c:when>
  <c:otherwise>
    <c:set var="text"><cms:contentText key="IMAGE_UPLOAD_FAILURE" code="recognition.submit"/></c:set>
  </c:otherwise>
</c:choose>

{
	"messages":[
		{
		"type": "serverCommand",
        "command": "modal",
		"name": "Image Upload Success",
		"text": "${text}"
		}
	],
    "properties": {
      "isSuccess": ${ecardUploadStatus.success},
      "imageUrl": "${ecardUploadStatus.imageUrl}"
    }
}
