<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<!-- ======== GOALQUEST PAGE EDIT ======== -->

<!--
    Create and Edit GQs and CPs
    - struts powered
-->

<c:set var="baseAmount" value="false"/>

<c:choose>
    <c:when test="${ goalQuestWizardForm.uaEnabled == true }">
    <div id="goalquestPageEditView" class="goalquest page-content ua-page">
        <div class="ua-decoration"><!--UA-add entire div-->
        </div>
    </c:when>
    <c:otherwise>
    <div id="goalquestPageEditView" class="goalquest page-content">
    </c:otherwise>
</c:choose>

<c:choose>
    <c:when test="${ goalQuestWizardForm.uaEnabled == true }">
        <div class="row-fluid ua-page-content ua-widget-container">
    </c:when>
    <c:otherwise>
        <div class="row-fluid">
    </c:otherwise>
</c:choose>

        <div class="span12 logo-container">
            <h2>
                <%-- This page services both GQ and CP promotions, but we need to pick the right logo --%>
                <c:choose>
                    <c:when test="${ goalQuestWizardForm.promotionType == 'challengepoint' }">
                        <img src="${pageContext.request.contextPath}/assets/img/goalquest/challengepoint.svg" alt="Challengepoint" class="gqtitlelogo">
                    </c:when>
                    <c:otherwise>
                        <img src="${pageContext.request.contextPath}/assets/img/goalquest/goalquest.svg" alt="GoalQuest" class="gqtitlelogo">
                    </c:otherwise>
                </c:choose>
            </h2>

            <c:choose>
                <c:when test="${ goalQuestWizardForm.uaEnabled == true  &&  goalQuestWizardForm.uaConnected == true}">
                    <div class="ua-logo">
                        <div class="ua-widget connected">
                            <div class="ua-dot" data-title-connect-status="Connected">
                                    <i class="icon icon-link-2"></i>
                            </div>
                        </div>
                    </div>
                </c:when>
                <c:when test="${ goalQuestWizardForm.uaEnabled == true  &&  goalQuestWizardForm.uaConnected == false}">
                    <div class="ua-logo">
                        <div class="ua-widget">
                            <div class="ua-dot" data-title-connect-status="Not Connected">
                                    <i class="icon icon-link-3-broken"></i>
                            </div>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                </c:otherwise>
            </c:choose>

            <ul class="export-tools fr">
                <li><a class="pageView_print btn btn-small" href="#"><cms:contentText key="PRINT" code="system.button"/> <i class="icon-printer"></i></a></li>
            </ul>
        </div>
    </div>

    <c:choose>
        <c:when test="${ goalQuestWizardForm.uaEnabled == true }">
            <div class="row-fluid ua-page-content ua-tab-container">
        </c:when>
        <c:otherwise>
            <div class="row-fluid">
        </c:otherwise>
    </c:choose>


        <div class="span12">
            <h3><c:out value="${ goalQuestWizardForm.promotionName }" escapeXml="false"/></h3>

            <!-- WizardTabsView -->
            <ul class="wizardTabsView" data-content=".wizardTabsContent" style="visibility:hidden">
                <!-- generated using json+tpl by WizardTabsView -->
            </ul><!-- /.wizardTabsView -->
        </div>
    </div>
    <c:choose>
        <c:when test="${ goalQuestWizardForm.uaEnabled == true }">
            <div class="row-fluid ua-page-content">
        </c:when>
        <c:otherwise>
            <div class="row-fluid">
        </c:otherwise>
    </c:choose>
        <div class="span12">

            <!-- tab contents -->
            <div class="wizardTabsContent">
		<html:form styleClass="gqEditForm" action="selectGoal.do?method=submit" >

					<%-- JAVA NOTE: value = promotionId --%>
                    <html:hidden styleClass="promotionIdInput" property="promotionId"/>

                    <%-- JAVA NOTE: value = stepOverview|stepRules|stepGoal|stepPartner|stepSubmit (set active step) --%>
                    <html:hidden styleClass="activeStepInput" property="activeStep"/>

                    <%-- JAVA NOTE: value = true|false (showing the select partner step)--%>
                    <html:hidden styleClass="showPartnerStepInput" property="isPartnersEnabled"/>

                    <%-- JAVA NOTE: value = true|false (showing the UA Connect step)--%>
                    <html:hidden property="uaEnabled" />
                    <html:hidden styleClass="showUAStepInput" property="showUAStepInput" />

                    <%-- JAVA NOTE: selected partners (please see Send a Recognition for example) --%>
                    <html:hidden property="partnersCount"/>

					<c:if test="${ not empty goalQuestWizardForm.preselectedPartners }">
						<c:forEach var="partner" items="${ goalQuestWizardForm.preselectedPartners }" varStatus="index">
							<input type="hidden" name="partners[${ index.index }].id" value="${ partner.id }" />
		                    <input type="hidden" name="partners[${ index.index }].countryCode" value="${ partner.countryCode }" />
		                    <input type="hidden" name="partners[${ index.index }].countryName" value="${ partner.countryName }" />
		                    <input type="hidden" name="partners[${ index.index }].firstName" value="${ partner.firstName }" />
		                    <input type="hidden" name="partners[${ index.index }].lastName" value="${ partner.lastName }" />
		                    <input type="hidden" name="partners[${ index.index }].orgName" value="${ partner.orgName }" />
		                    <input type="hidden" name="partners[${ index.index }].departmentName" value="${ partner.departmentName }" />
		                    <input type="hidden" name="partners[${ index.index }].jobName" value="${ partner.jobName }" />
						</c:forEach>
					</c:if>

                    <%-- JAVA NOTE:  value = points|plateau --%>
					<html:hidden styleClass="awardTypeInput" property="awardType"/>

                    <%-- JAVA NOTE:  value = true|false (plateau type only) if true, select specific product - if false, you're selecting the level --%>
                    <html:hidden styleClass="isSelectablePlateauAwardInput" property="isProductOnly"/>

                    <%-- JAVA NOTE:  value = true|false (plateau type only/selectable only) --%>
                    <input type="hidden" class="isPlateauAwardRequiredInput" name="someStrutsNameIsPlatAwdReq" value="true">
                    <%-- JAVA NOTE: value = maximum allowed partners (pre-populated may exceed, but user will be asked to remove extras) --%>
                    <html:hidden styleClass="maxPartnersInput" property="maxPartnersInput"/>

                    <%-- **************************************************************
                        OVERVIEW
                     ***************************************************************** --%>

                     <c:choose>
						<c:when test="${ goalQuestWizardForm.uaEnabled == true }">
						      <div class="stepOverviewContent stepContent" style="display:none">
                                  <h4><cms:contentText key="OVERVIEW" code="promotion.goalquest.selection.wizard"/></h4>
                                  <p>
                                      <c:out value="${ goalQuestWizardForm.overview }" escapeXml="false"/>
                                  </p>
                        <cms:contentText key="WHAT_IS_THIS" code="promotion.goalquest.selection.wizard"/>
                        <section class="ua-partners">
                        <cms:contentText key="UA_CONNECT_FITNESS" code="promotion.goalquest.selection.wizard"/>
                            <p class="partner-logos">
                             <img src="${pageContext.request.contextPath}/assets/img/underarmour/ua-device-nike.png" alt="">
                             <img src="${pageContext.request.contextPath}/assets/img/underarmour/ua-device-fitbit.png" alt="">
                             <img src="${pageContext.request.contextPath}/assets/img/underarmour/ua-device-jawbone.png" alt="">
                             <img src="${pageContext.request.contextPath}/assets/img/underarmour/ua-device-myfitnesspal.png" alt="">
                             <img src="${pageContext.request.contextPath}/assets/img/underarmour/ua-device-wego.png" alt="">
                            </p>
                        </section>
                        <hr />
                        <cms:contentText key="WHAT_TO_DO" code="promotion.goalquest.selection.wizard"/>
                        <p class="partner-apps">
                            <img src="${pageContext.request.contextPath}/assets/img/underarmour/ua-app-record.png" alt="">
                             <img src="${pageContext.request.contextPath}/assets/img/underarmour/ua-app-39.png" alt="">
                             <%-- <img src="${pageContext.request.contextPath}/assets/img/underarmour/ua-app-endomondo.png" alt=""> --%>
                             <img src="${pageContext.request.contextPath}/assets/img/underarmour/ua-app-mapmy.png" alt="">
                             <img src="${pageContext.request.contextPath}/assets/img/underarmour/ua-app-myfitnesspal.png" alt="">
                        </p>
                        <%-- <cms:contentText key="READY_TO_GO" code="promotion.goalquest.selection.wizard"/> --%>
                        <div class="stepContentControls form-actions pullBottomUp">
                            <button class="btn btn-primary nextBtn">
                                <cms:contentText key="NEXT" code="system.button"/> &raquo;
                            </button>
                        </div><!-- /.stepContentControls -->

                    </div><!-- /.stepOverviewContent -->
						</c:when>
						<c:otherwise>
						    <div class="stepOverviewContent stepContent" style="display:none">
                                <h4><cms:contentText key="OVERVIEW" code="promotion.goalquest.selection.wizard"/></h4>
                                <p>
                                    <c:out value="${ goalQuestWizardForm.overview }" escapeXml="false"/>
                                </p>
                             <div class="stepContentControls form-actions pullBottomUp">
                            <button class="btn btn-primary nextBtn">
                                <cms:contentText key="NEXT" code="system.button"/> &raquo;
                            </button>
                        </div><!-- /.stepContentControls -->

                    </div><!-- /.stepOverviewContent -->
						</c:otherwise>

					</c:choose>




                    <!-- **************************************************************
                        RULES
                     ***************************************************************** -->
                    <div class="stepRulesContent stepContent" style="display:none">
                     <div class="row-fluid">
                     <div class="span12">
                         <h4><cms:contentText key="RULES" code="promotion.goalquest.selection.wizard"/></h4>
                    <dl class="dl-horizontal dl-h1">
                       <dt><cms:contentText key="PROMOTION_OBJECTIVE" code="promotion.goalquest.selection.wizard"/></dt>
                            <dd><c:out value="${ goalQuestWizardForm.objectiveFromCM }" escapeXml="false"/></dd>
                       <dt><cms:contentText key="PROGRAM_PERIOD" code="promotion.goalquest.selection.wizard"/></dt>
                            <dd><fmt:formatDate value="${ goalQuestWizardForm.promotionStartDate }" type="date" pattern='${JstlDatePattern}'/> <cms:contentText key="THROUGH" code="promotion.goalquest.selection.wizard"/> <fmt:formatDate value="${ goalQuestWizardForm.promotionEndDate }" type="date" pattern='${JstlDatePattern}'/></dd>
                    </dl>
                   </div>
                   </div>
                   <div class="rulesText">
                        <c:out value="${ goalQuestWizardForm.rules }" escapeXml="false"/>
                    </div>
                        <div class="stepContentControls form-actions pullBottomUp">
                            <button class="btn backBtn">
                                &laquo; <cms:contentText key="BACK" code="system.button"/>
                            </button>
                            <button class="btn btn-primary nextBtn">
                                <cms:contentText key="NEXT" code="system.button"/> &raquo;
                            </button>
                        </div><!-- /.stepContentControls -->

                    </div><!-- /.stepRulesContent -->

                    <!-- **************************************************************
                        GOAL
                     ***************************************************************** -->
                    <div class="stepGoalContent stepContent" style="display:none">

					<c:if test="${ goalQuestWizardForm.promotionType == 'challengepoint' }" >
                        <h4><cms:contentText key="BASE_PROGRAM" code="promotion.goalquest.detail"/></h4>
                        <p>
                          	<c:set var = "prec" value = "${promotion.achievementPrecision.precision }"/>
							<c:if test="${thresholdApplicable}">
							<cms:contentTemplateText code="promotion.challengepoint.select.level"
							   key="RIGHT_TILE_INSTRUCTION"
							   args="${goalQuestWizardForm.challengepointPaxValueBean.awardPerIncrementLocaleBased}!
							 ${goalQuestWizardForm.challengepointPaxValueBean.beforeUnitLabel}!
							 ${goalQuestWizardForm.challengepointPaxValueBean.calculatedIncrementAmountLocaleBased}!
							 ${goalQuestWizardForm.challengepointPaxValueBean.afterUnitLabel} "
							  delimiter="!"/>
							</c:if>

							<c:if test="${!thresholdApplicable}">
							<cms:contentTemplateText code="promotion.challengepoint.select.level"
							   key="RIGHT_TILE_NO_THRESHOLD"
							   args="
							   ${goalQuestWizardForm.challengepointPaxValueBean.awardPerIncrementLocaleBased}!
							 ${goalQuestWizardForm.challengepointPaxValueBean.beforeUnitLabel}!
							 ${goalQuestWizardForm.challengepointPaxValueBean.calculatedIncrementAmountLocaleBased}!
							 ${goalQuestWizardForm.challengepointPaxValueBean.afterUnitLabel} "
							  delimiter="!"/>
							</c:if>
                        </p>
                        <c:if test="${ not empty goalQuestWizardForm.challengepointPaxValueBean.calculatedThresholdLocaleBased}">
                        <dl class="dl-horizontal dl-h1">
                            <dt><cms:contentText key="THRESHOLD" code="promotion.goalquest.selection.wizard"/></dt>
                            <dd>
                           		<c:if test="${promotion.baseUnit ne null }">
                            		<c:if test="${promotion.baseUnitPosition ne null and promotion.baseUnitPosition.code eq 'before' }">
                            			<c:out value="${promotion.baseUnitText}" escapeXml="false"/>
                            		</c:if>
                           		</c:if>
                           		<c:out value="${ goalQuestWizardForm.challengepointPaxValueBean.calculatedThresholdLocaleBased }"/>
                           		<c:if test="${promotion.baseUnit ne null }">
                            		<c:if test="${promotion.baseUnitPosition ne null and promotion.baseUnitPosition.code eq 'after' }">
                            			<c:out value="${promotion.baseUnitText}" escapeXml="false"/>
                            		</c:if>
                           		</c:if>
                             </dd>
                        </dl>
                        </c:if>
                     </c:if>

                        <h4><cms:contentText key="SELECT_GOAL" code="promotion.goalquest.selection.wizard"/></h4>
                        <c:if test="${ goalQuestWizardForm.promotionType == 'challengepoint' }" >
                        <p class="goalDesc"><cms:contentText key="EARN_POINTS" code="promotion.goalquest.selection.wizard"/></p>
                        </c:if>
                        <p class="selectBy"><cms:contentText key="SELECT_GOAL_BY" code="promotion.goalquest.selection.wizard"/> <fmt:formatDate value="${ goalQuestWizardForm.goalSelectionEndDate }" type="date" pattern='${JstlDatePattern}'/></p>

                        <div class="form-horizontal levelsWrapper">
                            <fieldset>
                                <%-- JAVA NOTE: selected plateauAward item data (edit AND plateau type only) --%>
                                <html:hidden styleClass="selectedPlateauAwardIdInput" property="selectedProductId" />
                                <html:hidden styleClass="selectedPlateauAwardNameInput" property="selectedProductName" />
                                <html:hidden styleClass="selectedPlateauAwardImgUrlInput" property="selectedProductImgUrl"/>
                                <%--
                                    JAVA NOTE: loop through and render the levels
                                    - JAVA should produce a checked radio input on a selected level (for edit gq)
                                    - all DOM elements should be included within each .levelItem, JS will manipulate
                                --%>
                                <c:choose>
                                  <c:when test="${ goalQuestWizardForm.promotionType == 'goalquest' }">
                                    <c:forEach var="level" items="${goalLevelBeans}">
		                                <label class="radio levelItem">
		                                	<html:radio property="selectedGoalId" styleClass="levelIdInput" value="${level.goalLevel.id}"/>

		                                    <p class="levelName"><c:out value="${ level.goalLevel.goalLevelName}"/></p>
		                                    <p class="levelDesc"><c:out value="${ level.goalLevel.goalLevelDescription}"/></p>
		                                    <div class="levelDeets">
		                                        <div class="deetLabels">
		                                        	<c:if test="${not empty level.baseAmountLocaleBased }">
		                                        		<c:set var="baseAmount" value="true"/>
		                                            	<span><cms:contentText key="BASELINE" code="promotion.goalquest.selection.wizard"/></span>
		                                            </c:if>
		                                            <c:if test="${not empty level.calculatedGoalAmtLocaleBased}">
		                                            	<span><cms:contentText key="YOUR_GOAL" code="promotion.goalquest.selection.wizard"/>:</span>
		                                            </c:if>
		                                            <span><cms:contentText key="AWARD" code="promotion.goalquest.selection.wizard"/></span>
		                                        </div>
		                                        <div class="deetValues">
		                                        	<c:if test="${not empty level.baseAmountLocaleBased }">
		                                            	<span class="baselineValue">
		                                            		<c:if test="${promotion.baseUnit ne null }">
			                                            		<c:if test="${promotion.baseUnitPosition ne null and promotion.baseUnitPosition.code eq 'before' }">
			                                            			<c:out value="${promotion.baseUnitText}" escapeXml="false"/>
			                                            		</c:if>
		                                            		</c:if>
		                                            		<c:out value="${level.baseAmountLocaleBased}"/>
		                                            		<c:if test="${promotion.baseUnit ne null }">
			                                            		<c:if test="${promotion.baseUnitPosition ne null and promotion.baseUnitPosition.code eq 'after' }">
			                                            			<c:out value="${promotion.baseUnitText}" escapeXml="false"/>
			                                            		</c:if>
		                                            		</c:if>
		                                            	</span>
		                                            </c:if>
		                                            <c:if test="${not empty level.calculatedGoalAmtLocaleBased }">
		                                            	<span class="goalValue">
		                                            		<c:if test="${promotion.baseUnit ne null }">
			                                            		<c:if test="${promotion.baseUnitPosition ne null and promotion.baseUnitPosition.code eq 'before' and level.managerOverride ne 'true'}">
			                                            			<c:out value="${promotion.baseUnitText}" escapeXml="false"/>
			                                            		</c:if>
		                                            		</c:if>
		                                            		<c:out value="${level.calculatedGoalAmtLocaleBased}"/>
		                                            		<c:if test="${promotion.baseUnit ne null }">
			                                            		<c:if test="${promotion.baseUnitPosition ne null and promotion.baseUnitPosition.code eq 'after' and level.managerOverride ne 'true'}">
			                                            			<c:out value="${promotion.baseUnitText}" escapeXml="false"/>
			                                            		</c:if>
		                                            		</c:if>
															<c:if test="${level.managerOverride }">%</c:if>
		                                            	</span>
		                                            </c:if>
		                                            <!-- points award -->
		                                            <span class="awardValue wideValue">
			                                             <c:if test="${promotion.awardType.code eq 'points'}">
															<c:choose>
																<c:when test="${level.managerOverride}">
																	<fmt:formatNumber value="${level.goalLevel.managerAward}"/>
																</c:when>
																<c:when test="${promotion.payoutStructure.code eq 'both' }">
																	<c:out value="${level.awardLocaleBased}"/>
																	&nbsp;<c:out value="${promotion.awardTypeNameFromCM}" />
																	<cms:contentText key="FOR"
																		code="promotion.goalquest.selection.wizard" />
																	<c:if test="${promotion.baseUnit ne null }">
									                            		<c:if test="${promotion.baseUnitPosition ne null and promotion.baseUnitPosition.code eq 'before' }">
									                            			<c:out value="${promotion.baseUnitText}" escapeXml="false"/>
									                            		</c:if>
									                           		</c:if>
																	<c:out value="${level.calculatedAchievementAmountLocaleBased}"/>
																	<c:if test="${promotion.baseUnit ne null }">
									                            		<c:if test="${promotion.baseUnitPosition ne null and promotion.baseUnitPosition.code eq 'after' }">
									                            			<c:out value="${promotion.baseUnitText}" escapeXml="false"/>
									                            		</c:if>
									                           		</c:if>

																	<cms:contentText key="AND"
																		code="promotion.goalquest.selection.wizard" />
																	<c:out value="${level.bonusAwardLocaleBased}"/>
																	&nbsp;
																	<c:out value="${promotion.awardTypeNameFromCM}" />

																	<cms:contentText key="FOR"
																		code="promotion.goalquest.selection.wizard" />
																	<cms:contentText key="EVERY"
																		code="promotion.goalquest.selection.wizard" />
																	<c:if test="${promotion.baseUnit ne null }">
									                            		<c:if test="${promotion.baseUnitPosition ne null and promotion.baseUnitPosition.code eq 'before' }">
									                            			<c:out value="${promotion.baseUnitText}" escapeXml="false"/>
									                            		</c:if>
									                           		</c:if>
																	<c:out value="${level.calculatedIncrementAmountLocaleBased}"/>
																	<c:if test="${promotion.baseUnit ne null }">
									                            		<c:if test="${promotion.baseUnitPosition ne null and promotion.baseUnitPosition.code eq 'after' }">
									                            			<c:out value="${promotion.baseUnitText}" escapeXml="false"/>
									                            		</c:if>
									                           		</c:if>

																	<cms:contentText key="OVER"
																		code="promotion.goalquest.selection.wizard" />
																	<c:if test="${promotion.baseUnit ne null }">
									                            		<c:if test="${promotion.baseUnitPosition ne null and promotion.baseUnitPosition.code eq 'before' }">
									                            			<c:out value="${promotion.baseUnitText}" escapeXml="false"/>
									                            		</c:if>
									                           		</c:if>
																	<c:out value="${level.calculatedMinimumQualifierLocaleBased}"/>
																	<c:if test="${promotion.baseUnit ne null }">
									                            		<c:if test="${promotion.baseUnitPosition ne null and promotion.baseUnitPosition.code eq 'after' }">
									                            			<c:out value="${promotion.baseUnitText}" escapeXml="false"/>
									                            		</c:if>
									                           		</c:if>

																	<c:if test="${not empty level.maximumPointsLocaleBased}">
																	  <cms:contentText key="UP_TO"
																		code="promotion.goalquest.selection.wizard" />
																	  <c:out value="${level.maximumPointsLocaleBased}"/>
																	  &nbsp;
																	  <c:out value="${promotion.awardTypeNameFromCM}" />
																	</c:if>
																</c:when>
																<c:when test="${promotion.payoutStructure.code eq 'rate' }">
																	<c:out value="${level.awardLocaleBased}"/>&nbsp;<c:out value="${promotion.awardTypeNameFromCM}" />
																	<cms:contentText key="FOR"
																		code="promotion.goalquest.selection.wizard" />
																	<cms:contentText key="EVERY"
																		code="promotion.goalquest.selection.wizard" />
																	<c:if test="${promotion.baseUnit ne null }">
									                            		<c:if test="${promotion.baseUnitPosition ne null and promotion.baseUnitPosition.code eq 'before' }">
									                            			<c:out value="${promotion.baseUnitText}" escapeXml="false"/>
									                            		</c:if>
										                           	</c:if>
																	<c:out value="${level.calculatedIncrementAmountLocaleBased}"/>
																	<c:if test="${promotion.baseUnit ne null }">
									                            		<c:if test="${promotion.baseUnitPosition ne null and promotion.baseUnitPosition.code eq 'after' }">
									                            			<c:out value="${promotion.baseUnitText}" escapeXml="false"/>
									                            		</c:if>
										                           	</c:if>
																	<cms:contentText key="OVER"
																		code="promotion.goalquest.selection.wizard" />
																	<c:if test="${promotion.baseUnit ne null }">
									                            		<c:if test="${promotion.baseUnitPosition ne null and promotion.baseUnitPosition.code eq 'before' }">
									                            			<c:out value="${promotion.baseUnitText}" escapeXml="false"/>
									                            		</c:if>
										                           	</c:if>
																	<c:out value="${level.calculatedMinimumQualifierLocaleBased}"/>
																	<c:if test="${promotion.baseUnit ne null }">
									                            		<c:if test="${promotion.baseUnitPosition ne null and promotion.baseUnitPosition.code eq 'after' }">
									                            			<c:out value="${promotion.baseUnitText}" escapeXml="false"/>
									                            		</c:if>
										                           	</c:if>
																	<c:if test="${not empty level.maximumPointsLocaleBased}">
																	  <cms:contentText key="UP_TO"
																		code="promotion.goalquest.selection.wizard" />
																	  <c:out value="${level.maximumPointsLocaleBased}"/>&nbsp;<c:out value="${promotion.awardTypeNameFromCM}" />
																    </c:if>
																</c:when>
																<c:otherwise>
																	<c:out value="${level.awardLocaleBased}"/>&nbsp;<c:out value="${promotion.awardTypeNameFromCM}" />
																</c:otherwise>
															</c:choose>
														</c:if>
													</span>
		                                            <!-- selected award name -->
		                                            <span class="awardName wideValue">
		                                                <!-- dynamic JS -->
		                                            </span>
		                                            <!-- browse award -->
		                                            <span class="awardBrowse awardBtn">
		                                                <button class="btn btn-small"><cms:contentText key="BROWSE_AWARDS" code="promotion.goalquest.selection.wizard"/></button>
		                                            </span>
		                                            <!-- set award -->
		                                            <span class="awardSet awardBtn">
		                                                <button class="btn btn-small btn-primary"><cms:contentText key="SELECT_AWARD" code="promotion.goalquest.selection.wizard"/></button>
		                                            </span>
		                                            <!-- edit award -->
		                                            <span class="awardEdit awardBtn">
		                                                <button class="btn btn-small"><cms:contentText key="CHANGE_AWARD" code="promotion.goalquest.selection.wizard"/></button>
		                                            </span>
		                                        </div><!-- /.deetValues -->
		                                    </div><!-- /.levelDeets -->
		                                </label><!-- /.radio -->
								     </c:forEach>
                                  </c:when>
                                  <c:otherwise>
                                    <c:forEach var="level" items="${cpLevelBeans}">
		                                <label class="radio levelItem">
		                                	<html:radio property="selectedGoalId" styleClass="levelIdInput" value="${level.goalLevel.id}"/>

		                                    <p class="levelName"><c:out value="${ level.goalLevel.goalLevelName}"/></p>
		                                    <p class="levelDesc"><c:out value="${ level.goalLevel.goalLevelDescription}"/></p>
		                                    <div class="levelDeets">
		                                          <div class="baseProgramDeets" style="display:none">
		                                            <div class="span12">
		                                                <h4><cms:contentText key="BASE_PROGRAM" code="promotion.goalquest.detail"/></h4>
		                                                <p>
		                                                  	<c:set var = "prec" value = "${promotion.achievementPrecision.precision }"/>
															<c:if test="${thresholdApplicable}">
																<cms:contentTemplateText code="promotion.challengepoint.select.level"
																   key="RIGHT_TILE_INSTRUCTION"
																   args="${level.awardPerIncrementLocaleBased}!
																  ${level.beforeUnitLabel}!
																  ${level.calculatedIncrementAmountLocaleBased}!
																  ${level.afterUnitLabel} "
																   delimiter="!"/>
															</c:if>

															<c:if test="${!thresholdApplicable}">
																<cms:contentTemplateText code="promotion.challengepoint.select.level"
																   key="RIGHT_TILE_NO_THRESHOLD"
																   args="
																   ${level.awardPerIncrementLocaleBased}!
																  ${level.beforeUnitLabel}!
																  ${level.calculatedIncrementAmountLocaleBased}!
																  ${level.afterUnitLabel} "
																   delimiter="!"/>
															</c:if>
		                                                </p>
		                                                <c:if test="${ not empty level.calculatedThresholdLocaleBased}">
		                                                  <dl class="dl-horizontal dl-h1">
		                                                    <dt><cms:contentText key="THRESHOLD" code="promotion.goalquest.selection.wizard"/></dt>
		                                                    <dd>
		                                                    <c:if test="${promotion.baseUnit ne null }">
			                                            		<c:if test="${promotion.baseUnitPosition ne null and promotion.baseUnitPosition.code eq 'before' }">
			                                            			<c:out value="${promotion.baseUnitText}" escapeXml="false"/>
			                                            		</c:if>
		                                            		</c:if>
		                                                    <c:out value="${ level.calculatedThresholdLocaleBased }"/>
		                                                    <c:if test="${promotion.baseUnit ne null }">
			                                            		<c:if test="${promotion.baseUnitPosition ne null and promotion.baseUnitPosition.code eq 'after' }">
			                                            			<c:out value="${promotion.baseUnitText}" escapeXml="false"/>
			                                            		</c:if>
		                                            		</c:if>
		                                                    </dd>
		                                                    <%-- <dt>Your Activity:</dt>
		                                                    <dd>48</dd>
		                                                    <dt>Base Program Points Earned</dt>
		                                                    <dd>
		                                                      <c:out value="${ level.totalBasicAwardEarned }" />
		                                                    </dd>
		                                                    <dt>Base Program Points Deposited</dt>
		                                                    <dd>
		                                                      <c:out value="${ level.interimAwardDeposited }" />
		                                                    </dd> --%>
                                                          </dl>
		                                                </c:if>
		                                            </div>
		                                          </div><!-- /.baseProgramDeets -->
		                                        <div class="deetLabels">
		                                        	<c:if test="${ not empty level.baseAmountLocaleBased }">
		                                        		<c:set var="baseAmount" value="true"/>
		                                            	<span><cms:contentText key="BASELINE" code="promotion.goalquest.selection.wizard"/></span>
		                                            </c:if>
		                                            <c:if test="${ not empty level.amountToAchieveLocaleBased }">
		                                            	<span><cms:contentText key="YOUR_GOAL" code="promotion.goalquest.selection.wizard"/>:</span>
		                                            </c:if>
		                                            <span><cms:contentText key="AWARD" code="promotion.goalquest.selection.wizard"/></span>
		                                        </div>
		                                        <div class="deetValues">
		                                        	<c:if test="${ not empty level.baseAmountLocaleBased }">
		                                            	<span class="baselineValue">
		                                            		<c:if test="${promotion.baseUnit ne null }">
			                                            		<c:if test="${promotion.baseUnitPosition ne null and promotion.baseUnitPosition.code eq 'before' }">
			                                            			<c:out value="${promotion.baseUnitText}" escapeXml="false"/>
			                                            		</c:if>
		                                            		</c:if>
		                                            		<c:out  value="${level.baseAmountLocaleBased}"/>
		                                            		<c:if test="${promotion.baseUnit ne null }">
			                                            		<c:if test="${promotion.baseUnitPosition ne null and promotion.baseUnitPosition.code eq 'after' }">
			                                            			<c:out value="${promotion.baseUnitText}" escapeXml="false"/>
			                                            		</c:if>
		                                            		</c:if>
		                                            	</span>
		                                            </c:if>
		                                            <c:if test="${ not empty level.amountToAchieveLocaleBased }">
		                                            	<span class="goalValue">
		                                            		<c:if test="${promotion.baseUnit ne null }">
			                                            		<c:if test="${promotion.baseUnitPosition ne null and promotion.baseUnitPosition.code eq 'before'}">
			                                            			<c:out value="${promotion.baseUnitText}" escapeXml="false"/>
			                                            		</c:if>
		                                            		</c:if>
		                                            		<c:out value="${level.amountToAchieveLocaleBased}"/>
		                                            		<c:if test="${promotion.baseUnit ne null }">
			                                            		<c:if test="${promotion.baseUnitPosition ne null and promotion.baseUnitPosition.code eq 'after'}">
			                                            			<c:out value="${promotion.baseUnitText}" escapeXml="false"/>
			                                            		</c:if>
		                                            		</c:if>

		                                            	</span>
		                                            </c:if>
		                                            <!-- points award -->
		                                            <span class="awardValue wideValue">
			                                             <c:if test="${promotion.awardType.code eq 'points'}">
															<c:out value="${level.awardLocaleBased}"/>&nbsp;<c:out value="${promotion.awardTypeNameFromCM}" />
														</c:if>
													</span>
		                                            <!-- selected award name -->
		                                            <span class="awardName wideValue">
		                                                <!-- dynamic JS -->
		                                            </span>
		                                            <!-- browse award -->
		                                            <span class="awardBrowse awardBtn">
		                                                <button class="btn btn-small"><cms:contentText key="BROWSE_AWARDS" code="promotion.goalquest.selection.wizard"/></button>
		                                            </span>
		                                            <!-- set award -->
		                                            <span class="awardSet awardBtn">
		                                                <button class="btn btn-small btn-primary"><cms:contentText key="SELECT_AWARD" code="promotion.goalquest.selection.wizard"/></button>
		                                            </span>
		                                            <!-- edit award -->
		                                            <span class="awardEdit awardBtn">
		                                                <button class="btn btn-small"><cms:contentText key="CHANGE_AWARD" code="promotion.goalquest.selection.wizard"/></button>
		                                            </span>
		                                        </div><!-- /.deetValues -->
		                                    </div><!-- /.levelDeets -->
		                                </label><!-- /.radio -->
								      </c:forEach>
                                  </c:otherwise>
                                </c:choose>

                            </fieldset>

                            <!-- this gets dumped into a qtip and shows currently selected plat. aw. -->
                            <div class="selectedPlateauAwardWrapper" style="display:none">
                                <div class="selectedPlateauAward">
                                    <div class="hasAwardContent">
                                        <div class="paImg">
                                            <!-- js fills this in -->
                                        </div>
                                        <h5 class="paTitle">
                                            <!-- js fills this in -->
                                        </h5>
                                    </div>
                                    <div class="noAwardContent">
                                        <i class="icon-picture"></i>
                                        <br><br>
                                        <cms:contentText key="NONE_SELECTED" code="promotion.goalquest.selection.wizard"/>
                                    </div>
                                </div>
                            </div>

                        </div><!-- /.levelsWrapper -->

                        <div class="stepContentControls form-actions pullBottomUp">
                            <button class="btn backBtn">
                                &laquo; <cms:contentText key="BACK" code="system.button"/>
                            </button>
                            <button class="btn btn-primary nextBtn">
                                <cms:contentText key="NEXT" code="system.button"/> &raquo;
                            </button>
                        </div><!-- /.stepContentControls -->

                    </div><!-- /.stepGoalContent -->



                    <!-- **************************************************************
                        CONNECT TO UA
                    ***************************************************************** -->

			<c:choose>
				<c:when test="${ goalQuestWizardForm.uaEnabled == true }">
					<c:choose>
						<c:when test="${ goalQuestWizardForm.uaConnected == true }">
							<div class="uaConnectContent stepContent" style="display: none">
                                <h4><cms:contentText key="EXCELLENT" code="promotion.goalquest.selection.wizard" /></h4>
                                <p><cms:contentText key="UA_CONNECTED_WITH_SITE" code="promotion.goalquest.selection.wizard" /></p>
                                <hr />

                                <h4><cms:contentText key="REMEMBER" code="promotion.goalquest.selection.wizard" /></h4>
                                <h5><cms:contentText key="LINK_FITNESS_TO_UA" code="promotion.goalquest.selection.wizard" /></h5>
                                <p><cms:contentText key="HERE_IS_SOME_UA_APP" code="promotion.goalquest.selection.wizard" /></p>
                                <p class="partner-apps">
                                    <img src="${pageContext.request.contextPath}/assets/img/underarmour/ua-app-record.png" alt="">
                                    <img src="${pageContext.request.contextPath}/assets/img/underarmour/ua-app-39.png" alt="">
                                    <%-- <img src="${pageContext.request.contextPath}/assets/img/underarmour/ua-app-endomondo.png" alt=""> --%>
                                    <img src="${pageContext.request.contextPath}/assets/img/underarmour/ua-app-mapmy.png" alt="">
                                    <img src="${pageContext.request.contextPath}/assets/img/underarmour/ua-app-myfitnesspal.png" alt="">
                                </p>
