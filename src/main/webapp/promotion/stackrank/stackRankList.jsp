<%--UI REFACTORED --%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.ui.promotion.stackrank.StackRankFormBean"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

<html:form styleId="contentForm" action="promotionListMaintain">

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="HEADER" code="promotion.stackrank.list"/></span>
        <br/><br/>
        <span class="content-instruction"><cms:contentText key="INSTRUCTION" code="promotion.stackrank.list"/></span>
        <br/><br/>

        <cms:errors/>

        <table width="50%">
          <tr>
            <td>
              <table>
                <tr class="form-row-spacer">
                  <td colspan="2">
										<%	Map parameterMap = new HashMap();
											StackRankFormBean temp = null;
										%>
                    <display:table name="stackRankFormBeanList" id="stackRankBean" sort="list" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
					<display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
				   	</display:setProperty>
				   	<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
                      <display:column titleKey="promotion.stackrank.list.PROMOTION_NAME" headerClass="crud-table-header-row" class="crud-content left-align top-align nowrap">
                        <c:out value="${stackRankBean.promotionName}"/>
                      </display:column>

                      <display:column titleKey="promotion.stackrank.list.ACTIONS" headerClass="crud-table-header-row" class="crud-content left-align top-align nowrap">
                        <table width="100%">
                          <tr>

                            <%-- update parameters --%>
                            <td>
															<%	temp = (StackRankFormBean)pageContext.getAttribute( "stackRankBean" );
																if (temp != null) {	
																	parameterMap.put( "id", temp.getPromotionId() );
																	pageContext.setAttribute("viewUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), "/promotionProductClaim/promotionPayout.do?promotionTypeCode=product_claim&method=display&disableProductSection=true&alternateReturnUrl=" + RequestUtils.getBaseURI(request) + "/promotion/stackRankListDisplay.do", parameterMap ) );
																	parameterMap.remove("id");
																	parameterMap.put( "promotionId", temp.getPromotionId() );																	pageContext.setAttribute("createUrl", ClientStateUtils.generateEncodedLink( "", "displayCreateStackRankForm.do", parameterMap ) );
																	pageContext.setAttribute("approveUrl", ClientStateUtils.generateEncodedLink( "", "stackRankPendingList.do", parameterMap ) );
																	pageContext.setAttribute("historyUrl", ClientStateUtils.generateEncodedLink( "", "stackRankHistory.do?method=display", parameterMap ) );
																}
															%>
                              <a href="<c:out value="${viewUrl}"/>" class="crud-content-link">
                                <cms:contentText key="UPDATE_PARAMETERS" code="promotion.stackrank.list"/>
                              </a>
                            </td>

                            <%-- create stack rank --%>
                            <td>
                              <c:choose>
                                <c:when test="${stackRankBean.displayCreate}">
                                  <a href="<c:out value="${createUrl}"/>" class="crud-content-link">
                                    <cms:contentText key="CREATE_STACK_RANK_LIST" code="promotion.stackrank.list"/>
                                  </a>
                                </c:when>
                                <c:otherwise>
                                  <span style="visibility: hidden">
                                    <cms:contentText key="CREATE_STACK_RANK_LIST" code="promotion.stackrank.list"/>
                                  </span>
                                </c:otherwise>
                              </c:choose>
                            </td>

                            <%-- approve and post stack rank --%>
                            <td>
                              <c:choose>
                                <c:when test="${stackRankBean.displayApprove}">
                                  <a href="<c:out value="${approveUrl}"/>" class="crud-content-link">
                                    <cms:contentText key="APPROVE_AND_POST" code="promotion.stackrank.list"/>
                                  </a>
                                </c:when>
                                <c:otherwise>
                                  <span style="visibility: hidden">
                                    <cms:contentText key="APPROVE_AND_POST" code="promotion.stackrank.list"/>
                                  </span>
                                </c:otherwise>
                              </c:choose>
                            </td>

                            <%-- stack rank history --%>
                            <td>
                              <c:choose>
                                <c:when test="${stackRankBean.displayHistory}">
                                  <a href="<c:out value="${historyUrl}"/>" class="crud-content-link">
                                    <cms:contentText key="RANKING_HISTORY" code="promotion.stackrank.list"/>
                                  </a>
                                </c:when>
                                <c:otherwise>
                                  <span style="visibility: hidden">
                                    <cms:contentText key="RANKING_HISTORY" code="promotion.stackrank.list"/>
                                  </span>
                                </c:otherwise>
                              </c:choose>
                            </td>
                          </tr>
                        </table>
                      </display:column>
                    </display:table>
                  </td>
                </tr>

                <tr class="form-blank-row"><td></td></tr>

                <%--BUTTON ROWS --%>
                <tr class="form-buttonrow">
                  <td>
                    <table width="100%">
                      <tr>
                        <td align="left">
                          <html:submit styleClass="content-buttonstyle" onclick="setActionAndDispatch('expiredStackRankListDisplay.do','')" >
                            <cms:contentText code="promotion.stackrank.list" key="VIEW_EXPIRED_PROMOTIONS" />
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