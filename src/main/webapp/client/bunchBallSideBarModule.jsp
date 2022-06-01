<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

<!-- ======== BUNCH BALL SIDEBAR MODULE ======== -->
<script type="text/template" id="bunchBallSideBarModuleTpl">

<h3 class="section-header"><cms:contentText key="SIDE_MODULE_HEADER" code="coke.bunchball"/></h3>
<div class="module-liner">          
    <div class="missionWrapper">
	    <div class="missionLogo">
	    	<img src="<%=RequestUtils.getBaseURI(request)%><cms:contentText key="SIDE_MODULE_IMG_URL_TXT" code="coke.bunchball"/>" align="left">
	    </div>
	    <div class="missionText">
	    	<cms:contentText key="SIDE_MODULE_TEXT" code="coke.bunchball"/>

	    </div>	    			    
	</div>
	<a href="<%=RequestUtils.getBaseURI(request)%>/homePage.do#launch/missions"><cms:contentText key="SIDE_MODULE_LINK_TEXT" code="coke.bunchball"/></a>
</div>
</script>