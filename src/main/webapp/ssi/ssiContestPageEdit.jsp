<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@page import="com.biperf.core.utils.UserManager"%>
<%@page import="com.biperf.core.utils.DateUtils"%>
<%@page import="java.util.Date"%>
<!-- ======== CONTEST EDIT PAGE ======== -->

<%
  Date date = new Date();
  String todayDate = DateUtils.toDisplayString( date );
%>

<c:set var="currentDate" scope="request" value="<%=todayDate%>" />

<!--
    Create and Edit Contests.
    - JSON used due to the complex nature of the quiz
    - JAVA, please discuss any alterations needed for information exchange with FE (Aaron)
-->

<div id="ssiContestPageEditView"
	class="contestPage ssiContestPageEdit page-content">
    <div class="row-fluid">
        <div class="span12">
            <h2><cms:contentText key="PAGE_TITLE" code="ssi_contest.generalInfo" /></h2>
            <h3><c:out value="${contestName}"/></h3>
        </div>
    </div>
	<div class="row-fluid">

		<!-- DIV.row-fluid up next to wrapping div.page-content for IE7 -->
		<div class="span12">

			<!-- JAVA NOTE: in the rare event that the server has an error (FE should catch all errors) -->

			<logic:present name="org.apache.struts.action.ERROR">
				<!-- JAVA NOTE: in the rare event that the server has an error (FE should catch all errors) -->
				<div class="alert alert-block alert-error">
					<button type="button" class="close" data-dismiss="alert">
						<i class="icon-close"></i>
					</button>
					<div class="error">
						<h4>
							<cms:contentText key="FOLLOWING_ERRORS"
								code="system.generalerror" />
						</h4>
						<ul>
							<html:messages id="error">
								<li><c:out value="${error}" /></li>
							</html:messages>
						</ul>
					</div>
				</div>
			</logic:present>

			<!-- **************************************************************
                WizardTabsView
             ***************************************************************** -->
			<ul class="wizardTabsView" data-content=".wizardTabsContent"
				style="visibility: hidden">
				<!-- generated using json+tpl by WizardTabsView -->
			</ul>
			<!-- /.wizardTabsView -->


			<!-- **************************************************************
                WIZARD TABS CONTENT
             ***************************************************************** -->
			<div class="wizardTabsContent">


