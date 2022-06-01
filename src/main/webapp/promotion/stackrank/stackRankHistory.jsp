<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<html:form styleId="contentForm" action="stackRankHistory">
  <html:hidden property="method"/>
	<beacon:client-state>
		<beacon:client-state-entry name="promotionId" value="${stackRankHistoryForm.promotionId}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="TITLE" code="promotion.stackrank.history"/></span>        
        <br/><br/>

        <span class="content-instruction">
          <cms:contentText key="INSTRUCTIONAL_COPY" code="promotion.stackrank.history"/>
        </span>
        <br/><br/>

        <cms:errors/>
        
        <table width="100%">
          <tr>
            <td>
              <table>
                <tr class="form-row-spacer">
                  <td class="content-field-label">
                    <cms:contentText key="RANKING_PERIOD" code="promotion.stackrank.history"/>
                  </td>
                  <td class="content-field">
                    <html:select property="stackRankId" styleClass="content-field" onchange="setDispatchAndSubmit('onChangeStackRank')">
                      <html:options collection="stackRankList" property="id" labelProperty="rankingPeriodWithDateSubmitted" />
                    </html:select>
                  </td>
                </tr>
                <tr class="form-row-spacer">
                  <td class="content-field-label">
                    <cms:contentText key="NODE_TYPE" code="promotion.stackrank.history"/>
                  </td>
                  <td class="content-field">
                    <html:select property="nodeTypeId" styleClass="content-field" onchange="setDispatchAndSubmit('onChangeNodeType')">
                      <html:option value=''><cms:contentText key="SELECT_ONE" code="system.general"/></html:option>
                      <html:options collection="nodeTypeList" property="id" labelProperty="nodeTypeName" />
                    </html:select>
                  </td>
                </tr>
                <tr class="form-row-spacer">
                  <td class="content-field-label">
                    <cms:contentText key="NODE_NAME" code="promotion.stackrank.history"/>
                  </td>
                  <td class="content-field">
                    <html:select property="nodeId" styleClass="content-field">
                      <html:option value=''><cms:contentText key="SELECT_ONE" code="system.general"/></html:option>
                      <html:options collection="nodeList" property="id" labelProperty="name" />
                    </html:select>
                    <html:button styleClass="content-buttonstyle" onclick="setDispatchAndSubmit('display')" property="showRankings" >
                      <cms:contentText code="promotion.stackrank.history" key="SHOW_RANKINGS" />
                    </html:button>
                  </td>
                </tr>
              </table>
              <table width="70%">
                <tr class="form-row-spacer">
                  <td colspan="2">
                    <display:table name="stackRankParticipantList" id="stackRankParticipant" pagesize="20" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
                    <display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
				   	</display:setProperty>
				   	<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
                      <display:column titleKey="promotion.stackrank.history.RANK" headerClass="crud-table-header-row" class="crud-content center-align top-align nowrap">
                        <c:out value="${stackRankParticipant.rank}"/><c:if test="${stackRankParticipant.tied}">&nbsp;(<cms:contentText code="promotion.stackrank.history" key="TIE" />)</c:if>
                      </display:column>
                      <display:column titleKey="promotion.stackrank.history.SUBMITTER" headerClass="crud-table-header-row" class="crud-content left-align nowrap content-field-label-top" >
                        <c:out value="${stackRankParticipant.participant.nameLFMWithComma}"/>
                      </display:column>
                      <display:column titleKey="promotion.stackrank.history.RANK_FACTOR" property="stackRankFactor" headerClass="crud-table-header-row" class="crud-content center-align top-align nowrap" />
                      <display:column titleKey="promotion.stackrank.history.PAYOUT" property="payout" headerClass="crud-table-header-row" class="crud-content center-align top-align nowrap" />
                    </display:table>
                  </td>
                 </tr>

                  <tr class="form-blank-row">
                    <td colspan="2"></td>
                  </tr>

                <%--BUTTON ROWS --%>
                <tr class="form-buttonrow">
                  <td colspan="2">
                    <table width="100%">
                      <tr>
                        <td align="center">
                          <html:submit styleClass="content-buttonstyle" onclick="setActionAndDispatch('stackRankListDisplay.do','')" >
                            <cms:contentText code="promotion.stackrank.history" key="BACK_TO_STACK_RANK_LIST" />
                        </html:submit>
                        </td>
                      </tr>
                    </table>
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
