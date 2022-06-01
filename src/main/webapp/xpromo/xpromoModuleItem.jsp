<%@ include file="/include/taglib.jspf"%>
<div class="xpromo-story {{type}} {{data.classes}}">

    <div class="comm-story"
        data-image="{{data.bannerImageUrl}}{{data.storyImageUrl}}"
        data-image-mobile="{{data.bannerImageUrl_mobile}}{{data.storyImageUrl_mobile}}"
        data-image-max="{{data.bannerImageUrl_max}}{{data.storyImageUrl_max}}">

        {{#eq type "banner"}}
        <a href="{{data.linkUrl}}" target="{{data.target}}">
        {{/eq}}
        {{#eq type "news"}}
        <a href="${pageContext.request.contextPath}/newsDetail.do?messageUniqueId={{data.id}}">
        <div class="story">
            <h3>{{{data.storyName}}}</h3>
        </div>
        {{/eq}}
        </a>
    </div>
</div>
