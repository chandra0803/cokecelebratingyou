<%@ include file="/include/taglib.jspf"%>

<!-- ======== APPROVALS: APPROVALS RECOGNITION DETAIL ======== -->

<div id="approvalsNominationDetailView" class="approvalSearchWrapper page-content">
    <div class="row-fluid">
        <div class="span12">
            <div id="approvalErrorBlock" class="alert alert-block alert-error" style="display:none;">
                <h4><cms:contentText key="FOLLOWING_ERRORS" code="system.generalerror" /></h4>
                <ul>
                <html:messages id="actionMessage" >
                    <c:set var="serverReturnedError" value="true"/>
                    <li>${actionMessage}</li>
                </html:messages>
                </ul>
            </div>
        </div>
    </div>

    <div id="approvalsNominationDetailWrapper">
		<div class="row-fluid">
			<div class="span6">
            	<h3><c:out value="${claimDetails.promotion.name}"/></h3>
        	</div>
			<div class="span6">
	            <ul class="export-tools fr">
	            	<c:if test="${claimDetails.promotion.calculator != null}">
		            	<li>
							<a id="recogCalcPayoutTableLink" href="#" style=""><cms:contentText code="recognition.approval.list" key="VIEW_PAYOUT_GRID"/></a>
						</li>
					</c:if>
	                <li><a href="#" class="pageView_print btn btn-small"><cms:contentText code="system.button" key="PRINT" /> <i class="icon-printer"></i></a></li>
	            </ul>
	        </div>
	    </div>

		<!--  Should be only 1 recipient -->
		<c:forEach items="${claimDetails.claimRecipients}" var="claimRecipient">
			<c:set var="approvalRecipient" value="${claimRecipient}"/>
		</c:forEach>

		<html:form action="approvalsRecognitionDetailsUpdate.do?method=saveApprovals" styleClass="form-horizontal">
	    	<div class="row-fluid">

			    	<c:if test="${ ecardForApproval ne null && ecardForApproval != '' }">
				    	<div id="ecardImage" class="ecard-span span4" >
				    		<img src="<c:out value="${ecardForApproval}"/>" alt="image" border="0" style="margin: 0 0 10px 0;" />
				    	</div>
				    </c:if>

				    <div id="nominationDetails" class="recognitionDetails span8">

			    		<!-- Recipient row-fluid Starts-->
			    		<div class="row-fluid">
			    			<div class="span3">
			    				<p><strong>
			    					<cms:contentText code="recognition.approval.details" key="RECIPIENT" />
			    				</strong></p>
			    			</div>
			    			<div class="span9">
					    		<p>
					    			<c:out value="${approvalRecipient.recipient.lastName}"/>,
									<c:out value="${approvalRecipient.recipient.firstName}"/>
									<img src="${pageContext.request.contextPath}/assets/img/flags/${approvalRecipient.recipient.primaryCountryCode}.png" />,
									<c:out value="${approvalRecipient.recipient.paxOrgName}" />,
									<c:out value="${approvalRecipient.recipient.paxDeptName}" />,
									<c:out value="${approvalRecipient.recipient.paxJobName}" />
					    		</p>
			    			</div>
			    		</div>
						<!-- Recipient row-fluid Ends-->


						<!-- Award row-fluid Starts-->
						<c:if test="${claimDetails.promotion.awardActive}">
						<c:choose>
								<c:when test="${claimDetails.promotion.adihCashOption}">
									<div class="row">
										<div class="span3">
											<p><strong><cms:contentText code="coke.cash.recognition" key="RECIPIENT_AWARD_VAL" /></strong></p>
										</div>
										<div class="span5">
											<p>${approvalRecipient.awardQuantity}&nbsp;${approvalRecipient.cashCurrencyCode}</p>
										</div>
									</div>
									<div class="row">
										<div class="span3">
											<p><strong><cms:contentText code="coke.cash.recognition" key="USD_AWARD_VAL" /></strong></p>
										</div>
										<div class="span5">
											<p>${approvalRecipient.displayUSDAwardQuantity}</p>
										</div>
									</div>
								</c:when>
								<c:otherwise>
				    		<div class="row-fluid">
				    			<div class="span3">
				    				<p><strong>
						    			<c:choose>
						    				<c:when test="${claimDetails.promotion.calculator == null && not claimDetails.promotion.awardAmountTypeFixed
						    									&& not claimDetails.promotion.awardType.merchandiseAwardType}">
												<cms:contentTemplateText code="recognition.approval.details" key="AWARD_RANGE"
													args="${claimDetails.promotion.awardAmountMin},${claimDetails.promotion.awardAmountMax}" delimiter=","/>:
						    				</c:when>
						    				<c:otherwise>
						    					<cms:contentText code="recognition.approval.details" key="AWARD" />
						    				</c:otherwise>
						    			</c:choose>
					    			</strong></p>
				    			</div>
				    			<div class="span9">
				    				<p>
				    					<c:choose>
				    						<c:when test="${approvalsRecognitionDetailsForm.scoredByApprover && not claimDetails.promotion.awardType.merchandiseAwardType}">
				    							<a class="calcLink">
													<c:out value="${approvalsRecognitionDetailsForm.approvalsRecognitionForm.awardAmount}"/>
													<c:out value="${claimDetails.promotion.awardType.name}" />
												</a>
												<html:hidden styleId="awardPointsInput" property="approvalsRecognitionForm.awardAmount" value="${approvalsRecognitionDetailsForm.approvalsRecognitionForm.awardAmount}" />
				    							<c:forEach var="calculatorResponse" items="${claimDetails.calculatorResponses}" varStatus="responseStatus">
				    								<input type="hidden" name="claimRecipientFormBeans[0].calculatorResultBeans[${responseStatus.index}].criteriaId" value="${calculatorResponse.criterion.id}">
				    								<input type="hidden" name="claimRecipientFormBeans[0].calculatorResultBeans[${responseStatus.index}].ratingId" value="${calculatorResponse.selectedRating.id}">
						    						<input type="hidden" name="claimRecipientFormBeans[0].calculatorResultBeans[${responseStatus.index}].criteriaRating" value="${calculatorResponse.selectedRating.ratingValue}">
				    							</c:forEach>
				    						</c:when>
				    						<c:when test="${approvalsRecognitionDetailsForm.scoredByApprover && claimDetails.promotion.awardType.merchandiseAwardType}">
				    							<a class="calcLink">
													<c:out value="${approvalRecipient.promoMerchProgramLevel.displayLevelName} (${approvalRecipient.promoMerchProgramLevel.maxValue})"/>
												</a>
												<html:hidden styleId="awardPointsInput" property="approvalsRecognitionForm.awardLevelId" value="${approvalsRecognitionDetailsForm.approvalsRecognitionForm.programLevel.id}" />
				    							<c:forEach var="calculatorResponse" items="${claimDetails.calculatorResponses}">
				    								<input type="hidden" name="claimRecipientFormBeans[0].calculatorResultBeans[${responseStatus.index}].criteriaId" value="${calculatorResponse.criterion.id}">
				    								<input type="hidden" name="claimRecipientFormBeans[0].calculatorResultBeans[${responseStatus.index}].ratingId" value="${calculatorResponse.selectedRating.id}">
						    						<input type="hidden" name="claimRecipientFormBeans[0].calculatorResultBeans[${responseStatus.index}].criteriaRating" value="${calculatorResponse.selectedRating.ratingValue}">
				    							</c:forEach>
				    						</c:when>
				    						<c:when test="${claimDetails.promotion.calculator == null && not claimDetails.promotion.awardAmountTypeFixed && not claimDetails.promotion.awardType.merchandiseAwardType}">
				    							<input id="awardPointsInput" name="approvalsRecognitionForm.awardAmount"  class="awardPointsInput"
				    								type="text" value="${approvalsRecognitionDetailsForm.approvalsRecognitionForm.awardAmount}"
													data-range-min="${claimDetails.promotion.awardAmountMin}" data-range-max="${claimDetails.promotion.awardAmountMax}" />
				    						</c:when>
				    						<c:when test="${claimDetails.promotion.awardType.merchandiseAwardType}">
				    							<c:out value="${approvalRecipient.promoMerchProgramLevel.displayLevelName}" />
				    							<input type="hidden" id="awardPointsInput" value="${approvalsRecognitionDetailsForm.approvalsRecognitionForm.programLevel.maxValue}"/>
				    						</c:when>
				    						<c:otherwise>
				    							<c:if test="${approvalRecipient.awardQuantity != null }">
													<fmt:formatNumber value="${approvalRecipient.awardQuantity}" />
													<c:out value="${claimDetails.promotion.awardType.name}" />
												</c:if>
												<html:hidden styleId="awardPointsInput" property="approvalsRecognitionForm.awardAmount" value="${approvalsRecognitionDetailsForm.approvalsRecognitionForm.awardAmount}" />
				    						</c:otherwise>
				    					</c:choose>
				    					<input type="hidden" value="${conversionRatio}" id="conversionRate">
				    				</p>
				    			</div>
				    		</div>
				    		</c:otherwise>
							</c:choose>
				    	</c:if>
				    	
				    	<!-- Award row-fluid Ends-->

				    	<!-- Score row-fluid Element Starts -->
				    	<c:if test="${claimDetails.promotion.calculator.displayScores}">
							<c:if test="${claimDetails.promotion.calculator != null }" >
							<div class="row-fluid">
								<div class="span3">
									<p><strong>
										<c:out value="${claimDetails.promotion.calculator.scoreLabel}"/>
									</strong></p>
								</div>
								<div class="span9">
									<p id="awardElm">
										<c:choose>
											<c:when test="${approvalRecipient.calculatorScore == null}"><cms:contentText code="recognition.approval.list" key="NOT_SELECTED" /></c:when>
											<c:otherwise><c:out value="${approvalRecipient.calculatorScore}" /></c:otherwise>
										</c:choose>
									</p>
									<html:hidden styleId="awardFormElm" property="calculatorScore" value="${approvalRecipient.calculatorScore}"/>
								</div>
							</div>
							</c:if>
						</c:if>
			    		<!-- Score row-fluid Element Starts -->

						<!-- Giver row-fluid Element Starts -->
						<div class="row-fluid">
							<div class="span3">
								<p><strong>
			    					<cms:contentText code="recognition.approval.details" key="GIVER" />
			    				</strong></p>
			    			</div>
			    			<div class="span9">
					    		<p>
					    			<c:out value="${claimDetails.submitter.lastName}"/>,
									<c:out value="${claimDetails.submitter.firstName}"/>
									<img src="${pageContext.request.contextPath}/assets/img/flags/${claimDetails.submitter.primaryCountryCode}.png" />,
									<c:out value="${claimDetails.submitter.paxOrgName}" />,
									<c:out value="${claimDetails.submitter.paxDeptName}" />,
									<c:out value="${claimDetails.submitter.paxJobName}" />
					    		</p>
					    	</div>
					    </div>
			    		<!-- Giver row-fluid Element Ends -->

			    		<!-- Comments row-fluid Element Starts -->
			    		<div class="row-fluid">
			    			<div class="span3">
			    				<p><strong>
			    					<cms:contentText code="recognition.approval.details" key="COMMENTS" />
			    				</strong></p>
			    			</div>
			    			<div class="span9 comment-wrapper">
			    				<div class="comment-text"><c:out value="${claimDetails.submitterComments }" escapeXml="false" /></div>
                  <c:if test="${allowTranslate}"><a href="${translateClientState}" class="translateTextLink"><cms:contentText code="recognition.approval.details" key="TRANSLATE" /></a></c:if>
			    			</div>
			    		</div>
			    		<!-- Comments row-fluid Element Ends -->

			    		<!-- Behavior row-fluid Element Starts -->
						<c:if test="${claimDetails.behavior.active}">
							<div class="row-fluid">
								<div class="span3">
									<p><strong>
										<cms:contentText code="recognition.approval.details" key="BEHAVIOR" />
									</strong></p>
								</div>
								<div class="span9">
									<p><c:out value="${claimDetails.behavior.name }" /></p>
								</div>
							</div>
						</c:if>
						<!-- Behavior row-fluid Element Ends -->

			    		<!-- Claim Elements Starts here -->
						<c:forEach items="${claimDetails.claimElements}" var="claimElement" varStatus="status">
							<c:if test="${!claimElement.claimFormStepElement.claimFormElementType.addressBookSelect}">
								<div class="row-fluid">
									<!-- labels -->
									<div class="span3">
										<p><strong>
											<c:choose>
												<c:when test="${!claimDetails.managerAward }">
													<dt><cms:contentText code="${claimDetails.promotion.claimForm.cmAssetCode}" key="${claimElement.claimFormStepElement.cmKeyForElementLabel}" /></dt>
												</c:when>
												<c:otherwise>
													<dt><cms:contentText code="${claimDetails.parentManagerPromotion.claimForm.cmAssetCode}" key="${claimElement.claimFormStepElement.cmKeyForElementLabel}" /></dt>
												</c:otherwise>
											</c:choose>
										</strong></p>
									</div>

									<!-- values -->
									<div class="span9">
										<c:choose>
											<c:when test="${claimElement.claimFormStepElement.claimFormElementType.textBoxField || claimElement.claimFormStepElement.claimFormElementType.textField || claimElement.claimFormStepElement.claimFormElementType.numberField}">
												<c:if test="${claimElement.claimFormStepElement.maskedOnEntry}">
													<p>*************</p>
												</c:if>
												<c:if test="${!claimElement.claimFormStepElement.maskedOnEntry}">
													<p><c:out value="${claimElement.value}" /></p>
												</c:if>
											</c:when>

											<c:when test="${claimElement.claimFormStepElement.claimFormElementType.addressBlock}">
												<p><beacon:addressDisplay address="${claimElement.value}" /></p>
											</c:when>


											<c:when test="${claimElement.claimFormStepElement.claimFormElementType.dateField}">
												<p><c:out value="${claimElement.value}" /></p>
											</c:when>

											<c:when test="${claimElement.claimFormStepElement.claimFormElementType.booleanField}">
												<c:if test="${claimElement.value == 'true'}">
													<c:if test="${!claimDetails.managerAward }">
														<p><cms:contentText code="${claimDetails.promotion.claimForm.cmAssetCode}" key="${claimElement.claimFormStepElement.cmKeyForLabelTrue}" /></p>
													</c:if>
													<c:if test="${claimDetails.managerAward }">
														<p><cms:contentText code="${claimDetails.parentManagerPromotion.claimForm.cmAssetCode}" key="${claimElement.claimFormStepElement.cmKeyForLabelTrue}" /></p>
													</c:if>
												</c:if>
												<c:if test="${claimElement.value == 'false'}">
													<c:if test="${!claimDetails.managerAward }">
														<p><cms:contentText code="${claimDetails.promotion.claimForm.cmAssetCode}" key="${claimElement.claimFormStepElement.cmKeyForLabelFalse}" /></p>
													</c:if>
													<c:if test="${claimDetails.managerAward }">
														<p><cms:contentText code="${claimDetails.parentManagerPromotion.claimForm.cmAssetCode}" key="${claimElement.claimFormStepElement.cmKeyForLabelFalse}" /></p>
													</c:if>
												</c:if>
											</c:when>

											<c:when test="${claimElement.claimFormStepElement.claimFormElementType.selectField}">
												<p><c:out value="${claimElement.pickListItems[0].name}" /></p>
											</c:when>

											<c:when test="${claimElement.claimFormStepElement.claimFormElementType.multiSelectField}">
												<c:forEach items="${claimElement.pickListItems}" var="item" varStatus="status">
													<p><c:out value="${item.name}" /><br></p>
												</c:forEach>
											</c:when>

											<c:when test="${claimElement.claimFormStepElement.claimFormElementType.button}"></c:when>
											<c:when test="${claimElement.claimFormStepElement.claimFormElementType.sectionHeading}"></c:when>
										</c:choose>
									</div><!-- /.span9 -->
								</div><!-- /.row-fluid -->
							</c:if>
						</c:forEach>
						<!-- Claim Elements Ends here -->

			    		<!-- Giver's Manager Starts here -->
			    		<div class="row-fluid">
							<div class="span3">
								<p><strong>
									<cms:contentText code="recognition.approval.details" key="GIVER_MANAGER" />
								</strong></p>
							</div>
			    			<div class="span5">
				    			<c:forEach items="${submitterNodeOwners}" var="owner">
					    			<p>
					    				<c:out value="${owner.lastName}"/>,
										<c:out value="${owner.firstName}"/>
										<img src="${pageContext.request.contextPath}/assets/img/flags/${owner.primaryCountryCode}.png" />,
										<c:out value="${owner.paxOrgName}" />,
										<c:out value="${owner.paxDeptName}" />,
										<c:out value="${owner.paxJobName}" />
									</p>
								</c:forEach>
							</div>
						</div>
			    		<!-- Giver's Manager Ends here -->

			    		<!-- Date Submitted Starts here -->
						<div class="row-fluid">
							<div class="span3">
								<p><strong>
			    					<cms:contentText code="recognition.approval.details" key="DATE_SUBMITTED" />
			    				</strong></p>
			    			</div>
			    			<div class="span5">
			    				<p><fmt:formatDate pattern="${JstlDatePattern}" value="${claimDetails.submissionDate}" /></p>
			    			</div>
			    		</div>
			    		<!-- Date Submitted Ends here -->

				    </div><!-- /.span8 -->

					<c:if test="${claimDetails.promotion.budgetUsed}">
					    <div class="row-fluid">
					    	<div class="span8">
								<table id="budgetTable" class="table table-bordered table-striped approvalBudgetTable approvalsNoBudgetTable">
									<tbody>
										<tr>
					                        <td><cms:contentText code="recognition.approval.details" key="AVAILABLE_BUDGET"/></td>
					                        <td id="availableBudget"><c:out value="${availableBudget}"/></td>
					                    </tr>

					                    <tr>
					                        <td><cms:contentText code="recognition.approval.details" key="CALCULATED_BUDGET"/></td>
					                        <td id="budgetDeductionElm"></td>
					                    </tr>

					                    <tr>
					                        <td><cms:contentText code="recognition.approval.details" key="REMAINING_BUDGET"/></td>
					                        <td id="remainingBudgetElm"></td>
					                    </tr>
									</tbody>
								</table>
							</div>
						</div>
					</c:if>

			</div><!-- /.row-fluid -->
			<div class="row-fluid">
				<div class="span12">
					<div id="approverActions" class="approverActions">
						<legend><cms:contentText code="recognition.approval.details" key="APPROVER_ACTIONS" /></legend>

			    		<div class="control-group recogDetail-Group">
							<div class="controls">
								<label><cms:contentText code="recognition.approval.list" key="STATUS"/></label>
									<html:select styleClass="approvalNominationStatus" property="approvalsRecognitionForm.approvalStatusType">
										<c:set var="approvalOptionTypes" value="${claimDetails.promotion.approvalOptionTypes}" />
										<html:options collection="approvalOptionTypes" property="code" labelProperty="name" />
									</html:select>

			    			</div>
			    		</div>

			    		<div class="control-group recogDetail-Group validateme" data-validate-flags="nonempty" data-validate-fail-msgs="{&quot;nonempty&quot; : &quot;You must select reason to deny recognition&quot;}">
							<div class="controls">
			    				<c:set var="deniedReasonTypes" value="${claimDetails.promotion.deniedReasonCodeTypes}" />
								  <html:select styleClass="claimReasonNomDetails" disabled="true" styleId="Denied_Reason" property="approvalsRecognitionForm.denyPromotionApprovalOptionReasonType">
									<html:option value=""><cms:contentText code="claims.product.approval" key="SELECT_RC" /></html:option>
									<html:options collection="deniedReasonTypes" property="code" labelProperty="name" />
								</html:select>
					    	</div>
						</div>

						<div class="control-group recogDetail-Group validateme" data-validate-flags="nonempty" data-validate-fail-msgs="{&quot;nonempty&quot; : &quot;You must select reason to hold recognition&quot;}" style="display: none">
							<div class="controls">
			    				<c:set var="holdReasonTypes" value="${claimDetails.promotion.heldReasonCodeTypes}" />
								  <html:select styleClass="holdReasonNomDetails" disabled="true" styleId="Hold_Reason" property="approvalsRecognitionForm.holdPromotionApprovalOptionReasonType">
									<html:option value=""><cms:contentText code="claims.product.approval" key="SELECT_RC" /></html:option>
									<html:options collection="holdReasonTypes" property="code" labelProperty="name" />
								</html:select>
					    	</div>
						</div>

                                                <div class="form-actions pullBottomUp">
                                                  <beacon:authorize ifNotGranted="LOGIN_AS">
                                                    <html:submit styleId="submitButton" styleClass="btn btn-primary btn-fullmobile" disabled="true"><cms:contentText code="system.button" key="SUBMIT"/></html:submit>
                                                  </beacon:authorize>
                                                  <a class="btn cancelBtn btn-fullmobile" href="approvalsRecognitionList.do"><cms:contentText code="system.button" key="CANCEL"/></a>
                                                </div>
					</div>
					<input type="hidden" id="serverReturnedErrored" value="${serverReturnedError}">
					<input type="hidden" id="hasAward" value="false">
					<html:hidden styleId="promotionId" property="promoId" value="${claimDetails.promotion.id}" />
					<html:hidden property="approvalsRecognitionForm.promotionId" />
					<html:hidden styleId="participantId" property="paxId" value="${approvalRecipient.recipient.id}" />
					<html:hidden styleId="scoredByApprover" property="scoredByApprover" value="${approvalsRecognitionDetailsForm.scoredByApprover}"/>
					<beacon:client-state>
						<beacon:client-state-entry name="claimId" value="${approvalsRecognitionDetailsForm.claimId}" />
					</beacon:client-state>
				</div><!-- /.span5 -->
			</div><!-- /.row-fluid -->
		</html:form>
	</div>
