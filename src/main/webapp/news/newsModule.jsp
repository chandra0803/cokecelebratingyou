<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

  <!-- ======== NEWS MODULE ======== -->

<script type="text/template" id="newsModuleTpl">
<div class="module-liner">

    <a href="<%= RequestUtils.getBaseURI(request)%>/newsPage.do#index" class="visitAppBtn">
       <cms:contentText key="ALL" code="diyCommunications.common.labels" /> <i class="icon-arrow-1-circle-right"></i>
    </a>

    <div class="module-content">


        <div id="newsCarousel" class="carousel">


        </div>
        <div class="newsPager">
            <ul>

            </ul>
        </div>

    </div>

</div>
</script>


<%@include file="/news/newsModuleItem.jsp" %>
