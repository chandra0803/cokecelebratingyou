<%@ include file="/include/taglib.jspf"%>


<script type="text/template" id="reportsModuleTpl">
<!-- ======== REPORTS MODULE ======== -->

<div class="module-liner reportsModule">

    <div class="title-icon-view">

        <h3><cms:contentText key="REPORTS" code="report.dashboard"/></h3>

    </div>

</div>

<!--subTpl.favoriteTpl=
<i class="icon-g6-reorder"></i>
    <a href="{{clickThruUrl}}" class="visitAppBtn invert">
        <i class="icon-arrow-2-circle-right"></i>
    </a>

    <div class="title-icon-view">
        <h3>{{displayName}}</h3>
        <i class="icon-g5-reportsfav"></i>
    </div>
    <div class="wide-view">
        <div class="description">
            <h4 class="displayName">{{displayName}}</h4>
            <p class="parameters">{{favoriteParameters}}</p>
        </div>

        <div id="chartContainer{{reportIndex}}" class="chartContainer"><span class="spin"></span></div>
    </div>
subTpl-->
</script>

<script type="text/template" id="reportsPageAllTpl">
    <%@include file="/reports/reportsPageAll.jsp" %>
</script>
<script type="text/template" id="reportsFavoritesPopoverViewTpl">
    <!-- ======== popover for report favorites ======== -->
    <%@include file="/reports/reportsFavoritesPopoverView.jsp" %>
</script>
