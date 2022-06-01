<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.domain.enums.ActivityType"%>
<%@ page import="com.biperf.core.value.ActivityCenterValueBean"%>

<div id="approvalsNoEmailView" class="approvalSearchWrapper page-content">	
	<div>
		<div class="row-fluid">
	    	<div class="span12">
	    		<cms:contentText code="recognition.approval.printondemand" key="NO_EMAIL_TEXT_LIST" />
				<table class="table table-striped">
					<thead>
						<tr>
							<th><cms:contentText code="recognition.approval.printondemand" key="PROMOTION" /></th>
							<th><cms:contentText code="recognition.approval.printondemand" key="RECIPIENT" /></th>
							<th><cms:contentText code="recognition.approval.printondemand" key="ACTION" /></th>				
						</tr>
					</thead>
					<c:forEach var="noEmailBean" items="${noEmailList}">						
						<tr> 
							<td><c:out value="${noEmailBean.promotionName}" /></td> 
							<td>
								<c:out value="${noEmailBean.recipientDisplayName}"/>
								<img src="${pageContext.request.contextPath}/assets/img/flags/${noEmailBean.recipientCountryCode}.png">,
								<c:out value="${noEmailBean.recipientNodeName}"/>,
								<c:out value="${noEmailBean.recipientDepartment}"/>,
								<c:out value="${noEmailBean.recipientJobPosition}"/>
							</td> 
							<td>
								<c:set var="claimId" value="${noEmailBean.claimId}"/>
								<%
									Map<String,Object> parameterMap = new HashMap<String,Object>();
									parameterMap.put( "claimId", pageContext.getAttribute("claimId") );
									pageContext.setAttribute("publicRecognitionPageDetailUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), "/claim/claimDetail.do", parameterMap ) ); 
								%>
								<a href="${publicRecognitionPageDetailUrl}" class="btn approvalsReviewBtn btn-primary" target="_blank"><cms:contentText code="recognition.approval.printondemand" key="VIEW_CLAIM_DETAIL" /></a>
							</td>
						</tr>
					</c:forEach>
				</table>
			</div>
		</div>
	</div>
</div>


<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
$(document).ready(function() {
	
	
	//attach the view to an existing DOM element
    var aimlp = new PageView({
    	el:$('#approvalsNoEmailView'),
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
        pageTitle : '<cms:contentText code="recognition.approval.printondemand" key="TITLE" />'
    });
	
});
</script>