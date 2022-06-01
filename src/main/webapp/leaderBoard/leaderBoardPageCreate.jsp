<%@page import="com.biperf.core.utils.UserManager"%>
<%@page import="com.biperf.core.utils.DateUtils"%>
<%@page import="java.util.Date"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.ui.leaderBoard.LeaderBoardForm"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ include file="/include/taglib.jspf"%>
<!-- ======== LEADERBOARD PAGE CREATE ======== -->

<%
Date date = new Date();
String todayDate=DateUtils.toDisplayString(date);
String backButtonUrl="";

if(UserManager.getUser().isParticipant())
{
  backButtonUrl="leaderBoardsDetailPage.do?method=getUserRole";
}
else
{
  backButtonUrl="leaderBoardsList.do";
}

%>
<!-- Page Body -->
<div class="page-content" id="leaderboardPageView">

<div class="row-fluid">

	<div class="span12">
        <div class="row-fluid">
            <div class="span12">
                <h2><cms:contentText  key="CREATE_LEADERBOARD" code="leaderboard.label" /></h2>
            </div>
        </div>
		<html:form styleId="contentForm"
			action="leaderBoardPreviewAction?method=processPreview"
			styleClass="form-horizontal" method="post">
			<html:hidden property="leaderBoardId" styleId="leaderBoardId" />
			<html:hidden property="clientState" />
			<html:hidden property="cryptoPassword" />
			<html:hidden property="source" />
			<html:hidden property="status" />
			<c:set var="currentDate" scope="request" value="<%=todayDate%>"/>
			<cms:errors></cms:errors>
			<!-- NOTE: this fieldset is used on the create and edit promo info form pages -->
			<fieldset id="leaderboardFieldsetPromoInfo">
				<div id="errorShowDiv"></div>
				<div class="control-group validateme" data-validate-flags="nonempty"
					data-validate-fail-msgs='{"nonempty" : "<cms:contentText key="LB_NAME_REQUIRED" code="leaderboard.errors" escapeHtml="true"/>"}'>
					<label class="control-label" for="name"><cms:contentText
							key="PROMOTION_NAME" code="leaderboard.label" /> </label>
					<div class="controls">
						<html:text property="leaderBoardName" maxlength="50"
							styleClass="content-field" styleId="leaderBoardName" />
					</div>
				</div>

				<!-- <div class="form-inline"> -->
					<div class="control-group validateme"
						data-validate-flags="nonempty"
						data-validate-fail-msgs='{"nonempty" : "<cms:contentText key="PROMOTION_START_DATE" code="leaderboard.errors" escapeHtml="true"/>"}'>
						<label class="control-label" for="startDate"><cms:contentText
								key="PROMOTION_START_DATE" code="leaderboard.label" /> </label>
						<div class="controls">

						 <c:if test="${leaderBoardForm.leaderBoardId==null or leaderBoardForm.status=='U'}">
							<span class="input-append datepickerTrigger"
								data-date-autoclose="true"
								data-date-format="<%=UserManager.getUserDatePattern().toLowerCase()%>"
                                data-date-language="<%=UserManager.getUserLocale()%>"
								data-date-todaydate="<%=todayDate%>">
								<input type="text"
									value="${leaderBoardForm.startDate}" id="startDate"
									readonly="readonly" name="startDate" class="date">
								<button class="btn" type="button">
									<i class="icon-calendar"></i>
								</button>
							</span>
						 </c:if>

						 <c:if test="${leaderBoardForm.leaderBoardId!=null and leaderBoardForm.status!='U'}">

						 	<c:if test="${leaderBoardForm.startDate>currentDate}">
								<span class="input-append datepickerTrigger"
									data-date-autoclose="true"
									data-date-format="<%=UserManager.getUserDatePattern().toLowerCase()%>"
                                    data-date-language="<%=UserManager.getUserLocale()%>"
									data-date-startdate="<%=todayDate%>"
									data-date-todaydate="<%=todayDate%>">
									<input type="text"
										value="${leaderBoardForm.startDate}" id="startDate"
										readonly="readonly" name="startDate" class="date">
									<button class="btn" type="button">
										<i class="icon-calendar"></i>
									</button>
								</span>
							</c:if>

							<c:if test="${leaderBoardForm.startDate<=currentDate}">
								<span class="input-append datepickerTrigger"
									data-date-autoclose="true"
									data-date-format="<%=UserManager.getUserDatePattern().toLowerCase()%>"
                                    data-date-language="<%=UserManager.getUserLocale()%>"
									data-date-startdate="<%=todayDate%>"
									data-date-todaydate="<%=todayDate%>">
									<input type="text"
										value="${leaderBoardForm.startDate}" id="startDate"
										readonly="readonly" name="startDate" class="date" >

									<button class="btn" type="button" disabled="disabled">
										<i class="icon-calendar"></i>
									</button>
								</span>
							</c:if>

						 </c:if>

						</div><!-- /.controls -->
					</div><!-- /.control-group -->

					<div class="control-group">
						<label class="control-label" for="endDate"><cms:contentText
								key="PROMOTION_END_DATE" code="leaderboard.label" /> </label>
						<div class="controls">
							<span class="input-append datepickerTrigger" data-date-autoclose="true" data-date-startdate="${leaderBoardForm.startDate}"
								  data-date-format="<%=UserManager.getUserDatePattern().toLowerCase()%>"
                                  data-date-language="<%=UserManager.getUserLocale()%>">
								<input type="text"
									value="${leaderBoardForm.endDate}" id="endDate" name="endDate"
									readonly="readonly" class="date">
								<button class="btn" type="button">
									<i class="icon-calendar"></i>
								</button>
							</span>
							<span class="optional"><cms:contentText key="OPTIONAL" code="system.common.labels" /></span>
						</div>
					</div>

				<!-- </div> -->

				<c:if test="${leaderBoardForm.leaderBoardId!=null}">
				<div class="control-group">
					<label class="control-label" for="displayEndDate"><cms:contentText
							key="DISPLAY_END_DATE" code="leaderboard.label" /> </label>
					<div class="controls">
						<span class="input-append datepickerTrigger" data-date-autoclose="true" data-date-startdate="${leaderBoardForm.startDate}"
							  data-date-format="<%=UserManager.getUserDatePattern().toLowerCase()%>"
                              data-date-language="<%=UserManager.getUserLocale()%>">
							<input type="text"
								value="${leaderBoardForm.leaderBoardDisplayEndDate}"
								id="displayEndDate" name="leaderBoardDisplayEndDate"
								readonly="readonly" class="date">
							<button class="btn" type="button">
								<i class="icon-calendar"></i>
							</button>
						</span>
                        <span class="optional"><cms:contentText key="OPTIONAL" code="system.common.labels" /></span>
					</div>
				</div>
				</c:if>
				<c:if test="${leaderBoardForm.leaderBoardId==null}">
				<div class="control-group">
					<label class="control-label" for="displayEndDate"><cms:contentText
							key="DISPLAY_END_DATE" code="leaderboard.label" /> </label>
					<div class="controls">
						<span class="input-append datepickerTrigger" data-date-autoclose="true" data-date-startdate="<%=todayDate%>"
						      data-date-format="<%=UserManager.getUserDatePattern().toLowerCase()%>"
                              data-date-language="<%=UserManager.getUserLocale()%>">
							<input type="text"
								value="${leaderBoardForm.leaderBoardDisplayEndDate}"
								id="displayEndDate" name="leaderBoardDisplayEndDate"
								readonly="readonly" class="date">
							<button class="btn" type="button">
								<i class="icon-calendar"></i>
							</button>
						</span>
						<span class="optional"><cms:contentText key="OPTIONAL" code="system.common.labels" /></span>
					</div>
				</div>
				</c:if>

				<div class="control-group validateme" data-validate-flags="nonempty"
					data-validate-fail-msgs='{"nonempty" : "<cms:contentText key="ACTIVITY_TITLE" code="leaderboard.errors" escapeHtml="true"/>"}'>
					<label class="control-label" for="activityTitle"><cms:contentText
							key="ACTIVITY_TITLE" code="leaderboard.label" /> <i class="icon-question" data-popover-content="<cms:contentText key="ACTIVITY_TITLE_TOOLTIP" code="leaderboard.label" />"></i> </label>
					<div class="controls">
						<html:text property="activityTitle" maxlength="100"
							styleId="activityTitle" />
					</div>
				</div>

				<div class="control-group">
					<label class="control-label" for="activitySortOrder"><cms:contentText
							key="SORT_ORDER" code="leaderboard.label" /> <i class="icon-question" data-popover-content="<cms:contentText key="SORT_ORDER_TOOLTIP" code="leaderboard.label" />"></i> </label>
					<div class="controls">
						<html:select property="sortOrder" size="1" styleId="sortOrderId">
							<html:option value="asce"><cms:contentText key="LOW_HIGH" code="leaderboard.label" /></html:option>
							<html:option value="desc"><cms:contentText key="HIGH_LOW" code="leaderboard.label" /></html:option>
						</html:select>
					</div>
				</div>


				<div class="control-group validateme" data-validate-flags="nonempty,maxlength"
				    data-validate-fail-msgs='{"nonempty" : "<cms:contentText key="PROMOTION_RULES" code="leaderboard.errors"  escapeHtml="true"/>", "maxlength" : "<cms:contentText key="RULES_TXT_MAX_CHARS" code="leaderboard.errors" escapeHtml="true"/>"}'
				    data-validate-max-length="2000">
					<label class="control-label" for="rules"><cms:contentText
							key="PROMOTION_RULES" code="leaderboard.label" /> </label>
					<div class="controls">
						<textarea name="leaderBoardRulesText" rows="5" data-max-chars="2000"
							id="leaderBoardRulesText" class="richtext" data-localization="<%=UserManager.getUserLanguage()%>">
							<c:out value="${leaderBoardForm.leaderBoardRulesText}" escapeXml="false"/>
							</textarea>
					</div>
				</div>

			</fieldset>
			<!-- /#leaderboardFieldsetPromoInfo -->


			<!-- NOTE: this fieldset is used on the create and edit activity form pages -->
			<fieldset id="leaderboardFieldsetActivity">
				<div class="control-group">
				 <label class="control-label" for="activityDate"><cms:contentText
							key="ACTIVITY_DATE" code="leaderboard.label" /> <i class="icon-question" data-popover-content="<cms:contentText key="ACTIVITY_DATE_TOOLTIP" code="leaderboard.label" />"></i> </label>

					<div class="controls">

						 <c:if test="${leaderBoardForm.leaderBoardId!=null}">
							<span class="input-append datepickerTrigger"
								data-date-autoclose="true"
								data-date-format="<%=UserManager.getUserDatePattern().toLowerCase()%>"
                                data-date-language="<%=UserManager.getUserLocale()%>"
								data-date-startdate="${leaderBoardForm.startDate}"
								data-date-enddate ="${leaderBoardForm.endDate}">
								<input type="text"
									value="${leaderBoardForm.activityDate}" id="activityDate"
									name="activityDate" class="date" readonly="readonly">
								<button class="btn" type="button">
									<i class="icon-calendar"></i>
								</button>
							</span>
						</c:if>

						<c:if test="${leaderBoardForm.leaderBoardId==null}">
							<span class="input-append datepickerTrigger"
								data-date-autoclose="true"
								data-date-format="<%=UserManager.getUserDatePattern().toLowerCase()%>"
                                data-date-language="<%=UserManager.getUserLocale()%>"
								data-date-startdate="<%=todayDate%>">
								<input type="text"
									value="${leaderBoardForm.activityDate}" id="activityDate"
									name="activityDate" class="date" readonly="readonly">
								<button class="btn" type="button">
									<i class="icon-calendar"></i>
								</button>
							</span>
						</c:if>

                        <span class="optional"><cms:contentText key="OPTIONAL" code="system.common.labels" /></span>

					</div><!-- /.controls -->
				</div><!-- /.control-group -->

				<h2>
					<cms:contentText key="ADD_ACTIVITY" code="leaderboard.label" />
				</h2>

				<p>
					<cms:contentText key="NOTES" code="leaderboard.label" />
				</p>

				<div class="paxSearchStartView" ></div><!-- /.paxSearchStartView -->
				<!-- Participant search view Element
				<div class="" id="participantSearchView" style="display:none"
					data-search-types='[{"id":"lastName","name":"<cms:contentText key="SEARCHBY_LAST_NAME" code="recognition.select.recipients"/>"},{"id":"firstName","name":"<cms:contentText key="SEARCHBY_FIRST_NAME" code="recognition.select.recipients"/>"},{"id":"location","name":"<cms:contentText key="SEARCHBY_LOCATION" code="recognition.select.recipients"/>"},{"id":"jobTitle","name":"<cms:contentText key="SEARCHBY_JOB_TITLE" code="recognition.select.recipients"/>"},{"id":"department","name":"<cms:contentText key="SEARCHBY_DEPARTMENT" code="recognition.select.recipients"/>"},{"id":"country","name":"<cms:contentText key="COUNTRY" code="recognition.select.recipients"/>"}]'
					data-search-params='{"extraKey":"extraValue","anotherKey":"some value"}'
					data-autocomp-delay="500" data-autocomp-min-chars="2"
					data-autocomp-url="leaderBoardSearchParticipant.do?method=doAutoComplete"
					data-search-url="leaderBoardSearchParticipant.do?method=generatePaxSearchView"
                    data-select-mode="multiple"
                    data-msg-select-txt='<cms:contentText key="ADD" code="system.button"/>'
                    data-msg-selected-txt="<i class='icon icon-check'></i>"
                    data-visibility-controls="showAndHide"
                    data-msg-show='<cms:contentText key="MORE_PAX" code="leaderboard.label" />'
                    data-msg-hide='<cms:contentText key="DONE_ADDING" code="profile.proxies.tab"/>'>
				</div>-->



				<div class="container-splitter with-splitter-styles participantCollectionViewWrapper">

					<!-- dynamic -->

					<script id="particpantLeaderboardRowTpl" type="text/x-handlebars-template">

                                            <tr class="participant-item"
                                                    data-participant-cid="{{cid}}"
                                                    data-participant-id="{{id}}">

                                                <td class="participant">
                                                    <html:hidden property="participants[{{autoIndex}}].id" value="{{id}}" />

                                                    <a class="participant-popover" href="#" data-participant-ids="[{{id}}]">
				                                        <span class="avatarwrap">
				                                            {{#if avatarUrl}}
				                                                <img src="{{#timeStamp avatarUrl}}{{/timeStamp}}"  />
				                                            {{else}}
				                                                <div class="avatar-initials">{{trimString firstName 0 1}}{{trimString lastName 0 1}}</div>
				                                            {{/if}}
				                                        </span>
				                                        {{firstName}}
				                                        {{lastName}}
				                                        {{#if countryCode}}<img src="img/flags/{{countryCode}}.png" alt="{{countryCode}}" class="countryFlag" title="{{countryName}}" />{{/if}}
				                                    </a>
				                                     <span class="org">{{orgName}} {{#if departmentName}}{{#if orgName}}&bull;{{/if}} {{departmentName}}{{/if}} {{#if jobName}}{{#if departmentName}}&bull;{{else}}{{#if orgName}}&bull;{{/if}}{{/if}} {{jobName}}{{/if}}</span>


                                                                                    <html:hidden property="participants[{{autoIndex}}].lastName" value="{{lastName}}" />
                                                                                    <html:hidden property="participants[{{autoIndex}}].firstName" value="{{firstName}}" />
                                                </td>

                                                <td class="score number">
                                                    <html:hidden property="participants[{{autoIndex}}].score" value="{{#if score}}{{score}}{{else}}0{{/if}}" />

                                                    {{#if score}}{{score}}{{else}}0{{/if}}
                                                </td>

                                                <td class="newScore number">
                                                    <html:text property="participants[{{autoIndex}}].newScore" value="{{#if newScore}}{{newScore}}{{else}}0{{/if}}" styleClass="number" />
                                                </td>

                                                <td class="remove">
                                                    <a class="remParticipantControl" title="<cms:contentText key='REMOVE_PAX' code='participant.search'/>"><i class="icon-trash"></i></a>
                                                </td>

                                            </tr><!-- /.participant-item -->
                                        </script>

                                        <h3><cms:contentText key="SELECTED_PAX" code="leaderboard.label" /></h3>

					<table class="table table-condensed table-striped">
						<thead>
							<tr>
								<th class="participant"><cms:contentText key="PAX" code="leaderboard.label" /></th>
								<th class="score"><c:out value="${activityAsof}" escapeXml="false"/></th>
								<th class="newScore"><cms:contentText key="TOTAL_ACTIVITY" code="leaderboard.label" /></th>
								<th class="remove"><cms:contentText key="REMOVE" code="leaderboard.label" /></th>
							</tr>
						</thead>

						<tbody id="participantsView" class="participantCollectionView"
							data-msg-empty="<cms:contentText key="NOT_ADDED" code="leaderboard.label" />"
							data-hide-on-empty="false">
						</tbody>
					</table>
					<!--
                        used to keep track of the number of participants, req. a 'participantCount' class
                        name is flexible
                     -->

					<html:hidden property="paxCount" value="${leaderBoardForm.paxCount}" styleClass="participantCount" />
				</div>
				<!-- /.container-splitter.with-splitter-styles.participantCollectionViewWrapper -->

			</fieldset>
			<!-- /#leaderboardFieldsetActivity -->


			<!-- NOTE: this fieldset is used on all leaderboard form pages -->
			<fieldset id="leaderboardFieldsetNotify">

				<html:hidden styleId="notifyPaxChecked" property="notifyPaxChecked" value="${leaderBoardForm.notifyPaxChecked}"/>
				<div class="control-group">
                    <label class="control-label checkbox"><html:checkbox
                        styleId="notifyParticipants" property="notifyParticipants"/> <cms:contentText
                            key="NOTIFY_PAX" code="leaderboard.label" />
                    </label>
					<div class="controls hide validateme" data-validate-flags="maxlength" data-validate-fail-msgs='{"maxlength" : "<cms:contentText key="RULES_TXT_MAX_CHARS" code="leaderboard.errors" escapeHtml="true"/>"}'
				data-validate-max-length="4000">
                        <textarea name="notifyMessage" class="richtext" data-max-chars="4000"
                            rows="5" id="notifyMessage">
                            <c:out value="${leaderBoardForm.notifyMessage}" escapeXml="false"/>
                        </textarea>
                    </div>
				</div>

			</fieldset>
			<!-- /#leaderboardFieldsetNotify -->


			<!-- NOTE:  -->
			<fieldset id="leaderboardFieldsetActions" class="form-actions">
			<beacon:authorize ifNotGranted="LOGIN_AS">
				<button type="submit" id="leaderboardButtonPreviewLeaderboard"
					name="button" value="leaderboardButtonPreview"
					formaction="<%=RequestUtils.getBaseURI(request)%>/leaderBoardSaveAction.do?method=previewLeaderBoard"
					class="btn btn-primary">
					<cms:contentText key="PREVIEW_LB" code="leaderboard.label" />
				</button>
			</beacon:authorize>
				<c:if test="${status != 'C'}">
					<button type="submit" id="leaderboardButtonSaveDraft" name="button"
						value="leaderboardButtonSaveDraft"
						formaction="<%=RequestUtils.getBaseURI(request)%>/leaderBoardSaveAction.do?method=saveDraft"
						class="btn btn-primary btn-inverse">
						<cms:contentText key="SAVE_DRAFT" code="leaderboard.label" />
					</button>
				</c:if>
				<button type="submit" id="leaderboardButtonCancel" name="button"
					value="leaderboardButtonCancel"
					formaction="<%=RequestUtils.getBaseURI(request)%>/leaderBoardCancelAction.do?method=prepareCancel"
					class="btn">
					<cms:contentText key="CANCEL" code="leaderboard.label" />
				</button>
				<div class="leaderboardCancelDialog" style="display:none">
                    <p>
                        <b><cms:contentText key="ARE_YOU_SURE" code="system.general" /></b>
                    </p>
                    <p>
                        <cms:contentText key="CHANGES_DISCARDED" code="system.general" />
                    </p>
                    <p class="tc">
                        <button id="leaderboardCancelDialogConfirm" class="btn btn-primary"><cms:contentText key="YES" code="system.button" /></button>
                        <button id="leaderboardCancelDialogCancel" class="btn"><cms:contentText key="NO" code="system.button" /></button>
                    </p>
                </div><!-- /.promoChangeConfirmDialog -->
			</fieldset>
			<!-- /#leaderboardFieldsetActions -->
			<!-- /#leaderboardFormCreate -->
		</html:form>
	</div>
	<!-- /.span12 -->

</div>
<!-- /.row-fluid -->
</div>

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
    $(document).ready(function() {

        // declare our variables for later use
        var prefilledLeaders, lbpcec;

        //Mini Profile PopUp JSON
        G5.props.URL_JSON_PARTICIPANT_INFO = 'participantPublicProfile.do?method=populatePax';

        //Mini Profile PopUp Follow Unfollow Pax JSON
        G5.props.URL_JSON_PARTICIPANT_FOLLOW = G5.props.URL_ROOT+'publicRecognitionFollowUnFollowPax.do?method=followUnFollowPax';

        // if there are leaders that need to be prefilled into the leaderboard (either the creator's team members for a new board or the existing leaders on an edited board), build the object here.
        prefilledLeaders = [<c:forEach var="bean" items="${beans}" varStatus="bt">
            {
                "id":"<c:out value="${bean.id}"/>",
                "lastName":"<c:out value="${bean.lastName}" escapeXml="false"/>",
                "firstName":"<c:out value="${bean.firstName}" escapeXml="false"/>",
                "score":"<c:out value="${bean.score}"/>",
                "newScore":"<c:out value="${bean.newScore}"/>"
            }
            <c:if test="${(bt.index+1) < fn:length(beans)}">, </c:if>
   		 </c:forEach>];

        //attach the view to an existing DOM element
        lbpcec = new LeaderboardPageCreateEditCopyView({
            el : $('#leaderboardPageView'),
            mode : 'create',
            leaders : prefilledLeaders,
            noSidebar : ${isDelegate},
            noGlobalNav : ${isDelegate},
            pageNav : {
                back : {
                    text : '<cms:contentText key="BACK" code="system.button" />',
                    url : '<%=backButtonUrl%>'
                },
                home : {
                    text : '<cms:contentText key="HOME" code="system.general" />',
                    url : '<%= RequestUtils.getBaseURI(request)%>/homePage.do'
                }
            },
            pageTitle : '${pageName}'
        });

    });
</script>

	<%@include file="/search/paxSearchStart.jsp" %>

    <script type="text/template" id="participantSearchViewTpl">
		<%@include file="/profileutil/participantSearchView.jsp"%>
	</script>

    <script type="text/template" id="participantSearchTableRowTpl">
		<%@include file="/profileutil/participantSearchTableRow.jsp"%>
	</script>

		<%@include file="/submitrecognition/easy/flipSide.jsp"%>
