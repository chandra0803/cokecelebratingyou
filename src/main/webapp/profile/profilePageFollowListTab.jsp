<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<%-- TODO:contentText key --%>
<h2><cms:contentText key="FOLLOW_LIST" code="system.general" /></h2>

<div id="profilePageFollowListView">

    <%--<div class="row-fluid">
        <div class="span12">
            <h2><cms:contentText key="IM_FOLLOWING" code="recognition.public.recognition.item" /></h2>
        </div>
    </div>--%>
    <div class="paxSearchStartView"
    	data-search-url="${pageContext.request.contextPath}/participantFolloweeSearch/participantFolloweeSearch.action"
    	data-select-url="publicRecognitionFollowUnFollowPax.do?method=followUnFollowPax&isFollowed=false"
    	data-deselect-url="publicRecognitionFollowUnFollowPax.do?method=followUnFollowPax&isFollowed=true"></div>
    <!--
        Participant search view Element
        - data-search-types: defines the dropdowns and autocompletes
        - data-search-params: defines extra static parameters to send with autocomp and participant requests
        - data-autocomp-delay: how long to wait after key entry to query server
        - data-autocomp-min-chars: min num chars before querying server
        - data-autocomp-url: override autocomplete json provider (maybe needed)
        - data-autocomp-msg-instruction: inital text of autocomplete dropdown
        - data-search-url: override search json provider (usually needed)
        - data-select-url: send selected participant id to server (optional)
        - data-deselect-url: send deselected participant id to server (optional)
        - data-select-mode: 'single' OR 'multiple' select behavior
        - data-msg-select-txt: link to select (optional)
        - data-msg-selected-txt: text to show something is selected (optional)
    -->
   <!-- <div class="" id="participantSearchView" style="display:none"
        data-search-types='[{"id":"lastName","name":"<cms:contentText key="SEARCHBY_LAST_NAME" code="recognition.select.recipients"/>"},{"id":"firstName","name":"<cms:contentText key="SEARCHBY_FIRST_NAME" code="recognition.select.recipients"/>"},{"id":"location","name":"<cms:contentText key="SEARCHBY_LOCATION" code="recognition.select.recipients"/>"},{"id":"jobTitle","name":"<cms:contentText key="SEARCHBY_JOB_TITLE" code="recognition.select.recipients"/>"},{"id":"department","name":"<cms:contentText key="SEARCHBY_DEPARTMENT" code="recognition.select.recipients"/>"},{"id":"country","name":"<cms:contentText key="COUNTRY" code="recognition.select.recipients"/>"}]'
        data-search-params='{"extraKey":"extraValue","anotherKey":"some value"}'
        data-autocomp-delay="500"
        data-autocomp-min-chars="2"
        data-autocomp-msg-instruction='<cms:contentText key="SEARCH_INFO" code="recognition.public.recognition.item" />'
        data-autocomp-url="${pageContext.request.contextPath}/recognitionWizard/publicRecognitionFolloweeSearch.do?method=doAutoComplete"
        data-search-url="${pageContext.request.contextPath}/recognitionWizard/publicRecognitionFolloweeSearch.do?method=generatePaxSearchView"
        data-select-url="publicRecognitionFollowUnFollowPax.do?method=followUnFollowPax&isFollowed=false"
        data-deselect-url="publicRecognitionFollowUnFollowPax.do?method=followUnFollowPax&isFollowed=true"
        data-select-mode="multiple"
        data-msg-select-txt='<cms:contentText key="FOLLOW" code="recognition.public.recognition.item" />'
        data-msg-selected-txt="<i class='icon icon-user-add'></i>"
        data-visibility-controls="showAndHide"
        data-msg-show='<cms:contentText key="ADD_TO_FOLLOW_LIST" code="recognition.public.recognition.item" />'
        data-msg-hide='<cms:contentText key="HIDE_LIST" code="recognition.public.recognition.item" />'>

    </div>
    -->
    <!-- /.paxSearchStartView -->

    <%-- FE change moves the buttons into the search view, leaving the code here for reference to beacon:auth LOGIN_AS
    <div class="span12">
        <p class="addToFollowList">
        	<beacon:authorize ifNotGranted="LOGIN_AS">
            <button
                id="showHideFollowListAdd"
                class="btn btn-primary"
                data-msg-show=""
                data-msg-hide=""><cms:contentText key="ADD_TO_FOLLOW_LIST" code="recognition.public.recognition.item" /></button>
            </beacon:authorize>
        </p>
    </div>
    --%>

    <div class="participantCollectionViewWrapper pullBottomUp">
        <h3><cms:contentText key="IM_FOLLOWING" code="recognition.public.recognition.item" /></h3>

        <table class="table table-condensed table-striped">
            <thead>
                <tr class="participant-item">
                    <th class="participant"><cms:contentText key="PAX" code="recognition.public.recognition.item" /></th>
                    <th class="remove"><cms:contentText key="REMOVE" code="recognition.public.recognition.item" /></th>
                </tr>
            </thead>
            <!--
                data-msg-empty: message to display if no followees
                data-hide-on-empty: hide/show the view (using the *ViewWrapper) when empty/not-empty
            -->
            <tbody id="followeesView"
                class="participantCollectionView"
                data-msg-empty='<cms:contentText key="NOT_FOLLOWING" code="recognition.public.recognition.item" />'
                data-hide-on-empty="false"
                data-alert-type="info">

                <!-- dynamic -->

            </tbody>
        </table>
        <!--
            used to keep track of the number of participants, req. a 'participantCount' class
            name is flexible
         -->
        <input type="hidden" name="paxCount" value="0" class="participantCount" />
    </div><!-- /.participantCollectionViewWrapper -->

</div><!-- /#profilePageFollowListView -->
