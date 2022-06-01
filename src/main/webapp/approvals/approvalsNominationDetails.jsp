<!-- ======== APPROVALS: APPROVAL DETAILS ======== -->
<%--UI REFACTORED--%>
<%@page import="com.biperf.core.utils.UserManager"%>
<%@ page import="java.util.*"%>
<%@ page import="com.biperf.core.domain.claim.NominationClaim"%>
<%@ page import="com.biperf.core.domain.claim.Claim"%>
<%@ page import="com.biperf.core.domain.claim.ClaimGroup"%>
<%@ page
    import="com.biperf.core.ui.approvals.ApprovalsNominationDetailsForm"%>
<%@ page import="java.lang.ClassCastException"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>
<%@ include file="/include/taglib.jspf"%>
<!-- ======== APPROVALS: APPROVAL DETAILS ======== -->
<script type="text/javascript">
</script>
<div id="approvalsNominationDetailView" class="approvalSearchWrapper page-content">

    <div class="row-fluid">
        <div class="span12">
            <div id="approvalErrorBlock" class="alert alert-block alert-error" style="display:none">
                <h4><cms:contentText key="FOLLOWING_ERRORS" code="system.generalerror" /></h4>
                <ul>
                    <html:messages id="actionMessage" >
                        <c:set var="serverReturnedError" value="true"/>
                        <li>${actionMessage}</li>
                    </html:messages>
                 </ul>
                 <input type="hidden" id="serverReturnedErrored" value="${serverReturnedError}">
            </div>
        </div>
    </div>

	<div id="approvalsNominationDetailWrapper">
        <html:form action="approvalsNominationDetailsUpdate.do?method=saveApprovals" styleClass="form-horizontal">
            <html:hidden property="method" />
            <html:hidden property="approvableTypeCode" />
            <html:hidden property="viewApprovalStatusCode" />
            <html:hidden property="claimGroupStartDate" />
            <html:hidden property="claimGroupEndDate" />
            <html:hidden property="publicationDate"
                value="${approvalsNominationDetailsForm.publicationDateActive}" />
            <html:hidden property="version" />
            <beacon:client-state>
                <beacon:client-state-entry name="approvableId"
                    value="${approvalsNominationDetailsForm.approvableId}" />
                <beacon:client-state-entry name="claimGroupPaxId"
                    value="${approvalsNominationDetailsForm.claimGroupPaxId}" />
                <beacon:client-state-entry name="claimGroupNodeId"
                    value="${approvalsNominationDetailsForm.claimGroupNodeId}" />
                <beacon:client-state-entry name="promotionId"
                    value="${approvalsNominationDetailsForm.promotionId}" />
            </beacon:client-state>

            <div class="row-fluid">
                <div class="span10">
                    <h3>
                        <c:out value="${approvable.promotion.name}" />
                    </h3>
                </div>

                <div class="span2">
                    <ul class="export-tools fr">
                        <li><a href="#" class="pageView_print btn btn-small"><cms:contentText code="system.button" key="PRINT" /> <i class="icon-printer"></i> </a></li>
                    </ul>
                </div>
            </div>

            <div class="row-fluid">

                <c:if test="${ ecardForNominationApproval ne null && ecardForApproval != '' }">
                    <div id="ecardImage" class="ecard-span span4" >
                        <img src="<c:out value="${ecardForNominationApproval}"/>" alt="image" border="0" style="margin: 0 0 10px 0;" />
                    </div>
                </c:if>

                <div id="nominationDetails" class="nominationDetails span8">

                    <div class="row-fluid">
                        <div class="span3">
                            <p><strong>
                                <cms:contentText code="nomination.approval.details" key="NOMINEE"/>
                            </strong></p>
                        </div>
                        <div class="span5">
                            <c:choose>
                                <c:when test="${approvable.promotion.cumulative}">
                                    <p>
                                        <c:out value="${approvable.participant.lastName}"/>,
                                        <c:out value="${approvable.participant.firstName}"/>
                                        <img src="${pageContext.request.contextPath}/assets/img/flags/${approvable.participant.primaryCountryCode}.png" />,
                                        <c:out value="${approvable.participant.paxOrgName}" />,
                                        <c:out value="${approvable.participant.paxDeptName}" />,
                                        <c:out value="${approvable.participant.paxJobName}" />
                                    </p>
                                </c:when>
                                <c:when test="${claimDetails.teamName == null}">
                                    <c:forEach var="recipient" items="${approvable.claimRecipients}">
                                        <p>
                                            <c:out value="${recipient.lastName}"/>,
                                            <c:out value="${recipient.firstName}"/>
                                            <img src="${pageContext.request.contextPath}/assets/img/flags/${recipient.recipient.primaryCountryCode}.png" />,
                                            <c:out value="${recipient.recipient.paxOrgName}" />,
                                            <c:out value="${recipient.recipient.paxDeptName}" />,
                                            <c:out value="${recipient.recipient.paxJobName}" />
                                        </p>
                                    </c:forEach>
                                </c:when>
                                <c:when test="${claimDetails.teamName != null}">
                                    <c:forEach var="recipient" items="${approvable.teamMembers}">
                                        <p>
                                            <c:out value="${recipient.participant.lastName}"/>,
                                            <c:out value="${recipient.participant.firstName}"/>
                                            <img src="${pageContext.request.contextPath}/assets/img/flags/${recipient.participant.primaryCountryCode}.png" />,
                                            <c:out value="${recipient.participant.paxOrgName}" />,
                                            <c:out value="${recipient.participant.paxDeptName}" />,
                                            <c:out value="${recipient.participant.paxJobName}" />
                                        </p>
                                    </c:forEach>
                                </c:when>
                            </c:choose>
                        </div><!-- /.span5 -->
                    </div><!-- /.row-fluid -->

                    <c:if test="${claimDetails.teamName != null}">
                        <div class="row-fluid">
                            <div class="span3">
                                <p><strong>
                                    <cms:contentText code="nomination.approval.list" key="TEAM_NAME"/>
                                </strong></p>
                            </div>
                            <div class="span5">
                                <p>
                                    <c:out value="${claimDetails.teamName}" />
                                </p>
                            </div>
                        </div><!-- /.row-fluid -->
                    </c:if>

                    <c:if test="${ !approvable.promotion.cumulative}">
                    <div class="row-fluid">
                        <div class="span3">
                            <p><strong>
                                <cms:contentText code="nomination.approval.details" key="NOMINATOR"/>
                            </strong></p>
                        </div>
                        <div class="span5">
                            <p>
                                <c:out value="${claimDetails.submitter.lastName}" />,
                                <c:out value="${claimDetails.submitter.firstName}" />
                                <img src="${pageContext.request.contextPath}/assets/img/flags/${claimDetails.submitter.primaryCountryCode}.png" />,
                                <c:out value="${claimDetails.submitter.paxOrgName}" />,
                                <c:out value="${claimDetails.submitter.paxDeptName}" />,
                                <c:out value="${claimDetails.submitter.paxJobName}" />
                            </p>
                        </div><!-- /.span5 -->

                        <c:if test="${claimDetails.submitterComments ne null}">
                        <div class="span3">
                            <p><strong>
                                <cms:contentText code="nomination.approval.details" key="COMMENTS"/>
                            </strong></p>
                        </div>
                        <div class="span5 comment-wrapper">
                            <div class="comment-text">
                                <c:out value="${claimDetails.submitterComments}" escapeXml="false"/>
                            </div>
                            <c:if test="${allowTranslate}"><a href="${translateClientState}" class="translateTextLink"><cms:contentText code="nomination.approval.details" key="TRANSLATE"/></a></c:if>
                        </div>
                        </c:if>
                        <!--  added for bug fix 57653 -->
                        <c:if test="${ claimDetails.behavior.name ne null}">
                            <div class="span3">
                                <p><strong>
                                    <cms:contentText code="nomination.approval.details" key="BEHAVIOR"/>
                                </strong></p>
                            </div>
                            <div class="span5">
                                <p>
                                    <c:out value="${claimDetails.behavior.name}" />
                                </p>
                            </div>
                        </c:if>
                          <!--  end for bug fix 57653 -->
                    </div><!-- /.row-fluid -->

                                        <!--  ClaimDetails Starts  -->
                    <c:forEach items="${claimDetails.claimElements}" var="claimElement"
                        varStatus="status">

                        <c:if
                            test="${!claimElement.claimFormStepElement.claimFormElementType.addressBookSelect}">

                            <c:if
                                test="${claimElement.claimFormStepElement.claimFormElementType.sectionHeading}">
                                <div class="row-fluid">
                                    <div class="span3">
                                        <p><strong>
                                            <c:if test="${!claimDetails.managerAward }">
                                                <cms:contentText
                                                    code="${claimDetails.promotion.claimForm.cmAssetCode}"
                                                    key="${claimElement.claimFormStepElement.cmKeyForHeading}" />
                                            </c:if>
                                        </strong></p>
                                    </div><!-- /.span3 -->
                                    <div class="span5">
                                        <p>
                                            <c:if test="${claimDetails.managerAward }">
                                                <cms:contentText
                                                    code="${claimDetails.parentManagerPromotion.claimForm.cmAssetCode}"
                                                    key="${claimElement.claimFormStepElement.cmKeyForHeading}" />
                                            </c:if>
                                        </p>
                                    </div><!-- /.span5 -->
                                </div><!-- /.row-fluid -->
                            </c:if>

                            <c:if
                                test="${claimElement.claimFormStepElement.claimFormElementType.textBoxField || claimElement.claimFormStepElement.claimFormElementType.textField || claimElement.claimFormStepElement.claimFormElementType.numberField}">
                                <div class="row-fluid">
                                    <div class="span3">
                                        <p><strong>
                                            <c:if test="${!claimDetails.managerAward }">
                                                <cms:contentText
                                                    code="${claimDetails.promotion.claimForm.cmAssetCode}"
                                                    key="${claimElement.claimFormStepElement.cmKeyForElementLabel}" />
                                            </c:if>
                                        </strong></p>
                                    </div><!-- /.span3 -->
                                    <div class="span5">
                                        <c:if test="${claimDetails.managerAward }">
                                            <p>
                                                <cms:contentText
                                                    code="${claimDetails.parentManagerPromotion.claimForm.cmAssetCode}"
                                                    key="${claimElement.claimFormStepElement.cmKeyForElementLabel}" />
                                            </p>
                                        </c:if>
                                        <c:if test="${claimElement.claimFormStepElement.maskedOnEntry}">
                                            *************
                                        </c:if>
                                        <c:if test="${!claimElement.claimFormStepElement.maskedOnEntry}">
                                            <p>
                                                <c:out value="${claimElement.value}" />
                                            </p>
                                        </c:if>
                                    </div><!-- /.span5 -->
                                </div><!-- /.row-fluid -->
                            </c:if>

                            <c:if
                                test="${claimElement.claimFormStepElement.claimFormElementType.addressBlock}">
                                <div class="row-fluid">
                                    <div class="span3">
                                        <c:if
                                            test="${!claimDetails.managerAward }">
                                            <p><strong>
                                                <cms:contentText
                                                    code="${claimDetails.promotion.claimForm.cmAssetCode}"
                                                    key="${claimElement.claimFormStepElement.cmKeyForElementLabel}" />
                                            </strong></p>
                                        </c:if>
                                    </div><!-- /.span3 -->
                                    <div class="span5">
                                        <c:if test="${claimDetails.managerAward }">
                                            <p>
                                                <cms:contentText
                                                    code="${claimDetails.parentManagerPromotion.claimForm.cmAssetCode}"
                                                    key="${claimElement.claimFormStepElement.cmKeyForElementLabel}" />
                                            </p>
                                        </c:if>
                                        <p>
                                            <beacon:addressDisplay
                                                address="${claimElement.value}" />
                                        </p>
                                    </div><!-- /.span5 -->
                                </div><!-- /.row-fluid -->
                            </c:if>

                            <c:if
                                test="${claimElement.claimFormStepElement.claimFormElementType.dateField}">
                                <div class="row-fluid">
                                    <div class="span3">
                                        <p><strong>
                                            <c:if test="${!claimDetails.managerAward }">
                                                <cms:contentText
                                                    code="${claimDetails.promotion.claimForm.cmAssetCode}"
                                                    key="${claimElement.claimFormStepElement.cmKeyForElementLabel}" />
                                            </c:if>
                                        </strong></p>
                                    </div><!-- /.span3 -->
                                    <div class="span5">
                                        <c:if test="${claimDetails.managerAward }">
                                            <p>
                                                <cms:contentText
                                                    code="${claimDetails.parentManagerPromotion.claimForm.cmAssetCode}"
                                                    key="${claimElement.claimFormStepElement.cmKeyForElementLabel}" />
                                            </p>
                                        </c:if>
                                        <p>
                                            <c:out value="${claimElement.value}" />
                                        </p>
                                    </div><!-- /.span5 -->
                                </div><!-- /.row-fluid -->
                            </c:if>

                            <c:if
                                test="${claimElement.claimFormStepElement.claimFormElementType.booleanField}">
                                <div class="row-fluid">
                                    <div class="span3">
                                        <p><strong>
                                            <c:if test="${!claimDetails.managerAward }">
                                                <cms:contentText
                                                    code="${claimDetails.promotion.claimForm.cmAssetCode}"
                                                    key="${claimElement.claimFormStepElement.cmKeyForElementLabel}" />
                                            </c:if>
                                        </strong></p>
                                    </div><!-- /.span3 -->
                                    <div class="span5">
                                        <c:if test="${claimDetails.managerAward }">
                                            <p>
                                                <cms:contentText
                                                    code="${claimDetails.parentManagerPromotion.claimForm.cmAssetCode}"
                                                    key="${claimElement.claimFormStepElement.cmKeyForElementLabel}" />
                                            </p>
                                        </c:if>
                                        <c:if test="${claimElement.value == 'true'}">
                                            <c:if test="${!claimDetails.managerAward }">
                                                <p>
                                                    <cms:contentText
                                                        code="${claimDetails.promotion.claimForm.cmAssetCode}"
                                                        key="${claimElement.claimFormStepElement.cmKeyForLabelTrue}" />
                                                </p>
                                            </c:if>
                                            <c:if test="${claimDetails.managerAward }">
                                                <p>
                                                    <cms:contentText
                                                        code="${claimDetails.parentManagerPromotion.claimForm.cmAssetCode}"
                                                        key="${claimElement.claimFormStepElement.cmKeyForLabelTrue}" />
                                                </p>
                                            </c:if>
                                        </c:if>
                                        <c:if test="${claimElement.value == 'false'}">
                                            <c:if test="${!claimDetails.managerAward }">
                                                <p>
                                                    <cms:contentText
                                                        code="${claimDetails.promotion.claimForm.cmAssetCode}"
                                                        key="${claimElement.claimFormStepElement.cmKeyForLabelFalse}" />
                                                </p>
                                            </c:if>
                                            <c:if test="${claimDetails.managerAward }">
                                                <p>
                                                    <cms:contentText
                                                        code="${claimDetails.parentManagerPromotion.claimForm.cmAssetCode}"
                                                        key="${claimElement.claimFormStepElement.cmKeyForLabelFalse}" />
                                                </p>
                                            </c:if>
                                        </c:if>
                                    </div><!-- /.span5 -->
                                </div><!-- /.row-fluid -->
                            </c:if>

                            <c:if
                                test="${claimElement.claimFormStepElement.claimFormElementType.selectField}">
                                <div class="row-fluid">
                                    <div class="span3">
                                        <p><strong>
                                            <c:if test="${!claimDetails.managerAward }">
                                                <cms:contentText
                                                    code="${claimDetails.promotion.claimForm.cmAssetCode}"
                                                    key="${claimElement.claimFormStepElement.cmKeyForElementLabel}" />
                                            </c:if>
                                        </strong></p>
                                    </div><!-- /.span3 -->
                                    <div class="span5">
                                        <c:if test="${claimDetails.managerAward }">
                                            <p>
                                                <cms:contentText
                                                    code="${claimDetails.parentManagerPromotion.claimForm.cmAssetCode}"
                                                    key="${claimElement.claimFormStepElement.cmKeyForElementLabel}" />
                                            </p>
                                        </c:if>
                                        <p><c:out value="${claimElement.value}" /></p>
                                    </div><!-- /.span5 -->
                                </div><!-- /.row-fluid -->
                            </c:if>

                            <c:if
                                test="${claimElement.claimFormStepElement.claimFormElementType.multiSelectField}">
                                <div class="row-fluid">
                                    <div class="span3">
                                        <p><strong>
                                            <c:if test="${!claimDetails.managerAward }">
                                                <cms:contentText
                                                    code="${claimDetails.promotion.claimForm.cmAssetCode}"
                                                    key="${claimElement.claimFormStepElement.cmKeyForElementLabel}" />
                                            </c:if>
                                        </strong></p>
                                    </div><!-- /.span3 -->
                                    <div class="span5">
                                        <c:if test="${claimDetails.managerAward }">
                                            <p>
                                                <cms:contentText
                                                    code="${claimDetails.parentManagerPromotion.claimForm.cmAssetCode}"
                                                    key="${claimElement.claimFormStepElement.cmKeyForElementLabel}" />
                                            </p>
                                        </c:if>
                                        <p>
                                                <c:out value="${claimElement.valueName}" />
                                                <br>
                                        </p>
                                    </div><!-- /.span5 -->
                                </div><!-- /.row-fluid -->
                            </c:if>

                            <c:if
                                test="${claimElement.claimFormStepElement.claimFormElementType.button}">
                                <div class="row-fluid">
                                    <div class="span3">
                                        <p><strong>
                                            <c:if test="${!claimDetails.managerAward }">
                                                <cms:contentText
                                                    code="${claimDetails.promotion.claimForm.cmAssetCode}"
                                                    key="${claimElement.claimFormStepElement.cmKeyForElementLabel}" />
                                            </c:if>
                                        </strong></p>
                                    </div><!-- /.span3 -->
                                    <div class="span5">
                                        <c:if test="${claimDetails.managerAward }">
                                            <p>
                                                <cms:contentText
                                                    code="${claimDetails.parentManagerPromotion.claimForm.cmAssetCode}"
                                                    key="${claimElement.claimFormStepElement.cmKeyForElementLabel}" />
                                            </p>
                                        </c:if>
                                    </div><!-- /.span5 -->
                                </div><!-- /.row-fluid -->
                            </c:if>

                        </c:if>
                    </c:forEach>

                        <div class="row-fluid">
                        <div class="span3">
                            <p><strong>
                                <cms:contentText code="nomination.approval.details" key="DATE_SUBMITTED"/>
                            </strong></p>
                        </div>
                        <div class="span5">
                        <p>
                            <fmt:formatDate pattern="${JstlDatePattern}" value="${claimDetails.submissionDate}" />
                        </p>
                        </div>
                        </div>

                    </c:if>

                    <c:if test="${ approvable.promotion.cumulative}">

                        <c:forEach items="${approvable.claims}" var="claimGroupClaim" varStatus="claimGroupClaimStatus">
                        <div class="row-fluid">
                        <div class="claimgroup">
                            <div class="span3">
                                <p><strong>
                                    <cms:contentText code="nomination.approval.details" key="NOMINATOR"/>
                                </strong></p>
                            </div>
                            <div class="span5">
                                <p>
                                    <c:out value="${claimGroupClaim.submitter.lastName}" />,
                                    <c:out value="${claimGroupClaim.submitter.firstName}" />
                                    <img src="${pageContext.request.contextPath}/assets/img/flags/${claimGroupClaim.submitter.primaryCountryCode}.png" />,
                                    <c:out value="${claimGroupClaim.submitter.paxOrgName}" />,
                                    <c:out value="${claimGroupClaim.submitter.paxDeptName}" />,
                                    <c:out value="${claimGroupClaim.submitter.paxJobName}" />
                                </p>
                            </div>

                            <div class="span3">
                                <p><strong>
                                    <cms:contentText code="nomination.approval.details" key="COMMENTS"/>
                                </strong></p>
                            </div>
                            <div class="span5 comment-wrapper">
                                <div class="comment-text">
                                    <c:out value="${claimGroupClaim.submitterComments}" escapeXml="false"/>
                                </div>
                                <c:if test="${allowTranslate}"><a href="${translateClientState}" class="translateTextLink"><cms:contentText code="nomination.approval.details" key="TRANSLATE"/></a></c:if>
                            </div>
                            <c:if test="${ claimGroupClaim.behavior.name ne null}">
                             <!--  added for bug fix 57653 -->
                            <div class="span3">
                                <p><strong>
                                    <cms:contentText code="nomination.approval.details" key="BEHAVIOR"/>
                                </strong></p>
                            </div>
                            <div class="span5">
                                <p>
                                    <c:out value="${ claimGroupClaim.behavior.name }" />
                                </p>
                            </div>
                         <!--  end for bug fix 57653 -->
                         </c:if>

                         </div><!-- /.claimgroup -->
                        </div><!-- /.row-fluid -->


                         <!--  ClaimDetails Starts  -->
                    <c:forEach items="${claimGroupClaim.claimElements}" var="claimElement"
                        varStatus="status">

                        <c:if
                            test="${!claimElement.claimFormStepElement.claimFormElementType.addressBookSelect}">

                            <c:if
                                test="${claimElement.claimFormStepElement.claimFormElementType.sectionHeading}">
                                    <div class="row-fluid">
                                    <div class="span3">
                                        <p><strong>
                                            <c:if test="${!claimGroupClaim.managerAward }">
                                                <cms:contentText
                                                    code="${claimGroupClaim.promotion.claimForm.cmAssetCode}"
                                                    key="${claimElement.claimFormStepElement.cmKeyForHeading}" />
                                            </c:if>
                                        </strong></p>
                                    </div><!-- /.span3 -->
                                    <div class="span5">
                                        <p>
                                            <c:if test="${claimGroupClaim.managerAward }">
                                                <cms:contentText
                                                    code="${claimGroupClaim.parentManagerPromotion.claimForm.cmAssetCode}"
                                                    key="${claimElement.claimFormStepElement.cmKeyForHeading}" />
                                            </c:if>
                                        </p>
                                    </div><!-- /.span5 -->
                                    </div>
                            </c:if>

                            <c:if
                                test="${claimElement.claimFormStepElement.claimFormElementType.textBoxField || claimElement.claimFormStepElement.claimFormElementType.textField || claimElement.claimFormStepElement.claimFormElementType.numberField}">
                                    <div class="row-fluid">
                                    <div class="span3">
                                        <p><strong>
                                            <c:if test="${!claimGroupClaim.managerAward }">
                                                <cms:contentText
                                                    code="${claimGroupClaim.promotion.claimForm.cmAssetCode}"
                                                    key="${claimElement.claimFormStepElement.cmKeyForElementLabel}" />
                                            </c:if>
                                        </strong></p>
                                    </div><!-- /.span3 -->
                                    <div class="span5">
                                        <c:if test="${claimGroupClaim.managerAward }">
                                            <p>
                                                <cms:contentText
                                                    code="${claimGroupClaim.parentManagerPromotion.claimForm.cmAssetCode}"
                                                    key="${claimElement.claimFormStepElement.cmKeyForElementLabel}" />
                                            </p>
                                        </c:if>
                                        <c:if test="${claimElement.claimFormStepElement.maskedOnEntry}">
                                            *************
                                        </c:if>
                                        <c:if test="${!claimElement.claimFormStepElement.maskedOnEntry}">
                                            <p>
                                                <c:out value="${claimElement.value}" />
                                            </p>
                                        </c:if>
                                    </div><!-- /.span5 -->
                                    </div>
                            </c:if>

                            <c:if
                                test="${claimElement.claimFormStepElement.claimFormElementType.addressBlock}">
                                    <div class="row-fluid">
                                    <div class="span3">
                                        <c:if
                                            test="${!claimGroupClaim.managerAward }">
                                            <p><strong>
                                                <cms:contentText
                                                    code="${claimGroupClaim.promotion.claimForm.cmAssetCode}"
                                                    key="${claimElement.claimFormStepElement.cmKeyForElementLabel}" />
                                            </strong></p>
                                        </c:if>
                                    </div><!-- /.span3 -->
                                    <div class="span5">
                                        <c:if test="${claimGroupClaim.managerAward }">
                                            <p>
                                                <cms:contentText
                                                    code="${claimGroupClaim.parentManagerPromotion.claimForm.cmAssetCode}"
                                                    key="${claimElement.claimFormStepElement.cmKeyForElementLabel}" />
                                            </p>
                                        </c:if>
                                        <p>
                                            <beacon:addressDisplay
                                                address="${claimElement.value}" />
                                        </p>
                                    </div><!-- /.span5 -->
                                    </div>
                            </c:if>

                            <c:if
                                test="${claimElement.claimFormStepElement.claimFormElementType.dateField}">
                                    <div class="row-fluid">
                                    <div class="span3">
                                        <p><strong>
                                            <c:if test="${!claimGroupClaim.managerAward }">
                                                <cms:contentText
                                                    code="${claimGroupClaim.promotion.claimForm.cmAssetCode}"
                                                    key="${claimElement.claimFormStepElement.cmKeyForElementLabel}" />
                                            </c:if>
                                        </strong></p>
                                    </div><!-- /.span3 -->
                                    <div class="span5">
                                        <c:if test="${claimGroupClaim.managerAward }">
                                            <p>
                                                <cms:contentText
                                                    code="${claimGroupClaim.parentManagerPromotion.claimForm.cmAssetCode}"
                                                    key="${claimElement.claimFormStepElement.cmKeyForElementLabel}" />
                                            </p>
                                        </c:if>
                                        <p>
                                            <c:out value="${claimElement.value}" />
                                        </p>
                                    </div><!-- /.span5 -->
                                    </div>
                            </c:if>

                            <c:if
                                test="${claimElement.claimFormStepElement.claimFormElementType.booleanField}">
                                    <div class="row-fluid">
                                    <div class="span3">
                                        <p><strong>
                                            <c:if test="${!claimGroupClaim.managerAward }">
                                                <cms:contentText
                                                    code="${claimGroupClaim.promotion.claimForm.cmAssetCode}"
                                                    key="${claimElement.claimFormStepElement.cmKeyForElementLabel}" />
                                            </c:if>
                                        </strong></p>
                                    </div><!-- /.span3 -->
                                    <div class="span5">
                                        <c:if test="${claimGroupClaim.managerAward }">
                                            <p>
                                                <cms:contentText
                                                    code="${claimGroupClaim.parentManagerPromotion.claimForm.cmAssetCode}"
                                                    key="${claimElement.claimFormStepElement.cmKeyForElementLabel}" />
                                            </p>
                                        </c:if>
                                        <c:if test="${claimElement.value == 'true'}">
                                            <c:if test="${!claimGroupClaim.managerAward }">
                                                <p>
                                                    <cms:contentText
                                                        code="${claimGroupClaim.promotion.claimForm.cmAssetCode}"
                                                        key="${claimElement.claimFormStepElement.cmKeyForLabelTrue}" />
                                                </p>
                                            </c:if>
                                            <c:if test="${claimGroupClaim.managerAward }">
                                                <p>
                                                    <cms:contentText
                                                        code="${claimGroupClaim.parentManagerPromotion.claimForm.cmAssetCode}"
                                                        key="${claimElement.claimFormStepElement.cmKeyForLabelTrue}" />
                                                </p>
                                            </c:if>
                                        </c:if>
                                        <c:if test="${claimElement.value == 'false'}">
                                            <c:if test="${!claimGroupClaim.managerAward }">
                                                <p>
                                                    <cms:contentText
                                                        code="${claimGroupClaim.promotion.claimForm.cmAssetCode}"
                                                        key="${claimElement.claimFormStepElement.cmKeyForLabelFalse}" />
                                                </p>
                                            </c:if>
                                            <c:if test="${claimGroupClaim.managerAward }">
                                                <p>
                                                    <cms:contentText
                                                        code="${claimGroupClaim.parentManagerPromotion.claimForm.cmAssetCode}"
                                                        key="${claimElement.claimFormStepElement.cmKeyForLabelFalse}" />
                                                </p>
                                            </c:if>
                                        </c:if>
                                    </div><!-- /.span5 -->
                                    </div>
                            </c:if>

                            <c:if
                                test="${claimElement.claimFormStepElement.claimFormElementType.selectField}">
                                    <div class="row-fluid">
                                    <div class="span3">
                                        <p><strong>
                                            <c:if test="${!claimGroupClaim.managerAward }">
                                                <cms:contentText
                                                    code="${claimGroupClaim.promotion.claimForm.cmAssetCode}"
                                                    key="${claimElement.claimFormStepElement.cmKeyForElementLabel}" />
                                            </c:if>
                                        </strong></p>
                                    </div><!-- /.span3 -->
                                    <div class="span5">
                                        <c:if test="${claimGroupClaim.managerAward }">
                                            <p>
                                                <cms:contentText
                                                    code="${claimGroupClaim.parentManagerPromotion.claimForm.cmAssetCode}"
                                                    key="${claimElement.claimFormStepElement.cmKeyForElementLabel}" />
                                            </p>
                                        </c:if>
                                        <p><c:out value="${claimElement.value}" /></p>
                                    </div><!-- /.span5 -->
                                    </div>
                            </c:if>

                            <c:if
                                test="${claimElement.claimFormStepElement.claimFormElementType.multiSelectField}">
                                    <div class="row-fluid">
                                    <div class="span3">
                                        <p><strong>
                                            <c:if test="${!claimGroupClaim.managerAward }">
                                                <cms:contentText
                                                    code="${claimGroupClaim.promotion.claimForm.cmAssetCode}"
                                                    key="${claimElement.claimFormStepElement.cmKeyForElementLabel}" />
                                            </c:if>
                                        </strong></p>
                                    </div><!-- /.span3 -->
                                    <div class="span5">
                                        <c:if test="${claimGroupClaim.managerAward }">
                                            <p>
                                                <cms:contentText
                                                    code="${claimGroupClaim.parentManagerPromotion.claimForm.cmAssetCode}"
                                                    key="${claimElement.claimFormStepElement.cmKeyForElementLabel}" />
                                            </p>
                                        </c:if>
                                        <p>
                                                <c:out value="${claimElement.valueName}" />
                                                <br>
                                        </p>
                                    </div><!-- /.span5 -->
                                    </div>
                            </c:if>

                            <c:if
                                test="${claimElement.claimFormStepElement.claimFormElementType.button}">
                                    <div class="row-fluid">
                                    <div class="span3">
                                        <p><strong>
                                            <c:if test="${!claimGroupClaim.managerAward }">
                                                <cms:contentText
                                                    code="${claimGroupClaim.promotion.claimForm.cmAssetCode}"
                                                    key="${claimElement.claimFormStepElement.cmKeyForElementLabel}" />
                                            </c:if>
                                        </strong></p>
                                    </div><!-- /.span3 -->
                                    <div class="span5">
                                        <c:if test="${claimGroupClaim.managerAward }">
                                            <p>
                                                <cms:contentText
                                                    code="${claimGroupClaim.parentManagerPromotion.claimForm.cmAssetCode}"
                                                    key="${claimElement.claimFormStepElement.cmKeyForElementLabel}" />
                                            </p>
                                        </c:if>
                                    </div><!-- /.span5 -->
                                    </div>
                            </c:if>

                        </c:if>
                    </c:forEach>

                    <div class="row-fluid">
                        <div class="span3">
                            <p><strong>
                                <cms:contentText code="nomination.approval.details" key="DATE_SUBMITTED"/>
                            </strong></p>
                        </div>
                        <div class="span5">
                        <p>
                            <fmt:formatDate pattern="${JstlDatePattern}" value="${claimGroupClaim.submissionDate}" />
                        </p>
                        </div>
                        </div>
                        </c:forEach>

                    </c:if>

                    <c:if test="${approvable.promotion.budgetUsed and !readOnly}">
                        <div class="row-fluid">
                            <div class="span8">
                                <table id="budgetTable" class="table table-bordered table-striped approvalBudgetTable approvalsNoBudgetTable">
                                    <tbody>
                                        <tr>
                                            <td><cms:contentText code="nomination.approval.details" key="AVAILABLE_BUDGET"/></td>
                                            <td id="availableBudget"><c:out value="${availableBudget}"/></td>
                                        </tr>

                                        <tr>
                                            <td><cms:contentText code="nomination.approval.details" key="CALCULATED_BUDGET"/></td>
                                            <td id="budgetDeductionElm"></td>
                                        </tr>

                                        <tr>
                                            <td><cms:contentText code="nomination.approval.details" key="REMAINING_BUDGET"/></td>
                                            <td id="remainingBudgetElm"></td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div><!-- /.span8 -->
                        </div><!-- /.row-fluid -->
                    </c:if>

                </div><!-- /.nominationDetails.span8 -->
            </div><!-- /.row-fluid -->

            <div class="row-fluid">
                <div class="span12">

                    <!-- Approver Action Section Starts -->
                    <div id="approverActions" class="approverActions">
                        <legend><cms:contentText code="nomination.approval.details" key="APPROVER_ACTION"/></legend>

                        <!-- Status Field Starts -->
                        <div class="control-group">
                            <label class="control-label">
                                <b><cms:contentText code="nomination.approval.details" key="STATUS"/></b>
                            </label>
                            <div class="controls">
                                <c:if test="${approvalsNominationDetailsForm.viewApprovalStatusCode == 'pend'}">
                                    <html:select styleId="status"
                                        styleClass="approvalNominationStatus nominationDetial"
                                        property="approvalStatusType">
                                        <html:options collection="statusType" property="code" labelProperty="name" />
                                    </html:select>
                                </c:if>
                                <c:if test="${approvalsNominationDetailsForm.viewApprovalStatusCode == 'non_winner' }">
                                    <span class="naked-input">
                                        <cms:contentText key="NON_WINNER" code="nomination.approval.list" />
                                    </span>
                                </c:if>
                                <c:if test="${approvalsNominationDetailsForm.viewApprovalStatusCode == 'winner' }">
                                    <span class="naked-input">
                                        <cms:contentText key="WINNER" code="nomination.approval.list" />
                                    </span>
                                </c:if>
                            </div><!-- /.controls -->
                        </div>
                        <!-- Status Field Ends -->
						<c:if test="${ approvable.promotion.levelSelectionByApprover }">
						<div class="control-group">
						    <label class="control-label">
                                <b><cms:contentText key="LEVEL" code="nomination.approval.list"/></b>
                            </label>
						
							<div class="controls">
								<html:select property="level" styleClass="levelSelectDetail">
									<html:option value="">
										<cms:contentText code="system.general" key="SELECT_ONE" />
									</html:option>
									<html:options collection="approverLevelTypes" property="code" labelProperty="name" />
								</html:select>
							</div>
						</div>
						</c:if>
                         <%-- Client customization for WIP 58122 --%>
						 <c:if test="${  approvable.promotion.levelPayoutByApproverAvailable && final_Approver }">
						 <div class="control-group">
						    <label class="control-label">
                                <b><cms:contentText key="LEVEL" code="nomination.approval.list"/></b>
                            </label>
														<c:set var="charIndex" value="${-1}" />
														<c:if test="${levelPayouts != null }">
														<logic:iterate name="levelPayouts" id="levelCopy">
														<c:set var="charIndex" value="${charIndex+1}" />
																<html:hidden property="levelCopy[${charIndex}].totalPoints"	styleClass="levelInputs"/>
							
														</logic:iterate>
														</c:if>
														<html:hidden property="capPerPax" 	styleClass="capPerPax"/>
														<input type="hidden" class="teamSizeValue" name="teamSizeValue" value="${participantNumber}">
											
											 
													<div class="controls">
														<html:select property="level" styleClass="levelSelect">
															<html:option value="">
																<cms:contentText code="system.general" key="SELECT_ONE" />
															</html:option>
															<html:options collection="levelPayouts" property="levelId" labelProperty="levelDescription" />
														</html:select>
													</div>
						 </div>
						</c:if>
											 <c:if test="${ approvable.promotion.levelPayoutByApproverAvailable && final_Approver }">
											 <div class="control-group">
												<label class="control-label">
													<b><cms:contentText key="AWARD_PER_PERSON" code="nomination.approval.list"/></b>
												</label>
											 
											 <div class="controls">													
												<html:text property="awardQuantity" styleId="awardPointsInput" size="7" readonly="true"/>
											 </div>	
											 </div>												 
											 </c:if>

                        <!-- Award Field starts -->
                        <c:if test="${ approvable.promotion.awardActive   && !approvable.promotion.levelPayoutByApproverAvailable}">
                            <div id="awardPoints" class="control-group approverAction">
                                <label class="control-label">
                                    <b>
                                    <c:choose>
                                        <c:when test="${approvable.promotion.awardAmountTypeFixed}">
                                            <cms:contentText code="nomination.approval.details" key="AWARD_AMOUNT"/>
                                        </c:when>
                                        <c:otherwise>
                                            <cms:contentTemplateText code="nomination.approval.details" key="AWARD_RANGE" args="${approvable.promotion.awardAmountMin},${approvable.promotion.awardAmountMax}" delimiter=","/>
                                        </c:otherwise>
                                    </c:choose>
                                    </b>
                                </label>
                                <div class="controls">
                                    <c:choose>
                                        <c:when test="${readOnly}">
                                            <c:out value="${approvalsNominationDetailsForm.awardQuantity}" />
                                        </c:when>
                                        <c:otherwise>
                                            <c:choose>
                                                <c:when test="${approvable.promotion.awardAmountTypeFixed}">
                                                    <span class="naked-input">
                                                        <c:out value="${ approvable.promotion.awardAmountFixed }" />
                                                    </span>
                                                    <html:hidden styleId="awardPointsInput" styleClass="awardPointsInput" property="awardQuantity" value="${approvable.promotion.awardAmountFixed }" />
                                                </c:when>
                                                <c:otherwise>
                                                    <input value="${approvalsNominationDetailsForm.awardQuantity}" name="awardQuantity" id="awardPointsInput"
                                                        class="awardPointsInput" type="text" data-range-min="${approvable.promotion.awardAmountMin}" data-range-max="${approvable.promotion.awardAmountMax}"
                                                    <c:if test="${approvalsNominationDetailsForm.approvalStatusType != 'winner'}">disabled="disabled"</c:if> />
                                                </c:otherwise>
                                            </c:choose>
                                        </c:otherwise>
                                    </c:choose>
                                </div><!-- /.controls -->
                                <%-- Will use number of participants for budget calculations --%>
                                <input type="hidden" value="${participantNumber}" id="participantNumber">
                            </div>
                        </c:if>
                        <!-- Award Field ends -->

                        <!-- Notification Date field starts -->
                        <div class="control-group approverAction">
                            <label class="control-label">
                                <b><cms:contentText code="nomination.approval.details" key="COMMUNICATION_DATE" /></b>
                            </label>
                            <div class="controls">
                                <c:choose>
                                    <c:when test="${ readOnly }">
                                        <span class="naked-input">
                                            <c:out value="${approvalsNominationDetailsForm.notificationDate}" />
                                        </span>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="input-append datepickerTrigger"
                                            data-date-format="<%=UserManager.getUserDatePattern().toLowerCase()%>"
                                            data-date-language="<%=UserManager.getUserLocale()%>"
                                            data-date-startdate="${tomorrowDate}"
                                            data-date-autoclose="true">
                                            <html:text  styleId="awardScheduleDate" readonly="true" property="notificationDate" />
                                            <button class="btn"><i class="icon-calendar"></i></button>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div><!-- /.controls -->
                        </div>
                        <!-- Notification Date field ends -->

                        <div class="form-actions pullBottomUp">
                          <beacon:authorize ifNotGranted="LOGIN_AS">
                            <c:if test="${approvalsNominationDetailsForm.viewApprovalStatusCode == 'pend'}">
                            <html:submit styleId="submitButton" styleClass="btn btn-primary btn-fullmobile"><cms:contentText code="system.button" key="SUBMIT"/></html:submit>
                            </c:if>
                          </beacon:authorize>
                            <a class="btn cancelBtn btn-fullmobile" href="approvalsNominationList.do"><cms:contentText code="system.button" key="CANCEL"/></a>
                        </div>
                    </div><!-- /#approverActions -->
                    <!-- Approver Action Section Ends -->

                </div><!-- /.span12 -->
            </div><!-- /.row-fluid -->

        </html:form>
</div>
</div>


<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
    //attach the view to an existing DOM element
    $(document).ready(function() {
        G5.props.URL_JSON_APPROVALS_TRANSLATE_COMMENT = G5.props.URL_ROOT + "claim/approvalTranslate.do";

        var aimlp = new ApprovalsNominationDetailModelView({
            el : $('#approvalsNominationDetailView'),
            pageNav : {
                back : {
                    text : '<cms:contentText key="BACK" code="system.button" />',
                    url : 'approvalsNominationList.do'
                },
                home : {
                    text : '<cms:contentText key="HOME" code="system.general" />',
                    url : '${pageContext.request.contextPath}/homePage.do'
                }
            },
            pageTitle : '<cms:contentText code="nomination.approval.details" key="TITLE" />'
        });
    });
</script>
