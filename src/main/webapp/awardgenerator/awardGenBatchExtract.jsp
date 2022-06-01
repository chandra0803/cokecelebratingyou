<%@ include file="/include/taglib.jspf"%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

<div id="main3"> 
<html:form action="awardGeneratorBatchExtract.do?method=extractBatch">
    <html:hidden property="method" />
    <html:hidden property="awardGeneratorId"/>
    <html:hidden property="awardActive"/>       
    
	<table border="0" cellpadding="10" cellspacing="0" width="100%">
		<tr class="form-row-spacer"><td></td></tr> 
		<tr class="form-row-spacer">
			<td><span class="headline"><cms:contentText
						key="GENERATE_EXTRACT_TITLE" code="awardgenerator.batch" />
			</span></td>
			<td></td>
		</tr>
   		<tr class="form-row-spacer">
	   		<td>
	   			<table>
					<tr class="form-blank-row">
						<beacon:label property="awardGenBatchId" required="true" styleClass="content-field">
							<cms:contentText key="BATCH_DATE_SELECTION" code="awardgenerator.batch" />
						</beacon:label> 
						<td class="content-field">
							<html:select property="awardGenBatchId" styleId="awardGenBatchId" styleClass="content-field killme" >
							<html:option value=""><cms:contentText key="SELECT_ONE" code="system.general"/></html:option>
								<c:if test="${fn:length(extractBatchList)!=0}">
									<html:options collection="extractBatchList" property="id" labelProperty="value" />
								</c:if>
							</html:select>
						</td>
		   				<td align="left">
			          		<html:submit styleClass="content-buttonstyle" onclick="setActionAndDispatch('awardGeneratorBatchExtract.do','extractBatch')">
			          	  	 <cms:contentText code="awardgenerator.batch" key="GENERATE_EXTRACT_BTN" />
			         	  	</html:submit>
		        		 </td>		        		 
	        		 </tr>
					<tr class="form-blank-row">
						<td colspan="3">&nbsp;</td>
					</tr>
					<tr class="form-buttonrow">
		            <td></td>
		            <td></td>
		            <td align="right">
		            <div align="right">
			         	  	<html:button property="cancelBtn" styleClass="content-buttonstyle" onclick="setDispatchAndSubmit('cancel')">
		             				 <cms:contentText code="system.button" key="CANCEL" />
		            		</html:button>
		            </div>
		       		</td>
			     </tr>

	        	</table>
	        </td>
	 	</tr>
	 	
   	</table>
</html:form>
</div>


