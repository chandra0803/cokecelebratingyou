<%@page import="com.biperf.core.utils.UserManager"%>
<%@ include file="/include/taglib.jspf"%>

<input type="hidden" name="contestId" value="{{id}}" />
{{#eq contestType "doThisGetThat"}}
    <div class="row-fluid dtgtActivityLables">
        <div class="span2"><cms:contentText key="ACTIVITY" code="ssi_contest.claims" /></div>
        {{#eq measureActivityIn "units"}}
        <div class="span2"><cms:contentText key="QUANTITY" code="ssi_contest.claims" /></div>
        {{else}}
        <div class="span2"><cms:contentText key="AMOUNT" code="ssi_contest.claims" /></div>
        {{/eq}}
    </div>
    <div class="dtgtActivityWrap clearfix">
        {{#each activities}}
        <input type="hidden" name="activities[{{index}}].id" value="{{id}}"/>
        <div class="control-group validateme" data-validate-fail-msgs='{"nonempty":"This field is required."}' data-validate-flags='nonempty'>
            <label for="ssiActivity" class="control-label span2">{{name}}</label>
            <div class="controls span2">
                <input type="text" name="activities[{{index}}].value" class="input-mini Quantity" data-display-name="{{name}}"/>
            </div>
        </div>
        {{/each}}
    </div>
{{else}}
    {{#if previewMode}}
    <div class="control-group">
        <div class="controls">
            {{#if activity}}
                <span>{{activity}}</span>
            {{else}}
                <span><cms:contentText key="UNIQUE_OBJ_DES" code="ssi_contest.claims" /></span>
            {{/if}}
        </div>
    </div>
    {{else}}
    <div class="control-group validateme" data-validate-fail-msgs='{"nonempty":"This field is required."}' data-validate-flags='nonempty'>
        <div class="controls">
            <span>{{activity}}</span>
            <input type="text" id="ssiActivityHidden" name="activity" data-display-name="Activity" value="{{activity}}" />
        </div>
    </div>
    {{/if}}
{{/eq}}

{{#each fields}}

    <%-- every field gets hidden fields --%>
    <input type="hidden" name="fields[{{index}}].id" value="{{id}}" />
    <input type="hidden" name="fields[{{index}}].type" value="{{type}}" />
    <input type="hidden" name="fields[{{index}}].fieldGroup" value="{{fieldGroup}}" />
    <input type="hidden" name="fields[{{index}}].subType" value="{{subType}}" />
    <input type="hidden" name="fields[{{index}}].sequenceNumber" value="{{sequenceNumber}}" />
    <input type="hidden" name="fields[{{index}}].isRequired" value="{{#if isRequired}}true{{else}}false{{/if}}" />
    {{#ueq type "file"}}
    <input type="hidden" name="fields[{{index}}].description" />
    {{/ueq}}

    {{#eq type "date"}}
        <div class="control-group {{name}} {{#if isRequired}}validateme{{/if}} {{#if fieldGroup}} {{fieldGroup}}Group{{/if}}"
            {{#if isRequired}}data-validate-fail-msgs='{"nonempty":"<cms:contentTemplateText code="system.errors" key="REQUIRED" args="{{label}}"/>"}'
            data-validate-flags='nonempty'{{/if}}>
            <label for="{{name}}" class="control-label">{{label}}{{#if isRequired}}*{{/if}}</label>
            <div class="controls">
                <!--
                    NOTE: JSP set data-date-format AND data-date-startdate
                -->
                <span class="input-append datepickerTrigger"
                        data-date-format="<%= UserManager.getUserDatePattern().toLowerCase() %>"
                        data-date-language="<%= UserManager.getUserLocale() %>"
                        data-date-startdate="${currentDate }"
                        data-date-todaydate="${currentDate }"
                        data-date-autoclose="true">
                    <input class="date datepickerInp"
                            type="text"
                            name="fields[{{index}}].value"
                            data-display-name="{{label}}"
                            placeholder=""
                            readonly="readonly"><button class="btn datepickerBtn" type="button"><i class="icon-calendar"></i></button>
                </span>
            </div>
        </div>
    {{/eq}}

    {{#eq type "text"}}
        <div class="control-group {{name}} {{#if isRequired}}validateme{{/if}} {{#if fieldGroup}} {{fieldGroup}}Group{{/if}}"
            {{#if isRequired}}data-validate-fail-msgs='{"nonempty":"<cms:contentTemplateText code="system.errors" key="REQUIRED" args="{{label}}"/>"}'
            data-validate-flags='nonempty'{{/if}}>
            <label for="{{name}}" class="control-label">{{label}}{{#if isRequired}}*{{/if}}</label>
            <div class="controls">
                <input type="text" name="fields[{{index}}].value" data-display-name="{{label}}" />
            </div>
        </div>
    {{/eq}}

    {{#eq type "number"}}
        <div class="control-group {{name}} {{#if isRequired}}validateme{{/if}} {{#if fieldGroup}} {{fieldGroup}}Group{{/if}}"
            {{#if isRequired}}data-validate-fail-msgs='{"nonempty":"<cms:contentTemplateText code="system.errors" key="REQUIRED" args="{{label}}"/>"}'
            data-validate-flags='nonempty'{{/if}}>
            <label for="{{name}}" class="control-label">{{label}}{{#if isRequired}}*{{/if}}</label>
            <div class="controls">
                <input type="text" name="fields[{{index}}].value" data-display-name="{{label}}" />
            </div>
        </div>
    {{/eq}}

    {{#eq type "email"}}
        <div class="control-group {{name}} {{#if isRequired}} validateme{{/if}} {{#if fieldGroup}} {{fieldGroup}}Group{{/if}}"
            {{#if isRequired}}data-validate-fail-msgs='{"nonempty":"<cms:contentTemplateText code="system.errors" key="REQUIRED" args="{{label}}"/>", "email": "Email is invalid."}'
            data-validate-flags='nonempty,email'{{/if}}>
            <label for="{{name}}" class="control-label">{{label}}{{#if isRequired}}*{{/if}}</label>
            <div class="controls">
                <input type="text" name="fields[{{index}}].value" data-display-name="{{label}}" />
            </div>
        </div>
    {{/eq}}

    {{#eq type "select"}}
        <div class="control-group {{name}} {{#if isRequired}}validateme{{/if}} {{#if fieldGroup}} {{fieldGroup}}Group{{/if}}"
            {{#if isRequired}}data-validate-fail-msgs='{"nonempty":"<cms:contentTemplateText code="system.errors" key="REQUIRED" args="{{label}}"/>"}'
            data-validate-flags='nonempty'{{/if}}>
            <label for="{{name}}" class="control-label">{{label}}{{#if isRequired}}*{{/if}}</label>
            <div class="controls">
                <select name="fields[{{index}}].value" id="{{name}}" data-display-name="{{label}}">
                    <option class="defaultOption" selected="selected" value=""><cms:contentText key="CHOOSE_ONE" code="system.general" /></option>
                    {{#selectList}}
                        <option value="{{id}}" name="{{name}}" {{#if countryCode}}class="{{countryCode}}"{{/if}}>{{name}}</option>
                    {{/selectList}}
                </select>
            </div>
        </div>
    {{/eq}}

    {{#eq type "file"}}
        <div class="control-group ssiFileUploadWrap {{name}} {{#if isRequired}}validateme{{/if}} {{#if fieldGroup}} {{fieldGroup}}Group{{/if}}"
            {{#if isRequired}}data-validate-fail-msgs='{"nonempty":"<cms:contentTemplateText code="system.errors" key="REQUIRED" args="{{label}}"/>"}'
            data-validate-flags='nonempty'{{/if}}>

                <label for="{{name}}" class="control-label">{{label}}{{#if isRequired}}*{{/if}}<i class="icon icon-info"></i></label>
                <button type="button" class="btn btn-primary ssiClaimFileUpload"><cms:contentText key="BROWSE" code="system.general" /></button>

                <div class="ssiClaimDocDisplayWrap controls">
                    <span class="ssiClaimDocDisplay"></span>
                    <a class="removeLink btn btn-mini btn-danger"><i class="icon-trash"></i></a>
                    <input type="text" id="ssiClaimDocHidden" name="fields[{{index}}].value" data-display-name="{{label}}" />
                </div>
            </div>
            <input type="hidden" id="ssiClaimDocHiddenDesc" name="fields[{{index}}].description" />

            <!-- Doc Popover -->
            <div class="ssiClaimAddDocPopover" style="display: none">
                <div class="control-group">
                    <div class="controls">
                        <form id="ssiClaimDocUpload" enctype="multipart/form-data">
                            <input type="hidden" name="fields[{{index}}].id" value="{{id}}" />
                            <input type="file" name="ssiClaimDoc" id="ssiClaimDoc" />
                            <p class="ssiClaimHelpText muted"><cms:contentText key="FILE_SIZE_LIMIT" code="ssi_contest.creator" /> {{maxFileSize}}. <cms:contentText key="SUPPORTS" code="ssi_contest.creator" /> {{supportedFileTypes}}</p>
                        </form>
                    </div>
                </div>
            </div><!--/.ssiClaimAddDocPopover-->
        </div>
    {{/eq}}

    {{#eq type "textarea"}}
        <div class="control-group {{name}} {{#if isRequired}}validateme{{/if}} {{#if fieldGroup}} {{fieldGroup}}Group{{/if}}"
            {{#if isRequired}}data-validate-fail-msgs='{"nonempty":"<cms:contentTemplateText code="system.errors" key="REQUIRED" args="{{label}}"/>"}'
            data-validate-flags='nonempty'{{/if}}>
            <label for="{{name}}" class="control-label">{{label}}{{#if isRequired}}*{{/if}}</label>
            <div class="controls">
                <textarea name="fields[{{index}}].value" cols="10" rows="5" data-display-name="{{label}}"></textarea>
            </div>
        </div>
    {{/eq}}

    {{#if ../previewMode}}
        {{#eq type "address"}}
            <div class="control-group" >
                <label class="control-label">{{label}}{{#if isRequired}}*{{/if}}</label>
                <div class="controls">
                	<cms:contentText key="ADDRESS_FIELDS_FORMAT" code="ssi_contest.payout_objectives" />
                </div>
            </div>
        {{/eq}}
    {{/if}}

{{/each}}

<p><cms:contentText key="REQUIRED" code="ssi_contest.payout_objectives" /> </p>

<div class="ssiAttachDocInfoPopover" style="display: none">
    <p><cms:contentText key="DOC_DESC" code="ssi_contest.claims" /></p>
</div>
