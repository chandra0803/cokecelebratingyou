<%--UI REFACTORED--%>
<%@ include file="/include/taglib.jspf"%>

<script type="text/javascript">
<!--
function callUrl( urlToCall )
{
	window.location=urlToCall;
}

//-->
</script>

<html:form styleId="contentForm" action="stackRankPendingListSave">
  <html:hidden property="method" value=""/>

  <table border="0" cellpadding="10" cellspacing="0" width="100%">
	<tr>
	  <td>
		<span class="headline"><cms:contentText key="CONFIRM_TITLE" code="promotion.stack.rank.pending.list"/></span>
		<br/>
	    <span class="content-bold"><c:out value="${stackRankPendingListForm.promotionName}"/></span>
		<%--Confirmation Message--%>
		<br/><br/>
     	<span class="content-instruction">
     	<c:choose>
     	  <c:when test="${stackRankPendingListForm.approved == 'true'}">
			<cms:contentText key="CONFIRM_MESSAGE_W_APPROVAL" code="promotion.stack.rank.pending.list"/>
	      </c:when>
	      <c:otherwise>
	        <cms:contentText key="CONFIRM_MESSAGE_WO_APPROVAL" code="promotion.stack.rank.pending.list"/>
	      </c:otherwise>
	    </c:choose>
     	</span>
     	<br/><br/>
     	<%--END INSTRUCTIONS--%>
      </td>
    </tr>
    <%--BUTTON ROWS --%>
    <tr class="form-buttonrow">
      <td align="center">
        <c:url var="viewUrl" value="stackRankListDisplay.do"/>
          <html:button property="cancel" styleClass="content-buttonstyle" onclick="callUrl('${viewUrl}')">
			<cms:contentText code="promotion.stack.rank.pending.list" key="BACK_TO_LIST_BUTTON" />
		  </html:button>		
	  </td>
	</tr>
</table>
</html:form>