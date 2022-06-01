<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf" %>

<!-- ======== PURL CELEBRATE PAGE ======== -->
	<c:choose>
		<c:when test="${isNewSAEnabled == true}">
			<div id="purlCelebratePageView" class="page-content sa-filter">
		</c:when>    
		<c:otherwise>
			<div id="purlCelebratePageView" class="page-content">
		</c:otherwise>
	</c:choose>
	
    <div class="page-liner purlCelebrateItemsCont" data-msg-empty="<c:choose><c:when test="${purlPastPresentSelect == 'past'}"><cms:contentText key="NO_RECOGNITION_PURL" code="purl.celebration.module"/></c:when><c:otherwise><cms:contentText key="NO_RECOGNITION_PURL" code="purl.celebration.module"/></c:otherwise></c:choose>">
        <div class="row-fluid">
            <div class="span12">
                <div class="page-topper">
                    <div class="purlCelebrateView">
                        <form class="purlCelebrateSelectView">
                            <!-- <span class="toggleLabel">&nbsp;</span> -->

                            <div class="control-group" id="purlPastPresentSelect">
                                <div class="controls">

                                    <input type="radio" class="purlSelectRadio" id="purlPastPresentSelectA" name="purlPastPresentSelect" value="upcoming" <c:if test="${purlPastPresentSelect == 'upcoming'}">checked</c:if> />

                                    <label class="radio btn btn-primary" for="purlPastPresentSelectA" style="display: inline-block;">
                                       <!--  <cms:contentText key="VIEW_UPCOMING_PURLS" code="purl.celebration.module"/> -->
                                       <cms:contentText key="UPCOMING" code="purl.celebration.module"/>
                                    </label>


                                    <input type="radio" class="purlSelectRadio" id="purlPastPresentSelectB" name="purlPastPresentSelect" value="past"  <c:if test="${purlPastPresentSelect == 'past'}">checked</c:if> />

                                    <label class="radio btn" for="purlPastPresentSelectB" style="display: inline-block;">
                                        <!-- <cms:contentText key="VIEW_PAST_PURLS" code="purl.celebration.module"/> -->
                                        <cms:contentText key="PAST" code="purl.celebration.module"/>
                                    </label>
                                </div>
                            </div>
                        </form>
                    </div>

                    <div class="purlCelebrateFilterWrap">
                        <span class="filterLabel"><cms:contentText key="CELEBRATION_FILTER_BY" code="purl.celebration.module"/></span>

                        <div class="filterTokens">
                            <!-- dynamic from PurlCelebratePageView.js-->
                        </div>

                        <div class="dropdown">
                            <ul class="dropdown-menu purlCelebrateTabs" role="menu" aria-labelledby="dLabel">
                            </ul>
                        </div>
                    </div>

                    <div class="purlCelebrateSearch">
                        <form id="purlCelebrateSearchForm">
                            <div class="selected-filters">
                                <label for="purlCelebrateNameInput"><cms:contentText key="SEARCH" code="purl.celebration.module"/></label>

                                <span class="selected-filter" style="display:none">
                                    <span class="filter-bold"></span>
                                        <span class="removeSearchToken">
                                            <i class="icon icon-cancel-circle" ></i>
                                        </span>
                                </span>
                            </div>

                            <div class="input-append input-append-inside validateme searchWrap dropdown"
                                data-validate-fail-msgs='{"minlength":"<cms:contentText key="ENTER_ATLEAST_TWO" code="purl.celebration.module"/>"}'
                                data-validate-flags='minlength'
                                data-validate-min-length="2">

                                <input name="purlCelebrateNameInput" id="purlCelebrateNameInput" type="text" placeholder="<cms:contentText key="LAST_NAME" code="purl.celebration.module"/>"
                                        data-autocomp-delay="500"
                                        data-autocomp-min-chars="2"
                                        data-autocomp-url="${pageContext.request.contextPath}/recognitionWizard/recipientSearch.do?method=doAutoComplete">

                                <ul class="dropdown-menu purlSearchDropdownMenu" role="menu"
                                    data-msg-instruction="<cms:contentText key="START_TYPING" code="purl.celebration.module"/>"
                                    data-msg-no-results="<cms:contentText key="NO_RESULTS" code="purl.celebration.module"/>">
                                    <!-- dynamic -->
                                </ul>

                                <button class="btn btn-link add-on searchBtn"><i class="icon-magnifier-1"></i></button>
                                <div class="spinnerWrap"></div>
                            </div>

                            <div class="searchList" style="display: none" data-msg-no-results="<c:choose><c:when test="${purlPastPresentSelect == 'upcoming'}"><cms:contentText key="NO_SEARCH_UPCOMING_PURL" code="purl.celebration.module"/></c:when><c:otherwise><cms:contentText key="NO_SEARCH_PAST_PURL" code="purl.celebration.module"/></c:otherwise></c:choose>">
                                 <!-- dynamic from purlCelebrateSearchItem.html -->
                            </div>
                        </form>
                    </div>
                </div><!-- /.page-topper -->
            </div><!-- /.span12 -->
        </div><!-- /.row-fluid -->

        <div class="row-fluid">
            <div class="span12">
            <h2 class="celebrationTitle upcomingTitle"><cms:contentText key="UPCOMING_PURLS" code="purl.celebration.module"/></h2>
            <h2 class="celebrationTitle pastTitle"><cms:contentText key="PAST_PURLS" code="purl.celebration.module"/></h2>

            <p class="purlCelebrateDesc" ></p>

            <div class="pagination pagination-right paginationControls first"></div>

            <div class="purlCelebrateItems">
                <!-- dynamic - purlCelebrateSet.html -->
            </div>

            <div class="pagination pagination-right paginationControls first"></div>
                <!--subTpl.paginationTpl=
                    <%@include file="/include/paginationView.jsp" %>
                subTpl-->
            </div><!-- /.span12 -->
        </div><!-- /.row-fluid-fluid -->
    </div>
