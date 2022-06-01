<%--  UI Refactored --%>
<%@ include file="/include/taglib.jspf"%>

<SCRIPT LANGUAGE="JavaScript" type="text/javascript">	
		 	if (!useRedirect) 
		 	{// if dynamic embedding is turned on
 				if(hasRightVersion)  // if we've detected an acceptable version
		        {
					var objectTags = '<c:out value="${flashRequestString}" escapeXml="false"/>'
					document.write(objectTags);
				} 
			}
</script>	