<%@ include file="/include/taglib.jspf"%>

        <div class="row-fluid newsStory"> <!-- start of template -->
            <div class="span12">
                <img alt="{{storyName}}" class="storyImage" src="{{storyImageUrl_placeholder}}"
                    data-image="{{storyImageUrl}}"
                    data-image-mobile="{{storyImageUrl_mobile}}"
                    data-image-max="{{storyImageUrl_max}}">

                <h3 class="headline_3">{{storyName}}</h3>
                <p class="timeStamp"><time datetime="2013-04-03">{{storyDate}}</time></p>
                <div class="storySummary">{{{storyContentShort}}}</div>
                <div class="read-more"><p><a href="#story/{{id}}"><cms:contentText key="READ_MORE" code="hometile.communication"/></a></p></div>
            </div>
        </div> <!-- end of template -->
