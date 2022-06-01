<%--UI REFACTORED--%>
<%@page import="com.biperf.core.utils.PageConstants"%>
<%@ include file="/include/taglib.jspf"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>

<script type="text/javascript">
function submitAction(url) 
{
	document.forms[0].action=url;
	document.forms[0].submit();
	document.getElementById("viewCodes").removeAttribute('disabled');
}
function viewAction(url) 
{
	document.forms[0].action=url;
	document.forms[0].submit();
}
</script>

	<table border="0" cellpadding="10" cellspacing="0" width="100%">
		<tr>
			<td>
				<span class="headline">
					<cms:contentText key="SELF_ENROLLMENT" code="home.navmenu.user.system" />
				</span> <br />
				<span class="content-instruction"></span> <br /><br /> 
				<cms:errors />
				<%
					pageContext.setAttribute("generateCodes",RequestUtils.getBaseURI(request)+PageConstants.SELF_ENROLLMENT_GENERATE_CODES);
					pageContext.setAttribute("viewCodes",RequestUtils.getBaseURI(request)+PageConstants.SELF_ENROLLMENT_VIEW_CODES);
				%>
				<table>
					<tr class="form-buttonrow">
						<td></td>
						<td></td>
						<td align="left">
							<c:choose>
								<c:when test="${nodesToGenerateCodes}">
									<button id="generateCodes" onclick="submitAction('${generateCodes}')"><cms:contentText key="GENERATE_CODES" code="admin.self.enrollment" /></button>
								</c:when>
								<c:otherwise>
									<button id="generateCodes" disabled="disabled" onclick="submitAction('${generateCodes}')"><cms:contentText key="GENERATE_CODES" code="admin.self.enrollment" /></button>
								</c:otherwise>
							</c:choose>
							<c:choose>
								<c:when test="${nodesToViewCodes}">
										<button id="viewCodes" onclick="viewAction('${viewCodes}')"><cms:contentText key="VIEW_CODES" code="admin.self.enrollment" /></button>
								</c:when>
								<c:otherwise>
										<button id="viewCodes" disabled="disabled" onclick="viewAction('${viewCodes}')"><cms:contentText key="VIEW_CODES" code="admin.self.enrollment" /></button>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
