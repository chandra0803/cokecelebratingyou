<%@ include file="/include/taglib.jspf"%>
<%--UI REFACTORED--%>
<html:form styleId="contentForm" action="quizFormCopy">
  <html:hidden property="method" />
	<beacon:client-state>
		<c:if test="${viewQuizFormForm.quizFormId != null && quizFormForm.quizFormId == null}">
		<beacon:client-state-entry name="quizFormId" value="${viewQuizFormForm.quizFormId}"/>
		</c:if>
		<c:if test="${viewQuizFormForm.quizFormId == null && quizFormForm.quizFormId != null}">
		<beacon:client-state-entry name="quizFormId" value="${quizFormForm.quizFormId}"/>
		</c:if>
		<c:if test="${viewQuizFormForm.quizFormId != null && quizFormForm.quizFormId != null}">
		<beacon:client-state-entry name="quizFormId" value="${viewQuizFormForm.quizFormId}"/>
		</c:if>
		<c:if test="${viewQuizFormForm.quizFormName != null && quizFormForm.quizFormName == null}">
		<beacon:client-state-entry name="oldQuizFormName" value="${viewQuizFormForm.quizFormName}"/>
		</c:if>
		<c:if test="${viewQuizFormForm.quizFormName == null && quizFormForm.quizFormName != null}">
		<beacon:client-state-entry name="oldQuizFormName" value="${quizFormForm.quizFormName}"/>
		</c:if>
		<c:if test="${viewQuizFormForm.quizFormName != null && quizFormForm.quizFormName != null}">
		<beacon:client-state-entry name="oldQuizFormName" value="${viewQuizFormForm.quizFormName}"/>
		</c:if>
	</beacon:client-state>

	<table border="0" cellpadding="10" cellspacing="0" width="100%">
		<tr>
			<td>
        <span class="headline"><cms:contentText key="COPY_TITLE" code="quiz.form"/></span>
        <span id="quicklink-add"></span><script type="text/javascript">quicklink_display_add('<cms:contentText key="COPY_TITLE" code="quiz.form"/>','<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>'); </script>	  
        
        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="COPY_INSTRUCTIONS" code="quiz.form"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
     	
        <cms:errors/>
        
        <table>
					<tr class="form-row-spacer">				  
            <beacon:label property="quizFormName" required="true">
              <cms:contentText key="NAME" code="quiz.form"/>
            </beacon:label>	
            <td class="content-field-review">
              <c:if test="${viewQuizFormForm.quizFormName != null }">
              <c:out value="${viewQuizFormForm.quizFormName}"/>
              </c:if>
              <c:if test="${quizFormForm.quizFormName != null }">
              <c:out value="${quizFormForm.quizFormName}"/>
              </c:if>
            </td>
					</tr>

          <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>	        
          
					<tr class="form-row-spacer">				  
            <beacon:label property="quizFormName" required="true">
              <cms:contentText key="NEW_NAME" code="quiz.form"/>
            </beacon:label>	
            <td class="content-field">
              <html:text property="copyQuizFormName" styleClass="content-field" size="50" maxlength="30"/>
            </td>
					</tr>

         <tr class="form-buttonrow">
           <td></td>
           <td></td>
           <td align="left">
             <beacon:authorize ifNotGranted="LOGIN_AS">
             <html:submit styleClass="content-buttonstyle" onclick="setDispatch('copy')">
               <cms:contentText code="system.button" key="SAVE" />
             </html:submit>
             </beacon:authorize>
             <html:button property="cancelBtn" styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('quizFormView.do','display')">
               <cms:contentText code="system.button" key="CANCEL" />
             </html:button>
           </td>
         </tr>
        </table>
      </td>
     </tr>        
   </table>
</html:form>