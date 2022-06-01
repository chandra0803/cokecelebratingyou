<%@page import="com.biperf.core.domain.quiz.QuizLearningSlideDetails"%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.quiz.QuizQuestion" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.ui.quiz.ViewQuizFormForm"%>
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
			errorMessage='<cms:contentText key="SELECT_ATLEAST_ONE_QUESTION" code="quiz.question" />';
			$("#messageDiv").html(errorMessage);
	    	$("#messageDiv").show();
			return false;
		}
	}
	function validateAndSubmit()
	{
		var checked = $("input[name=deleteSlides]:checked").length > 0;   
		var errorMessage='';
		var confirmationMessage='<cms:contentText key="SLIDE_CONFIRMATION_MESSAGE" code="quiz.learningForm" />';
			
		if(checked)
		{
			if(confirm(confirmationMessage))
			{
				$("#messageDiv").hide();
				setActionDispatchAndSubmit('quizLearningObjectSubmit.do?method=removeSlides', 'removeSlides');
				return true;
			}
			else
			{
				$("#messageDiv").hide();
				return false;
			}
		}
		else
		{
			errorMessage='<cms:contentText key="SELECT_ATLEAST_ONE_SLIDE" code="quiz.learningForm" />';
			$("#messageDiv").html(errorMessage);
	    	$("#messageDiv").show();
			return false;
		}
		
	}
	
	$(document).ready(function(){
	   
	    $("#messageDiv").hide();
	    
	});
</script>

<html:form styleId="contentForm" action="quizFormView">

