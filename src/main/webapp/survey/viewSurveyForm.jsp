<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.promotion.SurveyQuestion" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.ui.survey.ViewSurveyForm"%>
<%@ page import="com.biperf.core.ui.servlet.*"%>
<%@ include file="/include/taglib.jspf"%>
<%--UI REFACTORED--%>
<link rel="stylesheet" href="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/css/quizadmin.css" type="text/css">
<script src="<%=RequestUtils.getBaseURI(request)%>/assets/libs/video-js/video.min.js"></script>
<script>_V_.options.flash.swf ="<%=RequestUtils.getBaseURI(request)%>/assets/rsrc/video-js.swf"</script>

<script type="text/javascript">
	function reorderElement( url, newQuestionSequenceNum )
	{
		var whereToGo = url + "&newQuestionSequenceNum=" + newQuestionSequenceNum;
		window.location = whereToGo;
	}
	
	function validateQuestionAndSubmit()
	{
		var checked = $("input[name=deleteIds]:checked").length > 0;   
		var errorMessage='';
		if(checked)
		{
				$("#messageDiv").hide();
				setDispatchAndSubmit('remove');
				return true;
		}
		else
		{
			errorMessage='<cms:contentText key="SELECT_ATLEAST_ONE_QUESTION" code="survey.question" />';
			$("#messageDiv").html(errorMessage);
	    	$("#messageDiv").show();
			return false;
		}
	}
	
	
	$(document).ready(function(){
	   
	    $("#messageDiv").hide();
	    
	});
</script>

<html:form styleId="contentForm" action="surveyFormView">

