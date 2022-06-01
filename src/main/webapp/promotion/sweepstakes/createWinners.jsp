<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-en.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-setup.js"></script>

<html:form styleId="contentForm" action="createSweepstakesWinnersListSave">
  <html:hidden property="method" value="execute"/>
	<beacon:client-state>
		<beacon:client-state-entry name="promotionId" value="${createWinnersForm.promotionId}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
		<tr>
			<td>
        <span class="headline"><cms:contentText key="TITLE" code="promotion.sweepstakes.create.winners"/></span>
        <span id="quicklink-add"></span><script type="text/javascript">quicklink_display_add('<cms:contentText key="TITLE" code="promotion.sweepstakes.create.winners"/>','<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>'); </script>	  
      	<%-- Subheadline --%>
      	<br/>
      	<span class="subheadline"><c:out value="${promotion.name}"/></span>
      	<%-- End Subheadline --%>
        
        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <c:choose>
            <c:when test="${promotion.surveyPromotion}">
              <cms:contentText key="SURVEY_INSTRUCTIONS" code="promotion.sweepstakes.create.winners"/>
            </c:when>
            <c:otherwise>
              <cms:contentText key="INSTRUCTIONS" code="promotion.sweepstakes.create.winners"/>
            </c:otherwise>
          </c:choose>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
     	
        <cms:errors/>
        
        <table>
          
            <tr class="form-row-spacer">
              <td class="content-field-label">
                <cms:contentText code="promotion.sweepstakes.create.winners" key="SWEEPSTAKE_PERIOD"/>&nbsp;&nbsp;&nbsp;
              </td>
              <beacon:label property="startDate" required="true">
                <cms:contentText code="promotion.sweepstakes.create.winners" key="START_DATE"/>
              </beacon:label>
              <td class="content-field">
                 <html:text property="startDate" styleId="startDate" size="10" maxlength="10" readonly="true"  styleClass="content-field" onfocus="clearDateMask(this);"/>
                 <img id="startDateTrigger" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" alt="<cms:contentText key='START_DATE' code='promotion.sweepstakes.create.winners'/>"/>
              </td>
            </tr>

            <%-- Needed between every regular row --%>
            <tr class="form-blank-row">
              <td></td>
            </tr>

            <tr class="form-row-spacer">
              <td>&nbsp;</td>
              <beacon:label property="endDate" required="true">
                <cms:contentText code="promotion.sweepstakes.create.winners" key="END_DATE"/>
              </beacon:label>
              <td class="content-field">
                 <html:text property="endDate" styleId="endDate" size="10" maxlength="10" readonly="true"  styleClass="content-field" onfocus="clearDateMask(this);"/>
                 <img id="endDateTrigger" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/images/calendar_icon.gif" class="calendar-icon" alt="<cms:contentText key='END_DATE' code='promotion.sweepstakes.create.winners'/>"/>
              </td>
            </tr>
         

					<tr class="form-buttonrow">
						<td align="center" colspan="4">
              <beacon:authorize ifNotGranted="LOGIN_AS">
             	<html:submit styleClass="content-buttonstyle">
               	<cms:contentText code="system.button" key="SUBMIT" />
             	</html:submit>
              </beacon:authorize>

             	<html:button property="cancelBtn" styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('promotionSweepstakesListDisplay.do','display')">
               	<cms:contentText code="system.button" key="CANCEL" />
             	</html:button>
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
      inputField  : "startDate",       	// ID of the input field
      ifFormat    : "${TinyMceDatePattern}",    		// the date format
      button      : "startDateTrigger"  // ID of the button
    });
    Calendar.setup(
    {
      inputField  : "endDate",         	// ID of the input field
      ifFormat    : "${TinyMceDatePattern}",    		// the date format
      button      : "endDateTrigger"    // ID of the button
    });
  </script>

