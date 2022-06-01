<%@page import="java.util.Locale"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

<div class="modal hide fade autoModal autoModalGoalquest">
    <div class="modal-header">
        <button class="close" data-dismiss="modal"><i class="icon-close"></i></button>

        <%-- JAVA NOTE: if is create --%>
        <c:choose>
        <c:when test="${ newGoal }">
	        <h3><cms:contentText key="THANK_MESSAGE" code="promotion.goalquest.selection.wizard"/></h3>
        </c:when>
        <c:otherwise>
	        <%-- JAVA NOTE: if is edit --%>
	        <h3><cms:contentText key="GOAL_CHANGED" code="promotion.goalquest.selection.wizard"/></h3>
        </c:otherwise>
		</c:choose>

    </div>

    <%-- JAVA: render the following .modal-body element ONLY IF there are budgets --%>
    <%-- JAVA: populate this list as needed --%>
    <div class="modal-body">
            <h4 class="levelTitle"><c:out value="${ goalLevelValueBean.goalLevel.goalLevelName }" /></h4>
            <p class="levelDesc"><c:out value="${ goalLevelValueBean.goalLevel.goalLevelDescription }"/></p>
            <dl class="dl-horizontal dl-h2 levelDeets">
            
      		   <c:if test="${ paxGoal.goalQuestPromotion.promotionType.code eq 'challengepoint'}" >
                   <h5><cms:contentText key="BASE_PROGRAM" code="promotion.goalquest.detail"/></h5>
                   <p>
					<c:if test="${thresholdApplicable}">
					<cms:contentTemplateText code="promotion.challengepoint.select.level"
					   key="RIGHT_TILE_INSTRUCTION" 
					   args="${goalLevelValueBean.awardPerIncrementLocaleBased}!
					 ${goalLevelValueBean.beforeUnitLabel}!
					 ${goalLevelValueBean.calculatedIncrementAmountLocaleBased}!
					 ${goalLevelValueBean.afterUnitLabel} "
					  delimiter="!"/>
					</c:if>
					
					<c:if test="${!thresholdApplicable}">
					<cms:contentTemplateText code="promotion.challengepoint.select.level"
					   key="RIGHT_TILE_NO_THRESHOLD" 
					   args="
					   ${goalLevelValueBean.awardPerIncrementLocaleBased}!
					 ${goalLevelValueBean.beforeUnitLabel}!
					 ${goalLevelValueBean.calculatedIncrementAmountLocaleBased}!
					 ${goalLevelValueBean.afterUnitLabel} "
					  delimiter="!"/>
					</c:if>
                   </p>
                </c:if>
                
                <%-- BASELINE INFO --%>
            	<c:if test="${ not empty goalLevelValueBean.baseAmountLocaleBased }">
                <dt><cms:contentText key="BASELINE" code="promotion.goalquest.selection.wizard"/></dt>
                <dd><c:if test="${paxGoal.goalQuestPromotion.baseUnit ne null }"><c:if test="${paxGoal.goalQuestPromotion.baseUnitPosition ne null and paxGoal.goalQuestPromotion.baseUnitPosition.code eq 'before' }"><c:out value="${paxGoal.goalQuestPromotion.baseUnitText}" escapeXml="false"/>&nbsp;</c:if></c:if><c:out  value="${goalLevelValueBean.baseAmountLocaleBased}"/><c:if test="${paxGoal.goalQuestPromotion.baseUnit != null }"><c:if test="${paxGoal.goalQuestPromotion.baseUnitPosition ne null and paxGoal.goalQuestPromotion.baseUnitPosition.code eq 'after' }">&nbsp;<c:out value="${paxGoal.goalQuestPromotion.baseUnitText}" escapeXml="false"/></c:if></c:if></dd>
                </c:if>
                
                <%-- GQ Promotion Goal--%>
                <c:if test="${paxGoal.goalQuestPromotion.promotionType.code eq 'goalquest'}">
                <%-- GOAL --%>
                <c:if test="${ not empty goalLevelValueBean.calculatedGoalAmtLocaleBased }">
                  <dt><cms:contentText key="YOUR_GOAL" code="promotion.goalquest.selection.wizard"/>:</dt>
                  <dd><c:if test="${paxGoal.goalQuestPromotion.baseUnit ne null }"><c:if test="${paxGoal.goalQuestPromotion.baseUnitPosition ne null and paxGoal.goalQuestPromotion.baseUnitPosition.code eq 'before'}"><c:out value="${paxGoal.goalQuestPromotion.baseUnitText}" escapeXml="false"/>&nbsp;</c:if></c:if><c:out value="${goalLevelValueBean.calculatedGoalAmtLocaleBased}"/><c:if test="${paxGoal.goalQuestPromotion.baseUnit != null }"><c:if test="${paxGoal.goalQuestPromotion.baseUnitPosition ne null and paxGoal.goalQuestPromotion.baseUnitPosition.code eq 'after'}">&nbsp;<c:out value="${paxGoal.goalQuestPromotion.baseUnitText}" escapeXml="false"/></c:if></c:if></dd>
                </c:if>
                </c:if>
                
                 <%-- CP Promotion Goal--%>
                <c:if test="${paxGoal.goalQuestPromotion.promotionType.code eq 'challengepoint'}">
                <%-- CHALLENGEPOINT --%>
                <c:if test="${ not empty goalLevelValueBean.amountToAchieveLocaleBased }">
                  <dt><cms:contentText key="YOUR_GOAL" code="promotion.goalquest.selection.wizard"/>:</dt>
                  <dd><c:if test="${paxGoal.goalQuestPromotion.baseUnit ne null }"><c:if test="${paxGoal.goalQuestPromotion.baseUnitPosition ne null and paxGoal.goalQuestPromotion.baseUnitPosition.code eq 'before'}"><c:out value="${paxGoal.goalQuestPromotion.baseUnitText}" escapeXml="false"/>&nbsp;</c:if></c:if><c:out value="${goalLevelValueBean.amountToAchieveLocaleBased}"/><c:if test="${paxGoal.goalQuestPromotion.baseUnit != null }"><c:if test="${paxGoal.goalQuestPromotion.baseUnitPosition ne null and paxGoal.goalQuestPromotion.baseUnitPosition.code eq 'after'}">&nbsp;<c:out value="${paxGoal.goalQuestPromotion.baseUnitText}" escapeXml="false"/></c:if></c:if></dd>
                </c:if>
                </c:if>                

                <%-- GOAL SELECTION --%>
                <dt><cms:contentText key="AWARD" code="promotion.goalquest.selection.wizard"/></dt>
                <dd>
                 <c:if test="${paxGoal.goalQuestPromotion.promotionType.code eq 'challengepoint'}">
                 	<c:if test="${paxGoal.goalQuestPromotion.challengePointAwardType.code eq 'points'}">
               		 <c:choose>
					  <c:when test="${paxGoal.goalQuestPromotion.payoutStructure.code eq 'both' }">	
						<c:out  value="${goalLevelValueBean.awardLocaleBased}"/>&nbsp;<c:out value="${paxGoal.goalQuestPromotion.awardType.name}" />
						<cms:contentText key="FOR" code="promotion.goalquest.selection.wizard" /> 
						<c:if test="${paxGoal.goalQuestPromotion.baseUnit ne null }">
							<c:if test="${paxGoal.goalQuestPromotion.baseUnitPosition ne null and paxGoal.goalQuestPromotion.baseUnitPosition.code eq 'before'}">
								<c:out value="${paxGoal.goalQuestPromotion.baseUnitText}" escapeXml="false"/>
							</c:if>
						</c:if>
						<c:out  value="${goalLevelValueBean.calculatedAchievementAmountLocaleBased}"/>	
						<c:if test="${paxGoal.goalQuestPromotion.baseUnit ne null }">
							<c:if test="${paxGoal.goalQuestPromotion.baseUnitPosition ne null and paxGoal.goalQuestPromotion.baseUnitPosition.code eq 'after'}">
								<c:out value="${paxGoal.goalQuestPromotion.baseUnitText}" escapeXml="false"/>
							</c:if>
						</c:if>									
							
						<cms:contentText key="AND" code="promotion.goalquest.selection.wizard" />
						<c:out value="${goalLevelValueBean.bonusAwardLocaleBased}"/>
						<c:out value="${paxGoal.goalQuestPromotion.awardType.name}" />
						
						<cms:contentText key="FOR" code="promotion.goalquest.selection.wizard" />
						<cms:contentText key="EVERY" code="promotion.goalquest.selection.wizard" />
						<c:if test="${paxGoal.goalQuestPromotion.baseUnit ne null }">
							<c:if test="${paxGoal.goalQuestPromotion.baseUnitPosition ne null and paxGoal.goalQuestPromotion.baseUnitPosition.code eq 'before'}">
								<c:out value="${paxGoal.goalQuestPromotion.baseUnitText}" escapeXml="false"/>
							</c:if>
						</c:if>
						<c:out  value="${goalLevelValueBean.calculatedIncrementAmountLocaleBased}"/>
						<c:if test="${paxGoal.goalQuestPromotion.baseUnit ne null }">
							<c:if test="${paxGoal.goalQuestPromotion.baseUnitPosition ne null and paxGoal.goalQuestPromotion.baseUnitPosition.code eq 'after'}">
								<c:out value="${paxGoal.goalQuestPromotion.baseUnitText}" escapeXml="false"/>
							</c:if>
						</c:if>										
						<cms:contentText key="OVER"	code="promotion.goalquest.selection.wizard" />
						<c:if test="${paxGoal.goalQuestPromotion.baseUnit ne null }">
							<c:if test="${paxGoal.goalQuestPromotion.baseUnitPosition ne null and paxGoal.goalQuestPromotion.baseUnitPosition.code eq 'before'}">
								<c:out value="${paxGoal.goalQuestPromotion.baseUnitText}" escapeXml="false"/>
							</c:if>
						</c:if>
						<c:out  value="${goalLevelValueBean.calculatedMinimumQualifierLocaleBased}"/>
						<c:if test="${paxGoal.goalQuestPromotion.baseUnit ne null }">
							<c:if test="${paxGoal.goalQuestPromotion.baseUnitPosition ne null and paxGoal.goalQuestPromotion.baseUnitPosition.code eq 'after'}">
								<c:out value="${paxGoal.goalQuestPromotion.baseUnitText}" escapeXml="false"/>
							</c:if>
						</c:if>
													
						<c:if test="${ not empty goalLevelValueBean.maximumPointsLocaleBased}">
						  <cms:contentText key="UP_TO" code="promotion.goalquest.selection.wizard" />
						  <c:out  value="${goalLevelValueBean.maximumPointsLocaleBased}"/>
						  <c:out value="${paxGoal.goalQuestPromotion.awardType.name}" />
						</c:if>
					</c:when>
					<c:when test="${paxGoal.goalQuestPromotion.payoutStructure.code eq 'rate' }">
							<c:out value="${goalLevelValueBean.awardLocaleBased}"/>&nbsp;<c:out value="${paxGoal.goalQuestPromotion.awardType.name}" />
							<cms:contentText key="FOR" code="promotion.goalquest.selection.wizard" />
							<cms:contentText key="EVERY" code="promotion.goalquest.selection.wizard" />
							<c:if test="${paxGoal.goalQuestPromotion.baseUnit ne null }">
								<c:if test="${paxGoal.goalQuestPromotion.baseUnitPosition ne null and paxGoal.goalQuestPromotion.baseUnitPosition.code eq 'before'}">
									<c:out value="${paxGoal.goalQuestPromotion.baseUnitText}" escapeXml="false"/>
								</c:if>
							</c:if>
							<c:out value="${goalLevelValueBean.calculatedIncrementAmountLocaleBased}"/>
							<c:if test="${paxGoal.goalQuestPromotion.baseUnit ne null }">
								<c:if test="${paxGoal.goalQuestPromotion.baseUnitPosition ne null and paxGoal.goalQuestPromotion.baseUnitPosition.code eq 'after'}">
									<c:out value="${paxGoal.goalQuestPromotion.baseUnitText}" escapeXml="false"/>
								</c:if>
							</c:if>
							
							<cms:contentText key="OVER"	code="promotion.goalquest.selection.wizard" />
							<c:if test="${paxGoal.goalQuestPromotion.baseUnit ne null }">
								<c:if test="${paxGoal.goalQuestPromotion.baseUnitPosition ne null and paxGoal.goalQuestPromotion.baseUnitPosition.code eq 'before'}">
									<c:out value="${paxGoal.goalQuestPromotion.baseUnitText}" escapeXml="false"/>
								</c:if>
							</c:if>
							<c:out value="${goalLevelValueBean.calculatedMinimumQualifierLocaleBased}"/>
							<c:if test="${paxGoal.goalQuestPromotion.baseUnit ne null }">
								<c:if test="${paxGoal.goalQuestPromotion.baseUnitPosition ne null and paxGoal.goalQuestPromotion.baseUnitPosition.code eq 'after'}">
									<c:out value="${paxGoal.goalQuestPromotion.baseUnitText}" escapeXml="false"/>
								</c:if>
							</c:if>
							<c:if test="${ not empty goalLevelValueBean.maximumPointsLocaleBased }">
							  <cms:contentText key="UP_TO" code="promotion.goalquest.selection.wizard" />
							  <c:out  value="${goalLevelValueBean.maximumPointsLocaleBased}"/><c:out value="${paxGoal.goalQuestPromotion.awardType.name}" />
						    </c:if>								
					</c:when>
					<c:otherwise>
						<c:out value="${goalLevelValueBean.awardLocaleBased}"/>&nbsp;<c:out value="${paxGoal.goalQuestPromotion.awardType.name}" />
					</c:otherwise>
				</c:choose>
				</c:if>
					 <c:if test="${paxGoal.goalQuestPromotion.challengePointAwardType.code eq 'merchTra'}">
					 	<c:if test="${paxGoal.goalQuestPromotion.merchGiftCodeType.code eq 'level'}"> 
							<c:out value="${ goalLevelValueBean.goalLevel.goalLevelName }" />
					 	</c:if>
					 	<c:if test="${paxGoal.goalQuestPromotion.merchGiftCodeType.code eq 'product'}">
					 	  <c:if test="${paxGoal.productSetId != 'null' }">
					 	  	<c:out value="${ paxGoal.selectedProductName }" />
				 	      </c:if>
					 	</c:if>
					</c:if>
                 </c:if>
                  <c:if test="${paxGoal.goalQuestPromotion.promotionType.code eq 'goalquest'}">
                  	<c:if test="${paxGoal.goalQuestPromotion.awardType.code eq 'points'}">
               		 <c:choose>
					  <c:when test="${paxGoal.goalQuestPromotion.payoutStructure.code eq 'both' }">	
						<c:out value="${goalLevelValueBean.awardLocaleBased}"/>&nbsp;<c:out value="${paxGoal.goalQuestPromotion.awardType.name}" />
						<cms:contentText key="FOR" code="promotion.goalquest.selection.wizard" /> 
						<c:if test="${paxGoal.goalQuestPromotion.baseUnit ne null }">
							<c:if test="${paxGoal.goalQuestPromotion.baseUnitPosition ne null and paxGoal.goalQuestPromotion.baseUnitPosition.code eq 'before'}">
								<c:out value="${paxGoal.goalQuestPromotion.baseUnitText}" escapeXml="false"/>
							</c:if>
						</c:if>
						<c:out value="${goalLevelValueBean.calculatedAchievementAmountLocaleBased}"/>
						<c:if test="${paxGoal.goalQuestPromotion.baseUnit ne null }">
							<c:if test="${paxGoal.goalQuestPromotion.baseUnitPosition ne null and paxGoal.goalQuestPromotion.baseUnitPosition.code eq 'after'}">
								<c:out value="${paxGoal.goalQuestPromotion.baseUnitText}" escapeXml="false"/>
							</c:if>
						</c:if>										
							
						<cms:contentText key="AND" code="promotion.goalquest.selection.wizard" />
						<c:out value="${goalLevelValueBean.bonusAwardLocaleBased}"/>
						<c:out value="${paxGoal.goalQuestPromotion.awardType.name}" />
						
						<cms:contentText key="FOR" code="promotion.goalquest.selection.wizard" />
						<cms:contentText key="EVERY" code="promotion.goalquest.selection.wizard" />
						<c:if test="${paxGoal.goalQuestPromotion.baseUnit ne null }">
							<c:if test="${paxGoal.goalQuestPromotion.baseUnitPosition ne null and paxGoal.goalQuestPromotion.baseUnitPosition.code eq 'before'}">
								<c:out value="${paxGoal.goalQuestPromotion.baseUnitText}" escapeXml="false"/>
							</c:if>
						</c:if>
						<c:out value="${goalLevelValueBean.calculatedIncrementAmountLocaleBased}"/>
						<c:if test="${paxGoal.goalQuestPromotion.baseUnit ne null }">
							<c:if test="${paxGoal.goalQuestPromotion.baseUnitPosition ne null and paxGoal.goalQuestPromotion.baseUnitPosition.code eq 'after'}">
								<c:out value="${paxGoal.goalQuestPromotion.baseUnitText}" escapeXml="false"/>
							</c:if>
						</c:if>
																
						<cms:contentText key="OVER"	code="promotion.goalquest.selection.wizard" />
						<c:if test="${paxGoal.goalQuestPromotion.baseUnit ne null }">
							<c:if test="${paxGoal.goalQuestPromotion.baseUnitPosition ne null and paxGoal.goalQuestPromotion.baseUnitPosition.code eq 'before'}">
								<c:out value="${paxGoal.goalQuestPromotion.baseUnitText}" escapeXml="false"/>
							</c:if>
						</c:if>
						<c:out value="${goalLevelValueBean.calculatedMinimumQualifierLocaleBased}"/>
						<c:if test="${paxGoal.goalQuestPromotion.baseUnit ne null }">
							<c:if test="${paxGoal.goalQuestPromotion.baseUnitPosition ne null and paxGoal.goalQuestPromotion.baseUnitPosition.code eq 'after'}">
								<c:out value="${paxGoal.goalQuestPromotion.baseUnitText}" escapeXml="false"/>
							</c:if>
						</c:if>		
											
						<c:if test="${ not empty goalLevelValueBean.maximumPointsLocaleBased }">
						  <cms:contentText key="UP_TO" code="promotion.goalquest.selection.wizard" />
						  <c:out value="${goalLevelValueBean.maximumPointsLocaleBased}"/>
						  <c:out value="${paxGoal.goalQuestPromotion.awardType.name}" />
						</c:if>
					</c:when>
					<c:when test="${paxGoal.goalQuestPromotion.payoutStructure.code eq 'rate' }">
							<c:out value="${goalLevelValueBean.awardLocaleBased}"/>&nbsp;<c:out value="${paxGoal.goalQuestPromotion.awardType.name}" />
							<cms:contentText key="FOR" code="promotion.goalquest.selection.wizard" />
							<cms:contentText key="EVERY" code="promotion.goalquest.selection.wizard" />
							<c:if test="${paxGoal.goalQuestPromotion.baseUnit ne null }">
								<c:if test="${paxGoal.goalQuestPromotion.baseUnitPosition ne null and paxGoal.goalQuestPromotion.baseUnitPosition.code eq 'before'}">
									<c:out value="${paxGoal.goalQuestPromotion.baseUnitText}" escapeXml="false"/>
								</c:if>
							</c:if>
							<c:out value="${goalLevelValueBean.calculatedIncrementAmountLocaleBased}"/>
							<c:if test="${paxGoal.goalQuestPromotion.baseUnit ne null }">
								<c:if test="${paxGoal.goalQuestPromotion.baseUnitPosition ne null and paxGoal.goalQuestPromotion.baseUnitPosition.code eq 'after'}">
									<c:out value="${paxGoal.goalQuestPromotion.baseUnitText}" escapeXml="false"/>
								</c:if>
							</c:if>
							
							<cms:contentText key="OVER"	code="promotion.goalquest.selection.wizard" />
							<c:if test="${paxGoal.goalQuestPromotion.baseUnit ne null }">
								<c:if test="${paxGoal.goalQuestPromotion.baseUnitPosition ne null and paxGoal.goalQuestPromotion.baseUnitPosition.code eq 'before'}">
									<c:out value="${paxGoal.goalQuestPromotion.baseUnitText}" escapeXml="false"/>
								</c:if>
							</c:if>
							<c:out value="${goalLevelValueBean.calculatedMinimumQualifierLocaleBased}"/>
							<c:if test="${paxGoal.goalQuestPromotion.baseUnit ne null }">
								<c:if test="${paxGoal.goalQuestPromotion.baseUnitPosition ne null and paxGoal.goalQuestPromotion.baseUnitPosition.code eq 'after'}">
									<c:out value="${paxGoal.goalQuestPromotion.baseUnitText}" escapeXml="false"/>
								</c:if>
							</c:if>
							<c:if test="${ not empty goalLevelValueBean.maximumPointsLocaleBased }">
							  <cms:contentText key="UP_TO" code="promotion.goalquest.selection.wizard" />
							  <c:out value="${goalLevelValueBean.maximumPointsLocaleBased}"/>&nbsp;<c:out value="${paxGoal.goalQuestPromotion.awardType.name}" />
						    </c:if>								
					</c:when>
					<c:otherwise>
						<c:out value="${goalLevelValueBean.awardLocaleBased}"/>&nbsp;<c:out value="${paxGoal.goalQuestPromotion.awardType.name}" />
					</c:otherwise>
				</c:choose>
				</c:if>
					 <c:if test="${paxGoal.goalQuestPromotion.awardType.code eq 'merchandise'}">
					 	<c:if test="${paxGoal.goalQuestPromotion.merchGiftCodeType.code eq 'level'}"> 
							<c:out value="${ goalLevelValueBean.goalLevel.goalLevelName }" />
					 	</c:if>
					 	<c:if test="${paxGoal.goalQuestPromotion.merchGiftCodeType.code eq 'product'}">
					 	  <c:if test="${paxGoal.productSetId != 'null' }">
					 	  	<c:out value="${ paxGoal.selectedProductName }" />
				 	      </c:if>
					 	</c:if>
					</c:if>
               </c:if>
             </dd>
                <c:if test="${ null!=paxGoal.goalQuestPromotion.partnerAudienceType }">
                <!-- JAVA NOTE: if promo allows partners -->
                <dt><cms:contentText key="PARTNERS" code="promotion.goalquest.selection.wizard"/></dt>
                <dd>
                <!-- JAVA NOTE: "Partners Selected" OR "No Partners Selected" -->
               	<c:choose>
               		<c:when test="${paxPartnerList!=null && not empty paxPartnerList }">
               			<cms:contentText key="PARTNERS_SELECTED" code="promotion.goalquest.selection.wizard"/>
               		</c:when>
               		<c:otherwise>
               			<cms:contentText key="NO_PARTNERS" code="promotion.goalquest.selection.wizard"/>
               		</c:otherwise>
               	</c:choose>
               	</dd>
                </c:if>
            </dl>
    </div><!-- /.modal-body -->
    
  <c:if test="${ hasSurvey }">
    <div class="modal-footer">
      <div class="actions tc">
    	<b><cms:contentText key="SURVEY_WAITING" code="promotion.goalquest.selection.wizard"/></b>
      </div>
    </div>
  </c:if>
</div>
<%-- yuck --%>
<c:remove var="paxGoal" scope="session" />
<c:remove var="paxPartner" scope="session" />
<c:remove var="goalLevelValueBean" scope="session" />
<c:remove var="newGoal" scope="session" />
<c:remove var="goalSelectionModal" scope="session" />