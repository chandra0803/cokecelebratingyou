<%--UI REFACTORED--%>
<%@ include file="/include/taglib.jspf" %>

<script type="text/javascript">
<!--
function maintainUserNode( method )
{
	document.userNodeListForm.method.value = method;	
	document.userNodeListForm.action = "maintainUserNodes.do";
	document.userNodeListForm.submit();
}
//-->
</script>

<html:form styleId="contentForm" action="maintainUserNodes">
	<html:hidden property="method"/>
	<beacon:client-state>
		<beacon:client-state-entry name="userId" value="${userNodeForm.userId}"/>
	</beacon:client-state>
	
  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="ADD_TITLE" code="user.node.details"/></span>        	  
        <%-- Subheadline --%>
        <br/>
         <beacon:username userId="${displayNameUserId}"/>
        <%-- End Subheadline --%>

        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="ADD_INFO" code="user.node.details"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
     	
        <cms:errors/>

        
        <table>
        	<c:if test="${!empty unassignedNodeList}">
	          <tr class="form-row-spacer">			
	            <beacon:label property="nodeId" required="true">
	              <cms:contentText key="NAME" code="user.node.details"/>
	            </beacon:label>	        
	            <td class="content-field">
	              <html:select property="nodeId" size="1" styleClass="content-field">
	                <html:options collection="unassignedNodeList" property="id" labelProperty="name"  />
	              </html:select>
	            </td>  	
	          </tr>	   
	          <tr class="form-blank-row">
	            <td></td>
	          </tr>
	
	          <tr class="form-row-spacer">			
	            <beacon:label property="role" required="true">
	              <cms:contentText key="ROLE" code="user.node.details"/>
	            </beacon:label>	        
	            <td class="content-field">
	              <html:select property="role" size="1" styleClass="content-field">
	                <html:options collection="hierarchyTypeValues" property="code" labelProperty="name"  />
	              </html:select>
	            </td>  	
	          </tr>
	         <tr class="form-buttonrow">
	           <td></td>
	           <td></td>
	           <td align="left">	
               <beacon:authorize ifNotGranted="LOGIN_AS">    			
		    			<html:button property="Add" styleClass="content-buttonstyle" onclick="setDispatchAndSubmit('addUserNode')">
							<cms:contentText key="SAVE" code="system.button"/>
						</html:button>	
               </beacon:authorize>
						<html:cancel styleClass="content-buttonstyle">
			                <cms:contentText key="CANCEL" code="system.button"/>
			            </html:cancel>    			
	           </td>
	         </tr>	          	   
		  	</c:if>
			<c:if test="${empty unassignedNodeList}">
			<tr class="form-row-spacer">
				<td class="content-field-review">
					<cms:contentText key="NO_DATA_TO_ASSIGN" code="user.node.details"/>
				</td>
			</tr>
	         <tr class="form-buttonrow">
	           <td></td>
	           <td></td>
	           <td align="left">
	    			<html:button property="View" styleClass="content-buttonstyle" onclick="setDispatchAndSubmit('displayListToRemove')">
						<cms:contentText key="BUTTON_VIEW_NODE" code="user.node.details"/>
					</html:button>	    			
	           </td>
	         </tr>			
			</c:if>

        </table>                
        
      </td>
     </tr>        
   </table>
</html:form>