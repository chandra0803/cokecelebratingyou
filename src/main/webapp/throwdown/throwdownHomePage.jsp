<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf" %>

<link rel="stylesheet" href="${siteUrlPrefix}/assets/skins/<c:out value='${designTheme}'/>/css/throwdown-app.css" type="text/css">

<div id="LaunchPageView">
    <div class="moduleContainerViewElement">
        <div class="modules-woke"></div>
        <div class="modules-slept"></div>

        <!-- dynamic content -->

    </div> <!-- /.moduleContainerViewElement -->
</div><!-- /#homeAppPageView -->


<!-- MODULES SETUP -->
<script>

    //Main Home Application Setup Function
    $(document).ready(function(){
        var allModules;

        //instantiate HomeApp
        window.hapv = new ThrowdownPageView({
            el : $('#LaunchPageView'),
            pageNav : {
                back : {
                    text : '<cms:contentText key="BACK" code="system.button" />',
                   	<c:choose>
                    	<c:when test="${not empty filterName}">
                    		url : '${pageContext.request.contextPath}/homePage.do#launch/${filterName}'
                    	</c:when>
            			<c:otherwise>
                			url : '${pageContext.request.contextPath}/homePage.do'
                        </c:otherwise>
                	</c:choose>
                },
                home : {
                    text : '<cms:contentText key="HOME" code="system.general" />',
                    url : '${pageContext.request.contextPath}/homePage.do'
                }
            },
            pageTitle : '<cms:contentText key="PAGE_TITLE" code="throwdown.generalInfo" />',
            isFooterSheets : false
        });

        //Celebration App
        G5.props.URL_JSON_THROWDOWN_MATCHES = 'matchSummary.do?method=summary&promotionId=';<%-- This JSON URL gets appended for the promotionId via the _throwdown.js file --%>
        G5.props.URL_JSON_THROWDOWN_PROMOTIONS = 'promotionSelector.do?method=list';
        G5.props.URL_JSON_THROWDOWN_STANDINGS_LINK = '<%=RequestUtils.getBaseURI(request)%>/throwdown/standingsDetail.do?method=link';
        G5.props.URL_JSON_TD_NEWS = 'throwdownNewsResult.do?method=communicationsList';
        G5.props.URL_JSON_SMACK_TALK = 'smackTalkSummary.do?method=summary&promotionId=';
        G5.props.URL_JSON_SMACK_TALK_SAVE_COMMENT = 'smackTalkComment.do?method=postComment';
        G5.props.URL_JSON_SMACK_TALK_SAVE_LIKE = 'smackTalkComment.do?method=like';
        G5.props.URL_JSON_SMACK_TALK_SAVE_HIDE='smackTalkHideAction.do?method=hide';
        G5.props.URL_THROWDOWN_ALL_MATCHES = 'viewMatchesSummary.do?method=summary&matchFilterName=all&promotionId=';
        G5.props.URL_THROWDOWN_ALL_MATCHES_TEAM = 'viewMatchesSummary.do?method=summary&matchFilterName=team&promotionId=';

        // Every Module in the World
        // Listed in order by appName
        allModules = [
            /*
             * Throwdown modules
             */
         <c:forEach items="${filteredModuleList}" var="filteredModule" >
            <c:if test="${filteredModule.module.tileMappingType.code == 'throwdownMatchModule' }">
            {
                name: 'throwdownMatchModule',
                appName: 'throwdown',
                filters:{
                    'recognition':{size:'4x2', order:1},
                    'throwdown':{size:'4x2', order:1}
                }
            },
            </c:if>
            <c:if test="${filteredModule.module.tileMappingType.code == 'throwdownPromoSelectModule' }">
            {
                name: 'throwdownPromoSelectModule',
                appName: 'throwdown',
                filters:{
                    'recognition':{size:'4x2', order:1},
                    'throwdown':{size:'2x2', order:2}
                }
            },
            </c:if>
            <c:if test="${filteredModule.module.tileMappingType.code == 'smackTalkModule' }">
            {
                name:'smackTalkModule',
                appName: 'throwdown',
                filters:{
                    'recognition':{size:'2x2', order:1},
                    'throwdown':{size:'4x2', order:3}
                }
            },
            </c:if>
            <c:if test="${filteredModule.module.tileMappingType.code == 'throwdownRankingsModule' }">
            {
                name: 'throwdownRankingsModule',
                appName: 'throwdown',
                filters:{
                    'recognition':{size:'2x2', order:1},
                    'throwdown':{size:'2x2', order:4}
                }
            },
            </c:if>
            <c:if test="${filteredModule.module.tileMappingType.code == 'throwdownNewsModule' }">
            {
                name:'throwdownNewsModule',
                appName: 'throwdown',
                filters:{
                    'recognition':{size:'2x1', order:1},
                    'throwdown':{size:'2x1', order:5}
                }
            },
            </c:if>
            <c:if test="${filteredModule.module.tileMappingType.code == 'throwdownTrainingCampModule' }">
            {
                name:'throwdownTrainingCampModule',
                appName: 'throwdown',
                filters:{
                    'recognition':{size:'2x1', order:1},
                    'throwdown':{size:'2x1', order:6}
                }
            },
            </c:if>
            <c:if test="${filteredModule.module.tileMappingType.code == 'throwdownStandingsModule' }">
            {
                name:'throwdownStandingsModule',
                appName: 'throwdown',
                filters:{
                    'recognition':{size:'2x1', order:1},
                    'throwdown':{size:'2x1', order:7}
                }
            },
            </c:if>
            <c:if test="${filteredModule.module.tileMappingType.code == 'throwdownAllMatchesModule' }">
            {
                name: 'throwdownAllMatchesModule',
                appName: 'throwdown',
                filters:{
                    'recognition':{size:'4x2', order:1},
                    'throwdown':{size:'4x2', order:1}
                }
            }
            </c:if>
        </c:forEach>
        ]; // allModules

        hapv.launchApp.moduleCollection.reset(allModules);
    });

