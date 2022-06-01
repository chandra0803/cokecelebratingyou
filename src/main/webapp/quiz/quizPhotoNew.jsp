<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

<p>
    <img src="" alt="Photo" class="thumb" />
    <span class="caption"></span>
</p>

<div class="singleline editmedia">
    <label for="" class="text">
        <span class="label"></span>
        <textarea name="contributor_photos" id="contributor_photos" class="text"></textarea>
        <span class="ghost"><cms:contentText key="PHOTO_CAPTION" code="purl.ajax"/></span>
    </label>
</div>

<p class="tools">
    <button type="button" class="fancy fancy-cancel fancy-small deletemedia" id="" formaction="<%=RequestUtils.getBaseURI(request)%>/quiz/quizLearningObjectSubmit.do?method=deletePhoto"><span><span><cms:contentText key="DELETE" code="purl.ajax"/></span></span></button>
    <button type="button" class="fancy fancy-cancel fancy-small cancelmedia" id=""><span><span><cms:contentText key="CANCEL" code="system.button"/></span></span></button>
    <button type="button" class="fancy fancy-small editmedia" id=""><span><span><cms:contentText key="EDIT" code="system.button"/></span></span></button>
    <button type="button" class="fancy fancy-small savemedia" id=""><span><span><cms:contentText key="SAVE" code="system.button"/></span></span></button>
    <span class="filename"></span>
</p>

<script type="text/javascript" charset="utf-8">
if ( $.browser.msie && $.browser.version > 6 ) {
	$('#contributor_photos').spellayt({ url: "<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/jquery/jquery.spellayt.dictionary.txt" });
}
</script>
