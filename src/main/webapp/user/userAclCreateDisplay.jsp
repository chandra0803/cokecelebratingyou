<%-- UI REFACTORED --%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.ui.user.UserAclForm"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ include file="/include/taglib.jspf" %>
<script type="text/javascript">
<!--
	function handleAclChange(url) 
	{
		var aclId = document.userAclForm.aclId.value;
		var gotoUrl = url + '&aclId=' + aclId;
		document.location = gotoUrl;
	}
//-->
</script>
<html:form styleId="contentForm" action="userAclAdd" >
	<html:hidden property="method"/>
	<beacon:client-state>
		<beacon:client-state-entry name="guid" value="${userAclForm.guid}"/>
		<beacon:client-state-entry name="userId" value="${userAclForm.userId}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="HEADING" code="admin.user.acl"/></span>
        <%-- Subheadline --%>
        <br/>
         <beacon:username userId="${displayNameUserId}"/>
        <%-- End Subheadline --%>

        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText code="admin.user.acl" key="INSTRUCTION"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
       
        <cms:errors/>
        
		<table>
		  <c:choose>
			<c:when test="${allAcls == null && !userAclForm.update}">				
				<tr class="form-row-spacer">
					<td class="content-field-review">
						<cms:contentText code="admin.user.acl" key="NONE_AVAILABLE"/>
					</td>
				</tr>
				
				<tr class="form-buttonrow">		          
		           <td align="left">		             
					 <html:cancel styleClass="content-buttonstyle">
							<cms:contentText key="CANCEL" code="system.button"/>
					  </html:cancel>
		           </td>
         		</tr>
			</c:when>
			<c:otherwise>
				<tr class="form-row-spacer">				  
          <beacon:label property="aclId" required="true">
            <cms:contentText code="admin.acl" key="ACL_CODE"/>
          </beacon:label>
          <c:choose>
					  <c:when test="${userAclForm.update}">
					  	<td class="content-field-review">
								<html:hidden property="aclId" value="${selectedAcl.id}"/>
						  	<c:out value="${selectedAcl.code}"/>
							</td>
					  </c:when>
					  <c:otherwise>
					    <td class="content-field">
								<%	Map parameterMap = new HashMap();
										UserAclForm temp = (UserAclForm)request.getAttribute("userAclForm");
										parameterMap.put( "userId", temp.getUserId() );
										pageContext.setAttribute("linkUrl", ClientStateUtils.generateEncodedLink( "", "displayCreateUserAcl.do?method=displayCreateUserAcl", parameterMap ) );
								%>
						  	<html:select property="aclId" styleClass="content-field" onchange="handleAclChange( '${linkUrl}' )">
									<html:options collection="allAcls" property="id" labelProperty="code" filter="false"/>
						  	</html:select>
						</td>
					  </c:otherwise>
				   </c:choose>		            
        </tr>
          		
				<tr class="form-blank-row">
            		<td></td>
          		</tr>	 
				
				<tr class="form-row-spacer">	
					<td></td>
					<td></td>
					<td class="content-instruction">
						<c:out value="${userAclForm.helpText}"/>
            <html:hidden property="helpText"/>
          </td>
				</tr>
				
				<tr class="form-row-spacer">				  
            		<beacon:label property="aclTarget" required="true">
              			<cms:contentText code="admin.user.acl" key="ACL_TARGET"/>
            		</beacon:label>	
            		<td class="content-field">
              			<html:text property="aclTarget" styleClass="content-field"/>
            		</td>
          		</tr>
				
				<tr class="form-blank-row">
            		<td></td>
          		</tr>
						
				<tr class="form-row-spacer">				  
            		<beacon:label property="aclPermission" required="true">
              			<cms:contentText code="admin.user.acl" key="ACL_PERMISSION"/>
            		</beacon:label>	
            		<td class="content-field">
              			<html:text property="aclPermission" styleClass="content-field"/>
            		</td>
          		</tr>
				
				<tr class="form-blank-row">
            		<td></td>
          		</tr>
				
				<tr class="form-buttonrow">
		           <td></td>
		           <td></td>
		           <td align="left">
                 <beacon:authorize ifNotGranted="LOGIN_AS">
		             <html:submit styleClass="content-buttonstyle">
						<cms:contentText key="SAVE" code="system.button"/>
					 </html:submit>
  					     </beacon:authorize>
					 <html:cancel styleClass="content-buttonstyle">
							<cms:contentText key="CANCEL" code="system.button"/>
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