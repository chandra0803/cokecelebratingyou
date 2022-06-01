<%--UI REFACTORED--%>

<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-en.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-setup.js"></script>

<% String formAction = "processScheduleList"; //default action %>

<html:form styleId="contentForm" action="<%=formAction%>" >
  <html:hidden property="method"/>
	<beacon:client-state>
		<beacon:client-state-entry name="processId" value="${processScheduleListForm.processId}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td align="left" valign="top">
        <span class="headline"><cms:contentText key="TITLE" code="process.schedules"/></span>
        <span class="subheadline"><c:out value="${processSchedulesForm.name}"/></span>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="INSTRUCTIONS" code="process.schedules"/>
        </span>
        <br/><br/>
      </td>
    </tr>
    <tr>
      <td>
        <cms:errors/>
      </td>
    </tr>
    <tr>
      <td width="50%" valign="top">
        <table>

          <tr class="form-row-spacer">
          <td>
            <display:table defaultsort="1" defaultorder="ascending" name="scheduleList" id="schedule" sort="list"
                requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
				<display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
				</display:setProperty>
				<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
                    <display:column titleKey="process.schedules.FREQUENCY" headerClass="crud-table-header-row"
                      class="crud-content left-align nowrap" sortable="true">
                      <c:out value="${schedule.processSchedule.processFrequencyType.name}" />
                   </display:column>

                    <display:column titleKey="process.schedules.DAY_AND_TIME" headerClass="crud-table-header-row"
                      class="crud-content left-align nowrap" sortable="true">
                      <c:choose>
                      	<c:when test="${schedule.processSchedule.processFrequencyType.code != 'cron' }">
	                      <c:if test="${schedule.processSchedule.processFrequencyType.code == 'weekly' }">
	                        <c:out value="${schedule.processSchedule.dayOfWeekType.name}" />&nbsp;
	                      </c:if>
	                      <c:if test="${schedule.processSchedule.processFrequencyType.code == 'monthly' }">
	                        <c:out value="${schedule.processSchedule.dayOfMonthType.name}" />&nbsp;
	                      </c:if>
	                      
	            		  <fmt:formatDate value="${schedule.processSchedule.timeOfDayAsDate}" pattern="hh:mm a zz"/>
	            		</c:when>
	            		<c:otherwise>
	            			<c:out value="${schedule.processSchedule.cronExpression}" />&nbsp;
	            		</c:otherwise>
	            	  </c:choose>
                    </display:column>

                    <display:column titleKey="process.schedules.DATES" headerClass="crud-table-header-row"
                      class="crud-content left-align nowrap" sortable="true">
            <fmt:formatDate value="${schedule.processSchedule.startDate}" pattern="${JstlDatePattern}" />
            <c:if test="${schedule.processSchedule.processFrequencyType.code != 'one_time_only' }">
              <c:choose>
                <c:when test="${empty schedule.processSchedule.endDate}">
                   - <cms:contentText key="NO_END_DATE" code="process.schedules"/>
                </c:when>
                <c:otherwise>
                   - <fmt:formatDate value="${schedule.processSchedule.endDate}" pattern="${JstlDatePattern}" />
                </c:otherwise>
              </c:choose>
            </c:if>
                    </display:column>
                    
                    <display:column titleKey="process.schedules.PARAMETERS" headerClass="crud-table-header-row"
                      class="crud-content left-align nowrap" sortable="true">
                      
                      <c:set var="parameterValueMap" value="${schedule.processParameterStringArrayMap}"/>
                      <c:set var="parameterValueTDClass" value="content-field"/>
                      
                      <%@ include file="/process/processParameterValueDisplay.jspf"%>                       
                    </display:column>
 
                     <display:column titleKey="process.schedules.REMOVE" headerClass="crud-table-header-row"
                      class="crud-content left-align nowrap" sortable="true">
                      <INPUT name="removeSchedules" type="checkbox" value="<c:out value="${schedule.name}"/>" >                      
                    </display:column>
                                       
              </display:table>
              </td>
          </tr>

          <beacon:authorize ifAnyGranted="${process.launchRoleNames}">
            <tr class="form-blank-row"><td></td></tr>

            <tr valign="top">
              <td>
                <table border="0" cellpadding="10" cellspacing="0" width="100%">
                  <tr>
                    <td align="left" valign="top">
                      <html:submit styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('scheduleProcess.do', 'unspecified')" >
                        <cms:contentText code="process.schedules" key="ADD_SCHEDULE"/>
                      </html:submit>
                    </td>
                    <td align="right" valign="top">
                      <html:submit styleClass="content-buttonstyle" onclick="setDispatch('removeSchedules')" >
                        <cms:contentText code="process.schedules" key="REMOVE_SELECTED"/>
                     </html:submit>
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
          </beacon:authorize>

        </table>
      </td>
    </tr>
    
    <tr><td>&nbsp;</td></tr>
    
    <tr>
      <td>
        <html:button property="back_to_list" styleClass="content-buttonstyle" onclick="callUrl('processList.do')" >
          <cms:contentText code="process.schedules" key="BACK_TO_PROCESS_LIST"/>
        </html:button>
      </td>
    </tr>
  </table>
</html:form>
   