<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>

<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>


<table border="0" cellpadding="10" cellspacing="0" width="100%"> 
    <tr>
      <td>
        <span class="headline"><cms:contentText key="PROGRAM_BASICS" code="ots.settings.info"/></span>        
        <br/><br/>
        
        <cms:errors/>         	
                
        <table>
        
          <tr class="form-row-spacer">					  
		  <td class="content-field">
			<span class="subheadline"><cms:contentText key="PROGRAM_NUMBER" code="ots.settings.info"/></span>			 
		  </td>	
		  <td class="content-field">
			<span class="subheadline"><c:out value="${otsProgramDetails.programNumber}" /></span>
		  </td>
		</tr>
		<tr class="form-blank-row">
		  <td></td>
		</tr>
		
		<tr class="form-row-spacer">					  
		  <td class="content-field">
			<span class="subheadline"><cms:contentText key="CLIENT" code="ots.settings.info"/></span>
		  </td>	
		  <td class="content-field">
			<span class="subheadline"><c:out value="${otsProgramDetails.clientName}" /></span>
		  </td>
		</tr>
		<tr class="form-blank-row">
		  <td></td>
		</tr>
		
		<tr class="form-row-spacer">					  
		  <td class="content-field">
			<span class="subheadline"><cms:contentText key="PROGRAM_DESCRIPTION" code="ots.settings.info"/></span>
		  </td>	
		  <td class="content-field">
			<span class="subheadline"><c:out value="${otsProgramDetails.programDescription}" /></span>
		  </td>
		</tr>
		<tr class="form-blank-row">
		  <td></td>
		</tr>
		
		<tr class="form-row-spacer">					  
		  <td class="content-field">
		  </br>
			<span class="headline"><cms:contentText key="BATCH_DETAILS" code="ots.settings.info"/></span>
		  </td>			 
		</tr>
		<tr class="form-blank-row">
		  <td></td>
		</tr>
		
		<tr class="form-row-spacer">					  
		  <td class="content-field">
			<b><cms:contentText key="TABLE_BEFORE_HEADING" code="ots.settings.info"/></b>
		  </td>			 
		</tr>
		<tr class="form-blank-row">
		  <td></td>
		</tr>
			
   	   </table>
   	   
     </td>
   </tr>	
