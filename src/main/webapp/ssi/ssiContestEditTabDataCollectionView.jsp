<%@ include file="/include/taglib.jspf"%>
<%@page import="com.biperf.core.utils.UserManager"%>
<h4 class="defaultName">{{_defContName.text}}</h4>

<h5><cms:contentText key="COLLECT_CONTEST_DATA" code="ssi_contest.datacollection" /></h5>
<div class="control-group validateme"
        data-validate-flags="nonempty"
        data-validate-fail-msgs="{&quot;nonempty&quot; : &quot;Please make a choice&quot;}">
    {{#if isUploadSpreadsheetAvailable}}
    <label class="radio" for="collectDataMethodUploadSpreadsheet">
        <input type="radio"
                class="collectDataMethodRadio autoBind"
                id="collectDataMethodUploadSpreadsheet"
                name="collectDataMethod"
                value="activityUpload"
                data-model-key="collectDataMethod"
                {{#eq collectDataMethod "activityUpload"}}checked{{/eq}}>
        <cms:contentText key="SPREAD_SHEET_METHOD" code="ssi_contest.datacollection"/>
    </label>
    {{/if}}
    {{#if isFillOutFormAvailable}}
    <label class="radio" for="collectDataMethodFillOutForm">
        <input type="radio"
                class="collectDataMethodRadio autoBind"
                id="collectDataMethodFillOutForm"
                name="collectDataMethod"
                value="claimSubmission"
                data-model-key="collectDataMethod"
                {{#eq collectDataMethod "claimSubmission"}}checked{{/eq}}>
        <cms:contentText key="CLAIM_SUBMISSION_METHOD" code="ssi_contest.datacollection"/>
    </label>
    {{/if}}
</div>



<div id="buildAClaimForm" {{#ueq collectDataMethod "claimSubmission"}}style="display:none"{{/ueq}}>
    <hr/>
    <h3><cms:contentText key="BUILD_CLAIM_FORM" code="ssi_contest.datacollection" /></h3>



    <!--
            888b.       w
            8   8 .d88 w8ww .d88b
            8   8 8  8  8   8.dP'
            888P' `Y88  Y8P `Y88P
    -->
    <div class="control-group validateme claimDeadlineDateWrapper"
        data-validate-flags="nonempty"
        data-validate-fail-msgs='{"nonempty" : "Please enter a deadline date"}'>
        <label class="control-label" for="claimDeadlineDate"><cms:contentText key="LAST_DATE_SUBMIT_CLAIM" code="ssi_contest.datacollection" /></label>
        <div class="controls">
            <!-- JAVA NOTE: set data-date-* attrs -->
            <span class="input-append datepickerTrigger claimDeadlineDateTrigger"
                    data-date-format="<%=UserManager.getUserDatePattern().toLowerCase()%>"
					data-date-language="<%=UserManager.getUserLocale()%>"
					data-date-todaydate="{{contestEndDate}}"
					data-date-startdate="{{contestEndDate}}"
                    data-date-autoclose="true">
                <input type="text"
                        id="claimDeadlineDate"
                        data-model-key="claimDeadlineDate"
                        readonly="readonly"
                        class="date claimDeadlineDateInput"
                        value="{{claimDeadlineDate}}">
                <button class="btn" type="button"><i class="icon-calendar"></i></button>
            </span>
        </div>
    </div>





    <!--
            8888 w       8    8
            8www w .d88b 8 .d88 d88b
            8    8 8.dP' 8 8  8 `Yb.
            8    8 `Y88P 8 `Y88 Y88P
    -->
    <h4><cms:contentText key="CLAIM_FORM_FIELDS" code="ssi_contest.datacollection" /></h4>

    <!--subTpl.fieldItem=
        <div class="fieldItem{{#if isSelected}} selected{{/if}}" id="field_{{name}}" data-field-name="{{name}}">
            <div class="cellSort fieldCell">
                {{#unless _editable}}
                    &nbsp;
                {{else}}
                    <i class="icon-swap-vertical dragHandle"></i>
                {{/unless}}
            </div>
            <div class="cellSelect fieldCell">
                {{#unless _editable}}
                    {{#if isSelected}}
                        <i class="icon-check"></i>
                    {{else}}
                        &nbsp;
                    {{/if}}
                {{else}}
                    {{#if isSelected}}
                        <i class="icon-check selectBtn"></i>
                    {{else}}
                        <a href="#" class="addBtn"><cms:contentText key="ADD" code="ssi_contest.datacollection" /></a>
                    {{/if}}
                {{/unless}}
            </div>
            <div class="cellName fieldCell">
                {{label}}<br>
                <small>{{typeDisplay}}</small>
            </div>
            <div class="cellRequired fieldCell">
                <label class="checkbox">
                    {{#unless _editable}}
                        <input type="checkbox" disabled="disabled" {{#if isRequired}}checked{{/if}} />Required
                    {{else}}
                        <input type="checkbox" {{#if isRequired}}checked{{/if}} />Required
                    {{/unless}}
                </label>
            </div>
        </div>
    subTpl-->

    <h5><cms:contentText key="REQUIRED_FIELDS" code="ssi_contest.datacollection" /></h5>
    <div class="fieldItemListWrapper">
        <div class="fieldItemList systemFields">
            <!-- dyn -->
        </div>
    </div>

    {{#_hasAdditionalFields}}
    <h5><cms:contentText key="ADDITIONAL_FIELDS" code="ssi_contest.datacollection" /></h5>
    <p>
    	<cms:contentText key="ADDITIONAL_FIELDS_INFO" code="ssi_contest.datacollection" />
    </p>
    <div class="fieldItemListWrapper">
        <div class="fieldItemList additionalFields{{#ueq status "live"}} editable{{/ueq}}">
            <!-- dyn -->
        </div>
    </div>
    {{/_hasAdditionalFields}}

    <%--

        NOTE: This functionality removed for 5.5 release.

    <div class="addNewFieldWrapper">
        <div class="showFieldEditControls">
            {{#ueq status "live"}}
            <a href="#" class="showAddBtn">
                Add a new field
            </a>
            {{/ueq}}
        </div>
        <div class="fieldEditor" style="display:none">
            <div><strong>Add a New Field</strong></div>
            <input class="newFieldLabel" type="text"
                placeholder="enter field label" />
            <span class="errMsg label label-important" style="display:none">required</span>

            <div class="control-group">
                <div>Field Type</div>
                <select class="fieldTypeInp">
                    {{#fieldTypes}}
                        <option value="{{id}}">{{display}}</option>
                    {{/fieldTypes}}
                </select>
            </div>

            <div class="control-group">
                <label class="checkbox">
                    <input class="isReqInp" type="checkbox" />Required
                </label>
            </div>

            <div class="fieldEditorControls">
                <button class="btn btn-small btn-primary addFieldBtn">
                    Add
                </button>
                <button class="btn btn-small cancelAddFieldBtn">
                    Cancel
                </button>
            </div>
        </div>
    </div>
    --%>

    <div class="control-group">
        <br>
        <button class="previewBtn btn btn-primary">
            <cms:contentText key="PREVIEW_CLAIM_FORM" code="ssi_contest.datacollection" />
        </button>
    </div>
    <div class="modal modal-stack hide fade" id="previewClaimFormModal" data-y-offset="adjust">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal"><i class="icon-close"></i></button>
            <h3><cms:contentText key="CLAIM_FORM_FIELDS" code="ssi_contest.datacollection" /></h3>
        </div>
        <div class="modal-body">
            <!-- dynamic -->
        </div>
    </div>

</div>
