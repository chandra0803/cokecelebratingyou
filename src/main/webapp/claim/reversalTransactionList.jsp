<%--UI REFACTORED--%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<table width="50%">
  <tr>
    <td align="right">

        <display:table defaultsort="1" defaultorder="ascending" name="transactionHistoryReversalList" id="journal" pagesize="20" sort="list" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
        <display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
		</display:setProperty>  
		<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>            
              <display:column titleKey="transactionhistory.discretionary.PROMOTION" property="promotion.name" headerClass="crud-table-header-row" class="crud-content left-align nowrap" sortable="true"/>
              <display:column titleKey="transactionhistory.discretionary.MODULE" property="promotion.promotionType.desc" headerClass="crud-table-header-row" class="crud-content left-align nowrap" sortable="true"/>  
              <display:column titleKey="transactionhistory.discretionary.DATE" property="transactionDate" headerClass="crud-table-header-row" class="crud-content left-align nowrap" sortable="true"/>  
              <display:column titleKey="transactionhistory.discretionary.AMOUNT" property="transactionAmount" headerClass="crud-table-header-row" class="crud-content right-align nowrap" sortable="true"/>			  
        </display:table>
           
     </td>
   </tr>    
 </table>