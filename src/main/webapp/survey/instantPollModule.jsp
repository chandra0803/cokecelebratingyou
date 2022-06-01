<%@ include file="/include/taglib.jspf"%>
<!-- ======== INSTANT POLL MODULE ======== -->
<script type="text/template" id="instantPollModuleTpl">
<div class="modal-content">

    <div class="modal-header">
	    <button type="button" class="close" data-dismiss="modal"><i class="icon-close"></i></button>
        <h3 class="modal-title"><cms:contentText key="TILE_TITLE" code="instantpoll.library" /></h3>
    </div>

    <div id="pollsContainer" class="modal-body">
            <!-- dynamic -->
    </div>
</div>


<!--subTpl.surveyTakeTpl=
    <jsp:include page="/survey/surveyTakeTpl.jsp"/>
subTpl-->

<!--tplVariable.cm=
{
    "btn" : {
        "submit" : "<cms:contentText key="VOTE" code="instantpoll.library" />"
    }
}
tplVariable-->
</script>
