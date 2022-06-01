<%@ include file="/include/taglib.jspf"%>
<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.diycommunication.DIYCommunicationsTipsForm" %>
<%@page import="com.biperf.core.utils.UserManager"%>

<!-- ======== COMMUNICATIONS TIPS EDIT/CREATE PAGE ======== -->
<div id="communicationsTipsEditView" class="page-content">
    <div class="row-fluid">
        <div class="span12">
            <h2><cms:contentText key="CREATE_TIPS" code="diyCommunications.common.labels" /></h2>

            <form class="sendForm pullBottomUp" method="post" action="manageTipsSave.do">

            	<!--JAVA NOTE: Display error text in the below list when page submit returns an error and set the hidden input value to true. -->
            	<div id="communicationsErrorBlock" class="alert alert-block alert-error" style="display:none">
                    <h4><cms:contentText key="FOLLOWING_ERRORS" code="diyCommunications.errors" /></h4>

                    <html:messages id="actionMessage" >
                    <ul>
                    	<c:set var="serverReturnedErrorForTips" value="true"/>
                        <li>${actionMessage}</li>
                    </ul>
                    </html:messages>

                    <input type="hidden" id="serverReturnedErrored" value="${serverReturnedErrorForTips}">
                </div>

                <input type="hidden" name="method" id="sendFormMethod" value="saveTipsDetails"/>

                <fieldset class="formSection tipsSection contentInfo">
                    <h3><cms:contentText key="PAGE_TITLE" code="diyCommunications.tips.labels" /></h3>

                    <div class="control-group validateme"
                        data-validate-fail-msgs='{"nonempty":"<cms:contentText key="TITLE_REQUIRED" code="diyCommunications.errors" />"}'
                        data-validate-flags='nonempty'>
                        <label class="control-label" for="tipTitle"><cms:contentText key="TIP_TITLE" code="diyCommunications.tips.labels" /></label>
                        <div class="controls">
                            <input type="text" name="tipTitle" id="tipTitle" class="contentTitle" maxlength="100"/>
                        </div>
                    </div>

                    <div class="control-group validateme"
                        data-validate-fail-msgs='{"nonempty":"<cms:contentText key="START_DATE_REQUIRED" code="diyCommunications.errors" />"}'
                        data-validate-flags='nonempty' >
                        <label for="tipStartDate" class="control-label"><cms:contentText key="START_DATE" code="diyCommunications.common.labels" /></label>
                        <div class="controls">
                            <!--
                                NOTE: JSP set data-date-format AND data-date-startdate
                            -->
                          <c:choose>
                            <c:when test="${disableDate}">
                               <span class="input-append datepickerTrigger"
		                                data-date-format="<%=UserManager.getUserDatePattern().toLowerCase()%>"
		                                data-date-language="<%=UserManager.getUserLocale()%>"
		                                data-date-autoclose="true">
		                                <input type="text" class="date datepickerInp contentStartDate"
		                                    name="tipStartDate"
		                                    id="tipStartDate"
		                                    readonly="readonly"><button type="button" class="btn datepickerBtn"><i class="icon-calendar"></i></button>
                            	</span>
                            </c:when>
                            <c:otherwise>
                                 <span class="input-append datepickerTrigger"
		                                data-date-format="<%=UserManager.getUserDatePattern().toLowerCase()%>"
		                                data-date-language="<%=UserManager.getUserLocale()%>"
		                                data-date-autoclose="true">
		                                <input type="text" class="date datepickerInp contentStartDate"
		                                    name="tipStartDate"
		                                    id="tipStartDate"
		                                    readonly="readonly"><button type="button" class="btn datepickerBtn"><i class="icon-calendar"></i></button>
                            	</span>
                            </c:otherwise>
                          </c:choose>
                        </div>
                    </div>

                    <div class="control-group validateme"
                        data-validate-fail-msgs='{"nonempty":"<cms:contentText key="END_DATE_REQUIRED" code="diyCommunications.errors" />"}'
                        data-validate-flags='nonempty' >
                        <label for="tipEndDate" class="control-label"><cms:contentText key="END_DATE" code="diyCommunications.common.labels" /></label>
                        <div class="controls">
                            <!--
                                NOTE: JSP set data-date-format AND data-date-endDate
                            -->
                            <span class="input-append datepickerTrigger"
                                data-date-format="<%=UserManager.getUserDatePattern().toLowerCase()%>"
                                data-date-language="<%=UserManager.getUserLocale()%>"
                                data-date-autoclose="true">
                                <input type="text" class="date datepickerInp contentEndDate"
                                    name="tipEndDate"
                                    id="tipEndDate"
                                    readonly="readonly"><button type="button" class="btn datepickerBtn"><i class="icon-calendar"></i></button>
                            </span>
                        </div>
                    </div>
                </fieldset>

                <fieldset class="formSection tipSelectAudience">
                    <h3><cms:contentText key="SELECT_AUDIENCE" code="diyCommunications.common.labels" /></h3>

                    <div class="selectAudienceTableWrapper">
                        <!-- Dynamic from selectAudienceParticipants in base-->
                    </div>
                </fieldset>

                <fieldset class="formSection tipAddContentSection">
                    <h3><cms:contentText key="ADD_TIP" code="diyCommunications.tips.labels" /></h3>
					<p><cms:contentText key="ADD_TIP_DESC" code="diyCommunications.tips.labels" /></p>

                    <div class="tipsAddedContent addedContent">
                        <table class="table table-striped contentTable" id="tipsContentTable">
                            <thead>
                                <tr>
                                    <th class="defaultHeader"><cms:contentText key="DEFAULT" code="diyCommunications.common.labels" /></th>
                                    <th><cms:contentText key="LANGUAGE" code="diyCommunications.common.labels" /></th>
                                    <th><cms:contentText key="PAGE_TITLE" code="diyCommunications.tips.labels" /></th>
                                    <th class="editColumn"><cms:contentText key="EDIT" code="diyCommunications.common.labels" /></th>
                                    <th class="remove"><cms:contentText key="REMOVE" code="diyCommunications.common.labels" /></th>
                                </tr>
                            </thead>
                            <tbody data-msg-empty="<cms:contentText key="EMPTY_TIP" code="diyCommunications.errors" />">
                            </tbody>
                        </table>
                    </div>

                    <script id="tipsContentTableTpl" type="text/x-handlebars-template">
                        <tr data-tipId="{{id}}" data-indexId="{{index}}" {{#if isNew}} class="newTip" {{/if}}>
                            <td class="defaultColumn">
                                <div class="control-group validateme"
                                    data-validate-fail-msgs="<cms:contentText key="DEFAULT_TIP_REQUIRED" code="diyCommunications.errors" />"
                                    data-validate-flags='nonempty'>
                                    <div class="controls">
                                        <input type="radio" name="defaultLanguage" value="{{language}}" {{#if isDefaultLang}}checked{{/if}} {{#if isSystemLanguage}}class="systemDefault"{{/if}}/>
                                    </div>
                                </div>

                            </td>
                            <td class="languageColumn" data-language="{{language}}">
                                <span data-language="{{language}}">{{languageDisplay}}</span>

                                <input type="hidden" name="tipContent[{{index}}].id" value="{{id}}"/>
                                <input type="hidden" name="tipContent[{{index}}].language" value="{{language}}"/>
 								<input type="hidden" name="tipContent[{{index}}].languageDisplay" value="{{languageDisplay}}"/>
                                <input type="hidden" name="tipContent[{{index}}].content" value="{{content}}"/>
                            </td>
                            <td class="contentColumn">{{content}}</td>
                            <td class="editColumn">
                                <a href="#">
                                    <i class="icon-pencil2"></i>
                                </a>
                            </td>
                            <td class="remove">
                                <a href="#" class="remParticipantControl {{#if systemLocale}}disabled{{/if}}" title="<cms:contentText key="REMOVE_TIPS" code="diyCommunications.common.labels" />" {{#if systemLocale}}disabled{{/if}}><i class="icon-trash"></i></a>
                            </td>
                        </tr>
                    </script>

                    <div class="tipRemoveConfirmDialog contentRemoveConfirmDialog" style="display:none">
                        <p>
                            <b><cms:contentText key="REMOVE_POP_UP_TXT" code="diyCommunications.common.labels" /></b>
                        </p>
                        <p class="tc">
                            <button type="button" id="tipRemoveDialogConfirm" class="btn btn-primary contentRemoveDialogConfirm"><cms:contentText key="YES" code="diyCommunications.common.labels" /></button>
                            <button type="button" id="tipRemoveDialogCancel" class="btn contentRemoveDialogCancel"><cms:contentText key="NO" code="diyCommunications.common.labels" /></button>
                        </p>
                    </div>

                    <div class="tipAddNewContent addNewContentContainer container-splitter with-splitter-styles">
                        <input type="hidden" name="tipId" id="tipId" class="contentId" />
                        <input type="hidden" id="userDefaultLanguage" name="userDefaultLanguage" value="${diyCommunicationsTipsForm.chooseLanguage}" />
                        <div class="control-group validateme"
                            data-validate-fail-msgs='{"nonempty":"<cms:contentText key="LANGUAGE_REQUIRED" code="diyCommunications.errors" />"}'
                            data-validate-flags='nonempty'>
                            <label class="control-label" for="chooseLanguage">
                                <cms:contentText key="AUDIENCE_LANGUAGE" code="diyCommunications.common.labels" />
                            </label>
                            <div class="controls">
                                <select id="chooseLanguage" name="chooseLanguage" >
					               <c:forEach var="language" items="${languageList}">
					          		  <option value="<c:out value="${language.code}"/>" <c:if test="${language.code == diyCommunicationsTipsForm.chooseLanguage}">selected="selected"</c:if>><c:out value="${language.name}"/></option>
					       		   </c:forEach>
              					</select>
                            </div>
                        </div>

                        <div class="contribCommentWrapper">
                            <div class="mask" style="display:none"></div>
                            <div class="control-group validateme"
                                data-validate-fail-msgs='{"nonempty":"<cms:contentText key="TIP_REQUIRED" code="diyCommunications.errors" />"}'
                                data-validate-flags='nonempty'>
                                <label class="control-label" for="chooseLanguage">
                                    <cms:contentText key="TIP_CONTENT" code="diyCommunications.tips.labels" />
                                </label>
                                <div class="commentTools">
                                    <cms:contentText key="REMAINING_CHARS" code="diyCommunications.tips.labels" /> <span class="remChars">&nbsp;</span>
                                    <span class="spellchecker dropdown">
                                        <button class="checkSpellingDdBtn btn btn-mini btn-icon btn-primary btn-inverse dropdown-toggle"
                                                title=<cms:contentText key="CHECK_SPELLING" code="diyCommunications.tips.labels" />
                                                type="button"
                                                data-toggle="dropdown">
                                            <i class="icon-check"></i>
                                        </button>
                                        <ul class="dropdown-menu">
                                            <li><a class="check"><b><cms:contentText key="SPELL_CHECK" code="diyCommunications.tips.labels" /></b></a></li>
                                        </ul>
                                    </span>
                                </div>
                                <div class="controls">
                                    <textarea class="contribCommentInp" rows="4" maxlength="275" placeholder="<cms:contentText key="ADD_COMMENT" code="diyCommunications.tips.labels" />"></textarea>
                                </div>
                            </div>

                            <div class="alert alert-danger contribCommentBadWords" style="display:none">
                                <button type="button" class="close"><i class="icon-close"></i></button>
                                <span>&nbsp;</span>
                            </div>
                        </div>

                        <button type="button" class="btn btn-primary saveContent"><cms:contentText key="SAVE_TIP" code="diyCommunications.tips.labels" /></button>
                        <button type="button" class="btn cancelContent"><cms:contentText key="CANCEL" code="diyCommunications.common.labels" /></button>
                    </div>

                    <div class="addLanguageContent">
                        <a href="#" class="disabled"><cms:contentText key="ADD_TIP" code="diyCommunications.common.labels" /></a>
                    </div>
                </fieldset>

                <fieldset class="formSection form-actions pullBottomUp">
                    <button class="tipSubmit contentSubmit btn btn-primary" type="submit" disabled>
                        <cms:contentText key="SUBMIT" code="diyCommunications.common.labels" />
                    </button>
                    <button class="tipCancel contentCancel btn" data-url="${pageContext.request.contextPath}/participant/manageTips.do">
                        <cms:contentText key="CANCEL" code="diyCommunications.common.labels" />
                    </button>
                </fieldset>

                <div class="saveCancelConfirm" style="display:none">
                    <p>
                        <b><cms:contentText key="CANCEL_CONFIRMATION" code="diyCommunications.common.labels" /></b>
                    </p>
                    <p class="tc">
                        <button type="submit" id="saveCancelDialogConfirm" class="btn btn-primary"><cms:contentText key="YES" code="diyCommunications.common.labels" /></button>
                        <button type="submit" id="saveCancelDialogCancel" class="btn"><cms:contentText key="NO" code="diyCommunications.common.labels" /></button>
                    </p>
                </div>

                <div class="tipsCancelConfirm contentCancelConfirm" style="display:none">
                    <p>
                        <b><cms:contentText key="CANCEL_CONFIRMATION" code="diyCommunications.common.labels" /></b>
                    </p>
                    <p class="tc">
                        <button type="submit" id="tipsCancelDialogConfirm" class="btn btn-primary contentCancelDialogConfirm"><cms:contentText key="YES" code="diyCommunications.common.labels" /></button>
                        <button type="submit" id="tipsCancelDialogCancel" class="btn contentCancelDialogCancel"><cms:contentText key="NO" code="diyCommunications.common.labels" /></button>
                    </p>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
$(document).ready(function() {

	<%
		DIYCommunicationsTipsForm form = (DIYCommunicationsTipsForm) request.getAttribute("diyCommunicationsTipsForm");
		Long tempTipsId = (Long)request.getAttribute("tipsId");
		Map clientStateParameterMap = ClientStateUtils.getClientStateMap(request);
		clientStateParameterMap.put( "resourceId", tempTipsId );

		pageContext.setAttribute("encodedEditUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), "/participant/communicationsTipsData.do?method=populateTipsDataEdit", clientStateParameterMap ) );
		pageContext.setAttribute("encodedAudienceUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), "/participant/communicationsTipsAudienceList.do?method=populatePublicAudience", clientStateParameterMap ) );
	%>

	G5.props.URL_JSON_COMMUNICATION_TIPS_DATA = "${encodedEditUrl}";
	G5.props.URL_JSON_SELECT_AUDIENCE_DATA = "${encodedAudienceUrl}";

    //attach the view to an existing DOM element
    var ctev = new CommunicationsEditView({
        el:$('#communicationsTipsEditView'),
        pageNav : {
            back : {
                text : '<cms:contentText key="BACK" code="system.button" />',
                url : 'javascript:history.go(-1);'
            },
            home : {
                text : '<cms:contentText key="HOME" code="system.general" />',
                url : '<%= RequestUtils.getBaseURI(request)%>/homePage.do'
            }
        },
        mode: 'tips',
        pageTitle : '<cms:contentText key="CREATE_TIPS" code="diyCommunications.common.labels" />'
    });

});
</script>

<script type="text/template" id="selectAudienceParticipantsViewTpl">
		<%@include file="/diypaxcommunications/selectAudienceParticipantsView.jsp"%>
</script>
