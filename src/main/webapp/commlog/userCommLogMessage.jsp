<%--UI REFACTORED--%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.ui.commlog.CommLogForm"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ include file="/include/taglib.jspf"%>

<html:form styleId="contentForm" action="userCommLogMessageDisplay">
	<html:hidden property="method" value="message"/>
	<beacon:client-state>
	 	<beacon:client-state-entry name="commLogId" value="${commLogForm.commLogId}"/>	 	
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="MESSAGE_TITLE" code="communication_log.detail"/></span>
        <br/>
        <beacon:username userId="${displayNameUserId}"/>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="MESSAGE_ADD_INFO" code="communication_log.detail"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>

        
  		<table>
         
 		  <tr class="form-blank-row">
            <td></td>
          </tr>	
          
           <tr class="form-row-spacer">
           
		     <beacon:label property="message" required="false" styleClass="content-field-label-top">
	              <cms:contentText key="MESSAGE" code="communication_log.detail"/>
	         </beacon:label>
	    	<td class="content-field-review" valign="top">
	    		<textarea cols="50" rows="10" readonly="true" styleClass="content-field" id="message" name="message"><c:out value="${message}"/></textarea>	   
   	 	    </td>
	  	  </tr>
	  	  
	  	  <tr class="form-blank-row">
            <td></td>
          </tr>	
          
		<tr class="form-buttonrow">
           <td></td>
           <td></td>
           <td align="center">
           			<%  Map paramMap = new HashMap();
						CommLogForm temp = (CommLogForm)request.getAttribute("commLogForm");
						paramMap.put( "commLogId", temp.getCommLogId() );						
					%>
				<c:if test="${actionType == 'send'}">
					<%  pageContext.setAttribute("sendUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), "/participant/userCommLogMessageDisplay.do?method=sendMessage", paramMap ) );
					%>								
				    <html:submit styleClass="content-buttonstyle" onclick="setActionAndDispatch('${sendUrl}','');">
						<cms:contentText code="communication_log.detail" key="SEND_MESSAGE_BUTTON" />
					</html:submit>
				</c:if>
      				
				<%	
				  String fromParam = RequestUtils.getOptionalParamString(request,"from");
				  if ("transactionhistory".equals(fromParam))
				  {
				    Map clientStateMap = ClientStateUtils.getClientStateMap( request );
				    String backUrl = ClientStateUtils.getParameterValue(request,clientStateMap, "backUrl");
				    if (backUrl != null) {
					  pageContext.setAttribute("backUrl", backUrl );   					
				    } else {
				    pageContext.setAttribute("backUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), "/participant/userCommLogDisplay.do?method=prepareDisplay", paramMap ) );
				    }
				  }
				  else
				  {
				  	pageContext.setAttribute("backUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), "/participant/userCommLogDisplay.do?method=prepareDisplay", paramMap ) );
				  }
   				%>
				<html:submit property="back" styleClass="content-buttonstyle" onclick="setActionAndDispatch('${backUrl}','');" >			
					<cms:contentText code="system.button" key="CANCEL" />
				</html:submit>
           </td>
         </tr>
		</table>
               
      </td>
     </tr>        
   </table> 
</html:form>   
	