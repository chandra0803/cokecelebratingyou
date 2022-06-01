<%@ include file="/include/taglib.jspf"%>
<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.diycommunication.DIYCommunicationsBannerForm" %>
<%@page import="com.biperf.core.utils.UserManager"%>

<!-- ======== COMMUNICATIONS BANNER EDIT/CREATE PAGE ======== -->
<div id="communicationsBannersEditView" class="page-content">
    <div class="row-fluid">
        <div class="span12">
            <h2><cms:contentText key="CREATE_BANNERS" code="diyCommunications.common.labels" /></h2>

            <form class="sendForm pullBottomUp" method="post" action="saveDiyBanner.do">

            <!--JAVA NOTE: Display error text in the below list when page submit returns an error and set the hidden input value to true. -->
                <div id="communicationsErrorBlock" class="alert alert-block alert-error" style="display:none">
	                <h4><cms:contentText key="FOLLOWING_ERRORS" code="diyCommunications.errors" /></h4>

	                <html:messages id="actionMessage" >
                    <ul>
                    	<c:set var="serverReturnedErrorForBanner" value="true"/>
                        <li>${actionMessage}</li>
                    </ul>
                    </html:messages>

                    <input type="hidden" id="serverReturnedErrored" value="${serverReturnedErrorForBanner}" />
                </div>

                <input type="hidden" name="method" id="sendFormMethod" value="saveBanner" />

                <fieldset class="formSection bannerInfo contentInfo">
                    <h3><cms:contentText key="PAGE_TITLE" code="diyCommunications.banner.labels" /></h3>

                    <div class="control-group validateme"
                        data-validate-fail-msgs='{"nonempty":"<cms:contentText key="TITLE_REQUIRED" code="diyCommunications.errors" />"}'
                        data-validate-flags='nonempty'>
                        <label class="control-label" for="bannerTitle"><cms:contentText key="BANNER_TITLE" code="diyCommunications.banner.labels" /></label>
                        <div class="controls">
                            <input type="text" name="bannerTitle" id="bannerTitle" class="contentTitle" maxlength="100"/>
                        </div>
                    </div>

                    <div class="control-group validateme"
                        data-validate-fail-msgs='{"nonempty":"<cms:contentText key="START_DATE_REQUIRED" code="diyCommunications.errors" />"}'
                        data-validate-flags='nonempty' >
                        <label for="bannerStartDate" class="control-label"><cms:contentText key="START_DATE" code="diyCommunications.common.labels" /></label>
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
	                                    name="bannerStartDate"
	                                    id="bannerStartDate"
	                                    value=""
	                                    readonly="readonly"><button type="button" class="btn datepickerBtn"><i class="icon-calendar"></i></button>
                            	</span>
                            </c:when>
                            <c:otherwise>
                                 <span class="input-append datepickerTrigger"
	                                data-date-format="<%=UserManager.getUserDatePattern().toLowerCase()%>"
	                                data-date-language="<%=UserManager.getUserLocale()%>"
	                                data-date-autoclose="true">

	                                <input type="text" class="date datepickerInp contentStartDate"
	                                    name="bannerStartDate"
	                                    id="bannerStartDate"
	                                    value=""
	                                    readonly="readonly"><button type="button" class="btn datepickerBtn"><i class="icon-calendar"></i></button>
                            	</span>
                            </c:otherwise>
                          </c:choose>
                        </div>
                    </div>

                    <div class="control-group validateme"
                        data-validate-fail-msgs='{"nonempty":"<cms:contentText key="END_DATE_REQUIRED" code="diyCommunications.errors" />"}'
                        data-validate-flags='nonempty' >
                        <label for="bannerEndDate" class="control-label"><cms:contentText key="END_DATE" code="diyCommunications.common.labels" /></label>
                        <div class="controls">
                            <!--
                                NOTE: JSP set data-date-format AND data-date-endDate
                            -->
                            <span class="input-append datepickerTrigger"
                                data-date-format="<%=UserManager.getUserDatePattern().toLowerCase()%>"
                                data-date-language="<%=UserManager.getUserLocale()%>"
                                data-date-autoclose="true">

                                <input type="text" class="date datepickerInp contentEndDate"
                                    name="bannerEndDate"
                                    id="bannerEndDate"
                                    value=""
                                    readonly="readonly"><button type="button" class="btn datepickerBtn"><i class="icon-calendar"></i></button>
                            </span>
                        </div>
                    </div>
                </fieldset>

                <fieldset class="formSection bannerSelectAudience">
                    <h3><cms:contentText key="SELECT_AUDIENCE" code="diyCommunications.common.labels" /></h3>

                    <div class="selectAudienceTableWrapper"></div>

                </fieldset>

                <fieldset class="formSection bannerAddContentSection">
                    <h3><cms:contentText key="ADD_BANNER" code="diyCommunications.banner.labels" /></h3>
                    <p><cms:contentText key="ADD_BANNER_DESC" code="diyCommunications.banner.labels" /></p>

                    <div class="bannerAddedContent addedContent">
                        <table class="table table-striped contentTable" id="bannersTable">
                            <thead>
                                <tr>
                                    <th class="defaultHeader"><cms:contentText key="DEFAULT" code="diyCommunications.common.labels" /></th>
                                    <th><cms:contentText key="LANGUAGE" code="diyCommunications.common.labels" /></th>
                                    <th><cms:contentText key="IMAGE" code="diyCommunications.common.labels" /></th>
                                    <th><cms:contentText key="LINK" code="diyCommunications.common.labels" /></th>
                                    <th class="editColumn"><cms:contentText key="EDIT" code="diyCommunications.common.labels" /></th>
                                    <th class="remove"><cms:contentText key="REMOVE" code="diyCommunications.common.labels" /></th>
                                </tr>
                            </thead>
                            <tbody data-msg-empty="<cms:contentText key="EMPTY_BANNER" code="diyCommunications.errors" />">

                            </tbody>
                        </table>
                    </div>

                    <script id="bannersContentTableTpl" type="text/x-handlebars-template">
                        <tr data-bannerId="{{id}}" data-indexId="{{index}}" {{#if isNew}} class="newBanner" {{/if}}>
                            <td class="defaultColumn">
                                <div class="control-group validateme"
                                   data-validate-fail-msgs="<cms:contentText key="DEFAULT_BANNER_REQUIRED" code="diyCommunications.errors" />"
                                    data-validate-flags='nonempty'>
                                    <div class="controls">
                                        <input type="radio" name="defaultLanguage" value="{{language}}" {{#if isDefaultLang}}checked{{/if}} {{#if isSystemLanguage}}class="systemDefault"{{/if}}/>
                                    </div>
                                </div>

                            </td>
                            <td class="languageColumn" data-language="{{language}}">
                                <span data-language="{{language}}">{{languageDisplay}}</span>

                                <input type="hidden" name="bannerContent[{{index}}].id" value="{{id}}"/>
                                <input type="hidden" name="bannerContent[{{index}}].language" value="{{language}}"/>
								<input type="hidden" name="bannerContent[{{index}}].languageDisplay" value="{{languageDisplay}}"/>
								<input type="hidden" name="bannerContent[{{index}}].imageId" value="{{imageId}}"/>
                                <input type="hidden" name="bannerContent[{{index}}].imageSize" value="{{imageSize}}"/>
                                <input type="hidden" name="bannerContent[{{index}}].imageSize_mobile" value="{{imageSize_mobile}}"/>
                                <input type="hidden" name="bannerContent[{{index}}].imageSize_max" value="{{imageSize_max}}"/>
                                <input type="hidden" name="bannerContent[{{index}}].link" value="{{link}}"/>
                            </td>
                            <td class="imageColumn">
                                <img data-id="{{imageId}}" src="{{imageSize_mobile}}" alt="" />
                            </td>
                            <td class="linkColumn">{{link}}</td>
                            <td class="editColumn">
                                <a href="#">
                                    <i class="icon-pencil2"></i>
                                </a>
                            </td>
                            <td class="remove">
                                <a href="#" class="remParticipantControl {{#if systemLocale}}disabled{{/if}}" title="<cms:contentText key="REMOVE_BANNER" code="diyCommunications.common.labels" />" {{#if systemLocale}}disabled{{/if}}><i class="icon-trash"></i></a>
                            </td>
                        </tr>
                    </script>

                    <div class="bannerRemoveConfirmDialog contentRemoveConfirmDialog" style="display:none">
                        <p>
                              <b><cms:contentText key="REMOVE_POP_UP_TXT" code="diyCommunications.common.labels" /></b>
                        </p>
                        <p class="tc">
                            <button type="button" id="bannerRemoveDialogConfirm" class="btn btn-primary contentRemoveDialogConfirm"><cms:contentText key="YES" code="diyCommunications.common.labels" /></button>
                            <button type="button" id="bannerRemoveDialogCancel" class="btn contentRemoveDialogCancel"><cms:contentText key="NO" code="diyCommunications.common.labels" /></button>
                        </p>
                    </div>

                    <div class="bannerAddNewContent addNewContentContainer container-splitter with-splitter-styles">
                        <input type="hidden" name="bannerId" id="bannerId" class="contentId" />
                        <input type="hidden" id="userDefaultLanguage" name="userDefaultLanguage" value="${diyCommunicationsBannerForm.chooseLanguage}" />
                        <div class="control-group validateme"
                            data-validate-fail-msgs='{"nonempty":"<cms:contentText key="LANGUAGE_REQUIRED" code="diyCommunications.errors" />"}'
                            data-validate-flags='nonempty'>
                            <label class="control-label" for="chooseLanguage">
                                <cms:contentText key="AUDIENCE_LANGUAGE" code="diyCommunications.common.labels" />
                            </label>
                            <div class="controls">
                               <select id="chooseLanguage" name="chooseLanguage" >
					               <c:forEach var="language" items="${languageList}">
					          		  <option value="<c:out value="${language.code}"/>" <c:if test="${language.code == diyCommunicationsBannerForm.chooseLanguage}">selected="selected"</c:if>><c:out value="${language.name}"/></option>
					       		   </c:forEach>
              					</select>
                            </div>
                        </div>

                        <!-- Images -->
                        <div class="formSection chooseImagesSection" ></div>

                        <script id="chooseImagesTpl" type="text/x-handlebars-template">
                            <div class="imageLargeWrapper" data-size='{"ratio":"2.35:1"}'><p><cms:contentText key="UPLOAD_IMAGE_TEXT" code="diyCommunications.common.labels"/></p></div>

                            <div class="thumbnailWrapper validateme" data-validate-flags='nonempty' data-validate-fail-msgs='{"nonempty":"<cms:contentText key="IMAGE_REQUIRED" code="diyCommunications.errors" />"}'>
                                <input type="checkbox" name="selectedImage" class="selectedImageInput"/>

                                <div id="thumbnailPager">
                                    <a class="btn btn-icon" data-pager="prev" title="<cms:contentText key="PREVIOUS" code="diyCommunications.common.labels" />"><i class="icon-arrow-1-up"></i></a>
                                    <a class="btn btn-icon" data-pager="next" title="<cms:contentText key="NEXT" code="diyCommunications.common.labels" />"><i class="icon-arrow-1-down"></i></a>

                                    <!--subTpl.eCardThumbnailPagerMeta=
                                    <span id="thumbnailPagerMeta"><span class="range">{{startNumber}}&#8211;{{endNumber}}</span> <i class="of"><cms:contentText key="OF" code="diyCommunications.common.labels" /></i> <span class="total">{{total}}</span>
                                    </span>subTpl-->
                                </div>

                                <div id="thumbnailContainer">
                                    <ul id="thumbnailSelect">
                                        <li class="uploadContainer" data-id="">
                                            <img class="imageThumbnail hide" alt="uploadedImage" src="{{imageSizeMobile}}"/>
                                            <span class="upload-text">
                                                <i class="icon-upload-1"></i> <cms:contentText key="UPLOAD_IMAGE" code="diyCommunications.common.labels" />
                                            </span>
                                        </li>
                                        {{#each bannerImages.images}}
                                            <li data-id="{{id}}">
                                                <img class="imageThumbnail" alt="{{name}}" src="{{imageSizeMobile}}"/>
                                            </li>
                                        {{/each}}
                                    </ul>
                                </div>
                            </div>
                        </script>

                        <div class="control-group bannerAddLink contentAddLink"
                            <%-- data-validate-fail-msgs='{"nonempty":"<cms:contentText key="LINK_REQUIRED" code="diyCommunications.errors" />"}'
                            data-validate-flags='nonempty'--%>>
                            <label class="control-label" for="bannerLink"><cms:contentText key="LINK_RESOURCE" code="diyCommunications.banner.labels" /> <span class="optional"><cms:contentText key="OPTIONAL" code="system.common.labels" /></span></label>
                            <p class="communicationsHelpText"><cms:contentText key="SELECT_DOCUMENT" code="diyCommunications.banner.labels" /></p>
                            <div>
                                <a href="#" class="addDocBanner addDoc"><i class="icon-file-upload"></i><cms:contentText key="DOCUMENT" code="diyCommunications.common.labels" /></a>
                                <a href="#" class="addUrlBanner addUrl"><i class="icon-link-1"></i><cms:contentText key="URL" code="diyCommunications.common.labels" /></a>
                            </div>

                            <div class="bannerDisplayAttached contentDisplayAttached controls">
                                <span class="bannerDisplayLink contentDisplayLink"></span>
                                <input type="text" id="bannerLink" name="bannerLink" class="contentLink" />
                                <a class="removeLink btn btn-mini btn-icon btn-danger hide"><i class="icon-trash"></i></a>
                            </div>
                        </div>

                        <!-- URL Popover -->
                        <div class="addUrlPopover" style="display: none">
                            <div class="control-group"
                                data-validate-fail-msgs='{"nonempty":"<cms:contentText key="TITLE_REQUIRED" code="diyCommunications.errors" />"}'
                                data-validate-flags='nonempty'>
                                <label class="control-label" for="bannerURL"><cms:contentText key="ENTER_URL" code="diyCommunications.common.labels" /></label>
                                <div class="controls">
                                    <input type="text" name="bannerURL" id="bannerURL" class="contentUrl" />
                                    <button type="button" class="attachUrlBtn btn btn-primary"><cms:contentText key="ATTACH" code="diyCommunications.common.labels" /></button>
                                    <p class="muted"><cms:contentText key="EXAMPLE_LINK" code="diyCommunications.common.labels" /> <br> <br> <cms:contentText key="VIDEO_DIRECT_INSTRUCTIONS" code="diyCommunications.common.labels" /></p>
                                </div>
                            </div>
                        </div>

                        <button type="button" class="btn btn-primary saveContent"><cms:contentText key="SAVE_BANNER" code="diyCommunications.banner.labels" /></button>
                        <button type="button" class="btn cancelContent"><cms:contentText key="CANCEL" code="diyCommunications.common.labels" /></button>
                    </div>

                    <div class="addLanguageContent">
                        <a href="#" class="disabled"><cms:contentText key="ADD_BANNER" code="diyCommunications.common.labels" /></a>
                    </div>
                </fieldset>

                <fieldset class="formSection form-actions pullBottomUp">
                    <button class="bannerSubmit contentSubmit btn btn-primary" type="submit" disabled>
                        <cms:contentText key="SUBMIT" code="diyCommunications.common.labels" />
                    </button>
                    <!-- JAVA NOTE: Set the data url to the page the user will return to-->
                    <button class="bannerCancel contentCancel btn" data-url="${pageContext.request.contextPath}/participant/manageBanners.do">
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

                <div class="bannerCancelConfirm contentCancelConfirm" style="display:none">
                    <p>
                        <b><cms:contentText key="CANCEL_CONFIRMATION" code="diyCommunications.common.labels" /></b>
                    </p>
                    <p class="tc">
                        <button type="submit" id="bannerCancelDialogConfirm" class="btn btn-primary contentCancelDialogConfirm"><cms:contentText key="YES" code="diyCommunications.common.labels" /></button>
                        <button type="submit" id="bannerCancelDialogCancel" class="btn contentCancelDialogCancel"><cms:contentText key="NO" code="diyCommunications.common.labels" /></button>
                    </p>
                </div>
            </form>

            <!-- Upload Modals -->
            <div class="modal modal-stack hide fade" id="uploadImageModal" data-y-offset="adjust">
                <div class="modal-header">
                    <h3><cms:contentText key="UPLOAD_IMAGES" code="diyCommunications.common.labels" /></h3>
                </div>
                <div class="modal-body">
                    <div class="row-fluid">
                        <div class="span12">
                            <p><cms:contentText key="UPLOAD_DESCRIPTION" code="diyCommunications.banner.labels" /></p>
                        </div>
                    </div>

                    <div class="row-fluid">
                        <div class="imageUploadContainer span12">
                            <section class="imageUploadFormContainer">
                                <form class="bannerUploadImage imageUploadForm" enctype="multipart/form-data">
                                    <p>
                                        <input id="image" type="file" name="image" />
                                        <label for="image"></label>
                                        <button id="imageUploadSubmit" class="btn btn-primary" type="submit" style="display:none"><cms:contentText key="UPLOAD" code="diyCommunications.banner.labels" /></button>
                                    </p>

                                    <p>
                                        <span class="uploadRequirements"><cms:contentText key="IMAGE_MUST_BE_ATLEAST" code="diyCommunications.banner.labels" /></span>
                                        <span class="uploadRequirements"><cms:contentText key="FILE_TYPES" code="diyCommunications.banner.labels" /></span>
                                    </p>
                                </form>
                            </section>

                            <section class="imageUploadPreviewContainer" style="display:none">
                            </section>

                            <div id="uploadReplacementImage" style="display:none;">
                                <form class="uploadReplacementImageForm">
                                    <input class="" type="file" name="replacementImage" id="replacementImage">
                                    <input type="hidden" name="size" id="replacementImageSize" value="">
                                    <span class="uploadRequirements"><cms:contentText key="FILE_TYPES" code="diyCommunications.banner.labels" /></span>
                                    <p class="alert alert-info hide no-fileinput"><cms:contentText key="UPLOAD_ERROR_MESSAGE" code="diyCommunications.banner.labels" /></p>
                                </form>
                            </div>
                        </div>
                    </div>

                    <div class="uploadCancelConfirm" style="display:none">
                        <p>
                           <b><cms:contentText key="CANCEL_CONFIRMATION" code="diyCommunications.common.labels" /></b>
                        </p>
                        <p class="tc">
                            <button type="submit" id="uploadCancelDialogConfirm" class="btn btn-primary"><cms:contentText key="YES" code="diyCommunications.common.labels" /></button>
                            <button type="submit" id="uploadCancelDialogCancel" class="btn"><cms:contentText key="NO" code="diyCommunications.common.labels" /></button>
                        </p>
                    </div>

                    <div class="row-fluid">
                        <div class="span12 imageUploadButtons">
                            <button class="bannerImageSave imageUploadSave btn btn-primary" type="submit"><cms:contentText key="SAVE" code="diyCommunications.common.labels" />
                            </button>
                            <button class="bannerImageCancel imageUploadCancel btn"><cms:contentText key="CANCEL" code="diyCommunications.common.labels" />
                            </button>
                        </div>
                    </div>
                </div><!-- /.modal-body -->
            </div><!--/.uploadImageModal-->

            <div class="addDocPopover" style="display: none">
                <div class="control-group"
                    data-validate-fail-msgs='{"nonempty":"<cms:contentText key="TITLE_REQUIRED" code="diyCommunications.errors" />"}'
                    data-validate-flags='nonempty'>
                    <label class="control-label"><cms:contentTemplateText key="UPLOAD_DOCUMENT" code="diyCommunications.resource.labels" args="${pdfSixeLimit},${imgSixeLimit}" delimiter=","/></label>
                    <div class="controls">
                        <form id="bannerDocUpload" class="addDocUpload" enctype="multipart/form-data">
                            <input type="file" name="bannerContentDoc" id="bannerContentDoc" class="contentDoc" />
                        </form>
                    </div>
                </div>
            </div><!--/.addDocPopover-->
        </div>
    </div>
</div>

<script type="text/template" id="uploadPreviewTpl">
<%@include file="communicationsImageUploadPreview.jsp"%>
</script>

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
$(document).ready(function() {
	<%
		DIYCommunicationsBannerForm form = (DIYCommunicationsBannerForm) request.getAttribute("diyCommunicationsBannerForm");
		Long tempBannerId = (Long)request.getAttribute("bannerId");
		if( tempBannerId != null )
		{
			Map parameterMap = ClientStateUtils.getClientStateMap(request);
			parameterMap.put( "bannerId", tempBannerId );

			pageContext.setAttribute("encodedEditUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), "/participant/createDiyBanner.do?method=populateBannerContent", parameterMap ) );
			pageContext.setAttribute("encodedAudienceUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), "/participant/createDiyBanner.do?method=populatePublicAudience", parameterMap ) );
		}else{
			pageContext.setAttribute("encodedEditUrl", RequestUtils.getBaseURI(request)+ "/participant/createDiyBanner.do?method=populateBannerContent");
			pageContext.setAttribute("encodedAudienceUrl", RequestUtils.getBaseURI(request)+ "/participant/createDiyBanner.do?method=populatePublicAudience");
		}
    %>
	G5.props.URL_JSON_COMMUNICATION_BANNER_DATA = '${encodedEditUrl}';
	G5.props.URL_JSON_SELECT_AUDIENCE_DATA = '${encodedAudienceUrl}';
	G5.props.URL_JSON_COMMUNICATION_UPLOAD_DOCUMENT = "${pageContext.request.contextPath}/participant/createDiyBanner.do?method=uploadDocument";
	G5.props.URL_JSON_COMMUNICATION_UPLOAD_IMAGES = "${pageContext.request.contextPath}/participant/createDiyBanner.do?method=uploadBanner";

    //attach the view to an existing DOM element
    var cbev = new CommunicationsEditView({
        el:$('#communicationsBannersEditView'),
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
        mode: 'banners',
        pageTitle : '<cms:contentText key="CREATE_BANNERS" code="diyCommunications.common.labels" />'
    });

});
</script>

<script type="text/template" id="selectAudienceParticipantsViewTpl">
		<%@include file="/diypaxcommunications/selectAudienceParticipantsView.jsp"%>
</script>
