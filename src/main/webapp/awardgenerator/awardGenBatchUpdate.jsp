<%@ include file="/include/taglib.jspf"%>

<%@ page import="java.util.*" %>

<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI( request )%>/assets/g4skin/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI( request )%>/assets/g4skin/scripts/calendar/calendar-en.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI( request )%>/assets/g4skin/scripts/calendar/calendar-setup.js"></script>



<div id="main2"> 
<html:form action="awardGeneratorBatchUpdate.do?method=launchBatch">
    <html:hidden property="method" />
    <html:hidden property="awardGeneratorId"/>
    <html:hidden property="awardType"/>
    <html:hidden property="awardActive"/>       

	<table border="0" cellpadding="10" cellspacing="0" width="100%">
		<tr class="form-row-spacer"><td></td></tr> 
		<tr class="form-row-spacer">
			<td><span class="headline"><cms:contentText
						key="UPDATE_TITLE" code="awardgenerator.batch" />
			</span></td>
			<td></td>
		</tr>
   		<tr class="form-row-spacer">
	   		<td>
	   			<table>
					<tr>
					<beacon:label property="awardGenBatchId" required="true" styleClass="content-field">
						<cms:contentText key="BATCH_DATE_SELECTION" code="awardgenerator.batch" />
					</beacon:label> 
					<td class="content-field">
						<html:select property="awardGenBatchId" styleId="awardGenBatchId"  styleClass="content-field killme" >
						<html:option value=""><cms:contentText key="SELECT_ONE" code="system.general"/></html:option>
							<c:if test="${fn:length(batchList)!=0}">
								<html:options collection="batchList" property="id" labelProperty="value" />
							</c:if>
						</html:select>
					</td>
	   				<td align="left">
		          		<html:submit styleClass="content-buttonstyle" onclick="setActionAndDispatch('awardGeneratorBatchUpdate.do','launchBatch')">
		          	  	 <cms:contentText code="awardgenerator.batch" key="UPDATE_BTN" />
		         	  	</html:submit>
	        		 </td>
	        		 </tr>
					 <tr class="form-blank-row">
						<td colspan="3">&nbsp;</td>
					 </tr>
					</table>
	        </td>
	 	</tr>

   	</table>
</html:form>
</div>

