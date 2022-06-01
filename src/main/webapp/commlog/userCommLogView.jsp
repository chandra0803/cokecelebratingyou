<%--UI REFACTORED--%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.ui.commlog.CommLogForm"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ include file="/include/taglib.jspf"%>

<script type="text/javascript">
<!--
function maintainUserCommLog( method, action )
{
	document.commLogForm.method.value=method;	
	document.commLogForm.action = action;
	document.commLogForm.submit();
}
//-->
</script>
<html:form styleId="contentForm" action="userCommLogMaintainUpdate">
	<html:hidden property="method" value="display"/>
	<beacon:client-state>
	 	<beacon:client-state-entry name="userId" value="${commLogForm.userId}"/>
	 	<beacon:client-state-entry name="commLogId" value="${commLogForm.commLogId}"/>
	 	<beacon:client-state-entry name="assignedToUserId" value="${commLogForm.assignedToUserId}"/>
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
            <beacon:label property="sourceType" required="false">
              <cms:contentText key="SOURCE" code="communication_log.detail"/>
            </beacon:label>	
            <td class="content-field">              
				<c:out value="${commLogForm.sourceDesc}" />
            </td>
          </tr>
 		  <tr class="form-blank-row">
            <td></td>
          </tr>	 
          
           <tr class="form-row-spacer">				  
            <beacon:label property="categoryType" required="false">
              <cms:contentText key="CATEGORY" code="communication_log.detail"/>
            </beacon:label>	
            <td class="content-field">              
				<c:out value="${commLogForm.categoryDesc}" /> 
            </td>
          </tr>
 		  <tr class="form-blank-row">
            <td></td>
          </tr>	 
          
          <tr class="form-row-spacer">				  
            <beacon:label property="reasonType" required="false">
              <cms:contentText key="REASON" code="communication_log.detail"/>
            </beacon:label>	
            <td class="content-field">              
				<c:out value="${commLogForm.reasonDesc}" /> 
				
				  <c:choose>
						<c:when test="${commLogForm.sourceType == 'sysgen'}">
							&nbsp;&nbsp;&nbsp;
							<%  Map paramMap = new HashMap();
									CommLogForm temp = (CommLogForm)request.getAttribute("commLogForm");
									paramMap.put( "actionType", "view" );
									//code Fix to Bug#16854 put the valueOf commLogId long value as String
									paramMap.put( "commLogId", String.valueOf(temp.getCommLogId()));
									paramMap.put( "backUrl",RequestUtils.getOriginalRequestURIWithQueryString(request));
									pageContext.setAttribute("viewUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), "/participant/userCommLogMessageDisplay.do", paramMap ) );
							%>
				     	<a href="<c:out value='${viewUrl}'/>" class="content-link" align="right">
								<cms:contentText key="VIEW_MESSAGE" code="communication_log.detail"/>
						</a>&nbsp; 
							<% 	paramMap.remove( "actionType" );
									paramMap.put( "actionType", "send" );
									pageContext.setAttribute("sendUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), "/participant/userCommLogMessageDisplay.do", paramMap ) ); 
							%>
				     	<a href="<c:out value='${sendUrl}'/>" class="content-link" align="right">
								<cms:contentText key="SEND_MESSAGE" code="communication_log.detail"/>
						</a> 
						</c:when>
						<c:otherwise>
						&nbsp;
						</c:otherwise>
				  </c:choose>
				
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
	    		<html:textarea property="commentHistory" cols="50" rows="5" readonly="true" styleClass="content-field"/>
   	 	    </td>
	  	  </tr>
	  	  
	  	  <tr class="form-blank-row">
            <td></td>
          </tr>	
          
          <tr class="form-row-spacer">				  
            <beacon:label property="statusType" required="false">
              <cms:contentText key="STATUS" code="communication_log.detail"/>
            </beacon:label>	
            <td class="content-field">              
				<c:out value="${commLogForm.statusDesc}" /> 
            </td>
          </tr>
 		  <tr class="form-blank-row">
            <td></td>
          </tr>	 
          
          <tr class="form-row-spacer">				  
            <beacon:label property="urgencyType" required="false">
              <cms:contentText key="URGENCY" code="communication_log.detail"/>
            </beacon:label>	
            <td class="content-field">    
            	<c:out value="${commLogForm.urgencyDesc}" />          
            </td>
          </tr>
 		  <tr class="form-blank-row">
            <td></td>
          </tr>	
          
         <tr>
			<td class="content-field-label"></td>
			<td class="content-field-label">
				<cms:contentText key="ASSIGNED_TO" code="communication_log.detail"/>
			</td>
			<td class="content-field-review">
				<c:out value="${commLogForm.assignedToName}" />
			</td>
		</tr>
          	
	
		<tr class="form-buttonrow">
           <td></td>
           <td></td>
           <td align="left">
                <c:choose>
                  <c:when test="${param.from eq 'transactionhistory'}">
            	    <html:button property="BackToForm2" styleClass="content-buttonstyle" onclick="callUrl('${backUrl }');">
						<cms:contentText key="BACK_BUTTON_RECOGNITION_DETAIL" code="communication_log.detail"/>
				    </html:button>
                  </c:when>
                  <c:otherwise>
            	    <html:button property="BackToForm" styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('userCommLogListDisplay.do', 'displayList');">
						<cms:contentText key="BACK_BUTTON" code="communication_log.detail"/>
				    </html:button>
				  </c:otherwise>
				</c:choose>
           </td>
         </tr>
		</table>
        
        
      </td>
     </tr>        
   </table>
</html:form>  
       
	