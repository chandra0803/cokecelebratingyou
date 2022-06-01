<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf" %>

<!-- ======== CELEBRATION RECOGNTION PURL MODULE ======== -->
<script type="text/template" id="celebrationRecognitionPurlModuleTpl">
<div class="module-liner">
    <div class="wide-view">
        <div class="module-content">

            <h3 class="module-title"><cms:contentText key="WHAT_OTHERS_SAY" code="celebration.recognition.purl"/></h3>
            <div class="module-actions">
                <a class="visitAppBtn" href="${celebrationValue.purlUrl}">
                  <cms:contentText key="VIEW_ALL" code="celebration.recognition.purl"/>
                </a>
                <!-- end -->
            </div>
            <div class="purl-items" id="purlItems">
                <div class="purl-col split50 left">
                    <!-- Content gets added dynamically through JS -->
                </div>

                <div class="purl-col split50 right">
                    <!-- Content gets added dynamically through JS -->
                </div>

                <a href="${celebrationValue.purlUrl}" class="read-more" id="protoReadMore"><cms:contentText key="MORE" code="celebration.recognition.purl"/></a>
            </div>
        </div> <!-- ./module-content -->
    </div>


</div>
<!-- PURL Comment template -->

<!--subTpl.purlCommentsTpl=
    <div class="comment">
       <span class="avatarwrap">
            {{#if avatarUrl}}
                <img src="{{#timeStamp avatarUrl}}{{/timeStamp}}"  />
            {{else}}
                <div class="avatar-initials">{{trimString firstName 0 1}}{{trimString lastName 0 1}}</div>
            {{/if}}
        </span>
        <div class="comment-bubble">
            <i class="icon-arrow-1-left"></i>
            <div class="comment-text {{#if imgUrl}}has-img{{/if}}">
                <h4>{{firstName}} {{lastName}}</h4>
                <p>{{comment}}</p>
                {{#if hasVid}}
                <i class="purlCommentVideoIcon icon-video-camera"/><i>
                {{/if}}
                {{#if hasImg}}
                <div class="img-wrap"><img src="{{ imgUrl }}" alt=""></div>
                {{/if}}
            </div>
        </div>
    </div>
subTpl-->
</script>
