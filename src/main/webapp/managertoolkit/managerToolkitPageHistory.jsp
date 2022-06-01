<%@page import="com.biperf.core.utils.UserManager"%>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="java.util.*"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>
<%@ page import="com.biperf.core.ui.budget.BudgetReallocationHistoryForm"%>

<!-- ======== MANAGER TOOLKIT PAGE BUDGET ALLOCATION HISTORY ======== -->

<div id="managerToolkitPageHistoryView" class="managerToolkitPageHistory-liner page-content">
    <div class="row-fluid">
        <div class="span12">

        	<div id="managerBudgetHistoryErrorBlock" class="alert alert-block alert-error" style="display:none;">
                <h4><cms:contentText key="FOLLOWING_ERRORS" code="system.generalerror" /></h4>
                <ul>
                	<html:messages id="actionMessage" >
                		<c:set var="serverReturnedError" value="true"/>
           				<li>${actionMessage}</li>
		  			</html:messages>
                </ul>
	        </div>

	        <table border="0" cellpadding="10" cellspacing="0" width="100%">
			  <tr>
			   <td>
		    	 <h4><cms:contentText key="TITLE" code="budget.reallocation.history"/>&nbsp;<span class="subheadline"><c:out value="${budgetMasterName}"/></span></h4>
		    	  <b><cms:contentText key="BUDGET_TIME_PERIOD_TITLE" code="budget.reallocation.history"/>&nbsp;<c:out value="${budgetSegmentName}"/></b>
		         <br/><br/>
			   </td>
			  </tr>
			</table>

            <%--<h3><cms:contentText key="VIEW_HEADER" code="budget.reallocation.history"/></h3>--%>
            <form id="managerToolkitFormHistoryDatePicker" class="form-inline" action="g5ReduxManagerToolKitPageHistory.do" method="post">
	            <input type="hidden" name="budgetMasterId" value="${budgetReallocationHistoryForm.budgetMasterId}">
	            <input type="hidden" name="budgetSegmentId" value="${budgetReallocationHistoryForm.budgetSegmentId}">
	            <input type="hidden" name="ownerBudgetId" value="${budgetReallocationHistoryForm.ownerBudgetId}">
                <fieldset>
                    <div class="form-inline">
                        <div class="control-group">
                            <label class="control-label" for="startDate"><cms:contentText key="FROM" code="budget.reallocation.history"/></label>
                            <div class="controls">
                                <span class="input-append datepickerTrigger"
                                    data-date-format="<%=UserManager.getUserDatePattern().toLowerCase()%>"
                                    data-date-language="<%=UserManager.getUserLocale()%>"
                                    data-date-autoclose="true">
                                    <input type="text" id="startDate" name="startDate" value="${budgetReallocationHistoryForm.startDate}" readonly="readonly" class="date">
                                    <button class="btn" type="button"><i class="icon-calendar"></i></button>
                                </span>
                            </div>
                        </div>

                        <div class="control-group">
                            <label class="control-label" for="endDate"><cms:contentText key="TO" code="budget.reallocation.history"/></label>
                            <div class="controls">
                                <span class="input-append datepickerTrigger"
                                    data-date-format="<%=UserManager.getUserDatePattern().toLowerCase()%>"
                                    data-date-language="<%=UserManager.getUserLocale()%>"
                                    data-date-autoclose="true">
                                    <input type="text" id="endDate" name="endDate" value="${budgetReallocationHistoryForm.endDate}" readonly="readonly" class="date">
                                    <button class="btn" type="button"><i class="icon-calendar"></i></button>
                                </span>
                            </div>
                        </div>
                        <button name="refreshBudgetReallocationTable" class="btn btn-primary" type="submit"><cms:contentText key="REFRESH" code="system.button"/></button>
                    </div>
                </fieldset>
            </form>
            <table id="valueBean" class="table table-striped">
                <thead>
                    <tr>
                    	<c:choose>
                    		<c:when test="${budgetReallocationHistoryForm.isOrderByDateAscending}"><c:set var="sortDir" value="ascending"/></c:when>
                    		<c:otherwise><c:set var="sortDir" value="descending"/></c:otherwise>
                    	</c:choose>
                        <th class="sortable dateColumn sorted ${sortDir}">
                        	<% 	Map<String,Object> parameterMap = new HashMap<String,Object>();
                        		BudgetReallocationHistoryForm tempForm = (BudgetReallocationHistoryForm)request.getAttribute("budgetReallocationHistoryForm");
					          	parameterMap.put("ownerBudgetId", tempForm.getOwnerBudgetId());
					          	parameterMap.put("budgetMasterId", tempForm.getBudgetMasterId());
					          	parameterMap.put("budgetSegmentId", tempForm.getBudgetSegmentId());
					          	parameterMap.put("startDate", tempForm.getStartDate());
					          	parameterMap.put("endDate", tempForm.getEndDate());
					          	parameterMap.put("isOrderByDateAscending", !tempForm.getIsOrderByDateAscending());
							  	pageContext.setAttribute("sortDateUrl", ClientStateUtils.generateEncodedLink(RequestUtils.getBaseURI(request), "/g5ReduxManagerToolKitPageHistory.do", parameterMap));
					       	%>
                            <a href="${sortDateUrl}"><cms:contentText key="DATE" code="budget.reallocation.history"/> <i class="icon-arrow-1-down"></i><i class="icon-arrow-1-up"></i></a>
                        </th>
                        <th class="crud-table-header-row">
                            <cms:contentText key="NAME" code="budget.reallocation.history"/>
                        </th>
                        <th class="crud-table-header-row">
                            <cms:contentText key="AMOUNT" code="budget.reallocation.history"/>
                        </th>
                    </tr>
                </thead>
                <tbody>
                	<c:choose>
                		<c:when test="${empty budgetAdjustmentList}">
                			<tr class="crud-content" align="left">
	                    		<td colspan="3">
	                             	<cms:contentText key="NO_RESULTS" code="budget.reallocation.history"/>
	                        	</td>
	                    	</tr>
                		</c:when>
                		<c:otherwise>
                			<c:forEach var="budgetAdjustmentItem" items="${budgetAdjustmentList}">
		                    	<tr class="crud-content" align="left">
		                    		<td><c:out value="${budgetAdjustmentItem.budgetHistoryDate}"/></td>
		                    		<td><c:out value="${budgetAdjustmentItem.budgetName}"/></td>
		                    		<td><c:out value="${budgetAdjustmentItem.adjustedAmount}"/></td>
		                    	</tr>
		                    </c:forEach>
                		</c:otherwise>
                	</c:choose>
                </tbody>
            </table>
        </div>
    </div>
</div>

<input type="hidden" id="serverReturnedErrored" value="${serverReturnedError}">

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
	var mtpbv;

	//attach the view to an existing DOM element
	$(document).ready(function(){
    	mtpbv = new ManagerToolkitPageHistoryView({
	        el:$('#managerToolkitPageHistoryView'),
	        pageNav : {
	            back : {
	                text : '<cms:contentText key="BACK" code="system.button"/>',
	                url : '<%= RequestUtils.getBaseURI(request)%>/g5ReduxManagerToolKitBudgetTransfer.do?method=display'
	            },
	            home : {
	                text : '<cms:contentText key="HOME" code="system.general" />',
	                url : '<%= RequestUtils.getBaseURI(request)%>/homePage.do'
	            }
	        },
	        pageTitle : '<cms:contentText key="PAGE_TITLE" code="budget.reallocation.history"/>'
	    });
	});
</script>
