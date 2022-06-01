<%@ include file="/include/taglib.jspf" %>
<!-- ======== RULES PAGE ======== -->

<!-- Page Header  -->
<div id="rulesPageView" class="page-content rules"> 
    
    <!-- Page Body -->
        <div id="rulesPage" class="row-fluid">
            <!-- <p id="rulesEmpty" syle="display: none"><cms:contentText key="NO_RULES_AVAILABLE" code="promotion.form.rules" /></p> -->
        </div>
</div>
<!-- /#rulesPageView --> 

<script type="text/template" id="rulesPageViewTpl">
		<%@include file="/rules/rulesPageView.jsp"%>
</script>

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to --> 
<script>
$(document).ready(function(){
	
	G5.props.URL_JSON_RULES = '${pageContext.request.contextPath}/rules.do?method=fetchPromotionRules';
    var rpv = new RulesPageView({
        el:$('#rulesPageView'),
        promoId:1234,
        pageTitle : '<cms:contentText key="PROMO_RULES_PAGE_TITLE" code="promotion.form.rules" />',
        footerLinksTarget: "page"
    });
    
});
</script>