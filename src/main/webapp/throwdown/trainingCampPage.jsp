<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>
<div id="trainingCampPageView" class="trainingCampPage-liner page-content">

    <div class="row-fluid">
        <div class="span12">
            <h3><cms:contentText key="TRAINING_CAMP" code="home.rail"/></h3>

            <ul>
                <cms:content var="trainingCamps" code="home.throwdown.trainingCamp"/>
	 			<c:if test="${!empty trainingCamps}">
    			<c:forEach var="trainingCamp" items="${trainingCamps}" varStatus="status">
          			<li><a href="<c:out escapeXml="false" value="${trainingCamp.contentDataMap['URL']}"/>" target="_blank"> <c:out escapeXml="false" value="${trainingCamp.contentDataMap['TEXT']}"/></a></li>
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
    var rcpv = new ThrowdownTrainingCampPageView({
        el:$('#trainingCampPageView'),
        pageTitle : '<cms:contentText key="TRAINING_CAMP" code="home.rail"/>'
    });
});
</script>
