<%@ include file="/include/taglib.jspf" %>
<%@ page import="java.util.*"%>
<%@ page import="com.biperf.core.ui.claim.RecognitionHistoryValueObject"%>
<%@ page import="com.biperf.core.ui.claim.RecognitionHistoryForm"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>
<%@ page import="com.biperf.core.domain.claim.Claim"%>


<div id="profilePageActivityHistoryResponseData" data-percent-complete=".85">
    <div class="tabbable" id="profilePageActivityHistoryActiveTabPageTabSet">
    <c:if test = "${showTabView == 'true'}">
	    <ul class="nav nav-tabs" id="profilePageActivityHistoryTabSelectedTabs">
	    	<li class="active">
	            <a  data-toggle="tab" href="#nominationsSent">
	                <c:if test="${ ! empty activityHistoryForm.promotionTypeCode }"> 
	                    <span><cms:contentText key="${activityHistoryForm.promotionTypeCode}_submitted" code="activitycenter.history"/></span>
	                </c:if>
	                <c:if test="${ empty activityHistoryForm.promotionTypeCode }"> 
	                    <span><cms:contentText key="submitted" code="activitycenter.history"/></span>
	                </c:if>	                            	   
	            </a>
	    	</li>
        	<li>
                <a data-toggle="tab" href="#nominationsReceived">
                 	<c:if test="${ ! empty activityHistoryForm.promotionTypeCode }"> 
                        <span><cms:contentText key="${activityHistoryForm.promotionTypeCode}_won" code="activitycenter.history"/></span>
                    </c:if>
                    <c:if test="${ empty activityHistoryForm.promotionTypeCode }"> 
                        <span><cms:contentText key="won" code="activitycenter.history"/></span>
                    </c:if>	                            		 
                </a>
            </li>
	    </ul>
	    </c:if>
        <div class="tab-content">
   
      	<c:if test = "${tabClicked == 'sent' or tabClicked == 'both'}">
			<%@ include file="profilePageNominationResponseSent.jsp"%>
		</c:if>
		<c:if test = "${tabClicked == 'received' or tabClicked == 'both'}">
			<%@ include file="profilePageNominationResponseReceived.jsp" %>
		</c:if>
        </div>
    </div>
</div>