<%--UI REFACTORED --%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.commlog.CommLog"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ include file="/include/taglib.jspf"%>

<script type="text/javascript">
<!--
function goHome()
{
  document.location = '<%=RequestUtils.getBaseURI( request )%>/homepage.do';
}
//-->
</script>

<html:form styleId="contentForm" action="userCommLogMaintain">
	<html:hidden property="method" value="displayList"/>
	<beacon:client-state>
	 	<beacon:client-state-entry name="userId" value="${commLogListForm.userId}"/>
	</beacon:client-state>

<table border="0" cellpadding="10" cellspacing="0" width="100%">
  <tr>
    <td>
      <span class="headline"><cms:contentText key="MY_COMMLOG_TITLE" code="communication_log.list"/></span>
      <%--INSTRUCTIONS--%>
      <br/><br/>
      <span class="content-instruction">
        <cms:contentText key="MY_COMMLOG_INFO" code="communication_log.list"/>
      </span>
      <br/><br/>
      <%--END INSTRUCTIONS--%>

      <cms:errors/>

      <table width="80%">
        <tr>
          <td align="right">
						<%  Map paramMap = new HashMap();
								CommLog temp;
						%>
            <display:table defaultsort="1" defaultorder="ascending" name="commLogList" id="userCommLog" sort="list" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
            <display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
			</display:setProperty>
			<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
              <display:column titleKey="communication_log.list.PARTICIPANT" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true">
                <beacon:username userId="${userCommLog.user.id}"/>
              </display:column>

              <display:column titleKey="communication_log.list.DATE_HEADER" headerClass="crud-table-header-row" class="crud-content center-align" sortable="false">
              					<%  temp = (CommLog)pageContext.getAttribute("userCommLog");
								//code Fix to Bug#16854 put the valueOf commLogId long value as String
              					paramMap.put( "commLogId", String.valueOf(temp.getId()));
												%>
								<c:choose>
									<%-- code Fix to Bug#17851 add the variable commLogList in href--%>
									<c:when test="${userCommLog.commLogSourceType.code != 'sysgen'}">
										<%  pageContext.setAttribute("updateUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), "/participant/userCommLogDisplay.do?method=prepareUpdate&commLogList=adminCommLogs", paramMap ) );
											%>
						  			<a href="<c:out value="${updateUrl}"/>" class="crud-content-link" align="right">
       								<fmt:formatDate value="${userCommLog.dateInitiated}" pattern="${JstlDateTimePattern}" />
										</a> 
									</c:when>
									<c:otherwise>
										<%	pageContext.setAttribute("viewUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), "/participant/userCommLogDisplay.do?method=prepareDisplay", paramMap ) ); 
											%>
										<a href="<c:out value="${viewUrl}"/>" class="crud-content-link" align="right">										
										<fmt:formatDate value="${userCommLog.dateInitiated}" pattern="${JstlDateTimePattern}" />
										</a> 
									</c:otherwise>
				  			</c:choose>
              </display:column>

              <display:column titleKey="communication_log.list.STATUS_HEADER" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true">
                <c:out value="${userCommLog.commLogStatusType.name}"/>
              </display:column>

              <display:column titleKey="communication_log.list.URGENCY_HEADER" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true">
                <c:out value="${userCommLog.commLogUrgencyType.name}"/>
              </display:column>

              <display:column titleKey="communication_log.list.SOURCE_HEADER" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true">
                <c:out value="${userCommLog.commLogSourceType.name}"/>
              </display:column>

                <display:column titleKey="communication_log.list.CATEGORY_HEADER" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true">
                  <c:out value="${userCommLog.commLogCategoryType.name}"/>
                </display:column>

                <display:column titleKey="communication_log.list.REASON_HEADER" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true">
                  <c:out value="${userCommLog.commLogReasonType.name}"/>
                </display:column>
              </display:table>
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
</html:form>

