<%@ include file="/include/taglib.jspf"%>
<%@page import="com.biperf.core.ui.utils.RequestUtils"%>

<!-- ======== COMMUNICATIONS PAGE ======== -->

<div id="communicationsPageView" class="communicationsPage-liner page-content">
    <div class="row-fluid">
        <div class="commBlockList span12">
            <h2><cms:contentText key="TITLE" code="diyCommunications.common.labels" /></h2>

          <c:if test="${userInBannerAudience}">
            <div class="manageBannersBlock communicationsBlock card span6">
                <div class="blockHeadline">
                    <a href="<%= RequestUtils.getBaseURI(request)%>/participant/manageBanners.do" class="btn btn-mini btn-primary"><cms:contentText key="GO" code="diyCommunications.types.labels" /></a>
                    <h3><i class="icon-bookmark-2"></i> <span><cms:contentText key="BANNER" code="diyCommunications.types.labels" /></span></h3>
                </div>
                <div class="blockContent">
                    <p><cms:contentText key="BANNER_INTRO" code="diyCommunications.types.labels" /></p>
                    <p><cms:contentText key="BANNER_TITLE" code="diyCommunications.types.labels" /> :</p>
                    <ul>
                        <li><cms:contentText key="BANNER_POINT_1" code="diyCommunications.types.labels" /></li>
                        <li><cms:contentText key="BANNER_POINT_2" code="diyCommunications.types.labels" /></li>
                        <li><cms:contentText key="BANNER_POINT_3" code="diyCommunications.types.labels" /></li>
                        <li><cms:contentText key="BANNER_POINT_4" code="diyCommunications.types.labels" /></li>
                    </ul>
                    <%--<a href="#exampleContentModal" class="exampleLink exampleBanner" data-toggle="modal"><cms:contentText key="SEE_EXAMPLE" code="diyCommunications.types.labels" /> &raquo;</--%>
                </div>
            </div>
          </c:if>

          <c:if test="${userInNewsAudience}">
            <div class="manageNewsBlock communicationsBlock card span6">
                <div class="blockHeadline">
                    <a href="<%= RequestUtils.getBaseURI(request)%>/participant/manageNews.do" class="btn btn-mini btn-primary"><cms:contentText key="GO" code="diyCommunications.types.labels" /> </a>
                    <h3><i class="icon-newspaper"></i> <span><cms:contentText key="NEWS_STORY" code="diyCommunications.types.labels" /></span></h3>
                </div>
                <div class="blockContent">
                    <p><cms:contentText key="NEWS_INTRO" code="diyCommunications.types.labels" /></p>
                    <p><cms:contentText key="NEWS_TITLE" code="diyCommunications.types.labels" /> :</p>
                    <ul>
                        <li><cms:contentText key="NEWS_POINT_1" code="diyCommunications.types.labels" /></li>
                        <li><cms:contentText key="NEWS_POINT_2" code="diyCommunications.types.labels" /></li>
                        <li><cms:contentText key="NEWS_POINT_3" code="diyCommunications.types.labels" /></li>
                        <li><cms:contentText key="NEWS_POINT_4" code="diyCommunications.types.labels" /></li>
                    </ul>
                    <%--<a href="#exampleContentModal" class="exampleLink exampleNews" data-toggle="modal"><cms:contentText key="SEE_EXAMPLE" code="diyCommunications.types.labels" /> &raquo;</a>--%>
                </div>
            </div>
          </c:if>

    	  <c:if test="${userInResourceCenterAudience}">
            <div class="manageResourceBlock communicationsBlock card span6">
                <div class="blockHeadline">
                    <a href="<%= RequestUtils.getBaseURI(request)%>/participant/manageResourceCenter.do" class="btn btn-mini btn-primary"><cms:contentText key="GO" code="diyCommunications.types.labels" /> </a>
                    <h3><i class="icon-clipboard-file"></i> <span><cms:contentText key="RESOURCE_CONTENT" code="diyCommunications.types.labels" /></span></h3>
                </div>
                <div class="blockContent">
                    <p><cms:contentText key="RESOURCE_INTRO" code="diyCommunications.types.labels" /></p>
                    <p><cms:contentText key="RESOURCE_TITLE" code="diyCommunications.types.labels" /> :</p>
                    <ul>
                        <li><cms:contentText key="RESOURCE_POINT_1" code="diyCommunications.types.labels" /></li>
                        <li><cms:contentText key="RESOURCE_POINT_2" code="diyCommunications.types.labels" /></li>
                        <li><cms:contentText key="RESOURCE_POINT_3" code="diyCommunications.types.labels" /></li>
                        <li><cms:contentText key="RESOURCE_POINT_4" code="diyCommunications.types.labels" /></li>
                    </ul>
                    <%--<a href="#exampleContentModal" class="exampleLink exampleResource" data-toggle="modal"><cms:contentText key="SEE_EXAMPLE" code="diyCommunications.types.labels" /> &raquo;<--%>
                </div>
            </div>
          </c:if>

          <c:if test="${userInTipsAudience}">
            <div class="manageTipsBlock communicationsBlock card span6">
                <div class="blockHeadline">
                    <a href="<%= RequestUtils.getBaseURI(request)%>/participant/manageTips.do" class="btn btn-mini btn-primary"><cms:contentText key="GO" code="diyCommunications.types.labels" /> </a>
                    <h3><i class="icon-bulb-idea"></i> <span><cms:contentText key="TIPS" code="diyCommunications.types.labels" /></span></h3>
                </div>
                <div class="blockContent">
                    <p><cms:contentText key="TIPS_INTRO" code="diyCommunications.types.labels" /></p>
                    <p><cms:contentText key="TIPS_TITLE" code="diyCommunications.types.labels" /> :</p>
                    <ul>
                        <li><cms:contentText key="TIPS_POINT_1" code="diyCommunications.types.labels" /></li>
                        <li><cms:contentText key="TIPS_POINT_2" code="diyCommunications.types.labels" /></li>
                        <li><cms:contentText key="TIPS_POINT_3" code="diyCommunications.types.labels" /></li>
                        <li><cms:contentText key="TIPS_POINT_4" code="diyCommunications.types.labels" /></li>
                    </ul>
                    <%--<a href="#exampleContentModal" class="exampleLink exampleTip" data-toggle="modal"><cms:contentText key="SEE_EXAMPLE" code="diyCommunications.types.labels" /> &raquo;</a>--%>
                </div>
            </div>
          </c:if>

		  <c:if test="${ showManagerAlert }">
            <div class="manageAlertsBlock communicationsBlock card span6">
                <div class="blockHeadline">
                    <a href="<%= RequestUtils.getBaseURI(request)%>/g5ReduxManagerToolKitAlert.do" class="btn btn-mini btn-primary"><cms:contentText key="GO" code="diyCommunications.types.labels" /> </a>
                    <h3><i class="icon-warning-triangle"></i> <span><cms:contentText key="SEND_ALERTS" code="diyCommunications.types.labels" /></span></h3>
                </div>
                <div class="blockContent">
                    <p><cms:contentText key="ALERTS_INTRO" code="diyCommunications.types.labels" /></p>
                    <p><cms:contentText key="ALERTS_TITLE" code="diyCommunications.types.labels" />:</p>
                    <ul>
                        <li><cms:contentText key="ALERTS_POINT_1" code="diyCommunications.types.labels" /></li>
                        <li><cms:contentText key="ALERTS_POINT_2" code="diyCommunications.types.labels" /></li>
                        <li><cms:contentText key="ALERTS_POINT_3" code="diyCommunications.types.labels" /></li>
                        <li><cms:contentText key="ALERTS_POINT_4" code="diyCommunications.types.labels" /></li>
                    </ul>
                    <%--<a href="#exampleContentModal" class="exampleLink exampleAlert" data-toggle="modal"><cms:contentText key="SEE_EXAMPLE" code="diyCommunications.types.labels" /> &raquo;</a>--%>
                </div>
            </div>
          </c:if>
        </div><!-- /.commBlockList -->
    </div><!-- /.row-fluid -->

    <%--
    <div class="modal hide fade" id="exampleContentModal" >
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal"><i class="icon-close"></i></button>
            <h3><cms:contentText key="EXAMPLE_CONTENT" code="diyCommunications.types.labels" /></h3>
        </div>
        <div class="modal-body">
            <div class="row-fluid">
                <div class="exampleImage exampleBannerModal" style="display:none">
                    <img src="${pageContext.request.contextPath}/assets/img/communications/bannerExample.png" alt="exampleBannerContent" />
                </div>

                <div class="exampleImage exampleNewsModal" style="display:none">
                    <img src="${pageContext.request.contextPath}/assets/img/communications/newsStoryExample.png" alt="exampleNewsContent" />
                </div>

                <div class="exampleImage exampleResourceModal" style="display:none">
                    <img src="${pageContext.request.contextPath}/assets/img/communications/resourceExample.png" alt="exampleResourceContent" />
                </div>

                <div class="exampleImage exampleTipModal" style="display:none">
                    <img src="${pageContext.request.contextPath}/assets/img/communications/tipExample.png" alt="exampleTipContent" />
                </div>

                <div class="exampleImage exampleAlertModal" style="display:none">
                    <img src="${pageContext.request.contextPath}/assets/img/communications/alertExample.png" alt="exampleAlertContent" />
                </div>
            </div>
        </div>
    </div>--%><!--/#exampleContentModal-->
</div>

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
$(document).ready(function() {

    //attach the view to an existing DOM element
    var cpv = new CommunicationsPageView({
        el:$('#communicationsPageView'),
        pageNav : {
        	 back : {
                 text : '<cms:contentText key="BACK" code="system.button" />',
                 url : 'javascript:history.go(-1);'
             },
             home : {
                 text : '<cms:contentText key="HOME" code="system.general" />',
                 url : '<%= RequestUtils.getBaseURI(request)%>/homePage.do'
             }
        },
        pageTitle : '<cms:contentText key="TITLE" code="diyCommunications.common.labels" />'
    });

});
</script>
