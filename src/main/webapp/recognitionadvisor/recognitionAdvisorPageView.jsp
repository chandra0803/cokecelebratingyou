<%@ include file="/include/taglib.jspf"%>
<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<!-- ======== Recognition Advisor: Recognition Advisor Page ======== -->

<div id="recognitionAdvisorPageView" class="raPageWrapper page-content">
	<div id="rec-advisor"></div>
</div>

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
		<%
			pageContext.setAttribute("baseURI", RequestUtils.getBaseURI(request));
		%>
	window.recognitionAdvisor = {
			raUrl: 'ra/reminders.action',
			raEligibleProgramsUrl: 'ra/eligiblePrograms.action',
			raTilePageDisplay: 'no',
			raDetailPageDisplay: 'yes',
			baseURI: '${baseURI}',
			raEndModelPageDisplay: 'no',
			content: [
	            {
	                key: "RA_RECOGNITION_ADVISOR",
	                code: "recognition.content.model.info",
	                content: "<cms:contentText key="RA_RECOGNITION_ADVISOR" code="recognition.content.model.info" escapeJavascript="true" />"
	            },
	            {
	                key: "MY_TEAM",
	                code: "ssi_contest.creator",
	                content: "<cms:contentText key="MY_TEAM" code="ssi_contest.creator" escapeJavascript="true" />"
	            },
				{
	                key: "FILTER_BY",
	                code: "recognition.content.model.info",
	                content: "<cms:contentText key="FILTER_BY" code="recognition.content.model.info" escapeJavascript="true" />"
	            },
				{
	                key: "ALL",
	                code: "recognition.content.model.info",
	                content: "<cms:contentText key="ALL" code="recognition.content.model.info" escapeJavascript="true" />"
	            },
				{
	                key: "RA_NEW",
	                code: "recognition.content.model.info",
	                content: "<cms:contentText key="RA_NEW" code="recognition.content.model.info" escapeJavascript="true" />"
	            },
				{
	                key: "RA_OVERDUE",
	                code: "recognition.content.model.info",
	                content: "<cms:contentText key="RA_OVERDUE" code="recognition.content.model.info" escapeJavascript="true" />"
	            },
	            {
	            	key: "RA_UPCOMING",
	            	code: "recognition.content.model.info",
	            	content: "<cms:contentText key="RA_UPCOMING" code="recognition.content.model.info" escapeJavascript="true" />"
	            },
				{
	                key: "RA_STATUS",
	                code: "recognition.content.model.info",
	                content: "<cms:contentText key="RA_STATUS" code="recognition.content.model.info" escapeJavascript="true" />"
	            },
				{
	                key: "RA_EMPLOYEE",
	                code: "recognition.content.model.info",
	                content: "<cms:contentText key="RA_EMPLOYEE" code="recognition.content.model.info" escapeJavascript="true" />"
	            },
				{
	                key: "RA_BY_ME",
	                code: "recognition.content.model.info",
	                content: "<cms:contentText key="RA_BY_ME" code="recognition.content.model.info" escapeJavascript="true" />"
	            },				
				{
	                key: "RA_DAYS_OVER_DUE",
	                code: "recognition.content.model.info",
	                content: "<cms:contentText key="RA_DAYS_OVER_DUE" code="recognition.content.model.info" escapeJavascript="true" />"
	            },
				{
	                key: "RA_RECOGNITION_ADVISOR",
	                code: "recognition.content.model.info",
	                content: "<cms:contentText key="RA_RECOGNITION_ADVISOR" code="recognition.content.model.info" escapeJavascript="true" />"
	            },
				{
	                key: "DAYS",
	                code: "ssi_contest.participant",
	                content: "<cms:contentText key="DAYS" code="ssi_contest.participant" escapeJavascript="true" />"
	            },
				{
	                key: "RA_POINTS",
	                code: "recognition.content.model.info",
	                content: "<cms:contentText key="RA_POINTS" code="recognition.content.model.info" escapeJavascript="true" />"
	            },
				{
	                key: "RA_DAYS",
	                code: "recognition.content.model.info",
	                content: "<cms:contentText key="RA_DAYS" code="recognition.content.model.info" escapeJavascript="true" />"
	            },				
				{
	                key: "RA_RECOGNIZE",
	                code: "recognition.content.model.info",
	                content: "<cms:contentText key="RA_RECOGNIZE" code="recognition.content.model.info" escapeJavascript="true" />"
	            },
				{
	                key: "DETAILS",
	                code: "claims.product.approval",
	                content: "<cms:contentText key="DETAILS" code="claims.product.approval" escapeJavascript="true" />"
	            },
	            {
	                key: "RA_AGO",
	                code: "recognition.content.model.info",
	                content: "<cms:contentText key="RA_AGO" code="recognition.content.model.info" escapeJavascript="true" />"
	            },
	            {
	                key: "RA_STARTED",
	                code: "recognition.content.model.info",
	                content: "<cms:contentText key="RA_STARTED" code="recognition.content.model.info" escapeJavascript="true" />"
	            },
	            {
	                key: "RA_CAUGHT",
	                code: "recognition.content.model.info",
	                content: "<cms:contentText key="RA_CAUGHT" code="recognition.content.model.info" escapeJavascript="true" />"
	            },
	            {
	            	key: "RA_PROGRAMS_DISPLAY",
	            	code: "recognition.content.model.info",
	            	content: "<cms:contentText key="RA_PROGRAMS_DISPLAY" code="recognition.content.model.info" escapeJavascript="true" />"
            	},
            	{
            		key: "RA_PROGRAMS_HEADING",
            		code: "recognition.content.model.info",
            		content: "<cms:contentText key="RA_PROGRAMS_HEADING" code="recognition.content.model.info" escapeJavascript="true" />"
           		},
           		{
           			key: "RA_PENDING",
           			code: "recognition.content.model.info",
           			content: "<cms:contentText key="RA_PENDING" code="recognition.content.model.info" escapeJavascript="true" />"
           		},
           		{
           			key: "RA_PENDING_MESSAGE",
           			code: "recognition.content.model.info",
           			content: "<cms:contentText key="RA_PENDING_MESSAGE" code="recognition.content.model.info" escapeJavascript="true" />"
     			},
     			{
           			key: "RA_BY_OTHERS",
           			code: "recognition.content.model.info",
           			content: "<cms:contentText key="RA_BY_OTHERS" code="recognition.content.model.info" escapeJavascript="true" />"
     			},
     			{
           			key: "RA_EMPLOYEES",
           			code: "recognition.content.model.info",
           			content: "<cms:contentText key="RA_EMPLOYEES" code="recognition.content.model.info" escapeJavascript="true" />"
     			},
     			{
           			key: "RA_POINT",
           			code: "recognition.content.model.info",
           			content: "<cms:contentText key="RA_POINT" code="recognition.content.model.info" escapeJavascript="true" />"
     			},
     			{
           			key: "RA_DAY",
           			code: "recognition.content.model.info",
           			content: "<cms:contentText key="RA_DAY" code="recognition.content.model.info" escapeJavascript="true" />"
     			},
     			{
           			key: "RA_NEW_EMPLOYEE",
           			code: "recognition.content.model.info",
           			content: "<cms:contentText key="RA_NEW_EMPLOYEE" code="recognition.content.model.info" escapeJavascript="true" />"
     			},
     			{
           			key: "RA_EMP_BETTER_PERFORM_CONTENT",
           			code: "recognition.content.model.info",
           			content: "<cms:contentText key="RA_EMP_BETTER_PERFORM_CONTENT" code="recognition.content.model.info" escapeJavascript="true" />"
     			},
     			{
           			key: "RA_INSIGHTS",
           			code: "recognition.content.model.info",
           			content: "<cms:contentText key="RA_INSIGHTS" code="recognition.content.model.info" escapeJavascript="true" />"
     			},
     			{
           			key: "ALL",
           			code: "system.general",
           			content: "<cms:contentText key="ALL" code="system.general" escapeJavascript="true" />"
     			},     			
     			{
           			key: "OF",
           			code: "system.general",
           			content: "<cms:contentText key="OF" code="system.general" escapeJavascript="true" />"
     			},
     			{
           			key: "RA_PROGRAMS_DISPLAY",
           			code: "recognition.content.model.info",
           			content: "<cms:contentText key="RA_PROGRAMS_DISPLAY" code="recognition.content.model.info" escapeJavascript="true" />"
     			},
     			{
           			key: "RA_PROGRAMS_HEADING",
           			code: "recognition.content.model.info",
           			content: "<cms:contentText key="RA_PROGRAMS_HEADING" code="recognition.content.model.info" escapeJavascript="true" />"
     			},
     			{
           			key: "RA_TODAY",
           			code: "recognition.content.model.info",
           			content: "<cms:contentText key="RA_TODAY" code="recognition.content.model.info" escapeJavascript="true" />"
     			}

	            ]
	}

	$(document).ready(function(){
		
		G5.props.URL_JSON_EZ_RECOGNITION_MEMBER_INFO = G5.props.URL_ROOT+'/recognitionWizard/memberInfo.do';
		
		//Mini Profile PopUp Follow Unfollow Pax JSON
        G5.props.URL_JSON_PARTICIPANT_FOLLOW = G5.props.URL_ROOT+'publicRecognitionFollowUnFollowPax.do?method=followUnFollowPax';
	    //attach the view to an existing DOM element
	    var rapv = new RecognitionAdvisorPageView({
	    	el:$('#recognitionAdvisorPageView'),
	        pageNav : {
	            back : {
	                text : 'Back',
	                url : 'layout.html'
	            },
	            home : {
	                text : 'Home',
	                url : 'layout.html?tplPath=base/tpl/&amp;tpl=modulesPage.html'
	            }
	        },
	        pageTitle : 'Recognition Advisor'
	    });  
	    window.renderAdvisor();
	});
	

	
</script>
<%@include file="/submitrecognition/easy/flipSide.jsp"%>