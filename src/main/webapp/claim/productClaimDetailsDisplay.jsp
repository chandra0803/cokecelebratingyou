<%--UI REFACTORED--%>
<%@ page import="java.util.*" %>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils" %>
<%@ page import="com.biperf.core.domain.claim.Claim" %>
<%@ include file="/include/taglib.jspf"%>

<script type="text/javascript">
  function backToReport()
  {
    window.location = "<%= RequestUtils.getBaseURI(request) %>/reports/displayReport.do?method=showReport";
  }
</script>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td>
        <span class="headline"><cms:contentText key="DETAIL_HEADER" code="claims.details"/></span>
        <%-- Subheadline --%>
        <br/>
        <c:if test="${param.callingScreen == 'transactions'}">
         		<span class="subheadline">
	        		<c:out value="${claimDetails.submitter.titleType.name}" />
			  		<c:out value="${claimDetails.submitter.firstName}" />
					<c:out value="${claimDetails.submitter.middleName}" />
					<c:out value="${claimDetails.submitter.lastName}" />
					<c:out value="${claimDetails.submitter.suffixType.name}" />	
	    		</span>
	    	<br/>
	    </c:if>
        <span class="subheadline">
        	<c:out value="${claimDetails.promotion.name}"/>
        </span>
        <%-- End Subheadline --%>
        
        <%--INSTRUCTIONS--%>
        <br/><br/>
        <span class="content-instruction">
          <cms:contentText key="DETAILS_INFO" code="claims.details"/>
        </span>
        <br/>
        <%--END INSTRUCTIONS--%>
     	
        <cms:errors/>
		<%@ include file="/claim/productClaimDetails.jspf"%>
	  
		<table>
		  <tr>
		    <td>
          <%-- Add the print button first, then the correct back button --%>
					<%  Map parameterMap = new HashMap();
							Map clientStateMap = ClientStateUtils.getClientStateMap( request );
							Claim claim = (Claim)request.getAttribute("claimDetails");
							parameterMap.put( "claimId", claim.getId() );
							parameterMap.put( "userId", ClientStateUtils.getParameterValue( request, clientStateMap, "userId" ) );
							pageContext.setAttribute("printViewUrl", ClientStateUtils.generateEncodedLink( "", "claimDetailsPrinterFriendlyDisplay.do", parameterMap ) );
					%>
          <html:button styleClass="content-buttonstyle" property="print" onclick="javascript:popUpWin('${printViewUrl}', 'console', 750, 500, false, true);" >
            <cms:contentText code="system.button" key="PRINT"/>
          </html:button>
        </td>
        <td>
				<%  Map paramMap = new HashMap();
					Map clientStateMap2 = ClientStateUtils.getClientStateMap( request );
					Claim claimDetail = (Claim)request.getAttribute("claimDetails");
					paramMap.put( "userId", claimDetail.getSubmitter().getId());
					paramMap.put( "promotionId", ClientStateUtils.getParameterValue( request, clientStateMap2, "promotionId" ) );
					paramMap.put( "startDate", ClientStateUtils.getParameterValue( request, clientStateMap2, "startDate" ) );
					paramMap.put( "endDate", ClientStateUtils.getParameterValue( request, clientStateMap2, "endDate" ) );
					paramMap.put( "open", ClientStateUtils.getParameterValue( request, clientStateMap2, "open" ) );
					paramMap.put( "mode", ClientStateUtils.getParameterValue( request, clientStateMap2, "mode" ) );
					pageContext.setAttribute("returnToHistoryTransactionsUrl", ClientStateUtils.generateEncodedLink( "", "transactionHistory.do?method=showActivity&open="+ClientStateUtils.getParameterValue( request, clientStateMap, "open" )+"&promotionType=product_claim", paramMap ) );
				%>
        
			    <html:form styleId="contentForm" action="transactionHistory">
					<html:submit styleClass="content-buttonstyle" onclick="setActionAndDispatch('${returnToHistoryTransactionsUrl}','showActivity')">
		 				<cms:contentText key="TRANSACTION_LIST_BACK" code="claims.details"/>
		 		 	</html:submit>     
		 	   	</html:form>	
				</td>                                  
			</tr>     
		</table>
		</td>
	</tr>        
</table>