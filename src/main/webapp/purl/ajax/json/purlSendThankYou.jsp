<%@ include file="/include/taglib.jspf" %>

{
	"messages": [
		{
			<c:choose>
				<c:when test="${status == 'success'}">
					"isSuccess": true,
					"errorMessage": ""
				</c:when>
				<c:otherwise>				
					"isSuccess": false,
					"errorMessage": "<cms:contentText key="THANKYOU_SEND_FAILED" code="purl.recipient"/>"
				</c:otherwise>
			</c:choose>
		}			
	]
}