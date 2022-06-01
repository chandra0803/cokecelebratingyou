<%@page import="com.biperf.core.utils.UserManager"%>
<%@page import="com.biperf.core.domain.enums.ReportParameterType"%>
<%@page import="com.biperf.core.domain.report.Report"%>
<%@ include file="/include/taglib.jspf"%>

<%
  Report report = (Report)request.getAttribute( "report" );
  String reporturl = report.getUrl();
  int beginIndex = reporturl.indexOf( "/reports/" ) + 9;
  int endIndex = reporturl.indexOf( ".do" );
  String actionName = reporturl.substring( beginIndex, endIndex );
%>

<div class="modal-header">
    <button class="close" type="button"><i class="icon-close"></i></button>
    <h3><cms:contentText key="CHANGE_FILTERS" code="report.display.page" /></h3>
</div>

<div class="modal-body">
<html:form styleClass="form" action="<%=actionName%>"
	styleId="reportParametersForm">

	<html:hidden property="method" value="displaySummaryReport" />
	<input type="hidden" name="isFiltersPage" value="Y"/>

        <div class="alert alert-error errorHidden" style="display: none;"
            data-msg-error="<cms:contentText key="NO_DATA_FOR_FILTERS" code="report.display.page" /> "></div>

		<div class="row-fluid">

			<c:set var="SELECT_PICKLIST" value="<%=ReportParameterType.SELECT_PICKLIST%>" scope="page" />
			<c:set var="SELECT_QUERY" value="<%=ReportParameterType.SELECT_QUERY%>" scope="page" />
			<c:set var="MULTI_SELECT_PICKLIST" value="<%=ReportParameterType.MULTI_SELECT_PICKLIST%>" scope="page" />
			<c:set var="MULTI_SELECT_QUERY" value="<%=ReportParameterType.MULTI_SELECT_QUERY%>" scope="page" />
			<c:set var="TEXT" value="<%=ReportParameterType.TEXT%>" scope="page" />

			<c:if test="${reportParametersForm.reportParameterInfoList != null}">

			  <c:if test="${showDates}">
                <%-- start TIME_FRAME section --%>
				<div class="span6">

                    <%-- start TIME_FRAME fieldset --%>
					<fieldset>
						<c:forEach items="${reportParametersForm.reportParameterInfoList}"
							var="parameterInfo1" varStatus="countersd">

							<c:choose>
								<c:when test="${parameterInfo1.adminSelectOnly == true}">
									<beacon:authorize ifNotGranted="BI_ADMIN">
										<c:set var="hideParam" value="true"/>
									</beacon:authorize>
								</c:when>
								<c:otherwise>
									<c:set var="hideParam" value="false"/>
								</c:otherwise>
							</c:choose>

							<c:if test="${'TIME_FRAME' eq parameterInfo1.parameterGroup}">

								<html:hidden
									property="reportParameterInfoList[${countersd.index}].id" />

								<c:if test="${'fromDate' eq parameterInfo1.name}">
									<h4 class="subset"><cms:contentText key="${parameterInfo1.parameterGroup}" code="report.parameters" /></h4>
									<div class="form-inline">
                                        <div class="control-group" <c:if test="${hideParam}">style="display:none"</c:if>>
                                            <label class="control-label"
                                                for="${reportParametersForm.reportParameterInfoList[countersd.index].name}">
                                                <cms:contentText key="${parameterInfo1.cmKey}"
													code="report.parameters" />
                                            </label>
                                            <div class="controls">
                                                <span class="input-append datepickerTrigger"
                                                        data-date-format="<%=UserManager.getUserDatePattern().toLowerCase() %>"
                                                        data-date-language="<%=UserManager.getUserLocale()%>"
                                                        data-date-startdate=""
                                                        data-date-todaydate=""
                                                        data-date-autoclose="true">
                                                    <html:text styleId="${reportParametersForm.reportParameterInfoList[countersd.index].name}" property="reportParameterInfoList[${countersd.index}].value" value="${parameterInfo1.value}" readonly="readonly" styleClass="date" />
                                                    <%-- please make the html:text input look as close to the following example as possible:
                                                         if nothing else, just add the "date" class.
                                                    <input type="text" id="fromDate" name="reportParameterInfoList[0].value" value="01/01/2012" readonly="readonly" class="date"> --%>
                                                    <button class="btn" type="button"><i class="icon-calendar"></i></button>
                                                </span>
                                            </div>
                                        </div>
                                    </div>
								</c:if>
							</c:if>
						</c:forEach>

						<c:forEach items="${reportParametersForm.reportParameterInfoList}"
							var="parameterInfo1" varStatus="countered">

							<c:choose>
								<c:when test="${parameterInfo1.adminSelectOnly == true}">
									<beacon:authorize ifNotGranted="BI_ADMIN">
										<c:set var="hideParam" value="true"/>
									</beacon:authorize>
								</c:when>
								<c:otherwise>
									<c:set var="hideParam" value="false"/>
								</c:otherwise>
							</c:choose>

							<c:if test="${'TIME_FRAME' eq parameterInfo1.parameterGroup}">
								<c:if test="${'toDate' eq parameterInfo1.name}">
                                        <div class="control-group" <c:if test="${hideParam}">style="display:none"</c:if>>
                                            <label class="control-label"
                                                for="${reportParametersForm.reportParameterInfoList[countered.index].name}">
                                                <cms:contentText key="${parameterInfo1.cmKey}"
                                                    code="report.parameters" />
                                            </label>
                                            <div class="controls">
                                                <span class="input-append datepickerTrigger"
                                                        data-date-format="<%=UserManager.getUserDatePattern().toLowerCase()%>"
                                                        data-date-language="<%=UserManager.getUserLocale()%>"
                                                        data-date-startdate=""
                                                        data-date-todaydate=""
                                                        data-date-autoclose="true">
                                                    <html:text styleId="${reportParametersForm.reportParameterInfoList[countered.index].name}" property="reportParameterInfoList[${countered.index}].value" value="${parameterInfo1.value}" readonly="readonly" styleClass="date" />
                                                    <%-- please make the html:text input look as close to the following example as possible:
                                                         if nothing else, just add the "date" class.
                                                    <input type="text" id="toDate" name="reportParameterInfoList[1].value" value="01/01/2012" readonly="readonly" class="date"> --%>
                                                    <button class="btn" type="button"><i class="icon-calendar"></i></button>
                                                </span>
                                            </div>
                                        </div>
                                        <div class="control-group">
                                            <label class="control-label" for="autoUpdate">
                                                <cms:contentText key="AUTO_UPDATE" code="report.parameters" />
                                            </label>
                                            <div class="controls">
                                                <%-- please add the attribute/value id="autoUpdate" so clicking on the label triggers the checkbox --%>
                                                <html:checkbox styleId="autoUpdate" name="reportParametersForm" property="reportParameterInfoList[${countered.index}].autoUpdate" value="true"/>
                                                <html:hidden property="autoUpdateIndex" value="${countered.index}" />
                                                <h6><cms:contentText key="AUTO_UPDATE_INFO" code="report.parameters" /></h6>
                                            </div>
                                        </div>
								</c:if>
							</c:if>
						</c:forEach>

						<c:forEach items="${reportParametersForm.reportParameterInfoList}"
							var="parameterInfo1" varStatus="countered1">

							<c:choose>
								<c:when test="${parameterInfo1.adminSelectOnly == true}">
									<beacon:authorize ifNotGranted="BI_ADMIN">
										<c:set var="hideParam" value="true"/>
									</beacon:authorize>
								</c:when>
								<c:otherwise>
									<c:set var="hideParam" value="false"/>
								</c:otherwise>
							</c:choose>

							<c:if test="${'TIME_FRAME' eq parameterInfo1.parameterGroup}">
								<c:if test="${'selectMonth' eq parameterInfo1.name}">
								<h4 class="subset"><cms:contentText key="${parameterInfo1.parameterGroup}" code="report.parameters" /></h4>
								<div class="form-inline">
                                        <div class="control-group" <c:if test="${hideParam}">style="display:none"</c:if>>
									<label class="control-label"
										for="${reportParametersForm.reportParameterInfoList[countered1.index].name}">
										<cms:contentText key="${parameterInfo1.cmKey}"
											code="report.parameters" />
									</label>
									<div class="controls">
									<html:select  styleClass="${reportParametersForm.reportParameterInfoList[countered1.index].name eq 'parentNodeId' ? 'searchSelectList' : ''}"
										styleId="${reportParametersForm.reportParameterInfoList[countered1.index].name}"
										property="reportParameterInfoList[${countered1.index}].value">
										<html:options collection="${parameterInfo1.collectionName}"
											property="id" labelProperty="value" />
										</html:select>
										</div>
									</div>
									</div>
								</c:if>
							</c:if>
							</c:forEach>
					</fieldset>
                    <%-- end TIME_FRAME fieldset --%>

				</div><!-- /.span6 -->
			  </c:if>
              <%-- end TIME_FRAME section --%>

              <%-- start PARTICIPANTS section --%>
              <c:choose>
                <c:when test="${showDates}">
                	<div class="span6">
                </c:when>
                <c:otherwise>
                	<div class="span12">
                </c:otherwise>
              </c:choose>
					<c:forEach items="${reportParametersForm.reportParameterInfoList}"
						var="parameterInfo2" varStatus="counter2">

						<c:choose>
							<c:when test="${parameterInfo2.adminSelectOnly == true}">
								<beacon:authorize ifNotGranted="BI_ADMIN">
									<c:set var="hideParam" value="true"/>
								</beacon:authorize>
							</c:when>
							<c:otherwise>
								<c:set var="hideParam" value="false"/>
							</c:otherwise>
						</c:choose>

						<c:if test="${'PARTICIPANTS' eq parameterInfo2.parameterGroup}">
							<c:if
								test="${counter2.index == 0 || (counter2.index != 0 && reportParametersForm.reportParameterInfoList[(counter2.index)-1].parameterGroup ne reportParametersForm.reportParameterInfoList[(counter2.index)].parameterGroup) }">
								<fieldset>
									<h4><cms:contentText key="${parameterInfo2.parameterGroup}" code="report.parameters" /></h4>
							</c:if>
							<html:hidden
								property="reportParameterInfoList[${counter2.index}].id" />

							<div class="control-group" <c:if test="${hideParam}">style="display:none"</c:if>>
								<label class="control-label"
									for="${reportParametersForm.reportParameterInfoList[counter2.index].name}">
									<cms:contentText key="${parameterInfo2.cmKey}"
										code="report.parameters" />
										<c:if test="${MULTI_SELECT_PICKLIST eq parameterInfo2.type || MULTI_SELECT_QUERY eq parameterInfo2.type}">*</c:if>
								</label>
                                <div class="controls">
								<c:choose>
									<c:when test="${TEXT eq parameterInfo2.type}">
										<html:text
											styleId="${reportParametersForm.reportParameterInfoList[counter2.index].name}"
											property="${parameterInfo2.name}" />
									</c:when>
									<c:when test="${SELECT_PICKLIST eq parameterInfo2.type}">
										<html:select   styleClass="${reportParametersForm.reportParameterInfoList[counter2.index].name eq 'parentNodeId' ? 'searchSelectList' : ''}"
											styleId="${reportParametersForm.reportParameterInfoList[counter2.index].name}"
											property="reportParameterInfoList[${counter2.index}].value">
											<c:if test="${not parameterInfo2.hideShowAllOption}">
												<html:option value="show_all">
													<cms:contentText key="SHOW_ALL" code="report.parameters" />
												</html:option>
											</c:if>
											<html:options collection="${parameterInfo2.collectionName}"
												property="code" labelProperty="name" />
										</html:select>
									</c:when>
									<c:when test="${MULTI_SELECT_PICKLIST eq parameterInfo2.type}">
										<html:select
											styleId="${reportParametersForm.reportParameterInfoList[counter2.index].name}"
											property="reportParameterInfoList[${counter2.index}].values"
											multiple="true">
											<c:if test="${not parameterInfo2.hideShowAllOption}">
												<html:option value="show_all">
													<cms:contentText key="SHOW_ALL" code="report.parameters" />
												</html:option>
											</c:if>
											<html:options collection="${parameterInfo2.collectionName}"
												property="code" labelProperty="name" />
										</html:select>
									</c:when>
									<c:when test="${SELECT_QUERY eq parameterInfo2.type}">
										<html:select styleClass="${reportParametersForm.reportParameterInfoList[counter2.index].name eq 'parentNodeId' ? 'searchSelectList' : ''}"
											styleId="${reportParametersForm.reportParameterInfoList[counter2.index].name}"
											property="reportParameterInfoList[${counter2.index}].value">
											<c:if test="${not parameterInfo2.hideShowAllOption}">
												<html:option value="show_all">
													<cms:contentText key="SHOW_ALL" code="report.parameters" />
												</html:option>
											</c:if>
											<html:options collection="${parameterInfo2.collectionName}"
												property="id" labelProperty="value" />
										</html:select>
									</c:when>
									<c:when test="${MULTI_SELECT_QUERY eq parameterInfo2.type}">
										<html:select
											styleId="${reportParametersForm.reportParameterInfoList[counter2.index].name}"
											property="reportParameterInfoList[${counter2.index}].values"
											multiple="true">
											<c:if test="${not parameterInfo2.hideShowAllOption}">
												<html:option value="show_all">
													<cms:contentText key="SHOW_ALL" code="report.parameters" />
												</html:option>
											</c:if>
											<html:options collection="${parameterInfo2.collectionName}"
												property="id" labelProperty="value" />
										</html:select>
									</c:when>
								</c:choose>
                                </div>
							</div>
							<c:if
								test="${counter2.index == (fn:length(reportParametersForm.reportParameterInfoList)-1) || (reportParametersForm.reportParameterInfoList[(counter2.index)].parameterGroup ne (reportParametersForm.reportParameterInfoList[(counter2.index)+1].parameterGroup)) }">
								</fieldset>
							</c:if>
						</c:if>
					</c:forEach>
				</div><!-- /.span6 -->
                <%-- end PARTICIPANTS section --%>

                <%-- start BASICS and EXPORT_SELECTION sections --%>
				<div class="span12">

                    <%-- start BASICS fieldset --%>
                    <fieldset>
						<c:forEach items="${reportParametersForm.reportParameterInfoList}"
							var="parameterInfo1" varStatus="counterbs">

							<c:choose>
								<c:when test="${parameterInfo1.adminSelectOnly == true}">
									<beacon:authorize ifNotGranted="BI_ADMIN">
										<c:set var="hideParam" value="true"/>
									</beacon:authorize>
								</c:when>
								<c:otherwise>
									<c:set var="hideParam" value="false"/>
								</c:otherwise>
							</c:choose>

							<c:if test="${parameterInfo1.parameterGroup eq 'BASICS'}">
								<c:if
									test="${counterbs.index == 0 || (counterbs.index != 0 && reportParametersForm.reportParameterInfoList[(counterbs.index)-1].parameterGroup ne reportParametersForm.reportParameterInfoList[counterbs.index].parameterGroup) }">
									<h4><cms:contentText key="${parameterInfo1.parameterGroup}" code="report.parameters" /></h4>
								</c:if>
								<html:hidden
									property="reportParameterInfoList[${counterbs.index}].id" />

								<div class="control-group" <c:if test="${hideParam}">style="display:none"</c:if>>
									<label class="control-label"
										for="${reportParametersForm.reportParameterInfoList[counterbs.index].name}">
										<cms:contentText key="${parameterInfo1.cmKey}"
											code="report.parameters" />
										<c:if test="${MULTI_SELECT_PICKLIST eq parameterInfo1.type || MULTI_SELECT_QUERY eq parameterInfo1.type}">*</c:if>
									</label>
                                    <div class="controls">
									<c:choose>
										<c:when test="${TEXT eq parameterInfo1.type}">
											<html:text property="${parameterInfo1.name}" />
										</c:when>
										<c:when test="${SELECT_PICKLIST eq parameterInfo1.type}">
											<html:select styleClass="${reportParametersForm.reportParameterInfoList[counterbs.index].name eq 'parentNodeId' ? 'searchSelectList' : ''}"
												styleId="${reportParametersForm.reportParameterInfoList[counterbs.index].name}"
												property="reportParameterInfoList[${counterbs.index}].value">
												<c:if test="${not parameterInfo1.hideShowAllOption}">
													<html:option value="show_all">
														<cms:contentText key="SHOW_ALL" code="report.parameters" />
													</html:option>
												</c:if>
												<html:options collection="${parameterInfo1.collectionName}"
													property="code" labelProperty="name" />
											</html:select>
										</c:when>
										<c:when test="${MULTI_SELECT_PICKLIST eq parameterInfo1.type}">
											<html:select
												styleId="${reportParametersForm.reportParameterInfoList[counterbs.index].name}"
												property="reportParameterInfoList[${counterbs.index}].values"
												multiple="true">
												<c:if test="${not parameterInfo1.hideShowAllOption}">
													<html:option value="show_all">
														<cms:contentText key="SHOW_ALL" code="report.parameters" />
													</html:option>
												</c:if>
												<html:options collection="${parameterInfo1.collectionName}"
													property="code" labelProperty="name" />
											</html:select>
										</c:when>
										<c:when test="${SELECT_QUERY eq parameterInfo1.type}">
											<html:select styleClass="${reportParametersForm.reportParameterInfoList[counterbs.index].name eq 'parentNodeId' ? 'searchSelectList' : ''}"
												styleId="${reportParametersForm.reportParameterInfoList[counterbs.index].name}"
												property="reportParameterInfoList[${counterbs.index}].value">
												<c:if test="${not parameterInfo1.hideShowAllOption}">
													<c:choose>
		                                                <c:when test="${showSelectPromotion}">
		                                                	<html:option value=" ">
			                                                	<cms:contentText key="SELECT_PROMOTION" code="report.parameters" />
															</html:option>
		                                                </c:when>
		                                                <c:otherwise>
			                                                <html:option value="show_all">
																<cms:contentText key="SHOW_ALL" code="report.parameters" />
															</html:option>
		                                                </c:otherwise>
	                                                </c:choose>
                                                </c:if>
												<%-- <c:if test="${Report.name ne Report.RECOGNITION_BEHAVIORS && Report.name ne Report.NOMINATION_BEHAVIORS }">
												<html:option value="show_all">
													<cms:contentText key="SHOW_ALL" code="report.parameters" />
												</html:option>
												</c:if> --%>
												<html:options collection="${parameterInfo1.collectionName}"
													property="id" labelProperty="value" />
											</html:select>
										</c:when>
										<c:when test="${MULTI_SELECT_QUERY eq parameterInfo1.type}">
											<html:select
												styleId="${reportParametersForm.reportParameterInfoList[counterbs.index].name}"
												property="reportParameterInfoList[${counterbs.index}].values"
												multiple="true">
												<c:if test="${not parameterInfo1.hideShowAllOption}">
													<html:option value="show_all">
														<cms:contentText key="SHOW_ALL" code="report.parameters" />
													</html:option>
												</c:if>
												<c:choose>
												  <c:when test="${parameterInfo1.collectionName == 'awardLevelTypeList' }">
												    <html:options collection="${parameterInfo1.collectionName}"
													  property="value" labelProperty="value" />
												  </c:when>
												  <c:otherwise>
												    <html:options collection="${parameterInfo1.collectionName}"
													  property="id" labelProperty="value" />
											      </c:otherwise>
											    </c:choose>
											</html:select>
										</c:when>
									</c:choose>
                                    </div>
								</div>
							</c:if>
						</c:forEach>
					</fieldset>
                    <%-- end BASICS fieldset --%>

					<c:forEach items="${reportParametersForm.reportParameterInfoList}"
						var="parameterInfo2" varStatus="counter2">

						<c:choose>
							<c:when test="${parameterInfo2.adminSelectOnly == true}">
								<beacon:authorize ifNotGranted="BI_ADMIN">
									<c:set var="hideParam" value="true"/>
								</beacon:authorize>
							</c:when>
							<c:otherwise>
								<c:set var="hideParam" value="false"/>
							</c:otherwise>
						</c:choose>
						<c:if test ="${showExportSelection }">
						<c:if test="${'EXPORT_SELECTION' eq parameterInfo2.parameterGroup}">
							<c:if
								test="${counter2.index == 0 || (counter2.index != 0 && reportParametersForm.reportParameterInfoList[(counter2.index)-1].parameterGroup ne reportParametersForm.reportParameterInfoList[(counter2.index)].parameterGroup) }">
								<fieldset>
									<h4><cms:contentText key="${parameterInfo2.parameterGroup}" code="report.parameters" /></h4>
									<c:if test="${displayExportOnlyFilterLabel}"><h6><cms:contentText key="EXPORT_ONLY_FILTER_MSG" code="report.parameters" /></h6></c:if>
							</c:if>
							<html:hidden
								property="reportParameterInfoList[${counter2.index}].id" />

							<div class="control-group" <c:if test="${hideParam}">style="display:none"</c:if>>
								<label class="control-label"
									for="${reportParametersForm.reportParameterInfoList[counter2.index].name}">
									<cms:contentText key="${parameterInfo2.cmKey}"
										code="report.parameters" />
										<c:if test="${MULTI_SELECT_PICKLIST eq parameterInfo2.type || MULTI_SELECT_QUERY eq parameterInfo2.type}">*</c:if>
								</label>
                                <div class="controls">
								<c:choose>
									<c:when test="${TEXT eq parameterInfo2.type}">
										<html:text
											styleId="${reportParametersForm.reportParameterInfoList[counter2.index].name}"
											property="${parameterInfo2.name}" />
									</c:when>
									<c:when test="${SELECT_PICKLIST eq parameterInfo2.type}">
										<html:select styleClass="${reportParametersForm.reportParameterInfoList[counter2.index].name eq 'parentNodeId' ? 'searchSelectList' : ''}"
											styleId="${reportParametersForm.reportParameterInfoList[counter2.index].name}"
											property="reportParameterInfoList[${counter2.index}].value">
											<c:if test="${not parameterInfo2.hideShowAllOption}">
												<html:option value="show_all">
													<cms:contentText key="SHOW_ALL" code="report.parameters" />
												</html:option>
											</c:if>
											<html:options collection="${parameterInfo2.collectionName}"
												property="code" labelProperty="name" />
										</html:select>
									</c:when>
								</c:choose>
                                </div>
							</div>
							<c:if
								test="${counter2.index == (fn:length(reportParametersForm.reportParameterInfoList)-1) || (reportParametersForm.reportParameterInfoList[(counter2.index)].parameterGroup ne (reportParametersForm.reportParameterInfoList[(counter2.index)+1].parameterGroup)) }">
								</fieldset>
							</c:if>
						</c:if>
						</c:if>
					</c:forEach>
				</div><!-- /.span12 -->
                <%-- end BASICS and EXPORT_SELECTION sections --%>
			</c:if>
		</div>
		<%
		  boolean isParticipantSearch = false;
		  boolean isSurveyReport = false;
		  boolean isWorkHappierReport = false;
		%>
		<c:forEach items="${reportParametersForm.reportParameterInfoList}"
			var="parameterInfo3">
			<c:if test="${'ParticipantSearch' eq parameterInfo3.parameterGroup}">
				<%
				  isParticipantSearch = true;
				%>
			</c:if>
			<c:if test="${'surveyAnalysis' eq parameterInfo3.reportCode}">
				<%
				  isSurveyReport = true;
				%>
			</c:if>
			<c:if test="${'happinessPulse' eq parameterInfo3.reportCode}">
				<%
				isWorkHappierReport = true;
				%>
			</c:if>
		</c:forEach>
		<%
		  if ( isParticipantSearch )
		    {
		%>
		<div class="row-fluid">
			<div class="span12">
				<div id="participantsSearch">
                    <div class="paxSearchStartView" ></div><!-- /.paxSearchStartView -->
					<!-- Participant search view Element -->
					<!--<div class="" id="participantSearchView"
						data-search-types='[{"id":"lastName","name":"<cms:contentText key="LAST_NAME" code="report.parameters" />"},{"id":"firstName","name":"<cms:contentText key="FIRST_NAME" code="report.parameters" />"},{"id":"location","name":"<cms:contentText key="LOCATION" code="report.parameters" />"},{"id":"jobTitle","name":"<cms:contentText key="JOB_TITLE" code="report.parameters" />"},{"id":"department","name":"<cms:contentText key="DEPARTMENT" code="report.parameters" />"},{"id":"country","name":"<cms:contentText key="COUNTRY_FIELD" code="report.parameters" />"}]'
						data-search-params='{}' data-autocomp-delay="500"
						data-autocomp-min-chars="2"
						data-autocomp-url="${systemUrl}/reports/participantsSearchReport.do?method=doAutoComplete"
						data-search-url="${systemUrl}/reports/participantsSearchReport.do?method=generatePaxSearchView"
						data-select-mode="single"
						data-visibility-controls="true"
                        data-visibility-controls="hideOnly"
                        data-msg-hide='<cms:contentText key="DONE_ADDING" code="profile.proxies.tab"/>'>
					</div>-->
				</div>

				<div
					class="container-splitter with-splitter-styles participantCollectionViewWrapper">

					<table  class="table table-condensed table-striped">
						<thead>
							<tr>
								<th class="participant"><cms:contentText key="PARTICIPANT" code="participant.search"/></th>
								<th class="remove"><cms:contentText key="REMOVE" code="participant.search"/></th>
							</tr>
						</thead>

						<tbody id="participantsView" class="participantCollectionView"
							data-msg-empty="<cms:contentText key="NOT_CHOSEN_ANYONE" code="participant.search"/>"
							data-hide-on-empty="false">
						</tbody>
					</table>
					<!--
                    used to keep track of the number of participants, req. a 'participantCount' class
                    name is flexible
                 -->
					<input type="hidden" name="paxCount" value="0" class="participantCount" />
				</div>
				<!-- /.container-splitter.with-splitter-styles.participantCollectionViewWrapper -->

                <!--
                If a participant needs to be prefilled in the widget, put a stringified JSON object of the pax data inside this textarea.
                If no participant needs to be prefilled, do not display the textarea at all
                -->
                <%--SAMPLE:
                <textarea name="prefilledParticipant" id="prefilledParticipant" style="display:none">
                    [{"id":5594,"firstName":"Susan","lastName":"Smith","avatarUrl":null,"orgName":"Midwest_","departmentName":"Service","jobName":"Employee","countryName":"United States","countryCode":"us","city":"Minneapolis","newScore":null,"countryRatio":null,"urlEdit":null,"isSelected":false,"isLocked":false}]
                </textarea>
                --%>
                <c:if test="${! empty reportParametersForm.paxId}">
                  <textarea name="prefilledParticipant" id="prefilledParticipant" style="display:none">
                    [{"id":${reportParametersForm.paxId},"firstName":"${reportParametersForm.paxFirstName}","lastName":"${reportParametersForm.paxLastName}","avatarUrl":"${reportParametersForm.avatarUrl}","isSelected":true,"isLocked":false}]
                </textarea>
                </c:if>

			</div><!-- /.span12 -->
		</div>
		<!-- row-fluid -->
		<%
		  } else{
		      if( !( isSurveyReport || isWorkHappierReport ) ) {%>
		    <div class="row-fluid">
				<div class="span12">
					<cms:contentText key="MULTI_SELECTION_ALLOWED" code="report.parameters"/>
				</div>
			</div>
		<%
		      }  }
		%>
	<!-- </div> --><!-- /.container-fluid -->
	<!-- span12 -->
	<div class="row-fluid">
		<div class="form-actions">
			<button id="submitButton" type="button" class="btn btn-primary"><cms:contentText key="UPDATE_REPORT" code="report.parameters"/></button>
			<button id="cancelButton" type="button" class="btn"><cms:contentText key="CANCEL" code="report.parameters" /></button>
		</div>
	</div>
	<!-- span12 -->
</html:form>
</div><!-- /.modal-body -->

<script>
$(document).ready(function(){
	$(".searchSelectList").chosen();
	G5.props.URL_JSON_PARTICIPANT_INFO = G5.props.URL_ROOT+'participantPublicProfile.do?method=populatePax';
	G5.props.URL_JSON_EZ_RECOGNITION_MEMBER_INFO = "${pageContext.request.contextPath}/recognitionWizard/memberInfo.do";

});

</script>

<%@include file="/submitrecognition/easy/flipSide.jsp"%>
