<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.promotion.SurveyQuestionResponse" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>
<%--UI REFACTORED--%>
<script type="text/javascript">
	function reorderElement( url, newResponseSequenceNum )
	{
		var whereToGo = url + "&newResponseSequenceNum=" + newResponseSequenceNum;
		window.location = whereToGo;
	}
</script>
  
<html:form styleId="contentForm" action="surveyQuestionSave">
  <html:hidden property="method" />
  <html:hidden property="surveyQuestionText" />
  <html:hidden property="surveyQuestionStatus"/>
  <html:hidden property="surveyQuestionStatusText"/>
  <html:hidden property="surveyName" />
  <html:hidden property="newResponseSequenceNum" />
  <html:hidden property="surveyQuestionCmAssetName" />
	<beacon:client-state>
		<beacon:client-state-entry name="surveyQuestionResponseId" value="${surveyQuestionForm.surveyQuestionResponseId}"/>
		<beacon:client-state-entry name="surveyFormId" value="${surveyQuestionForm.surveyFormId}"/>
		<beacon:client-state-entry name="surveyQuestionId" value="${surveyQuestionForm.surveyQuestionId}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
	  <td>

        <span class="headline"><cms:contentText key="TITLE" code="survey.question"/></span>
        <span id="quicklink-add"></span><script type="text/javascript">quicklink_display_add('<cms:contentText key="TITLE" code="survey.question"/>','<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>'); </script>	  
        
        <%--INSTRUCTIONS--%>
        <br/>
        <span class="content-instruction">
          <cms:contentText key="INSTRUCTIONS" code="survey.question"/>
        </span>
        <br/>
        <%--END INSTRUCTIONS--%>
     	
        <cms:errors/>
        
        <table>
          <tr>
            <td>
            <table>
		  <tr class="form-row-spacer">				  
            <beacon:label property="surveyQuestionText" required="true" styleClass="content-field-label-top">
              <cms:contentText key="QUESTION" code="survey.question"/>
            </beacon:label>	
            <td class="content-field-review">
              <c:out escapeXml='false' value="${surveyQuestionForm.surveyQuestionText}"/>
           	  &nbsp;&nbsp;
              <a href="javascript:setActionDispatchAndSubmit('surveyQuestionDisplay.do', 'display')"><cms:contentText key="EDIT_LINK" code="survey.form.library"/></a>
            </td>					
		  </tr>
         
          <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>	        
          
          <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>	        
          
		  <tr class="form-row-spacer">				  
            <beacon:label property="surveyQuestionStatus" required="true">
              <cms:contentText key="QUESTION_STATUS" code="survey.question"/>
            </beacon:label>	        
            <td class="content-field-review">
              <c:out escapeXml='false' value="${surveyQuestionForm.surveyQuestionStatusText}"/>	
            </td>
		  </tr>
					
          <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>
	     </table>
	     </td>
	    </tr>
	    
	    <c:if test="${ surveyQuestionForm.responseType == 'standardResponse' }">
	    
	  	  <tr class="form-row-spacer">			  
			<td colspan="2">
			  <table width="100%">
                <tr>
                  <td align="right">
                   <c:set var="rowNum" value="0"/>
			        <%	Map parameterMap = new HashMap();
									SurveyQuestionResponse temp;
							%>
				<c:set var="surveyQuestionForm_responses" value="${surveyQuestionForm.answers}" scope="request"/>
			        <display:table name="surveyQuestionForm_responses" id="answer" style="width: 100%">
				      <display:setProperty name="basic.msg.empty_list">
  					    <tr class="crud-content" align="left"><td colspan="{0}"><cms:contentText key="NO_ELEMENTS" code="survey.question"/></td></tr>
				      </display:setProperty>
				      <display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
        		      <display:column titleKey="survey.question.RESPONSES" headerClass="crud-table-header-row" class="crud-content left-align" sortProperty="questionResponseText">
									<%	temp = (SurveyQuestionResponse)pageContext.getAttribute( "answer" );
											parameterMap.put( "surveyQuestionId", temp.getSurveyQuestion().getId() );
											parameterMap.put( "surveyQuestionResponseId", temp.getId() );
											pageContext.setAttribute("editUrl", ClientStateUtils.generateEncodedLink( "", "surveyQuestionResponseDisplay.do?method=display", parameterMap ) );
									%>
        		        <a href="<c:out value="${editUrl}"/>" class="crud-content-link">
        		        <c:choose>
        		        	<c:when test="${surveyQuestionForm.responseType == 'openEnded'}">
        		        		<cms:contentText key="OPEN_QUESTION" code="survey.question"/>
        		        	</c:when>
        		        	<c:otherwise>
        		        		<c:out escapeXml='false' value="${answer.questionResponseText}"/>
        		        	</c:otherwise>
        		        </c:choose>
				        		</a>
        		      </display:column>
        		      <display:column property="statusType.name" titleKey="survey.form.question.list.STATUS" headerClass="crud-table-header-row" class="crud-content center-align" sortable="false"/>
					  <display:column titleKey="survey.question.ORDER" class="crud-content center-align" headerClass="crud-table-header-row">
        		        <c:out value="${rowNum + 1}"/>
        		      </display:column>
        		      <beacon:authorize ifNotGranted="LOGIN_AS">
        		      <display:column titleKey="survey.question.REORDER" class="crud-content left-align nowrap" headerClass="crud-table-header-row">
        		        <table border="0" cellpadding="3" cellspacing="1">
        		          <tr>
        		        	<td width="10">
        		        	<%	pageContext.setAttribute("reorderUrl", ClientStateUtils.generateEncodedLink( "", "surveyQuestionView.do?method=reorder", parameterMap ) ); %>
        		        	  <c:if test="${rowNum != 0}">
        		        	    <a href="<c:out value="${reorderUrl}"/>&newResponseSequenceNum=<c:out value="${rowNum-1}"/>"><img src="<%= RequestUtils.getBaseURI(request) %>/assets/g4skin/images/order-arrowup.gif" border="0"/></a>
        		        	  </c:if>
        		        	</td>
        		        	<td width="10">
        		        	  <c:if test="${rowNum != surveyQuestionForm.answersSize - 1}">
	        		        	<a href="<c:out value="${reorderUrl}"/>&newResponseSequenceNum=<c:out value="${rowNum+1}"/>"><img src="<%= RequestUtils.getBaseURI(request) %>/assets/g4skin/images/order-arrowdown.gif" border="0"/></a>
        		        	  </c:if>
        		        	</td>
        		        	<td>
        		        	  <%--This is not on the actual struts form because it is just a placeholder for the value passed to the javescript function--%>
        		        	  <input type="text" name="newResponseSequenceNum<c:out value="${rowNum}"/>" size="2" maxlength="3" />
        		        	</td>
        		        	<td>
        		        	  <input type="button" onclick="reorderElement('<c:out value="${reorderUrl}"/>',newResponseSequenceNum<c:out value="${rowNum}"/>.value - 1);" name="reorderbutton" class="content-buttonstyle" value="<cms:contentText key="REORDER" code="survey.question"/>" />
        		        	</td>
        		          </tr>
        		        </table>
        		      </display:column>
        		      <display:column titleKey="system.general.CRUD_REMOVE_LABEL" style="width: 75px" class="crud-content center-align" headerClass="crud-table-header-row">
        		        <html:checkbox property="deletedResponses" value="${answer.id}" />
        		      </display:column>
        		      </beacon:authorize>
        		      <c:set var="rowNum" value="${rowNum + 1}"/>
        		    </display:table>
              </td>
            </tr>
          </table>
			  </td>
		  </tr>
		  </c:if>
		  
		  <beacon:authorize ifNotGranted="LOGIN_AS">
		  <c:if test="${ surveyQuestionForm.responseType == 'standardResponse' }">
		  <tr class="form-buttonrow">
            <td align="left">
              	<html:button property="Add" styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('surveyQuestionResponseDisplay.do', 'display');">
				  <cms:contentText key="ADD_RESPONSE" code="survey.question"/>
			  	</html:button>
            </td>
            <td align="right">
			  <html:button property="Remove" styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('surveyQuestionView.do', 'remove');">
				<cms:contentText key="REMOVE_SELECTED" code="system.button"/>
			  </html:button>
			</td>
          </tr>
          </c:if>
          </beacon:authorize>

		  <tr class="form-buttonrow">
           	<td colspan="3" align="center">
              <html:cancel styleClass="content-buttonstyle" onclick="setDispatch('save')">
                <cms:contentText code="survey.form" key="BACK_TO_SURVEY" />
              </html:cancel>
	       	</td>
          </tr>
		</table>
      </td>
	</tr>        
  </table>
</html:form>
