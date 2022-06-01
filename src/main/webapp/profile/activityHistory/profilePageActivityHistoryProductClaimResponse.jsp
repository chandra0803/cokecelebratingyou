<%@page import="com.biperf.core.ui.claim.ProductClaimValueObject"%>
<%@ include file="/include/taglib.jspf" %>
<%@ page import="java.util.*"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>


<div id="profilePageActivityHistoryResponseData" data-percent-complete=".85">
    <div class="tabbable" id="profilePageActivityHistoryActiveTabPageTabSet">
	    <ul class="nav nav-tabs" id="profilePageActivityHistoryTabSelectedTabs">
	        <li class="active">
	            <a data-toggle="tab" href="#productclaimOpen">
	                <c:if test="${ ! empty activityHistoryForm.promotionTypeCode }"> 
	                    <span><cms:contentText key="${activityHistoryForm.promotionTypeCode}_submitted" code="activitycenter.history"/></span>
	                </c:if>
	                <c:if test="${ empty activityHistoryForm.promotionTypeCode }"> 
	                    <span><cms:contentText key="submitted" code="activitycenter.history"/></span>
	               </c:if>	                            	   
	            </a>
	        </li>
	        <li>
                <a data-toggle="tab" href="#productclaimClosed">
                    <c:if test="${ ! empty activityHistoryForm.promotionTypeCode }"> 
                        <span><cms:contentText key="${activityHistoryForm.promotionTypeCode}_won" code="activitycenter.history"/></span>
                    </c:if>
                    <c:if test="${ empty activityHistoryForm.promotionTypeCode }"> 
                        <span><cms:contentText key="won" code="activitycenter.history"/></span>
                    </c:if>	                            		 
                </a>
            </li>
	    </ul>
        <div class="tab-content">
         <c:if test = "${tabClicked == 'open' or tabClicked == 'both'}">
         
			<%@ include file="profilePageProductClaimResponseOpen.jsp"%>
			
		</c:if>
		<c:if test = "${tabClicked == 'closed' or tabClicked == 'both'}">
		
			<%@ include file="profilePageProductClaimResponseClosed.jsp" %>
			
		</c:if>
        </div>
       
        </div>
    </div>
</div>