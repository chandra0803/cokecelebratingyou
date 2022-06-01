<%@page import="com.biperf.core.utils.UserManager"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf" %>
<%@ page import="java.util.*"%>

<logic:messagesPresent>
	<div><p><cms:errors/></p></div>
</logic:messagesPresent>

<logic:messagesNotPresent>
<c:if test="${fn:length(valueObjects)!=0}">

<%-- populated via JS view, grabbing from display:table output --%>


<h3 class="fl"><cms:contentText key="TRANSACTION_DETAILS" code="profile.statment.tab"/>&nbsp;<cms:contentText key="PLATEAU_AWARDS" code="profile.statment.tab"/></h3>
<div class="">
<ul class="export-tools fr" style="display:none">
     <li class="export csv">
         <a href="layout.html" class="exportCsvButtonPlateau" style="display:none">
            <span class="btn btn-inverse btn-compact btn-export-csv">
                <cms:contentText key="CSV" code="system.general" /> <i class="icon-download-2"></i>
            </span>
         </a>
     </li>
     <%-- <li class="export xls">
         <a href="layout.html" class="exportXlsButtonPlateau" style="display:none">
            <span class="btn btn-inverse btn-compact btn-export-xls">
                XLS <i class="icon-download-2"></i>
            </span>
         </a>
     </li> --%>
     <li class="export pdf">
         <a href="layout.html" class="exportPdfButtonPlateau" style="display:none">
            <span class="btn btn-inverse btn-compact btn-export-pdf">
                <cms:contentText key="PDF" code="system.general" /> <i class="icon-download-2"></i>
            </span>
         </a>
     </li>
</ul>
</div>
<div class="row-fluid">
 <c:if test="${ accountTransactionCount > 500 }" >
            			<cms:contentText key="TOO_MANY_RECORDS" code="profile.statment.tab"/>
 </c:if>
 <c:if test="${ accountTransactionCount <= 500 }" >
    <div class="span12">
    <display:table name="valueObjects" id="valueObject"  class="table table-striped"
						    requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>" export="true">

        	<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
			  <display:setProperty name="export.xml" value="false" />
			  <display:setProperty name="export.csv" value="true" />
		 	  <display:setProperty name="export.csv.label" value="CSV" />
			  <display:setProperty name="export.csv.filename" value="export.csv" />
			  <display:setProperty name="export.csv.include_header" value="true" />
			  <display:setProperty name="export.csv.class" value="com.biperf.core.ui.utils.CustomCsvView" />
			  <display:setProperty name="export.excel" value="true" />
			  <display:setProperty name="export.excel.label" value="XLS" />
			  <display:setProperty name="export.pdf" value="true" />
			  <display:setProperty name="export.pdf.filename" value="export.pdf" />
			  <display:setProperty name="export.pdf.include_header" value="true" />
			  <display:setProperty name="export.pdf.class" value="com.biperf.core.ui.utils.CustomPdfView" />
			  <display:setProperty name="export.banner" value="<table width='100%'><tr><td align='left'><span class='export'><input type='hidden' class='selectedMode' value='plateau'>{0}</span></td></tr></table>" />
	  	    <display:column titleKey="participant.myaccount.DATE_HEADER" property="formatSubmissionDate" headerClass="crud-table-header-row" class="crud-content top-align left-align nowrap" sortable="true">
	      	</display:column>
	  	    <display:column titleKey="participant.myaccount.DESCRIPTION" property="promotion.name" headerClass="crud-table-header-row" class="crud-content top-align left-align nowrap" sortable="true"/>
	  		<display:column titleKey="recognition.history.AWARD" headerClass="crud-table-header-row" class="crud-content left-align nowrap" sortable="true">
	  		<c:choose>
               <c:when test="${ valueObject.levelName != null }">
                 <c:out value="${ valueObject.levelName }"/><br>
               </c:when>
               <c:otherwise>
		  		 <c:if test="${valueObject.merchGiftCodeActivityList != null }">
		           <c:forEach items="${valueObject.merchGiftCodeActivityList}" var="merchGiftCodeActivity">
		               <c:if test="${merchGiftCodeActivity.merchOrder != null }">
		                 <c:choose>
		                   <c:when test="${merchGiftCodeActivity.merchOrder.merchGiftCodeType.code == 'level' }">
		                     <c:if test="${merchGiftCodeActivity.merchOrder.promoMerchProgramLevel != null }">
		                       <cms:contentText key="LEVEL_NAME" code="${ merchGiftCodeActivity.merchOrder.promoMerchProgramLevel.cmAssetKey}"/><br>
		                     </c:if>
		                   </c:when>
		                   <c:otherwise>
		                     <c:out value="${ merchGiftCodeActivity.merchOrder.productDescription}"/><br>
		                   </c:otherwise>
		                 </c:choose>
		               </c:if>
		             </c:forEach>
		          </c:if>
	            </c:otherwise>
	          </c:choose>
	  		  <c:if test="${ valueObject.awardQuantity > 0 || valueObject.awardQuantity < 0 }">
                 <%-- BugFix 17702 Remove the &nbsp; so that display tag exports Properly(There is an problem with parsing &nbsp; for exports) --%>
                 <c:if test="${!valueObject.reversal}">
                   <c:out value="${valueObject.awardQuantity}"/> <c:out value="${valueObject.awardTypeName}"/><c:if test="${valueObject.awardTypeName eq  'Points'}"><c:out value="${valueObject.regSymbol}"/></c:if>
                 </c:if>
                 <c:if test="${valueObject.reversal}">
                   <c:out value="${valueObject.awardQuantity}"/>&nbsp;<cms:contentText key="REVERSE_SYMBOL" code="participant.transactionhistory"/>&nbsp;<c:out value="${valueObject.awardTypeName}"/><c:if test="${valueObject.awardTypeName eq  'Points'}"><c:out value="${valueObject.regSymbol}"/></c:if>
                 </c:if>
	            </c:if>
	     </display:column>
	  </display:table>
    </div>
  </c:if>
