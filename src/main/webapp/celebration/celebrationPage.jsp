<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf" %>

<div id="LaunchPageView">
    <div class="moduleContainerViewElement">
        <div class="modules-woke"></div>
        <div class="modules-slept"></div>
        <!-- dynamic content -->
    </div> <!-- /.moduleContainerViewElement -->

     <c:if test="${celebrationValue.displayShareOptions}">
	    <div class="container shareIcons">
	        <div class="social-icons-container">
	            <ul class="share-print">
	            	<c:forEach items="${ celebrationValue.shareLinkBeanList }" var="shareLinkBean" varStatus="status">
		                <li>
		                    <a target="_blank"
		                        class="social-icon ${shareLinkBean.name} icon-size-16"
		                        href="${shareLinkBean.url}"></a>
		                </li>
	                </c:forEach>
	            </ul>
	        </div>
	    </div>
	</c:if>

</div><!-- /#LaunchPageView -->


<!-- MODULES SETUP -->
<script>

    //Main Home Application Setup Function
    $(document).ready(function(){
        var allModules;

        //instantiate HomeApp
        window.hapv = new CelebrationPageView({
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
            pageTitle : '<cms:contentText key="CELEBRATION_TITLE" code="celebration.tile.page"/>',
            isFooterSheets : false
        });

        //Celebration App
        G5.props.URL_JSON_CELEBRATION_RECOGNITION_PURL = "${celebrationRecognitionPurlUrl}";
        G5.props.URL_JSON_CELEBRATION_IMAGE_FILLER_URL = "${celebrationImageFillerUrl}";

        // Every Module in the World
        // Listed in order by appName
        allModules = [
            /*
             * Celebration modules
             */
             //1st row
            {
                name:'celebrationCongratsModule',
                appName: 'celebration',
                filters:{
                    'recognition':{order:1}
                }
            } ,


            <c:if test="${celebrationValue.displayPointsChooseAward || celebrationValue.displayPlateauChooseAward}">
            {
                name:'celebrationChooseAwardModule',
                appName: 'celebration',
                filters:{
                    'recognition':{order:2}
                }
            } ,
            </c:if>

            <c:if test="${celebrationValue.displayPlateauBrowseAwards}">
            {
                name:'celebrationBrowseAwardsModule',
                appName: 'celebration',
                filters:{
                    'recognition':{order:3}
                }
            }
            </c:if>

            <c:if test="${not empty celebrationValue.managerMessageList}">
            ,{
                name:'celebrationManagerMessageModule',
                appName: 'celebration',
                filters:{
                    'recognition':{order:4}
                }
            }
            </c:if>

            <c:if test="${celebrationValue.displayPurl}">
            ,{
                name:'celebrationRecognitionPurlModule',
                appName: 'celebration',
                filters:{
                    'recognition':{order:5}
                }
            }
            </c:if>

            <c:if test="${celebrationValue.displayYearTile}">
            ,{
                name:'celebrationAnniversaryFactsModule',
                appName: 'celebration',
                filters:{
                    'recognition':{order:6}
                }
            }
            </c:if>


            <c:if test="${celebrationValue.displayTimelineTile}">
            ,{
                name:'celebrationCompanyTimelineModule',
                appName: 'celebration',
                filters:{
                    'recognition':{order:7}
                }
            }
            </c:if>

            <c:if test="${celebrationValue.displayVideoTile}">
            ,{
	           name:'celebrationCorporateVideoModule',
	           appName: 'celebration',
	           filters:{
	           'recognition':{order:8}
	           }
	        }
	    	</c:if>


        ]; // allModules

        hapv.launchApp.moduleCollection.reset(allModules);
    });

</script>


<tiles:insert definition="celebration.congrats.module" flush="true" ignore="true"/>

<c:if test="${celebrationValue.displayPointsChooseAward || celebrationValue.displayPlateauChooseAward }">
  	<tiles:insert definition="celebration.choose.award.module" flush="true" ignore="true"/>
</c:if>

<c:if test="${celebrationValue.displayPlateauBrowseAwards}">
 	<tiles:insert definition="celebration.browse.awards.module" flush="true" ignore="true"/>
</c:if>

<tiles:insert definition="celebration.manager.message.module" flush="true" ignore="true"/>

<c:if test="${celebrationValue.displayPurl}">
	<tiles:insert definition="celebration.recognition.purl.module" flush="true" ignore="true"/>
</c:if>

<c:if test="${celebrationValue.displayYearTile}">
	<tiles:insert definition="celebration.anniversary.facts.module" flush="true" ignore="true"/>
</c:if>

<c:if test="${celebrationValue.displayTimelineTile}">
	<tiles:insert definition="celebration.company.timeline.module" flush="true" ignore="true"/>
</c:if>

<c:if test="${celebrationValue.displayVideoTile}">
	<tiles:insert definition="celebration.corporate.video.module" flush="true" ignore="true"/>
</c:if>

