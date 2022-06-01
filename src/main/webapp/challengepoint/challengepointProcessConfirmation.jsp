<%--UI REFACTORED--%>
<%@ include file="/include/taglib.jspf"%>

	<table border="0" cellpadding="10" cellspacing="0" width="100%">
		<tr>
		  <td>
			<span class="headline"><cms:contentText key="TITLE" code="promotion.challengepointcalc.confirmation"/></span>
			<br/>
    	 	<span class="content-instruction">
				<b><c:out value="${promotionName}"/></b>
	     	</span>		
	     	<logic:messagesNotPresent> 
			<%--INSTRUCTIONS--%>
			<br/><br/>
    	 	<span class="content-instruction">
				<cms:contentText key="CONFIRMATION_MESSAGE" code="promotion.challengepointcalc.confirmation"/>
     		</span>
	     	<br/><br/>
    	 	<%--END INSTRUCTIONS--%>
    	 	</logic:messagesNotPresent>
     		<cms:errors/>
		</td>
		</tr>    
		<tr align="center">
			<td>
      			<html:submit styleClass="content-buttonstyle" onclick="callUrl('awardListDisplay.do')" >
					<cms:contentText key="BACK_TO_CP_LIST" code="promotion.challengepointcalc.confirmation"/>
				</html:submit>	
       	  	</td>
   		</tr>
	</table>    		


