<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="java.util.*"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>
<%@ page import="com.biperf.core.ui.budget.BudgetReallocationForm"%>
<%@ page import="com.biperf.core.utils.UserManager"%>

<!-- ======== MANAGER TOOLKIT PAGE BUDGET ALLOCATION PREVIEW ======== -->

<div id="managerToolkitPageBudgetPreviewView" class="managerToolkitPageBudgetPreview-liner page-content"><div class="row-fluid">
        <div class="span12">
            <%--<h3><cms:contentText key="VIEW_HEADER" code="budget.reallocation.preview"/></h3>--%>
            <html:form styleId="managerToolkitFormReallocateBudget" action="g5ReduxManagerToolKitBudgetTransferSubmit">
            	<input type="hidden" name="budgetSelected" value="${budgetReallocationForm.budgetSelected}">
            	<input type="hidden" name="budgetMasterId" value="${budgetReallocationForm.budgetMasterId}">
            	<input type="hidden" name="budgetSegmentId" value="${budgetReallocationForm.budgetSegmentId}">
            	<input type="hidden" name="ownerBudgetId" value="${budgetReallocationForm.ownerBudgetId}">
            	<input type="hidden" name="ownerBudgetNodeId" value="${budgetReallocationForm.ownerBudgetNodeId}">
            	<input type="hidden" name="ownerBudgetCurrentValue" value="${budgetReallocationForm.ownerBudgetCurrentValue}">
            	<input type="hidden" name="budgetMasterName" value="${budgetReallocationForm.budgetMasterName}">
				<input type="hidden" name="budgetSegmentName" value="${budgetReallocationForm.budgetSegmentName}">
				<input type="hidden" name="NA" value="${budgetReallocationForm.NA}">
                <fieldset>
                	<table border="0" cellpadding="10" cellspacing="0" width="100%">
					  <tr>
					   <td>
				    	 <h4><cms:contentText key="TITLE" code="budget.reallocation.history"/>&nbsp;<span class="subheadline"><c:out value="${budgetReallocationForm.budgetMasterName}"/></span></h4>
				    	 <b><cms:contentText key="BUDGET_TIME_PERIOD_TITLE" code="budget.reallocation.history"/>&nbsp;<c:out value="${budgetReallocationForm.budgetSegmentName}"/></b>
				         <br/><br/>
					   </td>
					  </tr>
					</table>
                    <table class="table table-striped">
                        <thead>
                            <tr>
                                <th><cms:contentText key="NAME" code="budget.reallocation.preview"/></th>
                                <th><cms:contentText key="CURRENT" code="budget.reallocation.preview"/></th>
                                <th><cms:contentText key="PROPOSED_TRANSFER" code="budget.reallocation.preview"/></th>
                                <th><cms:contentText key="AMOUNT_AFTER_TRANSFER" code="budget.reallocation.preview"/></th>
                            </tr>
                        </thead>
                        <tbody>
                        	<c:forEach var="childBudget" items="${budgetReallocationForm.childReallocationBudgetList}" varStatus="childBudgetStatus">
							<tr style="display :none">
		                            <td style="display :none">
		                                <input type="hidden" name="childReallocationBean[${childBudgetStatus.index}].adjustmentAmount" value="${childBudget.adjustmentAmount}">
		                                <input type="hidden" name="childReallocationBean[${childBudgetStatus.index}].nodeName" value="${childBudget.nodeName}">
		                                <input type="hidden" name="childReallocationBean[${childBudgetStatus.index}].budgetSpent" value="${childBudget.budgetSpent}">
		                                <input type="hidden" name="childReallocationBean[${childBudgetStatus.index}].currentBudget" value="${childBudget.currentBudget}">
		                                <input type="hidden" name="childReallocationBean[${childBudgetStatus.index}].childNodeOwnerNodeId" value="${childBudget.childNodeOwnerNodeId}">
	                                	<input type="hidden" name="childReallocationBean[${childBudgetStatus.index}].childBudgetId" value="${childBudget.childBudgetId}">
	                                	<input type="hidden" name="childReallocationBean[${childBudgetStatus.index}].userId" value="${childBudget.userId}">
	                                	<input type="hidden" name="childReallocationBean[${childBudgetStatus.index}].NA" class="NA" value="${childBudget.NA}">
		                            </td>
		                        </tr>
	                            <tr <c:if test="${empty childBudget.adjustmentAmount or childBudget.adjustmentAmount == 0}">style="display:none"</c:if>> 
								<td><c:out value="${childBudget.nodeName}"/></td>
									<c:if  test="${ childBudget.NA }">
										<td>N/A</td>
									</c:if>
									<c:if  test="${not childBudget.NA }">
										<td><c:out value="${childBudget.currentBudget}"/></td>
									</c:if >
	                                <td><c:out value="${childBudget.adjustmentAmount}"/></td>
									<c:if test="${ childBudget.NA }">
										<td>N/A</td>
									</c:if>
									<c:if  test="${not childBudget.NA }">
										<td><c:out value="${childBudget.amountAfterAdjustment}"/></td>
									</c:if >	
	                            </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </fieldset>
                <fieldset class="managerToolkitCurrentBudgetTable">
                    <table class="table table-striped table-bordered">
                        <tbody>
                            <tr>
                                <th><cms:contentText key="CURRENT_BUDGET" code="budget.reallocation.preview"/></th>
                                <th><c:out value="${budgetReallocationForm.ownerBudgetCurrentValue}"/></th>
                            </tr>
                            <tr>
                                <td><cms:contentText key="TOTAL_TRANSFERS" code="budget.reallocation.preview"/></td>
                                <td><c:out value="${budgetReallocationForm.totalAdjustments}"/></td>
                            </tr>
                            <tr>
                                <td><cms:contentText key="TOTAL_AFTER_TRANSFERS" code="budget.reallocation.preview"/></td>
                                <td><c:out value="${budgetReallocationForm.ownerBudgetAfterAdjustments}"/></td>
                            </tr>
                        </tbody>
                    </table>
                </fieldset>
                <div class="form-actions">
                  <beacon:authorize ifNotGranted="LOGIN_AS">
	                <button type="submit" class="btn btn-primary" name="button" formaction="<%= RequestUtils.getBaseURI(request)%>/g5ReduxManagerToolKitBudgetTransferSubmit.do?method=update"><cms:contentText key="SUBMIT" code="system.button"/></button>
	              </beacon:authorize>

	                <button type="submit" class="btn" name="button" formaction="<%= RequestUtils.getBaseURI(request)%>/g5ReduxManagerToolKitBudgetTransfer.do?method=prepareEdit"><cms:contentText key="EDIT" code="system.button"/></button>

	                <a class="btn previewCancelBtn" href="#"><cms:contentText key="CANCEL" code="system.button"/></a>
                </div>
            </html:form>
           	<% 	Map<String,Object> parameterMap = new HashMap<String,Object>();
	          	BudgetReallocationForm tempForm = (BudgetReallocationForm)request.getAttribute("budgetReallocationForm");
	          	parameterMap.put("ownerBudgetId", tempForm.getOwnerBudgetId());
	          	parameterMap.put("budgetMasterId", tempForm.getBudgetMasterId());
	           	parameterMap.put("budgetSegmentId", tempForm.getBudgetSegmentId());
			  	pageContext.setAttribute("budgetHistoryUrl", ClientStateUtils.generateEncodedLink(RequestUtils.getBaseURI(request), "/g5ReduxManagerToolKitPageHistory.do", parameterMap));
	       	%>
            <p><a href="${budgetHistoryUrl}"><cms:contentText key="VIEW_BUDGET_HISTORY" code="budget.reallocation.preview"/></a></p>
        </div>
    </div>
