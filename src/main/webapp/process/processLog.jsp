<%--UI REFACTORED--%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.process.ProcessInvocation"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<html:form styleId="contentForm" action="processLog">
  <html:hidden property="method"/>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>        
		<span class="headline"><cms:contentText key="TITLE" code="process.log"/></span>
		<span id="quicklink-add"></span><script type="text/javascript">quicklink_display_add('<cms:contentText key="TITLE" code="process.log"/>','<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>'); </script>
		<%-- Subheadline --%>
        <br/>
        <span class="subheadline"><c:out value="${process.name}"/></span>
        <%-- End Subheadline --%>
        <%--INSTRUCTIONS--%>
		<br/><br/>
		<span class="content-instruction">
		  <cms:contentText key="INSTRUCTIONS" code="process.log"/>
		</span>
		<br/><br/>
		<%--END INSTRUCTIONS--%>
    	
        <cms:errors/>
        
        <%-- Process Log table  --%>
  		<table width="100%">
  		  <tr>
            <td align="right">
							<%  Map parameterMap = new HashMap();
									ProcessInvocation temp;
							%>
							<%-- To fix 20794 inorder to sort by date properly in process log screen --%>
            	<display:table defaultsort="1" defaultorder="descending" name="processLog" id="processInvocation"  partialList="true" pagesize="20" size="size" sort="external" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
            	<display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
				</display:setProperty>   
				<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>          	
                	<display:column titleKey="process.log.BEGIN_DATE" headerClass="crud-table-header-row" class="crud-content-link link-align" sortable="true" style="white-space: nowrap;" sortProperty="startDate">	
                		<fmt:formatDate value="${processInvocation.startDate}" pattern="${JstlDateTimePattern}" />
                	</display:column>
                			
                	<display:column titleKey="process.log.END_DATE" headerClass="crud-table-header-row" class="crud-content-link link-align" sortable="true" style="white-space: nowrap;" sortProperty="endDate">	
                		<fmt:formatDate value="${processInvocation.endDate}" pattern="${JstlDateTimePattern}" />
                	</display:column>
                	             	 	
					<display:column sortProperty="runAsUser" titleKey="process.log.RUN_BY" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true">		
						<c:choose>
							<%-- Scheduler is a person --%>
							<c:when test='${processInvocation.runAsUser != null}'>
								<c:out value="${processInvocation.runAsUser.lastName}" />,&nbsp;<c:out value="${processInvocation.runAsUser.firstName}" />&nbsp;<c:out value="${processInvocation.runAsUser.middleName}" />
							</c:when>
							<%-- Scheduler is System --%>
							<c:otherwise>
								<cms:contentText key="SCHEDULER" code="process.log"/>
							</c:otherwise>
						</c:choose>
					</display:column>
										
					<display:column titleKey="process.log.PARAMETERS" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true" style="white-space: nowrap;">										
						<c:set var="parameterValueMap" value="${processInvocation.formattedParameterValueMap}"/>
            <c:set var="parameterValueTDClass" value="content-field"/>
            
            <%@ include file="/process/processParameterValueDisplay.jspf"%>      
					</display:column>					
					          									
					<display:column titleKey="process.log.COMMENT_LOG" headerClass="crud-table-header-row" class="crud-content-link link-align" sortable="false" >	                		
						<%-- Don't show link if there are no comments --%>
						<c:choose>
							<c:when test="${empty processInvocation.processInvocationComments}"> 
								&nbsp;
							</c:when>
							<c:otherwise>
								<%  temp = (ProcessInvocation)pageContext.getAttribute("processInvocation");
										parameterMap.put( "processInvocationId", temp.getId() );
										pageContext.setAttribute("viewCommentsUrl", ClientStateUtils.generateEncodedLink( "", "processCommentLog.do", parameterMap ) );
								%>
	              <a href="<c:out value="${viewCommentsUrl}"/>" class="content-link">
									<cms:contentText key="VIEW_COMMENTS" code="process.log"/>
								</a>
							</c:otherwise>
						</c:choose>
                	</display:column>
       			</display:table>		    
            </td>
          </tr>
           
          <%--BUTTON ROWS --%>
          <tr class="form-buttonrow">
            
            <td>
              <table width="100%">                
                <tr>                  
	              <td align="center">
	                    <html:button property="view_list" styleClass="content-buttonstyle" onclick="callUrl('processList.do')">
	                      <cms:contentText code="process.list" key="VIEW_ACTIVE" />
	                    </html:button>		
	              	</td>
                </tr>
              </table>
            </td>
          </tr>
        </table>
        <%-- Table --%>
       </td>
     </tr>
  </table>
</html:form>