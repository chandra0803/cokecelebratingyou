<%@ include file="/include/taglib.jspf"%>

<html:form styleId="contentForm" action="promotionActivitySubmissionSave">
  <html:hidden property="method" />
  <html:hidden property="promotionName" />
  <html:hidden property="promotionTypeName" />
  <html:hidden property="promotionTypeCode" styleId="promotionTypeCode" />
  
  <beacon:client-state>
		<beacon:client-state-entry name="promotionId" value="${promotionSSIActivitySubmissionForm.promotionId}"/>		
  </beacon:client-state>  

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td colspan="3"><c:set var="promoTypeCode" scope="request" value="${promotionSSIActivitySubmissionForm.promotionTypeCode}" />
        <c:set var="promoTypeName" scope="request" value="${promotionSSIActivitySubmissionForm.promotionTypeName}" />
        <c:set var="promoName" scope="request" value="${promotionSSIActivitySubmissionForm.promotionName}" />
        <tiles:insert attribute="promotion.header" />
      </td>
    </tr>
    <cms:errors />
  </table>

  <table border="0" cellpadding="0" cellspacing="0">
    <tiles:insert attribute="promotionSSIActivitySubmissionMiddle"/>
	<tr>
		<td colspan="3" align="center">
			<tiles:insert attribute="promotion.footer" />
		</td>
	</tr>
  </table>	
</html:form>

<tiles:insert attribute="promotionSSIActivitySubmissionJS"/>

<c:choose>
	<c:when test="${promotionSSIActivitySubmissionForm.allowClaimSubmission}">
		<script type="text/javascript">
					showLayer('claimFormSection');
					showLayer('daysToApproveClaimSection');
		</script>
	</c:when>
	<c:otherwise>
	  <script type="text/javascript">
		hideLayer('claimFormSection');
		hideLayer('daysToApproveClaimSection');
	  </script>
	</c:otherwise>
</c:choose>
