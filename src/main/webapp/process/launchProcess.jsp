<%--UI REFACTORED--%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-en.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-setup.js"></script>

<script type="text/javascript">
<!--

//-->
</script>

<html:form styleId="contentForm" action="/processConfirm">
	<html:hidden property="method" />
	<html:hidden property="processBeanName" />
	<beacon:client-state>
		<beacon:client-state-entry name="processId" value="${processLaunchForm.processId}"/>
	</beacon:client-state>
	
	<INPUT TYPE=HIDDEN NAME="countOfParameters" value="<c:out value="${countOfParameters}"/>">

 <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="LAUNCH_PROCESS" code="process.launch"/>
        </span>
        <%-- Subheadline --%>
        <br/>
        <span class="subheadline">
			<c:out value="${process.name}" />			               
        </span>
        <%-- End Subheadline --%>

        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="INSTRUCTIONAL_COPY" code="process.launch"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
     	
        <cms:errors/>
        
        <%-- Start Input Example  --%>
        <table>
          <tr class="form-blank-row">
            <td></td>
          </tr>	  
          <%-- Process Parameter Values --%>
      	  <%@ include file="/process/processParameterValueInput.jspf"%>
                  
         <%--BUTTON ROWS ... For Input--%>
         <tr class="form-buttonrow">
           <td></td>
           <td></td>
           <td align="left">
             <beacon:authorize ifNotGranted="LOGIN_AS">
			 <html:submit styleClass="content-buttonstyle">
			  <cms:contentText code="system.button" key="SUBMIT" />
			 </html:submit>     
             </beacon:authorize>
             
            <html:cancel styleClass="content-buttonstyle" onclick="setActionAndDispatch('processLaunch.do','launch');">
				<cms:contentText code="system.button" key="CANCEL" />
			</html:cancel>          
           </td>
         </tr>
         <%--END BUTTON ROW--%>
      
        </table>
        <%-- End Inputs  --%>
      </td>
     </tr>        
   </table>
</html:form>
