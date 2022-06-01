<%--UI REFACTORED--%>
<%@ include file="/include/taglib.jspf"%>

<html:form styleId="contentForm" action="userMaintainUpdateLoginInfo" focus="userName">
	<html:hidden property="method"/>
	<html:hidden property="version"/>
	<beacon:client-state>
		<beacon:client-state-entry name="userId" value="${userForm.userId}"/>
		<beacon:client-state-entry name="password2" value="${userForm.password2}"/>
		<beacon:client-state-entry name="confirmPassword2" value="${userForm.confirmPassword2}"/>
		<beacon:client-state-entry name="passwordSystemGenerated" value="${userForm.passwordSystemGenerated}"/>
	</beacon:client-state>

<table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="TITLE_UPDATE_WEB_SITE_INFO" code="user.user"/></span>
          <%-- Subheadline --%>
        <br/>
   		<beacon:username userId="${displayNameUserId}"/>
        <%-- End Subheadline --%>
        <cms:errors/>

 		<table>
        
          <tr class="form-row-spacer">				  
            <beacon:label property="userName" required="true">
              <cms:contentText key="LOGIN_ID" code="user.user"/>
            </beacon:label>	
            <td class="content-field" nowrap>
				<html:text property="userName" size="30" styleClass="content-field"/>
			</td>
		  </tr>

		  <tr class="form-blank-row">
            <td></td>
          </tr>	
<%-- 
         <tr class="form-row-spacer">	
         	<beacon:label property="password" required="true">
              <cms:contentText key="PASSWORD" code="user.user"/>
            </beacon:label>
	          <c:choose>
	           <c:when test="${allowSecurityUpdate}">			  
	            <td class="content-field" nowrap>
				 <html:password property="password" size="30" styleClass="content-field"/>
				 &nbsp;&nbsp;&nbsp;
				</td>
	           </c:when>
	           <c:otherwise>
				<td class="content-field" nowrap>
	            <beacon:authorize ifAnyGranted="BI_ADMIN,REISSUE_PASSWORD">
				 <html:button property="CreatePassword" styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('userDisplay.do','reissuePassword');">
				   <cms:contentText code="system.button" key="REISSUE_PASSWORD" />
				 </html:button>
				 </beacon:authorize>
				<!-- Security Patch 3 - start -->
				<c:if test="${validIP && validNotDelegate && reissueSendPwdOn}">
					<beacon:authorize ifAnyGranted="REISSUE_SEND_PASSWORD">
					 	<html:button property="CreatePassword" styleClass="content-buttonstyle-reissue-send" onclick="setActionDispatchAndSubmit('userDisplay.do','reissueSendPassword');">
					   		<cms:contentText code="system.button" key="REISSUE_SEND_PASSWORD" />
					 	</html:button>
					</beacon:authorize>
				</c:if>
				
				<c:if test="${userForm.passwordSystemGenerated}">
				 	<c:choose>
				   		<c:when test="${reissueSendPassword}">
				   			<cms:contentText key="PWD_SENT" code="reissue.send.password" />
				   		</c:when>
				   		<c:otherwise>
				   			<cms:contentText key="PASSWORD_REISSUED" code="user.user"/>
				   		</c:otherwise>
				 	</c:choose>
				 </c:if>
				 <!-- Security Patch 3 - end -->
				</td>
			   </c:otherwise>
			  </c:choose>--%>
		  </tr>
<%-- 
		  <tr class="form-blank-row">
            <td></td>
          </tr>	
          
          <c:if test="${allowSecurityUpdate}">
          <tr class="form-row-spacer">
          	<beacon:label property="confirmPassword" required="true">
              <cms:contentText key="CONFIRM_PASSWORD" code="user.user"/>
            </beacon:label>	
            <td class="content-field" nowrap>
				<html:password property="confirmPassword" size="30" styleClass="content-field"/>
			</td>
		  </tr>

		  <tr class="form-blank-row">
            <td></td>
          </tr>	
          </c:if>

  		 <tr class="form-row-spacer">				  
            <beacon:label property="secretQuestion">
              <cms:contentText key="SECRET_QUESTION" code="user.user"/>
            </beacon:label>	
            <c:choose>
	           <c:when test="${allowSecurityUpdate}">			  
	            <td class="content-field">
				<html:select property="secretQuestion" styleClass="content-field">
				    <html:option value=''><cms:contentText key="SELECT_ONE" code="system.general"/></html:option>
	        		<html:options collection="secretQuestionList" property="code" labelProperty="name"  />
	       		</html:select>
	       		<html:hidden property="secretQuestionDesc" />
			    </td>
	           </c:when>
	           <c:otherwise>
	            <td class="content-field">
                  <html:hidden property="secretQuestion" />
				  <html:hidden property="secretQuestionDesc" />
				  <c:out value="${ userForm.secretQuestionDesc }"/>
			    </td>
			   </c:otherwise>
			</c:choose>
		  </tr>

		  <tr class="form-blank-row">
            <td></td>
          </tr>	

        <tr class="form-row-spacer">				  
            <beacon:label property="secretAnswer">
              <cms:contentText key="SECRET_ANSWER" code="user.user"/>
            </beacon:label>	
            <c:choose>
	           <c:when test="${allowSecurityUpdate}">			  
	            <td class="content-field" nowrap>
				  <html:text property="secretAnswer" size="30" styleClass="content-field"/>
				  <html:hidden property="secretAnswerDesc" />
			    </td>
	           </c:when>
	           <c:otherwise>
	            <td class="content-field" nowrap>
				  <html:hidden property="secretAnswer" />
				  <html:hidden property="secretAnswerDesc" />
				  <c:out value="${ userForm.secretAnswerDesc }"/>
			    </td>
			   </c:otherwise>
			</c:choose>
		  </tr>
--%>
 		<tr class="form-buttonrow">
	    	<td></td>
	        <td></td>
	        <td align="left">
            <beacon:authorize ifAnyGranted="BI_ADMIN">
	        		<html:submit styleClass="content-buttonstyle" onclick="setDispatch('updateLoginInfo')">
						<cms:contentText code="system.button" key="SAVE" />
					</html:submit>
            </beacon:authorize>
					<html:cancel styleClass="content-buttonstyle">
						<cms:contentText code="system.button" key="CANCEL" />
					</html:cancel>
	        </td>
          </tr>   
        </table>
		</td>
	</tr>
</table>
</html:form>
		