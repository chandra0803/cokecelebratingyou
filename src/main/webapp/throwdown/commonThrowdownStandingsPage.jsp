<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<!-- ======== THROWDOWN STANDINGS PAGE ======== -->
<div id="throwdownStandingsPage" class="page-content">

     <div class="tab-content">
        <div class="tab-pane active" id="Matches">
            <div class="row-fluid" id="matchesTab">
                <div class="span12">

                    <div class="roundPagination pagination pagination-center paginationControls first"></div>
                    <!--subTpl.roundPaginationTpl=
                            <ul>

                                <li class="prev {{#eq currentRound "1"}} disabled {{/eq}}">
                                    <a>&#171;</a>
                                </li>

                                <li>
                                    <p><cms:contentText key="ROUND" code="promotion.stackrank.history" /> <span class="your-round">{{currentRound}}</span> <cms:contentText key="OF" code="promotion.stackrank.history" /> <span class="round-total">{{totalRounds}}</span></p>

                                     <p class= "round-dates">
                                    {{#if roundStartDate}}
                                        {{roundStartDate}} - {{roundEndDate}}
                                    {{/if}}
                                    </p>
                                </li>

                                <li class="next {{#eq totalRounds currentRound}} disabled {{/eq}}">
                                    <a>&#187;</a>
                                </li>

                            </ul>
                            <span class="td-fine-print">{{promotionOverview}}, {{#if isProgressLoaded}}<cms:contentText code="participant.throwdownstats" key="FROM" /> {{roundStartDate}} <cms:contentText code="participant.throwdownstats" key="TO" /> {{progressEndDate}}
                          {{else}}
                            {{#if roundCompleted}}
                                <cms:contentTemplateText key="AS_OF_DATE" code="promotion.goalquest.progress" args="{{roundEndDate}}"/>
                            {{else}}
                                <cms:contentText code="participant.throwdownstats" key="NO_PROGRESS" />
                           {{/if}}
                         {{/if}}
                         </span>

                        subTpl-->

                </div><!-- /.span12 -->
            </div><!-- /.row-fluid -->
        </div><!-- /#Matches.tab-pane -->

    </div><!-- /.tab-content -->

</div><!-- /#throwdownStandingsPage.page-content -->


