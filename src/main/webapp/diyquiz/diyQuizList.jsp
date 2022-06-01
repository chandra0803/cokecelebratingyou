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

function validateAndSubmit(quizType)
{
	var checked=false;
	if(quizType=='incomplete')
		checked = $("input[name=deleteQuizzesIncomplete]:checked").length > 0;  
	else if(quizType=='pending')
		checked = $("input[name=deleteQuizzesPending]:checked").length > 0;  
	else if(quizType=='active')
		checked = $("input[name=deleteQuizzesActive]:checked").length > 0;
		
	$("#messageDiv").hide();	
	var errorMessage='';
	var confirmationMessage='<cms:contentText key="CONFIRMATION_DELETE" code="quiz.diy.errors" />';
		
	if(checked)
	{
		if(confirm(confirmationMessage))
		{
			setActionDispatchAndSubmit('diyQuizRemove.do?method=prepareRemove&quizType='+quizType, 'prepareRemove');
			return true;
		}
		else
		{
			return false;
		}
	}
	else
	{
		if(quizType=='incomplete')
			errorMessage='<cms:contentText key="ATLEAST_ONE_UNDER_CONST" code="quiz.diy.errors"/>';
			//errorMessage='Please select atleast 1 under construction quiz to remove';  
		else if(quizType=='pending')
			errorMessage='<cms:contentText key="ATLEAST_ONE_PENDING" code="quiz.diy.errors"/>';
			//errorMessage='Please select atleast 1 live quiz to remove';  
		else if(quizType=='active')
			errorMessage='<cms:contentText key="ATLEAST_ONE_ACTIVE" code="quiz.diy.errors"/>';
			//errorMessage='Please select atleast 1 active quiz to remove';
		
		$("#messageDiv").html(errorMessage);
    	$("#messageDiv").show();
		return false;
	}
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
  createNewQuiz.put( "type", "create" );
  createNewQuiz.put( "promotionId", diyQuizForm.getPromotionId(  ));
  pageContext.setAttribute( "createUrl", ClientStateUtils.generateEncodedLink( siteUrl, "/promotion/diyQuizMaintain.do?method=prepareCreate", createNewQuiz ) );
  
  Map<String,Object> viewInactiveQuiz = new HashMap<String,Object>();
  viewInactiveQuiz.put( "source", "admin" );
  viewInactiveQuiz.put( "type", "viewInactive" );
  viewInactiveQuiz.put( "promotionId", diyQuizForm.getPromotionId(  ));
  pageContext.setAttribute( "veiwInactiveUrl", ClientStateUtils.generateEncodedLink( siteUrl, "/promotion/diyQuizMaintain.do?method=viewInactive", viewInactiveQuiz ) );
%>

<html:form styleId="contentForm" action="diyQuizMaintain">
	<html:hidden property="method" value="displayList" />
	<div id="messageDiv" class="error"></div>
	<cms:errors/>
	<c:if test="${hasPromotion ne null}">
	<table>
		<tr>
			<td><span class="headline">
				<cms:contentText key="DIY_QUIZ_LIST_HEADER" code="quiz.diy.form" />
				</span>
			</td>
		</tr>
		<beacon:authorize
			ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
			<tr class="form-row-spacer">
				<td align="left">
					<html:button property="quizAdd" styleClass="content-buttonstyle" onclick="callUrl('${createUrl}')">
						<cms:contentText key="CREATE_QUIZ" code="quiz.diy.form" />
					</html:button>
				</td>
			</tr>
		</beacon:authorize>
		
		<!-- START INCOMPLETE QUIZZES -->
		<%
		  Map<String,Object> incompleteQuizParamMap = new HashMap<String,Object>();
		  Quiz incompleteTempQuiz;
		%>
		<tr class="form-row-spacer">
			<td colspan="2" class="subheadline" align="left"><cms:contentText
					key="INCOMPLETE_QUIZZES_HEADER" code="quiz.diy.form" />
			</td>
		</tr>

		<tr class="form-row-spacer">
			<td colspan="2">
				<display:table name="incompleteQuizzes" id="incompleteQuizzes" sort="list" requestURI="<%=RequestUtils.getOriginalRequestURI( request )%>">
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
							incompleteTempQuiz = (Quiz)pageContext.getAttribute( "incompleteQuizzes" );
							incompleteQuizParamMap.put( "quizId", incompleteTempQuiz.getId() );
							incompleteQuizParamMap.put( "source", "admin" );
						    pageContext.setAttribute( "viewUrl", ClientStateUtils.generateEncodedLink( siteUrl, "/promotion/diyQuizMaintain.do?method=prepareUpdate", incompleteQuizParamMap ) );
						%>
						<c:url var="url" value="${viewUrl}" />
						<a href="<c:out value="${url}" />" class="crud-content-link">
							<c:out value="${incompleteQuizzes.name}" />
						</a>
					</display:column>
					
					<display:column titleKey="quiz.diy.form.FIRST_NAME"
						headerClass="crud-table-header-row"
						class="crud-content left-align top-align nowrap" sortable="true"
						sortProperty="name">
						<c:out value="${incompleteQuizzes.owner.firstName}" />
					</display:column>
					
					<display:column titleKey="quiz.diy.form.LAST_NAME"
						headerClass="crud-table-header-row"
						class="crud-content left-align top-align nowrap" sortable="true"
						sortProperty="name">
						<c:out value="${incompleteQuizzes.owner.lastName}" />
					</display:column>

					<display:column titleKey="quiz.diy.form.START_DATE"
						headerClass="crud-table-header-row"
						class="crud-content left-align top-align nowrap" sortable="true"
						sortProperty="name">
						<c:out value="${incompleteQuizzes.displayStartDate}" />
					</display:column>

					<display:column titleKey="quiz.diy.form.END_DATE"
						headerClass="crud-table-header-row"
						class="crud-content left-align top-align nowrap" sortable="true"
						sortProperty="name">
						<c:out value="${incompleteQuizzes.displayEndDate}" />
					</display:column>
					
					<display:column titleKey="quiz.diy.form.LAST_UPDATED"
						headerClass="crud-table-header-row"
						class="crud-content left-align top-align nowrap" sortable="true">
						<c:out value="${incompleteQuizzes.displayLastUpdatedDate}" />
					</display:column>

					<beacon:authorize
						ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
						<display:column class="crud-content left-align top-align nowrap"
							titleKey="system.general.CRUD_REMOVE_LABEL"
							headerClass="crud-table-header-row">
							<input type="checkbox" name="deleteQuizzesIncomplete" value="<c:out value="${incompleteQuizzes.id}"/>">
						</display:column>
					</beacon:authorize>
				</display:table>
			</td>
		</tr>
		<tr>
			<td></td>
			<td><html:submit styleClass="content-buttonstyle"
					onclick="return validateAndSubmit('incomplete')">
					<cms:contentText key="REMOVE_SELECTED" code="system.button" />
				</html:submit>
			</td>
		</tr>
		<%-- END INCOMPLETE QUIZZES --%>
		
		<!-- START PENDING QUIZZES -->
		<%
		  Map<String,Object> pendingQuizParamMap = new HashMap<String,Object>();
		  Quiz pendingTempQuiz;
		%>
		<tr class="form-row-spacer">
			<td colspan="2" class="subheadline" align="left"><cms:contentText
					key="PENDING_QUIZZES_HEADER" code="quiz.diy.form" />
			</td>
		</tr>

		<tr class="form-row-spacer">
			<td colspan="2">
				<display:table name="pendingQuizzes" id="pendingQuizzes" sort="list" requestURI="<%=RequestUtils.getOriginalRequestURI( request )%>">
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
							pendingTempQuiz = (Quiz)pageContext.getAttribute( "pendingQuizzes" );
							pendingQuizParamMap.put( "quizId", pendingTempQuiz.getId() );
							pendingQuizParamMap.put( "source", "admin" );
						    pageContext.setAttribute( "viewUrl", ClientStateUtils.generateEncodedLink( siteUrl, "/promotion/diyQuizMaintain.do?method=prepareUpdate", pendingQuizParamMap ) );
						%>
						<c:url var="url" value="${viewUrl}" />
						<a href="<c:out value="${url}" />" class="crud-content-link">
							<c:out value="${pendingQuizzes.name}" />
						</a>
					</display:column>
					
					<display:column titleKey="quiz.diy.form.FIRST_NAME"
						headerClass="crud-table-header-row"
						class="crud-content left-align top-align nowrap" sortable="true"
						sortProperty="name">
						<c:out value="${pendingQuizzes.owner.firstName}" />
					</display:column>
					
					<display:column titleKey="quiz.diy.form.LAST_NAME"
						headerClass="crud-table-header-row"
						class="crud-content left-align top-align nowrap" sortable="true"
						sortProperty="name">
						<c:out value="${pendingQuizzes.owner.lastName}" />
					</display:column>

					<display:column titleKey="quiz.diy.form.START_DATE"
						headerClass="crud-table-header-row"
						class="crud-content left-align top-align nowrap" sortable="true"
						sortProperty="name">
						<c:out value="${pendingQuizzes.displayStartDate}" />
					</display:column>

					<display:column titleKey="quiz.diy.form.END_DATE"
						headerClass="crud-table-header-row"
						class="crud-content left-align top-align nowrap" sortable="true"
						sortProperty="name">
						<c:out value="${pendingQuizzes.displayEndDate}" />
					</display:column>
					
					<display:column titleKey="quiz.diy.form.LAST_UPDATED"
						headerClass="crud-table-header-row"
						class="crud-content left-align top-align nowrap" sortable="true">
						<c:out value="${pendingQuizzes.displayLastUpdatedDate}" />
					</display:column>

					<beacon:authorize
						ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
						<display:column class="crud-content left-align top-align nowrap"
							titleKey="system.general.CRUD_REMOVE_LABEL"
							headerClass="crud-table-header-row">
							<input type="checkbox" name="deleteQuizzesPending" value="<c:out value="${pendingQuizzes.id}"/>">
						</display:column>
					</beacon:authorize>
				</display:table>
			</td>
		</tr>
		<tr>
			<td></td>
			<td><html:submit styleClass="content-buttonstyle"
					onclick="return validateAndSubmit('pending')">
					<cms:contentText key="REMOVE_SELECTED" code="system.button" />
				</html:submit>
			</td>
		</tr>
		<%-- END PENDING QUIZZES --%>

		<!-- START ACTIVE QUIZZES -->
		<%
		  Map<String,Object> activeQuizParamMap = new HashMap<String,Object>();
		  Quiz activeTempQuiz;
		%>
		<tr class="form-row-spacer">
			<td colspan="2" class="subheadline" align="left"><cms:contentText
					key="ACTIVE_QUIZZES_HEADER" code="quiz.diy.form" />
			</td>
		</tr>

		<tr class="form-row-spacer">
			<td colspan="2">
				<display:table name="activeQuizzes" id="activeQuizzes" sort="list" requestURI="<%=RequestUtils.getOriginalRequestURI( request )%>">
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
							activeTempQuiz = (Quiz)pageContext.getAttribute( "activeQuizzes" );
							activeQuizParamMap.put( "quizId", activeTempQuiz.getId() );
							activeQuizParamMap.put( "source", "admin" );
						    pageContext.setAttribute( "viewUrl", ClientStateUtils.generateEncodedLink( siteUrl, "/promotion/diyQuizMaintain.do?method=prepareUpdate", activeQuizParamMap ) );
						%>
						<c:url var="url" value="${viewUrl}" />
						<a href="<c:out value="${url}" />" class="crud-content-link">
							<c:out value="${activeQuizzes.name}" />
						</a>
					</display:column>
					
					<display:column titleKey="quiz.diy.form.FIRST_NAME"
						headerClass="crud-table-header-row"
						class="crud-content left-align top-align nowrap" sortable="true"
						sortProperty="name">
						<c:out value="${activeQuizzes.owner.firstName}" />
					</display:column>
					
					<display:column titleKey="quiz.diy.form.LAST_NAME"
						headerClass="crud-table-header-row"
						class="crud-content left-align top-align nowrap" sortable="true"
						sortProperty="name">
						<c:out value="${activeQuizzes.owner.lastName}" />
					</display:column>

					<display:column titleKey="quiz.diy.form.START_DATE"
						headerClass="crud-table-header-row"
						class="crud-content left-align top-align nowrap" sortable="true"
						sortProperty="name">
						<c:out value="${activeQuizzes.displayStartDate}" />
					</display:column>

					<display:column titleKey="quiz.diy.form.END_DATE"
						headerClass="crud-table-header-row"
						class="crud-content left-align top-align nowrap" sortable="true"
						sortProperty="name">
						<c:out value="${activeQuizzes.displayEndDate}" />
					</display:column>
					
					<display:column titleKey="quiz.diy.form.LAST_UPDATED"
						headerClass="crud-table-header-row"
						class="crud-content left-align top-align nowrap" sortable="true">
						<c:out value="${activeQuizzes.displayLastUpdatedDate}" />
					</display:column>

					<beacon:authorize
						ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
						<display:column class="crud-content left-align top-align nowrap"
							titleKey="system.general.CRUD_REMOVE_LABEL"
							headerClass="crud-table-header-row">
							<input type="checkbox" name="deleteQuizzesActive" value="<c:out value="${activeQuizzes.id}"/>">
						</display:column>
					</beacon:authorize>
				</display:table>
			</td>
		</tr>
		<tr>
			<td></td>
			<td><html:submit styleClass="content-buttonstyle"
					onclick="return validateAndSubmit('active')">
					<cms:contentText key="REMOVE_SELECTED" code="system.button" />
				</html:submit>
			</td>
		</tr>
		<%-- END ACTIVE QUIZZES --%>
		

		<%--BUTTON ROWS --%>
		<tr class="form-buttonrow">
			<td>
				<table>
					<tr>
						<td align="left">
							<html:button property="viewInactive" styleClass="content-buttonstyle" onclick="callUrl('${veiwInactiveUrl}')">
								<cms:contentText key="VIEW_INACTIVE_QUIZZES" code="quiz.diy.form" />
							</html:button>
						</td>
						<td><html:button property="cancelBtn" styleClass="content-buttonstyle" onclick="callUrl('../homePage.do')">
								<cms:contentText key="CANCEL" code="system.button" />
							</html:button>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</c:if>	
</html:form>