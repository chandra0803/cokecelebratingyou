<%@ include file="/include/taglib.jspf"%>

<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.ui.utils.PresentationUtils"%>
<%@ page import="com.biperf.core.value.DailyTipValueBean" %>

	<!-- ======== TIP MODULE ======== -->
<script type="text/template" id="tipModuleTpl">
<div class="module-liner">

    <div class="module-content">

    	<div id="tipContainer">

        <h3><cms:contentText key="DAILYTIP" code="home.rail"/></h3>
       	<div id="tipText"></div>

       </div>

    </div>

</div>
</script>
