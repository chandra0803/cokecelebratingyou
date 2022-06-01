<%@ include file="/include/taglib.jspf"%>

    <div class="row-fluid">
        <div class="span12">
            <h2><cms:contentText key="NEWS_DETAIL" code="hometile.throwdownNews"/></h2>
        </div>
    </div>
    <div class="row-fluid">
        <div class="span4">
            <img alt="{{storyName}}" src="{{storyImageUrl}}">
        </div>
        <div class="span8">
            <h3>{{storyName}}</h3>
            <p class="timeStamp"><time datetime="{{storyDate}}">{{storyDate}}</time></p>
            {{{storyContent}}}
        </div>
    </div>
