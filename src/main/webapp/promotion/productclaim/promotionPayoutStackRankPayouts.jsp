<%--UI REFACTORED--%>
<%--
UI REFACTORING Note: Header and footer pages needs refactoring if necessary. 
As this doen't comes under any of our standard layouts and is specific to this page, changed the content wherever necessary as per refactoring requirements.
--%>

<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

<% int calendarCounter = 0; %>


  <c:set var="groupCounter" value="${0}"/>

<script type="text/javascript">
  <!-- 
  var needToConfirm = false;
  function saveForm(value)
  { 
    needToConfirm = value;
  }

  function setStackRankGroupId( id )
  {
    document.promotionPayoutForm.stackRankGroupEditId.value = id;
  }
  //-->
</script>
    
  <%-- groupId tracks which group add category, add product, or remove has been called for --%>
  <html:hidden property="groupEditId" value=""/>
  <html:hidden property="stackRankGroupEditId" value=""/>
	<html:hidden property="hasPendingStackRank"/>
     <tr class="form-blank-row">
      <td></td>
    </tr>
     <tr class="form-row-spacer">
 	<nested:iterate id="promoPayoutGroup" name="promotionPayoutForm" property="promoStackRankPayoutGroupValueList">   
    
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
            <cms:contentText code="promotion.payout" key="RANK"/>
            </th>
            <th class="crud-table-header-row"><cms:contentText code="promotion.payout" key="TO"/><br>
            <cms:contentText code="promotion.payout" key="RANK"/></th>
            <th class="crud-table-header-row"><cms:contentText code="promotion.payout" key="PAYOUT"/></th>
            <th class="crud-table-header-row"><cms:contentText code="system.general" key="CRUD_REMOVE_LABEL"/></th>
          </tr>
                   
 
          <input type="hidden" name="<c:out value="groupStackRankPayoutSize${promoPayoutGroup.guid}"/>" value="<c:out value="${promoPayoutGroup.promoStackRankPayoutValueListCount}"/>"/>
          <nested:hidden property="guid"/>
          <nested:hidden property="promoPayoutGroupId"/>
          <nested:hidden property="version"/>
          <nested:hidden property="createdBy"/>
          <nested:hidden property="dateCreated"/>
          <c:choose>
            <c:when test="${promoPayoutGroup.promoStackRankPayoutValueListCount == '0'}">
              <tr>
                <td colspan="6" class="content-field left-align"><cms:contentText code="promotion.payout" key="NOTHING_FOUND"/></td>
              </tr>
            </c:when>
            <c:otherwise>
              <c:set var="promoPayoutCounter" value="${0}"/>
              <c:set var="switchColor" value="false"/>
              <nested:iterate id="promoPayoutValue" property="promoStackRankPayoutValueList">
         
              <nested:hidden property="promoPayoutId"/>
              <nested:hidden property="version"/>
              <nested:hidden property="createdBy"/>
              <nested:hidden property="dateCreated"/>
              <nested:hidden property="nodeTypeId"/>
              <nested:hidden property="submittersRankNameId"/>

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
	                   <nested:select property="../nodeTypeId" styleClass="content-field" onchange="javascript:saveForm(true)">
	                    <c:forEach items="${nodeTypeList}" var="nodeType">
		                  <html:option value="${nodeType.id}"><cms:contentText key="${nodeType.nameCmKey}" code="${nodeType.cmAssetCode}"/></html:option>
		                </c:forEach>
	                  </nested:select>
				   </td>
				    <td class="content-field left-align">
	                      <nested:select property="../submittersRankNameId" styleClass="content-field" onchange="javascript:saveForm(true)">
					        <html:options collection="submittersToRankTypeList" property="code" labelProperty="name" />			      
					      </nested:select>
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

                <c:choose>
	              <c:when test="${promotionPayoutForm.promotionStatus == 'live'}">
			            <td class="content-field center-align">
			              <nested:text property="fromRank" size="5" maxlength="10" styleClass="content-field" onchange="javascript:saveForm(true)"/>		              
			            </td>
			             <td class="content-field center-align">
			              <nested:text property="toRank" size="5" maxlength="10" styleClass="content-field" onchange="javascript:saveForm(true)"/>		              
			            </td>
			             <td class="content-field center-align">
			              <nested:text property="payoutAmount" size="5" maxlength="10" styleClass="content-field" onchange="javascript:saveForm(true)"/>		              
			            </td>
		            </c:when>                  
	              <c:otherwise>	
	                  	<td class="content-field center-align">
			              <nested:text property="fromRank" size="5" maxlength="10" styleClass="content-field"/>		              
			            </td>
			             <td class="content-field center-align">
			              <nested:text property="toRank" size="5" maxlength="10" styleClass="content-field"/>		              
			            </td>
			             <td class="content-field center-align">
			              <nested:text property="payoutAmount" size="5" maxlength="10" styleClass="content-field"/>		              
			            </td>
	              </c:otherwise>
	              </c:choose> 
                 
              <td class="content-field center-align">
                <nested:checkbox property="removePayout" value="Y"/>
                <c:set var="hasRemoveablePayout" value="true"/>
              </td>
             
              <c:set var="promoPayoutCounter" value="${promoPayoutCounter + 1}"/>
            </tr>
          </nested:iterate>
        </c:otherwise>
      </c:choose>
    </table>
  </td>
</tr>
  <tr class="form-row-spacer">
    <td colspan="2">&nbsp;</td>
    <td colspan="2">
    <input type="button" onclick="setStackRankGroupId('<c:out value="${promoPayoutGroup.guid}"/>');setActionDispatchAndSubmit('promotionPayout.do','addPayoutForThisNode');" name="add_payout_this_node" class="content-buttonstyle" value="<cms:contentText code="promotion.payout" key="ADD_PAY_OUT_LEVEL"/>" />
    </td>
    <td align="right">
      <c:if test="${hasRemoveablePayout}">
       <input type="button" onclick="setStackRankGroupId('<c:out value="${promoPayoutGroup.guid}"/>');setActionDispatchAndSubmit('promotionPayout.do', 'removeStackRankPayouts');" name="add_payout_this_node" class="content-buttonstyle" value="<cms:contentText code="system.button" key="REMOVE_SELECTED"/>" />
      </c:if>
    </td>
  </tr>
    
   <tr class="form-blank-row">
      <td></td>
    </tr>
  <c:set var="groupCounter" value="${groupCounter + 1}"/>
  </nested:iterate>

<tr class="form-row-spacer">
  <td colspan="2">&nbsp;</td>
  <td colspan="2">
	    <html:button property="add_group" styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('promotionPayout.do','addPayoutForNode')" >
	      <cms:contentText code="promotion.payout" key="ADD_PAYOUT_ANOTHER_NODE" />
	    </html:button>
  </td>
</tr>
