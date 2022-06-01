{
<%@ include file="/include/taglib.jspf" %>

    "_comment" : "send back the new media framework URL, media type, the ID of the media, the full-size image filename and the thumbnail filename",
    "status" : "${status}",
    "newmedia" : "purlContribution.do?method=newPhoto",
    "media" : "picture",
    "id" : "${globalUniqueId}",
    "full" : "${mediaFullUrl}",
    "thumb" : "${mediaThumbUrl}",
    "caption" : "",
    "filename" : "${mediaFilename}",
    "imageurl" : "purlTempImage.do?imageUrl=${mediaThumbUrl}",
    "fail" : "<cms:contentTemplateText code='purl.ajax' key='PHOTO_UPLOAD_FAILED' args='${maxImageSize}, ${maxVideoSize}' delimiter=','/>"
}
