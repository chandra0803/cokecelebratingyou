<%--UI REFACTORED--%>

<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-en.js"></script>
<script type="text/javascript" src="<%=RequestUtils.getBaseURI(request)%>/assets/g4skin/scripts/calendar/calendar-setup.js"></script>

<html:form styleId="contentForm" action="processExecutionList" >
  <html:hidden property="method"/>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td align="left" valign="top">
        <span class="headline"><cms:contentText key="TITLE" code="process.execution.list"/></span>
        <br/>
        <span class="content-instruction">
          <cms:contentText key="INSTRUCTIONS" code="process.execution.list"/>
        </span>
        <br/>
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
            <display:table defaultsort="1" defaultorder="ascending" name="processList" id="process" sort="list"
                requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
				<display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
				</display:setProperty>
				<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
				
                    <display:column titleKey="process.execution.list.PROCESS_NAME" headerClass="crud-table-header-row" class="crud-content left-align nowrap" sortable="true">
                      <c:out value="${process.processName}" />
                   </display:column>
 
                     <display:column titleKey="process.execution.list.INTERRUPT" headerClass="crud-table-header-row"
                      class="crud-content left-align nowrap" sortable="true">
                      <INPUT name="interruptProcessIds" type="checkbox" value="<c:out value="${process.processId}"/>" >                      
                    </display:column>
                                       
              </display:table>
          </tr>

          <tr class="form-blank-row"><td>&nbsp;</td></tr>

          <tr valign="top">
            <td>
              <table border="0" cellpadding="10" cellspacing="0" width="100%">
                <tr>
                  <td>
        			<c:if test="${ allowInterrupt }">
        			<beacon:authorize ifAnyGranted="BI_ADMIN">
	                    <html:submit styleClass="content-buttonstyle" onclick="setDispatch('interruptProcess')" >
	                      <cms:contentText code="process.execution.list" key="INTERRUPT_SELECTED"/>
	                    </html:submit>
	                    <%--
	                    <html:submit styleClass="content-buttonstyle" onclick="setDispatch('interruptAllProcesses')" >
	                      <cms:contentText code="process.execution.list" key="INTERRUPT_ALL"/>
	                    </html:submit>
	                    --%>
        			</beacon:authorize>
        			</c:if>
        			
			        <html:button property="refresh" styleClass="content-buttonstyle" onclick="callUrl('processExecutionList.do')" >
			          <cms:contentText code="process.execution.list" key="REFRESH"/>
			        </html:button>
                  </td>
                </tr>

          		<tr class="form-blank-row"><td>&nbsp;</td></tr>

			    <tr>
			      <td>
			        <html:button property="back_to_list" styleClass="content-buttonstyle" onclick="callUrl('processList.do')" >
			          <cms:contentText code="process.execution.list" key="BACK_TO_PROCESS_LIST"/>
			        </html:button>
			      </td>
			    </tr>
              </table>
            </td>
          </tr>

        </table>
      </td>
    </tr>
  </table>
</html:form>
