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
        <c:out value="${promotionRulesForm.promotionName}" />&nbsp;<cms:contentText key="TITLE" code="promotion.promotionrules"/>
      </h2>
    </div>
    
	<div class="guts">              
	  		
	   <div style="margin:10px;" id="programRules">
	      <c:if test="${promotionRulesForm.method == 'modalFriendly'}">
	 	  <cms:contentText code="${promotionRulesForm.cmsCode}" key="${promotionRulesForm.cmsKey}"/>
	      </c:if>
	   </div>
	   
	  </div>
</div><!-- /.module -->
</div><!-- /.column1 -->