<!--
INFO
-->
				<div id="ssiContestEditTabInfoView" class="stepInfoContent stepContent" style="display: none">


					<div class="control-group validateme"
						data-validate-flags="nonempty"
						data-validate-fail-msgs='{"nonempty" : "<cms:contentText key="NAME_ERROR" code="ssi_contest.generalInfo" />"}'>
						<label class="control-label" for="name"> <cms:contentText
								key="CONTEST_NAME" code="ssi_contest.generalInfo" />
						</label>
						<div class="controls defLangNameTrans">
							<input type="text" id="name" class="contestNameInput input-xlarge"
								data-model-key="name" maxlength="50">
							<div class="iconWrap">	
								<span class="contestNameCheckingSpinnner" style="display: none"></span>
								<i
									class="contestNameInvalid contestNameValidDispItem icon-cancel-circle"
									style="display: none"></i>  <i
									class="contestNameValid contestNameValidDispItem icon-check-circle"
									style="display: none"></i>
							</div>	
							<span class="contestNameInvalidMsg contestNameValidDispItem" style="display: none">&nbsp;</span>
						</div>
                        <div class="awardThemNowOnly defTransContNameDispOnly" style="display: none;">
                            <b><!-- dyn --></b>
						</div>
					</div>

					<div class="translationsWrapper nameTranslationsWrapper" style="display:none">
						<div class="translationsSummary nameTranslationsSummary">
							<div class="translationsCountMsg" style="display: none">
								<b></b>
								<cms:contentText key="TRANSLATIONS_ADDED"
									code="ssi_contest.generalInfo" />
							</div>
							<a href="#" class="addTranslationsBtn" style="display: none"><cms:contentText
									key="ADD_TRANSLATIONS" code="ssi_contest.generalInfo" /></a>
                            <a href="#" class="showTranslationsBtn" style="display: none"><cms:contentText
									key="SHOW_TRANSLATIONS" code="ssi_contest.generalInfo" /></a>
						</div>
						<div class="spinWrap" style="display: none">
							<span class="ajaxSpinner"></span>
							<cms:contentText key="LOADING_TRANSLATIONS"
								code="ssi_contest.generalInfo" />
						</div>
						<div
							class="translationsEditor nameTranslationsEditor container-splitter with-splitter-styles"
							style="display: none">
							<a href="#" class="hideTranslationsBtn"><cms:contentText
									key="HIDE_TRANSLATIONS" code="ssi_contest.generalInfo" /></a>
							<table class="translationsTable">
								<thead>
									<tr>
										<th><cms:contentText key="LANGUAGE"
												code="ssi_contest.generalInfo" /></th>
										<th><cms:contentText key="CONTEST_NAME"
												code="ssi_contest.generalInfo" /></th>
									</tr>
								</thead>
								<tbody>
									<!-- dynamic -->
									<script id="nameItemsTpl" type="text/x-handlebars-template">
                                        {{#names}}
                                        <tr>
                                            <td>
                                            {{#if _isNew}}
                                                <select>
                                                {{#each langs}}
                                                    <option value="{{id}}" {{#isSelected}}selected{{/isSelected}} >
                                                        {{name}}
                                                    </option>
                                                {{/each}}
                                                </select>
                                            {{else}}
                                                {{langName}}
                                            {{/if}}
                                            </td>
                                            <td>
                                                <input class="nameInput" type="text" value="{{text}}" data-index="{{index}}" data-name-lang="{{language}}"/>
                                                <span class="contestNameCheckingSpinnner" style="display:none"></span>
                                                <i class="contestNameInvalid contestNameValidDispItem icon-cancel-circle" style="display:none"></i>                                           
                                                <i class="contestNameValid contestNameValidDispItem icon-check-circle" style="display:none"></i>
                                            </td>
                                        </tr>
                                        {{/names}}
                                    </script>
									<!-- /#nameItemsTpl -->
								</tbody>
							</table>
							<a href="#" class="addAnotherTranslationBtn"><cms:contentText
									key="ADD_ANOTHER_TRANSLATIONS" code="ssi_contest.generalInfo" /></a>
						</div>
					</div>
					<!-- /.translationsWrapper nameTranslationsWrapper -->
                    <!--
						DATES
					-->
					<div class="form-inline">
						<div class="control-group validateme startDateWrapper"
							data-validate-flags="nonempty"
							data-validate-fail-msgs='{"nonempty" : "<cms:contentText key="START_DATE_REQ_ERROR" code="ssi_contest.generalInfo" />"}'>
							<label class="control-label" for="startDate"><cms:contentText
									key="CONTEST_START_DATE" code="ssi_contest.generalInfo" /></label>
							<div class="controls">
								<!-- JAVA NOTE: set data-date-* attrs -->
								<span class="input-append datepickerTrigger startDateTrigger"
									data-date-format="<%=UserManager.getUserDatePattern().toLowerCase()%>"
									data-date-language="<%=UserManager.getUserLocale()%>"
									data-date-todaydate="<%=todayDate%>" data-date-autoclose="true">
									<input type="text" id="startDate" data-model-key="startDate"
									readonly="readonly" class="date startDateInput">
									<button class="btn" type="button">
										<i class="icon-calendar"></i>
									</button>
								</span>
							</div>
						</div>
						<div class="control-group awardThemNowOnly startDateDispOnly" style="display:none">
                            <label class="control-label"><cms:contentText key="CONTEST_START_DATE" code="ssi_contest.generalInfo" /></label>
                            <div class="controls"><b><!-- dyn --></b></div>
                        </div>

						<div class="control-group validateme"
							data-validate-flags="nonempty"
							data-validate-fail-msgs='{"nonempty" : "<cms:contentText key="END_DATE_REQ_ERROR" code="ssi_contest.generalInfo" />"}'>
							<label class="control-label" for="endDate"><cms:contentText
									key="CONTEST_END_DATE" code="ssi_contest.generalInfo" /></label>
							<div class="controls">
								<!-- JAVA NOTE: set data-date-* attrs -->
								<span class="input-append datepickerTrigger endDateTrigger"
									data-date-format="<%=UserManager.getUserDatePattern().toLowerCase()%>"
									data-date-language="<%=UserManager.getUserLocale()%>"
									data-date-startdate="<%=todayDate%>"
									data-date-todaydate="<%=todayDate%>" data-date-autoclose="true">
									<input type="text" id="endDate" data-model-key="endDate"
									readonly="readonly" class="date endDateInput">
									<button class="btn" type="button">
										<i class="icon-calendar"></i>
									</button>
								</span>
							</div>
						</div>
					</div>


					<div class="form-inline tileDispStartDateWrapper">
						<div class="control-group validateme"
							data-validate-flags="nonempty"
							data-validate-fail-msgs='{"nonempty" : "<cms:contentText key="TILE_START_DATE_REQ_ERROR" code="ssi_contest.generalInfo" />"}'>
							<label class="control-label" for="tileStartDate"><cms:contentText
									key="CONTEST_DISPLAY_DATE" code="ssi_contest.generalInfo" /></label>
							<i class="icon-info pageView_help"
                                data-help-content="<cms:contentText key="CONTEST_DISPLAY_DATE_EXPLAIN" code="ssi_contest.generalInfo" />"></i>
							<div class="controls">
								<!-- JAVA NOTE: set data-date-* attrs -->
								<span
									class="input-append datepickerTrigger tileStartDateTrigger"
									data-date-format="<%=UserManager.getUserDatePattern().toLowerCase()%>"
									data-date-language="<%=UserManager.getUserLocale()%>"
									data-date-todaydate="<%=todayDate%>" data-date-autoclose="true">
									<input type="text" id="tileStartDate"
									data-model-key="tileStartDate" readonly="readonly"
									class="date tileStartDateInput">
									<button class="btn" type="button">
										<i class="icon-calendar"></i>
									</button>
								</span>
							</div>
						</div>
					</div>

                    <hr class="section">

                    <!--<div class="control-group">
                        <label class="checkbox">
                            <input type="checkbox" class="showRecurrancePeriod autoBind" data-model-key="includeMessage">
                            <strong>
                                Recurring Contest?
                                <i class="icon-info pageView_help"
                            data-help-content="[explanation of what 'Recurring Contest' is]"></i>
                            </strong>

                        </label>
                        <div class="recurranceGuts validateme"
                                data-validate-flags="maxlength,nonempty"
                                data-validate-fail-msgs='{"maxlength" : "Maximum number of digits exceeded.","nonempty":"Please enter a number of days."}'
                                data-validate-max-length="3"
                                style="display: none">
                                <label class="control-label" for="name">
                                    Recurrance period (in days)
                                </label>
                                <div class="controls">
                                    <input type="number" id="name" class="recurringAmount input-xlarge" data-model-key="name" maxlength="3">
                                </div>
                        </div>
                    </div>

                    <hr class="section">-->

					<div class="control-group contestDescription validateme"
						data-validate-flags="maxlength,nonempty"
						data-validate-fail-msgs='{"maxlength" : "<cms:contentText key="MAX_CHARS_ERROR" code="ssi_contest.generalInfo" />","nonempty":"<cms:contentText key="DESCRIPTION_REQ_ERROR" code="ssi_contest.generalInfo" />"}'
						data-validate-max-length="1000">
						<label class="control-label">
                            <cms:contentText key="CONTEST_DESCRIPTION" code="ssi_contest.generalInfo" />
                            <i class="icon-info pageView_help"
                                data-help-content="<cms:contentText key="CONTEST_PAX_OVERVIEW" code="ssi_contest.generalInfo" />"></i>
						</label>
                        <div class="charCount input-xxlarge tr" data-chars-from=".defaultDescriptionInput">
                            <cms:contentText key="REM_CHAR" code="ssi_contest.generalInfo" />
                            <b>--</b>
                        </div>
						<div class="controls">
							<textarea rows="5" data-max-chars="1000"
								class="defaultDescriptionInput input-xxlarge"></textarea>
						</div>
					</div>

					<div class="translationsWrapper descriptionTranslationsWrapper" style="display:none">
						<div class="translationsSummary descriptionTranslationsSummary">
							<div class="translationsCountMsg" style="display: none">
								<b></b>
								<cms:contentText key="TRANSLATIONS_ADDED"
									code="ssi_contest.generalInfo" />
							</div>
							<a href="#" class="addTranslationsBtn" style="display: none"><cms:contentText
									key="ADD_TRANSLATIONS" code="ssi_contest.generalInfo" /></a> <a
								href="#" class="showTranslationsBtn" style="display: none"><cms:contentText
									key="SHOW_TRANSLATIONS" code="ssi_contest.generalInfo" /></a>
						</div>
						<div class="spinWrap" style="display: none">
							<span class="ajaxSpinner"></span>
							<cms:contentText key="LOADING_TRANSLATIONS"
								code="ssi_contest.generalInfo" />
						</div>
						<div
							class="translationsEditor descriptionTranslationsEditor container-splitter with-splitter-styles"
							style="display: none">
							<a href="#" class="hideTranslationsBtn"><cms:contentText
									key="HIDE_TRANSLATIONS" code="ssi_contest.generalInfo" /></a>
							<table class="translationsTable">
								<thead>
									<tr>
										<th><cms:contentText key="LANGUAGE"
												code="ssi_contest.generalInfo" /></th>
										<th><cms:contentText key="CONTEST_DESCRIPTION"
												code="ssi_contest.generalInfo" /></th>
									</tr>
								</thead>
								<tbody>
									<!-- dynamic -->
									<script id="descriptionItemsTpl"
										type="text/x-handlebars-template">
                                        {{#descriptions}}
                                        <tr>
                                            <td>
                                                {{#if _isNew}}
                                                <select>
                                                {{#each langs}}
                                                    <option value="{{id}}" {{#isSelected}}selected{{/isSelected}} >
                                                        {{name}}
                                                    </option>
                                                {{/each}}
                                                </select>
                                                {{else}}
                                                    {{langName}}
                                                {{/if}}
                                            </td>
                                            <td class="descriptionInputWrapper" data-desc-lang="{{language}}">
                                                <textarea rows="5" data-max-chars="1000" class="descriptionInput input-xxlarge">{{text}}</textarea>
                                            </td>
                                        </tr>
                                        {{/descriptions}}
                                    </script>
									<!-- /#descriptionItemsTpl -->
								</tbody>
							</table>
							<a href="#" class="addAnotherTranslationBtn"><cms:contentText
									key="ADD_ANOTHER_TRANSLATIONS" code="ssi_contest.generalInfo" /></a>
						</div>
					</div>
					<!-- /.translationsWrapper descriptionTranslationsWrapper -->

					<hr class="section">

					<div class="documentsWrapper">
						<strong>
                            <cms:contentText key="ATTACH_DOCUMENT" code="ssi_contest.generalInfo" />
                            <span class="optional"><cms:contentText key="OPTIONAL_FIELD" code="ssi_contest.generalInfo" /></span>
                        </strong>
                        <br>
                        <small><cms:contentText key="ATTACH_DOCUMENT_TYPE" code="ssi_contest.generalInfo" /></small>
						<div class="fileInputWrapper">
							<input type="file" name="documentFile"
								class="documentFileInput uploaderFileInput">
							<button class="btn btn-primary uploadDocBtn">

								<span class="noDocMsg docBtnMsg"><cms:contentText key="BROWSE" code="system.button" /></span>
                                <i class="icon-upload-1"></i>
							</button>
							<span class="label label-important uploadError" style="display: none">
								<cms:contentText key="UPLOAD_ERROR" code="ssi_contest.generalInfo" />
								<span><!-- js dyn error text --></span>
							</span>
							<span class="docOrigName" style="display: none"><!-- dyn --></span>
							<i class="remDocBtn btn btn-mini btn-icon btn-danger icon-trash" title=<cms:contentText key="REMOVE_DOCUMENT" code="system.general" />></i>
						</div>

						<div class="uploadingIndicator" style="display: none">
							<div class="uploadingSpinner"></div>
							<cms:contentText key="UPLOADING" code="ssi_contest.generalInfo" />
						</div>

						<div class="control-group docDisplayName" style="display:none">
							<label class="control-label" for="docDispNameInput"> <cms:contentText
									key="DOCUMENT_DIS_NAME" code="ssi_contest.generalInfo" />
							</label>
							<div class="controls">
								<input type="text" id="docDispNameInput" class="docDispNameInput" maxlength="50">
							</div>
						</div>

                        <div class="docRemoveDialog" style="display:none">
                            <p>
                                <b><cms:contentText key="REMOVE_DOCUMENT" code="ssi_contest.generalInfo" /></b>
                            </p>
                            <p>
                                <cms:contentText key="DOCUMENT_WILL_DELETE" code="ssi_contest.generalInfo" />
                            </p>
                            <p class="tc">
                                <a class="btn btn-small confirmBtn closeTip"><cms:contentText key="YES" code="system.general" /></a>
                                <a class="btn btn-small cancelBtn closeTip"><cms:contentText key="NO" code="system.general" /></a>
                            </p>
                        </div><!-- /.docRemoveDialog -->

					</div>
					<!-- /.documentsWrapper -->

					<div class="translationsWrapper documentsTranslationsWrapper" style="display:none">
						<div class="translationsSummary documentsTranslationsSummary">
							<div class="translationsCountMsg" style="display: none">
								<b></b>
								<cms:contentText key="TRANSLATIONS_ADDED"
									code="ssi_contest.generalInfo" />
							</div>
							<a href="#" class="addTranslationsBtn" style="display: none"><cms:contentText
									key="ADD_TRANSLATIONS" code="ssi_contest.generalInfo" /></a> <a
								href="#" class="showTranslationsBtn" style="display: none"><cms:contentText
									key="SHOW_TRANSLATIONS" code="ssi_contest.generalInfo" /></a>
						</div>
						<div class="spinWrap" style="display: none">
							<span class="ajaxSpinner"></span>
							<cms:contentText key="LOADING_TRANSLATIONS"
								code="ssi_contest.generalInfo" />
						</div>
						<div
							class="translationsEditor documentsTranslationsEditor container-splitter with-splitter-styles"
							style="display: none">
							<a href="#" class="hideTranslationsBtn"><cms:contentText
									key="HIDE_TRANSLATIONS" code="ssi_contest.generalInfo" /></a>
							<table class="translationsTable">
								<thead>
									<tr>
										<th><cms:contentText key="LANGUAGE"
												code="ssi_contest.generalInfo" /></th>
										<th><cms:contentText key="ATTACH_DOCUMENT"
												code="ssi_contest.generalInfo" /></th>
										<th><cms:contentText key="DOCUMENT_DIS_NAME"
												code="ssi_contest.generalInfo" /></th>
									</tr>
								</thead>
								<tbody>
									<!-- dynamic -->
									<script id="documentItemsTpl" type="text/x-handlebars-template">
                                        {{#docs}}
                                        <tr>
                                            <td>
                                                {{#if _isNew}}
                                                <select>
                                                {{#each langs}}
                                                    <option value="{{id}}" {{#isSelected}}selected{{/isSelected}} >
                                                        {{name}}
                                                    </option>
                                                {{/each}}
                                                </select>
                                                {{else}}
                                                    {{langName}}
                                                {{/if}}
                                            </td>
                                            <td class="fileInputCell">
                                                <div class="fileInputWrapper" data-doc-lang="{{language}}" data-doc-is-new="{{_isNew}}">
                                                    <input type="file" name="documentFile" class="documentFileInput uploaderFileInput">
                                                    <button class="btn btn-primary btn-small uploadDocBtn">

                                                        <span class="hasDocMsg docBtnMsg" {{#unless url}}style="display:none"{{/unless}}>Change</span>
                                                        <span class="noDocMsg docBtnMsg" {{#url}}style="display:none"{{/url}}>Upload</span>

                                                        <i class="icon-upload-1"></i>
                                                    </button>
                                                    <span class="label label-important uploadError" style="display:none">
                                                        <cms:contentText key="UPLOAD_ERROR" code="ssi_contest.payout_objectives" /> <span><!-- js dyn error text --></span>
                                                    </span>
                                                    {{#originalFilename}}
                                                        <span class="docOrigName">{{this}}</span>
                                                    {{/originalFilename}}
                                                </div>

                                                <div class="uploadingIndicator" style="display:none">
                                                    <div class="uploadingSpinner"></div>
                                                    <cms:contentText key="UPLOADING" code="ssi_contest.payout_objectives" />
                                                    <!--
                                                    <div class="progress progress-striped active" style="display:none">
                                                        <div class="bar" style="width:0%"></div>
                                                    </div>
                                                    -->
                                                </div>
                                            </td>
                                            <td>
                                                <input class="documentInput" type="text" value="{{name}}" data-index="{{index}}"/>
                                            </td>
                                        </tr>
                                        {{/docs}}
                                    </script>
									<!-- /#documentItemsTpl -->
								</tbody>
							</table>
							<a href="#" class="addAnotherTranslationBtn"><cms:contentText
									key="ADD_ANOTHER_TRANSLATIONS" code="ssi_contest.generalInfo" /></a>
						</div>
					</div>
					<!-- /.translationsWrapper documentsTranslationsWrapper -->

					<hr class="section">

                    <!--
						ATN
                    -->
                    <div class="displayForAwardThemNow" style="display:none">

                        <h5><cms:contentText key="MEASURE_ACTIVITY_IN" code="ssi_contest.approvals.summary" /></h5>
                        <div class="control-group validateme measureActivityContGroup"
                            data-validate-flags="nonempty"
                            data-validate-fail-msgs="{&quot;nonempty&quot; : &quot;Please make a choice&quot;}">
                            <label class="radio" for="measureActivityCurrency_infoStep">
                                <input type="radio" class="measureTypeRadio autoBind" id="measureActivityCurrency_infoStep" name="measureType" value="currency" data-model-key="measureType">
                                <cms:contentText key="CURRENCY_REVENUE" code="ssi_contest.creator" />
                            </label>
                            <div class="currencyTypeWrapper" style="display:none">
                                <div><cms:contentText key="CURRENCY_TYPE" code="ssi_contest.payout_dtgt" /></div>
                                <select name="currencyTypeSelect" class="autoBind dropdown-toggle" id="currencyTypeSelect" data-model-key="currencyTypeId">

                                    <script id="currencyTypeOptionsTpl" type="text/x-handlebars-template">
                                        {{#currencyTypes}}
                                            <option value="{{id}}">{{name}}</option>
                                        {{/currencyTypes}}
                                    </script><!-- #currencyTypeSelectTpl -->

                                </select>
                            </div>
                            <label class="radio" for="measureActivityUnits_infoStep">
                                <input type="radio" class="measureTypeRadio autoBind" id="measureActivityUnits_infoStep" name="measureType" value="units" data-model-key="measureType">
                                <cms:contentText key="UNITS_NUMBER" code="ssi_contest.payout_objectives" />
                            </label>
                        </div>
                         <div class="awardThemNowOnly measureTypeDispOnly" style="display: none">
                            <span class="dispTxt"><!-- dyn --></span>
                        </div>

                        <hr class="section">

                        <h5><cms:contentText key="PAYOUT_TYPE" code="ssi_contest.payout_objectives" /></h5>
                        <div class="control-group validateme payoutContGroup"
                            data-validate-flags="nonempty"
                            data-validate-fail-msgs="{&quot;nonempty&quot; : &quot;Please make a choice&quot;}">
                            <label class="radio" for="payoutTypePoints_infoStep">
                                <input type="radio" class="payoutTypeRadio autoBind" id="payoutTypePoints_infoStep" name="payoutType" value="points" data-model-key="payoutType">
                                <cms:contentText key="POINTS" code="ssi_contest.payout_objectives" />
                            </label>
                            <label class="radio" for="payoutTypeOther_infoStep">
                                <input type="radio" class="payoutTypeRadio autoBind" id="payoutTypeOther_infoStep" name="payoutType" value="other" data-model-key="payoutType">
                                <cms:contentText key="OTHER" code="ssi_contest.payout_objectives" />
                                <i class="icon-info pageView_help"
                                data-help-content="<cms:contentText key="OTHER_EXPLAIN" code="ssi_contest.generalInfo" />"></i>
                            </label>
                            <div class="otherPayoutTypeWrapper" style="display:none">
                                <div><cms:contentText key="VALUE_OF_AWARD_IN" code="ssi_contest.payout_objectives" /></div>
                                <select name="otherPayoutTypeSelect" class="autoBind" id="otherPayoutTypeSelect" data-model-key="otherPayoutTypeId">
                                    <!-- dyn using subTpl.currencyTypeOptions -->
                                </select>
                            </div>
                        </div>
						<div class="awardThemNowOnly payoutTypeDispOnly" style="display: none">
                            <span class="dispTxt"><!-- dyn --></span>
                        </div>

                        <div class="billToRoot"></div>

                        <div class="badgesOuterWrapper control-group" style="display:none">

                            <hr class="section">

                            <h5><cms:contentText key="BADGE" code="ssi_contest.payout_objectives" /></h5>
                            <div class="badgesSelectorView" data-msg-no-badge="No Badge"><!-- dyn --></div>
                        </div>

                        <hr class="section">
                    </div><!-- .displayForAwardThemNow -->

                    <!--
						MESSAGE
                    -->
					<div class="personalMessageWrapper">
						<div class="control-group">
							<label class="checkbox">
								<input type="checkbox" class="showMessageText autoBind" data-model-key="includeMessage" checked>
								<strong>
									<cms:contentText key="INCLUDE_PERSONAL_MESG"
										code="ssi_contest.generalInfo" />
									<i class="icon-info pageView_help"
                                data-help-content="<cms:contentText key="PERSONAL_MSG_EXPLAIN" code="ssi_contest.generalInfo" />"></i>
                                	<span class="optional"><cms:contentText key="OPTIONAL_FIELD" code="ssi_contest.generalInfo" /></span>
								</strong>

							</label>
							<div class="messageTextGuts validateme"
								data-validate-flags="maxlength,nonempty"
								data-validate-fail-msgs='{"maxlength" : "<cms:contentText key="MAX_CHARS_ERROR" code="ssi_contest.generalInfo" />","nonempty":"<cms:contentText key="MESSAGE_REQ_ERROR" code="ssi_contest.generalInfo" />"}'
								data-validate-max-length="140">
								<label class="control-label" for=""> <cms:contentText
										key="MESSAGE" code="ssi_contest.generalInfo" />
								</label>
								<div class="controls">
									<textarea rows="5" data-max-chars="140"
										class="richtext defaultMessageInput"></textarea>
								</div>
							</div>
						</div>

						<div class="translationsWrapper messageTranslationsWrapper" style="display:none">
							<div class="translationsSummary messageTranslationsSummary">
								<div class="translationsCountMsg" style="display: none">
									<b></b>
									<cms:contentText key="TRANSLATIONS_ADDED"
										code="ssi_contest.generalInfo" />
								</div>
								<a href="#" class="addTranslationsBtn" style="display: none"><cms:contentText
										key="ADD_TRANSLATIONS" code="ssi_contest.generalInfo" /></a> <a
									href="#" class="showTranslationsBtn" style="display: none"><cms:contentText
										key="SHOW_TRANSLATIONS" code="ssi_contest.generalInfo" /></a>
							</div>
							<div class="spinWrap" style="display: none">
								<span class="ajaxSpinner"></span>
								<cms:contentText key="LOADING_TRANSLATIONS"
									code="ssi_contest.generalInfo" />
							</div>
							<div
								class="translationsEditor messageTranslationsEditor container-splitter with-splitter-styles"
								style="display: none">
								<a href="#" class="hideTranslationsBtn"><cms:contentText
										key="HIDE_TRANSLATIONS" code="ssi_contest.generalInfo" /></a>
								<table class="translationsTable">
									<thead>
										<tr>
											<th><cms:contentText key="LANGUAGE"
													code="ssi_contest.generalInfo" /></th>
											<th><cms:contentText key="MESSAGE"
													code="ssi_contest.generalInfo" /></th>
										</tr>
									</thead>
									<tbody>
										<!-- dynamic -->
										<script id="messageItemsTpl" type="text/x-handlebars-template">
	                                        {{#messages}}
	                                        <tr>
	                                            <td>
	                                                {{#if _isNew}}
	                                                <select>
	                                                {{#each langs}}
	                                                    <option value="{{id}}" {{#isSelected}}selected{{/isSelected}} >
	                                                        {{name}}
	                                                    </option>
	                                                {{/each}}
	                                                </select>
	                                                {{else}}
	                                                    {{langName}}
	                                                {{/if}}
	                                            </td>
	                                            <td class="messageInputWrapper" data-mess-lang="{{language}}">
	                                                <textarea rows="5" data-max-chars="1000" placeholder="" class="richtext messageInput">{{text}}</textarea>
	                                            </td>
	                                        </tr>
	                                        {{/messages}}
	                                    </script>
										<!-- /#messageItemsTpl -->
									</tbody>
								</table>
								<a href="#" class="addAnotherTranslationBtn"><cms:contentText
										key="ADD_ANOTHER_TRANSLATIONS" code="ssi_contest.generalInfo" /></a>
							</div>
						</div><!-- /.translationsWrapper messageTranslationsWrapper -->

						<hr class="section">

					</div><!-- /.personalMessageWrapper -->


                    <!--
 							APPROVERS
 				     -->
					<div class="approversAjaxSpinner" style="display: none">
						<span class="ajaxSpinner"></span>
						<cms:contentText key="LOADING_APPROVERS"
							code="ssi_contest.generalInfo" />
					</div>
					<div class="approversWrapper" style="display: none">
						<strong>
							<cms:contentText key="CONTEST_APPPROVERS" code="ssi_contest.generalInfo" />
							<span class="optional"><cms:contentText key="MULTIPLE_APPROVERS" code="ssi_contest.generalInfo" /></span>
						</strong>
						<div class="contestApproversLists control-group">
							<!-- generated content -->
						</div>
					</div>

					<!-- client side template for approvers levels -->
					<!-- TODO: make a nice widget to select approvers, this is being used for expedience -->
					<script id="approversTpl" type="text/x-handlebars-template">
                        {{#contestApprovers}}
                            <label class="levelLabel control-label" for="contestApproversLevel{{id}}">{{name}}</label>
                            <div class="controls">
                                <select id="contestApproversLevel{{id}}" data-level-id="{{id}}" multiple="multiple" size="5">
									<option disabled selected class="hidden">
										<cms:contentText key="SELECT_APPROVER" code="ssi_contest.generalInfo" />
									</option>
                                {{#approvers}}
                                    <option value="{{id}}" {{#selected}}selected{{/selected}}>
                                        {{lastName}}, {{firstName}}
                                    </option>
                                {{/approvers}}
                                </select>
                            </div>
                        {{/contestApprovers}}
                    </script>
					<!-- /#approversTpl -->

					<hr class="section">

					<div class="stepContentControls">
						<button class="btn btn-primary btn-inverse fr saveDraftBtn btn-fullmobile"
							style="display: none;">
							<cms:contentText key="SAVE_AS_DRAFT"
								code="ssi_contest.generalInfo" />
								</button>
                        	<button class="btn fr cancelAtnBtn btn-fullmobile" style="display:none;">
                            <cms:contentText key="CANCEL" code="ssi_contest.generalInfo" />

						</button>
						<span></span>
						<button class="btn btn-primary nextBtn btn-fullmobile">
							<cms:contentText key="NEXT" code="ssi_contest.generalInfo" />
							&raquo;
						</button>
						<button class="btn btn-primary saveAtnBtn btn-fullmobile" style="display: none">
                            <cms:contentText key="SAVE" code="ssi_contest.generalInfo" />
                        </button>
					</div>
					<!-- /.stepContentControls -->

				</div>
				<!-- /.stepInfoContent -->


<!--
PARTICIPANTS AND MANAGERS
-->
				<div id="contestEditTabParticipantsManagersView"
					class="stepParticipantsManagersContent stepContent"
					style="display: none">

					<h5 class="defaultName">
						<!-- dynamic -->
					</h5>

					<div class="paxManLoadSpinnerWrapper" style="display: none">
						<span class="paxManLoadSpinner"></span>
						<cms:contentText key="LOADING" code="ssi_contest.generalInfo" />
					</div>

                    <div class="spreadsheetImportWrapper hideOnAwardThemNow">
                        <div class="paxSelectWrap paxSelectWrapForParticipants">
                            <form id="ssiUploadParticipantsForm" class="ssiShowHideForm span12" method="post" action="" enctype="multipart/form-data">
                                <h4><cms:contentText key="EXTRACT_INFO_TEXT" code="ssi_contest.pax.manager" /></h4>
                                <strong><cms:contentText key="SELECT_DOC" code="ssi_contest.pax.manager" /></strong>
                                <p><cms:contentText key="EXTRACT_GENERAL_TEXT" code="ssi_contest.pax.manager" />
                                  <a href="createGeneralInfo.do?method=extractReport">
                                <cms:contentText key="EXTRACT_DOWNLOAD" code="ssi_contest.pax.manager" /></a></p>
                                <div class="ssiUploadSSButton control-group validateme" data-extra-validate='<cms:contentText key="SUPPORTED_FILE_FORMAT" code="ssi_contest.pax.manager" />'>
                                    <div class="controls">
                                        <input type="file" id="ssiHiddenUploadParticipants" name="ssiHiddenUpload"/>
                                    </div>
                                </div>
                                <div id="ssiUploadParticipantsError" class="alert-danger" style="display:none">
                                </div>
                                <div class="ssiUploadFormActions hidden">
                                   <button class="btn btn-primary ssiUploadParticipantsSave" disabled><cms:contentText key="SUBMIT" code="ssi_contest.generalInfo" /></button>
                                   <button class="btn ssiUploadParticipantsCancel" disabled><cms:contentText key="CANCEL" code="ssi_contest.generalInfo" /></button>
                                </div>
                            </form>
                        </div>
                    </div> <!-- /.spreadsheetImportWrapper -->

                    <hr class="section">

					<div class="paxSelectContextWrapper participantsWrapper" data-context="participant" style="display: none">
                        <div class="errorPaxCount alert-danger" style="display:none;">
                            <cms:contentText key="ADD_TWO_PAX" code="ssi_contest.creator" />
                        </div>
                        <div class="paxSelectWrap paxSelectWrapForParticipants">
    						<h4><cms:contentText key='PARTICIPANTS' code='ssi_contest.pax.manager' /></h4>
                            <p class="hideOnAwardThemNow"><cms:contentText key="PAX_INFO_TEXT1" code="ssi_contest.pax.manager" /></p>
                            <p class="showOnAwardThemNow" style="display:none"><cms:contentText key="PAX_INFO_TEXT2" code="ssi_contest.pax.manager" /></p>

                            <!-- pax search start -->
                            <ul class="nav nav-tabs" id="myTab">
                                <li class="filter-search active"><a href="#filterSearch"><cms:contentText key="SEARCH_BY_RECIPIENT_TEAM" code="ssi_contest.creator" /></a></li>
                                <li class="name-search"><a href="#nameSearch"><cms:contentText key="SEARCH_BY_INDIVIDUAL" code="ssi_contest.creator" /></a></li>
                            </ul>

                            <div class="tab-content" style="overflow: inherit;">
                                <div class="tab-pane active" id="filterSearch"><div class='paxSearchFilterStartView'  data-search-url="${pageContext.request.contextPath}/ssiParticipantSearch/ssiParticipantSearch.action"></div></div>
                                <div class="tab-pane" id="nameSearch"><div class='paxSearchStartView' data-search-url="${pageContext.request.contextPath}/ssiParticipantSearch/ssiParticipantSearch.action"></div></div>
                            </div>

                            <script>
                                $('#myTab a').click(function (e) {
                                    e.preventDefault();
                                    $(this).tab('show');
                                });
                            </script>
                            <!-- /.paxSearchStartView -->




                            <script id="participantSearchTableRowTpl" type="text/x-handlebars-template">
                                <tr class="participantSearchResultRow{{#if isSelected}} selected{{/if}}{{#if isLocked}} locked{{/if}}"><!--
                                    --><td class="selectCell{{#if isSelected}} selected{{/if}}" data-participant-id="{{id}}">

                                        {{#if isLocked}}
                                            <i class='icon-lock'></i>
                                        {{else}}

                                            {{#if _selectTxt}}
                                                <a href="#"
                                                    data-participant-id="{{id}}"
                                                    class="participantSelectControl select-txt"
                                                >{{{_selectTxt}}}</a>
                                                <span class="deselTxt selected-txt">{{{_selectedTxt}}}</span>
                                            {{else}}
                                                <input name="partSelCheck{{id}}"
                                                    type="checkbox"
                                                    data-participant-id="{{id}}"
                                                    class="participantSelectControl"
                                                    {{#if isSelected}} checked="checked"{{/if}} />
                                            {{/if}}

                                        {{/if}}
                                    </td><!--
                                    --><td class="participantSearchNameCol">
                                        {{firstName}} {{lastName}}
                                    </td><!--
                                    --><td class="participantSearchOrgCol">
                                        {{! this is just the first node in the node array }}
                                        {{nodes.0.name}}
                                    </td><!--
                                    --><td class="participantSearchCountryCol">
                                        {{#if countryCode}}<img class="countryFlag" src="../assets/img/flags/{{countryCode}}.png" alt="{{countryCode}}" title="{{countryName}}" />{{/if}}
                                    </td><!--
                                    --><td class="participantSearchDepartmentCol">{{departmentName}}</td>
                                    <td class="participantSearchJobCol">{{jobName}}</td><!--
                                --></tr>
                            </script><!-- /#participantSearchTableRowTpl -->


                            <div id="ssiParticipants">
    							<!-- ParticipantPaginatedView -->
    						</div><!-- /#ssiParticipants -->

    						<script id="ssiParticipantsPaginatedViewTpl" type="text/x-handlebars-template">
                                <div class="spincover" style="display:none;"><div class="spin"></div></div>
                                <div class="paginationConts pagination pagination-right"></div>
                                <div class="emptyMsg alert" style="display:none"><cms:contentText key="NOT_ADDED_ANYONE" code="ssi_contest.pax.manager"/></div>
                                <div class="hasPax container-splitter with-splitter-styles participantCollectionViewWrapper" style="display:none">
                                    <h5>
										<span class="paxMode"><cms:contentText key="SELECTED_PARTICIPANTS" code="ssi_contest.pax.manager"/></span>
										<span class="manMode"><cms:contentText key="SELECTED_MANAGERS" code="ssi_contest.pax.manager"/></span>
									</h5>
                                    <table class="table table-condensed table-striped">
                                        <thead>
                                            <tr>
                                                <th class="participant sortHeader sortable" data-sort="lastName">
                                                    <a href="#">
                                                    	<span class="paxMode">
                                                    		<cms:contentText key="PARTICIPANT" code="ssi_contest.pax.manager" />
                                                            <i class="icon-arrow-1-up"></i>
                                                            <i class="icon-arrow-1-down"></i>
                                                    	</span>
                                                        <span class="manMode">
                                                        	<cms:contentText key="MANAGER" code="ssi_contest.pax.manager" />
                                                            <i class="icon-arrow-1-up"></i>
                                                            <i class="icon-arrow-1-down"></i>
                                                        </span>
                                                        <span class="superViewerMode">
                                                            <cms:contentText key="SUPERVIEWER" code="ssi_contest.pax.manager" />
                                                            <i class="icon-arrow-1-up"></i>
                                                            <i class="icon-arrow-1-down"></i>
                                                        </span>
                                                    </a>
                                                </th>
                                                <th class="remove"><cms:contentText key="REMOVE" code="ssi_contest.pax.manager" /></th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                        </tbody>
                                    </table>
                                </div>
                                <div class="paginationConts pagination pagination-right"></div>

                                <!--subTpl.paxRow=
                                    <tr class="participant-item" data-participant-id="{{id}}">
                                        <td class="participant">
                                            <a class="participant-popover" href="#" data-participant-ids="[{{userId}}]">
                                                {{lastName}}, {{firstName}}
                                                <img src="../assets/img/flags/{{countryCode}}.png" alt="{{countryCode}}" class="countryFlag" title="{{countryName}}" />
                                            </a>
                                            <span class="singleRecipNode">{{orgName}}</span>
                                            <div class="org">
                                                {{#if departmentName}}{{departmentName}}{{/if}} {{#if jobName}}{{#if departmentName}}&bull;{{else}}{{#if orgName}}&bull;{{/if}}{{/if}} {{jobName}}{{/if}}
                                            <div>
                                        </td>
                                        <td class="remove">
                                            <a class="remParticipantControl" title=<cms:contentText key="REMOVE_THIS_PARTICIPANT" code="claims.submission" /> data-participant-id="{{id}}"><i class="icon-trash"></i></a>
                                        </td>
                                    </tr>
                                subTpl-->
                            </script><!-- /.ssiParticipantsPaginatedViewTpl -->

                        </div><!-- /.paxSelectWrap -->


                        <hr class="section hideOnAwardThemNow">


                        <div class="paxSelectWrap hideOnAwardThemNow">
                            <h4><cms:contentText key="MANAGERS" code="ssi_contest.pax.manager" /></h4>
                            <p><cms:contentText key="MANAGER_INFO_TEXT1" code="ssi_contest.pax.manager" /></p>
                            <p class="showIfHasMan">
                                <strong>
                                    <span class="manCountBind">#</span>
                                    <cms:contentText key="MGR_SELECTED" code="ssi_contest.pax.manager" />
                                </strong>
                            </p>
    						<button class="btn btn-primary doSelectManagers" data-context="managers">
                                <span class="hideIfHasMan">
                                    <cms:contentText key="SELECT_MGRS" code="ssi_contest.pax.manager" />
                                </span>
                                <span class="showIfHasMan">
                                    <cms:contentText key="ADD_MORE_MANAGERS" code="ssi_contest.pax.manager" />
                                </span>
                            </button>
                            <span class="paxManSearchSpinnerWrapper" style="display: none">
    							<div class="paxManSearchSpinner"></div>
                                <cms:contentText key="LOADING" code="ssi_contest.generalInfo" />
    						</span>
                        </div><!-- /.paxSelectWrap -->

                        <hr class="section">

                        <div class="paxSelectWrap hideOnAwardThemNow">
                            <h4><cms:contentText key="SUPERVIEWERS" code="ssi_contest.pax.manager" /></h4>
                            <p><cms:contentText key="SUPERVIEWER_INFO_TEXT1" code="ssi_contest.pax.manager" /></p>
                            <p class="showIfHasSuperViewer">
                                <strong>
                                    <span class="superViewerCountBind">#</span>
                                    <cms:contentText key="SUPERVIEWER_SELECTED" code="ssi_contest.pax.manager" />
                                </strong>
                            </p>
                            <button class="btn btn-primary doSelectSuperViewers" data-context="superViewers">
                                <span class="hideIfHasSuperViewer">
                                    <cms:contentText key="SELECT_SUPERVIEWERS" code="ssi_contest.pax.manager" />
                                </span>
                                <span class="showIfHasSuperViewer">
                                    <cms:contentText key="ADD_MORE_SUPERVIEWERS" code="ssi_contest.pax.manager" />
                                </span>
                            </button>
                        </div><!-- /.paxSelectWrap -->
						
						<hr class="section hideOnAwardThemNow">
						
					</div><!-- /.participantsWrapper -->

					<div class="paxSelectContextWrapper managersWrapper" data-context="manager" style="display: none">
                        <div class="paxSelectWrap">
                        	<h4><cms:contentText key="PARTICIPANTS" code="ssi_contest.pax.manager" /></h4>
                            <p><cms:contentText key="PAX_INFO_TEXT1" code="ssi_contest.pax.manager" /></p>
                            <p class="showIfHasPax">
                                <strong>
                                    <span class="paxCountBind">#</span>
                                    <cms:contentText key="PAX_SELECTED" code="ssi_contest.pax.manager" />
                                </strong>
                            </p>
                            <button class="btn btn-primary doSelectParticipants" data-context="participants">
                                <span class="hideIfHasPax">
                                    <cms:contentText key="SELECT_PARTICIPANTS" code="ssi_contest.pax.manager" />
                                </span>
                                <span class="showIfHasPax">
                                    <cms:contentText key="ADD_MORE_PAX" code="ssi_contest.pax.manager" />
                                </span>
                            </button>
                        </div><!-- /.paxSelectWrap -->

                        <hr class="section">

                        <div class="paxSelectWrap">
                            <h4><cms:contentText key="MANAGERS" code="ssi_contest.pax.manager" /></h4>
                            <p><cms:contentText key="MANAGER_INFO_TEXT1" code="ssi_contest.pax.manager" /></p>
    						<div class="managerSearchTableWrapper">
    							<div class="scrollTable">
    								<table
    									class="table table-striped table-condensed"
    									data-msg-no-results="<cms:contentText key="NO_MANAGERS_FOUND" code="ssi_contest.pax.manager" />">
    									<thead>
    										<tr>
    											<th class="selectHeader">
                                                    <span class="msgSelect" style="display: none">
                                                        <cms:contentText key="SELECT" code="system.button" />
                                                    </span>
    												<button class="btn btn-mini selectAllBtn">
    													<cms:contentText key="SELECT_ALL" code="profile.personal.info" />
    												</button>
                                                </th>
    											<th class="sortHeader sortControl unsorted nameHeader" data-sort="lastName">
                                                    <a href="#">
                                                        <cms:contentText key="MANAGER_NAME" code="ssi_contest.pax.manager" />
                                                        <i class="icon-arrow-1-up"></i>
                                                        <i class="icon-arrow-1-down"></i>
                                                    </a>
                                                </th>
    											<th class="sortHeader sortControl unsorted orgHeader" data-sort="orgName">
                                                    <a href="#">
                                                        <cms:contentText key="ORG_NAME" code="profile.personal.info" />
                                                        <i class="icon-arrow-1-up"></i>
                                                        <i class="icon-arrow-1-down"></i>
                                                    </a>
                                                </th>
    											<th class="sortHeader sortControl unsorted orgTypeHeader" data-sort="orgType">
                                                    <a href="#">
            											<cms:contentText key="ORG_TYPE" code="ssi_contest.approvals.summary" />
                                                        <i class="icon-arrow-1-up"></i>
    	       											<i class="icon-arrow-1-down"></i>
                                                    </a>
                                                </th>
    											<th class="sortHeader sortControl unsorted departmentHeader" data-sort="departmentName">
                                                    <a href="#">
                                                        <cms:contentText key="DEPARTMENT" code="profile.personal.info" />
                                                        <i class="icon-arrow-1-up"></i>
                                                        <i class="icon-arrow-1-down"></i>
                                                    </a>
    											</th>
    											<th class="sortHeader sortControl unsorted jobHeader" data-sort="jobName">
                                                    <a href="#">
                                                        <cms:contentText key="JOB_TITLE" code="participant.search" />
                                                        <i class="icon-arrow-1-up"></i>
                                                        <i class="icon-arrow-1-down"></i>
                                                    </a>
                                                </th>
    										</tr>
    									</thead>
    									<tbody>
    										<!-- dyn -->
    									</tbody>
    								</table>
    							</div>
    							<script id="manSearchRowsTpl" type="text/x-handlebars-template">
                                    {{#managers}}
                                    <tr><!--
                                        --><td id="manSearchSelCell{{id}}" class="manSearchSelCell">
                                            <a href="#" data-participant-id="{{id}}"
                                                    class="participantSelectControl select-txt"
                                                    {{#if _isChecked}} style="display:none" {{/if}}>
                                                <cms:contentText key="ADD" code="participant.search" />
                                            </a>
                                            <span class="deselTxt selected-txt"
                                                    {{#unless _isChecked}} style="display:none" {{/unless}}>
                                                <i class="icon icon-check"></i>
                                            </span>
                                        </td><!--
                                        --><td>
                                            {{firstName}} {{lastName}}
                                        </td><!--
                                        --><td>{{orgName}}</td><!--
                                        --><td>{{orgType}}</td><!--
                                        --><td>{{departmentName}}</td><!--
                                        --><td>{{jobName}}</td><!--
                                    --></tr>
                                    {{/managers}}
                                </script>
    						</div><!-- /.managerSearchTableWrapper -->


    						<div id="ssiManagers">
    							<!-- ParticipantPaginatedView - using #ssiParticipantsPaginatedViewTpl (above) -->
    						</div><!-- /#ssiManagers -->

                        </div><!-- /.paxSelectWrap -->

                        <hr class="section">

                        <div class="paxSelectWrap">
                            <h4><cms:contentText key="SUPERVIEWERS" code="ssi_contest.pax.manager" /></h4>
                            <p><cms:contentText key="SUPERVIEWER_INFO_TEXT1" code="ssi_contest.pax.manager" /></p>
                            <p class="showIfHasSuperViewer">
                                <strong>
                                    <span class="superViewerCountBind">#</span>
                                    <cms:contentText key="SUPERVIEWER_SELECTED" code="ssi_contest.pax.manager" />
                                </strong>
                            </p>
                            <button class="btn btn-primary doSelectSuperViewers" data-context="superViewers">
                                <span class="hideIfHasSuperViewer">
                                    <cms:contentText key="SELECT_SUPERVIEWERS" code="ssi_contest.pax.manager" />
                                </span>
                                <span class="showIfHasSuperViewer">
                                    <cms:contentText key="ADD_MORE_SUPERVIEWERS" code="ssi_contest.pax.manager" />
                                </span>
                            </button>
                        </div><!-- /.paxSelectWrap -->

						<hr class="section">

                    </div><!-- /.managersWrapper -->

                    <div class="paxSelectContextWrapper superViewersWrapper" data-context="superViewer" style="display:none">
                        <div class="paxSelectWrap">
                            <h4><cms:contentText key="PARTICIPANTS" code="ssi_contest.pax.manager" /></h4>
                            <p><cms:contentText key="PAX_INFO_TEXT1" code="ssi_contest.pax.manager" /></p>
                            <p class="showIfHasPax">
                                <strong>
                                    <span class="paxCountBind">#</span>
                                    <cms:contentText key="PAX_SELECTED" code="ssi_contest.pax.manager" />
                                </strong>
                            </p>
                            <button class="btn btn-primary doSelectParticipants" data-context="participants">
                                <span class="hideIfHasPax">
                                    <cms:contentText key="SELECT_PARTICIPANTS" code="ssi_contest.pax.manager" />
                                </span>
                                <span class="showIfHasPax">
                                    <cms:contentText key="ADD_MORE_PAX" code="ssi_contest.pax.manager" />
                                </span>
                            </button>
                        </div><!-- /.paxSelectWrap -->

                        <hr class="section">

                        <div class="paxSelectWrap hideOnAwardThemNow">
                            <h4><cms:contentText key="MANAGERS" code="ssi_contest.pax.manager" /></h4>
                            <p><cms:contentText key="MANAGER_INFO_TEXT1" code="ssi_contest.pax.manager" /></p>
                            <p class="showIfHasMan">
                                <strong>
                                    <span class="manCountBind">#</span>
                                    <cms:contentText key="MGR_SELECTED" code="ssi_contest.pax.manager" />
                                </strong>
                            </p>
                            <button class="btn btn-primary doSelectManagers" data-context="managers">
                                <span class="hideIfHasMan">
                                    <cms:contentText key="SELECT_MGRS" code="ssi_contest.pax.manager" />
                                </span>
                                <span class="showIfHasMan">
                                    <cms:contentText key="ADD_MORE_MANAGERS" code="ssi_contest.pax.manager" />
                                </span>
                            </button>
                            <span class="paxManSearchSpinnerWrapper" style="display: none">
                                <div class="paxManSearchSpinner"></div>
                                <cms:contentText key="LOADING" code="ssi_contest.generalInfo" />
                            </span>
                        </div><!-- /.paxSelectWrap -->

                        <hr class="section">

                        <div class="paxSelectWrap paxSelectWrapForSuperViewers">
                            <h4><cms:contentText key="SUPERVIEWERS" code="ssi_contest.pax.manager" /></h4>
                            <p><cms:contentText key="SUPERVIEWER_INFO_TEXT1" code="ssi_contest.pax.manager" /></p>
                            <!-- super viewers search start -->
                            <ul class="nav nav-tabs" id="myTab-2">
                                <li class="filter-search active"><a href="#filterSearchSV"><cms:contentText key="SEARCH_BY_RECIPIENT_TEAM" code="ssi_contest.creator" /></a></li>
                                <li class="name-search"><a href="#nameSearchSV"> <cms:contentText key="SEARCH_BY_INDIVIDUAL" code="ssi_contest.creator" /></a></li>
                            </ul>

                            <div class="tab-content" style="overflow: inherit;">
                                <div class="tab-pane active" id="filterSearchSV" ><div class="superViewersSearchFilterStartView" data-search-url="${pageContext.request.contextPath}/ssiParticipantSearch/ssiParticipantSearch.action" ></div></div>
                                <div class="tab-pane" id="nameSearchSV" ><div class="superViewersSearchStartView" data-search-url="${pageContext.request.contextPath}/ssiParticipantSearch/ssiParticipantSearch.action" ></div></div>
                            </div>

                            <script>
                                $('#myTab-2 a').click(function (e) {
                                    e.preventDefault();
                                    $(this).tab('show');
                                });
                            </script>
                            <!-- /.superViewersSearchStartView -->

                            <!-- super viewers search for paxes -->

                            <script id="superViewerSearchTableRowTpl" type="text/x-handlebars-template">
                                <tr class="superViewerSearchResultRow{{#if isSelected}} selected{{/if}}{{#if isLocked}} locked{{/if}}"><!--
                                    --><td class="selectCell{{#if isSelected}} selected{{/if}}" data-participant-id="{{id}}">

                                        {{#if isLocked}}
                                            <i class='icon-lock'></i>
                                        {{else}}

                                            {{#if _selectTxt}}
                                                <a href="#"
                                                    data-participant-id="{{id}}"
                                                    class="participantSelectControl select-txt"
                                                >{{{_selectTxt}}}</a>
                                                <span class="deselTxt selected-txt">{{{_selectedTxt}}}</span>
                                            {{else}}
                                                <input name="partSelCheck{{id}}"
                                                    type="checkbox"
                                                    data-participant-id="{{id}}"
                                                    class="participantSelectControl"
                                                    {{#if isSelected}} checked="checked"{{/if}} />
                                            {{/if}}

                                        {{/if}}
                                    </td><!--
                                    --><td class="participantSearchNameCol">
                                        {{firstName}} {{lastName}}
                                    </td><!--
                                    --><td class="participantSearchOrgCol">
                                        {{! this is just the first node in the node array }}
                                        {{nodes.0.name}}
                                    </td><!--
                                    --><td class="participantSearchCountryCol">
                                        {{#if countryCode}}<img class="countryFlag" src="img/flags/{{countryCode}}.png" alt="{{countryCode}}" title="{{countryName}}" />{{/if}}
                                    </td><!--
                                    --><td class="participantSearchDepartmentCol">{{departmentName}}</td><!--
                                    --><td class="participantSearchJobCol">{{jobName}}</td><!--
                                --></tr>
                            </script><!-- /#superViewerSearchTableRowTpl -->

                            <div id="ssiSuperViewers">
                                <!-- ParticipantPaginatedView -->
                            </div><!-- /#ssiParticipants -->

                            <script id="ssiSuperViewersPaginatedViewTpl" type="text/x-handlebars-template">
                                <div class="spincover" style="display:none;"><div class="spin"></div></div>
                                <div class="paginationConts pagination pagination-right"></div>
                                <div class="emptyMsg alert" style="display:none"><cms:contentText key="NOT_ADDED_ANYONE" code="ssi_contest.pax.manager"/></div>
                                <div class="hasPax container-splitter with-splitter-styles participantCollectionViewWrapper" style="display:none">
                                    <h5>
                                        <span class="paxMode"><cms:contentText key="SELECTED_PARTICIPANTS" code="ssi_contest.pax.manager"/></span>
                                        <span class="manMode"><cms:contentText key="SELECTED_MANAGERS" code="ssi_contest.pax.manager"/></span>
                                        <span class="superViewerMode"><cms:contentText key="SELECTED_SUPERVIEWERS" code="ssi_contest.pax.manager"/></span>
                                    </h5>
                                    <table class="table table-condensed table-striped">
                                        <thead>
                                            <tr>
                                                <th class="participant sortHeader sortable" data-sort="lastName">
                                                    <a href="#">
                                                        <span class="paxMode">
                                                            <cms:contentText key="PARTICIPANT" code="ssi_contest.pax.manager" />
                                                            <i class="icon-arrow-1-up"></i>
                                                            <i class="icon-arrow-1-down"></i>
                                                        </span>
                                                        <span class="manMode">
                                                            <cms:contentText key="MANAGER" code="ssi_contest.pax.manager" />
                                                            <i class="icon-arrow-1-up"></i>
                                                            <i class="icon-arrow-1-down"></i>
                                                        </span>
                                                        <span class="superViewerMode">
                                                            <cms:contentText key="SUPERVIEWER" code="ssi_contest.pax.manager" />
                                                            <i class="icon-arrow-1-up"></i>
                                                            <i class="icon-arrow-1-down"></i>
                                                        </span>
                                                    </a>
                                                </th>
                                                <th class="remove"><cms:contentText key="REMOVE" code="ssi_contest.pax.manager" /></th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                        </tbody>
                                    </table>
                                </div>
                                <div class="paginationConts pagination pagination-right"></div>

                                <!--subTpl.paxRow=
                                    <tr class="participant-item" data-participant-id="{{id}}">
                                        <td class="participant">
                                            <a class="participant-popover" href="#" data-participant-ids="[{{userId}}]">
                                                {{lastName}}, {{firstName}}
                                                <img src="img/flags/{{countryCode}}.png" alt="{{countryCode}}" class="countryFlag" title="{{countryName}}" />
                                            </a>
                                            <span class="singleRecipNode">{{orgName}}</span>
                                            <div class="org">
                                                {{#if departmentName}}{{#if orgName}}&bull;{{/if}} {{departmentName}}{{/if}} {{#if jobName}}{{#if departmentName}}&bull;{{else}}{{#if orgName}}&bull;{{/if}}{{/if}} {{jobName}}{{/if}}
                                            <div>
                                        </td>
                                        <td class="remove">
                                            <a class="remParticipantControl" title=<cms:contentText key="REMOVE_THIS_PARTICIPANT" code="claims.submission" /> data-participant-id="{{id}}"><i class="icon-trash"></i></a>
                                        </td>
                                    </tr>
                                subTpl-->
                            </script><!-- /.ssiParticipantsPaginatedViewTpl -->

                        </div><!-- /.paxSelectWrap -->

						<hr class="section">

                    </div><!-- /.SuperViewersWrapper -->

					<div class="stepContentControls">
						<button class="btn btn-primary btn-inverse fr saveDraftBtn btn-fullmobile">
							<cms:contentText key="SAVE_AS_DRAFT"
								code="ssi_contest.generalInfo" />
						</button>
                        <button class="btn fr cancelAtnBtn btn-fullmobile" style="display:none;">
                            <cms:contentText key="CANCEL" code="ssi_contest.generalInfo" />
						</button>
						<span></span>
						<button class="btn backBtn btn-fullmobile">
							&laquo;
							<cms:contentText key="BACK" code="ssi_contest.generalInfo" />
						</button>
						<button class="btn btn-primary nextBtn btn-fullmobile">
							<cms:contentText key="NEXT" code="ssi_contest.generalInfo" />
							&raquo;
						</button>
					</div>
					<!-- /.stepContentControls -->
				</div>
				<!-- /.stepParticipantsManagersContent -->



<!--
PAYOUTS
 -->
				<div id="contestEditTabPayoutsView"
					class="stepPayoutsContent stepContent" style="display: none">

					<div class="contestSpecificView">
						<!-- dyn -->
					</div>

					<div class="stepContentControls">
						<button class="btn fr btn-primary btn-inverse saveDraftBtn btn-fullmobile">
							<cms:contentText key="SAVE_AS_DRAFT"
								code="ssi_contest.generalInfo" />
							</button>
	                        <button class="btn fr cancelAtnBtn btn-fullmobile" style="display:none;">
                            <cms:contentText key="CANCEL" code="ssi_contest.generalInfo" />
						</button>
						<span></span>
						<button class="btn backBtn btn-fullmobile">
							&laquo;
							<cms:contentText key="BACK" code="ssi_contest.generalInfo" />
						</button>
						<button class="btn btn-primary nextBtn btn-fullmobile">
							<cms:contentText key="NEXT" code="ssi_contest.generalInfo" />
							&raquo;
						</button>
					</div>
					<!-- /.stepContentControls -->

				</div>
				<!-- /.stepPayoutsContent -->

                <!--
				BILL TO

                -->
                <script id="billToTpl" type="text/x-handlebars-template">

                <div class="control-group {{#if billCodeRequired}} validateme {{/if}} billToWrapper"
                    {{#if _initVisible}}style="display:none"{{/if}}
                    data-validate-flags="nonempty"
                    data-validate-fail-msgs="{&quot;nonempty&quot; : &quot;<cms:contentText key="CHARGE_CONTEST_TO_REQ" code="ssi_contest.generalInfo" />&quot;}"
                    style="display:none">

                    <hr class="section">

                    <h5>
                        <cms:contentText key="CHARGE_CONTEST_TO" code="ssi_contest.generalInfo" />
                        <i class="icon-info pageView_help"
                            data-help-content="<cms:contentText key="CHARGE_CONTEST_TO_EXPLAIN" code="ssi_contest.generalInfo" />"></i>
                    </h5>
                    {{#iterSsi billCodes}}
                        <div class="billCodeSelectWrap">
                        <label><cms:contentText key="BILL_CODE" code="ssi_contest.generalInfo" />{{inc position}}</label>

                        <select name="ssiBillCodes[{{inc position}}]" id="ssiBillCodes[{{inc position}}]" class="ssiBillCodes" data-model-key="ssiBillCodes" {{#if index}} data-index="{{index}}" {{else}}  data-index="{{promoBillCodeOrder}}"  {{/if}} >
                            <option value="default" selected><cms:contentText key="SELECT_BILL_CODE" code="ssi_contest.generalInfo" /></option>
                           {{#../billCodes}}
                            <option value="{{billCode}}" {{#if customValue}}data-custom-value={{customValue}}{{/if}}>{{billCodeName}}</option>
                            {{/../billCodes}}
                        </select>
						<input type="text" class="custombillcode" name="customBillCode[{{inc index}}]" style="display:none;" value="{{customValue}}" data-index="{{index}}" data-position="{{position}}" />
                    </div>
                    {{/iterSsi}}
                    <div class="awardThemNowOnly billToDispOnly" style="display: none">
                        <span class="dispTxt"><!-- dyn --></span>
                    </div>

                </div><!-- /.billToTypesWrapper -->
                </script>

<!--
DATA
 -->
				<div id="contestEditTabDataCollectionView" class="stepDataCollectionContent stepContent" style="display: none">

                    <div class="ssiContestDataCollectionWrap"><!-- dynamic from ssiContestEditTabDataCollectionView.html --></div>

					<div class="stepContentControls
                     ">
						<button class="btn btn-primary btn-inverse fr saveDraftBtn btn-fullmobile">
							<cms:contentText key="SAVE_AS_DRAFT" code="ssi_contest.generalInfo" />
						</button>
						<span></span>
						<button class="btn backBtn btn-fullmobile">
							&laquo;
							<cms:contentText key="BACK" code="ssi_contest.generalInfo" />
						</button>
						<button class="btn btn-primary nextBtn btn-fullmobile">
							<cms:contentText key="NEXT" code="ssi_contest.generalInfo" />
							&raquo;
						</button>
					</div>
					<!-- /.stepContentControls -->

				</div>
				<!-- /.stepDataCollectionContent -->




<!--
PREVIEW
-->
				<div id="contestEditTabPreviewView"
					class="stepPreviewContent stepContent" style="display: none">

					<div class="ssiContestPreviewWrap">
						<!-- dynamic from ssiContestEditTabPreviewView.html -->
					</div>

                    <div class="showOnAwardThemNow" style="display:none">
                        <cms:contentText key="EMAIL_NOTIFICATION_MESSAGE" code="ssi_contest.generalInfo" />
                    </div>

					<div class="stepContentControls">
							<button class="btn fr cancelAtnBtn btn-fullmobile" style="display:none;">
                            <cms:contentText key="CANCEL" code="ssi_contest.generalInfo" />
                        </button>
						<button class="btn backBtn btn-fullmobile">
							&laquo;
							<cms:contentText key="BACK" code="ssi_contest.generalInfo" />
						</button>
						<button href="#" class="btn btn-primary submitBtn btn-fullmobile"> <cms:contentText
								key="CREATE_CONTEST" code="ssi_contest.creator" />
						</button>
                        <!-- for after the contest has been created (edit mode) -->
                        <a href="${notifyUrl}" class="btn btn-primary saveBtn btn-fullmobile" style="display:none;">
                            <cms:contentText key="SAVE" code="system.button" />
                        </a>
						<a href="#" class="btn btn-primary issueAwardsBtn btn-fullmobile" style="display:none;">
                            <cms:contentText key="ISSUE_AWARDS" code="ssi_contest.generalInfo" />
                        </a>
					</div>
					<!-- /.stepContentControls -->

				</div>
				<!-- /.stepPreviewContent -->




				<!-- VALIDATION MSGS - informational tooltip for validation -->
				<div class="errorTipWrapper" style="display: none">
					<div class="errorTip">

						<!-- display for validateme generic errors -->
						<div class="errorMsg msgGenericError">
							<i class="icon-warning-circle"></i>
							<cms:contentText key="ERRORS" code="ssi_contest.generalInfo" />
						</div>

						<!-- save draft - quiz name not set -->
						<div class="errorMsg msgSaveDraftNameIssue">
							<i class="icon-warning-circle"></i>
							<cms:contentText key="CONTEST_NAME_ERROR"
								code="ssi_contest.generalInfo" />
						</div>

						<!-- Info errors -->
						<div class="errorMsg msgContestNameNotUnique">
							<i class="icon-warning-circle"></i>
							<cms:contentText key="CONTEST_UNIQUE_NAME" code="ssi_contest.generalInfo" />
						</div>
                        <div class="errorMsg msgStartDateTooEarly">
                            <i class="icon-warning-circle"></i>
                            <cms:contentText key="START_DATE_TODAY_OR_LATER" code="ssi_contest.generalInfo" />
                        </div>
						<div class="errorMsg msgStartDateTooLate">
							<i class="icon-warning-circle"></i>
							<cms:contentText key="START_DATE_ERROR" code="ssi_contest.generalInfo" />
						</div>
						<div class="errorMsg msgEndDateTooEarly">
							<i class="icon-warning-circle"></i>
							<cms:contentText key="END_DATE_ERROR" code="ssi_contest.generalInfo" />
						</div>
                        <div class="errorMsg msgStartDateTooLate">
                            <i class="icon-warning-circle"></i>
                            <cms:contentText key="START_DATE_MUST_BE_TODAY" code="ssi_contest.generalInfo" />
                        </div>
						<div class="errorMsg msgTileStartDateTooLate">
                            <i class="icon-warning-circle"></i>
                            <cms:contentText key="TILE_START_DATE_ERROR" code="ssi_contest.generalInfo" />
                        </div>
						<div class="errorMsg msgTileStartDateTooEarly">
							<i class="icon-warning-circle"></i>
							<cms:contentText key="TILE_START_DATE_ERROR" code="ssi_contest.generalInfo" />
						</div>
						<div class="errorMsg msgTileEndDateTooEarly">
							<i class="icon-warning-circle"></i>
							<cms:contentText key="TILE_END_DATE_ERROR" code="ssi_contest.generalInfo" />
						</div>
						<div class="errorMsg msgDocMustHaveDispName">
							<i class="icon-warning-circle"></i>
							<cms:contentText key="DISPLAY_NAME_ERROR"
								code="ssi_contest.generalInfo" />
						</div>
						<div class="errorMsg msgUnsupportedFileType">
							<i class="icon-warning-circle"></i>
							<cms:contentText key="UNSUPPORTED_FILE_TYPE_ERROR"
								code="ssi_contest.generalInfo" />
						</div>
						<div class="errorMsg msgApproversReq">
							<i class="icon-warning-circle"></i>
							<cms:contentText key="APPROVERS_ERROR"
								code="ssi_contest.generalInfo" />
						</div>
						<div class="errorMsg msgBillToDataReq">
                            <i class="icon-warning-circle"></i>
							<cms:contentText key="CHARGE_CONTEST_TO_ERROR" code="ssi_contest.generalInfo" />
                        </div>
                        <div class="errorMsg msgBillToCodeReq">
                            <i class="icon-warning-circle"></i>
							<cms:contentText key="BILL_CODE_REQ_ERROR" code="ssi_contest.generalInfo" />
                        </div>
                        <div class="errorMsg msgContestGoalReq">
                            <i class="icon-warning-circle"></i>
                            <cms:contentText key="GOAL_REQ_ERROR" code="ssi_contest.generalInfo" />
                        </div>
                        <div class="errorMsg msgBadgeReq">
                            <i class="icon-warning-circle"></i>
                            <cms:contentText key="NO_BADGE_SELECTED_ERROR" code="ssi_contest.payout_objectives" />
                        </div>

						<!-- payouts - DTGT -->
                        <div class="errorMsg msgActivityTypeReq">
                            <i class="icon-warning-circle"></i>
                            <cms:contentText key="SELECT_AN_ACTIVITY_TYPE_ERR"
								code="ssi_contest.generalInfo" />
                        </div>
                        <div class="errorMsg msgPayoutTypeReq">
                            <i class="icon-warning-circle"></i>
                            <cms:contentText key="SELECT_PAYOUT_TYPE_ERR0R"
								code="ssi_contest.generalInfo" />
                        </div>
                        <div class="errorMsg msgDTGTActivityReq">
                            <i class="icon-warning-circle"></i>
                            <cms:contentText key="ADD_ATLEAST_ONE_ACTIVITY_ERR"
								code="ssi_contest.generalInfo" />
                        </div>
                        <div class="errorMsg msgDTGTActivityMissingValDesc">
                            <i class="icon-warning-circle"></i>
                            <cms:contentText key="EDIT_ACTIVITY_FOR_AWARD_ERR"
								code="ssi_contest.generalInfo" />
                        </div>

                        <!-- payouts - SIU -->
                        <div class="errorMsg msgSIUActivityDescReq">
                            <i class="icon-warning-circle"></i>
                            <cms:contentText key="ACT_DESC_REQ_ERROR" code="ssi_contest.generalInfo" />
                        </div>
                        <div class="errorMsg msgSIULevelReq">
                            <i class="icon-warning-circle"></i>
                            <cms:contentText key="ATLEAST_TWO_LEVELS" code="ssi_contest.payout_stepitup" />
                        </div>
                        <div class="errorMsg msgSIULevelMissingDesc">
                            <i class="icon-warning-circle"></i>
                            <cms:contentText key="PAYOUT_DESC_ERROR" code="ssi_contest.payout_stepitup" />
                        </div>
                        <div class="errorMsg msgSIULevelMissingAmount">
                            <i class="icon-warning-circle"></i>
                            <cms:contentText key="AMOUNT_ERROR" code="ssi_contest.payout_stepitup" />
                        </div>
                        <div class="errorMsg msgSIUBonusFieldsReq">
                            <i class="icon-warning-circle"></i>
                            <cms:contentText key="BONUS_ERROR" code="ssi_contest.payout_stepitup" />
                        </div>
                        <div class="errorMsg msgSIUBonusFieldsReq_dec_2">
                            <i class="icon-warning-circle"></i>
                            <cms:contentText key="NUMBER_WITH_TWO_DECIMAL" code="ssi_contest.payout_stepitup" />
                        </div>
                        <div class="errorMsg msgSIUBonusFieldsReq_dec_4">
                            <i class="icon-warning-circle"></i>
                            <cms:contentText key="NUMBER_WITH_FOUR_DECIMAL" code="ssi_contest.payout_stepitup" />
                        </div>
                        <div class="errorMsg msgSIUBonusFieldsReq_nat">
                            <i class="icon-warning-circle"></i>
                            <cms:contentText key="BONUS_DECIMAL_ERROR" code="ssi_contest.payout_stepitup" />
                        </div>
                        <div class="errorMsg msgSIUBonusFieldsTooLarge">
                            <i class="icon-warning-circle"></i>
                            <cms:contentText key="LIMIT_EXCEDEED" code="ssi_contest.payout_stepitup" />
                        </div>

                        <div class="errorMsg msgBonusCapTooSmall">
                            <i class="icon-warning-circle"></i>
                             <cms:contentText key="BONUS_GREATER_ERR" code="ssi_contest.payout_stepitup" />
                        </div>
                        <div class="errorMsg msgBonusCapMultiple">
                            <i class="icon-warning-circle"></i>
                             <cms:contentText key="BONUS_MULTIPLE_ERR" code="ssi_contest.payout_stepitup" />
                        </div>


                        <!-- payouts - SR -->
                        <div class="errorMsg msgSRMinQualReq">
                            <i class="icon-warning-circle"></i>
                            <cms:contentText key="MIN_QUALIFIER_ERROR" code="ssi_contest.payout_stackrank" />
                        </div>
                        <div class="errorMsg msgSRMinQualNumReq">
                            <i class="icon-warning-circle"></i>
                            <cms:contentText key="DECIMAL_ERROR" code="ssi_contest.payout_stackrank" />
                        </div>
                        <div class="errorMsg msgSROrderReq">
                            <i class="icon-warning-circle"></i>
                            <cms:contentText key="SORT_ORDER_REQ" code="ssi_contest.payout_stackrank" />
                        </div>
                        <div class="errorMsg msgSRRankReq">
                            <i class="icon-warning-circle"></i>
                            <cms:contentText key="RANK_REQ" code="ssi_contest.payout_stackrank" />
                        </div>
                        <div class="errorMsg msgSRRankDataReq">
                            <i class="icon-warning-circle"></i>
                            <cms:contentText key="ALL_INFO_REQ" code="ssi_contest.payout_stackrank" />
                        </div>
                        <div class="errorMsg msgSRRankPayoutAmountIssue">
                            <i class="icon-warning-circle"></i>
                            <cms:contentText key="NUMERIC_ONLY" code="ssi_contest.payout_stepitup" />
                        </div>

                        <!-- data collection -->
                        <div class="errorMsg msgDCApproverRequired">
                            <i class="icon-warning-circle"></i>
                            <cms:contentText key="CLAIM_APPROVAL_REQ" code="ssi_contest.payout_stepitup" />
                        </div>

					</div><!-- /.errorTip -->
				</div><!-- /.errorTipWrapper -->



				<!-- modal to catch errors from server on submit OR save draft -->
				<div class="modal hide fade contestErrorsModal">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">
							<i class="icon-close"></i>
						</button>
						<h3>
							<i class="icon-warning-circle"></i>
							<cms:contentText key="FOLLOWING_ERRORS"
								code="system.generalerror" />
						</h3>
					</div>
					<div class="modal-body">
						<ul class="errorsList">
							<!-- dynamic -->
						</ul>
					</div>
					<div class="modal-footer">
						<a href="#" class="btn" data-dismiss="modal"><cms:contentText
								key="CLOSE" code="ssi_contest.generalInfo" /></a>
					</div>
				</div>
				<!-- /.contestErrorsModal -->


			</div>
			<!-- /.wizardTabsContent -->



		</div>
		<!-- /.span12 -->
	</div>
	<!-- /.row-fluid -->

</div>
<!-- /#ssiContestPageEditView -->

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
    $(document).ready(function() {
        var contestJson = ${initializationJson},
            tabsJson = null,
            contestTypeJson = null;

     	// default json for WizardTabs
        tabsJson = [
            {
                "id" : 1,
                "name" : "stepInfo",
                "isActive" : false,
                "state" : "unlocked",
                "contentSel" : ".wizardTabsContent .stepInfoContent",
                "wtNumber" : "",
                "wtName" : "<cms:contentText key='TITLE' code='ssi_contest.generalInfo' />",
                "hideDeedle" : false
            },
            {
                "id" : 2,
                "name" : "stepParticipantsManagers",
                "isActive" : false,
                "state" : "locked",
                "contentSel" : ".wizardTabsContent .stepParticipantsManagersContent",
                "wtNumber" : "",
                "wtName" : "<cms:contentText key='TITLE' code='ssi_contest.pax.manager' />",
                "hideDeedle" : false
            },
            {
                "id" : 3,
                "name" : "stepPayouts",
                "isActive" : false,
                "state" : "locked",
                "contentSel" : ".wizardTabsContent .stepPayoutsContent",
                "wtNumber" : "",
                "wtName" : "<cms:contentText key='TITLE' code='ssi_contest.objectives' />",
                "hideDeedle" : false
            },
            {
                "id" : 4,
                "name" : "stepDataCollection",
                "isActive" : false,
                "state" : "locked",
                "contentSel" : ".wizardTabsContent .stepDataCollectionContent",
                "wtNumber" : "",
                "wtName" : "<cms:contentText key='TITLE' code='ssi_contest.datacollection' />",
                "hideDeedle" : false
            },
            {
                "id" : 5,
                "name" : "stepPreview",
                "isActive" : false,
                "state" : "locked",
                "contentSel" : ".wizardTabsContent .stepPreviewContent",
                "wtNumber" : "",
                "wtName" : "<cms:contentText key='TITLE' code='ssi_contest.preview' />",
                "hideDeedle" : false
            }
        ];

        // contest type specific settings
        contestTypeJson = {
            // to be used to change display names for specific contests/steps
			tabNamesByContestType: {
                "stackRank" : { "stepPayouts": "<cms:contentText key='RANKS_PAYOUTS' code='ssi_contest.approvals.detail' />" },
                "stepItUp" : { "stepPayouts": "<cms:contentText key='LEVELS_PAYOUT_DESC_PART1' code='ssi_contest.payout_stepitup' />" },
                "doThisGetThat" : { "stepPayouts": "<cms:contentText key='ACTIVITIES_AND_PAYOUTS' code='ssi_contest.approvals.summary' />" },
                "awardThemNow" : {
                    "stepParticipantsManagers" : "<cms:contentText key='PARTICIPANTS' code='ssi_contest.pax.manager' />",
                    "stepPayouts" : "<cms:contentText key='PAYOUTS' code='ssi_contest.approvals.summary' />"
                }
            },

            // used to decide which tabs active by contest type
            activeTabsByContestType: {
                "stepItUp" : contestJson.allowSpreadsheet ? [1,2,3,4,5] : [1,2,3,5],
                "stackRank" : contestJson.allowSpreadsheet ? [1,2,3,4,5] : [1,2,3,5],
                "doThisGetThat" : contestJson.allowSpreadsheet ? [1,2,3,4,5] : [1,2,3,5],
                "objectives" : contestJson.allowSpreadsheet ? [1,2,3,4,5] : [1,2,3,5],
                "awardThemNow" : [1,2,3,5]
            }
        };

     	<%
     	  Map parameterMap = new HashMap();
	      if ( request.getAttribute( "promotionId" ) != null )
	      {
	        parameterMap.put( "promotionId", request.getAttribute( "promotionId" ) );
	      }
	      if ( request.getAttribute( "contestId" ) != null )
	      {
	        parameterMap.put( "promotionId", request.getAttribute( "contestId" ) );
	      }
	      pageContext.setAttribute( "contestApproversUrl", ClientStateUtils.generateEncodedLink( "", "ssi/createGeneralInfo.do?method=populateContestApprovers", parameterMap ) );
	     %>

        // JAVA NOTE: set this to doc upload url
        G5.props.URL_JSON_CONTEST_DOCUMENT_UPLOAD = G5.props.URL_ROOT+'ssi/createGeneralInfo.do?method=uploadContestDocuments';

        // JAVA NOTE: set this to the URL to check if a contest name is unique/valid
        G5.props.URL_JSON_CONTEST_CHECK_NAME = G5.props.URL_ROOT+'ssi/createGeneralInfo.do?method=validateContestName';

        // JAVA NOTE: set this to url for getting approvers
        G5.props.URL_JSON_CONTEST_APPROVERS = G5.props.URL_ROOT+ '<%=pageContext.getAttribute( "contestApproversUrl" )%>';

        // JAVA NOTE: set this to url for getting bill to options
        G5.props.URL_JSON_CONTEST_BILL_TO_TYPES = G5.props.URL_ROOT+'ssi/createGeneralInfo.do?method=populateContestBillToTypes';



	    // JAVA NOTE: set these to the urls for adding and removing participants
        <c:choose>
        	<c:when test='${contestType!=null && contestType eq "awardThemNow"}'>
		        G5.props.URL_JSON_CONTEST_PARTICIPANT_ADD = G5.props.URL_ROOT+'ssi/manageContestParticipantsAwardThemNowInfo.do?method=addParticipants';
		        G5.props.URL_JSON_CONTEST_PARTICIPANT_REM = G5.props.URL_ROOT+'ssi/manageContestParticipantsAwardThemNowInfo.do?method=deleteParticipant';
		        G5.props.URL_JSON_CONTEST_PARTICIPANTS = G5.props.URL_ROOT+'ssi/manageContestParticipantsAwardThemNowInfo.do?method=loadParticipants';
	        </c:when>
	        <c:otherwise>
		        G5.props.URL_JSON_CONTEST_PARTICIPANT_ADD = G5.props.URL_ROOT+'ssi/manageContestParticipantsInfo.do?method=addParticipants';
		        G5.props.URL_JSON_CONTEST_PARTICIPANT_REM = G5.props.URL_ROOT+'ssi/manageContestParticipantsInfo.do?method=deleteParticipant';
		        // JAVA NOTE: set this to the url to get selected pax AND list of team filters for PAX SEARCH
		        G5.props.URL_JSON_CONTEST_PARTICIPANTS = G5.props.URL_ROOT+'ssi/displayContestParticipantsInfo.do?method=loadParticipants';
	        </c:otherwise>
	    </c:choose>

        // JAVA NOTE: set these to the urls for adding and removing managers
        G5.props.URL_JSON_CONTEST_MANAGER_ADD = G5.props.URL_ROOT+'ssi/manageContestManagersInfo.do?method=addManagers';
        G5.props.URL_JSON_CONTEST_MANAGER_REM = G5.props.URL_ROOT+'ssi/manageContestManagersInfo.do?method=removeManager';

        // JAVA NOTE: set these to the urls for adding and removing super viewers
        G5.props.URL_JSON_CONTEST_SUPERVIEWER_ADD = G5.props.URL_ROOT+'ssi/manageContestSuperViewersInfo.do?method=addSuperViewers';
        G5.props.URL_JSON_CONTEST_SUPERVIEWER_REM = G5.props.URL_ROOT+'ssi/manageContestSuperViewersInfo.do?method=removeSuperViewer';


     	// JAVA NOTE: this url provides basic information for objectives - payout step
        G5.props.URL_JSON_CONTEST_PAYOUT_DATA_OBJECTIVES = G5.props.URL_ROOT+'ssi/manageContestPayoutObjectives.do?method=populateContestPayoutsObjective';

     	// JAVA NOTE: this url provides basic information for award them now - payout step
        G5.props.URL_JSON_CONTEST_PAYOUT_DATA_ATN = G5.props.URL_ROOT+'ssi/manageContestPayoutAwardThemNow.do?method=populateContestPayoutAwardThemNow';

     	// JAVA NOTE: this url provides data for DTGT (basic info + activities collection) - payout step
        G5.props.URL_JSON_CONTEST_PAYOUT_DATA_DTGT = G5.props.URL_ROOT+'ssi/manageContestPayoutDoThisGetThat.do?method=populateContestPayoutsDoThisGetThat';

     	// JAVA NOTE: this url provides CRUD for Activities
        G5.props.URL_JSON_CONTEST_PAYOUT_DTGT_ACTIVITY = G5.props.URL_ROOT+'ssi/manageContestPayoutDoThisGetThat.do';

     	// JAVA NOTE: this url provides data for SIU (basic info + activities collection) - payout step
        G5.props.URL_JSON_CONTEST_PAYOUT_DATA_SIU = G5.props.URL_ROOT+'ssi/manageContestPayoutStepItUp.do?method=populateContestPayoutsStepItUp';

        // JAVA NOTE: this url provides CRUD for Levels
        G5.props.URL_JSON_CONTEST_PAYOUT_SIU_LEVEL = G5.props.URL_ROOT+'ssi/manageContestPayoutStepItUp.do';

        // JAVA NOTE: this url provides basic data for stack rank - payout step
        G5.props.URL_JSON_CONTEST_PAYOUT_DATA_SR = G5.props.URL_ROOT+'ssi/manageContestPayoutStackRank.do?method=populateContestPayoutsStackRank';

        // JAVA NOTE: set this to the url to get the list of team search filters
        G5.props.URL_JSON_CONTEST_TEAM_SEARCH_FILTERS = G5.props.URL_ROOT+'ssi/displayContestParticipantsInfo.do?method=fetchTeamSearchFilter';

        // JAVA NOTE: set this to the url to get managers for paxes
        G5.props.URL_JSON_CONTEST_MANAGERS_SEARCH = G5.props.URL_ROOT+'ssi/displayContestManagersInfo.do?method=loadManagers';

        // JAVA NOTE: set this to the url to get selected managers
        G5.props.URL_JSON_CONTEST_MANAGERS = G5.props.URL_ROOT+'ssi/displayContestManagersInfo.do?method=loadSelectedManagers';

        // JAVA NOTE: set this to the url to get selected super viewers
        G5.props.URL_JSON_CONTEST_SUPERVIEWERS = G5.props.URL_ROOT+'ssi/displayContestSuperViewersInfo.do?method=loadSelectedSuperViewers';

        // JAVA NOTE: this url provides calculation results from payouts step for objectives-type contests
        G5.props.URL_JSON_CONTEST_CALCULATE_PAYOUT_OBJECTIVES = G5.props.URL_ROOT+'ssi/manageContestPayoutObjectives.do?method=calculatePayoutObjectivesTotals';

        // JAVA NOTE: this url provides ssi contest preview
        G5.props.URL_JSON_SSI_CONTEST_TAB_PREVIEW = G5.props.URL_ROOT+'ssi/previewContest.do?method=display';

        // to follow unfollow pax from the creation contest steps
        G5.props.URL_JSON_PARTICIPANT_FOLLOW = G5.props.URL_ROOT+'publicRecognitionFollowUnFollowPax.do?method=followUnFollowPax';

        // JAVA NOTE: this url provides data for  data collection step
        G5.props.URL_JSON_SSI_CONTEST_TAB_DATA_COLLECTION = G5.props.URL_ROOT+'ssi/dataCollection.do?method=display';

        G5.props.URL_JSON_SSI_PAX_UPLOAD_DOCUMENT = G5.props.URL_ROOT+'ssi/uploadContestResults.do?method=uploadContestDetailsValid';

        //attach the view to an existing DOM element
        window.ssiContestPageEditView = new SSIContestPageEditView({
            el:$('#ssiContestPageEditView'),
            contestJson: contestJson, // contest json to populate model
            contestTypeJson: contestTypeJson, // tab variations etc per contestType
            pageNav : {
                back : {
                    text : '<cms:contentText key="BACK" code="system.button" />',
                    url : 'javascript:history.go(-1);'
                },
                home : {
                    text : '<cms:contentText key="HOME" code="system.general" />',
                    url : '<%=RequestUtils.getBaseURI( request )%>/homePage.do'
                }
            },
            pageTitle : '<cms:contentText key="PAGE_TITLE" code="ssi_contest.generalInfo" />', // JAVA NOTE: 'Create a Contest' OR 'Edit a Contest'
            tabsJson : tabsJson
        });

    });
</script>

<!-- wizardTab template -->
<script type="text/template" id="wizardTabTpl">
    <jsp:include page="/include/wizardTab.jsp"/>
</script>

<!-- paginationView template -->
<script type="text/template" id="paginationViewTpl">
    <jsp:include page="/include/paginationView.jsp"/>
</script>

<jsp:include page="/search/paxSearchStart.jsp"/>

<script type="text/template" id="participantSearchTableRowTpl">
  <jsp:include page="../profileutil/participantSearchTableRow.jsp"/>
</script>

<!-- contest payout objectives template -->
<script type="text/template" id="ssiContestEditTabPayoutsView_objectivesTpl">
  <jsp:include page="ssiContestPayoutsObjectives.jsp"/>
</script>

<script type="text/template" id="ssiContestEditTabPayoutsView_objectives_paxTableTpl">
 <jsp:include page="ssiContestPayoutsObjectivesPaxTable.jsp"/>
</script>
<!-- end contest payout objectives template -->

<!-- contest payout do this get that template -->
<script type="text/template" id="ssiContestEditTabPayoutsViewDTGTTpl">
  <jsp:include page="ssiContestEditTabPayoutsViewDTGT.jsp"/>
</script>

<script type="text/template" id="ssiDtgtActivityCollectionViewTpl">
  <jsp:include page="ssiDtgtActivityCollectionView.jsp"/>
</script>

<script type="text/template" id="ssiDtgtActivityModelViewTpl">
  <jsp:include page="ssiDtgtActivityModelView.jsp"/>
</script>
<!-- end contest payout do this get that template -->

<!-- contest payout step it up template -->
<script type="text/template" id="ssiContestEditTabPayoutsViewSIUTpl">
 <jsp:include page="ssiContestEditTabPayoutsViewSIU.jsp"/>
</script>

<script type="text/template" id="ssiContestEditTabPayoutsViewSIUPaxTableTpl">
 <jsp:include page="ssiContestEditTabPayoutsViewSIUPaxTable.jsp"/>
</script>

<script type="text/template" id="ssiSiuLevelModelViewTpl">
 <jsp:include page="ssiSiuLevelModelView.jsp"/>
</script>

<script type="text/template" id="ssiSiuLevelCollectionViewTpl">
 <jsp:include page="ssiSiuLevelCollectionView.jsp"/>
</script>
<!-- end contest payout step it up template -->

<!-- contest award them now template -->
<script type="text/template" id="ssiContestEditTabPayoutsViewATNpaxTableTpl">
 <jsp:include page="ssiContestEditTabPayoutsViewATNpaxTable.jsp"/>
</script>

<script type="text/template" id="ssiContestEditTabPayoutsViewATNTpl">
 <jsp:include page="ssiContestEditTabPayoutsViewATN.jsp"/>
</script>
<!-- end contest award them now template -->

<!-- contest stack rank template -->
<script type="text/template" id="ssiContestEditTabPayoutsViewSRTpl">
 <jsp:include page="ssiContestEditTabPayoutsViewSR.jsp"/>
</script>
<!-- end contest stack rank template -->

<!-- contest preview template -->
<script type="text/template" id="ssiContestEditTabPreviewViewTpl">
 <jsp:include page="ssiContestEditTabPreviewView.jsp"/>
</script>
<!-- end contest preview template -->

<script type="text/template" id="badgesSelectorViewTpl">
  <jsp:include page="ssiContestBadgesSelectorView.jsp"/>
</script>

<script type="text/template" id="ssiContestEditTabDataCollectionViewTpl">
  <jsp:include page="ssiContestEditTabDataCollectionView.jsp"/>
</script>

<jsp:include page="/submitrecognition/easy/flipSide.jsp"/>

<script type="text/template" id="ssiSubmitClaimFormTplTpl">
  <jsp:include page="ssiSubmitClaimFormTpl.jsp"/>
</script>