</div>

<div id="cancelConfirmDialog" class="cancelConfirmDialog" style="display:none">
    <p>
        <b><cms:contentText key="CANCEL_CONFIRM_HEADER" code="budget.reallocation.preview"/></b>
    </p>
    <p class="tc">
        <button id="recognitionButtonChangePromoConfirm" class="btn btn-primary" data-url="<%= RequestUtils.getBaseURI(request)%>/g5ReduxManagerToolKitBudgetTransfer.do?method=display"><cms:contentText key="YES" code="system.button"/></button>
        <button id="recognitionButtonChangePromoCancel" class="btn"><cms:contentText key="NO" code="system.button"/></button>
    </p>
</div><!-- /.promoChangeConfirmDialog -->

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
	var mtpbv;

	//attach the view to an existing DOM element
	$(document).ready(function(){
	    mtpbv = new ManagerToolkitPageBudgetView({
	        el:$('#managerToolkitPageBudgetPreviewView'),
	        pageNav : {
	        	back : {
                    text : '<cms:contentText key="BACK" code="system.button"/>',
                    formId : 'managerToolkitFormReallocateBudget',
                    formAction : '<%= RequestUtils.getBaseURI(request)%>/g5ReduxManagerToolKitBudgetPage.do'
                },
                home : {
                    text : '<cms:contentText key="HOME" code="system.general" />',
                    url : '<%= RequestUtils.getBaseURI(request)%>/homePage.do'
                }
	        },
	        pageTitle : '<cms:contentText key="PAGE_TITLE" code="budget.reallocation.preview"/>'
	    });
	});
</script>