</table>


 <table width="100%">
		      <tr>
		        <td align="right">		
		        	  <display:table defaultsort="1" defaultorder="ascending" name="batches" id="batches" sort="list" pagesize="50" requestURI="/ots/addOTSProgram.do?method=addProgram" excludedParams="method">
		        	 	 <c:set  var="batchNbr" value="${batches.batchNumber}"/>
		        	 	 <c:set var="programNbr" value="${otsProgramDetails.programNumber}"/>
		        	  		<% 
								Map paramMap = new HashMap();
								paramMap.put("batchNumber", pageContext.getAttribute("batchNbr"));
								paramMap.put("programNumber",pageContext.getAttribute("programNbr"));
								
								pageContext.setAttribute("updateBatch", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), "/ots/otsBatchUpdate.do?method=updateBatch&batchNumber="+ pageContext.getAttribute("batchNbr")+"&programNumber="+pageContext.getAttribute("programNbr"), paramMap ) );
							%>
					  <display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
				   		</display:setProperty>						
						<display:column  titleKey="ots.settings.info.BATCH_NUMBER" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true">
							<a href="<c:out value="${updateBatch}"/>"> <c:out value="${batchNbr}"/> </a>
						</display:column>										
						<display:column  titleKey="ots.settings.info.BATCH_DESCRIPTION" headerClass="crud-table-header-row" class="crud-content left-align nowrap">
							<c:forEach items="${batches.batchDescription}" var="desc">
								<c:if test="${desc.locale == 'en_US'}">   
									<c:out value="${desc.cmText}" />
								</c:if>
							</c:forEach>
						</display:column>												
						<display:column titleKey="ots.settings.info.BILL_CODE_1" headerClass="crud-table-header-row" class="crud-content left-align nowrap">
						 			<c:set var="count" value="0"/>
						 			<c:set var="charName" value=""/>
						 			<c:forEach items="${characteristics}" var="characteristic">
						 				<c:if test="${batches.OTSBillCodes[0].billCode eq characteristic.charId}">
						 					<c:set var="charName" value="${characteristic.characteristicName}"/>
						 					<c:set var="count" value="1"/>
						 				</c:if>
						 			</c:forEach>
						 			<c:if test="${count eq 0 }">
						 				<c:out value="${batches.OTSBillCodes[0].billCode}" />
						 			</c:if>
						 			<c:if test="${count ne 0 }">
						 				<c:out value="${charName}" />
						 			</c:if>
	        			</display:column>
						<display:column titleKey="ots.settings.info.BILL_CODE_2" headerClass="crud-table-header-row" class="crud-content left-align nowrap">						 	
									<c:set var="count" value="0"/>
						 			<c:set var="charName" value=""/>
						 			<c:forEach items="${characteristics}" var="characteristic">
						 				<c:if test="${batches.OTSBillCodes[1].billCode eq characteristic.charId}">
						 					<c:set var="charName" value="${characteristic.characteristicName}"/>
						 					<c:set var="count" value="1"/>
						 				</c:if>
						 			</c:forEach>
						 			<c:if test="${count eq 0 }">
						 				<c:out value="${batches.OTSBillCodes[1].billCode}" />
						 			</c:if>
						 			<c:if test="${count ne 0 }">
						 				<c:out value="${charName}" />
						 			</c:if>
	        			</display:column>				  
	        			<display:column titleKey="ots.settings.info.BILL_CODE_3" headerClass="crud-table-header-row" class="crud-content left-align nowrap">						 	
									<c:set var="count" value="0"/>
						 			<c:set var="charName" value=""/>
						 			<c:forEach items="${characteristics}" var="characteristic">
						 				<c:if test="${batches.OTSBillCodes[2].billCode eq characteristic.charId}">
						 					<c:set var="charName" value="${characteristic.characteristicName}"/>
						 					<c:set var="count" value="1"/>
						 				</c:if>
						 			</c:forEach>
						 			<c:if test="${count eq 0 }">
						 				<c:out value="${batches.OTSBillCodes[2].billCode}" />
						 			</c:if>
						 			<c:if test="${count ne 0 }">
						 				<c:out value="${charName}" />
						 			</c:if>
	        			</display:column>
	        			<display:column titleKey="ots.settings.info.BILL_CODE_4" headerClass="crud-table-header-row" class="crud-content left-align nowrap">						 		
									<c:set var="count" value="0"/>
						 			<c:set var="charName" value=""/>
						 			<c:forEach items="${characteristics}" var="characteristic">
						 				<c:if test="${batches.OTSBillCodes[3].billCode eq characteristic.charId}">
						 					<c:set var="charName" value="${characteristic.characteristicName}"/>
						 					<c:set var="count" value="1"/>
						 				</c:if>
						 			</c:forEach>
						 			<c:if test="${count eq 0 }">
						 				<c:out value="${batches.OTSBillCodes[3].billCode}" />
						 			</c:if>
						 			<c:if test="${count ne 0 }">
						 				<c:out value="${charName}" />
						 			</c:if>
	        			</display:column>
	        			<display:column titleKey="ots.settings.info.BILL_CODE_5" headerClass="crud-table-header-row" class="crud-content left-align nowrap">						 	
									<c:set var="count" value="0"/>
						 			<c:set var="charName" value=""/>
						 			<c:forEach items="${characteristics}" var="characteristic">
						 				<c:if test="${batches.OTSBillCodes[4].billCode eq characteristic.charId}">
						 					<c:set var="charName" value="${characteristic.characteristicName}"/>
						 					<c:set var="count" value="1"/>
						 				</c:if>
						 			</c:forEach>
						 			<c:if test="${count eq 0 }">
						 				<c:out value="${batches.OTSBillCodes[4].billCode}" />
						 			</c:if>
						 			<c:if test="${count ne 0 }">
						 				<c:out value="${charName}" />
						 			</c:if>										  
	        			</display:column>
	        			<display:column titleKey="ots.settings.info.BILL_CODE_6" headerClass="crud-table-header-row" class="crud-content left-align nowrap">						 	
									<c:set var="count" value="0"/>
						 			<c:set var="charName" value=""/>
						 			<c:forEach items="${characteristics}" var="characteristic">
						 				<c:if test="${batches.OTSBillCodes[5].billCode eq characteristic.charId}">
						 					<c:set var="charName" value="${characteristic.characteristicName}"/>
						 					<c:set var="count" value="1"/>
						 				</c:if>
						 			</c:forEach>
						 			<c:if test="${count eq 0 }">
						 				<c:out value="${batches.OTSBillCodes[5].billCode}" />
						 			</c:if>
						 			<c:if test="${count ne 0 }">
						 				<c:out value="${charName}" />
						 			</c:if>
						</display:column>
	        			<display:column titleKey="ots.settings.info.BILL_CODE_7" headerClass="crud-table-header-row" class="crud-content left-align nowrap">						 	
									<c:set var="count" value="0"/>
						 			<c:set var="charName" value=""/>
						 			<c:forEach items="${characteristics}" var="characteristic">
						 				<c:if test="${batches.OTSBillCodes[6].billCode eq characteristic.charId}">
						 					<c:set var="charName" value="${characteristic.characteristicName}"/>
						 					<c:set var="count" value="1"/>
						 				</c:if>
						 			</c:forEach>
						 			<c:if test="${count eq 0 }">
						 				<c:out value="${batches.OTSBillCodes[6].billCode}" />
						 			</c:if>
						 			<c:if test="${count ne 0 }">
						 				<c:out value="${charName}" />
						 			</c:if>								  
	        			</display:column>
	        			<display:column titleKey="ots.settings.info.BILL_CODE_8" headerClass="crud-table-header-row" class="crud-content left-align nowrap">						 	 
									<c:set var="count" value="0"/>
						 			<c:set var="charName" value=""/>
						 			<c:forEach items="${characteristics}" var="characteristic">
						 				<c:if test="${batches.OTSBillCodes[7].billCode eq characteristic.charId}">
						 					<c:set var="charName" value="${characteristic.characteristicName}"/>
						 					<c:set var="count" value="1"/>
						 				</c:if>
						 			</c:forEach>
						 			<c:if test="${count eq 0 }">
						 				<c:out value="${batches.OTSBillCodes[7].billCode}" />
						 			</c:if>
						 			<c:if test="${count ne 0 }">
						 				<c:out value="${charName}" />
						 			</c:if>								
	        			</display:column>
	        			<display:column titleKey="ots.settings.info.BILL_CODE_9" headerClass="crud-table-header-row" class="crud-content left-align nowrap">						 	
									<c:set var="count" value="0"/>
						 			<c:set var="charName" value=""/>
						 			<c:forEach items="${characteristics}" var="characteristic">
						 				<c:if test="${batches.OTSBillCodes[8].billCode eq characteristic.charId}">
						 					<c:set var="charName" value="${characteristic.characteristicName}"/>
						 					<c:set var="count" value="1"/>
						 				</c:if>
						 			</c:forEach>
						 			<c:if test="${count eq 0 }">
						 				<c:out value="${batches.OTSBillCodes[8].billCode}" />
						 			</c:if>
						 			<c:if test="${count ne 0 }">
						 				<c:out value="${charName}" />
						 			</c:if>								
	        			</display:column>
	        			<display:column titleKey="ots.settings.info.BILL_CODE_10" headerClass="crud-table-header-row" class="crud-content left-align nowrap">
						 	<c:set var="count" value="0"/>
						 			<c:set var="charName" value=""/>
						 			<c:forEach items="${characteristics}" var="characteristic">
						 				<c:if test="${batches.OTSBillCodes[9].billCode eq characteristic.charId}">
						 					<c:set var="charName" value="${characteristic.characteristicName}"/>
						 					<c:set var="count" value="1"/>
						 				</c:if>
						 			</c:forEach>
						 			<c:if test="${count eq 0 }">
						 				<c:out value="${batches.OTSBillCodes[9].billCode}" />
						 			</c:if>
						 			<c:if test="${count ne 0 }">
						 				<c:out value="${charName}" />
						 			</c:if>
	        			</display:column>
					  </display:table>
					  
				</td>
		 </tr>
