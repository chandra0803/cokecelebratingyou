<%@ include file="/include/taglib.jspf"%>
<!-- ======== HERO: HERO MODULE ======== -->
<script type="text/template" id="heroModuleTpl">
<div class="module-liner{{#if ${billBoardImageUrl}}}{{else}} default{{/if}}" data-size='<c:out value="${beacon:systemVarString('Billboard.Size')}"/>' 
{{#if ${billBoardImageUrl}}} data-img='"<cms:contentText key="BILL_BOARD_IMAGE" code="participant.search.view" />"' style='background-image:url("<cms:contentText key="BILL_BOARD_IMAGE" code="participant.search.view" />")'  {{/if}}>

   <c:if test="${sessionScope.promoGiver == true}">    
     <div class="paxSearchStartView" data-start-placeholder='<cms:contentText key="FIND_SOMEONE_TO_RECOGNIZE" code="participant.search.view" />'>
     </div><!-- /.paxSearchStartView -->
   </c:if>

    <%--<div class="ezRecView launchModule recognition recognitionModule hide">
    </div>--%><!-- /.ezRecView -->
</div> <!-- ./module-liner -->
<!-- TODO: ... -->
<!--tplVariable.paxSearchOn=
    ["recognition","programs"]
tplVariable-->
</script>


<%@include file="/search/paxSearchStart.jsp"%>
