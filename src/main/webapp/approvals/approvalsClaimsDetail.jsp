<!-- ======== APPROVALS: APPROVAL DETAILS ======== -->
<%--UI REFACTORED--%>
<%@page import="com.biperf.core.utils.UserManager"%>
<%@ page import="java.util.*"%>
<%@ page import="com.biperf.core.domain.claim.ProductClaim"%>
<%@ page import="com.biperf.core.domain.claim.Claim"%>
<%@ page import="com.biperf.core.ui.approvals.ApprovalsClaimDetailsForm"%>
<%@ page import="java.lang.ClassCastException"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>
<%@ include file="/include/taglib.jspf"%>
<!-- ======== APPROVALS: APPROVAL DETAILS ======== -->
<script type="text/javascript">
    function dispatchSave()
    {
        document.forms[0].action = "<%=RequestUtils.getBaseURI(request)%>/claim/approvalsClaimDetailsUpdate.do?method=saveApprovals";
        document.forms[0].submit();
    }
</script>

<div id="approvalsPageClaimsDetail" class="approvalSearchWrapper page-content">
  <div id="approvalsNominationDetailWrapper">
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

    <div class="row-fluid">
		<div class="span6">
            <h3><c:out value="${claimDetails.promotion.name}"/></h3>
        </div>
		<div class="span6">
	        <ul class="export-tools fr">
	            <li><a href="#" class="pageView_print btn btn-small"><cms:contentText code="system.button" key="PRINT"/> <i class="icon-printer"></i></a></li>
	        </ul>
	    </div>
	</div>

	<div class="row-fluid">
	  <div class="span12">
	    <legend><cms:contentText code="claims.product.approval.details" key="CLAIM_INFORMATION"/></legend>
		<div id="claimDetails">
		  <dl class="dl-horizontal">
			<dt><cms:contentText code="claims.product.approval.details" key="CLAIM_NUMBER"/></dt>
			<dd><c:out value="${claimDetails.claimNumber}"/>&nbsp;</dd>

			<dt><cms:contentText code="claims.product.approval.details" key="CLAIM_DATE"/></dt>
			<dd><fmt:formatDate value="${claimDetails.auditCreateInfo.dateCreated}" pattern="${JstlDatePattern}" />&nbsp;</dd>

			<c:forEach items="${claimDetails.claimElements}" var="claimElement" varStatus="status">
 			  <c:if test="${claimElement.claimFormStepElement.claimFormElementType.sectionHeading}">
 			    <dt><cms:contentText key="${claimElement.claimFormStepElement.cmKeyForHeading}" code="$claimDetails.promotion.claimForm.cmAssetCode}"/></dt>
                <dd>&nbsp;</dd>
        	  </c:if>

        	  <c:if test="${claimElement.claimFormStepElement.claimFormElementType.textBoxField || claimElement.claimFormStepElement.claimFormElementType.textField || claimElement.claimFormStepElement.claimFormElementType.numberField}">
        		<dt><cms:contentText key="${claimElement.claimFormStepElement.cmKeyForElementLabel}" code="${claimDetails.promotion.claimForm.cmAssetCode}"/></dt>
                <dd>
                	<c:if test="${claimElement.claimFormStepElement.maskedOnEntry}">
                  		*************
              		</c:if>
              		<c:if test="${!claimElement.claimFormStepElement.maskedOnEntry}">
                  		<c:out value="${claimElement.value}"/>
              		</c:if>
              		&nbsp;
                </dd>
        	  </c:if>

        	  <c:if test="${claimElement.claimFormStepElement.claimFormElementType.booleanField}">
          		<dt><cms:contentText key="${claimElement.claimFormStepElement.cmKeyForElementLabel}" code="${claimDetails.promotion.claimForm.cmAssetCode}"/></dt>
          		<dd>
          			<c:if test="${claimElement.value == 'true'}">
                  		<cms:contentText code="${claimDetails.promotion.claimForm.cmAssetCode}" key="${claimElement.claimFormStepElement.cmKeyForLabelTrue}"/>
                	</c:if>
                	<c:if test="${claimElement.value == 'false'}">
                    	<cms:contentText code="${claimDetails.promotion.claimForm.cmAssetCode}" key="${claimElement.claimFormStepElement.cmKeyForLabelFalse}"/>
                	</c:if>
                	&nbsp;
                </dd>
        	  </c:if>

        	  <c:if test="${claimElement.claimFormStepElement.claimFormElementType.dateField}">
         		<dt><cms:contentText key="${claimElement.claimFormStepElement.cmKeyForElementLabel}" code="${claimDetails.promotion.claimForm.cmAssetCode}"/></dt>
                <dd><c:out value="${claimElement.value}"/>&nbsp;</dd>
        	  </c:if>

        	  <c:if test="${claimElement.claimFormStepElement.claimFormElementType.addressBlock}">
          		<dt><cms:contentText key="${claimElement.claimFormStepElement.cmKeyForElementLabel}" code="${claimDetails.promotion.claimForm.cmAssetCode}"/></dt>
                <dd><beacon:addressDisplay address="${claimElement.value}"/>&nbsp;</dd>
        	  </c:if>

        	  <c:if test="${claimElement.claimFormStepElement.claimFormElementType.selectField}">
          		<dt><cms:contentText key="${claimElement.claimFormStepElement.cmKeyForElementLabel}" code="${claimDetails.promotion.claimForm.cmAssetCode}"/></dt>
                <dd><c:out value="${claimElement.pickListItems[0].name}"/>&nbsp;</dd>
        	  </c:if>

        	  <c:if test="${claimElement.claimFormStepElement.claimFormElementType.multiSelectField}">
          		<dt><cms:contentText key="${claimElement.claimFormStepElement.cmKeyForElementLabel}" code="${claimDetails.promotion.claimForm.cmAssetCode}"/></dt>
                <dd>
                	<c:forEach items="${claimElement.pickListItems}" var="item" varStatus="status">
                  		<c:out value="${item.name}"/><br>
                	</c:forEach>
                	&nbsp;
                </dd>
        	  </c:if>
            </c:forEach>
            <dt><cms:contentText code="claims.product.approval" key="SUBMITTER"/></dt>
			<dd><c:out value="${claimDetails.submitter.nameLFMWithComma}" />&nbsp;</dd>
		  </dl>
		</div>

                <html:form action="approvalsClaimDetailsUpdate.do?method=saveApprovals" styleClass="form-horizontal" styleId="claimSubmissionForm">
                    <h3><cms:contentText code="claims.submission" key="PRODUCT_INFORMATION"/></h3>
                    <div class="claimTableWrapper">
                        <table id="claimProductInfoTable" class="table table-striped">
                            <thead>
                                <tr>
                                    <th><cms:contentText code="claims.product.approval.details" key="CATEGORY"/></th>
                                    <th><cms:contentText code="claims.product.approval.details" key="SUBCATEGORY"/></th>
                                    <th><cms:contentText code="claims.product.approval.details" key="PRODUCT"/></th>
                                    <th><cms:contentText code="claims.product.approval.details" key="CHARACTERISTICS"/></th>
                                    <th><cms:contentText code="claims.product.approval.details" key="QUANTITY"/></th>
                                    <th><cms:contentText code="claims.product.approval" key="APPROVER"/></th>
                                    <th class="approvalsActionColumn"><cms:contentText code="claims.product.approval.details" key="ACTION"/></th>
                                </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${claimDetails.claimProducts}" var="claimProduct" varStatus="claimProductStatus">
                                <c:set var="claimProductIdString">
                                  <c:out value="${claimProduct.id}" />
                                </c:set>
                                <c:set var="formApprovalStatusTypeCode" value="${approvalsClaimsListForm.claimProductApprovalFormByClaimProductIdString[claimProductIdString].approvalStatusType}"/>
                                <c:set var="formDeniedReasonTypeCode" value="${approvalsClaimsListForm.claimProductApprovalFormByClaimProductIdString[claimProductIdString].denyPromotionApprovalOptionReasonType}"/>
                                <c:set var="formHoldReasonTypeCode" value="${approvalsClaimsListForm.claimProductApprovalFormByClaimProductIdString[claimProductIdString].holdPromotionApprovalOptionReasonType}"/>
                                <c:set var="showableClaimProductIndex" value="${showableClaimProductIndex + 1}" />
                                <tr>
                                    <td><c:out value="${claimProduct.product.productCategoryName}"/></td>
                                    <td><c:out value="${claimProduct.product.productSubCategoryName}"/></td>
                                    <td><c:out value="${claimProduct.product.name}"/></td>
                                    <td class="characteristics">
                                        <c:forEach items="${claimProduct.claimProductCharacteristics}" var="claimProductChar" varStatus="status">
                                        <span class="char"><c:out value="${claimProductChar.value}"/></span>
                                        </c:forEach>
                                    </td>
                                    <td><c:out value="${claimProduct.quantity}"/></td>
                                    <c:choose>
                                        <c:when test="${formApprovalStatusTypeCode ne 'pend' && claimProduct.currentApproverUser != null }">
                                            <td><c:out value="${claimProduct.currentApproverUser.nameLFMWithComma}"/></td>
                                        </c:when>
                                        <c:when test="${formApprovalStatusTypeCode ne 'pend' && claimProduct.currentApproverUser == null }">
                                            <td><cms:contentText code="home.navmenu.admin" key="SYSTEM" /></td>
                                        </c:when>
                                        <c:otherwise><td></td></c:otherwise>
                                    </c:choose>

                                    <td class="hasSelects">
                                        <c:choose>
                                            <c:when test="${formApprovalStatusTypeCode == 'approv' || formApprovalStatusTypeCode == 'deny'}">
                                                    <c:out value="${claimProduct.approvalStatusType.name}" />
                                                    <c:if test="${formApprovalStatusTypeCode == 'deny'}">
                                                            :
                                                            <c:forEach items="${claimDetails.promotion.deniedReasonCodeTypes}" var="deniedReasonType" varStatus="reasonStatus">
                                                            <c:if test="${deniedReasonType.code eq formDeniedReasonTypeCode}">
                                                            <c:out value="${deniedReasonType.name}" />
                                                            </c:if>
                                                            </c:forEach>
                                                    </c:if>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="status validateme" data-validate-flags="nonempty" data-validate-fail-msgs='{"nonempty" : "<cms:contentText code="claims.product.approval.details" key="PLEASE_SELECT_STATUS"/>"}'>
                                                    <c:if test="${formApprovalStatusTypeCode == 'pend' || formApprovalStatusTypeCode == 'hold'}">
                                                    <html:select styleId="${applyAllClassClaimAndClaimProductId}-status"
                                                            styleClass="approvalStatusType" property="claimProductApprovalFormByClaimProductIdString(${claimProduct.id}).approvalStatusType">
                                                    <c:set var="approvalOptionTypes" value="${claimDetails.promotion.approvalOptionTypes}" />
                                                    <html:options collection="approvalOptionTypes" property="code" labelProperty="name" />
                                                    </html:select>
                                                    </c:if>
                                                </span>

                                                <span class="statusReason denyReason validateme" data-validate-flags="nonempty" data-validate-fail-msgs='{"nonempty" : "<cms:contentText code="claims.product.approval.details" key="PLEASE_SELECT_REASON"/>"}'>
                                                    <c:set var="deniedReasonTypes" value="${claimDetails.promotion.deniedReasonCodeTypes}" />
                                                    <html:select styleId="deny-${applyAllClassClaimAndClaimProductId}-reason"
                                                            property="claimProductApprovalFormByClaimProductIdString(${claimProduct.id}).denyPromotionApprovalOptionReasonType">
                                                        <html:option value=""><cms:contentText code="claims.product.approval" key="SELECT_RC" /></html:option>
                                                        <html:options collection="deniedReasonTypes" property="code" labelProperty="name" />
                                                    </html:select>
                                                </span>
                                                <span class="statusReason holdReason validateme" data-validate-flags="nonempty" data-validate-fail-msgs='{"nonempty" : "<cms:contentText code="claims.product.approval.details" key="PLEASE_SELECT_REASON"/>"}'>
                                                    <c:set var="heldReasonTypes" value="${claimDetails.promotion.heldReasonCodeTypes}" />
                                                    <html:select styleId="onHoldReasonSelect"
                                                            property="claimProductApprovalFormByClaimProductIdString(${claimProduct.id}).holdPromotionApprovalOptionReasonType">
                                                        <html:option value=""><cms:contentText code="claims.product.approval" key="SELECT_RC" /></html:option>
                                                        <html:options collection="heldReasonTypes" property="code" labelProperty="name" />
                                                    </html:select>
                                                </span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>

		<c:if test="${claimDetails.promotion.promotionType.productClaimPromotion && claimDetails.promotion.teamUsed}">
		  <legend><cms:contentText code="claims.submission" key="TEAM_INFORMATION" /></legend>
		  <dl class="dl-horizontal">
		  	<c:if test="${not empty claimDetails.submitter.lastName}">
			  <dt>
		    	<cms:contentText code="claims.submission" key="SUBMITTER"/>
		      </dt>
		      <dd>
		    	<p><c:out value="${claimDetails.submitter.firstName}"/>&nbsp;<c:out value="${claimDetails.submitter.lastName}"/></p>&nbsp;
		      </dd>
			</c:if>

    		<c:if test="${claimDetails.promotion.teamCollectedAsGroup}">
      		  <c:forEach items="${claimDetails.claimParticipants}" var="productClaimParticipant">
        		<dt>
	            	<cms:contentText code="claims.submission" key="TEAM_MEMBER"/>
	      		</dt>
	      		<dd>
				  <p><c:if test="${not empty productClaimParticipant.participant.lastName}">
					<c:out value="${productClaimParticipant.participant.firstName}"/>&nbsp;<c:out value="${productClaimParticipant.participant.lastName}"/>
				  </c:if>
				  <c:if test="${empty productClaimParticipant.participant.lastName}">
					<cms:contentText key="NOT_ASSIGNED" code="claims.submission"/>
				  </c:if></p>&nbsp;
      	  		</dd>
      		  </c:forEach>
    		</c:if>

    		<c:if test="${not claimDetails.promotion.teamCollectedAsGroup}">
     		  <c:forEach items="${claimParticipantList}" var="productClaimParticipant">
       			<dt>
	            	<cms:contentText code="claims.submission" key="TEAM_MEMBER"/>
	     		</dt>
     	 		<dd>
     	 		  <p><c:if test="${productClaimParticipant.participant != null}">
	           		<c:out value="${productClaimParticipant.participant.firstName}"/>&nbsp;<c:out value="${productClaimParticipant.participant.lastName}"/>
	        	  </c:if>
	        	  <c:if test="${productClaimParticipant.participant == null}">
	           		<cms:contentText key="NOT_ASSIGNED" code="claims.submission"/>
	        	  </c:if>
     	 		  &nbsp;&nbsp;
     	   		  <c:out value="${productClaimParticipant.promotionTeamPosition.promotionJobPositionType.name}"/></p>&nbsp;
     	 		</dd>
     		  </c:forEach>
    		</c:if>
		  </dl>
		</c:if>

                <fieldset>
                    <div>
                        <div class="control-group">
                            <label for="approverComments" class="control-label">
                                <cms:contentText code="claims.product.approval.details" key="APPROVER_COMMENTS"/>
                                <span class="optional"><cms:contentText code="claims.product.approval.details" key="OPTIONAL"/></span>
                            </label>
                            <div class="controls">
                                <textarea id="subject" name="approverComments" rows="4" class="approverComments" <c:if test="${!approvalsClaimsListForm.open}">disabled="true"</c:if>><c:out value="${claimDetails.approverComments}"/></textarea>
                            </div>
                        </div>

                        <div class="control-group">
                            <label for="adminComments" class="control-label">
                                <cms:contentText code="claims.product.approval.details" key="ADMIN_COMMENTS"/>
                                <span class="optional"><cms:contentText code="claims.product.approval.details" key="OPTIONAL"/></span>
                            </label>
                            <div class="controls">
                                <textarea name="adminComments" rows="4" class="adminComments" <c:if test="${!approvalsClaimsListForm.open}">disabled="true"</c:if>><c:out value="${claimDetails.adminComments}"/></textarea>
                            </div>
                        </div>
                        <c:set var="count" value="0" scope="page" />
                        <c:forEach items="${claimDetails.claimProducts}" var="claimProduct" varStatus="claimProductStatusIndex">
                        <c:if test="${count < 1}">
                        <c:set var="claimProductIdString">
                        <c:out value="${claimProduct.id}" />
                        </c:set>
		                <c:set var="formApprovalStatusTypeCode" value="${approvalsClaimsListForm.claimProductApprovalFormByClaimProductIdString[claimProductIdString].approvalStatusType}"/>
                        <c:if test="${formApprovalStatusTypeCode ne 'approv' && formApprovalStatusTypeCode ne 'deny' && formApprovalStatusTypeCode ne 'expired'}">
                        <div class="form-actions">
                            <beacon:authorize ifNotGranted="LOGIN_AS">
                              <html:submit styleId="submitButton" styleClass="btn btn-primary  btn-fullmobile"><cms:contentText code="system.button" key="SUBMIT"/></html:submit>
                            </beacon:authorize>
                            <a href="#" id="cancelButton" class="btn cancelBtn btn-fullmobile"><cms:contentText code="system.button" key="CANCEL"/></a>

                            <div class="approvalsClaimsCancelDialog" style="display:none">
                                <p>
                                    <b><cms:contentText key="CANCEL_THIS_CLAIMS" code="claims.submission"/></b>
                                </p>
                                <p>
                                    <cms:contentText key="ALL_CHANGES_DISCARDED" code="claims.submission"/>
                                </p>
                                <p class="tc">
                                    <a href="approvalsProductClaimList.do" id="approvalsClaimsCancelDialogConfirm" class="btn btn-primary"><cms:contentText key="YES" code="system.button"/></a>
                                    <a href="#" id="approvalsClaimsCancelDialogCancel" class="btn"><cms:contentText key="NO" code="system.button"/></a>
                                </p>
                            </div>
                        </div>
                        <c:set var="count" value="${count + 1}" scope="page" />
                        </c:if>
                        </c:if>
                        </c:forEach>
                    </div>
                </fieldset>

		<beacon:client-state>
			<beacon:client-state-entry name="claimId" value="${approvalsClaimDetailsForm.claimId}" />
		</beacon:client-state>

	  </html:form>
	  </div>
	</div>
  </div>
  <div id="sameForAllTipTpl" class="sameForAllTip" style="display:none">
    <a class="approvalsToolTip"><cms:contentText code="recognition.approval.list" key="SAME_FOR_ALL" /></a>
  </div>
</div>

<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
    //attach the view to an existing DOM element
    $(document).ready(function() {
    	<%	Map parameterMap = new HashMap();
			Claim temp = (Claim)request.getAttribute("claimDetails");
			parameterMap.put( "claimId", temp.getId() );
			pageContext.setAttribute("productInfoUrl", ClientStateUtils.generateEncodedLink( "", "claim/getProductInfo.do?method=populateProductInfo", parameterMap ) );
	  	%>
    	G5.props.URL_JSON_APPROVALS_LOAD_CLAIM_DETAIL_DATA = G5.props.URL_ROOT+'<c:out value="${productInfoUrl}" escapeXml="false"/>';

        window.acdmv = new ApprovalsClaimDetailModelView({
            el : $('#approvalsPageClaimsDetail'),
            pageNav : {
                back : {
                    text : '<cms:contentText key="BACK" code="system.button" />',
                    url : 'approvalsProductClaimList.do'
                },
                home : {
                    text : '<cms:contentText key="HOME" code="system.general" />',
                    url : '${pageContext.request.contextPath}/homePage.do'
                }
            },
            pageTitle : '<cms:contentText key="DETAIL_HEADER" code="claims.details" />'
        });
    });
</script>
