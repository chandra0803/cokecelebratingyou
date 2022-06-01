<!DOCTYPE html>
<%@ include file="/include/taglib.jspf" %>

<div id="main" class="content adminHome"> <!-- /.homeContent BEGIN-->       

  <ul>
  	<beacon:authorize ifAnyGranted="BI_ADMIN">
	<li><a href="<%=request.getContextPath()%>/participant/audienceListDisplay.do"><cms:contentText code="home.navmenu.admin" key="AUDIENCE"/></a></li>
	</beacon:authorize>
	
	<beacon:authorize ifAnyGranted="BI_ADMIN">
	<li><a href="<%=request.getContextPath()%>/admin/displayImportFileList.do"><cms:contentText code="home.navmenu.admin" key="FILE_LOADS"/></a></li>
	</beacon:authorize>
  
  	<beacon:authorize ifAnyGranted="BI_ADMIN">
	<li><a href="<%=request.getContextPath()%>/admin/systemVariableListDisplay.do"><cms:contentText code="home.navmenu.admin" key="SYSTEM_VARIABLES"/></a></li>
	</beacon:authorize>
	
	<c:if test="${beacon:systemVarBoolean('install.badges')}">
	<beacon:authorize ifAnyGranted="BI_ADMIN">
	<li><a href="<%=request.getContextPath()%>/promotion/badgeList.do"><cms:contentText code="home.navmenu.admin" key="VIEW_BADGE_LIST"/></a></li>
	</beacon:authorize>
	</c:if>
	
	<beacon:authorize ifAnyGranted="BI_ADMIN">
	<li><a href="<%=request.getContextPath()%>/admin/budgetMasterListDisplay.do"><cms:contentText code="home.navmenu.admin" key="VIEW_BUDGET_MASTER_LIST"/></a></li>
	</beacon:authorize>
	
	<beacon:authorize ifAnyGranted="BI_ADMIN">
	<li><a href="<%=request.getContextPath()%>/promotion/promotionListDisplay.do"><cms:contentText code="home.navmenu.admin" key="VIEW_PROMO_LIST"/></a></li>
	</beacon:authorize>
	
  </ul>
  
</div><!-- /.homeContent END-->       


