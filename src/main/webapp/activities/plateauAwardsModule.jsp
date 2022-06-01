<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<!-- ======== PLATEAU AWARDS MODULE ======== -->
<script type="text/template" id="plateauAwardsModuleTpl">
<div class="module-liner">

    <div class="module-content">
        <h3 class="module-title"><cms:contentText key="BROWSE_PLATEAU_AWARDS" code="hometile.plateauAward" /></h3>
        <!-- slideshow -->
        <div class="carousel" id="plateauCarousel" data-cycle-legend-style="arrows">
            <!-- dynamic -->
        </div>



    </div>

</div>
</script>



	<!--Speciy item here. "id" should match item specified in view.js(in this case PlateauAwardsModuleView.js)  -->
	<script type="text/template" id="plateauAwardsModuleItemTpl">
	<div data-url="{{url}}" class="item">

	<a target="_blank">

	    <img alt="" src="{{thumbnail}}">

	    <div class="item-title tc">

	        {{name}}

	    </div>

	</a>

	</div>
	</script>
