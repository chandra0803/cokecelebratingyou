<!-- ======== CELEBRATION BROWSE MODULE ======== -->
<%@ include file="/include/taglib.jspf"%>
<%@page import="com.biperf.core.ui.utils.RequestUtils"%>

<script type="text/template" id="celebrationBrowseAwardsModuleTpl">
<div class="module-liner">
    <div class="wide-view">
        <div class="module-content">

            <h3 class="headline_3"><cms:contentText key="AWARDS" code="celebration.browse.awards" /></h3>
            <div class="module-actions">
	           	<beacon:authorize ifNotGranted="LOGIN_AS">
                	<a class="visitAppBtn" href="${celebrationValue.merchLinqShoppingUrl}">
                   		<cms:contentText key="VIEW_ALL" code="celebration.recognition.purl"/>
                	</a>
   				</beacon:authorize>
				<beacon:authorize ifAnyGranted="LOGIN_AS">
                	<a class="visitAppBtn" href="#">
                   		<cms:contentText key="VIEW_ALL" code="celebration.recognition.purl"/>
                    </a>
 				 </beacon:authorize>
            </div>
            <div id="awardsCarousel" class="carousel">
                <div class="cycle carousel-inner">
				<c:forEach items="${ celebrationValue.merchProducts }" var="product" varStatus="status">

                    <div class="item">
                        <div class="item-wrap">
							<beacon:authorize ifNotGranted="LOGIN_AS">
               					<a href="${product.url}" target="_blank">
                                	<img src="${product.img}" alt="<cms:contentText key="AWARD_PRODUCT_TITLE" code="celebration.browse.awards" />">
                            	</a>
							</beacon:authorize>
							<beacon:authorize ifAnyGranted="LOGIN_AS">
        						<img src="${product.img}" alt="<cms:contentText key="AWARD_PRODUCT_TITLE" code="celebration.browse.awards" />">
							</beacon:authorize>
                        </div>
                        <div class="carousel-caption">
							<beacon:authorize ifNotGranted="LOGIN_AS">
                            	<p><a href="${product.url}" target="_blank">${product.name}</a></p>
							</beacon:authorize>
							<beacon:authorize ifAnyGranted="LOGIN_AS">
           						<p>${product.name}</p>
							</beacon:authorize>
                        </div>
                    </div>

				</c:forEach>

                </div>

            </div> <!-- ./carousel -->
        </div> <!-- ./module-content -->
    </div>

</div>
</script>