<%--                            <p>
                                    <cms:contentText key="YOU_ARE_CONNECTED_WITH_UA" code="promotion.goalquest.selection.wizard" />
                                </p>
--%>
								<div class="stepContentControls form-actions pullBottomUp">
									<button class="btn backBtn">
										&laquo;
										<cms:contentText key="BACK" code="system.button" />
									</button>
									<button class="btn btn-primary nextBtn">
										<cms:contentText key="NEXT" code="system.button" />
										&raquo;
									</button>
								</div>
								<!-- /.stepContentControls -->
							</div>
						</c:when>
						<c:otherwise>
							<div class="uaConnectContent stepContent" style="display: none">
<%--
								<div class="ua-connect-status-edit">
									<div class="ua-widget">
										<i class="icon icon-link-3-broken"></i>
										<cms:contentText key="STATUS"
											code="promotion.goalquest.selection.wizard" />
										<strong><cms:contentText key="NOT_CONNECTED"
												code="promotion.goalquest.selection.wizard" /></strong>
									</div>
								</div>
--%>
								<h4>
									<cms:contentText key="CONNECT_YOUR_ACCOUNT_TO_UA"
										code="promotion.goalquest.selection.wizard" />
								</h4>
								<p>
									<cms:contentText key="CONNECT_TO_UA_MSG"
										code="promotion.goalquest.selection.wizard" />
								</p>
								<p class="ua-connect-button">
									<a href='<c:out value="${ goalQuestWizardForm.uaOAuthUrl}" />'
										class="btn btn-primary btn-uaconnect"><cms:contentText
											key="LOG_IN_WITH" code="promotion.goalquest.selection.wizard" />
										<strong><cms:contentText key="UNDER_ARMOUR"
												code="promotion.goalquest.selection.wizard" /></strong>
									</a>
								</p>

							    <div class="stepContentControls form-actions pullBottomUp">
								<button class="btn backBtn">
									&laquo;
									<cms:contentText key="BACK" code="system.button" />
								</button>
								<div name="uaLogout" id="uaLogout" top="0" left="0" height="0" width="0" style="visibility: hidden; background-color: #fff; height: 0px;">
									<object type="text/html" id="logOut" data="${goalQuestWizardForm.uaLogOutUrl}" width="0px" height="0px" />
								</div>
							</div>
							<!-- /.stepContentControls -->
							
											
							
						
						</div>

						</c:otherwise>
					</c:choose>
				</c:when>
				<c:otherwise>
				</c:otherwise>
			</c:choose>

			<!-- **************************************************************
                        PARTNER SELECTION
                    ***************************************************************** -->
                    <div class="stepPartnerContent stepContent" style="display:none">

                        <label class="radio">
                            <html:radio styleClass="selectPartnerYesInput" property="isPartnersSelected" value="false" />
                            <cms:contentText key="NO_PARTNER" code="promotion.goalquest.selection.wizard"/>
                        </label>

                        <label class="radio">
                        	<html:radio styleClass="selectPartnerYesInput" property="isPartnersSelected" value="true" />
                            <cms:contentText key="ENABLE_PARTNER" code="promotion.goalquest.selection.wizard"/>
                        </label>

                        <div class="partnerSearchSlidingWrapper" style="display:none">
                            <h3><cms:contentText key="FIND_PARTNERS" code="promotion.goalquest.selection.wizard"/></h3>
                            <!-- pax search for Partners -->
                            <div class='paxSearchStartView'  data-search-url="${pageContext.request.contextPath}/goalQuestSearch/partners.action" ></div>

							<%@include file="/search/paxSearchStart.jsp" %>

                            <script type="text/template" id="participantSearchTableRowTpl">
								<%@include file="/profileutil/participantSearchTableRow.jsp" %>
							</script>

							<script type="text/template" id="participantRowItemTpl">
								<%@include file="/profileutil/participantRowItem.jsp" %>
							</script>

							<c:if test="${ goalQuestWizardForm.isPartnersSelected }">
                              <p class="preselectedPartnersMsg">
                                <cms:contentText key="PARTNER_MESSAGE" code="promotion.goalquest.selection.wizard"/>
                              </p>
                            </c:if>

                            <div class="container-splitter participantCollectionViewWrapper">

                                <!-- this name is set by the JS View, the name must be set after the struts value is parsed -->
                                <input type="hidden" name="xCount" value="0" class="participantCount" />

                                <h3><cms:contentText key="SELECTED_PARTNERS" code="promotion.goalquest.selection.wizard"/></h3>

                                <table class="table table-condensed table-striped">
                                    <thead>
                                        <tr>
                                            <th class="participant"><cms:contentText key="PARTNER" code="promotion.goalquest.selection.wizard"/></th>
                                            <th class="remove"><cms:contentText key="REMOVE" code="promotion.goalquest.selection.wizard"/></th>
                                        </tr>
                                    </thead>

                                    <tbody class="partnerView participantCollectionView"
                                        data-msg-empty="<cms:contentText key="NOT_ADDED" code="promotion.goalquest.selection.wizard"/>"
                                        data-hide-on-empty="false">
                                    </tbody>
                                </table>

                                <!-- client side template for each partner in the table -->
                                <script id="partnerRowTpl" type="text/x-handlebars-template">
                                    <tr class="participant-item"
                                            data-participant-cid="{{cid}}"
                                            data-participant-id="{{id}}">

                                        <td class="participant">
                                            <input type="hidden" name="{{partnerPrefix}}[{{autoIndex}}].id" value="{{id}}" />
                                            <input type="hidden" name="{{partnerPrefix}}[{{autoIndex}}].firstName" value="{{firstName}}" />
                                            <input type="hidden" name="{{partnerPrefix}}[{{autoIndex}}].lastName" value="{{lastName}}" />

                                            <a class="participant-popover" href="#" data-participant-ids="[{{id}}]">
                                                {{firstName}}
                                                {{lastName}}
                                                {{#if countryCode}}<img src="${pageContext.request.contextPath}/assets/img/flags/{{countryCode}}.png" alt="{{countryCode}}" class="countryFlag" title="{{countryName}}" />{{/if}}
                                            </a>
                                            <span class="org">{{orgName}} {{#if departmentName}}{{#if orgName}}&bull;{{/if}} {{departmentName}}{{/if}} {{#if jobName}}{{#if departmentName}}&bull;{{else}}{{#if orgName}}&bull;{{/if}}{{/if}} {{jobName}}{{/if}}</span>
                                        </td>
                                        <td class="remove">
                                            <a class="remParticipantControl" title="<cms:contentText key='REMOVE_PAX' code='participant.search'/>"><i class="icon-trash"></i></a>
                                        </td>
                                    </tr>
                                </script>

                            </div><!-- /.partnerCollectionViewWrapper -->

                        </div><!-- /.partnerSearchSlidingWrapper -->

                        <div class="stepContentControls form-actions pullBottomUp">
                            <button class="btn backBtn">
                                &laquo; <cms:contentText key="BACK" code="system.button"/>
                            </button>
                            <button class="btn btn-primary nextBtn">
                                <cms:contentText key="NEXT" code="system.button"/> &raquo;
                            </button>
                        </div><!-- /.stepContentControls -->

                    </div><!-- /.stepPartnerContent -->

                    <!-- **************************************************************
                        SUBMIT
                     ***************************************************************** -->
                    <div class="stepSubmitContent stepContent" style="display:none">

                        <div class="row-fluid">
                            <div class="span12">
                                <h4><cms:contentText key="SUMMARY" code="promotion.goalquest.selection.wizard"/></h4>
                                <dl class="dl-horizontal dl-h1">
                                    <dt><cms:contentText key="PROMOTION_OBJECTIVE" code="promotion.goalquest.selection.wizard"/></dt>
                                    <dd><c:out value="${ goalQuestWizardForm.objectiveFromCM }" escapeXml="false"/></dd>
                                    <dt><cms:contentText key="PROGRAM_PERIOD" code="promotion.goalquest.selection.wizard"/></dt>
                                    <dd><fmt:formatDate value="${ goalQuestWizardForm.promotionStartDate }" type="date" pattern='${JstlDatePattern}'/> <cms:contentText key="THROUGH" code="promotion.goalquest.selection.wizard"/> <fmt:formatDate value="${ goalQuestWizardForm.promotionEndDate }" type="date" pattern='${JstlDatePattern}'/></dd>
                                </dl>
                            </div>
                        </div>

                    <%-- <div class="row-fluid modal-header">
                    </div>
                    <div class="modal-body">
                        <c:if test="${ goalQuestWizardForm.promotionType == 'challengepoint' }" >
	                        <div class="row-fluid">
	                            <div class="span12">
	                                <dl class="dl-horizontal dl-h1">

										<c:if test="${level.promotion.challengePointAwardType.points }">
											<dd class="submitAwardValue dyn"><!-- dynamic - JS --></dd>
	                                   		<dd class="submitAwardName dyn"><!-- dynamic - JS --></dd>
										 </c:if>

										 <c:if test="${level.promotion.challengePointAwardType.merchTravel }">

										 <cms:contentTemplateText code="promotion.challengepoint.select.level"
											   key="RIGHT_TILE_MERCH_PAYOUT"
											   args="
											   ${selectYourChallengePointForm.selectedLevelName}"
											   delimiter=","/>

										 </c:if>
	                                </dl>
	                            </div>
	                        </div>
                        </c:if> --%>

						<div class="row-fluid submitBaseProgramDeets dyn dyn_showIfCont">
                            <!-- populated if assoc. level has .baseProgramDeets element -->
                        </div>

                        <div class="row-fluid">
                            <div class="span6">
                                <h4><cms:contentText key="YOUR_GOAL" code="promotion.goalquest.selection.wizard"/></h4>
                                <div class="submitLevelName dyn">
                                    <!-- dynamic - JS -->
                                </div>
                                <div class="submitLevelDesc dyn">
                                    <!-- dynamic - JS -->
                                </div>
                                <dl class="dl-horizontal dl-h1">
                                	<c:if test="${ baseAmount }">
                                    	<dt><cms:contentText key="BASELINE" code="promotion.goalquest.selection.wizard"/></dt>
                                    	<dd class="submitBaselineValue dyn"><!-- dynamic - JS --></dd>
                                    </c:if>
                                    <dt><cms:contentText key="YOUR_GOAL" code="promotion.goalquest.selection.wizard"/>:</dt>
                                    <dd class="submitGoalValue dyn"><!-- dynamic - JS --></dd>
                                    <dt><cms:contentText key="AWARD" code="promotion.goalquest.selection.wizard"/></dt>
                                    <dd class="submitAwardValue dyn"><!-- dynamic - JS --></dd>
                                    <dd class="submitAwardName dyn"><!-- dynamic - JS --></dd>
                                    <dt class="partnerOnly"><cms:contentText key="PARTNERS" code="promotion.goalquest.selection.wizard"/></dt>
                                    <dd class="submitPartners partnerOnly"><!-- dynamic - JS --></dd>
                                </dl>
                            </div>
                            <div class="span6">
                                <div class="submitPlateauAwardImageContent">
                                    <div class="paImg"><!-- dynamid - JS --></div>
                                    <div class="paTitle"><!-- dynamid - JS --></div>
                                </div>
                            </div>
                        </div>

                        <div class="stepContentControls form-actions pullBottomUp">
                            <button class="btn backBtn">
                                &laquo; <cms:contentText key="BACK" code="system.button"/>
                            </button>
                            <button class="btn btn-primary submitBtn">
                                <cms:contentText key="SUBMIT" code="system.button"/>
                            </button>
                        </div><!-- /.stepContentControls -->

                    </div><!-- /.stepSubmitContent -->

                </html:form> <!-- /.gqEditForm -->

                <!-- informational tooltip for validation -->
                <div class="nextBtnTipWrapper" style="display:none">
                    <div class="nextBtnTip">
                        <div class="nextBtnMsg msgNoPlatAw">
                            <i class="icon-exclamation-sign"></i>
                            <cms:contentText key="SELECT_PLATEAU" code="promotion.goalquest.selection.wizard"/>
                        </div>
                        <div class="nextBtnMsg msgNoLevel">
                            <i class="icon-exclamation-sign"></i>
                            <cms:contentText key="SELECT_LEVEL" code="promotion.goalquest.selection.wizard"/>
                        </div>
                        <div class="nextBtnMsg msgNoPartnerYesPref">
                            <i class="icon-exclamation-sign"></i>
                            <cms:contentText key="SELECT_OPTION" code="promotion.goalquest.selection.wizard"/>
                        </div>
                        <div class="nextBtnMsg msgNoPartners">
                            <i class="icon-exclamation-sign"></i>
                            <cms:contentText key="ADD_PARTNER" code="promotion.goalquest.selection.wizard"/>
                        </div>
                        <div class="nextBtnMsg msgMaxPartners">
                            <i class="icon-exclamation-sign"></i>
                            <cms:contentText key="PARTNERS_EXCEEDED" code="promotion.goalquest.selection.wizard"/>
                        </div>
                    </div>
                </div>

            </div><!-- /.wizardTabsContent -->

        </div><!-- /.span12 -->
    </div><!-- /.row-fluid -->

    <div id="levelMerchModal" class="modal modal-stack hide fade" data-y-offset="adjust">
        <div class="modal-header">
            <button data-dismiss="modal" class="close" type="button"><i class="icon-close"></i></button>
            <h3><cms:contentText key="SELECT_AWARD" code="promotion.goalquest.selection.wizard"/></h3>
        </div>
        <div class="modal-body">
            <!-- dynamic -->
        </div>
    </div><!-- /.levelMerchModal -->

</div><!-- /#goalquestPageRulesView -->

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
    $(document).ready(function(){
    	G5.props.URL_JSON_PLATEAU_AWARDS = "${pageContext.request.contextPath}/goalquest/selectGoal.do?method=getPromotionAwards" ;
    	G5.props.URL_PAGE_PLATEAU_AWARDS = '${pageContext.request.contextPath}/goalquest/awardsPage.do';
    	G5.props.URL_JSON_PARTICIPANT_INFO = "${pageContext.request.contextPath}/participantPublicProfile.do?method=populatePax";
    	// Recognition wizard info
        G5.props.URL_JSON_EZ_RECOGNITION_MEMBER_INFO = G5.props.URL_ROOT+'/recognitionWizard/memberInfo.do';
        //Mini Profile PopUp Follow Unfollow Pax JSON
        G5.props.URL_JSON_PARTICIPANT_FOLLOW = G5.props.URL_ROOT+'publicRecognitionFollowUnFollowPax.do?method=followUnFollowPax';

        // JAVA NOTE: you should only have to i18n the "wtName" field in
        //            this set, no need to create a class for this or anything
        // json for WizardTabs -- easier to read/maintain than HTML setup
        // TODO: logic for when to insert UA Connect tab (see HTML file for JSON order/copy)
        // TODO: java add logic for UA connect tab.  If not connected, the state should be incomplete
        var tabsJson = [
            {
                "id" : 1,
                "name" : "stepOverview",
                "isActive" : false,
                "state" : "unlocked",
                "contentSel" : ".wizardTabsContent .stepOverviewContent",
                "wtNumber" : "",
                "wtName" : "<cms:contentText key="OVERVIEW" code="promotion.goalquest.selection.wizard"/>",
                "hideDeedle" : false
            },
            {
                "id" : 2,
                "name" : "stepRules",
                "isActive" : false,
                "state" : "locked",
                "contentSel" : ".wizardTabsContent .stepRulesContent",
                "wtNumber" : "",
                "wtName" : "<cms:contentText key="READ_RULES" code="promotion.goalquest.selection.wizard"/>",
                "hideDeedle" : false
            },
            {
                "id" : 3,
                "name" : "stepUAConnect",
                "isActive" : false,
                "state" : "locked",
                "contentSel" : ".wizardTabsContent .uaConnectContent",
                "wtNumber" : "",
                "wtName" : "<cms:contentText key="CONNECT_TO_UA" code="promotion.goalquest.selection.wizard"/>",
                "hideDeedle" : false
            },
            {
                "id" : 4,
                "name" : "stepGoal",
                "isActive" : false,
                "state" : "locked",
                "contentSel" : ".wizardTabsContent .stepGoalContent",
                "wtNumber" : "",
                "wtName" : "<cms:contentText key="GOAL_SELECTION" code="promotion.goalquest.selection.wizard"/>",
                "hideDeedle" : false
            },
             {
                 "id" : 5,
                 "name" : "stepPartner",
                 "isActive" : false,
                 "state" : "locked",
                 "contentSel" : ".wizardTabsContent .stepPartnerContent",
                 "wtNumber" : "",
                 "wtName" : "Partner Selection",
                 "hideDeedle" : false
             },
            {
                "id" : 6,
                "name" : "stepSubmit",
                "isActive" : false,
                "state" : "locked",
                "contentSel" : ".wizardTabsContent .stepSubmitContent",
                "wtNumber" : "",
                "wtName" : "<cms:contentText key="SUBMIT_GOAL" code="promotion.goalquest.selection.wizard"/>",
                "hideDeedle" : false
            }
        ];

        //attach the view to an existing DOM element
        window.pageView = new GoalquestPageEditView({
            el:$('#goalquestPageEditView'),
            pageNav : {
                back : {
                    text : '<cms:contentText key="BACK" code="system.button"/>',
                    url : 'javascript:history.go(-1);'
                },
                home : {
                    text : '<cms:contentText key="HOME" code="system.button"/>',
                    url : '<%= RequestUtils.getBaseURI(request)%>/homePage.do'
                }
            },
            tabsJson: tabsJson,
            <c:choose>
            	<c:when test="${goalQuestWizardForm.promotionType == 'challengepoint'}">
	            	pageTitle : '<cms:contentText key="TITLE_CHALLENGEPOINT" code="promotion.goalquest.selection.wizard"/>'
	           	</c:when>
            	<c:otherwise>
       	        	pageTitle : '<cms:contentText key="TITLE" code="promotion.goalquest.selection.wizard"/>'
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

<%@include file="/submitrecognition/easy/flipSide.jsp"%>

<!-- wizardTab template -->
<script type="text/template" id="wizardTabTpl">
    <%@include file="/include/wizardTab.jsp" %>
</script>
