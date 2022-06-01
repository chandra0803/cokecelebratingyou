<%--UI REFACTORED--%>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

<html:form styleId="contentForm" action="audienceCopy">
	<html:hidden property="method" value="copy"/>
	<beacon:client-state>
		<beacon:client-state-entry name="audienceId" value="${audienceCopyForm.audienceId}"/>
	</beacon:client-state>
	
	<table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <%-- Subheadline --%>
        <br/>
        <span class="subheadline"><c:out value="${audience.name}"/></span>
        <%-- End Subheadline --%>        

        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="COPY_AUDIENCE_INSTRUCTION" code="participant.list.builder.details"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
     	
        <cms:errors/>
        
        <table>
	
    		<tr class="form-row-spacer">				  
                <beacon:label property="newAudienceName" required="true">
                    <cms:contentText key="NEW_AUDIENCE_NAME_LABEL" code="participant.list.builder.details"/>
                </beacon:label>	
             <td class="content-field">
              <html:hidden property="saveAudienceDefinition" value="true"/>
              <html:text property="newAudienceName" value="" maxlength="30" styleClass="content-field"/>
             </td>
        </tr>					
			
		   <tr class="form-buttonrow">
           <td></td>
           <td></td>
           <td align="left" colspan="3">
             <beacon:authorize ifNotGranted="LOGIN_AS">
                <html:submit styleClass="content-buttonstyle" onclick="setDispatch('copy')" >
				           <cms:contentText key="SAVE" code="promotion.copy"/>
			          </html:submit>	
             </beacon:authorize>
             <html:cancel styleClass="content-buttonstyle" onclick="setDispatch('cancel')">
								<cms:contentText code="system.button" key="CANCEL" />
			 			 </html:cancel>
           </td>
         </tr>
         </table>		  
		  	</td>
     </tr>        
   </table>
</html:form>