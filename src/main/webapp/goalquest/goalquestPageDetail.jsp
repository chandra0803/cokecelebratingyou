<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%-- ======== GOALQUEST DETAIL PAGE ======== --%>
<%--
    Detail Page for Participants, Partners (and Managers?)
    - JSP powered
    - almost every option is displayed, JSP will have to decide what to show based on the various factors
--%>
<div id="goalquestPageDetailView" class="goalquest page-content">
    <div class="row-fluid">
        <div class="span12">
            <h2>
                <%-- This page services both GQ and CP promotions, but we need to pick the right logo --%>
                <c:choose>
                    <c:when test="${goalQuestDetailsForm.promotion.promotionType.code == 'challengepoint'}">
                        <img src="${pageContext.request.contextPath}/assets/img/goalquest/challengepoint.svg" alt="Challengepoint" class="gqtitlelogo">
                    </c:when>
                    <c:otherwise>
                        <img src="${pageContext.request.contextPath}/assets/img/goalquest/goalquest.svg" alt="GoalQuest" class="gqtitlelogo">
                    </c:otherwise>
                </c:choose>
            </h2>
            <ul class="export-tools fr">
                <li><a class="pageView_print btn btn-small" href="#"><cms:contentText key="PRINT" code="system.button"/> <i class="icon-printer"></i></a></li>
            </ul>
        </div>
    </div>

    <div class="row-fluid">
        <div class="span12">
            <h3><c:out value="${ goalQuestDetailsForm.promotionName }" escapeXml="false"/></h3>

            <!-- rules link -->
            <a class="fr rulesLink" href="${pageContext.request.contextPath}/goalquest/goalquestShowRules.do?method=showRulesDetail&promotionId=${ goalQuestDetailsForm.promotion.id }&paxGoalId=${ goalQuestDetailsForm.paxGoalId }&isPartner=${ goalQuestDetailsForm.partner }&backUrl=showDetail"><cms:contentText key="RULES" code="promotion.goalquest.detail"/></a>

            <div class="gqDescWrapper">
                <dl class="dl-horizontal dl-h1 gqDeets">
                    <dt><cms:contentText key="PROMOTION_OBJECTIVE" code="promotion.goalquest.selection.wizard"/></dt>
                    <dd><c:out value="${ goalQuestDetailsForm.promotion.objectiveFromCM }" escapeXml="false"/></dd>
                    <dt><cms:contentText key="PROGRAM_PERIOD" code="promotion.goalquest.detail"/></dt>
                    <dd>${ goalQuestDetailsForm.promotionStartDate } <cms:contentText key="THROUGH" code="promotion.goalquest.detail"/> ${ goalQuestDetailsForm.promotionEndDate }</dd>
                </dl>
            </div>
        </div>
    </div>

    <hr/>

	<c:choose>
		<c:when test="${ goalQuestDetailsForm.paxGoal.goalLevel !=null }">

    		<%-- CP only - base program description/details --%>
		    <c:if test="${ goalQuestDetailsForm.challengePointPromotion && !goalQuestDetailsForm.partner}">
		    <c:set var = "prec" value = "${goalQuestDetailsForm.promotion.achievementPrecision.precision }"/>
		    <div class="row-fluid cpProgramWrapper">
		        <div class="span12">
		            <h4><cms:contentText key="BASE_PROGRAM" code="promotion.goalquest.detail"/></h4>
		            <p>
		              <c:if test="${thresholdApplicable}">
							<cms:contentTemplateText code="promotion.challengepoint.select.level"
							   key="RIGHT_TILE_INSTRUCTION"
							   args="${cpPaxBean.awardPerIncrementLocaleBased}!
							  ${cpPaxBean.beforeUnitLabel}!
							  ${cpPaxBean.calculatedIncrementAmountLocaleBased}!
							  ${cpPaxBean.afterUnitLabel} "
							   delimiter="!"/>
				      </c:if>

					  <c:if test="${!thresholdApplicable}">
							<cms:contentTemplateText code="promotion.challengepoint.select.level"
							   key="RIGHT_TILE_NO_THRESHOLD"
							   args="
							   ${cpPaxBean.awardPerIncrementLocaleBased}!
							  ${cpPaxBean.beforeUnitLabel}!
							  ${cpPaxBean.calculatedIncrementAmountLocaleBased}!
							  ${cpPaxBean.afterUnitLabel} "
							   delimiter="!"/>
					  </c:if>
		            </p>
		            <p class="gqProgressAsOf"><cms:contentText key="BASE_PROGRESS" code="promotion.goalquest.detail"/> ${ goalQuestDetailsForm.progressDate }</p>
		            <dl class="dl-horizontal dl-h2">
		                <c:if test="${ not empty cpPaxBean.calculatedThresholdLocaleBased }">
			                <dt><cms:contentText key="THRESHOLD" code="promotion.goalquest.detail"/></dt>
			                <dd>

	                        	<span class="baselineValue">
	                        		<c:if test="${goalQuestDetailsForm.promotion.baseUnitText ne null }">
	                        		<c:if test="${goalQuestDetailsForm.promotion.baseUnitPosition ne null and goalQuestDetailsForm.promotion.baseUnitPosition.code eq 'before' }">
	                        		<c:out value="${goalQuestDetailsForm.promotion.baseUnitText}" escapeXml="false"/>
	                        		</c:if></c:if>
	                        		<c:out value="${cpPaxBean.calculatedThresholdLocaleBased}" />
	                        		<c:if test="${goalQuestDetailsForm.promotion.baseUnitText != null }">
	                        		<c:if test="${goalQuestDetailsForm.promotion.baseUnitPosition ne null and goalQuestDetailsForm.promotion.baseUnitPosition.code eq 'after' }">
	                        		<c:out value="${goalQuestDetailsForm.promotion.baseUnitText}" escapeXml="false"/>
	                        		</c:if></c:if>
	                        	</span>
							</dd>
				        </c:if>
		                <dt><cms:contentText key="YOUR_ACTIVITY" code="promotion.goalquest.detail"/></dt>
		                <dd>
		                  <c:if test="${goalQuestDetailsForm.promotion.baseUnitText != null }">
	                        <c:if test="${goalQuestDetailsForm.promotion.baseUnitPosition ne null and goalQuestDetailsForm.promotion.baseUnitPosition.code eq 'before' }">
	                        <c:out value="${goalQuestDetailsForm.promotion.baseUnitText}" escapeXml="false"/>
	                        </c:if>
	                        </c:if>
			                <c:out value="${ cpPaxBean.paxGoal.currentValueLocaleBased}"/>
			                <c:if test="${goalQuestDetailsForm.promotion.baseUnitText != null }">
	                        <c:if test="${goalQuestDetailsForm.promotion.baseUnitPosition ne null and goalQuestDetailsForm.promotion.baseUnitPosition.code eq 'after' }">
	                        <c:out value="${goalQuestDetailsForm.promotion.baseUnitText}" escapeXml="false"/>
	                        </c:if>
	                      </c:if>
		                </dd>
		                <dt><cms:contentText key="BASE_POINTS_EARNED" code="promotion.goalquest.detail"/></dt>
		                <dd><c:out value="${cpPaxBean.totalBaseAwardEarnedLocaleBased}" />  <c:out value="${goalQuestDetailsForm.promotion.awardTypeNameFromCM}" /></dd>
		                <dt><cms:contentText key="BASE_POINTS_DEPOSITED" code="promotion.goalquest.detail"/></dt>
		                <dd><c:out value="${cpPaxBean.totalAwardDepositedLocaleBased}" />  <c:out value="${goalQuestDetailsForm.promotion.awardTypeNameFromCM}" /></dd>
		            </dl>
		        </div>
		    </div><!-- /.cpProgramWrapper -->
		    </c:if>
		    <%-- /CP only --%>

		    <%-- GQ/CP instruction, progress and status message --%>
		    <div class="row-fluid gqProgressWrapper">
		        <div class="span12">
		            <%-- JAVA NOTE: display apropo .progressTitle element --%>
		            <c:if test="${ !goalQuestDetailsForm.partner }">
		            <c:choose>
		            	<c:when test="${ goalQuestDetailsForm.goalQuestPromotion }">
		            		<h4 class="progressTitle"><cms:contentText key="YOUR_GOAL" code="promotion.goalquest.detail"/></h4>
		            	</c:when>
		            	<c:otherwise>
		            		<h4 class="progressTitle"><cms:contentText key="YOUR_CP" code="promotion.goalquest.detail"/></h4>
		            	</c:otherwise>
		            </c:choose>
		            </c:if>

		            <c:if test="${ goalQuestDetailsForm.partner }">
		            <h5 class="progressTitle partnerTitle"><cms:contentText key="PARTNERED_WITH" code="promotion.goalquest.detail"/>
		                <span class="partnerName"><c:out value="${ goalQuestDetailsForm.owner.firstName }"/> <c:out value="${ goalQuestDetailsForm.owner.lastName }"/></span>
		            </h5>
		            </c:if>

		            <%-- NOTE: .gqInstruction is not in any wireframe at the moment, keeping it around in a comment for awhile --%>
		            <c:if test="${goalQuestDetailsForm.gqProgress.percentToGoal > 0 or goalQuestDetailsForm.cpProgress.percentToGoal > 0 }">
		            <p class="gqProgressAsOf"><cms:contentTemplateText key="PROGRESS_AS_OF" code="promotion.goalquest.detail" args="${ goalQuestDetailsForm.progressDate }"/></p>
	                <%-- JAVA NOTE: set the percentage in style attr below --%>
	                <c:set var="precision" value="${ goalQuestDetailsForm.promotion.achievementPrecision.precision }"/>
	                <c:choose>
	                    <c:when test="${ goalQuestDetailsForm.promotion.promotionType.code == 'goalquest' }">
                            <div class="progress progress-tip" data-value="<c:out value="${goalQuestDetailsForm.gqProgress.percentToGoal }"/>%">
                            <c:if test="${goalQuestDetailsForm.gqProgress.percentToGoal ge 100}">
                                <div class="bar" style="width: 100%;">
                                    <%--<c:out value="${goalQuestDetailsForm.gqProgress.percentToGoal }"/>%--%>
                                </div>
                            </c:if>
                            <c:if test="${goalQuestDetailsForm.gqProgress.percentToGoal lt 100}">
                                <div class="bar" style="width: <c:out value="${goalQuestDetailsForm.gqProgress.percentToGoal }"/>%;">
                                    <%--<c:out value="${goalQuestDetailsForm.gqProgress.percentToGoal }"/>%--%>
                                </div>
                            </c:if>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="progress progress-tip" data-value="<c:out value="${goalQuestDetailsForm.cpProgress.percentToGoal }"/>%">
	                  		<c:if test="${goalQuestDetailsForm.cpProgress.percentToGoal ge 100}">
	                    		<div class="bar" style="width: 100%;">
                        			<%--<c:out value="${goalQuestDetailsForm.cpProgress.percentToGoal }"/>%--%>
                                </div>
                            </c:if>
                            <c:if test="${goalQuestDetailsForm.cpProgress.percentToGoal lt 100}">
                                <div class="bar" style="width: <c:out value="${goalQuestDetailsForm.cpProgress.percentToGoal }"/>%;">
                                    <%--<c:out value="${goalQuestDetailsForm.cpProgress.percentToGoal }"/>%--%>
                                </div>
                            </c:if>
                    		</div>
	                    </c:otherwise>
	                </c:choose>
		            </c:if>

		            <%-- result status --%>
		            <%-- JAVA NOTE: display apropo status result message for partner or pax --%>
		            <c:if test="${ goalQuestDetailsForm.promotion.issueAwardsRun }">
		            <c:choose>
		            	<c:when test="${ goalQuestDetailsForm.goalAchieved }">
				            <%-- Stage 4b --%>
				            <p class="statusTxt achieved">
                                <i class="achieveIcn visualItem icon-verification"></i>
				                <c:choose>
				                	<c:when test="${ goalQuestDetailsForm.partner }">
				                		<%-- partner msg --%>
				                		<cms:contentText key="GOAL_ACHIEVED_PARTNER" code="promotion.goalquest.detail"/>
				                	</c:when>
				                	<c:otherwise>
				                		<%-- pax msg --%>
				                		<cms:contentText key="GOAL_ACHIEVED" code="promotion.goalquest.detail"/>
				                	</c:otherwise>
				           		</c:choose>
				            </p>
				       	</c:when>
				       	<c:otherwise>
				       		<%-- Stage 4a --%>
				            <p class="statusTxt notAchieved">
                                <i class="notAchieveIcn visualItem icon-cancel-circle"></i>
				            	<c:choose>
				                	<c:when test="${ goalQuestDetailsForm.partner }">
				                		<%-- partner msg --%>
				                		<cms:contentText key="GOAL_NOT_ACHIEVED_PARTNER" code="promotion.goalquest.detail"/>
				                	</c:when>
				                	<c:otherwise>
				                		<%-- pax msg --%>
				                		<cms:contentText key="GOAL_NOT_ACHIEVED" code="promotion.goalquest.detail"/>
				                	</c:otherwise>
				           		</c:choose>
				            </p>
				       	</c:otherwise>
					</c:choose>
					</c:if>
		        </div>
		    </div><!-- /.gqProgressWrapper -->

		    <%-- GQ/CP selected level/goal details --%>
		    <div class="row-fluid gqLevelWrapper">
		        <div class="span6">
		            <h5 class="levelTitle"><c:out value="${ goalQuestDetailsForm.paxGoal.goalLevel.goalLevelName }"/></h5>
		            <p class="levelDesc"><c:out value="${ goalQuestDetailsForm.paxGoal.goalLevel.goalLevelDescription }"/></p>
		            <dl class="dl-horizontal dl-h2 levelDeets">

		            	<c:if test="${goalQuestDetailsForm.paxGoal.baseQuantity != null }">
		            	<dt>
		            	<c:if test="${ goalQuestDetailsForm.partner}" >
		                <cms:contentText key="BASELINE" code="promotion.goalquest.detail"/>
		                </c:if>
		                <c:if test="${ !goalQuestDetailsForm.partner}" >
		                <cms:contentText key="YOUR_BASELINE" code="promotion.goalquest.detail"/>
		                </c:if>
		                </dt>
		                <dd>
		                <c:if test="${goalQuestDetailsForm.promotion.baseUnitText != null }">
                        <c:if test="${goalQuestDetailsForm.promotion.baseUnitPosition ne null and goalQuestDetailsForm.promotion.baseUnitPosition.code eq 'before' }">
                         <c:out value="${goalQuestDetailsForm.promotion.baseUnitText}" escapeXml="false"/>
                        </c:if>
                        </c:if>
		                <c:out value="${goalQuestDetailsForm.paxGoal.baseQuantityLocaleBased}" />
		                <c:if test="${goalQuestDetailsForm.promotion.baseUnitText != null }">
                        <c:if test="${goalQuestDetailsForm.promotion.baseUnitPosition ne null and goalQuestDetailsForm.promotion.baseUnitPosition.code eq 'after' }">
                        <c:out value="${goalQuestDetailsForm.promotion.baseUnitText}" escapeXml="false"/>
                        </c:if>
                        </c:if>
                        </dd>
		                </c:if>
		                <dt>
		                <c:if test="${ !goalQuestDetailsForm.partner}" >
		                <cms:contentText key="YOUR_GOAL_SELECTION" code="promotion.goalquest.detail"/>
		                </c:if>
		                <c:if test="${ goalQuestDetailsForm.partner}" >
		                <cms:contentText key="GOAL_SELECTION" code="promotion.goalquest.detail"/>
		                </c:if>
		                </dt>
		                 <dd>
		                <c:if test="${goalQuestDetailsForm.promotion.baseUnitText != null }">
                        <c:if test="${goalQuestDetailsForm.promotion.baseUnitPosition ne null and goalQuestDetailsForm.promotion.baseUnitPosition.code eq 'before' }">
                        <c:out value="${goalQuestDetailsForm.promotion.baseUnitText}" escapeXml="false"/>
                        </c:if>
                        </c:if>
                        <c:out value="${ goalQuestDetailsForm.calculatedAchievementAmount }"/>
		                <c:if test="${goalQuestDetailsForm.promotion.baseUnitText != null }">
                        <c:if test="${goalQuestDetailsForm.promotion.baseUnitPosition ne null and goalQuestDetailsForm.promotion.baseUnitPosition.code eq 'after' }">
                        <c:out value="${goalQuestDetailsForm.promotion.baseUnitText}" escapeXml="false"/>
                        </c:if>
                        </c:if>
		                </dd>
		                <c:if test="${ goalQuestDetailsForm.paxGoal.currentValue != null }">
		                <dt>
		                <c:if test="${ !goalQuestDetailsForm.partner}" >
		                <cms:contentText key="YOUR_ACTIVITY" code="promotion.goalquest.detail"/>
		                </c:if>
		                <c:if test="${ goalQuestDetailsForm.partner}" >
		                <cms:contentText key="ACTIVITY" code="promotion.goalquest.detail"/>
		                </c:if>
		                </dt>
		                <dd>
		                <c:if test="${goalQuestDetailsForm.promotion.baseUnitText != null }">
                        <c:if test="${goalQuestDetailsForm.promotion.baseUnitPosition ne null and goalQuestDetailsForm.promotion.baseUnitPosition.code eq 'before' }">
                        <c:out value="${goalQuestDetailsForm.promotion.baseUnitText}" escapeXml="false"/>
                        </c:if>
                        </c:if>
		                <c:out value="${ goalQuestDetailsForm.paxGoal.currentValueLocaleBased}"/>
		                <c:if test="${goalQuestDetailsForm.promotion.baseUnitText != null }">
                        <c:if test="${goalQuestDetailsForm.promotion.baseUnitPosition ne null and goalQuestDetailsForm.promotion.baseUnitPosition.code eq 'after' }">
                        <c:out value="${goalQuestDetailsForm.promotion.baseUnitText}" escapeXml="false"/>
                        </c:if>
                        </c:if>
		                </dd>
		                </c:if>
		                <dt><cms:contentText key="AWARD" code="promotion.goalquest.detail"/></dt>
		                <%-- JAVA NOTE: points or the title of selected plat. aw. --%>
		                <dd>
			                <c:if test="${ goalQuestDetailsForm.partner && not empty  goalQuestDetailsForm.partnerPayout && goalQuestDetailsForm.promotion.awardType.code ne 'merchandise' }">
			                	&nbsp;<c:out value="${ goalQuestDetailsForm.partnerPayout }"/>&nbsp;<c:out value="${ goalQuestDetailsForm.promotion.awardType.name }"/>
			                </c:if>
			                <c:if test="${ ! goalQuestDetailsForm.partner && empty goalQuestDetailsForm.partnerPayout && goalQuestDetailsForm.promotion.awardType.code ne 'merchandise' }">
			                	<c:out value="${ goalQuestDetailsForm.paxGoal.awardLocaleBased }"/>&nbsp;<c:out value="${ goalQuestDetailsForm.promotion.awardType.name }"/>
							</c:if>
							<c:if test="${ null != goalQuestDetailsForm.promotion.awardType &&  goalQuestDetailsForm.promotion.awardType.code eq 'merchandise'}">
					        	<%-- JAVA NOTE:  value = true|false (plateau type only) --%>
			            		<input type="hidden" class="isSelectablePlateauAwardInput" value="${ goalQuestDetailsForm.userAbleToSelectSpecificProduct }">
					        	<input type="hidden" class="promotionIdInput" value="${ goalQuestDetailsForm.promotion.id }">
					        	<input type="hidden" class="levelIdInput" value="${ goalQuestDetailsForm.levelId }">
			                    <span class="awardBrowse awardBtn">
			                        <button class="btn btn-small">
			                        <c:choose>
			        					<c:when test="${ goalQuestDetailsForm.userAbleToChangeProducts }"><cms:contentText key="VIEW_OR_CHANGE" code="promotion.goalquest.selection.wizard"/></c:when>
			        					<c:otherwise><cms:contentText key="BROWSE_AWARDS" code="promotion.goalquest.selection.wizard"/></c:otherwise>
			        				</c:choose>
			        				</button>
			                    </span>
					        </c:if>
						</dd>

		                <%-- JAVA NOTE: only if has partners --%>
		                <c:if test="${ goalQuestDetailsForm.partnersEnabled }">
		                <dt class="partnersLabel"><cms:contentText key="PARTNERS" code="promotion.goalquest.detail"/></dt>
		                <dd class="partnersList">
		                	<c:choose>
		                		<c:when test="${ not empty goalQuestDetailsForm.partners  }">
		                			<c:forEach var="paxPartner" items="${ goalQuestDetailsForm.partners }">
		                    			<span class="label label-inverse"><c:out value="${paxPartner.partner.firstName}"/> <c:out value="${paxPartner.partner.lastName}"/></span>
		                    		</c:forEach>
		                    	</c:when>
		                    	<c:otherwise>
		                    		<cms:contentText key="NO_PARTNERS" code="promotion.goalquest.detail"/>
		                    	</c:otherwise>
		                    </c:choose>
		                </dd>
		                </c:if>
		            </dl>
		        </div>
		        <%-- GQ/CP selected plateau award --%>
		        <%-- JAVA NOTE: only if award selected --%>
		        <c:if test="${ goalQuestDetailsForm.promotion.merchGiftCodeType.product }">
		        <div class="span6 gqPlateauAward">
                    <%-- IF an award is selected, show this image --%>
                    <c:choose>
                      <c:when test="${ goalQuestDetailsForm.productView.img != null }">
                    <img class="paImg" src="${ goalQuestDetailsForm.productView.img }" >
                      </c:when>
                    <%-- ELSE if no award is selected, show this span --%>
                      <c:otherwise>
                    <span class="paImg"></span>
                      </c:otherwise>
                    </c:choose>
		            <p class="paTitle">
		                <c:out value="${ goalQuestDetailsForm.productView.name }"/>
		            </p>
		        </div>
		        </c:if><%-- Show the modal for products if it's a merch code type promotion --%>
		    </div><!-- /.gqLevelWrapper -->

		    <%-- JAVA NOTE: only if uploaded table --%>
		    <c:if test="${ goalQuestDetailsForm.promotion.progressLoadType.code=='auto' }">
		    <div class="row-fluid gqUploadedTableWrapper">
		        <div class="span12">
		            <table class="table table-striped">
		                <thead>
		                    <tr>
		                        <th><cms:contentText key="SALE_DATE" code="promotion.goalquest.detail"/></th>
		                        <th><cms:contentText key="DELIVERY_DATE" code="promotion.goalquest.detail"/></th>
		                        <th><cms:contentText key="MODEL" code="promotion.goalquest.detail"/></th>
		                        <th><cms:contentText key="VIN" code="promotion.goalquest.detail"/></th>
		                        <th><cms:contentText key="TRANSACTION_TYPE" code="promotion.goalquest.detail"/></th>
		                        <th><cms:contentText key="DEALER_CODE" code="promotion.goalquest.detail"/></th>
		                        <th><cms:contentText key="DEALER_NAME" code="promotion.goalquest.detail"/></th>
		                    </tr>
		                </thead>
		                <tbody>
		                <c:if test="${ goalQuestDetailsForm.promotion.promotionType.code == 'goalquest'}">
		                  <c:forEach var="progress" items="${ goalQuestDetailsForm.gqAutoProgressList }">
		                    <tr>
		                        <td><fmt:formatDate value="${progress.saleDate}" pattern="${JstlDatePattern}" /></td>
		                        <td><fmt:formatDate value="${progress.deliveryDate}" pattern="${JstlDatePattern}" /></td>
		                        <td>${ progress.model }</td>
		                        <td>${ progress.vin }</td>
		                        <td>${ progress.transactionType.code }</td>
		                        <td>${ progress.dealerCode }</td>
		                        <td>${ progress.dealerName }</td>
		                    </tr>
		                  </c:forEach>
		                </c:if>

		                <c:if test="${ goalQuestDetailsForm.promotion.promotionType.code == 'challengepoint'}">
		                  <c:forEach var="progress" items="${ goalQuestDetailsForm.cpAutoProgressList }">
		                    <tr>
		                        <td><fmt:formatDate value="${progress.saleDate}" pattern="${JstlDatePattern}" /></td>
		                        <td><fmt:formatDate value="${progress.deliveryDate}" pattern="${JstlDatePattern}" /></td>
		                        <td>${ progress.model }</td>
		                        <td>${ progress.vin }</td>
		                        <td>${ progress.transactionType.code }</td>
		                        <td>${ progress.dealerCode }</td>
		                        <td>${ progress.dealerName }</td>
		                    </tr>
		                  </c:forEach>
		                </c:if>
		                </tbody>
		            </table>
		        </div><!-- /.span12 -->
		    </div><!-- /.gqUploadedTableWrapper -->
		    </c:if>
    	</c:when>
    	<c:otherwise>
    		<%-- JAVA NOTE: only if no goal chosen and too late to choose a goal --%>
		    <div class="row-fluid gqNoGoalTooLateWrapper">
		        <div class="span12">
		            <!-- Stage 3b -->
		            <div class="statusTxt noGoalSel">
		                <i class="levelOpen visualItem icon-cancel-circle"></i>
                        <cms:contentText key="SORRY" code="promotion.goalquest.detail"/> <cms:contentText key="SORRY_NO_GOAL" code="promotion.goalquest.detail"/>
		            </div><!-- /.noGoalSel -->
		        </div>
		    </div><!-- /.gqNoGoalTooLateWrapper -->
    	</c:otherwise>
    </c:choose>

    <div id="levelMerchModal" class="modal modal-stack hide fade" data-y-offset="adjust">
        <div class="modal-header">
            <button data-dismiss="modal" class="close" type="button"><i class="icon-close"></i></button>
            <h3><cms:contentText key="SELECT_AWARD" code="promotion.goalquest.selection.wizard"/></h3>
        </div>
        <div class="modal-body">
            <!-- dynamic -->
        </div>
    </div><!-- /.levelMerchModal -->

