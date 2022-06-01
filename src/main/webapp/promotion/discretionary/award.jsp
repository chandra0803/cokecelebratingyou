<%-- UI REFACTORED --%>
<%@ include file="/include/taglib.jspf"%>

<html:form styleId="contentForm" action="discretionaryAwardSave">
	<html:hidden property="method" value=""/>
	<html:hidden property="discretionaryAwardMin"/>
	<html:hidden property="discretionaryAwardMax"/>
	<beacon:client-state>
		<beacon:client-state-entry name="userId" value="${discretionaryAwardForm.userId}"/>
	</beacon:client-state>
	
  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="TITLE" code="promotion.discretionary.award"/></span>
        <%-- Subheadline --%>
        <br/>
        <span class="subheadline">
        	<c:out value="${user.titleType.name}" />
		  	<c:out value="${user.firstName}" />
			<c:out value="${user.middleName}" />
			<c:out value="${user.lastName}" />
			<c:out value="${user.suffixType.name}" />	
		</span>
        <%-- End Subheadline --%>

        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="INSTRUCTIONS" code="promotion.discretionary.award"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
     	
        <cms:errors/>
    	
    	<table>
          <tr class="form-row-spacer">				  
            <beacon:label property="promotion" required="true">
              <cms:contentText key="PROMOTION" code="promotion.discretionary.award"/>
            </beacon:label>	
            <td class="content-field">
              <html:select property="promotion" styleClass="content-field">
			  	<html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>
			  	<html:options collection="promotionList" property="id" labelProperty="name"/>
			  </html:select>
            </td>
          </tr>

          <tr class="form-blank-row">
            <td></td>
          </tr>	        

          <tr class="form-row-spacer">				  
            <beacon:label property="amount" required="true">
              <cms:contentText key="AWARD_AMOUNT" code="promotion.discretionary.award"/>
            </beacon:label>	
            <td class="content-field">
              <html:text property="amount" size="10" maxlength="10" styleClass="content-field"/>
            </td>
          </tr>

          <tr class="form-blank-row">
            <td></td>
          </tr>	        
          
          <tr class="form-row-spacer">				  
            <beacon:label property="comments" required="true">
              <cms:contentText key="COMMENTS" code="promotion.discretionary.award"/>
            </beacon:label>	
            <td class="content-field">
              <html:text property="comments" size="50" maxlength="50" styleClass="content-field"/>
            </td>
          </tr>

          	     		
	  <c:choose>
		<c:when test='${param.depositSuccess == "true"}'>
		
		  <tr class="form-blank-row">
            <td></td>
          </tr>
		
		
		  <tr class="form-row-spacer">
		    <td></td>
		    <td></td>
			<td class="error">
				<cms:contentText code="system.general" key="DISCRETIONARY_DEPOSIT_SUCCESS" />
			</td>
		  </tr>
		  
		  <tr class="form-buttonrow">
           <td></td>
           <td></td>
           <td align="left">
             <html:submit styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('../participant/participantDisplay.do','display')">
				<cms:contentText code="system.button" key="BACK" />
			 </html:submit>
           </td>
         </tr>
		  
		</c:when>
		<c:otherwise>
			<tr class="form-buttonrow">
	           <td></td>
	           <td></td>
	           <td align="left">
               <beacon:authorize ifNotGranted="LOGIN_AS">
		            <html:submit styleClass="content-buttonstyle" onclick="setDispatch('depositAward')">
					  <cms:contentText code="system.button" key="SUBMIT" />
					</html:submit>
               </beacon:authorize>
	
					<html:cancel styleClass="login-buttonstyle" onclick="setActionDispatchAndSubmit('../participant/participantDisplay.do','display')">
					  <cms:contentText code="system.button" key="CANCEL" />
					</html:cancel>
	           </td>
	         </tr>				
		</c:otherwise>
	  </c:choose>
        </table>
      </td>
     </tr>        
   </table>
</html:form>