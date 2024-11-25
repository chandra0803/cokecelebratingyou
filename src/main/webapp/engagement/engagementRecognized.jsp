<%@ include file="/include/taglib.jspf"%>

<div class="tree mode_{{mode}} type_{{type}}">

{{#if participants}}
    {{#if root}}
        {{#with root}}
        <div class="root" {{#if userId}}data-user-id="{{userId}}"{{/if}} {{#if nodeId}}data-node-id="{{nodeId}}"{{/if}}>
             <span class="avatarwrap">
                {{#if avatarUrl}}
                    <img src="{{#timeStamp avatarUrl}}{{/timeStamp}}"  />
                {{else}}
                    <div class="avatar-initials">{{trimString name 0 1}}</div>
                {{/if}}
            </span>
            <span class="name">{{name}}</span>
            <i class="icon-arrow-2-down paxRecTo"></i>
            <i class="icon-arrow-2-up paxRecBy"></i>
        </div>
        {{/with}}
    {{/if}}

    <ul class="nodes unstyled">
        {{#each nodes}}
        <li class="node {{#if isFirst}}first{{/if}} {{#if isLast}}last{{/if}}" data-node-id="{{id}}">
            <div class="nodewrap">
                <h5 class="name">{{name}}</h5>

                <ul class="members unstyled">
                    {{#each members}}
                    <li class="member {{#if isFirst}}first{{/if}} {{#if isLast}}last{{/if}}" data-user-id="{{id}}">
                        <a class="participant-popover" href="#" data-participant-ids="[{{id}}]">
                             <span class="avatarwrap">
                                {{#if avatarUrl}}
                                    <img src="{{#timeStamp avatarUrl}}{{/timeStamp}}"  />
                                {{else}}
                                    <div class="avatar-initials">{{trimString name 0 1}}</div>
                                {{/if}}
                            </span>
                            {{#ueq count 1}}
                            <span class="count">{{count}}</span>
                            {{/ueq}}
                            <span class="name">{{name}}</span>
                        </a>
                    </li>
                    {{/each}}
                </ul><!-- /.members -->
            </div><!-- /.nodewrap -->
        </li><!-- /.node -->
        {{/each}}
        {{#if hasMore}}
        <li class="hasMore">
            <a href="#" class="showMore"><cms:contentText key="SHOW_MORE" code="engagement.participant"/></a>
        </li>
        {{/if}}
    </ul><!-- /.nodes -->
{{/if}}

</div><!-- /.tree.mode_{{mode}}.type_{{type}} -->
