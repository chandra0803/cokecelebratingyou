<%--UI REFACTORED--%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<table width="50%">
  <tr>
    <td align="right">
        <display:table defaultsort="1" defaultorder="ascending" name="valueObjects" id="goalquestHistoryValueObject" pagesize="20" sort="list" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
        <display:setProperty name="basic.msg.empty_list">
			<tr class="crud-content" align="left"><td colspan="{0}">
              <cms:contentText key="NOTHING_FOUND" code="system.errors"/>
             </td></tr>
		</display:setProperty> 
 		<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>      
              <display:column titleKey="transactionhistory.discretionary.PROMOTION" headerClass="crud-table-header-row" class="crud-content left-align nowrap" sortProperty="promotionName" sortable="true">
              	<c:out value="${goalquestHistoryValueObject.promotionName}"/> 
              </display:column>
              <display:column titleKey="transactionhistory.discretionary.MODULE" headerClass="crud-table-header-row" class="crud-content left-align nowrap" sortProperty="promotionTypeName" sortable="true">
              	<c:out value="${goalquestHistoryValueObject.promotionTypeName}"/>
              </display:column>
              <display:column titleKey="transactionhistory.discretionary.DATE" headerClass="crud-table-header-row" class="crud-content left-align nowrap" sortProperty="transactionDate" sortable="true">
              	<fmt:formatDate value="${goalquestHistoryValueObject.transactionDate}" pattern="${JstlDatePattern}"/>
              </display:column>
              <display:column titleKey="transactionhistory.discretionary.AMOUNT" headerClass="crud-table-header-row" class="crud-content right-align nowrap" sortProperty="awardQuantity" sortable="true">               	
              	<c:if test="${goalquestHistoryValueObject.awardQuantity > 0 || goalquestHistoryValueObject.awardQuantity < 0 }">
			       <fmt:formatNumber value="${goalquestHistoryValueObject.awardQuantity}"/>&nbsp;<c:out value="${goalquestHistoryValueObject.awardTypeName}"/>
            	</c:if>
            	<c:if test="${goalquestHistoryValueObject.awardQuantity == null }">
                	0
                </c:if>
              </display:column>
			  <display:column titleKey="transactionhistory.discretionary.STATUS" headerClass="crud-table-header-row" class="crud-content left-align nowrap" sortProperty="journalStatus" sortable="true">  			  		
			  	<c:out value="${goalquestHistoryValueObject.journalStatus}"/>
			  </display:column>

        </display:table>
           
     </td>
   </tr>    
 </table>