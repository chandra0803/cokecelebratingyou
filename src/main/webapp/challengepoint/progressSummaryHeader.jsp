<%-- UI REFACTORED --%>

<%@ page import="com.biperf.core.utils.ClientStateUtils"%>
<%@ include file="/include/taglib.jspf"%>
   
<script type="text/javascript">
function openItemDetail(itemCopy, itemName, detailImageUrl)
{
    popUpWin('<%=request.getContextPath()%>/challengepoint/itemDetail.do?itemName=' + escape(itemName) + '&imageUrl=' + detailImageUrl + '&itemCopy=' + itemCopy , 'console', 400, 400, false, true);
}    
</script>

     
	<div id="content-top-cp">
	<div class="">
	<h2><span><cms:contentText key="HEADER" code="promotion.challengepoint.progress.summary"/></span></h2>
	<h3><c:out value="${cpPaxBean.promotion.name}"/></h3>
	<div id="left">
	<cms:contentText code="promotion.challengepoint.progress.summary" key="HEADER_INSTRUCTION"/>
	</div> <%-- end left div --%>
	
	<div id="right">	
	</div><%-- end right div --%>
	
	<div class="clear"></div> <%-- end clear --%>
	
	</div>
	</div> <%-- end content-top div --%>



