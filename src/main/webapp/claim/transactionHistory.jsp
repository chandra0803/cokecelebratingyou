<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.ui.claim.TransactionHistoryForm"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ include file="/include/taglib.jspf"%>

<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-en.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-setup.js"></script>

<script type="text/javascript">
<!--
function maintainEmployer( method, action )
{
  document.transactionHistoryForm.method.value=method;
  document.transactionHistoryForm.action = action;
  document.transactionHistoryForm.submit();
}
//-->
</script>

<html:form styleId="contentForm" action="transactionHistory">
  <html:hidden property="method" />
  <html:hidden property="open" />
  <html:hidden property="mode" />
	<beacon:client-state>
	 	<beacon:client-state-entry name="userId" value="${transactionHistoryForm.userId}"/>
	 	<beacon:client-state-entry name="livePromotionId" value="${transactionHistoryForm.livePromotionId}"/>
	 	<beacon:client-state-entry name="livePromotionType" value="${transactionHistoryForm.livePromotionType}"/>
	 	<beacon:client-state-entry name="liveStartDate" value="${transactionHistoryForm.liveStartDate}"/>
	 	<beacon:client-state-entry name="liveEndDate" value="${transactionHistoryForm.liveEndDate}"/>
	 	<beacon:client-state-entry name="promotionId" value="${transactionHistoryForm.promotionId}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <table width="100%">
          <tr>
            <td valign="top">
              <span class="headline"><cms:contentText key="TITLE" code="participant.transactionhistory"/>
              </span>
              
              <br/>
              <span class="subheadline">
                <c:out value="${participant.titleType.name}" />
                <c:out value="${participant.firstName}" />
                <c:out value="${participant.middleName}" />
                <c:out value="${participant.lastName}" />
                <c:out value="${participant.suffixType.name}" />
              </span>
              <br/><br/>
              <span class="content-instruction">
                <cms:contentText key="INSTRUCTIONAL_COPY" code="participant.transactionhistory"/>
                <c:if test="${toShowRCopy!=null && toShowRCopy =='Yes'}">
			      	<cms:contentText key="REVERSE_COPY" code="participant.transactionhistory"/>
			    </c:if>
              </span>
              <br/><br/>

              <cms:errors/>
            </td>

            <td>
              <table class="criteria-table" border="0" cellpadding="2" cellspacing="0" align="right">
                <tr>
                  <td>
                    <span class="subheadline"><cms:contentText key="SHOW_TRANSACTIONS" code="participant.transactionhistory"/></span>
                    <table>
                      <tr>
                        <beacon:label property="startDate" required="true">
                          <cms:contentText key="START_DATE_LABEL" code="participant.transactionhistory"/>
                        </beacon:label>
                        <td class="content-field nowrap">
                          <html:text property="startDate" styleId="startDate" size="10" maxlength="10" styleClass="content-field" onfocus="clearDateMask(this);" readonly="true"/>
                          <img id="startDateTrigger" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" alt="<cms:contentText key='START_DATE_LABEL' code='participant.transactionhistory'/>"/>
                        </td>
                        <td>&nbsp;&nbsp;&nbsp;</td>
                        <td class="content-field-label-req">*</td>
                        <td class="content-field">
                          <cms:contentText key="END_DATE_LABEL" code="participant.transactionhistory"/>
                        </td>
                        <td class="content-field nowrap">
                          <html:text property="endDate" styleId="endDate" size="10" maxlength="10" styleClass="content-field" onfocus="clearDateMask(this);" readonly="true"/>
                          <img id="endDateTrigger" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" alt="<cms:contentText key='END_DATE_LABEL' code='participant.transactionhistory'/>"/>
                        </td>
                      </tr>

                      <tr class="form-row-spacer">
                        <beacon:label property="active" required="true">
                          <cms:contentText key="TYPE" code="participant.transactionhistory"/>
                        </beacon:label>
                        <td class="content-field" colspan="7">
                          <html:select property="promotionType" size="1" styleClass="content-field killme" onchange="maintainEmployer('display','transactionHistory.do')">
                            <html:option value=""><cms:contentText key="SELECT_ONE" code="participant.transactionhistory"/></html:option>
                            <html:options collection="transactionHistoryType" property="code" labelProperty="name" />
                          </html:select>
                        </td>
                      </tr>

                      <tr class="form-row-spacer">
                        <beacon:label property="active" required="true">
                          <cms:contentText key="PROMOTION" code="participant.transactionhistory"/>
                        </beacon:label>
                        <td class="content-field" colspan="2">
                          <html:select property="promotionId" size="1" styleClass="content-field killme">
                            <html:option value=""><cms:contentText key="ALL_PROMOTIONS" code="participant.transactionhistory"/></html:option>
                            <html:options collection="promotionList" property="id" labelProperty="name" />
                          </html:select>
                        </td>
                        <td colspan="3">
													<%  Map paramMap = new HashMap();
														TransactionHistoryForm temp = (TransactionHistoryForm)request.getAttribute( "transactionHistoryForm" );
														paramMap.put( "userId", temp.getUserId() );
														pageContext.setAttribute("searchTransactionHistoryClaimListUrl", ClientStateUtils.generateEncodedLink( "", "transactionHistory.do", paramMap) );
													%>
                          <html:submit styleClass="content-buttonstyle" onclick="setActionAndDispatch('${searchTransactionHistoryClaimListUrl}','showActivity')">
                            <cms:contentText code="participant.transactionhistory" key="SHOW_TRANSACTIONS" />
                          </html:submit>
                        </td>
                      </tr>
                    </table>
                  </td>
                </tr>
              </table>
            </td>
          </tr>
        </table>

        <br/>

        <tiles:insert attribute="transactionList" />

        <table width="100%">
          <tr class="form-buttonrow">
            <td align="center">
              <c:url var="returnToParticipantUrl" value="/participant/participantDisplay.do">
                <c:param name="firstName" value="${participant.firstName}"/>
                <c:param name="lastName" value="${participant.lastName}"/>
              </c:url>
              <html:submit styleClass="content-buttonstyle" onclick="setActionAndDispatch('${returnToParticipantUrl}','display')">
                <cms:contentText key="BACK_TO_PARTICIPANT_OVERVIEW" code="participant.transactionhistory"/>
              </html:submit>
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
</html:form>

<script type="text/javascript">
<!--
  Calendar.setup(
    {
      inputField  : "startDate",         // ID of the input field
      ifFormat    : "${TinyMceDatePattern}",          // the date format
      button      : "startDateTrigger"   // ID of the button
    }
  );
  Calendar.setup(
    {
      inputField  : "endDate",          // ID of the input field
      ifFormat    : "${TinyMceDatePattern}",         // the date format
      button      : "endDateTrigger"    // ID of the button
    }
  );
//-->
</script>


