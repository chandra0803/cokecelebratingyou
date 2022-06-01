<%@page import="com.biperf.core.utils.UserManager"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="com.biperf.core.domain.participant.AccountSummary"%>
<%@ include file="/include/taglib.jspf" %>
<%@ page import="java.util.*"%>


<%
AccountSummary accountSummarytemp = (AccountSummary)request.getAttribute( "accountSummary");
%>


<h2><cms:contentText key="STATEMENT" code="profile.page"/></h2>

<c:if test="${displaySummary}">
<%--<ul class="export-tools fr">
    <li><a href="#" class="btn btn-small"><cms:contentText key="PRINT" code="profile.statment.tab"/> <i class="icon-printer"></i></a></li>
</ul>--%>

<div id="profilePageStatementTabSummary">
    <h3><cms:contentText key="SUMMARY" code="profile.statment.tab"/></h3>
    <c:if test="${accountSummary.accountNumber == null || accountSummary.accountNumber == ''}">
    <dl class="dl-horizontal dl-h1">
        <p><cms:contentText key="NO_ACCOUNT_FOUND" code="profile.statment.tab"/></p>
    </dl>
    </c:if>

    <c:if test="${accountSummary.accountNumber != null && accountSummary.accountNumber != ''}">
    <dl class="dl-horizontal dl-h1">
    <beacon:authorize ifNotGranted="LOGIN_AS">
        <dt><cms:contentText key="ACCOUNT_NUMBER" code="profile.statment.tab"/></dt>
        <dd><c:out value="${accountSummary.accountNumber}"/></dd>
	</beacon:authorize>
        <dt><cms:contentText key="BEGINNING_BALANCE" code="profile.statment.tab"/></dt>
        <dd  id="beginningBalance"><c:out value="${ accountSummary.displayBeginningBalance }"/></dd>

        <dt><cms:contentText key="EARNED" code="profile.statment.tab"/></dt>
        <dd id="earnedBalance"><c:out value="${ accountSummary.displayEarnedThisPeriod }"/></dd>

        <dt><cms:contentText key="REDEEMED" code="profile.statment.tab"/></dt>
        <dd id="redeemedBalance"><c:out value="${ accountSummary.displayRedeemedThisPeriod }"/></dd>

        <dt><cms:contentText key="ADJUSTMENTS" code="profile.statment.tab"/></dt>
        <dd id="adjustmentBalance"><c:out value="${ accountSummary.displayAdjustmentsThisPeriod }"/></dd>

        <dt><cms:contentText key="PENDING_ORDER" code="profile.statment.tab"/></dt>
        <dd id="pendingBalance"><c:out value="${ accountSummary.displayPendingOrder }"/></dd>

        <dt><cms:contentText key="ENDING_BALANCE" code="profile.statment.tab"/></dt>
        <dd id="endingBalance"><c:out value="${ accountSummary.displayEndingBalance }"/></dd>
    </dl>
    </c:if>
</div><%-- /#profilePageStatementTabSummary --%>
</c:if>

<html:form styleId="profilePageStatementTabForm" styleClass="form-inline" action="profilePageStatementTabResponse">
    <h3><cms:contentText key="SUMMARY_PERIOD" code="profile.statment.tab"/></h3>

    <html:hidden property="method" value="display"/>
    <html:hidden property="printerFriendly" />

      <div class="control-group validateme" data-validate-flags="nonempty" data-validate-fail-msgs='{"nonempty" : "<cms:contentText  key="START_DATE_REQ" code="profile.errors" />"}'>
        <label class="control-label" for="profilePageStatementTabStartDate"><cms:contentText  key="SHOW_ACTIVITY" code="profile.statment.tab" /></label>
        <div class="controls">

            <span class="input-append datepickerTrigger"
                data-date-format="<%=UserManager.getUserDatePattern().toLowerCase()%>"
                data-date-language="<%=UserManager.getUserLocale()%>"
                data-date-autoclose="true">
                <input type="text"  class="input-medium date datepickerInp"
                    id="profilePageStatementTabStartDate"
                    name="startDate"
                    value="${myAccountForm.startDate}"
                    readonly="readonly">
                <button class="btn datepickerBtn" type="button"><i class="icon-calendar"></i></button>
            </span>

        </div>
    </div>

    <div class="control-group validateme" data-validate-flags="nonempty" data-validate-fail-msgs='{"nonempty" : "<cms:contentText  key="END_DATE_REQUIRED" code="profile.errors" />"}'>
        <label class="control-label" for="profilePageStatementTabEndDate"><cms:contentText  key="TO" code="profile.statment.tab" /></label>
        <div class="controls">

            <span class="input-append datepickerTrigger"
                data-date-format="<%=UserManager.getUserDatePattern().toLowerCase()%>"
                data-date-language="<%=UserManager.getUserLocale()%>"
                data-date-autoclose="true">
                <input type="text"  class="input-medium date datepickerInp"
                    id="profilePageStatementTabEndDate"
                    name="endDate"
                    value="${myAccountForm.endDate}"
                    readonly="readonly">
                <button class="btn datepickerBtn" type="button"><i class="icon-calendar"></i></button>
            </span>

        </div>
    </div>

    <div class="controls">
        <button type="submit" class="btn btn-primary" id="statementTabUpdateTransactionDetailsButton"><cms:contentText key="VIEW" code="profile.statment.tab"/></button>
    </div>

</html:form>

<div id="profilePageStatementTabTransactionDetails">
</div>