<div id="messageDiv" class="error">
	
	</div>
  <html:hidden property="method" />

  <input type="hidden" name="surveyFormName" value="<c:out value='${viewSurveyForm.surveyFormName}' />" />  
  <input type="hidden" name="newQuestionSequenceNum" value="<c:out value='${viewSurveyForm.newQuestionSequenceNum}' />" /> 
  <input type="hidden" name="description" value="<c:out value='${viewSurveyForm.description}' escapeXml='true'/>"/>
  
	<beacon:client-state>
		<beacon:client-state-entry name="surveyQuestionId" value="${viewSurveyForm.surveyQuestionId}"/>
		<beacon:client-state-entry name="surveyFormId" value="${viewSurveyForm.surveyFormId}"/>
		<beacon:client-state-entry name="surveyFormName" value="${viewSurveyForm.surveyFormName}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
		<tr>
			<td>
        <span class="headline"><cms:contentText key="VIEW_TITLE" code="survey.form"/></span>
        <span id="quicklink-add"></span><script type="text/javascript">quicklink_display_add('<cms:contentText key="VIEW_TITLE" code="survey.form"/>'); </script>	  
        
        <%--INSTRUCTIONS--%>
        <br/>
        <span class="content-instruction">
          <cms:contentText key="VIEW_INSTRUCTIONS" code="survey.form"/>
        </span>
        <br/>
        <%--END INSTRUCTIONS--%>
     	
        <cms:errors/>
        
        <table>
					<tr class="form-row-spacer">				  
            <beacon:label property="surveyFormName">
              <cms:contentText key="NAME" code="survey.form"/>
            </beacon:label>	
            <td class="content-field-review">
              <c:out value="${viewSurveyForm.surveyFormName}"/>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<a class="crud-content-link" href="javascript: setActionDispatchAndSubmit('surveyFormDisplay.do', 'display');">
              	<cms:contentText key="EDIT_LINK" code="survey.form.library"/>
              </a>			
            </td>					
					</tr>

          <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>

					<tr class="form-row-spacer">				  
            <beacon:label property="description" styleClass="content-field-label-top">
              <cms:contentText key="DESCRIPTION" code="survey.form"/>
            </beacon:label>	
            <td class="content-field-review">
              <c:if test="${viewSurveyForm.description != null}">
              <%
              	ViewSurveyForm viewSurveyForm = (ViewSurveyForm) request.getAttribute("viewSurveyForm");
              	String description = StripXSSUtil.stripXSS(viewSurveyForm.getDescription());
              %>
                <c:out value="<%=description%>" escapeXml="false"/>
          	  </c:if>	
            </td>					
		 	 		</tr>	 

          <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>	        
          
					<%-- <tr class="form-row-spacer">			
            <beacon:label property="surveyType">
              <cms:contentText key="DISPLAY_METHOD" code="survey.form"/>
            </beacon:label>	        
            <td class="content-field-review">
              <c:out value="${viewSurveyForm.surveyType}"/>			
            </td>
          </tr> --%>
        </table>
        <table>
          <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>
					<tr class="form-row-spacer">				  
						<td colspan="2">
			        <table width="100%">
      			    <tr>
            			<td align="right">
              			<c:set var="rowNum" value="0"/>
						        <%	Map parameterMap = new HashMap();
												SurveyQuestion temp;
										%>
				<c:set var="viewSurveyForm_questions" value="${viewSurveyForm.questions}" scope="request"/>
			        			<display:table name="viewSurveyForm_questions" id="question" style="width: 100%">
				        			<display:setProperty name="basic.msg.empty_list">
					        			<tr class="crud-content" align="left"><td colspan="{0}">
                          <cms:contentText key="NO_ELEMENTS" code="survey.form"/>
                        </td></tr>
				        			</display:setProperty>
				        			<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
        		        	<display:column titleKey="survey.form.question.list.QUESTION" headerClass="crud-table-header-row" class="crud-content left-align" sortable="false" sortProperty="questionText" >
												<%	temp = (SurveyQuestion)pageContext.getAttribute( "question" );
														parameterMap.put( "surveyQuestionId", temp.getId() );
														parameterMap.put( "surveyFormId", temp.getSurvey().getId() );
														pageContext.setAttribute("editUrl", ClientStateUtils.generateEncodedLink( "", "surveyQuestionView.do?method=display", parameterMap ) );
													    String questionText = StripXSSUtil.stripXSS(temp.getQuestionText());
												%>
        		        		<a href="<c:out value="${editUrl}"/>" class="crud-content-link">
								 <c:out escapeXml='false' value="<%=questionText%>"/>
								</a>
        		        	</display:column>
        		        	<display:column property="statusType.name" titleKey="survey.form.question.list.STATUS" headerClass="crud-table-header-row" class="crud-content center-align" sortable="false"/>
        		        	<display:column titleKey="quiz.form.question.list.ORDER" class="crud-content center-align" headerClass="crud-table-header-row">
        		        		<c:out value="${rowNum + 1}"/>
        		        	</display:column>			
        		          <beacon:authorize ifNotGranted="LOGIN_AS">
        		          <display:column titleKey="survey.form.question.list.REORDER" class="crud-content left-align nowrap" headerClass="crud-table-header-row">
        		        	<table border="0" cellpadding="3" cellspacing="1">
        		        		<tr>
        		        			<td width="10">
        		        			<%	pageContext.setAttribute("reorderUrl", ClientStateUtils.generateEncodedLink( "", "surveyFormView.do?method=reorder", parameterMap ) ); %>
        		        			<c:if test="${rowNum != 0}">
        		        				<a href="<c:out value="${reorderUrl}"/>&newQuestionSequenceNum=<c:out value="${rowNum-1}"/>"><img src="<%= RequestUtils.getBaseURI(request) %>/assets/g4skin/images/order-arrowup.gif" border="0"/></a>
        		        			</c:if>
        		        			</td>
        		        			<td width="10">
        		        			<c:if test="${rowNum != viewSurveyForm.questionsSize - 1}">
        		        				<a href="<c:out value="${reorderUrl}"/>&newQuestionSequenceNum=<c:out value="${rowNum+1}"/>"><img src="<%= RequestUtils.getBaseURI(request) %>/assets/g4skin/images/order-arrowdown.gif" border="0"/></a>
        		        			</c:if>
        		        			</td>
        		        			<td>
        		        				<%--This is not on the actual struts form because it is just a placeholder for the value passed to the javescript function--%>
        		        				<input type="text" name="newQuestionSequenceNum<c:out value="${rowNum}"/>" size="2" maxlength="3" />
        		        			</td>
        		        			<td>
        		        				<input type="button" onclick="reorderElement('<c:out value="${reorderUrl}"/>',newQuestionSequenceNum<c:out value="${rowNum}"/>.value - 1);" name="reorderbutton" class="content-buttonstyle" value="<cms:contentText key="REORDER" code="survey.form.question.list"/>" />
        		        			</td>
        		        		</tr>
        		        	</table>
        		          </display:column>
        		          <display:column titleKey="survey.form.question.list.REMOVE" style="width: 75px" class="crud-content center-align" headerClass="crud-table-header-row">
        		            <html:checkbox property="deleteIds" value="${question.id}" />
        		          </display:column>
        		          </beacon:authorize>
        		        	<c:set var="rowNum" value="${rowNum + 1}"/>
        		    		</display:table>
            			</td>
								</tr>

						<tr class="form-buttonrow">
									<td>      			      
										<table width="100%">
                			<tr>
                			
                			
                			
                  			<td align="left">
            			  			<beacon:authorize ifNotGranted="LOGIN_AS">
            			  			<html:button property="Add" styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('surveyQuestionDisplay.do', 'display');">
										<cms:contentText key="ADD_QUESTION" code="survey.form"/>
									</html:button>
									</beacon:authorize>
            						</td>
            						<td width="25%" align="right">
						  						<c:if test="${not empty viewSurveyForm.questions}">
													<beacon:authorize ifNotGranted="LOGIN_AS">
													<html:button property="Remove" styleClass="content-buttonstyle" onclick="return validateQuestionAndSubmit()">
							  							<cms:contentText key="REMOVE_SELECTED" code="system.button"/>
						  							</html:button>
						  							</beacon:authorize>
												</c:if>
					  						</td>
          						</tr>
										</table>
									</td>
								</tr>
            	</table>
						</td>
					</tr>

					<tr class="form-buttonrow">
					<td></td>
						<td align="center" >
             	<html:button property="cancelBtn" styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('surveyFormListDisplay.do','')">
               	<cms:contentText code="survey.form" key="BACK_TO_SURVEY_LIBRARY" />
             	</html:button>

             	<beacon:authorize ifNotGranted="LOGIN_AS">
             	<html:submit styleClass="content-buttonstyle" onclick="setDispatch('prepareCopy')">
               	<cms:contentText code="survey.form" key="COPY_SURVEY" />
             	</html:submit>
             	</beacon:authorize>
							
							<c:if test="${not empty viewSurveyForm.questions && viewSurveyForm.status == 'undrconstr'}">
                <beacon:authorize ifNotGranted="LOGIN_AS">
	             	<html:submit styleClass="content-buttonstyle" onclick="setActionAndDispatch('surveyFormMarkComplete.do', 'markComplete')">
  	             	<cms:contentText code="system.button" key="MARK_COMPLETE" />
    	         	</html:submit>
                </beacon:authorize>
							</c:if>
           	</td>
         	</tr>
				</table>
			</td>
		</tr>        
	</table>
</html:form>