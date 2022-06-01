<%--UI REFACTORED--%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.promotion.Promotion"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ include file="/include/taglib.jspf"%>
<script type="text/javascript">
<!--

//-->
</script>
<html:form styleId="contentForm" action="claimFormPromotionList">
  <html:hidden property="method" />
	<beacon:client-state>
	 	<beacon:client-state-entry name="claimFormId" value="${claimFormForm.claimFormId}"/>
	</beacon:client-state>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="TITLE" code="claims.form.promotionList"/></span>
        <span id="quicklink-add"></span><script type="text/javascript">quicklink_display_add('<cms:contentText key="TITLE" code="claims.form.promotionList"/>','<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>'); </script>

        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="INSTRUCTIONAL_COPY" code="claims.form.promotionList"/>
        </span>
        <br/><br/>
        <%--END INSTRUCTIONS--%>

        <cms:errors/>

        <table width="50%">                                
          <tr>
            <td>
              <span class="subheadline"><cms:contentText key="PROMOTIONS" code="claims.form.promotionList"/></span>
            </td>
          </tr>          
          <tr>
            <td align="right">
							<%  Map paramMap = new HashMap();
									Promotion temp;
							%>
              <display:table name="assignedPromotionList" id="live" sort="list" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>" pagesize="10" defaultsort="1" defaultorder="ascending">
              	<display:setProperty name="basic.msg.empty_list_row">
					<tr class="crud-content" align="left"><td colspan="{0}"><cms:contentText key="NOTHING_FOUND" code="system.errors"/></td></tr>
				</display:setProperty>
								<%  temp = (Promotion)pageContext.getAttribute("live");
										if (null != temp)
										{
											paramMap.put( "promotionId", temp.getId() );
										}
										pageContext.setAttribute("viewUrl", ClientStateUtils.generateEncodedLink( "", "../promotion/promotionOverview.do?method=display", paramMap ) );
								%>
              	<c:choose>
                	<%-- save the id if it's a parent promotion --%>
            		<c:when test="${live.promotionType.code == 'product_claim' and live.childrenCount > 0}" >
              			<c:set var='savedPromotionId' value="${live.id}"/>
            		</c:when>
          		</c:choose>
         	  	<display:column titleKey="promotion.list.PROMO_NAME" headerClass="crud-table-header-row" class="crud-content left-align top-align nowrap" sortable="true">
               		<c:choose>
                 		<%-- Only indent if the child promotion is right underneath its parent on the list --%>
                 		<c:when test='${live.promotionType.code == "product_claim" and live.parentPromotion.id > 0 and live.parentPromotion.id == savedPromotionId}'>
                   			&nbsp;&nbsp;&nbsp;
                   			<a href="<c:out value="${viewUrl}"/>" class="crud-content-link"><c:out value="${live.name}"/></a>
                 		</c:when>
              			<c:otherwise>
              				<a href="<c:out value="${viewUrl}"/>" class="crud-content-link"><c:out value="${live.name}"/></a>
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
              <display:column titleKey="promotion.list.PROMO_FORM" headerClass="crud-table-header-row" class="crud-content left-align top-align nowrap" sortable="true">
                <c:choose>
                <c:when test="${live.promotionType.code == 'quiz'}">
                  <c:out value="${live.quiz.name}"/>
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
         <%--BUTTON ROW --%>
          <tr class="form-buttonrow">
            <td  align="center" colspan="3">
              <html:button property="removeSelected" styleClass="content-buttonstyle" onclick="setActionAndSubmit('claimFormList.do')">
                <cms:contentText code="claims.form.details" key="BACK_BTN" />
              </html:button>
              <beacon:authorize ifNotGranted="LOGIN_AS">
              &nbsp;&nbsp;
              </beacon:authorize>
             </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
</html:form>