</div><!-- /#purlCelebratePageView -->

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
    $(document).ready(function(){
    	G5.props.URL_JSON_PURL_CELEBRATE_DATA = '${ajaxUrl}';
    	G5.props.URL_JSON_PARTICIPANT_INFO = "${pageContext.request.contextPath}/participantPublicProfile.do?method=populatePax";
        G5.props.URL_JSON_PARTICIPANT_FOLLOW = G5.props.URL_ROOT+'publicRecognitionFollowUnFollowPax.do?method=followUnFollowPax';
        G5.props.URL_JSON_EZ_RECOGNITION_MEMBER_INFO = "${pageContext.request.contextPath}/recognitionWizard/memberInfo.do";
        G5.props.URL_JSON_EZ_RECOGNITION_SEND_EZRECOGNITION = "${pageContext.request.contextPath}/recognitionWizard/submitEasyRecognition.do";
        G5.props.URL_JSON_PURL_CELEBRATE_SEARCH_AUTOCOMPLETE = "${pageContext.request.contextPath}/recognitionWizard/recipientSearch.do?method=doAutoComplete";
        G5.props.URL_JSON_PURL_CELEBRATE_SEARCH_RESULTS = '${ajaxUrl}';

        //attach the view to an existing DOM element
        var pcpv = new PurlCelebratePageView({
            el:$('#purlCelebratePageView'),
            pageNav : {
                back : {
                    text : '<cms:contentText key="BACK" code="system.button" />',
                    url : 'javascript:history.back();'
                },
                home : {
                    text : '<cms:contentText key="HOME" code="system.general" />',
                    url : '${pageContext.request.contextPath}/homePage.do'
                }
            },
            pageTitle : '<cms:contentText key="RECOGNITION_PURLS" code="purl.celebration.module"/>'
        });
    });
</script>

<script type="text/template" id="purlCelebrateSetTpl">
    <%@include file="/purl/purlCelebrateSet.jsp" %>
</script>

<script type="text/template" id="paginationViewTpl">
     <%@ include file="/include/paginationView.jsp" %>
</script>

<script type="text/template" id="purlCelebrateSearchItemTpl">
    <%@include file="/purl/purlCelebrateSearchItem.jsp" %>
</script>

<%@include file="/submitrecognition/easy/flipSide.jsp" %>
