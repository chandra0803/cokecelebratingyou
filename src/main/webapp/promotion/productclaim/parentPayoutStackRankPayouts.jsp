<%--UI REFACTORED--%>
<%--
UI REFACTORING Note: Header and footer pages needs refactoring if necessary. 
As this doen't comes under any of our standard layouts and is specific to this page, changed the content wherever necessary as per refactoring requirements.
--%>

<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

  <c:set var="groupCounter" value="${0}"/>

     <tr class="form-blank-row">
      <td></td>
    </tr>
  
    <c:forEach items="${promotion.stackRankPayoutGroups}" var="promoPayoutGroup"> 
    <tr class="form-row-spacer">
    
     <c:choose>
         <c:when test="${groupCounter == '0'}">
           	<beacon:label property="payoutType" required="true" styleClass="content-field-label-top">
        		<cms:contentText key="PAYOUTS" code="promotion.payout"/>
   			</beacon:label>
         </c:when>
         <c:otherwise>
         	<td colspan="2">&nbsp;</td>
         </c:otherwise>
	</c:choose>
		

      <td colspan="6">
        <table class="crud-table" width="100%">
          <tr>
            <th class="crud-table-header-row"><cms:contentText code="promotion.payout" key="NODE_TYPE"/></th>
            <th class="crud-table-header-row"><cms:contentText code="promotion.payout" key="SUBMITTERS_RANK"/></th>
            <th class="crud-table-header-row"><cms:contentText code="promotion.payout" key="FROM"/><br>
            <cms:contentText code="promotion.payout" key="RANK"/></th>
            <th class="crud-table-header-row"><cms:contentText code="promotion.payout" key="TO"/><br>
            <cms:contentText code="promotion.payout" key="RANK"/></th>
            <th class="crud-table-header-row"><cms:contentText code="promotion.payout" key="PAYOUT"/></th>
          </tr>
        
              <c:set var="promoPayoutCounter" value="${0}"/>
              <c:set var="switchColor" value="false"/>
   
			<c:forEach items="${promoPayoutGroup.stackRankPayouts}" var="promoPayoutValue"> 
              <c:choose>
                <c:when test="${switchColor == 'false'}">
                  <tr class="crud-table-row1">
                  <c:set var="switchColor" scope="page" value="true"/>
                </c:when>
                <c:otherwise>
                  <tr class="crud-table-row2">
                  <c:set var="switchColor" scope="page" value="false"/>
                </c:otherwise>
              </c:choose>
              
              <c:choose>
	              <c:when test="${promoPayoutCounter == '0'}">
	               <td class="content-field left-align">
	               	         <c:out value="${promoPayoutGroup.nodeType.nodeTypeName}"/>         
				   </td>
				    <td class="content-field left-align">
				              <c:out value="${promoPayoutGroup.submittersToRankType.name}"/> 
				   </td>
				 </c:when>                  
	              <c:otherwise>	             
	               <td class="content-field left-align">
	                   &nbsp;
				   </td>
				    <td class="content-field left-align">
				    	&nbsp;
	                </td>
				 </c:otherwise>
			  </c:choose>   
          
	        <td class="content-field center-align">
	        	<c:out value="${promoPayoutValue.startRank}"/>	             
			</td>
			
			<td class="content-field center-align">
	             <c:out value="${promoPayoutValue.endRank}"/>
			</td>
			
			<td class="content-field center-align">
				<c:out value="${promoPayoutValue.payout}"/>
			</td>
								

              <c:set var="promoPayoutCounter" value="${promoPayoutCounter + 1}"/>
            </tr>
          </c:forEach>
    </table>
  </td>
</tr>

  
   <tr class="form-blank-row">
      <td></td>
    </tr>
  <c:set var="groupCounter" value="${groupCounter + 1}"/>
  </c:forEach>

