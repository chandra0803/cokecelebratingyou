<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>
<!-- ======== THROWDOWN NEWS PAGE ======== -->

<div id="newsPageView" class="newsPage-liner page-content">
</div>

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
    $(document).ready(function(){
   	 G5.props.URL_JSON_TD_NEWS = '<%=RequestUtils.getBaseURI(request)%>/throwdownNewsResult.do';
    //attach the view to an existing DOM element
    var cpv = new ThrowdownNewsPageView({
        el:$('#newsPageView'),
        pageTitle : '<cms:contentText key="THROWDOWNNEWS" code="hometile.throwdownNews"/>'
    });
  });
</script>

<script type="text/template" id="throwdownNewsCollectionViewTpl">
    <div class="page-topper">
        <div class="row-fluid">
            <div class="span12">
                <form class="form-inline">
                    <label><cms:contentText key="SORT_BY" code="hometile.throwdownNews"/></label>
                    <select id="sortBy">
                        <option value="date"{{#if sortedByDate}} selected="selected"{{/if}}><cms:contentText key="MOST_RECENT" code="hometile.throwdownNews"/></option>
                        <option value="name"{{#if sortedByName}} selected="selected"{{/if}}><cms:contentText key="ALPHA_SORT" code="hometile.throwdownNews"/></option>       
                    </select>
                </form>
            </div>
        </div>
    </div>
    <div class="row-fluid">
        <div class="span12">
            <h2><cms:contentText key="RECENT_NEWS" code="hometile.throwdownNews"/></h2>
        </div>
    </div>
    <div id="collectionOfStories">
    </div>
</script>

<script type="text/template" id="throwdownNewsPageDetailItemTpl">
    <%@include file="/throwdown/newsPageDetailItem.jsp" %>
</script>

<script type="text/template" id="throwdownNewsPageItemTpl">
    <%@include file="/throwdown/newsPageItem.jsp" %>
</script>

