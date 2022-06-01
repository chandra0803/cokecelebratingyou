<%@ include file="/include/taglib.jspf"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Survey Test</title>
</head>
<body leftmargin="0" topmargin="0" marginheight="0" marginwidth="0"
	class="login-body">

	<table border="0" cellpadding="0" cellspacing="0" width="100%">
		<tr>
			<td valign="top" class="login-form-table">
			  <table border="0" cellpadding="10" cellspacing="0" width="100%">
				<tr>
					<td>
						<%--INSTRUCTIONS--%> <br /> <br />
						<p>
							<c:out value="${login}" /><br />
							<c:out value="${org}" /><br />
							<c:out value="${company}" /><br />
						</p>
					</td>
				</tr>
			  </table>
			</td>
		</tr>
	</table>

	<%--footer--%>

	<%--end footer--%>
</body>
</html>
