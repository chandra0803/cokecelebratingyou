<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.domain.enums.ActivityType"%>
<%@ page import="com.biperf.core.value.SurveyPageValueBean"%>
<%@ page import="com.biperf.core.utils.NumberFormatUtil" %>

<!-- ======== SURVEY LIST PAGE ======== -->

<% 	Map paramMap = null;
    SurveyPageValueBean surveyValueBean = null;
	Long awardAmount = null;
	Long attemptsRemaining = null;
%>

<div id="surveyPageListView" class="page-content">

    <div class="row-fluid">
        <div class="span12">
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th><cms:contentText key="SURVEY" code="survey.list"/></th>
                        <th><cms:contentText key="STATUS" code="survey.list"/></th>
                        <th><cms:contentText key="TIME_REMAINING" code="survey.list"/></th>
                    </tr>
                </thead>
                <tbody>
                <c:forEach var="surveyValueBean" items="${surveySubmissionList}" varStatus="surveyStatus">
                	  <%
	     				paramMap = new HashMap();
                	    surveyValueBean = (SurveyPageValueBean)pageContext.getAttribute("surveyValueBean");
	     				if( surveyValueBean != null && surveyValueBean.getPromotion() != null && surveyValueBean.getPromotion().getId() != null )
	     				{
	       				  paramMap.put( "promotionId", surveyValueBean.getPromotion().getId() );
	       				  paramMap.put( "surveyId", surveyValueBean.getSurveyId(  ) );
	       				  pageContext.setAttribute("surveySubmitUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), "/takeSurvey.do?method=display", paramMap ) );
	       				}
	     			  %>
                      <tr>
                        <td>
                          <c:choose>
							<c:when test="${surveyValueBean.takeSurvey}">
								<beacon:authorize ifNotGranted="LOGIN_AS">
									<a href="<c:out value='${surveySubmitUrl}'/>"><c:out value="${surveyValueBean.promotion.name}"/></a>
								</beacon:authorize>
								<beacon:authorize ifAnyGranted="LOGIN_AS">
									<c:out value="${surveyValueBean.promotion.name}"/>
								</beacon:authorize>
							</c:when>
							<c:when test="${surveyValueBean.resumeSurvey}">
								<beacon:authorize ifNotGranted="LOGIN_AS">
									<a href="<c:out value='${surveySubmitUrl}'/>"><c:out value="${surveyValueBean.promotion.name}"/></a>
								</beacon:authorize>
							</c:when>
						</c:choose>
                        </td>
                        <td>
                          <c:choose>
                          	<c:when test="${surveyValueBean.takeSurvey}">
                          	  <beacon:authorize ifNotGranted="LOGIN_AS">
                          	  	<a href="<c:out value='${surveySubmitUrl}'/>"><cms:contentText key="NOT_STARTED" code="survey.list"/></a>
                          	  </beacon:authorize>
                          	  <beacon:authorize ifAnyGranted="LOGIN_AS">
                          	  	<cms:contentText key="NOT_STARTED" code="survey.list"/>
                          	  </beacon:authorize>
                          	</c:when>
                           	<c:when test="${surveyValueBean.resumeSurvey}">
                          	  <cms:contentText key="INCOMPLETE" code="survey.list"/> <beacon:authorize ifNotGranted="LOGIN_AS"><a href="<c:out value='${surveySubmitUrl}'/>"><cms:contentText key="RESUME_SURVEY" code="survey.list"/></a></beacon:authorize>
                          	</c:when>
                          </c:choose>
                        </td>
                        <td>
                          <c:choose>
                            <c:when test="${surveyValueBean.timeRemaining > 0 and surveyValueBean.timeRemaining < 2}">
                              <c:out value="${surveyValueBean.timeRemaining}"/> <cms:contentText key="DAY" code="survey.list"/>
                            </c:when>
                            <c:when test="${surveyValueBean.timeRemaining >= 2}">
                              <c:out value="${surveyValueBean.timeRemaining}"/> <cms:contentText key="DAYS" code="survey.list"/>
                            </c:when>
                            <c:otherwise>
                              <cms:contentText key="NOT_AVAILABLE" code="system.general"/>
                            </c:otherwise>
                          </c:choose>
                        </td>
                    </tr>
                  </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
    $(document).ready(function() {

        //attach the view to an existing DOM element
        var prp = new SurveyPageListView({
            el:$('#surveyPageListView'),
            pageNav : {
                back : {
                    text : '<cms:contentText key="BACK" code="system.button" />',
                    url : '<%= RequestUtils.getBaseURI(request)%>/participantProfilePage.do#profile/AlertsAndMessages'
                },
                home : {
                    text : '<cms:contentText key="HOME" code="system.general" />',
                    url : '<%= RequestUtils.getBaseURI(request)%>/homePage.do'
                }
            },
            pageTitle : '<cms:contentText key="TITLE" code="survey.list"/>'
        });

    });
</script>
