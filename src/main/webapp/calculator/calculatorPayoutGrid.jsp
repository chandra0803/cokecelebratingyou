<%@ include file="/include/taglib.jspf"%>
<table width="100%">
  <tr>
    <td>
      <display:table name="calculator.calculatorPayouts" id="payout">
        <display:setProperty name="basic.msg.empty_list">
          <tr class="crud-content" align="left">
	        <td colspan="{0}">
              <cms:contentText key="NO_PAYOUT_ELEMENTS" code="calculator.criterion"/>
            </td>
          </tr>
        </display:setProperty>
        <display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
        <display:column titleKey="calculator.payouts.SCORE" headerClass="crud-table-header-row center-align" class="crud-content center-align" sortable="false">
          <c:out value="${payout.lowScore}"/> - <c:out value="${payout.highScore}"/>
        </display:column>
        <display:column titleKey="calculator.payouts.PAYOUT" headerClass="crud-table-header-row center-align" class="crud-content center-align" sortable="false">
          <c:if test="${calculator.calculatorAwardType.merchLevelAward}">
            <cms:contentText code="promotion.awards" key="LEVEL_PREFIX" /> <c:out value="${ payout_rowNum }" />
          </c:if>
          <c:if test="${calculator.calculatorAwardType.merchLevelAward == 'false'}">
          <c:out value="${payout.lowAward}"/>
          <c:if test="${calculator.calculatorAwardType.rangeAward == 'true'}">
            - <c:out value="${payout.highAward}"/>
          </c:if>
          </c:if>
        </display:column>
      </display:table>
    </td>
  </tr>
  <tr><td>&nbsp;</td></tr>
  <tr><td class="center-align"><c:out value="${calculator.description}"/></td></tr>
  <tr><td>&nbsp;</td></tr>
  <tr>
    <td align="center">
      <input type="button" class="content-buttonstyle" 
        onclick="window.close();" value="<cms:contentText code="system.button" key="CLOSE_WINDOW" />"/> 
	</td>
  </tr>
</table>