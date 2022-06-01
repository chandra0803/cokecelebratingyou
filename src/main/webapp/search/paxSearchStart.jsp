<%@ include file="/include/taglib.jspf"%>
<script type="text/template" id="paxSearchStartViewTpl">
<div class="paxSearchOuterWrap hide">
    <div class="paxSearchWrap">
        <input type="search" class="paxSearchInput" placeholder="<cms:contentText key="FIND_SOMEONE" code="participant.search.view" />" autocomplete="false">
        <i class="icon-magnifier-1"></i>
    </div>
     <div class="select-groups hide">
            <div class="dropdown">
                <a class="dropdown-toggle btn btn-inverse" data-toggle="dropdown" href="#"><span><cms:contentText key="MY_GROUPS" code="system.general" /></span> <i class="icon-arrow-1-down arrowToken"></i></a>
                <ul class="dropdown-menu groupTabs" role="menu" aria-labelledby="dLabel">
                <li class="group-prompt hide"><em><cms:contentText key="HAVENT_CREATED_ANY_GROUPS" code="system.general" /></em></li>
                <li class="create-li"><btn data-href="${pageContext.request.contextPath}/participantProfilePage.do#profile/Groups" class="btn btn-block btn-primary create-a-group"><cms:contentText key="CREATE_GROUP" code="system.general" /></btn></li>
                </ul>
            </div>
    </div><!-- /.select-groups -->
</div>

<div class="pubRecFilterWrap hide" >
    <span class="filterLabel"><cms:contentText key="FILTER_BY" code="participant.search.view" /></span>
    <div class="filterTokens">
    </div>
    <div class="dropdown">
        <a class="dropdown-toggle btn btn-primary btn-inverse" data-toggle="dropdown" href="#"><cms:contentText key="SELECT_TEAM" code="participant.search.view" /><b class="caret"></b></a>
        <ul class="dropdown-menu pubRecTabs" role="menu" aria-labelledby="dLabel">
        </ul>
    </div>
</div>
</script>

<script type="text/template" id="paxSearchViewTpl">
 <%@include file="/search/paxSearchView.jsp" %>
</script>

<script type="text/template" id="PaxSelectedPaxViewTpl">
 <%@include file="/search/PaxSelectedPaxView.jsp" %>
</script>

<script type="text/template" id="cheersRecognitionModelTpl">
	<%@include file="/client/cheersRecognitionModel.jsp" %>
</script>

<%@include file="/submitrecognition/easy/flipSide.jsp" %>