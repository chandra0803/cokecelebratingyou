<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-en.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-setup.js"></script>

<html:form action="createStackRank" styleId="contentForm">
  <html:hidden property="method" value="createStackRank"/>
	<beacon:client-state>
		<beacon:client-state-entry name="promotionId" value="${createStackRankForm.promotionId}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">

    <%-- title and instructional copy --%>
    <tr>
      <td>
        <span class="headline"><cms:contentText key="TITLE" code="promotion.stackrank.create"/></span>
        <br/>
        <span class="subheadline"><c:out value="${promotion.name}"/></span>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="INSTRUCTIONAL_COPY" code="promotion.stackrank.create"/>
        </span>
        <br/><br/>

        <cms:errors/>
      </td>
    </tr>

    <tr>
      <td>
        <table>
          <tr class="form-row-spacer">
            <td></td>
            <td class="content-field-label top-align"><cms:contentText key="STACK_RANK_PERIOD" code="promotion.stackrank.create"/></td>
            <td>
              <table>
                <tr class="form-row-spacer">
                  <beacon:label property="startDate" required="true">
                    <cms:contentText key="START_DATE" code="promotion.stackrank.create"/>
                  </beacon:label>
                  <td class="content-field">
                    <html:text property="startDate" maxlength="10" size="10" styleClass="content-field" readonly="true" styleId="startDate" onfocus="clearDateMask(this);"/>
                    <img alt="start date" id="startDateTrigger" src="<%= RequestUtils.getBaseURI(request) %>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon"/>
                  </td>
                </tr>

                <tr class="form-row-spacer">
                  <beacon:label property="endDate" required="true">
                    <cms:contentText key="END_DATE" code="promotion.stackrank.create"/>
                  </beacon:label>
                  <td class="content-field">
                    <html:text property="endDate" maxlength="10" size="10" styleClass="content-field" readonly="true" styleId="endDate" onfocus="clearDateMask(this);"/>
                    <img alt="end date" id="endDateTrigger" src="<%= RequestUtils.getBaseURI(request) %>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon"/>
                  </td>
                </tr>
              </table>
            </td>
          </tr>

          <tr class="form-row-spacer">
            <beacon:label property="calculatePayout" required="true" styleClass="top-align">
              <cms:contentText key="PROCESS_TO_RUN" code="promotion.stackrank.create"/>
            </beacon:label>
            <td>
              <table>
                <tr class="form-row-spacer">
                  <td class="content-field">
                    <html:radio property="calculatePayout" value="false"/>&nbsp;&nbsp;<cms:contentText key="RANKING_ONLY" code="promotion.stackrank.create"/>
                  </td>
                </tr>
                <tr class="form-row-spacer">
                  <td class="content-field">
                    <html:radio property="calculatePayout" value="true"/>&nbsp;&nbsp;<cms:contentText key="RANKING_AND_PAYOUTS" code="promotion.stackrank.create"/>
                  </td>
                </tr>
              </table>
            </td>
          </tr>

          <%-- buttons --%>
          <tr class="form-buttonrow">
            <td></td>
            <td></td>
            <td align="left">

              <html:submit styleClass="content-buttonstyle">
                <cms:contentText key="SUBMIT" code="system.button"/>
              </html:submit>

              <html:cancel styleClass="content-buttonstyle">
                <cms:contentText key="CANCEL" code="system.button"/>
              </html:cancel>
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
</html:form>

<script type="text/javascript">
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

</script>