<%@page import="com.biperf.core.domain.claim.ProductClaim"%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.claim.Claim"%>
<%@ page import="com.biperf.core.ui.claim.ProductClaimValueObject"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<!-- ======== PROFILE: CLAIM DETAILS ======== -->

<div id="profilePageClaimDetailView" class="claimPage-liner page-content">
    <div class="row-fluid">
        <div class="span12">
            <ul class="export-tools fr">
                <li><a href="#" class="pageView_print btn btn-small"><cms:contentText code="system.button" key="PRINT"/> <i class="icon-printer"></i></a></li>
            </ul>
            <h2><c:out value="${claimDetails.promotion.name}"/></h2>
        </div>
    </div>

    <div class="row-fluid">
        <div class="span12">
            <h3><cms:contentText code="claims.product.approval.details" key="CLAIM_INFORMATION"/></h3>
            <dl class="dl-horizontal dl-h1">
                <dt><cms:contentText code="claims.product.approval.details" key="CLAIM_NUMBER"/></dt>
                <dd><c:out value="${claimDetails.claimNumber}"/></dd>

                <dt><cms:contentText code="claims.product.approval.details" key="CLAIM_DATE"/></dt>
                <dd><fmt:formatDate value="${claimDetails.auditCreateInfo.dateCreated}" pattern="${JstlDatePattern}" /></dd>

                <dt><cms:contentText code="claims.product.approval.details" key="CLAIM_STATUS"/></dt>
                <dd>
                	<c:choose>
		    			<c:when test="${claimDetails.open}">
		    				<cms:contentText code="claims.product.approval.details" key="OPEN"/>
		    			</c:when>
		    			<c:otherwise>
		    				<cms:contentText code="claims.product.approval.details" key="CLOSED"/>
		    			</c:otherwise>
		    		</c:choose>
                </dd>
			  <c:if test="${!claimDetails.open}">
                <dt><cms:contentText code="claims.product.approval.details" key="APPROVED_BY"/></dt>
                <dd>
                  <c:if test="${!systemApproved}">
              		<c:if test="${mostRecentClaimProduct.currentClaimItemApprover != null}">
                	  <c:set var="approverUser" value="${mostRecentClaimProduct.currentClaimItemApprover.approverUser}"/>
                	  <c:if test="${approverUser.lastName != null}">
                  		<c:out value="${approverUser.firstName}"/>&nbsp;
                  		<c:out value="${approverUser.lastName}"/>
                	  </c:if>
              		</c:if>
        	      </c:if>
        		  <c:if test="${systemApproved}">
        			<cms:contentText code="claims.product.approval.details" key="SYSTEM"/>
        		  </c:if>
                </dd>

                <dt><cms:contentText code="claims.product.approval.details" key="APPROVAL_DATE"/></dt>
                <dd><fmt:formatDate value="${mostRecentClaimProduct.dateApproved}" pattern="${JstlDatePattern}" /></dd>
              </c:if>

 			  <c:forEach items="${claimDetails.claimElements}" var="claimElement" varStatus="status">
 			   	<c:if test="${claimElement.claimFormStepElement.claimFormElementType.sectionHeading}">
 			    	<dt><cms:contentText key="${claimElement.claimFormStepElement.cmKeyForHeading}" code="${claimDetails.promotion.claimForm.cmAssetCode}"/></dt>
                	<dd>&nbsp;</dd>
        		</c:if>

        		<c:if test="${claimElement.claimFormStepElement.claimFormElementType.textBoxField || claimElement.claimFormStepElement.claimFormElementType.textField || claimElement.claimFormStepElement.claimFormElementType.numberField}">
        			<dt><cms:contentText key="${claimElement.claimFormStepElement.cmKeyForElementLabel}" code="${claimDetails.promotion.claimForm.cmAssetCode}"/></dt>
                	<dd>
                	  <c:if test="${claimElement.claimFormStepElement.maskedOnEntry}">
                  		*************
              		  </c:if>
              		  <c:if test="${!claimElement.claimFormStepElement.maskedOnEntry}">
                  		<c:out value="${claimElement.value}"/>&nbsp;
              		  </c:if>
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

        		<c:if test="${claimElement.claimFormStepElement.claimFormElementType.copyBlock}">
            			<dt><strong><cms:contentText key="${claimElement.claimFormStepElement.cmKeyForCopyBlock}" code="${claimDetails.promotion.claimForm.cmAssetCode}"/></strong></dt>
            			<dd>&nbsp;</dd>
        			  </c:if>

               <c:if test="${claimElement.claimFormStepElement.claimFormElementType.link}">
            			<dt><strong><a href="<c:out value="${claimElement.claimFormStepElement.linkURL}"/>"><cms:contentText key="${claimElement.claimFormStepElement.cmKeyForLinkName}" code="${claimDetails.promotion.claimForm.cmAssetCode}"/></a></strong></dt>
            			<dd>&nbsp;</dd>
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
                	  </c:forEach>&nbsp;
                	</dd>
        		</c:if>
              </c:forEach>

              <c:if test="${claimDetails.approverComments != null && claimDetails.approverComments != ''}">
	        	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<%  Map paramMap = new HashMap();
								Claim temp = (Claim)request.getAttribute("claimDetails");
								paramMap.put( "claimId", temp.getId() );
								pageContext.setAttribute("linkUrl", ClientStateUtils.generateEncodedLink( "", "claimDetailsApproverCommentsDisplay.do", paramMap, true ) );
						%>
		          <a class="crud-content-link" href="javascript:popUpWin('<c:out value="${linkUrl}"/>','console', 750, 500, false, true);" >
								<cms:contentText code="claims.product.approval.details" key="SEE_COMMENTS"/>
			      </a>
	      	</c:if>
            </dl>
        </div>
    </div>

    <c:if test="${claimDetails.promotion.teamUsed}">
      <div class="row-fluid">
        <div class="span12">
            <h3><cms:contentText code="claims.submission" key="TEAM_INFORMATION" /></h3>
            <dl class="dl-horizontal dl-h1">
              <c:if test="${not empty claimDetails.submitter.lastName}">
                <dt><cms:contentText code="claims.submission" key="SUBMITTER"/></dt>
                <dd><c:out value="${claimDetails.submitter.firstName}"/>&nbsp;<c:out value="${claimDetails.submitter.lastName}"/></dd>
              </c:if>

              <c:if test="${claimDetails.promotion.teamCollectedAsGroup}">
                <c:forEach items="${claimDetails.claimParticipants}" var="productClaimParticipant">
                  <dt><cms:contentText code="claims.submission" key="TEAM_MEMBER"/></dt>
                  <dd>
                	<c:if test="${not empty productClaimParticipant.participant.lastName}">
						<c:out value="${productClaimParticipant.participant.firstName}"/>&nbsp;<c:out value="${productClaimParticipant.participant.lastName}"/>
					</c:if>
					<c:if test="${empty productClaimParticipant.participant.lastName}">
						<cms:contentText key="NOT_ASSIGNED" code="claims.submission"/>
					</c:if>
                  </dd>
                </c:forEach>
              </c:if>

              <c:if test="${not claimDetails.promotion.teamCollectedAsGroup}">
                <c:forEach items="${claimParticipantList}" var="productClaimParticipant">
                  <dt><cms:contentText code="claims.submission" key="TEAM_MEMBER"/></dt>
                  <dd>
                	<c:if test="${not empty productClaimParticipant.participant.lastName}">
						<c:out value="${productClaimParticipant.participant.firstName}"/>&nbsp;<c:out value="${productClaimParticipant.participant.lastName}"/>
					</c:if>
					<c:if test="${empty productClaimParticipant.participant.lastName}">
						<cms:contentText key="NOT_ASSIGNED" code="claims.submission"/>
					</c:if>
					</br>
					<c:out value="${productClaimParticipant.promotionTeamPosition.promotionJobPositionType.name}"/>
                  </dd>
                </c:forEach>
              </c:if>
            </dl>
        </div>
      </div>
    </c:if>

    <div class="row-fluid">
        <div class="span12">
            <h3><cms:contentText code="claims.submission" key="PRODUCT_INFORMATION"/></h3>
        <display:table defaultorder="ascending" name="claimProductsList" id="claimProductsViewBean" class="table table-striped crud-table"
                       pagesize="10" sort="list" requestURI="<%=RequestUtils.getOriginalRequestURI( request )%>" >

        <display:setProperty name="basic.msg.empty_list_row">
		<tr class="crud-content" align="left">
		<td colspan="{0}">
		<cms:contentText key="NOTHING_FOUND" code="system.errors" />
		</td>
                    </tr>
		</display:setProperty>
		<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>

		<display:column titleKey="claims.product.approval.details.CATEGORY" sortProperty="productCategoryName" class="sortable topicColumn sorted ascending" sortable="true">
		<c:out value="${claimProductsViewBean.productCategoryName}" />
		</display:column>

		<display:column titleKey="claims.product.approval.details.SUBCATEGORY" sortProperty="productSubCategory" class="sortable topicColumn sorted ascending" sortable="true">
		<c:out value="${claimProductsViewBean.productSubCategory}" />
		</display:column>

		<display:column titleKey="claims.product.approval.details.PRODUCT" class="sortable topicColumn sorted ascending" sortProperty="productName" sortable="true">
		<c:out value="${claimProductsViewBean.productName}" />
		</display:column>

		<display:column titleKey="claims.product.approval.details.CHARACTERISTICS" class="sortable topicColumn sorted ascending" sortProperty="claimProductCharacteristics" sortable="true">
		<c:forEach items="${claimProductsViewBean.claimProductCharacteristics}" var="claimProductCharacteristic">
			              	<c:out value="${claimProductCharacteristic.productCharacteristicType.characteristicName}"/><br>
			              	<c:out value="${claimProductCharacteristic.value}"/>
			              	<br>
			              </c:forEach>
		</display:column>

		<display:column titleKey="claims.product.approval.details.QUANTITY" class="sortable topicColumn sorted ascending" sortProperty="quantity" sortable="true">
		<c:out value="${claimProductsViewBean.quantity}" />
		</display:column>

		<display:column titleKey="claims.product.approval.details.STATUS" class="sortable topicColumn sorted ascending" sortProperty="approvalStatusTypeName" sortable="true">
		<c:out value="${claimProductsViewBean.approvalStatusTypeName}"/>
		<c:if test="${ claimProductsViewBean.promotionApprovalOptionReasonTypeName != null }">
		&nbsp;-&nbsp;<c:out value="${claimProductsViewBean.promotionApprovalOptionReasonTypeName}" />
			              </c:if>
		</display:column>

        </display:table>
        </div>
    </div>

  <c:if test="${!claimDetails.open}">
  	<c:choose>
      <c:when test="${empty journals}">
       	<div class="row-fluid">
          <div class="span12">
            <h3><cms:contentText code="claims.product.approval.details" key="PAYOUT_INFO"/> - <c:out value="${claimDetails.promotion.name}"/></h3>
            <dl class="dl-h2">
                <dt><cms:contentText code="claims.product.approval.details" key="NO_PAYOUT_INFO"/></dt>
                <dd></dd>
            </dl>
          </div>
    	</div>
      </c:when>
      <c:otherwise>
        <c:forEach items="${journals}" var="journal">
          <div class="row-fluid">
        	<div class="span12">
              <h3><cms:contentText code="claims.product.approval.details" key="PAYOUT_INFO"/> - <c:out value="${journal.promotion.name}"/></h3>
              <dl class="dl-horizontal dl-h2">
                <dt><cms:contentText code="claims.product.approval.details" key="DATE"/></dt>
                <dd><fmt:formatDate value="${journal.transactionDate}" pattern="${JstlDatePattern}" /></dd>

                <dt><cms:contentText code="claims.product.approval.details" key="AWARD"/></dt>
                <dd>
                  <c:if test="${journal.journalStatusType.code != 'pend_min_qual'}">
					<c:out value="${journal.transactionAmount}"/>
				  </c:if>
				  <c:if test="${journal.journalStatusType.code == 'pend_min_qual'}">
					0 (<cms:contentText key="DID_NOT_MEET_PAYOUT_CRITERIA" code="claims.list"/>)
				  </c:if>
                </dd>

                <c:if test="${journal.promotion.payoutCarryOver && journal.promotion.payoutType.code == 'tiered'}">
              	  <c:forEach items="${journal.activityJournals}" var="activityJournal">
               		<dt><cms:contentText code="claims.product.approval.details" key="QTY"/></dt>
                	<dd><c:out value="${activityJournal.activity.quantity}"/></dd>

                	<dt><cms:contentText code="claims.product.approval.details" key="CLAIM_NBR"/></dt>
                	<dd><c:out value="${activityJournal.activity.claim.claimNumber}"/></dd>
              	  </c:forEach>
            	</c:if>
              </dl>
        	</div>
    	  </div>
        </c:forEach>
      </c:otherwise>
    </c:choose>
  </c:if>
</div> <!-- ./claimPage-liner -->


<!-- Instantiate the PageView - this goes here as it expresses the DOM element the PageView is getting attached to -->
<script>
    $(document).ready(function(){
        //attach the view to an existing DOM element
        var prp = new PageView({
            el:$('#profilePageClaimDetailView'),
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
            pageTitle : '<cms:contentText key="DETAIL_HEADER" code="claims.details" />'
        });
    });
</script>
