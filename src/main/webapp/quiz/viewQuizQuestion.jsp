<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.quiz.QuizQuestionAnswer" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>
<%--UI REFACTORED--%>
<script type="text/javascript">
	function reorderElement( url, newAnswerSequenceNum )
	{
		var whereToGo = url + "&newAnswerSequenceNum=" + newAnswerSequenceNum;
		window.location = whereToGo;
	}
</script>
  
<html:form styleId="contentForm" action="quizQuestionSave">
  <html:hidden property="method" />
  <html:hidden property="quizQuestionText" />
  <html:hidden property="quizQuestionRequired" />
  <html:hidden property="quizQuestionStatus"/>
  <html:hidden property="quizQuestionStatusText"/>
  <html:hidden property="quizName" />
  <html:hidden property="quizType" />
  <html:hidden property="newAnswerSequenceNum" />
  <html:hidden property="quizQuestionCmAssetName" />
	<beacon:client-state>
		<beacon:client-state-entry name="quizQuestionAnswerId" value="${quizQuestionForm.quizQuestionAnswerId}"/>
		<beacon:client-state-entry name="quizFormId" value="${quizQuestionForm.quizFormId}"/>
		<beacon:client-state-entry name="quizQuestionId" value="${quizQuestionForm.quizQuestionId}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
	  <td>

        <span class="headline"><cms:contentText key="TITLE" code="quiz.question"/></span>
        <span id="quicklink-add"></span><script type="text/javascript">quicklink_display_add('<cms:contentText key="TITLE" code="quiz.question"/>','<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>'); </script>	  
        
        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="INSTRUCTIONS" code="quiz.question"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
     	
        <cms:errors/>
        
        <table>
          <tr>
            <td>
            <table>
		  <tr class="form-row-spacer">				  
            <beacon:label property="quizQuestionText" required="true" styleClass="content-field-label-top">
              <cms:contentText key="QUESTION" code="quiz.question"/>
            </beacon:label>	
            <td class="content-field-review">
              <c:out escapeXml='false' value="${quizQuestionForm.quizQuestionText}"/>
           	  &nbsp;&nbsp;
              <a href="javascript:setActionDispatchAndSubmit('quizQuestionDisplay.do', 'display')"><cms:contentText key="EDIT_LINK" code="quiz.form.library"/></a>
            </td>					
		  </tr>
         
          <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>	        
          
		  <tr class="form-row-spacer">			
            <beacon:label property="quizQuestionRequired" required="true" styleClass="content-field-label-top">
              <cms:contentText key="QUESTION_REQUIRED" code="quiz.question"/>
            </beacon:label>	        
            <td class="content-field-review">
              <c:choose>
                <c:when test="${quizQuestionForm.quizQuestionRequired == 'true'}">
                  <cms:contentText code="system.common.labels" key="YES"/>
                </c:when>
                <c:otherwise>
                  <cms:contentText code="system.common.labels" key="NO"/>
                </c:otherwise>
              </c:choose>
            </td>
          </tr>

          <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>	        
          
		  <tr class="form-row-spacer">				  
            <beacon:label property="quizQuestionStatus" required="true">
              <cms:contentText key="QUESTION_STATUS" code="quiz.question"/>
            </beacon:label>	        
            <td class="content-field-review">
              <c:out escapeXml='false' value="${quizQuestionForm.quizQuestionStatusText}"/>	
            </td>
		  </tr>
					
          <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>
	     </table>
	     </td>
	    </tr>
	  	  <tr class="form-row-spacer">			  
			<td colspan="2">
			  <table width="100%">
                <tr>
                  <td align="right">
                   <c:set var="rowNum" value="0"/>
			        <%	Map parameterMap = new HashMap();
									QuizQuestionAnswer temp;
							%>
				<c:set var="quizQuestionForm_answers" value="${quizQuestionForm.answers}" scope="request"/>
			        <display:table name="quizQuestionForm_answers" id="answer" style="width: 100%">
				      <display:setProperty name="basic.msg.empty_list">
  					    <tr class="crud-content" align="left"><td colspan="{0}"><cms:contentText key="NO_ELEMENTS" code="quiz.question"/></td></tr>
				      </display:setProperty>
				      <display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
        		      <display:column titleKey="quiz.question.ANSWERS" headerClass="crud-table-header-row" class="crud-content left-align" sortProperty="questionAnswerText">
									<%	temp = (QuizQuestionAnswer)pageContext.getAttribute( "answer" );
											parameterMap.put( "quizQuestionId", temp.getQuizQuestion().getId() );
											parameterMap.put( "quizQuestionAnswerId", temp.getId() );
											pageContext.setAttribute("editUrl", ClientStateUtils.generateEncodedLink( "", "quizQuestionAnswerDisplay.do?method=display", parameterMap ) );
									%>
        		        <a href="<c:out value="${editUrl}"/>" class="crud-content-link">
        						  <c:out escapeXml='false' value="${answer.questionAnswerText}"/>
				        		</a>
        		      </display:column>
        		      <display:column property="displayCorrect" titleKey="quiz.question.CORRECT" headerClass="crud-table-header-row" class="crud-content center-align" />			
					        <display:column titleKey="quiz.question.ORDER" class="crud-content center-align" headerClass="crud-table-header-row">
        		        <c:out value="${rowNum + 1}"/>
        		      </display:column>
        		      <beacon:authorize ifNotGranted="LOGIN_AS">
        		      <display:column titleKey="quiz.question.REORDER" class="crud-content left-align nowrap" headerClass="crud-table-header-row">
        		        <table border="0" cellpadding="3" cellspacing="1">
        		          <tr>
        		        	<td width="10">
        		        	<%	pageContext.setAttribute("reorderUrl", ClientStateUtils.generateEncodedLink( "", "quizQuestionView.do?method=reorder", parameterMap ) ); %>
        		        	  <c:if test="${rowNum != 0}">
        		        	    <a href="<c:out value="${reorderUrl}"/>&newAnswerSequenceNum=<c:out value="${rowNum-1}"/>"><img src="<%= RequestUtils.getBaseURI(request) %>/assets/g4skin/images/order-arrowup.gif" border="0"/></a>
        		        	  </c:if>
        		        	</td>
        		        	<td width="10">
        		        	  <c:if test="${rowNum != quizQuestionForm.answersSize - 1}">
	        		        	<a href="<c:out value="${reorderUrl}"/>&newAnswerSequenceNum=<c:out value="${rowNum+1}"/>"><img src="<%= RequestUtils.getBaseURI(request) %>/assets/g4skin/images/order-arrowdown.gif" border="0"/></a>
        		        	  </c:if>
        		        	</td>
        		        	<td>
        		        	  <%--This is not on the actual struts form because it is just a placeholder for the value passed to the javescript function--%>
        		        	  <input type="text" name="newAnswerSequenceNum<c:out value="${rowNum}"/>" size="2" maxlength="3" />
        		        	</td>
        		        	<td>
        		        	  <input type="button" onclick="reorderElement('<c:out value="${reorderUrl}"/>',newAnswerSequenceNum<c:out value="${rowNum}"/>.value - 1);" name="reorderbutton" class="content-buttonstyle" value="<cms:contentText key="REORDER" code="quiz.question"/>" />
        		        	</td>
        		          </tr>
        		        </table>
        		      </display:column>
        		      <display:column titleKey="system.general.CRUD_REMOVE_LABEL" style="width: 75px" class="crud-content center-align" headerClass="crud-table-header-row">
        		        <html:checkbox property="deletedAnswers" value="${answer.id}" />
        		      </display:column>
        		      </beacon:authorize>
        		      <c:set var="rowNum" value="${rowNum + 1}"/>
        		    </display:table>
              </td>
            </tr>
          </table>
			  </td>
		  </tr>
		  
		  <beacon:authorize ifNotGranted="LOGIN_AS">
		  <tr class="form-buttonrow">
            <td align="left">
              <html:button property="Add" styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('quizQuestionAnswerDisplay.do', 'display');">
				<cms:contentText key="ADD_ANSWER" code="quiz.question"/>
			  </html:button>
            </td>
            <td align="right">
			  <html:button property="Remove" styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('quizQuestionView.do', 'remove');">
				<cms:contentText key="REMOVE_SELECTED" code="system.button"/>
			  </html:button>
			</td>
          </tr>
          </beacon:authorize>

		  <tr class="form-buttonrow">
           	<td colspan="3" align="center">
              <html:cancel styleClass="content-buttonstyle" onclick="setDispatch('save')">
                <cms:contentText code="quiz.form" key="BACK_TO_QUIZ" />
              </html:cancel>
	       	</td>
          </tr>
		</table>
      </td>
	</tr>        
  </table>
</html:form>
