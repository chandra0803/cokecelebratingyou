<%@ include file="/include/taglib.jspf" %>
<%@ page import="com.biperf.core.domain.purl.PurlContributorComment"%>

{
	"messages": [],

	"activityPods": [	
		<c:forEach var="commentItem" items="${comments}" varStatus="commentIndex">
			{
				"activityId": <c:out value="${commentItem.id}"/>,
				"userInfo": [
					{
						"userName": "<c:out value="${commentItem.purlContributor.firstName} ${commentItem.purlContributor.lastName}"/>",
						"contributorID": <c:out value="${commentItem.purlContributor.id}"/>,
						"signedIn": "false",
						"profileLink": "",
						"profilePhoto": "<c:out value="${commentItem.purlContributor.avatarUrl}"/>"
					}
				],
				"commentText": "<c:out value="${commentItem.comments}"/>",
				
				<c:choose>
					<c:when test="${commentItem.videoType.code == 'web' and commentItem.videoStatus.code == 'active'}">
						"videoWebLink": "<c:out value="${commentItem.videoUrl}"/>",
						"media": ""
					</c:when>
					<c:when test="${commentItem.videoType.code == 'direct' and commentItem.videoStatus.code == 'active'}">
						"videoWebLink": "",
						"media": [
							{
								<%
									String videoUrl = ( (PurlContributorComment) pageContext.getAttribute( "commentItem" ) ).getVideoUrl();
									String fileType = videoUrl.substring( videoUrl.lastIndexOf( '.' ) + 1 );
									pageContext.setAttribute( "fileType", fileType );
								%>
								"video": [
									{
										"fileType": "<c:out value="${fileType}"/>", 
										"src": "<c:out value="${commentItem.videoUrl}"/>"
									}
								],		
								"photo":[]
							}
						]
					</c:when>
					<c:when test="${not empty commentItem.imageUrl and commentItem.imageStatus.code == 'active'}">
						"media": [
							{
								"video": [],			
								"photo":[
									{
										"src": "<c:out value="${commentItem.imageUrl}"/>"
									}
								]
							}
						]						
					</c:when>
					<c:otherwise>
						"videoWebLink": "",
						"media": ""
					</c:otherwise>
				</c:choose>			
			}
			<c:if test="${commentIndex.index < fn:length(comments) - 1}">,</c:if>
		</c:forEach>
	]
}
