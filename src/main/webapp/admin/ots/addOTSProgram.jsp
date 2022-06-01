<%@ include file="/include/taglib.jspf"%>
<html:form styleId="addOtsProgramForm" action="/addOTSProgram">
<html:hidden property="method" value="addProgram" />       
 
 <table border="0" cellpadding="10" cellspacing="0" width="100%"> 
    <tr>
      <td>
        <span class="headline"><cms:contentText key="PROGRAM_BASICS" code="ots.settings.info"/></span>        
        <br/><br/>
        
        <cms:errors/>

        <%--INSTRUCTIONS--%>
        
        <span class="content-instruction">
          <cms:contentText key="PROGRAM_TITLE_MESSAGE" code="ots.settings.info"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>   	
                
        <%-- Start Input Example  --%>
        <table>
          <tr class="form-row-spacer">					  
		  <beacon:label property="promotionType" required="true">
  			<cms:contentText key="PROGRAM_NUMBER" code="ots.settings.info"/>
		  </beacon:label>	
		  <td class="content-field">
			<html:text property="programNumber" size="10" maxlength="5" styleClass="content-field"/>
		  </td>
		</tr>
		<tr class="form-blank-row">
		  <td></td>
		</tr>
		<%-- End Start Input Example  --%>		

	
		<%--BUTTON ROWS ... only display these two rows if there are buttons to be displayed.--%>
        <tr class="form-buttonrow">
	    	<td></td>
	        <td></td>
	        <td align="left">           
	          	<html:submit styleClass="content-buttonstyle" onclick="setDispatch('forward')">
				  <cms:contentText code="system.button" key="START" />
				</html:submit>
            	
			<html:button property="homePageButton" styleClass="content-buttonstyle" onclick="callUrl('${pageContext.request.contextPath}/ots/otsAdministration.do')">
				<cms:contentText code="system.button" key="CANCEL" />
			</html:button> 
	        </td>
        </tr>
		<%--END BUTTON ROW--%>
	
   	   </table>
     </td>
   </tr>	
</table>
</html:form>
	
