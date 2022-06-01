<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<div id="resourceCenterPageView" class="resourceCenterPage-liner page-content">
 <c:choose>
        <c:when test = "${promotion.webRulesActive}">
      <div class="row-fluid">
        <div class="span12">
        <ul class="export-tools fr">
                <li><a class="pageView_print btn btn-small" href="#"><cms:contentText key="PRINT" code="system.button"/> <i class="icon-print"></i></a></li>
            </ul>
 		<h2><!--<cms:contentText key="PROMO_NAME" code="participant.promotions"/>:--><c:out value = "${promotion.name}"/></h2>
 		 </div>
    </div>
    <div class="row-fluid">
        <div class="span12">
        <table><tr>
        <td><h5><cms:contentText key="PROGRAM_PERIOD" code="promotion.goalquest.selection.wizard"/></h5></td>
        <td><fmt:formatDate value="${ promotion.submissionStartDate }" type="date" pattern="${sessionScope.loggedInUserDate}"/> <cms:contentText key="THROUGH" code="promotion.goalquest.selection.wizard"/> <fmt:formatDate value="${promotion.submissionEndDate }" type="date" pattern="${sessionScope.loggedInUserDate}"/></td>
        </tr>
        </table>
          </div>
    </div>
    <div class="row-fluid">
        <div class="span12">
            <cms:contentText key="${ promotion.webRulesCmKey }" code= "${ promotion.cmAssetCode }"/>
            </div>
     </div>
 		</c:when>
 		<c:otherwise>
 		 <div class="row-fluid">
        <div class="span12">
 		  <h4><cms:contentText key="NO_RULES_AVAILABLE" code="promotion.form.rules" /></h4>
 		  </div>
 		  </div>
 		</c:otherwise>
 		</c:choose>
</div>
	<!-- ADDED TEMPORARILY FOR VIEW -THIS NEEDS TO CHANGE  -->
<script>
	$(document).ready(function(){
	    //attach the view to an existing DOM element
	    var rcpv = new ResourceCenterPageView({
	        el:$('#resourceCenterPageView'),
	        pageNav : {
	            back : {
	                text : '<cms:contentText key="BACK" code="system.button" />',
	                url : 'javascript:history.go(-1);'
	            },
	            home : {
	                text : '<cms:contentText key="HOME" code="system.general" />',
	                url : '${pageContext.request.contextPath}/homePage.do'
	            }
	        },
	        pageTitle :'<cms:contentText key="RULES" code="promotion.stackrank.history"/>'
	    });
	});
</script>