</script>

<c:forEach items="${filteredModuleList}" var="filteredModule" >
	<c:if test="${filteredModule.module.tileMappingType.code == 'smackTalkModule' }">
	    <tiles:insert name="throwdown.smacktalk.module" flush="true" ignore="true"/>
	</c:if>
	<c:if test="${filteredModule.module.tileMappingType.code == 'throwdownNewsModule' }">
	    <tiles:insert name="throwdown.throwdownnews.module" flush="true" ignore="true"/>
	</c:if>
	<c:if test="${filteredModule.module.tileMappingType.code == 'throwdownTrainingCampModule' }">
	    <tiles:insert name="throwdown.trainingcamp.module" flush="true" ignore="true"/>
	</c:if>
	<c:if test="${filteredModule.module.tileMappingType.code == 'throwdownStandingsModule' }">
	    <tiles:insert name="throwdown.standings.module" flush="true" ignore="true"/>
	</c:if>
	<c:if test="${filteredModule.module.tileMappingType.code == 'throwdownPromoSelectModule' }">
	    <tiles:insert definition="throwdown.promoselect.module" flush="true" ignore="true"/>
	</c:if>
	<c:if test="${filteredModule.module.tileMappingType.code == 'throwdownMatchModule' }">
	    <tiles:insert definition="throwdown.matchsummary.module" flush="true" ignore="true"/>
	</c:if>
	<c:if test="${filteredModule.module.tileMappingType.code == 'throwdownRankingsModule' }">
	    <tiles:insert definition="throwdown.rankings.module" flush="true" ignore="true"/>
	</c:if>
	<c:if test="${filteredModule.module.tileMappingType.code == 'throwdownAllMatchesModule' }">
	    <tiles:insert definition="throwdown.matchlist.module" flush="true" ignore="true"/>
	</c:if>
</c:forEach>