<div id="messageDiv" class="error">
	
	</div>
  <html:hidden property="method" />
  <html:hidden property="quizFormName" />
  <html:hidden property="description" />
  <html:hidden property="quizType" />
  <html:hidden property="newQuestionSequenceNum" />
  
	<beacon:client-state>
		<beacon:client-state-entry name="quizQuestionId" value="${viewQuizFormForm.quizQuestionId}"/>
		<beacon:client-state-entry name="quizFormId" value="${viewQuizFormForm.quizFormId}"/>
		<beacon:client-state-entry name="quizFormName" value="${viewQuizFormForm.quizFormName}"/>
		<beacon:client-state-entry name="numberToAsk" value="${viewQuizFormForm.numberToAsk}"/>
		<beacon:client-state-entry name="passingScore" value="${viewQuizFormForm.passingScore}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
		<tr>
			<td>
        <span class="headline"><cms:contentText key="VIEW_TITLE" code="quiz.form"/></span>
        <span id="quicklink-add"></span><script type="text/javascript">quicklink_display_add('<cms:contentText key="VIEW_TITLE" code="quiz.form"/>'); </script>	  
        
        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="VIEW_INSTRUCTIONS" code="quiz.form"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
     	
        <cms:errors/>
        
        <table>
					<tr class="form-row-spacer">				  
            <beacon:label property="quizFormName">
              <cms:contentText key="NAME" code="quiz.form"/>
            </beacon:label>	
            <td class="content-field-review">
              <c:out value="${viewQuizFormForm.quizFormName}"/>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<a class="crud-content-link" href="javascript: setActionDispatchAndSubmit('quizFormDisplay.do', 'display');">
              	<cms:contentText key="EDIT_LINK" code="quiz.form.library"/>
              </a>			
            </td>					
					</tr>

          <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>

					<tr class="form-row-spacer">				  
            <beacon:label property="description" styleClass="content-field-label-top">
              <cms:contentText key="DESCRIPTION" code="quiz.form"/>
            </beacon:label>	
            <td class="content-field-review">
              <c:if test="${viewQuizFormForm.description != null}">
                	<c:out value="${viewQuizFormForm.description}" escapeXml="false"/>
          	  </c:if>	
            </td>					
		 	 		</tr>	 

          <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>	        
          
					<tr class="form-row-spacer">			
            <beacon:label property="quizType">
              <cms:contentText key="DISPLAY_METHOD" code="quiz.form"/>
            </beacon:label>	        
            <td class="content-field-review">
              <c:out value="${viewQuizFormForm.quizType}"/>			
            </td>
          </tr>

          <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>	        
          
					<c:if test="${viewQuizFormForm.quizType eq 'random'}">
						<tr class="form-row-spacer">				  
            	<beacon:label property="numberToAsk">
              	<cms:contentText key="NUMBER_TO_ASK" code="quiz.form"/>
            	</beacon:label>	        
            	<td class="content-field-review">
              	<c:out value="${viewQuizFormForm.numberToAsk}"/>			
            	</td>
          	</tr>

          	<%-- Needed between every regular row --%>
          	<tr class="form-blank-row">
            	<td></td>
          	</tr>	        
          </c:if>

					<tr class="form-row-spacer">				  
            <beacon:label property="passingScore">
              <cms:contentText key="PASSING_SCORE" code="quiz.form"/>
            </beacon:label>	        
            <td class="content-field-review">
              <c:out value="${viewQuizFormForm.passingScore}"/>			
            </td>
					</tr>
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
												QuizQuestion temp;
										%>
				<c:set var="viewQuizFormForm_questions" value="${viewQuizFormForm.questions}" scope="request"/>
			        			<display:table name="viewQuizFormForm_questions" id="question" style="width: 100%">
				        			<display:setProperty name="basic.msg.empty_list">
					        			<tr class="crud-content" align="left"><td colspan="{0}">
                          <cms:contentText key="NO_ELEMENTS" code="quiz.form"/>
                        </td></tr>
				        			</display:setProperty>
				        			<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
        		        	<display:column titleKey="quiz.form.question.list.QUESTION" headerClass="crud-table-header-row" class="crud-content left-align" sortable="false" sortProperty="questionText" >
												<%	temp = (QuizQuestion)pageContext.getAttribute( "question" );
														parameterMap.put( "quizQuestionId", temp.getId() );
														parameterMap.put( "quizFormId", temp.getQuiz().getId() );
														pageContext.setAttribute("editUrl", ClientStateUtils.generateEncodedLink( "", "quizQuestionView.do?method=display", parameterMap ) );
												%>
        		        		<a href="<c:out value="${editUrl}"/>" class="crud-content-link">
													<c:out escapeXml='false' value="${question.questionText}"/>
												</a>
        		        	</display:column>
        		        	<display:column property="statusType.name" titleKey="quiz.form.question.list.STATUS" headerClass="crud-table-header-row" class="crud-content center-align" sortable="false"/>			
                			<display:column property="displayRequired" titleKey="quiz.form.question.list.REQUIRED" headerClass="crud-table-header-row" class="crud-content center-align" sortable="false"/>
											<display:column titleKey="quiz.form.question.list.ORDER" class="crud-content center-align" headerClass="crud-table-header-row">
        		        		<c:out value="${rowNum + 1}"/>
        		        	</display:column>
        		          <beacon:authorize ifNotGranted="LOGIN_AS">
        		          <display:column titleKey="quiz.form.question.list.REORDER" class="crud-content left-align nowrap" headerClass="crud-table-header-row">
        		        	<table border="0" cellpadding="3" cellspacing="1">
        		        		<tr>
        		        			<td width="10">
        		        			<%	pageContext.setAttribute("reorderUrl", ClientStateUtils.generateEncodedLink( "", "quizFormView.do?method=reorder", parameterMap ) ); %>
        		        			<c:if test="${rowNum != 0}">
        		        				<a href="<c:out value="${reorderUrl}"/>&newQuestionSequenceNum=<c:out value="${rowNum-1}"/>"><img src="<%= RequestUtils.getBaseURI(request) %>/assets/g4skin/images/order-arrowup.gif" border="0"/></a>
        		        			</c:if>
        		        			</td>
        		        			<td width="10">
        		        			<c:if test="${rowNum != viewQuizFormForm.questionsSize - 1}">
        		        				<a href="<c:out value="${reorderUrl}"/>&newQuestionSequenceNum=<c:out value="${rowNum+1}"/>"><img src="<%= RequestUtils.getBaseURI(request) %>/assets/g4skin/images/order-arrowdown.gif" border="0"/></a>
        		        			</c:if>
        		        			</td>
        		        			<td>
        		        				<%--This is not on the actual struts form because it is just a placeholder for the value passed to the javescript function--%>
        		        				<input type="text" name="newQuestionSequenceNum<c:out value="${rowNum}"/>" size="2" maxlength="3" />
        		        			</td>
        		        			<td>
        		        				<input type="button" onclick="reorderElement('<c:out value="${reorderUrl}"/>',newQuestionSequenceNum<c:out value="${rowNum}"/>.value - 1);" name="reorderbutton" class="content-buttonstyle" value="<cms:contentText key="REORDER" code="quiz.form.question.list"/>" />
        		        			</td>
        		        		</tr>
        		        	</table>
        		          </display:column>
        		          <display:column titleKey="quiz.form.question.list.REMOVE" style="width: 75px" class="crud-content center-align" headerClass="crud-table-header-row">
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
								
                			
                			<!-- code to show learning objects -->
                			<tr class="form-row-spacer">
                			
                			<th class="crud-table-header-row">
                			<cms:contentText key="CRUD_REMOVE_LABEL" code="system.general" />
                			</th>
                			<th class="crud-table-header-row">
                			<cms:contentText key="SLIDE_NUMBER" code="quiz.learningForm" />
                			</th>
                			<th class="crud-table-header-row">
                			<cms:contentText key="FILE_NAMES" code="quiz.learningForm" />
                			</th>
                			<th class="crud-table-header-row">
                			<cms:contentText key="FILE_DESCRIPTION" code="quiz.learningForm" />
                		
                			</th>
                			
                			</tr>
                			<%Map slideParamMap=new HashMap();;
                			QuizLearningSlideDetails quizSlideDetails;
                			%>
                			
             <c:if test="${empty viewQuizFormForm.quizLearningObjects}">   
             
             	 <tr><td>   	<cms:contentText key="NO_SLIDES" code="quiz.learningForm" /> </td></tr>
             </c:if>
             
             <c:if test="${!empty viewQuizFormForm.quizLearningObjects}">         			
	             <c:forEach var="quizLearningBean" items="${viewQuizFormForm.quizLearningObjects}" varStatus="itemCount">
						<c:set var="quizFormId" value="${viewQuizFormForm.quizFormId}" scope="request"/> 
						
						<%	quizSlideDetails = (QuizLearningSlideDetails)pageContext.getAttribute( "quizLearningBean" );
									slideParamMap.put( "slideId", quizSlideDetails.getSlideNumber() );
									slideParamMap.put( "quizFormId", quizSlideDetails.getQuizFormId());
									pageContext.setAttribute("editSlideUrl", ClientStateUtils.generateEncodedLink( "", "quizLearningView.do?method=prepareEdit", slideParamMap ) );
						%>
													
					<tr class="crud-table-row2">
					
					 <td  width="5%">
					 			<input type="checkbox" name="deleteSlides" id="deleteSlides" value="<%=quizSlideDetails.getSlideNumber()%>"/>
					 </td>
					
													
					<td width="8%">
										    	<a href="<c:out value="${editSlideUrl}"/>" class="crud-content-link">
														<c:out escapeXml='false' value="${quizLearningBean.slideNumber}"/>
					</a>
	 									    	
					 </td>
					  <c:forEach var="quizLearningFile" items="${quizLearningBean.detailList}" varStatus="itemDetailCountLeft">
					  		<td  width="57%"><c:out escapeXml="false"  value="${quizLearningFile.leftColumn}"/></td>
					  		<td  width="30%"><c:out escapeXml="false"  value="${quizLearningFile.rightColumn}"/></td>
					  		 
					  </c:forEach>
					
					
					
					</tr>
				
				   </c:forEach>
			   </c:if>
        		    		
        		    		
                			
                			<!-- end of code to show learning objects -->
                			
                  			<td align="left">
            			  			<beacon:authorize ifNotGranted="LOGIN_AS">
            			  			<html:button property="Add" styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('quizQuestionDisplay.do', 'display');">
										<cms:contentText key="ADD_QUESTION" code="quiz.form"/>
									</html:button>
									</beacon:authorize>
            						</td>
            						
            						<td align="left">
            			  			<beacon:authorize ifNotGranted="LOGIN_AS">
            			  			<html:button property="Add" styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('quizLearningObjectDisplay.do', 'display');">
										<cms:contentText key="ADD_LEARNING_OBJECT" code="quiz.form"/>
									</html:button>
									</beacon:authorize>
            						</td>
            						<td align="left">
            			  			<beacon:authorize ifNotGranted="LOGIN_AS">
            						<html:button property="removeSlide"	styleClass="content-buttonstyle" onclick="return validateAndSubmit()">
                   						 <cms:contentText key="REMOVE_SELECTED_SLIDE" code="quiz.learningForm" />
                    				</html:button>
                    				</beacon:authorize>
            						</td>
            						<td width="25%" align="right">
						  						<c:if test="${not empty viewQuizFormForm.questions}">
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
             	<html:button property="cancelBtn" styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('quizFormListDisplay.do','')">
               	<cms:contentText code="quiz.form" key="BACK_TO_QUIZ_LIBRARY" />
             	</html:button>

             	<beacon:authorize ifNotGranted="LOGIN_AS">
             	<html:submit styleClass="content-buttonstyle" onclick="setDispatch('prepareCopy')">
               	<cms:contentText code="quiz.form" key="COPY_QUIZ" />
             	</html:submit>
             	</beacon:authorize>
							
							<c:if test="${not empty viewQuizFormForm.questions && viewQuizFormForm.status == 'undrconstr'}">
                <beacon:authorize ifNotGranted="LOGIN_AS">
	             	<html:submit styleClass="content-buttonstyle" onclick="setActionAndDispatch('quizFormMarkComplete.do', 'markComplete')">
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