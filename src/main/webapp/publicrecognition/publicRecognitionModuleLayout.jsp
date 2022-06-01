<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>
<!--  PUBLIC RECOGNITION MODULE  -->
<!--
    NOTE:
    - JSON data structure ex: ajax/publicRecognition.json
    - JSON data nested thus:
        recognitionSet-recognitions-recognition-comments-comment
-->
<div class="module-liner">

    <div class="wide-view">

        <!-- View and Edit links only appear on follow tab if user does not have anyone in their follow list. -->
        <div class="row-fluid">
            <div class="publicRecTitle">
                <h3><cms:contentText key="PUBLIC_RECOGNITION" code="promotion.overview" /></h3>
            </div>
        <div class="pubRecFilterWrap">
            <span class="filterLabel"><cms:contentText key="CELEBRATION_FILTER_BY" code="purl.celebration.module" /></span>

            <div class="filterTokens">
                <!-- dynamic from PublicRecognitionSetCollectionView.js-->
            </div>

            <div class="dropdown">
                <ul class="dropdown-menu pubRecTabs" role="menu" aria-labelledby="dLabel">
                </ul>
            </div>
			<input type="hidden" id="countryDeptFilter" value= "" />
			<input type="hidden" id="divisionFilter" value= "" />
        </div>
    </div>

        <ul class="recognition-controls follow-list-links" style="display: none;">
			<beacon:authorize ifNotGranted="LOGIN_AS">
            	<li>
            		<a href="${pageContext.request.contextPath}/participantProfilePage.do#profile/FollowList">
                        <i class="icon-pencil2"></i>
            			<cms:contentText key="VIEW_EDIT_LIST" code="recognition.public.recognition.item" />
            		</a>
            	</li>
			</beacon:authorize>
            <!--li><a href="#">Edit Follow List</a></li-->

        </ul>



        <!-- Page Body | Employee View -->

        <div class="app-slider pubRecItemsCont" data-msg-empty="<cms:contentText key="NO_RECOGNITION_FOUND" code="recognition.public.recognition.item" />">

            <div class="publicRecognitionItems">
                <!-- dynamic - pubRecItems -->
            </div>

            <!-- DO NOT REMOVE FROM DOM - this element needs to be at bottom of recogs -->
            <!-- shown when there are more than n number of recognitions -->
            <div class="app-row">
                <p>
                    <a href="#" class="viewAllRecognitions"  style="display:none">
                        <cms:contentText key="VIEW_MORE" code="recognition.public.recognition.item" />
                    </a>
                </p>
            </div>

        </div>


        <!-- If the user doesn't have anyone in their follow list, display a message to add followers and button that links to the follow list page. -->
        <div class="app-slider createFollowListWrapper"  style="display:none">
            <div class="app-row">

                <h2>
                    <i class="icon-user-add"></i>
                    <cms:contentText key="ADD_PEOPLE_TO_LIST" code="recognition.public.recognition.item" />
                </h2>
                <p><cms:contentText key="CREATE_OWN_LIST" code="recognition.public.recognition.item" /></p>

                <a href="${pageContext.request.contextPath}/participantProfilePage.do#profile/FollowList" class="btn btn-primary" data-toggle="button createFollowListBtn"><cms:contentText key="CREATE_FOLLOW_LIST" code="recognition.public.recognition.item" /></a>

            </div>
        </div>

      <!-- If the user has followees in their follow list but there are no recognitions that are public from those followees to display, then display a message to explain why they don't see anything. -->
        <div class="app-slider noRecognitionsFollowListWrapper"  style="display:none">
            <div class="app-row">

                <p><cms:contentText key="NONE" code="recognition.public.recognition.item" /></p>

            </div>
        </div>


    </div> <!-- ./wide-view -->
</div> <!-- ./module-liner -->
<!--  /PUBLIC RECOGNITION MODULE  -->