</div><!-- /#goalquestPageDetailView -->

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
    $(document).ready(function(){

    	G5.props.URL_JSON_PLATEAU_AWARDS = "${pageContext.request.contextPath}/goalquest/selectGoal.do?method=getAwardsForLevel" ;
    	G5.props.URL_PAGE_PLATEAU_AWARDS = '${pageContext.request.contextPath}/goalquest/awardsPage.do';
        <%-- JAVA NOTE: set this variable to the URL to set a new plateau award --%>
        G5.props.URL_JSON_GOALQUEST_CHANGE_PLATEAU_AWARD = "${pageContext.request.contextPath}/goalquest/selectGoal.do?method=updateAwards" ;//G5.props.URL_JSON_GOALQUEST_CHANGE_PLATEAU_AWARD;

        //attach the view to an existing DOM element
        window.gpdv = new GoalquestPageDetailView({
            el:$('#goalquestPageDetailView'),
            pageNav : {
            	back : {
                    text : '<cms:contentText key="BACK" code="system.button"/>',
                    url : 'javascript:history.go(-1);'
                },
                home : {
                    text : '<cms:contentText key="BACK" code="system.button"/>',
                    url : '<%= RequestUtils.getBaseURI(request)%>/homePage.do'
                }
            },
            <c:choose>
        	  <c:when test="${goalQuestDetailsForm.promotion.promotionType.code == 'challengepoint'}">
        	  	pageTitle : '<cms:contentText key="CP_PAGE_TITLE" code="promotion.goalquest.detail"/>'
           	  </c:when>
        	  <c:otherwise>
        	  	pageTitle : '<cms:contentText key="PAGE_TITLE" code="promotion.goalquest.detail"/>'
   	          </c:otherwise>
        	</c:choose>
        });
    });
</script>

<%-- Stealing the product and product detail viewer from the activities/recognition pages --%>
<script type="text/template" id="plateauAwardsItemTpl">
	<%@include file="/activities/plateauAwardsItem.jsp" %>
</script>
<script type="text/template" id="plateauAwardsDrawerTpl">
	<%@include file="/activities/plateauAwardsDrawer.jsp" %>
</script>
