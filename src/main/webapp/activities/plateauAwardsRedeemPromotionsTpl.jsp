<%@ include file="/include/taglib.jspf"%>

{{#if promotions.[1]}}
<div class="redeem-multi-promotion">
    <h2><cms:contentText key="CHOOSE_YOUR_AWARD" code="hometile.plateauAward" /> <span><cms:contentText key="CLICK_BELOW" code="hometile.plateauAward" /></span></h2>
    <ul>
        {{#each promotions}}
        <li class="promotion-info {{#if ../promotions.[2]}}long-list{{/if}}" data-end-date="{{endDate}}">

            <a href="{{catalogUrl}}" target="_blank">
            <span class="promo-name">{{name}}</span>
            <span class="redeemCountdown">
                <span class="time-display"></span>
                <i class="icon-clock"></i> <br/>
                <span class="time-label days"><cms:contentText key="DAYS_LEFT" code="hometile.plateauAward" /></span>
                <span class="time-label hours"><cms:contentText key="HOURS_LEFT" code="hometile.plateauAward" /></span>
                <span class="time-label minutes"><cms:contentText key="MINUTES_LEFT" code="hometile.plateauAward" /></span>
                <span class="time-label expired"><cms:contentText key="EXPIRED" code="hometile.plateauAward" /></span>
            </span>

            </a>
        </li>
        {{/each}}
    </ul>
</div>

{{else}}
<div class="redeem-single-promotion">
    <h2><cms:contentText key="CHOOSE_YOUR_AWARD" code="hometile.plateauAward" /> <span><cms:contentText key="YOU_EARNED_IT" code="hometile.plateauAward" /></span></h2>
    <beacon:authorize ifNotGranted="LOGIN_AS">
    <a href="{{promotions.[0].catalogUrl}}" class="btn btn-primary" target="_blank"><cms:contentText key="CHOOSE_AWARD" code="hometile.plateauAward" /></a> <!-- link to catalog page -->
    </beacon:authorize>

    <div class="redeem-countdown-container promotion-info" data-end-date="{{promotions.[0].endDate}}">
        <p><cms:contentText key="TIME_LEFT_TO_REDEEM" code="hometile.plateauAward" /></p>

        <i class="icon-clock"></i>

        <div class="countdown redeemCountdown">
            <span class="d"><span class="cd-digit">00</span>:</span><!-- Adding comments here to
            --><span class="h"><span class="cd-digit">00</span>:</span><!-- prevent whitespace from
            --><span class="m"><span class="cd-digit">00</span>:</span><!-- appearing between digits
            --><span class="s"><span class="cd-digit">00</span></span>
        </div>

    </div>
</div>
{{/if}}


