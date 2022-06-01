<%--UI REFACTORED--%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.process.Process"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<html:form styleId="contentForm" action="processList">
  <html:hidden property="method"/>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
   <tr>
      <td>
        <c:choose>
          <c:when test="${ pageType == 'active' }">
            <span class="headline"><cms:contentText key="TITLE" code="process.list"/></span>
            <span id="quicklink-add"></span><script type="text/javascript">quicklink_display_add('<cms:contentText key="TITLE" code="process.list"/>','<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>'); </script>
            <br/><br/>
            <span class="content-instruction">
              <cms:contentText key="INSTRUCTIONS" code="process.list"/>
            </span>
            <br/><br/>
          </c:when>
          <c:otherwise>
            <span class="headline"><cms:contentText key="INACTIVE_TITLE" code="process.list"/></span>
            <span id="quicklink-add"></span><script type="text/javascript">quicklink_display_add('<cms:contentText key="INACTIVE_TITLE" code="process.list"/>','<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>'); </script>
            <br/><br/>
            <span class="content-instruction">
              <cms:contentText key="INACTIVE_INSTRUCTIONS" code="process.list"/>
            </span>
            <br/><br/>
          </c:otherwise>
        </c:choose>

        <cms:errors/>
        
        <%-- Process List table  --%>
        <table width="80%">
        <c:if test="${ pageType == 'active' }">
         <tr>
          <td class="content-field">
            <span class="content-bold error"><cms:contentText key="PROCESS_TYPE" code="process.list"/></span>
       	      <html:select property="processType" styleClass="content-field" onchange="setDispatchAndSubmit('display');">
		          <html:option value=''><cms:contentText key="CHOOSE_ONE" code="system.general"/></html:option>
		          <html:options collection="processListType" property="code" labelProperty="name" />
	          </html:select>
	          <cms:contentText key="PROCESS_TYPE_INSTR" code="process.list"/>
    	    </td>
          </tr>
          </c:if>
          
          <tr>
            <td align="right">
							<%  Map parameterMap = new HashMap();
									Process temp;
							%>
              <display:table defaultsort="1" defaultorder="ascending" name="processList" id="thisProcess" pagesize="20" sort="list" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
				<display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
				</display:setProperty>
				<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
                <display:column titleKey="process.list.NAME" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true" sortProperty="name">
										<%  temp = (Process)pageContext.getAttribute("thisProcess");
												parameterMap.put( "processId", temp.getId() );
												pageContext.setAttribute("processNameUrl", ClientStateUtils.generateEncodedLink( "", "processDetailDisplay.do", parameterMap ) );
										%>
                  <beacon:authorize ifAnyGranted="${thisProcess.editRoleNames }">
                    <a href="<c:out value="${processNameUrl}"/>">
                  </beacon:authorize>
                  <c:out value="${thisProcess.name}"/>
                  <beacon:authorize ifAnyGranted="${thisProcess.editRoleNames}">
                    </a>
                  </beacon:authorize>
                </display:column>

                <c:if test="${ pageType == 'inactive' }">
                  <display:column property="processStatusType.name" titleKey="process.list.TYPE" headerClass="crud-table-header-row" class="crud-content left-align nowrap" sortable="false"></display:column>
                </c:if>

                <display:column titleKey="process.list.LAST_EXECUTED_DATE" headerClass="crud-table-header-row" class="crud-content left-align nowrap" sortable="true">
                  <fmt:formatDate value="${thisProcess.processLastExecutedDate}" pattern="${JstlDateTimePattern}" />
                </display:column>

                <display:column titleKey="process.list.ACTIONS" headerClass="crud-table-header-row" class="crud-content left-align nowrap" sortable="false">
                  <table border="0" cellpadding="0" cellspacing="0">
                    <tr>
                      <c:if test="${ pageType == 'active' }">
                        <td class="subheadline" style="width: 30%;">
                          <beacon:authorize ifAnyGranted="${thisProcess.launchRoleNames}">
														<% pageContext.setAttribute("launchUrl", ClientStateUtils.generateEncodedLink( "", "processLaunch.do?method=displayLaunch", parameterMap ) ); %>
                            <a href="<c:out value="${launchUrl}"/>">
                              <cms:contentText key="LAUNCH" code="process.list"/>
                            </a>
                          </beacon:authorize>
                        </td>
                        <td>&nbsp;</td>
                        <td class="subheadline" style="width: 30%;">
                          <beacon:authorize ifAnyGranted="${thisProcess.launchRoleNames}">
														<% pageContext.setAttribute("scheduleUrl", ClientStateUtils.generateEncodedLink( "", "processScheduleList.do", parameterMap ) ); %>
                            <a href="<c:out value="${scheduleUrl}"/>">
                              <cms:contentText key="SCHEDULE" code="process.list"/>
                            </a>
                          </beacon:authorize>
                        </td>
                        <td>&nbsp;</td>
                      </c:if>
                      <td class="subheadline" style="width: 40%;" align="right" >
                        <beacon:authorize ifAnyGranted="${thisProcess.viewLogRoleNames}">
													<% 	temp = (Process)pageContext.getAttribute("thisProcess");
														parameterMap.put( "processId", temp.getId() );
														pageContext.setAttribute("viewLogUrl", ClientStateUtils.generateEncodedLink( "", "processLog.do", parameterMap ) ); %>
                          <a href="<c:out value="${viewLogUrl}"/>">
                            <cms:contentText key="VIEW_LOG" code="process.list"/>
                          </a>
                        </beacon:authorize>
                      </td>
                    </tr>
                  </table>
                </display:column>

               </display:table>
              </td>
            </tr>

            <%--BUTTON ROWS --%>
            <tr class="form-buttonrow">
              <td>
                <table width="100%">
                   <c:if test="${ pageType == 'active' }">
                     <beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
                      <tr>
                        <td align="left">
                          <html:button property="add" styleClass="content-buttonstyle" onclick="callUrl('processDetailDisplay.do')">
                            <cms:contentText code="process.list" key="ADD_NEW_PROCESS" />
                          </html:button>
                        </td>
                      </tr>
                    </beacon:authorize>
                  </c:if>
                  <tr class="form-blank-row">
                    <td></td>
                  </tr>
                   <c:if test="${ pageType == 'active' }">
                     <beacon:authorize ifAnyGranted="BI_ADMIN">
                      <tr>
                        <td align="left">
                          <html:button property="add" styleClass="content-buttonstyle" onclick="callUrl('processExecutionList.do')">
                            <cms:contentText code="process.list" key="VIEW_EXECUTE" />
                          </html:button>
                        </td>
                      </tr>
                    </beacon:authorize>
                  </c:if>
                  <tr>
                    <c:if test="${ pageType == 'active' }">
                    <td align="center">
                      <html:button property="view_inactive_list" styleClass="content-buttonstyle" onclick="callUrl('inactiveProcessList.do')">
                          <cms:contentText code="process.list" key="VIEW_INACTIVE" />
                        </html:button>
                    </td>
                  </c:if>
                  <c:if test="${ pageType == 'inactive' }">
                    <td align="center">
                        <html:button property="view_list" styleClass="content-buttonstyle" onclick="callUrl('processList.do')">
                          <cms:contentText code="process.list" key="VIEW_ACTIVE" />
                        </html:button>
                    </td>
                  </c:if>
                  </tr>
                </table>
              </td>
            </tr>
          </table>
       </td>
     </tr>
  </table>
</html:form>        