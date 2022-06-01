<!-- ======== QUIZ PAGE ======== -->
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>
<div id="resourceCenterPageView" class="resourceCenterPage-liner page-content">

    <div class="row-fluid">
        <div class="span12">
            <h3><cms:contentText key="RESOURCE_CENTER" code="home.rail"/></h3>

            <ul>
				<c:if test="${!empty resourceCenter}">
    			<c:forEach var="resource" items="${resourceCenter}" varStatus="status">
          			<li><a href="<c:out escapeXml="false" value="${resource.contentDataMap['URL']}"/>" target="_blank"> <c:out escapeXml="false" value="${resource.contentDataMap['TEXT']}"/></a></li>
      			</c:forEach>
				</c:if>
           </ul>
        </div>
    </div>
</div>

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
$(document).ready(function(){
    //attach the view to an existing DOM element
    var rcpv = new ResourceCenterPageView({
        el:$('#resourceCenterPageView'),
        pageTitle : '<cms:contentText key="RESOURCE_CENTER" code="home.rail"/>'
    });
});
</script>
