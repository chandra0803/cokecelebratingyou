<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="java.util.*"%>
<%@ page import="com.biperf.core.ui.promotion.PromotionAudienceFormBean"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>
<%@ include file="/include/taglib.jspf"%>

<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-en.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-setup.js"></script>


<script type="text/javascript">
</script>

<html:form styleId="contentForm" action="resendWelcomeEmail">
	<html:hidden property="method" />
	<html:hidden property="audienceListCount" />
	
	<table>
		<tr>
			<td>
				<span class="headline">
					<cms:contentText key="TITLE" code="admin.resend.welcome.email" />
				</span>
				<br /><br />
				
				<span class="content-instruction">
					<cms:contentText key="INSTRUCTIONS" code="admin.resend.welcome.email" />
				</span>
			</td>
		</tr>
	</table>
	
	<table>
		<tr>
			<td colspan="4"><cms:errors /></td>
		</tr>
		<c:if test="${resendWelcomeEmailForm.countSuccess == 'true'}">
		<tr>
			<td colspan="4"><cms:contentText code="admin.resend.welcome.email" key="COUNT_MESSAGE" /></td>
		</tr>
		</c:if>
		<c:if test="${resendWelcomeEmailForm.mailSuccess == 'true'}">
		   <td colspan="4"><cms:contentText code="admin.resend.welcome.email" key="MAIL_MESSAGE" /></td>
		</c:if>
		
		<tr>
			<td>
				<html:radio property="option" value="audienceDefinition" />
			</td>
			<td>&nbsp;</td>
			<td class="content-field-label">
				<cms:contentText key="AUDIENCE_LIST" code="admin.resend.welcome.email" />
			</td>
			<td class="content-field">
				<html:select property="selectedAudienceId" styleClass="content-field">
					<html:option value=''><cms:contentText key="SELECT_SAVED_AUDIENCE" code="admin.resend.welcome.email" /></html:option>
					<html:options collection="availableAudiences" property="id" labelProperty="name" />
				</html:select>
				<html:submit styleClass="content-buttonstyle" onclick="javascript:setActionAndDispatch('resendWelcomeEmail.do', 'addAudience')">
					<cms:contentText code="admin.resend.welcome.email" key="ADD" />
				</html:submit>
			</td>
		</tr>
		
		<tr>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td class="content-field-label" colspan="2">
				<cms:contentText key="CREATE_AUDIENCE" code="admin.resend.welcome.email" />
				<a href="javascript:setActionDispatchAndSubmit('resendWelcomeEmail.do', 'prepareAudienceLookup');" class="crud-content-link">
					<cms:contentText key="LIST_BUILDER" code="admin.resend.welcome.email" />
				</a>
			</td>
		</tr>
		
		<tr>
			<td>
				<html:radio property="option" value="fileLoadDate" />
			</td>
			<td>&nbsp;</td>
			<td class="content-field-label">
				<cms:contentText key="FILE_LOAD_DATE" code="admin.resend.welcome.email" />
			</td>
			<td class="content-field">
				<html:text property="fileLoadDate" styleId="fileLoadDate" size="20" maxlength="19" readonly="true" styleClass="content-field" onfocus="clearDateMask(this);" />
				<img id="fileLoadDateTrigger" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" alt="<cms:contentText key="FILE_LOAD_DATE" code="admin.resend.welcome.email"/>" />
			</td>
		</tr>
		
		<tr>
			<td>
				<html:radio property="option" value="fileLoadId" />
			</td>
			<td>&nbsp;</td>
			<td class="content-field-label">
				<cms:contentText key="FILE_LOAD_ID" code="admin.resend.welcome.email" />
			</td>
			<td class="content-field">
				<html:text property="fileLoadId" size="20" maxlength="19" styleClass="content-field" />
			</td>
		</tr>
	</table>
	
	<table>
		<tr>
			<td>
				<table>
				<tr class="form-blank-row">
					<td></td>
				</tr>
				<tr>
					<td></td>
					<td class="content-field" width="20"></td>
					<td>
					<table>
						<tr>
							<td>
							<table class="crud-table" width="100%">
								<c:set var="rowCount" value="0" />
								<c:set var="switchColor" value="false" />
								<nested:iterate id="audience" name="resendWelcomeEmailForm" property="audienceList">
									<nested:hidden property="id" />
									<nested:hidden property="audienceId" />
									<nested:hidden property="name" />
									<nested:hidden property="size" />
									<nested:hidden property="audienceType" />
									<c:choose>
										<c:when test="${switchColor == 'false'}">
											<tr class="crud-table-row1">
												<c:set var="switchColor" scope="page" value="true" />
										</c:when>
										<c:otherwise>
											<tr class="crud-table-row2">
												<c:set var="switchColor" scope="page" value="false" />
										</c:otherwise>
									</c:choose>
									<%-- in general we use content-field-review for no editables, but for this screen class is content-field--%>
									<td class="content-field">
										<c:out value="${audience.name}" />
									</td>
									<td class="content-field">
										&lt; <c:out value="${audience.size}" /> &gt;
									</td>
									<td class="content-field">
									<%
											Map parameterMap = new HashMap();
											PromotionAudienceFormBean temp = (PromotionAudienceFormBean) pageContext.getAttribute("audience");
											parameterMap.put("audienceId", temp.getAudienceId());
											pageContext.setAttribute( "previewUrl", ClientStateUtils.generateEncodedLink(RequestUtils.getBaseURI(request), "/promotion/promotionAudience.do?method=displayPaxListPopup", parameterMap, true));
									%>
										<a href="javascript:popUpWin('<c:out value="${previewUrl}"/>', 'console', 750, 500, false, true);" class="crud-content-link">
											<cms:contentText key="VIEW_LIST" code="admin.resend.welcome.email" />
										</a>
									</td>
									<td align="center" class="content-field">
										<nested:checkbox property="removed" />
									</td>
									<c:set var="rowCount" value="${rowCount+1}" />
								</nested:iterate>
							</table>
							</td>
						</tr>
						<c:if test="${rowCount>0}">
							<tr>
								<td align="right">
									<html:submit styleClass="content-buttonstyle" onclick="javascript:setActionAndDispatch('resendWelcomeEmail.do','removeAudience');">
										<cms:contentText key="REMOVE" code="admin.resend.welcome.email" />
									</html:submit>
								</td>
							</tr>
						</c:if>
					</table>
					</td>
				</tr>
			</table>
			</td>
		</tr>
	</table>
	
	<table>
		<tr class="form-buttonrow">
			<td align="left">
			<html:button property="countBtn" styleClass="content-buttonstyle" onclick="javascript:setActionDispatchAndSubmit('resendWelcomeEmail.do', 'count')">
				<cms:contentText code="admin.resend.welcome.email" key="COUNT" />
			</html:button>
			&nbsp;
			<html:submit styleClass="content-buttonstyle" onclick="javascript:setActionAndDispatch('resendWelcomeEmail.do', 'send')">
				<cms:contentText code="admin.resend.welcome.email" key="SEND" />
			</html:submit>
			&nbsp;
			<html:button property="cancelBtn" styleClass="content-buttonstyle" onclick="javascript:callUrl('../homePage.do')">
				<cms:contentText code="admin.resend.welcome.email" key="CANCEL" />
			</html:button></td>
		</tr>
	</table>

<script type="text/javascript">

  Calendar.setup(
    {
      inputField  : "fileLoadDate",         // ID of the input field
      ifFormat    : "${TinyMceDatePattern}",    			// the date format
      showsTime   : "true",
      timeFormat  : "12",
      button      : "fileLoadDateTrigger"   // ID of the button
    }
  );

</script>

</html:form>
