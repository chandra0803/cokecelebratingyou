<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>
<script type="text/template" id="throwdownPromoSelectModuleTpl">
<!-- ======== THROWDOWN PROMO SELECT MODULE ======== -->
<div class="module-liner">
    <div class="td-promo-info">
        <div class="td-promo-select-container">
            <div class="td-promo-select-selected">
                <div class="outer"><div>{{ promotion.0.promoName }}</div></div>
            </div>
            <div class="down-arrow-container">
                <span class="down-arrow"></span>
                <span class="down-arrow-shadow"></span>
            </div>
        </div>
         <ul class="td-promo-select-list">
            {{#each promotion}}
            <li class="td-promo-select-item" data-promoid="{{ promoId }}">{{ promoName }}</li>
            {{/each}}
        </ul>

        <div class="td-promo-countdown">
            <div class="clearfix">
                <h4><cms:contentText key="TIME_REMAINING" code="participant.throwdownstats" /></h4>
                <a class="td-promo-info-link" href = "{{ promotion.0.rulesUrl }}" ><cms:contentText key="RULES" code="leaderboard.label"/></a>
            </div>
            <ul class="clearfix">
                <li class="d"><span class="cd-digit">00</span><hr/><hr/><span class="cd-label"><cms:contentText key="DAYS" code="participant.throwdownstats" /></span></li>
                <li class="h"><span class="cd-digit">00</span><hr/><hr/><span class="cd-label"><cms:contentText key="HOURS" code="participant.throwdownstats" /></span></li>
                <li class="m"><span class="cd-digit">00</span><hr/><hr/><span class="cd-label"><cms:contentText key="MINUTES" code="participant.throwdownstats" /></span></li>
                <li class="s"><span class="cd-digit">00</span><hr/><hr/><span class="cd-label"><cms:contentText key="SECONDS" code="participant.throwdownstats" /></span></li>
            </ul>
        </div>
    </div> <!-- /.td-promo-info -->

    <a href="{{ promotion.0.matchesUrl }}" class="td-view-matches">
        <div class="title-icon-view">
            <h3>
                <cms:contentText key="VIEW_MATCHES" code="participant.throwdownstats" />
            </h3>
        </div>
    </a>

</div> <!-- /.module-liner -->
</script>
