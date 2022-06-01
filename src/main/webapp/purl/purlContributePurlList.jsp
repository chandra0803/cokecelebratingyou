<%@ include file="/include/taglib.jspf" %>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

<!-- ======== PURLCONTRIBUTE: CONTRIBUTER: PURL LIST ======== -->

<div id="PURLContributeListView" class="PURLContributeListView page-content">
	<div id="PURLContributeListViewWrapper" class="row-fluid">
        <div class="span12">
			<c:choose>
				<c:when test="${isNewSAEnabled == true}">
					<h5><cms:contentText key="INSTRUCTION_SA_COPY" code="purl.contribution.list"/></h5>
				</c:when>    
				<c:otherwise>
					<h5><cms:contentText key="INSTRUCTION_COPY" code="purl.contribution.list"/></h5>
				</c:otherwise>
			</c:choose>
            <div id="PURLContributeListTable">
            	<table class="table table-striped PURLContributeListTable">
            		<thead>
            			<tr>
            				<th><cms:contentText key="RECIPIENT" code="purl.contribution.list"/></th>
            				<th><cms:contentText key="COUNTRY" code="purl.contribution.list"/></th>
            				<th><cms:contentText key="CONTRIBUTORS" code="purl.contribution.list"/></th>
            				<th><cms:contentText key="CONTRIBUTION_END_DATE" code="purl.contribution.list"/></th>
            				<th><cms:contentText key="AWARD_DATE" code="purl.contribution.list"/></th>
            				<th></th>
            			</tr>
            		</thead>
            		<tbody>
						<c:forEach var="purlContribution" items="${purlContributionList}">
							<tr>
	            				<td>
	            					<c:out value="${purlContribution.purlRecipient.user.firstName}"/> <c:out value="${purlContribution.purlRecipient.user.lastName}"/>
	            				</td>
	            				<td>
	            					<img src="${siteUrlPrefix}/assets/img/flags/${purlContribution.purlRecipient.user.primaryCountryCode}.png" title="${purlContribution.purlRecipient.user.primaryCountryName}"/>
	            				</td>
	            				<td>
	            					<div class="purlContributers">
	    	        					<p><c:out value="${purlContribution.purlRecipient.contributorInvited}"/> <cms:contentText key="CONTRIBUTOR_INVITED_LABEL" code="purl.contribution.list"/></p>
			        					<p><c:out value="${purlContribution.purlRecipient.contributorViewed}"/> <cms:contentText key="CONTRIBUTOR_VISITED_LABEL" code="purl.contribution.list"/></p>		        					
	            					</div>
	            				</td>
	            				<td>
	            					<fmt:formatDate value='${purlContribution.purlRecipient.closeDate}' pattern='${JstlDatePattern}'/>
	            				</td>	
	            				<td>
	            					<fmt:formatDate value='${purlContribution.purlRecipient.awardDate}' pattern='${JstlDatePattern}'/>
	            				</td>
	            				<td>
	            					<c:set var="purlContributorId" value="${purlContribution.id}" />
	            					<%            						
										Map<String,Object> purlContributionParamMap = new HashMap<String,Object>();
										purlContributionParamMap.put( "purlContributorId", (Long)pageContext.getAttribute("purlContributorId") );
										pageContext.setAttribute("purlContributionLinkUrl", ClientStateUtils.generateEncodedLink( "", "purlTNC.do?method=display", purlContributionParamMap ) );
									%>
	            					<button class="btn btn-primary" name="PurlContribute" onclick="window.location='${purlContributionLinkUrl}'"><cms:contentText key="CONTRIBUTE_BUTTON" code="purl.contribution.list"/></button>
	            				</td>
	            			</tr>					
						</c:forEach>
            		</tbody>
            	</table>
            </div>
        </div>
	</div>
</div>

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>	
	var pccpl;

	$(document).ready(function(){				
		pccpl = new PageView({
    		el:$('#PURLContributeListView'),
            pageTitle : '<cms:contentText key="HEADER" code="purl.contribution.list"/>'
        });
    });
</script>