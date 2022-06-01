<%@ include file="/include/taglib.jspf"%>

    <div class="row-fluid newsDetail">
        <div class="span12">
            <div class="page-topper" data-size='{"ratio":"2.35:1"}'>

                <img alt="{{storyName}}" class="storyImage" src="{{storyImageUrl}}"
                    data-image="{{storyImageUrl}}"
                    data-image-mobile="{{storyImageUrl_mobile}}"
                    data-image-max="{{storyImageUrl_max}}">

                <a href="javascript:history.go(-1);" class="returnToList"><i class="icon-arrow-1-circle-left"></i> <cms:contentText key="BACK" code="system.button" /></a>
                <h2 class="headline_2">{{{storyName}}}</h2>
            </div>
        </div>
    </div>
    <div class="row-fluid">
        <div class="span12">
            <p class="timeStamp"><time datetime="{{storyDate}}">{{storyDate}}</time></p>
            {{{storyContent}}}
        </div>
    </div>
