<%--UI REFACTORED--%>
<%--
UI REFACTORING Note: Header and footer pages needs refactoring if necessary. 
--%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.ui.promotion.PromotionGoalPayoutLevelFormBean"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
<%@ include file="/include/taglib.jspf"%>

	<c:if test="${promotionType == 'challengepoint'}">
		<c:set var="payoutForm" value="${challengePointPayoutForm}"/>
	</c:if>
	<c:if test="${promotionType == 'goalquest'}">
		<c:set var="payoutForm" value="${promotionGoalPayoutForm}"/>
	</c:if>

    <table class="crud-table" width="100%">
      <tr>
        <th valign="top" class="crud-table-header-row">
          *<cms:contentText code="promotion.payout.goalquest" key="DISPLAY"/><br>
            <cms:contentText code="promotion.payout.goalquest" key="ORDER"/>
        </th>
        <th valign="top" class="crud-table-header-row">
            *<cms:contentText code="promotion.payout.goalquest" key="NAME"/>
        </th>
        <th valign="top" class="crud-table-header-row">
            *<cms:contentText code="promotion.payout.goalquest" key="DESCRIPTION"/>
        </th>
        <th valign="top" class="crud-table-header-row">
            *<cms:contentText code="promotion.payout.goalquest" key="MINIMUM"/><br>
            <cms:contentText code="promotion.payout.goalquest" key="QUALIFIER"/><br>
            <cms:contentText code="promotion.payout.goalquest" key="FOR_BONUS"/>
        </th>
        <th valign="top" class="crud-table-header-row">
            *<cms:contentText code="promotion.payout.goalquest" key="INCREMENTAL"/><br>
            <cms:contentText code="promotion.payout.goalquest" key="QUANTITY"/>
        </th>
        <th valign="top" class="crud-table-header-row">
            *<cms:contentText code="promotion.payout.goalquest" key="AWARD"/>
        </th>
        <th valign="top" class="crud-table-header-row">
            <cms:contentText code="promotion.payout.goalquest" key="MAXIMUM"/><br>
            <cms:contentText code="promotion.payout.goalquest" key="POINTS"/>
        </th>
        <c:if test="${displayFlag2 == 'false'}">
          <th valign="top" class="crud-table-header-row">
              <cms:contentText code="promotion.payout.goalquest" key="REORDER"/><br>
              <cms:contentText code="promotion.payout.goalquest" key="LEVELS"/>
          </th>
        </c:if>
        <c:if test="${displayFlag == 'false'}">
          <th valign="top" class="crud-table-header-row">
              <cms:contentText code="promotion.payout.goalquest" key="DELETE"/>
          </th>
        </c:if>
      </tr>                                                
      <c:set var="switchColor" value="false"/>
      <% Map parameterMap = new HashMap();
         PromotionGoalPayoutLevelFormBean temp; %>      
      <nested:iterate id="goalLevel" name="payoutForm" property="goalLevelValueList" indexId="goalLevelIndex">
        <nested:hidden property="goalLevelId"/>
        <nested:hidden property="managerAward"/>
        <nested:hidden property="goalLevelcmAssetCode"/>
        <nested:hidden property="nameKey"/>
        <nested:hidden property="descriptionKey"/>
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
        <td align="center">
          <c:out value="${goalLevel.sequenceNumber}"/>
        </td>
        <c:choose>
          <c:when test="${displayFlag2 == 'false'}">          
            <td align="center">
              <nested:text property="name" size="20" maxlength="80" styleClass="content-field" />        
            </td>        
            <td align="center">
              <nested:text property="description" size="50" maxlength="80" styleClass="content-field"/>        
            </td>
            <td align="center">
              <nested:text property="minimumQualifier" size="10" maxlength="20" styleClass="content-field" />        
            </td>
            <td align="center">
              <nested:text property="incrementalQuantity" size="10" maxlength="20" styleClass="content-field" />        
            </td>
            <td align="center">
               <nested:text property="award" size="10" maxlength="20" styleClass="content-field" />        
            </td>
            <td align="center">
               <nested:text property="maximumPoints" size="10" maxlength="20" styleClass="content-field" />        
            </td>
            <td align="center">
              <table>
                <tr align="center">
                  <td width="15">
                    <c:if test="${goalLevelIndex != 0}">
                      <img src="<%= RequestUtils.getBaseURI(request) %>/assets/g4skin/images/order-arrowup.gif" border="0" onclick="setActionDispatchAndSubmit('promotionPayout.do?oldSequence=<c:out value="${goalLevelIndex+1}"/>&newElementSequenceNum=<c:out value="${goalLevelIndex}"/>', 'reorder')"/>
                    </c:if>
                  </td>
                  <td width="15">
                    <c:if test="${goalLevelIndex != payoutForm.goalLevelValueListSize - 1}">
                      <img src="<%= RequestUtils.getBaseURI(request) %>/assets/g4skin/images/order-arrowdown.gif" border="0" onclick="setActionDispatchAndSubmit('promotionPayout.do?oldSequence=<c:out value="${goalLevelIndex+1}"/>&newElementSequenceNum=<c:out value="${goalLevelIndex+2}"/>', 'reorder')"/>
                    </c:if>
                  </td>        
                </tr>
              </table>
            </td>
          </c:when>
          <c:otherwise>
            <td align="center">
              <nested:write property="name"/>
            </td>        
            <td align="center">
              <nested:write property="description"/>
            </td>
            <td align="center">
              <nested:write property="minimumQualifier"/>
            </td>
            <td align="center">
              <nested:write property="incrementalQuantity"/>
            </td>
            <td align="center">
               <nested:write property="award"/>
            </td>
            <td align="center">
               <nested:write property="maximumPoints"/>
            </td>          
          </c:otherwise>
        </c:choose>
        <c:if test="${displayFlag == 'false'}">          
          <td align="center">
            <c:if test="${(promoPayoutValue.promoPayoutId == null) ||
  			                    (promoPayoutValue.promoPayoutId == '0') ||
  			                    (payoutForm.promotionStatus != 'live')}">
              <nested:checkbox property="removeGoal" value="Y"/>
              <c:set var="hasRemoveableGoal" value="true"/>
            </c:if>        
          </td>
        </c:if>
        </tr>
      </nested:iterate>
    <tr class="form-row-spacer">
      <td colspan="2">
        <c:if test="${payoutForm.goalLevelValueListSize < 6 && displayFlag2 == 'false'}">
          <a href="#" onclick="setActionDispatchAndSubmit('promotionPayout.do', 'addAnother')">
            <cms:contentText code="promotion.payout.goalquest" key="ADD_ANOTHER" />
          </a>
        </c:if>
      </td>
      <td colspan="7" align="right">
        <c:if test="${hasRemoveableGoal && displayFlag == 'false'}">
          <html:button property="remove" styleClass="content-buttonstyle" onclick="setActionDispatchAndSubmit('promotionPayout.do', 'removeGoals')" >
            <cms:contentText code="system.button" key="REMOVE_SELECTED" />
          </html:button>
        </c:if>
      </td>
    </tr>        
    </table>

