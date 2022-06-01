<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="java.util.*"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>
<%@ page import="com.biperf.core.ui.budget.BudgetReallocationForm"%>
<%@ page import="org.apache.struts.Globals"%>
<%@ page import="org.apache.struts.taglib.html.Constants" %>

<!-- ======== MANAGER TOOLKIT PAGE BUDGET ALLOCATION ======== -->

<div id="managerToolkitPageBudgetView" class="managerToolkitPageBudget-liner page-content">

    <div class="page-topper">
        <div class="row-fluid">
            <div class="span12">

            	<div id="managerBudgetErrorBlock" class="alert alert-block alert-error" style="display:none;">
            		<c:if test="${not isInfMsg}">
	                	<h4><cms:contentText key="FOLLOWING_ERRORS" code="system.generalerror" /></h4>
	                </c:if>
	                <ul>
	                	<html:messages id="actionMessage" >
	                		<c:set var="serverReturnedError" value="true"/>
            				<li>${actionMessage}</li>
			  			</html:messages>
	                </ul>
	            </div>

                <html:form styleId="promotionSelect" action="/g5ReduxManagerToolKitBudgetTransfer.do?method=display" styleClass="form-inline">
                    <div class="control-group">
                        <label class="control-label" for="budgetMasterId"><cms:contentText key="SELECT_BUDGET_MASTER" code="budget.reallocation"/></label>

                        <div class="controls">
                          <html:select styleId="budgetMasterId" property="budgetMasterId">
	                           	<html:option value=""><cms:contentText key="SELECT_BUDGET_OPTION" code="budget.reallocation"/></html:option>
	                            <c:forEach var="budMaster" items="${budgetMasterList}">
										<html:option value="${budMaster.id}"><cms:contentText code="${budMaster.cmAssetCode}" key="${budMaster.nameCmKey}" /></html:option>
									</c:forEach>
							</html:select>
                        </div>
                    </div><!-- /.control-group -->

                    <!-- this select only needs to be visible and populated when a budget is selected -->
					<c:if test="${not empty budgetSegmentList}">
	                    <div class="control-group">
	                        <label class="control-label" for="budgetSegmentId"><cms:contentText key="SELECT_BUDGET_SEGMENT" code="budget.reallocation"/></label>

	                        <div class="controls">
	                            <!-- May or May not show dependent on selected promotion -->
	                            <html:select styleId="budgetSegmentId" property="budgetSegmentId">
	                            	<c:if test="${fn:length(budgetSegmentList) gt 0}"><html:option value=""><cms:contentText key="TIME_PERIOD" code="budget.reallocation"/></html:option></c:if>
		                            <html:options collection="budgetSegmentList" property="id" labelProperty="paxDisplaySegmentName" />
								</html:select>
	                        </div>
	                    </div>
	              	</c:if>

                    <!-- this select only needs to be visible and populated when a node selection is required -->
					<c:if test="${not empty eligibleNodeList}">
	                    <div class="control-group">
	                        <label class="control-label" for="ownerBudgetNodeId"><cms:contentText key="SELECT_ORG_UNIT" code="budget.reallocation"/></label>

	                        <div class="controls">
	                            <!-- May or May not show dependent on selected promotion -->
	                            <html:select styleId="ownerBudgetNodeId" property="ownerBudgetNodeId">
	                            	<c:if test="${eligibleNodeListSize != 1}"><html:option value=""><cms:contentText key="SELECT_ORG_UNIT" code="budget.reallocation"/></html:option></c:if>
		                            <html:options collection="eligibleNodeList" property="id" labelProperty="name" />
								</html:select>
	                        </div>
	                    </div>
	              	</c:if>

                </html:form>
            </div><!-- /.span12 -->
        </div><!-- /.row -->
    </div><!-- /.page-topper -->


	<c:if test="${budgetReallocationForm.budgetSelected}">
	    <div class="row-fluid">
	        <div class="span12">
	            <h2><cms:contentText key="VIEW_HEADER" code="budget.reallocation"/> ${budgetReallocationForm.ownerBudgetCurrentValue}</h2>
	            <% 	Map<String,Object> parameterMap = new HashMap<String,Object>();
		          	BudgetReallocationForm tempForm = (BudgetReallocationForm)request.getAttribute("budgetReallocationForm");
		          	parameterMap.put("ownerBudgetId", tempForm.getOwnerBudgetId());
		          	parameterMap.put("budgetSegmentId", tempForm.getBudgetSegmentId());
		            parameterMap.put("budgetMasterId", tempForm.getBudgetMasterId());
				  	pageContext.setAttribute("budgetHistoryUrl", ClientStateUtils.generateEncodedLink(RequestUtils.getBaseURI(request), "/g5ReduxManagerToolKitPageHistory.do", parameterMap));
		       	%>
	            <p class="managerToolkitRelHistoryLink"><a href="<c:out value="${budgetHistoryUrl}"/>"><cms:contentText key="VIEW_HISTORY" code="budget.reallocation"/></a></p>
	            <h3>&nbsp;</h3>
				<form id="managerToolkitFormAllocatePoints" class="managerToolkitFormAllocatePoints" action="#" >
					<c:if test="${displayAdditionalManagerAdd}"> 
					<!--  Org Unit Budget -->
					<c:if test="${not empty eligibleNodeList}">
						<div class="paxSearchStartView"
    						data-search-url="${pageContext.request.contextPath}/search/ownerSearch.action"></div>
    				</c:if>
    				<!-- Pax Budget -->
					<c:if test="${empty eligibleNodeList}">
						<div class="paxSearchStartView"
    						data-search-url="${pageContext.request.contextPath}/participantFolloweeSearch/participantFolloweeSearch.action"></div>
    				</c:if>
    				</c:if>
	                <fieldset>

	                    <table class="table table-striped">
	                    	<thead>
	                            <tr>
	                                <th><cms:contentText key="BUDGET_NAME" code="budget.reallocation"/></th>
	                                <th><cms:contentText key="BUDGET_SPENT" code="budget.reallocation"/></th>
	                                <th><cms:contentText key="CURRENT_BUDGET" code="budget.reallocation"/></th>
	                                <th><cms:contentText key="TRANSFER_AMOUNT" code="budget.reallocation"/></th>
	                            </tr>
	                        </thead>

	                        <tbody>
	                           	<c:choose>
	                           		<c:when test="${fn:length(budgetReallocationForm.childReallocationBudgetList) == 0}">
	                           			<tr class="warning">
	                           				<td colspan="4"><cms:contentText key="EMPTY_LIST_MSG" code="budget.reallocation"/></td>
	                           			</tr>
	                           		</c:when>
	                           		<c:otherwise>
				                        <c:forEach var="childBudget" items="${budgetReallocationForm.childReallocationBudgetList}" varStatus="childBudgetStatus">
					                        <tr>
					                            <td><c:out value="${childBudget.nodeName}"/></td>
					                            <c:if  test="${ childBudget.NA }">
					                             	<td>N/A</td>
					                            	<td>N/A</td>
					                            </c:if>
					                            <c:if  test="${not childBudget.NA }">
					                            	<td><c:out value="${childBudget.budgetSpent}"/></td>
					                            	<td><c:out value="${childBudget.currentBudget}"/></td>
					                            </c:if>
                                                <td class="reallocationAmount">
					                                <input type="hidden" name="childReallocationBean[${childBudgetStatus.index}].nodeName" class="nodeName" value="${childBudget.nodeName}">
					                                <input type="hidden" name="childReallocationBean[${childBudgetStatus.index}].budgetSpent" class="budgetSpent" value="${childBudget.budgetSpent}">
					                                <input type="hidden" name="childReallocationBean[${childBudgetStatus.index}].currentBudget" class="currentBudget" value="${childBudget.currentBudget}">
					                                <input type="hidden" name="childReallocationBean[${childBudgetStatus.index}].childNodeOwnerNodeId" class="childNodeOwnerNodeId" value="${childBudget.childNodeOwnerNodeId}">
				                                	<input type="hidden" name="childReallocationBean[${childBudgetStatus.index}].childBudgetId" class="childBudgetId" value="${childBudget.childBudgetId}">
				                                	<input type="hidden" name="childReallocationBean[${childBudgetStatus.index}].userId" class="userId" value="${childBudget.userId}">
						 			<input type="hidden" name="childReallocationBean[${childBudgetStatus.index}].childNodeOwnerNodeId" class="childNodeOwnerNodeId" value="${childBudget.childNodeOwnerNodeId}">
									<input type="hidden" name="childReallocationBean[${childBudgetStatus.index}].NA" class="NA" value="${childBudget.NA}">
					                                <input type="text" name="childReallocationBean[${childBudgetStatus.index}].adjustmentAmount" maxlength="10" size="10" value="${childBudget.adjustmentAmount}" id="amt_${childBudgetStatus.index}" class="content-field number" data-number-type="decimal">
					                                </td>
					                        </tr>
				                         </c:forEach>
			                         </c:otherwise>
		                         </c:choose>
	                        </tbody>
	                    </table>

		                <div class="form-actions pullBottomUp">
		                <input   class="btn btn-primary" type="button" name="submit" value="<cms:contentText key="PREVIEW" code="system.button"/>" onclick="buildParam();" />
	                        <a class="btn" href="<%= RequestUtils.getBaseURI(request)%>/g5ReduxManagerToolKitBudgetTransfer.do?method=display"><cms:contentText key="CANCEL" code="system.button"/></a>
	                    </div>
	                </fieldset>
	            </form>
	        </div>
    	</div>
    </c:if>
