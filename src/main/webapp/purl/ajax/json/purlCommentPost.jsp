{
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

    "_comment" : "send back the status, the processed comments, and the failure message [optional]",
    "status" : "${status}",
    "comments" : "<c:if test="${not empty purlComment.purlContributor.avatarUrl}"><img src='purlTempImage.do?imageUrl=${purlComment.purlContributor.avatarUrl}' alt='${purlComment.purlContributor.firstName} ${purlComment.purlContributor.lastName}' class='thumb' /></c:if><p><c:out value="${purlComment.comments}" /></p>",
    "fail" : "<cms:contentText code="purl.ajax" key="COMMENT_POST_FAILED"/>"
}
