<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.domain.enums.ActivityType"%>
<%@ page import="com.biperf.core.value.QuizPageValueBean"%>
<%@ page import="com.biperf.core.utils.NumberFormatUtil" %>

<% 	Map paramMap = null;
	QuizPageValueBean quizValueBean = null;
	Long awardAmount = null;
	Long attemptsRemaining = null;
%>

<!-- ======== QUIZ PAGE ======== -->


<div id="quizPageView" class="quizPage-liner page-content">

	<c:if test="${displayManageQuizzes}">
	<div class="row-fluid">
        <div class="span12">
        	<h2><cms:contentText key="QUIZ_TITLE" code="quiz.manage.library" /></h2>
            <p><a href="<%= RequestUtils.getBaseURI(request)%>/quiz/diyQuizManage.do?method=manage"><cms:contentText key="GO_TO_MANAGE_QUIZ" code="quiz.manage.library" /> &#187;</a></p>
        </div>
    </div>
    </c:if>

    <div class="row-fluid">
        <div class="span12">
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th><cms:contentText key="QUIZ" code="hometile.quiz"/></th>
                        <th><cms:contentText key="STATUS" code="hometile.quiz"/></th>
                        <th><cms:contentText key="ATTEMPTS" code="hometile.quiz"/></th>
                        <th><cms:contentText key="TIME_REMAINING" code="hometile.quiz"/></th>
                        <th><cms:contentText key="AWARD" code="hometile.quiz"/></th>
                    </tr>
                </thead>
                <tbody>
                  <c:forEach var="quizValueBean" items="${quizSubmissionList}" varStatus="quizStatus">
                	  <%
	     				paramMap = new HashMap();
                	  	quizValueBean = (QuizPageValueBean)pageContext.getAttribute("quizValueBean");
	     				if( quizValueBean != null && quizValueBean.getPromotion() != null && quizValueBean.getPromotion().getId() != null )
	     				{
	     				 	awardAmount = quizValueBean.getAwardAmount();
	     				 	attemptsRemaining = quizValueBean.getAttemptsRemaining();
	       					paramMap.put( "promotionId", quizValueBean.getPromotion().getId() );
	       					if(quizValueBean.getPromotion().isDIYQuizPromotion(  )){
	       						paramMap.put( "quizId", quizValueBean.getDiyQuizId(  ) );
	       					}
	       					if ( quizValueBean.getClaimId() > 0)
	       					{
	       						paramMap.put( "claimId", quizValueBean.getClaimId() );
	       					}
	       					pageContext.setAttribute("quizSubmitUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), "/quiz/quizPageTake.do", paramMap ) );
	       					pageContext.setAttribute("resumeQuizUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), "/quiz/quizPageTake.do", paramMap ) );
	       				}
	     			  %>
                    <tr>
							<td>
								<c:choose>
									<c:when test="${quizValueBean.promotion.promotionType.code eq 'diy_quiz'}">
										<c:choose>
											<c:when test="${quizValueBean.takeQuiz}">
												<beacon:authorize ifNotGranted="LOGIN_AS">
													<a href="<c:out value='${quizSubmitUrl}'/>"><c:out value="${quizValueBean.diyQuizName}"/></a>
												</beacon:authorize>
												<beacon:authorize ifAnyGranted="LOGIN_AS">
													<c:out value="${quizValueBean.promotion.name}"/>
												</beacon:authorize>
											</c:when>
											<c:when test="${quizValueBean.resumeQuiz}">
												<beacon:authorize ifNotGranted="LOGIN_AS">
													<a href="<c:out value='${resumeQuizUrl}'/>"><c:out value="${quizValueBean.diyQuizName}"/></a>
												</beacon:authorize>
											</c:when>
											<c:when test="${quizValueBean.retakeQuiz}">
												<beacon:authorize ifNotGranted="LOGIN_AS">
													<a href="<c:out value='${quizSubmitUrl}'/>"><c:out value="${quizValueBean.diyQuizName}"/></a>
												</beacon:authorize>
											</c:when>
										</c:choose>
									</c:when>
									<c:otherwise>
										<c:choose>
											<c:when test="${quizValueBean.takeQuiz}">
												<beacon:authorize ifNotGranted="LOGIN_AS">
													<a href="<c:out value='${quizSubmitUrl}'/>"><c:out value="${quizValueBean.promotion.name}"/></a>
												</beacon:authorize>
												<beacon:authorize ifAnyGranted="LOGIN_AS">
													<c:out value="${quizValueBean.promotion.name}"/>
												</beacon:authorize>
											</c:when>
											<c:when test="${quizValueBean.resumeQuiz}">
												<beacon:authorize ifNotGranted="LOGIN_AS">
													<a href="<c:out value='${resumeQuizUrl}'/>"><c:out value="${quizValueBean.promotion.name}"/></a>
												</beacon:authorize>
											</c:when>
											<c:when test="${quizValueBean.retakeQuiz}">
												<beacon:authorize ifNotGranted="LOGIN_AS">
													<a href="<c:out value='${quizSubmitUrl}'/>"><c:out value="${quizValueBean.promotion.name}"/></a>
												</beacon:authorize>
											</c:when>
										</c:choose>
									</c:otherwise>
								</c:choose>
							</td>
							<td>
                          <c:choose>
                          	<c:when test="${quizValueBean.takeQuiz}">
                          	  <beacon:authorize ifNotGranted="LOGIN_AS">
                          	  	<a href="<c:out value='${quizSubmitUrl}'/>"><cms:contentText key="NOT_STARTED" code="hometile.quiz"/></a>
                          	  </beacon:authorize>
                          	  <beacon:authorize ifAnyGranted="LOGIN_AS">
                          	  	<cms:contentText key="NOT_STARTED" code="hometile.quiz"/>
                          	  </beacon:authorize>
                          	</c:when>
                           	<c:when test="${quizValueBean.resumeQuiz}">
                          	  <cms:contentText key="INCOMPLETE" code="hometile.quiz"/> <beacon:authorize ifNotGranted="LOGIN_AS"><a href="<c:out value='${resumeQuizUrl}'/>"><cms:contentText key="RESUME_QUIZ" code="hometile.quiz"/></a></beacon:authorize>
                          	</c:when>
                          	<c:when test="${quizValueBean.retakeQuiz}">
                          	  <cms:contentText key="DID_NOT_PASS" code="hometile.quiz"/> <beacon:authorize ifNotGranted="LOGIN_AS"><a href="<c:out value='${quizSubmitUrl}'/>"><cms:contentText key="RETAKE_QUIZ" code="hometile.quiz"/></a></beacon:authorize>
                          	</c:when>
                          </c:choose>
                        </td>
                        <td>
                          <c:if test="${quizValueBean.attemptsRemaining ne null}">
                        	<c:out value="<%=NumberFormatUtil.getLocaleBasedNumberFormat(attemptsRemaining.longValue())%>"/>
                          </c:if>
                           <c:if test="${quizValueBean.attemptsRemaining eq null}">
                        	<cms:contentText key="UNLIMITED" code="hometile.quiz"/>
                          </c:if>
                        </td>
                        <td>
                          <c:if test="${quizValueBean.timeRemaining > 0 and quizValueBean.timeRemaining < 2}">
                        	<c:out value="${quizValueBean.timeRemaining}"/> <cms:contentText key="DAY" code="hometile.quiz"/>
                          </c:if>
                          <c:if test="${quizValueBean.timeRemaining >= 2}">
                        	<c:out value="${quizValueBean.timeRemaining}"/> <cms:contentText key="DAYS" code="hometile.quiz"/>
                          </c:if>
                        </td>
                        <td>
                          <c:if test="${quizValueBean.awardAmount ne null}">
                        	<c:out value="<%=NumberFormatUtil.getLocaleBasedNumberFormat(awardAmount.longValue())%>"/>
                          </c:if>
                          <c:if test="${quizValueBean.awardAmount eq null}"><cms:contentText key="NOT_AVAILABLE" code="system.general"/></c:if>
                        </td>
                    </tr>
                  </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <div class="row-fluid">
        <div class="span12">
            <p><a href="<%= RequestUtils.getBaseURI(request)%>/participantProfilePage.do#profile/ActivityHistory/promotionId=allQuizzes"><cms:contentText key="QUIZ_HISTORY" code="hometile.quiz"/> &#187;</a></p>
        </div>
    </div>
</div>

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
$(document).ready(function(){

    //attach the view to an existing DOM element
    var prp = new QuizPageView({
        el:$('#quizPageView'),
        pageTitle : '<cms:contentText key="TITLE" code="claims.quiz.submission"/>'
    });
});


</script>
