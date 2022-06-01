<%@ include file="/include/taglib.jspf" %>
<%@ page import="java.util.*"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>

<!-- ======== CLAIM PREVIEW PAGE ======== -->

<div id="claimPagePreviewView" class="claimPagePreview-liner page-content">
	<html:form action="submitClaim">

        <div class="row-fluid">
            <div class="span6">
                <dl class="dl-horizontal">

                    <dt><cms:contentText key="PROMOTION" code="claims.submission" /></dt>
                    <dd>${productClaimSubmissionForm.promotionName}&nbsp;</dd>

                    <c:if test="${productClaimSubmissionForm.nodeId != null}">
                      <dt><cms:contentText key="ORG_UNIT" code="claims.submission" /></dt>
                      <dd>${productClaimSubmissionForm.orgUnitName}&nbsp;</dd>
                    </c:if>

                    <dt><cms:contentText key="CLAIM_STATUS" code="claims.submission" /></dt>
                    <dd><cms:contentText code="claims.product.approval.details" key="OPEN"/>&nbsp;</dd>

                 	<c:forEach items="${productClaimSubmissionForm.claimElements}" var="claimElement" varStatus="status">
                  	  <c:if test="${claimElement.claimFormStepElement.claimFormElementType.sectionHeading}">
          				<dt><strong><cms:contentText key="${claimElement.claimFormStepElement.cmKeyForHeading}" code="${claimElement.claimFormAssetCode}"/></strong></dt>
            			<dd>&nbsp;</dd>
        			  </c:if>

        			  <c:if test="${claimElement.claimFormStepElement.claimFormElementType.textBoxField || claimElement.claimFormStepElement.claimFormElementType.textField || claimElement.claimFormStepElement.claimFormElementType.numberField}">
          				<dt><strong><cms:contentText key="${claimElement.claimFormStepElement.cmKeyForElementLabel}" code="${claimElement.claimFormAssetCode}"/></strong></dt>
            			<dd>
                		  <c:if test="${claimElement.claimFormStepElement.maskedOnEntry}">
                  			*************
              			  </c:if>
              			  <c:if test="${!claimElement.claimFormStepElement.maskedOnEntry}">
                  			<c:out value="${claimElement.value}"/>
                  		  </c:if>&nbsp;
            			</dd>
        			  </c:if>

        			  <c:if test="${claimElement.claimFormStepElement.claimFormElementType.booleanField}">
          				<dt><strong><cms:contentText key="${claimElement.claimFormStepElement.cmKeyForElementLabel}" code="${claimElement.claimFormAssetCode}"/></strong></dt>
            			<dd>
                		  <c:if test="${claimElement.value == 'true'}">
                  			<cms:contentText code="${claimElement.claimFormAssetCode}" key="${claimElement.claimFormStepElement.cmKeyForLabelTrue}"/>
                		  </c:if>
                		  <c:if test="${claimElement.value == 'false'}">
                    		<cms:contentText code="${claimElement.claimFormAssetCode}" key="${claimElement.claimFormStepElement.cmKeyForLabelFalse}"/>
                		  </c:if>&nbsp;
                		</dd>
        			  </c:if>

        			  <c:if test="${claimElement.claimFormStepElement.claimFormElementType.dateField}">
          				<dt><strong><cms:contentText key="${claimElement.claimFormStepElement.cmKeyForElementLabel}" code="${claimElement.claimFormAssetCode}"/></strong></dt>
            			<dd><c:out value="${claimElement.value}"/>&nbsp;</dd>
        			  </c:if>

        			  <c:if test="${claimElement.claimFormStepElement.claimFormElementType.addressBlock}">
          				<dt><strong><cms:contentText key="${claimElement.claimFormStepElement.cmKeyForElementLabel}" code="${claimElement.claimFormAssetCode}"/></strong></dt>
          				<c:if test="${not empty claimElement.value}">
            			<dd><beacon:addressDisplay address="${claimElement.value}"/>&nbsp;</dd>
        			  </c:if>
        			  </c:if>

        			  <c:if test="${claimElement.claimFormStepElement.claimFormElementType.link}">
            			<dt><strong><a href="<c:out value="${claimElement.claimFormStepElement.linkURL}"/>"><cms:contentText key="${claimElement.claimFormStepElement.cmKeyForLinkName}" code="${claimElement.claimFormAssetCode}"/></a></strong></dt>
            			<dd>&nbsp;</dd>
        			  </c:if>

        			  <c:if test="${claimElement.claimFormStepElement.claimFormElementType.selectField}">
          				<dt><strong><cms:contentText key="${claimElement.claimFormStepElement.cmKeyForElementLabel}" code="${claimElement.claimFormAssetCode}"/></strong></dt>
            			<dd>
            			  <c:out value="${claimElement.pickListItems[0].name}"/>&nbsp;
            			</dd>
        			  </c:if>

        			  <c:if test="${claimElement.claimFormStepElement.claimFormElementType.multiSelectField}">
          				<dt><strong><cms:contentText key="${claimElement.claimFormStepElement.cmKeyForElementLabel}" code="${claimElement.claimFormAssetCode}"/></strong></dt>
            			<dd>
                		  <c:forEach items="${claimElement.pickListItems}" var="item" varStatus="status">
                  			<c:out value="${item.name}"/><br>
                		  </c:forEach>&nbsp;
                		</dd>
        			  </c:if>
      			  	</c:forEach>
                </dl>
            </div>

            <c:if test="${not empty productClaimSubmissionForm.teamMembers}">
              <div class="span5 offset1">
                <h4><cms:contentText key="TEAM_MEMBER" code="claims.submission" /></h4>
                <ul>
                  <c:forEach items="${productClaimSubmissionForm.teamMembers}" var="claimParticipant" varStatus="status">
                    <li><c:out value="${claimParticipant.firstName}"/> <c:out value="${claimParticipant.lastName}"/></li>
                  </c:forEach>
                </ul>
              </div>
            </c:if>
        </div>

        <div class="row-fluid">
            <div class="span12">
              <display:table name="${productClaimSubmissionForm.products}" id="productBean" class="table table-striped crud-table"
                           pagesize="50" sort="list" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>" >
                <%-- Quantity --%>
                <display:column titleKey="claims.submission.QUANTITY" class="sortable discussionsColumn sorted ascending"  sortable="false">
                <c:out value="${productBean.quantity}"/>
                </display:column>

                <%-- Product --%>
                <display:column titleKey="claims.submission.PRODUCT" class="sortable discussionCountColumn unsorted" sortable="false">
                <c:out value="${productBean.name}"/>
                </display:column>

                <%-- Category --%>
                <display:column titleKey="claims.submission.CATEGORY" class="sortable repliesColumn unsorted" sortable="false">
                        <c:out value="${productBean.category}"/>
                </display:column>

                <%-- Subcategory --%>
                <display:column titleKey="claims.submission.SUBCATEGORY" class="sortable lastPostColumn unsorted" sortable="false">
                        <c:out value="${productBean.subcategory}"/>
                </display:column>

                <%-- Characteristics --%>
                <display:column titleKey="claims.submission.CHARACTERISTICS" class="sortable lastPostColumn unsorted" sortable="false">
                  <c:forEach var="characteristicsBean" items="${productBean.characteristicsValues}" varStatus="status">
                  <c:if test="${not empty characteristicsBean.value || not empty characteristicsBean.values}">
                    <c:out value="${characteristicsBean.name}"/><br>
                    </c:if>
                    <c:choose>
                    <c:when test="${not empty characteristicsBean.values}">
                    <c:forEach var="claimProductCharValue" items="${characteristicsBean.values}" varStatus="valStatus">
                    <c:forEach var="claimProductCharElement" items="${characteristicsBean.characteristicsTypeListValues}" varStatus="clStatus">
                    <c:if test="${claimProductCharValue eq claimProductCharElement.id}">
                    <c:out value="${claimProductCharElement.name}"/><br>
                    </c:if>
                    </c:forEach>
                    </c:forEach>
                    </c:when>
                    <c:otherwise>
                    <c:out value="${characteristicsBean.value}"/>
                    </c:otherwise>
                    </c:choose>
                    <br/>
                  </c:forEach>
                </display:column>
              </display:table>

                <div class="form-actions pullBottomUp">
                  <beacon:authorize ifNotGranted="LOGIN_AS">
                    <button class="btn btn-primary goToLink" id="claimButtonSend" type="submit"><cms:contentText key="SUBMIT" code="system.button"/></button>
                  </beacon:authorize>
                    <button type="button" data-url="${pageContext.request.contextPath}/claim/editClaimSubmission.do?method=showClaim" id="ClaimButtonEdit" class="btn goToLink"><cms:contentText key="EDIT" code="system.button"/></button>
                    <button type="button" data-url="${pageContext.request.contextPath}/homePage.do" id="claimButtonCancel" class="btn goToLink"><cms:contentText key="CANCEL" code="system.button"/></button>
                </div>
            </div>
        </div>
    </html:form>

    <div class="claimSendCancelDialog" style="display:none">
        <p>
            <i class="icon-question"></i>
            <b><cms:contentText key="ARE_YOU_SURE" code="claims.submission" /></b>
        </p>
        <p>
            <cms:contentText key="ALL_CHANGES_DISCARDED" code="claims.submission" />
        </p>
        <p class="tc">
            <button type="submit" id="claimCancelDialogConfirm" class="btn btn-primary dialog-option"><cms:contentText key="YES" code="system.button"/></button>
            <button type="submit" id="claimCancelDialogCancel" class="btn dialog-option"><cms:contentText key="NO" code="system.button"/></button>
        </p>
    </div>
</div>

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>

    //attach the view to an existing DOM element
    $(function(){

    var cpp = new ClaimPageView({
        el:$('#claimPagePreviewView'),
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
        pageTitle : '<cms:contentText key="SUBMIT_CLAIM" code="claims.submission" />'
    });

    });

</script>
