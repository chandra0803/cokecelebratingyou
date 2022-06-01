<%@ include file="/include/taglib.jspf"%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

	<tr class="form-row-spacer" id="allowActivityUploadSection">
	    <td colspan="2">
			<div>
				*<cms:contentText key="SPREADSHEET_UPLOAD" code="promotion.ssi.activitysubmission" />
			</div>
		</td>
		<td class="content-field">
			<table>
				<tr>
					<td class="content-field" valign="top">
						<html:radio styleId="allowActivityUpload" property="allowActivityUpload" value="false" /> 
						<cms:contentText code="system.common.labels" key="NO" />
					</td>
				</tr>
				<tr>
					<td class="content-field" valign="top">
						<html:radio styleId="allowActivityUpload" property="allowActivityUpload" value="true" /> 
						<cms:contentText code="system.common.labels" key="YES" />
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<%-- Claim submission and approval has been commented out. This moved to SSI_Phase_2 --%>
	
	<tr class="form-row-spacer" id="allowClaimSubmissionSection">
	    <td colspan="2">
			<div>
				*<cms:contentText key="CLAIM_SUBMISSION" code="promotion.ssi.activitysubmission" />
			</div>
		</td>
		<td class="content-field">
			<table>
				<tr>
					<td class="content-field" valign="top">
						<html:radio styleId="allowClaimSubmission1" property="allowClaimSubmission" value="false" onclick="disableClaimApproval();" />
						<cms:contentText code="system.common.labels" key="NO" />
					</td>
				</tr>
				<tr>
					<td class="content-field" valign="top">
						<html:radio styleId="allowClaimSubmission2" property="allowClaimSubmission" value="true" onclick="enableClaimApproval();" /> 
						<cms:contentText code="system.common.labels" key="YES" />
					</td>
				</tr>
			</table>
		</td>
	</tr>
	
	<tr class="form-row-spacer" id="claimFormSection">
        <td colspan="2">
			<div>
				*<cms:contentText key="ACTIVITY_FORM" code="promotion.basics"/>
			</div>
		</td>
		<td class="content-field">
			<div >
	            <c:choose>
		           <c:when test="${promotionSSIActivitySubmissionForm.expired}">
		             <c:out value="${promotionSSIActivitySubmissionForm.activityFormName}" />
		             <html:hidden property="activityFormName" />
		           </c:when>
		           <c:otherwise>
				  <html:select property="claimFormId" styleClass="content-field" disabled="${promotionSSIActivitySubmissionForm.expired}" >
		               <html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>
					<html:options collection="activityFormList" property="id" labelProperty="name"  />
				  </html:select>
				</c:otherwise>
			  </c:choose>
		  </div>
       </td>
	</tr>	
	
   <tr class="form-row-spacer"  id="daysToApproveClaimSection">
		<td colspan="2">
			<div>
				*<cms:contentText code="promotion.ssi.activitysubmission" key="DAYS_TO_APPROVE_CLAIM" />
			</div>
		</td>
		<td class="content-field">
			<div >
	            <c:choose>
		           <c:when test="${promotionSSIActivitySubmissionForm.expired}">
		             <c:out value="${promotionSSIActivitySubmissionForm.daysToApproveClaim}" />
		             <html:hidden property="daysToApproveClaim" />
		           </c:when>
		           <c:otherwise>
		           <html:text styleId="daysToApproveClaimId"  property="daysToApproveClaim"></html:text>
				</c:otherwise>
			  </c:choose>
		  </div>
       </td>
	</tr>