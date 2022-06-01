<%--UI REFACTORED--%>
<%@ include file="/include/taglib.jspf"%>

<script type="text/javascript">
<!--
function maintainUserCommLog( method, action )
{
	document.commLogForm.method.value=method;	
	document.commLogForm.action = action;
	document.commLogForm.submit();
}

function selectUser(method)
{
	document.commLogForm.method.value=method;	
	document.commLogForm.action = "userCommLogMaintainDisplay.do";
	document.commLogForm.submit();
	return false;
}
//--%>
</script>

<script TYPE="text/javascript">
  
  function hideOrDisplayField( whichLayer, field )
  { 
    if ( field.value == 'closed' ||  field.value == 'deferred')
    {
    	hideLayer(whichLayer);
    }
	else
	{
		showLayer(whichLayer);	
	}   
  }
  
    function showLayer(whichLayer)
      {
        if (document.getElementById)
        {
          // this is the way the standards work
          var style2 = document.getElementById(whichLayer).style;
          style2.display = "block";
        }
        else if (document.all)
        {
          // this is the way old msie versions work
          var style2 = document.all[whichLayer].style;
          style2.display = "block";
        }
        else if (document.layers)
        {
          // this is the way nn4 works
          var style2 = document.layers[whichLayer].style;
          style2.display = "block";
        }
      }
      function hideLayer(whichLayer)
      {
        if (document.getElementById)
        {
          // this is the way the standards work
          var style2 = document.getElementById(whichLayer).style;
          style2.display = "none";
        }
        else if (document.all)
        {
          // this is the way old msie versions work
          var style2 = document.all[whichLayer].style;
          style2.display = "none";
        }
        else if (document.layers)
        {
          // this is the way nn4 works
          var style2 = document.layers[whichLayer].style;
          style2.display = "none";
        }
      }
  
