<%@ include file="/include/taglib.jspf"%>

<html:form styleId="contentForm" action="promotionApprovalSave">
  <html:hidden property="method" />
  <html:hidden property="promotionName" />
  <html:hidden property="promotionTypeName" />
  <html:hidden property="promotionTypeCode" styleId="promotionTypeCode" />
  <html:hidden property="approvalLvl1AudienceCount"/>
  <html:hidden property="approvalLvl2AudienceCount"/>
  
  <beacon:client-state>
		<beacon:client-state-entry name="promotionId" value="${promotionSSIApprovalForm.promotionId}"/>		
  </beacon:client-state>  

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
      <td colspan="3"><c:set var="promoTypeCode" scope="request" value="${promotionSSIApprovalForm.promotionTypeCode}" />
        <c:set var="promoTypeName" scope="request" value="${promotionSSIApprovalForm.promotionTypeName}" />
        <c:set var="promoName" scope="request" value="${promotionSSIApprovalForm.promotionName}" />
        <tiles:insert attribute="promotion.header" />
      </td>
    </tr>
    <cms:errors />
  </table>

  <table border="0" cellpadding="0" cellspacing="0">

    <tiles:insert attribute="promotionSSIApprovalAudiences"/>

	<tr>
		<td colspan="3" align="center">
			<tiles:insert attribute="promotion.footer" />
		</td>
	</tr>
  </table>	
</html:form>


<tiles:insert attribute="promotionSSIApprovalJS"/>

<c:choose>
	<c:when test="${promotionSSIApprovalForm.requireContestApproval eq true}">
	  <script type="text/javascript">
		//Begin inline javascript  (because window.onload conflicts with menu.js call to window.onload)
			showLayer('contestApprovalLevelsSection');
			showLayer('daysToApproveOnSubmissionSection');
	  </script>	  
	  <c:choose>
		   <c:when test="${promotionSSIApprovalForm.contestApprovalLevels == 1}">
		     <script type="text/javascript">
				hideLayer('teamlvl2audiencelist');
			 </script>
			</c:when>
			 <c:when test="${promotionSSIApprovalForm.contestApprovalLevels == 2}">
			  <script type="text/javascript">
				 //hideLayer('teamlvl1audiencelist');
			 	 //hideLayer('teamlvl2audiencelist');
			  </script>
			 </c:when>
			<c:otherwise>
			</c:otherwise>
		</c:choose>	
	</c:when>
			
	<c:otherwise>
	  <script type="text/javascript">
		//Begin inline javascript  (because window.onload conflicts with menu.js call to window.onload)
			hideLayer('contestApprovalLevelsSection');
			hideLayer('daysToApproveOnSubmissionSection');
			hideLayer('teamlvl1audiencelist');
			hideLayer('teamlvl2audiencelist');
	  </script>
	</c:otherwise>
</c:choose>

  <script type="text/javascript">
	function hideOrShowAudience(selectedValue) {
	if(selectedValue == 1)
	{
		showLayer('teamlvl1audiencelist');
		hideLayer('teamlvl2audiencelist');
	}
	else if (selectedValue == 2)
	{
		showLayer('teamlvl1audiencelist');
		showLayer('teamlvl2audiencelist');
	}
	else
	{
		hideLayer('teamlvl1audiencelist');
		hideLayer('teamlvl2audiencelist');
	}
	}

	function enableContestApproval() {
		showLayer('contestApprovalLevelsSection');
		showLayer('daysToApproveOnSubmissionSection');
	}
	
	function disableContestApproval() {
		hideLayer('contestApprovalLevelsSection');
		hideLayer('daysToApproveOnSubmissionSection');
		hideLayer('teamlvl1audiencelist');
		hideLayer('teamlvl2audiencelist');
		$("select[name='contestApprovalLevels'] option[value='']").attr('selected', 'selected');
	}
	
	function populateAudience() {
		showLayer('contestApprovalLevelsSection');
		showLayer('daysToApproveOnSubmissionSection');
	}
  </script>
