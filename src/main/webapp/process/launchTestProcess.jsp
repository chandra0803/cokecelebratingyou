<%--UI REFACTORED--%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<script type="text/javascript"
	src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar.js"></script>
<script type="text/javascript"
	src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-en.js"></script>
<script type="text/javascript"
	src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-setup.js"></script>

<script type="text/javascript">
<!--

//-->
</script>

<html:form styleId="contentForm" action="/testProcessConfirm">
	<html:hidden property="method" />
	<html:hidden property="processBeanName" />
	<beacon:client-state>
		<beacon:client-state-entry name="processId"
			value="${processLaunchForm.processId}" />
	</beacon:client-state>

	<INPUT TYPE=HIDDEN NAME="countOfParameters"
		value="<c:out value="${countOfParameters}"/>">

	<table border="0" cellpadding="10" cellspacing="0" width="100%">
		<tr>
			<td><span class="headline"><cms:contentText
						key="LAUNCH_PROCESS" code="process.launch" /> </span> <%-- Subheadline --%>
				<br /> <span class="subheadline"> <c:out
						value="${process.name}" /></span> <%-- End Subheadline --%> <%--INSTRUCTIONS--%>
				<br /> <br /> <span class="content-instruction"> <cms:contentText
						key="INSTRUCTIONAL_COPY" code="process.launch" /></span> <br /> <br />
				<%--END INSTRUCTIONS--%> <cms:errors /> <%-- Start Input Example  --%>
				<table>
					<tr class="form-blank-row">
						<td></td>
					</tr>
					<c:forEach items="${processParameterList}" var="processParameterValue" varStatus="count">
						<tr class="form-row-spacer">
							<beacon:label property="actionCode" required="${processParameterValue.name ne 'customFormField1' and processParameterValue.name ne 'customFormField2' and processParameterValue.name ne 'customFormField3' }">
								<c:out value="${process.processParameters[processParameterValue.name].description}" />
							</beacon:label>
							<c:choose>
								<c:when
									test='${processParameterValue.formatType == "text_field"}'>
									<c:choose>
										<c:when test='${processParameterValue.secret}'>
											<td class="content-field"><input type="password"
												name="processParameterValueList[<c:out value="${count.index}"/>].value"
												value="<c:out value="${processParameterValue.value}"/>"
												size="30" maxlength="60" class="content-field"
												autocomplete="<c:choose><c:when test="${allowPasswordFieldAutoComplete}">on</c:when><c:otherwise>off</c:otherwise></c:choose>" />
											</td>
										</c:when>
										<c:otherwise>
											<c:choose>
												<c:when test="${processParameterValue.name eq 'purlRecipientUserName'
													         or processParameterValue.name eq 'recipientUserName'
													         or processParameterValue.name eq 'userName'}">
													<td class="content-field">
														<div id="autocomplete" class="yui-ac">
															<input type="hidden" id="searchBy1" name="searchBy" value="lastName">
															<input type="text" id="searchQuery1" styleClass="killme" name="processParameterValueList[<c:out value="${count.index}"/>].value"
																value="<c:out value="${processParameterValue.value}"/>" size="30" class="content-field" placeholder="Enter last name here" />
															<div id="searchResults1" style="z-index: 1; width: 500px;"></div>
														</div>
													</td>
												</c:when>
												<c:when test="${process.processBeanName eq 'adminTestRecogReceivedEmailProcess' and processParameterValue.name eq 'senderUserName'
												             or process.processBeanName eq 'adminTestRecogPurlContributorsNonResponseEmailProcess' and processParameterValue.name eq 'purlContributorUserName'
												             or process.processBeanName eq 'adminTestCelebRecogReceivedEmailProcess' and processParameterValue.name eq 'managerUserName'
												             or process.processBeanName eq 'adminTestCelebMgrNonResponseEmailProcess' and processParameterValue.name eq 'managerUserName'
												             or process.processBeanName eq 'adminTestCelebMgrNotificationEmailProcess' and processParameterValue.name eq 'managerUserName'
												             or process.processBeanName eq 'adminTestPurlMgrNotificationEmailProcess' and processParameterValue.name eq 'managerUserName'
												             or process.processBeanName eq 'adminTestPurlContributorsInvitationEmailProcess' and processParameterValue.name eq 'invitedContributorUserName'
												             or process.processBeanName eq 'adminTestRecogPurlManagersNonResponseEmailProcess' and processParameterValue.name eq 'purlManagerUserName'}">
													<td class="content-field">
														<div id="autocomplete" class="yui-ac">
															<input type="hidden" id="searchBy2" name="searchBy" value="lastName">
															<input type="text" id="searchQuery2" styleClass="killme" name="processParameterValueList[<c:out value="${count.index}"/>].value"
																value="<c:out value="${processParameterValue.value}"/>" size="30" class="content-field" placeholder="Enter last name here" />
															<div id="searchResults2" style="z-index: 1; width: 500px;"></div>
														</div>
													</td>
												</c:when>
												<c:when test="${process.processBeanName eq 'adminTestRecogPurlContributorsNonResponseEmailProcess' and processParameterValue.name eq 'purlInvitedContributorUserName'
												             or process.processBeanName eq 'adminTestPurlContributorsInvitationEmailProcess' and processParameterValue.name eq 'contributorUserName'
												             or process.processBeanName eq 'adminTestCelebRecogReceivedEmailProcess' and processParameterValue.name eq 'managerAboveUserName'}">
													<td class="content-field">
														<div id="autocomplete" class="yui-ac">
															<input type="hidden" id="searchBy3" name="searchBy" value="lastName">
															<input type="text" id="searchQuery3" styleClass="killme" name="processParameterValueList[<c:out value="${count.index}"/>].value"
																value="<c:out value="${processParameterValue.value}"/>" size="30" class="content-field" placeholder="Enter last name here" />
															<div id="searchResults3" style="z-index: 1; width: 500px;"></div>
														</div>
													</td>
												</c:when>
												<c:when test="${process.processBeanName eq 'adminTestCelebRecogReceivedEmailProcess' and processParameterValue.name eq 'senderUserName'}">
													<td class="content-field">
														<div id="autocomplete" class="yui-ac">
															<input type="hidden" id="searchBy4" name="searchBy" value="lastName">
															<input type="text" id="searchQuery4" styleClass="killme" name="processParameterValueList[<c:out value="${count.index}"/>].value"
																value="<c:out value="${processParameterValue.value}"/>" size="30" class="content-field" placeholder="Enter last name here" />
															<div id="searchResults4" style="z-index: 1; width: 500px;"></div>
														</div>
													</td>
												</c:when>
  		                                        <c:when test="${processParameterValue.name == 'submitterComments' or processParameterValue.name == 'comments'
  		                                                     or processParameterValue.name == 'celebManagerMessage' or processParameterValue.name == 'celebManagerAboveMessage'}">
	  			                                    <td class="content-field">
   				                                        <textarea style="WIDTH: 100%" id="submitterComments"
   				                                        name="processParameterValueList[<c:out value="${count.index}"/>].value" rows="5" ><c:out value="${processParameterValue.value}"/></textarea>
  				                                    </td>
  				                                </c:when>
												<c:otherwise>
													<td class="content-field">
														<input type="text" name="processParameterValueList[<c:out value="${count.index}"/>].value" value="<c:out value="${processParameterValue.value}"/>" size="25" class="content-field"/>
													</td>
												</c:otherwise>
											</c:choose>
										</c:otherwise>
									</c:choose>
								</c:when>

								<c:when test='${processParameterValue.formatType == "radio"}'>
									<td class="content-field"><input type="radio"
										name="processParameterValueList[<c:out value="${count.index}"/>].value"
										value="yes" class="content-field" checked="checked">Yes<!--TODO: cms key--><br />
										<input type="radio"
										name="processParameterValueList[<c:out value="${count.index}"/>].value"
										value="no" class="content-field">No<!--TODO: cms key--></td>
								</c:when>

								<c:when
									test='${processParameterValue.formatType == "drop_down"}'>

									<td class="content-field"><SELECT
										name="processParameterValueList[<c:out value="${count.index}"/>].value"
										selected="<c:out value='${processParameterValue.value}'/>"
										class="content-field killme">
	                                    <c:if test = "${processParameterValue.name eq 'recipientLocale'}">
	    		                          <OPTION VALUE="<c:out value=""/>"><cms:contentText code="system.general" key="SELECT_ONE"/>
	                                    </c:if>
	                                    <c:if test = "${process.processBeanName eq 'adminTestRecogReceivedEmailProcess'
	                                                  and processParameterValue.name eq 'badgePromotionId'
	                                                  or process.processBeanName eq 'adminTestCelebRecogReceivedEmailProcess'
	                                                  and processParameterValue.name eq 'badgePromotionId'}">
	    		                          <OPTION VALUE="<c:out value=""/>"><cms:contentText code="system.general" key="SELECT_ONE"/>
	                                    </c:if>
											<c:forEach
												items="${processParameterValue.sourceValueChoices}"
												var="source">
												<c:choose>
													<c:when
														test='${processParameterValue.sourceType == "picklist_asset_name"}'>
														<c:set var="optionValue" value="${source.code}" />
														<c:set var="optionText" value="${source.name}" />
													</c:when>
													<c:otherwise>
														<c:set var="optionValue" value="${source.id}" />
														<c:set var="optionText" value="${source.value}" />
													</c:otherwise>
												</c:choose>
												<c:choose>
													<c:when
														test='${processParameterValue.value == optionValue}'>
														<OPTION VALUE="<c:out value="${optionValue}"/>" selected><c:out
																value="${optionText}" />
													</c:when>
													<c:otherwise>
														<OPTION VALUE="<c:out value="${optionValue}"/>"><c:out
																value="${optionText}" />
													</c:otherwise>
												</c:choose>
											</c:forEach>

									</SELECT></td>

									<c:if
										test="${process.processBeanName == 'reportExtractProcess' }">
										<c:if test="${processParameterValue.name == 'giverReceiver' }">
											<td><cms:contentText code="report.extract.parameters"
													key="GIVER_RECEIVER" /></td>
										</c:if>
									</c:if>

								</c:when>

								<c:when test='${processParameterValue.formatType == "date"}'>
									<td class="content-field"><input type="text"
										name="processParameterValueList[<c:out value="${count.index}"/>].value"
										value="<c:out value='${processParameterValue.value}'/>"
										id="processParameterValueList<c:out value="${count.index}"/>value"
										size="10" maxlength="10" readonly="true" class="content-field"
										onfocus="clearDateMask(this);" /> <img
										id="processParameterValueList<c:out value="${count.index}"/>valueTrigger"
										src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif"
										class="calendar-icon"
										alt="<cms:contentText key='START_DATE' code='process.schedule'/>" />
									</td>

									<script type="text/javascript">
	Calendar.setup(
		{
				inputField  : "processParameterValueList<c:out value="${count.index}"/>value",       	// ID of the input field
				ifFormat    : "${TinyMceDatePattern}",    		// the date format
				button      : "processParameterValueList<c:out value="${count.index}"/>valueTrigger"  // ID of the button
		});
	</script>

								</c:when>

							</c:choose>

							<input type="hidden"
								name="processParameterValueList[<c:out value="${count.index}"/>].value1"
								value="<c:out value="${processParameterValue.value}"/>">
							<input type="hidden"
								NAME="processParameterValueList[<c:out value="${count.index}"/>].name"
								value="<c:out value="${processParameterValue.name}"/>">
							<input type="hidden"
								NAME="processParameterValueList[<c:out value="${count.index}"/>].dataType"
								value="<c:out value="${processParameterValue.dataType}"/>">
							<input type="hidden"
								NAME="processParameterValueList[<c:out value="${count.index}"/>].formatType"
								value="<c:out value="${processParameterValue.formatType}"/>">
							<input type="hidden"
								NAME="processParameterValueList[<c:out value="${count.index}"/>].sourceType"
								value="<c:out value="${processParameterValue.sourceType}"/>">
							<input type="hidden"
								NAME="processParameterValueList[<c:out value="${count.index}"/>].processBeanName"
								value="<c:out value="${processParameterValue.processBeanName}"/>">
						</tr>
					</c:forEach>


					<%--BUTTON ROWS ... For Input--%>
					<tr class="form-buttonrow">
						<td></td>
						<td></td>
						<td align="left"><beacon:authorize ifNotGranted="LOGIN_AS">
								<html:submit styleClass="content-buttonstyle">
									<cms:contentText code="system.button" key="SUBMIT" />
								</html:submit>
							</beacon:authorize> <html:cancel styleClass="content-buttonstyle"
								onclick="setActionAndDispatch('processLaunch.do','launch');">
								<cms:contentText code="system.button" key="CANCEL" />
							</html:cancel></td>
					</tr>
					<%--END BUTTON ROW--%>
				</table> <%-- End Inputs  --%></td>
		</tr>
	</table>
</html:form>

<%@ include file="/include/paxSearch.jspf"%>
<script type="text/javascript">
<!--

  YAHOO.example.ACXml1 = paxSearch_instantiate('<%=RequestUtils.getBaseURI(request)%>/process/participantSearch.do','',1);

  YAHOO.example.ACXml2 = paxSearch_instantiate('<%=RequestUtils.getBaseURI(request)%>/process/participantSearch.do','',2);

  YAHOO.example.ACXml3 = paxSearch_instantiate('<%=RequestUtils.getBaseURI(request)%>/process/participantSearch.do','',3);

  YAHOO.example.ACXml4 = paxSearch_instantiate('<%=RequestUtils.getBaseURI(request)%>/process/participantSearch.do','',4);

	function paxSearch_selectPax(paxId, nodeId, paxDisplayName, index) {
			document.getElementById('searchQuery'+index).value = paxId;
	}
//-->
</script>
