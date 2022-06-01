<%--UI REFACTORED--%>
<%@ include file="/include/taglib.jspf" %>
	   
<html:form styleId="contentForm" action="changePasswordEdit">
  <html:hidden property="method" value="unspecified"/>
  <c:if test="${not empty collectSecurityDetailsOnly}">
		<beacon:client-state>
			<beacon:client-state-entry name="newPassword" value="fake"/>
			<beacon:client-state-entry name="confirmNewPassword" value="fake"/>
		</beacon:client-state>
	</c:if>
  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
       <c:choose>
       		<c:when test="${not empty collectSecurityDetailsOnly}">	
	       		 	<span class="headline"><cms:contentText key="SECRET_TITLE" code="login.change.password"/></span>
			        <%--INSTRUCTIONS--%>
			        <br/><br/>
			        <span class="content-instruction">
				      <cms:contentText key="SECRET_TEXT" code="login.change.password"/>
				  	</span>	     
		  	</c:when>
		  	<c:otherwise>
		  	      	<span class="headline"><cms:contentText key="TITLE" code="login.change.password"/></span>
			        <%--INSTRUCTIONS--%>
			        <br/><br/>
			        <span class="content-instruction">
				      <cms:contentText key="TEXT" code="login.change.password"/>
				  	</span>
		  	</c:otherwise>
	   </c:choose>
	   
	   <%-- Subheadline --%>
	   <c:if test="${not empty displayExpiredPasswordMessage}">
        <br/>
        <span class="subheadline"><cms:contentText key="EXPIRED_PASSWORD_MSG" code="login.change.password"/></span>
        <%-- End Subheadline --%>
	   </c:if>
	   
        <br/><br/>
        <%--END INSTRUCTIONS--%>
        <cms:errors/>

        <table>
        <c:if test="${empty collectSecurityDetailsOnly}">
	          <tr class="form-row-spacer">
	            <beacon:label property="newPassword" required="true">
	              <cms:contentText key="PASSWORD_LABEL" code="login.change.password"/>
	            </beacon:label>
	            <td class="content-field">
	              <html:password name="changePasswordForm" property="newPassword"/>
	            </td></tr>
	
	          <tr class="form-blank-row">
	            <td></td>
	          </tr>
	
	          <tr class="form-row-spacer">
	            <beacon:label property="confirmNewPassword" required="true">
	              <cms:contentText key="CONFIRM_PASSWORD_LABEL" code="login.change.password"/>
	            </beacon:label>
	            <td class="content-field">
	              <html:password name="changePasswordForm" property="confirmNewPassword"/>
	            </td></tr>
	
			  	<tr class="form-blank-row">
		            <td></td>
		          </tr>	
	      </c:if>

  		 <tr class="form-row-spacer">				  
            <beacon:label property="secretQuestion" required="true">
              <cms:contentText key="SECRET_QUESTION" code="user.user"/>
            </beacon:label>	
            <td class="content-field">
				<html:select property="secretQuestion" styleClass="content-field">
					<html:option value=''><cms:contentText key="SELECT_ONE" code="system.general"/></html:option>
	        		<html:options collection="secretQuestionList" property="code" labelProperty="name"  />
	       		</html:select>
			</td>
		</tr>

		  <tr class="form-blank-row">
            <td></td>
          </tr>	

        <tr class="form-row-spacer">				  
            <beacon:label property="secretAnswer" required="true">
              <cms:contentText key="SECRET_ANSWER" code="user.user"/>
            </beacon:label>	
            <td class="content-field" nowrap>
				<html:text property="secretAnswer" size="30" styleClass="content-field"/>
			</td>
		  </tr>

          <tr class="form-blank-row">
            <td></td>
          </tr>

          <%--BUTTON ROWS ... only display these two rows if there are buttons to be displayed.--%>
          <tr class="form-buttonrow">
            <td></td>
            <td></td>
            <td align="left">
              <beacon:authorize ifNotGranted="LOGIN_AS">
           <c:choose>
       		<c:when test="${not empty collectSecurityDetailsOnly}">	
	          <html:submit styleClass="content-buttonstyle" onclick="setDispatch('saveSecurityInfo')">
                	<cms:contentText key="SUBMIT" code="system.button"/>
              </html:submit>
		  	</c:when>
		  	<c:otherwise>
		 		<html:submit styleClass="content-buttonstyle" onclick="setDispatch('savePassword')">
                	<cms:contentText key="SUBMIT" code="system.button"/>
              	</html:submit>
		  	</c:otherwise>
	   	</c:choose>
	            </beacon:authorize>
              <html:cancel styleClass="content-buttonstyle" onclick="setDispatch('savePassword')">
                <cms:contentText key="CANCEL" code="system.button"/>
              </html:cancel>
            </td>
          </tr>
          <%--END BUTTON ROW--%>
        </table>
      </td></tr>
  </table>

</html:form>
