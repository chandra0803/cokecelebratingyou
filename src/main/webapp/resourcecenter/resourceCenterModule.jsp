<!-- ======== MANAGER TOOLKIT MODULE ======== -->
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<script language="javascript">

function openExternalURL(url)
  {
	//url = "http://localhost:7001/pentag/docs/resource_center/BestPractice.pdf";
    popUpWin(url , 'console', 800, 600, false, true);
  }

</script>
<script type="text/template" id="resourceCenterModuleTpl">
<div class="module-liner">

    <div class="module-content">
        <h3 class="module-title"><cms:contentText key="RESOURCE_CENTER" code="home.rail"/></h3>
        <div class="module-actions">
            <ul class="button-container">
    	 		<c:if test="${displayResourceCenter}">
        			<c:forEach var="resource" items="${resourceCenter}" varStatus="status">
    					<c:if test="${null != resource.contentDataMap['TEXT']}">
              				<li class="btnWrap"><a class="btn btn-primary btn-inverse" href="<c:out escapeXml="false" value="${resource.contentDataMap['URL']}"/>" target="_blank"> <c:out escapeXml="false" value="${resource.contentDataMap['TEXT']}"/></a></li>
    					 </c:if>
    				</c:forEach>
    			</c:if>
         	</ul>
        </div>
    </div>

</div>
</script>
