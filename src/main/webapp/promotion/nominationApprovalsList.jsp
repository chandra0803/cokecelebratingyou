<%--UI REFACTORED --%>
<%@page import="com.biperf.core.utils.UserManager"%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.value.nomination.NominationAdminApprovalsBean"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>

    <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="NOMINATION_HEADER" code="promotion.list"/></span>
        <span id="quicklink-add"></span><script type="text/javascript">quicklink_display_add('<cms:contentText key="NOMINATION_HEADER" code="promotion.list"/>','<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>'); </script>
        <br/><br/>

        <span class="content-instruction">
          <cms:contentText key="NOMINATION_INSTRUCTIONS" code="promotion.list"/>
        </span>
        <br/><br/>

        <cms:errors/>
        
        <table width="50%">
          <tr>
            <td align="right">
            
            <%	Map parameterMap = new HashMap();
            NominationAdminApprovalsBean temp;
									%>
            <table>
            
            <%-- LIVE PROMOTIONS ONLY FOR NOMINATIONS APPROVALS LIST --%>
            <tr class="form-row-spacer">
              <td colspan="2" class="subheadline" align="left"><cms:contentText code="promotion.list" key="LIVE_LABEL"/></td>
            </tr>
            
            <tr class="form-row-spacer">
                <td colspan="2">
									
                  <display:table name="liveSet" id="live" sort="list" requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
                  <display:setProperty name="basic.msg.empty_list_row">
					       			<tr class="crud-content" align="left"><td colspan="{0}">
                          			<cms:contentText key="NOTHING_FOUND" code="system.errors"/>
                       				 </td></tr>
				   				</display:setProperty>
				   				<display:setProperty name="basic.empty.showtable" value="true"></display:setProperty>
										<%	temp = (NominationAdminApprovalsBean)pageContext.getAttribute( "live" );
										    if (temp != null) {
												parameterMap.put( "promotionId", temp.getId() );
												parameterMap.put( "approverUserId", UserManager.getUserId() );
												parameterMap.put( "levelNumber", 1 );
												pageContext.setAttribute("viewLiveUrl", ClientStateUtils.generateEncodedLink( "", RequestUtils.getBaseURI(request) + "/claim/nominationsApprovalPage.do?method=display", parameterMap ) );
										    }
										%>
           <display:column titleKey="promotion.list.PROMO_NAME" headerClass="crud-table-header-row" class="crud-content left-align top-align nowrap" sortable="true" sortProperty="promotionName">
             <a href="<c:out value="${viewLiveUrl}"/>" class="crud-content-link"><c:out value="${live.promotionName}"/></a>
           </display:column>
           <display:column titleKey="promotion.list.LIVE_DATE" headerClass="crud-table-header-row" class="crud-content left-align top-align nowrap" sortable="true">
             <c:out value="${live.liveDate}"/>
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
 