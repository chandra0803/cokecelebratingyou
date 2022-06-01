{
<%@ include file="/include/taglib.jspf" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

    "_comment" : "send back the new media framework URL, media type, the ID of the media, the video URL and the thumbnail filename",
    "status" : "${status}",
    "newmedia" : ""<%=RequestUtils.getBaseURI(request)%>/quiz/quizLearningObjectSubmit.do?method=newVideo",
    "media" : "video_url",
    "id" : "${globalUniqueId}",
    "full" : "${mediaUrl}",
    "thumb" : "",
    "caption" : "",
    "filename" : "${mediaUrl}",
    "imageurl" : "<%= RequestUtils.getBaseURI(request) %>/assets/g4skin/images/video.jpg",
    "fail" : "<cms:contentText code="purl.ajax" key="VIDEO_SUBMIT_FAILED"/>"
}
