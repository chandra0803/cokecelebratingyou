<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ include file="/include/taglib.jspf"%>

<c:if test="${recognitionState.promotionId != null && recognitionState.promotionId != 0}">
  <input type="hidden" name="promotionId" value="${recognitionState.promotionId}" />
  <input type="hidden" name="promotionType" value="${recognitionState.promotionType}" />
</c:if>

<c:if test="${recognitionState.nodeId != null && recognitionState.nodeId != 0}">
  <input type="hidden" name="nodeId" value="${recognitionState.nodeId}" />
</c:if>

<!-- is this form 'launched as' mode? -->
<beacon:authorize ifNotGranted="LOGIN_AS">
	<input type="hidden" name="isLaunchedAs" value="false" />
</beacon:authorize>
<beacon:authorize ifAnyGranted="LOGIN_AS">
	<input type="hidden" name="isLaunchedAs" value="true" />
</beacon:authorize>


<c:if test="${recognitionState.recipientNodeId != null && recognitionState.recipientNodeId != ''}">
  <input type="hidden" name="recipientNodeId" value="${recognitionState.recipientNodeId}" />
</c:if>

<c:if test="${recognitionState.recipientId != null && recognitionState.recipientId != 0}">
  <input type="hidden" name="recipientId" value="${recognitionState.recipientId}" />
</c:if>

<c:if test="${not empty recognitionState.teamName}">
  <input type="hidden" name="teamName" value="<c:out value='${recognitionState.teamName}' />" />
</c:if>

<!-- is the preselected list of contributors editable? -->
<input type="hidden" name="preselectedLocked" value="${recognitionState.preselectedContributorsLocked}"/>

<input type="hidden" name="claimRecipientFormBeansCount" value="${recognitionState.claimRecipientFormBeansCount}" />

<c:if test="${not empty recognitionState.recipients}">
  <c:forEach var="recipient" items="${recognitionState.recipients}" varStatus="status">
    <input type="hidden" name="claimRecipientFormBeans[${status.index}].id" value="${recipient.userId}" />
    <c:if test="${recipient.awardQuantity > 0}">
      <input type="hidden" name="claimRecipientFormBeans[${status.index}].awardQuantity" value="${recipient.awardQuantity}" />
    </c:if>
      <input type="hidden" name="claimRecipientFormBeans[${status.index}].isOptOut" value="${recipient.optOutAwards}" />
 	<%-- client customization for wip #26532 starts --%>
    <input type="hidden" id="purlAllowOutsideDomains" name="claimRecipientFormBeans[${status.index}].purlAllowOutsideDomains" value="${recipient.purlAllowOutsideDomains}" />
    <%-- client customization for wip #26532 ends --%> 
    
    <%-- Client customization for wip #42701 starts --%>
    <input type="hidden" id="currency" name="claimRecipientFormBeans[${status.index}].currency" value="${recipient.currency}" />
    <input type="hidden" id="awardMax" name="claimRecipientFormBeans[${status.index}].awardMax" value="${recipient.awardMax}" />
    <input type="hidden" id="awardMin" name="claimRecipientFormBeans[${status.index}].awardMin" value="${recipient.awardMin}" />
    <%-- Client customization for wip #42701 ends --%>
    <input type="hidden" name="claimRecipientFormBeans[${status.index}].awardLevel" value="${recipient.awardLevel}" />
    <c:if test="${recipient.awardLevelId > 0}">
      <input type="hidden" name="claimRecipientFormBeans[${status.index}].awardLevelId" value="${recipient.awardLevelId}" />
    </c:if>
    <input type="hidden" name="claimRecipientFormBeans[${status.index}].awardLevelValue" value="${recipient.awardLevelValue}" />
    <input type="hidden" name="claimRecipientFormBeans[${status.index}].awardLevelName" value="${recipient.awardLevelName}" />
    <input type="hidden" name="claimRecipientFormBeans[${status.index}].countryCode" value="${recipient.countryCode}" />
    <input type="hidden" name="claimRecipientFormBeans[${status.index}].countryRatio" value="${recipient.countryRatio}" />
    <input type="hidden" name="claimRecipientFormBeans[${status.index}].countryName" value="${recipient.countryName}" />
    <input type="hidden" name="claimRecipientFormBeans[${status.index}].firstName" value="<c:out value='${recipient.firstName}' />" />
    <input type="hidden" name="claimRecipientFormBeans[${status.index}].lastName" value="<c:out value='${recipient.lastName}' />" />
    <input type="hidden" name="claimRecipientFormBeans[${status.index}].emailAddr" value="${recipient.emailAddr}" />
    <input type="hidden" name="claimRecipientFormBeans[${status.index}].nodeId" value="${recipient.nodeId}" />
    <input type="hidden" name="claimRecipientFormBeans[${status.index}].nodeName" value="${recipient.nodeName}" />
    <input type="hidden" name="claimRecipientFormBeans[${status.index}].organization" value="${recipient.nodeName}" />
    <input type="hidden" name="claimRecipientFormBeans[${status.index}].departmentName" value="${recipient.departmentName}" />
    <input type="hidden" name="claimRecipientFormBeans[${status.index}].jobName" value="${recipient.jobName}" />
	<input type="hidden" name="claimRecipientFormBeans[${status.index}].optOutAwards" value="${recipient.optOutAwards}" />
	<input type="hidden" name="claimRecipientFormBeans[${status.index}].avatarUrl" value="${recipient.avatarUrl}" />    
    <textarea name="claimRecipientFormBeans[${status.index}].nodes" style="display:none;">
      ${recipient.nodes}
    </textarea>
    
    <c:if test="${not empty recipient.calculatorResults}">
      <c:forEach var="calcResult" items="${recipient.calculatorResults}" varStatus="calcStatus">
        <input type="hidden" name="claimRecipientFormBeans[${status.index}].calculatorResultBeans[${calcStatus.index}].criteriaId" value="${calcResult.criteriaId}">
        <input type="hidden" name="claimRecipientFormBeans[${status.index}].calculatorResultBeans[${calcStatus.index}].ratingId" value="${calcResult.ratingId}">
        <input type="hidden" name="claimRecipientFormBeans[${status.index}].calculatorResultBeans[${calcStatus.index}].criteriaRating" value="${calcResult.criteriaRating}">
      </c:forEach>
    </c:if>
  </c:forEach>
  <textarea name="contributorTeamsSearchFilters" style="display:none;">
      ${recognitionState.contributorTeamsSearchFilters}
    </textarea>
