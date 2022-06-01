<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

<div class="modal-header">
    <button type="button" class="close" data-dismiss="modal"><i class="icon-close"></i></button>
    <h3><cms:contentText key="SELECT_CONTEST_TYPE" code="ssi_contest.creator" /></h3>
</div>
<div class="modal-body">
    <p class="text-right">
        <a href="{{contestGuideUrl}}" title='<cms:contentText key="CONTEST_GUIDE" code="ssi_contest.creator" />' target="_blank" class="contestGuide"><cms:contentText key="CONTEST_GUIDE" code="ssi_contest.creator" /> <span class="btn btn-mini invert-btn btn-export-pdf"><cms:contentText key="PDF" code="system.general" /> <i class="icon-download-2"></i></span></a>
    </p>

    <ul class="nav nav-tabs">
        {{#each contestTypes}}
        <li class="tabNavContent" data-id="{{contestType}}">
            <a href="#ssi-contest-create-{{contestType}}" data-toggle="tab">
                {{#eq contestType "objectives"}}
                    <i class="icon-g5-ssi-objectives"></i>
                {{/eq}}
                {{#eq contestType "stepItUp"}}
                    <i class="icon-g5-ssi-stepItUp"></i>
                {{/eq}}
                {{#eq contestType "doThisGetThat"}}
                    <i class="icon-g5-ssi-doThisGetThat"></i>
                {{/eq}}
                {{#eq contestType "stackRank"}}
                    <i class="icon-g5-ssi-stackRank"></i>
                {{/eq}}
                {{#eq contestType "awardThemNow"}}
                    <i class="icon-g5-ssi-awardThemNow"></i>
                {{/eq}}
                <span>{{contestTypeName}}</span>
            </a>
        </li>
        {{/each}}
    </ul><!-- /.nav.nav-tabs -->

    <div class="tab-content">
        {{#each contestTypes}}
        <div class="tabContent tab-pane" id="ssi-contest-create-{{contestType}}">
            <h4>{{contestTypeName}} <cms:contentText key="CONTEST" code="ssi_contest.participant" /></h4>
            <p>
                {{description}}
            </p>
        </div>
        {{/each}}
    </div>
</div>
<div class="modal-footer">
    <button class="btn btn-primary qtipSubmit"><cms:contentText key="CREATE_CONTEST" code="ssi_contest.creator" /></button>
</div>
