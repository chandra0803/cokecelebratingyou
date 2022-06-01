<%--UI REFACTORED--%>
<%@ page import="java.util.*"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>
<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@page import="com.biperf.core.utils.PageConstants"%>
<%@ include file="/include/taglib.jspf"%>

<script text="javascript">
	function submitAction(url) 
	{
		document.forms[0].action=url;
		document.forms[0].submit();
	}
</script>

<table border="0" cellpadding="10" cellspacing="0" width="100%">
	<tr>
		<td><span class="headline"><cms:contentText key="ENROLLMENT_CODES" code="admin.self.enrollment" /></span> <br /> <br />
			<span class="content-instruction">
		</span> <br /> <br /> <cms:errors />
			<%
				pageContext.setAttribute("back",RequestUtils.getBaseURI(request)+PageConstants.SELF_ENROLLMENT_DISPLAY);
			%>
			<table width="75%">
				<tr>
					<td align="right">
						<display:table name="${nodes}" id="node" pagesize="${itemsPerPage}" sort="page" 
							size="${count}" partialList="true" export="true"
							requestURI="<%=RequestUtils.getOriginalRequestURI(request)%>">
							<display:setProperty name="basic.msg.empty_list_row">
								<tr class="crud-content" align="left">
									<td colspan="{0}"><cms:contentText key="NOTHING_FOUND"
											code="system.errors" /></td>
								</tr>
							</display:setProperty>
							<display:setProperty name="basic.empty.showtable" value="true" />
							<display:column titleKey="admin.self.enrollment.NODE_NAME"
								headerClass="crud-table-header-row"
								class="crud-content left-align top-align nowrap" sortable="true"
								sortProperty="name">
								<c:out value="${node.name}" />
							</display:column>
							<display:column titleKey="admin.self.enrollment.CODE" property="selfEnrollmentCode"
								headerClass="crud-table-header-row"
								class="crud-content left-align top-align nowrap" sortable="true"
								sortProperty="selfEnrollmentCode" />
						</display:table></td>
				</tr>
				<tr class="form-buttonrow">
					<td>
						<table width="100%">
							<tr>
								<td align="left"><button class="content-buttonstyle"
										onclick="submitAction('${back}')">
										<cms:contentText key="BACK" code="system.button" />
									</button></td>
							</tr>
						</table></td>
				</tr>
			</table></td>
	</tr>
</table>
