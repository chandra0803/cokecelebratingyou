<%@ include file="/include/taglib.jspf"%>


<script type="text/template" id="throwdownNewsModuleTpl">
<div class="module-liner">
    
    <a href="${pageContext.request.contextPath}/throwdown/throwdownNewsPage.do#index" class="visitAppBtn"><i class="icon-arrow-2-circle-right"></i></a>
    <div class="wide-view">

        
        <div id="communicationsCarousel" class="carousel">
            
            <div class="cycle carousel-inner">

                <!--things go here-->

            </div>

        </div>

    </div>
    
    <div class="title-icon-view">
        <h3>
            <cms:contentText key="COMMISSIONER_NEWS" code="hometile.throwdownNews"/>
        </h3>
    </div>
    
</div>
</script>


    <script type="text/template" id="throwdownNewsModuleItemTpl">
        <div class="item" style="background-image:url({{storyImageUrl}})"> <!--Should maybe be slide[i] where i is the Id of this story-->

            <div class="wide-view comm-story {{storyFormat}}">

                <h3>{{storyName}}</h3> {{!--no spot for date in here, yet. When it is ready to be added, the handlebars attribute is "storyDate"--}}

                <div class="story-content-short">

                    {{{storyContentShort}}}

                </div>

                <div class="read-more"><p><a href="{{storySlug}}?messageUniqueId={{id}}"><cms:contentText key="READ_MORE" code="hometile.communication"/> &raquo;</a></p></div>

            </div>

        </div>

    </script>                            
