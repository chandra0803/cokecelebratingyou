<!-- ======== CELEBRATION CORPORATE VIDEO MODULE ======== -->
<%@ include file="/include/taglib.jspf"%>
<script type="text/template" id="celebrationCorporateVideoModuleTpl">
<div class="module-liner">
    <div class="wide-view">
        <div class="module-content">

            <c:if test="${celebrationValue.customUrl}">
        	<video id="celebrationVideo" class="video-js vjs-default-skin vjs-16-9 vjs-big-play-centered" controls preload="auto" poster="${celebrationValue.videoImage}"> controls preload="auto" poster="${celebrationValue.videoImage}">
            	<source src="${celebrationValue.corporateVideoUrl}" type='video/mp4' />
            	<source src="${celebrationValue.corporateVideoUrl}" type='video/webm' />
            	<source src="${celebrationValue.corporateVideoUrl}" type='video/ogg' />
        	</video>
        </c:if>
        <c:if test="${!celebrationValue.customUrl}">
	        <video id="celebrationVideo" class="video-js vjs-default-skin vjs-16-9 vjs-big-play-centered" controls preload="auto" poster="${celebrationValue.videoImage}">
	            <source src="${celebrationValue.corporateVideoUrl}.mp4" type='video/mp4' />
		        <source src="${celebrationValue.corporateVideoUrl}.webm" type='video/webm' />
            	<source src="${celebrationValue.corporateVideoUrl}.ogg" type='video/ogg' />
	        </video>
        </c:if>
        </div> <!-- ./module-content -->
    </div>

</div>

<!-- Video Modal -->
<!--div id="corporateVideoModal" class="modal hide fade">
    <div class="modal-body">
        <c:if test="${celebrationValue.customUrl}">
        	<video id="celebrationVideo" class="video-js vjs-default-skin vjs-big-play-centered" controls preload="auto">
            	<source src="${celebrationValue.corporateVideoUrl}" type='video/mp4' />
            	<source src="${celebrationValue.corporateVideoUrl}" type='video/webm' />
            	<source src="${celebrationValue.corporateVideoUrl}" type='video/ogg' />
        	</video>
        </c:if>
        <c:if test="${!celebrationValue.customUrl}">
	        <video id="celebrationVideo" class="video-js vjs-default-skin vjs-big-play-centered" controls preload="auto">
	            <source src="${celebrationValue.corporateVideoUrl}.mp4" type='video/mp4' />
		        <source src="${celebrationValue.corporateVideoUrl}.webm" type='video/webm' />
            	<source src="${celebrationValue.corporateVideoUrl}.ogg" type='video/ogg' />
	        </video>
        </c:if>
    </div>

    <button type="button" class="close" data-dismiss="modal">
        <i class="icon-remove-sign"></i>
    </button>
</div-->
</script>
