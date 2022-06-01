<%@ include file="/include/taglib.jspf"%>

<li class="item budgetItem {{#if isOdd}}odd{{else}}even{{/if}}">

    {{#if name}}<h4 class="subsection-header budgetName">{{name}}</h4>{{/if}}

    {{#promotions}}
        <div class="promoItem">
            {{#if cheersPromotion}}
			{{name}}
			{{else}}
            <a class="promoName" href="{{url}}">{{name}}</a>
			{{/if}}
            {{#if ../endDate}}
                <span class="promoEndDate"><cms:contentText key="ENDS" code="hometile.budgetTracker" /> {{../endDate}}</span>
            {{else}}
                <span class="promoEndDate"> <cms:contentText key="NO_END_DATE" code="hometile.budgetTracker" /></span>
            {{/if}}
             <!--Begin custom for WIP #25589-->
            {{#if budgetOwner}}
                <span class="promoBudgetOwner"><cms:contentText key="BUDGET_OWNER" code="client.cheers.recognition"/>: {{ budgetOwner }}</span>
            {{/if}}
			<!--End custom for WIP #25589-->
        </div>
    {{/promotions}}

    <div class="progress progress-tip">
        <!-- budget starts at 100% and animates down -->
        <div class="bar" style="width: 100%;"></div>
    </div>
    <div class="minMax">
        <div class="min">0</div>
        <div class="max">{{formatNumber total}}</div>
    </div>

</div><!-- /.budgetItemWrapper -->
