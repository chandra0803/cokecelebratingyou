<%@ include file="/include/taglib.jspf"%>
<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.diycommunication.DIYCommunicationsResourceCenterForm" %>
<%@page import="com.biperf.core.utils.UserManager"%>

<!-- ======== COMMUNICATIONS RESOURCE CENTER EDIT/CREATE PAGE ======== -->
<div id="communicationsResourceCenterEditView" class="page-content">
    <div class="row-fluid">
        <div class="span12">
            <h2><cms:contentText key="CREATE_RESOURCE_CENTER_CONTENT" code="diyCommunications.common.labels" /></h2>

            <form class="sendForm pullBottomUp" method="post" action="manageResourceCenterSave.do">

            	<!--JAVA NOTE: Display error text in the below list when page submit returns an error and set the hidden input value to true. -->
            	<div id="communicationsErrorBlock" class="alert alert-block alert-error" style="display:none">
                    <h4><cms:contentText key="FOLLOWING_ERRORS" code="diyCommunications.errors" /></h4>

                    <html:messages id="actionMessage" >
                    <ul>
                        <c:set var="serverReturnedErrorForResourceCenter" value="true"/>
                        <li>${actionMessage}</li>
                    </ul>
                    </html:messages>

                    <input type="hidden" id="serverReturnedErrored" value="${serverReturnedErrorForResourceCenter}">
                </div>

                <input type="hidden" name="method" id="sendFormMethod" value="saveResourceCenterDetails" />

                <fieldset class="formSection resourceCenterInfo contentInfo">
                    <h3><cms:contentText key="PAGE_HEADING" code="diyCommunications.resource.labels" /></h3>

                    <div class="control-group validateme"
                        data-validate-fail-msgs='{"nonempty":"<cms:contentText key="TITLE_REQUIRED" code="diyCommunications.errors" />"}'
                        data-validate-flags='nonempty'>
                        <label class="control-label" for="resourceTitle"><cms:contentText key="CONTENT_TITLE" code="diyCommunications.resource.labels" /></label>
                        <div class="controls">
                            <input type="text" name="resourceTitle" id="resourceTitle" class="contentTitle" maxlength="100"/>
                        </div>
                    </div>

                    <div class="control-group validateme"
                        data-validate-fail-msgs='{"nonempty":"<cms:contentText key="START_DATE_REQUIRED" code="diyCommunications.errors" />"}'
                        data-validate-flags='nonempty' >
                        <label for="resourceStartDate" class="control-label"><cms:contentText key="START_DATE" code="diyCommunications.common.labels" /></label>
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
		                               name="resourceStartDate"
		                               id="resourceStartDate"
		                               value=""
		                               readonly="readonly" ><button type="button" class="btn datepickerBtn"><i class="icon-calendar"></i></button>

                               </span>
                            </c:when>
                            <c:otherwise>
                                <span class="input-append datepickerTrigger"
			                          data-date-format="<%=UserManager.getUserDatePattern().toLowerCase()%>"
			                          data-date-language="<%=UserManager.getUserLocale()%>"
			                          data-date-autoclose="true">

                                <input type="text" class="date datepickerInp contentStartDate"
		                               name="resourceStartDate"
		                               id="resourceStartDate"
		                               value=""
		                               readonly="readonly" ><button type="button" class="btn datepickerBtn"><i class="icon-calendar"></i></button>

                               </span>
                            </c:otherwise>
                          </c:choose>
                        </div>
                    </div>

                    <div class="control-group validateme"
                        data-validate-fail-msgs='{"nonempty":"<cms:contentText key="END_DATE_REQUIRED" code="diyCommunications.errors" />"}'
                        data-validate-flags='nonempty' >
                        <label for="resourceEndDate" class="control-label"><cms:contentText key="END_DATE" code="diyCommunications.common.labels" /></label>
                        <div class="controls">
                            <!--
                                NOTE: JSP set data-date-format AND data-date-endDate
                            -->
                            <span class="input-append datepickerTrigger"
                                data-date-format="<%=UserManager.getUserDatePattern().toLowerCase()%>"
                                data-date-language="<%=UserManager.getUserLocale()%>"
                                data-date-autoclose="true">

                                <input type="text" class="date datepickerInp contentEndDate"
                                    name="resourceEndDate"
                                    id="resourceEndDate"
                                    value=""
                                    readonly="readonly"><button type="button" class="btn datepickerBtn"><i class="icon-calendar"></i></button>
                            </span>
                        </div>
                    </div>
                </fieldset>

                <fieldset class="formSection resourceCenterSelectAudience">
                    <h3><cms:contentText key="SELECT_AUDIENCE" code="diyCommunications.common.labels" /></h3>
                    <p><cms:contentText key="AUDIENCE_VISIBILITY" code="diyCommunications.resource.labels" /></p>

                    <div class="selectAudienceTableWrapper"></div>

                </fieldset>

                <fieldset class="formSection resourceCenterAddContentSection">
                    <h3><cms:contentText key="ADD_RESOURCE_CONTENT" code="diyCommunications.resource.labels" /></h3>
					<p><cms:contentText key="ADD_RESOURCE_CONTENT_DESC" code="diyCommunications.resource.labels" /></p>

                    <div class="resourceCenterAddedContent addedContent">
                        <table class="table table-striped contentTable" id="resourceCenterContentTable">
                            <thead>
                                <tr>
                                    <th class="defaultHeader"><cms:contentText key="DEFAULT" code="diyCommunications.common.labels" /></th>
                                    <th><cms:contentText key="LANGUAGE" code="diyCommunications.common.labels" /></th>
                                    <th><cms:contentText key="LINK_TITLE" code="diyCommunications.resource.labels" /></th>
                                    <th><cms:contentText key="LINK" code="diyCommunications.common.labels" /></th>
                                    <th class="editColumn"><cms:contentText key="EDIT" code="diyCommunications.common.labels" /></th>
                                    <th class="remove"><cms:contentText key="REMOVE" code="diyCommunications.common.labels" /></th>
                                </tr>
                            </thead>
                            <tbody data-msg-empty="<cms:contentText key="EMPTY_RESOURCE_CONTENT" code="diyCommunications.errors" />">
                            </tbody>
                        </table>
                    </div>

                    <script id="resourceCenterContentTableTpl" type="text/x-handlebars-template">
                        <tr data-resourceId="{{id}}" data-indexId="{{index}}" {{#if isNew}} class="newResource" {{/if}}>
                            <td class="defaultColumn">
                                <div class="control-group validateme"
                                    data-validate-fail-msgs="<cms:contentText key="DEFAULT_RESOURCE_REQUIRED" code="diyCommunications.errors" />"
                                    data-validate-flags='nonempty'>
                                    <div class="controls">
                                        <input type="radio" name="defaultLanguage" value="{{language}}" {{#if isDefaultLang}}checked{{/if}} {{#if isSystemLanguage}}class="systemDefault"{{/if}}/>
                                    </div>
                                </div>

                            </td>
                            <td class="languageColumn" data-language="{{language}}">
                                <span data-language="{{language}}">{{languageDisplay}}</span>

                                <input type="hidden" name="resourceContent[{{index}}].id" value="{{id}}"/>
                                <input type="hidden" name="resourceContent[{{index}}].language" value="{{language}}"/>
                                <input type="hidden" name="resourceContent[{{index}}].title" value="{{title}}"/>
                                <input type="hidden" name="resourceContent[{{index}}].link" value="{{link}}"/>
                            </td>
                            <td class="titleColumn">{{title}}</td>
                            <td class="linkColumn">{{link}}</td>
                            <td class="editColumn">
                                <a href="#">
                                    <i class="icon-pencil2"></i>
                                </a>
                            </td>
                            <td class="remove">
                                <a href="#" class="remParticipantControl {{#if systemLocale}}disabled{{/if}}" title="<cms:contentText key="REMOVE_RESOURCE" code="diyCommunications.common.labels"/>" {{#if systemLocale}}disabled{{/if}}><i class="icon-trash"></i></a>
                            </td>
                        </tr>
                    </script>

                    <div class="resourceRemoveConfirmDialog contentRemoveConfirmDialog" style="display:none">
                        <p>
                            <b><cms:contentText key="REMOVE_POP_UP_TXT" code="diyCommunications.common.labels" /></b>
                        </p>
                        <p class="tc">
                            <button type="button" id="resourceRemoveDialogConfirm" class="btn btn-primary contentRemoveDialogConfirm"><cms:contentText key="YES" code="diyCommunications.common.labels" /></button>
                            <button type="button" id="resourceRemoveDialogCancel" class="btn contentRemoveDialogCancel"><cms:contentText key="NO" code="diyCommunications.common.labels" /></button>
                        </p>
                    </div>

                    <div class="resourceCenterAddNewContent addNewContentContainer container-splitter with-splitter-styles">
                        <input type="hidden" name="resourceContentId" id="resourceContentId" class="contentId" />
                        <input type="hidden" id="userDefaultLanguage" name="userDefaultLanguage" value="${diyCommunicationsResourceCenterForm.chooseLanguage}" />
                        <div class="control-group validateme"
                            data-validate-fail-msgs='{"nonempty":"<cms:contentText key="LANGUAGE_REQUIRED" code="diyCommunications.errors" />"}'
                            data-validate-flags='nonempty'>
                            <label class="control-label" for="chooseLanguage">
                                <cms:contentText key="AUDIENCE_LANGUAGE" code="diyCommunications.common.labels" />
                            </label>
                            <div class="controls">
                                <select id="chooseLanguage" name="chooseLanguage" >
					               <c:forEach var="language" items="${languageList}">
					          		  <option value="<c:out value="${language.code}"/>" <c:if test="${language.code == diyCommunicationsResourceCenterForm.chooseLanguage}">selected="selected"</c:if>><c:out value="${language.name}"/></option>
					       		   </c:forEach>
              					</select>
                            </div>
                        </div>

                        <div class="control-group validateme"
                            data-validate-fail-msgs='{"nonempty":"<cms:contentText key="TITLE_REQUIRED" code="diyCommunications.errors" />"}'
                            data-validate-flags='nonempty'>
                            <label class="control-label" for="resourceContentLinkTitle"><cms:contentText key="LINK_TITLE" code="diyCommunications.resource.labels" /></label>
                            <div class="controls">
                                <input type="text" name="resourceContentLinkTitle" id="resourceContentLinkTitle" class="contentLinkTitle" />
                            </div>
                        </div>

                        <div class="control-group validateme resourceCenterAddLink contentAddLink"
                            data-validate-fail-msgs='{"nonempty":"<cms:contentText key="LINK_REQUIRED" code="diyCommunications.errors" />"}'
                            data-validate-flags='nonempty'>
                            <label class="control-label" for="resourceCenterLink"><cms:contentText key="LINK_RESOURCE" code="diyCommunications.resource.labels" /></label>
                            <p class="communicationsHelpText"><cms:contentText key="SELECT_DOCUMENT" code="diyCommunications.resource.labels" /></p>
                            <div>
                                <a href="#" class="addDocResource addDoc"><i class="icon-file-upload"></i><cms:contentText key="DOCUMENT" code="diyCommunications.common.labels" /></a>
                                <a href="#" class="addUrlResource addUrl"><i class="icon-link-1"></i><cms:contentText key="URL" code="diyCommunications.common.labels" /></a>
                            </div>

                            <div class="resourceDisplayAttached contentDisplayAttached controls">
                                <span class="resourceDisplayLink contentDisplayLink"></span>
                                <input type="text" id="resourceCenterLink" name="resourceCenterLink" class="contentLink" />
                                <a class="removeLink btn btn-mini btn-icon btn-danger hide"><i class="icon-trash"></i></a>
                            </div>
                        </div>

                        <!-- URL Popover -->
                        <div class="addUrlPopover" style="display: none">
                            <div class="control-group"
                                data-validate-fail-msgs='{"nonempty":"<cms:contentText key="TITLE_REQUIRED" code="diyCommunications.errors" />"}'
                                data-validate-flags='nonempty'>
                                <label class="control-label" for="resourceContentURL"><cms:contentText key="ENTER_URL" code="diyCommunications.common.labels" /></label>
                                <div class="controls">
                                    <input type="text" name="resourceContentURL" id="resourceContentURL" class="contentUrl" />
                                    <button type="button" class="attachUrlBtn btn btn-primary"><cms:contentText key="ATTACH" code="diyCommunications.common.labels" /></button>
                                    <p class="muted"><cms:contentText key="EXAMPLE_LINK" code="diyCommunications.common.labels" /></p>
                                </div>
                            </div>
                        </div>

                        <button type="button" class="btn btn-primary saveContent"><cms:contentText key="SAVE_RESOURCE_CONTENT" code="diyCommunications.resource.labels" /></button>
                        <button type="button" class="btn cancelContent"><cms:contentText key="CANCEL" code="diyCommunications.common.labels" /></button>
                    </div>

                    <div class="addLanguageContent">
                        <a href="#" class="disabled"><cms:contentText key="ADD_CONTENT" code="diyCommunications.common.labels" /></a>
                    </div>
                </fieldset>

                <fieldset class="formSection form-actions pullBottomUp">
                    <button class="resourceContentSubmit contentSubmit btn btn-primary" type="submit" disabled>
                        <cms:contentText key="SUBMIT" code="diyCommunications.common.labels" />
                    </button>
                    <button class="resourceContentCancel contentCancel btn" data-url="${pageContext.request.contextPath}/participant/manageResourceCenter.do">
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

                <div class="resourceCenterCancelConfirm contentCancelConfirm" style="display:none">
                    <p>
                        <b><cms:contentText key="CANCEL_CONFIRMATION" code="diyCommunications.common.labels" /></b>
                    </p>
                    <p class="tc">
                        <button type="submit" id="resourceCenterCancelDialogConfirm" class="btn btn-primary contentCancelDialogConfirm"><cms:contentText key="YES" code="diyCommunications.common.labels" /></button>
                        <button type="submit" id="resourceCenterCancelDialogCancel" class="btn contentCancelDialogCancel"><cms:contentText key="NO" code="diyCommunications.common.labels" /></button>
                    </p>
                </div>
            </form>

            <!-- Doc Popover -->
            <div class="addDocPopover" style="display: none">
                <div class="control-group"
                    data-validate-fail-msgs='{"nonempty":"<cms:contentText key="TITLE_REQUIRED" code="diyCommunications.errors" />"}'
                    data-validate-flags='nonempty'>
                    <label class="control-label"><cms:contentTemplateText key="UPLOAD_DOCUMENT" code="diyCommunications.resource.labels" args="${pdfSixeLimit},${imgSixeLimit}" delimiter=","/></label>
                    <div class="controls">
                        <form id="resourceDocUpload" class="addDocUpload" enctype="multipart/form-data">
                            <input type="file" name="resourceContentDoc" id="resourceContentDoc" class="contentDoc" />
                        </form>
                    </div>
                </div>
            </div><!--/.addDocPopover-->
        </div>
    </div>
</div>

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
$(document).ready(function() {

	<%
		DIYCommunicationsResourceCenterForm form = (DIYCommunicationsResourceCenterForm) request.getAttribute("diyCommunicationsResourceCenterForm");
		Long tempResourceId = (Long)request.getAttribute("resourceId");
		Map clientStateParameterMap = ClientStateUtils.getClientStateMap(request);
		clientStateParameterMap.put( "resourceId", tempResourceId );

		pageContext.setAttribute("encodedEditUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), "/participant/communicationResourceData.do?method=populateResourceCenterDataEdit", clientStateParameterMap ) );
		pageContext.setAttribute("encodedAudienceUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), "/participant/communicationResourceAudienceList.do?method=populatePublicAudience", clientStateParameterMap ) );
	%>

	G5.props.URL_JSON_COMMUNICATION_RESOURCE_CENTER_DATA = "${encodedEditUrl}";
	G5.props.URL_JSON_SELECT_AUDIENCE_DATA = "${encodedAudienceUrl}";
	G5.props.URL_JSON_COMMUNICATION_UPLOAD_DOCUMENT = "${pageContext.request.contextPath}/participant/communicationUploadDocument.do?method=uploadDocument";

    //attach the view to an existing DOM element
    var crcev = new CommunicationsEditView({
        el:$('#communicationsResourceCenterEditView'),
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
        mode: 'resources',
        pageTitle : '<cms:contentText key="CREATE_RESOURCE_CENTER_CONTENT" code="diyCommunications.common.labels" />'
    });

});
</script>

<script type="text/template" id="selectAudienceParticipantsViewTpl">
		<%@include file="/diypaxcommunications/selectAudienceParticipantsView.jsp"%>
</script>
