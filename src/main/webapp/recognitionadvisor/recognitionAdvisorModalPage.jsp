<%@ include file="/include/taglib.jspf"%>
<!-- ======== Recognition Advisor: Recognition End Modal Page ======== -->

	<div id="rec-advisor">
	</div>

<script>	
	
	window.recognitionAdvisor = {
			raUrl: 'ra/reminders.action',
			raClaimDetail: '${raClaimUrl}',
			raTilePageDisplay: 'no',
			raDetailPageDisplay: 'no',
			raEndModelPageDisplay: 'yes',
			baseURI: '${baseURI}',
			badgeDetailsCount: ${badgeDetailsCount}, 
			raRecognitionSentBean: ${recognitionBadgesSent},			
			content: [
	            {
	                key: "THANK_YOU",
	                code: "recognition.confirmation",
	                content: "<cms:contentText key="THANK_YOU" code="recognition.confirmation" escapeJavascript="true" />"
	            },
	            {
	                key: "SUBMITTED_MESSAGE",
	                code: "recognition.confirmation",
	                content: "<cms:contentText key="SUBMITTED_MESSAGE" code="recognition.confirmation" escapeJavascript="true" />"
	            },
	            {
	                key: "PRINT_COPIES",
	                code: "recognition.confirmation",
	                content: "<cms:contentText key="PRINT_COPIES" code="recognition.confirmation" escapeJavascript="true" />"
	            },
	            {
	                key: "OK",
	                code: "system.button",
	                content: "<cms:contentText key="OK" code="system.button" escapeJavascript="true" />"
	            },
	            {
	                key: "RA_MODAL_BODY_HEADER",
	                code: "recognition.content.model.info",
	                content: "<cms:contentText key="RA_MODAL_BODY_HEADER" code="recognition.content.model.info" escapeJavascript="true" />"
	            },
	            {
	                key: "RA_MODAL_BODY_FOOTER",
	                code: "recognition.content.model.info",
	                content: "<cms:contentText key="RA_MODAL_BODY_FOOTER" code="recognition.content.model.info" escapeJavascript="true" />"
	            },
	            {
	                key: "RA_NEW_EMPLOYEE",
	                code: "recognition.content.model.info",
	                content: "<cms:contentText key="RA_NEW_EMPLOYEE" code="recognition.content.model.info" escapeJavascript="true" />"
	            },
	            {
	                key: "RA_OVER_DUE_EMPLOYEE",
	                code: "recognition.content.model.info",
	                content: "<cms:contentText key="RA_OVER_DUE_EMPLOYEE" code="recognition.content.model.info" escapeJavascript="true" />"
	            },
	            {
	                key: "RA_RECOGNIZE",
	                code: "recognition.content.model.info",
	                content: "<cms:contentText key="RA_RECOGNIZE" code="recognition.content.model.info" escapeJavascript="true" />"
	            },
	            {
	            	key: "RA_DAYS_OVER_DUE",
	            	code: "recognition.content.model.info",
	            	content: "<cms:contentText key="RA_DAYS_OVER_DUE" code="recognition.content.model.info" escapeJavascript="true" />"
	            },
	            {
	            	key: "EARNED_DATE",
	            	code: "gamification.admin.labels",
	            	content: "<cms:contentText key="EARNED_DATE" code="gamification.admin.labels" escapeJavascript="true" />"
	            }

	        ]
	
	}

	$(document).ready(function(){
	    //Initiate The RA Modal Window	    
      window.renderAdvisor();
	});

</script>
