<%@ include file="/include/taglib.jspf" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils" %>
<%@ page import="com.biperf.core.domain.enums.ApprovalType" %>

<c:forEach var="customApproverValueBean1" items="${promotionApprovalForm.customApproverValueBeanListAsList}" >		
<c:choose>		
		<c:when test="${not empty customApproverValueBean1.nomApproverList}">
			<div id='modal1<c:out value="${customApproverValueBean1.approverOptionId}" />' class="modal" >
				<div class="content">
					<div class="column1">
						<div class="module">
							<div class="topper">
								<h2><cms:contentText code="promotion.approvals" key="APPROVERS_LIST" /></h2>
							</div>
							<div class="guts">								
								<table>
									<thead>
										<tr>
											<th><cms:contentText code="promotion.approvals" key="USER_NAME" /></th>
											<th><cms:contentText code="promotion.approvals" key="LAST_NAME" /></th>
											<th><cms:contentText code="promotion.approvals" key="FIRST_NAME" /></th>
											<th><cms:contentText code="promotion.approvals" key="APPROVER_TYPE" /></th>
											<th><cms:contentText code="promotion.approvals" key="APPROVER_VALUE" /></th>
										</tr>
									</thead>
									<tbody>
										<c:forEach var="nomApprovers" items="${customApproverValueBean1.nomApproverList}">
											<tr>
												<td>${nomApprovers.username}</td>
												<td>${nomApprovers.lastname}</td>
												<td>${nomApprovers.firstname}</td>
												<td>${nomApprovers.approverType}</td>
												<td>${nomApprovers.approverValue}</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</div>
							<!-- /.guts -->
						</div>
						<!-- /.module -->
					</div>
					<!-- /.column1 -->
				</div>
			</div>
			<!-- /#modal1.modal -->
		</c:when>
		<c:otherwise>
		</c:otherwise>		
</c:choose>		
</c:forEach>