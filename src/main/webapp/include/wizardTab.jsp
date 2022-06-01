{{#each .}}
<li class="wtTab{{#if isActive}} active{{/if}}"
    data-tab-uid="{{id}}"
    data-tab-name="{{name}}"
    data-tab-content="{{contentSel}}"
    data-tab-state="{{state}}" >
    <div class="wtTabLiner">
        <div class="wtNumber"><i class="icon-rec-circle"></i><span>{{wtNumber}}{{! if empty, view will fill this in}}</span></div>
        <div class="wtName"><span>{{wtName}}</span></div>
        <div class="wtIconWrap">
            <span class="wtIcon iconLocked"><i class="icon-lock"></i></span>
            <span class="wtIcon iconUnlocked"><i class="icon-unlocked"></i></span>
            <span class="wtIcon iconIncomplete"><i class="icon-warning-circle"></i></span>
            <span class="wtIcon iconComplete"><i class="icon-check-circle"></i></span>
        </div>
    </div>
    {{#unless hideDeedle}}{{! iff you want to hide the nubbin}}<div class="wtDeedle">&nbsp;</div>{{/unless}}
</li><!-- /.wtTab -->
{{/each}}
