<%--UI REFACTORED --%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.promotion.Promotion"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.utils.UserManager"%>
<%@ include file="/include/taglib.jspf"%>

<script type="text/javascript">
<!--
function callUrl( urlToCall )
{
  window.location=urlToCall;
}

function filterPromotionList( )
{
  var url = 'promotionListDisplay.do';
  var promotionType = document.promotionListForm.promotionType.value;
  var urlToCall = url + "?promotionType=" + promotionType;

  window.location=urlToCall;
}
//-->
</script>

<html:form styleId="contentForm" action="pendingGoalquestSummaryDisplay">

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="HEADER" code="promotion.list"/></span>
        <span id="quicklink-add"></span><script type="text/javascript">quicklink_display_add('<cms:contentText key="HEADER" code="promotion.list"/>','<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>'); </script>
        <br/><br/>

        <span class="content-instruction">
          <cms:contentText key="INSTRUCTION" code="promotion.list"/>
        </span>
        <br/><br/>

        <cms:errors/>
        
        <table width="50%">
          <tr>
            <td align="right">
            
              <%-- Select the promotion type to filter list by --%>
              <table width="100%">
                <tr class="form-row-spacer">
                  <td colspan="2" class="content-field" align="left">
                    <cms:contentText code="promotion.list" key="TYPE_TO_SHOW"/>

                    <html:select property="promotionType" styleClass="content-field" >
                      <html:option value='all'><cms:contentText key="SHOW_ALL_TYPES" code="promotion.list"/></html:option>
                      <html:options collection="promotionTypeList" property="code" labelProperty="name"  />
                    </html:select>
                    <html:button property="selectPromotionTypeFilter" styleClass="content-buttonstyle" onclick="javascript:filterPromotionList();">
                      <cms:contentText code="promotion.list" key="GO"/>
                    </html:button>
                  </td>
               </tr>
            </table>
            
            <%-- UNDER CONSTRUCTION PROMOTIONS --%>
            <table>
              <tr class="form-row-spacer">
                <td colspan="2" class="subheadline" align="left"><cms:contentText code="promotion.list" key="UNDER_CONSTRUCTION"/></td>
              </tr>
              <beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
                <tr class="form-row-spacer">
                  <td align="left">
                    <html:button property="addPromotion" styleClass="content-buttonstyle" onclick="callUrl('startPromotionWizard.do')">
                      <cms:contentText code="promotion.list" key="ADD_PROMO"/>
                    </html:button>
                  </td>
                </tr>
              </beacon:authorize>
              <tr class="form-row-spacer">
                <td colspan="2">
                <c:set var="index" value="1"/>
									<%	Map parameterMap = new HashMap();
											Promotion temp;
									%>
                  <display:table name="underConstructionSet" id="underConstruction" sort="list" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
                  <display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
				   				</display:setProperty>
				   <display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
										<%	temp = (Promotion)pageContext.getAttribute( "underConstruction" );
										    if (temp != null) {
												parameterMap.put( "promotionId", temp.getId() );
												pageContext.setAttribute("viewUrl", ClientStateUtils.generateEncodedLink("", "promotionOverview.do?method=display", parameterMap ) );
										    }
										%>
                    <c:set var="index" value="${index + 1}"/>
                    <c:choose>
                      <%-- save the id if it's a parent promotion --%>
                      <c:when test="${underConstruction.promotionType.code == 'product_claim' && underConstruction.childrenCount > 0}" >
                        <c:set var='savedPromotionId' value="${underConstruction.id}"/>
                      </c:when>
                    </c:choose>

                    <display:column titleKey="promotion.list.PROMO_NAME" headerClass="crud-table-header-row" class="crud-content left-align top-align nowrap" sortable="true" sortProperty="name">
                      <c:choose>
                         <%-- Only indent if the child promotion is right underneath its parent on the list --%>
                        <c:when test='${underConstruction.promotionType.code == "product_claim" and underConstruction.parentPromotion.id > 0 and underConstruction.parentPromotion.id == savedPromotionId}'>
                          &nbsp;&nbsp;&nbsp;
                          <a href="<c:out value="${viewUrl}"/>" class="crud-content-link"><c:out value="${underConstruction.name}"/></a>
                        </c:when>
                        <c:otherwise>
                          <a href="<c:out value="${viewUrl}"/>" class="crud-content-link"><c:out value="${underConstruction.name}"/></a>
                        </c:otherwise>
                      </c:choose>
                    </display:column>

                    <display:column titleKey="promotion.list.PROMO_FAMILY" headerClass="crud-table-header-row" class="crud-content left-align top-align nowrap">
                      <c:choose>
                        <%-- If Recognition Promotion then children are not applicable --%>
                        <c:when test="${underConstruction.promotionType.code == 'product_claim'}">
                          <c:choose>
                            <c:when test="${empty underConstruction.parentPromotion.id }" >
                              <cms:contentText code="promotion.list" key="PARENT"/> -
                              <c:out value="${underConstruction.childrenCount}"/>
                              <c:choose>
                                <c:when test="${underConstruction.childrenCount != 1}" >
                                  <cms:contentText code="promotion.list" key="CHILDREN"/>
                                </c:when>
                                <c:otherwise>
                                  <cms:contentText code="promotion.list" key="CHILD"/>
                                </c:otherwise>
                              </c:choose>
                            </c:when>
                            <c:otherwise>
                              <cms:contentText code="promotion.list" key="CHILD_OF"/> <c:out value="${underConstruction.parentPromotion.name}"/>
                            </c:otherwise>
                          </c:choose>
                        </c:when>
                        <c:otherwise>
                          <cms:contentText code="promotion.list" key="FAMILY_NA"/>
                        </c:otherwise>
                      </c:choose>
                    </display:column>

                    <display:column titleKey="promotion.list.PROMO_TYPE" headerClass="crud-table-header-row" class="crud-content left-align top-align nowrap" sortable="true">
                      <c:out value="${underConstruction.promotionType.name}"/>
                    </display:column>

                    <display:column titleKey="promotion.list.PROMO_FORM" headerClass="crud-table-header-row" class="crud-content left-align top-align" sortable="true">
                      <c:choose>
                        <c:when test="${underConstruction.promotionType.code == 'quiz'}">
                          <c:out value="${underConstruction.quiz.name}"/>
                        </c:when>
                        <c:when test="${underConstruction.promotionType.code == 'survey' or underConstruction.promotionType.code == 'goalquest'}">
                          <cms:contentText code="promotion.list" key="FAMILY_NA"/>
                        </c:when>
                        <c:otherwise>
                          <c:out value="${underConstruction.claimForm.name}"/>
                        </c:otherwise>
                      </c:choose>
                    </display:column>

                    <display:column titleKey="promotion.list.PROMO_LAST_UPDATED" headerClass="crud-table-header-row" class="crud-content right-align top-align nowrap" sortable="true">
                      <c:out value="${underConstruction.displayLastUpdatedDate}"/>
                    </display:column>

                    <beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
                       <display:column class="center-align" titleKey="system.general.CRUD_REMOVE_LABEL" headerClass="crud-table-header-row">
                        <input type="checkbox" name="deleteUnderConstructionPromos" value="<c:out value="${underConstruction.id}"/>">
                      </display:column>
                    </beacon:authorize>
                  </display:table>
                </td>
              </tr>

              <beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
                <tr class="form-row-spacer">
                  <td align="left">
                    <c:choose>
                       <c:when test="${index > 15}" >
                            <html:button property="addPromotion" styleClass="content-buttonstyle" onclick="callUrl('startPromotionWizard.do')">
		                      <cms:contentText code="promotion.list" key="ADD_PROMO"/>
		                    </html:button>
                       </c:when>
                       <c:otherwise>
                         &nbsp;
                       </c:otherwise>
                     </c:choose>               
                  </td>
                  <td align="right">
                    <html:submit styleClass="content-buttonstyle" onclick="setDispatch('deleteUnderConstructionPromo')">
                      <cms:contentText key="REMOVE_SELECTED" code="system.button"/>
                    </html:submit>
                  </td>
                </tr>
              </beacon:authorize>

              <tr class="form-blank-row">
                <td></td>
              </tr>

              <%-- COMPLETE PROMOTIONS --%>
              <tr class="form-row-spacer">
                <td colspan="2" class="subheadline" align="left"><cms:contentText code="promotion.list" key="COMPLETE_LABEL"/></td>
              </tr>
			  <c:if test="${not empty completeSet}">
              <tr class="form-row-spacer">
                <td colspan="2">
									<%	Promotion temp2; %>
                  <display:table name="completeSet" id="complete" sort="list" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
                  <display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
				   				</display:setProperty>
						<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>				   				
										<%	
											    temp2 = (Promotion)pageContext.getAttribute( "complete" );
												if (temp2 != null) {
												  parameterMap.remove( "promotionId" );
												  parameterMap.put( "promotionId", temp2.getId() );
												  pageContext.setAttribute("viewCompUrl", ClientStateUtils.generateEncodedLink( "", "promotionOverview.do?method=display", parameterMap ) );
												}
										%>
                    <c:choose>
                      <%-- save the id if it's a parent promotion --%>
                      <c:when test="${complete.promotionType.code == 'product_claim' and complete.childrenCount > 0}" >
                        <c:set var='savedPromotionId' value="${complete.id}"/>
                      </c:when>
                    </c:choose>

                    <display:column titleKey="promotion.list.PROMO_NAME" headerClass="crud-table-header-row" class="crud-content left-align top-align nowrap" sortable="true" sortProperty="name">
                      <c:choose>
                        <%-- Only indent if the child promotion is right underneath its parent on the list --%>
                        <c:when test='${complete.promotionType.code == "product_claim" and complete.parentPromotion.id > 0 and complete.parentPromotion.id == savedPromotionId}'>
                          &nbsp;&nbsp;&nbsp;
                          <a href="<c:out value="${viewCompUrl}"/>" class="crud-content-link"><c:out value="${complete.name}"/></a>
                        </c:when>
                        <c:otherwise>
                          <a href="<c:out value="${viewCompUrl}"/>" class="crud-content-link"><c:out value="${complete.name}"/></a>
                        </c:otherwise>
                      </c:choose>
                    </display:column>

                    <display:column titleKey="promotion.list.PROMO_FAMILY" headerClass="crud-table-header-row" class="crud-content left-align top-align nowrap">
                      <c:choose>
                        <%-- If Recognition Promotion then children are not applicable --%>
                        <c:when test="${complete.promotionType.code == 'product_claim'}">
                          <c:choose>
                            <c:when test="${ empty complete.parentPromotion.id }" >
                              <cms:contentText code="promotion.list" key="PARENT"/> -
                              <c:out value="${complete.childrenCount}"/>
                              <c:choose>
                                <c:when test="${complete.childrenCount != 1}" >
                                  <cms:contentText code="promotion.list" key="CHILDREN"/>
                                </c:when>
                                <c:otherwise>
                                  <cms:contentText code="promotion.list" key="CHILD"/>
                                </c:otherwise>
                              </c:choose>
															<%	pageContext.setAttribute("compChildUrl", ClientStateUtils.generateEncodedLink( "", "startChildPromotionWizard.do?method=createChild", parameterMap ) ); %>
                              &nbsp;&nbsp;
															<a href='<c:out value="${compChildUrl}"/>' class="crud-content-link">
																<cms:contentText code="promotion.list" key="ADD_CHILD"/>
															</a>
                            </c:when>
                          <c:otherwise>
                            <cms:contentText code="promotion.list" key="CHILD_OF"/> <c:out value="${complete.parentPromotion.name}"/>
                          </c:otherwise>
                        </c:choose>
                       </c:when>
                       <c:otherwise>
                         <cms:contentText code="promotion.list" key="FAMILY_NA"/>
                       </c:otherwise>
                     </c:choose>
                    </display:column>

                    <display:column titleKey="promotion.list.PROMO_TYPE" headerClass="crud-table-header-row" class="crud-content left-align top-align nowrap" sortable="true">
                      <c:out value="${complete.promotionType.name}"/>
                    </display:column>

                    <display:column titleKey="promotion.list.PROMO_FORM" headerClass="crud-table-header-row" class="crud-content left-align top-align" sortable="true">
                      <c:choose>
                        <c:when test="${complete.promotionType.code == 'quiz'}">
                          <c:out value="${complete.quiz.name}"/>
                        </c:when>
                        <c:when test="${complete.promotionType.code == 'survey' or complete.promotionType.code == 'goalquest'}">
                          <cms:contentText code="promotion.list" key="FAMILY_NA"/>
                        </c:when>
                        <c:otherwise>
                          <c:out value="${complete.claimForm.name}"/>
                        </c:otherwise>
                      </c:choose>
                    </display:column>

                    <display:column titleKey="promotion.list.PROMO_LAST_UPDATED" headerClass="crud-table-header-row" class="crud-content right-align top-align nowrap" sortable="true">
                      <c:out value="${complete.displayLastUpdatedDate}"/>
                    </display:column>

                    <beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
                      <display:column titleKey="system.general.CRUD_REMOVE_LABEL" headerClass="crud-table-header-row" class="crud-content center-align top-align nowrap">
                        <input type="checkbox" name="deleteCompletePromos" value="<c:out value="${complete.id}"/>">
                      </display:column>
                    </beacon:authorize>
                  </display:table>
                </td>
              </tr>
              </c:if>

              <beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
                <tr class="form-row-spacer">
                  <td>&nbsp;</td>
                  <td align="right">
                    <html:submit styleClass="content-buttonstyle" onclick="setDispatch('deleteCompletePromo')">
                      <cms:contentText key="REMOVE_SELECTED" code="system.button"/>
                    </html:submit>
                  </td>
                </tr>
              </beacon:authorize>

              <tr class="form-blank-row">
                <td></td>
              </tr>

              <%-- LIVE PROMOTIONS --%>
              <tr class="form-row-spacer">
                <td colspan="2" class="subheadline" align="left"><cms:contentText code="promotion.list" key="LIVE_LABEL"/></td>
              </tr>

              <tr class="form-row-spacer">
                <td colspan="2">
									<%	Promotion temp3; %>
                  <display:table name="liveSet" id="live" sort="list" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
                  <display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
				   				</display:setProperty>
							<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>				   				
										<%	temp3 = (Promotion)pageContext.getAttribute( "live" );
										    if (temp3 != null) {
												parameterMap.remove( "promotionId" );
												parameterMap.put( "promotionId", temp3.getId() );
												pageContext.setAttribute("viewLiveUrl", ClientStateUtils.generateEncodedLink( "", "promotionOverview.do?method=display", parameterMap ) );
										    }
										%>
              <c:choose>
                <%-- save the id if it's a parent promotion --%>
            <c:when test="${live.promotionType.code == 'product_claim' and live.childrenCount > 0}" >
              <c:set var='savedPromotionId' value="${live.id}"/>
            </c:when>
          </c:choose>
         <display:column titleKey="promotion.list.PROMO_NAME" headerClass="crud-table-header-row" class="crud-content left-align top-align nowrap" sortable="true" sortProperty="name">
               <c:choose>
                 <%-- Only indent if the child promotion is right underneath its parent on the list --%>
                 <c:when test='${live.promotionType.code == "product_claim" and live.parentPromotion.id > 0 and live.parentPromotion.id == savedPromotionId}'>
                   &nbsp;&nbsp;&nbsp;
                   <a href="<c:out value="${viewLiveUrl}"/>" class="crud-content-link"><c:out value="${live.name}"/></a>
                 </c:when>
            <c:otherwise>
              <a href="<c:out value="${viewLiveUrl}"/>" class="crud-content-link"><c:out value="${live.name}"/></a>
            </c:otherwise>
          </c:choose>
              </display:column>
              <display:column titleKey="promotion.list.PROMO_FAMILY" headerClass="crud-table-header-row" class="crud-content left-align top-align nowrap">
            <c:choose>
           <%-- If Recognition Promotion then children are not applicable --%>
             <c:when test="${live.promotionType.code == 'product_claim'}">
               <c:choose>
                      <c:when test="${ empty live.parentPromotion.id }" >
                        <cms:contentText code="promotion.list" key="PARENT"/> -
                        <c:out value="${live.childrenCount}"/>
                        <c:choose>
                          <c:when test="${live.childrenCount != 1}" >
                            <cms:contentText code="promotion.list" key="CHILDREN"/>
                          </c:when>
                          <c:otherwise>
                            <cms:contentText code="promotion.list" key="CHILD"/>
                          </c:otherwise>
                        </c:choose>
												<%	pageContext.setAttribute("liveChildUrl", ClientStateUtils.generateEncodedLink( "", "startChildPromotionWizard.do?method=createChild", parameterMap ) ); %>
                        &nbsp;&nbsp;
												<a href='<c:out value="${liveChildUrl}"/>' class="crud-content-link">
													<cms:contentText code="promotion.list" key="ADD_CHILD"/>
												</a>
                      </c:when>
                      <c:otherwise>
                        <cms:contentText code="promotion.list" key="CHILD_OF"/> <c:out value="${live.parentPromotion.name}"/>
                      </c:otherwise>
                  </c:choose>
             </c:when>
             <c:otherwise>
               <cms:contentText code="promotion.list" key="FAMILY_NA"/>
             </c:otherwise>
           </c:choose>

              </display:column>

              <display:column titleKey="promotion.list.PROMO_TYPE" headerClass="crud-table-header-row" class="crud-content left-align top-align nowrap" sortable="true">
                <c:out value="${live.promotionType.name}"/>
              </display:column>
              <display:column titleKey="promotion.list.PROMO_FORM" headerClass="crud-table-header-row" class="crud-content left-align top-align" sortable="true">
                <c:choose>
                <c:when test="${live.promotionType.code == 'quiz'}">
                  <c:out value="${live.quiz.name}"/>
                </c:when>
                <c:when test="${live.promotionType.code == 'survey' or live.promotionType.code == 'goalquest'}">
                  <cms:contentText code="promotion.list" key="FAMILY_NA"/>
                </c:when>
                <c:otherwise>
                  <c:out value="${live.claimForm.name}"/>
                </c:otherwise>
              </c:choose>
              </display:column>
              <display:column titleKey="promotion.list.LIVE_DATE" headerClass="crud-table-header-row" class="crud-content right-align top-align nowrap" sortable="true">
                <c:out value="${live.displayLiveDate}"/>
              </display:column>
            </display:table>
          </td>
        </tr>
         </table>  
        </td>
      </tr>
             <%--BUTTON ROWS --%>
          <tr class="form-buttonrow">
            <td>
              <table width="100%">
                <tr>
                  <td align="left">
                    <html:button property="view_list" styleClass="content-buttonstyle" onclick="callUrl('expiredPromoListDisplay.do')" >
                    <cms:contentText code="promotion.list" key="VIEW_LIST" />
                </html:button>
                  </td>
                </tr>
    </table>
  </html:form>  