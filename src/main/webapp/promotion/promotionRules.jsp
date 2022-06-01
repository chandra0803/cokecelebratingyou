<%--UI REFACTORED--%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.ui.promotion.PromotionRulesForm"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ include file="/include/taglib.jspf"%>
<%  
Long userId = (Long)request.getAttribute("userId");
 
%>
<script type="text/javascript">
function printable() 
{
    document.promotionRulesForm.method.value='printerFriendly';
    document.promotionRulesForm.target='_blank';
    document.promotionRulesForm.submit();
    document.promotionRulesForm.target='';    
}
</script>

<html:form styleId="contentForm" action="promotionRules">
  <html:hidden property="method"/>
	<beacon:client-state>
		<beacon:client-state-entry name="promotionId" value="${promotionRulesForm.promotionId}"/>
	</beacon:client-state>

<br>
<span class="headline"><c:out value="${promotionRulesForm.promotionName}" />&nbsp;<cms:contentText key="TITLE" code="promotion.promotionrules"/></span>
<c:if test="${promotionRulesForm.method == 'display'}">
  <span id="quicklink-add"></span><script type="text/javascript">quicklink_display_add("<cms:contentText key='TITLE' code='promotion.promotionrules'/> - <c:out value='${promotionRulesForm.promotionName}' />","<cms:contentText code='system.general' key='ADD_PAGE_TO_QUICKLINKS'/>"); </script>
  &nbsp;&nbsp;&nbsp;&nbsp;
	<%	Map parameterMap = new HashMap();
			PromotionRulesForm temp = (PromotionRulesForm)request.getAttribute( "promotionRulesForm" );
			parameterMap.put( "promotionId", temp.getPromotionId() );
			parameterMap.put( "userId", userId );
			pageContext.setAttribute("printUrl", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), "/promotion/promotionRules.do?method=printerFriendly", parameterMap, true ) );
			pageContext.setAttribute("selectLevelURL", ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI(request), "/challengepoint/selectYourChallengePointDisplay.do", parameterMap ) );
	%>
  <a href="javascript:popUpWin('<c:out value="${printUrl}"/>', 'console', 750, 500, true, true);" class="crud-content-link">
    <cms:contentText key="PRINT_PAGE" code="system.link"/>
  </a>
</c:if>
<c:set var="backURL" ><%=request.getContextPath()%>/homePage.do#promo<c:out value="${promotionRulesForm.promotionId}"/></c:set>
<br/>
<br/>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr>
		<td colspan="2" class="crud-content"><cms:errors/></td>
	</tr>
	<tr>
	  <td colspan="2">
        <table>
          <tr>
            <td>
              <cms:contentText code="${promotionRulesForm.cmsCode}" key="${promotionRulesForm.cmsKey}"/>
            </td>
          </tr><%--Fix 21562--%>
          <tr class="form-buttonrow">	            
	  		    	<td align="center">
	  		    	
	  		    	  <c:choose>
                       <c:when test="${promotionRulesForm.pageName == 'selectChallengepoint'}">
                         <input type="button" class="content-buttonstyle" onclick="callUrl('<c:out value="${selectLevelURL}"/>')"
					             value="<cms:contentText code="system.button" key="BACK"/>">
                         </c:when>
                      <c:otherwise>
                       <input type="button" class="content-buttonstyle" onclick="callUrl('<c:out value="${backURL}"/>')"
					             value="<cms:contentText code="system.button" key="BACK"/>">
                        </c:otherwise>
                        </c:choose>
	  			  							
			        </td>
	  		  </tr>
			   <c:if test="${promotionRulesForm.method == 'printerFriendly'}">

            <tr class="form-blank-row">
              <td></td>
            </tr>
		    <tr class="form-buttonrow">
              <td align="center">
                <html:button property="closeBtn" styleClass="content-buttonstyle" onclick="javascript:window.print()">
                  <cms:contentText code="system.button" key="PRINT" />
                </html:button>
				&nbsp;&nbsp;&nbsp;
                <html:button property="closeBtn" styleClass="content-buttonstyle" onclick="javascript:window.close()">
                  <cms:contentText code="system.button" key="CLOSE_WINDOW" />
                </html:button>
		      </td>
		    </tr>			
		  </c:if>
		</table>
      </td>
	</tr>

</table>

</html:form>      