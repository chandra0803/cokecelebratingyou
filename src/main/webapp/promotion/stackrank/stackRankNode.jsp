<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

<html:form action="displayStackRankNode" styleId="contentForm">
	<beacon:client-state>
		<beacon:client-state-entry name="stackRankId" value="${stackRankNodeForm.stackRankId}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="TITLE" code="promotion.stackranknode"/></span>
        <br/>
        <span class="subheadline"><c:out value="${stackRank.promotion.name}"/></span>
        <br/><br/>
        <span class="content-instruction"><cms:contentText key="INSTRUCTIONAL_COPY" code="promotion.stackranknode"/></span>
        <br/><br/>

        <cms:errors/>

        <table width="100%" cellpadding="3" cellspacing="1">
          <tr>
            <td valign="top">
              <table cellpadding="3" cellspacing="1">

                <tr class="form-row-spacer">
                  <td></td>
                  <td class="content-field">
                    <cms:contentText key="RANKING_PERIOD" code="promotion.stackranknode"/>
                  </td>
                  <td class="content-field nowrap" colspan="3">
                    <fmt:formatDate value="${stackRank.startDate}" pattern="${JstlDatePattern}"/>
                    &nbsp;
                    <cms:contentText key="THROUGH" code="promotion.stackranknode"/>
                    &nbsp;
                    <fmt:formatDate value="${stackRank.endDate}" pattern="${JstlDatePattern}"/>
                  </td>
                </tr>

                <tr class="form-row-spacer">
                  <td></td>
                  <td class="content-field">
                    <cms:contentText key="NODE_TYPE" code="promotion.stackranknode"/>
                  </td>
                  <td class="content-field-review" colspan="3">
                    <c:if test="${node != null}">
                      <c:out value="${node.nodeType.i18nName}" />
                    </c:if>
                  </td>
                </tr>

                <tr class="form-row-spacer">
                  <beacon:label property="nameOfNode" required="false">
                    <cms:contentText key="NODE_NAME" code="promotion.stackranknode"/>
                  </beacon:label>
                  <td class="crud-content nowrap">
                    <html:select property="nameOfNode" styleClass="content-field"  styleId="nodeId" >
                      <html:options collection="nodeList" property="name" labelProperty="name"/>
                    </html:select>
                    <html:submit styleClass="content-buttonstyle">
                      <cms:contentText key="GO" code="system.button"/>
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

          <c:if test="${stackRankNode != null}">
            <tr>
              <td align="right">
                <display:table name="stackRankNode.stackRankParticipants" id="stackRankParticipant" sort="list" pagesize="20" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
                <display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
				 </display:setProperty>
				 <display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
                  <display:column titleKey="promotion.stackranknode.RANK" headerClass="crud-table-header-row" class="crud-content center-align">
                    <c:out value="${stackRankParticipant.rank}"/>
                  </display:column>

                  <display:column titleKey="promotion.stackranknode.SUBMITTER" headerClass="crud-table-header-row" class="crud-content">
                    <c:out value="${stackRankParticipant.participant.lastName}, ${stackRankParticipant.participant.firstName} - ${stackRankParticipant.participant.positionType} - ${stackRankParticipant.participant.departmentType}"/>
                  </display:column>

                  <display:column titleKey="promotion.stackranknode.RANK_FACTOR" headerClass="crud-table-header-row" class="crud-content center-align">
                    <c:out value="${stackRankParticipant.stackRankFactor}"/>
                  </display:column>

                  <c:if test="${stackRankNode.stackRank.calculatePayout}">
                    <display:column titleKey="promotion.stackranknode.PAYOUT" headerClass="crud-table-header-row" class="crud-content center-align">
                      <c:out value="${stackRankParticipant.payout}"/>
                    </display:column>
                  </c:if>
                </display:table>
              </td>
            </tr>
          </c:if>
        </table>

        <table width="100%" cellpadding="3" cellspacing="1" >
          <tr class="form-buttonrow">
            <td align="center">
              <c:url var="homePageUrl" value="/homePage.do"/>
              <html:button property="homePageButton" styleClass="content-buttonstyle" onclick="callUrl('${homePageUrl}')">
                <cms:contentText key="BACK_TO_HOME" code="promotion.stackranknode"/>
              </html:button>
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
</html:form>