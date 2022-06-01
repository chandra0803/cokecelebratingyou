<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ include file="/include/taglib.jspf"%>
<%--UI REFACTORED--%>
 
<html:form styleId="contentForm" action="surveyFormSave">
  <html:hidden property="method" />
  <html:hidden property="status" />
	<beacon:client-state>
		<beacon:client-state-entry name="surveyAssigned" value="${surveyForm.surveyAssigned}"/>
		<beacon:client-state-entry name="surveyFormId" value="${surveyForm.surveyFormId}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
	<tr>
		<td>
		  <c:choose>
			<c:when test="${surveyForm.surveyFormId == 0}">
        		<span class="headline"><cms:contentText key="ADD_TITLE" code="survey.form"/></span>
        		<span id="quicklink-add"></span><script type="text/javascript">quicklink_display_add('<cms:contentText key="ADD_TITLE" code="survey.form"/>','<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>'); </script>	  
        
        		<%--INSTRUCTIONS--%>
        		<br/>
        		<span class="content-instruction">
          			<cms:contentText key="ADD_INSTRUCTIONS" code="survey.form"/>
        		</span>
        		<br/>
        		<%--END INSTRUCTIONS--%>
     		</c:when>
			<c:otherwise>
				<span class="headline"><cms:contentText key="EDIT_TITLE" code="survey.form"/></span>
        		<span id="quicklink-add"></span><script type="text/javascript">quicklink_display_add('<cms:contentText key="EDIT_TITLE" code="survey.form"/>','<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>'); </script>	  
        
        		<%--INSTRUCTIONS--%>
        		<br/><br/>
        		<span class="content-instruction">
          			<cms:contentText key="EDIT_INSTRUCTIONS" code="survey.form"/>
        		</span>
        		<br/><br/>
        		<%--END INSTRUCTIONS--%>
			</c:otherwise>
		  </c:choose>
        <cms:errors/>
        
        <table>
			<tr class="form-row-spacer">				  
            	<beacon:label property="surveyFormName" required="true">
              		<cms:contentText key="NAME" code="survey.form"/>
            	</beacon:label>	
            	<td class="content-field">
              		<html:text property="surveyFormName" styleId="surveyFormName" styleClass="content-field" size="50" maxlength="200"/>
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
            	<td class="content-field">
              		<TEXTAREA style="WIDTH: 100%" id="description" name="description" rows=6><c:if test="${surveyForm.description != null}"><c:out value="${surveyForm.description}" /></c:if></TEXTAREA>
            	</td>					
		  	</tr>
          
          	<%-- Needed between every regular row --%>
          	<tr class="form-blank-row">
            	<td></td>
          	</tr>
          	
          	<tr class="form-row-spacer">				  
           	<beacon:label property="promoModule" required="true">
              		<cms:contentText key="PROMOTION_MODULE" code="survey.form"/>
           	</beacon:label>	
           	<td class="content-field">
             		<html:select property="surveyFormType" styleClass="content-field" >
					  <html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>	
					  <html:options collection="moduleList" property="code" labelProperty="name"  />
					</html:select>
            	</td>					
		  	</tr>

			<%-- <tr class="form-row-spacer">			
            <beacon:label property="surveyType" required="true" styleClass="content-field-label-top">
              <cms:contentText key="DISPLAY_METHOD" code="survey.form"/>
            </beacon:label>
			  <c:choose>
            	<c:when test="${surveyForm.surveyAssigned == 'true' }">
            	  <html:hidden property="surveyType"/>
				  <td class="content-field-review">
              	    <c:out value="${surveyForm.surveyType}"/>			
            	  </td>
				</c:when>
				<c:otherwise>
  				  <td class="content-field">
                	<c:forEach var="surveyTypeOptions" items="${surveyTypeOptions}" varStatus="index">
                	    <c:if test="${surveyTypeOptions.code != 'fixed' }">
                	   		 <c:out value="${surveyTypeOptions.name}"/>
                	   		  <html:hidden property="surveyType" styleId="surveyType"  value="${surveyTypeOptions.code }"/>
                	    </c:if>
                	</c:forEach>
            	  </td>
				</c:otherwise>
			  </c:choose>
          	</tr> --%>

         	<tr class="form-buttonrow">
           		<td></td>
           		<td></td>
           		<td align="left">
             		<beacon:authorize ifNotGranted="LOGIN_AS">
             		  <html:submit styleClass="content-buttonstyle" onclick="setDispatch('save')">
               			<cms:contentText code="system.button" key="SAVE" />
             		  </html:submit>
             		</beacon:authorize>
             		<html:button property="cancelBtn" styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('surveyFormListDisplay.do','')">
               			<cms:contentText code="system.button" key="CANCEL" />
             		</html:button>
           		</td>
         	</tr>
        </table>
      </td>
     </tr>        
   </table>
   
    
</html:form>
