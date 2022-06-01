<%@ include file="/include/taglib.jspf"%>
<script type="text/template" id="SSISuperViewerModuleTpl">
<div class="module-liner" data-temp-debug="superViewer">
    <div class="module-content">

        <h3 class="module-title"><cms:contentText key="MY_TEAMS_CONTESTS" code="ssi_contest.pax.superViewer" /></h3>

        <div class="ssiSuperViewerModuleView"></div>

    </div><!-- /.module-content -->
</div><!-- /.module-liner -->

<!--subTpl.contestListItems=

    <div class="contestListWrap scrollArea">
        <ul class="contestList unstyled">
            {{#each activeContestsList}}
                <li class="contestListItem">
                    <a class='ssi-a' href="{{managerDetailPageUrl}}&id={{id}}">
                        {{#if contestTypeName}}<span class='ssi-iconwrap'><i class="type-icon {{contestTypeName}} icon-g5-ssi-{{contestTypeName}}"></i></span>{{/if}}
                        <span class='ssi-textwrap'>{{name}}</span>
                    </a>
                </li>
            {{/each}}
        </ul>
        <div class="contestStatus">
            <span class="pending"></span>
        </div>
    </div>

subTpl-->
</script>

<script>
	$(document).ready(function() {

	//G5.props.URL_JSON_SSI_ACTIVE_CONTESTS = G5.props.URL_ROOT+'assets/ajax/ssiActiveContests.json';
	G5.props.URL_JSON_SSI_ACTIVE_CONTESTS = G5.props.URL_ROOT+'ssi/managerContestList.do?method=fetchLiveContests';

	});
</script>
