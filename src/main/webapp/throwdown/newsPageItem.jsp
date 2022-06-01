<%@ include file="/include/taglib.jspf"%>

        <div class="row-fluid"> <!-- start of template -->
            <div class="span2">
                <img alt="{{storyName}}" src="{{storyImageUrl}}">
            </div>
            <div class="span10">
                <h3>{{storyName}}</h3>
                <p class="timeStamp"><time datetime="2013-04-03">{{storyDate}}</time></p>
                <div class="storySummary">{{{storyContentShort}}}</div>
                <div class="read-more"><p><a href="\#story/{{id}}"><cms:contentText key="READ_MORE" code="hometile.throwdownNews"/> &raquo;</a></p></div>
            </div>
        </div> <!-- end of template -->
