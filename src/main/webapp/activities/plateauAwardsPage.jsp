<%@ include file="/include/taglib.jspf"%>
<!--  PLATEAU AWARDS PAGE  -->
<!--
    DEVELOPERS NOTE: 
    - the data-promo-id, data-level-id, and data-product-id attributes will be selected on load
    - data-promo-id will be forwared on the JSON request for data as 'promoId'
    - OTHER OPTIONS - such as using the URL query string are implementable (just ask)
    - JSON data:
      * every request should return all promotion objects
      * every request must include the levels+products for provided promotionId parameter (this data will be cached in browser)
      * PLEASE SEE: ajax/browsePlateauAwards.json  and  ajax/browsePlateauAwards_differentPromotion.json
      * JSON structure: object.promotions.levels.products (see ajax/browsePlateauAwards.json)
-->

<%@include file="/activities/plateauAwardsPageInclude.jsp" %>

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->

<script>
    $(document).ready(function(){
        //attach the view to an existing DOM element
        G5.props.URL_JSON_PLATEAU_AWARDS = '${pageContext.request.contextPath}/plateauAwardsDetail.do?method=detailViewResult';

        if(!G5.views || !G5.views.page){ // are we in Send a Recognition?
            var pap = new PlateauAwardsPageView({
                el:$('#plateauAwardsPageView'),
                pageTitle : '<cms:contentText key="BROWSE_PLATEAU_AWARDS" code="hometile.plateauAward" />'
            });
        }

    });
</script>

<script type="text/template" id="plateauAwardsItemTpl">
	
	<%@include file="/activities/plateauAwardsItem.jsp" %>


</script>
	
<script type="text/template" id="plateauAwardsDrawerTpl">
	
	<%@include file="/activities/plateauAwardsDrawer.jsp" %>

</script>