</div>

<input type="hidden" id="serverReturnedErrored" value="${serverReturnedError}">

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>

    $(document).ready(function() {
    	
    	G5.props.URL_JSON_PARTICIPANT_SEARCH_RESULTS_BUDGET_TRANSFER = G5.props.URL_ROOT + 'customBudgetTransfer.do?method=addAdditionalPax';
    	
        //attach the view to an existing DOM element
        window.mtpbv = new ManagerToolkitPageBudgetView({
	        el: $('#managerToolkitPageBudgetView'),
	        pageTitle : '<cms:contentText key="PAGE_TITLE" code="budget.reallocation"/>'
        });
    });
	function buildParam(){
		var paramTempForm = "",
		urlIndex = "${pageContext.request.contextPath}",
		param_default = "";
		$(".content-field.number").each( function() {
			var tansferAmount =  $(this).val(),
			tansferAmountVal =  parseInt(tansferAmount);
			if(isNaN(tansferAmountVal)){
				tansferAmountVal = 0;
			}
			if(tansferAmountVal !=0) {
				var adjustmentAmount =  $(this).attr('name'),
				adjustmentAmountVal =  adjustmentAmount+'='+$(this).val(),
				na = $(this).siblings(".NA").attr('name'),
				naVal = na+'='+$(this).siblings(".NA").val(),
				nodeName = $(this).siblings(".nodeName").attr('name'),
				nodeNameVal = nodeName+'='+$(this).siblings(".nodeName").val(),
				budgetSpent = $(this).siblings(".budgetSpent").attr('name'),
				budgetSpentVal  = budgetSpent+'='+$(this).siblings(".budgetSpent").val(),
				currentBudget = $(this).siblings(".currentBudget").attr('name'),
				currentBudgetVal = currentBudget+'='+$(this).siblings(".currentBudget").val(),
				childNodeOwnerNodeId = $(this).siblings(".childNodeOwnerNodeId").attr('name'),
				childNodeOwnerNodeIdVal  = childNodeOwnerNodeId+'='+$(this).siblings(".childNodeOwnerNodeId").val(),
				childBudgetId = $(this).siblings(".childBudgetId").attr('name'),
				childBudgetIdVal  = childBudgetId+'='+$(this).siblings(".childBudgetId").val();
				userId = $(this).siblings(".userId").attr('name'),
				userIdVal  = userId+'='+$(this).siblings(".userId").val();
				childNodeOwnerNodeId = $(this).siblings(".childNodeOwnerNodeId").attr('name'),
				childNodeOwnerNodeIdVal  = childNodeOwnerNodeId+'='+$(this).siblings(".childNodeOwnerNodeId").val();
				paramTempForm = paramTempForm +"&"+ nodeNameVal+"&"+adjustmentAmountVal+"&"+budgetSpentVal+"&"+currentBudgetVal+"&"+childNodeOwnerNodeIdVal+"&"+childBudgetIdVal+"&"+userIdVal+"&"+childNodeOwnerNodeIdVal+"&"+naVal+"";
			}
		});

		param_default = getBudgetMasterParam() ;
		param = param_default +""+paramTempForm;
		post(""+urlIndex+"/g5ReduxManagerToolKitBudgetTransferSubmit.do?method=prepareReview",param );
	}

	function getBudgetMasterParam(){
		var budgetSelected = "${budgetReallocationForm.budgetSelected}",
		budgetMasterId = "${budgetReallocationForm.budgetMasterId}",
		budgetSegmentId ="${budgetReallocationForm.budgetSegmentId}",
		ownerBudgetNodeId = "${budgetReallocationForm.ownerBudgetNodeId}",
		ownerBudgetId  = "${budgetReallocationForm.ownerBudgetId}",
		ownerBudgetCurrentValue  = "${budgetReallocationForm.ownerBudgetCurrentValue}",
		token = '<%= session.getAttribute(Globals.TRANSACTION_TOKEN_KEY) %>' ;

		var param = "&budgetSelected=" + budgetSelected +
					"&budgetMasterId=" + budgetMasterId +
					"&budgetSegmentId=" + budgetSegmentId+
					"&ownerBudgetNodeId=" +ownerBudgetNodeId +
					"&ownerBudgetId=" + ownerBudgetId +
					"&ownerBudgetCurrentValue=" + ownerBudgetCurrentValue +
					"&org.apache.struts.taglib.html.TOKEN=" + token +"";
		return param;

	}

	function post(URL, PARAM) {
		var temp = document.createElement("form");
		temp.action=URL;
		temp.method="POST";
		temp.style.display="none";
		var PARAMS = PARAM.split("&");
		for(var x=0 ;x< PARAMS.length; x++) {
			var opt=document.createElement("INPUT");
			var PARAMSValue = PARAMS[x].split("=");
			opt.setAttribute('type','hidden');
			opt.setAttribute('name',PARAMSValue[0]);
			opt.setAttribute('value',PARAMSValue[1]);
			temp.appendChild(opt);
		}
		document.body.appendChild(temp);
		temp.submit();
		return temp;
	}

</script>


<%@include file="/search/paxSearchStart.jsp" %>
