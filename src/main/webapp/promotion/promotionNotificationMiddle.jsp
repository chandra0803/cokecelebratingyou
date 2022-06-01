<%@ include file="/include/taglib.jspf"%>

<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.ui.promotion.PromotionNotificationFormBean"%>
<%@ page import="com.biperf.core.utils.MessageUtils"%>
<%@ page import="java.util.Map"%>

<c:choose>
  <c:when test="${empty promotionNotificationForm.claimFormStepList }">
    <tr class="form-row-spacer">
      <td colspan="4" class="content-field-label"><cms:contentText code="promotion.notification" key="NO_STEPS" /></td>
    </tr>
    <tr class="form-blank-row">
      <td></td>
    </tr>
  </c:when>
  <c:otherwise>
    <nested:iterate id="claimFormStepValue" name="promotionNotificationForm" property="claimFormStepList">
      <nested:hidden property="claimFormStepId" />
      <nested:hidden property="claimFormNotificationListCount" />
      <nested:hidden property="cmAssetCode" />
      <nested:hidden property="cmName" />
      <tr class="form-row-spacer">
        <td class="content-bold" colspan="4"><cms:contentText code="${claimFormStepValue.cmAssetCode}"
          key="${claimFormStepValue.cmName}" /></td>
      </tr>

      <nested:iterate id="claimFormNotificationValue" property="claimFormNotificationList" indexId="count">
        <nested:hidden property="promotionNotificationId" />
        <nested:hidden property="claimFormStepEmailId" />
        <nested:hidden property="version" />
        <nested:hidden property="promotionNotificationType" />
        <nested:hidden property="notificationType" />
        <nested:hidden property="notificationTypeName" />
        <nested:hidden property="createdBy" />
        <nested:hidden property="dateCreated" />

        <tr class="form-row-spacer">
          <td>*</td>
          <td class="content-field-label"><c:out value="${claimFormNotificationValue.notificationTypeName}" />&nbsp;</td>
	      <%
	         PromotionNotificationFormBean notificationValueParam = (PromotionNotificationFormBean) pageContext.getAttribute("claimFormNotificationValue");
	         Map notificationMessageMapParam = (Map)request.getAttribute("notificationMessageMap");
			 pageContext.setAttribute("notificationMessageTypeList", notificationMessageMapParam.get(MessageUtils.getMessageTypeCode(notificationValueParam.getNotificationType())));
	      %>
          <td class="content-field" colspan="3">
          	<c:set var="disableInactivityField" value="${ disableField|| promotionNotificationForm.hasParent}" />
          	<%
		    			boolean overallDisableField = ((Boolean)pageContext.getAttribute("disableInactivityField")).booleanValue();
            %>
          	<nested:select property="notificationMessageId" styleId="claimFormNotificationMessageId[${count}]" disabled="<%=overallDisableField%>" styleClass="content-field content-field-notification-email killme">
            	<html:options collection="notificationMessageTypeList" property="id" labelProperty="name" />
          	</nested:select> 
          </td>
          <td>	
          	&nbsp;&nbsp; 
          	<a class="content-link" href="#" onclick="popupPreviewMessagePage('<nested:writeNesting property="notificationMessageId" />');return false;">
          		<cms:contentText code="promotion.notification" key="PREVIEW" />
          	</a>
          </td>
        </tr>
      </nested:iterate>
      <tr class="form-blank-row">
        <td></td>
      </tr>
    </nested:iterate>
  </c:otherwise>
</c:choose>
