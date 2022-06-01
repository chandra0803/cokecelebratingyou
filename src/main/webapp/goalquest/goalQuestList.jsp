<%--UI REFACTORED--%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.promotion.Promotion" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ include file="/include/taglib.jspf"%>

<html:hidden property="method" value=""/>

<table border="0" cellpadding="10" cellspacing="0" width="100%">
	<tr>
	  <td>
		<span class="headline"><cms:contentText key="TITLE" code="promotion.goalquest.list"/></span>

		<%--INSTRUCTIONS--%>
		<br/><br/>
     	<span class="content-instruction">
			<cms:contentText key="INSTRUCTION" code="promotion.goalquest.list"/>
     	</span>
     	<br/><br/>
     	<%--END INSTRUCTIONS--%>
     	<cms:errors/>
     	
		<%--  START HTML needed with display table --%>
		<%-- Table --%>
		<table width="50%">
    	  <tr>
	    	<td align="right">
				<%  Map parameterMap = new HashMap();
						Promotion temp;
				%>			
			  <display:table defaultsort="1" defaultorder="ascending" name="goalQuestList" id="goalQuest" sort="list" pagesize="20" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
			  <display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
				   				</display:setProperty>
				   				<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
				<display:column titleKey="promotion.goalquest.list.NAME" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true" sortProperty="name">
					<c:out value="${goalQuest.name}"/>
		        </display:column>	
		        <display:column titleKey="promotion.goalquest.list.STATUS" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true" sortProperty="promotionStatus">
					<c:out value="${goalQuest.promotionStatus.name}"/>	        	
		        </display:column>
				<display:column titleKey="promotion.goalquest.list.ACTIONS" headerClass="crud-table-header-row" class="crud-content center-align">
					<%  temp = (Promotion)pageContext.getAttribute("goalQuest");
						Boolean isExpired = new Boolean(temp.getPromotionStatus().isExpired()); 
							parameterMap.put( "id", temp.getId() );
							parameterMap.put( "isExpired", isExpired );
							pageContext.setAttribute("viewUrl", ClientStateUtils.generateEncodedLink( "", "pendingGQAwardSummary.do?method=runCalculation", parameterMap ) );
					%>
			    	<a href="<c:out value="${viewUrl}"/>" class="crud-content-link">
			    		<cms:contentText key="RUN_CALCULATION" code="promotion.goalquest.list"/>
			    	</a>		            
		        </display:column>	
				
		      </display:table>		    
		    </td>
          </tr>
        </table>
	    	<%-- Table --%>
		<%--  END HTML needed with display table --%>
		
	  </td>
	</tr>
</table>
