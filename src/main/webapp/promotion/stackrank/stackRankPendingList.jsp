<%--UI REFACTORED--%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.ui.promotion.stackrank.StackRankPendingListForm"%>
<%@ page import="com.biperf.core.value.StackRankValueBean"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ include file="/include/taglib.jspf"%>

<html:form styleId="contentForm" action="stackRankPendingListSave">
  <html:hidden property="method" value=""/>
  <html:hidden property="promotionName"/>
	<beacon:client-state>
		<beacon:client-state-entry name="stackRankId" value="${stackRankPendingListForm.stackRankId}"/>
		<beacon:client-state-entry name="promotionId" value="${stackRankPendingListForm.promotionId}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="TITLE" code="promotion.stack.rank.pending.list"/></span>
        <br/>
        <span class="content-bold"><c:out value="${stackRankPendingListForm.promotionName}"/></span>
        <br/><br/>
        <span class="content-instruction"><cms:contentText key="INSTRUCTION" code="promotion.stack.rank.pending.list"/></span>
        <br/>

        <cms:errors/>

      </td>
    </tr>

    <tr>
      <td>
        <table>
          <tr>
            <td class="content-field">
              <cms:contentText key="RANKING_PERIOD" code="promotion.stack.rank.pending.list"/>
            </td>
            <td class="content-bold" colspan="2">
              <c:out value="${startDate}"/>
              &nbsp;
              <cms:contentText key="THROUGH" code="promotion.stack.rank.pending.list"/>
              &nbsp;
              <c:out value="${endDate}"/>
            </td>
          </tr>

          <tr>
            <td class="content-field">
              <cms:contentText key="RANKING_SUBMITTED" code="promotion.stack.rank.pending.list"/>
            </td>
            <td class="content-bold" colspan="2">
              <c:out value="${rankingSubmitted}"/>
            </td>
          </tr>

          <tr>
            <td colspan="3">
							<%	Map parameterMap = new HashMap();
									StackRankPendingListForm temp;
							%>
              <display:table defaultorder="ascending" name="nodeTypeList" id="stackRank" sort="list">
              <display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
			</display:setProperty>
			<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
                <display:column titleKey="promotion.stack.rank.pending.list.NODE_TYPE" headerClass="crud-table-header-row" class="crud-content left-align">
									<%	temp = (StackRankPendingListForm)request.getAttribute( "stackRankPendingListForm" );
											parameterMap.put( "promotionId", temp.getPromotionId() );
											parameterMap.put( "stackRankId", temp.getStackRankId() );
											StackRankValueBean temp2 = (StackRankValueBean)pageContext.getAttribute( "stackRank" );
											parameterMap.put( "nodeType", temp2.getNodeTypeName() );
											parameterMap.put( "nodeTypeId", temp2.getNodeTypeId() );
											pageContext.setAttribute("linkUrl", ClientStateUtils.generateEncodedLink( "", "../promotion/pendingStackRankingDetails.do", parameterMap ) );
									%>
                  <a href="<c:out value="${linkUrl}"/>" class="crud-content-link">
                    <c:out value="${stackRank.nodeTypeName}"/>
                  </a>
                </display:column>

                <display:column titleKey="promotion.stack.rank.pending.list.TOTAL_NODES" headerClass="crud-table-header-row" class="crud-content center-align">
                  <c:out value="${stackRank.totalNodes}"/>
                </display:column>

                <display:column titleKey="promotion.stack.rank.pending.list.NODES_WITH_PENDING" headerClass="crud-table-header-row" class="crud-content center-align">
                  <c:out value="${stackRank.totalNodesWithRankings}"/>
                </display:column>

              </display:table>
            </td>
          </tr>

          <tr>
            <td class="content-field" colspan="3">
              <html:checkbox property="approved"/>
              <c:choose>
                <c:when test="${pendingStackRank.calculatePayout == 'true'}">
                  <cms:contentText key="APPROVE_RANKINGS_WITH_PAYOUTS" code="promotion.stack.rank.pending.list"/>
                </c:when>
                <c:otherwise>
                  <cms:contentText key="APPROVE_RANKINGS" code="promotion.stack.rank.pending.list"/>
                </c:otherwise>
              </c:choose>
            </td>
          </tr>

          <%--BUTTON ROWS --%>
          <tr class="form-buttonrow">
            <td colspan="3" align="center">
              <table>
                <tr>
                  <td align="left">
                    <html:submit property="add" styleClass="content-buttonstyle" onclick="setDispatch('approveStackRank');">
                      <cms:contentText key="SUBMIT" code="system.button"/>
                    </html:submit>
                  </td>
                  <td align="right">
                    <html:button property="cancel" styleClass="content-buttonstyle" onclick="callUrl('stackRankListDisplay.do')">
                      <cms:contentText key="CANCEL" code="system.button"/>
                    </html:button>
                  </td>
                </tr>
              </table>
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
</html:form>