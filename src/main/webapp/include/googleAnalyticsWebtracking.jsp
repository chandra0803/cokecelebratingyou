<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf" %>

	
	
<c:if test="${googleAnalyticsAccount != 'none'}">

<script type="text/javascript">
	   var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
	   document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
	</script>

	<script type="text/javascript">
	  try 
	  {
	     var pageTracker = _gat._getTracker("<c:out value='${googleAnalyticsAccount}'/>");
	     <tiles:useAttribute name="trackingTitle" id="trackingTitle" classname="java.lang.String"/>
	   	 _gat._anonymizeIp();//security fix
	     pageTracker._setCustomVar(1, "Page", "<c:out value='${trackingTitle}'/>", 3) 
	     pageTracker._trackPageview();
	  } 
	  catch(err) 
	  {}
	  function recordOutboundLink(category, action) {
	      try { 
			var pageTracker=_gat._getTracker("<c:out value='${googleAnalyticsAccount}'/>");
			_gat._anonymizeIp();//security fix
			pageTracker._trackEvent(category, action); 
			 
			 }catch(err){}}
	   
</script>

</c:if>
<c:if test="${googleAnalyticsAccount == null  || googleAnalyticsAccount == 'none'||googleAnalyticsAccount == ' '}">
<script type="text/javascript">
	 
	  function recordOutboundLink(category, action) 
	  {
	    
			 return;
		} 
	   
</script>
</c:if>