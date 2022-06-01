<%--UI REFACTORED --%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.commlog.CommLog"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ include file="/include/taglib.jspf"%>

<script type="text/javascript">
<!--
	function maintainUserCommLogs( method, action )
	{

		document.commLogListForm.method.value=method;
		document.commLogListForm.action = action;
		document.commLogListForm.submit();
	}
//-->
</script>

<html:form styleId="contentForm" action="userCommLogMaintain">
	<html:hidden property="method" value="displayList"/>
	<%--BugFix 18201--%>
	<c:if test="${commLogListForm.userId == null}">
	
	    <c:if test="${userId != null}">
	     
	      
	      <beacon:client-state>
	      	 	<beacon:client-state-entry name="isFromCommlog" value="true"/>
	      	 	<beacon:client-state-entry name="userIdFromCommLog" value="${userId}"/>
	      </beacon:client-state>
	   </c:if>
	
	</c:if>
	<beacon:client-state>
	 	<beacon:client-state-entry name="userId" value="${commLogListForm.userId}"/>
	</beacon:client-state>

	<table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="TITLE" code="communication_log.list"/></span>
        <%-- Subheadline --%>
        <br/>
   		<beacon:username userId="${displayNameUserId}"/>
        <%-- End Subheadline --%>
        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="ADD_INFO" code="communication_log.list"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>
     	
        <cms:errors/>
        
         <table width="90%">
          <tr>
            <td align="right">
            	<%  Map paramMap = new HashMap();
									CommLog temp;
							%>
            	<display:table defaultsort="1" defaultorder="ascending" name="commLogList" id="userCommLog" pagesize="20" sort="list" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
            	<display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
				</display:setProperty>
				<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
            		<display:column titleKey="communication_log.list.DATE_HEADER" headerClass="crud-table-header-row" class="crud-content center-align" sortable="true" sortProperty="dateInitiated">
		        		  <c:choose>
										<c:when test="${userCommLog.commLogSourceType.code != 'sysgen'}">			
											<%  temp = (CommLog)pageContext.getAttribute("userCommLog");
											//code Fix to Bug#16854 put the valueOf commLogId long value as String	
											paramMap.put( "commLogId", String.valueOf(temp.getId()));
											//code Fix to Bug#17851 add the variable commLogList in href
												pageContext.setAttribute("updateUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), "/participant/userCommLogDisplay.do?method=prepareUpdate&commLogList=participantCommLogs", paramMap ) );
											%>
										<beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
							  			<a href="<c:out value='${updateUrl}'/>" class="crud-content-link" align="right">
							  			</beacon:authorize>
         				 					<fmt:formatDate value="${userCommLog.dateInitiated}" pattern="${JstlDateTimePattern}" />
		        		  				<beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
		        		  				</a> 
		        		  				</beacon:authorize>
										</c:when>
										<c:otherwise>
											<%  temp = (CommLog)pageContext.getAttribute("userCommLog");
											//									//code Fix to Bug#16854 put the valueOf commLogId long value as String	
											paramMap.put( "commLogId", String.valueOf(temp.getId()));
												pageContext.setAttribute("viewUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), "/participant/userCommLogDisplay.do?method=prepareDisplay", paramMap ) ); 
											%>
										<beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
										<a href="<c:out value='${viewUrl}'/>" class="crud-content-link" align="right">
										</beacon:authorize>
	       				 					<fmt:formatDate value="${userCommLog.dateInitiated}" pattern="${JstlDateTimePattern}" />
		        		  				<beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
		        		  				</a> 
		        		  				</beacon:authorize>
										</c:otherwise>
						  		</c:choose>
		        		</display:column>

		        	<display:column titleKey="communication_log.list.STATUS_HEADER" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true">
		        	 	<c:out value="${userCommLog.commLogStatusType.name}"/>
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
		        	
		        	<display:column titleKey="communication_log.list.ASSIGNED_TO_HEADER" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true">
		        		<c:out value="${userCommLog.assignedToUser.lastName}"/>
		        		<c:if test="${userCommLog.assignedToUser.lastName}">
		        		,&nbsp;
		        		</c:if>
		        		<c:out value="${userCommLog.assignedToUser.firstName}"/>&nbsp;<c:out value="${userCommLog.assignedToUser.middleName}"/>
	            	</display:column>
		       
		    	</display:table>
		   </td>
		 </tr> 
		 
		    <%--BUTTON ROWS --%>
		  <beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
          <tr class="form-buttonrow">
            <td>
              <table width="100%">
              <tr>
              <td align="left">
                  <html:button property="Add" styleClass="content-buttonstyle" onclick="maintainUserCommLogs('prepareCreate','userCommLogDisplay.do');">
							<cms:contentText key="ADD_COMM_LOG_BUTTON" code="communication_log.list"/>
				  </html:button>
                 </td>
              </tr>
              </table>
          </td>
          </tr>
          </beacon:authorize>
		 
		     <%--  second row of buttons  --%>
          <tr class="form-buttonrow">
            <td>
              <table width="100%">
                <tr>
                  <td align="center">
                    <html:button titleKey="communication_log.list.BACK_BUTTON" property="edit" styleClass="content-buttonstyle"
		               onclick="maintainUserCommLogs('display','participantDisplay.do');">
        			     <cms:contentText key="BACK_BUTTON" code="communication_log.list"/>
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

