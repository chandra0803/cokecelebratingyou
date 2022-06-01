<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.ui.promotion.PromotionRulesForm"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>
<style>
<!--
.guts b,
.guts strong {
	font-weight: bold;
}
.guts i,
.guts em {
	font-style: italic;
}
-->
</style>
<div class="column1">
<div class="module">
    <div class="topper">
      <h2>
        &nbsp;<cms:contentText key="TITLE" code="promotion.promotionrules"/>
      </h2>
    </div>
    
	<div class="guts">              
	  		
	   <div style="margin:10px;" id="programRulesList">
	      <c:if test="${promotionRulesForm.method == 'footerDisplay'}">
	      <c:choose>
	      <c:when test="${empty promotionRulesForm.promotionRulesList}" >
	      		<c:out value="No promotions to view Rule"/>
	      </c:when>
	      <c:otherwise>
				<c:forEach items="${promotionRulesForm.promotionRulesList}" var="promoRules">
					<c:out value="${promoRules.name}" /><br />
				</c:forEach>
		  </c:otherwise>
		  </c:choose>
	      </c:if>
	   </div>
	   
	  </div>
	  
</div><!-- /.module -->
</div><!-- /.column1 -->