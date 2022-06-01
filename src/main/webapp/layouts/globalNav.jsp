<%@ include file="/include/taglib.jspf"%>
<script type="text/template" id="globalNavViewTpl">
<div id="globalNavView">
    <div class="nav">

    <beacon:authorize ifAnyGranted="MANAGER,PROCESS_TEAM,BI_ADMIN,PROJ_MGR,VIEW_REPORTS">
        <c:set var="reportEligible" value="true"/>
    </beacon:authorize>

     <c:forEach var="filterItem" items="${filterList}">
          <c:choose>
               <c:when test="${filterItem.reportFilter }">
                    <c:if test="${reportEligible}">
                            <span class='item <c:out value="${filterItem.code }"/>Filter {{#eq active "<c:out value="${filterItem.code }"/>"}}active{{/eq}}'>
                                <a href='${pageContext.request.contextPath}/homePage.do#launch/<c:out value="${filterItem.code }"/>'><c:out value="${filterItem.name }"/></a>
                            </span>
                    </c:if>
               </c:when>
               <c:otherwise>
                <span class='item <c:out value="${filterItem.code }"/>Filter {{#eq active "<c:out value="${filterItem.code }"/>"}}active{{/eq}}'>
                       <a href='${pageContext.request.contextPath}/homePage.do#launch/<c:out value="${filterItem.code }"/>'><c:out value="${filterItem.name }"/></a>
                   </span>
               </c:otherwise>
          </c:choose>
     </c:forEach>
    </div>
</div><!-- /#globalNavView -->
</script>
