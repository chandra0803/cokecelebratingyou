<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.ui.promotion.PromotionOverviewForm"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.domain.promotion.Promotion" %>
<%@ include file="/include/taglib.jspf"%>

<%-- PROMOTION FAMILY SECTION --%>
<td  align="right" valign="top">
  <table border="0" bgcolor="d8ded5">
    <tr>
      <td colspan="3" align="left" class="content-bold"><cms:contentText code="promotion.overview" key="PROMOTION_FAMILY"/></td>
    </tr>
    <tr>
      <td>&nbsp;</td>
      <td colspan="2">
        <table>
          <tr>
            <td align="left" valign="top" class="content-field-label"><cms:contentText code="promotion.overview" key="PARENT_PROMO"/></td>
            <td>&nbsp;</td>
            <td align="left" valign="top" class="content-bold">
			  <% Map paramMap = new HashMap(); 
				 PromotionOverviewForm temp = (PromotionOverviewForm) session.getAttribute( "promotionOverviewForm" ); 
			  %>
              <c:choose>
                <c:when test="${empty promotionOverviewForm.parentPromotionId}">
                  <c:out value="${promotionOverviewForm.promotionName}" />
                </c:when>
                <c:otherwise>
				  <%	
				    if (temp != null)
                    {
					  paramMap.put( "promotionId", temp.getParentPromotionId() );
					  pageContext.setAttribute("viewUrl", ClientStateUtils.generateEncodedLink( "", "promotionOverview.do?method=display", paramMap ) );
					}
				  %>
                  <a class="content-link" href="<c:out value="${viewUrl}"/>"><c:out value="${promotionOverviewForm.parentPromotionName}"/></a>
                </c:otherwise>
              </c:choose>
            </td>
          </tr>
          <c:forEach items="${promotionOverviewForm.childPromotions}" var="child">
            <tr>
              <td align="left" valign="top" class="content-field-label"><cms:contentText code="promotion.overview" key="CHILD_PROMO"/></td>
              <td>&nbsp;</td>                    
              <td align="left" valign="top" class="content-bold">
                <c:choose>
                  <c:when test="${ promotionOverviewForm.promotionId == child.id}" >
                    <c:out value="${child.name}"/>
                  </c:when>
                  <c:otherwise>
				    <% Promotion tempPromo = (Promotion)pageContext.getAttribute( "child" );
					  if (tempPromo != null)
					  {
					    paramMap.put( "promotionId", tempPromo.getId() );
					    pageContext.setAttribute("viewUrl", ClientStateUtils.generateEncodedLink( "", "promotionOverview.do?method=display", paramMap ) );
					  }
				    %>
                    <a class="content-link" href="<c:out value="${viewUrl}"/>"><c:out value="${child.name}"/></a>
                  </c:otherwise>
                </c:choose>
              </td>
            </tr>
          </c:forEach>
                  
          <tr class="form-blank-row">
            <td></td>
          </tr>
          		  
          <c:if test="${ (promotionOverviewForm.live) ||
                         (promotionOverviewForm.complete) }">
                 
            <beacon:authorize ifAnyGranted="PROJ_MGR,PROCESS_TEAM,BI_ADMIN">
			  <tr>
				<td colspan="3">        
				  <c:choose>
					<c:when test="${empty promotionOverviewForm.parentPromotionId}">
					  <%
					    if (temp != null)
						{
						  paramMap.put( "promotionId", temp.getPromotionId() );
						  pageContext.setAttribute("createUrl", ClientStateUtils.generateEncodedLink( "", "startChildPromotionWizard.do?method=createChild", paramMap ) );
						}
					  %>
					  <a class="content-buttonstyle-link" href="<c:out value="${createUrl}"/>"><cms:contentText code="promotion.overview" key="ADD_CHILD_PROMO"/></a>
					</c:when>
					<c:otherwise>
					  <%
						if (temp != null)
						{
						  paramMap.put( "promotionId", temp.getParentPromotionId() );
  						  pageContext.setAttribute("addUrl", ClientStateUtils.generateEncodedLink( "", "startChildPromotionWizard.do?method=createChild", paramMap ) );
						}
					  %>
					  <a class="content-buttonstyle-link" href="<c:out value="${addUrl}"/>"><cms:contentText code="promotion.overview" key="ADD_ANOTHER_CHILD_PROMO"/></a>			
					</c:otherwise>
				  </c:choose>
				</td>
			  </tr>
			</beacon:authorize>         
          </c:if>
        </table>
      </td>
    </tr>
  </table>
</td>