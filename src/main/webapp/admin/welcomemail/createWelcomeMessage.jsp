<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="java.util.*"%>
<%@ page import="com.biperf.core.ui.promotion.PromotionAudienceFormBean"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>
<%@ include file="/include/taglib.jspf"%>

<script type="text/javascript"
	src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar.js"></script>
<script type="text/javascript"
	src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-en.js"></script>
<script type="text/javascript"
	src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-setup.js"></script>


<script type="text/javascript">
	 function previewMessage(obj)
    {
      var returnValue = false;

      var selectObj = findElement(getContentForm(), obj);
      if (selectObj != null)
      {
        popUpWin('<%= request.getContextPath() %>/admin/displayPreviewMessage.do?doNotSaveToken=true&messageId=' + selectObj.value, 'console', 750, 500, false, true);
      }

      return returnValue;
    }

   // Displays the Message Library message that corresponds to the item selected
   // from the specified HTML select element.
  function previewMessageFromSelectElement(obj)
  {
    var returnValue = false;

    var selectObj = findElement(getContentForm(),obj);
    if ((selectObj != null) && (selectObj.options != null))
    {
      var selectedOption = null;
      for (var i = 0; i < selectObj.options.length; i++) {
        if (selectObj.options[i].selected) {
          selectedOption = selectObj.options[i].value
          break;
        }
      }
      
      if ( selectedOption == null || selectedOption <= 0 ){
      alert("There is nothing to preview for this option.");
      return false;
    }

      if ((selectedOption != null) && (selectedOption != ""))
      {
        popUpWin('<%=request.getContextPath()%>/admin/displayPreviewMessage.do?doNotSaveToken=true&messageId='+selectedOption, 'console', 750, 500, false, true);
        returnValue = true;
      }
    }

    return returnValue;
  }  
      