</div>


<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
	$(document).ready(function() {
		G5.props.URL_JSON_SEND_RECOGNITION_CALCULATOR_INFO = G5.props.URL_ROOT+'recognitionWizard/sendRecognitionCalculatorInfo.do';
    G5.props.URL_JSON_APPROVALS_TRANSLATE_COMMENT = G5.props.URL_ROOT + "claim/approvalTranslate.do";

		var thePreLoadedCalculator;

	  	thePreLoadedCalculator = {
			"attributes":
				{
					<c:if test="${bean.recognitionCalculator != null}">
						"hasWeight": <c:out value="${bean.recognitionCalculator.attributes.hasWeight}"/>,
						"hasScore": <c:out value="${bean.recognitionCalculator.attributes.hasScore}"/>,
						"showPayTable": <c:out value="${bean.recognitionCalculator.attributes.showPayTable}"/>,
						"weightLabel": "<c:out value="${bean.recognitionCalculator.attributes.weightLabel}"/>",
						"scoreLabel": "<c:out value="${bean.recognitionCalculator.attributes.scoreLabel}"/>",
						"awardType": "<c:out value="${bean.recognitionCalculator.attributes.awardType}"/>"
					</c:if>
				},
			<%--"awardLevel":
				{
			        "id": 252,
			        "name": "Level 3",
			        "value": 39900
		    	},--%>
		    "criteria":[
		    	<c:forEach var="crit" items="${bean.recognitionCalculator.criteria}" varStatus="r9">
					{
						"id":"<c:out value="${crit.id}"/>",
						"label":"<c:out value="${crit.label}"/>",
						"ratings":[
							<c:forEach var="rating" items="${crit.ratings}" varStatus="rate">
								{
									"value": "<c:out value="${rating.value}"/>",
									"label": "<c:out value="${rating.label}"/>",
									"id": <c:out value="${rating.id}"/>
								}
								<c:if test="${(rate.index+1) < fn:length(crit.ratings)}">, </c:if>
							</c:forEach>
						]
					}
					<c:if test="${(r9.index+1) < fn:length(bean.recognitionCalculator.criteria)}">, </c:if>
				</c:forEach>
			],
			"payTable":
				{
					"rows":[
						<c:forEach var="row" items="${bean.recognitionCalculator.payTable.rows}" varStatus="rowStatus">
							{
								"score": "<c:out value="${row.score}"/>",
								"payout":"<c:out value="${row.payout}"/>"
							}
							<c:if test="${(rowStatus.index+1) < fn:length(bean.recognitionCalculator.payTable.rows)}">, </c:if>
						</c:forEach>
					]
	    		}
		}


	    //attach the view to an existing DOM element
	    var aimlp = new ApprovalsNominationDetailModelView({
	    	el:$('#approvalsNominationDetailView'),
	    	preLoadedCalculator: thePreLoadedCalculator,
	    	pageNav : {
	            back : {
	                text : '<cms:contentText key="BACK" code="system.button" />',
	                url : 'approvalsRecognitionList.do'
	            },
	            home : {
	                text : '<cms:contentText key="HOME" code="system.general" />',
	                url : '${pageContext.request.contextPath}/homePage.do'
	            }
	        },
	        pageTitle : '<cms:contentText key="TITLE" code="recognition.approval.details" />'
	    });
	});
