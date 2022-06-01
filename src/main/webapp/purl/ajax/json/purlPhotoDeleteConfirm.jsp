{
<%@ include file="/include/taglib.jspf" %>

    "_comment" : "send back the globally unique ID of the item being deleted and a success/failure message",
    "id" : "${globalUniqueId}",
    "status" : "${status}",
    "fail" : "<cms:contentText code="purl.ajax" key="PHOTO_DELETE_FAILED"/>"
}