</c:if>

<c:if test="${not empty recognitionState.selectedBehavior}">
  <input type="hidden" name="selectedBehavior" value="<c:out value='${recognitionState.selectedBehavior}' />" />
</c:if>

<input type="hidden" name="cardType" value="${recognitionState.cardType}" />
<input type="hidden" name="videoUrl" value="${recognitionState.videoUrl}" />
<input type="hidden" name="cardId" value="${recognitionState.cardId}" />
<input type="hidden" name="cardCanEdit" value="${recognitionState.cardCanEdit}" />
<input type="hidden" name="cardUrl" value="${recognitionState.cardUrl}" />
<input type="hidden" name="drawingData" value="${recognitionState.drawingData}" />

<input type="hidden" name="comments" value="${recognitionState.comments}" />


<input type="hidden" name="sendCopyToManager" value="${recognitionState.sendCopyToManagerForDataForm}" />
<input type="hidden" name="sendCopyToMe" value="${recognitionState.sendCopyToMeForDataForm}" />
<input type="hidden" name="sendCopyToOthers" value="<c:out value='${recognitionState.sendCopyToOthers}' />" />
<input type="hidden" name="makeRecPrivate" value="${recognitionState.makeRecPrivate}" />

<input type="hidden" name="claimElementsCount" value="${recognitionState.claimElementsCount}" />
<c:if test="${not empty recognitionState.claimElementsList}">
  <c:forEach var="claimElementForm" items="${recognitionState.claimElementsList}" varStatus="claimElementStatus">
      <input type="hidden" name="claimElement[${claimElementStatus.index}].claimFormStepElementId" value="${claimElementForm.claimFormStepElementId}" />
      <input type="hidden" name="claimElement[${claimElementStatus.index}].claimElementId" value="${claimElementForm.claimElementId}" />
      <input type="hidden" name="claimElement[${claimElementStatus.index}].claimElementVersion" value="${claimElementForm.claimElementVersion}" />
      <input type="hidden" name="claimElement[${claimElementStatus.index}].claimElementDateCreated" value="${claimElementForm.claimElementDateCreated}" />
      <input type="hidden" name="claimElement[${claimElementStatus.index}].claimElementCreatedBy" value="${claimElementForm.claimElementCreatedBy}" />
      <input type="hidden" name="claimElement[${claimElementStatus.index}].value" value="${claimElementForm.value}" />
  </c:forEach>
