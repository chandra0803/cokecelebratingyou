<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="java.util.*"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>


<!-- ======== NOMINATIONS MODULE ======== -->
<script type="text/template" id="nominationsModuleTpl">

<div class="module-liner">
	<div class="wide-view">
		<div class="nominationIcon">
				<i class="icon-badge-2"></i>
		</div>

        <form class="nomination-module-wrapper selectNominationForm" action="nomination/submitNomination.do" method="post">
            <div class="promoSelectWrap"></div>
        </form>
    </div> <!-- ./wide-view -->

    <div class="errorTipWrapper" style="display:none">
        <i class="icon-warning-triangle"></i>
        <cms:contentText key="MAX_NOMINATION_REACHED" code="promotion.nomination.submit"/>
    </div><!-- /.errorTipWrapper -->

	<!--subTpl.promoSelector=
		{{#unless nominations.[1]}}
            <div class="control-group row-fluid">
                 <div class="span12 text-center">
                    <span class="promotionName" data-id={{nominations.[0].promoId}}>{{nominations.[0].name}}</span>
                    <input type="hidden" name="promoId"" value="{{nominations.[0].promoId}}" />
                </div>
            </div>

            <div class="row-fluid button-wrap">
                <div class="span12 text-center">
                    <button type="button" class="btn btn-primary nominateBtn"><cms:contentText key="NOMINATE_SOMEONE" code="promotion.nomination.submit"/></button>
                </div>
            </div>
        {{else}}
            <div class="control-group row-fluid">
                 <div class="span12 text-center">
                    <select class="promoSelector" id="promoSelector" name="promoId">
                        <option class="defaultOption" selected="selected" value=""><cms:contentText key="SELECT_PROMO" code="promotion.nomination.submit"/></option>
                        {{#each nominations}}
                            <option value="{{promoId}}" name="{{name}}" data-id="{{promoId}}">
                                {{name}}
                            </option>
                        {{/each}}
                    </select>
                </div>
            </div>

            <div class="row-fluid button-wrap">
                <div class="span12 text-center">
                    <button type="button" class="btn btn-primary nominateBtn" disabled="disabled"><cms:contentText key="NOMINATE_SOMEONE" code="promotion.nomination.submit"/></button>
                </div>
            </div>
        {{/unless}}
        {{#if showPastWinnersLink}}
            <div class="row-fluid button-wrap">
                <div class="span12 text-center">
                    <a class="btn btn-primary pastwinnersBtn" href="<%=RequestUtils.getBaseURI(request)%>/nomination/viewNominationPastWinnersList.do?method=display"><cms:contentText key="VIEW_PAST_WINNERS" code="nomination.winners"/> <i class="icon-trophy-1"></i></a>
                </div>
            </div>
        {{/if}}
	subTpl-->
</div><!-- ./module-liner -->

</script>

<script>
/* jshint ignore:end */
</script>
