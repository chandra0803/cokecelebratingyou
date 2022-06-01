<!-- ======== CELEBRATION CONGRATS MODULE ======== -->
<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf" %>


<script type="text/template" id="celebrationChooseAwardModuleTpl">
<div class="module-liner">
    <div class="wide-view">
		<c:if test="${celebrationValue.awardActive}">
            <div class="module-content">

    			<c:if test="${celebrationValue.plateauAward}">

                        <c:choose>
                           <c:when test="${celebrationValue.awardRedeemEndDate > 0}">
                    <div class="split40 left-col">
                           </c:when>
                           <c:otherwise>
                    <div class="single-col">
                           </c:otherwise>
                        </c:choose>


               		    <h3 class="headline_3 chooseHeader  module-title"><cms:contentText key="AWARD_MSG_1" code="celebration.choose.award" /> <span><cms:contentText key="AWARD_MSG_2" code="celebration.choose.award" /></span></h3>

    					<beacon:authorize ifNotGranted="LOGIN_AS">
                			<a href="${celebrationValue.awardLink}" class="btn btn-primary" target="_blank"><cms:contentText key="PLATEAU_AWARD_BTN" code="celebration.choose.award" /></a> <!-- link to merchlink page -->
    					</beacon:authorize>

    					<beacon:authorize ifAnyGranted="LOGIN_AS">
    						<a href="#" class="btn btn-primary"><cms:contentText key="PLATEAU_AWARD_BTN" code="celebration.choose.award" /></a>
    					</beacon:authorize>

                    </div><!-- /split || single-col -->


    				<c:if test="${celebrationValue.awardRedeemEndDate > 0}">
                		<div class="celebrationCountdownContainer split60 right-col" >
                    		<h5 class="timeLeft headline_5"><cms:contentText key="PLATEAU_AWARD_REDEEM_TIME" code="celebration.choose.award" /></h5>
                    		<i class="icon-clock "></i>
                    		<div class="countdown" id="redeemCountdown">
                        		<span class="d "><span class="cd-digit">00</span>:</span><!-- Adding comments here to
                        		--><span class="h "><span class="cd-digit">00</span>:</span><!-- prevent whitespace from
                        		--><span class="m "><span class="cd-digit">00</span>:</span><!-- appearing between digits
                        		--><span class="s "><span class="cd-digit">00</span></span>
                    		</div>
                		</div>
            		</c:if>

    			</c:if>


				<c:if test="${celebrationValue.pointsAward}">
            		<!--Java Note: if points are given instead of countdown display celebrationPointsContainer below-->
            		<div class="celebrationPointsContainer single-col">
            			<c:if test="${not empty celebrationValue.awardAmount}" >
	 						<h3 class="headline_3 chooseHeader  module-title "><cms:contentText key="AWARD_MSG_1" code="celebration.choose.award" /> <span><cms:contentText key="AWARD_MSG_2" code="celebration.choose.award" /></span></h3>
                			<h5 class="headline_5 pointsEarned"><cms:contentTemplateText code="celebration.choose.award" key="POINTS_MSG" args="${celebrationValue.awardAmount}" delimiter=","/></h5>
							<beacon:authorize ifNotGranted="LOGIN_AS">
								<a href="${celebrationValue.awardLink}"  class="btn btn-primary" target="_blank"><cms:contentText key="POINTS_AWARD_BTN" code="celebration.choose.award" /></a> <!-- link to catalog page -->
							</beacon:authorize>
							<beacon:authorize ifAnyGranted="LOGIN_AS">
								<a href="#" class="btn btn-primary"><cms:contentText key="POINTS_AWARD_BTN" code="celebration.choose.award" /></a>
							</beacon:authorize>
                		</c:if>
            		</div>
				</c:if>

				<beacon:authorize ifNotGranted="LOGIN_AS">
					<a href="${celebrationValue.awardLink}" class="link-cover" target="_blank"></a> <!-- link to catalog page or merchlink -->
				</beacon:authorize>

            </div> <!-- ./module-content -->
		</c:if>
    </div>
</div>
<!--
// JAVA NOTE: Countdown end date (if it exists)
// Set endDate for the countdown date here in milliseconds or as a date string e.g. "Thu March 01 2014" or "March 01, 2014 16:05:00"
-->
<c:if test="${celebrationValue.awardRedeemEndDate > 0}">
<!--tplVariable.endDate=<c:out value="${celebrationValue.awardRedeemEndDate}"/>tplVariable-->
</c:if>
</script>
