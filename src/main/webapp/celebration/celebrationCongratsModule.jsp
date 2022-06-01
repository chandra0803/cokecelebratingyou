<!-- ======== CELEBRATION CONGRATS MODULE ======== -->

<%@ include file="/include/taglib.jspf" %>

<script type="text/template" id="celebrationCongratsModuleTpl">
<div class="module-liner">
    <div class="congrats-vid-container congrats-fulldiv">
        <video id='video' playsinline autoplay muted loop preload="auto"></video>
    </div>

    <div class="congrats-bg-container congrats-fulldiv"></div>
    <div class="wide-view">
        <div class="module-content">
            <div class="celebrationCongratsInfoContainer split60">
                <div class="celebrationCongratsInfo">
                <span class="headline_2 congrats-intro"><cms:contentText key="CONGRATS_INTRO" code="celebration.congrats" /></span>
                <span class="headline_3 congrats-name">${celebrationValue.recipientFirstName}</span>
				<c:choose>
					<c:when test="${celebrationValue.anniversaryInYears != null && celebrationValue.anniversaryInYears }">
					  <c:choose>
						<c:when test="${celebrationValue.anniversaryNumberOfYears > 0}">
                			<span class="headline_5 congrats-msg">
 								<cms:contentTemplateText code="celebration.congrats" key="CONGRATS_MSG_YEAR" args="${celebrationValue.anniversaryNumberOfYears},${celebrationValue.displayInfo}" delimiter=","/>
							</span>
						</c:when>
						<c:otherwise>
                			<span class="headline_5 congrats-msg">
								<cms:contentTemplateText code="celebration.congrats" key="CONGRATS_MSG_WITHOUT_YEAR_OR_DAY" args="${celebrationValue.displayInfo}" delimiter=","/>
							</span>
						</c:otherwise>
					  </c:choose>
					</c:when>
					<c:otherwise>
						<c:choose>
							<c:when test="${celebrationValue.anniversaryNumberOfDays > 0}">
                				<span class="headline_5 congrats-msg">
 									<cms:contentTemplateText code="celebration.congrats" key="CONGRATS_MSG_DAY" args="${celebrationValue.anniversaryNumberOfDays},${celebrationValue.displayInfo}" delimiter=","/>
								</span>
							</c:when>
							<c:otherwise>
                				<span class="headline_5 congrats-msg">
									<cms:contentTemplateText code="celebration.congrats" key="CONGRATS_MSG_WITHOUT_YEAR_OR_DAY" args="${celebrationValue.displayInfo}" delimiter=","/>
								</span>
							</c:otherwise>
						</c:choose>
					</c:otherwise>
				</c:choose>
            </div>
            </div>

            <div class="celebrationCongratsEcard split40">
                <img src="${celebrationValue.eCardUrl}" alt="">
            </div>
        </div> <!-- ./module-content -->
    </div>



</div>
</script>