</script>

<script id="table-template" type="text/x-handlebars-template">
	<tbody>
		<tr>
			<td><cms:contentText code="recognition.approval.details" key="AVAILABLE_BUDGET"/></td>
			<td>{{availableBudget}}</td>
		</tr>

		<tr>
			<td><cms:contentText code="recognition.approval.details" key="CALCULATED_BUDGET"/></td>
			<td>{{calculatedBudgetDeduction}}</td>
		</tr>

		<tr>
			<td><cms:contentText code="recognition.approval.details" key="REMAINING_BUDGET"/></td>
			<td>{{remainingBudget}}</td>
		</tr>
	</tbody>
</script>

<script type="text/template" id="recognitionCalculatorPayoutGridTemplateTpl">
	<div id="recogCalcInnerWrapper" class="recogCalcInnerWrapper">
		<h1><cms:contentText code="recognition.approval.list" key="PAYOUT_GRID" /></h1>
		<span id="recogCalcPayoutCloseBtn" class="recogCalcPayoutCloseBtn"><i class="icon-close"></i></span>
		<table>
			<tbody>
				<tr>
					<th><cms:contentText code="recognition.approval.list" key="SCORE" /></th>
					<th><cms:contentText code="recognition.approval.list" key="PAYOUT" /></th>
				</tr>
			</tbody>
		</table>
	</div>
