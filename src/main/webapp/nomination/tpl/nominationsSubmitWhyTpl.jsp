<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ include file="/include/taglib.jspf"%>
<%@page import="com.biperf.core.utils.UserManager"%>

<div class="nominationsFormWrapper">

    <h3>{{promotion.whyTabLabel}}</h3>

    <span class="nominationSaved" style="display:none"><cms:contentText key="SAVED" code="promotion.nomination.submit" /> &nbsp;<i class="icon icon-diskette-1"></i></span>

    {{#if promotion.defaultWhyActive}}
    <div class="defaultWhySection">
        <div class="nominationReason validateme"
            data-validate-flags='nonempty,maxlength'
            data-validate-max-length="2000"
            data-validate-fail-msgs='{"nonempty":"<cms:contentText key="COMMENTS_REQUIRED" code="promotion.nomination.submit" />","maxlength":"<cms:contentText key="COMMENTS_REQUIRED" code="promotion.nomination.submit" />"}'>
             <textarea data-max-chars="2000" class="richtext comments" data-model-key="comments">{{promotion.comments}}</textarea>
        </div><!-- /.nominationReason -->
    </div>
    {{/if}}

    {{#if promotion.customFieldsActive}}
        {{#customElements}}
            <div class="customElementsWrap">
            {{#each fields}}
                {{#eq type "date"}}
                    <div class="control-group {{name}} {{#if isRequired}}validateme{{/if}} {{#if fieldGroup}} {{fieldGroup}}Group{{/if}}"
                        {{#if isRequired}}data-validate-fail-msgs='{"nonempty":"<cms:contentText key="FIELD_REQUIRED" code="promotion.nomination.submit" />"}'
                        data-validate-flags='nonempty'{{/if}}>
                        <label for="{{name}}" class="control-label">{{label}}{{#if isRequired}}*{{/if}}</label>
                        <div class="controls">
                            <!--
                                NOTE: JSP set data-date-format AND data-date-startdate
                            -->
                            <span class="input-append datepickerTrigger"
                                    data-date-format="<%=UserManager.getUserDatePattern().toLowerCase()%>"
			                        data-date-language="<%=UserManager.getUserLocale()%>"
			                        data-date-startdate=""
			                        data-date-todaydate=""
			                        data-date-autoclose="true">
                                <input class="date datepickerInp"
                                        type="text"
                                        data-index="{{index}}"
                                        placeholder=""
                                        value="{{value}}"
                                        readonly="readonly"><button class="btn datepickerBtn" type="button"><i class="icon-calendar"></i></button>
                            </span>
                        </div>
                    </div>
                {{/eq}}

                {{#eq type "text"}}
                    <div class="control-group {{name}} {{#if isRequired}}validateme{{/if}} {{#eq format "numeric"}}validateme{{/eq}} {{#eq format "email"}}validateme{{/eq}} {{#if fieldGroup}} {{fieldGroup}}Group{{/if}}"
                        {{#if isRequired}}data-validate-fail-msgs='{"nonempty":"<cms:contentText key="FIELD_REQUIRED" code="promotion.nomination.submit" />"{{#eq format "email"}},"email":"<cms:contentText key="VALID_EMAIL" code="promotion.nomination.submit" />"{{/eq}} {{#eq format "numeric"}},"numeric": "<cms:contentText key="FIELD_REQUIRED" code="promotion.nomination.submit" />"{{/eq}} }'
                        data-validate-flags='nonempty{{#eq format "email"}},email{{/eq}}{{#eq format "numeric"}},numeric{{/eq}}'{{/if}}>
                        <label for="{{name}}" class="control-label">{{label}}{{#if isRequired}}*{{/if}}</label>
                        <div class="controls">
                            <input type="text" data-index="{{index}}" {{#if size}} maxlength="{{size}}"{{/if}} value="{{value}}" />
                        </div>
                    </div>
                {{/eq}}

                {{#eq type "number"}}
                    <div class="control-group numericInput {{name}} {{#if isRequired}}validateme{{/if}} {{#if fieldGroup}} {{fieldGroup}}Group{{/if}}"
                        {{#if isRequired}}data-validate-fail-msgs='{"nonempty":"<cms:contentText key="FIELD_REQUIRED" code="promotion.nomination.submit" />"}'
                        data-validate-flags='nonempty'{{/if}} data-maxdecimal="{{maxDecimal}}">
                        <label for="{{name}}" class="control-label">{{label}}{{#if isRequired}}*{{/if}}</label>
                        <div class="controls">
                            <input {{#if mask}}type="password" {{else}} type="text"{{/if}} data-index="{{index}}" value="{{value}}" />
                        </div>
                    </div>
                {{/eq}}

                {{#eq type "selection"}}
                    <div class="control-group {{name}} {{#if isRequired}}validateme{{/if}} {{#if fieldGroup}} {{fieldGroup}}Group{{/if}}"
                        {{#if isRequired}}data-validate-fail-msgs='{"nonempty":"<cms:contentText key="FIELD_REQUIRED" code="promotion.nomination.submit" />"}'
                        data-validate-flags='nonempty'{{/if}}>
                        <label for="{{name}}" class="control-label">{{label}}{{#if isRequired}}*{{/if}}</label>
                        <div class="controls">
                            <select data-index="{{index}}" id="{{name}}" >
                                <option class="defaultOption" value="">Choose one<!--TODO: cms key--></option>
                                {{#selectList}}
                                    <option value="{{id}}" name="{{name}}" {{#if countryCode}}class="{{countryCode}}"{{/if}} {{#eq ../value id}}selected{{/eq}}>{{name}}</option>
                                {{/selectList}}
                            </select>
                        </div>
                    </div>
                {{/eq}}

                {{#eq type "multi_selection"}}
                    <div class="control-group {{name}} {{#if isRequired}}validateme{{/if}} {{#if fieldGroup}} {{fieldGroup}}Group{{/if}}"
                        {{#if isRequired}}data-validate-fail-msgs='{"nonempty":"<cms:contentText key="FIELD_REQUIRED" code="promotion.nomination.submit" />"}'
                        data-validate-flags='nonempty'{{/if}}>
                        <label for="{{name}}" class="control-label">{{label}}{{#if isRequired}}*{{/if}}</label>
                        <div class="controls">
                            <select data-index="{{index}}" id="{{name}}" multiple="true" size="5" value="{{value}}">
                                {{#selectList}}
                                    <option value="{{id}}" name="{{name}}" {{#if countryCode}}class="{{countryCode}}"{{/if}} {{#eq ../value id}}selected{{/eq}}>{{name}}</option>
                                {{/selectList}}
                            </select>
                        </div>
                    </div>
                {{/eq}}

                {{#eq type "text_box"}}
                    <div class="control-group {{name}} {{#if isRequired}}validateme{{/if}} {{#if fieldGroup}} {{fieldGroup}}Group{{/if}}"
                        {{#if isRequired}}data-validate-fail-msgs='{"nonempty":"<cms:contentText key="FIELD_REQUIRED" code="promotion.nomination.submit" />"}'
                        data-validate-flags='nonempty'{{/if}}>
                        {{#eq ../customWhyId id}}
                        <label for="{{name}}" class="control-label customWhyLabel">{{label}}{{#if isRequired}}*{{/if}}</label>

                        <div class="controls">
                            <textarea data-index="{{index}}" cols="10" rows="5" value="{{value}}" data-max-chars="{{customWhyCharCount}}" class="richtext customWhyComment">{{value}}</textarea>
                        </div>

                        {{else}}
                        <label for="{{name}}" class="control-label">{{label}}{{#if isRequired}}*{{/if}}</label>
                        <div class="controls">
                            <textarea class="richtext" data-max-chars="{{customWhyCharCount}}" data-index="{{index}}" cols="10" rows="5" value="{{value}}">{{value}}</textarea>
                        </div>
                        {{/eq}}
                    </div>
                {{/eq}}
                {{#eq type "sect_head"}}
                    <div class="control-group">
                        <h4>{{name}}</h4>
                    </div>
                {{/eq}}

                {{#eq type "link"}}
					<div class="control-group">						
						<a class="customFormLink" target="_blank" href="{{linkUrl}}" id="{{id}}" title={{linkName}}>
							{{linkName}}
						</a>
					</div>
				{{/eq}}
				
                {{#eq type "boolean"}}
                    <div class="control-group {{name}} {{#if isRequired}}validateme{{/if}} {{#if fieldGroup}} {{fieldGroup}}Group{{/if}}"
                        {{#if isRequired}}data-validate-fail-msgs='{"nonempty":"<cms:contentText key="FIELD_REQUIRED" code="promotion.nomination.submit" />"}'
                        data-validate-flags='nonempty'{{/if}}>
                        <label for="{{name}}" class="control-label">{{label}}{{#if isRequired}}*{{/if}}</label>
                        <div class="controls">
                            <label class="radio" for="{{name}}A" style="display: inline-block;">
                                <input type="radio" id="{{name}}A" name="{{name}}" data-index="{{index}}" value="true" {{#eq value "true"}}checked{{/eq}}> {{trueLabel}}
                            </label>

                            <label class="radio" for="{{name}}B" style="display: inline-block;">
                                <input type="radio" id="{{name}}B" name="{{name}}" data-index="{{index}}" value="false" {{#eq value "false"}}checked{{/eq}}> {{falseLabel}}
                            </label>
                        </div>
                    </div>
                {{/eq}}

                {{#eq type "copy"}}
                    <div class="control-group">
                        <h5>{{label}}</h5>
                        <p>{{{copyValue}}}</p>
                    </div>
                {{/eq}}

            {{/each}}
            </div>
        {{/customElements}}
    {{/if}}

<div class="control-group">
        <label class="checkbox" for="addAttachment">
            <input type="checkbox" name="addAttachment" id="addAttachment" />
            			<cms:contentText key="ADD_ATTACHMENT" code="promotion.nomination.submit" />
        </label>
    </div>
	<div id="maxDocAttached" style="display:none" class="alert-danger">
        <p><b>You attached maximum documents.</b></p>
    </div>
	
    <div class="attachmentDrawer" style="display: none">
        <div class="control-group nominationAddLink">
            <p class="attachmentHelpText"><cms:contentTemplateText code="promotion.nomination.more.info" key="ATTACHMENT_HELP_TEXT" args="{{promotion.minDocsAllowed}},{{promotion.maxDocsAllowed}}" delimiter=","/></p>
            <div>
                {{#or promotion.fileName promotion.nominationUrl}}
                    <a href="#" class="addDoc" style="display:none;"><i class="icon-file"></i><cms:contentText key="DOCUMENT" code="promotion.nomination.submit" /></a>
                    <a href="#" class="addDocNew"><i class="icon-file"></i><cms:contentText key="CHANGE_DOCUMENT" code="promotion.nomination.submit" /></a>
                    <a href="#" class="addUrl" style="display:none;"><i class="icon-link-1"></i><cms:contentText key="URL" code="promotion.nomination.submit" /></a>
                    <a href="#" class="addUrlNew"><i class="icon-link-1"></i><cms:contentText key="CHANGE_URL" code="promotion.nomination.submit" /></a>
                {{else}}
                    <a href="#" class="addDoc"><i class="icon-file"></i><cms:contentText key="DOCUMENT" code="promotion.nomination.submit" /></a>                    
                    <a href="#" class="addDocNew" style="display:none;"><i class="icon-file"></i><cms:contentText key="DOCUMENT" code="promotion.nomination.submit" /></a>
                    <a href="#" class="addUrl"><i class="icon-link-1"></i><cms:contentText key="URL" code="promotion.nomination.submit" /></a>                    
                    <a href="#" class="addUrlNew" style="display:none;"><i class="icon-link-1"></i><cms:contentText key="URL" code="promotion.nomination.submit" /></a>
                {{/or}}
            </div>
            <div class="attachmentSection nominationDisplayAttached"></div>            
            <!--subTpl.attachmentDocTpl=                
                <span class="nominationDisplayLink">
                    <div data-validate-fail-msgs='{"nonempty":"You must enter minimum of {{promotion.minDocsAllowed}} attachment or URL."}' data-validate-flags='nonempty' class="{{#lt promotion.updatedDocCount promotion.minDocsAllowed}}validateme{{else}}{{/lt}}">
                        <input type="text" id="nominationLink" name="nominationLink" data-model-key="nominationLink" value="{{#lt promotion.updatedDocCount promotion.minDocsAllowed}}{{else}}{{promotion.updatedDocCount}}{{/lt}}" />
                    </div>                    
                        {{#each promotion.nominationLinks}}  
                            <p>
                            {{#if this.nominationLink}}
                                {{this.fileName}}
                            {{else}}
                                {{#if this.nominationUrl}}
                                    {{this.nominationUrl}}
                                {{/if}}
                            {{/if}}                      
                            <button data-linkid="{{this.linkid}}" class="removeLink btn btn-mini btn-icon btn-danger {{#if this.nominationUrl}} {{/if}} {{#if this.nominationLink}}{{/if}}"><i class="icon-trash"></i></button>
                            </p>                            
                        {{/each}}                    
                    </span>

                    {{#eq promotion.updatedDocCount promotion.maxDocsAllowed}}<p>You Reached Limit</p>{{/eq}}
            subTpl-->                                
        </div>
        


            <div class="removeAttachmentPopover" style="display:none">
				<p><b><cms:contentText key="REMOVAL_ALERT" code="promotion.nomination.more.info" /></b></p>
                <p><cms:contentText key="REMOVAL_ALERT_INFO" code="promotion.nomination.more.info" /></p>
                <p class="tc">
                    <button id="removeAttachmentConfirmBtn" class="btn btn-primary"><cms:contentText key="ALERT_YES" code="promotion.nomination.more.info" /></button>
                    <button id="removeAttachmentCancelBtn" class="btn"><cms:contentText key="ALERT_NO" code="promotion.nomination.more.info" /></button>
                </p>
            </div><!-- /.removeAttachmentPopover -->
        </div>
    </div>
</div>
<!-- URL Popover -->
<div class="addUrlPopover" style="display: none">   
    <div class="control-group" id="attachUrlSection">
        <label class="control-label" for="nominationUrl">Enter URL of your video or image</label>
        <div class="controls">
            <input type="text" class="nominationUrlAdd" />
            <button type="button" class="attachUrl btn btn-primary"><cms:contentText key="ATTACH" code="promotion.nomination.submit" /></button>
            <p class="muted"><cms:contentText key="URL_EXAMPLE" code="promotion.nomination.submit" /></p>
        </div>
    </div>
</div>

<!-- MAX REACHED Popover -->
<div class="maxDocPopover" style="display: none">   
    <div class="control-group" id="attachUrlSection">
        <label class="control-label" for="nominationUrl">You Reached Maximum Upload Limit</label>
        <div class="controls">            
            <button id="removeAttachmentCancelBtn" class="btn">Ok</button>
        </div>
    </div>
</div>
           

<!-- Doc Popover -->
<div class="addDocPopover" style="display: none">
    <div class="control-group">
        <label class="control-label"><cms:contentText key="UPLOAD_DOC" code="promotion.nomination.submit" /></label>
        <div class="controls">
            <form id="nominationDocUpload" enctype="multipart/form-data">
                <input type="file" name="nominationLink" id="nominationLink" class="nominationLink" accept=".pdf,.xls,.doc,.docx,.jpg,.png,.ppt,.mp4,.webm" />
            </form>
        </div>
    </div>
</div>


<!-- Max Upload Popover -->
<div class="maxUploadReached" style="display: none">    
    <div class="control-group" id="attachDocSection">
        <p><strong>You Reached Maximum Limit of Uploads</strong></p>  
        <p class="tc">            
            <button class="btn btn-mini cancelTip">OK</button>
        </p>          
    </div>
</div>
<!--- Custom Added for Tooltip Doc -->
<div class="attachingNewDoc" style="display:none">

	<p>
	<cms:contentText key="ATTACHING_NEW_DOCUMENT" code="promotion.nomination.submit" />
	</p>
	<p class="tc">
		<button id="nominationNewDocConfirmBtn" class="btn btn-primary"><cms:contentText key="ATTACH" code="promotion.nomination.submit" /></button>
		<button id="nominationNewDocDoNotCancelBtn" class="btn"><cms:contentText key="CANCEL" code="promotion.nomination.submit" /></button>
	</p>
</div>
<!--- //Custom Added for Tooltip Doc -->

<!--- Custom Added for Tooltip for addUrlNew -->
<div class="attachingNewUrl" style="display:none">

	<p>
	<cms:contentText key="ATTACHING_NEW_URL" code="promotion.nomination.submit" />
	</p>
	<p class="tc">
		<button id="nominationNewUrlConfirmBtn" class="btn btn-primary"><cms:contentText key="ATTACH" code="promotion.nomination.submit" /></button>
		<button id="nominationNewUrlDoNotCancelBtn" class="btn"><cms:contentText key="CANCEL" code="promotion.nomination.submit" /></button>
	</p>
</div>
<!--- //Custom Added for Tooltip for addUrlNew -->

<div class="privateNom" style="display:none">
    <div class="control-label">
        <h3><cms:contentText key="PRIVATE_NOMINATION" code="promotion.nomination.submit" /></h3>
    </div>
    <div class="control-group">
        <label class="checkbox" for="privateNomination">
            <input type="checkbox" id="privateNomination" data-model-key="privateNomination" />
            <cms:contentText key="MAKE_NOMINATION_PRIVATE" code="promotion.nomination.submit" />
        </label>
    </div>
</div>

<div class="stepContentControls">

<%-- client customization start --%>
    {{#ueq promotion.nominationStatus "complete"}}
    <%-- client customization end --%>
    <beacon:authorize ifNotGranted="LOGIN_AS">
	    <button class="btn btn-primary submitBtn">
	        <cms:contentText key="SUBMIT_NOMINATION" code="promotion.nomination.submit" />
	    </button>
    </beacon:authorize>
    <button class="btn btn-primary nextBtn">
        <cms:contentText key="NEXT" code="promotion.nomination.submit" />
    </button>
    <beacon:authorize ifNotGranted="LOGIN_AS">
		<button href="#" class="btn cancelBtn">
	        <cms:contentText key="CANCEL" code="promotion.nomination.submit" />
	    </button>
    </beacon:authorize>
    <beacon:authorize ifNotGranted="LOGIN_AS">
        <button class="btn saveDraftBtn btn-primary btn-inverse">
            <cms:contentText key="SAVE_DRAFT" code="promotion.nomination.submit" />
        </button>
    </beacon:authorize>
    <%-- client customization start --%>
    {{/ueq}}
    {{#eq promotion.nominationStatus "complete"}}
    <beacon:authorize ifNotGranted="LOGIN_AS">
	    <button class="btn btn-primary submitBtn">
	        <cms:contentText key="SAVE_NOMINATION" code="lenovo.promotion.nomination.submit" />
	    </button>
    </beacon:authorize>
    <button class="btn btn-primary nextBtn">
        <cms:contentText key="NEXT" code="promotion.nomination.submit" />
    </button>
    <beacon:authorize ifNotGranted="LOGIN_AS">
		<button href="#" class="btn cancelBtn">
	        <cms:contentText key="CANCEL_EDIT" code="lenovo.promotion.nomination.submit" />
	    </button>
    </beacon:authorize>
    {{/eq}}
    <%-- client customization end --%>
</div><!-- /.stepContentControls -->
