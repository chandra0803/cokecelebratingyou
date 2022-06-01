<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
{{#if _totalPages}}
    <p class="lead"><cms:contentText key="COURSE_MATERIAL" code="claims.quiz.submission" /> {{pageNumber}} <cms:contentText key="OF" code="claims.quiz.submission" /> {{_totalPages}}</p>
{{/if}}

<!-- if there are files associated, it's one of type "image"/"pdf"/"video" -->
{{#if files}}
    <div class="files {{type}}">
        <div class="attachments">

            {{#eq type "image" }}
            <img src="{{files.0.url}}" alt="{{files.0.name}}" class="courseMaterial">
            {{/eq}}

            {{#eq type "pdf" }}
            <ul class="unstyled">
                {{#each files}}
                <li class="file"><a href="{{url}}" class="courseMaterial fileLink" target="_blank">
                    <span>{{name}}</span>
                    <i class="icon-file-pdf btn btn-icon invert-btn btn-export-pdf"></i>
                </a></li>
            {{/each}}
            </ul>
            {{/eq}}

            {{#eq type "video" }}
                {{#eq _videoType "oembed"}}
                <a href="{{files.0.url}}" class="courseMaterial"><cms:contentText key="VIDEO_ICON" code="claims.quiz.submission" /></a>
                {{/eq}}

                {{#eq _videoType "html5"}}
                <div class="videoWrapper">
                    <video id="courseMaterialVideo" class="video-js vjs-default-skin vjs-16-9 vjs-big-play-centered" controls preload="auto" data-setup="{}">
                        {{#each files}}
                            <source src="{{url}}" type="{{type}}">
                        {{/each}}
                    </video>
                </div>
                {{/eq}}
            {{/eq}}

        </div><!-- /.attachments  -->
        <div class="text">
            {{{text}}}
        </div>
    </div><!-- /.files  -->

<!-- otherwise, it's type "text" -->
{{else}}
    {{{text}}}
{{/if}}


<div class="stepContentControls form-actions pullBottomUp">
    {{#if _isFirst}}
    <button class="btn backBtn">
        <i class="icon-arrow-1-left"></i> <cms:contentText key="BACK_QUIZ_DETAILS" code="claims.quiz.submission" />
    </button>
    {{else}}
    <button class="btn backMaterialBtn">
        <i class="icon-arrow-1-left"></i> <cms:contentText key="PREVIOUS" code="system.button" />
    </button>
    {{/if}}

    {{#if _isLast}}
        {{#if _isCompleted}}
        <button class="btn btn-primary resultsBtn">
            <cms:contentText key="VIEW_RESULTS" code="claims.quiz.submission" /> <i class="icon-arrow-1-right"></i>
        </button>
        {{else}}
        <button class="btn btn-primary nextBtn">
            <cms:contentText key="START_TEST" code="claims.quiz.submission" /> <i class="icon-arrow-1-right"></i>
        </button>
        {{/if}}
    {{else}}
    <button class="btn btn-primary nextMaterialBtn">
        <cms:contentText key="NEXT" code="system.button" /> <i class="icon-arrow-1-right"></i>
    </button>
    {{/if}}
</div>
