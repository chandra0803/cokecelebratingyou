<%--UI REFACTORED--%>
<%@ include file="/include/taglib.jspf"%>
<table border="0" cellpadding="10" cellspacing="0" width="100%">
  <tr>
    <td colspan="2">
      <span class="headline"><cms:contentText key="AWARD_CONFIRM_TITLE" code="promotion.sweepstakes"/></span>
      <span id="quicklink-add"></span><script type="text/javascript">quicklink_display_add('<cms:contentText key="TITLE" code="promotion.sweepstakes"/>','<cms:contentText code="system.general" key="ADD_PAGE_TO_QUICKLINKS"/>'); </script>	  
      <%-- Subheadline --%>
      <br/>
      <span class="subheadline"><c:out value="${promotionName}"/></span>
      <%-- End Subheadline --%>
		</td>
	</tr>
	<%--MESSAGE--%>
	<tr>
		<td width="15">&nbsp;</td>
		<td>
      <cms:contentText key="AWARD_CONFIRM_MESSAGE" code="promotion.sweepstakes"/>
 		</td>
	</tr>
  <%--END MESSAGE--%>
	<tr class="form-buttonrow">
		<td colspan="2" align="center">
			<html:button property="back" styleClass="content-buttonstyle" onclick="callUrl('promotionSweepstakesListDisplay.do?method=display')">
				<cms:contentText key="BACK_TO_LIST" code="promotion.sweepstakes"/>
			</html:button>	
		</td>
	</tr>
</table>