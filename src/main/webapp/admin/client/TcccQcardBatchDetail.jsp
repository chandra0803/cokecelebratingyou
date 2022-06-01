
<%@ page import="com.biperf.core.ui.client.TcccQcardBtachForm"%>
<%@ page import="java.util.*"%>




<%@ page import="com.biperf.core.ui.utils.RequestUtils"%>
<%@ page import="com.biperf.core.utils.ClientStateUtils"%>
<%@ include file="/include/taglib.jspf"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

	<html:form action="qcardBatchDisplay">
		<html:hidden property="method" />

		<table border="0" cellpadding="10" cellspacing="0" width="100%">
			<tr>
				<c:if test="${ mode  == 'add'}">
					<td><span class="headline"><cms:contentText
								key="ADD_BATCHLIST_HEADING" code="OnTheSpot.label" /></span> <br /> <br />
						<span class="content-instruction"> <cms:contentText
								key="ADD_BATCHLIST_INFO" code="OnTheSpot.label" />
					</span> <br /> <br />
				</c:if>
				<c:if test="${ mode  == 'edit'}">
					<td><span class="headline"><cms:contentText
								key="HEADING" code="OnTheSpot.label" /></span> <br /> <br /> <span
						class="content-instruction"> <cms:contentText key="INFO"
								code="OnTheSpot.label" />
					</span> <br /> <br />
				</c:if>
				<cms:errors />
				<table>
					<tr class="form-row-spacer">
						<beacon:label property="entityName" required="true">
							<cms:contentText key="BatchNumber" code="OnTheSpot.label" />
						</beacon:label>
						<td class="content-field"><c:choose>
								<c:when test="${ mode  == 'edit'}">
									<c:out value="${tcccQcardBtachForm.batchNumber}" />
								</c:when>
								<c:when test="${ mode  == 'add'}">
									<html:text property="batchNumber" size="60" maxlength="60"
										styleClass="content-field" />
								</c:when>
								<c:otherwise>
									<html:text property="batchNumber" size="60" maxlength="60"
										styleClass="content-field" />
								</c:otherwise>
							</c:choose></td>
					</tr>
					<tr class="form-row-spacer">
						<beacon:label property="entityName" required="true">
							<cms:contentText key="DivisionKey" code="OnTheSpot.label" />
						</beacon:label>
						<td class="content-field"><html:text property="divisionKey"
								size="60" maxlength="60" styleClass="content-field" /></td>
					</tr>
					<tr class="form-row-spacer">
						<beacon:label property="entityName" required="true">
							<cms:contentText key="WorkCountry" code="OnTheSpot.label" />
						</beacon:label>
						<td class="content-field"><html:text property="workCountry"
								size="60" maxlength="60" styleClass="content-field" /> <html:hidden
								property="method" /> <html:hidden property="qcardBatchId" /> <html:hidden
								property="version" /> <html:hidden property="batchNumber" /> <html:hidden
								property="mode" /></td>
					</tr>

					<table width="25%">
						<br>
						<br>

						<tr>

							<td></td>

							<td><html:submit styleClass="content-buttonstyle"
									onclick="setDispatch('save')">
									<cms:contentText key="SAVE" code="system.button" />
								</html:submit></td>
							<td><c:url var="homePageUrl"
									value="/admin/onTheSpotBatchList.do" /> <html:button
									property="homePageButton" styleClass="content-buttonstyle"
									onclick="callUrl('${homePageUrl}')">
									<cms:contentText key="CANCEL" code="system.button" />
								</html:button></td>

						</tr>

					</table>

				</table>
				<br>
		</table>
	</html:form>

</body>
</html>