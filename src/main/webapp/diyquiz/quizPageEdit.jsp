 <%@page import="com.biperf.core.utils.DateUtils"%>
<%@page import="com.biperf.core.utils.UserManager"%>
<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf" %>

<!-- ======== QUIZ EDIT PAGE ======== -->

<!--
    Create and Edit Quizes.
    - JSON used due to the complex nature of the quiz
    - JAVA, please discuss any alterations needed for information exchange with FE (Aaron)
-->

<div id="quizPageEditView" class="quizPage quizPageEdit page-content">
    <div class="row-fluid">
        <div class="span12">
            <h2><cms:contentText key="PAGE_TITLE" code="quiz.diy.form"/></h2>
        </div>
    </div>
    <div class="row-fluid">

    <!-- DIV.row-fluid up next to wrapping div.page-content for IE7 -->
     <div class="span12">
        <logic:present name="org.apache.struts.action.ERROR">
            <!-- JAVA NOTE: in the rare event that the server has an error (FE should catch all errors) -->
            <div class="alert alert-block alert-error">
                <button type="button" class="close" data-dismiss="alert"><i class="icon-close"></i></button>
                <div class="error">
                    <h4><cms:contentText key="FOLLOWING_ERRORS" code="system.generalerror" /></h4>
                    <ul>
                        <html:messages id="error" >
                            <li><c:out value="${error}"/></li>
                        </html:messages>
                    </ul>
                </div>
            </div>
        </logic:present>


            <!-- **************************************************************
                WizardTabsView
             ***************************************************************** -->
            <ul class="wizardTabsView" data-content=".wizardTabsContent" style="visibility:hidden">
                <!-- generated using json+tpl by WizardTabsView -->
            </ul><!-- /.wizardTabsView -->





            <!-- **************************************************************
                WIZARD TABS CONTENT
             ***************************************************************** -->
            <div class="wizardTabsContent">



                <!-- **************************************************************
                    INTRO
                 ***************************************************************** -->
                <div id="quizEditTabIntroView" class="stepIntroContent stepContent" style="display:none">

                    <div class="createAsDisplayOnly" style="display:none">
                        <div class="createAsParticipant">
                            <p>
                                <i class="icon-user"></i>
                                <strong>
                                    <span class="_firstName _pax">&nbsp;</span>
                                    <span class="_lastName _pax">&nbsp;</span>
                                </strong>
                                <cms:contentText key="OWNS_QUIZ" code="quiz.diy.form"/>
                            </p>
                        </div>
                    </div><!-- /.createAsDisplayOnly -->

                    <div class="createAsWrapper" style="display:none">
                        <div class="control-group">
                            <cms:contentText key="CREATE_ON_BEHALF" code="quiz.diy.form"/>
                            <i data-help-content="<cms:contentText key="CREATE_ON_BEHALF_HELP" code="quiz.diy.form"/>" class="icon-question pageView_help"></i>
                        </div>

                        <div class="createAsSearchWrapper">
                            <div class="createAsParticipant">
                                <p class="_paxShow" style="display:none">
                                    <i class="icon-user"></i>
                                    <strong>
                                        <span class="_firstName _pax">&nbsp;</span>
                                        <span class="_lastName _pax">&nbsp;</span>
                                    </strong>
                                     <cms:contentText key="WILL_OWN_QUIZ" code="quiz.diy.form"/>
                                </p>
                            </div>
                            <!-- pax search for paxes -->
                            <div class="paxSearchStartView" ></div><!-- /.paxSearchStartView -->

                            <!--
                                Participant search view Element
                                - data-search-types: defines the dropdowns and autocompletes
                                - data-search-params: defines extra static parameters to send with autocomp and participant requests
                                - data-search-url: override search json provider (usually needed)
                                ...
                            -->
                            <!--<div class="paxCreateAsView" style="display:none"
                                data-search-types='[{"id":"lastName","name":"<cms:contentText key="SEARCHBY_LAST_NAME" code="recognition.select.recipients"/>"},{"id":"firstName","name":"<cms:contentText key="SEARCHBY_FIRST_NAME" code="recognition.select.recipients"/>"},{"id":"location","name":"<cms:contentText key="SEARCHBY_LOCATION" code="recognition.select.recipients"/>"},{"id":"jobTitle","name":"<cms:contentText key="SEARCHBY_JOB_TITLE" code="recognition.select.recipients"/>"},{"id":"department","name":"<cms:contentText key="SEARCHBY_DEPARTMENT" code="recognition.select.recipients"/>"},{"id":"country","name":"<cms:contentText key="COUNTRY" code="recognition.select.recipients"/>"}]'
                                data-autocomp-delay="500"
                                data-autocomp-min-chars="2"
                                data-autocomp-url="${pageContext.request.contextPath}/quiz/diyQuizOnBehalfSearchParticipant.do?method=doAutoComplete&promotionId=${diyQuizForm.promotionId}"
                                data-search-url="${pageContext.request.contextPath}/quiz/diyQuizOnBehalfSearchParticipant.do?method=generatePaxSearchView&promotionId=${diyQuizForm.promotionId}"
                                data-msg-show-single="<cms:contentText key="QUIZ_OWNER_REQUIRED" code="quiz.diy.errors"/>"
                                data-visibility-controls="showAndHide" >
                            </div>-->
                        </div><!-- /.createAsSearchWrapper -->
                    </div><!-- /.createAsWrapper -->

                    <div class="control-group validateme"
                        data-validate-flags="nonempty"
                        data-validate-fail-msgs='{"nonempty" : "<cms:contentText key="QUIZ_NAME_REQUIRED" code="quiz.diy.form"/>"}'>
                        <label class="control-label" for="name">
                            <cms:contentText key="QUIZ_NAME" code="quiz.diy.form"/>
                        </label>
                        <div class="controls">
                            <input type="text" id="name" class="quizNameInput" data-model-key="name" maxlength="50">
                            <span class="quizNameCheckingSpinnner" style="display:none"></span>
                            <i class="quizNameInvalid quizNameValidDispItem icon-cancel-circle" style="display:none"></i>
                            <span class="quizNameInvalidMsg quizNameValidDispItem" style="display:none">&nbsp;</span>
                            <i class="quizNameValid quizNameValidDispItem icon-check-circle" style="display:none"></i>
                        </div>
                    </div>


                    <div class="form-inline">
                        <div class="control-group validateme"
                            data-validate-flags="nonempty"
                            data-validate-fail-msgs='{"nonempty" : "<cms:contentText key="START_DATE_REQUIRED" code="quiz.diy.form"/>"}'>
                            <label class="control-label" for="startDate"><cms:contentText key="QUIZ_START_DATE" code="quiz.diy.form"/></label>
                            <div class="controls">
                                <!-- JAVA NOTE: set data-date-* attrs -->
                                <span class="input-append datepickerTrigger startDateTrigger"
                                        data-date-format="<%=UserManager.getUserDatePattern().toLowerCase()%>"
                                        data-date-language="<%=UserManager.getUserLocale()%>"
                                        data-date-startdate="${diyQuizForm.startDate}"
                                        data-date-todaydate="<%=DateUtils.toDisplayString( DateUtils.getCurrentDateTrimmed(  ) ) %>"
                                        data-date-autoclose="true">
                                    <input type="text" id="startDate" data-model-key="startDate" readonly="readonly" class="date startDateInput">
                                    <button class="btn" type="button"><i class="icon-calendar"></i></button>
                                </span>
                            </div>
                        </div>

                        <div class="control-group validateme"
                            data-validate-flags="nonempty"
                            data-validate-fail-msgs='{"nonempty" : "<cms:contentText key="END_DATE_REQUIRED" code="quiz.diy.form"/>"}'>
                            <label class="control-label" for="endDate"><cms:contentText key="QUIZ_END_DATE" code="quiz.diy.form"/></label>
                            <div class="controls">
                                <!-- JAVA NOTE: set data-date-* attrs -->
                                <span class="input-append datepickerTrigger endDateTrigger"
                                        data-date-format="<%=UserManager.getUserDatePattern().toLowerCase()%>"
                                        data-date-language="<%=UserManager.getUserLocale()%>"
                                        data-date-startdate="${diyQuizForm.startDate}"
                                        data-date-todaydate="<%=DateUtils.toDisplayString( DateUtils.getCurrentDateTrimmed(  ) ) %>"
                                        data-date-autoclose="true">
                                    <input type="text" id="endDate" data-model-key="endDate" readonly="readonly" class="date endDateInput">
                                    <button class="btn" type="button"><i class="icon-calendar"></i></button>
                                </span>
                            </div>
                        </div>
                    </div>

                    <fieldset><!-- fieldset required for validateme tooltip -->
                        <div class="control-group">
                            <label class="control-label">
                                <cms:contentText key="INTRODUCTION_TEXT" code="quiz.diy.form"/>
                            </label>
                            <div class="controls validateme"
                                data-validate-flags="maxlength,nonempty"
                                data-validate-fail-msgs='{"maxlength" : "<cms:contentText key="MAX_CHARS_EXCEEDED" code="quiz.diy.form"/>","nonempty":"<cms:contentText key="INTRODUCTION_TEXT_REQUIRED" escapeHtml="true" code="quiz.diy.form"/>"}'
                                data-validate-max-length="1000">
                                <!-- Placeholder text not rendering to RichText editor, needs more work -->
                                <textarea rows="5" data-max-chars="1000" data-model-key="introText" placeholder="<c:out value='${diyQuizForm.introText}'/>" class="richtext introTextInput"></textarea>
                            </div>
                        </div>
                    </fieldset>

                    <div class="teamMembersLoadingWrapper" style="display:none">
                        <div class="tmlSpin"></div><b><cms:contentText key="LOADING_TEAM_MEMBERS" code="quiz.diy.form"/></b>
                    </div><!-- /.teamMembersLoadingWrapper -->

                    <div class="teamMembersWrapper">
                        <h4><cms:contentText key="PARTICIPANTS" code="quiz.diy.form"/></h4>
                        <p class="teamMembersText" style="display:none">
                            <span class="_firstName">&nbsp;</span> <span class="_lastName">&nbsp;</span>'<cms:contentText key="TEAM_MEMBERS_ADDED_TEXT" code="quiz.diy.form"/>
                        </p>
                        <!-- pax search for paxes -->
                        <div class="paxSearchStartView2" ></div><!-- /.paxSearchStartView -->
                        <!--
                            Participant search view Element
                            - data-search-types: defines the dropdowns and autocompletes
                            - data-search-params: defines extra static parameters to send with autocomp and participant requests
                            - data-search-url: override search json provider (usually needed)
                            ...
                        -->
                        <!--<div class="paxSearchView" style="display:none"
                            data-search-types='[{"id":"lastName","name":"<cms:contentText key="SEARCHBY_LAST_NAME" code="recognition.select.recipients"/>"},{"id":"firstName","name":"<cms:contentText key="SEARCHBY_FIRST_NAME" code="recognition.select.recipients"/>"},{"id":"location","name":"<cms:contentText key="SEARCHBY_LOCATION" code="recognition.select.recipients"/>"},{"id":"jobTitle","name":"<cms:contentText key="SEARCHBY_JOB_TITLE" code="recognition.select.recipients"/>"},{"id":"department","name":"<cms:contentText key="SEARCHBY_DEPARTMENT" code="recognition.select.recipients"/>"},{"id":"country","name":"<cms:contentText key="COUNTRY" code="recognition.select.recipients"/>"}]'
                            data-autocomp-delay="500"
                            data-autocomp-min-chars="2"
                            data-autocomp-url="${pageContext.request.contextPath}/quiz/diyQuizSearchParticipant.do?method=doAutoComplete"
                            data-search-url="${pageContext.request.contextPath}/quiz/diyQuizSearchParticipant.do?method=generatePaxSearchView"
                            data-select-mode="multiple"
                            data-msg-select-txt="<cms:contentText key="ADD" code="system.button"/>"
                            data-msg-selected-txt="<i class='icon icon-check'></i>"
                            data-msg-validation="<cms:contentText key="PARTICIPANT_REQUIRED" code="quiz.diy.errors"/>"
                            data-visibility-controls="showAndHide" >
                        </div>
                        -->

                        <div class="container-splitter with-splitter-styles participantCollectionViewWrapper">
                            <table class="table table-condensed table-striped">
                                <thead>
                                    <tr>
                                        <th class="participant"><cms:contentText key="PARTICIPANTS" code="quiz.diy.form"/></th>
                                        <th class="remove"><cms:contentText key="REMOVE" code="quiz.diy.form"/></th>
                                    </tr>
                                </thead>
                                <tbody class="paxView participantCollectionView"
                                    data-msg-empty="<cms:contentText key="NOT_ADDED" code="quiz.diy.form"/>"
                                    data-hide-on-empty="true">
                                </tbody>
                            </table>

                            <!-- client side template for each participant in the table -->
                            <script id="paxRowTpl" type="text/x-handlebars-template">
                                <tr class="participant-item"
                                        data-participant-cid="{{cid}}"
                                        data-participant-id="{{id}}">
                                    <td class="participant">
                                        <a class="participant-popover" href="#" data-participant-ids="[{{id}}]">
                                            {{lastName}}, {{firstName}}
                                        </a>
                                    </td>
                                    <td class="remove">
                                    {{#unless locked}}
                                        <a class="remParticipantControl" title="<cms:contentText key='REMOVE_PAX' code='participant.search'/>"><i class="icon-trash"></i></a>
                                    {{/unless}}
                                    </td>
                                </tr>
                            </script><!-- /#paxRowTpl -->
                        </div><!-- /.participantCollectionViewWrapper -->
                    </div><!-- /.teamMembersWrapper -->

                    <div class="stepContentControls form-actions pullBottomUp">
                        <button class="btn btn-primary btn-inverse fr saveDraftBtn">
                            <cms:contentText key="SAVE_DRAFT" code="quiz.diy.form"/>
                        </button>
                        <span></span>
                        <button class="btn btn-primary nextBtn">
                            <cms:contentText key="NEXT" code="system.button"/> &raquo;
                        </button>
                    </div><!-- /.stepContentControls -->

                </div><!-- /.stepIntroContent -->




                <!-- **************************************************************
                    MATERIALS
                 ***************************************************************** -->
                <div id="quizEditTabMaterialsView" class="stepMaterialsContent stepContent" style="display:none">

                    <div id="activeMaterialMask" class="quizMask" style="display:none">
                    </div>

                    <h4><cms:contentText key="COURSE_MATERIALS" code="quiz.diy.form" /> <span class="optional"><cms:contentText key="OPTIONAL" code="system.common.labels" /></span></h4>

                    <div class="container-splitter with-splitter-styles materialWrapper">

                        <div class="materials">

                            <div class="materialItem headerDescriptions" style="display:none">
                                <div class="materialHeader materialPage">
                                    <cms:contentText key="PAGE" code="quiz.diy.form" />
                                </div>
                                <div class="materialHeader materialType">
                                    <cms:contentText key="TYPE" code="quiz.diy.form" />
                                </div>
                                <div class="materialHeader materialSummary">
                                    <cms:contentText key="TEXT" code="quiz.diy.form" />
                                </div>
                                <div class="materialHeader materialEdit">
                                    <cms:contentText key="EDIT" code="quiz.diy.form" />
                                </div>
                                <div class="materialHeader materialRemove">
                                    <cms:contentText key="REMOVE" code="quiz.diy.form" />
                                </div>
                            </div><!-- /.materialItem.headerDescriptions -->

                            <div class="materialList">
                                <!-- dynamic JS -->
                            </div>

                            <div class="materialRemoveDialog" style="display:none">
                                <p>
                                    <i class="icon-question"></i>
                                    <b><cms:contentText key="REMOVE_MATERIALS" code="quiz.diy.form"/></b>
                                </p>
                                <p>
                                    <cms:contentText key="ALL_MATERIALS_LOST" code="quiz.diy.form"/>
                                </p>
                                <p class="tc">
                                    <a class="btn btn-small confirmBtn"><cms:contentText key="YES" code="quiz.diy.form"/></a>
                                    <a class="btn btn-small cancelBtn closeTip"><cms:contentText key="NO" code="quiz.diy.form"/></a>
                                </p>
                            </div><!-- /.materialRemoveDialog -->

                            <div class="materialItem noMaterials text-error" style="display:none">
                                <i class="icon-warning-triangle"></i>
                                <cms:contentText key="NO_MATERIAL_ADDED" code="quiz.diy.form"/>
                            </div><!-- /.noMaterials -->

                            <div class="newMaterial">
                                <!-- dynamic JS -->
                            </div>
                            <div class="alert alert-info"><i class="icon-warning-triangle"></i> <cms:contentText key="NOTE_COURSE_MATERIAL" code="quiz.diy.form"/></div>
                        </div><!-- /.materials -->

                        <!-- client side template for each partner in the table -->
                        <script id="materialItemTpl" type="text/x-handlebars-template">
                        <div class="materialItem" data-cid="{{cid}}" data-material-id="{{id}}">

                            {{#if isNew}}
                                <h5 class="newMaterialMsg">
                                    <i class="icon-plus-circle"></i>
                                    <cms:contentText key="NEW_COURSE_MATERIAL" code="quiz.diy.form"/>
                                </h5>


                            {{else}}
                            <div class="materialHeaders">
                                <div class="materialHeader materialPage">{{pageNumber}}</div>
                                <div class="materialHeader materialType {{type}}Type">
                                    <i class="icon-file-image imageIcon"></i>
                                    <i class="icon-file textIcon"></i>
                                    <i class="icon-film-1 videoIcon"></i>
                                    <i class="icon-file-pdf pdfIcon"></i>
                                </div>
                                <div class="materialHeader materialSummary">{{{text}}}</div>
                                <div class="materialHeader materialEdit"><i class="icon-pencil2"></i></div>
                                <div class="materialHeader materialRemove {{#unless isActive}}isSaved{{/unless}}"><i class="icon-trash removeControl"></i></div>
                            </div>
                            {{/if}}

                            <div class="materialTypeWrapper">
                            <p><cms:contentText key="ADD_COURSE_MATERIAL" code="quiz.diy.form"/></p>
                                <cms:contentText key="FILE_TYPE" code="quiz.diy.form"/>
                                <div class="btn-group materialTypes">
                                    <button class="btn btn-mini" data-file-type="text">
                                        <i class="icon-check on"></i>
                                        <!--i class="icon-stop off"></i-->
                                        <cms:contentText key="TEXT" code="quiz.diy.form"/>
                                    </button>
                                    <button class="btn btn-mini" data-file-type="image">
                                        <i class="icon-check on"></i>
                                        <!--i class="icon-stop off"></i-->
                                        <cms:contentText key="IMAGE" code="quiz.diy.form"/>
                                    </button>
                                    <button class="btn btn-mini" data-file-type="video">
                                        <i class="icon-check on"></i>
                                        <!--i class="icon-stop off"></i-->
                                        <cms:contentText key="VIDEO" code="quiz.diy.form"/>
                                    </button>
                                    <button class="btn btn-mini" data-file-type="pdf">
                                        <i class="icon-check on"></i>
                                        <!--i class="icon-stop off"></i-->
                                        <cms:contentText key="PDF" code="quiz.diy.form"/>
                                    </button>
                                </div>
                            </div>

                            <div class="materialTypeDependantWrapper" style="display:none">


                                <div class="imageTypeWrapper typeContentWrapper" style="display:none">
                                    <div class="uploadedImageWrapper" style="display:none;">
                                        <!-- dynamic JS -->
                                    </div>
                                    <div class="emptyImageWrapper" style="display:none;">
                                        <i class="icon-file-image"></i>
                                        <cms:contentText key="NO_IMAGE_UPLOADED" code="quiz.diy.form"/>
                                    </div>

                                    <div class="fileInputWrapper">
                                        <input type="file" name="materialFile" class="imageFileInput uploaderFileInput">
                                        <button class="btn btn-primary btn-small uploadImageBtn">
                                            <i class="icon-upload-1"></i>
                                            <span class="noImageMsg imageBtnMsg" style="display:none"><cms:contentText key="UPLOAD_IMAGE" code="quiz.diy.form"/></span>
                                            <span class="hasImageMsg imageBtnMsg" style="display:none"><cms:contentText key="CHANGE_IMAGE" code="quiz.diy.form"/></span>
                                        </button>
										<p class="muted"><cms:contentTemplateText key="PHOTO_UPLOAD_INSTRUCTIONS" code="purl.contributor" args="${beacon:systemVarInteger('system.image.upload.size.limit')}" /></p>
                                        <span class="label label-important uploadError" style="display:none">
                                            <cms:contentText key="UPLOAD_ERROR" code="quiz.diy.form"/>: <span><!-- js dyn error text --></span>
                                        </span>
                                    </div>

                                    <div class="uploadingIndicator" style="display:none">
                                        <div class="uploadingSpinner"></div>
                                        <cms:contentText key="UPLOADING" code="quiz.diy.form"/>
                                        <!--
                                        <div class="progress progress-striped active" style="display:none">
                                            <div class="bar" style="width:0%"></div>
                                        </div>
                                        -->
                                    </div>

                                </div>


                                <div class="videoTypeWrapper typeContentWrapper" style="display:none">
                                    <label><cms:contentText key="VIDEO_URL" code="quiz.diy.form"/></label>
                                    <input type="text" class="videoUrlInput">
                                </div>


                                <div class="pdfTypeWrapper typeContentWrapper" style="display:none">

                                    <div class="pdfItems"><!-- dynamic JS --></div>
                                    <!--subTpl.pdfItem=
                                    <div class="pdfItem" data-pdf-id="{{id}}">
                                        <i class="icon-file-pdf btn-export-pdf"></i>
                                        <span class="pdfName">{{originalFilename}}</span>
                                        <span class="pdfTitle"> <cms:contentText key="PDF_NAME" code="quiz.diy.form"/></span>
                                        <input type="text" maxlength="100" class="pdfTitleInput" placeholder="<cms:contentText key="PDF_TITLE" code="quiz.diy.form"/>" value="{{name}}">
                                        <a href="#" class="pdfRemoveBtn">
                                            <i class="icon-trash pdfRemoveBtn"></i>
                                        </a>
                                    </div>
                                    subTpl-->

                                    <div class="fileInputWrapper">
                                        <input type="file" id="pdfFileInput" name="materialFile" class="pdfFileInput uploaderFileInput">
                                        <button class="btn btn-primary btn-small uploadPdfBtn">
                                            <i class="icon-upload-1"></i>
                                            <span class="noPdfsMsg pdfBtnMsg" style="display:none"><cms:contentText key="UPLOAD_PDF" code="quiz.diy.form"/></span>
                                            <span class="hasPdfsMsg pdfBtnMsg" style="display:none"><cms:contentText key="UPLOAD_ANOTHER_PDF" code="quiz.diy.form"/></span>
                                        </button>
                                        <span class="label label-important uploadError" style="display:none">
                                            <cms:contentText key="UPLOAD_ERROR" code="quiz.diy.form"/>: <span><!-- js dyn error text --></span>
                                        </span>
                                    </div>

                                    <div class="uploadingIndicator" style="display:none">
                                        <div class="uploadingSpinner"></div>
                                        Uploading<!--TODO: cms key-->
                                        <div class="progress progress-striped active" style="display:none">
                                            <div class="bar" style="width:0%"></div>
                                        </div>
                                    </div>

                                </div>

                                <!-- visible for all types of material -->
                                <div class="materialTextWrapper">
                                    <cms:contentText key="TEXT" code="quiz.diy.form"/>: <br>
                                    <textarea class="materialText richtext" data-max-chars="1000"></textarea>
                                </div>

                                <div class="materialItemActions">
                                    {{#if isNew}}
                                    <button class="btn btn-primary btn-small saveNewMaterialBtn">
                                        <cms:contentText key="SAVE_COURSE_MATERIAL" code="quiz.diy.form"/>
                                    </button>
                                    <button class="btn btn-small cancelMaterialBtn">
                                        <cms:contentText key="CANCEL" code="system.button" />
                                    </button>
                                    {{else}}
                                    <button class="btn btn-primary btn-small saveMaterialBtn">
                                        <cms:contentText key="SAVE" code="system.button" />
                                    </button>
                                    <button class="btn btn-small cancelMaterialBtn">
                                        <cms:contentText key="CANCEL" code="system.button" />
                                    </button>
                                    {{/if}}
                                </div>

                            </div><!-- /.materialTypeDependantWrapper -->

                        </div><!-- /.materialItem -->
                        </script><!-- /#materialItemTpl -->

                    </div>

                    <div class="stepContentControls form-actions pullBottomUp">
                        <button class="btn btn-primary btn-inverse fr saveDraftBtn">
                            <cms:contentText key="SAVE_DRAFT" code="quiz.diy.form"/>
                        </button>
                        <span></span>
                        <button class="btn backBtn">
                            &laquo; <cms:contentText key="BACK" code="system.button" />
                        </button>
                        <button class="btn btn-primary nextBtn">
                            <cms:contentText key="NEXT" code="system.button" /> &raquo;
                        </button>
                    </div><!-- /.stepContentControls -->

                </div><!-- /.stepMaterialsContent -->





                <!-- **************************************************************
                    QUESTIONS
                 ***************************************************************** -->
                <div id="quizEditTabQuestionsView" class="stepQuestionsContent stepContent" style="display:none">


                    <div id="activeQuestionMask" class="quizMask" style="display:none">
                    </div>

                    <h4><cms:contentText key="QUESTIONS_AND_ANSWERS" code="quiz.diy.form"/></h4>

                    <div class="container-splitter with-splitter-styles questionWrapper">

                        <div class="questions">

                            <div class="questionItem headerDescriptions" style="display:none">
                                <div class="questionHeader questionQuestion">
                                    <cms:contentText key="QUESTION" code="quiz.diy.form"/>
                                </div>
                                <div class="questionHeader questionEdit">
                                    <cms:contentText key="EDIT" code="quiz.diy.form"/>
                                </div>
                                <div class="questionHeader questionRemove">
                                    <cms:contentText key="REMOVE" code="quiz.diy.form"/>
                                </div>
                            </div><!-- /.materialItem.headerDescriptions -->

                            <div class="questionList">
                                <!-- dynamic JS -->
                            </div>

                            <div class="questionRemoveDialog" style="display:none">
                                <p>
                                    <i class="icon-question"></i>
                                    <b><cms:contentText key="REMOVE_QUESTION" code="quiz.diy.form"/></b>
                                </p>
                                <p>
                                    <cms:contentText key="ALL_QUESTION_DATA_LOST" code="quiz.diy.form"/>
                                </p>
                                <p class="tc">
                                    <a class="btn btn-small confirmBtn"><cms:contentText key="YES" code="quiz.diy.form"/></a>
                                    <a class="btn btn-small cancelBtn closeTip"><cms:contentText key="NO" code="quiz.diy.form"/></a>
                                </p>
                            </div><!-- /.questionRemoveDialog -->

                            <div class="answerRemoveDialog" style="display:none">
                                <p>
                                    <b><cms:contentText key="REMOVE_ANSWER" code="quiz.diy.form"/></b>
                                </p>
                                <p>
                                     <cms:contentText key="ALL_QUESTION_DATA_LOST" code="quiz.diy.form"/>
                                </p>
                                <p class="tc">
                                    <a class="btn btn-small confirmBtn"><cms:contentText key="YES" code="quiz.diy.form"/></a>
                                    <a class="btn btn-small cancelBtn closeTip"><cms:contentText key="NO" code="quiz.diy.form"/></a>
                                </p>
                            </div><!-- /.answerRemoveDialog -->

                            <div class="questionItem noQuestions text-error" style="display:none">
                                <i class="icon-warning-triangle"></i>
                                <cms:contentText key="NO_QUESTIONS_ADDED" code="quiz.diy.form"/>
                            </div><!-- /.noQuestions -->

                            <div class="newQuestion">
                                <!-- dynamic JS -->
                            </div>
                            <div class="alert alert-info"><i class="icon-warning-triangle"></i> <cms:contentText key="NOTE_QUESTIONS" code="quiz.diy.form"/></div>

                        </div><!-- /.questions -->

                        <!-- client side template for each question answer -->
                        <script id="questionAnswerTpl" type="text/x-handlebars-template">
                        <div class="answerItem{{#if isCorrect}} correctAnswer{{/if}}" data-answer-id="{{id}}">
                            <span class="answerControls">
                                <i class="icon-check-square correctIndicator correctToggleBtn"></i>
                                <i class="icon-stop incorrectIndicator correctToggleBtn"></i>
                            </span>
                            <span class="answerNumber">{{number}}</span>
                            <input type="text" class="answerInput" placeholder="<cms:contentText key="ANSWER_TEXT" code="quiz.diy.form"/>" value="{{text}}" tabindex="1">
                            <a href="#" class="answerRemoveBtn" tabindex="2">
                                <i class="icon-trash"></i>
                            </a>
                        </div>
                        </script><!-- /#questionAnswerTpl -->

                        <!-- client side template for each question item -->
                        <script id="questionItemTpl" type="text/x-handlebars-template">
                        <div class="questionItem" data-cid="{{cid}}" data-question-id="{{id}}">

                            {{#if isNew}}
                                <h5 class="newQuestionMsg">
                                    <i class="icon-plus-circle"></i>
                                    <cms:contentText key="NEW_QUESTION" code="quiz.diy.form"/>
                                </h5>
                            {{else}}

                            <div class="questionHeaders">
                                <div class="questionHeader questionQuestion">
                                    <span class="questionNumber">{{number}}</span>
                                    {{text}}
                                </div>
                                <div class="questionHeader questionEdit"><i class="icon-pencil2"></i></div>
                                <div class="questionHeader questionRemove {{#unless isActive}}isSaved{{/unless}}"><i class="icon-trash removeControl"></i></div>
                            </div>
                            {{/if}}

                            {{#if isNew}}
                            <div class="questionWrapper">
                                <textarea class="questionInput" placeholder="<cms:contentText key="QUESTION_TEXT" code="quiz.diy.form"/>" value="{{text}}" maxlength="500" cols="2" tabindex="1">{{text}}</textarea>
                            </div><!-- /.questionWrapper -->
                            {{/if}}

                            <div class="questionDetails" style="display:none">
                                {{#unless isNew}}
                                <div class="questionWrapper">
                                    <h4>Question {{number}}</h4>
                                    <textarea class="questionInput"  placeholder="<cms:contentText key="QUESTION_TEXT" code="quiz.diy.form"/>" value="{{text}}" maxlength="500" cols="2" tabindex="1">{{text}}</textarea>
                                </div><!-- /.questionWrapper -->
                                {{/unless}}

                                <h4><cms:contentText key="ANSWERS" code="quiz.diy.form"/></h4>
                                <div class="answerList" data-msg-none="<cms:contentText key="NO_ANSWERS_ADDED" code="quiz.diy.form"/>">
                                    <!-- after this item renders, the answer list is rendered here -->
                                </div><!-- /.answerList -->

                                <div class="alert alert-info"><i class="icon-warning-triangle"></i> <cms:contentText key="NOTE_ANSWERS" code="quiz.diy.form"/></div>

                                <div class="addAnswerWrapper">
                                    <button href="#" class="btn btn-primary btn-mini addAnswerBtn" tabindex="1">
                                        <cms:contentText key="ADD_ANOTHER_ANSWER" code="quiz.diy.form"/> <i class="icon-plus"></i>
                                    </button>
                                </div>

                                <div class="questionItemActions">
                                    {{#if isNew}}
                                    <button class="btn btn-primary btn-small saveNewQuestionBtn" tabindex="1">
                                        <cms:contentText key="SAVE_QUESTION" code="quiz.diy.form"/>
                                    </button>
                                    <button class="btn btn-small cancelQuestionBtn" tabindex="2">
                                        <cms:contentText key="CANCEL" code="system.button"/>
                                    </button>
                                    {{else}}
                                    <button class="btn btn-primary btn-small saveQuestionBtn" tabindex="1">
                                        <cms:contentText key="SAVE" code="system.button"/>
                                    </button>
                                    <button class="btn btn-small cancelQuestionBtn" tabindex="2">
                                        <cms:contentText key="CANCEL" code="system.button"/>
                                    </button>
                                    {{/if}}
                                </div>

                            </div><!-- /.questionDetails -->

                        </div><!-- /.questionItem -->
                        </script><!-- /#questionItemTpl -->

                    </div><!-- /.questionWrapper -->

                    <div class="stepContentControls form-actions pullBottomUp">
                        <button class="btn btn-primary btn-inverse fr saveDraftBtn">
                            <cms:contentText key="SAVE_DRAFT" code="quiz.diy.form"/>
                        </button>
                        <span></span>
                        <button class="btn backBtn">
                            &laquo; <cms:contentText key="BACK" code="system.button"/>
                        </button>
                        <button class="btn btn-primary nextBtn">
                            <cms:contentText key="NEXT" code="system.button"/> &raquo;
                        </button>
                    </div><!-- /.stepContentControls -->

                </div><!-- /.stepQuestionsContent -->






                <!-- **************************************************************
                    RESULTS
                ***************************************************************** -->
                <div id="quizEditTabResultsView" class="stepResultsContent stepContent" style="display:none">

                    <div class="control-group">
                        <label class="control-label" for="passingScore">
                            <Strong><cms:contentText key="PASSING_SCORE" code="quiz.diy.form"/></Strong>
                            <span class="help-block"><cms:contentText key="NO_CORRECT_ANS_NEEDED" code="quiz.diy.form"/>:</span>
                        </label>
                        <div class="controls">
                            <input class="passingScoreInput" type="text" id="passingScore" data-model-key="passingScore">
                            <cms:contentText key="OUT_OF" code="quiz.diy.form"/>
                            <span class="numQuestionsTxt"></span>
                            <cms:contentText key="QUESTIONS" code="quiz.diy.form"/>
                        </div>
                    </div>

                    <div class="control-group">
                        <Strong><cms:contentText key="NO_OF_ATTEMPTS" code="quiz.diy.form"/></Strong>
                        <i class="icon-question pageView_help"
                            data-help-content="<cms:contentText key="NO_OF_ATTEMPTS_TOOLTIP" code="quiz.diy.form"/>"></i>
                        <div class="allowedAttemptsWrapper">
                            <label class="radio allowedAttemptsInputLabel">
                                <input type="radio" class="isAttemptsLimitInput isAttemptsLimitTrue" data-model-key="isAttemptsLimit" name="isAttemptsLimit" value="true" checked="checked">
                                <cms:contentText key="USE_SPECIFIC_NUMBER" code="quiz.diy.form"/>:
                            </label>
                            <label class="allowedAttemptsInputLabel">
                                <input class="allowedAttemptsInput" type="text" data-model-key="allowedAttempts">
                                <cms:contentText key="ATTEMPTS" code="quiz.diy.form"/>
                            </label>
                        </div>
                        <label class="radio">
                            <input type="radio" class="isAttemptsLimitInput" data-model-key="isAttemptsLimit" name="isAttemptsLimit" value="false">
                            <cms:contentText key="UNLIMITED" code="quiz.diy.form"/>
                        </label>
                    </div>

                    <div class="control-group">
                        <Strong><cms:contentText key="DISPLAY_QUESTION_RANDOM" code="quiz.diy.form"/></Strong>
                        <i class="icon-question pageView_help"
                            data-help-content="<cms:contentText key="RANDOM_TOOLTIP" code="quiz.diy.form"/>"></i>
                        <label class="radio">
                            <input type="radio" class="isRandomQuestionOrderInput" data-model-key="isRandomQuestionOrder" name="isRandomQuestionOrder" value="true">
                            <cms:contentText key="YES" code="quiz.diy.form"/>
                        </label>
                        <label class="radio">
                            <input type="radio" class="isRandomQuestionOrderInput" data-model-key="isRandomQuestionOrder" name="isRandomQuestionOrder" value="false">
                            <cms:contentText key="NO" code="quiz.diy.form"/>
                        </label>
                    </div>


                    <div class="control-group badgesSelector"
                        data-no-badge-json='{"id":null,"name":"<cms:contentText key="NO_BADGE" code="quiz.diy.form"/>","iconClass":"icon-ban","contClass":"noBadgeContent"}'>
                        <h5><cms:contentText key="SELECT_BADGE" code="quiz.diy.form"/></h5>

                        <div class="btn-group">
                            <a class="btn dropdown-toggle badgeBtn" data-toggle="dropdown" href="#">
                                <!-- dyn -->
                            </a>
                            <ul class="dropdown-menu badgeItems">
                                <!-- dropdown menu links -->
                            </ul>
                        </div>
                    </div><!-- /.badgesSelector -->

                    <script id="badgeBtnTpl" type="text/x-handlebars-template">
                        <span class="badgeBtnContent {{contClass}}">
                            {{#if img}}<img src="{{img}}" alt="{{name}}">{{/if}}
                            {{#if iconClass}}<i class="{{iconClass}}"></i>{{/if}}
                            <span>{{name}}</span>
                        </span>
                        <i class="icon-arrow-1-down badgeBtnCaret"></i>
                    </script><!-- /#badgeBtnTpl -->

                    <script id="badgeItemTpl" type="text/x-handlebars-template">
                        <li class="badgeItem">
                            <a href="#" data-badge-id="{{id}}">
                                {{#if img}}<img src="{{img}}" alt="{{name}}">{{/if}}
                                {{#if iconClass}}<i class="{{iconClass}}"></i>{{/if}}
                                {{name}}
                            </a>
                        </li>
                    </script><!-- /#badgeItemTpl -->


                    <div class="control-group certificatesSelector"
                        data-no-cert-json='{"id":null,"name":"No Certificate","iconClass":"icon-ban","contClass":"noCertContent"}'>
                        <h5 class="selectCertTitle"><cms:contentText key="SELECT_CERTIFICATE" code="quiz.diy.form"/></h5>
                        <label class="checkbox">
                            <input type="checkbox" class="showCertificateSelector" checked>
                            <cms:contentText key="ADD_CERTIFICATE" code="quiz.diy.form"/>
                        </label>
                        <div class="certificateGuts">
                            <div class="certSelected">
                                <!-- dyn -->
                            </div>
                            <ul class="certItems unstyled">
                                <!-- dyn -->
                            </ul>
                        </div>
                    </div><!-- /.certificatesSelector -->

                    <script id="certSelectedTpl" type="text/x-handlebars-template">
                        <div class="{{contClass}}">
                        {{#if imgLg}}<img src="{{imgLg}}" alt="{{name}}">{{/if}}
                        {{#if iconClass}}<i class="{{iconClass}}"></i>{{/if}}
                        {{#if name}}<div class="certName">{{name}}</div>{{/if}}
                        </div>
                    </script><!-- /#certSelectedTpl -->

                    <script id="certItemTpl" type="text/x-handlebars-template">
                        <li class="certItem" data-cert-id="{{id}}">
                            <img src="{{img}}" alt="{{name}}">
                        </li>
                    </script><!-- /#certItemTpl -->


                    <div class="control-group">
                        <label class="checkbox">
                            <input type="checkbox" class="showNotifyText" data-model-key="isNotifyParticipants"  checked>
                            <Strong><cms:contentText key="NOTIFY_PARTICIPANTS" code="quiz.diy.form"/></Strong> <span class="optional"><cms:contentText key="OPTIONAL" code="system.common.labels" /></span>
                        </label>
                        <div class="notifyTextGuts">
                            <cms:contentText key="TEXT_EMAIL_NOTIFICATION" code="quiz.diy.form"/>
                            <div class="controls validateme"
                                data-validate-flags="maxlength,nonempty"
                                data-validate-fail-msgs='{"maxlength" : "<cms:contentText key="INTRO_MAX_EXCEEDED" code="quiz.diy.errors"/>","nonempty":"<cms:contentText key="INTRO_TEXT_REQUIRED" escapeHtml="true" code="quiz.diy.errors"/>"}'
                                data-validate-max-length="2000">
                                <textarea rows="5" data-max-chars="2000" class="richtext notifyText" data-model-key="notifyText"></textarea>
                            </div>
                        </div>
                    </div>


                    <div class="stepContentControls form-actions pullBottomUp">
                        <button class="btn btn-primary btn-inverse fr saveDraftBtn">
                            <cms:contentText key="SAVE_DRAFT" code="quiz.diy.form"/>
                        </button>
                        <span></span>
                        <button class="btn backBtn">
                            &laquo; <cms:contentText key="BACK" code="system.button"/>
                        </button>
                        <button class="btn btn-primary nextBtn">
                            <cms:contentText key="NEXT" code="system.button"/> &raquo;
                        </button>
                    </div><!-- /.stepContentControls -->

                </div><!-- /.stepResultsContent -->




                <!-- **************************************************************
                    PREVIEW
                 ***************************************************************** -->
                <div id="quizEditTabPreviewView" class="stepPreviewContent stepContent" style="display:none">

                    <div class="previewStepBoxesWrapper">

                        <script id="previewStepItemsTpl" type="text/x-handlebars-template">
                        <div class="previewStepBoxWrapper well">
                            <div class="previewStepBoxHeader">
                                <button class="btn btn-primary btn-small editStepBtn" data-step-name="stepIntro">
                                    <cms:contentText key="EDIT" code="system.button"/>
                                    <i class="icon-pencil2"></i>
                                </button>
                                <span class="previewStepBoxTitle">1. <cms:contentText key="QUIZ_INTRODUCTION_PARTICIPANTS" code="quiz.diy.form"/></span>
                            </div>
                            <div class="previewStepBoxContent">

                                {{#if createAsParticipant}}
                                    <div class="row-fluid">
                                        <div class="span3"><div class="previewLabel">Quiz Owner<!--TODO: cms key--></div></div>
                                        <div class="span9">{{createAsParticipant.firstName}} {{createAsParticipant.lastName}}</div>
                                    </div>
                                {{/if}}

                                <div class="row-fluid">
                                    <div class="span3"><div class="previewLabel"><cms:contentText key="QUIZ_NAME" code="quiz.diy.form"/></div></div>
                                    <div class="span9">{{name}}</div>
                                </div>

                                <div class="row-fluid">
                                    <div class="span3"><div class="previewLabel"><cms:contentText key="QUIZ_START_DATE" code="quiz.diy.form"/></div></div>
                                    <div class="span9">{{startDate}}</div>
                                </div>

                                <div class="row-fluid">
                                    <div class="span3"><div class="previewLabel"><cms:contentText key="QUIZ_END_DATE" code="quiz.diy.form"/></div></div>
                                    <div class="span9">{{endDate}}</div>
                                </div>

                                <div class="row-fluid">
                                    <div class="span3"><div class="previewLabel"><cms:contentText key="INTRODUCTION_TEXT" code="quiz.diy.form"/></div></div>
                                    <div class="span9">
                                        {{{introText}}}
                                    </div>
                                </div>

                                <div class="row-fluid">
                                    <div class="span3"><div class="previewLabel"><cms:contentText key="PARTICIPANTS" code="quiz.diy.form"/></div></div>
                                    <div class="span9">
                                        {{#participants}}
                                        <span class="label label-inverse">{{firstName}} {{lastName}}</span>
                                        {{/participants}}
                                    </div>
                                </div>
                            </div>
                        </div><!-- /.previewStepBoxWrapper.well -->

                        <div class="previewStepBoxWrapper well">
                            <div class="previewStepBoxHeader">
                                <button class="btn btn-primary btn-small editStepBtn" data-step-name="stepMaterials">

                                    <cms:contentText key="EDIT" code="system.button"/> <i class="icon-pencil2"></i>
                                </button>
                                <span class="previewStepBoxTitle">2. <cms:contentText key="COURSE_MATERIALS" code="quiz.diy.form"/></span>
                            </div>
                            <div class="previewStepBoxContent">

                                {{#materials}}
                                <div class="row-fluid">
                                    <div class="span12 previewMaterialItem">
                                        <div class="row-fluid">
                                            <div class="span12">
                                                <div class="previewMaterialHeader">
                                                    <cms:contentText key="COURSE_MATERIAL" code="quiz.diy.form"/> {{pageNumber}} <cms:contentText key="OF" code="quiz.diy.form"/> {{../materials.length}}
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row-fluid">
                                            <div class="span3">
                                                <div class="previewMaterialItemContent {{type}}TypeContent">
                                                {{#eq type "text"}}
                                                    <i class="icon-file"></i>
                                                {{/eq}}

                                                {{#eq type "image"}}
                                                    <img src="{{files.0.url}}" alt="{{files.0.name}}">
                                                {{/eq}}

                                                {{#eq type "video"}}
                                                    <a class="btn btn-primary" href="{{files.0.url}}" target="_blank" >

                                                       <cms:contentText key="VISIT_VIDEO_URL" code="quiz.diy.form"/> <i class="icon-film-1"></i>
                                                    </a>
                                                {{/eq}}

                                                {{#eq type "pdf"}}
                                                    {{#files}}
                                                    <ul class="previewPdfFiles unstyled">
                                                        <li>
                                                            <a class="fileLink" href="{{url}}" target="_blank" >
                                                                <i class="icon-file-pdf btn btn-icon invert-btn btn-export-pdf"></i>
                                                                {{name}}
                                                            </a>
                                                        </li>
                                                    </ul>
                                                    {{/files}}
                                                {{/eq}}

                                                </div>
                                            </div>
                                            <div class="span9 previewMaterialItemText">{{{text}}}</div>
                                        </div><!-- /.row-fluid -->
                                    </div><!-- /.previewMaterialItem -->
                                </div><!-- /.row-fluid -->
                                {{/materials}}

                            </div>
                        </div><!-- /.previewStepBoxWrapper.well -->

                        <div class="previewStepBoxWrapper well">
                            <div class="previewStepBoxHeader">
                                <button class="btn btn-primary btn-small editStepBtn" data-step-name="stepQuestions">
                                    <cms:contentText key="EDIT" code="system.button"/>
                                    <i class="icon-pencil2"></i>
                                </button>
                                <span class="previewStepBoxTitle">3. <cms:contentText key="QUESTIONS_AND_ANSWERS" code="quiz.diy.form"/></span>
                            </div>
                            <div class="previewStepBoxContent">

                                {{#questions}}
                                <div class="row-fluid">
                                    <div class="span12 previewQuestionItem">
                                        <div class="row-fluid">
                                            <div class="span12">
                                                <div class="previewQuestionHeader">
                                                    <cms:contentText key="QUESTION" code="quiz.diy.form"/> {{number}} <cms:contentText key="OF" code="quiz.diy.form"/> {{../questions.length}}
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row-fluid">
                                            <div class="span12">
                                                <div class="previewQuestionText">
                                                {{text}}
                                                </div>
                                                <ul class="previewQuestionAnswers unstyled">
                                                {{#answers}}
                                                    <li>
                                                        <div class="isCorrect {{isCorrect}}Bool">
                                                        {{#if isCorrect}}
                                                            <i class="icon-check-square"></i>
                                                        {{else}}
                                                            <i class="icon-stop"></i>
                                                        {{/if}}
                                                        </div>
                                                        <div class="answerText">
                                                            {{_letterOrder}}) {{text}}
                                                        </div>
                                                    </li>
                                                {{/answers}}
                                                </ul>
                                            </div><!-- /.span12 -->
                                        </div><!-- /.row-fluid -->
                                    </div><!-- /.previewQuestionItem -->
                                </div><!-- /.row-fluid -->
                                {{/questions}}

                            </div>
                        </div><!-- /.previewStepBoxWrapper.well -->

                        <div class="previewStepBoxWrapper well">
                            <div class="previewStepBoxHeader">
                                <button class="btn btn-primary btn-small editStepBtn" data-step-name="stepResults">
                                    <cms:contentText key="EDIT" code="system.button"/> <i class="icon-pencil2"></i>
                                </button>
                                <span class="previewStepBoxTitle">4. <cms:contentText key="RESULTS_AND_AWARDS" code="quiz.diy.form"/></span>
                            </div>
                            <div class="previewStepBoxContent">
                                <div class="row-fluid">
                                    <div class="span3"><div class="previewLabel"><cms:contentText key="PASSING_SCORE" code="quiz.diy.form"/></div></div>
                                    <div class="span9">
                                        <cms:contentTemplateText key="SCORE_SUMMARY" code="quiz.diy.form" args="{{passingScore}},{{questions.length}}" delimiter=","/>
                                    </div>
                                </div>

                                <div class="row-fluid">
                                    <div class="span3"><div class="previewLabel"><cms:contentText key="NO_OF_ATTEMPTS" code="quiz.diy.form"/></div></div>
                                    <div class="span9">
                                    {{#if isAttemptsLimit}}
                                        {{allowedAttempts}}
                                    {{else}}
                                        <cms:contentText key="UNLIMITED" code="quiz.diy.form"/>
                                    {{/if}}
                                    </div>
                                </div>

                                <div class="row-fluid">
                                    <div class="span3"><div class="previewLabel"><cms:contentText key="DISPLAY_QUESTION_RANDOM" code="quiz.diy.form"/></div></div>
                                    <div class="span9">
                                    {{#if isRandomQuestionOrder}}
                                        <cms:contentText key="YES" code="quiz.diy.form"/>
                                    {{else}}
                                        <cms:contentText key="NO" code="quiz.diy.form"/>
                                    {{/if}}
                                    </div>
                                </div>

                                <div class="row-fluid">
                                    <div class="span3"><div class="previewLabel"><cms:contentText key="NOTIFY_PARTICIPANTS" code="quiz.diy.form"/></div></div>
                                    <div class="span9">
                                    {{#if isNotifyParticipants}}
                                        <cms:contentText key="YES" code="quiz.diy.form"/>
                                    {{else}}
                                        <cms:contentText key="NO" code="quiz.diy.form"/>
                                    {{/if}}
                                    </div>
                                </div>

                                {{#if isNotifyParticipants}}
                                <div class="row-fluid">
                                    <div class="span3"><div class="previewLabel"><cms:contentText key="TEXT_EMAIL_NOTIFICATION" code="quiz.diy.form"/></div></div>
                                    <div class="span9">
                                        {{{notifyText}}}
                                    </div>
                                </div>
                                {{/if}}

                                <div class="row-fluid">
                                    <div class="span12">
                                        <div class="previewBadgeCertLiner">
                                        {{#if _badge}}
                                            <h5><cms:contentText key="BADGE" code="quiz.diy.form"/></h5>
                                            <img src="{{_badge.img}}" alt="{{_badge.name}}">
                                            <span>{{_badge.name}}</span>
                                        {{/if}}

                                        {{#if _cert}}
                                            <h5><cms:contentText key="CERTIFICATE" code="quiz.diy.form"/></h5>
                                            <img src="{{_cert.imgLg}}" alt="{{_cert.name}}">
                                        {{/if}}
                                        </div>
                                    </div>
                                </div>

                            </div>
                        </div><!-- /.previewStepBoxWrapper.well -->

                        </script><!-- /#previewStepItemsTpl -->

                    </div><!-- /.reveiewStepBoxesWrapper -->

                    <div class="stepContentControls form-actions pullBottomUp">
                        <button class="btn backBtn">
                            &laquo; <cms:contentText key="BACK" code="system.button" />
                        </button>
                        <a href="#" class="btn btn-primary submitBtn">
                            <cms:contentText key="SUBMIT" code="system.button" />
                        </a>
                    </div><!-- /.stepContentControls -->

                </div><!-- /.stepPreviewContent -->




                <!-- VALIDATION MSGS - informational tooltip for validation -->
                <div class="errorTipWrapper" style="display:none">
                    <div class="errorTip">

                        <!-- display for validateme generic errors -->
                        <div class="errorMsg msgGenericError">
                            <i class="icon-warning-triangle"></i>
                            <cms:contentText key="PLEASE_CORRECT" code="quiz.diy.errors" />
                        </div>

                        <!-- save draft - quiz name not set -->
                        <div class="errorMsg msgSaveDraftNoName">
                            <i class="icon-warning-triangle"></i>
                            <cms:contentText key="QUIZ_NAME_REQUIRED" code="quiz.diy.errors" />
                        </div>

                        <!-- Intro errors -->
                        <div class="errorMsg msgStartDateTooEarly">
                            <i class="icon-warning-triangle"></i>
                            <cms:contentText key="START_DATE_LATER" code="quiz.diy.errors" />
                        </div>
                        <div class="errorMsg msgEndDateTooEarly">
                            <i class="icon-warning-triangle"></i>
                            <cms:contentText key="END_DATE_LATER" code="quiz.diy.errors" />
                        </div>
                        <div class="errorMsg msgMustHavePax">
                            <i class="icon-warning-triangle"></i>
                            <cms:contentText key="ADD_PARTICIPANTS" code="quiz.diy.errors" />
                        </div>
                        <div class="errorMsg msgMustHaveOwnerPax">
                            <i class="icon-warning-triangle"></i>
                            <cms:contentText key="OWNER_REQUIRED" code="quiz.diy.errors" />
                        </div>
                        <div class="errorMsg msgQuizNameNotUnique">
                            <i class="icon-warning-triangle"></i>
                            <cms:contentText key="DUPLICATE_QUIZ_NAME" code="quiz.diy.errors" />
                        </div>

                        <!-- Materials errors -->
                        <div class="errorMsg msgMaterialMustHaveOne">
                            <i class="icon-warning-triangle"></i>
                            <cms:contentText key="ADD_MATERIAL" code="quiz.diy.errors" />
                        </div>
                        <div class="errorMsg msgMaterialModified">
                            <i class="icon-warning-triangle"></i>
                            <cms:contentText key="SAVE_COURSE_MATERIAL" code="quiz.diy.errors" />
                        </div>
                        <div class="errorMsg msgMaterialMustHaveText">
                            <i class="icon-warning-triangle"></i>
                            <cms:contentText key="TEXT_MATERIAL_REQUIRED" code="quiz.diy.errors" />
                        </div>
                        <div class="errorMsg msgMaterialMustHaveImage">
                            <i class="icon-warning-triangle"></i>
                            <cms:contentText key="UPLOAD_IMAGE" code="quiz.diy.errors" />
                        </div>
                        <div class="errorMsg msgMaterialMustHavePdf">
                            <i class="icon-warning-triangle"></i>
                            <cms:contentText key="UPLOAD_PDF" code="quiz.diy.errors" />
                        </div>
                        <div class="errorMsg msgMaterialTooManyPdfs">
                            <i class="icon-warning-triangle"></i>
                            <cms:contentText key="TOO_MANY_PDFS" code="quiz.diy.errors" />
                        </div>
                        <div class="errorMsg msgMaterialMustHavePdfTitle">
                            <i class="icon-warning-triangle"></i>
                            <cms:contentText key="PDF_TITLE_REQUIRED" code="quiz.diy.errors" />
                        </div>
                        <div class="errorMsg msgMaterialVideoUrlFormat">
                            <i class="icon-warning-triangle"></i>
                            <cms:contentText key="VIDEO_URL_VALID" code="quiz.diy.errors" />
                        </div>
                        <div class="errorMsg msgMaterialsTooMany">
                            <i class="icon-warning-triangle"></i>
                            <cms:contentText key="TOO_MANY_MATERIALS" code="quiz.diy.errors" />
                        </div>
                        <div class="errorMsg msgFileType_image">
                            <i class="icon-warning-triangle"></i>
                            <cms:contentText key="INVALID_IMAGE_TYPE" code="quiz.diy.errors" />
                        </div>
                        <div class="errorMsg msgFileType_pdf">
                            <i class="icon-warning-triangle"></i>
                            <cms:contentText key="INVALID_PDF_TYPE" code="quiz.diy.errors" />
                        </div>
                        <div class="errorMsg msgMaterialTooManyChars">
                            <i class="icon-warning-triangle"></i>
                            <cms:contentText key="TOO_MANY_CHARACTERS" code="quiz.diy.errors" />
                        </div>

                        <!-- Questions errors -->
                        <div class="errorMsg msgQuestionModified">
                            <i class="icon-warning-triangle"></i>
                            <cms:contentText key="SAVE_ACTIVE_QUESTION" code="quiz.diy.errors" />
                        </div>
                        <div class="errorMsg msgQuestionMustHaveText">
                            <i class="icon-warning-triangle"></i>
                            <cms:contentText key="QUESTION_TEXT_REQUIRED" code="quiz.diy.errors" />
                        </div>
                        <div class="errorMsg msgQuestionMustHaveTwoAnswers">
                            <i class="icon-warning-triangle"></i>
                            <cms:contentText key="ATLEAST_TWO_ANSWERS_REQ" code="quiz.diy.errors" />
                        </div>
                        <div class="errorMsg msgQuestionMustHaveAnswerText">
                            <i class="icon-warning-triangle"></i>
                            <cms:contentText key="ANSWER_TEXT_REQUIRED" code="quiz.diy.errors" />
                        </div>
                        <div class="errorMsg msgQuestionMustHaveCorrect">
                            <i class="icon-warning-triangle"></i>
                            <cms:contentText key="CORRECT_ANSWER_REQUIRED" code="quiz.diy.errors" />
                        </div>
                        <div class="errorMsg msgQuestionMustHaveOne">
                            <i class="icon-warning-triangle"></i>
                            <cms:contentText key="QUESTION_REQUIRED" code="quiz.diy.errors" />
                        </div>
                        <div class="errorMsg msgQuestionTooManyQuestions">
                            <i class="icon-warning-triangle"></i>
                            <cms:contentText key="TOO_MANY_QUESTIONS" code="quiz.diy.errors" />
                        </div>
                        <div class="errorMsg msgQuestionTooManyAnswers">
                            <i class="icon-warning-triangle"></i>
                            <cms:contentText key="TOO_MANY_ANSWERS" code="quiz.diy.errors" />
                        </div>
                        <div class="errorMsg msgQuestionDuplicateAnswer">
                            <i class="icon-warning-triangle"></i>
                            <cms:contentText key="NOT_UNIQUE_ANSWER" code="quiz.diy.errors" />
                        </div>
                        <div class="errorMsg msgQuestionDuplicateQuestion">
                            <i class="icon-warning-triangle"></i>
                            <cms:contentText key="NOT_UNIQUE_QUESTION" code="quiz.diy.errors" />
                        </div>

                        <!-- Results errors -->
                        <div class="errorMsg msgPassingScoreReq">
                            <i class="icon-warning-triangle"></i>
                            <cms:contentText key="PASSSING_SCORE_REQUIRED" code="quiz.diy.errors" />
                        </div>
                        <div class="errorMsg msgPassingScoreRange">
                            <i class="icon-warning-triangle"></i>
                            <cms:contentText key="SCORE_LESS_QUESTIONS" code="quiz.diy.errors" />
                        </div>

                        <div class="errorMsg msgAttemptLimitSelectReq">
                            <i class="icon-warning-triangle"></i>
                            <cms:contentText key="SELECTION_REQUIRED" code="quiz.diy.errors" />
                        </div>
                        <div class="errorMsg msgAttemptLimitReq">
                            <i class="icon-warning-triangle"></i>
                            <cms:contentText key="NO_OF_ATTEMPTS_REQUIRED" code="quiz.diy.errors" />
                        </div>

                        <div class="errorMsg msgIsRandomSelectReq">
                            <i class="icon-warning-triangle"></i>
                            <cms:contentText key="SELECTION_REQUIRED" code="quiz.diy.errors" />
                        </div>

                        <div class="errorMsg msgNotifyTextReq">
                            <i class="icon-warning-triangle"></i>
                            <cms:contentText key="NOTIFY_TEXT_REQUIRED" code="quiz.diy.errors" />
                        </div>

                    </div><!-- /.errorTip -->
                </div><!-- /.errorTipWrapper -->


                <!-- modal to catch errors from server on submit OR save draft -->
                <div class="modal hide fade quizErrorsModal">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal"><i class="icon-close"></i></button>
                        <h3>
                            <i class="icon-warning-triangle"></i>
                            <cms:contentText key="ERRORS_ENCOUNTERED" code="quiz.diy.errors" />
                        </h3>
                    </div>
                    <div class="modal-body">
                        <ul class="errorsList">
                            <!-- dynamic -->
                        </ul>
                    </div>
                    <div class="modal-footer">
                        <a href="#" class="btn" data-dismiss="modal"><cms:contentText key="CLOSE" code="system.button"/></a>
                    </div>
                </div><!-- /.quizErrorsModal -->

            </div><!-- /.wizardTabsContent -->

        </div><!-- /.span12 -->
    </div><!-- /.row-fluid -->

</div><!-- /#quizPageEditView -->

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
    $(document).ready(function() {

        var quizJson =  ${diyQuizForm.initializationJson};

        // JAVA NOTE: set this to whatever is apropo
        G5.props.URL_JSON_QUIZ_MATERIAL_UPLOAD = G5.props.URL_ROOT+'promotion/diyQuizSave.do?method=uploadLearningObject';

        // JAVA NOTE: set this to the URL to post the quiz data to
        G5.props.URL_JSON_QUIZ_EDIT_SAVE = G5.props.URL_ROOT+'promotion/diyQuizSave.do';

        // JAVA NOTE: set this to a URL providing team member's for a particular owner
        G5.props.URL_JSON_QUIZ_TEAM_MEMBERS = G5.props.URL_ROOT+'promotion/diyQuizMaintain.do?method=loadManagerParticipants';

        // Ajax call to check for duplicate quiz name
        G5.props.URL_JSON_QUIZ_CHECK_NAME = G5.props.URL_ROOT+'promotion/diyQuizSave.do?method=checkQuizName';

        //Mini Profile PopUp JSON
        G5.props.URL_JSON_PARTICIPANT_INFO = G5.props.URL_ROOT+'participantPublicProfile.do?method=populatePax';

        // Recognition wizard info
        G5.props.URL_JSON_EZ_RECOGNITION_MEMBER_INFO = G5.props.URL_ROOT+'/recognitionWizard/memberInfo.do';


        //Mini Profile PopUp Follow Unfollow Pax JSON
        G5.props.URL_JSON_PARTICIPANT_FOLLOW = G5.props.URL_ROOT+'publicRecognitionFollowUnFollowPax.do?method=followUnFollowPax';

        G5.props.spellcheckerUrl = "${pageContext.request.contextPath}/spellchecker/jazzySpellCheck.do";


        // JAVA NOTE: you should only have to i18n the "wtName" field in
        //            this set, no need to create a class for this or anything
        // json for WizardTabs -- easier to read/maintain than HTML setup
        var tabsJson = [
            {
                "id" : 1,
                "name" : "stepIntro",
                "isActive" : false,
                "state" : "unlocked",
                "contentSel" : ".wizardTabsContent .stepIntroContent",
                "wtNumber" : "",
                "wtName" : "<cms:contentText key="QUIZ_INTRODUCTION_PARTICIPANTS" code="quiz.diy.form" />",
                "hideDeedle" : false
            },
            {
                "id" : 2,
                "name" : "stepMaterials",
                "isActive" : false,
                "state" : "locked",
                "contentSel" : ".wizardTabsContent .stepMaterialsContent",
                "wtNumber" : "",
                "wtName" : "<cms:contentText key="QUIZ_MATERIALS" code="quiz.diy.form"/>",
                "hideDeedle" : false
            },
            {
                "id" : 3,
                "name" : "stepQuestions",
                "isActive" : false,
                "state" : "locked",
                "contentSel" : ".wizardTabsContent .stepQuestionsContent",
                "wtNumber" : "",
                "wtName" : "<cms:contentText key="QUESTIONS_AND_ANSWERS" code="quiz.diy.form"/>",
                "hideDeedle" : false
            },
            {
                "id" : 4,
                "name" : "stepResults",
                "isActive" : false,
                "state" : "locked",
                "contentSel" : ".wizardTabsContent .stepResultsContent",
                "wtNumber" : "",
                "wtName" : "<cms:contentText key="RESULTS_AND_AWARDS" code="quiz.diy.form"/>",
                "hideDeedle" : false
            },
            {
                "id" : 5,
                "name" : "stepPreview",
                "isActive" : false,
                "state" : "locked",
                "contentSel" : ".wizardTabsContent .stepPreviewContent",
                "wtNumber" : "",
                "wtName" : "<cms:contentText key="PREVIEW_QUIZ" code="quiz.diy.form"/>",
                "hideDeedle" : false
            }
        ];

        //attach the view to an existing DOM element
        window.quizPageEditView = new QuizPageEditView({
            el:$('#quizPageEditView'),
            quizJson: quizJson, // quiz json to populate model
            pageNav : {
                back : {
                    text : '<cms:contentText key="BACK" code="system.button" />',
                    <c:choose>
                        <c:when test="${source ne null && source eq 'admin' }">
                            url : '<%= RequestUtils.getBaseURI(request)%>/promotion/diyQuizList.do'
                        </c:when>
                        <c:otherwise>
                            url : '<%= RequestUtils.getBaseURI(request)%>/quiz/diyQuizManage.do?method=manage'
                        </c:otherwise>
                    </c:choose>
                },
                home : {
                    text : '<cms:contentText key="HOME" code="system.general" />',
                    url : '<%= RequestUtils.getBaseURI(request)%>/homePage.do'
                }
            },
            tabsJson : tabsJson,
            <c:choose>
                <c:when test="${mode ne null && mode eq 'edit' }">
                    pageTitle : '<cms:contentText key="EDIT_QUIZ_TITLE" code="quiz.diy.form"/>'
                </c:when>
                <c:otherwise>
                    pageTitle : '<cms:contentText key="CREATE_QUIZ_TITLE" code="quiz.diy.form"/>'
                </c:otherwise>
            </c:choose>
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

<!-- wizardTab template -->
<script type="text/template" id="wizardTabTpl">
    <%@include file="/include/wizardTab.jsp" %>
</script>
