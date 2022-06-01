<%@page import="com.biperf.core.utils.UserManager"%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.claim.Claim"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<!-- ======== APPROVALS: APPROVALS PENDING NOMINATIONS PAGE ======== -->

<div id="approvalsPagePendingNominations" class="page-content">
	<div class="row-fluid">
		<div class="span12">
			<h2><cms:contentText key="PAGE_TITLE" code="system.general" /></h2>
		</div>
	</div>
	<div id="pendingNominationsWrapper" class="approvalSearchWrapper">
		<div class="row-fluid">
			<div id="approvalsErrorBlock" class="span10 alert alert-block alert-error" style="display:none;">
				<h4><cms:contentText key="FOLLOWING_ERRORS" code="system.generalerror" /></h4>
		    	<ul>
		      		<html:messages id="actionMessage" >
		       			<c:set var="serverReturnedError" value="true"/>
		    			<li>${actionMessage}</li>
					</html:messages>
		     	</ul>
			</div>
			<input type="hidden" id="serverReturnedErrored" value="${serverReturnedError}">

			<c:if test="${ saveOccurred }">
				<div id="approvalsSaveSuccessBlock" class="span10 alert alert-block alert-info">
					<h4><cms:contentText key="SAVE_OCCURRED" code="claims.product.approval"/></h4>
				</div>
			</c:if>
			<div class="span6">
            	<h3><cms:contentText key="TITLE" code="claims.product.approval"/></h3>
        	</div>
			<div class="span12">
	           <ul class="export-tools fr">
	                <li><a href="#" class="pageView_print btn btn-small"><cms:contentText code="system.button" key="PRINT" /> <i class="icon-printer"></i></a></li>
	            </ul>
	        </div>
	    </div>

	    <div class="row-fluid">
	    <div class="span12">
	    <table>
	    <tr>
	    <td><h3><c:out value="${claimsSubmitted}"/></h3></td>
	    <td><h3><c:out value="${claimsPending}"/></h3></td>
	    <td><h3><c:out value="${productsSubmitted}"/></h3></td>
	    <td><h3><c:out value="${productsApproved}"/></h3></td>
	    <td><h3><c:out value="${productsDenied}"/></h3></td>
	    <td><h3><c:out value="${productsPending}"/></h3></td>
	    </tr>
	    <tr>
	    <td>
	    <cms:contentText key="CLAIMS_SUBMITTED" code="claims.product.approval"/>
	    <cms:contentText key="CLAIMS_PENDING" code="claims.product.approval"/>
	    <cms:contentText key="PRODUCTS_SUBMITTED" code="claims.product.approval"/>
	    <cms:contentText key="PRODUCTS_APPROVED" code="claims.product.approval"/>
	    <cms:contentText key="PRODUCTS_DENIED" code="claims.product.approval"/>
	    <cms:contentText key="PRODUCTS_PENDING" code="claims.product.approval"/>
	    </td>
	    </tr>
	    </table>
	    </div>
	    </div>

	   	<div class="row-fluid">
	   		<div class="span12">
				<html:form styleId="approvalsSearchForm" action="approvalsClaimsListMaintain.do?method=prepareUpdate" styleClass="form-horizontal">
					<div class="control-group">
						<label class="control-label">
							<cms:contentText code="recognition.approval.list" key="SUBMITTED_BETWEEN"/>
						</label>

					  <div class="controls">
                        <div class="input-append input-append-inside datepickerTrigger showTodayBtn"
			                    data-date-format="<%=UserManager.getUserDatePattern().toLowerCase()%>"
			                    data-date-language="<%=UserManager.getUserLocale()%>"
			                    data-date-startdate=""
			                    data-date-todaydate=""
			                    data-date-autoclose="true">
			                    <html:text property="startDate" styleId="dateStart" styleClass="input-medium" readonly="true" />
			                    <button class="btn awardDateIcon">
			                        <i class="icon-calendar"></i>
			                    </button>
			            </div>

						<cms:contentText key="AND" code="nomination.approval.list"/>

                        <div class="input-append input-append-inside datepickerTrigger showTodayBtn"
			                    data-date-format="<%=UserManager.getUserDatePattern().toLowerCase()%>"
			                    data-date-language="<%=UserManager.getUserLocale()%>"
			                    data-date-startdate=""
			                    data-date-todaydate=""
			                    data-date-autoclose="true">
			                    <html:text property="endDate" styleId="dateEnd"  styleClass="input-medium" readonly="true"/>
			                    <button class="btn awardDateIcon">
			                        <i class="icon-calendar"></i>
			                    </button>
			            </div>
			          </div>
					</div>

					<div class="control-group statusGroup">
						<label class="control-label">
							<cms:contentText key="STATUS" code="nomination.approval.list"/>
						</label>

						<div class="controls">
							<html:select property="open">
                      			<html:option value="true"><cms:contentText key="OPEN" code="system.general"/></html:option>
                      			<html:option value="false"><cms:contentText key="CLOSED" code="system.general"/></html:option>
                    		</html:select>
						</div>
					</div>

					<div class="control-group promotionGroup">
						<label class="control-label">
							<cms:contentText key="PROMOTION" code="nomination.approval.list"/>
						</label>

						<div class="controls">
							<html:select styleId="promotionSelected" property="promotionId">
								<html:options collection="promotionList" property="id" labelProperty="name" />
							</html:select>
						</div>
					</div>

					<div class="control-group">
						<label class="control-label">
							&nbsp;
						</label>

						<div class="controls">
							<input type="submit" name="showClaims" value="<cms:contentText key="SHOW_ACTIVITY" code="nomination.approval.list"/>" class="btn btn-primary showActivityBtn"/>
							<c:set var="totalPages" value="${approvalsClaimsListForm.listPageInfo.totalPages}"/>
							<c:set var="currentPage" value="${approvalsClaimsListForm.listPageInfo.currentPage}"/>
							<input type="hidden" id="currentPage" name="approvalsClaimsListForm.listPageInfo.currentPage" value="${currentPage}">
							<html:hidden property="requestedPage" styleId="requestedPage" value="1"/>
							<html:hidden property="listPageInfo.resultsPerPage"/>
						</div>
					</div>
				</html:form>

				<div id="nominationsTableWrapper" class="nominationsTableWrapper">
				<div class="row-fluid">
                    <ul class="export-tools approvalsExportIconsWrapper pushDown">
                        <li class="export csv">
                            <a href="<%=RequestUtils.getBaseURI(request)%>/claim/approvalsClaimsListMaintain.do?method=extractAsCsv">
                                <span class="btn btn-inverse btn-compact btn-export-csv">
                                    <cms:contentText key="CSV" code="system.general" /> <i class="icon-download-2"></i>
                                </span>
                            </a>
                        </li>
                    </ul>
				</div>

				  <html:form styleId="approvalsCalcGiverForm" action="approvalsClaimsListUpdate.do?method=saveApprovals">
					<div class="row-fluid">
					<div class="span12">
			      <c:forEach items="${promotionClaimsValueList}" var="promotionClaimsValue" varStatus="promotionClaimsValueStatus">
				  <display:table name="${promotionClaimsValue.approvables}" id="claim" class="table table-striped crud-table"
						   pagesize="10" sort="list" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>" >
				    <display:setProperty name="basic.msg.empty_list_row">
		             <tr class="crud-content" align="left"><td colspan="{0}">
				      <cms:contentText key="NOTHING_FOUND" code="system.errors"/>
				      </td>
				     </tr>
	                </display:setProperty>

	                <display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>

	                <display:column titleKey="claims.product.approval.CLAIM_NUMBER" sortProperty="claimNumber"  class="sortable discussionCountColumn unsorted" sortable="true">
	                                  <%	Map parameterMap = new HashMap();
											Claim temp = (Claim)pageContext.getAttribute("claim");
											parameterMap.put( "claimId", temp.getId() );
											pageContext.setAttribute("approveClaimUrl", ClientStateUtils.generateEncodedLink( "", "approvalsClaimDetailsMaintain.do?method=prepareUpdate", parameterMap ) );
									  %>
                        			  <a href="<c:out value="${approveClaimUrl}"/>">
                          				<c:out value="${claim.claimNumber}" />
                        			  </a>
		            </display:column>

		            <display:column titleKey="claims.product.approval.DATE" sortProperty="date"  class="sortable discussionCountColumn unsorted" sortable="true">
                    <fmt:formatDate pattern="${JstlDatePattern}" value="${claim.auditCreateInfo.dateCreated}" />
		            </display:column>

		            <display:column titleKey="claims.product.approval.SUBMITTER" sortProperty="submitter"  class="sortable discussionCountColumn unsorted" sortable="true">
                    <c:out value="${claim.submitter.nameLFMWithComma}" />
		            </display:column>

		            <display:column titleKey="claims.product.approval.APPROVER" sortProperty="approver"  class="sortable discussionCountColumn unsorted" sortable="true">
                    <c:out value="${claim.claimNumber}" />
		            </display:column>

		            <display:column titleKey="claims.product.approval.ITEM" sortProperty="item"  class="sortable discussionCountColumn unsorted" sortable="true">
		            <c:forEach items="${claim.claimProducts}" var="claimProduct" varStatus="claimProductStatus">
                    <c:if test="${formApprovalStatusTypeCode != 'approv' || !approvalsClaimsListForm.open}">
                    <c:out value="${claimProduct.product.name}" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <br>
                    </c:if>
		            </c:forEach>
		            </display:column>

		            <display:column titleKey="claims.product.approval.STATUS" sortProperty="status"  class="sortable discussionCountColumn unsorted" sortable="true">
		            <c:forEach items="${claim.claimProducts}" var="claimProduct" varStatus="claimProductStatus">
					<c:set var="claimProductIdString">
                    <c:out value="${claimProduct.id}" />
                    </c:set>
                    <c:set var="formApprovalStatusTypeCode" value="${approvalsClaimsListForm.claimProductApprovalFormByClaimProductIdString[claimProductIdString].approvalStatusType}" />
                    <c:set var="showableClaimProductIndex" value="${showableClaimProductIndex + 1}" />
                    <c:choose>
                    <c:when test="${formApprovalStatusTypeCode == 'approv'}">
                    <c:out value="${claimProduct.approvalStatusType.name}" />
                    <br>
                    </c:when>
                    <c:otherwise>
                    <html:select styleId="${applyAllClassClaimAndClaimProductId}-status"
                    property="claimProductApprovalFormByClaimProductIdString(${claimProduct.id}).approvalStatusType">
                    <c:if test="${formApprovalStatusTypeCode == 'pend' || formApprovalStatusTypeCode == 'hold'}">
                    <c:set var="approvalOptionTypes" value="${promotionClaimsValue.promotion.approvalOptionTypes}" />
                    <html:options collection="approvalOptionTypes" property="code" labelProperty="name" />
                    </c:if>
                    <c:if test="${formApprovalStatusTypeCode == 'deny'}">
                    <html:options collection="approvalStatusWhenCurrentlyDeniedList" property="code" labelProperty="name" />
                    </c:if>
                    </html:select>

                    <c:set var="deniedReasonTypes" value="${promotionClaimsValue.promotion.deniedReasonCodeTypes}" />
                    <html:select styleId="deny-${applyAllClassClaimAndClaimProductId}-reason"
                    property="claimProductApprovalFormByClaimProductIdString(${claimProduct.id}).denyPromotionApprovalOptionReasonType"
                    disabled="${ formApprovalStatusTypeCode == 'pend' || formApprovalStatusTypeCode == 'approv' || formApprovalStatusTypeCode == 'hold' }">
                    <html:option value="">
                    <cms:contentText code="claims.product.approval" key="SELECT_RC" />
                    </html:option>
                    <html:options collection="deniedReasonTypes" property="code" labelProperty="name" />
                    </html:select>

                    <c:set var="heldReasonTypes" value="${promotionClaimsValue.promotion.heldReasonCodeTypes}" />
                    <html:select styleId="onHoldReasonSelect"
                    property="claimProductApprovalFormByClaimProductIdString(${claimProduct.id}).holdPromotionApprovalOptionReasonType"
                    disabled="${ formApprovalStatusTypeCode == 'pend' || formApprovalStatusTypeCode == 'approv' || formApprovalStatusTypeCode == 'deny' }">
                    <html:option value="">
                    <cms:contentText code="claims.product.approval" key="SELECT_RC" />
                    </html:option>
                    <html:options collection="heldReasonTypes" property="code" labelProperty="name" />
                    </html:select>

                    </c:otherwise>
                    </c:choose>
		            </c:forEach>
		            </display:column>
				    </display:table>
				    </c:forEach>
				    <beacon:authorize ifNotGranted="LOGIN_AS">
				      <html:submit styleClass="btn btn-primary btn-fullmobile"><cms:contentText code="system.button" key="SAVE"/></html:submit>
				    </beacon:authorize>
					<a class="btn btn-fullmobile" href="<%=RequestUtils.getBaseURI(request)%>/claim/approvalsListPage.do"><cms:contentText code="system.button" key="CANCEL" /></a>

					</div>
					</div>
			  	  </html:form>
			</div>
		</div>
	</div>

	<form id="dataForm">

	<c:forEach var="hiddenAttributeSelectList" items="${hiddenAttributeSelectList}" varStatus="status">
                    <input type="hidden" name="claimProductApprovalFormByClaimProductIdString(${hiddenAttributeSelectList.claimProductIdString}).approvalStatusType" value="${hiddenAttributeSelectList.approvalStatusType}">
                    <input type="hidden" name="claimProductApprovalFormByClaimProductIdString(${hiddenAttributeSelectList.claimProductIdString}).denyPromotionApprovalOptionReasonType" value="${hiddenAttributeSelectList.denyPromotionApprovalOptionReasonType}">
    </c:forEach>

	</form>

</div>
</div>

<div id="sameForAllTipTpl" class="sameForAllTip" style="display:none">
	<a href="#"><cms:contentText code="recognition.approval.list" key="SAME_FOR_ALL" /></a>
</div>

<input type="hidden" id="serverReturnedErrored" value="${serverReturnedError}">

<script>
	$(document).ready(function() {

		//attach the view to an existing DOM element
		var asmlp = new ApprovalsSearchModelView({
			el:$('#approvalsPagePendingNominations'),
	        pageNav : {
	            back : {
	                text : '<cms:contentText key="BACK" code="system.button" />',
	                url : 'approvalsListPage.do'
	            },
	            home : {
	                text : '<cms:contentText key="HOME" code="system.general" />',
	                url : '${pageContext.request.contextPath}/homePage.do'
	            }
	        },
	        pageTitle : '<cms:contentText code="promotion.approvals" key="TITLE_NEW" />'
		});

	});
</script>
