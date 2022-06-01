<%@ include file="/include/taglib.jspf"%>

<%@ page import="java.util.*" %>

<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI( request )%>/assets/g4skin/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI( request )%>/assets/g4skin/scripts/calendar/calendar-en.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI( request )%>/assets/g4skin/scripts/calendar/calendar-setup.js"></script>



<div id="main1"> 
<html:form styleId="batchForm" action="awardGeneratorBatchGenerate.do?method=launchBatch">
    <html:hidden property="method" />
    <html:hidden property="awardGeneratorId"/>
    <html:hidden property="promotionId"/>
    <html:hidden property="awardType"/>
    <html:hidden property="awardActive"/>   
	
	<table border="0" cellpadding="10" cellspacing="0" width="100%">
		<tr class="form-row-spacer">
			<td><span class="headline"><cms:contentText
						key="LAUNCH_TITLE" code="awardgenerator.batch" />
			</span></td>
			<td></td>
		</tr>

		<tr class="form-row-spacer"><td></td></tr> 
		<tr class="form-row-spacer">
			<td>
				<table>
					<tr>
						<beacon:label property="useIssueDate" required="true"
							styleClass="content-field">
							<cms:contentText key="SEND_DATE_OPTION" code="awardgenerator.batch" />
						</beacon:label>
					</tr>
					<tr>
						<beacon:label property="startDate" required="true"
							styleClass="content-field">
							<cms:contentText key="START_DATE" code="awardgenerator.batch" />
						</beacon:label>
						<td class="content-field"><html:text property="startDate"
								styleId="startDate" size="10" maxlength="10"
								styleClass="content-field" onfocus="clearDateMask(this);"
								readonly="true" /> <img id="startDateTrigger"
							src="<%=RequestUtils.getBaseURI( request )%>/assets/g4skin/images/calendar_icon.gif"
							class="calendar-icon"
							alt="<cms:contentText key='START_DATE' code='awardgenerator.batch'/>" />
						</td>

						<beacon:label property="endDate" required="true"
							styleClass="content-field">
							<cms:contentText key="END_DATE" code="awardgenerator.batch" />
						</beacon:label>
						<td class="content-field"><html:text property="endDate"
								styleId="endDate" size="10" maxlength="10"
								styleClass="content-field" onfocus="clearDateMask(this);"
								readonly="true" /> <img id="endDateTrigger"
							src="<%=RequestUtils.getBaseURI( request )%>/assets/g4skin/images/calendar_icon.gif"
							class="calendar-icon"
							alt="<cms:contentText key='END_DATE' code='awardgenerator.batch'/>" />
						</td>
					</tr>
				</table></td>
			<td></td>
		</tr>

		<%-- Issue Date Active --%>
		<tr class="form-row-spacer">
			<td>
				<table>
					<tr>
						<beacon:label property="useIssueDate" required="true"
							styleClass="content-field">
							<cms:contentText key="SEND_DATE_OPTION" code="awardgenerator.batch" />
						</beacon:label>
					</tr>
				</table>
			</td>
			</tr>
			<tr>
			<td class="content-field">
				<table>
					<tr>
						<td class="content-field"><html:radio
								styleId="useIssueDateFalse" property="useIssueDate"
								value="false" /> <cms:contentText code="awardgenerator.batch"
								key="ACTUAL_DATE" /></td>
					</tr>
					<tr>
						<td class="content-field"><html:radio
								styleId="useIssueDateTrue" property="useIssueDate" value="true" />
								<cms:contentText code="awardgenerator.batch"
								key="SEND_RECOGNITION_DATE" /></td>
						<td class="content-field"><html:text property="issueDate"
								styleId="issueDate" size="10" maxlength="10"
								styleClass="content-field" onfocus="clearDateMask(this);"
								readonly="true" /> <img id="issueDateTrigger"
							src="<%=RequestUtils.getBaseURI( request )%>/assets/g4skin/images/calendar_icon.gif"
							class="calendar-icon"
							alt="<cms:contentText key='SEND_RECOGNITION_DATE' code='awardgenerator.batch'/>" />
						</td>
						<td align="left">
		            	<div align="center">
							<html:submit styleClass="content-buttonstyle"
										onclick="setActionAndDispatch('awardGeneratorBatchGenerate.do','launchBatch')">
								<cms:contentText code="awardgenerator.batch" key="LAUNCH_BTN" />
							</html:submit>
						</div>
					</td>
					</tr>
					<tr class="form-buttonrow">
		            <td></td>
				    </tr>					
				</table>
			</td>
		</tr>
	</table>

</html:form>
</div>

<script type="text/javascript">
	Calendar.setup(
	{
	  inputField  : "startDate",       	// ID of the input field
	  ifFormat    : "%m/%d/%Y",    		// the date format
	  button      : "startDateTrigger"  // ID of the button
	});
	Calendar.setup(
	{
	  inputField  : "endDate",       	// ID of the input field
	  ifFormat    : "%m/%d/%Y",    		// the date format
	  button      : "endDateTrigger"    // ID of the button
	});
	Calendar.setup(
	{
	  inputField  : "issueDate",       	// ID of the input field
	  ifFormat    : "%m/%d/%Y",    		// the date format
	  button      : "issueDateTrigger"  // ID of the button
	});	
</script>

