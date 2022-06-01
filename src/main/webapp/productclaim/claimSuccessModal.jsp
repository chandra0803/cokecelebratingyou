<%@ page import="java.util.*"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.utils.DateUtils"%>
<%@ page import="com.biperf.core.ui.productclaim.ClaimSubmittedBean"%>

<!-- AutoModal (if more than one: first one will be shown - error will be logged) -->
<div class="modal hide fade autoModal">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal"><i class="icon-close"></i></button>
		<h3><cms:contentText key="TITLE" code="claims.submission.confirmation"/></h3>
	</div>
	<div class="modal-body">
		<p><cms:contentText key="CLAIM_NBR" code="claims.submission.confirmation"/>: <c:out value="${claimSubmittedBean.claimNumber}"/></p>
		<p><cms:contentText key="CLAIM_DATE" code="claims.submission.confirmation"/>: <fmt:formatDate value="${claimSubmittedBean.submittedDate}" pattern="${JstlDatePattern}" /></p>

	</div>
	<div class="modal-footer">
	  <div class="actions tc">
		<%
		  ClaimSubmittedBean temp = (ClaimSubmittedBean) request.getAttribute("claimSubmittedBean");
	      Map paramMap = new HashMap();
	      paramMap.put("claimId", temp.getClaimId());
	      pageContext.setAttribute("detailUrl", ClientStateUtils.generateEncodedLink("", "claim/productClaimDetail.do?method=display", paramMap));
	    %>
		<a href="<c:out value='${detailUrl}'/>" class="btn btn-primary btn-fullmobile"><cms:contentText key="PRINT" code="system.button"/></a>
	    <%
	      ClaimSubmittedBean temp2 = (ClaimSubmittedBean) request.getAttribute("claimSubmittedBean");
	      Map paramMapSent = new HashMap();
	      paramMapSent.put("promotionId", temp2.getPromotionId());
	      pageContext.setAttribute("submitClaimUrl", ClientStateUtils.generateEncodedLink("", "claim/startClaimSubmission.do?method=showClaim", paramMapSent ));
	  	%>
		<a href="<c:out value='${submitClaimUrl}'/>" class="btn btn-primary btn-fullmobile"><cms:contentText key="SUBMIT_ANOTHER" code="claims.submission.confirmation"/></a>
	  </div>
	</div>
</div>
