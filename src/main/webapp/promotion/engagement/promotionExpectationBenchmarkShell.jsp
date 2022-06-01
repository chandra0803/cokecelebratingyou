<%@ include file="/include/taglib.jspf"%>

<c:set var="displayFlag" value="${promotionStatus == 'expired' }" />

<c:choose>
	<c:when test="${promotionExpectationBenchmarkForm.eScoreActive eq true}">
	  <script type="text/javascript">
		//Begin inline javascript  (because window.onload conflicts with menu.js call to window.onload)
			showLayer('audiencebenchmark');
	  </script>
	</c:when>
	<c:otherwise>
	  <script type="text/javascript">
		//Begin inline javascript  (because window.onload conflicts with menu.js call to window.onload)
			hideLayer('audiencebenchmark');
	  </script>
	</c:otherwise>
</c:choose>

<html:form styleId="contentForm" action="promotionExpectationBenchmarkSave" onsubmit="submitBenchmarkAudiences()">
  <html:hidden property="method" />
  <html:hidden property="promotionName" />
  <html:hidden property="promotionTypeName" />
  <html:hidden property="promotionTypeCode" styleId="promotionTypeCode" />
  
  <beacon:client-state>
		<beacon:client-state-entry name="promotionId" value="${promotionExpectationBenchmarkForm.promotionId}"/>		
  </beacon:client-state>  

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td colspan="3"><c:set var="promoTypeCode" scope="request" value="${promotionExpectationBenchmarkForm.promotionTypeCode}" />
        <c:set var="promoTypeName" scope="request" value="${promotionExpectationBenchmarkForm.promotionTypeName}" />
        <c:set var="promoName" scope="request" value="${promotionExpectationBenchmarkForm.promotionName}" />
        <tiles:insert attribute="promotion.header" />
      </td>
    </tr>
    <cms:errors />
  </table>

  <table border="0" cellpadding="0" cellspacing="0">

    <tiles:insert attribute="expectationBenchmarkTop"/>

    <tr>
      <td colspan="3" align="center">
        <tiles:insert attribute="promotion.footer" />
      </td>
    </tr>
    
  </table>
  
</html:form>

<tiles:insert attribute="expectationBenchmarkJS"/>
