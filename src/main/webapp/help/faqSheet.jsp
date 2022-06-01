<%@ include file="/include/taglib.jspf" %>

<div id="faqPageView" class="page page-content globalFaq">
	<div name="main" id="main" class="row-fluid">
        <div class="span12">
            <h2><cms:contentText code="help.faq.list" key="FAQs"/></h2>
            <!--  <cms:assetsByType var="assetList" name="FAQ Group"/>
            <c:forEach var="asset" items="${assetList}">
                <c:if test="${asset.parentAsset.code == 'help.faq.list'}">
                    <cms:content var="faqGroup" code="${asset.code}"/>
                    <c:if test="${faqGroup.id != null}">
						<c:choose>
							<c:when test="${fn:length(assetList) > 1}">
								<ul>
									<li><a href="#s<c:out escapeXml="false" value='${faqGroup.id}'/>"><c:out escapeXml="false" value='${faqGroup.contentDataMap["TITLE"]}'/></a></li>
								</ul>
							</c:when>
							<c:otherwise>
							    <a href="#s<c:out escapeXml="false" value='${faqGroup.id}'/>"><c:out escapeXml="false" value='${faqGroup.contentDataMap["TITLE"]}'/></a>
							</c:otherwise>
						</c:choose>
					</c:if>
                </c:if>
            </c:forEach> -->

            <cms:assetsByType var="assetList" name="FAQ Group"/>
            <c:forEach var="asset" items="${assetList}">
                <c:if test="${asset.parentAsset.code == 'help.faq.list'}">
                    <cms:content var="faqGroup" code="${asset.code}"/>
                    <c:if test="${faqGroup.id != null}">
                        <h3 class="questionHeader" id="s<c:out escapeXml="false" value='${faqGroup.id}'/>"><c:out escapeXml="false" value='${faqGroup.contentDataMap["TITLE"]}'/></h3>

                        <ul class="questionList">
                        <cms:assetsByType var="entryAssetList" name="FAQ Entry"/>
                        <c:forEach var="entryAsset" items="${entryAssetList}">
                            <c:if test="${entryAsset.parentAsset.code == asset.code}">
                                <cms:content var="faqItems" code="${entryAsset.code}"/>
                                <c:forEach var="faqEntry" items="${faqItems}">
                                    <li><cms:contentText key="QUESTION_LABEL" code="help.faq.list"/> <a href="#q<c:out escapeXml="false" value='${faqEntry.id}'/>"><c:out escapeXml="false" value='${faqEntry.contentDataMap["QUESTION"]}'/></a></li>
                                </c:forEach>
                            </c:if>
                        </c:forEach>
                        </ul><!-- /.questionList -->
                    </c:if>
                </c:if>
            </c:forEach>

            <p class="backToTop"><a href="#main"><i class="icon-back-4"></i> <cms:contentText code="help.faq.list" key="BACK_TO_TOP"/></a></p>

            <cms:assetsByType var="assetList" name="FAQ Group"/>
            <c:forEach var="asset" items="${assetList}">
                <c:if test="${asset.parentAsset.code == 'help.faq.list'}">
                    <cms:content var="faqGroup" code="${asset.code}"/>
					<c:if test="${faqGroup.id != null}">
                        <h3 class="answerHeader"><c:out escapeXml="false" value='${faqGroup.contentDataMap["TITLE"]}'/></h3>

                        <ul class="answerList unstyled">
                        <cms:assetsByType var="entryAssetList" name="FAQ Entry"/>
                        <c:forEach var="entryAsset" items="${entryAssetList}">
                            <c:if test="${entryAsset.parentAsset.code == asset.code}">
                                <cms:content var="faqItems" code="${entryAsset.code}"/>
                                <c:forEach var="faqItem" items="${faqItems}">
                                    <li id="q<c:out escapeXml="false" value='${faqItem.id}'/>">
                                        <h4><c:out escapeXml="false" value='${faqItem.contentDataMap["QUESTION"]}'/></h4>
                                        <blockquote>
                                            <p>
                                                <c:out escapeXml="false" value='${faqItem.contentDataMap["ANSWER"]}'/>
                                                <span class="backToTop"><a href="#main"><i class="icon-back-4"></i> <cms:contentText code="help.faq.list" key="BACK_TO_TOP"/></a></span>
                                            </p>
                                        </blockquote>
                                    </li>
                                </c:forEach>
                            </c:if>
                        </c:forEach>
                        </ul><!-- /.answerList.unstyled -->
                    </c:if>
                </c:if>
            </c:forEach>
        </div><!-- /.span12 -->
    </div><!-- /.row-fluid -->
</div><!-- /#faqPageView.page-content.globalFaq -->