</script>
<html:form styleId="contentForm" action="welcomeMessageDisplay">
	<html:hidden property="method" />

	<html:hidden property="audienceListCount" />
	<table style="width:auto">
		<tr>
			<td><span class="headline"> <cms:contentText
				key="ADD_WELCOME_MESSAGE" code="admin.welcomemessage" /> </span> <%--INSTRUCTIONS--%>

			<br />
			<br />
			<span class="content-instruction"> <cms:contentText
				key="ADD_INSTRUCTION" code="admin.welcomemessage" /> </span></td>
		</tr>
		<tr>
			<td><cms:errors /></td>
		</tr>
		<%-- Start Message To Specific Audience --%>
		<tr>
			<td>
			<table style="width:auto;">
				<tr class="form-blank-row">
					<td></td>
				</tr>
				<tr>
					<td></td>
					<td>
					<table>
						<tr>
							<td>
							<table class="crud-table" width="100%">
								<tr>
									<th colspan="3" class="crud-table-header-row">* <cms:contentText
										key="AUDIENCE_LIST_LABEL" code="promotion.audience" />
									 <html:select
										property="selectedAudienceId" styleClass="content-field">
										<html:option value=''>
											<cms:contentText key="SELECT_SAVED_AUDIENCE"
												code="admin.send.message" />
										</html:option>
										<html:options collection="availableAudiences" property="id"
											labelProperty="name" />
									</html:select> <html:submit styleClass="content-buttonstyle"
										onclick="javascript:setActionAndDispatch('welcomeMessageDisplay.do', 'addAudience')">
										<cms:contentText code="system.button" key="ADD" />
									</html:submit> <br>
									<cms:contentText key="CREATE_AUDIENCE_LABEL"
										code="promotion.audience" /> <a
										href="javascript:setActionDispatchAndSubmit('welcomeMessageDisplay.do', 'prepareAudienceLookup');"
										class="crud-content-link"> <cms:contentText
										key="LIST_BUILDER_LABEL" code="promotion.audience" /> </a></th>
									<th valign="top" class="crud-table-header-row"><cms:contentText
										code="promotion.audience" key="CRUD_REMOVE_LABEL" /></th>
								</tr>
								<c:set var="rowCount" value="0" />
								<c:set var="switchColor" value="false" />
								<nested:iterate id="audience" name="welcomeMessageForm"
									property="audienceList">
									<nested:hidden property="id" />
									<nested:hidden property="audienceId" />
									<nested:hidden property="name" />
									<nested:hidden property="size" />
									<nested:hidden property="audienceType" />
									<c:choose>
										<c:when test="${switchColor == 'false'}">
											<tr class="crud-table-row1">
												<c:set var="switchColor" scope="page" value="true" />
										</c:when>
										<c:otherwise>
											<tr class="crud-table-row2">
												<c:set var="switchColor" scope="page" value="false" />
										</c:otherwise>
									</c:choose>
									<%-- in general we use content-field-review for no editables, but for this screen class is content-field--%>
									<td class="content-field"><c:out value="${audience.name}" />
									</td>
									<td class="content-field">< <c:out
										value="${audience.size}" /> ></td>
									<td class="content-field">
									<%
											Map parameterMap = new HashMap();
											PromotionAudienceFormBean temp = (PromotionAudienceFormBean) pageContext
											.getAttribute("audience");
											parameterMap.put("audienceId", temp.getAudienceId());
											pageContext
											.setAttribute(
													"previewUrl",
													ClientStateUtils
													.generateEncodedLink(
													RequestUtils
															.getBaseURI(request),
													"/promotion/promotionAudience.do?method=displayPaxListPopup",
													parameterMap, true));
									%> <a
										href="javascript:popUpWin('<c:out value="${previewUrl}"/>', 'console', 750, 500, false, true);"
										class="crud-content-link"><cms:contentText key="VIEW_LIST"
										code="promotion.audience" /></a></td>
									<td align="center" class="content-field"><nested:checkbox
										property="removed" /></td>

									<c:set var="rowCount" value="${rowCount+1}" />
								</nested:iterate>
								<c:if test="${rowCount==0}">
									<tr class="crud-table-row1">
										<td class="content-field"><cms:contentText
											key="NONE_DEFINED" code="system.general" /></td>
									</tr>
								</c:if>
							</table>
							</td>
						</tr>
						<c:if test="${rowCount>0}">
							<tr>
								<td align="right"><html:submit
									styleClass="content-buttonstyle"
									onclick="javascript:setActionAndDispatch('welcomeMessageDisplay.do','removeAudience');">
									<cms:contentText key="REMOVE" code="system.button" />
								</html:submit></td>
							</tr>
						</c:if>
					</table>
					</td>
				</tr>
			</table>
			</td>
		</tr>
		<%-- End Message To Specific Audience --%>
        <table>
		  <tr>
		    <td class="content-field">
              *
              <cms:contentText key="NOTIFICATION_DATE" code="admin.welcomemessage" />
            </td>
		    <td class="content-field">
			  <html:text property="notificationDate"
				         styleId="notificationDate" size="20" maxlength="19"
				         styleClass="content-field" readonly="true" onfocus="clearDateMask(this);" /> <img
				         id="notificationDateTrigger"  
				         src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif"
			  class="calendar-icon"
			  alt="<cms:contentText key="NOTIFICATION_DATE" code="admin.welcomemessage"/>" />
		    </td>

		  </tr>
		  <tr>
		    <td class="content-field">
              *
		      <cms:contentText key="MESSAGE" code="admin.welcomemessage" />
            </td>
		    <td class="content-field">
              <c:choose>
		        <c:when test="${messageListSize == 0}">
                  <cms:contentText key="MESSAGE_NOT_DEFINED" code="admin.fileload.errors" />
		        </c:when>
    	        <c:when test="${messageListSize >0}">
                  <html:select property="messageId">
    		        <html:option value="">
    			      <cms:contentText key="SELECT_ONE" code="admin.fileload.common" />
    		        </html:option>
    		        <html:options collection="messageList" property="id" labelProperty="name" />
    		      </html:select>
                  <a href="javascript:void(0)"
                     onclick="previewMessageFromSelectElement('messageId'); return false;">
                    <cms:contentText code="admin.fileload.importFileDetails" key="PREVIEW" />
                  </a>
    	        </c:when>
		      </c:choose>
		    </td>
		  </tr>
		  <tr>
		    <td class="content-field">
            </td>
		    <td class="content-field">
              <c:choose>
		        <c:when test="${messageListSize == 0}">
                  <cms:contentText key="MESSAGE_NOT_DEFINED" code="admin.fileload.errors" />
		        </c:when>
		      </c:choose>
		    </td>
		  </tr>
        </table>

		<tr class="form-buttonrow">


			<td align="left">
			
			<beacon:authorize ifNotGranted="LOGIN_AS">
				<html:submit styleClass="content-buttonstyle"
					onclick="javascript:setActionAndDispatch('welcomeMessageDisplay.do', 'createMessage')">
					<cms:contentText code="system.button" key="SAVE" />
				</html:submit>
			</beacon:authorize> <html:cancel styleClass="content-buttonstyle"
				onclick="javascript:setActionAndDispatch('welcomeMessageDisplay.do', 'cancel')">
				<cms:contentText code="system.button" key="CANCEL" />
			</html:cancel></td>


		</tr>
	</table>


	<script type="text/javascript">

 
  Calendar.setup(
    {
      inputField  : "notificationDate",         // ID of the input field
      ifFormat    : "${TinyMceDatePattern}",    // the date format
      showsTime   : "true",
      timeFormat  : "12",
      button      : "notificationDateTrigger"       // ID of the button
    }
  );

</script>

</html:form>
