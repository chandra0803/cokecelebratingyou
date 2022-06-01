<%@ include file="/include/taglib.jspf" %>

<!-- ======== SA CONTRIBUTE: CONTRIBUTER: LIST ======== -->

<div id="SAContributeListView" class="SAContributeListView page-content">
	<div id="SAContributeListViewWrapper" class="row-fluid">
        <div class="span12">
            <h5><cms:contentText key="INSTRUCTION_SA_COPY" code="purl.contribution.list"/></h5>

            <div id="PURLContributeListTable">
            	<table class="table table-striped PURLContributeListTable">
            		<thead>
            			<tr>
            				<th><cms:contentText key="RECIPIENT" code="purl.contribution.list"/></th>
            				<th><cms:contentText key="COUNTRY" code="purl.contribution.list"/></th>
            				<th><cms:contentText key="CONTRIBUTORS" code="purl.contribution.list"/></th>
            				<th><cms:contentText key="AWARD_DATE" code="purl.contribution.list"/></th>
            				<th></th>
            			</tr>
            		</thead>
            		<tbody>
						<c:forEach var="saContribution" items="${saContributionList}">
							<tr>
	            				<td>
	            					<c:out value="${saContribution.recipient.firstName}"/> <c:out value="${saContribution.recipient.lastName}"/>
	            				</td>
	            				<td>
	            					<img src="${siteUrlPrefix}/assets/img/flags/${saContribution.recipient.primaryCountryCode}.png" title="${saContribution.recipient.primaryCountryName}"/>
	            				</td>
	            				<td>
	            					<div class="purlContributers">
	    	        					<p><c:out value="${saContribution.inviteCount}"/> <cms:contentText key="CONTRIBUTOR_INVITED_LABEL" code="purl.contribution.list"/></p>
			        					<p><c:out value="${saContribution.viewCount}"/> <cms:contentText key="CONTRIBUTOR_VISITED_LABEL" code="purl.contribution.list"/></p>		        					
	            					</div>
	            				</td>
	            				<td>
	            					<fmt:formatDate value='${saContribution.awardDate}' pattern='${JstlDatePattern}'/>
	            				</td>
	            				<td>
	            					<button class="btn btn-primary sa-action" name="saContribute" data-cid="${saContribution.celebrationId }"><cms:contentText key="CONTRIBUTE_BUTTON" code="purl.contribution.list"/></button>
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
		pccpl = new NewServiceAnniversaryContributePageView({
    		el:$('#SAContributeListView'),
            pageTitle : '<cms:contentText key="HEADER" code="purl.contribution.list"/>'
        });
    });
</script>