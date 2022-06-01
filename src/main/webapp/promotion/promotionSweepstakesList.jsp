<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@page import="com.biperf.core.domain.promotion.Promotion"%>
<%@page import="com.biperf.core.ui.promotion.PromotionWizardManager"%>
<%@page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.value.PromotionSweepstakesListValueBean"%>

<script type="text/javascript">
<!--
	function callUrl( urlToCall )
	{
		window.location=urlToCall;
	}
//-->
</script>

<html:form styleId="contentForm" action="promotionListMaintain">
  <html:hidden property="method" />

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td><span class="headline"><cms:contentText key="HEADER" code="promotion.sweepstakes.list" /></span> <span id="quicklink-add"></span><script
        type="text/javascript">quicklink_display_add('<cms:contentText key="HEADER" code="promotion.sweepstakes.list"/>','<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>'); </script>
      <br />
      <br />

      <span class="content-instruction"> <cms:contentText key="INSTRUCTION" code="promotion.sweepstakes.list" /> </span> <br />
      <br />

      <cms:errors />

      <table width="100%">
        <tr>
          <td align="right">
          <table width="100%">
            <%-- SWEEPSTAKES --%>
            <tr class="form-row-spacer">
              <td colspan="2">
								<%	Map parameterMap = new HashMap();
								PromotionSweepstakesListValueBean temp;
								%>
								<display:table defaultsort="1" defaultorder="ascending" name="promotionList" id="promotionSweepstakesListValueBean" sort="list" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
								<display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
				   				</display:setProperty>
				   				<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
									<%		temp = (PromotionSweepstakesListValueBean)pageContext.getAttribute( "promotionSweepstakesListValueBean" );
											if(temp!=null)
											{
												parameterMap.put( "promotionId", temp.getPromotion().getId() );
												String url = "../" + PromotionWizardManager.getStrutsModulePath( temp.getPromotion().getPromotionType().getCode() ) + "/promotionSweepstakes.do";
												pageContext.setAttribute("reviewUrl", ClientStateUtils.generateEncodedLink( "", url, parameterMap ) );
												pageContext.setAttribute("createUrl", ClientStateUtils.generateEncodedLink( "", "createSweepstakesWinnersList.do", parameterMap ) );
												pageContext.setAttribute("approveUrl", ClientStateUtils.generateEncodedLink( "", "pendingSweepstakesWinnersDisplay.do", parameterMap ) );
												pageContext.setAttribute("historyUrl", ClientStateUtils.generateEncodedLink( "", "sweepstakesWinnerHistoryDisplay.do", parameterMap ) );
											}	
								
									%>
                        <display:column titleKey="promotion.list.PROMO_NAME" headerClass="crud-table-header-row"
                          class="crud-content left-align nowrap" sortable="true">
                          <c:out value="${promotionSweepstakesListValueBean.promotion.name}" />
                        </display:column>

                        <display:column titleKey="promotion.list.PROMO_TYPE" headerClass="crud-table-header-row"
                          class="crud-content left-align nowrap" sortable="true">
                          <c:out value="${promotionSweepstakesListValueBean.promotion.promotionType.name}" />
                        </display:column>

                        <display:column titleKey="promotion.list.ACTIONS" headerClass="crud-table-header-row"
                          class="crud-content left-align nowrap" sortable="false">
                          <table>
                            <tr>
                            <td>
															<a href='<c:out value="${reviewUrl}" />' class="crud-content-link">
																<cms:contentText key="REVIEW_PARAMETERS" code="promotion.sweepstakes.list"/>
															</a>
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
														</td>
                            <td>
															<a href='<c:out value="${createUrl}" />' class="crud-content-link">
																<cms:contentText key="CREATE_WINNERS_LIST" code="promotion.sweepstakes.list"/>
															</a>
                              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
														</td>
                            <td>
															<c:choose>
                                <c:when test="${promotionSweepstakesListValueBean.hasSweepstakesToProcess == true}">
                                  <a href='<c:out value="${approveUrl}" />' class="crud-content-link">
																		<cms:contentText key="APPROVE_WINNERS" code="promotion.sweepstakes.list"/>
																	</a>
                                  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                </c:when>
                                <c:otherwise>
                                  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                </c:otherwise>
                              </c:choose>
														</td>
                            <td>
															<c:choose>
                                <c:when test="${promotionSweepstakesListValueBean.hasSweepstakesHistory == true}">
                                  <a href='<c:out value="${historyUrl}" />' class="crud-content-link">
																		<cms:contentText key="WINNER_HISTORY" code="promotion.sweepstakes.list"/>
																	</a>
                                  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                </c:when>
                                <c:otherwise>
                                  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                </c:otherwise>
                              </c:choose>
														</td>                              
                          </tr>
                        </table>
                      </display:column>
              			</display:table>

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