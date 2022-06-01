{{debug}}
<div class="ezCheersModal">

    <div class="modal-body">
        <button type="button" id="ezRecModalCloseBtn" class="close" aria-hidden="true"><i class="icon-close"></i></button>

        <div class="cheersProfileContainer">
            <div class="cheers-top">
                <span class="avatar avatarwrap">
                    {{#if avatarUrl}}
                        <img src="{{avatarUrl}}"  />
                    {{else}}
                        <div class="avatar-initials">{{trimString firstName 0 1}}{{trimString lastName 0 1}}</div>
                    {{/if}}                    
                </span>
                <div class='profileWrapper'>
                    <div class="participant-name"><a href='{{participantUrl}}'>{{firstName}} {{lastName}}</a></div>
                    <div class="participant-info">{{#if title}}{{title}} <br/> {{/if}}
                        {{#if orgName}}{{orgName}}{{/if}}
                        {{#if department}}{{#if orgName}} | {{/if}} {{department}}{{/if}}
                    </div>
                </div>
            </div>
        </div>
        
        <div class="formContainer">
            <div class="control-group validateme" data-validate-flags="nonempty" data-validate-fail-msgs='{"nonempty" : "Please select a Points"}'>
                    <label class="control-label" for="cheersPoints"><cms:contentText key="AWARD_POINTS" code="client.cheers.recognition"/></label>
                    <div class="controls">
                        <select id='cheersPoints' class="" name='points'>
                            <option value=""><cms:contentText key="SELECT_POINT" code="client.cheers.recognition"/></option>
                            {{#each points}}
                                <option value="{{points}}">                        
                                    + &nbsp; {{points}} &nbsp; <cms:contentText key="POINTS" code="client.cheers.recognition" />
                                </option>
                            {{/each}}
                        </select>
                    </div>
            </div>
             <div id="ezCheersCommentBox">
                    <label class="comment-label" for="comments"><cms:contentText key="COMMENTS" code="recognition.submit"/></label>
                    <div class="commentTools">
                        <span class="remCharsLabel"><cms:contentText key="CHAR_LIMIT" code="recognition.submit"/></span> <span class="remChars" id="ezRecharCount">&nbsp;</span>
                        <span class="spellchecker dropdown">
                            <button class="checkSpellingDdBtn btn btn-mini btn-icon btn-primary btn-inverse dropdown-toggle"
                                    title="<cms:contentText key="SPELL_CHECK" code="recognition.submit"/>"
                                    data-toggle="dropdown">
                                    <i class="icon-check"></i>
                            </button>
                            <ul class="dropdown-menu langs">
                                <li><a class="check"><b><cms:contentText key="SPELL_CHECK" code="recognition.submit"/></b></a></li>
                            {{#spellChecker}}
                            <li class="lang {{lang}}" data-lang="{{lang}}"><a>{{langTitle}}</a></li>
                            {{/spellChecker}}
                            </ul>
                        </span>
                    </div>

                    <div class="control-group validateme"
                            data-validate-fail-msgs='{"maxlength":"<cms:contentText key="COMMENT_LIMIT" code="recognitionSubmit.errors"/>"}'
                            data-validate-flags='maxlength'
                            data-validate-max-length="1000" >
                            <textarea name="comments" id="comments"
                                class="recognition-comment"
                                placeholder="<cms:contentText key='COMMENTS' code='recognition.submit'/>"
                                maxlength="1000"
                                data-max-chars="1000">
                            </textarea>
                        </div>
            </div>
            <div class="ezCheersFormBtnsWrapper">
                <button type="button" id="ezCheersSendBtn" class="btn btn-primary ezRecButton btn-fullmobile" data-loading-text="Sending..."><cms:contentText key="SEND" code="system.button"/></button>

                <button type="button" id="ezCheersCancelBtn" class="btn btn-inverse-primary ezRecButton btn-clear-bg btn-fullmobile"><cms:contentText key="CANCEL" code="system.button"/></button>

            </div>

        </div><!-- ./formContainer -->
    </div>

</div> <!-- ./module-liner -->

<div class="modal hide fade autoModal recognitionResponseModal" id="ezRecSendSuccessModal">
</div>
