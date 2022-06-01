<%--UI REFACTORED--%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>
<script type="text/javascript">
<!--

//-->
</script>
<html:form styleId="contentForm" action="/processList">

 <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="PROCESS_LAUNCHED" code="process.confirmation"/>
        </span>
        <%-- Subheadline --%>
        <br/>
        <span class="subheadline">
			<cms:contentText key="PROCESS_SUBMITTED_STATEMENT" code="process.confirmation"/>			               
        </span>
        <%-- End Subheadline --%>
     	
        <cms:errors/>
        
        <%-- Start Input Example  --%>
        <table>
          <tr class="form-row-spacer">				  
            <td class="content-field">
              <c:out value="${processLaunchForm.processName}"/>	
            </td>
          </tr>          
          <tr class="form-blank-row">
            <td></td>
          </tr>	   

          <tr class="form-blank-row">
            <td>
              <c:set var="parameterValueTDClass" value="content-field"/>
              <%@ include file="/process/processParameterValueDisplay.jspf"%>
            </td>
          </tr> 
                          
          <%-- 	<c:forEach items="${processLaunchForm.processParameterValueList}" var="parameter"> --%>
          
         <%--BUTTON ROWS ... For Input--%>
         <tr class="form-buttonrow">
           <td></td>
           <td></td>
           <td align="left">
			 <html:submit styleClass="content-buttonstyle">
			  <cms:contentText code="process.confirmation" key="BACK_TO_PROCESSES" />
			 </html:submit>
           </td>
         </tr>
         <%--END BUTTON ROW--%>
      
        </table>
        <%-- End Inputs  --%>
      </td>
     </tr>        
   </table>
</html:form>
