<!-- ======== CELEBRATION MANAGER MESSAGE MODULE ======== -->
<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<script type="text/template" id="celebrationManagerMessageModuleTpl">
<div class="module-liner">
    <div class="wide-view">
        <div class="module-content">

            <div id="managerMessageCarousel" class="carousel">
                <div class="cycle carousel-inner">

            		<c:forEach var="messageItem" items="${celebrationValue.managerMessageList}">
                    <div class="item">
                        <div class="item-wrap">
                            <div class="comment">
                                <p><c:out value="${messageItem.message}" escapeXml="true"/></p>

                            </div>

                            <div class="commenter">
                                <span class="avatarwrap">
                                    <c:if test="${ messageItem.avatarUrl != null }">
                                       <img src="${messageItem.avatarUrl}" alt="" class="avatar" />
                                    </c:if>
                                    <c:if test="${ messageItem.avatarUrl == null }">
                                        <c:set var="fcName" value="${messageItem.firstName.substring(0,1)}"/>
                                        <c:set var="lcName" value="${messageItem.lastName.substring(0,1)}"/>
                                        <span class="avatar-initials">${fcName}${lcName}</span>
                                    </c:if>
                                </span>
                                <div class="commenter-meta">
                                    <p class="commenter-name">${messageItem.firstName} ${messageItem.lastName}</p>
                                </div>
                            </div>
                        </div>
                    </div><!--/.item-->
            		</c:forEach>
                </div>
            </div> <!-- /.carousel -->
        </div> <!-- /.module-content -->
    </div>

</div>
</script>
