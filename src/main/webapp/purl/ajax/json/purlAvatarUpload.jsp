{
<%@ include file="/include/taglib.jspf"%>

    "_comment" : "send back the file upload status, URL to the profile image, and success/failure message",
    "status" : "${status}",
    "thumb" : "${mediaThumbUrl}",
    "imageurl" : "purlTempImage.do?imageUrl=${mediaThumbUrl}",
    "fail" : "<cms:contentText code="purl.ajax" key="AVATAR_UPLOAD_FAILED"/>"
}
