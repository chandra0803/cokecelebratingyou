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
		<span class="headline"><cms:contentText key="TITLE" code="promotion.challengepoint.calculationList"/></span>

		<%--INSTRUCTIONS--%>
		<br/><br/>
     	<span class="content-instruction">
			<cms:contentText key="INSTRUCTION" code="promotion.challengepoint.calculationList"/>
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
			  <display:table defaultsort="1" defaultorder="ascending" name="challengepointList" id="challengepointPromo" sort="list" pagesize="20" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
			  <display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
				 </display:setProperty>
				 <display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
				<display:column titleKey="promotion.challengepoint.calculationList.NAME" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true" sortProperty="name">
					<c:out value="${challengepointPromo.name}"/>
		        </display:column>	
		        <display:column titleKey="promotion.challengepoint.calculationList.STATUS" headerClass="crud-table-header-row" class="crud-content left-align" sortable="true" sortProperty="promotionStatus">
					<c:out value="${challengepointPromo.promotionStatus.name}"/>	        	
		        </display:column>
				<display:column titleKey="promotion.challengepoint.calculationList.ACTIONS" headerClass="crud-table-header-row" class="crud-content center-align">
					<%  temp = (Promotion)pageContext.getAttribute("challengepointPromo");
						Boolean isExpired = new Boolean(temp.getPromotionStatus().isExpired()); 
							parameterMap.put( "id", temp.getId() );
							parameterMap.put( "isExpired", isExpired );
							pageContext.setAttribute("viewUrl", ClientStateUtils.generateEncodedLink( "", "pendingCPAwardSummary.do", parameterMap ) );
					%>
					<c:if test="${not challengepointPromo.promotionStatus.expired}">
			    	<a href="<c:out value="${viewUrl}"/>" class="crud-content-link">
			    		<cms:contentText key="RUN_CALCULATION" code="promotion.challengepoint.calculationList"/>
			    	</a>
			    	</c:if>
					<c:if test="${challengepointPromo.promotionStatus.expired}">
			    		<cms:contentText key="RUN_CALCULATION" code="promotion.challengepoint.calculationList"/>
			    	</c:if>
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
