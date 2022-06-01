<%@ include file="/include/taglib.jspf"%>
<%--UI REFACTORED--%>
<html:form styleId="contentForm" action="surveyFormCopy">
  <html:hidden property="method" />
	<beacon:client-state>
		<c:if test="${viewSurveyForm.surveyFormId != null && surveyForm.surveyFormId == null}">
		<beacon:client-state-entry name="surveyFormId" value="${viewSurveyForm.surveyFormId}"/>
		</c:if>
		<c:if test="${viewSurveyForm.surveyFormId == null && surveyForm.surveyFormId != null}">
		<beacon:client-state-entry name="surveyFormId" value="${surveyForm.surveyFormId}"/>
		</c:if>
		<c:if test="${viewSurveyForm.surveyFormId != null && surveyForm.surveyFormId != null}">
		<beacon:client-state-entry name="surveyFormId" value="${viewSurveyForm.surveyFormId}"/>
		</c:if>
		<c:if test="${viewSurveyForm.surveyFormName != null && surveyForm.surveyFormName == null}">
		<beacon:client-state-entry name="oldSurveyFormName" value="${viewSurveyForm.surveyFormName}"/>
		</c:if>
		<c:if test="${viewSurveyForm.surveyFormName == null && surveyForm.surveyFormName != null}">
		<beacon:client-state-entry name="oldSurveyFormName" value="${surveyForm.surveyFormName}"/>
		</c:if>
		<c:if test="${viewSurveyForm.surveyFormName != null && surveyForm.surveyFormName != null}">
		<beacon:client-state-entry name="oldSurveyFormName" value="${viewSurveyForm.surveyFormName}"/>
		</c:if>
		<c:if test="${viewSurveyForm.surveyFormType != null && surveyForm.surveyFormType == null}">
		<beacon:client-state-entry name="surveyFormType" value="${viewSurveyForm.surveyFormType}"/>
		</c:if>
		<c:if test="${viewSurveyForm.surveyFormType == null && surveyForm.surveyFormType != null}">
		<beacon:client-state-entry name="surveyFormType" value="${surveyForm.surveyFormType}"/>
		</c:if>
		<c:if test="${viewSurveyForm.surveyFormType != null && surveyForm.surveyFormType != null}">
		<beacon:client-state-entry name="surveyFormType" value="${viewSurveyForm.surveyFormType}"/>
		</c:if>
	</beacon:client-state>

	<table border="0" cellpadding="10" cellspacing="0" width="100%">
		<tr>
			<td>
        <span class="headline"><cms:contentText key="COPY_TITLE" code="survey.form"/></span>
        <span id="quicklink-add"></span><script type="text/javascript">quicklink_display_add('<cms:contentText key="COPY_TITLE" code="survey.form"/>','<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>'); </script>	  
        
        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="COPY_INSTRUCTIONS" code="survey.form"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
     	
        <cms:errors/>
        
        <table>
					<tr class="form-row-spacer">				  
            <beacon:label property="surveyFormName" required="true">
              <cms:contentText key="NAME" code="survey.form"/>
            </beacon:label>	
            <td class="content-field-review">
              <c:if test="${viewSurveyForm.surveyFormName != null }">
              <c:out value="${viewSurveyForm.surveyFormName}"/>
              </c:if>
              <c:if test="${surveyForm.surveyFormName != null }">
              <c:out value="${surveyForm.surveyFormName}"/>
              </c:if>
            </td>
					</tr>

          <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>	        
          
					<tr class="form-row-spacer">				  
            <beacon:label property="surveyFormName" required="true">
              <cms:contentText key="NEW_NAME" code="survey.form"/>
            </beacon:label>	
            <td class="content-field">
              <html:text property="copySurveyFormName" styleClass="content-field" size="50" maxlength="30"/>
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
             <html:button property="cancelBtn" styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('surveyFormView.do','display')">
               <cms:contentText code="system.button" key="CANCEL" />
             </html:button>
           </td>
         </tr>
        </table>
      </td>
     </tr>        
   </table>
</html:form>