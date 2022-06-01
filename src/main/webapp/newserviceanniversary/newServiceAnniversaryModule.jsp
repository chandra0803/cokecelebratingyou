<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<script type="text/template" id="newServiceAnniversaryModuleTpl">
<div class="module-liner">    
    <div id="sa-app"></div>
</div>
<script>
window.serviceAnniversary = {
		content: [
            {
                key: "CELEBRATION_TOMORROW",
                code: "serviceanniversary.content",
                content: "<cms:contentText key="CELEBRATION_TOMORROW" code="serviceanniversary.content" escapeJavascript="true" />"
            },
            {
                key: "NO_CELEBRATIONS",
                code: "serviceanniversary.content",
                content: "<cms:contentText key="NO_CELEBRATIONS" code="serviceanniversary.content" escapeJavascript="true" />"
            },
            {
                key: "SHARE_A_MEMORY",
                code: "serviceanniversary.content",
                content: "<cms:contentText key="SHARE_A_MEMORY" code="serviceanniversary.content" escapeJavascript="true" />"
            },
            {
                key: "UPCOMING_PURLS",
                code: "purl.celebration.module",
                content: "<cms:contentText key="UPCOMING_PURLS" code="purl.celebration.module" escapeJavascript="true" />"
            },
            {
                key: "SEE_MORE",
                code: "serviceanniversary.content",
                content: "<cms:contentText key="SEE_MORE" code="serviceanniversary.content" escapeJavascript="true" />"
            },
            {
                key: "PROBLEM_LOADING_CELEBRATION",
                code: "serviceanniversary.content",
                content: "<cms:contentText key="PROBLEM_LOADING_CELEBRATION" code="serviceanniversary.content" escapeJavascript="true" />"
            },
            {
                key: "CELEBRATION_FOR",
                code: "serviceanniversary.content",
                content: "<cms:contentText key="CELEBRATION_FOR" code="serviceanniversary.content" escapeJavascript="true" />"
            },
            {
                key: "FAILED_TO_LOAD",
                code: "serviceanniversary.content",
                content: "<cms:contentText key="FAILED_TO_LOAD" code="serviceanniversary.content" escapeJavascript="true" />"
            },
            {
                key: "VIEW_PAST_PURLS",
                code: "purl.celebration.module",
                content: "<cms:contentText key="VIEW_PAST_PURLS" code="purl.celebration.module" escapeJavascript="true" />"
            }          
            
            ]
}
$(document).ready(function() {    
    //attach the view to an existing DOM element
	window.initializeSA();

});
</script>
</script>
