<%--UI REFACTORED--%>
<%@ include file="/include/taglib.jspf"%>

<html:form styleId="contentForm" action="userEmailAddressMaintain">
	<html:hidden property="method" />
	<html:hidden property="isPrimary"/>
	<html:hidden property="version"/>
	<html:hidden property="dateCreated"/>
	<html:hidden property="createdBy"/>
	<html:hidden property="fromPaxScreen"/>
	<html:hidden property="currentEmailAddr" value="${userEmailAddressForm.emailAddr}"/>
	<html:hidden property="rosterEmailId"/>	
	<beacon:client-state>
		<beacon:client-state-entry name="userId" value="${userEmailAddressForm.userId}"/>
		<beacon:client-state-entry name="id" value="${userEmailAddressForm.id}"/>
	</beacon:client-state>
  
<table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="TITLE" code="participant.emailaddr"/></span>
          <%-- Subheadline --%>
        <br/>
   		<beacon:username userId="${displayNameUserId}"/>
        <%-- End Subheadline --%>
        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="ADD_INFO" code="participant.emailaddr"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
     	
        <cms:errors/>
        
        <table>
        
          <tr class="form-row-spacer">				  
            <beacon:label property="emailAddrType" required="true">
              <cms:contentText key="TYPE" code="participant.emailaddr"/>
            </beacon:label>	
            <td class="content-field">
             <c:choose>
		        <c:when test='${userEmailAddressForm.id == 0}'>
				  <html:select property="emailAddrType" styleClass="content-field">
				    <c:forEach items="${userEmailAddressTypes}" var="userEmailAddrType">
				      <html:option value="${userEmailAddrType.code}"><c:out value="${userEmailAddrType.name}"/></html:option>
				    </c:forEach>			  
				  </html:select>
		        </c:when>
		        <c:otherwise>
		            <c:out value="${userEmailAddressForm.emailAddrType}"/>
		            <html:hidden property="emailAddrType"/>
		        </c:otherwise>
		      </c:choose>
            </td>
        </tr>
        
        <%-- Needed between every regular row --%>
        <tr class="form-blank-row">
          <td></td>
        </tr>	
        
        <%-- Verification status. If new email, default to unverified. If updating, keep existing value --%>
        <tr class="form-row-spacer">				  
          <beacon:label property="verificationStatus" required="true">
            <cms:contentText key="VERIFICATION" code="participant.emailaddr.list"/>
          </beacon:label>	
          <td class="content-field">
          <c:choose>
	        <c:when test='${userEmailAddressForm.id == 0}'>
			  <html:hidden property="verificationStatus" value="unverified"/>
			  <cms:contentText key="UNVERIFIED" code="participant.emailaddr.list"/>
	        </c:when>
	        <c:otherwise>
	          <html:hidden property="verificationStatus"/>
	          <c:out value="${userEmailAddressForm.verificationStatus}"/>
	        </c:otherwise>
	      </c:choose>
	      </td>
	      </tr>
        
          <%-- Needed between every regular row --%>
          <tr class="form-blank-row">
            <td></td>
          </tr>	
          
          <tr class="form-row-spacer">				  
            <beacon:label property="emailAddr" required="true">
              <cms:contentText key="EMAILADDR" code="participant.emailaddr.list"/>
            </beacon:label>	
            <td class="content-field" nowrap>
              <html:text property="emailAddr" styleClass="content-field" size="50" maxlength="50"/>
            </td>
          </tr>

        <tr class="form-buttonrow">
	    	<td></td>
	        <td></td>
	        <td align="left">
            <beacon:authorize ifNotGranted="LOGIN_AS">
	        		<c:choose>
						<c:when test='${userEmailAddressForm.id == 0}'>
							<html:submit styleClass="content-buttonstyle" onclick="setDispatch('create')">
								<cms:contentText code="system.button" key="SAVE" />
							</html:submit>				
						</c:when>
						<c:otherwise>
							<html:submit styleClass="content-buttonstyle" onclick="setDispatch('update')">
								<cms:contentText code="system.button" key="SAVE" />
							</html:submit>				
						</c:otherwise>
					</c:choose>
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
        
