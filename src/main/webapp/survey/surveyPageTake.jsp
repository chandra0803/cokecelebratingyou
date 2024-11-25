<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@page import="com.biperf.core.utils.ClientStatePasswordManager"%>
<%@page import="com.biperf.core.utils.ClientStateSerializer"%>
<%@page import="java.util.Map"%>
<%@ include file="/include/taglib.jspf"%>

<!-- ======== SURVEY TAKE PAGE ======== -->

<div id="surveyPageTakeView" class="page-content"><div class="row-fluid">

    <!-- DIV.row-fluid up next to wrapping div.page-content for IE7 -->
        <div class="span12" id="surveyTakeView">

            <!-- content generated by surveyPageTakeTpl -->

        </div><!-- /.span12 -->
    </div><!-- /.row-fluid -->
</div>

<!-- overrides for the JSON variables (these are set in settings.js but provided here for convenience in creating a JSP) -->
<!--script>
// G5.props.URL_JSON_SURVEY_PAGE_TAKE_SURVEY = G5.props.URL_ROOT+'ajax/surveyPageTakeSurvey.json';
// G5.props.URL_JSON_SURVEY_PAGE_SAVE_SURVEY = G5.props.URL_ROOT+'ajax/surveyPageSaveSurvey.json';
// G5.props.URL_JSON_SURVEY_PAGE_SUBMIT_SURVEY = G5.props.URL_ROOT+'ajax/surveyPageSubmitSurvey.json';
</script-->

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
    $(document).ready(function() {

    	<%
	  	  Long promotionId = null;
	  	  Long surveyId = null;
	  	  String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
        String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
        String password = ClientStatePasswordManager.getPassword();
    	  if ( cryptoPass != null && cryptoPass.equals( "1" ) )
    	  {
        	password = ClientStatePasswordManager.getGlobalPassword();
    	  }

       // Deserialize the client state.
       Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
       promotionId = (Long)clientStateMap.get( "promotionId" );
       if(clientStateMap.get( "id" )!=null){
         surveyId = (Long)clientStateMap.get( "id" );
       }
       pageContext.setAttribute("id",surveyId);
       pageContext.setAttribute("promotionId",promotionId);
		%>

    	 G5.props.URL_JSON_SURVEY_PAGE_TAKE_SURVEY = G5.props.URL_ROOT+'surveyPageTakeSurvey.do?method=displayInfo&promotionId=${promotionId}&surveyId=${surveyId}';
    	 G5.props.URL_JSON_SURVEY_PAGE_SAVE_SURVEY = G5.props.URL_ROOT+'surveyPageSaveSurvey.do?method=saveForLater&promotionId=${promotionId}&surveyId=${surveyId}';
    	 G5.props.URL_JSON_SURVEY_PAGE_SUBMIT_SURVEY = G5.props.URL_ROOT+'surveyPageSaveSurvey.do?method=submit&promotionId=${promotionId}&surveyId=${surveyId}';

        // if desired, the surveyJson object from core/base/ajax/surveyPageTakeSurvey.json can be embedded here
        var surveyJson;

        //attach the view to an existing DOM element
        G5.views.SurveyPageTake = new SurveyPageTakeView({
            el:$('#surveyPageTakeView'),
            pageNav : {
                back : {
                    text : '<cms:contentText key="BACK" code="system.button" />',
                    url : '<%= RequestUtils.getBaseURI(request)%>/surveyPage.do'
                },
                home : {
                    text : '<cms:contentText key="HOME" code="system.general" />',
                    url : '<%= RequestUtils.getBaseURI(request)%>/homePage.do'
                }
            },

            pageTitle : '<cms:contentText key="SURVEY_TITLE" code="survey.take" />',
            surveyJson : surveyJson,
            // options to pass to the SurveyModel
            modelOpts: {},
            // I have been doing some experimenting with passing CM keys as JSON instead of embedding within the JSPs, but haven't perfected the technique yet
            // That said, I want to try this out on this particular page
            cm : {
                btn : {
                    done : "<cms:contentText key="DONE" code="survey.take" />",
                    submit : "<cms:contentText key="SUBMIT_SURVEY" code="survey.take" />",
                    save : "<cms:contentText key="SAVE_FOR_LATER" code="survey.take" />"
                }
            }
        });

    });
</script>
<!-- NOTE: the following includes were added to make this shell behave like the JSP -->
<script type="text/template" id="surveyTakeTplTpl">
    <%@include file="/survey/surveyTakeTpl.jsp" %>
</script>