</table>

<table>
	<tr>
		<td>
		<html:form styleId="addOtsProgramForm" action="/addOTSProgram">
			<html:hidden property="programNumber" value="${otsProgramDetails.programNumber}"/> 
			<html:hidden property="clientName" value="${otsProgramDetails.clientName}"/>
			<html:hidden property="description" value="${otsProgramDetails.programDescription}"/>
			<html:hidden property="method" value="displayAudience"/>
			<html:submit property="saveAndContinue" styleClass="content-buttonstyle" onclick="setActionAndDispatch('addOTSProgram.do?method=displayAudience')">
                      <cms:contentText code="system.button" key="SAVE_CONTINUE" />
         	 </html:submit>
         	 </html:form>
        </td>
        <td>
        <html:form styleId="addOtsProgramForm" action="/addOTSProgram">
			<html:hidden property="programNumber" value="${otsProgramDetails.programNumber}"/> 
			<html:hidden property="description" value="${otsProgramDetails.programDescription}"/>
			<html:hidden property="clientName" value="${otsProgramDetails.clientName}"/> 
			<html:hidden property="method" value="overView"/>
			<html:submit property="saveAndContinue" styleClass="content-buttonstyle" onclick="setActionAndDispatch('addOTSProgram.do?method=overView')">
                      <cms:contentText code="system.button" key="SAVE_EXIT" />
            </html:submit>
            </html:form>
        </td>
        <td>
         <html:form styleId="addOtsProgramForm" action="/addOTSProgram">
         <html:hidden property="programNumber" value="${otsProgramDetails.programNumber}"/> 
			<html:hidden property="description" value="${otsProgramDetails.programDescription}"/>
			<html:hidden property="clientName" value="${otsProgramDetails.clientName}"/> 
			<html:hidden property="method" value="back"/>
         	<html:submit property="saveAndContinue" styleClass="content-buttonstyle" onclick="setActionAndDispatch('addOTSProgram.do?method=back')">
                      <cms:contentText code="system.button" key="BACK" />
            </html:submit>
         </html:form>
        </td>
	</tr>

</table>
