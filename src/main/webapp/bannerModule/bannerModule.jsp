<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ include file="/include/taglib.jspf"%>


<!-- ======== Banner MODULE ======== -->
<script type="text/template" id="bannerModuleModuleTpl">
<div class="module-liner">

    <div class="module-content">


        <div id="BannerCarousel" class="carousel">


        </div>

    </div>

    <div class="bannerPager">
        <ul>

        </ul>
    </div>

    <div class="slick-counter">

    </div>

</div>
</script>

<!--Specify item here. "id" should match item specified in view.js(in this case BannerModuleCollection.js)  -->
<script type="text/template" id="bannerSlideTplTpl">
	<%@include file="/bannerModule/bannerSlide.jsp" %>
</script>
