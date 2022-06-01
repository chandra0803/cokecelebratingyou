<%--UI REFACTORED--%>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="java.util.*"%>
<%@ page import="com.biperf.core.domain.promotion.ThrowdownPromotion"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>

<table border="0" cellpadding="10" cellspacing="0" width="100%">
	<tr>
		<td><span class="headline"><cms:contentText key="TITLE"
					code="promotion.throwdown.detail.extract" />
		</span> <br /> <span class="content-instruction"> <b><c:out
						value="${promotion.name}" />
			</b> </span> <%--INSTRUCTIONS--%> <br />
		<br /> <span class="content-instruction"> <cms:contentText
					key="CONFIRMATION_MESSAGE"
					code="promotion.throwdown.detail.extract" /> </span> <br />
		<br /> <%--END INSTRUCTIONS--%> <cms:errors /></td>
	</tr>
	<%  Map parameterMap = new HashMap();
						ThrowdownPromotion temp = (ThrowdownPromotion)request.getAttribute("promotion");
						Integer roundNumber =(Integer)request.getAttribute("roundNumber");
						parameterMap.put( "id", temp.getId() );
						parameterMap.put("roundNumber",String.valueOf(roundNumber));
						pageContext.setAttribute("viewUrl", ClientStateUtils.generateEncodedLink( "", "throwdownAwardSummary.do?method=display", parameterMap ) );
					%>
	<a href="<c:out value="${viewUrl}"/>" class="crud-content-link">

		<tr align="center">
			<td><html:submit styleClass="content-buttonstyle"
					onclick="callUrl('${viewUrl}')">
					<cms:contentText key="BACK_TO_SUMMARY"
						code="promotion.throwdown.detail.extract" />
				</html:submit></td>
		</tr>
</table>


