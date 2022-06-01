<%@ include file="/include/taglib.jspf" %>
<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@page import="org.apache.commons.lang3.StringUtils"%>

<!-- ======== ENGAGEMENT PAGE RECOGNIZED ======== -->

<div id="engagementPageRecognized" class="engagement engagementPage engagementPageRecognized page-content"><div class="row-fluid">

    <!-- DIV.ROW up next to wrapping div.page-content for IE7 -->
        <div class="span12">

		<% 
		String recognizedCountFormatted = request.getParameter( "recognizedCountFormatted" );
        if(  StringUtils.isNotBlank( recognizedCountFormatted ) && (recognizedCountFormatted.startsWith( "${" ) || recognizedCountFormatted.startsWith( "#{" )) )
        {
          recognizedCountFormatted = "0";
        }
		%>
          <h4 class="subhead">
	          <c:set var="type" value='<%= request.getParameter( "type" )%>'/>
	          <c:choose>
	          	 <c:when test="${ recognizedCountFormatted == '1' }">
		          <c:choose>
					  <c:when test="${ type eq 'paxRecTo' }">
					  	<cms:contentTemplateText code="engagement.participant" key="YOUR_TEAM_HAS_RECOGNIZED_PERSON" args='<%=recognizedCountFormatted%>'/>
					  </c:when>
					  <c:otherwise>
					  	<cms:contentTemplateText code="engagement.participant" key="YOUR_TEAM_HAS_BEEN_RECOGNIZED_PERSON" args='<%=recognizedCountFormatted%>'/>
					  </c:otherwise>
				  </c:choose>
	          	 </c:when>
	          	 <c:otherwise>
		          <c:choose>
					  <c:when test="${ type eq 'paxRecTo' }">
					  	<cms:contentTemplateText code="engagement.participant" key="YOUR_TEAM_HAS_RECOGNIZED" args='<%=recognizedCountFormatted%>'/>
					  </c:when>
					  <c:otherwise>
					  	<cms:contentTemplateText code="engagement.participant" key="YOUR_TEAM_HAS_BEEN_RECOGNIZED" args='<%=recognizedCountFormatted%>'/>
					  </c:otherwise>
				  </c:choose>
	          	 </c:otherwise>
	          </c:choose>
          </h4>

            <div class="engagementRecognizedModelView"></div><!-- /.engagementRecognizedModelView -->

        </div><!-- /.span12 -->
    </div><!-- /.row-fluid -->
</div>

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
    $(document).ready(function() {
        // overrides for the JSON variables (these are set in settings.js but provided here for convenience in creating a JSP)
        G5.props.URL_JSON_ENGAGEMENT_RECOGNIZED_COLLECTION = G5.props.URL_ROOT+'engagement/engagementDisplay.do?method=fetchEngagementRecognized';

        G5.props.URL_JSON_PARTICIPANT_INFO = "${pageContext.request.contextPath}/participantPublicProfile.do?method=populatePax";
        G5.props.URL_JSON_EZ_RECOGNITION_MEMBER_INFO = "${pageContext.request.contextPath}/recognitionWizard/memberInfo.do";
        G5.props.URL_JSON_EZ_RECOGNITION_SEND_EZRECOGNITION = "${pageContext.request.contextPath}/recognitionWizard/submitEasyRecognition.do";

        //Mini Profile PopUp Follow Unfollow Pax JSON
        G5.props.URL_JSON_PARTICIPANT_FOLLOW = G5.props.URL_ROOT+'publicRecognitionFollowUnFollowPax.do?method=followUnFollowPax';

        //attach the view to an existing DOM element
        G5.views.EngagementPageRecognized = new EngagementPageRecognizedView({
            el:$('#engagementPageRecognized'),
            pageNav : {
                back : {
                    text : '<cms:contentText key="BACK" code="system.button" />',
                    url : 'javascript:history.go(-1);'
                },
                home : {
                    text : '<cms:contentText key="HOME" code="system.general" />',
                    url : '<%= RequestUtils.getBaseURI(request)%>/homePage.do'
                }
            },
            pageTitle : '<c:choose> <c:when test="${ type eq 'paxRecTo' }"> <cms:contentText key="TEAM_RECOGNIZED" code="engagement.participant"/> </c:when> <c:otherwise> <cms:contentText key="TEAM_RECOGNIZED_BY" code="engagement.participant"/> </c:otherwise> </c:choose>',

            /*
             * NOTE: the following attributes are required for the data model to initialize properly
             *
             * mode can be team/user
             * type can be paxRecBy/paxRecTo
             * userId and nodeId only apply to their corresponding modes
             * modelUrl is the URL to the JSON for the recognized tree. it will use the JSON variable commented out above if nothing is passed
             */
            mode : "<%= request.getParameter( "mode" )%>",
            type : "<%= request.getParameter( "type" )%>",
            userId : "<%= request.getParameter( "userId" )%>",
            nodeId : "<%= request.getParameter( "nodeId" )%>",
            /*
             * NOTE: the following attributes are optional for loading the recognized tree JSON
             *
             * modelUrl is the URL to the JSON. it will use the JSON variable commented out above if nothing is passed
             * modelParams is a JSON object of additional request parameters to be sent to the URL
             */
            modelUrl : "<%= request.getParameter( "modelUrl" )%>",
            modelParams : {
                timeframeType : "<%= request.getParameter( "timeframeType" )%>",
                timeframeMonthId : "<%= request.getParameter( "timeframeMonthId" )%>",
                timeframeYear : "<%= request.getParameter( "timeframeYear" )%>"
            }
        });

    });
</script>

<script type="text/template" id="engagementRecognizedTpl">
  <%@include file="/engagement/engagementRecognized.jsp" %>
</script>
<script type="text/template" id="participantPopoverViewTpl">
    <%@include file="/profileutil/participantPopoverView.jsp" %>
</script>
<script type="text/template" id="participantProfileViewTpl">
    <%@include file="/profile/participantProfileView.jsp" %>
</script>

<%@include file="/submitrecognition/easy/flipSide.jsp" %>
