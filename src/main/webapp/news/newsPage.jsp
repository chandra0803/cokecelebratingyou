<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<!-- ======== NEWS PAGE ======== -->

<div id="newsPageView" class="newsPage-liner page-content">
</div>

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
$(document).ready(function() {

	G5.props.URL_JSON_NEWS = '<%=RequestUtils.getBaseURI(request)%>/newsResult.do';
    //attach the view to an existing DOM element
    var cpv = new NewsPageView({
        el:$('#newsPageView'),
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
        pageTitle : '<cms:contentText key="COMMUNICATION" code="hometile.communication" />'
    });

});
</script>

<script type="text/template" id="newsCollectionViewTpl">
    <div class="page-topper">
        <div class="row-fluid">
            <div class="span12">
                <form class="form-inline">
                    <label><cms:contentText key="SORT_BY" code="hometile.communication" /></label>
                    <select id="sortBy">
                        <option value="date"{{#if sortedByDate}} selected="selected"{{/if}}><cms:contentText key="MOST_RECENT" code="hometile.communication"/></option>
                        <option value="name"{{#if sortedByName}} selected="selected"{{/if}}><cms:contentText key="ALPHA_SORT" code="hometile.communication"/></option>
                    </select>
                </form>
            </div>
        </div>
    </div>
    <div class="row-fluid">
        <div class="span12">
            <h2><cms:contentText key="RECENT_NEWS" code="hometile.communication"/></h2>
        </div>
    </div>
    <div id="collectionOfStories">
    </div>
</script>

<script type="text/template" id="newsPageDetailItemTpl">
    <%@include file="/news/newsPageDetailItem.jsp" %>
</script>

<script type="text/template" id="newsPageItemTpl">
    <%@include file="/news/newsPageItem.jsp" %>
</script>
