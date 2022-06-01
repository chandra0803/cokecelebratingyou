<!-- ======== CELEBRATION COMPANY TIMELINE MODULE ======== -->
<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@page import="com.objectpartners.cms.util.CmsResourceBundle"%>
<%@ include file="/include/taglib.jspf"%>
<script type="text/template" id="celebrationCompanyTimelineModuleTpl">
  <div class="module-liner celebrationCompanyTimeline">
    <div class="wide-view">
      <div class="module-content">
        <h3 class="headline_3"><cms:contentText key="COMPANY_TIMELINE" code="celebration.company.timeline.new"/></h3>
        <h5><cms:contentText key="COMPANY_TIMELINE_HISTORY" code="celebration.company.timeline.new"/></h5>
		    <cms:content var="url" code="celebration.company.timeline.new"/>
        <c:set var="companyUrl" value="${url.contentDataMap['URL']}" />
        <c:choose>
          <c:when test="${fn:startsWith(companyUrl, 'http')}">
            <a href="<cms:contentText key="URL" code="celebration.company.timeline.new"/>" class="btn btn-primary timelineBtn" target="_blank"><cms:contentText key="VIEW_TIMELINE" code="celebration.company.timeline.new"/></a>
          </c:when>
          <c:otherwise>
            <a href="<%= RequestUtils.getBaseURI( request )%>/<cms:contentText key="URL" code="celebration.company.timeline.new"/>" class="btn btn-primary timelineBtn" target="_blank"><cms:contentText key="VIEW_TIMELINE" code="celebration.company.timeline.new"/></a>
          </c:otherwise>
        </c:choose>



      </div> <!-- ./module-content -->
    </div>

  </div>
</script>
