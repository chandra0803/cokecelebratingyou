<%@ page import="java.util.*"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.utils.DateUtils"%>

<script>
  $(document).ready(function() {

      G5.props.URL_JSON_PARTICIPANT_INFO = "${pageContext.request.contextPath}/participantPublicProfile.do?method=populatePax";

      //Mini Profile PopUp Follow Unfollow Pax JSON
      G5.props.URL_JSON_PARTICIPANT_FOLLOW = G5.props.URL_ROOT+'publicRecognitionFollowUnFollowPax.do?method=followUnFollowPax';

  });
</script>

<div class="modal hide fade autoModal autoModalInviteContributors">
    <div class="modal-header">
        <button class="close" data-dismiss="modal"><i class="icon-close"></i></button>

        <h3><cms:contentText key="INVITES_SENT" code="purl.contributor"/></h3>
    </div>

    <div class="modal-body">
        <!-- status class is one of the following: successStatus, existsStatus, failStatus -->
        <c:forEach var="invitee" items="${invites}" varStatus="istatus">
        <ul class="invitees unstyled">
            <!-- successStatus -->
            <c:if test="${invitee.status eq 'success'}">
            <li class="successStatus inviteeResultItem">
                <i class="icon-check-circle goodRes"></i>
                <!-- JAVA: this person is a participant in the program -->
                <c:choose>
                  <c:when test="${not empty invitee.lastName || not empty invitee.firstName}">
                	<strong>
                    <span class="name"><c:out value="${invitee.lastName}"/>, <c:out value="${invitee.firstName}"/></span>

                    <img src="${siteUrlPrefix}/assets/img/flags/${invitee.countryCode}.png" class="countryFlag" title="${invitee.countryName}"/>
                	</strong>
                	<span class="org">
                    <!-- JAVA: you will need to convert the following Handlebars logic to JSP logic to make all the permutations of orgName/departmentName/jobName display correctly with bullets in between:
                    {{orgName}} {{#if departmentName}}{{#if orgName}}&bull;{{/if}} {{departmentName}}{{/if}} {{#if jobName}}{{#if departmentName}}&bull;{{else}}{{#if orgName}}&bull;{{/if}}{{/if}} {{jobName}}{{/if}}
                    -->
                    <c:out value="${invitee.orgName}"/> &bull; <c:out value="${invitee.department}"/> &bull; <c:out value="${invitee.jobName}"/>
                	</span>
                  </c:when>
                  <c:otherwise>
                 	<strong>
                    	<span class="name"><c:out value="${invitee.emailAddr}"/></span>
                	</strong>
                  </c:otherwise>
                </c:choose>
            </li>
            </c:if>
            <!-- existsStatus -->
            <c:if test="${invitee.status eq 'exists'}">
            <li class="existsStatus inviteeResultItem">
                <i class="icon-check-circle neutralRes"></i>
                <!-- JAVA: this person is NOT a participant in the program -->
                <!-- JAVA: if no first or last name exists, leave out this <span> entirely -->
                <c:if test="${not empty invitee.lastName || not empty invitee.firstName}">
                <span class="name"><c:out value="${invitee.lastName}"/>, <c:out value="${invitee.firstName}"/></span>
                </c:if>
                <span class="email"><c:out value="${invitee.emailAddr}"/></span>
            </li>
            </c:if>
            <!-- failStatus -->
            <c:if test="${invitee.status eq 'fail'}">
            <li class="failStatus inviteeResultItem">
                <i class="icon-cancel-circle badRes"></i>
                <!-- JAVA: this person is NOT a participant in the program -->
                <span class="email"><c:out value="${invitee.emailAddr}"/></span>
            </li>
            </c:if>
        </ul>
       	<c:set var="invitingContributorId" value="${invitee.invitingContributorId}" />
        </c:forEach>

        <hr>

        <i class="icon-check-circle goodRes"></i> <cms:contentText key="ADDED" code="purl.contributor"/>&nbsp;&nbsp;
        <i class="icon-check-circle neutralRes"></i> <cms:contentText key="EXISTS" code="purl.contributor"/>&nbsp;&nbsp;
        <i class="icon-cancel-circle badRes"></i> <cms:contentText key="INVITE_FAILED" code="purl.contributor"/>
    </div><!-- /.modal-body -->

    <div class="modal-footer">
        <a href="#" class="btn btn-fullmobile" data-dismiss="modal"><cms:contentText key="CLOSE" code="purl.common"/></a>
        <%
            Map purlContributionParamMap = new HashMap();
			purlContributionParamMap.put( "purlContributorId", (Long)pageContext.getAttribute("invitingContributorId") );
            pageContext.setAttribute("purlContributionLinkUrl", ClientStateUtils.generateEncodedLink( "", "purl/purlTNC.do?method=display", purlContributionParamMap) );
        %>
        <a href="${purlContributionLinkUrl}" class="btn btn-primary btn-fullmobile"><cms:contentText key="CONTRIBUTE_PURL" code="purl.invitation.list"/></a>
    </div>

</div>
