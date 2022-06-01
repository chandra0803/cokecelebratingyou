<%@ include file="/include/taglib.jspf"%>
<%@page import="com.biperf.core.ui.utils.RequestUtils"%>

<!--
    This page combines the AwardeeCollectionView from RecognitionPageSendView with
    the HTML from Public Recognition Detail (top portion only - ecard, to, from ... comments)
-->


<!--
    IE7 likes the following div and row-fluid to have no space between them if they are not dynamically loaded
    if they are not next to one another you may experience style issues
-->
<div id="recognitionPageAddPointsView"  class="page-content publicRecognition public-recognition-page-detail">
<div class="row-fluid">

        <div class="row-fluid">
            <!-- for errors -->
            <c:if test="${not empty managerAddPointsValidationErrors}">
              <div class="span12">
                <c:forEach var="serviceError" items="${managerAddPointsValidationErrors}">
                  <div class="alert alert-block alert-error">
                      ${serviceError.arg1}
                  </div>
                </c:forEach>
              </div>
            </c:if>
        </div>

        <div class="row-fluid">
        	<div class="span12">
                <ul class="export-tools fr">
                    <li><a class="pageView_print btn btn-small" href="#"><cms:contentText code="system.button" key="PRINT" /> <i class="icon-printer"></i></a></li>
                </ul>
        		<h3><c:out value = "${managerAddPointsForm.promotionName}" /></h3>
        	</div>
        </div>

        <!-- using styles from Public Recognition Detail -->
        <div class="public-recognition-item">
            <div class="row-fluid">
                <c:if test="${not empty managerAddPointsForm.detailBean.recognition.ecard}">
                  <div class="span5 ecard-span">
                      <img alt="" src="${managerAddPointsForm.detailBean.recognition.ecard.imgUrl}">
                  </div>
                </c:if>

            <div class="span7 recognitionInformation">
                <div class="row-fluid section-container">
                    <div class="span3">
                        <p><strong><cms:contentText key="TO" code="recognition.public.recognition.item" /></strong></p>
                    </div>
                    <div class="span9">
                        <div class="avatarWrap">
                            <a class="profile-popover" href="#" data-participant-ids="[${managerAddPointsForm.recipientInfo.id}]">
                                <img alt="${managerAddPointsForm.recipientInfo.firstName} ${managerAddPointsForm.recipientInfo.lastName}" class="avatar" src="${managerAddPointsForm.recipientInfo.avatarUrl}">
                            </a>

                        </div>

                        <div class="recipientInfoWrap">
                            <a class="profile-popover" href="#" data-participant-ids="[${managerAddPointsForm.recipientInfo.id}]">
                             ${managerAddPointsForm.recipientInfo.lastName}, ${managerAddPointsForm.recipientInfo.firstName}
                             </a>

                            <span class="paxMeta">
                            ${managerAddPointsForm.recipientInfo.departmentName} | ${managerAddPointsForm.recipientInfo.jobName}
                            </span>
                        </div>
                    </div>
                </div>

                <div class="row-fluid section-container">
                    <div class="span3">
                        <p><strong><cms:contentText key="FROM" code="recognition.public.recognition.item" /></strong></p>
                    </div>
                    <div class="span9">
                        <div class="avatarWrap">
                            <a class="profile-popover" href="#" data-participant-ids="[${managerAddPointsForm.submitterInfo.id}]">
                                <img alt="${managerAddPointsForm.submitterInfo.firstName} ${managerAddPointsForm.submitterInfo.lastName}" class="avatar" src="${managerAddPointsForm.submitterInfo.avatarUrl}">

                            </a>
                        </div>

                        <div class="recipientInfoWrap">
                            <a class="profile-popover" href="#" data-participant-ids="[${managerAddPointsForm.submitterInfo.id}]">
                            ${managerAddPointsForm.submitterInfo.lastName}, ${managerAddPointsForm.submitterInfo.firstName}
                            </a>

                            <span class="paxMeta">
                            ${managerAddPointsForm.submitterInfo.departmentName} | ${managerAddPointsForm.submitterInfo.jobName}
                            </span>
                        </div>
                    </div>
                </div>

                <div class="row-fluid section-container">
                    <div class="row-fluid">
                        <div class="span3">
                            <p><strong><cms:contentText key="DATE" code="recognition.public.recognition.item" /></strong></p>
                        </div>
                        <div class="span9">
                            <p>${managerAddPointsForm.detailBean.recognition.date}</p>
                        </div>
                    </div>

                     <c:if test="${not empty managerAddPointsForm.detailBean.recognition.behavior}">
                      <div class="row-fluid">
                          <div class="span3">
                              <p><strong><cms:contentText key="BEHAVIOR" code="recognition.public.recognition.item" /></strong></p>
                          </div>
                          <div class="span9">
                              <p>${managerAddPointsForm.detailBean.recognition.behavior}</p>
                          </div>
                      </div>
                    </c:if>

                    <div class="row-fluid">
                        <div class="span3">
                            <p><strong><cms:contentText key="COMMENTS" code="recognition.public.recognition.item" /></strong></p>
                        </div>
                        <div class="span9">
                            <!-- do no wrap comment in P tag, it will have its own -->
                            <c:out value="${managerAddPointsForm.detailBean.recognition.comment}" escapeXml="false" />
                        </div>
                    </div>

                    <c:forEach var="reportDataId" items="${managerAddPointsForm.detailBean.recognition.extraFields}" varStatus="reportDataIndexId">
                      <div class="row-fluid">
                        <div class="span3">
                          <p>
                            <strong>
                                <c:out value="${reportDataId.name}" escapeXml="false" />:
                            </strong>
                          </p>
                        </div>
                        <div class="span9">
                          <c:out value="${reportDataId.value}" escapeXml="false" />
                        </div>
                      </div>
                    </c:forEach>
                    </div>
                </div><!-- /.span8 -->
            </div>
        </div><!-- /.publice-recognition-item -->


    </div><!-- /.row-fluid -->



    <div class="row-fluid">
        <div class="span12">
            <!-- JAVA NOTE: struts populated (see Send A Recongition) -->
            <!-- DATA FORM -->
            <form id="dataForm" method="post" style="display:none">

                <!-- JAVA NOTE: struts method -->
                <input type="hidden" name="method" value="someValue" />

                <!-- JAVA NOTE: value = pointsRange|pointsFixed -->
                <%--<input type="hidden" name="awardType" value="pointsRange" />--%>
                <input type="hidden" name="awardType" value="${managerAddPointsForm.awardType}" />

                <!-- JAVA NOTE: for awardType=pointsFixed only -->
                <c:if test="${managerAddPointsForm.awardType == 'pointsFixed'}">
                  <input type="hidden" name="awardFixed" value="${managerAddPointsForm.awardFixed}" />
                </c:if>


                <!-- JAVA NOTE: for awardType=pointRange only -->
                <c:if test="${managerAddPointsForm.awardType == 'pointsRange'}">
                  <input type="hidden" name="awardMin" value="${managerAddPointsForm.awardMin}" />
                  <input type="hidden" name="awardMax" value="${managerAddPointsForm.awardMax}" />
                </c:if>

                <!-- JAVA NOTE: single recipient -->
                <input type="hidden" name="claimRecipientFormBeansCount" value="${managerAddPointsForm.claimRecipientFormBeansCount}" />
                <c:if test="${not empty managerAddPointsForm.recipients}">
                  <c:forEach var="recipient" items="${managerAddPointsForm.recipients}" varStatus="status">
                    <input type="hidden" name="claimRecipientFormBeans[${status.index}].id" value="${recipient.userId}" />

                    <c:choose>
                      <c:when test="${managerAddPointsForm.awardType == 'pointsFixed'}">
                        <c:set var="recipientAwardQuantity" value="${managerAddPointsForm.awardFixed}" />
                      </c:when>
                      <c:otherwise>
                        <c:set var="recipientAwardQuantity" value="${managerAddPointsForm.awardFixed}" />
                      </c:otherwise>
                    </c:choose>
                    <input type="hidden" name="claimRecipientFormBeans[${status.index}].awardQuantity" value="${recipientAwardQuantity}" />

                    <input type="hidden" name="claimRecipientFormBeans[${status.index}].countryCode" value="${recipient.countryCode}" />
                    <input type="hidden" name="claimRecipientFormBeans[${status.index}].countryRatio" value="${recipient.countryRatio}" />
                    <input type="hidden" name="claimRecipientFormBeans[${status.index}].countryName" value="${recipient.countryName}" />
                    <input type="hidden" name="claimRecipientFormBeans[${status.index}].firstName" value="${recipient.firstName}" />
                    <input type="hidden" name="claimRecipientFormBeans[${status.index}].lastName" value="${recipient.lastName}" />
                    <input type="hidden" name="claimRecipientFormBeans[${status.index}].departmentName" value="${recipient.departmentName}" />
                    <input type="hidden" name="claimRecipientFormBeans[${status.index}].jobName" value="${recipient.jobName}" />
                    <input type="hidden" name="claimRecipientFormBeans[${status.index}].emailAddr" value="${recipient.emailAddr}" />
                    <input type="hidden" name="claimRecipientFormBeans[${status.index}].nodeId" value="${recipient.nodeId}" />
                  </c:forEach>
                </c:if>

                <!-- JAVA NOTE: selected nodeId -->
                <!--  <input type="hidden" name="nodeId" value="3343">  -->

                <!-- Budget and node info as a JSON string inside of a TEXTAREA -->
                  <textarea name="nodes">
					${managerAddPointsForm.nodeBudgetJson}
                </textarea>

            </form>

            <!-- JAVA NOTE: this form is not struts powered, it will use #dataForm above, which is struts -->
            <!-- SEND FORM -->
            <html:form action="managerAddPointsSubmit" styleId="sendForm" method="POST">

                <!-- method -->
                <input type="hidden" name="method" id="sendFormMethod" value="submit" />


                <!-- RECIPIENT(s) -->
                <fieldset class="formSection recipientsSection" id="recognitionFieldsetRecipients">
                    <div class="container-splitter with-splitter-styles participantCollectionViewWrapper"
                        data-msg-validation-over-budget="<cms:contentText key="OVER_BUDGET" code="recognitionSubmit.errors"/>" >

                        <input type="hidden" name="dynamic" value="0" class="participantCount" />

                        <div class="row-fluid">
        	             <div class="span12">
        		          	<h3><c:out value = "${managerAddPointsForm.mgrPromotionName}" /></h3>
        	             </div>
                        </div>

         				<div class="nodesWrapper"><!-- FE - dynamic --></div>
                        <script id="nodesTpl" type="text/x-handlebars-template">
                            {{! tricky check to see if there is > 1 item in nodes }}
                            {{#if nodes.1.id}}
                                <select id="orgUnitSelect" class="dropdown-toggle" name="orgUnit" title="<cms:contentText key="SELECT_ORG_UNIT" code="recognition.submit"/>">
                                    {{#nodes}}
                                        <option value="{{id}}">{{name}}</option>
                                    {{/nodes}}
                                </select>
                            {{else}}
                                Org Unit: {{nodes.0.name}}
                            {{/if}}
                        </script>

                        <h3><cms:contentText key="ADD_POINTS" code="recognition.send.manager"/></h3>

                        <table class="table table-condensed table-striped">
                            <thead>
                                <tr>
                                    <th class="participant"><cms:contentText key="RECIPIENT" code="recognition.send.manager"/></th>
                                    <th data-msg-points-range="<cms:contentText key="AWARD_RANGE" code="calculator.payouts"/>"
              							data-msg-points-fixed="<cms:contentText key="AWARD" code="calculator.payouts"/>"
              							data-msg-levels="<cms:contentText key="AWARD_LEVEL" code="recognition.submit"/>"
              							data-msg-calculated="<cms:contentText key="AWARD" code="calculator.payouts"/>"
                                        class="award"><!-- dynamic --></th>
                                    <th class="calcDeduction"><cms:contentText key="CALC_BUDGET_DEDUCTION" code="recognition.send.manager"/></th>
                                    <!--th class="remove">Remove</th-->
                                </tr>
                            </thead>

                            <tbody id="recipientsView"
                                class="participantCollectionView"
                                data-msg-empty="<cms:contentText key="NOT_ADDED" code="recognitionSubmit.errors"/>"
                                data-hide-on-empty="false">
                            </tbody>
                        </table>

                        <div class="budgetDeduction">
                            <h3><cms:contentText key="BUDGET_DEDUCTION" code="recognition.send.manager"/></h3>
                            <div class="progress">
                                <div class="bar bar-success" style="width: 0%;"></div>
                            </div>
                            <div class="totals clearfix">
                                <div class="budgetMin">0</div>
                                <div class="budgetMax"></div>
                            </div>
                            <p class="discrepancyWarning" style="display:none">
                                <small><i><cms:contentText key="WARNING_MESSAGE" code="recognition.send.manager"/></i></small>
                            </p>
                        </div>

                    </div><!-- /.participantCollectionViewWrapper -->
                </fieldset><!-- /#recognitionFieldsetRecipients -->


                <!-- COMMENT - public recognition comment -->
                <fieldset class="formSection commentSection" id="recognitionFieldsetMessage">
                    <div class="controls validateme"
                        data-validate-fail-msgs='{"nonempty":"<cms:contentText key="COMMENTS_REQUIRED" code="recognitionSubmit.errors"/>","maxlength":"Character limit of 300 exceeded"}'
                        data-validate-flags='nonempty,maxlength'
                        data-validate-max-length="300" >
                        <label><cms:contentText key="COMMENTS" code="recognition.send.manager"/></label>
                        <textarea name="comment" id="comments"
                            class="comment-input commentInputTxt richtext"
                            placeholder="Leave A Comment"
                            maxlength="300"
                            rows="4"><c:out value="${managerAddPointsForm.comment}" /></textarea>
                    </div>
                </fieldset><!-- /#recognitionFieldsetMessage -->

                <!-- ACTIONS -->
                <fieldset class="formSection actionsSection" id="recognitionFieldsetActions">

                    <button class="btn btn-primary"
                        value="recognitionButtonSubmit"
                        name="button"
                        id="recognitionButtonSubmit"
                        type="submit">
                        <cms:contentText key="SUBMIT" code="system.button"/>
                    </button>

                    <button class="btn btn-inverse btn-primary"
                        value="recognitionButtonCancel"
                        name="button"
                        id="recognitionButtonCancel"
                        data-url="${pageContext.request.contextPath}/homePage.do">
                        <cms:contentText key="CANCEL" code="system.button"/>
                    </button>

                    <div class="recognitionSendCancelDialog" style="display:none">
                        <p>
                            <b><cms:contentText key="CANCEL_RECOGNITION" code="recognition.send.manager"/></b>
                        </p>
                        <p>
                            <cms:contentText key="CHANGES_DISCARDED" code="recognition.send.manager"/>
                        </p>
                        <p class="tc">
                            <button type="submit" id="recognitionSendCancelDialogConfirm" class="btn btn-primary"><cms:contentText key="YES" code="system.button"/></button>
                            <button type="submit" id="recognitionSendCancelDialogCancel" class="btn btn-inverse btn-primary"><cms:contentText key="NO" code="system.button"/></button>
                        </p>
                    </div>

                </fieldset><!-- /recognitionFieldsetActions -->


            </html:form><!-- /#sendForm -->

        </div>

    </div><!-- /.row-fluid -->
</div><!-- /#recognitionPageSendView -->

<script type="text/template" id="participantRowAwardItemTpl">
  <%@include file="participantRowAwardItem.jsp" %>
</script>


<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
    $(document).ready(function(){

    	//public profile page Mini Profile Popup JSON
        G5.props.URL_JSON_PARTICIPANT_INFO = '<%=RequestUtils.getBaseURI(request)%>/participantPublicProfile.do?method=populatePax';

        //public profile page Mini Profile PopUp Follow Unfollow Pax JSON, Action Class is PublicRecognitionAction
        G5.props.URL_JSON_PARTICIPANT_FOLLOW = '<%=RequestUtils.getBaseURI(request)%>/publicRecognitionFollowUnFollowPax.do?method=followUnFollowPax';

        // attach the view to an existing DOM element
        window.recognitionPageAddPointsView = new RecognitionPageAddPointsView({
            el: $('#recognitionPageAddPointsView'),
            pageNav : {
                back : {
                    text : '<cms:contentText key="BACK" code="system.button" />',
                    url : '${pageContext.request.contextPath}/homePage.do'
                },
                home : {
                    text : '<cms:contentText key="HOME" code="system.general" />',
                    url : '${pageContext.request.contextPath}/homePage.do'
                }
            },
            pageTitle : '<cms:contentText key="PAGE_TITLE" code="recognition.send.manager"/>'
        });

    });
</script>