</script>

<script type="text/template" id="calculatorTemplateTpl">
	<div id="recogCalcWrapper" class="recogCalcWrapper">
        <div class="msgSelectRatingInstruction" style="display:none"><cms:contentText key="SELECT_RATING" code="recognitionSubmit.delivery.purl"/></div>
		<h1><cms:contentText key="AWARD_TITLE" code="calculator.response"/></h1>
		<span id="recogCalcCloseBtn" class="recogCalcCloseBtn"><i class="icon-close"></i></span>
		<ul>
			<li class="recogCalcRatingLabel"><cms:contentText key="RATING" code="recognitionSubmit.delivery.purl"/></li>
			{{#if hasScore}}
				<li class="recogCalcScoreLabel">{{scoreLabel}}</li>
			{{/if}}
			{{#if hasWeight}}
				<li class="recogCalcWeightLabel">{{weightLabel}}</li>
			{{/if}}
		</ul>
		<div id="recogCalcInnerWrapper" class="recogCalcInnerWrapper">
			{{{criteriaDiv}}}
		</div>
	</div>
</script>

<script type="text/template" id="recognitionCalculatorScoreWrapperTemplateTpl" >
	<div id="recogCalcScoreWrapper" class="recogCalcScoreWrapper">
	<div id="recogCalcTotalWrapper" class="recogCalcTotalWrapper">
	{{#if isDisplayScore}}
		<span id="recogCalcTotalLabel" class="recogCalcTotalLabel"><cms:contentText key="TOTAL_SCORE" code="recognitionSubmit.delivery.purl"/>: </span>
		<span id="recogCalcTotal" class="recogCalcTotal">{{totalScore}}</span>
	{{/if}}
	</div>
	{{#if isRange}}
		<p><cms:contentText key="SELECT_AMOUNT" code="recognitionSubmit.delivery.purl"/> {{{awardRange}}}</p>
	{{/if}}
	{{#if hasFixed}}
		<p class="recogCalcFixedAward"><cms:contentText key="AMOUNT" code="promotion.public.recognition"/>: {{fixedAmount}}</p>
	{{/if}}
	{{#if hasAwardLevel}}
		<p class="recogCalcFixedAward"><cms:contentText key="AMOUNT" code="promotion.public.recognition"/>: {{{awardLevel}}}</p>
	{{/if}}
	<button disabled="disabled"><cms:contentText key="SAVE" code="system.button"/></button>
</div>
</script>
