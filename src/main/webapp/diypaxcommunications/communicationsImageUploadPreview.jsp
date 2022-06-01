<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<div class="row-fluid">
    <div class="span12">
        <p><a href="#" class="btn btn-danger replaceAllImages"><cms:contentText key="REPLACE_ALL_IMAGES" code="diyCommunications.banner.labels" /></a></p>

        {{#if page}}<h3><cms:contentText key="MODEL_PREVIEWS" code="diyCommunications.banner.labels" /></h3>{{/if}}
        {{#eq type "banner"}}
            <div class="alert alert-success">
                <h4><cms:contentText key="SAFE_AREA" code="diyCommunications.banner.labels" /></h4>
                <p><cms:contentText key="BANNER_TEXT" code="diyCommunications.banner.labels" /></p>
            </div>
        {{/eq}}

        {{#each images}}
        <div class="launchModule {{../type}} size-{{size}}">
            <div class="item">
                <img class="fauxBgImg" src="{{imageUrl}}" alt="preview">
                {{#eq ../type "news"}}
                <div class="wide-view comm-story">
                    <h3><cms:contentText key="TITLE_GOES_HERE" code="diyCommunications.banner.labels" /></h3>
                </div>
                {{/eq}}

                {{#eq ../type "banner"}}
                <div class="safe-area">
                    <h4><cms:contentText key="SAFE_AREA" code="diyCommunications.banner.labels" /></h4>
                </div>
                {{/eq}}
            </div>

            {{#eq size "mobile"}}
            <div class="replaceImage">
                <a class="btn btn-mini btn-danger replaceImageBtn" id="replaceImage{{size}}" data-size="{{size}}"><cms:contentText key="REPLACE_IMAGE" code="diyCommunications.banner.labels" /></a>
            </div>
            {{/eq}}
        </div><!-- /.launchModule -->
        {{/each}}
    </div>
</div>
