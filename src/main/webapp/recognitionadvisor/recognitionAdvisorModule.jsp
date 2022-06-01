<%@ include file="/include/taglib.jspf"%>
<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<!-- RA Tile Page Body -->

<div class="module-liner">
    <div class="module-content">
		<div id="rec-advisor"></div>
	</div>
</div>

<script>
	<%
		pageContext.setAttribute("baseURI", RequestUtils.getBaseURI(request));
	%>
	window.recognitionAdvisor = {
    	raUrl: 'ra/reminders.action',
		raTilePageDisplay: 'yes',
		raDetailPageDisplay: 'no',
		baseURI: '${baseURI}',
		raEndModelPageDisplay: 'no',
		content: [
            {
                key: "RA_RECOGNITION_ADVISOR",
                code: "recognition.content.model.info",
                content: "<cms:contentText key="RA_RECOGNITION_ADVISOR" code="recognition.content.model.info" escapeJavascript="true" />"
            },
            {
                key: "RA_NEW",
                code: "recognition.content.model.info",
                content: "<cms:contentText key="RA_NEW" code="recognition.content.model.info" escapeJavascript="true" />"
            },
            {
                key: "RA_RECOGNIZE",
                code: "recognition.content.model.info",
                content: "<cms:contentText key="RA_RECOGNIZE" code="recognition.content.model.info" escapeJavascript="true" />"
            },
            {
                key: "RA_OVERDUE",
                code: "recognition.content.model.info",
                content: "<cms:contentText key="RA_OVERDUE" code="recognition.content.model.info" escapeJavascript="true" />"
            },
            {
                key: "RA_NEW_EMPLOYEE",
                code: "recognition.content.model.info",
                content: "<cms:contentText key="RA_NEW_EMPLOYEE" code="recognition.content.model.info" escapeJavascript="true" />"
            },
            {
                key: "VALID_VALS",
                code: "admin.characteristic.list",
                content: "<cms:contentText key="VALID_VALS" code="admin.characteristic.list" escapeJavascript="true" />"
            },
            {
            	key: "RA_DETAILS",
            	code: "recognition.content.model.info",
            	content: "<cms:contentText key="RA_DETAILS" code="recognition.content.model.info" escapeJavascript="true" />"
            },
            {
            	key: "RA_UPCOMING",
            	code: "recognition.content.model.info",
            	content: "<cms:contentText key="RA_UPCOMING" code="recognition.content.model.info" escapeJavascript="true" />"
            },
            {
                key: "RA_DAYS_OVER_DUE",
                code: "recognition.content.model.info",
                content: "<cms:contentText key="RA_DAYS_OVER_DUE" code="recognition.content.model.info" escapeJavascript="true" />"
            },
            {
                key: "RA_CAUGHT",
                code: "recognition.content.model.info",
                content: "<cms:contentText key="RA_CAUGHT" code="recognition.content.model.info" escapeJavascript="true" />"
            }
            
            ]
}
$(document).ready(function() {    
	//attach the view to an existing DOM element
	window.renderAdvisor();

});
</script>