</c:if>

<input type="hidden" name="recipientSendDate" value="<c:out value='${recognitionState.recipientSendDate}' />" />
<input type="hidden" name="anniversaryYears" value="${recognitionState.anniversaryYears}" />
<input type="hidden" name="anniversaryDays" value="${recognitionState.anniversaryDays}" />

<input type="hidden" name="claimContributorFormBeansCount" value="${recognitionState.claimContributorFormBeansCount}" />

<c:if test="${not empty recognitionState.contributors}">
  <c:forEach var="contributor" items="${recognitionState.contributors}" varStatus="status">
	 <c:if test="${not empty contributor.id}">
        <input type="hidden" name="claimContributorFormBeans[${status.index}].id" value="${contributor.id}" /> 
     </c:if>
    
    <input type="hidden" name="claimContributorFormBeans[${status.index}].firstName" value="<c:out value='${contributor.firstName}' />" />
    <input type="hidden" name="claimContributorFormBeans[${status.index}].lastName" value="<c:out value='${contributor.lastName}' />" />
    <input type="hidden" name="claimContributorFormBeans[${status.index}].contribType" value="${contributor.contribType}" />

    <c:choose>
      <c:when test="${contributor.contribType == 'other'}">
        <input type="hidden" name="claimContributorFormBeans[${status.index}].email" value="${contributor.email}" />
        <input type="hidden" name="claimContributorFormBeans[${status.index}].invitationSentDate" value="${contributor.invitationSentDate}" />
        <input type="hidden" name="claimContributorFormBeans[${status.index}].sourceType" value="${contributor.sourceType}" />
      </c:when>
      <c:when test="${contributor.contribType == 'preselected' || contributor.contribType == 'additional'}">
        <input type="hidden" name="claimContributorFormBeans[${status.index}].countryCode" value="${contributor.countryCode}" />
        <input type="hidden" name="claimContributorFormBeans[${status.index}].countryName" value="${contributor.countryName}" />
        <input type="hidden" name="claimContributorFormBeans[${status.index}].orgName" value="${contributor.orgName}" />
        <input type="hidden" name="claimContributorFormBeans[${status.index}].departmentName" value="${contributor.departmentName}" />
        <input type="hidden" name="claimContributorFormBeans[${status.index}].jobName" value="${contributor.jobName}" />
        <input type="hidden" name="claimContributorFormBeans[${status.index}].sourceType" value="${contributor.sourceType}" />
        <c:if test="${contributor.contribType == 'preselected' and not empty contributor.email}">
        	<input type="hidden" name="claimContributorFormBeans[${status.index}].email" value="${contributor.email}" />
        </c:if>
        <input type="hidden" name="claimContributorFormBeans[${status.index}].invitationSentDate" value="${contributor.invitationSentDate}" />
      </c:when>
    </c:choose>
  </c:forEach>
</c:if>

<!-- Client Customizations For WIP #39189 starts -->
<input type="hidden" name="claimUploadFormBeansCount" value="${recognitionState.claimUploadFormBeansCount}" />

<c:if test="${not empty recognitionState.claimUploads}">
  <c:forEach items="${recognitionState.claimUploads}" var="claimUpload" varStatus="status">
    <input type="hidden" name="claimUploadFormBeans[${status.index}].url" value="${claimUpload.url}" />
    <input type="hidden" name="claimUploadFormBeans[${status.index}].description" value="${claimUpload.description}" />
  </c:forEach>
</c:if>
<!-- Client Customizations For WIP #39189 ends -->

<cms:errors/>

<c:if test="${not empty recognitionState.errors}">
  <div class="error">
    <h4><cms:contentText key="FOLLOWING_ERRORS" code="system.generalerror" /></h4>
    <ul>
      <c:forEach var="error" items="${recognitionState.errors}">
        <li><cms:contentText code="${error.code}" key="${error.key}"/></li>
      </c:forEach>
    </ul>
  </div>
</c:if>