</script>


      
<html:form styleId="contentForm" action="userCommLogMaintainUpdate">
	<html:hidden property="method" value="update"/>
	<beacon:client-state>
	 	<beacon:client-state-entry name="userId" value="${commLogForm.userId}"/>
	 	<beacon:client-state-entry name="commLogId" value="${commLogForm.commLogId}"/>
	 	<beacon:client-state-entry name="assignedToUserId" value="${commLogForm.assignedToUserId}"/>
	 	<beacon:client-state-entry name="assignedToName" value="${commLogForm.assignedToName}"/>
	 	<beacon:client-state-entry name="assignedByUserId" value="${commLogForm.assignedByUserId}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="TITLE" code="communication_log.detail"/></span>
        <br/>
        <beacon:username userId="${displayNameUserId}"/>
        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="ADD_INFO" code="communication_log.detail"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
     	
        <cms:errors/>
        
  		<table>
          <tr class="form-row-spacer">				  
            <beacon:label property="sourceType" required="true">
              <cms:contentText key="SOURCE" code="communication_log.detail"/>
            </beacon:label>	
            <td class="content-field">              
				<html:select property="sourceType" styleClass="content-field">
					<html:option value=''><cms:contentText key="SELECT_ONE" code="system.general"/></html:option>
					<html:options collection="commLogSourceTypesList" property="code" labelProperty="name"  />
				</html:select>
            </td>
          </tr>
 		  <tr class="form-blank-row">
            <td></td>
          </tr>	 
          
           <tr class="form-row-spacer">				  
            <beacon:label property="categoryType" required="true">
              <cms:contentText key="CATEGORY" code="communication_log.detail"/>
            </beacon:label>	
            <td class="content-field">              
				<html:select property="categoryType" styleClass="content-field">
					<html:option value=''><cms:contentText key="SELECT_ONE" code="system.general"/></html:option>
					<html:options collection="commLogCategoryTypesList" property="code" labelProperty="name"  />
				</html:select>
            </td>
          </tr>
 		  <tr class="form-blank-row">
            <td></td>
          </tr>	 
          
          <tr class="form-row-spacer">				  
            <beacon:label property="reasonType" required="true">
              <cms:contentText key="REASON" code="communication_log.detail"/>
            </beacon:label>	
            <td class="content-field">              
				<html:select property="reasonType" styleClass="content-field">
					<html:option value=''><cms:contentText key="SELECT_ONE" code="system.general"/></html:option>
					<html:options collection="commLogReasonTypesList" property="code" labelProperty="name"  />
				</html:select>
            </td>
          </tr>
 		  <tr class="form-blank-row">
            <td></td>
          </tr>	
          
           <tr class="form-row-spacer">
		     <beacon:label property="commentHistory" required="false" styleClass="content-field-label-top">
	              <cms:contentText key="COMMENT_HISTORY" code="communication_log.detail"/>
	         </beacon:label>
	    	<td class="content-field" valign="top">
	    		<html:textarea property="commentHistory" cols="70" rows="14" readonly="true" styleClass="content-field"/>
   	 	    </td>
	  	  </tr>
	  	  
	  	  <tr class="form-blank-row">
            <td></td>
          </tr>	
          
          <tr class="form-row-spacer">
		     <beacon:label property="newComment" required="true" styleClass="content-field-label-top">
	              <cms:contentText key="COMMENTS" code="communication_log.detail"/>
	         </beacon:label>
	    	<td class="content-field" valign="top">
	    		<html:textarea property="newComment" cols="70" rows="7" styleClass="content-field"/>
   	 	    </td>
	  	  </tr>
	  	  
	  	  <tr class="form-blank-row">
            <td></td>
          </tr>	
       
          <tr class="form-row-spacer">	   
            <beacon:label property="statusType" required="true">
              <cms:contentText key="STATUS" code="communication_log.detail"/>
            </beacon:label>	
            <td class="content-field"> 
                <table>	
         			<tr>
         				 <td>	             
							<html:select property="statusType" styleClass="content-field" onchange="hideOrDisplayField('assignedToLabel', this);">
								<html:option value=''><cms:contentText key="SELECT_ONE" code="system.general"/></html:option>
								<html:options collection="commLogStatusTypesList" property="code" labelProperty="name"  />
							</html:select>
							&nbsp;&nbsp;
						</td>	
						 <td colspan="3">
						<DIV id="assignedToLabel">	
							<table>
							<tr>
								<beacon:label required="false">
				        			<cms:contentText key="ASSIGNED_TO" code="communication_log.detail"/>
				      			</beacon:label>	
								<td class="content-field-review">
									<c:out value="${commLogForm.assignedToName}" />&nbsp;&nbsp;&nbsp;					
										<a href="" class="content-link" onclick="return selectUser('changeUpdateUser');"><cms:contentText key="CHANGE_USER" code="communication_log.detail"/></a>				
								</td>
							</tr>
							</table>	
						</DIV>	
						</td>						
					</tr>
				</table>
			</td>
          </tr> 
          
	  	  <tr class="form-blank-row">
            <td></td>
          </tr>	
                 
          <tr class="form-row-spacer">				  
            <beacon:label property="urgencyType" required="true">
              <cms:contentText key="URGENCY" code="communication_log.detail"/>
            </beacon:label>	
            <td class="content-field">              
				<html:select property="urgencyType" styleClass="content-field">
					<html:option value=''><cms:contentText key="SELECT_ONE" code="system.general"/></html:option>
					<%-- code Fix to Bug#17851 passing the urgency type whether escalated or open--%>
					<html:options collection="commLogUrgencyTypesList" property="code" labelProperty="name"  />
				</html:select>
				<html:hidden property="urgencyTypeInitial" value=""/>				
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
            <html:submit styleClass="content-buttonstyle" onclick="setDispatch('update')">
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
   <%-- code Fix to Bug#17851 passing the commLogList--%>
   <INPUT type=hidden name=commLogList value=<c:out value="${commLogList}" /> /> 
</html:form>  
	<%-- code Fix to Bug#17851 passing the FormName--%>
  <script TYPE="text/javascript">
     document.commLogForm.urgencyTypeInitial.value = document.commLogForm.urgencyType.value;
  </script>

  <c:if test="${commLogForm.statusType == 'closed' || commLogForm.statusType == 'deferred' }">
  <script TYPE="text/javascript">
     hideLayer("assignedToLabel");
  </script>
</c:if>       
	