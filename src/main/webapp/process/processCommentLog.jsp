<%--UI REFACTORED--%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.process.ProcessInvocation"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<html:form styleId="contentForm" action="processCommentLog">
  <html:hidden property="method"/>
  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>        
		<span class="headline"><cms:contentText key="TITLE" code="process.comment.log"/></span>
		<span id="quicklink-add"></span><script type="text/javascript">quicklink_display_add('<cms:contentText key="TITLE" code="process.comment.log"/>','<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>'); </script>
		<%--INSTRUCTIONS--%>
		<br/><br/>
		<span class="content-instruction">
		  <cms:contentText key="INSTRUCTIONS" code="process.comment.log"/>
		</span>
		<br/><br/>
		<%--END INSTRUCTIONS--%>
    	
        <cms:errors/>
        
        <%-- Process Comment Log table  --%>
  		<table width="50%">
  		  <tr class="form-row-spacer">
            <td class="content-field-label">
              <cms:contentText key="NAME" code="process.comment.log"/>
            </td>
            <td class="content-field-review">
              <c:out value="${processInvocation.process.name}"/>			
            </td>
          </tr>	
          	
          <tr class="form-row-spacer">
            <td class="content-field-label">
              <cms:contentText key="BEGIN_DATE" code="process.log"/>
            </td>
            <td class="content-field-review">              
              <fmt:formatDate value="${processInvocation.startDate}" pattern="${JstlDateTimePattern}" />			
            </td>
          </tr>	
          
          <tr class="form-row-spacer">
            <td class="content-field-label">
              <cms:contentText key="END_DATE" code="process.log"/>
            </td>
            <td class="content-field-review">              
              <fmt:formatDate value="${processInvocation.endDate}" pattern="${JstlDateTimePattern}" />			
            </td>
          </tr>	
           
          <tr class="form-row-spacer">
            <td class="content-field-label">
            	<cms:contentText key="RUN_BY" code="process.log"/>
            </td>
          	<c:choose>
          		<%-- Scheduler is a person --%>
				<c:when test='${processInvocation.runAsUser != null}'>
					<td class="content-field-review">
						<c:out value="${processInvocation.runAsUser.lastName}" />,&nbsp;<c:out value="${processInvocation.runAsUser.firstName}" />&nbsp;<c:out value="${processInvocation.runAsUser.middleName}" />
					</td>
				</c:when>
				<%-- Scheduler is System --%>
				<c:otherwise>
					<td class="content-field-label">
						<cms:contentText key="SCHEDULER" code="process.log"/>
					</td>
				</c:otherwise>
			</c:choose>
          </tr>
          
          <%-- Parameters --%>
          <tr class="form-row-spacer">
            <td class="content-field-label">
            	<cms:contentText key="PARAMETERS" code="process.log"/>
            </td>          
            <td class="content-field-review" align="left">  
              <c:set var="parameterValueMap" value="${processInvocation.formattedParameterValueMap}"/>
              <c:set var="parameterValueTDClass" value="content-field-review"/>
              
              <%@ include file="/process/processParameterValueDisplay.jspf"%>             
  	        </td>
          </tr>
               
          <tr class="form-blank-row">
            <td></td>
          </tr>
         </table>
         
         <table width="100%"> 
          <%-- Comments --%>
	      <tr>
	      	<td align="left">   
		      	<display:table defaultsort="1" defaultorder="ascending" name="processComments" id="processInvocationComment" pagesize="20" sort="list" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
		      	<display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
				</display:setProperty>   
				<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>           	
		        	<display:column title="&nbsp;" headerClass="crud-table-header-row" class="crud-content left-align" sortable="false"  >										
						<c:out value="${processInvocationComment.comments}"/>							
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
										<%  Map parameterMap = new HashMap();
												ProcessInvocation temp = (ProcessInvocation)request.getAttribute("processInvocation");
												parameterMap.put( "processId", temp.getProcess().getId() );
												pageContext.setAttribute("processLogUrl", ClientStateUtils.generateEncodedLink( "", "processLog.do", parameterMap ) );
										%>
				  					<html:button property="view_list" styleClass="content-buttonstyle" onclick="callUrl('${processLogUrl}')">
											<cms:contentText key="VIEW_ACTIVE_LOG" code="process.comment.log"/>
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