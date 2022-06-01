<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>


<div id="profilePageActivityHistoryResponseData" data-percent-complete=".85">
    <div class="tabbable" id="profilePageActivityHistoryActiveTabPageTabSet">
     	<c:if test="${fn:length(promotionList)!=0}">
	        <ul class="nav nav-tabs" id="profilePageActivityHistoryTabSelectedTabs">
	          <li class="active">
	            <a data-toggle="tab" href="#quiz">
	                <c:if test="${ ! empty activityHistoryForm.promotionTypeCode }"> 
	                    <span><cms:contentText key="${activityHistoryForm.promotionTypeCode}_submitted" code="activitycenter.history"/></span>
	                 </c:if>
	                 <c:if test="${ empty activityHistoryForm.promotionTypeCode }"> 
	                    <span><cms:contentText key="submitted" code="activitycenter.history"/></span>
	                 </c:if>	                            	   
	            </a>
	          </li>
	        </ul>
        </c:if>
        <div class="tab-content">
           <%@ include file="profilePageQuizHistory.jsp" %>
        </div>
    </div>
</div>
