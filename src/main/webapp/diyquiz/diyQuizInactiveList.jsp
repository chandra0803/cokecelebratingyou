<%@page import="com.biperf.core.ui.diyquiz.DIYQuizForm"%>
<%@ include file="/include/taglib.jspf"%>
<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.biperf.core.utils.BeanLocator"%>
<%@page import="com.biperf.core.service.system.SystemVariableService"%>
<%@page import="java.util.Map"%>
<%@page import="com.biperf.core.domain.quiz.Quiz"%>

<script type="text/javascript">	
function callUrl(urlToCall) {
	window.location = urlToCall;
}

$(document).ready(function(){
	 $("#messageDiv").hide();
});
</script>

<%
  String siteUrl = ( (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME ) ).getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();  
  DIYQuizForm diyQuizForm = (DIYQuizForm)request.getAttribute( "diyQuizForm" );
  Map<String,Object> createNewQuiz = new HashMap<String,Object>();
  createNewQuiz.put( "source", "admin" );
  createNewQuiz.put( "promotionId", diyQuizForm.getPromotionId(  ));
  pageContext.setAttribute( "backUrl", ClientStateUtils.generateEncodedLink( siteUrl, "/promotion/diyQuizList.do?method=display", createNewQuiz ) );
%>

<html:form styleId="contentForm" action="diyQuizMaintain">
	<html:hidden property="method" value="displayList" />
	<div id="messageDiv" class="error"></div>

	<table>
		<tr>
			<td><span class="headline">
				<cms:contentText key="DIY_QUIZ_HEADER" code="quiz.diy.form" />
				</span>
			</td>
		</tr>
		
		<!-- START INACTIVE QUIZZES -->
		<%
		  Map<String,Object> inactiveQuizParamMap = new HashMap<String,Object>();
		  Quiz inactiveTempQuiz;
		%>
		<%-- <tr class="form-row-spacer">
			<td colspan="2" class="subheadline" align="left"><cms:contentText
					key="INACTIVE_QUIZ_HEADER" code="quiz.diy.form" />
			</td>
		</tr>--%>

		<tr class="form-row-spacer">
			<td colspan="2">
				<display:table name="inactiveQuizzes" id="inactiveQuizzes" sort="list" requestURI="<%=RequestUtils.getOriginalRequestURI( request )%>">
					<display:setProperty name="basic.msg.empty_list_row">
						<tr class="crud-content" align="left">
							<td colspan="{0}">
								<cms:contentText key="NOTHING_FOUND" code="system.errors" />
							</td>
						</tr>
					</display:setProperty>
					<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>

					<display:column titleKey="quiz.diy.form.QUIZ_NAME"
						headerClass="crud-table-header-row"
						class="crud-content left-align top-align nowrap" sortable="true"
						sortProperty="name">
						<%
							inactiveTempQuiz = (Quiz)pageContext.getAttribute( "inactiveQuizzes" );
							inactiveQuizParamMap.put( "quizId", inactiveTempQuiz.getId() );
							inactiveQuizParamMap.put( "source", "admin" );
						    pageContext.setAttribute( "viewUrl", ClientStateUtils.generateEncodedLink( siteUrl, "/promotion/diyQuizMaintain.do?method=prepareUpdate", inactiveQuizParamMap ) );
						%>
						<c:url var="url" value="${viewUrl}" />
						<a href="<c:out value="${url}" />" class="crud-content-link">
							<c:out value="${inactiveQuizzes.name}" />
						</a>
					</display:column>
					
					<display:column titleKey="quiz.diy.form.FIRST_NAME"
						headerClass="crud-table-header-row"
						class="crud-content left-align top-align nowrap" sortable="true"
						sortProperty="name">
						<c:out value="${inactiveQuizzes.owner.firstName}" />
					</display:column>
					
					<display:column titleKey="quiz.diy.form.LAST_NAME"
						headerClass="crud-table-header-row"
						class="crud-content left-align top-align nowrap" sortable="true"
						sortProperty="name">
						<c:out value="${inactiveQuizzes.owner.lastName}" />
					</display:column>

					<display:column titleKey="quiz.diy.form.START_DATE"
						headerClass="crud-table-header-row"
						class="crud-content left-align top-align nowrap" sortable="true"
						sortProperty="name">
						<c:out value="${inactiveQuizzes.displayStartDate}" />
					</display:column>

					<display:column titleKey="quiz.diy.form.END_DATE"
						headerClass="crud-table-header-row"
						class="crud-content left-align top-align nowrap" sortable="true"
						sortProperty="name">
						<c:out value="${inactiveQuizzes.displayEndDate}" />
					</display:column>
					
					<display:column titleKey="quiz.diy.form.LAST_UPDATED"
						headerClass="crud-table-header-row"
						class="crud-content left-align top-align nowrap" sortable="true">
						<c:out value="${inactiveQuizzes.displayLastUpdatedDate}" />
					</display:column>

				</display:table>
			</td>
		</tr>
		<%-- END INACTIVE QUIZZES --%>
		
		<%--BUTTON ROWS --%>
		<tr class="form-buttonrow">
			<td>
				<table>
					<tr>
						<td colspan="2" align="center">
							<html:button property="back" styleClass="content-buttonstyle" onclick="callUrl('${backUrl}')">
								<cms:contentText key="BACK_TO_DIY_QUIZ" code="quiz.diy.form" />
							</html:button>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</html:form>