</div>
</c:if>

<c:if test="${ !isGiftCodeOnlyPax }">



<h3 class="fl"><cms:contentText key="TRANSACTION_DETAILS" code="profile.statment.tab"/>&nbsp;<cms:contentText key="POINTS" code="profile.statment.tab"/></h3>
<%-- populated via JS view, grabbing from display:table output --%>
    <div class="">
    <ul class="export-tools" style="display:none">
         <li class="export csv">
             <a href="layout.html" class="exportCsvButtonPoints" style="display:none">
                <span class="btn btn-inverse btn-compact btn-export-csv">
                    <cms:contentText key="CSV" code="system.general" /> <i class="icon-download-2"></i>
                </span>
             </a>
         </li>
         <%-- <li class="export xls">
             <a href="layout.html" class="exportXlsButtonPoints" style="display:none">
                <span class="btn btn-inverse btn-compact btn-export-xls">
                    XLS <i class="icon-download-2"></i>
                </span>
             </a>
         </li> --%>
         <li class="export pdf">
             <a href="layout.html" class="exportPdfButtonPoints" style="display:none">
                <span class="btn btn-inverse btn-compact btn-export-pdf">
                    <cms:contentText key="PDF" code="system.general" /> <i class="icon-download-2"></i>
                </span>
             </a>
         </li>
    </ul>
    </div>
  <div class="row-fluid" >
    <c:if test="${ accountTransactionCount > 500 }" >
            			<cms:contentText key="TOO_MANY_RECORDS" code="profile.statment.tab"/>
    </c:if>
    <c:if test="${ accountTransactionCount <= 500 }" >
    <div class="span12">
    			<input type="hidden" id="beginningBalanceValue" value="${accountSummary.displayBeginningBalance}">
                <input type="hidden" id="earnedBalanceValue" value="${accountSummary.displayEarnedThisPeriod}">
                <input type="hidden" id="redeemedBalanceValue" value="${accountSummary.displayRedeemedThisPeriod}">
                <input type="hidden" id="adjustmentBalanceValue" value="${accountSummary.displayAdjustmentsThisPeriod}">
                <input type="hidden" id="pendingBalanceValue" value="${accountSummary.displayPendingOrder}">
                <input type="hidden" id="endingBalanceValue" value="${accountSummary.displayEndingBalance}">
           <display:table class="table table-striped" name="accountSummary.accountTransactions" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>" export="true" id="accountTransaction">
	    		<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
	    		<display:setProperty name="basic.msg.empty_list_row">
            		<tr class="crud-content" align="left"><td colspan="{0}">
                       	<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
	    		</display:setProperty>
	    		<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
				  <display:setProperty name="export.xml" value="false" />
			 	  <display:setProperty name="export.csv" value="true" />
		 	  	  <display:setProperty name="export.csv.label" value="CSV" />
			  	  <display:setProperty name="export.csv.filename" value="export.csv" />
			  	  <display:setProperty name="export.csv.include_header" value="true" />
			  	  <display:setProperty name="export.csv.class" value="com.biperf.core.ui.utils.CustomCsvView" />
				  <display:setProperty name="export.excel" value="true" />
			  	  <display:setProperty name="export.excel.label" value="XLS" />
				  <display:setProperty name="export.pdf" value="true" />
				  <display:setProperty name="export.pdf.filename" value="export.pdf" />
				  <display:setProperty name="export.pdf.include_header" value="true" />
				  <display:setProperty name="export.pdf.class" value="com.biperf.core.ui.utils.CustomPdfView" />
				  <display:setProperty name="export.banner" value="<table width='100%'><tr><td align='left'><span class='export'><input type='hidden' class='selectedMode' value='points'>{0}</span></td></tr></table>" />
            	<c:choose>
            		<c:when test="${ accountTransactionCount > 500 }" >
            			<cms:contentText key="TOO_MANY_RECORDS" code="profile.statment.tab"/>
            		</c:when>
            		<c:otherwise>
						    <display:column titleKey="participant.myaccount.DATE_HEADER" headerClass="unsorted ascending" sortable="true" sortProperty="transactionDate">
						    	<c:out value="${accountTransaction.formattedTransactionDate}"/>
						    </display:column>
						    <display:column titleKey="participant.myaccount.DESCRIPTION" headerClass="unsorted ascending" property="description" sortable="true" />
						    <display:column titleKey="participant.myaccount.AMOUNT" headerClass="unsorted ascending"  sortable="true" sortProperty="pointQuantity">
    							<c:out value="${accountTransaction.formattedTransactionAmount}"/>
    						</display:column>
            		</c:otherwise>
              </c:choose>
		</display:table>
    </div>
    </c:if>
  </div>
</c:if>
</logic:messagesNotPresent>
