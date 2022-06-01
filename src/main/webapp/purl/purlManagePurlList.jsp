<%@page import="com.biperf.core.utils.UserManager"%>
<%@ include file="/include/taglib.jspf" %>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.domain.purl.PurlRecipient" %>

<!-- ======== PURLCONTRIBUTE: MANAGER: PURL LIST ======== -->

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
                        <tr id="tableHeader">
                            <th><cms:contentText key="VIEW_ADD_CONTRIBUTORS" code="purl.invitation.list"/></th>
                            <th class="sortHeader sorted">
                                <a class="sortControl" data-sort="nodeName" data-sort-direction="up" data-sort-type="string">
                                    <cms:contentText key="PROMOTION_NAME_LABEL" code="purl.invitation.list"/>
                                    <i class="icon-arrow-1-up"></i>
                                    <i class="icon-arrow-1-down"></i>
                                </a>
                            </th>
                            <th class="sortHeader unsorted">
                                <a class="sortControl" data-sort="recipientName" data-sort-direction="up" data-sort-type="string">
                                    <cms:contentText key="RECIPIENT" code="purl.invitation.list"/>
                                    <i class="icon-arrow-1-up"></i>
                                    <i class="icon-arrow-1-down"></i>
                                </a>
                            </th>
                            <th>
                                <cms:contentText key="CONTRIBUTORS" code="purl.invitation.list"/>
                            </th>
                            <th class="sortHeader unsorted">
                                <a class="sortControl" data-sort="endDate" data-sort-direction="up" data-sort-type="number">
                                    <cms:contentText key="CONTRIBUTION_END" code="purl.invitation.list"/>
                                    <i class="icon-arrow-1-up"></i>
                                    <i class="icon-arrow-1-down"></i>
                                </a>
                            </th>
                            <th class="sortHeader unsorted">
                                <a class="sortControl" data-sort="awardDate" data-sort-direction="up" data-sort-type="number">
                                    <cms:contentText key="AWARD_DATE" code="purl.invitation.list"/>
                                    <i class="icon-arrow-1-up"></i>
                                    <i class="icon-arrow-1-down"></i>
                                </a>
                            </th>
                            <th></th>
                        </tr>
                    </thead>

                    <tbody>
                        <c:forEach var="purlInvitation" items="${purlInvitationList}" varStatus="purlInvitationIndex">
                            <%
                                PurlRecipient purlRecipient = (PurlRecipient)pageContext.getAttribute( "purlInvitation" );
                                String contextPath = (String) pageContext.getRequest().getAttribute("siteUrlPrefix");
                                Map<String,Object> parameterMap = new HashMap<String,Object>();
                                parameterMap.put( "purlRecipientId", purlRecipient.getId() );
                                parameterMap.put( "purlReturnUrl", RequestUtils.getOriginalRequestURIWithQueryString(request) );
                                pageContext.setAttribute("inviteContributorsLink", ClientStateUtils.generateEncodedLink( "", contextPath + "/recognitionWizard/purlInviteContributors.do?method=display", parameterMap ) );
                            %>
                            <tr name="row${purlInvitationIndex.index + 1}">
                                <td class="addViewContributors">
                                    <a class="btn btn-block btn-primary" href="<c:out value="${inviteContributorsLink}"/>"><cms:contentText key="VIEW_ADD_CONTRIBUTORS" code="purl.invitation.list"/></a>
                                </td>
                                <td class="nodeName"><c:out value="${purlInvitation.promotion.name}"/></td>
                                <td>
                                    <a class="participant-popover recipientName" href="#" data-participant-ids="[${purlInvitation.user.id}]">
                                        <c:out value="${purlInvitation.user.lastName}"/>, <c:out value="${purlInvitation.user.firstName}"/>
                                        <img src="${siteUrlPrefix}/assets/img/flags/${purlInvitation.user.primaryCountryCode}.png" class="countryFlag" title="${purlInvitation.user.primaryCountryName}"/>
                                    </a>
                                    <input type="hidden" id="purlRecipientId" value="${purlInvitation.id}">
                                </td>
                                <td>
                                    <div class="purlContributers">
                                        <span class="invited"><c:out value="${purlInvitation.contributorInvited}"/> <cms:contentText key="CONTRIBUTOR_INVITED_LABEL" code="purl.invitation.list"/></span>
                                        <span class="viewed"><c:out value="${purlInvitation.contributorViewed}"/> <cms:contentText key="CONTRIBUTOR_VISITED_LABEL" code="purl.invitation.list"/></span>
                                    </div>
                                </td>
                                <td id="endDate" class="endDate">
                                    <fmt:formatDate value='${purlInvitation.closeDate}' pattern='${JstlDatePattern}'/>
                                    <input type="hidden" id="sort-date" value="${purlInvitation.closeDate.time}">
                                </td>
                                <td id="awardDate" class="awardDate">
                                  <beacon:authorize ifNotGranted="LOGIN_AS">
                                    <a class="PURLAwardDateEdit" data-date-format="<%=UserManager.getUserDatePattern().toLowerCase()%>" data-date-language="<%=UserManager.getUserLocale()%>"><fmt:formatDate value='${purlInvitation.awardDate}' pattern='${JstlDatePattern}'/></a>
                                  </beacon:authorize>
                                  <beacon:authorize ifAnyGranted="LOGIN_AS">
                                    <fmt:formatDate value='${purlInvitation.awardDate}' pattern='${JstlDatePattern}'/>
                                  </beacon:authorize>
                                    <input type="hidden" id="sort-date" value="${purlInvitation.awardDate.time}">
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty purlInvitation.managerContributor}">
                                            <c:set var="purlContributorId" value="${purlInvitation.managerContributor.id}" />
                                            <%
                                                Map purlContributionParamMap = new HashMap();
                                                purlContributionParamMap.put( "purlContributorId", (Long)pageContext.getAttribute("purlContributorId") );
                                                pageContext.setAttribute("purlContributionLinkUrl", ClientStateUtils.generateEncodedLink( "", "purlTNC.do?method=display", purlContributionParamMap ) );
                                            %>
                                            <a class="btn btn-block btn-primary btn-inverse" href="${purlContributionLinkUrl}">
                                                <cms:contentText key="CONTRIBUTE_PURL" code="purl.invitation.list"/>
                                            </a>
                                        </c:when>
                                        <c:otherwise>
                                            <c:if test="${not empty purlInvitation.contributors}">
                                            <c:set var="purlRecipientId" value="${purlInvitation.id}" />
                                            <%
                                                Map paramMap = new HashMap();
                                                paramMap.put( "purlRecipientId", (Long)pageContext.getAttribute("purlRecipientId") );
                                                pageContext.setAttribute("purlRecipientLinkUrl", ClientStateUtils.generateEncodedLink( "", "purlRecipient.do?method=display", paramMap ) );
                                            %>
                                            <a class="btn btn-block btn-primary btn-inverse" href="${purlRecipientLinkUrl}">
                                                <cms:contentText key="VIEW_PURL" code="purl.invitation.list"/>
                                            </a>
                                            </c:if>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>

                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<script id="PURLActivityTable-Template" type="text/x-handlebars-template">
</script>

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
    var pccpl2;

    $(document).ready(function(){
        G5.props.URL_JSON_PURL_POST_NEW_AWARD_DATE = G5.props.URL_ROOT+'purl/purlMaintenance.do?method=awardDateUpdate';

        //Mini Profile Popup JSON
        G5.props.URL_JSON_PARTICIPANT_INFO = G5.props.URL_ROOT + 'participantPublicProfile.do?method=populatePax';

        //Mini Profile PopUp Follow Unfollow Pax JSON
        G5.props.URL_JSON_PARTICIPANT_FOLLOW = G5.props.URL_ROOT+'publicRecognitionFollowUnFollowPax.do?method=followUnFollowPax';

        pccpl2 = new PurlContributePurlListPageView({
            el:$('#PURLContributeListView'),
            pageTitle : '<cms:contentText key="HEADER" code="purl.invitation.list"/>'
        });
    });
</script>

<%@include file="/submitrecognition/easy/flipSide.jsp"%>
