<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>
<!--Java will be sending this html back to tile view -->
<!-- I included two widgets here. One is mission and other is achievements -->
<!-- The two div with the class  name "nitro-widget" is actually widget codes. These widget codes are copied from nitro studio -->
<!-- The script tag below also copied from nitro studio -->
<!-- data-user-id is currently added as my name. This shud be dynamic user id -->
<!-- data-custom-css is currently added as my localhost app-bunchball.css. This shud be the actual path of app-bunchball.css based on the environment -->
<!-- data-token shud be dynamic -->

<div class="widgets">
    <div name="MY MISSIONS"
        class="nitro-widget"
        width="100%"
        height="100%"
        data-user-id=${userName }
        data-name='MY MISSIONS'
        data-challenge-type='eligible'
        data-custom-css='["${siteUrlPrefix}/assets/skins/tcccg6/css/app-bunchball.css"]'
        data-detail-title='CHALLENGE'
        data-export-navigation='false'
        data-height='100%'
        data-log-click-through='false'
        data-main-title='<cms:contentText key="MISSION_WIDGET_HEADER" code="coke.bunchball"/>'
        data-navigation='inline'
        data-return-count='10'
        data-width='100%'>
    </div>
</div>

<div class="widgets">
    <div name="MY ACHIEVEMENTS"
        class="nitro-widget"
        width="100%"
        height="100%"
        data-user-id=${userName }
        data-name='MY ACHIEVEMENTS'
        data-custom-css='["${siteUrlPrefix}/assets/skins/tcccg6/css/app-bunchball.css"]'
        data-export-navigation='false'
        data-height='100%'
        data-return-count='10'
        data-title='<cms:contentText key="ACHV_WIDGET_HEADER" code="coke.bunchball"/>'
        data-width='100%'>
    </div>
</div>
<script id="nitro-js"
     data-token="${tokenName }"
     data-server="${apiUrl }"
     data-version="latest"
     src="https://widgets.bunchball.net/nitro/latest/nitro.min.js"
     data-custom-css='["${siteUrlPrefix}/assets/skins/<c:out value='${designTheme}'/>/css/app-bunchball.css"]'></script>
</div>