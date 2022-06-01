<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<script type="text/template" id="newsModuleItemTpl">
<div class="item" id="{{id}}"> <!--Should maybe be slide[i] where i is the Id of this story-->

    <a href="{{storySlug}}?messageUniqueId={{id}}" class="item-link"
        data-image="{{storyImageUrl}}"
        data-image-mobile="{{storyImageUrl_mobile}}"
        data-image-max="{{storyImageUrl_max}}">

        <div class="story {{storyFormat}}">
            <h3>{{{storyName}}}</h3>
        </div>
    </a>

</div>
</script>
