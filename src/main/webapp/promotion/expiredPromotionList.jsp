<%--UI REFACTORED--%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.domain.promotion.Promotion"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>
<script type="text/javascript">
<!--
function callUrl( urlToCall )
{
	window.location=urlToCall;
}

function filterPromotionList( )
{
  var url = 'expiredPromoListDisplay.do';
  var promotionType = document.promotionListForm.promotionType.value;
  var urlToCall = url + "?promotionType=" + promotionType;

  window.location=urlToCall;
}
//-->
</script>
<html:form styleId="contentForm" action="expiredPromoListMaintain">
<html:hidden property="method"/>

<table border="0" cellpadding="10" cellspacing="0" width="100%">
 <tr>
      <td>
        <span class="headline"><cms:contentText key="EXPIRED_HEADER" code="promotion.list"/></span>
         <br/><br/>

        <span class="content-instruction">
          <cms:contentText key="INSTRUCTION" code="promotion.list"/>
        </span>
        <br/><br/>

        <cms:errors/>
				
				<%-- Select the promotion type to filter list by --%>
				<table width="90%">
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
		    <table width="90%">
		          <tr>
		            <td align="right">
								<%	Map paramMap = new HashMap();
										Promotion temp;
								%>
		            <display:table defaultsort="1" defaultorder="ascending" name="expiredList" id="expired" pagesize="20" sort="list" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
		            <display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
				   	</display:setProperty>
				   	<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
									<%	temp = (Promotion)pageContext.getAttribute("expired");
									    if(temp!=null){
											paramMap.put( "promotionId", temp.getId() );
											pageContext.setAttribute("viewUrl", ClientStateUtils.generateEncodedLink( "", "promotionOverview.do?method=display", paramMap ) );
										}
									%>
				        <display:column titleKey="promotion.list.PROMO_NAME" headerClass="crud-table-header-row" class="crud-content left-align top-align nowrap">
				          <c:if test="${expired.promotionType.code == 'product_claim' && ! empty expired.parentPromotion.id }" >
				            &nbsp;&nbsp;&nbsp;&nbsp;
				          </c:if>
				          <a href="<c:out value="${viewUrl}"/>" class="crud-content-link"><c:out value="${expired.name}"/></a>
				        </display:column>
				
				        <display:column titleKey="promotion.list.PROMO_FAMILY" headerClass="crud-table-header-row" class="crud-content left-align nowrap">
				 			<c:choose>
				 				<%-- If Recognition Promotion then children are not applicable --%>
				     			<c:when test="${expired.promotionType.code == 'product_claim'}">
				     				<c:choose>
							            <c:when test="${ empty expired.parentPromotion.id }" >
							              <cms:contentText code="promotion.list" key="PARENT"/> -
							              <c:out value="${expired.childrenCount}"/>
							              <c:choose>
							                <c:when test="${expired.childrenCount != 1}" >
							                  <cms:contentText code="promotion.list" key="CHILDREN"/>
							                </c:when>
							                <c:otherwise>
							                  <cms:contentText code="promotion.list" key="CHILD"/>
							                </c:otherwise>
							              </c:choose>
							            </c:when>
							            <c:otherwise>
							              <cms:contentText code="promotion.list" key="CHILD_OF"/> <c:out value="${expired.parentPromotion.name}"/>
							            </c:otherwise>
						          </c:choose>
				
				     			</c:when>
				     			<c:otherwise>
				     				<cms:contentText code="promotion.list" key="FAMILY_NA"/>     			
				     			</c:otherwise>
				     		</c:choose>
				 
				        </display:column>
				        <display:column titleKey="promotion.list.PROMO_TYPE" headerClass="crud-table-header-row" class="crud-content left-align nowrap" sortable="true">
				          <c:out value="${expired.promotionType.name}"/>
				        </display:column>				        
				        <display:column titleKey="promotion.list.PROMO_FORM" headerClass="crud-table-header-row" class="crud-content left-align top-align" sortable="true" sortProperty="claimForm.name">
                	<c:choose>				           
				         	  <c:when test="${expired.promotionType.code == 'survey' or expired.promotionType.code == 'goalquest' or expired.promotionType.code == 'challengepoint' or expired.promotionType.code == 'diy_quiz'}">
				                <cms:contentText code="promotion.list" key="FAMILY_NA"/>              
				             </c:when>
				             <c:otherwise>
				               <c:out value="${expired.claimForm.name}"/>
				             </c:otherwise>
				           </c:choose>
				        </display:column>				        
				        <display:column titleKey="system.general.CRUD_REMOVE_LABEL" headerClass="crud-table-header-row" class="crud-content center-align nowrap">
				          <input type="checkbox" name="deleteExpiredPromos" value="<c:out value="${expired.id}"/>">
				        </display:column>
				      </display:table>		            		            
		            </td>
		  		  </tr>
		  		  
		  		   <tr class="form-buttonrow">
					<td align="right">
					  <html:submit styleClass="content-buttonstyle" onclick="setDispatch('deleteExpiredPromo')">
					    <cms:contentText key="REMOVE_SELECTED" code="system.button"/>
					  </html:submit>
					</td>
				  </tr>
				  <tr class="form-buttonrow">
					<td colspan="2" align="center">
					  <html:button property="back" styleClass="content-buttonstyle" onclick="callUrl('promotionListDisplay.do')" > 
				        <cms:contentText key="BACK_TO_PROMO" code="promotion.list"/>
				      </html:button>
					</td>
				  </tr>
			</table>		
	 </td>
  </tr>
</table>
</html:form>
