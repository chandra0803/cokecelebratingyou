<%@ include file="/include/taglib.jspf"%>

<script type="text/template" id="throwdownTrainingCampModuleTpl">
<div class="module-liner">

    <a class="visitAppBtn" href="${pageContext.request.contextPath}/throwdown/trainingCampPage.do"><i class="icon-arrow-2-circle-right"></i></a>

    <div class="wide-view">

        <h3><cms:contentText key="TRAINING" code="home.rail"/> <cms:contentText key="CAMP" code="home.rail"/></h3>

        <ul>

     	<cms:content var="trainingCamps" code="home.throwdown.trainingCamp"/>
		<c:if test="${!empty trainingCamps}">
		<c:forEach var="trainingCamp" items="${trainingCamps}" varStatus="status">
			<li><a href="<c:out escapeXml="false" value="${trainingCamp.contentDataMap['URL']}"/>" target="_blank"> <c:out escapeXml="false" value="${trainingCamp.contentDataMap['TEXT']}"/> &#187;</a></li>
		</c:forEach>
		</c:if>

        </ul>

    </div>

    <div class="title-icon-view">
        <h3><cms:contentText key="TRAINING_CAMP" code="home.rail"/></h3>
    </div>
</div>

</script>
