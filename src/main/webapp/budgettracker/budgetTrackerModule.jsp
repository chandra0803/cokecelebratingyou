<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.ui.utils.PresentationUtils"%>

<!-- ======== BUDGET TRACKER MODULE ======== -->
<script type="text/template" id="budgetTrackerModuleTpl">

    <h3 class="section-header">
        <cms:contentText key="MY_BUDGETS" code="hometile.budgetTracker" />
    </h3>

    <div class="module-liner">

        <!--
            each budget list has a special CM key containing the full translated string for the "Remaining as of" text
            the key ouput will have a {0} placeholder where the budgetAsOfTimestamp value is inserted
            this allows the translations to have plain text and the budgetAsOfTimestamp in any order
            we embed this CM output as a tplVariable in our budgetTrackerModule Handlebars template
            we also have an budgetAsOfTimestamp subTpl embedded in our budgetTrackerModule Handlebars template
            we pass the budgets.budgetAsOfTimestamp value from the JSON to the subTpl, then replace the {0} with the rendered output
            the final string is assigned to budgets.budgetAsOfTimestampFormatted in the JSON to be inserted into the module via jQ
        -->
        <!--tplVariable.budgetAsOfTimestamp= "<cms:contentText key="AS_OF" code="hometile.budgetTracker" />" tplVariable-->
        <!--subTpl.budgetAsOfTimestamp= <strong id="budgetAsOfTimestamp">{{budgetAsOfTimestamp}}</strong> subTpl-->
        <div class="budget-date">
            <span class="budgetAsOfTimestamp"></span>
            <%--<div class="budget-reload-button">
                <a class="reloadBtn">
                    <i class="icon-reload"></i> <cms:contentText key="REFRESH" code="system.button" />
                </a>
            </div>--%>
        </div>

        <!--
            DEVELOPERS NOTE:
            - the data-* attributes below will be included in JSON requests for budgetItems
            - the names will be converted to camel-case without 'data'
            - EX: 'data-some-param' => 'someParam'
        -->
        <ol class="unstyled sidebar-list budgetCollectionView"
            data-some-param="1234"
            data-another-param="98">
                <!-- dynamic content -->

        </ol>

		<c:if test="${beacon:systemVarBoolean('budget.transfer.show')}">
            <div class="transfer-button">
            	<a href="<%= RequestUtils.getBaseURI(request)%>/g5ReduxManagerToolKitBudgetTransfer.do?method=display" class="btn btn-primary btn-inverse transferBtn"><cms:contentText key="TRANSFER" code="hometile.budgetTracker" /></a>
            </div>
    	</c:if>

    </div><!-- /.module-liner -->
</script>

<!--Specify item here. "id" should match item specified in view.js(in this case BudgetCollectionView.js)  -->
<script type="text/template" id="budgetItemTpl">
    <%@include file="/budgettracker/budgetItem.jsp" %>
</script>
