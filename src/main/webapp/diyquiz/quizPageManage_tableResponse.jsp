<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>
<%@page import="com.biperf.core.service.system.SystemVariableService"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.biperf.core.utils.BeanLocator"%>
<%@page import="com.biperf.core.domain.quiz.Quiz"%>
<%@page import="com.biperf.core.ui.diyquiz.DIYQuizForm"%>

<!-- START QUIZZES -->
<%
  Map<String,Object> quizParamMap = new HashMap<String,Object>();
  Quiz tempQuiz;
%>

<div class="row-fluid">
  <div class="span12">
	<display:table name="quizzes" id="quizzes" class="table table-striped crud-table" pagesize="10"
			requestURI="<%=RequestUtils.getOriginalRequestURI( request )%>">

			<display:setProperty name="basic.msg.empty_list_row">
				<tr class="crud-content" align="left">
					<td colspan="{0}"><cms:contentText key="NOTHING_FOUND"
							code="system.errors" /></td>
				</tr>
			</display:setProperty>

			<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>

			<display:column titleKey="quiz.diy.form.EDIT" class="editColumn">
				<%
				  String siteUrl = ( (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME ) ).getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();

				  tempQuiz = (Quiz)pageContext.getAttribute( "quizzes" );
				  quizParamMap.put( "quizId", tempQuiz.getId() );
				  quizParamMap.put( "source", "pax" );
				  pageContext.setAttribute( "viewUrl", ClientStateUtils.generateEncodedLink( siteUrl, "/promotion/diyQuizMaintain.do?method=prepareUpdate", quizParamMap ) );
				%>
				<c:url var="url" value="${viewUrl}" />
				 <a href="<c:out value="${url}" />">
                    <i class="icon-pencil2" />
                </a>
			</display:column>

			<display:column titleKey="quiz.manage.library.QUIZ" class="sortable nameColumn unsorted" >
					<c:out value="${quizzes.name}" />
			</display:column>

			<display:column titleKey="quiz.manage.library.NO_OF_ATTEMPTS" class="sortable attemptsColumn unsorted">
				<c:if test="${quizzes.allowUnlimitedAttempts}">
					<cms:contentText key="UNLIMITED_ATTEMPTS" code="claims.quiz.submission" />
				</c:if>
				<c:if test="${not quizzes.allowUnlimitedAttempts}">
					<c:out value="${quizzes.maximumAttempts}" />
				</c:if>
			</display:column>

			<display:column titleKey="quiz.manage.library.QUIZ_START_DATE" class="sortable startDateColumn unsorted">
				<c:out value="${quizzes.displayStartDate}" />
			</display:column>

			<display:column titleKey="quiz.manage.library.QUIZ_END_DATE" class="sortable endDateColumn unsorted">
				<c:out value="${quizzes.displayEndDate}" />
			</display:column>

			<display:column titleKey="quiz.manage.library.STATUS" class="sortable statusColumn unsorted">
				<c:out value="${quizzes.quizStatus}" />
			</display:column>

			<display:column titleKey="quiz.manage.library.MAKE_A_COPY" class="sortable copyColumn unsorted">
				<%
				  String siteUrl = ( (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME ) ).getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();

				  tempQuiz = (Quiz)pageContext.getAttribute( "quizzes" );
				  quizParamMap.put( "quizId", tempQuiz.getId() );
				  quizParamMap.put( "source", "pax" );
				  pageContext.setAttribute( "viewUrl", ClientStateUtils.generateEncodedLink( siteUrl, "/promotion/diyQuizMaintain.do?method=prepareCopy", quizParamMap ) );
				%>
				<c:url var="url" value="${viewUrl}" />
				 <a href="<c:out value="${url}" />">
                    <i class="icon-file-copy" />
                </a>
			</display:column>

		</display:table>
	</div>
</div>
<%-- END QUIZZES --%>

<table width="100%">
	<tr>
		<td align="left"><span class="export displayTableExports"></span></td>
	</tr>
</table>
