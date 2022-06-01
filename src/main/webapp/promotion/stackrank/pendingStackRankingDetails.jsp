<%--UI REFACTORED--%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.ui.promotion.stackrank.PendingStackRankingDetailsForm"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<html:form styleId="contentForm" action="pendingStackRankingDetails">
  <html:hidden property="method"/>
  <html:hidden property="promotionName"/>
  <html:hidden property="nodeType"/>
  <html:hidden property="rankingPeriodFrom"/>
  <html:hidden property="rankingPeriodTo"/>
  <html:hidden property="preNodeName"/>
	<beacon:client-state>
		<beacon:client-state-entry name="stackRankId" value="${pendingStackRankForm.stackRankId}"/>
		<beacon:client-state-entry name="nodeTypeId" value="${pendingStackRankForm.nodeTypeId}"/>
		<beacon:client-state-entry name="promotionId" value="${pendingStackRankForm.promotionId}"/>
		<beacon:client-state-entry name="nodeId" value="${pendingStackRankForm.nodeId}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="HEADER" code="promotion.pending.stackranking.node.details"/></span>        
        <br/>
        <span class="subheadline"><c:out value="${pendingStackRankForm.promotionName}"/></span>
        <br/><br/>
        <span class="content-instruction"><cms:contentText key="INSTRUCTION" code="promotion.pending.stackranking.node.details"/></span>
        <br/><br/>

        <cms:errors/>
        
        <table width="100%" cellpadding="3" cellspacing="1">
          <tr>
            <td valign="top">
              <table cellpadding="3" cellspacing="1">
                <tr class="form-row-spacer">
                  <td class="content-field">
                    <cms:contentText key="RANKING_PERIOD" code="promotion.pending.stackranking.node.details"/>
                  </td>
                  <td class="content-field" nowrap>
                    <c:out value="${pendingStackRankForm.rankingPeriodFrom}" />
                    &nbsp;
                    <cms:contentText key="THROUGH" code="promotion.pending.stackranking.node.details"/>
                    &nbsp;
                    <c:out value="${pendingStackRankForm.rankingPeriodTo}" />
                  </td>
                  <td></td>
                  <td></td>
                </tr>

                <tr class="form-row-spacer">
                  <td class="content-field">
                    <cms:contentText key="NODE_TYPE" code="promotion.pending.stackranking.node.details"/>
                  </td>
                  <td class="content-field-review">
                    <c:out value="${pendingStackRankForm.nodeType}" />
                  </td>
                  <td></td>
                  <td></td>
                </tr>

                <tr class="form-row-spacer">
                  <td class="content-field" align="left">
                    <cms:contentText key="NODE_NAME" code="promotion.pending.stackranking.node.details"/>
                  </td>
                  <td class="content-field" align="left" nowrap>
                    <html:select property="nodeName"   styleId="nodeId" >
                      <html:options collection="nodeList" property="name" labelProperty="name"/>
                    </html:select>
                    <html:submit styleClass="content-buttonstyle" onclick="setDispatch('showDetails')">
                      <cms:contentText code="system.button" key="GO" />
                    </html:submit>
                  </td>
                  <td align="center">
                  </td>
                  <td class="content-field">

                  </td>
                </tr>
              </table>
            </td>
          </tr>

          <%--  display table --%>
          <c:if test="${participantsList != null }">
            <tr>
              <td align="right">
                <display:table name="participantsList" id="stackRankParticipant" sort="list" pagesize="20" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
				<display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
				  </display:setProperty>
				  <display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
                  <display:column titleKey="promotion.pending.stackranking.node.details.RANK" headerClass="crud-table-header-row" class="crud-content center-align">
                    <c:out value="${stackRankParticipant.rank}"/>
                  </display:column>

                  <display:column titleKey="promotion.pending.stackranking.node.details.SUBMITTER" headerClass="crud-table-header-row" class="crud-content center-align">
                    <c:out value="${stackRankParticipant.participant.lastName}, ${stackRankParticipant.participant.firstName} - ${stackRankParticipant.participant.positionType} - ${stackRankParticipant.participant.departmentType}"/>
                  </display:column>

                  <display:column titleKey="promotion.pending.stackranking.node.details.RANK_FACTOR" headerClass="crud-table-header-row" class="crud-content center-align">
                    <c:out value="${stackRankParticipant.stackRankFactor}"/>
                  </display:column>

                  <c:if test="${hasPayout}">
                    <display:column titleKey="promotion.pending.stackranking.node.details.PAYOUT" headerClass="crud-table-header-row" class="crud-content center-align">
                      <c:out value="${stackRankParticipant.payout}"/>
                    </display:column>
                  </c:if>
                </display:table>
              </td>
            </tr>
          </c:if>
          <%--  display table --%>
        </table>

        <table width="100%" cellpadding="3" cellspacing="1" >
          <tr class="form-buttonrow">
            <td align="center">
							<%	Map parameterMap = new HashMap();
									PendingStackRankingDetailsForm temp = (PendingStackRankingDetailsForm)request.getAttribute( "pendingStackRankForm" );
									parameterMap.put( "promotionId", temp.getPromotionId() );
									pageContext.setAttribute("viewUrl", ClientStateUtils.generateEncodedLink( "", "stackRankPendingList.do", parameterMap ) );
							%>
              <html:button property="cancel" styleClass="content-buttonstyle" onclick="callUrl('${viewUrl}')">
                <cms:contentText code="promotion.pending.stackranking.node.details" key="BACK_TO_PEND_STACK_RANK_LIST" />
              </html:button>
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
</html:form>