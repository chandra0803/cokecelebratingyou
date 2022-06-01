<%@ include file="/include/taglib.jspf"%>
<%@ page import="java.time.LocalDateTime"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<% pageContext.setAttribute("year",LocalDateTime.now().getYear()); %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Admin</title>
</head>
<body leftmargin="0" topmargin="0" marginheight="0" marginwidth="0"
	class="login-body">

	<table border="0" cellpadding="0" cellspacing="0" width="900"
		class="login-container-table">

		<tr>
			<td>
				<table border="0" cellpadding="0" cellspacing="0" width="100%"
					class="login-logo-area">
					<tr>
						<td></td>
					</tr>
				</table>
				<table width="100%" class="navrowbottom">
					<tr>
						<td></td>

					</tr>
				</table> <br>
				<table border="0" cellpadding="0" cellspacing="0" width="100%">
					<tr>
						<td valign="top" class="login-form-table"><beacon:authorize
								ifAnyGranted="BI_ADMIN">
								<table border="0" cellpadding="10" cellspacing="0" width="100%">
									<tr>
										<td>
											<%--INSTRUCTIONS--%> <br />
										<br />
											<p>
												<c:out value="${stat}" />
											</p>
										</td>
									</tr>
								</table>
							</beacon:authorize> <%--  end main content --%></td>

					</tr>
				</table>

			</td>
		</tr>
	</table>

	<%--footer--%>

	<table border="0" cellpadding="0" cellspacing="0" width="90%">
		<tr>
			<td class="footerlinks" align="center"></td>
		</tr>
		<tr class="footerlinks" align="center">
			<td>&copy&nbsp;<cms:contentTemplateText key="COPYRIGHT_TEXT" code="system.general" args="${year}" delimiter=","/>&nbsp;&reg;
			</td>

		</tr>
	</table>

	<%--end footer--%>
</body>
</html>
